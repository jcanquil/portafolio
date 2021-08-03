package cl.caserita.company.user.wsclient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import cl.caserita.dte.DTEDefType.Documento.Detalle.CdgItem;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Emisor;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Receptor;
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Totales;
import cl.caserita.dto.ActecoDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.PrmprvDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public class FormaXMLGuiaMermas {

	private static Documento doc = null;
	 private static Properties prop=null;
		private static String pathProperties;
		
	public String xml(VecmarDTO vecmarDTO,List vedmar,  PrmprvDTO prmprvDTO, int docelec, int docref, List acteco,TptempDTO tpt, TptbdgDTO tptBdg, PrdatcaDTO prdatcaDTO){
		String stringxml="";
		System.out.println("Procesa XML");
		DTEDefType dte = new DTEDefType();
		doc = new Documento();		
		//Documento doc = new Documento();
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(acteco, tpt, tptBdg);
		Receptor rec = receptor(vecmarDTO, prmprvDTO);
		
		
		int valorTotal = detalleDoc(vedmar);
		Totales tot = totales(vecmarDTO, docelec, valorTotal);
		enc.setEmisor(emi);
		enc.setIdDoc(idoc);
		enc.setReceptor(rec);
		enc.setTotales(tot);
		
		doc.setID("");
		doc.setEncabezado(enc);
		dte.setVersion(BigDecimal.valueOf(1.0));
		dte.setDocumento(doc);
			
		
		enc.setIdDoc(idoc(vecmarDTO, docelec, prdatcaDTO));
      StringWriter writer = new StringWriter();
      //genera archivo TXT
    //  String texto = fh(vecmar, clmcli)+fhir(vecmar)+DH(cldmco)+pe(vecmar);
      
		try{
			final JAXBContext jc = JAXBContext.newInstance(DTEDefType.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
			m.setProperty("jaxb.encoding", "ISO-8859-1");
			//m.JAXB_ENCODING.
		    
			
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        m.marshal(dte, baos);
	        String xml = new String(baos.toByteArray());
	        stringxml = xml;
	        
	       
	        stringxml = stringxml.replaceAll("dteDefType","DTE");
	       
	        int largo = stringxml.length();
	        stringxml = stringxml.substring(0, largo-7);
	        //System.out.println("XML Cortado:"+stringxml);
	        stringxml = stringxml + obtienePersonalizados(tptBdg, prdatcaDTO)+"</DTE>";
	       // System.out.println("XML Cortado2:"+stringxml);
	        stringxml = stringxml.replaceAll("\n","");
	       // stringxml = stringxml.replaceAll("\n","");
			//System.out.println(stringxml);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("Pruebas");
		}
		
		
		
		
		return stringxml;
	
	}
	public IdDoc idoc(VecmarDTO vecmar, int codele,PrdatcaDTO prdat){
		IdDoc id = new IdDoc();
		BigInteger tipoDte = BigInteger.valueOf(codele);
		if (codele==33){
			//BigInteger pago = new BigInteger(vecmar.getFormaPago());
			//id.setFmaPago(pago);
		}
			if (codele==38){
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
	    //Eliminar y verificar bien los datos
	    /*if (prdat!=null){
	    	if (prdat.getBanco()!=null && prdat.getBanco()!=""){
	    		id.setBcoPago("CHILE");
	    	}
	    	if (prdat.getNumCtaCte()!=null && prdat.getNumCtaCte()!=""){
	    		id.setNumCtaPago("333333");
	    	}
	    	if (prdat.getPlazo()!=0){
	    		id.setTermPagoDias(BigInteger.valueOf(prdat.getPlazo()));
	    	}
	    	
	    }*/
	    
	    if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().trim().length()>0 ){
			  int forma = Integer.parseInt(vecmar.getFormaPago());
			  String formapago="";
			  if (forma==1){
				  formapago="CONTADO";
			  }
				  formapago="CREDITO";
				  	//id.setNumCtaPago("123456");
				  	//id.setBcoPago("SANTANDER");
				  	//id.setTermPagoDias(BigInteger.valueOf(12));
				
			
		  }else{
			
			  //	id.setNumCtaPago("123456");
			  //	id.setBcoPago("SANTANDER");
			  //	id.setTermPagoDias(BigInteger.valueOf(12));
		  }
	    /*String fechaDespacho = String.valueOf(vecmar.getFechaDespacho());
	    año = fechaDespacho.substring(0, 4);
	    mes = fechaDespacho.substring(4, 6);
	    dia = fechaDespacho.substring(6, 8);
	    fechaDespacho = año +"-"+mes+"-"+dia;*/
	    try{
	    	System.out.println(getXMLCalendar(fecha));
	    	id.setFchEmis(getXMLCalendar(fecha));
	    	
	    	
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    

	  
	    
	   
	    Fecha fch = new Fecha();
	   // String fecha = fch.getDDMMYYYY(String.valueOf(vecmar.getFechaDocumento()));
	    //Pendiente idoc.setfc();
	    
		
		
		return id;
	}
	public Emisor emisor(List acteco,TptempDTO tptemp, TptbdgDTO tptbdg ){
		Emisor emisor = new Emisor();
		emisor.setRUTEmisor(tptemp.getRut()+"-"+tptemp.getDv());
		emisor.setRznSoc(tptemp.getRazonSocial().trim());
		emisor.setGiroEmis(tptemp.getGiro().trim());
		
		//emisor.setCdgSIISucur(new BigInteger(44));
		emisor.setDirOrigen(tptemp.getDireccionCasaMatriz().trim());
		emisor.setCmnaOrigen(tptemp.getComuna().trim());
		String desBodega=tptbdg.getDesBodega();
		if (tptbdg.getDesBodega().length()>29){
			desBodega=tptbdg.getDesBodega().substring(0, 20);
		}
		emisor.setSucursal(desBodega.trim());
		
		
		Iterator iter = acteco.iterator();
		while (iter.hasNext()){
			ActecoDTO actecoDTO = (ActecoDTO)iter.next();
			emisor.getActeco().add(BigInteger.valueOf(Integer.parseInt(actecoDTO.getCodigoActeco().trim())));
		}
		return emisor;
	}
	public Receptor receptor(VecmarDTO vecmar, PrmprvDTO prmprvDTO){
		Receptor receptor = new Receptor();
		System.out.println("Rescata rut");
		
		receptor.setRUTRecep(vecmar.getRutProveedor()+"-"+vecmar.getDvProveedor());
		receptor.setRznSocRecep(procesaCaracteres(prmprvDTO.getRazonSocialProv().trim()));
		receptor.setGiroRecep("DISTRIBUIDORA");
		receptor.setDirRecep(procesaCaracteres(prmprvDTO.getDireccionProv().trim()));
		receptor.setCmnaRecep(procesaCaracteres(prmprvDTO.getDescComunaProv().trim()));
		receptor.setCiudadRecep(procesaCaracteres(prmprvDTO.getDescCiudadProv().trim()));

		return receptor;
	}
	public Totales totales(VecmarDTO vecmarDTO, int docelec, int valor){
		Totales totales = new Totales();
		
		
		int val = (int) valor;
		//BigInteger neto = new BigInteger(val);
		totales.setMntNeto(BigInteger.valueOf(val));
		//totales.setMntExe(BigInteger.valueOf(0));
		//totales.setTasaIVA(BigDecimal.valueOf(19));
		//totales.setIVA(BigInteger.valueOf(vecmar.getTotalIva()));
		totales.setMntTotal(BigInteger.valueOf(val));
		if (docelec==33){
			//totales.setIVA(BigInteger.valueOf(vecmar.getTotalIva()));
			totales.setTasaIVA(BigDecimal.valueOf(19));
		}else if (docelec==34){
			//totales.setMntExe(BigInteger.valueOf(vecmar.getTotalDocumento()));
		}
		
		
		
		
		return totales;
		
	}
	public int detalleDoc(List vedmar){
		Detalle detalle=null;
		VedmarDTO vedmarDTO = null;
		Iterator iter = vedmar.iterator();
		double valor=0;
		int valorFinalF=0;
		int correlativo =0;
		while (iter.hasNext()){
			correlativo = correlativo+1;
			vedmarDTO = (VedmarDTO) iter.next();
			detalle = new Detalle();
			CdgItem cdgitem = new CdgItem();
			//System.out.println("Detalle:"+cld.getCodigoArticulo());
			cdgitem.setTpoCodigo("INT1");
			cdgitem.setVlrCodigo(String.valueOf(vedmarDTO.getCodigoArticulo()+"-"+vedmarDTO.getDigArticulo()));
			detalle.setNroLinDet(correlativo);
			detalle.getCdgItem().add(cdgitem);
			//String desc2 = procesaCaracteres(vedmarDTO.getDescArticulo());
			//desc = cld.getDescArticulo().replaceAll("ñ", "");
			detalle.setNmbItem(procesaCaracteres(vedmarDTO.getDescArticulo().trim()));
			//detalle.setNmbItem(vedmarDTO.getDescArticulo());
			detalle.setQtyItem(BigDecimal.valueOf(vedmarDTO.getCantidadArticulo()));
			double d = vedmarDTO.getPrecioUnidad();
	        DecimalFormat df = new DecimalFormat("#######.##");
	        System.out.print(df.format(d));
	        String valorFinal = df.format(d);
	        valorFinal = valorFinal.replace(",", ".");
	        double decimal= Double.parseDouble(valorFinal);
			
			
//			double decimal =vedmarDTO.getPrecioUnidad();
//			decimal = decimal*(java.lang.Math.pow(11, 2));
//			decimal = java.lang.Math.round(decimal);
			
			detalle.setPrcItem(BigDecimal.valueOf(decimal));//PRecio
			
			
			double vitem = decimal*vedmarDTO.getCantidadArticulo();
			vitem = vitem*(java.lang.Math.pow(9, 0));
			vitem = java.lang.Math.round(vitem);
			valor = valor + vitem;
			int valorItemFinal = (int) vitem;
			detalle.setMontoItem(BigInteger.valueOf(valorItemFinal));
			
			doc.getDetalle().add(detalle);
			
			//System.out.println("Correlativo:"+cld.getCorrelativo());
		}
		valorFinalF = (int) valor;
		return valorFinalF;
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

public String obtienePersonalizados(TptbdgDTO tptbdg, PrdatcaDTO prdatca){
	 StringBuffer xmlCas = new StringBuffer();
	  String xml="";
	
	 
	 if (prdatca!=null){
		  xmlCas.append("<Personalizados>");
		  xmlCas.append("<DocPersonalizado>");
		  //xmlCas.append("<Impresion>");
		  String desBodega=tptbdg.getDesBodega();
			if (tptbdg.getDesBodega().length()>29){
				desBodega=tptbdg.getDesBodega().substring(0, 29);
			}
		  //xmlCas.append("<Personalizado_01>"+desBodega.trim()+"</Personalizado_01>");
		  xmlCas.append("<campoString name="+"\"Personalizado_01"+"\">"+desBodega.trim()+"</campoString>");
		  if (prdatca.getTelefono()!=null && prdatca.getTelefono().length()>0){
			  //xmlCas.append("<Personalizado_02>"+prdatca.getTelefono()+"</Personalizado_02>"); 
			  xmlCas.append("<campoString name="+"\"Personalizado_02"+"\">"+prdatca.getTelefono().trim()+"</campoString>");
		  }else
		  {
			  //xmlCas.append("<Personalizado_02>"+"66664322"+"</Personalizado_02>");
			  //xmlCas.append("<campoString name="+"\"Personalizado_02"+"\">"+101+"</campoString>");
		  }
		 // xmlCas.append("<campoString name="+"\"Personalizado_02"+"\">"+898989+"</campoString>");	 
		 // xmlCas.append("<campoString name="+"\"Personalizado_03"+"\">"+111333+"</campoString>");	 
		  
		  if (prdatca.getNumOV()>0){
			 // xmlCas.append("<Personalizado_04>"+prdatca.getNumOV()+"</Personalizado_04>");
			  xmlCas.append("<campoString name="+"\"Personalizado_04"+"\">"+prdatca.getNumOV()+"</campoString>");
		  }
		  if (prdatca.getNumCarguio()>0){
			  //xmlCas.append("<Personalizado_05>"+prdatca.getNumCarguio()+"</Personalizado_05>");
			  xmlCas.append("<campoString name="+"\"Personalizado_05"+"\">"+prdatca.getNumCarguio()+"</campoString>");
		  }
		  else{
			  //xmlCas.append("<Personalizado_05>"+"76767"+"</Personalizado_05>");
			 // xmlCas.append("<campoString name="+"\"Personalizado_05"+"\">"+0665+"</campoString>");	 
		  }
		  if (prdatca.getNumeroDomicilio()!=null && prdatca.getNumeroDomicilio().length()>0){
			  //xmlCas.append("<Personalizado_06>"+prdatca.getNumeroDomicilio().trim()+"</Personalizado_06>");
			  xmlCas.append("<campoString name="+"\"Personalizado_06"+"\">"+prdatca.getNumeroDomicilio().trim()+"</campoString>");	 
		  }else{
			  //xmlCas.append("<Personalizado_06>"+"7726262"+"</Personalizado_06>");
			  //xmlCas.append("<campoString name="+"\"Personalizado_06"+"\">"+7726262+"</campoString>");	 
		  }
		  if (prdatca.getDeptoOficina()!=null && prdatca.getDeptoOficina().length()>0){
			  //xmlCas.append("<Personalizado_07>"+prdatca.getDeptoOficina().trim()+"</Personalizado_07>");
			  xmlCas.append("<campoString name="+"\"Personalizado_07"+"\">"+prdatca.getDeptoOficina().trim()+"</campoString>");
		  }else{
			  //xmlCas.append("<Personalizado_07>"+"445566"+"</Personalizado_07>"); 
			  //xmlCas.append("<campoString name="+"\"Personalizado_07"+"\">"+15448543+"</campoString>");
		  }
		  
		 // xmlCas.append("<Personalizado_08>"+"445566"+"</Personalizado_08>");
		  //xmlCas.append("<Personalizado_09>"+"CREDITO"+"</Personalizado_09>");
		  
		  xmlCas.append("<campoString name="+"\"Personalizado_08"+"\">"+15448543+"</campoString>");
		  xmlCas.append("<campoString name="+"\"Personalizado_09"+"\">"+"CREDITO"+"</campoString>");
		  /*if (prdat.getTelefono()!=null && prdat.getTelefono().length()>0){
			  xmlCas.append("<Personalizado_02>"+prdat.getTelefono()+"</Personalizado_02>"); 
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
		  
		  if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().length()>0){
			  int forma = Integer.parseInt(vecmar.getFormaPago());
			  String formapago="";
			  if (forma==1){
				  formapago="CONTADO";
			  }else if (forma==2){
				  formapago="CREDITO";
				  
			  }
			  xmlCas.append("<Personalizado_16>"+forma+"</Personalizado_16>");
			  if (forma==2){
			  if (prdat.getNumCtaCte()!=null && prdat.getNumCtaCte().length()>0){
				  xmlCas.append("<Personalizado_17>"+prdat.getNumCtaCte().trim()+"</Personalizado_17>");
			  }
			  if (prdat.getBanco()!=null && prdat.getBanco().length()>0){
				  xmlCas.append("<Personalizado_18>"+prdat.getBanco().trim()+"</Personalizado_18>");
				  //xmlCas.append("<TermPagoCdg>"+"1"+"</TermPagoCdg>");
				  //xmlCas.append("<TermPagoGlosa>"+"R"+"</TermPagoGlosa>");
				  
				    if (prdat.getPlazo()>0){
						  xmlCas.append("<Personalizado_19>"+prdat.getPlazo()+"</Personalizado_19>");
					  }
				 
				  
			  }
			  }
		  }
		  if (vecmar.getNombreVendedor()!=null && vecmar.getNombreVendedor().length()>0){
			  xmlCas.append("<Personalizado_20>"+vecmar.getNombreVendedor()+"</Personalizado_20>");
		  }*/
		  
		  
		  //xmlCas.append("</Impresion>");
		  xmlCas.append("</DocPersonalizado>");
		  xmlCas.append("</Personalizados>");
		  
	  }
	 xml = xmlCas.toString();
	  return xml;
	 
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
}
