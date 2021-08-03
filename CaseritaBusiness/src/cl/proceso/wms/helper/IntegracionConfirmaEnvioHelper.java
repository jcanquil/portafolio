package cl.caserita.proceso.wms.helper;

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

import cl.caserita.dto.AjusteDTO;

public class IntegracionConfirmaEnvioHelper {
	private static Logger logi = Logger.getLogger(IntegracionConfirmaEnvioHelper.class);

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
					
			NodeList nList = doc.getElementsByTagName("INVENTORY_RECONCILIATION_SEG");
					
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					logi.info("TRNDTE : " + eElement.getElementsByTagName("TRNDTE").item(0).getTextContent());

					logi.info("TRNNUM : " + eElement.getElementsByTagName("TRNNUM").item(0).getTextContent());
					logi.info("INVSTS : " + eElement.getElementsByTagName("INVSTS").item(0).getTextContent());
					/*if (eElement.getElementsByTagName("READCOD").item(0).getTextContent()!=null){
						logi.info("READCOD : " + eElement.getElementsByTagName("READCOD").item(0).getTextContent());

					}*/

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("UNTQTY : " + eElement.getElementsByTagName("UNTQTY").item(0).getTextContent());
					logi.info("HSTACC : " + eElement.getElementsByTagName("HSTACC").item(0).getTextContent());

				}
			}
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
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
