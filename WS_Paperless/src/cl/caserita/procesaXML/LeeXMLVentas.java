package cl.caserita.procesaXML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.DetalleDTO;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.dto.DocumentosErrorDTO;
import cl.caserita.dto.ReferenciaDocumentoDTO;

public class LeeXMLVentas {

	public DocumentosErrorDTO main(String argv) {
		String entrada;

		String cadena="";
		DocumentosErrorDTO doc = new DocumentosErrorDTO();
		List refencia = new ArrayList();
		ReferenciaDocumentoDTO ref = new ReferenciaDocumentoDTO();
		
		int cant=0;
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
					String forma="1";
				
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

						NodeList cuartoNombreElementoLista = primerElemento.getElementsByTagName("FmaPago");
					Element cuartoNombreElemento = (Element) cuartoNombreElementoLista.item(0);
					if (cuartoNombreElemento!=null){
						NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
						forma = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
					}
					
				//System.out.println("Titulo : "  + codDoc);
				//doc.setCodDocumentoPP(codDoc.trim());
				doc.setFolio(Integer.parseInt(folio));
				doc.setFechaDocumento(fecha);
				doc.setCodDocumento(Integer.parseInt(codDoc));
				doc.setFormaPago(Integer.parseInt(forma));
				/*if (!"33".equals(codDoc)){
					return doc;
				}*/
					}
				      }
					
			//Recupera datos Emisor
			nodeLista = documento.getElementsByTagName("Receptor");
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
							                        primerElemento.getElementsByTagName("RUTRecep");
								Element primerNombreElemento =
							                        (Element) primerNombreElementoLista.item(0);
								NodeList primerNombre = primerNombreElemento.getChildNodes();
								rut = ((Node) primerNombre.item(0)).getNodeValue().toString();
								//System.out.println("Titulo : "  + rut);
								doc.setRutReceptor(rut);
						
						
							//System.out.println("Titulo : "  + razon);
								
								
								}
					 }

					
					//Recupera datos Totales
					nodeLista = documento.getElementsByTagName("Totales");
					//System.out.println("Informacion de los libros");
							for (int s = 0; s < nodeLista.getLength(); s++) {
							
								Node primerNodo = nodeLista.item(s);
								String neto="0";
								String iva="0";
								String monTotal="0";
								String exento="0";
								if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
							
								Element primerElemento = (Element) primerNodo;
									
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
							
								

							NodeList tercerNombreElementoLista =
							    primerElemento.getElementsByTagName("MntTotal");
							Element tercerNombreElemento =
							    (Element) tercerNombreElementoLista.item(0);
							NodeList tercerNombre = tercerNombreElemento.getChildNodes();
							monTotal = ((Node) tercerNombre.item(0)).getNodeValue().toString();
							//System.out.println("Titulo : "  + monTotal);
							if ("34".equals(doc.getCodDocumento()) || "41".equals(doc.getCodDocumento()) ){
								NodeList cuartoNombreElementoLista =
								    primerElemento.getElementsByTagName("MntExe");
								Element cuartoNombreElemento =
								    (Element) cuartoNombreElementoLista.item(0);
								NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
								exento = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
								neto="0";
								iva="0";
							}/*else{
								NodeList cuartoNombreElementoLista =
								    primerElemento.getElementsByTagName("MntExe");
								Element cuartoNombreElemento =
								    (Element) cuartoNombreElementoLista.item(0);
								if (cuartoNombreElemento!=null){
									NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
									exento = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
								}
								
							}*/
													
							NodeList nodeLista2 = documento.getElementsByTagName("ImptoReten");
							String impuesto="";
							String valorImpuesto="";
							/*List impuestoList = new ArrayList();
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
								
								
								
							}*/
							
							
							
								doc.setTotalNeto(Integer.parseInt(neto));
								doc.setTotalIva(Integer.parseInt(iva));
								doc.setTotalDocumento(Integer.parseInt(monTotal));
								//doc.setMontoExento(exento);
								//doc.setImpuestosAdicionales(impuestoList);
								}
							      }
							
							nodeLista = documento.getElementsByTagName("Detalle");
							List deta = new ArrayList();
							//System.out.println("Informacion de los libros");
									for (int s = 0; s < nodeLista.getLength(); s++) {
										DetalleDTO detalle = new DetalleDTO();
										Node primerNodo = nodeLista.item(s);
										String nLinea;
										String codigo;
										String descripcion;
										String cantidad;
										String precioUnitario;
										String montoItem;
										if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
									
										Element primerElemento = (Element) primerNodo;
									
										NodeList primerNombreElementoLista =
									                        primerElemento.getElementsByTagName("NroLinDet");
										Element primerNombreElemento =
									                        (Element) primerNombreElementoLista.item(0);
										NodeList primerNombre = primerNombreElemento.getChildNodes();
										nLinea = ((Node) primerNombre.item(0)).getNodeValue().toString();
										//System.out.println("Titulo : "  + nLinea);
									
										NodeList segundoNombreElementoLista =
									        primerElemento.getElementsByTagName("VlrCodigo");
									Element segundoNombreElemento =
									        (Element) segundoNombreElementoLista.item(0);
									NodeList segundoNombre = segundoNombreElemento.getChildNodes();
									codigo = ((Node) segundoNombre.item(0)).getNodeValue().toString();
									//System.out.println("Titulo : "  + tpoRef);
									
									if (doc.getFolio()==15652842){
										System.out.println("error");
									}
									NodeList tercerNombreElementoLista =
									    primerElemento.getElementsByTagName("NmbItem");
									Element tercerNombreElemento =
									    (Element) tercerNombreElementoLista.item(0);
									NodeList tercerNombre = tercerNombreElemento.getChildNodes();
									descripcion = ((Node) tercerNombre.item(0)).getNodeValue().toString();
									int index = descripcion.length();
									int inicio = 40;
									System.out.println("Descripcion:"+descripcion);
									String precioBruto="0";
									if (descripcion.length()==47){
										 precioBruto = descripcion.substring(descripcion.length()-7, descripcion.length());
										precioBruto = precioBruto.trim().replace("@", "");
									}else{
										 precioBruto = descripcion.substring(descripcion.length()-7, descripcion.length());
											precioBruto = precioBruto.trim().replace("@", "");
										//precioBruto="0";
									}
									
									
									
									//System.out.println("Numero de Orden de Compra:"+folioRef);
									//System.out.println("Titulo : "  + folioRef);
									
									NodeList cuartoNombreElementoLista =
									    primerElemento.getElementsByTagName("QtyItem");
									Element cuartoNombreElemento =
									    (Element) cuartoNombreElementoLista.item(0);
									NodeList cuartoNombre = cuartoNombreElemento.getChildNodes();
									cantidad = ((Node) cuartoNombre.item(0)).getNodeValue().toString();
									
									
									NodeList quintoNombreElementoLista =
										    primerElemento.getElementsByTagName("PrcItem");
										Element quintoNombreElemento =
										    (Element) quintoNombreElementoLista.item(0);
										NodeList quintoNombre = quintoNombreElemento.getChildNodes();
										precioUnitario = ((Node) quintoNombre.item(0)).getNodeValue().toString();
										
										
										NodeList sextoNombreElementoLista =
											    primerElemento.getElementsByTagName("MontoItem");
											Element sextoNombreElemento =
											    (Element) sextoNombreElementoLista.item(0);
											NodeList sextoNombre = sextoNombreElemento.getChildNodes();
											montoItem = ((Node) sextoNombre.item(0)).getNodeValue().toString();
										
											String descuento="0";
											NodeList septimoNombreElementoLista =
												    primerElemento.getElementsByTagName("DescuentoMonto");
												Element septimoNombreElemento =
												    (Element) septimoNombreElementoLista.item(0);
												if (septimoNombreElemento!=null){
													NodeList septimoNombre = septimoNombreElemento.getChildNodes();
													descuento = ((Node) septimoNombre.item(0)).getNodeValue().toString();
												}
										cant = cant+1;
									//System.out.println("Titulo : "  + fechaRef);
									//|| "33".equals(tpoRef) || "033".equals(tpoRef)
									detalle.setNumeroLinea(Integer.parseInt(nLinea));
									detalle.setCodigoArticulo(codigo);
									detalle.setDescripcion(descripcion);
									detalle.setCantidad(Integer.parseInt(cantidad));
									detalle.setPrecioNeto(Double.parseDouble(precioUnitario));
									detalle.setMontoNeto(Integer.parseInt(montoItem));
									detalle.setMontoDescuento(Double.parseDouble(descuento));
									detalle.setPrecioBruto(Double.parseDouble(precioBruto.trim()));
									deta.add(detalle);
									
									
										}
									      }
									doc.setListaDetalle(deta);
									doc.setCantidadLineas(cant);


	  }
	  catch (Exception e) {
	    	e.printStackTrace();
	  }
	  return doc;
	 }
	
}
