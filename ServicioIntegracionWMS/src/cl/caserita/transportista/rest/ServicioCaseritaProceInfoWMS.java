package cl.caserita.transportista.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.StringTokenizer;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

import cl.caserita.proceso.wms.helper.IntegracionAjusteHelper;
import cl.caserita.proceso.wms.helper.IntegracionCambioEstadoHelper;
import cl.caserita.proceso.wms.helper.IntegracionConfirCamionHelper;
import cl.caserita.proceso.wms.helper.IntegracionConfirmaRecepcionHelper;
import cl.caserita.proceso.wms.helper.IntegracionConfirmacionASNHelper;

@Path("/wmsInfoCaserita")

public class ServicioCaseritaProceInfoWMS {

	private static Logger log = Logger.getLogger(servicioCaseritaXMLWMS.class);

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
		public String getvalidaUsuario(String par000) {
			//log.info(par000);
			Gson gson = new Gson();
			String xml="";
			String nombreArchivo="";
			//log.info(par000);
			try{
				JSONObject object = (JSONObject) new JSONParser().parse(par000);
				 
				 if (object.get("xml")!=null){
					 xml = object.get("xml").toString();//.replaceAll("\"","");
				 }
				 if (object.get("nameFile")!=null){
					 nombreArchivo = object.get("nameFile").toString().replaceAll("\"","");
				 }
				
			}catch(Exception e){
				e.printStackTrace();
			}
			log.info("PROCESA XML");
			log.info("Nombre Archivo XML:"+nombreArchivo);
			File fXmlFile = new File(xml);
			
			try{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				InputStream is = new ByteArrayInputStream(xml.getBytes());
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
				if ("<SHIP_LOAD_OUB_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())!=-1){
					IntegracionConfirCamionHelper helper = new IntegracionConfirCamionHelper();
					NodeList nListin = doc.getElementsByTagName("MOVE_SEG");
					log.info(nListin.getLength());
					log.info("----------------------------");
					for (int temp = 0; temp < nListin.getLength(); temp++) {

							Node nNode = nListin.item(temp);
									
							log.info("\nCurrent Element :" + nNode.getNodeName());
									
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								StringTokenizer st = new StringTokenizer(eElement.getElementsByTagName("CARCOD").item(0).getTextContent(),"-");
								int campo=0;
								 while (st.hasMoreTokens( )){
									 
									 	String tr = st.nextToken();
									 	log.info(tr);
									 	if (tr.compareTo("PROVEEDOR")!=0){
									 		log.info("PROCESA CAMION");
									 		helper.procesaConfirmaCamion2(xml, "CAMION".trim(),nombreArchivo);
									 	}else{
									 		log.info("PROCESA MERMA");

									 		helper.procesaConfirmaCamion2(xml, "MERMA".trim(),nombreArchivo);
									 	}
									 campo++;
									 break;
							        
							    }
								 
								 
							}
							
					}
					
					
				}else if ("<VC_INVENTORY_ADJUST>".indexOf(doc.getDocumentElement().getNodeName())!=-1){
					IntegracionAjusteHelper ajustes = new IntegracionAjusteHelper();
					log.info("Transportista 2");
					ajustes.procesaAjuste2(xml, nombreArchivo);
				}
				else if ("<INVENTORY_STATUS_CHANGE_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())!=-1){
					log.info("Procesa cambio de estado Inventario");
					IntegracionCambioEstadoHelper helper = new IntegracionCambioEstadoHelper();
					helper.procesaCambioEstado(xml, nombreArchivo);
				}else if ("<INVENTORY_RECEIPT_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())!=-1 || "<RCV_TRLR_CLOSE_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())!=-1){
					
					NodeList nListin = doc.getElementsByTagName("INVENTORY_RECEIPT_IFD_SEG");
					log.info(nListin.getLength());
					log.info("----------------------------");
					if (nListin.getLength()>0){
						for (int temp = 0; temp < nListin.getLength(); temp++) {

							Node nNode = nListin.item(temp);
									
							log.info("\nCurrent Element :" + nNode.getNodeName());
									
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								String  tipo = eElement.getElementsByTagName("TRKNUM").item(0).getTextContent();
								String  tipo2 = eElement.getElementsByTagName("CARCOD").item(0).getTextContent();
								log.info("tipo:"+tipo);
								log.info("tipo 2:"+tipo2);
								
								if (tipo2.indexOf("PROVEEDOR")>-1 || tipo2.indexOf("TRASPASO")>-1){
									IntegracionConfirmaRecepcionHelper helper = new IntegracionConfirmaRecepcionHelper();
									log.info("INGRESA A PROCESAR CONFIRMACIONES OC JORGE RAMIREZ");
									
									helper.procesaReconciliacion(xml, nombreArchivo);
									
								} 
								
								if (tipo.indexOf("DEV")>-1){
									IntegracionConfirmacionASNHelper helper = new IntegracionConfirmacionASNHelper();
									log.info("INGRESA A PROCESAR CONFIRMACIONES ASN JAIME");
									String tipoProceso ="INDIV";
									if ("<INVENTORY_RECEIPT_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())==0){
										tipo="INDIV";
										log.info("PROCESA DE FORMA INDIVIDUAL");
									}else if ("<RCV_TRLR_CLOSE_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())==0){
										log.info("PROCESA DE FORMA CIERRE CAMION");
										tipo="CIERRE";

									}
									helper.procesaReconciliacion(xml, nombreArchivo,tipo);
								}
								
								 
								 
							}
							
					}
					}else{
						 nListin = doc.getElementsByTagName("RCV_TRLR_CLOSE_IFD_VC");
						 log.info(nListin.getLength());
							log.info("----------------------------");
							if (nListin.getLength()>0){
								for (int temp = 0; temp < nListin.getLength(); temp++) {

									Node nNode = nListin.item(temp);
											
									log.info("\nCurrent Element :" + nNode.getNodeName());
											
									if (nNode.getNodeType() == Node.ELEMENT_NODE) {
										Element eElement = (Element) nNode;
										String  tipo = eElement.getElementsByTagName("TRKNUM").item(0).getTextContent();
										String  tipo2 = eElement.getElementsByTagName("CARCOD").item(0).getTextContent();
										log.info("tipo:"+tipo);
										log.info("tipo 2:"+tipo2);
										
										if (tipo2.indexOf("PROVEEDOR")>-1 || tipo2.indexOf("TRASPASO")>-1){
											IntegracionConfirmaRecepcionHelper helper = new IntegracionConfirmaRecepcionHelper();
											log.info("INGRESA A PROCESAR CONFIRMACIONES OC JORGE RAMIREZ");

											helper.procesaReconciliacion(xml, nombreArchivo);

										}
										
										if (tipo.indexOf("DEV")>-1){
											IntegracionConfirmacionASNHelper helper = new IntegracionConfirmacionASNHelper();
											log.info("INGRESA A PROCESAR CONFIRMACIONES ASN JAIME");
											String tipoProceso ="INDIV";
											int numero = "<RCV_TRLR_CLOSE_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName());
											log.info(numero);
											if ("<INVENTORY_RECEIPT_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())==1){
												tipo="INDIV";
												log.info("PROCESA DE FORMA INDIVIDUAL");
											}else if ("<RCV_TRLR_CLOSE_IFD_VC>".indexOf(doc.getDocumentElement().getNodeName())==1){
												log.info("PROCESA DE FORMA CIERRE CAMION");
												tipo="CIERRE";

											}
											helper.procesaReconciliacion(xml, nombreArchivo,tipo);
										}
										
										 
										 
									}
									
							}
							}
					}
					
					
				}

				log.info("Root element Nuevo:" + doc.getDocumentElement().getNodeName());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return par000;
		}
}
