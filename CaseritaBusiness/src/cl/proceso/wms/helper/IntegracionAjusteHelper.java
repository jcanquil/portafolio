package cl.caserita.proceso.wms.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.DetswmsDAO;
import cl.caserita.dao.iface.EncswmsDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.DetswmsDTO;
import cl.caserita.dto.EncswmsDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.VecfwmsDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.wms.helper.IntegraPrmprvHelper;
import cl.caserita.wms.integracion.helper.ActualizaStockInventarioHelper;
import sun.misc.IOUtils;

public class IntegracionAjusteHelper {
	private  static Logger logi = Logger.getLogger(IntegracionAjusteHelper.class);

	public void procesaAjuste(String urlFile, String nameFile){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse(fXmlFile);
			
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			AjusteDTO ajuste = null;
			doc.getDocumentElement().normalize();
			DAOFactory dao = DAOFactory.getInstance();
			VecmarDAO vecmar = dao.getVecmarDAO();
			VedmarDAO vedmar = dao.getVedmarDAO();
			ExmarbDAO exmarb = dao.getExmarbDAO();
			TpacorDAO tpacor = dao.getTpacorDAO();
			VecfwmsDAO vecfwms = dao.getVecfwmsDAO();
			StockinventarioDAO stock = dao.getStockinventarioDAO();
			
			Fecha fch = new Fecha();
			int correlativo=0;
			int correlativo2=0;
			VedmarDTO vedmarDTO = new VedmarDTO();

			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
					
			NodeList nList = doc.getElementsByTagName("INVENTORY_ADJUST_IFD");
			int inser=0;		
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					logi.info("ADJ_REF1 : " + eElement.getElementsByTagName("ADJ_REF1").item(0).getTextContent());

					logi.info("ADJ_REF2 : " + eElement.getElementsByTagName("ADJ_REF2").item(0).getTextContent());
					logi.info("INVSTS : " + eElement.getElementsByTagName("INVSTS").item(0).getTextContent());
					logi.info("LOTNUM : " + eElement.getElementsByTagName("LOTNUM").item(0).getTextContent());
					/*if (eElement.getElementsByTagName("READCOD").item(0).getTextContent()!=null){
						logi.info("READCOD : " + eElement.getElementsByTagName("READCOD").item(0).getTextContent());

					}*/

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("ADJQTY : " + eElement.getElementsByTagName("ADJQTY").item(0).getTextContent());
					logi.info("CATCH_QTY : " + eElement.getElementsByTagName("CATCH_QTY").item(0).getTextContent());
					inser++;
					 correlativo = tpacor.recupeCorrelativo(0, 9999);
					 correlativo2 = tpacor.recupeCorrelativo(0, 9);

					vedmarDTO.setCodigoEmpresa(2);
					vedmarDTO.setCodTipoMvto(7);
					vedmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vedmarDTO.setNumDocumento(correlativo);
					vedmarDTO.setCorrelativo(0);
					vedmarDTO.setCodigoBodega(26);
					vedmarDTO.setCodigoArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(26, vedmarDTO.getCodigoArticulo());
					vedmarDTO.setDigArticulo(exmarbDTO.getDvArticulo().trim());
					vedmarDTO.setFormato("U");
					vedmarDTO.setCantidadArticulo(Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent()));
					vedmarDTO.setCantidadFormato(Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent()));
					
					if (vedmarDTO.getCantidadArticulo()<0){
						vedmarDTO.setCantidadArticulo(vedmarDTO.getCantidadArticulo()*(0-1));
						vedmarDTO.setCantidadArticulo(vedmarDTO.getCantidadFormato()*(0-1));
					}
					vedmarDTO.setSectorBodega(exmarbDTO.getCodigoSector());
					vedmarDTO.setPesoLinea(1000);
					vedmarDTO.setPrecioUnidad(exmarbDTO.getCostoBruto());
					vedmarDTO.setPrecioNeto(exmarbDTO.getCostoNeto());
					vedmarDTO.setCostoNeto(exmarbDTO.getCostoNeto());
					vedmarDTO.setCostoTotalNeto(exmarbDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoBrutoLinea(vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoTotalLinea((int)vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoNeto(vedmarDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoTotalNetoLinea((int)vedmarDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoExento(0);//VERIFICAR FUNCIONALIDAD PARA EXENTOS
					vedmarDTO.setCodIngresoSalida("I");
					vedmar.generaMovimiento(vedmarDTO);
					VecfwmsDTO vecfwmsDTO = new VecfwmsDTO();
					
					
				}
				if (inser>0){
					VecmarDTO vecmarDTO = new VecmarDTO();
					vecmarDTO.setCodigoEmpresa(2);
					vecmarDTO.setCodTipoMvto(7);
					vecmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setNumDocumento(correlativo);
					vecmarDTO.setCodigoDocumento(9);
					vecmarDTO.setNumeroTipoDocumento(correlativo2);
					vecmarDTO.setFechaDocumento(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setBodegaOrigen(26);
					vecmarDTO.setCantidadLineaDetalle(1);
					vecmarDTO.setTotalNeto(vedmarDTO.getMontoTotalNetoLinea());
					vecmarDTO.setTotalImptoAdicional(0);
					vecmarDTO.setTotalIva(0);
					vecmarDTO.setTotalDocumento(vedmarDTO.getMontoTotalLinea());
					vecmarDTO.setPesoTotalMovto(0);
					vecmarDTO.setCodigoJefeLocal(50);
					vecmar.generaMovimiento(vecmarDTO);
				}
			}
			//Mueva archivo a carpeta de procesados
			moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void procesaAjuste2(String urlFile, String nombreArchivo){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			int fechaReal =0;
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			DAOFactory dao = DAOFactory.getInstance();
			Fecha fch = new Fecha();
			EncswmsDAO encDAO = dao.getEncswmsDAO();
			DetswmsDAO detDAO = dao.getDetswmsDAO();
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			logi.info("Procesa Elemento:" );
			
			NodeList nList = doc.getElementsByTagName("INVENTORY_ADJUST_IFD");
			int inser=0;		
			logi.info("----------------------------");
			int numero=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					logi.info("ADJ_REF1 : " + eElement.getElementsByTagName("ADJ_REF1").item(0).getTextContent());

					logi.info("ADJ_REF2 : " + eElement.getElementsByTagName("ADJ_REF2").item(0).getTextContent());
					logi.info("INVSTS : " + eElement.getElementsByTagName("INVSTS").item(0).getTextContent());
					logi.info("LOTNUM : " + eElement.getElementsByTagName("LOTNUM").item(0).getTextContent());
					/*if (eElement.getElementsByTagName("READCOD").item(0).getTextContent()!=null){
						logi.info("READCOD : " + eElement.getElementsByTagName("READCOD").item(0).getTextContent());

					}*/

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("ADJQTY : " + eElement.getElementsByTagName("ADJQTY").item(0).getTextContent());
					logi.info("CATCH_QTY : " + eElement.getElementsByTagName("CATCH_QTY").item(0).getTextContent());
					inser++;
					fechaReal = Integer.parseInt(eElement.getElementsByTagName("TRNDTE").item(0).getTextContent().substring(0, 8));
					 int cantidad=Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent());
					 String ingreso="";
					/* if (cantidad>0){
						 tipoMov=7;
						 ingreso="I";
					 }else{
						 tipoMov=28;
						 ingreso="S";
					 }*/
					
					 
					 DetswmsDTO detDTO = new DetswmsDTO();
					 detDTO.setCodigoEmpresa(2);
					 detDTO.setCodigoTipoSalida(1);
					 detDTO.setArchivoXML(nombreArchivo);
					 detDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
					 detDTO.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
					 detDTO.setCantidad(cantidad);
					 detDTO.setCodigoArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					 detDTO.setFechaRealGeneracion(fechaReal);
					 detDTO.setCodigoEstadoInventario(eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim());
					 detDTO.setNumeroDetalle(numero);
					 detDAO.generaDetalle(detDTO);
					//IF 7 suma y 28 resta solo cuando es D
					logi.info("ESTADO :"+eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim());
					numero++;
					
					
					
				}
				
			}
			//Mueva archivo a carpeta de procesados
			 EncswmsDTO enc = new EncswmsDTO();
			 enc.setCodigoEmpresa(2);
			 enc.setCodigoTipoSalida(1);
			 enc.setDescripcionTipoSalida("AJUSTES INVENTARIO");
			 enc.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
			 enc.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
			 enc.setArchivoXML(nombreArchivo);
			 enc.setFechaRealGeneracion(fechaReal);
			 encDAO.generaEncabezado(enc);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	public static void main(String []args){
		IntegracionAjusteHelper helper = new IntegracionAjusteHelper();
		helper.procesaAjuste2("/home2/ftp/in/INV_RCV_3748.xml", "INV_RCV_3748.xml");
		/*List lista = new ArrayList();
		helper.procesaFacturacion(2, 26, 13183,lista ,"");*/
				
	}
	public int moveFile(String urlFile, String nameFile){
		int numero=0;
		Path origenPath = FileSystems.getDefault().getPath(urlFile);
	    Path destinoPath = FileSystems.getDefault().getPath("/home2/ftp/proc/"+nameFile);


	    try {
	        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	    
	    
		return numero;
	}
}
