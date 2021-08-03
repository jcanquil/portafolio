package cl.caserita.proceso.wms.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ConsolidaasnDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.ConsolidaasnDTO;
import cl.caserita.dto.StockinventarioDTO;

public class IntegracionConfirmaComboHelper {
	private static Logger logi = Logger.getLogger(IntegracionConfirmaComboHelper.class);

	public void procesaReconciliacion(String urlFile, String nameFile){
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
			int numeroCarguio=0;
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
					
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
					detalleDTO.setCantidad(Integer.parseInt(eElement.getElementsByTagName("EXPQTY").item(0).getTextContent()));
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
					 
					 procesaASN(detalleDTO, numeroCarguio);

				}
			}
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void procesaASN(ConfirmacionCarguioDetalleDTO dto, int carguio){
		DAOFactory dao = DAOFactory.getInstance();
		ConsolidaasnDAO consolida = dao.getConsolidaasnDAO();
		ConsolidaasnDTO consolidaDTO = consolida.recuperaConsolidado(2, carguio, 26, dto.getCodArticulo());
		if (consolidaDTO.getCodigoArticulo()!=dto.getCantidad()){
			logi.info("PROCESA ACTUALIZACION CONSOLIDADO");
			consolidaDTO.setCantidadConfirmada(dto.getCantidad());
			consolidaDTO.setCantidadDiferencia(consolidaDTO.getCantidad()-dto.getCantidad());
			consolida.actualizaConsolidado(consolidaDTO);
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
