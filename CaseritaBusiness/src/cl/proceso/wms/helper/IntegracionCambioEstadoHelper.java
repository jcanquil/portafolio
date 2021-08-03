package cl.caserita.proceso.wms.helper;

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
import cl.caserita.dao.iface.DetswmsDAO;
import cl.caserita.dao.iface.EncswmsDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.DetswmsDTO;
import cl.caserita.dto.EncswmsDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.wms.integracion.helper.ActualizaStockInventarioHelper;

public class IntegracionCambioEstadoHelper {
	private  static Logger logi = Logger.getLogger(IntegracionCambioEstadoHelper.class);

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
			Fecha fch = new Fecha();
			EncswmsDAO encDAO = dao.getEncswmsDAO();
			DetswmsDAO detDAO = dao.getDetswmsDAO();			
			
			int tipoMov=0;
			
			

			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			logi.info("Procesa Elemento:" );

			NodeList nList = doc.getElementsByTagName("INVENTORY_STATUS_CHANGE_IFD_SEG");
			int inser=0;		
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					
					logi.info("CHGQTY : " + eElement.getElementsByTagName("CHGQTY").item(0).getTextContent());
					logi.info("FR_INVSTS : " + eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent());
					logi.info("TO_INVSTS : " + eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent());

					
					logi.info("DISMINUYE STOCK INVENTARIO");
					EncswmsDTO enc = new EncswmsDTO();
					 enc.setCodigoEmpresa(2);
					 enc.setCodigoTipoSalida(2);
					 enc.setDescripcionTipoSalida("CAMBIO ESTADO INVENTARIO");
					 enc.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
					 enc.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
					 enc.setArchivoXML(nombreArchivo);
					 enc.setFechaRealGeneracion(Integer.parseInt(eElement.getElementsByTagName("TRNDTE").item(0).getTextContent().substring(0, 8)));
					 encDAO.generaEncabezado(enc);
					 
					 DetswmsDTO detDTO = new DetswmsDTO();
					 detDTO.setCodigoEmpresa(2);
					 detDTO.setCodigoTipoSalida(2);
					 detDTO.setArchivoXML(nombreArchivo);
					 detDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
					 detDTO.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
					 detDTO.setCodigoEstadoInventario(eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent());
					 detDTO.setProximoEstadoInvenatrio(eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent());
					 detDTO.setCantidad(Integer.parseInt(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent()));
					 detDTO.setCodigoArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					 detDTO.setFechaRealGeneracion((Integer.parseInt(eElement.getElementsByTagName("TRNDTE").item(0).getTextContent().substring(0, 8))));
					 detDAO.generaDetalle(detDTO);
					 
				
					
					
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String []args){
		IntegracionCambioEstadoHelper helper = new IntegracionCambioEstadoHelper();
		helper.procesaCambioEstado("/home2/ftp/in/INV_STS_CHA_4100.xml", "INV_STS_CHA_4100.xml");
		/*List lista = new ArrayList();
		helper.procesaFacturacion(2, 26, 13183,lista ,"");*/
				
	}
}
