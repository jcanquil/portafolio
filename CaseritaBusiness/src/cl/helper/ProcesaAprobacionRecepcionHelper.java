package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.Conar1DAO;
import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dao.iface.ExmodcDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.Conar1DTO;
import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.RecepDocumentoDTO;
import cl.caserita.dto.ReferenciaDocumentoDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.procesaXML.LeerXml;

public class ProcesaAprobacionRecepcionHelper {
	private static Logger log = Logger.getLogger(procesaFacturacionServlet.class); 
	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	private static String procesaServlet="";
	DAOFactory factory = DAOFactory.getInstance();
	ConarcDAO conarcDAO = factory.getConarcDAO();
	TptdeleDAO tpt = factory.getTptdeleDAO();
	
	public void procesaRecepcin(int empresa){
		List docu = new ArrayList();
		WsClient ws = new WsClient();
		int devolver=0;
		try{
			
			prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			log.info("PROCESA APROBACION");
			pathProperties = Constants.FILE_PROPERTIES;
			archivoLog=prop.getProperty("urlServletAPPDoc");
			int fechaProceso=Integer.parseInt(prop.getProperty("fechaProceso"));
			log.info("Dias PROCESO:"+fechaProceso);
			Fecha fch = new Fecha();
			fch.substractDays(fechaProceso);
			fechaProceso = Integer.parseInt(fch.getYYYYMMDD());
			log.info("Fecha PROCESO:"+fechaProceso);
			
			String endPoint=prop.getProperty("endpoint");
			docu = procesaDocumentos(ws.onlineGestionRec(empresa,endPoint) );
			Iterator iter = docu.iterator();
			String aprueba="";
			while (iter.hasNext()){
				RecepDocumentoDTO recep = (RecepDocumentoDTO) iter.next();
				//log.info("Numero Documento:"+recep.getNumeroDocumento());
				String xml = ws.onlineRecoveryRec(Integer.parseInt(recep.getRutProveedor()), recep.getCodigoDoc(), recep.getNumeroDocumento(),empresa,endPoint);
				LeerXml leer = new LeerXml();
				DocumentoElectronicoDTO doc = leer.main(xml);
				String servlet="";
				String texto="";
				StringBuffer tmp = new StringBuffer(); 
				if (doc!=null && doc.getFecha()!="" && doc.getFecha()!=null && doc.getRutProveedor()!=null){
					String ano = doc.getFecha().substring(0, 4);
					String mes = doc.getFecha().substring(5, 7);
					String dia = doc.getFecha().substring(8, 10);
					String fecha = ano+mes+dia;
					int fechaDocum = Integer.parseInt(fecha);
					String rut="";
					String dv="";
					if (doc.getRutProveedor().length()>=10){
						rut = doc.getRutProveedor().substring(0, 8);
						dv = doc.getRutProveedor().substring(9, 10);
					}else{
						rut = doc.getRutProveedor().substring(0, 7);
						dv = doc.getRutProveedor().substring(8, 9);
					}
					log.info("Fecha Documento a Procesar:"+fechaDocum);
					int codigoCaserita = tpt.buscaDocumentoElectronico(Integer.parseInt(doc.getCodDocumento()));

					if (fechaDocum<fechaProceso){
						
						log.info("Codigo Documento a Buscar en CONARC:"+doc.getCodDocumento());
						int empresaB=0;
						if (empresa==96509850){
							empresaB=1;
						}else if (empresa==76288567){
							empresaB=2;
						}
						 if (codigoCaserita==33){
					//	if (conarcDAO.buscaDocumentoContabilizado(Integer.parseInt(doc.getFolio()), codigoCaserita,  Integer.parseInt(rut), dv)>0){
							 aprueba=empresa+"|"+recep.getRutProveedor()+"|"+recep.getCodigoDoc()+"|"+recep.getNumeroDocumento()+"|"+1+"|";
							 ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
							 aprueba="";
							 aprueba=empresa+"|"+recep.getRutProveedor()+"|"+recep.getCodigoDoc()+"|"+recep.getNumeroDocumento()+"|"+3+"|";
							ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
							log.info("XML Recepcion:"+xml);
							 servlet="http://192.168.1.4:8081/Facturacion/webservices/statusAcDTE.jsp";
							
							//procesaDoc(dto,empresa,0,servlet);
							log.info("FORMA SERVLET APROBACION");
							log.info("COMIENZA A FORMAR SERVLET:"+servlet);
							procesaServlet =servlet+"?e="+empresa+"&ee="+recep.getRutProveedor()+"&t="+recep.getCodigoDoc()+"&f="+recep.getNumeroDocumento()+"&ac=1&mr=ACEPTADA";
							
							log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
							
							 tmp = new StringBuffer(); 
					         texto = new String(); 
							try { 
					            // Crea la URL con del sitio introducido, ej: http://google.com 
					            URL url = new URL(procesaServlet); 
					     
					            // Lector para la respuesta del servidor 
					            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
					            String str; 
					            while ((str = in.readLine()) != null) { 
					                tmp.append(str); 
					            } 
					            //url.openStream().close();
					            in.close(); 
					            texto = tmp.toString(); 
					        }catch (MalformedURLException e) { 
					            texto = "<h2>No esta correcta la URL</h2>".toString(); 
					        } 
					        log.info("DOCUMENTO APROBADO:"+texto);
					        devolver=devolver+1;
						}
						
							
					else if (codigoCaserita==38){
							int codigoEmpresa=2;
							if (empresa==76288567){
								codigoEmpresa=2;
							}else if (empresa==96509850){
								codigoEmpresa=1;
							}
							//if (conarcDAO.buscaFolioExdfcr(codigoEmpresa,Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0){
								aprueba=empresa+"|"+recep.getRutProveedor()+"|"+recep.getCodigoDoc()+"|"+recep.getNumeroDocumento()+"|"+1+"|";
								 ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
								 aprueba="";
								 aprueba=empresa+"|"+recep.getRutProveedor()+"|"+recep.getCodigoDoc()+"|"+recep.getNumeroDocumento()+"|"+3+"|";
								ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
								log.info("XML Recepcion:"+xml);
								 servlet="http://192.168.1.4:8081/Facturacion/webservices/statusAcDTE.jsp";
								
								//procesaDoc(dto,empresa,0,servlet);
								log.info("FORMA SERVLET APROBACION");
								log.info("COMIENZA A FORMAR SERVLET:"+servlet);
								procesaServlet =servlet+"?e="+empresa+"&ee="+recep.getRutProveedor()+"&t="+recep.getCodigoDoc()+"&f="+recep.getNumeroDocumento()+"&ac=1&mr=ACEPTADA";
								
								log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
								
								 tmp = new StringBuffer(); 
						         texto = new String(); 
								try { 
						            // Crea la URL con del sitio introducido, ej: http://google.com 
						            URL url = new URL(procesaServlet); 
						     
						            // Lector para la respuesta del servidor 
						            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
						            String str; 
						            while ((str = in.readLine()) != null) { 
						                tmp.append(str); 
						            } 
						            //url.openStream().close();
						            in.close(); 
						            texto = tmp.toString(); 
						        }catch (MalformedURLException e) { 
						            texto = "<h2>No esta correcta la URL</h2>".toString(); 
						        } 
						        log.info("DOCUMENTO APROBADO:"+texto);
						        devolver=devolver+1;
								
							//}
							
						}else if (codigoCaserita==35){
							int codigoEmpresa=2;
							if (empresa==76288567){
								codigoEmpresa=2;
							}else if (empresa==96509850){
								codigoEmpresa=1;
							}
						//	if (conarcDAO.buscaFolioExdfcr(codigoEmpresa,Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0){
								aprueba=empresa+"|"+recep.getRutProveedor()+"|"+recep.getCodigoDoc()+"|"+recep.getNumeroDocumento()+"|"+1+"|";
								 ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
								 aprueba="";
								 aprueba=empresa+"|"+recep.getRutProveedor()+"|"+recep.getCodigoDoc()+"|"+recep.getNumeroDocumento()+"|"+3+"|";
								ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
								log.info("XML Recepcion:"+xml);
								 servlet="http://192.168.1.4:8081/Facturacion/webservices/statusAcDTE.jsp";
								
								//procesaDoc(dto,empresa,0,servlet);
								log.info("FORMA SERVLET APROBACION");
								log.info("COMIENZA A FORMAR SERVLET:"+servlet);
								procesaServlet =servlet+"?e="+empresa+"&ee="+recep.getRutProveedor()+"&t="+recep.getCodigoDoc()+"&f="+recep.getNumeroDocumento()+"&ac=1&mr=ACEPTADA";
								
								log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
								
								 tmp = new StringBuffer(); 
						         texto = new String(); 
								try { 
						            // Crea la URL con del sitio introducido, ej: http://google.com 
						            URL url = new URL(procesaServlet); 
						     
						            // Lector para la respuesta del servidor 
						            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
						            String str; 
						            while ((str = in.readLine()) != null) { 
						                tmp.append(str); 
						            } 
						            //url.openStream().close();
						            in.close(); 
						            texto = tmp.toString(); 
						        }catch (MalformedURLException e) { 
						            texto = "<h2>No esta correcta la URL</h2>".toString(); 
						        } 
						        log.info("DOCUMENTO APROBADO:"+texto);
						        devolver=devolver+1;
								
							//}
							
						}
						
				        
						
					}
				}
				//Procesa Ley 19.xxx
				/*if (devolver==100){
					break;
				}*/
				
				
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	//Procesa String de documentos de proveedor	
	public void procesaDocumentoIndividual(int codDoc, int numDoc, int rut, int empresa){
		WsClient ws = new WsClient();
		String aprueba="";
		try
		{
			//String xml = ws.onlineRecoveryRec(rut, codDoc, numDoc, empresa,"");
			DAOFactory factory = DAOFactory.getInstance();
		
			
			TptdeleDAO tpdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
			
			//Verifica si existe orden de compra
			//log.info("Ingresa Proceso de FV");
			//log.info("Codigo Documento PP:"+doc.getCodDocumentoPP());
			
			int codigo = tpdeleDAO.buscaDocumentoElectronico(codDoc);
			codDoc=codigo;
			//log.info("XML Recepcion:"+xml);
			LeerXml leer = new LeerXml();
			//procesaDoc(leer.main(xml),empresa,1,archivoLog);
			
			 aprueba=empresa+"|"+rut+"|"+codDoc+"|"+numDoc+"|"+1+"|";
			 log.info("Mensaje1:"+aprueba);
			 ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
			 aprueba="";
			 aprueba=empresa+"|"+rut+"|"+codDoc+"|"+numDoc+"|"+3+"|";
			 log.info("Mensaje2:"+aprueba);
			ws.procesaAprobacionRechazo(String.valueOf(empresa), "", aprueba);
			
			
			DocumentoElectronicoDTO dto = new DocumentoElectronicoDTO();
			String servlet="http://192.168.1.4:8081/Facturacion/webservices/statusAcDTE.jsp";
			dto.setCodDocumento(String.valueOf((codDoc)));
			dto.setFolio(String.valueOf(numDoc));
			dto.setCodDocumentoPP(String.valueOf(codDoc));
			dto.setRutProveedor(String.valueOf(rut));
			//procesaDoc(dto,empresa,0,servlet);
			log.info("FORMA SERVLET APROBACION");
			log.info("COMIENZA A FORMAR SERVLET:"+servlet);
			procesaServlet =servlet+"?e="+empresa+"&ee="+rut+"&t="+codDoc+"&f="+numDoc+"&ac=1&mr=ACEPTADA";
			
			log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
			
			StringBuffer tmp = new StringBuffer(); 
	        String texto = new String(); 
			try { 
	            // Crea la URL con del sitio introducido, ej: http://google.com 
	            URL url = new URL(procesaServlet); 
	     
	            // Lector para la respuesta del servidor 
	            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
	            String str; 
	            while ((str = in.readLine()) != null) { 
	                tmp.append(str); 
	            } 
	            //url.openStream().close();
	            in.close(); 
	            texto = tmp.toString(); 
	        }catch (MalformedURLException e) { 
	            texto = "<h2>No esta correcta la URL</h2>".toString(); 
	        } 
	        log.info("DOCUMENTO APROBADO");
		
		
	}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public List procesaDocumentos(String docs){
		List doc = new ArrayList();
		StringTokenizer st = new StringTokenizer(docs,"|");
		 while (st.hasMoreTokens( )){
			 
			 	String tr = st.nextToken();
			 	StringTokenizer stad = new StringTokenizer(tr,",");
			 	RecepDocumentoDTO recep = new RecepDocumentoDTO();
			 	int num=0;
			 	while (stad.hasMoreTokens( )){
			 		//log.info("Token: " + stad.nextToken( ));
			 		if (num==0){
			 			recep.setRutProveedor(stad.nextToken());
			 		}else if (num==1){
			 			recep.setCodigoDoc(Integer.parseInt(stad.nextToken()));
			 		}else if (num==2){
			 			String numeroDoc = stad.nextToken();
			 			//int nume = Integer.parseInt(numeroDoc);
			 			numeroDoc = numeroDoc.replaceAll("\n", "");
			 			//log.info("Numero" + numeroDoc);
			 			recep.setNumeroDocumento(Integer.parseInt(numeroDoc));
			 		}
			 		
			 		num++;
			 	}
			 	doc.add(recep);
			 
	        
	    }
		
		
		return doc;
		
	}
	public void procesaDoc(DocumentoElectronicoDTO doc, int rutEmpresa, int ind, String ruta){
		DAOFactory factory = DAOFactory.getInstance();
		ExmodcDAO exmodc = (ExmodcDAO) factory.getExmodcDAO();
		ConarcDAO conarc = (ConarcDAO) factory.getConarcDAO();
		Conar1DAO conar1 = (Conar1DAO) factory.getConar1DAO();
		TptempDAO tpempDAO = (TptempDAO) factory.getTptempDAO();
		TptdeleDAO tpdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
		ExmodcDTO exmodcDTO = null;
		ConarcDTO conarcDTO = null;
		Conar1DTO conar1DTO = null;
		TptempDTO tptempDTO = null;
		tptempDTO = tpempDAO.recuperaEmpresaPorRut(rutEmpresa);
		//Verifica si existe orden de compra
		//log.info("Ingresa Proceso de FV");
		//log.info("Codigo Documento PP:"+doc.getCodDocumentoPP());
		log.info("Numero Documento1:"+doc.getFolio());
		int codigo = tpdeleDAO.buscaDocumentoElectronico(Integer.parseInt(doc.getCodDocumentoPP()));
		doc.setCodDocumento(String.valueOf(codigo));
		//log.info("Codigo Documento:"+codigo);
		//log.info("Codigo Documento1:"+codigo);
	
		if (codigo>0){
			
					String rut="";
					String dv="";
				     /*if (doc.getRutProveedor().length()>=10){
				    	  rut = doc.getRutProveedor().substring(0, 8);
						  dv = doc.getRutProveedor().substring(9, 10);
				     }else{
				    	 rut = doc.getRutProveedor().substring(0, 7);
						  dv = doc.getRutProveedor().substring(8, 9);
				     }*/
					 
						String ano = doc.getFecha().substring(0, 4);
						String mes = doc.getFecha().substring(5, 7);
						String dia = doc.getFecha().substring(8, 10);
						String fecha = ano+mes+dia;
						int fechaDocum = Integer.parseInt(fecha);
						
						int numeroOC = 0;
								
						
							log.info("Procesa fecha documento:"+fechaDocum);
							log.info("Codigo Doc Caserita:"+doc.getCodDocumento());
							log.info("Codigo Doc PP:"+doc.getCodDocumentoPP());
							log.info("Rut Proveedor : "+doc.getRutProveedor());
							log.info("FOlio : "+doc.getFolio());
						
						
						if (isNumeric(doc.getFolio()) ){
							log.info("No procesa Documento");
						}else{
							
						//Procesa Recepcion por Fecha
							log.info("PROCESA APROBACION DIRECTA");
							log.info("Procesa fecha documento:"+fechaDocum);
							if (fechaDocum<20150501){
								log.info("FORMA SERVLET APROBACION");
								log.info("COMIENZA A FORMAR SERVLET:"+ruta);
								procesaServlet =ruta+"?e="+tptempDTO.getRut()+"&ee="+rut+"&t="+doc.getCodDocumentoPP()+"&f="+doc.getFolio()+"&ac=1&mr=ACEPTADA";
								
								log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
								
								StringBuffer tmp = new StringBuffer(); 
						        String texto = new String(); 
								try { 
						            // Crea la URL con del sitio introducido, ej: http://google.com 
						            URL url = new URL(procesaServlet); 
						     
						            // Lector para la respuesta del servidor 
						            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
						            String str; 
						            while ((str = in.readLine()) != null) { 
						                tmp.append(str); 
						            } 
						            //url.openStream().close();
						            in.close(); 
						            texto = tmp.toString(); 
						        }catch (MalformedURLException e) { 
						            texto = "<h2>No esta correcta la URL</h2>".toString(); 
						        } catch (IOException e) { 
						            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
						            } 
						        log.info("DOCUMENTO APROBADO");
							}
					    
							
							
						
						if (conarc.buscaDocumentoContabilizado(Integer.parseInt(doc.getFolio()), codigo,  Integer.parseInt(rut), dv)>0){
							
										procesaServlet =ruta+"?e="+tptempDTO.getRut()+"&ee="+rut+"&t="+doc.getCodDocumentoPP()+"&f="+doc.getFolio()+"&ac=1&mr=ACEPTADA";
										
										log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
										
										StringBuffer tmp = new StringBuffer(); 
								        String texto = new String(); 
										try { 
								            // Crea la URL con del sitio introducido, ej: http://google.com 
								            URL url = new URL(procesaServlet); 
								     
								            // Lector para la respuesta del servidor 
								            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
								            String str; 
								            while ((str = in.readLine()) != null) { 
								                tmp.append(str); 
								            } 
								            //url.openStream().close();
								            in.close(); 
								            texto = tmp.toString(); 
								        }catch (MalformedURLException e) { 
								            texto = "<h2>No esta correcta la URL</h2>".toString(); 
								        } catch (IOException e) { 
								            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
								            } 
					}else if (conarc.buscaDocumentoContabilizado(Integer.parseInt(doc.getFolio()),3,  Integer.parseInt(rut), dv)>0){
						
									
									procesaServlet =ruta+"?e="+tptempDTO.getRut()+"&ee="+rut+"&t=33&f="+doc.getFolio()+"&ac=1&mr=ACEPTADA";
									
									log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
									
									StringBuffer tmp = new StringBuffer(); 
							        String texto = new String(); 
									try { 
							            // Crea la URL con del sitio introducido, ej: http://google.com 
							            URL url = new URL(procesaServlet); 
							     
							            // Lector para la respuesta del servidor 
							            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
							            String str; 
							            while ((str = in.readLine()) != null) { 
							                tmp.append(str); 
							            } 
							            //url.openStream().close();
							            in.close(); 
							            texto = tmp.toString(); 
							        }catch (MalformedURLException e) { 
							            texto = "<h2>No esta correcta la URL</h2>".toString(); 
							        } catch (IOException e) { 
							            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
							            } 
							        
							        
								
								
	
						
						
				
				
					
				}else if (conarc.buscaDocumentoContabilizado(Integer.parseInt(doc.getFolio()),8,  Integer.parseInt(rut), dv)>0){
					
					
					procesaServlet =ruta+"?e="+tptempDTO.getRut()+"&ee="+rut+"&t=61&f="+doc.getFolio()+"&ac=1&mr=ACEPTADA";
					
					log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
					
					StringBuffer tmp = new StringBuffer(); 
			        String texto = new String(); 
					try { 
			            // Crea la URL con del sitio introducido, ej: http://google.com 
			            URL url = new URL(procesaServlet); 
			     
			            // Lector para la respuesta del servidor 
			            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
			            String str; 
			            while ((str = in.readLine()) != null) { 
			                tmp.append(str); 
			            } 
			            //url.openStream().close();
			            in.close(); 
			            texto = tmp.toString(); 
			        }catch (MalformedURLException e) { 
			            texto = "<h2>No esta correcta la URL</h2>".toString(); 
			        } catch (IOException e) { 
			            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
			            } 
			        
			        
				
				
				}else if (conarc.buscaDocumentoContabilizado(Integer.parseInt(doc.getFolio()),34,  Integer.parseInt(rut), dv)>0){
					
					
					procesaServlet =ruta+"?e="+tptempDTO.getRut()+"&ee="+rut+"&t=61&f="+doc.getFolio()+"&ac=1&mr=ACEPTADA";
					
					log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
					
					StringBuffer tmp = new StringBuffer(); 
			        String texto = new String(); 
					try { 
			            // Crea la URL con del sitio introducido, ej: http://google.com 
			            URL url = new URL(procesaServlet); 
			     
			            // Lector para la respuesta del servidor 
			            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
			            String str; 
			            while ((str = in.readLine()) != null) { 
			                tmp.append(str); 
			            } 
			            //url.openStream().close();
			            in.close(); 
			            texto = tmp.toString(); 
			        }catch (MalformedURLException e) { 
			            texto = "<h2>No esta correcta la URL</h2>".toString(); 
			        } catch (IOException e) { 
			            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
			            } 
			        
			        
				
				
				}else if (conarc.buscaDocumentoContabilizado(Integer.parseInt(doc.getFolio()),36,  Integer.parseInt(rut), dv)>0){
					
					
					procesaServlet =ruta+"?e="+tptempDTO.getRut()+"&ee="+rut+"&t=61&f="+doc.getFolio()+"&ac=1&mr=ACEPTADA";
					
					log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
					
					StringBuffer tmp = new StringBuffer(); 
			        String texto = new String(); 
					try { 
			            // Crea la URL con del sitio introducido, ej: http://google.com 
			            URL url = new URL(procesaServlet); 
			     
			            // Lector para la respuesta del servidor 
			            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
			            String str; 
			            while ((str = in.readLine()) != null) { 
			                tmp.append(str); 
			            } 
			            //url.openStream().close();
			            in.close(); 
			            texto = tmp.toString(); 
			        }catch (MalformedURLException e) { 
			            texto = "<h2>No esta correcta la URL</h2>".toString(); 
			        } catch (IOException e) { 
			            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
			            } 
			        
			        
				
				
				}
		
		


	
}
					
			
			
		}
	
		
		
	}
	public static void main (String[]args) throws IOException{
		
		Fecha fch = new Fecha();
		fch.substractDays(20);
		String fecha = fch.getDDMMYYYY();
		
		
		String caracteres ="91083000,61,112817|92091000,61,67521|93178000,33,4658208|76541630,61,1045794";
		//procesaRecepcionHelper helper = new procesaRecepcionHelper();
		//helper.procesaDocumentos(caracteres);
		
		procesaRecepcionHelper recp = new procesaRecepcionHelper();
		
		
		/*List docu = new ArrayList();
		WsClient ws = new WsClient();
		try{
			int emprea=2;
			docu = recp.procesaDocumentos(caracteres);
			Iterator iter = docu.iterator();
			while (iter.hasNext()){
				RecepDocumentoDTO recep = (RecepDocumentoDTO) iter.next();
				//log.info("Numero Documento:"+recep.getNumeroDocumento());
				String xml = ws.onlineRecoveryRec(Integer.parseInt(recep.getRutProveedor()), recep.getCodigoDoc(), recep.getNumeroDocumento(),2,"");
				//log.info("XML Recepcion:"+xml);
				LeerXml leer = new LeerXml();
				
				recp.procesaDoc(leer.main(xml),2,0,"",0);
				//break;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}*/
		String servlet="http://192.168.1.4:8081/Facturacion/webservices/statusAcDTE.jsp";
		
		//procesaDoc(dto,empresa,0,servlet);
		log.info("FORMA SERVLET APROBACION");
		log.info("COMIENZA A FORMAR SERVLET:"+servlet);
		procesaServlet =servlet+"?e="+76288567+"&ee="+91438000+"&t="+33+"&f="+514647+"&ac=1&mr=ACEPTADA";
		
		log.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
		
		StringBuffer tmp = new StringBuffer(); 
        String texto = new String(); 
		try { 
            // Crea la URL con del sitio introducido, ej: http://google.com 
            URL url = new URL(procesaServlet); 
     
            // Lector para la respuesta del servidor 
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
            String str; 
            while ((str = in.readLine()) != null) { 
                tmp.append(str); 
            } 
            //url.openStream().close();
            in.close(); 
            texto = tmp.toString(); 
        }catch (MalformedURLException e) { 
            texto = "<h2>No esta correcta la URL</h2>".toString(); 
        } 
        log.info("DOCUMENTO APROBADO:"+texto);
	}
	
	public String procesaCaracteres(String descripcion){
		  
		 /* descripcion = descripcion.replaceAll("�", "N");
		  descripcion = descripcion.replaceAll("�", "");
		  descripcion = descripcion.replaceAll("<", "&lt;");
		  descripcion = descripcion.replaceAll(">", "&gt;");
		  descripcion = descripcion.replaceAll("&", "&amp;");
		  descripcion = descripcion.replaceAll("\"", "&quot;");*/
		  descripcion = descripcion.replaceAll("'", "");
		  
		  
		  
		  return descripcion;
	  }
		
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return false;
		} catch (NumberFormatException nfe){
			return true;
		}
	} 
}
