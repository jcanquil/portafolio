package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmodcDAO;
import cl.caserita.dao.iface.ExmrecvaDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.ExmrecvaDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegracionExmrecvaHelper {
	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegracionExmrecvaHelper.class);

		public static void main(String[] args) {
		// TODO Auto-generated method stub
			
			IntegracionExmrecvaHelper helper = new IntegracionExmrecvaHelper();
			//helper.integracionAll();
			helper.integracion(2, 396529, 33, 454414, 20160714, 26, "OC", "A,PC,PC,JORGE");
			//helper.integracion(2, 58079, 38, 1088, 20160729, 1, "OT", "A,PC,PC,JORGE");
		}
		
		public void integracion(int codEmpresa, int numOC, int tipDocto, int numDocto, int fecDocto, int codBodega, String tipo, String accion){
			DAOFactory dao = DAOFactory.getInstance();
			ExmrecvaDAO exmrecva = dao.getExmrecvaDAO();
			ExmodcDAO exmodc = dao.getExmodcDAO();
			EnviaFTPWMS ftp = new EnviaFTPWMS();
			FtpprovDAO ftpDAO = dao.getFtpprovDAO();
			ExmartDAO exmartDAO = dao.getExmartDAO();
			ExmtraDAO exmtra = dao.getExmtraDAO();
			TptempDAO tptempDAO = dao.getTptempDAO();
			ConvierteStringDTO convierte = new ConvierteStringDTO();
			IntegracionDTO integraDTO = convierte.convierte(accion);
			
			int nroOCOT=0;
			String xml="";
			
			logi.info("ENTRA A BUSCAR LA LISTA DE ARTICULOS PARA LA GENERACION DEL XML DE RECIBO OC/OT");
			List lista = exmrecva.listaExmrecva(codEmpresa, numOC, tipDocto, numDocto, fecDocto, codBodega, tipo);
			
			if ("OC".equals(tipo.trim())){
				logi.info("ENTRA CREACION DE RECIBO PARA XML OC");
				ExmodcDTO exmodcdto = exmodc.buscaCabOrden(codEmpresa, numOC);
				xml = formaXML(exmodcdto, lista, integraDTO.getAccion().trim(), exmartDAO, tipDocto, numDocto);
				nroOCOT=exmodcdto.getNumeroOrden();
			}
			
			if ("OT".equals(tipo.trim())){
				logi.info("ENTRA CREACION DE RECIBO PARA XML OT");
				TptempDTO empresa = tptempDAO.recuperaEmpresa(codEmpresa);
				
				ExmtraDTO exmtradto = exmtra.recuperaEncabezado(codEmpresa, numOC);
				xml = formaXMLOT(exmtradto, lista, integraDTO.getAccion().trim(), exmartDAO, tipDocto, numDocto, empresa);
				nroOCOT=exmtradto.getNumTraspaso();
			}
			
			String xmlEnvio = generatxt(nroOCOT,xml.trim(), tipo.trim());
			
			//Envio el XML a WMS
			if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
				
			}
			else{
				
			}
			try{
				Thread.sleep(1000);
				logi.info("tread 1000:");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public String generatxt(int numoc, String XML, String tipo){
			Fecha fch = new Fecha();
			String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 14);
			String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
			String mes = fch.getYYYYMMDD().substring(4, 6);
			//logi.info("Mes:"+mes);
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
			
			String nombreArchivo = "recibomaestro_"+tipo.trim()+"_"+numoc+"_"+fechaStr+".xml";
			String archivoLog=carpeta+"recibomaestro_"+tipo.trim()+"_"+numoc+"_"+fechaStr+".xml";
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
		public String formaXML(ExmodcDTO dto, List dtolist, String tipoAccion, ExmartDAO exmartDAO, int tipDocto, int numeroDocto){
			//Forma XML para enviar a WMS
			String resp ="";
			StringBuffer xmlCas = new StringBuffer();
			String xml="";
			Fecha fch = new Fecha();
			
			String fechaStr = fch.getYYYYMMDDHHMMSS();
			
			xmlCas.append("<VC_RCPT_INB_IFD>");
				xmlCas.append("<CTRL_SEG>");
					xmlCas.append("<TRNNAM>VC_RCPT_INB</TRNNAM>");
					xmlCas.append("<TRNVER>2012.2</TRNVER>");
					xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
					xmlCas.append("<RCPT_TRLR_SEG>");						
						xmlCas.append("<SEGNAM>RECEIPT_TRAILER</SEGNAM>");
						xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
						//xmlCas.append("<TRLR_NUM>"+dto.getRutProveedor()+dto.getDigito()+"-"+tipDocto+"-"+numeroDocto+"</TRLR_NUM>");
						xmlCas.append("<TRLR_NUM>"+dto.getRutProveedor()+tipDocto+numeroDocto+"</TRLR_NUM>");
						xmlCas.append("<CARCOD>PROVEEDOR</CARCOD>");
						xmlCas.append("<TRLR_COD>RCV</TRLR_COD>");
						xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
						xmlCas.append("<RCPT_TRUCK_SEG>");
							xmlCas.append("<SEGMAN>RECEIPT_TRUCK</SEGMAN>");
							xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
							xmlCas.append("<TRKNUM>"+dto.getRutProveedor()+tipDocto+numeroDocto+"</TRKNUM>");
							xmlCas.append("<SHPDTE>"+fechaStr+"</SHPDTE>");
							xmlCas.append("<RCPT_INVOICE_SEG>");
								xmlCas.append("<SEGNAM>RECEIPT_INVOICE</SEGNAM>");
								xmlCas.append("<TRKNUM>"+dto.getRutProveedor()+tipDocto+numeroDocto+"</TRKNUM>");
								xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
								xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
								xmlCas.append("<INVTYP>PROV</INVTYP>");
								xmlCas.append("<SUPNUM>"+dto.getRutProveedor()+dto.getDigito()+"</SUPNUM>");
								xmlCas.append("<INVNUM>"+dto.getNumeroOrden()+"</INVNUM>");
								xmlCas.append("<PO_NUM>"+dto.getNumeroOrden()+"</PO_NUM>");
								
								if (tipDocto==3||tipDocto==33){
									xmlCas.append("<SADNUM>"+numeroDocto+"</SADNUM>");
									xmlCas.append("<WAYBIL></WAYBIL>");
								}
								else{
									xmlCas.append("<SADNUM></SADNUM>");
									xmlCas.append("<WAYBIL>"+numeroDocto+"</WAYBIL>");
								}
								xmlCas.append("<ORGREF></ORGREF>");
								
								//Detalle
								Iterator iter = dtolist.iterator();
								while (iter.hasNext()){
									ExmrecvaDTO dtolista = (ExmrecvaDTO) iter.next();
									
									xmlCas.append("<RCPT_LINE_SEG>");
										xmlCas.append("<SEGNAM>RECEIPT_LINE</SEGNAM>");
										xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
										xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
										xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
										xmlCas.append("<INVLIN>"+dtolista.getLinea()+"</INVLIN>");
										xmlCas.append("<INVSLN>S"+dtolista.getLinea()+"</INVSLN>");
										
										ExmartDTO exmart = exmartDAO.recuperaArticulo(dtolista.getCodigoArticulo(), dtolista.getDigitoArticulo());
										ExdartDTO dto2 = (ExdartDTO) exmart.getCodigos().get("U");
										if (dto2!=null){
											int cantidadBP=(int)dto2.getCantBasePallets();
											exmart.setPallet((int)dto2.getCantTotalPallets());
										}
										int unidades=0;
										if ("C".equals(dtolista.getFormato())){
											if (exmart.getDisplay()>0){
												unidades=exmart.getCaja()*exmart.getDisplay()*(int)dtolista.getCantidadRecepcionada();

											}else{
												unidades=exmart.getCaja()*(int)dtolista.getCantidadRecepcionada();
											}
										}else if ("D".equals(dtolista.getFormato())){
											unidades=exmart.getDisplay()*(int)dtolista.getCantidadRecepcionada();
										}else if ("P".equals(dtolista.getFormato())){
											if (exmart.getDisplay()>0){
												unidades=(exmart.getPallet()*exmart.getCaja()*exmart.getDisplay())*(int)dtolista.getCantidadRecepcionada();
											}else{
												unidades=(exmart.getPallet()*exmart.getCaja())*(int)dtolista.getCantidadRecepcionada();
											}
										}else if ("U".equals(dtolista.getFormato())){
											unidades=(int)dtolista.getCantidadRecepcionada();
										}
										
										xmlCas.append("<PRTNUM>"+dtolista.getCodigoArticulo()+"</PRTNUM>");
										xmlCas.append("<EXPQTY>"+unidades+"</EXPQTY>");
										xmlCas.append("<RCVSTS>"+dtolista.getEstadoInventario()+"</RCVSTS>");
									xmlCas.append("</RCPT_LINE_SEG>");
								}
							xmlCas.append("</RCPT_INVOICE_SEG>");	
						xmlCas.append("</RCPT_TRUCK_SEG>");
					xmlCas.append("</RCPT_TRLR_SEG>");	
				xmlCas.append("</CTRL_SEG>");					
			xmlCas.append("</VC_RCPT_INB_IFD>");
			
			resp = xmlCas.toString();  
					
			return resp;
		}
		
		public String formaXMLOT(ExmtraDTO dto, List dtolist, String tipoAccion, ExmartDAO exmartDAO, int tipDocto, int numeroDocto, TptempDTO empresa){
			//Forma XML para enviar a WMS
			String resp ="";
			StringBuffer xmlCas = new StringBuffer();
			String xml="";
			Fecha fch = new Fecha();
			
			String fechaStr = fch.getYYYYMMDDHHMMSS();
			
			xmlCas.append("<VC_RCPT_INB_IFD>");
				xmlCas.append("<CTRL_SEG>");
					xmlCas.append("<TRNNAM>VC_RCPT_INB</TRNNAM>");
					xmlCas.append("<TRNVER>2012.2</TRNVER>");
					xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
					xmlCas.append("<RCPT_TRLR_SEG>");						
						xmlCas.append("<SEGNAM>RECEIPT_TRAILER</SEGNAM>");
						xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
						//xmlCas.append("<TRLR_NUM>"+empresa.getRut()+empresa.getDv()+"-"+tipDocto+"-"+numeroDocto+"</TRLR_NUM>");
						xmlCas.append("<TRLR_NUM>"+empresa.getRut()+tipDocto+numeroDocto+"</TRLR_NUM>");
						xmlCas.append("<CARCOD>TRASPASO</CARCOD>");
						xmlCas.append("<TRLR_COD>RCV</TRLR_COD>");
						xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
						xmlCas.append("<RCPT_TRUCK_SEG>");
							xmlCas.append("<SEGMAN>RECEIPT_TRUCK</SEGMAN>");
							xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
							//xmlCas.append("<TRKNUM>"+empresa.getRut()+empresa.getDv()+"-"+tipDocto+"-"+numeroDocto+"</TRKNUM>");
							xmlCas.append("<TRKNUM>"+empresa.getRut()+tipDocto+numeroDocto+"</TRKNUM>");
							xmlCas.append("<SHPDTE>"+fechaStr+"</SHPDTE>");
							xmlCas.append("<RCPT_INVOICE_SEG>");
								xmlCas.append("<SEGNAM>RECEIPT_INVOICE</SEGNAM>");
								//xmlCas.append("<TRKNUM>"+empresa.getRut()+empresa.getDv()+"-"+tipDocto+"-"+numeroDocto+"</TRKNUM>");
								xmlCas.append("<TRKNUM>"+empresa.getRut()+tipDocto+numeroDocto+"</TRKNUM>");
								xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
								xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
								xmlCas.append("<INVTYP>OT</INVTYP>");
								xmlCas.append("<SUPNUM>"+empresa.getRut()+empresa.getDv()+"</SUPNUM>");
								xmlCas.append("<INVNUM>"+dto.getNumTraspaso()+"</INVNUM>");
								xmlCas.append("<PO_NUM>"+dto.getNumTraspaso()+"</PO_NUM>");
								
								xmlCas.append("<SADNUM></SADNUM>");
								xmlCas.append("<WAYBIL>"+dto.getNumGuiaDespacho()+"</WAYBIL>");
								xmlCas.append("<ORGREF>"+dto.getNumTraspaso()+"</ORGREF>");
								
								//Detalle
								Iterator iter = dtolist.iterator();
								while (iter.hasNext()){
									ExmrecvaDTO dtolista = (ExmrecvaDTO) iter.next();
									
									xmlCas.append("<RCPT_LINE_SEG>");
										xmlCas.append("<SEGNAM>RECEIPT_LINE</SEGNAM>");
										xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
										xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
										xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
										xmlCas.append("<INVLIN>"+dtolista.getLinea()+"</INVLIN>");
										xmlCas.append("<INVSLN>S"+dtolista.getLinea()+"</INVSLN>");
										
										ExmartDTO exmart = exmartDAO.recuperaArticulo(dtolista.getCodigoArticulo(), dtolista.getDigitoArticulo());
										ExdartDTO dto2 = (ExdartDTO) exmart.getCodigos().get("U");
										if (dto2!=null){
											int cantidadBP=(int)dto2.getCantBasePallets();
											exmart.setPallet((int)dto2.getCantTotalPallets());
										}
										int unidades=0;
										if ("C".equals(dtolista.getFormato())){
											if (exmart.getDisplay()>0){
												unidades=exmart.getCaja()*exmart.getDisplay()*(int)dtolista.getCantidadRecepcionada();

											}else{
												unidades=exmart.getCaja()*(int)dtolista.getCantidadRecepcionada();
											}
										}else if ("D".equals(dtolista.getFormato())){
											unidades=exmart.getDisplay()*(int)dtolista.getCantidadRecepcionada();
										}else if ("P".equals(dtolista.getFormato())){
											if (exmart.getDisplay()>0){
												unidades=(exmart.getPallet()*exmart.getCaja()*exmart.getDisplay())*(int)dtolista.getCantidadRecepcionada();
											}else{
												unidades=(exmart.getPallet()*exmart.getCaja())*(int)dtolista.getCantidadRecepcionada();
											}
										}else if ("U".equals(dtolista.getFormato())){
											unidades=(int)dtolista.getCantidadRecepcionada();
										}
										
										xmlCas.append("<PRTNUM>"+dtolista.getCodigoArticulo()+"</PRTNUM>");
										xmlCas.append("<EXPQTY>"+unidades+"</EXPQTY>");
										xmlCas.append("<RCVSTS>"+dtolista.getEstadoInventario()+"</RCVSTS>");
									xmlCas.append("</RCPT_LINE_SEG>");
								}
							xmlCas.append("</RCPT_INVOICE_SEG>");	
						xmlCas.append("</RCPT_TRUCK_SEG>");
					xmlCas.append("</RCPT_TRLR_SEG>");	
				xmlCas.append("</CTRL_SEG>");					
			xmlCas.append("</VC_RCPT_INB_IFD>");
			
			resp = xmlCas.toString();  
					
			return resp;
		}
		
}
