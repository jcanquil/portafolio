package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.TpctraDAO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.TpctraDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionTpctraHelper {
	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegracionTpctraHelper.class);

	public static void main (String []agrs){
		IntegracionTpctraHelper helper = new IntegracionTpctraHelper();
		helper.integracionAll();
		//helper.integracion(11111111, "A");
	}
	
	public void integracionAll(){
		DAOFactory dao = DAOFactory.getInstance();
		TpctraDAO tpctra = dao.getTpctraDAO();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		List lista = tpctra.buscaAllTransportista();
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			TpctraDTO dto = (TpctraDTO) iter.next();
			
			String xml = formaXML(dto, "A");
			
			if (ftp.enviaFTP(generatxt(dto.getRutChofer(),xml), ftpDAO)){
				
			}else{
				generatxt2(dto.getRutChofer(),xml);
			}
		}
		
	}
	
	public void integracion(int rut, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		TpctraDAO tpctra = dao.getTpctraDAO();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		TpctraDTO dto = tpctra.buscaTransportista(rut);
		if (dto!=null){
			String xml = formaXML(dto, integraDTO.getAccion().trim());
			//ftp.enviaFTP(generatxt(dto.getRutChofer(),xml));
			
			String xmlEnvio = generatxt(dto.getRutChofer(),xml);
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(dto.getRutChofer());
					loginDTO.setDv(dto.getDvChofer().trim());
					loginDTO.setCod2(dto.getRutTransportista());
					loginDTO.setDv2(dto.getDvTransportista().trim());
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("TPCTRA");
					loginDTO.setTipoAccion(integraDTO.getAccion());

					StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
					int campo=0;
					String xmlLog="";
					 while (st.hasMoreTokens( )){
						 
						 	String tr = st.nextToken();
						 	logi.info(tr);
						 	if (campo==1){
						 		xmlLog= tr.trim();
						 	}
						 campo++;
				        
				    }
					loginDTO.setNombreArchivo(xmlLog.trim());
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("N");
					loginDTO.setEstadoEnvio(0);
					
					log.generaLogChoferes(loginDTO);
					}
				
			}else{
				generatxt2(dto.getRutChofer(),xml);
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(dto.getRutChofer());
					loginDTO.setDv(dto.getDvChofer().trim());
					loginDTO.setTipoAccion(integraDTO.getAccion());
					loginDTO.setCod2(dto.getRutTransportista());
					loginDTO.setDv2(dto.getDvTransportista().trim());
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("TPCTRA");
					StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
					int campo=0;
					String xmlLog="";
					 while (st.hasMoreTokens( )){
						 
						 	String tr = st.nextToken();
						 	logi.info(tr);
						 	if (campo==1){
						 		xmlLog= tr.trim();
						 	}
						 campo++;
				        
				    }
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("E");
					loginDTO.setEstadoEnvio(1);
					
					log.generaLogChoferes(loginDTO);
				}
			}
			
		}
		
			
			
		
	}
	
	public String generatxt(int rut, String XML){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//logi.info("Mes:"+mes);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);
		String rutaLog="/home/ftp/in/";
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutaLog+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/";

		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		logi.info("Ruta:"+carpeta);
		String nombreArchivo = "trasporte_"+rut+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"transporte_"+rut+"_"+fechaStr+".xml";
		File f=new File(archivoLog);
		if (f.exists()){
			logi.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterLog=new FileWriter(archivoLog,true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		logi.info("Archivo LOG:"+archivoLog);
		fileWriterLog.write( XML+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return nombreArchivo+"|"+archivoLog;
	}
	
	public String generatxt2(int rut, String XML){
		Fecha fch = new Fecha();
		//String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 14);
		//String fechaStr = String.valueOf(fch.getTimestamp()).substring(0,10) + "_" + String.valueOf(fch.getTimestamp()).substring(11,23);
		String fechaStr = String.valueOf(fch.getTimestamp()).substring(0,10) + "_" + String.valueOf(fch.getTimestamp()).substring(11);
		logi.info(fechaStr);
		
		fechaStr = fechaStr.replace("-", "");
		fechaStr = fechaStr.replace("/", "");
		fechaStr = fechaStr.replace(":", "");
		fechaStr = fechaStr.replace(".", "");
		
		logi.info(fechaStr);
		
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//log.info("Mes:"+mes);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);
		String rutaLog="/home/stgin/";
		
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutaLog;
		
		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		logi.info("Ruta:"+carpeta);
		String nombreArchivo = "trasporte_"+rut+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"transporte_"+rut+"_"+fechaStr+".xml";
		File f=new File(archivoLog);
		if (f.exists()){
			logi.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterLog=new FileWriter(archivoLog,true);
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		logi.info("Archivo LOG:"+archivoLog);
		fileWriterLog.write( XML+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return nombreArchivo+"|"+archivoLog;
	}
	public String formaXML(TpctraDTO dto, String tipoAccion){
		//Forma XML para enviar a WMS
				String resp ="";
				StringBuffer xmlCas = new StringBuffer();
				String xml="";
				
				xmlCas.append("<VC_CARR_INB_IFD>");
				xmlCas.append("<CTRL_SEG>");
				xmlCas.append("<TRNNAM>CARR_TRAN</TRNNAM>");
				xmlCas.append("<TRNVER>2009.2</TRNVER>");
				xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
				
				xmlCas.append("<CAR_ADDR_SEG>");
				xmlCas.append("<SEGNAM>CAR_ADDR_SEG</SEGNAM>");
				xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<HOST_EXT_ID>"+"ADR"+dto.getRutChofer()+"-"+dto.getDvChofer().trim()+"</HOST_EXT_ID>"); //RUT DIRECCION PROVEEDOR
				xmlCas.append("<ADRNAM>"+"ADR"+dto.getRutChofer()+"-"+dto.getDvChofer()+"</ADRNAM>"); //DV DIRECCION PROVEEDOR
				xmlCas.append("<ADRLN1>"+dto.getDireccion().trim()+"</ADRLN1>");
				xmlCas.append("<ADRLN2>"+dto.getDireccion().trim()+"</ADRLN2>");
				xmlCas.append("<ADRCTY>"+dto.getDescripcionCiudad()+"</ADRCTY>"); //PRMCIU
				xmlCas.append("<ADRSTC>"+dto.getDescripcionComuna()+"</ADRSTC>"); //PRMCOM

				xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>"); //PRMPAI
				xmlCas.append("</CAR_ADDR_SEG>");

				xmlCas.append("<CARR_HDR_SEG>");
					xmlCas.append("<SEGNAM>CARRIER HDR</SEGNAM>");
					xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");

					xmlCas.append("<CARCOD>"+dto.getRutChofer()+"-"+dto.getDvChofer().trim()+"</CARCOD>");
					xmlCas.append("<CARNAM>"+dto.getNombreEmpresa()+"</CARNAM>");
					xmlCas.append("<SRVLVL>1</SRVLVL>"); //PRMCOM
					xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
					xmlCas.append("<HOST_EXT_ID>"+"ADR"+dto.getRutChofer()+"-"+dto.getDvChofer().trim()+"</HOST_EXT_ID>"); //RUT DIRECCION PROVEEDOR
				xmlCas.append("</CARR_HDR_SEG>");
				xmlCas.append("</CTRL_SEG>");
				xmlCas.append("</VC_CARR_INB_IFD>");
				
				resp = xmlCas.toString();  
				
				return resp;
	}
}
