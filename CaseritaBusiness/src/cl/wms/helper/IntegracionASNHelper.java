package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dto.CargonwDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionASNHelper {

private static FileWriter fileWriterLog;

private  static Logger logi = Logger.getLogger(IntegracionASNHelper.class);

	public static void main (String []args){
		IntegracionASNHelper integra = new IntegracionASNHelper();
		//integra.buscaCarguio(2, "C", 13115, 26, "RUTA","A");
		
		integra.procesaConsolidado(2,  43552, 26, "A,192,PC2,AMS", 20161129, "C");
	}
	
	
	public void procesaConsolidado(int codigoEmpresa, int numeroCarguio,   int codigoBodega, String accion, int fecha, String tipcar){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		CarguioDAO carguio = dao.getCarguioDAO();
		ExmartDAO exmartDAO = dao.getExmartDAO();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		CargonwDTO dto = null;
		ClidirDTO clidir =null;
		CarguioDTO lista = carguio.obtieneCarguio(codigoEmpresa, "C", codigoBodega, numeroCarguio);
		List consolida = carguio.obtienedevolucionCarguio(codigoEmpresa, "C", codigoBodega, numeroCarguio,tipcar);
		
		/*String xmlCliente2 = formaXMLCliente2(lista,accion,TipoPedCarguio);
		ftp.enviaFTP(generatxt(xmlCliente2, String.valueOf("Cliente2"+lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()) ));*/
		
		String xml="";
		
		if ("D".equals(tipcar.trim())){
			//XML articulos no despachados desde bodega distribucion
			xml = formaXMLDevo(integraDTO.getAccion().trim(), lista, fecha, consolida, exmartDAO);
		}
		else{
				//public String formaXML(String tipoAccion, CarguioDTO carguio, int fecha, List consolidado){
			xml = formaXML(integraDTO.getAccion().trim(), lista, fecha, consolida, exmartDAO);
		}
		
			//ftp.enviaFTP(generatxt(xml, String.valueOf(numeroCarguio) ));
			String xmlEnvio = generatxt(xml, String.valueOf(numeroCarguio),tipcar.trim());
			
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha2 = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha2);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("CARGCONW");
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
					lista.setNumeroRuta(numeroCarguio);
					log.generaLogCarguio(loginDTO, lista);
				}
				
			}else{
				generatxt2(xml, String.valueOf(numeroCarguio),tipcar.trim());
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha2 = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha2);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("CARGCONW");
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
					lista.setNumeroRuta(numeroCarguio);
					
					log.generaLogCarguio (loginDTO, lista);
				}
			}
		
	}
	
	public String generatxt(String XML, String nombreCarguio, String tipo){
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
		
		String nombreArchivo = "";
		String archivoLog=carpeta+"";
		
		if ("D".equals(tipo.trim())){
			nombreArchivo = "Devrampla"+nombreCarguio+"_"+fechaStr+".xml";
			archivoLog=carpeta+"Devrampla"+nombreCarguio+"_"+fechaStr+".xml";
		}
		else{
			nombreArchivo = "Devcarguio"+nombreCarguio+"_"+fechaStr+".xml";
			archivoLog=carpeta+"Devcarguio"+nombreCarguio+"_"+fechaStr+".xml";
		}
		
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
	
	public String generatxt2(String XML, String nombreCarguio, String tipo){
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
		String nombreArchivo = "carguio"+nombreCarguio+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"carguio"+nombreCarguio+"_"+fechaStr+".xml";
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
	
	public String formaXMLCliente(OrdvtaDTO dto, String tipoAccion, String tipoCliente, String tipoCarguio, CarguioDTO carguio){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		  /*if (TipoPedCarguio!="RAMPLA"){
				xmlCas.append("<STCUST>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</STCUST>");
			}else{
				xmlCas.append("<STCUST>"+carguio.getnumeroCarguioTransf()+"</STCUST>");
			}*/
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  if ("RAMPLA".equals(tipoCarguio)){
			  xmlCas.append("<CSTNUM>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</CSTNUM>");

		  }else{
			  xmlCas.append("<CSTNUM>"+carguio.getnumeroCarguioTransf()+"</CSTNUM>");

		  }
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
				  if ("RAMPLA".equals(tipoCarguio)){
					  xmlCas.append("<HOST_EXT_ID>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</HOST_EXT_ID>");
					  xmlCas.append("<ADRNAM>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</ADRNAM>");
				  }else{
					  xmlCas.append("<HOST_EXT_ID>"+carguio.getnumeroCarguioTransf()+"</HOST_EXT_ID>");
					  xmlCas.append("<ADRNAM>"+carguio.getnumeroCarguioTransf()+"</ADRNAM>");

				  }
				  
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  if ("RAMPLA".equals(tipoCarguio)){
					  xmlCas.append("<ADRLN1>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</ADRLN1>");
					  xmlCas.append("<ADRCTY>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</ADRCTY>");
					  xmlCas.append("<ADRSTC>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</ADRSTC>");
				  }else{
					  
					  xmlCas.append("<ADRLN1>"+carguio.getnumeroCarguioTransf()+"</ADRLN1>");
					  xmlCas.append("<ADRCTY>"+carguio.getnumeroCarguioTransf()+"</ADRCTY>");
					  xmlCas.append("<ADRSTC>"+carguio.getnumeroCarguioTransf()+"</ADRSTC>");
				  }
				  
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  if ("RAMPLA".equals(tipoCarguio)){
					  xmlCas.append("<LAST_NAME>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</LAST_NAME>");
					  xmlCas.append("<FIRST_NAME>"+dto.getRutCliente()+dto.getCorreDireccionOV()+"</FIRST_NAME>");
				  }else{
					  
					  xmlCas.append("<LAST_NAME>"+carguio.getnumeroCarguioTransf()+"</LAST_NAME>");
					  xmlCas.append("<FIRST_NAME>"+carguio.getnumeroCarguioTransf()+"</FIRST_NAME>");
				  }
				  

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	public String formaXMLCliente2(CarguioDTO dto, String tipoAccion, String tipoCliente){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
/*			xmlCas.append("<RTCUST>"+carguio.getNumeroCarguio()+"</RTCUST>");
*/
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<CSTNUM>"+dto.getNumeroCarguio()+"</CSTNUM>");
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
				  
				  xmlCas.append("<HOST_EXT_ID>"+dto.getNumeroCarguio()+"</HOST_EXT_ID>");
				  xmlCas.append("<ADRNAM>"+dto.getNumeroCarguio()+"</ADRNAM>");
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
				  xmlCas.append("<ADRLN1>"+dto.getNumeroCarguio()+"</ADRLN1>");
				  xmlCas.append("<ADRCTY>"+dto.getNumeroCarguio()+"</ADRCTY>");
				  xmlCas.append("<ADRSTC>"+dto.getNumeroCarguio()+"</ADRSTC>");
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  xmlCas.append("<LAST_NAME>"+dto.getNumeroCarguio()+"</LAST_NAME>");
				  xmlCas.append("<FIRST_NAME>"+dto.getNumeroCarguio()+"</FIRST_NAME>");

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	public String formaXMLCliente3( ClidirDTO clidir, String tipoAccion, String tipoCliente){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
/*			xmlCas.append("<BTCUST>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</BTCUST>");
*/
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<CSTNUM>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</CSTNUM>");
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
				  
				  xmlCas.append("<HOST_EXT_ID>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</HOST_EXT_ID>");
				  xmlCas.append("<ADRNAM>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRNAM>");
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
				  xmlCas.append("<ADRLN1>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRLN1>");
				  xmlCas.append("<ADRCTY>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRCTY>");
				  xmlCas.append("<ADRSTC>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRSTC>");
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  xmlCas.append("<LAST_NAME>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</LAST_NAME>");
				  xmlCas.append("<FIRST_NAME>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</FIRST_NAME>");

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	public String formaXML(String tipoAccion, CarguioDTO carguio, int fecha, List consolidado, ExmartDAO exmart){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		
		//xmlCas.append("<PART_INB_IFD>");
		xmlCas.append("<VC_RCPT_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
		xmlCas.append("<TRNNAM>VC_RCPT_INB</TRNNAM>");
		xmlCas.append("<TRNVER>2012.2</TRNVER>");
		xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		
		xmlCas.append("<RCPT_TRLR_SEG>");
		xmlCas.append("<SEGNAM>RECEIPT_TRAILER</SEGNAM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<TRLR_NUM>"+"DEV"+carguio.getNumeroCarguio()+"</TRLR_NUM>");
		xmlCas.append("<CARCOD>DEVOLUCION</CARCOD>");
		xmlCas.append("<TRLR_COD>RCV</TRLR_COD>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		
		
		xmlCas.append("<RCPT_TRUCK_SEG>");
		xmlCas.append("<SEGNAM>RECEIPT_TRUCK</SEGNAM>");

		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<TRKNUM>"+"DEV"+carguio.getNumeroCarguio()+"</TRKNUM>");

		xmlCas.append("<SHPDTE>"+fecha+"</SHPDTE>");
		
		
		xmlCas.append("<RCPT_INVOICE_SEG>");
		xmlCas.append("<SEGNAM>RECEIPT_INVOICE</SEGNAM>");
		xmlCas.append("<TRKNUM>"+"DEV"+carguio.getNumeroCarguio()+"</TRKNUM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");

		//xmlCas.append("<INVTYP>"+"CA"+carguio.getNumeroCarguio()+"</INVTYP>");
		xmlCas.append("<INVTYP>DEV</INVTYP>");
		xmlCas.append("<SUPNUM>CASERITA</SUPNUM>");
		xmlCas.append("<INVNUM>"+"DEV"+carguio.getNumeroCarguio()+"</INVNUM>");
		
		int correlativo=1;
		int correlativoPal=1;
		int corrLineaPal=1;
		
		Iterator car = consolidado.iterator();
		while (car.hasNext()){
			CargonwDTO carDTO = (CargonwDTO) car.next();
			xmlCas.append("<RCPT_LINE_SEG>");
			
			xmlCas.append("<SEGNAM>RECEIPT_LINE</SEGNAM>");
			xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");

			xmlCas.append("<INVLIN>"+correlativo+"</INVLIN>");
			xmlCas.append("<INVSLN>"+"S"+correlativo+"</INVSLN>");
			
			if (corrLineaPal>9){
				correlativoPal=correlativoPal+1;
				corrLineaPal=1;
			}
			
			xmlCas.append("<PRTNUM>"+carDTO.getCodArticulo()+"</PRTNUM>");
			
			xmlCas.append("<EXPQTY>"+carDTO.getCantidad()+"</EXPQTY>");
			xmlCas.append("<EXPIRE_DTE>"+carDTO.getFechaExpiracion()+"000000"+"</EXPIRE_DTE>");//FECHA EXPIRACION QUE VIENE DESDE TABLA DE CONSOLIDADO

			xmlCas.append("<RCVSTS>D</RCVSTS>");//verificar por el momento se enviara el mismo dia del pedido
						xmlCas.append("<RCPT_ASN_SEG>");
						xmlCas.append("<SEGNAM>RECEIPT_ASN</SEGNAM>");
						xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
						xmlCas.append("<PRTNUM>"+carDTO.getCodArticulo()+"</PRTNUM>");
						//OBTIENE DATOS DEL ARTICULO
						ExmartDTO exmartDTO = exmart.recuperaArticulo(carDTO.getCodArticulo(), carDTO.getDvArticulo());
						ExdartDTO exdartDTO = (ExdartDTO) exmartDTO.getCodigos().get("C");
						int unidades=0;
						int display=0;
						if (exmartDTO!=null){
							if (exmartDTO.getDisplay()>0){
								display=exmartDTO.getDisplay();
								unidades = exmartDTO.getCaja()*exmartDTO.getDisplay();
							}else{
								unidades=exmartDTO.getCaja();
							}
						}
						
						xmlCas.append("<UNTCAS>"+unidades+"</UNTCAS>");
						xmlCas.append("<UNTPAK>"+display+"</UNTPAK>");
						xmlCas.append("<UNTQTY>"+carDTO.getCantidad()+"</UNTQTY>");
						xmlCas.append("<INVSTS>D</INVSTS>");//verificar por el momento se enviara el mismo dia del pedido
						xmlCas.append("<FTPCOD>"+carDTO.getCodArticulo()+"</FTPCOD>");
						
						xmlCas.append("<LODNUM>"+"PAL"+carDTO.getNumeroCarguio()+"-"+correlativoPal+"</LODNUM>");
						//Solo para nuevo cambio en QA
						//xmlCas.append("<LODNUM>"+"PAL"+carDTO.getNumeroCarguio()+"-"+carDTO.getCodArticulo()+"</LODNUM>");
						
						xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
						
						xmlCas.append("<SUBNUM>"+"SUB"+carDTO.getNumeroCarguio()+"-"+correlativoPal+"</SUBNUM>");
						//Solo para nuevo cambio en QA
						//xmlCas.append("<SUBNUM>"+"SUB"+carDTO.getNumeroCarguio()+"-"+carDTO.getCodArticulo()+"</SUBNUM>");
						
						xmlCas.append("<DSTLOC>"+"DEV"+carDTO.getNumeroCarguio()+"</DSTLOC>");

							
						xmlCas.append("</RCPT_ASN_SEG>");

			xmlCas.append("</RCPT_LINE_SEG>");
			correlativo++;
			corrLineaPal++;
		}	
		xmlCas.append("</RCPT_INVOICE_SEG>");
		xmlCas.append("</RCPT_TRUCK_SEG>");
		xmlCas.append("</RCPT_TRLR_SEG>");
		xmlCas.append("</CTRL_SEG>");  
		xmlCas.append("</VC_RCPT_INB_IFD>");  
		//xmlCas.append("</PART_INB_IFD>");
		resp = xmlCas.toString();
			
		return resp;
	}
	
	public String formaXMLDevo(String tipoAccion, CarguioDTO carguio, int fecha, List consolidado, ExmartDAO exmart){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		
		//xmlCas.append("<PART_INB_IFD>");
		xmlCas.append("<VC_RCPT_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
		xmlCas.append("<TRNNAM>VC_RCPT_INB</TRNNAM>");
		xmlCas.append("<TRNVER>2012.2</TRNVER>");
		xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		
		xmlCas.append("<RCPT_TRLR_SEG>");
		xmlCas.append("<SEGNAM>RECEIPT_TRAILER</SEGNAM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<TRLR_NUM>"+"DEV"+carguio.getNumeroCarguio()+"</TRLR_NUM>");
		xmlCas.append("<CARCOD>DEVOLUCION</CARCOD>");
		xmlCas.append("<TRLR_COD>RCV</TRLR_COD>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		
		xmlCas.append("<RCPT_TRUCK_SEG>");
		xmlCas.append("<SEGNAM>RECEIPT_TRUCK</SEGNAM>");
		
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<TRKNUM>"+"DEV"+carguio.getNumeroCarguio()+"</TRKNUM>");
		
		xmlCas.append("<SHPDTE>"+fecha+"</SHPDTE>");
			
		xmlCas.append("<RCPT_INVOICE_SEG>");
		xmlCas.append("<SEGNAM>RECEIPT_INVOICE</SEGNAM>");
		xmlCas.append("<TRKNUM>"+"DEV"+carguio.getNumeroCarguio()+"</TRKNUM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		
		xmlCas.append("<INVTYP>DVRA</INVTYP>");
		xmlCas.append("<SUPNUM>CASERITA</SUPNUM>");
		xmlCas.append("<INVNUM>"+"DEV"+carguio.getNumeroCarguio()+"</INVNUM>");
		
		int correlativo=1;
		int correlativoPal=1;
		int corrLineaPal=1;
		
		Iterator car = consolidado.iterator();
		while (car.hasNext()){
			CargonwDTO carDTO = (CargonwDTO) car.next();
			xmlCas.append("<RCPT_LINE_SEG>");
			
			xmlCas.append("<SEGNAM>RECEIPT_LINE</SEGNAM>");
			xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
			
			xmlCas.append("<INVLIN>"+correlativo+"</INVLIN>");
			xmlCas.append("<INVSLN>"+"S"+correlativo+"</INVSLN>");
			
			if (corrLineaPal>9){
				correlativoPal=correlativoPal+1;
				corrLineaPal=1;
			}
			
			xmlCas.append("<PRTNUM>"+carDTO.getCodArticulo()+"</PRTNUM>");
			
			xmlCas.append("<EXPQTY>"+carDTO.getCantidad()+"</EXPQTY>");
			xmlCas.append("<EXPIRE_DTE>"+carDTO.getFechaExpiracion()+"000000"+"</EXPIRE_DTE>");//FECHA EXPIRACION QUE VIENE DESDE TABLA DE CONSOLIDADO
			xmlCas.append("<RCVSTS>F</RCVSTS>");//verificar por el momento se enviara el mismo dia del pedido
					xmlCas.append("<RCPT_ASN_SEG>");
					xmlCas.append("<SEGNAM>RECEIPT_ASN</SEGNAM>");
					xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
					xmlCas.append("<PRTNUM>"+carDTO.getCodArticulo()+"</PRTNUM>");
					//OBTIENE DATOS DEL ARTICULO
					ExmartDTO exmartDTO = exmart.recuperaArticulo(carDTO.getCodArticulo(), carDTO.getDvArticulo());
					ExdartDTO exdartDTO = (ExdartDTO) exmartDTO.getCodigos().get("C");
					int unidades=0;
					int display=0;
					if (exmartDTO!=null){
						if (exmartDTO.getDisplay()>0){
							display=exmartDTO.getDisplay();
							unidades = exmartDTO.getCaja()*exmartDTO.getDisplay();
						}else{
							unidades=exmartDTO.getCaja();
						}
					}
					
					xmlCas.append("<UNTCAS>"+unidades+"</UNTCAS>");
					xmlCas.append("<UNTPAK>"+display+"</UNTPAK>");
					xmlCas.append("<UNTQTY>"+carDTO.getCantidad()+"</UNTQTY>");
					xmlCas.append("<INVSTS>F</INVSTS>");//verificar por el momento se enviara el mismo dia del pedido
					xmlCas.append("<FTPCOD>"+carDTO.getCodArticulo()+"</FTPCOD>");
					
					xmlCas.append("<LODNUM>"+"PAL"+carDTO.getNumeroCarguio()+"-"+correlativoPal+"</LODNUM>");
					//Solo para nuevo cambio en QA
					//xmlCas.append("<LODNUM>"+"PAL"+carDTO.getNumeroCarguio()+"-"+carDTO.getCodArticulo()+"</LODNUM>");
					
					xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
					
					xmlCas.append("<SUBNUM>"+"SUB"+carDTO.getNumeroCarguio()+"-"+correlativoPal+"</SUBNUM>");
					//Solo para nuevo cambio en QA
					//xmlCas.append("<SUBNUM>"+"SUB"+carDTO.getNumeroCarguio()+"-"+carDTO.getCodArticulo()+"</SUBNUM>");
					
					xmlCas.append("<DSTLOC>"+"DEV"+carDTO.getNumeroCarguio()+"</DSTLOC>");
					
					xmlCas.append("</RCPT_ASN_SEG>");

				xmlCas.append("</RCPT_LINE_SEG>");
				correlativo++;
				corrLineaPal++;
			}	
			xmlCas.append("</RCPT_INVOICE_SEG>");
			xmlCas.append("</RCPT_TRUCK_SEG>");
			xmlCas.append("</RCPT_TRLR_SEG>");
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_RCPT_INB_IFD>");  
			//xmlCas.append("</PART_INB_IFD>");
			resp = xmlCas.toString();
					
		return resp;
	}
}
