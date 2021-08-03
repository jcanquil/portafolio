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
import cl.caserita.dao.iface.TptcomDAO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.TptcomDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionTptcomHelper {
	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegracionTptbdgHelper.class);

	public static void main (String []args){
		
		IntegracionTptcomHelper hel = new IntegracionTptcomHelper();
		hel.procesaComuna(1, 21, "A,192.168.1.10,PCJAIME,JHCANQUIL");
	}
	public void procesaComuna(int region , int comuna , String accion ){
		DAOFactory dao = DAOFactory.getInstance();
		TptcomDAO tptcom = dao.getTptcomDAO();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		logi.info("accion:"+accion);
		IntegracionDTO integraDTO = convierte.convierte(accion);
		logi.info("ip:"+integraDTO.getIpEquipo());
		if (region>0){
			TptcomDTO tpt = tptcom.Comuna(region, comuna);
			String xml = formaXML(tpt, integraDTO.getAccion().trim());
			logi.info(tpt.getDescripcionComuna());
			logi.info(tpt.getCodigoRegion());
			logi.info("entra");
			//ftp.enviaFTP(generaTxt(String.valueOf(tpt.getCodigoRegion())+tpt.getDescripcionComuna().trim(), xml));
			
			String xmlEnvio = generaTxt(String.valueOf(tpt.getCodigoRegion())+tpt.getDescripcionComuna().trim(), xml);
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				logi.info("entra2");

				if (integraDTO.getIpEquipo()!=null){
					logi.info("entra3");

					int fecha = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(region);
					loginDTO.setCod2(comuna);
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("TPTCOM");
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
					
					log.generaLogTptcom(loginDTO);
				}
				
			}else{
				generatxt2(String.valueOf(tpt.getCodigoRegion())+tpt.getDescripcionComuna().trim(), xml);
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					loginDTO.setCod(region);
					loginDTO.setCod2(comuna);
					loginDTO.setFechaArchivo(fecha);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("TPTCOM");
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
					
					log.generaLogTptcom(loginDTO);
					}
			}
		}else{
			List comunaList = tptcom.allComuna();
			Iterator iter = comunaList.iterator();
			while (iter.hasNext()){
				TptcomDTO tpt = (TptcomDTO) iter.next();
				
				String xml = formaXML(tpt, accion);
				
				//ftp.enviaFTP(generaTxt(String.valueOf(tpt.getCodigoRegion())+tpt.getDescripcionComuna().trim(), xml));
				
				String xmlEnvio = generaTxt(String.valueOf(tpt.getCodigoRegion())+tpt.getDescripcionComuna().trim(), xml);
				if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getYYYYMMDD());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(region);
						loginDTO.setCod2(comuna);
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("TPTCOM");
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
						
						log.generaLogTptcom(loginDTO);
					}
					
				}else{
					generatxt2(String.valueOf(tpt.getCodigoRegion())+tpt.getDescripcionComuna().trim(), xml);
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getDDMMYYYY());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(region);
						loginDTO.setCod2(comuna);
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("TPTCOM");
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
						
						log.generaLogTptcom(loginDTO);
						}
				}
				
				
			}
		}
		
	}
	public String generaTxt(String codigoBodega, String XML){
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
		String nombreArchivo = "comunas"+codigoBodega+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"comunas"+codigoBodega+"_"+fechaStr+".xml";
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
	
	public String generatxt2(String codigoBodega, String XML){
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
		String nombreArchivo = "comunas"+codigoBodega+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"comunas"+codigoBodega+"_"+fechaStr+".xml";
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
	public String formaXML(TptcomDTO dto, String accion){
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>26</WHSE_ID>");
		  
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+accion.trim()+"</TRNTYP>");
		  xmlCas.append("<CSTNUM>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</CSTNUM>");
		  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		  xmlCas.append("<BCKFLG>0</BCKFLG>");
		  xmlCas.append("<PARFLG>0</PARFLG>");
		  xmlCas.append("<CARFLG>UN</CARFLG>");
		  xmlCas.append("<SPLFLG>C</SPLFLG>");
		  xmlCas.append("<STDFLG>B</STDFLG>");
		  xmlCas.append("<SHPLBL>R</SHPLBL>");
		  
		  xmlCas.append("<SHIPBY></SHIPBY>");
		  xmlCas.append("<DEPTNO></DEPTNO>");
		  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		  xmlCas.append("<ORDINV></ORDINV>");
		  xmlCas.append("<CSTTYP>COMU</CSTTYP>");
		  xmlCas.append("<INVSTS_PRG></INVSTS_PRG>");

		  xmlCas.append("<ROUTE_TO_ADDRESS></ROUTE_TO_ADDRESS>");
		  xmlCas.append("<ADDR_SEG>");
				  xmlCas.append("<SEGNAM>0</SEGNAM>");
				  xmlCas.append("<TRNTYP></TRNTYP>");
				  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				  
				  xmlCas.append("<HOST_EXT_ID>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</HOST_EXT_ID>");
				  xmlCas.append("<ADRNAM>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</ADRNAM>");
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
				  xmlCas.append("<ADRLN1>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</ADRLN1>");
				  xmlCas.append("<ADRCTY>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</ADRCTY>");
				  xmlCas.append("<ADRSTC>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</ADRSTC>");
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  xmlCas.append("<LAST_NAME>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</LAST_NAME>");
				  xmlCas.append("<FIRST_NAME>"+dto.getCodigoRegion()+"-"+dto.getDescripcionComuna()+"</FIRST_NAME>");

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
}
