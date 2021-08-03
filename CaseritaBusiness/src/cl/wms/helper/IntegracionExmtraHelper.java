package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.TpctraDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.TpctraDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionExmtraHelper {

private static FileWriter fileWriterLog;
private  static Logger logi = Logger.getLogger(IntegracionExmtraHelper.class);

	public static void main (String []agrs){
		IntegracionExmtraHelper helper = new IntegracionExmtraHelper();
		//helper.integracionAll();
		//helper.integracion(11111111, "A");
	}
	
	
	
	public void integracion(int codigoEmpresa, int  numTraspaso, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		ExmtraDAO exmtra = dao.getExmtraDAO();
		ExdtraDAO exdtra = dao.getExdtraDAO();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		TptempDAO tptemdao = dao.getTptempDAO();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		ExmtraDTO dto = exmtra.recuperaEncabezado(codigoEmpresa, numTraspaso);
		TptempDTO dtoempre = tptemdao.recuperaEmpresa(codigoEmpresa);
		dto.setRutEmpresa(dtoempre.getRut());
		dto.setDvEmpresa(dtoempre.getDv());
		logi.info(dtoempre.getRut());
		logi.info(dtoempre.getDv());
		List lista = exdtra.recuperaDetalle(codigoEmpresa, numTraspaso);
		if (dto!=null){
			String xml = formaXML(dto, integraDTO.getAccion().trim(), lista);
			//ftp.enviaFTP(generatxt(dto.getNumTraspaso(),xml));
			
			String xmlEnvio = generatxt(dto.getNumTraspaso(),xml);
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setTipoAccion(integraDTO.getAccion());

					
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("EXMTRA");
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
					loginDTO.setTipoEnvio("N");
					loginDTO.setEstadoEnvio(0);
					
					log.generaLogTraspaso(loginDTO, dto);
					}
				
			}else{
				generatxt2(dto.getNumTraspaso(),xml);
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("EXMTRA");
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
					loginDTO.setTipoAccion(integraDTO.getAccion());

					log.generaLogTraspaso(loginDTO, dto);
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
		String nombreArchivo = "traspaso_"+rut+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"transpaso_"+rut+"_"+fechaStr+".xml";
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
		String nombreArchivo = "traspaso_"+rut+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"transpaso_"+rut+"_"+fechaStr+".xml";
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
	
	public String formaXML(ExmtraDTO dto, String tipoAccion, List lista){
		//Forma XML para enviar a WMS
				String resp ="";
				StringBuffer xmlCas = new StringBuffer();
				String xml="";
				
				xmlCas.append("<VC_RA_INB_IFD>");
				xmlCas.append("<CTRL_SEG>");
					xmlCas.append("<TRNNAM>RA_TRAN</TRNNAM>");
					xmlCas.append("<TRNVER>2010.2</TRNVER>");
					xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
					xmlCas.append("<HEADER_SEG>");
						xmlCas.append("<SEGNAM>HEADER_SEG</SEGNAM>");
						xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
						xmlCas.append("<INVNUM>"+dto.getNumTraspaso()+"</INVNUM>");
						xmlCas.append("<SUPNUM>"+dto.getRutEmpresa()+dto.getDvEmpresa()+"</SUPNUM>");
						xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
						xmlCas.append("<RIMSTS>OPEN</RIMSTS>");
						xmlCas.append("<INVTYP>OT</INVTYP>");
						xmlCas.append("<INVDTE>"+dto.getFechaTraspaso()+"</INVDTE>");
					
						Iterator iter = lista.iterator();
						while (iter.hasNext()){
							ExdtraDTO dtolista = (ExdtraDTO) iter.next();
					
							xmlCas.append("<LINE_SEG>");
								xmlCas.append("<SEGNAM>LINE_SEG</SEGNAM>");
								xmlCas.append("<INVLIN>"+dtolista.getLinea()+"</INVLIN>");
								xmlCas.append("<INVSLN>0000</INVSLN>");
								xmlCas.append("<EXPQTY>"+dtolista.getCantDespachada()+"</EXPQTY>");
								xmlCas.append("<PRTNUM>"+dtolista.getCodArticulo()+"</PRTNUM>");
								xmlCas.append("<ORGCOD>----</ORGCOD>");
								xmlCas.append("<REVLVL>----</REVLVL>");
								xmlCas.append("<LOTNUM>----</LOTNUM>");
								xmlCas.append("<RCVSTS>"+dto.getCodigoInventario().trim()+"</RCVSTS>");
							xmlCas.append("</LINE_SEG>");
						}
						
					xmlCas.append("</HEADER_SEG>");
				xmlCas.append("</CTRL_SEG>");
				xmlCas.append("</VC_RA_INB_IFD>");
				
				resp = xmlCas.toString();  
				
				return resp;
	}
	
}

