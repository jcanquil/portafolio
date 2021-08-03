package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

	public class IntegracionMermasHelper {
		private static FileWriter fileWriterLog;
		private  static Logger logi = Logger.getLogger(IntegracionMermasHelper.class);

		public static void main (String []agrs){
			IntegracionMermasHelper helper = new IntegracionMermasHelper();
				//helper.integracionAll();
				//helper.integracion(2,24,20160422,66943, "A,192.168.1.20,PCJAIME,JHCANQUIL");
				
				//Mermas anulacion
				helper.integracion(2,5,20160527,66922, "A,192.168.1.20,PCJAIME,JHCANQUIL");
			}
			
		
		public void integracion(int codEmpresa, int codMovto, int fechaMovto, int numDocumento, String accion){
			DAOFactory dao = DAOFactory.getInstance();
			VecmarDAO vecmar = dao.getVecmarDAO();
			VedmarDAO vedmar = dao.getVedmarDAO();
			FtpprovDAO ftpDAO = dao.getFtpprovDAO();

			EnviaFTPWMS ftp = new EnviaFTPWMS();
			LogintegracionDAO log = dao.getLogintegracionDAO();
			ConvierteStringDTO convierte = new ConvierteStringDTO();
			IntegracionDTO integraDTO = convierte.convierte(accion);
			ExmartDAO exmartDAO = dao.getExmartDAO();
			
			VecmarDTO vecmardto = vecmar.obtenerDatosVecmarMer(codEmpresa, codMovto, fechaMovto, numDocumento);
			List lista = vedmar.obtenerDatosVedmarMer(codEmpresa, codMovto, fechaMovto, numDocumento);
			
			//XML para generar MERMAS ANULACION MOVIMIENTO 05
			String xml="";
			
			if (codMovto==5){
				xml = formaXMLMermaAnulacion(vecmardto,lista,integraDTO.getAccion().trim(), exmartDAO);
			}
			else if (codMovto==24){
				
				//Cliente 1
				String xmlCliente = formaXMLCliente(vecmardto, integraDTO.getAccion().trim(),"DEVO");
				String xmlEnvioCliente = generatxt("Cliente1DEVO_"+String.valueOf(vecmardto.getNumDocumento()),xmlCliente);
				if (ftp.enviaFTP(xmlEnvioCliente, ftpDAO)){
					
				}else{
					generatxt2("Cliente1DEVO_"+String.valueOf(vecmardto.getNumDocumento()),xmlCliente);

				}
				try{
					Thread.sleep(1000);
				}catch (Exception e){
					e.printStackTrace();
				}
				
				//Cliente 2
				 xmlCliente = formaXMLCliente2(vecmardto, integraDTO.getAccion().trim(),"DEVO");
				 xmlEnvioCliente = generatxt("Cliente2DEVO_"+String.valueOf(vecmardto.getNumDocumento()),xmlCliente);
				if (ftp.enviaFTP(xmlEnvioCliente, ftpDAO)){
					
				}else{
					 generatxt2("Cliente2DEVO_"+String.valueOf(vecmardto.getNumDocumento()),xmlCliente);

				}
				try{
					Thread.sleep(1000);
				}catch (Exception e){
					e.printStackTrace();
				}
				
				xml = formaXML(vecmardto,lista, integraDTO.getAccion().trim());
			}
				
				//ftp.enviaFTP(generatxt(vecmardto.getNumDocumento(),xml));
			
				String xmlEnvio = generatxt(String.valueOf(vecmardto.getNumDocumento()),xml);
				if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getYYYYMMDD());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setTipoAccion(integraDTO.getAccion());
						
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("MERMAS");
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
						
						log.generaLogMerma(loginDTO, vecmardto);
						}
					
				}else{
					generatxt2(String.valueOf(vecmardto.getNumDocumento()),xml);
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getDDMMYYYY());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setTipoAccion(integraDTO.getAccion());

						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("MERMAS");
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
						
						log.generaLogMerma(loginDTO, vecmardto);
					}
				}
		}
		
		public String generatxt(String numdoc, String XML){
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
			String nombreArchivo = "devmermaprov_"+numdoc+"_"+fechaStr+".xml";
			String archivoLog=carpeta+"devmermaprov_"+numdoc+"_"+fechaStr+".xml";
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
		
		public String generatxt2(String numdoc, String XML){
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
			String nombreArchivo = "devmermaprov_"+numdoc+"_"+fechaStr+".xml";
			String archivoLog=carpeta+"devmermaprov_"+numdoc+"_"+fechaStr+".xml";
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
		
		public String formaXMLCliente(VecmarDTO dto, String tipoAccion, String tipoCliente){
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
			  xmlCas.append("<CSTNUM>"+dto.getRutProveedor()+"</CSTNUM>");
			  
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
					  
						  xmlCas.append("<HOST_EXT_ID>"+dto.getRutProveedor()+"</HOST_EXT_ID>");
						  xmlCas.append("<ADRNAM>"+dto.getRutProveedor()+"</ADRNAM>");
					  
					  
					  xmlCas.append("<ADRTYP>CST</ADRTYP>");
					  
						  xmlCas.append("<ADRLN1>"+dto.getRutProveedor()+"</ADRLN1>");
						  xmlCas.append("<ADRCTY>"+dto.getRutProveedor()+"</ADRCTY>");
						  xmlCas.append("<ADRSTC>"+dto.getRutProveedor()+"</ADRSTC>");
					  
					  
					  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
					  
						  xmlCas.append("<LAST_NAME>"+dto.getRutProveedor()+"</LAST_NAME>");
						  xmlCas.append("<FIRST_NAME>"+dto.getRutProveedor()+"</FIRST_NAME>");
					  
					  

			  xmlCas.append("</ADDR_SEG>");
			  
			  
			 
						
				xmlCas.append("</CUST_SEG>");  
				xmlCas.append("</CTRL_SEG>");  
				xmlCas.append("</VC_CUST_INB_IFD>");  
				resp = xmlCas.toString();
					  
			  
			  
			return resp;
		}
		
		public String formaXMLCliente2(VecmarDTO dto, String tipoAccion, String tipoCliente){
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
			  xmlCas.append("<CSTNUM>"+"DEVPROV-"+dto.getNumDocumento()+"</CSTNUM>");
			  
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
					  
						  xmlCas.append("<HOST_EXT_ID>"+"DEVPROV-"+dto.getNumDocumento()+"</HOST_EXT_ID>");
						  xmlCas.append("<ADRNAM>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRNAM>");
					  
					  
					  xmlCas.append("<ADRTYP>CST</ADRTYP>");
					  
						  xmlCas.append("<ADRLN1>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRLN1>");
						  xmlCas.append("<ADRCTY>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRCTY>");
						  xmlCas.append("<ADRSTC>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRSTC>");
					  
					  
					  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
					  
						  xmlCas.append("<LAST_NAME>"+"DEVPROV-"+dto.getNumDocumento()+"</LAST_NAME>");
						  xmlCas.append("<FIRST_NAME>"+"DEVPROV-"+dto.getNumDocumento()+"</FIRST_NAME>");
					  
					  

			  xmlCas.append("</ADDR_SEG>");
			  
			  
			 
						
				xmlCas.append("</CUST_SEG>");  
				xmlCas.append("</CTRL_SEG>");  
				xmlCas.append("</VC_CUST_INB_IFD>");  
				resp = xmlCas.toString();
					  
			  
			  
			return resp;
		}
		
		public String formaXML(VecmarDTO dto,List dtolist, String tipoAccion){
			//Forma XML para enviar a WMS
			String resp ="";
			StringBuffer xmlCas = new StringBuffer();
			String xml="";
			
			xmlCas.append("<VC_ORDER_INB_IFD>");  				//xmlCas.append("<VC_ORDER_INB_IFD>");
			xmlCas.append("<CTRL_SEG>");						//xmlCas.append("<CTRL_SEG>");
			xmlCas.append("<TRNNAM>ORDER_TRAN</TRNNAM>");   	//xmlCas.append("<TRNNAM>ORDER_TRAN</TRNNAM>");
			xmlCas.append("<TRNVER>8.2</TRNVER>");				//xmlCas.append("<TRNVER>8.2</TRNVER>");
			xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");			//xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
			
			xmlCas.append("<ST_CUST_SEG>");
			xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<HOST_EXT_ID>"+dto.getRutProveedor()+"</HOST_EXT_ID>");
			xmlCas.append("<ADRNAM>"+dto.getRutProveedor()+"</ADRNAM>");
			xmlCas.append("<ADRTYP>"+dto.getRutProveedor()+"</ADRTYP>");
			xmlCas.append("<ADRLN1>"+dto.getRutProveedor()+"</ADRLN1>");
			xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			xmlCas.append("</ST_CUST_SEG>");
			
			
			xmlCas.append("<RT_CUST_SEG>");
			xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<HOST_EXT_ID>"+"DEVPROV-"+dto.getNumDocumento()+"</HOST_EXT_ID>");
			xmlCas.append("<ADRNAM>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRNAM>");
			xmlCas.append("<ADRTYP>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRTYP>");
			xmlCas.append("<ADRLN1>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRLN1>");
			xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			xmlCas.append("</RT_CUST_SEG>");
			
			xmlCas.append("<BT_CUST_SEG>");
			xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<HOST_EXT_ID>"+"DEVPROV-"+dto.getNumDocumento()+"</HOST_EXT_ID>");
			xmlCas.append("<ADRNAM>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRNAM>");
			xmlCas.append("<ADRTYP>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRTYP>");
			xmlCas.append("<ADRLN1>"+"DEVPROV-"+dto.getNumDocumento()+"</ADRLN1>");
			xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			xmlCas.append("</BT_CUST_SEG>");
			
			
			
			
			xmlCas.append("<ORDER_SEG>");						//xmlCas.append("<ORDER_SEG>");
			xmlCas.append("<SEGNAM>ORDER</SEGNAM>");			//xmlCas.append("<SEGNAM>ORDER</SEGNAM>");
			xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>"); //xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");			//xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			
			xmlCas.append("<ORDNUM>"+"DEVPROV-"+dto.getNumDocumento()+"</ORDNUM>");	//xmlCas.append("<ORDNUM>"+carguio.getNumeroCarguio()+"-"+dto.getNumeroOV()+"-"+dto.getCorreDireccionOV()+"</ORDNUM>");
			xmlCas.append("<ORDTYP>DEVO</ORDTYP>");							//xmlCas.append("<ORDTYP>"+TipoPedCarguio.trim()+"</ORDTYP>");
			xmlCas.append("<ENTDTE>"+dto.getFechaMvto()+"000000"+"</ENTDTE>");	//xmlCas.append("<ENTDTE>"+dto.getFechaOrden()+"</ENTDTE>");
			
				xmlCas.append("<STCUST>"+dto.getRutProveedor()+"</STCUST>");	//xmlCas.append("<STCUST>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</STCUST>");
				xmlCas.append("<ST_HOST_ADR_ID>"+dto.getRutProveedor()+"</ST_HOST_ADR_ID>");	//xmlCas.append("<ST_HOST_ADR_ID>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</ST_HOST_ADR_ID>");
			
			
			xmlCas.append("<RTCUST>"+"DEVPROV-"+dto.getNumDocumento()+"</RTCUST>");	//xmlCas.append("<RTCUST>"+carguio.getNumeroCarguio()+"</RTCUST>");
			xmlCas.append("<RT_HOST_ADR_ID>"+"DEVPROV-"+dto.getNumDocumento()+"</RT_HOST_ADR_ID>");	//xmlCas.append("<RT_HOST_ADR_ID>"+carguio.getNumeroCarguio()+"</RT_HOST_ADR_ID>");
			
			xmlCas.append("<BTCUST>"+"DEVPROV-"+dto.getNumDocumento()+"</BTCUST>");	//xmlCas.append("<BTCUST>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</BTCUST>");
			xmlCas.append("<BT_HOST_ADR_ID>"+"DEVPROV-"+dto.getNumDocumento()+"</BT_HOST_ADR_ID>");	//xmlCas.append("<BT_HOST_ADR_ID>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</BT_HOST_ADR_ID>");
			
			xmlCas.append("<SHIPBY>RTCUST</SHIPBY>");	//xmlCas.append("<SHIPBY>RTCUST</SHIPBY>");
			xmlCas.append("<WAVE_FLG>1</WAVE_FLG>");	//xmlCas.append("<WAVE_FLG>1</WAVE_FLG>");
			
			xmlCas.append("<DEPTNO>"+dto.getPatente()+"</DEPTNO>");
			
			xmlCas.append("</ORDER_SEG>");		//xmlCas.append("</ORDER_SEG>");
			
			Iterator car = dtolist.iterator();				//Iterator car = dto.getDetord().iterator();
			int corre=1;
			while (car.hasNext()){							//while (car.hasNext()){
				VedmarDTO vedDTO = (VedmarDTO) car.next();		//DetordDTO carDTO = (DetordDTO) car.next();
				xmlCas.append("<ORDER_LINE_SEG>");			//xmlCas.append("<ORDER_LINE_SEG>");
				
				xmlCas.append("<SEGNAM>ORDER_LINE</SEGNAM>");					//xmlCas.append("<SEGNAM>ORDER_LINE</SEGNAM>");
				xmlCas.append("<ORDNUM>"+"DEVPROV-"+dto.getNumDocumento()+"</ORDNUM>");	//xmlCas.append("<ORDNUM>"+carguio.getNumeroCarguio()+"-"+dto.getNumeroOV()+"-"+dto.getCorreDireccionOV()+"</ORDNUM>");
				xmlCas.append("<ORDLIN>"+corre+"</ORDLIN>");	//xmlCas.append("<ORDLIN>"+carDTO.getCorrelativoDetalleOV()+"</ORDLIN>");
				xmlCas.append("<ORDSLN>"+"S"+corre+"</ORDSLN>");							//xmlCas.append("<ORDSLN>0000</ORDSLN>");
				xmlCas.append("<PRTNUM>"+vedDTO.getCodigoArticulo()+"</PRTNUM>");	//xmlCas.append("<PRTNUM>"+carDTO.getCodigoArticulo()+"</PRTNUM>");
				
				xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");		//xmlCas.append("<PRT_CLIENT_I>----</PRT_CLIENT_I>");
				xmlCas.append("<INVSTS>M</INVSTS>");						//xmlCas.append("<INVSTS>"+carguio.getEstadoInvWMS()+"</INVSTS>");
				xmlCas.append("<ORDQTY>"+vedDTO.getCantidadArticulo()+"</ORDQTY>");	//xmlCas.append("<ORDQTY>"+carDTO.getCantidadArticulo()+"</ORDQTY>");
				xmlCas.append("<CARCOD>PROVEEDOR</CARCOD>");		//xmlCas.append("<CARCOD>"+carguio.getRutChofer()+"-"+carguio.getDvChofer()+"</CARCOD>");
				xmlCas.append("<UNTPAK>0</UNTPAK>");	//xmlCas.append("<UNTPAK>0</UNTPAK>");
				xmlCas.append("<UNTPAL>0</UNTPAL>");	//xmlCas.append("<UNTPAL>0</UNTPAL>");
				xmlCas.append("<UNTCAS>0</UNTCAS>");	//xmlCas.append("<UNTCAS>0</UNTCAS>");
				xmlCas.append("<LATE_SHPDTE>"+dto.getFechaMvto()+"000000"+"</LATE_SHPDTE>");	//xmlCas.append("<LATE_SHPDTE>"+dto.getFechaOrden()+"</LATE_SHPDTE>");
				xmlCas.append("<SRVLVL>1</SRVLVL>");
				xmlCas.append("<DSTARE>SDESP</DSTARE>");
				xmlCas.append("</ORDER_LINE_SEG>");	//xmlCas.append("</ORDER_LINE_SEG>");
				corre++;
			}	
			xmlCas.append("</CTRL_SEG>");  		//xmlCas.append("</CTRL_SEG>");
			xmlCas.append("</VC_ORDER_INB_IFD>");  	//xmlCas.append("</VC_ORDER_INB_IFD>");
			resp = xmlCas.toString();		//resp = xmlCas.toString();
				
			return resp;
		}
		
		public String formaXMLMermaAnulacion(VecmarDTO dto,List dtolist,String tipoAccion, ExmartDAO exmartDAO){
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
					xmlCas.append("<INVNUM>"+"ANUDEVPROV-"+dto.getNumDocumento()+"</INVNUM>");
					xmlCas.append("<SUPNUM>"+dto.getRutProveedor()+dto.getDvProveedor()+"</SUPNUM>");
					xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
					xmlCas.append("<RIMSTS>OPEN</RIMSTS>");
					xmlCas.append("<INVTYP>PROV</INVTYP>");
					xmlCas.append("<INVDTE>"+dto.getFechaMvto()+"</INVDTE>");
					
					Iterator iter = dtolist.iterator();
					while (iter.hasNext()){
						VedmarDTO dtolista = (VedmarDTO) iter.next(); 
						
						xmlCas.append("<LINE_SEG>");
							xmlCas.append("<SEGNAM>LINE_SEG</SEGNAM>");
							xmlCas.append("<INVLIN>"+dtolista.getCorrelativo()+"</INVLIN>");
							xmlCas.append("<INVSLN>"+dtolista.getCorrelativo()+"</INVSLN>");
							ExmartDTO exmart = exmartDAO.recuperaArticulo(dtolista.getCodigoArticulo(), dtolista.getDigArticulo());
							
							ExdartDTO dto2 = (ExdartDTO) exmart.getCodigos().get("U");
							  if (dto2!=null){
								  int cantidadBP=(int)dto2.getCantBasePallets();
								  exmart.setPallet((int)dto2.getCantTotalPallets());
							  }
							  
							int unidades=0;
							if ("C".equals(dtolista.getFormato())){
								if (exmart.getDisplay()>0){
									unidades=exmart.getCaja()*exmart.getDisplay()*dtolista.getCantidadArticulo();

								}else{
									unidades=exmart.getCaja()*dtolista.getCantidadArticulo();

								}
							}else if ("D".equals(dtolista.getFormato())){
								unidades=exmart.getDisplay()*dtolista.getCantidadArticulo();
							}else if ("P".equals(dtolista.getFormato())){
								if (exmart.getDisplay()>0){
									unidades=(exmart.getPallet()*exmart.getCaja()*exmart.getDisplay())*dtolista.getCantidadArticulo();
								}else{
									unidades=(exmart.getPallet()*exmart.getCaja())*dtolista.getCantidadArticulo();
								}
								
							}else if ("U".equals(dtolista.getFormato())){
								unidades=dtolista.getCantidadArticulo();
							}
							
							//xmlCas.append("<EXPQTY>"+dtolista.getStocLinea()+"</EXPQTY>");//Se modifica por unidades
							xmlCas.append("<EXPQTY>"+unidades+"</EXPQTY>");
							xmlCas.append("<PRTNUM>"+dtolista.getCodigoArticulo()+"</PRTNUM>");
							xmlCas.append("<ORGCOD>----</ORGCOD>");
							xmlCas.append("<REVLVL>----</REVLVL>");
							xmlCas.append("<LOTNUM>----</LOTNUM>");
							xmlCas.append("<RCVSTS>"+'M'+"</RCVSTS>");
						xmlCas.append("</LINE_SEG>");
					}
					
				xmlCas.append("</HEADER_SEG>");
			xmlCas.append("</CTRL_SEG>");
			xmlCas.append("</VC_RA_INB_IFD>");
			
			resp = xmlCas.toString();  
			
			return resp;
		}
		
	}
	