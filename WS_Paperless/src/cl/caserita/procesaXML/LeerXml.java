package cl.caserita.procesaXML;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cl.caserita.comunes.properties.Constants;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.dto.ReferenciaDocumentoDTO;

public class LeerXml {
	 private static Properties prop=null;
		private static String pathProperties;
		public static String rutProv;
public DocumentoElectronicoDTO main(String argv) {
	String entrada;

	String cadena="";
	DocumentoElectronicoDTO doc = new DocumentoElectronicoDTO();
	List refencia = new ArrayList();
	ReferenciaDocumentoDTO ref = new ReferenciaDocumentoDTO();
	
	
	try {
	
	URL url=
	    new URL(argv);
	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));


		while ((entrada = br.readLine()) != null){
			cadena = cadena + entrada;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		InputSource archivo = new InputSource();
		archivo.setCharacterStream(new StringReader(cadena)); 
		
		Document documento = db.parse(archivo);
		documento.getDocumentElement().normalize();
		
		//Recupera Datos IDOC
		NodeList nodeLista = documento.getElementsByTagName("IdDoc");
		//System.out.println("Informacion de los libros");

				for (int s = 0; s < nodeLista.getLength(); s++) {
			
				Node primerNodo = nodeLista.item(s);
				String fecha;
				String folio;
				String codDoc;
			
				if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
			
				Element primerElemento = (Element) primerNodo;
			
				NodeList primerNombreElementoLista =
			                        primerElemento.getElementsByTagName("FchEmis");
				Element primerNombreElemento =
			                        (Element) primerNombreElementoLista.item(0);
				NodeList primerNombre = primerNombreElemento.getChildNodes();
				fecha = ((Node) primerNombre.item(0)).getNodeValue().toString();
				//System.out.println("Titulo : "  + fecha);
				
				NodeList segundoNombreElementoLista =
			        primerElemento.getElementsByTagName("Folio");
			Element segundoNombreElemento =
			        (Element) segundoNombreElementoLista.item(0);
			NodeList segundoNombre = segundoNombreElemento.getChildNodes();
			folio = ((Node) segundoNombre.item(0)).getNodeValue().toString();
			
				
			
			NodeList tercerNombreElementoLista =
			    primerElemento.getElementsByTagName("TipoDTE");
			Element tercerNombreElemento =
			    (Element) tercerNombreElementoLista.item(0);
			NodeList tercerNombre = tercerNombreElemento.getChildNodes();
			codDoc = ((Node) tercerNombre.item(0)).getNodeValue().toString();
			//System.out.println("Titulo : "  + codDoc);
			doc.setCodDocumentoPP(codDoc.trim());
			doc.setFolio(folio);
			doc.setFecha(fecha);
			doc.setCodDocumento(codDoc);
			
			/*if (!"33".equals(codDoc)){
				return doc;
			}*/
				}
			      }
				
