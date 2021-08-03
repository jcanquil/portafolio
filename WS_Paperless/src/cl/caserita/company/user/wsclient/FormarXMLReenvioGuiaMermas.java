package cl.caserita.company.user.wsclient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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
import cl.caserita.dto.PrmprvDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public class FormarXMLReenvioGuiaMermas {

	private static Documento doc = null;
	 private static Properties prop=null;
		private static String pathProperties;
		
	public String xml(VecmarDTO vecmarDTO,List vedmar,  PrmprvDTO prmprvDTO, int docelec, int docref, List acteco,TptempDTO tpt){
		String stringxml="";
		System.out.println("Procesa XML");
		DTEDefType dte = new DTEDefType();
		doc = new Documento();		
		//Documento doc = new Documento();
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(acteco, tpt);
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
			
		
		enc.setIdDoc(idoc(vecmarDTO, docelec));
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
	        
	       
	        stringxml = stringxml.replaceAll("dteDefType", "DTE");
			   
			//System.out.println(stringxml);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("Pruebas");
		}
		
		
		
		
		return stringxml;
	
	}
	public IdDoc idoc(VecmarDTO vecmar, int codele){
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
	    id.setFolio(BigInteger.valueOf(vecmar.getNumeroTipoDocumento()));
	    //getXMLCalendar
	    //20121203
	    String fecha = String.valueOf(vecmar.getFechaDocumento());
	    String año = fecha.substring(0, 4);
	    String mes = fecha.substring(4, 6);
	    String dia = fecha.substring(6, 8);
	    fecha = año +"-"+mes+"-"+dia;
	    
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
	public Emisor emisor(List acteco,TptempDTO tptemp ){
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
