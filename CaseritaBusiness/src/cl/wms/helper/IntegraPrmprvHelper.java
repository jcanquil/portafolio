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
import cl.caserita.dao.iface.PrmprvDAO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.PrmprvDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegraPrmprvHelper {
	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegraPrmprvHelper.class);

	public static void main (String[]args){
		IntegraPrmprvHelper helper = new IntegraPrmprvHelper();
		
		//Llamado para todos los proveedores
		//helper.procesaPrmprv();
		
		//Llamado por proveedor
		helper.procesaPorProveedor(84472400, "4", "A");
	}
	
	public void procesaPrmprv(){
		
		String tipoAccion="A";
		
		//Lista de Proveedores para enviar a WMS
		DAOFactory dao = DAOFactory.getInstance();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		EnviaFTPWMS ftp = new EnviaFTPWMS();
		PrmprvDAO prmprv = dao.getPrmprvDAO();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(tipoAccion);
		List lista = prmprv.recuperaProveedores();
		
		PrmprvDTO prmprvDTO = null;
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			prmprvDTO = (PrmprvDTO) iter.next();
			
			String xml = formaXML(prmprvDTO, integraDTO.getAccion().trim());
			//generatxt(xml,rutprov);
			
			//ftp.enviaFTP(generatxt(xml, prmprvDTO.getRutProv()));
			
			String xmlEnvio = generatxt(xml,  prmprvDTO.getRutProv());
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(prmprvDTO.getRutProv());
					loginDTO.setDv(prmprvDTO.getDvProv());
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("PRMPRV");
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
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("N");
					loginDTO.setEstadoEnvio(0);
					
					log.generaLogProveedor(loginDTO);
				}
				
			}else{
				generatxt2(xml,  prmprvDTO.getRutProv());
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(prmprvDTO.getRutProv());
					loginDTO.setDv(prmprvDTO.getDvProv());
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("PRMPRV");
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
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("E");
					loginDTO.setEstadoEnvio(1);
					
					log.generaLogProveedor(loginDTO);
				}
			}
			
			/*
			Iterator iter2 = PrmprvDTO.getCodigosBarras().iterator();
			while (iter2.hasNext()){
				ExdartDTO dto = (ExdartDTO) iter2.next();
				String xml = formaXML(PrmprvDTO, dto, tipoAccion);
				//Procesa Envio FTP
				//generatxt(xml);
				ftp.enviaFTP(generatxt(xml, PrmprvDTO.getCodigoArticulo()));
			}
			*/
		}
	}
	
	public void procesaPorProveedor(int rutProv, String dvProv, String tipoAccion){
		//Lista de Proveedor para enviar a WMS
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		PrmprvDAO prmprv = dao.getPrmprvDAO();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(tipoAccion);
		PrmprvDTO prmprvDTO = prmprv.obtieneProveedor(rutProv, dvProv);
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		if (prmprvDTO!=null){
			String xml = formaXML(prmprvDTO, integraDTO.getAccion().trim());
			//generatxt(xml,rutProv);
			try{
				Thread.sleep(1000);

			}catch (Exception e){
				e.printStackTrace();	}
			//ftp.enviaFTP(generatxt(xml, prmprvDTO.getRutProv()));
			
			String xmlEnvio = generatxt(xml,  prmprvDTO.getRutProv());
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				logi.info("entra1");
				if (integraDTO.getIpEquipo()!=null){
					logi.info("entra2");

					int fecha = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(prmprvDTO.getRutProv());
					loginDTO.setDv(prmprvDTO.getDvProv());
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("PRMPRV");
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
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("N");
					loginDTO.setEstadoEnvio(0);
					
					log.generaLogProveedor(loginDTO);
				}
				
			}else{
				generatxt2(xml,  prmprvDTO.getRutProv());
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(prmprvDTO.getRutProv());
					loginDTO.setDv(prmprvDTO.getDvProv());
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("PRMPRV");
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
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("E");
					loginDTO.setEstadoEnvio(1);
					
					log.generaLogProveedor(loginDTO);
				}
			}
		}
		
		
		
	}
	
	public String generatxt(String XML, int rutProv){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		
		int mesin = Integer.parseInt(mes);
		
		String mesPal = fch.recuperaMes(mesin);
		String rutaLog="/home/ftp/in/";
		
		String carpeta = rutaLog+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/";
		
		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		logi.info("Ruta:"+carpeta);
		String nombreArchivo = "proveedores_"+rutProv+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"proveedores_"+rutProv+"_"+fechaStr+".xml";
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
	
	public String generatxt2(String XML, int rutProv){
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
		String nombreArchivo = "proveedores_"+rutProv+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"proveedores_"+rutProv+"_"+fechaStr+".xml";
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
	
	public String formaXML(PrmprvDTO dto, String tipoAccion){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		
		xmlCas.append("<VC_SUPP_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
		xmlCas.append("<TRNNAM>SUPP_TRAN</TRNNAM>");
		xmlCas.append("<TRNVER>2009.2</TRNVER>");
		xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		
		xmlCas.append("<SUPP_SEG>");
		xmlCas.append("<SEGNAM>SUPPLIER</SEGNAM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<SUPNUM>"+dto.getRutProv()+dto.getDvProv()+"</SUPNUM>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		xmlCas.append("<HOST_EXT_ID>"+"ADR"+dto.getRutProv()+dto.getDvProv()+"</HOST_EXT_ID>"); //RUT DIRECCION PROVEEDOR
		xmlCas.append("<ADRNAM>"+"ADR"+dto.getRutProv()+dto.getDvProv()+"</ADRNAM>"); //DV DIRECCION PROVEEDOR
		xmlCas.append("<ADRTYP>CST</ADRTYP>");
		xmlCas.append("<ADRLN1>"+dto.getDvProv()+"</ADRLN1>");
		xmlCas.append("<ADRLN2></ADRLN2>");
		xmlCas.append("<ADRLN3></ADRLN3>"); //no se utilizara aun
		xmlCas.append("<ADRCTY>"+dto.getDescCiudadProv()+"</ADRCTY>"); //PRMCIU
		xmlCas.append("<ADRSTC>"+dto.getDescComunaProv()+"</ADRSTC>"); //PRMCOM
		xmlCas.append("<ADRPSZ></ADRPSZ>"); //no se utilizara aun
		xmlCas.append("<CTRY_NAME></CTRY_NAME>"); //PRMPAI
		xmlCas.append("<RGNCOD></RGNCOD>"); //no se utilizara aun
		xmlCas.append("<PHNNUM></PHNNUM>"); //no se utilizara aun
		xmlCas.append("<FAXNUM></FAXNUM>"); //no se utilizara aun
		xmlCas.append("<RSAFLG>0</RSAFLG>");
		xmlCas.append("<TEMP_FLG>0</TEMP_FLG>");
		
		xmlCas.append("<LAST_NAME>"+dto.getNombreEmpresa()+"</LAST_NAME>");
		xmlCas.append("<FIRST_NAME>"+dto.getRazonSocialProv()+"</FIRST_NAME>");
		xmlCas.append("<HONORIFIC></HONORIFIC>"); //no se utilizara aun
		xmlCas.append("<ADR_DISTRICT></ADR_DISTRICT>"); //no se utilizara aun
		xmlCas.append("<WEB_ADR></WEB_ADR>"); //no se utilizara aun
		xmlCas.append("<EMAIL_ADR></EMAIL_ADR>"); //no se utilizara aun
		xmlCas.append("<PAGNUM></PAGNUM>"); //no se utilizara aun
		xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		xmlCas.append("<PO_BOX_FLG></PO_BOX_FLG>");
		xmlCas.append("<TRK_CNSG_COD></TRK_CNSG_COD>"); //no se utilizara aun
		xmlCas.append("<ASSET_TYP>0</ASSET_TYP>");
		xmlCas.append("<RCVSTS></RCVSTS>"); //no se utilizara aun
		xmlCas.append("<LOT_FMT_ID></LOT_FMT_ID>"); //no se utilizara aun
		xmlCas.append("<SER_NUM_TYP_ID></SER_NUM_TYP_ID>"); //no se utilizara aun
		xmlCas.append("<TRUST_FLG>0</TRUST_FLG>");
		xmlCas.append("</SUPP_SEG>");

		xmlCas.append("</CTRL_SEG>");
		xmlCas.append("</VC_SUPP_INB_IFD>");

		resp = xmlCas.toString();  
		
		return resp;
	}
}
