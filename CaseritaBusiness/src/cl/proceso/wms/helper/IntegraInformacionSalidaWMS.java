package cl.caserita.proceso.wms.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.StringTokenizer;

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

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.enviomail.main.emailInformacionWMS;
import cl.caserita.proceso.wms.helper.IntegracionAjusteHelper;
import cl.caserita.proceso.wms.helper.IntegracionCambioEstadoHelper;
import cl.caserita.proceso.wms.helper.IntegracionConfirCamionHelper;
import cl.caserita.proceso.wms.helper.IntegracionConfirmaRecepcionHelper;
import cl.caserita.proceso.wms.helper.IntegracionConfirmacionASNHelper;
import cl.caserita.wms.dto.ConfirmacionDTO;
import cl.caserita.wms.helper.IntegracionSalidaWEBHelper;

public class IntegraInformacionSalidaWMS {

	private static Logger log = Logger.getLogger(IntegraInformacionSalidaWMS.class);
	private static Properties prop=null;
	private static String pathProperties;
//	private static emailInformacionWMS email = new emailInformacionWMS();

	public static void main (String []args){
		int i2=0;
		IntegraInformacionSalidaWMS integra = new IntegraInformacionSalidaWMS();
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream("/proceso_XML/"+"config.properties"));
			//prop.load(new FileInputStream("proceso/"+"config.properties"));
		}
		catch(Exception e){
			String mensaje = "Mensaje 1 :"+e.getMessage();
			//email.mail(mensaje);
			e.printStackTrace();
		}
		try{
			String ws=prop.getProperty("wsCaserita");
			String dir1=prop.getProperty("dirInicial");
			String dir2=prop.getProperty("dirFinal");
			while (i2==0){
				//String path = "/home2/ftp/out/";
				//String path = "/RedPrairie/LCSWMSTST/LES/files/hostout/";
				String path = dir1;

				String files;

				File directorio = new File(path);
				File [] ficheros = directorio.listFiles();
				String line;
				//log.info(ws);
				if (ficheros!=null){
					for (int i = 0; i < ficheros.length; i++) {
						
			            if (ficheros[i].isFile()) {
			            	files = ficheros[i].getName();
			                if (files.endsWith(".xml") || files.endsWith(".XML")){
			                	log.info("archivo:"+path+ficheros[i].getName());
			    				try{
			    					
			    					BufferedReader bf = new BufferedReader(new FileReader(path+ficheros[i].getName()));
			    					String linea="";
			    					String xml="";
			    					int lin=0;
			    					int band=1;
			    					String xml1="";
			    					Fecha fecha = new Fecha();

			    					while((linea=bf.readLine())!=null){
			    						xml=xml+linea;
			    						lin++;
			    						band=1;
			    						if (lin==5000){

			    							xml1=xml1+xml;
			    							xml="";
			    							lin=0;
			    							band=0;
			    						}
			    					}
			    					//Concatena XML 
			    					if (band==1){
			    						xml1=xml1+xml;
			    					}
			    					Fecha fecha2 = new Fecha();
			    					
			    					log.info(xml1);
			    					//System.out.println(xml);
			    					bf.close();
			    					ConfirmacionDTO confirmacion = new ConfirmacionDTO();
			    					confirmacion.setXml(xml1);
			    					confirmacion.setNameFile(ficheros[i].getName());
			    					Gson gson = new Gson();
			    					String json = gson.toJson(confirmacion);
			    					//System.out.println(json);
			    					try {
			    						 
			    						URL url = new URL(ws.trim());
			    						
			    						//URL url = new URL("http://192.168.1.30:8080/ServiciosTransportistaWEB/servicioTransRest/wmsCaserita/post");

			    						
			    						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			    						conn.setDoOutput(true);
			    						conn.setRequestMethod("POST");
			    						OutputStream os = conn.getOutputStream();
			    						os.write(json.getBytes());
			    						//os.write(xml.getBytes());
			    						os.flush();
			    						
			    				 
			    						if (conn.getResponseCode() != 200) {
			    							throw new RuntimeException("Failed : HTTP error code : "
			    									+ conn.getResponseCode());
			    						}
			    				 
			    						BufferedReader br = new BufferedReader(new InputStreamReader(
			    							(conn.getInputStream())));
			    				 
			    						String output;
			    						log.info("Output from Server .... \n");
			    						/*while ((output = br.readLine()) != null) {
			    							System.out.println(output);
			    						}*/
			    				 
			    						conn.disconnect();
			    						xml="";
					    				xml1="";
					    				log.info("Hora Inicial:"+fecha.getHHMMSS());

				    					log.info("Hora Final:"+fecha2.getHHMMSS());
			    					  } catch (MalformedURLException e) {
			    						  String mensaje = "Mensaje 2: "+e.getMessage();
			    							//email.mail(mensaje);
			    						e.printStackTrace();
			    				 
			    					  } catch (IOException e) {
			    						  String mensaje = "Mensaje 3: "+e.getMessage();
			    							//email.mail(mensaje);
			    						e.printStackTrace();
			    				 
			    					  }
			    					

			    				}catch(Exception e){
			    					String mensaje = "Mensaje 4: "+e.getMessage();
			    					//email.mail(mensaje);
			    					e.printStackTrace();
			    				}
			    				integra.moveFile(path+ficheros[i].getName(), ficheros[i].getName(),dir2);
			    				
			                }
			            }

						
					
					}
				}
				
				
				
			}
		}catch(Exception e){
			String mensaje = "Mensaje 5: "+e.getMessage();
			//email.mail(mensaje);			
			e.printStackTrace();
		}
		
	}
	
	public String proceso(String par000){
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
	
	public int moveFile(String urlFile, String nameFile, String dir){
		int numero=0;
		Path origenPath = FileSystems.getDefault().getPath(urlFile);
		//String path = "/home2/ftp/out/";
	    //Path destinoPath = FileSystems.getDefault().getPath("/home2/ftp/out/proc/"+nameFile);

	   // Path destinoPath = FileSystems.getDefault().getPath("/RedPrairie/LCSWMSTST/LES/files/hostout/caserita/"+nameFile);
	    Path destinoPath = FileSystems.getDefault().getPath(dir+nameFile);


	    try {
	        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	    	String mensaje = "Mensaje 6: "+e.getMessage();
			//email.mail(mensaje);	
	        log.info(e);
	        
	    }
	    
	    
		return numero;
	}
}
