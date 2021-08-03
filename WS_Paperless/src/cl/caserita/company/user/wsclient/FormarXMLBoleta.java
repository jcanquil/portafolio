package cl.caserita.company.user.wsclient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

//import org.kohsuke.rngom.digested.DSchemaBuilderImpl;


import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;

import cl.caserita.dte.DTEDefType;
import cl.caserita.dte.ObjectFactory;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Totales.ImptoReten;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.ClddiaDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.paperless.boleta.BOLETADefType;
import cl.paperless.boleta.EnvioBOLETA;
import cl.paperless.boleta.PersonalizadosDefType;
import cl.paperless.boleta.BOLETADefType.Documento;
import cl.paperless.boleta.BOLETADefType.Documento.DscRcgGlobal;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado;
import cl.paperless.boleta.BOLETADefType.Documento.SubTotInfo;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.IdDoc;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.Emisor;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.Receptor;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.Totales;
import cl.paperless.boleta.BOLETADefType.Documento.Detalle;
import cl.paperless.boleta.BOLETADefType.Documento.Detalle.CdgItem;
import cl.paperless.boleta.PersonalizadosDefType.DocPersonalizado;
import cl.paperless.boleta.PersonalizadosDefType.DocPersonalizado.Impresion;
import cl.paperless.boleta.EnvioBOLETA.SetDTE;
import cl.paperless.dto.DetalleDTO;

public class FormarXMLBoleta {

