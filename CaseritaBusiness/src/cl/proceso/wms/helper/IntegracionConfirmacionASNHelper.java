package cl.caserita.proceso.wms.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CargfwmsDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.CarswmsDAO;
import cl.caserita.dao.iface.ChoftranDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.ConsolidaasnDAO;
import cl.caserita.dao.iface.DocconfcDAO;
import cl.caserita.dao.iface.EncswmsDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.OrdswmsDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.StockdifDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.CargfwmsDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarswmsDTO;
import cl.caserita.dto.ChoftranDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.ConsolidaasnDTO;
import cl.caserita.dto.DocconfcDTO;
import cl.caserita.dto.EncswmsDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.OrdswmsDTO;
import cl.caserita.dto.RutservDTO;
import cl.caserita.dto.StockdifDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public class IntegracionConfirmacionASNHelper {
	private static Logger logi = Logger.getLogger(IntegracionConfirmacionASNHelper.class);

	public void procesaReconciliacion(String urlFile, String nameFile, String tipo){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			//Document doc = dBuilder.parse(fXmlFile);
			
			///*
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			//*/
			
			AjusteDTO ajuste = null;
			doc.getDocumentElement().normalize();
			int numeroCarguio=0;
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			if ("CIERRE".equals(tipo)){
				NodeList nList = doc.getElementsByTagName("RCV_TRLR_CLOSE_IFD_VC");
				
				logi.info("----------------------------");
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					logi.info("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
						Element eElement = (Element) nNode;
						

						logi.info("TRKNUM : " + eElement.getElementsByTagName("TRKNUM").item(0).getTextContent());
						logi.info("YARD_LOC : " + eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent());
						numeroCarguio=Integer.parseInt(eElement.getElementsByTagName("TRKNUM").item(0).getTextContent().substring(3, eElement.getElementsByTagName("TRKNUM").item(0).getTextContent().length()));
						
						String andenRecepcion=eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent();
						int fechareal = Integer.parseInt(eElement.getElementsByTagName("CLOSE_DTE").item(0).getTextContent().substring(0, 8));
						procesaCobro( numeroCarguio, nameFile, andenRecepcion, fechareal);

					}
				}
			}else{
				NodeList nList = doc.getElementsByTagName("INVENTORY_RECEIPT_IFD_SEG");
				
				logi.info("----------------------------");
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					logi.info("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
						Element eElement = (Element) nNode;
						

						logi.info("TRKNUM : " + eElement.getElementsByTagName("TRKNUM").item(0).getTextContent());
						

						logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
						
						logi.info("RCVQTY : " + eElement.getElementsByTagName("RCVQTY").item(0).getTextContent());
						
						logi.info("RCVSTS : " + eElement.getElementsByTagName("RCVSTS").item(0).getTextContent());
						logi.info("EXPQTY : " + eElement.getElementsByTagName("EXPQTY").item(0).getTextContent());
						
						detalleDTO.setCodArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
						detalleDTO.setCantidad(Integer.parseInt(eElement.getElementsByTagName("RCVQTY").item(0).getTextContent()));
						detalleDTO.setCantidadDespachada(Integer.parseInt(eElement.getElementsByTagName("EXPQTY").item(0).getTextContent()));
						
						StringTokenizer st = new StringTokenizer(eElement.getElementsByTagName("TRKNUM").item(0).getTextContent(),"-");
						int campo=0;
						
						String correlativo="";
						 while (st.hasMoreTokens( )){
							 
							 	String tr = st.nextToken();
							 	logi.info(tr);
							 	if (campo==0){
							 		numeroCarguio=Integer.parseInt(tr.substring(3, tr.length()));
							 	}else if(campo==1){
							 		correlativo=tr;
							 	}
							 campo++;
					        
					    }
						 

					}
				}
			}
			
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void procesaCobro(int carguio, String nombreArchivo, String andenRecep, int fechaReal){
		DAOFactory dao = DAOFactory.getInstance();
		CarguioDAO carguioDAO = dao.getCarguioDAO();
		
		ConsolidaasnDAO consolida = dao.getConsolidaasnDAO();
		EncswmsDAO encDAO = dao.getEncswmsDAO();
		CarswmsDAO carDAO = dao.getCarswmsDAO();
		OrdswmsDAO ordDAO = dao.getOrdswmsDAO();
		Fecha fch = new Fecha();

		//Recupera EndPoint facturacion electronica
		EncswmsDTO enc = new EncswmsDTO();
		 enc.setCodigoEmpresa(2);
		 enc.setCodigoTipoSalida(4);
		 enc.setDescripcionTipoSalida("CONFIRMACION COBRO TRANSPORTISTA");
		 enc.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
		 enc.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
		 enc.setArchivoXML(nombreArchivo);
		 enc.setFechaRealGeneracion(fechaReal);
		 encDAO.generaEncabezado(enc);
		
		int rut=0;
		
		
		List lista = consolida.recuperaConsolidadoCompleto(2, carguio, 26);
		CarguioDTO carguioDTO = carguioDAO.obtieneCarguioDTO(2, carguio, 26);
		
		
		VecmarDTO vecmar = null;
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			ConsolidaasnDTO dto = (ConsolidaasnDTO) iter.next();
			//Genera Boleta
			//Verifica si tiene documento  generado
			if (rut==0){
				CarswmsDTO carDTO = new CarswmsDTO();
				carDTO.setCodigoEmpresa(2);
				carDTO.setCodigoTipoSalida(4);
				carDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
				carDTO.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
				carDTO.setArchivoXML(nombreArchivo);
				carDTO.setNumeroCarguio(dto.getNumeroCarguio());
				carDTO.setTransferenciaNumeroCarguio(0);
				
				carDAO.generaDetalle(carDTO);
				rut++;
			}
			OrdswmsDTO ordDTO = new OrdswmsDTO();
			ordDTO.setCodigoEmpresa(2);
			ordDTO.setCodigoTipoSalida(4);
			ordDTO.setArchivoXML(nombreArchivo);
			ordDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
			ordDTO.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
			ordDTO.setTipoMovimiento(4);
			ordDTO.setNumeroCarguio(dto.getNumeroCarguio());
			ordDTO.setTransferenciaNumeroCarguio(0);
			ordDTO.setNumeroOrdenVenta(0);
			ordDTO.setCodigoArticulo(dto.getCodigoArticulo());
			ordDTO.setCantidadArticulo(dto.getCantidad());
			ordDTO.setCantidadRealArticulo(dto.getCantidad());
			ordDTO.setCodigoBodega(26);
			ordDTO.setFechaRealGeneracion(fechaReal);
			ordDAO.generaOrden(ordDTO);
			 }
			 
			
		
		
		
	}
	public static void main (String []args){
		IntegracionConfirmacionASNHelper helper = new IntegracionConfirmacionASNHelper();
		//helper.procesaReconciliacion("/home2/ftp/out/RCV_TRLR_CLO_4715.xml", "RCV_TRLR_CLO_4715.xml", "CIERRE");
		DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmar = dao.getVedmarDAO();
		int numero = vedmar.obtenerCorrelativo(2, 21, 20160920, 18000598);
		System.out.println("Correlativo:"+numero);
	}
}
