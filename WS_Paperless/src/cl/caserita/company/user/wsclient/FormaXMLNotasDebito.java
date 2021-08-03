package cl.caserita.company.user.wsclient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dte.DTEDefType;
import cl.caserita.dte.ObjectFactory;
import cl.caserita.dte.DTEDefType.Documento;
import cl.caserita.dte.DTEDefType.Documento.Detalle;
import cl.caserita.dte.DTEDefType.Documento.DscRcgGlobal;
import cl.caserita.dte.DTEDefType.Documento.Encabezado;
import cl.caserita.dte.DTEDefType.Documento.Referencia;
import cl.caserita.dte.DTEDefType.Documento.Detalle.CdgItem;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Emisor;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Receptor;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Totales;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Totales.ImptoReten;
import cl.caserita.dto.ActecoDTO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ConnodDTO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.ConnoiDTO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;

public class FormaXMLNotasDebito {

	private static Documento doc = new Documento();
	 private static Properties prop=null;
		private static String pathProperties;
		private static HashMap impuestosSII =null;	
	public String xml(List listaTotal, List connod, ClmcliDTO clmcli, int docelec, ConnohDTO connoh, int codref, int codRefDoc, int fecha, List acteco, TptempDTO tpt,HashMap impuestoSII, String nota,PrdatcaDTO prdat){
		String stringxml="";
		System.out.println("Procesa XML");
		DTEDefType dte = new DTEDefType();
		doc = new Documento();			
		//Documento doc = new Documento();
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(acteco, tpt);
		Receptor rec = receptor(clmcli);
		
		
		impuestosSII=impuestoSII;
		Detalle det = detalleDoc(connod, connoh);
		
		
		
		enc.setEmisor(emi);
		enc.setIdDoc(idoc);
		enc.setReceptor(rec);
		
			Totales tot = totales(listaTotal, connoh, docelec);
			enc.setTotales(tot);
	
			
	
		
		
		doc.setID("");

		doc.setEncabezado(enc);
		dte.setVersion(BigDecimal.valueOf(1.0));
		dte.setDocumento(doc);
			
		
		enc.setIdDoc(idoc(connoh, docelec,prdat));
		Referencia ref = new Referencia();
		if (connoh.getCodDocumento()==35){
			ref.setNroLinRef(1);
		}else {
			ref.setNroLinRef(1);
		}
		
		ref.setTpoDocRef(String.valueOf(codRefDoc));
		ref.setFolioRef(String.valueOf(connoh.getNumeroDocumento()));
		int notaPar = Integer.parseInt(nota);
		ref.setCodRef(BigInteger.valueOf(notaPar));
		 String fecha2 = String.valueOf(fecha);
		    String año = fecha2.substring(0, 4);
		    String mes = fecha2.substring(4, 6);
		    String dia = fecha2.substring(6, 8);
		    fecha2 = año +"-"+mes+"-"+dia;
		    try{
		    	ref.setFchRef(getXMLCalendar(fecha2));
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    }
		    if (notaPar==1 || notaPar==3){
		    	ref.setRazonRef("ANULA DOCUMENTO REFERENCIA");
		    }else{
		    	ref.setRazonRef("CORRIGE DOCUMENTO REFERENCIA");
		    }
		//ref.setRazonRef("ANULA DOCUMENTO REFERENCIA");
		doc.getReferencia().add(ref);
      StringWriter writer = new StringWriter();
      //genera archivo TXT
     // String texto = fh(vecmar, clmcli)+fhir(vecmar)+DH(cldmco)+pe(vecmar);
      
		try{
			final JAXBContext jc = JAXBContext.newInstance(DTEDefType.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
			m.setProperty("jaxb.encoding", "ISO-8859-1");
			
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     m.marshal(dte, baos);
		     String xml = new String(baos.toByteArray());
		     stringxml = xml;
	        
	        
	        stringxml = stringxml.replaceAll("dteDefType", "DTE");
	        int largo = stringxml.length();
	        stringxml = stringxml.substring(0, largo-7);
	        stringxml = stringxml + obtienePersonalizados(prdat)+"</DTE>";
	        //public String procesaPersonalizados(String xml, PrdatcaDTO prdat)
	        stringxml = stringxml.replaceAll("\n","");
			//System.out.println(stringxml);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("Pruebas");
		}
		
		
		
		
		return stringxml;
	
	}
	public IdDoc idoc(ConnohDTO connoh, int codele,PrdatcaDTO prdat){
		IdDoc id = new IdDoc();
		BigInteger tipoDte = BigInteger.valueOf(codele);
		if (codele==33){
			//BigInteger pago = new BigInteger(vecmar.getFormaPago());
			//id.setFmaPago(pago);
		}
			if (codele==39){
				id.setIndServicio(BigInteger.valueOf(3));
			}
		
		id.setTipoDTE(tipoDte);
	    id.setFolio(BigInteger.valueOf(0));
	    //getXMLCalendar
	    //20121203
	    String fecha = String.valueOf(connoh.getFechaNota());
	    String año = fecha.substring(0, 4);
	    String mes = fecha.substring(4, 6);
	    String dia = fecha.substring(6, 8);
	    fecha = año +"-"+mes+"-"+dia;
	    if (prdat!=null){
	    	if (prdat.getBanco()!=null && prdat.getBanco()!=""){
	    		id.setBcoPago(prdat.getBanco().trim());
	    	}
	    	if (prdat.getNumCtaCte()!=null && prdat.getNumCtaCte()!=""){
	    		id.setNumCtaPago(prdat.getNumCtaCte());
	    	}
	    	if (prdat.getPlazo()!=0){
	    		id.setTermPagoDias(BigInteger.valueOf(prdat.getPlazo()));
	    	}
	    	
	    }
	   // String fechaDespacho = String.valueOf(vecmar.getFechaDespacho());
	    /*if (!"".equals(fechaDespacho)){
	    	año = fechaDespacho.substring(0, 4);
		    mes = fechaDespacho.substring(4, 6);
		    dia = fechaDespacho.substring(6, 8);
		    fechaDespacho = año +"-"+mes+"-"+dia;
	    }*/
	    
	    
	    try{
	    	System.out.println(getXMLCalendar(fecha));
	    	id.setFchEmis(getXMLCalendar(fecha));
	    	
	    	if (codele==33){
	    //	id.setFchVenc(getXMLCalendar(fechaDespacho));
			}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    

	  
	    
	   
	    Fecha fch = new Fecha();
	   // String fecha = fch.getDDMMYYYY(String.valueOf(vecmar.getFechaDocumento()));
	    //Pendiente idoc.setfc();
	    
		
		
		return id;
	}
	public Emisor emisor(List acteco, TptempDTO tptemp){
		Emisor emisor = new Emisor();
		
		
		emisor.setRUTEmisor(tptemp.getRut()+"-"+tptemp.getDv());
		emisor.setRznSoc(tptemp.getRazonSocial().trim());
		emisor.setGiroEmis(tptemp.getGiro().trim());
		
		//emisor.setCdgSIISucur(new BigInteger(44));
		emisor.setDirOrigen(tptemp.getDireccionCasaMatriz().trim());
		emisor.setCmnaOrigen(tptemp.getComuna().trim());
		
		
		Iterator iter = acteco.iterator();
		while (iter.hasNext()){
			ActecoDTO actecoDTO = (ActecoDTO)iter.next();
			emisor.getActeco().add(BigInteger.valueOf(Integer.parseInt(actecoDTO.getCodigoActeco().trim())));
		}
		return emisor;
	}
	public Receptor receptor(ClmcliDTO clmcli){
		Receptor receptor = new Receptor();
		
		receptor.setRUTRecep(clmcli.getRutCliente()+"-"+clmcli.getDvCliente());
		receptor.setRznSocRecep(procesaCaracteres(clmcli.getRazonsocial().trim()));
		receptor.setGiroRecep(procesaCaracteres(clmcli.getTipoNegocio().trim()));
		receptor.setDirRecep(procesaCaracteres(clmcli.getDireccionCliente().trim()));
		receptor.setCmnaRecep(procesaCaracteres(clmcli.getDescComuna().trim()));
		receptor.setCiudadRecep(procesaCaracteres(clmcli.getDescCiudad().trim()));

		return receptor;
	}
	public Totales totales(List connoi, ConnohDTO connoh, int docelec){
		
			Totales totales = new Totales();
			double tasaImp=0;

			int impuesto=0;
			int iva=0;
			String netoString = String.valueOf(connoh.getMontoNeto());
			BigInteger neto = new BigInteger(netoString);
			totales.setMntNeto(neto);
			//totales.setMntExe(BigInteger.valueOf(0));
			//totales.setTasaIVA(BigDecimal.valueOf(19));
			//totales.setIVA(BigInteger.valueOf(vecmar.getTotalIva()));
			totales.setMntTotal(BigInteger.valueOf(connoh.getMontoTotal()));
			if (docelec==56 || docelec==61){
				if (connoh.getMontoIva()>0){
					totales.setIVA(BigInteger.valueOf(connoh.getMontoIva()));
					totales.setTasaIVA(BigDecimal.valueOf(19));
					
				}
				
			}
			ImptoReten imp = null;
			ConnoiDTO clc = null;
			if (docelec==56 || docelec==61){
				Iterator iter = connoi.iterator();
				while (iter.hasNext()){
					clc = (ConnoiDTO)iter.next();
					if (clc.getCodImpto()!=2 && clc.getMontoImpuesto()!=0){
						imp = new ImptoReten();
						//System.out.println("Valor" + clc.getMontoImpuesto());
						BigInteger valor = BigInteger.valueOf(clc.getMontoImpuesto());
						imp.setMontoImp(valor);
						impuesto=impuesto+clc.getMontoImpuesto();

						//Definir TipoImpuesto en sistema por cada impuesto
						//imp.setTipoImp(recuperaTasaSII(String.valueOf(clc.getCodImpto())));
						imp.setTipoImp(String.valueOf(obtieneImpuestoSII(clc.getCodImpto())));
						imp.setTasaImp(BigDecimal.valueOf(19));
						totales.getImptoReten().add(imp);
						//tasaImp=tasaImp+clc.getImpuesto();

						
					}
					else{
						iva = clc.getMontoImpuesto();
						impuesto=impuesto+clc.getMontoImpuesto();
					}
					
				}
			}
			
			
		
		
		
		return totales;
		
	}
	public Detalle detalleDoc(List connod, ConnohDTO connoh){
		Detalle detalle=null;
		ConnodDTO cld = null;
		Iterator iter = connod.iterator();
		int montoExento=0;
		int montoNeto=0;
		int correlativo=0;
		while (iter.hasNext()){
			
			cld = (ConnodDTO) iter.next();
			detalle = new Detalle();
			
			//System.out.println("Detalle:"+cld.getCodigoArticulo());
			
			if (cld.getCodArticulo()!=0){
				CdgItem cdgitem = new CdgItem();
				cdgitem.setTpoCodigo("INT1");
				cdgitem.setVlrCodigo(String.valueOf(cld.getCodArticulo()+"-"+cld.getDigArticulo()));
				detalle.getCdgItem().add(cdgitem);
			}
			correlativo=correlativo+1;
			detalle.setNroLinDet(correlativo);
			
			//String desc2 = procesaCaracteres(cld.getDescripcion());
			//desc = cld.getDescArticulo().replaceAll("ñ", "");
			detalle.setNmbItem(procesaCaracteres(cld.getDescripcion().trim()));
			
			//detalle.setNmbItem(cld.getDescripcion().trim());
			//Se agrega validacion para no enviar cantidad por nc diferencia precios
			if (cld.getCantidad()==0){
				cld.setCantidad(1);
				/*detalle.setQtyItem(BigDecimal.valueOf(cld.getCantidad()));*/
				/*if (cld.getPrecioUnitario()==0){
					detalle.setPrcItem(BigDecimal.valueOf(cld.getTotalNeto()));//PRecio
				}*/
			}else{
				detalle.setQtyItem(BigDecimal.valueOf(cld.getCantidad()));
			}
			
		     if ("C".equals(connoh.getTipoNota()) || "D".equals(connoh.getTipoNota()) ){
		    	 if (connoh.getCodDocumento()==39){
						int precioNeto = (int) cld.getPrecioUnitario();
						if (precioNeto>0){
							detalle.setPrcItem(BigDecimal.valueOf(precioNeto));//PRecio
						}
						
					}else
					{
						if (cld.getPrecioUnitario()>0){
							detalle.setPrcItem(BigDecimal.valueOf(cld.getPrecioUnitario()));//PRecio
						}
						
					}
					
					
					detalle.setMontoItem(BigInteger.valueOf(cld.getTotalNeto()));
		     }else{
		    	 detalle.setMontoItem(BigInteger.valueOf(0));
		     }
				
				
			
			
			if (cld.getDescLinea()!=0){
				 
				detalle.setDescuentoMonto(BigInteger.valueOf(cld.getDescLinea()));
			}
			List clddia = cld.getImpuesto();
			if (clddia!=null){
				ExtariDTO extari = new ExtariDTO();
				Iterator cl = clddia.iterator();
				
				String codigoImp =null;
				while (cl.hasNext()){
					extari = (ExtariDTO) cl.next();
					/*if (codigoImp!=null ){
						codigoImp=codigoImp+",";
						codigoImp = codigoImp +recuperaTasaSII(String.valueOf(extari.getCodImpto()));
					}else{
						codigoImp = recuperaTasaSII(String.valueOf(extari.getCodImpto()));
					}*/
					//detalle.getCodImpAdic().add(recuperaTasaSII(String.valueOf(extari.getCodImpto())));
					//detalle.getCodImpAdic().add(String.valueOf(obtieneImpuestoSII((extari.getCodImpto()))));
					if (extari.getCodImpto()!=1){
						//detalle.getCodImpAdic().add(recuperaTasaSII(String.valueOf(extari.getCodImpto())));
						detalle.getCodImpAdic().add(String.valueOf(obtieneImpuestoSII((extari.getCodImpto()))));
						montoNeto=montoNeto+cld.getTotalLinea();
					}
					else{
						montoExento = montoExento+cld.getTotalLinea();
						detalle.setIndExe(BigInteger.valueOf(1));
					}
				}
				//String []imp ={codigoImp};
				
			}
			doc.getDetalle().add(detalle);
			
			//System.out.println("Correlativo:"+cld.getCorrelativo());
		}
		
		return detalle;
	}
	public DscRcgGlobal descuentoGlobal(){
		DscRcgGlobal dsc = new DscRcgGlobal();
		
		
		return dsc;
	}
	
	public String fh(VecmarDTO vecmar, ClmcliDTO clmcli){
		String fh="FH";
		fh = fh +"|33";
		fh = fh +"|"+vecmar.getNumDocumento();
		fh = fh +"|"+vecmar.getFechaDocumento();
		fh = fh +"|"+"|"+"|"+"|"+"|"+"|";
		fh = fh +"|"+vecmar.getFechaDespacho();
		fh = fh +"|"+"96509850-K";
		fh = fh +"|"+"DISTRIBUIDORA COMERCIAL CASERITA LTDA.";
		fh = fh +"|"+"";
		fh = fh + "|"+"";
		fh = fh +"|"+"APOQUINDO 6275 OF 21";
		fh = fh +"|"+"LAS CONDES";
		fh = fh +"|"+"6275";
		fh = fh +"|"+vecmar.getRutProveedor()+"-"+vecmar.getDvProveedor();
		fh = fh +"|"+clmcli.getRazonsocial().trim();
		fh = fh +"|"+vecmar.getDireccionDespacho().trim();
		fh = fh +"|"+vecmar.getDescComuna();
		fh = fh +"|"+vecmar.getTotalNeto();
		fh = fh +"|"+"19.0";
		fh = fh +"|"+vecmar.getTotalIva();
		fh = fh +"|"+vecmar.getTotalDocumento();
		
		
		return fh;
	}
public String fhir(VecmarDTO vecmar){
	  String fhir="";
	  fhir ="FHIR"+"|";
	  fhir = fhir+"|"+"19";
	  fhir= fhir+"|"+"12";
	  fhir=fhir+"|"+vecmar.getTotalIva();
	  
	  
	  
	  return fhir;
}

public String DH(List cldmco){
	  String dh ="";
	  Iterator iter = cldmco.iterator();
	  CldmcoDTO cld =null;
	  while (iter.hasNext()){
		  dh ="DH";
		  cld = (CldmcoDTO) iter.next();
		  dh= dh+"|"+cld.getCorrelativo();
		  dh=  dh+ "|"+cld.getDescArticulo().trim();
		  dh = dh+"|"+cld.getCantidadArticulo();
		  dh = dh+"|"+cld.getPrecio();
		  dh = dh+"|"+"|";
		  dh = dh+"|"+"0";
		  dh = dh+"|"+cld.getMontoCompra();
		  dh = dh + "|";
		  dh = dh + "DHCD";
		  dh = dh + "|INT1"+"|"+cld.getCodigoArticulo();
	  }
	  
	  
	  return dh;
}

public String srDescuento(){
	  String sr="";
	  
	  return sr;
}

public String pe(VecmarDTO vecmar){
	  String pe="";
	  pe = "PE";
	  pe = pe +"|"+"30 DIAS";
	  pe = pe +"|"+vecmar.getBodegaOrigen();
	  pe = pe +"|"+vecmar.getDescBodega().trim();
	  pe = pe +"|"+"1";
	  pe = pe +"|"+vecmar.getDireccionDespacho().trim();
	  pe = pe +"|"+vecmar.getDescComunaBodega().trim();
	  pe = pe +"|"+vecmar.getNombreVendedor().trim();
	  pe = pe +"|"+vecmar.getTotalDocumento();
	  pe = pe +"|"+"11:00:00";
	  
	  return pe;
	  	  
}
public String FormaXML(){
	  String xml="";
	  String nme="S20121009T033F0000000454";
	  xml= "<Documento ID=";
	//  xml = xml+""+nme+;
	  
	  		
	  
	  
	  return xml;
	  
	  
	  
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
	  
	descripcion = Normalizer.normalize(descripcion, Normalizer.Form.NFD);
	 descripcion = descripcion.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

	 descripcion = descripcion.replace("ñ", "N");
	 descripcion = descripcion.replace("ñ", "N");
	 descripcion = descripcion.replace("ñ", "");
	 descripcion = descripcion.replace("<", "&lt;");
	 descripcion = descripcion.replace(">", "&gt;");
	 descripcion = descripcion.replace("&", "&amp;");
	 descripcion = descripcion.replace("\"", "&quot;");
	 descripcion = descripcion.replace("'", "&apos;");
	 descripcion = descripcion.replace("ñ", "A");
	 descripcion = descripcion.replace("ñ", "E");
	 descripcion = descripcion.replace("ñ", "I");
	 descripcion = descripcion.replace("ñ", "O");
	 descripcion = descripcion.replace("ñ", "U");
	 descripcion = descripcion.replace("ñ", "a");
	 descripcion = descripcion.replace("ñ", "e");
	 descripcion = descripcion.replace("ñ", "i");
	 descripcion = descripcion.replace("ñ", "o");
	 descripcion = descripcion.replace("ñ", "u");
	 descripcion = descripcion.replace("Nñ", "N");
	 descripcion = descripcion.replace("Gñ", "G");
	 descripcion = descripcion.replace("ñ", "N");
	 descripcion = descripcion.replace("ñ", "N");
	 descripcion = descripcion.replace("ñ", "");
	 descripcion = descripcion.replace("ñ", "");
	 descripcion = descripcion.replace("ñ", "");
	 descripcion = descripcion.replace("ñ", "");
	 descripcion = descripcion.replace("Ñ", "N");
	 descripcion = descripcion.replace("Ñ", "N");
	 descripcion = descripcion.replace("º", "");
	 descripcion = descripcion.replace("Nº", "");
	 descripcion = descripcion.replace("´", "");
	 descripcion = descripcion.replace("?", "");
	 descripcion = descripcion.replace("N?", "N");
	 descripcion = descripcion.replace("N°", "N");
	 descripcion = descripcion.replace("Ã", "");
	 descripcion = descripcion.replace("Ð", "D");
	 descripcion = descripcion.replaceAll("[^\\p{ASCII}]", "");
	  
	  
	  
	  return descripcion;
}
public String obtienePersonalizados(PrdatcaDTO prdat){
	 StringBuffer xmlCas = new StringBuffer();
	  String xml="";
	
	  if (prdat!=null){
		  
			  
		  xmlCas.append("<Personalizados>");
		  xmlCas.append("<DocPersonalizado xmlns="+"\"http://www.sii.cl/SiiDte"+"\" "+"dteID="+"\""+"\">");
		  //xmlCas.append("<DocPersonalizado>");
		  //xmlCas.append("<Impresion>");
		  xmlCas.append("<campoString name="+"\"Personalizado_01"+"\">"+prdat.getFormaPago().trim()+"</campoString>");
		  //xmlCas.append("<Personalizado_01>"+vecmar.getBodegaOrigen()+"</Personalizado_01>");
		  if (prdat.getTelefono()!=null && prdat.getTelefono().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_02"+"\">"+prdat.getTelefono()+"</campoString>");
			 // xmlCas.append("<Personalizado_02>"+prdat.getTelefono()+"</Personalizado_02>");
		  }
		  if (prdat.getNumOV()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_03"+"\">"+prdat.getNumOV()+"</campoString>");
			  //xmlCas.append("<Personalizado_03>"+prdat.getNumOV()+"</Personalizado_03>");
		  }
		  if (prdat.getDireccion()!=null && !prdat.getDireccion().equals("")){
			  xmlCas.append("<campoString name="+"\"Personalizado_04"+"\">"+prdat.getDireccion().trim()+"</campoString>");
			 // xmlCas.append("<Personalizado_04>"+prdat.getDireccion().trim()+"</Personalizado_04>");
		  }
		  if (prdat.getNumeroDomicilio()!=null && prdat.getNumeroDomicilio().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_05"+"\">"+prdat.getNumeroDomicilio().trim()+"</campoString>");
			  //xmlCas.append("<Personalizado_05>"+prdat.getNumeroDomicilio().trim()+"</Personalizado_05>");
		  }
		  if (prdat.getDeptoOficina()!=null && prdat.getDeptoOficina().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_06"+"\">"+prdat.getDeptoOficina().trim()+"</campoString>");
			  //xmlCas.append("<Personalizado_06>"+prdat.getDeptoOficina().trim()+"</Personalizado_06>");
		  }
		  