	private static Documento doc = null;
	 private static Properties prop=null;
		private static String pathProperties;
		private static HashMap impuestosSII =null;
	public String xml(VecmarDTO vecmar, List listaTotal, List cldmco, ClmcliDTO clmcli, int docelec, TptempDTO tpt, ClcmcoDTO clc, PrdatcaDTO prdat,HashMap impuestoSII){
		
		String stringxml="";
		System.out.println("Procesa XML");
		BOLETADefType boleta = new BOLETADefType();
		EnvioBOLETA b = new EnvioBOLETA();
		PersonalizadosDefType perso = new PersonalizadosDefType();
		impuestosSII=impuestoSII;
		//Comentarizado para aplicar
		//DocPersonalizado docPerso = new DocPersonalizado();
		//Impresion impresion = personalizados(prdat, vecmar);
		//docPerso.setImpresion(impresion);
		//perso.getDocPersonalizado().add(docPerso);
		//b.getPersonalizados().add(perso);
		
		
		
		doc = new Documento();
		
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(tpt);
		//Comentar para aplicar
		
		if (clc.getValorNeto()>0){
			SubTotInfo subtot = new SubTotInfo();
			subtot.setNroSTI(1);
			subtot.setGlosaSTI("SUBTOTAL");
			subtot.setOrdenSTI(1);
			subtot.setSubTotNetoSTI(BigDecimal.valueOf(clc.getTotalDocumento()));
			doc.getSubTotInfo().add(subtot);
		}
		
		Receptor rec = receptor(vecmar,clmcli);
		Totales tot = totales(listaTotal, vecmar, docelec,clc);
		Detalle det = detalleDoc(cldmco);
		enc.setEmisor(emi);
		enc.setIdDoc(idoc);
		enc.setReceptor(rec);
		enc.setTotales(tot);
		doc.setID("");
		
		doc.setEncabezado(enc);
		boleta.setVersion(BigDecimal.valueOf(1.0));
		boleta.setDocumento(doc);
		
		SetDTE dte = new SetDTE();
		dte.getDTE().add(boleta);
		b.setSetDTE(dte);
		enc.setIdDoc(idoc(vecmar, docelec,prdat));
        StringWriter writer = new StringWriter();
        //genera archivo TXT
       
        try{
			final JAXBContext jc = JAXBContext.newInstance(BOLETADefType.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
			m.setProperty("jaxb.encoding", "ISO-8859-1");
			//m.JAXB_ENCODING.
		    
			
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        m.marshal(boleta, baos);
	        String xml = new String(baos.toByteArray());
	        stringxml = xml;
	        
	       
	        stringxml = stringxml.replaceAll("boletaDefType", "DTE");
	        int largo = stringxml.length();
	       // Comentarizado para aplicar
	        stringxml = stringxml.substring(0, largo-7);
	        stringxml = stringxml + obtienePersonalizados(vecmar, prdat)+"</DTE>";
	        stringxml = stringxml.replaceAll("\n","");
	        //stringxml = stringxml + "</DTE>";
			//System.out.println(stringxml);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			//System.out.println("Pruebas");
		}
		
		return stringxml;
		
	}
	
	public Emisor emisor(TptempDTO tptemp){
		Emisor emisor = new Emisor();
		
		
		
		
		emisor.setRUTEmisor(tptemp.getRut()+"-"+tptemp.getDv());
		emisor.setRznSocEmisor(tptemp.getRazonSocial().trim());
		emisor.setGiroEmisor(tptemp.getGiro().trim());
		
		//emisor.setCdgSIISucur(new BigInteger(44));
		emisor.setDirOrigen(tptemp.getDireccionCasaMatriz().trim());
		emisor.setCmnaOrigen(tptemp.getComuna().trim());
		
		
	    //se QUITA PA BOLETASemisor.getActeco().add(BigInteger.valueOf(621010));
		return emisor;
	}
	public Receptor receptor(VecmarDTO vecmar,ClmcliDTO clmcli){
		Receptor receptor = new Receptor();
		//System.out.println("Rescata rut");
		//System.out.println("Rut" + vecmar.getRutProveedor());
		//System.out.println("Rut" + vecmar.getDvProveedor());
		receptor.setRUTRecep(vecmar.getRutProveedor()+"-"+vecmar.getDvProveedor());
		receptor.setRznSocRecep(procesaCaracteres(clmcli.getRazonsocial().trim()));
		//NOreceptor.setGiroRecep(clmcli.getTipoNegocio().trim());
		String direccionClie = clmcli.getDireccionCliente().substring(0, 25);
		receptor.setDirRecep(procesaCaracteres(direccionClie.trim()));
		String descComuna=clmcli.getDescComuna();
		if (clmcli.getDescComuna().length()>20){
			descComuna = clmcli.getDescComuna().substring(0, 19);
		}
		receptor.setCmnaRecep(procesaCaracteres(descComuna.trim()));
	//}
	
	
	if (clmcli.getDescCiudad()!=null && clmcli.getDescCiudad()!=""){
		String descCiudad = clmcli.getDescCiudad();
		if (clmcli.getDescCiudad().length()>20){
			descCiudad = clmcli.getDescCiudad().substring(0, 19);
		}
        receptor.setCiudadRecep(procesaCaracteres(descCiudad.trim()));
	}else{
		String descCiudad = clmcli.getDescCiudad();
		if (clmcli.getDescCiudad().length()>20){
			descCiudad = clmcli.getDescCiudad().substring(0, 19);
		}
        receptor.setCiudadRecep(procesaCaracteres(descCiudad.trim()));
	}
	
	
		/*receptor.setDirRecep(vecmar.getDireccionDespacho().trim());
		receptor.setCmnaRecep(vecmar.getDescComuna().trim());
*/
		return receptor;
	}
	public Totales totales(List clcdia, VecmarDTO vecmar, int docelec, ClcmcoDTO clco){
		Totales totales = new Totales();
		
		String netoString = String.valueOf(clco.getValorNeto());
		BigInteger neto = new BigInteger(netoString);
		totales.setMntNeto(neto);
		//NOtotales.setMntExe(BigInteger.valueOf(0));
		//NOtotales.setTasaIVA(BigDecimal.valueOf(19));
		if (docelec==39){
			totales.setIVA(BigInteger.valueOf(clco.getTotalIva()));
		}else if (docelec==41){
			totales.setMntExe(BigInteger.valueOf(clco.getTotalDocumento()));
		}
		
		totales.setMntTotal(BigInteger.valueOf(clco.getTotalDocumento()));
		
		ImptoReten imp = null;
		ClcdiaDTO clc = new ClcdiaDTO();
		Iterator iter = clcdia.iterator();
		while (iter.hasNext()){
			clc = (ClcdiaDTO)iter.next();
			//imp = new ImptoReten();
			//System.out.println("Valor" + clc.getMontoImpuesto());
			//BigInteger valor = BigInteger.valueOf(clc.getMontoImpuesto());
			//imp.setMontoImp(valor);
			//Definir TipoImpuesto en sistema por cada impuesto
			//imp.setTipoImp(recuperaTasaSII(String.valueOf(clc.getCodigoImpuesto())));
			//imp.setTasaImp(BigDecimal.valueOf(clc.getImpuesto()));
			//totales.getImptoReten().add(imp);
		}
		
		
		return totales;
		
	}
	public Detalle detalleDoc(List cldmco){
		Detalle detalle=null;
		CldmcoDTO cld = null;
		Iterator iter = cldmco.iterator();
		double desc =0;
		int correlativo=0;
		DscRcgGlobal dsc = null;
		while (iter.hasNext()){
			correlativo = correlativo+1;
			cld = (CldmcoDTO) iter.next();
			if (cld.getCodigoArticulo()>0){
			detalle = new Detalle();
			CdgItem cdgitem = new CdgItem();
			//System.out.println("Detalle:"+cld.getCodigoArticulo());
			cdgitem.setTpoCodigo("INT1");
			cdgitem.setVlrCodigo(String.valueOf(cld.getCodigoArticulo()+"-"+cld.getDigitoverificador()));
			detalle.setNroLinDet(BigInteger.valueOf(correlativo));
			detalle.getCdgItem().add(cdgitem);
			//String desc2 = procesaCaracteres(cld.getDescArticulo());
			//desc = cld.getDescArticulo().replaceAll("ñ", "");
			detalle.setNmbItem(procesaCaracteres(cld.getDescArticulo().trim()));
			//detalle.setNmbItem(cld.getDescArticulo().trim());
			detalle.setQtyItem(BigDecimal.valueOf(cld.getCantidadArticulo()));
			//int precioNeto = (int) cld.getPrecioNeto();
			detalle.setPrcItem(BigDecimal.valueOf(cld.getPrecio()));//PRecio
			int montoNeto = (int) cld.getValorNeto();
			detalle.setMontoItem(BigInteger.valueOf(cld.getMontoCompra()));
			if (cld.getDescuentoLinea()!=0){
				desc = desc + cld.getDescuentoLinea();
				 
				double monto = cld.getCantidadArticulo() * cld.getPrecio();
				int monto2=(int)monto;
				detalle.setMontoItem(BigInteger.valueOf(monto2));
				
			}
			/*List clddia = cld.getImpuestos();
			if (clddia.size()>0){
				ClddiaDTO clddiaDTO = new ClddiaDTO();
				Iterator cl = clddia.iterator();
				
				String codigoImp =null;
				while (cl.hasNext()){
					clddiaDTO = (ClddiaDTO) cl.next();
					if (codigoImp!=null ){
						codigoImp=codigoImp+",";
						codigoImp = codigoImp + String.valueOf(clddiaDTO.getCodigoImpuesto());
					}else{
						codigoImp = String.valueOf(clddiaDTO.getCodigoImpuesto());
					}
					
				}
				//String []imp ={codigoImp};
				detalle.getCodImpAdic().add(codigoImp);
			}*/
			doc.getDetalle().add(detalle);
			
			//System.out.println("Correlativo:"+cld.getCorrelativo());
			
			
		}
		
		}
		if (desc!=0){
			dsc = new DscRcgGlobal();
			dsc.setNroLinDR(1);
			dsc.setTpoMov("D");
			dsc.setGlosaDR("Descuento General");
			dsc.setTpoValor("$");
			dsc.setValorDR(BigDecimal.valueOf(Math.rint(desc*1000)/1000));
			doc.getDscRcgGlobal().add(dsc);
		}
		return detalle;
	}
	public Impresion personalizados(PrdatcaDTO pr, VecmarDTO vecmar){
		Impresion imp= null;
		if (pr !=null){
			imp = new Impresion();
			imp.setPersonalizado01(String.valueOf(vecmar.getBodegaOrigen()));
			imp.setPersonalizado02(pr.getTelefono());
			imp.setPersonalizado03(String.valueOf(pr.getNumOV()));
			imp.setPersonalizado04(pr.getDireccion());
			imp.setPersonalizado05(pr.getNumeroDomicilio());
			imp.setPersonalizado06(pr.getDeptoOficina());
			imp.setPersonalizado07(pr.getPoblacionVilla());
			imp.setPersonalizado08(pr.getComuna());
			imp.setPersonalizado09(pr.getCiudad());
			imp.setPersonalizado10(pr.getContacto());
			imp.setPersonalizado11(pr.getTelefono());//Telefono de contacto
			imp.setPersonalizado12(String.valueOf(0));
			imp.setPersonalizado14(pr.getBanco());
			
			
		}
		return imp;
		
	}
	
	public IdDoc idoc(VecmarDTO vecmar, int codele, PrdatcaDTO prdatDTO){
		IdDoc id = new IdDoc();
		BigInteger tipoDte = BigInteger.valueOf(codele);
		
			if (codele==39 || codele==41){
				id.setIndServicio(BigInteger.valueOf(3));
			}
		
		id.setTipoDTE(tipoDte);
	    id.setFolio(BigInteger.valueOf(0));
	    //getXMLCalendar
	    //20121203
	    String fecha = String.valueOf(vecmar.getFechaDocumento());
	    String año = fecha.substring(0, 4);
	    String mes = fecha.substring(4, 6);
	    String dia = fecha.substring(6, 8);
	    fecha = año +"-"+mes+"-"+dia;
	    String fechaDespacho=null;
	    if (vecmar.getFechaDespacho()!=0){
	    	fechaDespacho = String.valueOf(vecmar.getFechaDespacho());
		    año = fechaDespacho.substring(0, 4);
		    mes = fechaDespacho.substring(4, 6);
		    dia = fechaDespacho.substring(6, 8);
		    fechaDespacho = año +"-"+mes+"-"+dia;
	    }
	    //Fecha Vencimiento
	    String fechaVenc="";
	    if (prdatDTO!=null){
	    	 if (prdatDTO.getFechaVencimiento()>0){
	 	    	fechaVenc = String.valueOf(prdatDTO.getFechaVencimiento());
	 		    String añoVenc = fechaVenc.substring(0, 4);
	 		    String mesVenc = fechaVenc.substring(4, 6);
	 		    String diaVenc = fechaVenc.substring(6, 8);
	 		    fechaVenc = año +"-"+mes+"-"+dia;
	 		    
	 		    
	 	    }
	    }
	   
	    
	    try{
	    	//System.out.println(getXMLCalendar(fecha));
	    	id.setFchEmis(getXMLCalendar(fecha));
	    	if (prdatDTO!=null){
	    	if (prdatDTO.getFechaVencimiento()>0){
	    		id.setFchVenc(getXMLCalendar(fechaVenc));
	    	}
	    	}
	    	//se comenta porque no corresponde a fecha despacho
	    	/*if (vecmar.getFechaDespacho()!=0 && codele==39){
	    		id.setFchVenc(getXMLCalendar(fechaDespacho));
			}*/
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    
		
		
		return id;
	}
	
	public static XMLGregorianCalendar getXMLCalendar(String strDate) throws Exception
	{
		Calendar sDate = Calendar.getInstance();
		DatatypeFactory dtf = DatatypeFactory.newInstance();
		XMLGregorianCalendar calendar = null;
		
		// Dates (CCYY-MM-DD)
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		
		if(strDate != null)
		{
			sDate.setTime(DATE_FORMAT.parse(strDate));
			calendar = dtf.newXMLGregorianCalendar();
			calendar.setYear(sDate.get(Calendar.YEAR));
			calendar.setDay(sDate.get(Calendar.DAY_OF_MONTH));
			calendar.setMonth(sDate.get(Calendar.MONTH)+ 1);			
		}
		
		return calendar;
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
		  
		  descripcion = descripcion.replaceAll("ñ", "N");
		  descripcion = descripcion.replaceAll("ñ", "");
		  descripcion = descripcion.replaceAll("<", "&lt;");
		  descripcion = descripcion.replaceAll(">", "&gt;");
		  descripcion = descripcion.replaceAll("&", "&amp;");
		  descripcion = descripcion.replaceAll("\"", "&quot;");
		  descripcion = descripcion.replaceAll("'", "&apos;");
		  descripcion = descripcion.replace("ñ", "A");
		  descripcion = descripcion.replace("ñ", "E");
		  descripcion = descripcion.replace("ñ", "I");
		  descripcion = descripcion.replace("ñ", "O");
		  descripcion = descripcion.replace("ñ", "U");
		 descripcion = descripcion.replace("Nñ", "N");
		  
		  
		  return descripcion;
	  }
	 public String obtienePersonalizados(VecmarDTO vecmar, PrdatcaDTO prdat){
		 StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		
		 
		 if (prdat!=null){
			  xmlCas.append("<Personalizados>");
			  xmlCas.append("<DocPersonalizado>");
			 // xmlCas.append("<Impresion>");
			  if (prdat.getDescripcionBodega()!=null && prdat.getDescripcionBodega().length()>0){
				 // xmlCas.append("<Personalizado_01>"+prdat.getDescripcionBodega().trim()+"</Personalizado_01>");
				  xmlCas.append("<campoString name="+"\"Personalizado_01"+"\">"+prdat.getDescripcionBodega().trim()+"</campoString>");
			  }
			  if (prdat.getNombreVendedor()!=null && prdat.getNombreVendedor().length()>0 && prdat.getNombreVendedor()!=""){
				 // xmlCas.append("<Personalizado_02>"+prdat.getNombreVendedor().trim()+"</Personalizado_02>");
				  xmlCas.append("<campoString name="+"\"Personalizado_02"+"\">"+prdat.getNombreVendedor().trim()+"</campoString>");
					  
				  }
			  
			  
			
			  
			  
			  if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().length()>0){
				  int forma = Integer.parseInt(vecmar.getFormaPago());
				  String formapago="";
				  if (forma==1){
					  formapago="CONTADO";
				  }else if (forma==2){
					  formapago="CREDITO";
					  
				  }
				  //xmlCas.append("<Personalizado_03>"+forma+"</Personalizado_03>");
				  xmlCas.append("<campoString name="+"\"Personalizado_03"+"\">"+prdat.getFormaPago().trim()+"</campoString>");
				  //xmlCas.append("<Personalizado_04>"+"10"+"</Personalizado_04>");
				  xmlCas.append("<campoString name="+"\"Personalizado_04"+"\">"+prdat.getCondPago().trim()+"</campoString>");
				  if (forma==2){
					  if (prdat.getNumCheque()!=null && prdat.getNumCheque().length()>0){
						  //xmlCas.append("<Personalizado_05>"+prdat.getNumCheque().trim()+"</Personalizado_05>");  
						  xmlCas.append("<campoString name="+"\"Personalizado_05"+"\">"+prdat.getNumCheque().trim()+"</campoString>");
					  }
				  if (prdat.getNumCtaCte()!=null && prdat.getNumCtaCte().length()>0){
					 // xmlCas.append("<Personalizado_06>"+prdat.getNumCtaCte().trim()+"</Personalizado_06>");
					  xmlCas.append("<campoString name="+"\"Personalizado_06"+"\">"+prdat.getNumCtaCte().trim()+"</campoString>");
				  }
				  if (prdat.getBanco()!=null && prdat.getBanco().length()>0){
					  //xmlCas.append("<Personalizado_07>"+prdat.getBanco().trim()+"</Personalizado_07>");
					  xmlCas.append("<campoString name="+"\"Personalizado_07"+"\">"+prdat.getBanco().trim()+"</campoString>");
					  //xmlCas.append("<TermPagoCdg>"+"1"+"</TermPagoCdg>");
					  //xmlCas.append("<TermPagoGlosa>"+"R"+"</TermPagoGlosa>");
					  
					    if (prdat.getPlazo()>0){
							  //xmlCas.append("<Personalizado_08>"+prdat.getPlazo()+"</Personalizado_08>");
							  xmlCas.append("<campoString name="+"\"Personalizado_08"+"\">"+prdat.getPlazo()+"</campoString>");
						  }
					 
					  
				  }
				  }
			  }
			  //xmlCas.append("<Personalizado_09>"+"100"+"</Personalizado_09>");
			  xmlCas.append("<campoString name="+"\"Personalizado_09"+"\">"+100+"</campoString>");
			  if (prdat.getHora()!=null && prdat.getHora().length()>0){
				  //xmlCas.append("<Personalizado_10>"+prdat.getHora().trim()+"</Personalizado_10>");
				  xmlCas.append("<campoString name="+"\"Personalizado_10"+"\">"+prdat.getHora().trim()+"</campoString>");
			  }
			 
			  
			 
			  
			  
			//  xmlCas.append("</Impresion>");
			  xmlCas.append("</DocPersonalizado>");
			  xmlCas.append("</Personalizados>");
			  
		  }
		 xml = xmlCas.toString();
		  return xml;
		 
	 }
	 /*public String formaXMLString(Encabezado enca, Documento doc, TptempDTO tpt, PrdatcaDTO prdat, VecmarDTO vecmar){
		  StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		  
		  
		  
		  
		  xmlCas.append("<?xml>");
		  //<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
		  String xmp="<?xml "+"version="+"\"1.0\" "+"encoding="+"\"ISO-8859-1"+"\""+" standalone="+"\"yes"+"\"?"+">";
		  
		  
		  //xmlCas.append(recuperaTasaSII("xml1"));
		  xmlCas.append((xmp));
		  
		  String xmp2="<DTE version="+"\"1.0"+"\""+" xmlns:ns2="+"\"http://www.w3.org/2000/09/xmldsig#"+"\""+" xmlns="+"\"http://www.sii.cl/SiiDte"+"\">";
		  //xmlCas.append(recuperaTasaSII("xml2"));
		  xmlCas.append((xmp2));
		  String xmp3="<Documento ID="+"\""+"\">";	  
		  //xmlCas.append(recuperaTasaSII("xml3"));
		  xmlCas.append((xmp3));
		  
		  
		  xmlCas.append("<Encabezado>");
		  xmlCas.append("<IdDoc>");
		  xmlCas.append("<TipoDTE>"+enca.getIdDoc().getTipoDTE()+"</TipoDTE>");
		  xmlCas.append("<Folio>"+0+"</Folio>");
		  xmlCas.append("<FchEmis>"+enca.getIdDoc().getFchEmis()+"</FchEmis>");
		 if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().length()>0){
			  int forma = Integer.parseInt(vecmar.getFormaPago());
			  String formapago="";
			  if (forma==1){
				  formapago="CONTADO";
			  }else if (forma==2){
				  formapago="CREDITO";
				  
			  }
			  xmlCas.append("<FmaPago>"+forma+"</FmaPago>");
			  if (forma==2 && prdat!=null){
			  if (prdat.getNumCtaCte()!=null && prdat.getNumCtaCte()!=""){
				  xmlCas.append("<NumCtaPago>"+prdat.getNumCtaCte().trim()+"</NumCtaPago>");
			  }
			  if (prdat.getBanco()!=null && prdat.getBanco()!=""){
				  xmlCas.append("<BcoPago>"+prdat.getBanco().trim()+"</BcoPago>");
				  //xmlCas.append("<TermPagoCdg>"+"1"+"</TermPagoCdg>");
				  //xmlCas.append("<TermPagoGlosa>"+"R"+"</TermPagoGlosa>");
				  String fecha="";
				  if (prdat.getFechaVencimiento()>0){
					  fecha = String.valueOf(prdat.getFechaVencimiento());
					    String año = fecha.substring(0, 4);
					    String mes = fecha.substring(4, 6);
					    String dia = fecha.substring(6, 8);
					    fecha = año +"-"+mes+"-"+dia;  
				  }
				  
				    if (prdat.getPlazo()>0){
						  xmlCas.append("<TermPagoDias>"+prdat.getPlazo()+"</TermPagoDias>");
					  }
				    if (prdat.getFechaVencimiento()>0){
				    	xmlCas.append("<FchVenc>"+fecha.trim()+"</FchVenc>");
				    }
				  
				  
			  }
			  }
		  }
		  
		 
		  xmlCas.append("</IdDoc>");
		  xmlCas.append("<Emisor>");
		  xmlCas.append("<RUTEmisor>"+tpt.getRut()+"-"+tpt.getDv().trim()+"</RUTEmisor>");
		  xmlCas.append("<RznSocEmisor>"+tpt.getRazonSocial().trim()+"</RznSoc>");
		  xmlCas.append("<GiroEmis>DISTRIBUIDORA</GiroEmis>");
		  Iterator actecoIter = enca.getEmisor().getActeco().iterator();
		  while (actecoIter.hasNext()){
			  
			  xmlCas.append("<Acteco>"+actecoIter.next()+"</Acteco>");
		  }
		  
		  xmlCas.append("<DirOrigen>"+tpt.getDireccionCasaMatriz().trim()+"</DirOrigen>");
		  xmlCas.append("<CmnaOrigen>"+tpt.getComuna().trim()+"</CmnaOrigen>");
		  xmlCas.append("</Emisor>");
		  xmlCas.append("<Receptor>");
		  xmlCas.append("<RUTRecep>"+enca.getReceptor().getRUTRecep()+"</RUTRecep>");
		  
		  xmlCas.append("<RznSocRecep>"+enca.getReceptor().getRznSocRecep()+"</RznSocRecep>");
		  xmlCas.append("<GiroRecep>"+enca.getReceptor().getGiroRecep()+"</GiroRecep>");
		  xmlCas.append("<DirRecep>"+enca.getReceptor().getDirRecep()+"</DirRecep>");
		  xmlCas.append("<CmnaRecep>"+enca.getReceptor().getCmnaRecep()+"</CmnaRecep>");
		  //xmlCas.append("<CmnaRecep>"+enca.getReceptor().getCmnaRecep()+"</CmnaRecep>");
		  xmlCas.append("</Receptor>");
		  
		  xmlCas.append("<Totales>");
		  //System.out.println("Neto xml:"+enca.getTotales().getMntNeto());
		  if (enca.getTotales().getMntNeto()!=null){
			  if (enca.getTotales().getMntNeto().longValue()>0){
				  xmlCas.append("<MntNeto>"+enca.getTotales().getMntNeto()+"</MntNeto>");
			  }
		  }
		 
		  if (enca.getTotales().getMntExe()!=null){
			  if (enca.getTotales().getMntExe().longValue()>0){
				  xmlCas.append("<MntExe>"+enca.getTotales().getMntExe()+"</MntExe>");
				  if (enca.getTotales().getIVA()!=null){
					  if (enca.getTotales().getIVA().longValue()>0){
						  xmlCas.append("<TasaIVA>"+enca.getTotales().getTasaIVA()+"</TasaIVA>");
						  xmlCas.append("<IVA>"+enca.getTotales().getIVA()+"</IVA>");
					  }  
				  }
				  
		  
			  }
		  }else{
			  xmlCas.append("<TasaIVA>"+enca.getTotales().getTasaIVA()+"</TasaIVA>");
			  xmlCas.append("<IVA>"+enca.getTotales().getIVA()+"</IVA>");
		  }
//		  if (enca.getIdDoc().getTipoDTE().longValue()==33){
//			  xmlCas.append("<TasaIVA>"+enca.getTotales().getTasaIVA()+"</TasaIVA>");
//			  xmlCas.append("<IVA>"+enca.getTotales().getIVA()+"</IVA>");
//		  }
		  
		  
		  
		  
		  Iterator iter = enca.getTotales().getImptoReten().iterator();
		  ImptoReten imp=null;
		  while (iter.hasNext()){
			   imp = (ImptoReten) iter.next();
			   xmlCas.append("<ImptoReten>");
			   xmlCas.append("<TipoImp>"+imp.getTipoImp()+"</TipoImp>");
			   xmlCas.append("<TasaImp>"+imp.getTasaImp()+"</TasaImp>");
			   xmlCas.append("<MontoImp>"+imp.getMontoImp()+"</MontoImp>");
			   xmlCas.append("</ImptoReten>");
		  }
		  xmlCas.append("<MntTotal>"+enca.getTotales().getMntTotal()+"</MntTotal>");
		  
		  xmlCas.append("</Totales>");
		  xmlCas.append("</Encabezado>");
		  
		  Iterator iterDet = doc.getDetalle().iterator();
		  Detalle det = null;
		  while (iterDet.hasNext()){
			  det = (Detalle) iterDet.next();
			  xmlCas.append("<Detalle>");
			  xmlCas.append("<NroLinDet>"+det.getNroLinDet()+"</NroLinDet>");
			  CdgItem cdgitem =null;
			  Iterator cgi = det.getCdgItem().iterator();
			  if (det.getCdgItem()!=null && det.getCdgItem().size()>0){
				  xmlCas.append("<CdgItem>");
				  while (cgi.hasNext()){
					  cdgitem =(CdgItem) cgi.next();
					  xmlCas.append("<TpoCodigo>"+cdgitem.getTpoCodigo()+"</TpoCodigo>");
					  xmlCas.append("<VlrCodigo>"+cdgitem.getVlrCodigo()+"</VlrCodigo>");
				  }
				  xmlCas.append("</CdgItem>");

			  }
			  		  if (det.getIndExe()!=null){
				  if (det.getIndExe().longValue()>0){
					 
					  xmlCas.append("<IndExe>"+det.getIndExe()+"</IndExe>");
					   
			  
				  }
			  }
			  xmlCas.append(" <NmbItem>"+det.getNmbItem()+"</NmbItem>");
			  xmlCas.append("<QtyItem>"+det.getQtyItem()+"</QtyItem>");
			  xmlCas.append("<PrcItem>"+det.getPrcItem()+"</PrcItem>");
			//  System.out.println("Descuento Linea");
			  if (det.getDescuentoMonto()!=null){
				//  System.out.println("Descuento Linea2");
				  if (det.getDescuentoMonto().longValue()>0){
				//	  System.out.println("Descuento Linea3");
					  xmlCas.append("<DescuentoMonto>"+det.getDescuentoMonto()+"</DescuentoMonto>");
				  }
			  }
			  if (det.getCodImpAdic()!=null){
				  Iterator codImp = det.getCodImpAdic().iterator();
				  while (codImp.hasNext()){
					  xmlCas.append("<CodImpAdic>"+codImp.next()+"</CodImpAdic>");
				  }
			  }
			  xmlCas.append("<MontoItem>"+det.getMontoItem()+"</MontoItem>");
			  xmlCas.append("</Detalle>");
			  
		  }
		 
		  if (enca.getTotales().getMntNeto()!=null && enca.getTotales().getMntNeto().longValue()>0){
			  xmlCas.append("<SubTotInfo>");
			  xmlCas.append("<NroSTI>"+1+"</NroSTI>");
			  xmlCas.append("<GlosaSTI>"+"SUBTOTAL"+"</GlosaSTI>");
			  xmlCas.append("<SubTotNetoSTI>"+enca.getTotales().getMntNeto()+"</SubTotNetoSTI>");
			  
			  
			  xmlCas.append("</SubTotInfo>");  
		  }
		  
		  
		  xmlCas.append("</Documento>");
		  //Comentado por aplicar a produccion
		  if (prdat!=null){
			  xmlCas.append("<Personalizados>");
			  xmlCas.append("<DocPersonalizado>");
			  xmlCas.append("<Impresion>");
			  xmlCas.append("<Personalizado_01>"+vecmar.getBodegaOrigen()+"</Personalizado_01>");
			  if (prdat.getTelefono()!=null && prdat.getTelefono().length()>0){
				  xmlCas.append("<Personalizado_02>"+prdat.getTelefono()+"</Personalizado_02>"); 
			  }
			  if (prdat.getNumOV()>0){
				  xmlCas.append("<Personalizado_03>"+prdat.getNumOV()+"</Personalizado_03>");
			  }
			  if (prdat.getDireccion()!=null && prdat.getDireccion().length()>0){
				  xmlCas.append("<Personalizado_04>"+prdat.getDireccion().trim()+"</Personalizado_04>");
			  }
			  if (prdat.getNumeroDomicilio()!=null && prdat.getNumeroDomicilio().length()>0){
				  xmlCas.append("<Personalizado_05>"+prdat.getNumeroDomicilio().trim()+"</Personalizado_05>");
			  }
			  if (prdat.getDeptoOficina()!=null && prdat.getDeptoOficina().length()>0){
				  xmlCas.append("<Personalizado_06>"+prdat.getDeptoOficina().trim()+"</Personalizado_06>");  
			  }
			  
			  if (prdat.getPoblacionVilla()!=null && prdat.getPoblacionVilla().length()>0){
				  xmlCas.append("<Personalizado_07>"+prdat.getPoblacionVilla().trim()+"</Personalizado_07>");  
			  }
			  if (prdat.getComuna()!=null && prdat.getComuna().length()>0){
				  xmlCas.append("<Personalizado_08>"+prdat.getComuna().trim()+"</Personalizado_08>");
			  }
			  if (prdat.getCiudad()!=null && prdat.getCiudad().length()>0){
				  xmlCas.append("<Personalizado_09>"+prdat.getCiudad().trim()+"</Personalizado_09>");  
				  		  }
			  if (prdat.getContacto()!=null && prdat.getContacto().length()>0){
				  xmlCas.append("<Personalizado_10>"+prdat.getContacto().trim()+"</Personalizado_10>");  
			  }
			  if (prdat.getTelefono()!=null && prdat.getTelefono().length()>0){
				  xmlCas.append("<Personalizado_11>"+prdat.getTelefono().trim()+"</Personalizado_11>");  
			  }
			  
			  //xmlCas.append("<Personalizado_12>"+0+"</Personalizado_12>");
			  if (prdat.getBanco()!=null && prdat.getBanco().length()>0){
				  xmlCas.append("<Personalizado_13>"+prdat.getNumeroDomicilio().trim()+"</Personalizado_13>");  
			  }
			  if (prdat.getNumCheque()!=null && prdat.getNumCheque().length()>0){
				  xmlCas.append("<Personalizado_14>"+prdat.getNumCheque().trim()+"</Personalizado_14>");  
			  }
			  
			  xmlCas.append("<Personalizado_15>"+"NINGUNA"+"</Personalizado_15>");

			  xmlCas.append("</Impresion>");
			  xmlCas.append("</DocPersonalizado>");
			  xmlCas.append("</Personalizados>");
			  
		  }
		  xmlCas.append("</DTE>");
		  xml = xmlCas.toString();
		  return xml;
	  }*/
	 public int obtieneImpuestoSII(int codigo){
		  int impSII=0;
		  Iterator<Integer> iter = impuestosSII.keySet().iterator();
		  
		  while (iter.hasNext()){
			  if (impuestosSII.containsKey(codigo)){
				  impSII = (Integer) impuestosSII.get(codigo);
				 
				  break;
			  }
			  
		  }
		  
		  
		  
		  return impSII;
	  }
}