		//Recupera datos Emisor
		nodeLista = documento.getElementsByTagName("Emisor");
		//System.out.println("Informacion de los libros");
				for (int s = 0; s < nodeLista.getLength(); s++) {
				
					Node primerNodo = nodeLista.item(s);
					String rut=null;
					String razon=null;
					String hits=null;
					String direccion="";
					if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
				
					Element primerElemento = (Element) primerNodo;
				
					NodeList primerNombreElementoLista =
				                        primerElemento.getElementsByTagName("RUTEmisor");
					Element primerNombreElemento =
				                        (Element) primerNombreElementoLista.item(0);
					NodeList primerNombre = primerNombreElemento.getChildNodes();
					rut = ((Node) primerNombre.item(0)).getNodeValue().toString();
					//System.out.println("Titulo : "  + rut);
					rutProv=rut;
					NodeList segundoNombreElementoLista =
				        primerElemento.getElementsByTagName("RznSoc");
				Element segundoNombreElemento =
				        (Element) segundoNombreElementoLista.item(0);
				NodeList segundoNombre = segundoNombreElemento.getChildNodes();
				razon = ((Node) segundoNombre.item(0)).getNodeValue().toString();
				
				
				if (!primerElemento.getElementsByTagName("DirOrigen").equals(null) ){
					NodeList terceroNombreElementoLista = primerElemento.getElementsByTagName("DirOrigen");
					Element terceroNombreElemento =(Element) terceroNombreElementoLista.item(0);
					if (terceroNombreElemento!=null){
						NodeList terceroNombre = terceroNombreElemento.getChildNodes();
						direccion = ((Node) terceroNombre.item(0)).getNodeValue().toString();
					}
					
				}
			
			
				//System.out.println("Titulo : "  + razon);
					doc.setRutProveedor(rut);
					doc.setRazonSocialProveedor(procesaCaracteres(razon));
					if (direccion!=null && direccion!=""){
						doc.setDireccionProveedor(procesaCaracteres(direccion));
					}else{
						doc.setDireccionProveedor("");
					}
					
					}
				      }

				
				//Recupera datos Totales
				nodeLista = documento.getElementsByTagName("Totales");
				//System.out.println("Informacion de los libros");
						for (int s = 0; s < nodeLista.getLength(); s++) {
						
							Node primerNodo = nodeLista.item(s);
							String neto="";
							String iva="";
							String monTotal;
							String exento="";
							if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
						
							Element primerElemento = (Element) primerNodo;
							if ("33".equals(doc.getCodDocumentoPP()) || "61".equals(doc.getCodDocumentoPP()) || "52".equals(doc.getCodDocumentoPP())){
								
							NodeList primerNombreElementoLista =
						                        primerElemento.getElementsByTagName("MntNeto");
							Element primerNombreElemento =
						                        (Element) primerNombreElementoLista.item(0);
							System.out.println("antes de la caida");
							if (primerNombreElemento!=null){
								NodeList primerNombre = primerNombreElemento.getChildNodes();
								neto = ((Node) primerNombre.item(0)).getNodeValue().toString();
							}else{
								neto = String.valueOf(0);
							}
							
							//System.out.println("Titulo : "  + neto);
							}
							if ("33".equals(doc.getCodDocumentoPP()) || "61".equals(doc.getCodDocumentoPP()) || "52".equals(doc.getCodDocumentoPP())){
							System.out.println("RECUPERA IVA RECEP");
								NodeList segundoNombreElementoLista =
						        primerElemento.getElementsByTagName("IVA");
						Element segundoNombreElemento =
						        (Element) segundoNombreElementoLista.item(0);
						if (segundoNombreElemento!=null){
							NodeList segundoNombre = segundoNombreElemento.getChildNodes();
							iva = ((Node) segundoNombre.item(0)).getNodeValue().toString();
							System.out.println("IVA:"+iva);
						}else{
							iva=String.valueOf(0);
						}
						
						//System.out.println("Titulo : "  + iva);
						
							}

						NodeList tercerNombreElementoLista =
						    primerElemento.getElementsByTagName("MntTotal");
						Element tercerNombreElemento =
						    (Element) tercerNombreElementoLista.item(0);
						NodeList tercerNombre = tercerNombreElemento.getChildNodes();
						monTotal = ((Node) tercerNombre.item(0)).getNodeValue().toString();
						//System.out.println("Titulo : "  + monTotal);
						if ("34".equals(doc.getCodDocumentoPP())){
							NodeList cuartoNombreElementoLista =
							    primerElemento.getElementsByTagName("MntExe");
							Element cuartoNombreElemento =
							    (Element) cuartoNombreElementoLista.item(0);
							NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
							exento = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
							neto="0";
							iva="0";
						}else{
							NodeList cuartoNombreElementoLista =
							    primerElemento.getElementsByTagName("MntExe");
							Element cuartoNombreElemento =
							    (Element) cuartoNombreElementoLista.item(0);
							if (cuartoNombreElemento!=null){
								NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
								exento = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
							}
							
						}
												
						NodeList nodeLista2 = documento.getElementsByTagName("ImptoReten");
						String impuesto="";
						String valorImpuesto="";
						List impuestoList = new ArrayList();
						int linea=0;
						for (int si = 0; si < nodeLista2.getLength(); si++){
							Node primerNodo2 = nodeLista2.item(si);
							
							
						
							Element primerElemento2 = (Element) primerNodo2;
							
							NodeList cuartaNombreElementoLista =  primerElemento2.getElementsByTagName("TipoImp");
							Element cuartaNombreElemento = (Element) cuartaNombreElementoLista.item(0);
							NodeList cuartaNombre = cuartaNombreElemento.getChildNodes();
							impuesto = ((Node) cuartaNombre.item(0)).getNodeValue().toString();
							
								NodeList quintaNombreElementoLista =primerElemento2.getElementsByTagName("MontoImp");
								Element quintaNombreElemento =(Element) quintaNombreElementoLista.item(0);
								NodeList quintaNombre = quintaNombreElemento.getChildNodes();
								valorImpuesto = ((Node) quintaNombre.item(0)).getNodeValue().toString();
								ClcdiaDTO clc = new ClcdiaDTO();
								System.out.println("Impuesto:"+impuesto);
								clc.setImpuesto(Integer.parseInt(recuperaTasaSII(impuesto)));
								clc.setMontoImpuesto(Integer.parseInt(valorImpuesto));
								
								linea =linea+1;
								impuestoList.add(clc);
							
							
							
						}
						/*if (impuestoList.size()==0 && iva.trim().length()>0 && "52".equals(doc.getCodDocumentoPP())){
							ClcdiaDTO clc = new ClcdiaDTO();
							System.out.println("Impuesto SOLO IVA:"+impuesto);
							clc.setImpuesto(2);
							clc.setMontoImpuesto(Integer.parseInt(iva));
							//impuestoList.add(clc);
						}*/
						
						
							doc.setNeto(neto);
							doc.setIva(iva);
							doc.setTotal(monTotal);
							doc.setMontoExento(exento);
							doc.setImpuestosAdicionales(impuestoList);
							}
						      }
						
						//Recupera Referencias
						nodeLista = documento.getElementsByTagName("Referencia");
						//System.out.println("Informacion de los libros");
								for (int s = 0; s < nodeLista.getLength(); s++) {
								
									Node primerNodo = nodeLista.item(s);
									String nLinea;
									String tpoRef;
									String folioRef;
									String fechaRef;
									if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
								
									Element primerElemento = (Element) primerNodo;
								
									NodeList primerNombreElementoLista =
								                        primerElemento.getElementsByTagName("NroLinRef");
									Element primerNombreElemento =
								                        (Element) primerNombreElementoLista.item(0);
									NodeList primerNombre = primerNombreElemento.getChildNodes();
									nLinea = ((Node) primerNombre.item(0)).getNodeValue().toString();
									//System.out.println("Titulo : "  + nLinea);
								
									NodeList segundoNombreElementoLista =
								        primerElemento.getElementsByTagName("TpoDocRef");
								Element segundoNombreElemento =
								        (Element) segundoNombreElementoLista.item(0);
								NodeList segundoNombre = segundoNombreElemento.getChildNodes();
								tpoRef = ((Node) segundoNombre.item(0)).getNodeValue().toString();
								//System.out.println("Titulo : "  + tpoRef);
								

								NodeList tercerNombreElementoLista =
								    primerElemento.getElementsByTagName("FolioRef");
								Element tercerNombreElemento =
								    (Element) tercerNombreElementoLista.item(0);
								NodeList tercerNombre = tercerNombreElemento.getChildNodes();
								folioRef = ((Node) tercerNombre.item(0)).getNodeValue().toString();
								if (rutProv.equals("92091000-9")){
									int numero = folioRef.length();
									folioRef = folioRef.substring(0, folioRef.length()-1);
								}
								if (rutProv.equals("96656660-4")){
									int numero = folioRef.length();
									if (isNumeric(folioRef)){
										break;
									}
									if (numero>12){
										folioRef = folioRef.substring(10, 16);
									}
								
								}
								
								
								//System.out.println("Numero de Orden de Compra:"+folioRef);
								//System.out.println("Titulo : "  + folioRef);
								
								NodeList cuartoNombreElementoLista =
								    primerElemento.getElementsByTagName("FchRef");
								Element cuartoNombreElemento =
								    (Element) cuartoNombreElementoLista.item(0);
								NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
								fechaRef = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
								//System.out.println("Titulo : "  + fechaRef);
								//|| "33".equals(tpoRef) || "033".equals(tpoRef)
								if (!tpoRef.equals("") || tpoRef!=null ){
									ref.setNumLineaRef(Integer.parseInt(nLinea));
									if (isNumeric(tpoRef)){
										ref.setTipoDocRef(Integer.parseInt("33"));

									}else{
										ref.setTipoDocRef(Integer.parseInt(tpoRef));

									}
									ref.setFechaRef(fechaRef);
									ref.setFolioDocumento(folioRef);
									refencia.add(ref);
								}
								
								
								
									}
								      }
								
								doc.setReferencias(refencia);


  }
  catch (Exception e) {
    	e.printStackTrace();
  }
  return doc;
 }

	public String recuperaTasaSII(String codigo){
	  
	  String tasas="";
	  prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		tasas=prop.getProperty(codigo);
		
	  return tasas;
}
	public String procesaCaracteres(String descripcion){
		  
		  descripcion = descripcion.replace("�", "N");
		  descripcion = descripcion.replace("�", "N");
		  descripcion = descripcion.replace("�", "");
		  descripcion = descripcion.replace("<", "&lt;");
		  descripcion = descripcion.replace(">", "&gt;");
		  descripcion = descripcion.replace("&", "&amp;");
		  descripcion = descripcion.replace("\"", "&quot;");
		  descripcion = descripcion.replace("'", "&apos;");
		  descripcion = descripcion.replace("�", "A");
		  descripcion = descripcion.replace("�", "E");
		  descripcion = descripcion.replace("�", "I");
		  descripcion = descripcion.replace("�", "O");
		  descripcion = descripcion.replace("�", "U");
		  descripcion = descripcion.replace("�", "a");
		  descripcion = descripcion.replace("�", "e");
		  descripcion = descripcion.replace("�", "i");
		  descripcion = descripcion.replace("�", "o");
		  descripcion = descripcion.replace("�", "u");
		 descripcion = descripcion.replace("N�", "N");
		 descripcion = descripcion.replace("G�", "G");
		 descripcion = descripcion.replace("�", "N");
		 descripcion = descripcion.replace("�", "N");
		 descripcion = descripcion.replace("�", "");
		 descripcion = descripcion.replace("�", "");
		 descripcion = descripcion.replace("�", "");
		 descripcion = descripcion.replace("�", "");
		  
		  return descripcion;
	}
	public String recuperaCodDocCaserita(String codigo){
		  
		  String codCaserita="";
		  prop = new Properties();
			try{
				//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			pathProperties = Constants.FILE_PROPERTIES;
			codCaserita=prop.getProperty(codigo);
			
		  return codCaserita;
	}
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return false;
		} catch (NumberFormatException nfe){
			return true;
		}
	} 
	public static void main (String[]args){
		LeerXml leer = new LeerXml();
		String url="http://192.168.1.4:8081/Facturacion/XMLServlet?docId=338X/DI3Cu(MaS)zUasuew1ysA==&store=R";
		leer.main(url);
	}
}