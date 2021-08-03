package cl.caserita.wms.out.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.emailInformacionErroresWMS;
import cl.caserita.enviomail.main.enviaMailStockNegativo;
import cl.caserita.wms.integracion.helper.ActualizaStockInventarioHelper;

public class IntegracionCambioEstadoHelper {
	private  static Logger logi = Logger.getLogger(IntegracionCambioEstadoHelper.class);
	private static emailInformacionErroresWMS email = new emailInformacionErroresWMS();
	private static enviaMailStockNegativo mailNegativo = new enviaMailStockNegativo();

	public void procesaCambioEstado(String urlFile, String nombreArchivo){
		String estado="";
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
			DAOFactory dao = DAOFactory.getInstance();
			ExmartDAO exmartDAO = dao.getExmartDAO();
			
			StockinventarioDAO stockDAO = dao.getStockinventarioDAO();
			ActualizaStockInventarioHelper actualiza = new ActualizaStockInventarioHelper();
			VecfwmsDAO vecfwms = dao.getVecfwmsDAO();
			ExmarbDAO exmarbDAO = dao.getExmarbDAO();
			int tipoMov=0;
			StockinventarioDAO stock = dao.getStockinventarioDAO();
			
			Fecha fch = new Fecha();
			int correlativo=0;
			int correlativo2=0;
			double stocklinea=0;
			int correlativoDetalle=0;
			VedmarDTO vedmarDTO = new VedmarDTO();
			logi.info(" INICIO CAMBIO ESTADO INVENTARIO : "+ nombreArchivo);
			//logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			//logi.info("Procesa Elemento:" );

			NodeList nList = doc.getElementsByTagName("INVENTORY_STATUS_CHANGE_IFD_SEG");
			int inser=0;		
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				//logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					
					logi.info("CHGQTY : " + eElement.getElementsByTagName("CHGQTY").item(0).getTextContent());
					logi.info("FR_INVSTS : " + eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent());
					logi.info("TO_INVSTS : " + eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent());

					ExmartDTO dto = exmartDAO.recuperaArticuloSinDigito(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					
					logi.info("DISMINUYE STOCK INVENTARIO");
					actualiza.actualizaEstado(2, 26, eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent(), Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()),dto.getDvArticulo().trim(), 
							Integer.parseInt(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent()), stockDAO, nombreArchivo, correlativoDetalle);
					
					if (eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent() != eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent()){
						logi.info("ENTRA a AUMENTA STOCK");
						actualiza.actualizaEstadoxEstado(2, 26,  eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent()
								,Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()), dto.getDvArticulo().trim(),
								Integer.parseInt(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent()), stockDAO, nombreArchivo, correlativoDetalle);
					}
					
					
					if ("D".equals(eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent()) ){
						logi.info("ACTUALIZA STOCK EN LINEA");
						ExmarbDTO exmarbDTO = exmarbDAO.recuperaArticulo(26, dto.getCodigoArticulo());
						//TO SUMA
						//FROM BAJA
						//AGREGAR IF PARA QUE NO QUEDE NEGATIVO STOCK EN LINEA SI LA CANTIDAD ES INFERIOR
						
						/*
						if (exmarbDTO.getStockLinea() > Double.parseDouble(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent())){
							stocklinea=exmarbDTO.getStockLinea() - Double.parseDouble(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent());
						}
						else{
							stocklinea=exmarbDTO.getStockLinea() - Double.parseDouble(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent());
						}
						*/
						
						stocklinea=exmarbDTO.getStockLinea() - Double.parseDouble(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent());
						int stockLineaArt=exmarbDTO.getStockLinea()+vedmarDTO.getCantidadArticulo();
						if (stockLineaArt<0){
							//MAIL
							//mailNegativo.envioMail("Procesa CAMBIO ESTADO 128 :"+"NOMBRE ARCHIVO :"+ nombreArchivo +" ARTICULO : "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ vedmarDTO.getCantidadArticulo()+" STOCK AHORA : "+stockLineaArt);
						}
						exmarbDAO.actualizaStockLinea(26, dto.getCodigoArticulo(), dto.getDvArticulo(), stocklinea);
						
						logi.info("EL STOCK EN LINEA ACTUALIZADO ES : "+stocklinea);
						
					}else if ("D".equals(eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent())){
						ExmarbDTO exmarbDTO = exmarbDAO.recuperaArticulo(26, dto.getCodigoArticulo());
						//AGREGAR IF PARA QUE NO QUEDE NEGATIVO STOCK EN LINEA SI LA CANTIDAD ES INFERIOR
						
						stocklinea=Double.parseDouble(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent())+exmarbDTO.getStockLinea();
						int stockLineaArt=exmarbDTO.getStockLinea()+vedmarDTO.getCantidadArticulo();
						if (stockLineaArt<0){
							//MAIL
							//mailNegativo.envioMail("Procesa CAMBIO ESTADO 142 :"+"NOMBRE ARCHIVO :"+ nombreArchivo + " ARTICULO : "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ vedmarDTO.getCantidadArticulo()+" STOCK AHORA : "+stockLineaArt);
						}
						exmarbDAO.actualizaStockLinea(26, dto.getCodigoArticulo(), dto.getDvArticulo(), stocklinea);
						
						logi.info("EL STOCK EN LINEA ACTUALIZADO ES : "+stocklinea);
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
	}
	public static void main(String []args){
		IntegracionCambioEstadoHelper helper = new IntegracionCambioEstadoHelper();
		helper.procesaCambioEstado("/home2/ftp/in/INV_STS_CHA_4100.xml", "INV_STS_CHA_4100.xml");
		/*List lista = new ArrayList();
		helper.procesaFacturacion(2, 26, 13183,lista ,"");*/
				
	}
}
