package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExdacbDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.ExdacbDTO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionExdacbHelper {
	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegracionExdacbHelper.class);

	public static void main(String []args){
		IntegracionExdacbHelper helper = new IntegracionExdacbHelper();
		helper.procesa(17103, "4", "A,192.168.1.20,PCJAIME,JHCANQUIL", 10, 20160419);
	}
	public void procesa(int codigo, String dv, String accion, int cantidad, int fecha){
		DAOFactory dao = DAOFactory.getInstance();
		ExdacbDAO exdacb = dao.getExdacbDAO();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		List lista = exdacb.listaExdodc(codigo, dv);
		int cantidad2=0;
		if (cantidad>0){
			cantidad2=cantidad;
		}else{
			cantidad2 = cantidad * (0-1);
		}
		logi.info(cantidad2);
		int cantidadXML=0;
		String xml="";
		//Cliente 1

		if (cantidad>0){
			String xmlCliente = formaXMLCliente(codigo,dv, integraDTO.getAccion().trim(),"COMB", "MASCOMB", fecha);
			String xmlEnvioCliente = generatxt(xmlCliente,"ClienteCombo_"+String.valueOf(codigo));
			if (ftp.enviaFTP(xmlEnvioCliente, ftpDAO)){
				
			}else{
				generatxt2(xmlCliente,"ClienteCombo_"+String.valueOf(codigo));
			}
		}else{
			String xmlCliente = formaXMLCliente(codigo,dv, integraDTO.getAccion().trim(),"COMB", "MENCOMB", fecha);
			String xmlEnvioCliente = generatxt(xmlCliente,"ClienteCombo_"+String.valueOf(codigo));
			if (ftp.enviaFTP(xmlEnvioCliente, ftpDAO)){
				
			}else{
				generatxt2(xmlCliente,"ClienteCombo_"+String.valueOf(codigo));
			}
		}
		
		
		try{
			Thread.sleep(1000);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
		int correlativo=1;
		while (cantidad2>cantidadXML){
			
				
				if (cantidad>0){
					
					 xml = formaXMLPedido(lista,integraDTO.getAccion().trim(),codigo, dv,"COMB",fecha, correlativo);

				}else{
					 xml = formaXMLPedido2(integraDTO.getAccion().trim(),codigo, dv,"COMB",fecha, cantidad2, correlativo);
					 
					// xml = formaXMLOrdenes(lista,integraDTO.getAccion().trim(),codigo, dv,"COMB",fecha);

				}
				
				//ftp.enviaFTP(generatxt(xml, codigo+"_"+cantidadXML));
			
				String xmlEnvio = generatxt(xml, codigo+"_"+correlativo);
				correlativo++;
				if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha2 = Integer.parseInt(fch.getYYYYMMDD());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(codigo);
						loginDTO.setDv(dv);
						loginDTO.setFechaArchivo(fecha2);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("EXDACB");
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
						
						log.generaLogArticulo(loginDTO);
					}
					
				}else{
					generatxt2(xml, codigo+"_"+correlativo);
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha2 = Integer.parseInt(fch.getDDMMYYYY());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(codigo);
						loginDTO.setDv(dv);
						loginDTO.setFechaArchivo(fecha2);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("EXDACB");
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
						
						log.generaLogArticulo(loginDTO);
					}
				}
			cantidadXML++;
			if (cantidad<0){
				break;
			}
		}
		
		
	}
	
	public String formaXMLCliente(int codigo,String dv, String tipoAccion, String tipoCliente, String creacion, int fecha){
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
		  xmlCas.append("<CSTNUM>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</CSTNUM>");
		  
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
				  
					  xmlCas.append("<HOST_EXT_ID>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</HOST_EXT_ID>");
					  xmlCas.append("<ADRNAM>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</ADRNAM>");
				  
				  
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
					  xmlCas.append("<ADRLN1>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</ADRLN1>");
					  xmlCas.append("<ADRCTY>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</ADRCTY>");
					  xmlCas.append("<ADRSTC>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</ADRSTC>");
				  
				  
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  
					  xmlCas.append("<LAST_NAME>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</LAST_NAME>");
					  xmlCas.append("<FIRST_NAME>"+creacion+"-"+codigo+"-"+fecha+"000000"+"</FIRST_NAME>");
				  
				  

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	
	public String generatxt(String XML, String codigo){
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
		String nombreArchivo = "codCombo"+codigo+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"CodCombo"+codigo+"_"+fechaStr+".xml";
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
	
	public String generatxt2(String XML, String nombreCarguio){
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
	
	public String formaXMLPedido(List articulos, String tipoAccion, int codigo, String dv, String TipoPedCarguio, int fecha, int correlativo){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		
		//xmlCas.append("<PART_INB_IFD>");
		xmlCas.append("<VC_ORDER_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
		xmlCas.append("<TRNNAM>ORDER_TRAN</TRNNAM>");
		xmlCas.append("<TRNVER>8.2</TRNVER>");
		xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		
		xmlCas.append("<ORDER_SEG>");
		xmlCas.append("<SEGNAM>ORDER</SEGNAM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		
		xmlCas.append("<ORDNUM>"+"MASCOMB"+"-"+codigo+"-"+correlativo+"-"+fecha+"000000"+"</ORDNUM>");
		xmlCas.append("<ORDTYP>"+TipoPedCarguio.trim()+"</ORDTYP>");
		xmlCas.append("<ENTDTE>"+fecha+"000000"+"</ENTDTE>");
		xmlCas.append("<STCUST>"+"MASCOMB"+"-"+codigo+"-"+fecha+"000000"+"</STCUST>");
		xmlCas.append("<ST_HOST_ADR_ID>"+"MASCOMB"+"-"+codigo+"-"+fecha+"000000"+"</ST_HOST_ADR_ID>");
		xmlCas.append("<RTCUST>"+"MASCOMB"+"-"+codigo+"-"+fecha+"000000"+"</RTCUST>");
		xmlCas.append("<RT_HOST_ADR_ID>"+"MASCOMB"+"-"+codigo+"-"+fecha+"000000"+"</RT_HOST_ADR_ID>");
		xmlCas.append("<BTCUST>"+"MASCOMB"+"-"+codigo+"-"+fecha+"000000"+"</BTCUST>");
		xmlCas.append("<BT_HOST_ADR_ID>"+"MASCOMB"+"-"+codigo+"-"+fecha+"000000"+"</BT_HOST_ADR_ID>");
		
		xmlCas.append("<SHIPBY>RTCUST</SHIPBY>");
		xmlCas.append("<WAVE_FLG>1</WAVE_FLG>");
		xmlCas.append("</ORDER_SEG>");
		
		//Iterator car = carguio.getCarguioD().iterator();
		
			Iterator car = articulos.iterator();
			int cantidad=1;
			while (car.hasNext()){
				ExdacbDTO exdacb = (ExdacbDTO) car.next();
				xmlCas.append("<ORDER_LINE_SEG>");
				
				xmlCas.append("<SEGNAM>ORDER_LINE</SEGNAM>");
				xmlCas.append("<ORDNUM>"+"MASCOMB"+"-"+codigo+"-"+correlativo+"-"+fecha+"000000"+"</ORDNUM>");
				xmlCas.append("<ORDLIN>"+cantidad+"</ORDLIN>");
				xmlCas.append("<ORDSLN>"+"S"+cantidad+"</ORDSLN>");
				xmlCas.append("<PRTNUM>"+exdacb.getCodigoArticuloHijo()+"</PRTNUM>");
				
				xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
				xmlCas.append("<INVSTS>D</INVSTS>");
				xmlCas.append("<ORDQTY>"+exdacb.getCantidad()+"</ORDQTY>");
				xmlCas.append("<CARCOD>CASERITA</CARCOD>");
				xmlCas.append("<UNTPAK>0</UNTPAK>");
				xmlCas.append("<UNTPAL>0</UNTPAL>");
				xmlCas.append("<UNTCAS>0</UNTCAS>");
				xmlCas.append("<LATE_SHPDTE>"+fecha+"000000"+"</LATE_SHPDTE>");//verificar por el momento se enviara el mismo dia del pedido
				xmlCas.append("<SRVLVL>1</SRVLVL>");
				xmlCas.append("<DSTARE>SDESP</DSTARE>");
				xmlCas.append("</ORDER_LINE_SEG>");
				cantidad++;
			}	
			
		
		xmlCas.append("</CTRL_SEG>");  
		xmlCas.append("</VC_ORDER_INB_IFD>");  
		//xmlCas.append("</PART_INB_IFD>");
		resp = xmlCas.toString();
			
		return resp;
	}
	
	public String formaXMLPedido2(String tipoAccion, int codigo, String dv, String TipoPedCarguio, int fecha, int cantidadPedido, int correlativo){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		
		//xmlCas.append("<PART_INB_IFD>");
		xmlCas.append("<VC_ORDER_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
		xmlCas.append("<TRNNAM>ORDER_TRAN</TRNNAM>");
		xmlCas.append("<TRNVER>8.2</TRNVER>");
		xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		
		xmlCas.append("<ORDER_SEG>");
		xmlCas.append("<SEGNAM>ORDER</SEGNAM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		
		xmlCas.append("<ORDNUM>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</ORDNUM>");
		xmlCas.append("<ORDTYP>"+TipoPedCarguio.trim()+"</ORDTYP>");
		xmlCas.append("<ENTDTE>"+fecha+"000000"+"</ENTDTE>");
		xmlCas.append("<STCUST>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</STCUST>");
		xmlCas.append("<ST_HOST_ADR_ID>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</ST_HOST_ADR_ID>");
		xmlCas.append("<RTCUST>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</RTCUST>");
		xmlCas.append("<RT_HOST_ADR_ID>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</RT_HOST_ADR_ID>");
		xmlCas.append("<BTCUST>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</BTCUST>");
		xmlCas.append("<BT_HOST_ADR_ID>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</BT_HOST_ADR_ID>");
		
		xmlCas.append("<SHIPBY>RTCUST</SHIPBY>");
		xmlCas.append("<WAVE_FLG>1</WAVE_FLG>");
		xmlCas.append("</ORDER_SEG>");
		
		//Iterator car = carguio.getCarguioD().iterator();
		
			
				xmlCas.append("<ORDER_LINE_SEG>");
				
				xmlCas.append("<SEGNAM>ORDER_LINE</SEGNAM>");
				xmlCas.append("<ORDNUM>"+"MENCOMB"+"-"+codigo+"-"+fecha+"000000"+"</ORDNUM>");
				xmlCas.append("<ORDLIN>1</ORDLIN>");
				xmlCas.append("<ORDSLN>S1</ORDSLN>");
				xmlCas.append("<PRTNUM>"+codigo+"</PRTNUM>");
				
				xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
				xmlCas.append("<INVSTS>D</INVSTS>");
				xmlCas.append("<ORDQTY>"+cantidadPedido+"</ORDQTY>");
				xmlCas.append("<CARCOD>CASERITA</CARCOD>");
				xmlCas.append("<UNTPAK>0</UNTPAK>");
				xmlCas.append("<UNTPAL>0</UNTPAL>");
				xmlCas.append("<UNTCAS>0</UNTCAS>");
				xmlCas.append("<LATE_SHPDTE>"+fecha+"000000"+"</LATE_SHPDTE>");//verificar por el momento se enviara el mismo dia del pedido
				xmlCas.append("<SRVLVL>1</SRVLVL>");
				xmlCas.append("<DSTARE>SDESP</DSTARE>");
				xmlCas.append("</ORDER_LINE_SEG>");
			
			
		
		xmlCas.append("</CTRL_SEG>");  
		xmlCas.append("</VC_ORDER_INB_IFD>");  
		//xmlCas.append("</PART_INB_IFD>");
		resp = xmlCas.toString();
			
		return resp;
	}
	
	
	public String formaXMLOrdenes(List articulos, String tipoAccion, int codigo, String dv, String TipoPedCarguio, int fecha){
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
				xmlCas.append("<INVNUM>"+codigo+"</INVNUM>");
				xmlCas.append("<SUPNUM>"+codigo+"</SUPNUM>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<RIMSTS>OPEN</RIMSTS>");
				xmlCas.append("<INVTYP>COM</INVTYP>");
				xmlCas.append("<INVDTE>"+fecha+"</INVDTE>");
			
				Iterator iter = articulos.iterator();
				int cantidad=0;
				while (iter.hasNext()){
					ExdacbDTO dto = (ExdacbDTO) iter.next();
			
					xmlCas.append("<LINE_SEG>");
						xmlCas.append("<SEGNAM>LINE_SEG</SEGNAM>");
						xmlCas.append("<INVLIN>"+cantidad+"</INVLIN>");
						xmlCas.append("<INVSLN>0000</INVSLN>");
						xmlCas.append("<EXPQTY>"+dto.getCantidad()+"</EXPQTY>");
						xmlCas.append("<PRTNUM>"+dto.getCodigoArticuloHijo()+"</PRTNUM>");
						xmlCas.append("<ORGCOD>----</ORGCOD>");
						xmlCas.append("<REVLVL>----</REVLVL>");
						xmlCas.append("<LOTNUM>----</LOTNUM>");
						xmlCas.append("<RCVSTS>D</RCVSTS>");//DEFINIR ESTADO INVENTARIO PARA COMBOS
					xmlCas.append("</LINE_SEG>");
					cantidad++;
				}
				
			xmlCas.append("</HEADER_SEG>");
		xmlCas.append("</CTRL_SEG>");
		xmlCas.append("</VC_RA_INB_IFD>");

		
		
		resp = xmlCas.toString();  
		
		return resp;
	}
	
}
