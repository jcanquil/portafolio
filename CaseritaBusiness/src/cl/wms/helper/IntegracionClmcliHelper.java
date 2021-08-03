package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionClmcliHelper {

	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegracionClmcliHelper.class);

	public static void main (String[]args){
		IntegracionClmcliHelper helper = new IntegracionClmcliHelper();
		
		//helper.procesaClmcli();
		
		helper.procesaClmcliPorRutCorrelativo(8084782, "3", 100,"A");
	}
	
	public void procesaClmcli(){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		List lista = clmcliDAO.recuperaAllCliente();
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			ClmcliDTO clmcli = (ClmcliDTO) iter.next();
			Iterator iter2 = clmcli.getDirecciones().iterator();
			while (iter2.hasNext()){
				ClidirDTO dto = (ClidirDTO) iter2.next();
				String xml = formaXML(clmcli, dto, "A","CLIE");
				if (ftp.enviaFTP(generatxt(xml,clmcli.getRutCliente()),ftpDAO)){
					
				}else{
					generatxt2(xml,clmcli.getRutCliente());
				}
				try{
					 Thread.sleep(1000);
					 logi.info("tread 1000:");
				 }catch(Exception e){
					 e.printStackTrace();
				 }

			}
			
		}
	}
	
	public void procesaClmcliPorRut(int rutCliente, String dv, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		ClmcliDTO clmcli = clmcliDAO.recuperaCliente(rutCliente, dv);
		if (clmcli!=null){
			Iterator iter2 = clmcli.getDirecciones().iterator();
			while (iter2.hasNext()){
				ClidirDTO dto = (ClidirDTO) iter2.next();
				String xml = formaXML(clmcli, dto, accion, "CLIE");
				if (ftp.enviaFTP(generatxt(xml,rutCliente),ftpDAO)){
					
				}else{
					generatxt2(xml,rutCliente);
				}
				
				

			}
		}
			
			
		
	}
	
	public void procesaClmcliPorRutCorrelativo(int rutCliente, String dv,int correlativo, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		ClmcliDTO clmcli = clmcliDAO.recuperaClienteDireccion(rutCliente, dv, correlativo);
		
			Iterator iter2 = clmcli.getDirecciones().iterator();
			while (iter2.hasNext()){
				ClidirDTO dto = (ClidirDTO) iter2.next();
				String xml = formaXML(clmcli, dto, integraDTO.getAccion().trim(),"CLIE");
				//ftp.enviaFTP(generatxt(xml,rutCliente));
				String xmlEnvio = generatxt(xml,  rutCliente);
				if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getYYYYMMDD());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(clmcli.getRutCliente());
						loginDTO.setDv(clmcli.getDvCliente());
						loginDTO.setCorrelativoDir(dto.getCorrelativo());
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("CLIDIR");
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
						
						log.generaLogCliente(loginDTO);
						}
					
				}else{
					generatxt2(xml,  rutCliente);
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getDDMMYYYY());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(clmcli.getRutCliente());
						loginDTO.setDv(clmcli.getDvCliente());
						loginDTO.setCorrelativoDir(dto.getCorrelativo());
						loginDTO.setTipoAccion(integraDTO.getAccion());

						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("CLIDIR");
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
						
						log.generaLogCliente(loginDTO);
					}
				}
				
				
				try{
					 Thread.sleep(1000);
					 logi.info("tread 1000:");
				 }catch(Exception e){
					 e.printStackTrace();
				 }

			}
			
		
	}
	
	public String generatxt(String XML, int rutCliente){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 14);
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
		String nombreArchivo = "cliente_"+rutCliente+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"cliente_"+rutCliente+"_"+fechaStr+".xml";
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
	
	public String generatxt2(String XML, int rutCliente){
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
		String nombreArchivo = "cliente_"+rutCliente+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"cliente_"+rutCliente+"_"+fechaStr+".xml";
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
	
	public String formaXML(ClmcliDTO dto, ClidirDTO clidir, String tipoAccion, String tipoCliente){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		  
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<CSTNUM>"+dto.getRutCliente()+"-"+clidir.getCorrelativo()+"</CSTNUM>");
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
		  xmlCas.append("<CSTTYP>"+tipoCliente.trim()+"</CSTTYP>");
		  xmlCas.append("<INVSTS_PRG></INVSTS_PRG>");

		  xmlCas.append("<ROUTE_TO_ADDRESS></ROUTE_TO_ADDRESS>");
		  xmlCas.append("<ADDR_SEG>");
				  xmlCas.append("<SEGNAM>0</SEGNAM>");
				  xmlCas.append("<TRNTYP></TRNTYP>");
				  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				  
				  xmlCas.append("<HOST_EXT_ID>"+dto.getRutCliente()+"-"+clidir.getCorrelativo()+"</HOST_EXT_ID>");
				  xmlCas.append("<ADRNAM>"+dto.getRutCliente()+"-"+clidir.getCorrelativo()+"</ADRNAM>");
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
				  xmlCas.append("<ADRLN1>"+clidir.getDireccionCliente().trim()+"</ADRLN1>");
				  xmlCas.append("<ADRCTY>"+clidir.getDescripcionCiudad().trim()+"</ADRCTY>");
				  xmlCas.append("<ADRSTC>"+clidir.getDescripcionComuna().trim()+"</ADRSTC>");
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  xmlCas.append("<LAST_NAME>"+dto.getRazonsocial().trim()+"</LAST_NAME>");
				  xmlCas.append("<FIRST_NAME>"+dto.getRazonsocial().trim()+"</FIRST_NAME>");

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
}
