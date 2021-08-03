package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.Conar1DAO;
import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dao.iface.ExmodcDAO;
import cl.caserita.dao.iface.PrmprvDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.Conar1DTO;
import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.PrmprvDTO;
import cl.caserita.dto.RecepDocumentoDTO;
import cl.caserita.dto.ReferenciaDocumentoDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.enviomail.main.emailInformacionRecepcion;
import cl.caserita.procesaXML.LeerXml;

public class procesaRecepcionHelper {
	private  static Logger logi = Logger.getLogger(procesaRecepcionHelper.class);

	public static String tipoIngreso="";
	private static Properties prop=null;
	private static String pathProperties;
	private static int numerador=0;
	private static emailInformacionRecepcion mail = new emailInformacionRecepcion();
	DAOFactory factory = DAOFactory.getInstance();

	//Procesa recepcion de documentos
	public void procesaRecepcin(int empresa, String fechaProceso){
		List docu = new ArrayList();
		WsClient ws = new WsClient();

		prop = new Properties();
		try{
			//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		try{
			docu = procesaDocumentos(ws.onlineGestionRec(empresa, endPoint) );
			//docu = procesaDocumentos(ws.onlineRecoveryRecList(empresa, "", fechaProceso));
			
			tipoIngreso="A";
			Iterator iter = docu.iterator();
			while (iter.hasNext()){
				RecepDocumentoDTO recep = (RecepDocumentoDTO) iter.next();
				logi.info("Numero Documento:"+recep.getNumeroDocumento());
				logi.info("Rut :"+recep.getRutProveedor());
				logi.info("Codigo :"+recep.getCodigoDoc());
				String xml = ws.onlineRecoveryRec(Integer.parseInt(recep.getRutProveedor()), recep.getCodigoDoc(), recep.getNumeroDocumento(),empresa, endPoint);
				String url = ws.onlineRecoveryRecUrl(Integer.parseInt(recep.getRutProveedor()), recep.getCodigoDoc(), recep.getNumeroDocumento(),empresa,endPoint);
				logi.info("URL Recepcion:"+url);
				LeerXml leer = new LeerXml();
				
				procesaDoc(leer.main(xml),empresa,0,url,0);
				//break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	//Procesa String de documentos de proveedor	
	public void procesaDocumentoIndividual(int codDoc, int numDoc, int rut, int empresa, int numeroOCInd){
		WsClient ws = new WsClient();
		try
		{
			tipoIngreso="M";
			logi.info("Datos Recepcion Individual");
			logi.info("Rut:"+rut);
			logi.info("CodDoc:"+codDoc);
			logi.info("NumeroDoc:"+numDoc);
			logi.info("Empresa:"+empresa);
			String xml = ws.onlineRecoveryRec(rut, codDoc, numDoc, empresa,"");
			String url = ws.onlineRecoveryRecUrl(rut, codDoc, numDoc, empresa,"");
			
			logi.info("URL Recepcion:"+url);
			logi.info("XML:"+xml);
			LeerXml leer = new LeerXml();
			procesaDoc(leer.main(xml),empresa,1,url,numeroOCInd);
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
			 		//logi.info("Token: " + stad.nextToken( ));
			 		if (num==0){
			 			String rut = stad.nextToken();
			 			/*String digito="";
			 			if (rut.length()==10){
			 				digito=rut.substring(9, 10);
			 				rut = rut.substring(0, 8);
			 			}else if (rut.length()==9){
			 				digito=rut.substring(9, 9);
			 				rut = rut.substring(0, 7);
			 			}*/
			 			logi.info("Rut:"+rut);
			 			recep.setRutProveedor(rut);
			 		}else if (num==1){
			 			recep.setCodigoDoc(Integer.parseInt(stad.nextToken()));
			 			logi.info("Codigo Documento:"+recep.getCodigoDoc());
			 		}else if (num==2){
			 			String numeroDoc = stad.nextToken();
			 			//int nume = Integer.parseInt(numeroDoc);
			 			numeroDoc = numeroDoc.replaceAll("\n", "");
			 			//logi.info("Numero" + numeroDoc);
			 			recep.setNumeroDocumento(Integer.parseInt(numeroDoc));
			 			logi.info("Numero Documento:"+recep.getNumeroDocumento());
			 		}/*else if (num==3){
			 			stad.nextToken();
			 		}else if (num==4){
			 			stad.nextToken();
			 		}*/
			 		
			 		num++;
			 	}
			 	doc.add(recep);
			 
	        
	    }
		
		
		return doc;
		
	}
	public void procesaDoc(DocumentoElectronicoDTO doc, int rutEmpresa, int ind, String url, int numeroOCInd){
		ExmodcDAO exmodc = (ExmodcDAO) factory.getExmodcDAO();
		ConarcDAO conarc = (ConarcDAO) factory.getConarcDAO();
		Conar1DAO conar1 = (Conar1DAO) factory.getConar1DAO();
		TptempDAO tpempDAO = (TptempDAO) factory.getTptempDAO();
		TptdeleDAO tpdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
		///PrmprvDAO prmprvDAO  = (PrmprvDAO) factory.getPrmprvDAO();
		ExmodcDTO exmodcDTO = null;
		ConarcDTO conarcDTO = null;
		Conar1DTO conar1DTO = null;
		TptempDTO tptempDTO = null;
		//PrmprvDTO prmprvDTO = null;
		tptempDTO = tpempDAO.recuperaEmpresaPorRut(rutEmpresa);
		//Verifica si existe orden de compra
		logi.info("Ingresa Proceso de FV");
		logi.info("Codigo Documento:"+doc.getCodDocumento());
		logi.info("Numero Documento1:"+doc.getFolio());
		logi.info("Rut Proveedor:"+doc.getRutProveedor());
		int codigo = Integer.parseInt(doc.getCodDocumento());
		int codigoCaserita = tpdeleDAO.buscaDocumentoElectronicoPP(Integer.parseInt(doc.getCodDocumento()));
		//logi.info("Codigo Documento1:"+codigo);
		numerador=numerador+1;
		logi.info("Cantidad Documentos:"+numerador);
		try{
			if (codigo==33 || codigo==34){

			String rut="";
			String dv="";
			if (doc.getRutProveedor()==null){
				return;
			}
			if (doc.getRutProveedor().length()>=10){
				rut = doc.getRutProveedor().substring(0, 8);
				dv = doc.getRutProveedor().substring(9, 10);
			}else{
				rut = doc.getRutProveedor().substring(0, 7);
				dv = doc.getRutProveedor().substring(8, 9);
			}
			 
			 
				String ano = doc.getFecha().substring(0, 4);
				String mes = doc.getFecha().substring(5, 7);
				String dia = doc.getFecha().substring(8, 10);
				String fecha = ano+mes+dia;
				int fechaDocum = Integer.parseInt(fecha);
				if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
				{
					return;
				}
				
			if (doc.getReferencias()!=null && doc.getReferencias().size()>0){
			Iterator iter = doc.getReferencias().iterator();
			while (iter.hasNext()){
				ReferenciaDocumentoDTO ref = (ReferenciaDocumentoDTO) iter.next();
				if (ref.getTipoDocRef()>0){
						//Verifica si numero documento electronico es numerico
					logi.info("PROCESA CON REFERENCIA1");
						if (isNumeric(ref.getFolioDocumento())){
							ref.setFolioDocumento("1");
							logi.info("SALE DE PROCESO POR NUMERO CARACTER1");
							//break;
						}
						int numeroOC = Integer.parseInt(ref.getFolioDocumento());

						//Verifica si fecha documento es superior a 2014 para procesar
						if (fechaDocum<=20131231){
							break;
						}
						//Busca documento en CONARC si lo encuentra no lo procesa
						logi.info("PROCESA CON REFERENCIA2");
						if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
							
							//Verifica documento es numerico y si no es individual
							if (isNumeric(ref.getFolioDocumento()) && ind==0){
								ref.setFolioDocumento("1");
								logi.info("SALE DE PROCESO POR NUMERO CARACTER2");
								//break;
							}else{
								conarcDTO = new ConarcDTO();
								 conarcDTO.setFolio(0);	
								
								conarcDTO.setCodDocumento(codigoCaserita);
								
								conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
								conarcDTO.setRutProveedor(Integer.parseInt(rut));
								conarcDTO.setDigitoProveedor(dv);
								conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
								if (doc.getRazonSocialProveedor().length()>40){
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
								}else{
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
								}
								
								conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
								conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
								conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
								if (doc.getMontoExento()!="" || doc.getMontoExento()!=null){
									//conarcDTO.setValorNetoExento(Integer.parseInt(doc.getMontoExento()));
								}
								
								conarcDTO.setAno(0);
								conarcDTO.setMes(0);
								conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
								conarcDTO.setDireccion(doc.getDireccionProveedor());
								//conarc.generaDoc(conarcDTO);
								//int folioOC= Integer.parseInt(ref.getFolioDocumento());
								double neto=Double.parseDouble(doc.getNeto());
								double total=Double.parseDouble(doc.getTotal()); 
								double iva = Double.parseDouble(doc.getIva());
								
								double adicional=0;
								double exento=0;

								if (doc.getMontoExento().toString().length()>0){
									 exento = Double.parseDouble(doc.getMontoExento());
									 if (codigo==34){
											exento=Double.parseDouble(doc.getMontoExento());
										}
								}

								if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
									neto=0;
									iva=0;
									exento = total;
									doc.setImpuestosAdicionales(null);
									codigoCaserita=36;
									
								}
								//Verifica si documento existe en tabla de paso EXDFCPR
								if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
								{
									logi.info("EXISTE EN EXDFCPR");
									break;
								}
								//Verifica que el documento posea impuestos
								if (doc.getImpuestosAdicionales()!=null){
									if (doc.getImpuestosAdicionales().size()>0){
										
										//Graba impuestos IVA y adicionales
										Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
										logi.info("procesa generacion impto");
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										while (iterImpuesto.hasNext()){
											ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
											if (clcdiaDTO.getCodigoImpuesto()==2){
												//iva = clcdiaDTO.getMontoImpuesto();
												
											}else{
												adicional = adicional + clcdiaDTO.getMontoImpuesto();
											}
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
											
										}
									}else{
										//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
										if (iva>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
											if (exento>0){
												conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
											}
										}else if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
										
									}
								}else{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}
								logi.info("INSERTA FACTURA JAIME CANQ");
								conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),33,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
								
							
								
							}
							//Verifica si numero OC es mayor a 320000 
							
							logi.info("BUsca Documento en EXDFCPR");
							//Verifica si movimiento esta creado en EXDFCPR
							if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
							{
								logi.info("Procesa Break");
								break;
							}
							//Verifica si existe en OC en EXMODC
							exmodcDTO = exmodc.buscaOrden(Integer.parseInt(ref.getFolioDocumento()), Integer.parseInt(rut), dv);
							if (exmodcDTO!=null){
								if (exmodcDTO.getTotalOrden()!=0){
									 conarcDTO = new ConarcDTO();
									 conarcDTO.setFolio(0);	
									
									conarcDTO.setCodDocumento(codigoCaserita);
									
									conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
									conarcDTO.setRutProveedor(Integer.parseInt(rut));
									conarcDTO.setDigitoProveedor(dv);
									conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
									if (doc.getRazonSocialProveedor().length()>40){
										conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
									}else{
										conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
									}
									
									conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
									conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
									conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
									conarcDTO.setAno(0);
									conarcDTO.setMes(0);
									conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
									conarcDTO.setDireccion(doc.getDireccionProveedor());
									//conarc.generaDoc(conarcDTO);
									int folioOC= Integer.parseInt(ref.getFolioDocumento());
									double neto=Double.parseDouble(doc.getNeto());
									double total=Double.parseDouble(doc.getTotal()); 
									double iva = Double.parseDouble(doc.getIva());
									double adicional=0;
									double exento=0;
									if (doc.getMontoExento().toString().length()>0){
										 exento = Double.parseDouble(doc.getMontoExento());
										 if (codigo==34){
												exento=Double.parseDouble(doc.getMontoExento());
											}
									}
									//Verifica si es bat el proveedor
									if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
										neto=0;
										iva=0;
										exento = total;
										doc.setImpuestosAdicionales(null);
										codigoCaserita=36;
										
									}
									if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
									{
										break;
									}
									if (doc.getImpuestosAdicionales()!=null){
									if (doc.getImpuestosAdicionales().size()>0){
										logi.info("procesa generacion impto2");
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
										while (iterImpuesto.hasNext()){
											ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
											if (clcdiaDTO.getCodigoImpuesto()==2){
												//iva = clcdiaDTO.getMontoImpuesto();
												
											}else{
												adicional = adicional + clcdiaDTO.getMontoImpuesto();
											}
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
											//conar1DTO.setCodigoImpuesto(clcdiaDTO.getCodigoImpuesto());
											//conar1DTO.setMontoImpuesto(clcdiaDTO.getMontoImpuesto());
											//conar1.generaConar1(conar1DTO);
										}
									}else{
										if (iva>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
											if (exento>0){
												conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
											}
										}else if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
										
									}
									}else{
										if (iva>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
											if (exento>0){
												conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
											}
										}else if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
										
									}
									logi.info("INSERTA FACTURA JAIME C2");
									conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
									
									
									
									
							}
							}else
							{
								    //Graba sin OC encontrada en EXMODC
									conarcDTO = new ConarcDTO();
									conarcDTO.setFolio(0);	
									conarcDTO.setCodDocumento(codigoCaserita);
									conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
									conarcDTO.setRutProveedor(Integer.parseInt(rut));
									conarcDTO.setDigitoProveedor(dv);
									conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
									if (doc.getRazonSocialProveedor().length()>40){
										conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
									}else{
										conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
									}
									
									conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
									conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
									conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
									conarcDTO.setAno(0);
									conarcDTO.setMes(0);
									conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
									conarcDTO.setDireccion(doc.getDireccionProveedor());
									int folioOC= Integer.parseInt(ref.getFolioDocumento());
									//Verifica si documento ya se encuentra en EXDFCPR
									if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
									{
										break;
									}
								
									double neto=Double.parseDouble(doc.getNeto());
									double total=Double.parseDouble(doc.getTotal()); 
									double iva = Double.parseDouble(doc.getIva());
									double adicional=0;
									double exento=0;
									if (doc.getMontoExento().toString().length()>0){
										 exento = Double.parseDouble(doc.getMontoExento());
										 if (codigo==34){
												exento=Double.parseDouble(doc.getMontoExento());
											}
									}
									if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
										neto=0;
										iva=0;
										exento = total;
										doc.setImpuestosAdicionales(null);
										codigoCaserita=36;
										
									}
									//Verifica si el documento posee impuestos adicionales
									if (doc.getImpuestosAdicionales()!=null){
									if (doc.getImpuestosAdicionales().size()>0){
										//Inserta impuestos adicionales e IVA
										logi.info("procesa generacion impto3");
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
										while (iterImpuesto.hasNext()){
											ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
											if (clcdiaDTO.getCodigoImpuesto()==2){
												//iva = clcdiaDTO.getMontoImpuesto();
												
											}else{
												adicional = adicional + clcdiaDTO.getMontoImpuesto();
											}
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
										
										}
									}else{
										//Inserta impuesto IVA
										if (iva>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
											if (exento>0){
												conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
											}
										}else if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
										
									}}else{
										//Inserta impuesto IVA
										if (iva>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
											if (exento>0){
												conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
											}
										}else if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
										
									}
									logi.info("INSERTA FACTURA JAIME C3");
									conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
									
									
							
							
								logi.info("Orden de compra no existe, pero inserta asociado a otra OC");
							}
							
							
					
					
						
					}
						//Procesa Documento con codigo documento 36 que es igual a factura exenta
						else if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0 && doc.getMontoExento().length()>0){
						//Verifica si numero documento es numerico y si el proceso no es individual	
						if (isNumeric(ref.getFolioDocumento()) && ind==0){
							ref.setFolioDocumento("1");
							//break;
						}else{
							conarcDTO = new ConarcDTO();
							conarcDTO.setFolio(0);	
							
							conarcDTO.setCodDocumento(codigoCaserita);
							
							conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
							conarcDTO.setRutProveedor(Integer.parseInt(rut));
							conarcDTO.setDigitoProveedor(dv);
							conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
							if (doc.getRazonSocialProveedor().length()>40){
								conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
							}else{
								conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
							}
							
							conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
							conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
							conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
							conarcDTO.setAno(0);
							conarcDTO.setMes(0);
							conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
							conarcDTO.setDireccion(doc.getDireccionProveedor());
							double neto=0;
							double total=Double.parseDouble(doc.getTotal()); 
							double iva = 0;
							double exento=0;
							if (doc.getMontoExento().toString().length()>0){
								 exento = Double.parseDouble(doc.getMontoExento());
								 if (codigo==34){
										exento=Double.parseDouble(doc.getMontoExento());
									}
							}
							//Double.parseDouble(doc.getMontoExento());
							double adicional=0;
							//Verifica si documento esta en EXDFCPR si es asi no lo vuelve a crear
							if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
							{
								break;
							}
							//Verifica si existen impuestos adicionales para procesar
							if (doc.getImpuestosAdicionales()!=null){
							if (doc.getImpuestosAdicionales().size()>0){
								//Recorre impuestos adicionales para insertar ademas del IVA
								Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
								while (iterImpuesto.hasNext()){
									ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
									if (clcdiaDTO.getCodigoImpuesto()==2){
										//iva = clcdiaDTO.getMontoImpuesto();
										
									}else{
										adicional = adicional + clcdiaDTO.getMontoImpuesto();
									}
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),36,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
									//conar1DTO.setCodigoImpuesto(clcdiaDTO.getCodigoImpuesto());
									//conar1DTO.setMontoImpuesto(clcdiaDTO.getMontoImpuesto());
									//conar1.generaConar1(conar1DTO);
								}
							}else{
								//Si no tiene impuestos adicionales solo graba IVA
								if (iva>0){
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
								}else if (exento>0){
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
								}
								
							}}else{
								//Si no tiene impuestos adicionales solo graba IVA
								if (iva>0){
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									}else if (exento>0){
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
								}
								
							}
							//Graba documento en EXDFCPR
							conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
							
							
							
						}
						
						//Verifica si existe OC en EXMODC para documento 36 facturas exentas
						exmodcDTO = exmodc.buscaOrden(Integer.parseInt(ref.getFolioDocumento()), Integer.parseInt(rut), dv);
						if (exmodcDTO!=null){
							if (exmodcDTO.getTotalOrden()!=0){
								 conarcDTO = new ConarcDTO();
								 conarcDTO.setFolio(0);	
								
								conarcDTO.setCodDocumento(codigoCaserita);
								
								conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
								conarcDTO.setRutProveedor(Integer.parseInt(rut));
								conarcDTO.setDigitoProveedor(dv);
								conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
								if (doc.getRazonSocialProveedor().length()>40){
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
								}else{
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
								}
								
								conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
								conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
								conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
								conarcDTO.setAno(0);
								conarcDTO.setMes(0);
								conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
								conarcDTO.setDireccion(doc.getDireccionProveedor());
								//conarc.generaDoc(conarcDTO);
								int folioOC= Integer.parseInt(ref.getFolioDocumento());
								double neto=0;
								double total=Double.parseDouble(doc.getTotal()); 
								double iva = 0;
								double exento=0;//Double.parseDouble(doc.getMontoExento());
								if (doc.getMontoExento().toString().length()>0){
									 exento = Double.parseDouble(doc.getMontoExento());
									 if (codigo==34){
											exento=Double.parseDouble(doc.getMontoExento());
										}
								}
								double adicional=0;
								if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
								{
									break;
								}
								if (doc.getImpuestosAdicionales()!=null){
								if (doc.getImpuestosAdicionales().size()>0){
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
									while (iterImpuesto.hasNext()){
										ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
										if (clcdiaDTO.getCodigoImpuesto()==2){
											//iva = clcdiaDTO.getMontoImpuesto();
											
										}else{
											adicional = adicional + clcdiaDTO.getMontoImpuesto();
										}
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
										//conar1DTO.setCodigoImpuesto(clcdiaDTO.getCodigoImpuesto());
										//conar1DTO.setMontoImpuesto(clcdiaDTO.getMontoImpuesto());
										//conar1.generaConar1(conar1DTO);
									}
								}else{
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}}else{
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}
								
								conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
								
								
								
								
						}
						}else
						{

								//Si OC no existe, genera el movimiento con el mismo numero de documento en el numero OC
								 conarcDTO = new ConarcDTO();
								 conarcDTO.setFolio(0);	
			
								conarcDTO.setCodDocumento(codigoCaserita);
								
								conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
								conarcDTO.setRutProveedor(Integer.parseInt(rut));
								conarcDTO.setDigitoProveedor(dv);
								conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
								if (doc.getRazonSocialProveedor().length()>40){
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
								}else{
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
								}
								
								conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
								if (doc.getNeto()!=null){
									conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
								}
								
								conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
								conarcDTO.setAno(0);
								conarcDTO.setMes(0);
								conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
								conarcDTO.setDireccion(doc.getDireccionProveedor());
								int folioOC= Integer.parseInt(ref.getFolioDocumento());
								if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
								{
									break;
								}
								//conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),33);
								double neto=0;
								double total=Double.parseDouble(doc.getTotal()); 
								//double iva = Double.parseDouble(doc.getIva());
								double iva =0;
								double exento = 0;
								
								if (doc.getMontoExento().toString().length()>0){
									 exento = Double.parseDouble(doc.getMontoExento());
									 if (codigo==34){
											exento=Double.parseDouble(doc.getMontoExento());
										}
								}
								
								double adicional=0;
								//Verifica si existe impuestos adicionales
								if (doc.getImpuestosAdicionales()!=null){
								if (doc.getImpuestosAdicionales().size()>0){
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
									while (iterImpuesto.hasNext()){
										ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
										if (clcdiaDTO.getCodigoImpuesto()==2){
											//iva = clcdiaDTO.getMontoImpuesto();
											
										}else{
											adicional = adicional + clcdiaDTO.getMontoImpuesto();
										}
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
										//conar1DTO.setCodigoImpuesto(clcdiaDTO.getCodigoImpuesto());
										//conar1DTO.setMontoImpuesto(clcdiaDTO.getMontoImpuesto());
										//conar1.generaConar1(conar1DTO);
									}
								}else{
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}}else{
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}
								
								conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(folioOC), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
								
						
						
							logi.info("Orden de compra no existe, pero inserta asociado a otra OC");
						}
				}
					
				}
				
				
			}
		}else{
			//Si documento no posee documento de referencia procesa de igual forma el documento
			//Verifica si documento existe en CONARC
			//Procesa documento de forma individual
			logi.info("PROCESA SIN REFERENCIA");
			if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
				//Verifica si existe documento en EXDFCPR
				if (numeroOCInd==0){
					numeroOCInd=Integer.parseInt(doc.getFolio());
				}
				if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
				{
					logi.info("SALE SIN REFERENCIA, BUSCA EXDFCPR");
					return;
				}
				logi.info("PROCESA SIN REFERENCIA NUMERO 2:"+doc.getFolio());
				
					conarcDTO = new ConarcDTO();
					 //conarcDTO.setFolio(0);	
					
					conarcDTO.setCodDocumento(codigoCaserita);
					//conarcDTO.setFolio(Integer.parseInt(doc.getFolio()));
					conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
					conarcDTO.setRutProveedor(Integer.parseInt(rut));
					conarcDTO.setDigitoProveedor(dv);
					conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
					if (doc.getRazonSocialProveedor().length()>40){
						conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
					}else{
						conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
					}
					
					conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio().trim()));
					if (doc.getNeto()!=null){
						conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto().trim()));

					}
					conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal().trim()));
					conarcDTO.setAno(0);
					conarcDTO.setMes(0);
					conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
					conarcDTO.setDireccion(doc.getDireccionProveedor());
					//conarc.generaDoc(conarcDTO);
					//int folioOC= Integer.parseInt(ref.getFolioDocumento());
					double neto=Double.parseDouble(doc.getNeto());
					double total=Double.parseDouble(doc.getTotal()); 
					double iva = Double.parseDouble(doc.getIva());
					double adicional=0;
					double exento=0;
					if (doc.getMontoExento().toString().length()>0){
						 exento = Double.parseDouble(doc.getMontoExento());
						 if (codigo==34){
								exento=Double.parseDouble(doc.getMontoExento());
							}
					}
					
					//double exento=0;
					
					if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
						neto=0;
						iva=0;
						exento = total;
						doc.setImpuestosAdicionales(null);
						codigoCaserita=36;
						
					}
					//Si no es individual mueve el mismo numero de documento
					
					//Verifica si documento posee impuestos adicionales
					if (doc.getImpuestosAdicionales()!=null){
					if (doc.getImpuestosAdicionales().size()>0 ){
						Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
						logi.info("procesa generacion impto");
						conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
						while (iterImpuesto.hasNext()){
							ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
							if (clcdiaDTO.getCodigoImpuesto()==2){
								//iva = clcdiaDTO.getMontoImpuesto();
								
							}else{
								adicional = adicional + clcdiaDTO.getMontoImpuesto();
							}
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
							//conar1DTO.setCodigoImpuesto(clcdiaDTO.getCodigoImpuesto());
							//conar1DTO.setMontoImpuesto(clcdiaDTO.getMontoImpuesto());
							//conar1.generaConar1(conar1DTO);
						}
					}else{
						if (iva>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
							if (exento>0){
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
							}
						}else if (exento>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
						}
						
					}}else{
						if (iva>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
							if (exento>0){
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
							}
						}else if (exento>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
						}
						
					}
					logi.info("INSERTA FACTURA JAIME C");
					conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),String.valueOf(numeroOCInd), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
					
			
					logi.info("Orden de compra no existe, pero inserta asociado a otra OC");
				
				
				
		
		
			
		}
		}
	
		}
		//Procesa NC Proveedor desde Paperless
		
		else if (codigo==61 ){
			logi.info("Procesa Notas Credito");
			String rut="";
			String dv="";
			if (doc.getRutProveedor().length()>=10){
				rut = doc.getRutProveedor().substring(0, 8);
				dv = doc.getRutProveedor().substring(9, 10);
			}else{
				rut = doc.getRutProveedor().substring(0, 7);
				dv = doc.getRutProveedor().substring(8, 9);
			}
			if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
			{
				logi.info("Procesa Notas Credito2");
				return;
			}
			 
				String ano = doc.getFecha().substring(0, 4);
				String mes = doc.getFecha().substring(5, 7);
				String dia = doc.getFecha().substring(8, 10);
				String fecha = ano+mes+dia;
				int fechaDocum = Integer.parseInt(fecha);
			if (doc.getReferencias().size()>0){
			Iterator iter = doc.getReferencias().iterator();
			logi.info("Procesa Notas Credito3");
			int numeroIn=0;
			while (iter.hasNext()){
				ReferenciaDocumentoDTO ref = (ReferenciaDocumentoDTO) iter.next();
				if (numeroIn>=1){
					break;
				}
				if (ref.getTipoDocRef()>0 ){
					logi.info("Procesa Notas Credito4");
						//Verifica si numero documento electronico es numerico
					if (isNumeric(ref.getFolioDocumento()) && ind==0){
						ref.setFolioDocumento("1");
						logi.info("SALE DE PROCESO POR NUMERO CARACTER2");
						//break;
					}
						int numeroOC = Integer.parseInt(ref.getFolioDocumento());

						//Verifica si fecha documento es superior a 2014 para procesar
						
						//Busca documento en CONARC si lo encuentra no lo procesa
						if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
							logi.info("Procesa Notas Credito5");
							//Verifica documento es numerico y si no es individual
							if (isNumeric(ref.getFolioDocumento()) && ind==0){
								ref.setFolioDocumento("1");
								
								//break;
							}else{
								conarcDTO = new ConarcDTO();
								 conarcDTO.setFolio(0);	
								
								conarcDTO.setCodDocumento(codigoCaserita);
								
								conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
								conarcDTO.setRutProveedor(Integer.parseInt(rut));
								conarcDTO.setDigitoProveedor(dv);
								conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
								if (doc.getRazonSocialProveedor().length()>40){
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
								}else{
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
								}
								
								conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
								conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
								conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
								conarcDTO.setAno(0);
								conarcDTO.setMes(0);
								conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
								conarcDTO.setDireccion(doc.getDireccionProveedor());
								//conarc.generaDoc(conarcDTO);
								//int folioOC= Integer.parseInt(ref.getFolioDocumento());
								double neto=Double.parseDouble(doc.getNeto());
								double total=Double.parseDouble(doc.getTotal()); 
								double iva = Double.parseDouble(doc.getIva());
								double adicional=0;
								double exento=0;
								logi.info("IVA:"+iva);
								//Verifica si documento existe en tabla de paso EXDFCPR
								if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
								{
									//break;
								}
								if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
									neto=0;
									iva=0;
									exento = total;
									doc.setImpuestosAdicionales(null);
									codigoCaserita=42;
									
								}
								//Verifica que el documento posea impuestos
								if (doc.getImpuestosAdicionales()!=null){
								if (doc.getImpuestosAdicionales().size()>0){
									
									//Graba impuestos IVA y adicionales
									Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
									logi.info("procesa generacion impto");
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									while (iterImpuesto.hasNext()){
										ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
										if (clcdiaDTO.getCodigoImpuesto()==2){
											//iva = clcdiaDTO.getMontoImpuesto();
											
										}else{
											adicional = adicional + clcdiaDTO.getMontoImpuesto();
										}
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
										
									}
								}else
									{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}}
								}else{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}
								logi.info("INSERTA FACTURA JAIME CANQ");
								conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
							
								
							}
							
					
					
						
					}
						
						
					
				}
				numeroIn=numeroIn+1;
				
			}
		}
		}else if (codigo==52 ){
			logi.info("Procesa Guias Despacho");
			String rut="";
			String dv="";
			if (doc.getRutProveedor().length()>=10){
				rut = doc.getRutProveedor().substring(0, 8);
				dv = doc.getRutProveedor().substring(9, 10);
			}else{
				rut = doc.getRutProveedor().substring(0, 7);
				dv = doc.getRutProveedor().substring(8, 9);
			}
			if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
			{
				logi.info("Procesa Notas Credito2");
				return;
			}
			 
				String ano = doc.getFecha().substring(0, 4);
				String mes = doc.getFecha().substring(5, 7);
				String dia = doc.getFecha().substring(8, 10);
				String fecha = ano+mes+dia;
				int fechaDocum = Integer.parseInt(fecha);
			if (doc.getReferencias().size()>0){
			Iterator iter = doc.getReferencias().iterator();
			logi.info("Procesa Notas Credito3");
			int numeroIn=0;
			while (iter.hasNext()){
				ReferenciaDocumentoDTO ref = (ReferenciaDocumentoDTO) iter.next();
				if (numeroIn>=1){
					break;
				}
				if (ref.getTipoDocRef()>0 ){
					logi.info("Procesa Notas Credito4");
						//Verifica si numero documento electronico es numerico
					if (isNumeric(ref.getFolioDocumento()) && ind==0){
						ref.setFolioDocumento("1");
						logi.info("SALE DE PROCESO POR NUMERO CARACTER2");
						//break;
					}
						int numeroOC = Integer.parseInt(ref.getFolioDocumento());

						//Verifica si fecha documento es superior a 2014 para procesar
						
						//Busca documento en CONARC si lo encuentra no lo procesa
						if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
							logi.info("Procesa Notas Credito5");
							//Verifica documento es numerico y si no es individual
							if (isNumeric(ref.getFolioDocumento()) && ind==0){
								ref.setFolioDocumento("1");
								
								//break;
							}else{
								conarcDTO = new ConarcDTO();
								 conarcDTO.setFolio(0);	
								
								conarcDTO.setCodDocumento(codigoCaserita);
								
								conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
								conarcDTO.setRutProveedor(Integer.parseInt(rut));
								conarcDTO.setDigitoProveedor(dv);
								conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
								if (doc.getRazonSocialProveedor().length()>40){
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
								}else{
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
								}
								
								conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
								conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
								conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
								conarcDTO.setAno(0);
								conarcDTO.setMes(0);
								conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
								conarcDTO.setDireccion(doc.getDireccionProveedor());
								//conarc.generaDoc(conarcDTO);
								//int folioOC= Integer.parseInt(ref.getFolioDocumento());
								double neto=Double.parseDouble(doc.getNeto());
								double total=Double.parseDouble(doc.getTotal()); 
								double iva=0;
								if (doc.getIva()!=null && doc.getIva().trim().length()>0){
									 iva = Double.parseDouble(doc.getIva());
								}
								
								double adicional=0;
								double exento=0;
								logi.info("IVA:"+iva);
								//Verifica si documento existe en tabla de paso EXDFCPR
								if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
								{
									//break;
								}
								if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
									neto=0;
									iva=0;
									exento = total;
									doc.setImpuestosAdicionales(null);
									codigoCaserita=42;
									
								}
								//Verifica que el documento posea impuestos
								if (doc.getImpuestosAdicionales()!=null){
								if (doc.getImpuestosAdicionales().size()>0){
									
									//Graba impuestos IVA y adicionales
									Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
									logi.info("procesa generacion impto");
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									while (iterImpuesto.hasNext()){
										ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
										if (clcdiaDTO.getCodigoImpuesto()==2){
											//iva = clcdiaDTO.getMontoImpuesto();
											
										}else{
											adicional = adicional + clcdiaDTO.getMontoImpuesto();
										}
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
										
									}
								}else
									{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}}
								}else{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}
								logi.info("INSERTA FACTURA JAIME CANQ");
								conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
							
								
							}
							
					
					
						
					}
						
						
					
				}
				numeroIn=numeroIn+1;
				
			}
		}else{
			if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
				logi.info("Procesa Notas Credito5");
				//Verifica documento es numerico y si no es individual
				
					conarcDTO = new ConarcDTO();
					 conarcDTO.setFolio(0);	
					
					conarcDTO.setCodDocumento(codigoCaserita);
					
					conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
					conarcDTO.setRutProveedor(Integer.parseInt(rut));
					conarcDTO.setDigitoProveedor(dv);
					conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
					if (doc.getRazonSocialProveedor().length()>40){
						conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
					}else{
						conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
					}
					
					conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
					conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
					conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
					conarcDTO.setAno(0);
					conarcDTO.setMes(0);
					conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
					conarcDTO.setDireccion(doc.getDireccionProveedor());
					//conarc.generaDoc(conarcDTO);
					//int folioOC= Integer.parseInt(ref.getFolioDocumento());
					double neto=Double.parseDouble(doc.getNeto());
					double total=Double.parseDouble(doc.getTotal()); 
					double iva = 0;
					double adicional=0;
					double exento=0;
					logi.info("IVA:"+iva);
					//Verifica si documento existe en tabla de paso EXDFCPR
					if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
					{
						//break;
					}
					if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
						neto=0;
						iva=0;
						exento = total;
						doc.setImpuestosAdicionales(null);
						codigoCaserita=42;
						
					}
					//Verifica que el documento posea impuestos
					if (doc.getImpuestosAdicionales()!=null){
					if (doc.getImpuestosAdicionales().size()>0){
						
						//Graba impuestos IVA y adicionales
						Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
						logi.info("procesa generacion impto");
						conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
						while (iterImpuesto.hasNext()){
							ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
							if (clcdiaDTO.getCodigoImpuesto()==2){
								//iva = clcdiaDTO.getMontoImpuesto();
								
							}else{
								adicional = adicional + clcdiaDTO.getMontoImpuesto();
							}
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
							
						}
					}else
						{
						//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
						if (iva>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
							if (exento>0){
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
							}
						}else if (exento>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
						}}
					}else{
						//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
						if (iva>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
							if (exento>0){
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
							}
						}else if (exento>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
						}
						
					}
					logi.info("INSERTA FACTURA JAIME CANQ");
					conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
				
					
				
				
		
		
			
		}
		}
			//Procesa notas de debito
		}else if (codigo==56){

			logi.info("Procesa Guias Despacho");
			String rut="";
			String dv="";
			if (doc.getRutProveedor().length()>=10){
				rut = doc.getRutProveedor().substring(0, 8);
				dv = doc.getRutProveedor().substring(9, 10);
			}else{
				rut = doc.getRutProveedor().substring(0, 7);
				dv = doc.getRutProveedor().substring(8, 9);
			}
			if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(),Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
			{
				logi.info("Procesa Notas Credito2");
				return;
			}
			 
				String ano = doc.getFecha().substring(0, 4);
				String mes = doc.getFecha().substring(5, 7);
				String dia = doc.getFecha().substring(8, 10);
				String fecha = ano+mes+dia;
				int fechaDocum = Integer.parseInt(fecha);
			if (doc.getReferencias().size()>0){
			Iterator iter = doc.getReferencias().iterator();
			logi.info("Procesa Notas Credito3");
			int numeroIn=0;
			while (iter.hasNext()){
				ReferenciaDocumentoDTO ref = (ReferenciaDocumentoDTO) iter.next();
				if (numeroIn>=1){
					break;
				}
				if (ref.getTipoDocRef()>0 ){
					logi.info("Procesa Notas Credito4");
						//Verifica si numero documento electronico es numerico
					if (isNumeric(ref.getFolioDocumento()) && ind==0){
						ref.setFolioDocumento("1");
						logi.info("SALE DE PROCESO POR NUMERO CARACTER2");
						//break;
					}
						int numeroOC = Integer.parseInt(ref.getFolioDocumento());

						//Verifica si fecha documento es superior a 2014 para procesar
						
						//Busca documento en CONARC si lo encuentra no lo procesa
						if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
							logi.info("Procesa Notas Credito5");
							//Verifica documento es numerico y si no es individual
							if (isNumeric(ref.getFolioDocumento()) && ind==0){
								ref.setFolioDocumento("1");
								
								//break;
							}else{
								conarcDTO = new ConarcDTO();
								 conarcDTO.setFolio(0);	
								
								conarcDTO.setCodDocumento(codigoCaserita);
								
								conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
								conarcDTO.setRutProveedor(Integer.parseInt(rut));
								conarcDTO.setDigitoProveedor(dv);
								conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
								if (doc.getRazonSocialProveedor().length()>40){
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
								}else{
									conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
								}
								
								conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
								conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
								conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
								conarcDTO.setAno(0);
								conarcDTO.setMes(0);
								conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
								conarcDTO.setDireccion(doc.getDireccionProveedor());
								//conarc.generaDoc(conarcDTO);
								//int folioOC= Integer.parseInt(ref.getFolioDocumento());
								double neto=Double.parseDouble(doc.getNeto());
								double total=Double.parseDouble(doc.getTotal()); 
								double iva=0;
								if (doc.getIva()!=null && doc.getIva().trim().length()>0){
									 iva = Double.parseDouble(doc.getIva());
								}
								
								double adicional=0;
								double exento=0;
								logi.info("IVA:"+iva);
								//Verifica si documento existe en tabla de paso EXDFCPR
								if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
								{
									//break;
								}
								if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
									neto=0;
									iva=0;
									exento = total;
									doc.setImpuestosAdicionales(null);
									codigoCaserita=42;
									
								}
								//Verifica que el documento posea impuestos
								if (doc.getImpuestosAdicionales()!=null){
								if (doc.getImpuestosAdicionales().size()>0){
									
									//Graba impuestos IVA y adicionales
									Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
									logi.info("procesa generacion impto");
									conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
									while (iterImpuesto.hasNext()){
										ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
										if (clcdiaDTO.getCodigoImpuesto()==2){
											//iva = clcdiaDTO.getMontoImpuesto();
											
										}else{
											adicional = adicional + clcdiaDTO.getMontoImpuesto();
										}
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
										
									}
								}else
									{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}}
								}else{
									//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
									if (iva>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
										if (exento>0){
											conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
										}
									}else if (exento>0){
										conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
									}
									
								}
								logi.info("INSERTA FACTURA JAIME CANQ");
								conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),ref.getFolioDocumento(), Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
							
								
							}
							
					
					
						
					}
						
						
					
				}
				numeroIn=numeroIn+1;
				
			}
		}else{
			if (conarc.buscaDocumento(Integer.parseInt(doc.getFolio()), codigoCaserita, Integer.parseInt(fecha), Integer.parseInt(rut), dv)==0){
				logi.info("Procesa Notas Credito5");
				//Verifica documento es numerico y si no es individual
				
					conarcDTO = new ConarcDTO();
					 conarcDTO.setFolio(0);	
					
					conarcDTO.setCodDocumento(codigoCaserita);
					
					conarcDTO.setCodigoEmpresa(tptempDTO.getCodigoEmpresa());
					conarcDTO.setRutProveedor(Integer.parseInt(rut));
					conarcDTO.setDigitoProveedor(dv);
					conarcDTO.setFechaDocumento(Integer.parseInt(fecha));
					if (doc.getRazonSocialProveedor().length()>40){
						conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim().substring(0, 40)));
					}else{
						conarcDTO.setNombreProveedor(procesaCaracteres(doc.getRazonSocialProveedor().trim()));
					}
					
					conarcDTO.setNumeroDocumento(Integer.parseInt(doc.getFolio()));
					conarcDTO.setValorNeto(Integer.parseInt(doc.getNeto()));
					conarcDTO.setValorTotalDocumento(Integer.parseInt(doc.getTotal()));
					conarcDTO.setAno(0);
					conarcDTO.setMes(0);
					conarcDTO.setNombreProveedor(doc.getRazonSocialProveedor());
					conarcDTO.setDireccion(doc.getDireccionProveedor());
					//conarc.generaDoc(conarcDTO);
					//int folioOC= Integer.parseInt(ref.getFolioDocumento());
					double neto=Double.parseDouble(doc.getNeto());
					double total=Double.parseDouble(doc.getTotal()); 
					double iva = 0;
					double adicional=0;
					double exento=0;
					logi.info("IVA:"+iva);
					//Verifica si documento existe en tabla de paso EXDFCPR
					if (conarc.buscaFolioExdfcr(tptempDTO.getCodigoEmpresa(), Integer.parseInt(doc.getFolio()), Integer.parseInt(rut), dv, codigoCaserita)>0)
					{
						//break;
					}
					if (Integer.parseInt(rut)==88502900 || Integer.parseInt(rut)==76900940){
						neto=0;
						iva=0;
						exento = total;
						doc.setImpuestosAdicionales(null);
						codigoCaserita=42;
						
					}
					//Verifica que el documento posea impuestos
					if (doc.getImpuestosAdicionales()!=null){
					if (doc.getImpuestosAdicionales().size()>0){
						
						//Graba impuestos IVA y adicionales
						Iterator iterImpuesto = doc.getImpuestosAdicionales().iterator();
						logi.info("procesa generacion impto");
						conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
						while (iterImpuesto.hasNext()){
							ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterImpuesto.next();
							if (clcdiaDTO.getCodigoImpuesto()==2){
								//iva = clcdiaDTO.getMontoImpuesto();
								
							}else{
								adicional = adicional + clcdiaDTO.getMontoImpuesto();
							}
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,clcdiaDTO.getImpuesto(),clcdiaDTO.getMontoImpuesto());
							
						}
					}else
						{
						//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
						if (iva>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
							if (exento>0){
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
							}
						}else if (exento>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
						}}
					}else{
						//Inserta Solo impuesto IVA si el documento no trae impuestos adicionales
						if (iva>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,2,iva);
							if (exento>0){
								conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
							}
						}else if (exento>0){
							conarc.generaImpuestoFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,1,exento);
						}
						
					}
					logi.info("INSERTA FACTURA JAIME CANQ");
					conarc.generaFacturas(tptempDTO.getCodigoEmpresa(),"0", Integer.parseInt(doc.getFolio()),Integer.parseInt(rut),dv.trim(),codigoCaserita,fechaDocum,neto,iva,adicional,exento,total,tipoIngreso.trim(),url.trim(),conarcDTO.getNombreProveedor(),conarcDTO.getDireccion());
				
					
				
				
		
		
			
		}
		}
		
		}
		}catch (Exception e){
			logi.info("ERROR CAIDA RECEPCION");
			logi.info("Codigo Documento:"+doc.getCodDocumento());
			logi.info("Numero Documento1:"+doc.getFolio());
			logi.info("Rut Proveedor:"+doc.getRutProveedor());
			
			e.printStackTrace();
			mail.mail("Error en demonio de recepcion :"+e.getMessage().toString() +" "+ " Numero Documento : " +doc.getFolio() + " Codigo Documento : "+doc.getCodDocumentoPP() + " Rut Proveedor :"+doc.getRutProveedor());
		}
		
		
	}
	public static void main (String[]args) throws IOException{
		
		String caracteres ="90081000,33,425|90081000,33,431|90081000,33,426|90081000,33,428|90081000,33,421|90081000,33,429|90081000,33,433|90081000,33,424|90081000,33,430|90081000,33,422|90081000,33,436|90081000,33,434|90081000,33,493|90081000,33,427|90081000,33,435|90081000,33,423|90081000,33,432|96509850,52,166|96509850,33,605|96509850,52,157|96509850,52,170|96509850,52,154|96509850,33,604|96509850,33,584|96509850,33,594|96509850,33,600|96509850,52,159|96509850,52,171|96509850,52,163|96509850,52,164|96509850,52,165|96509850,52,172|96509850,52,182|96509850,52,173|96509850,52,183|96509850,52,184|96509850,52,185|96509850,52,186|96509850,52,198|96509850,52,202|96509850,52,203|96509850,52,211|96509850,52,213|96509850,33,775|96509850,52,212|90436000,33,101|96509850,52,215|96509850,33,796|96509850,33,797";
		//procesaRecepcionHelper helper = new procesaRecepcionHelper();
		//helper.procesaDocumentos(caracteres);
		
		procesaRecepcionHelper recp = new procesaRecepcionHelper();
		
		String cadena="";
		String url2="";
		String urlF = "http://192.168.1.4:8081/Facturacion/XMLServlet?docId=338X/DI3Cu8fHUuySwxl/g==&store=R";
		
		logi.info("XML a Procesar:"+cadena);
		
		List docu = new ArrayList();
		WsClient ws = new WsClient();
		try{
		
				//logi.info("Numero Documento:"+recep.getNumeroDocumento());
				//String xml = ws.onlineRecoveryRec(Integer.parseInt(recep.getRutProveedor()), recep.getCodigoDoc(), recep.getNumeroDocumento(),2);
				//String url = ws.onlineRecoveryRecUrl(Integer.parseInt(recep.getRutProveedor()), recep.getCodigoDoc(), recep.getNumeroDocumento(),2);
				//logi.info("XML Recepcion:"+xml);
				LeerXml leer = new LeerXml();
				
				recp.procesaDoc(leer.main(urlF),76288567,0,url2,0);
				//break;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