		  if (prdat.getPoblacionVilla()!=null && prdat.getPoblacionVilla().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_07"+"\">"+prdat.getPoblacionVilla().trim()+"</campoString>");
			  //xmlCas.append("<Personalizado_07>"+prdat.getPoblacionVilla().trim()+"</Personalizado_07>");
		  }else{
			  xmlCas.append("<campoString name="+"\"Personalizado_07"+"\">"+"NINGUNA"+"</campoString>");
			  //xmlCas.append("<Personalizado_07>"+"NINGUNA"+"</Personalizado_07>");
		  }
		  if (prdat.getComuna()!=null && prdat.getComuna().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_08"+"\">"+prdat.getComuna().trim()+"</campoString>");
			  //xmlCas.append("<Personalizado_08>"+prdat.getComuna().trim()+"</Personalizado_08>");
		  }
		  if (prdat.getCiudad()!=null && prdat.getCiudad().length()>0){
			 xmlCas.append("<campoString name="+"\"Personalizado_09"+"\">"+prdat.getCiudad().trim()+"</campoString>");
			 // xmlCas.append("<Personalizado_09>"+prdat.getCiudad().trim()+"</Personalizado_09>");
			  		  }
		  if (prdat.getContacto()!=null && prdat.getContacto().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_10"+"\">"+prdat.getContacto().trim()+"</campoString>");
			
		  }
		  if (prdat.getTelefono()!=null && !prdat.getTelefono().trim().equals("") && prdat.getTelefono().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_11"+"\">"+prdat.getTelefono().trim()+"</campoString>");  
			 
		  }
		  
		  xmlCas.append("<campoString name="+"\"Personalizado_12"+"\">"+101+"</campoString>");
		  
		  if (prdat.getNumeroOficinaPri()!=null && !prdat.getNumeroOficinaPri().trim().equals("") && prdat.getNumeroOficinaPri().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_13"+"\">"+prdat.getNumeroOficinaPri().trim()+"</campoString>");
			 
		  }
		  if (prdat.getNumCheque()!=null && !prdat.getNumCheque().trim().equals("") && prdat.getNumCheque().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_14"+"\">"+prdat.getNumCheque().trim()+"</campoString>");
			 
		  }
		  if (prdat.getDeptoOficinaPri()!=null && !prdat.getDeptoOficinaPri().trim().equals("") && prdat.getDeptoOficinaPri().length()>0){
			 
			  xmlCas.append("<campoString name="+"\"Personalizado_15"+"\">"+prdat.getDeptoOficinaPri().trim()+"</campoString>");
			 
		  }
		  
		  //xmlCas.append("<campoString name="+"\"Personalizado_16"+"\">"+formapago.trim()+"</campoString>");
		 
		  if (prdat.getNumCarguio()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_17"+"\">"+prdat.getNumCarguio()+"</campoString>");
			
		  }
		  
		  
		
		  xmlCas.append("</DocPersonalizado>");
		  xmlCas.append("</Personalizados>");
		  
	  
	  }
	 xml = xmlCas.toString();
	  return xml;
	 
}

public int obtieneImpuestoSII(int codigo){
	  int impSII=0;
	  Iterator<Integer> iter = impuestosSII.keySet().iterator();
	  if (codigo!=1){
		  while (iter.hasNext()){
			  if (impuestosSII.containsKey(codigo)){
				  impSII = (Integer) impuestosSII.get(codigo);
				  System.out.println("Clave"+impSII);
				  break;
			  }
			  
		  }
	  }
	 
	  
	  
	  
	  return impSII;
}
}
