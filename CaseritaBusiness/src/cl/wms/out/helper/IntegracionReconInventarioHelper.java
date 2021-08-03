package cl.caserita.wms.out.helper;

import java.io.File;
import java.io.IOException;
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

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.StockinventarioDTO;

public class IntegracionReconInventarioHelper {
	private static Logger logi = Logger.getLogger(IntegracionReconInventarioHelper.class);

	public void procesaReconciliacion(String urlFile, String nameFile){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			AjusteDTO ajuste = null;
			doc.getDocumentElement().normalize();

			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
					
			NodeList nList = doc.getElementsByTagName("INVENTORY_STATUS_CHANGE_ID");
					
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
					Element eElement = (Element) nNode;
					logi.info("TRNDTE : " + eElement.getElementsByTagName("TRNDTE").item(0).getTextContent());

					logi.info("TRNNUM : " + eElement.getElementsByTagName("TRNNUM").item(0).getTextContent());
					

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("CHGQTY : " + eElement.getElementsByTagName("CHGQTY").item(0).getTextContent());
					logi.info("SUBNUM : " + eElement.getElementsByTagName("SUBNUM").item(0).getTextContent());
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("FR_INVSTS : " + eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent());

					logi.info("TO_INVSTS : " + eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent());

					logi.info("REACOD : " + eElement.getElementsByTagName("REACOD").item(0).getTextContent());
					logi.info("TRNDTE : " + eElement.getElementsByTagName("TRNDTE").item(0).getTextContent());
					detalleDTO.setCodArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					detalleDTO.setCantidad(Integer.parseInt(eElement.getElementsByTagName("CHGQTY").item(0).getTextContent()));
					detalleDTO.setUnidad(eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					detalleDTO.setEstadoInventario(eElement.getElementsByTagName("FR_INVSTS").item(0).getTextContent());
					detalleDTO.setEstadoInventarioNuevo(eElement.getElementsByTagName("TO_INVSTS").item(0).getTextContent());
					procesaInventario(detalleDTO);

				}
			}
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void procesaInventario(ConfirmacionCarguioDetalleDTO dto){
		DAOFactory dao = DAOFactory.getInstance();
		StockinventarioDAO stock = dao.getStockinventarioDAO();
		StockinventarioDTO stockDTO = stock.lista(2,26,dto.getEstadoInventario().trim(),dto.getCodArticulo());
		StockinventarioDTO stockDTO2 = stock.lista(2,26,dto.getEstadoInventarioNuevo().trim(),dto.getCodArticulo());
		if ("UN".equals(dto.getUnidad())){
			int stockAntiguo=0;
			int stockActual=0;
			if (dto.getCantidad()>stockDTO.getCantidad()){
				stockAntiguo=dto.getCantidad()-stockDTO.getCantidad();
			}else{
				stockAntiguo=stockDTO.getCantidad()-dto.getCantidad();
			}
			stockActual=dto.getCantidad()+stockDTO.getCantidad();
			stockDTO.setCantidad(stockAntiguo);
			stockDTO2.setCantidad(stockActual);
			stock.actualizaStock(stockDTO);
			stock.actualizaStock(stockDTO2);
			
		}else if ("DP".equals(dto.getUnidad())){
			
		}else if ("CJ".equals(dto.getUnidad())){
			
		}else if ("PA".equals(dto.getUnidad())){
			
		}
		
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
