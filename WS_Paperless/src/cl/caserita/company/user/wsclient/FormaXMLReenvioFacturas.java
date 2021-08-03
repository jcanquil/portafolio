package cl.caserita.company.user.wsclient;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
import cl.caserita.dte.DTEDefType.Documento.Encabezado.Totales.ImptoReten;
import cl.caserita.dto.ActecoDTO;
import cl.caserita.dto.CalculoExentoNetoDTO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;

public class FormaXMLReenvioFacturas {

	private static Documento doc = null;
	 private static Properties prop=null;
		private static String pathProperties;
		
	public String xml(VecmarDTO vecmar, List listaTotal, List cldmco, ClmcliDTO clmcli, int docelec, ClcmcoDTO clcmcoDTO, List acteco, TptempDTO tpt,PrdatcaDTO prdat){
		String stringxml="";
		//System.out.println("Procesa XML");
		DTEDefType dte = new DTEDefType();
		doc = new Documento();		
		//Documento doc = new Documento();
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(acteco, tpt);
		Receptor rec = receptor(vecmar,clmcli);
		
		//Detalle deta = detalleDoc2(cldmco, listaTotal);
		CalculoExentoNetoDTO det = detalleDoc(cldmco, listaTotal);
		Totales tot = totales(listaTotal, vecmar, docelec, det, clcmcoDTO);
		enc.setEmisor(emi);
		enc.setIdDoc(idoc);
		enc.setReceptor(rec);
		enc.setTotales(tot);
		
		doc.setID("");
		doc.setEncabezado(enc);
		dte.setVersion(BigDecimal.valueOf(1.0));
		dte.setDocumento(doc);
			
		
		enc.setIdDoc(idoc(vecmar, docelec));
		String xml2=formaXMLString(enc,doc, tpt, prdat, vecmar);
       //genera archivo TXT
      // String texto = fh(vecmar, clmcli)+fhir(vecmar)+DH(cldmco)+pe(vecmar);
       //String xmlString ="<DTE version="1.0" xmlns:ns2="http://www.w3.org/2000/09/xmldsig#" xmlns="http://www.sii.cl/SiiDte">";
       
		/*try{
			//DTEDefType.class.getPackage().getName()
			//JAXBContext jc = JAXBContext.newInstance("cl.caserita.dte");
			JAXBContext jc = JAXBContext.newInstance("cl.caserita.dte");
			
			Marshaller m = jc.createMarshaller();
			
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
			m.setProperty("jaxb.encoding", "ISO-8859-1");
			//m.JAXB_ENCODING.
		    
			
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        m.marshal(dte, baos);
	        String xml = new String(baos.toByteArray());
	        stringxml = xml;
	        
	       // stringxml = stringxml.replaceAll("UTF-8", "ISO-8859-1");
	        stringxml = stringxml.replaceAll("dteDefType", "DTE");
	        
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("Pruebas");
		}
		*/
		
		
		
		return xml2;
	
	}
	public IdDoc idoc(VecmarDTO vecmar, int codele){
		IdDoc id = new IdDoc();
		BigInteger tipoDte = BigInteger.valueOf(codele);
		if (codele==33 && ""!=vecmar.getFormaPago() && vecmar.getFormaPago()!=null ){
			BigInteger pago = new BigInteger(vecmar.getFormaPago());
			id.setFmaPago(pago);
		}
			if (codele==39){
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
	    String fechaDespacho="";
	    if (vecmar.getFechaDespacho()!=0){
	    	fechaDespacho = String.valueOf(vecmar.getFechaDespacho());
	    	año = fechaDespacho.substring(0, 4);
	 	    mes = fechaDespacho.substring(4, 6);
	 	    dia = fechaDespacho.substring(6, 8);
	 	    fechaDespacho = año +"-"+mes+"-"+dia;
	    }
	    
	   
	    try{
	    	System.out.println(getXMLCalendar(fecha));
	    	id.setFchEmis(getXMLCalendar(fecha));
	    	
	    	if (codele==33 && vecmar.getFechaDespacho()!=0){
	    	id.setFchVenc(getXMLCalendar(fechaDespacho));
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
		
		//System.out.println("DATOS EMPRESA XML");
		emisor.setRUTEmisor(tptemp.getRut()+"-"+tptemp.getDv());
		emisor.setRznSoc(procesaCaracteres(tptemp.getRazonSocial().trim()));
		emisor.setGiroEmis(procesaCaracteres(tptemp.getGiro()));
		
		//emisor.setCdgSIISucur(new BigInteger(44));
		emisor.setDirOrigen(procesaCaracteres(tptemp.getDireccionCasaMatriz().trim()));
		emisor.setCmnaOrigen(procesaCaracteres(tptemp.getComuna().trim()));
		
		Iterator iter = acteco.iterator();
		while (iter.hasNext()){
			ActecoDTO actecoDTO = (ActecoDTO)iter.next();
			emisor.getActeco().add(BigInteger.valueOf(Integer.parseInt(actecoDTO.getCodigoActeco().trim())));
		}
	    
		return emisor;
	}
	public Receptor receptor(VecmarDTO vecmar,ClmcliDTO clmcli){
		Receptor receptor = new Receptor();
		receptor.setRUTRecep(vecmar.getRutProveedor()+"-"+vecmar.getDvProveedor());
		receptor.setRznSocRecep(procesaCaracteres(clmcli.getRazonsocial().trim()));
		receptor.setGiroRecep(clmcli.getTipoNegocio().trim());
		
		String direccionClie = clmcli.getDireccionCliente().substring(0, 30);
		/*if (!"".equals(vecmar.getDireccionDespacho()) && vecmar.getDireccionDespacho()!=null ){
			System.out.println("Direccion cliente CLMCLI");
			receptor.setDirRecep(procesaCaracteres(vecmar.getDireccionDespacho().trim()));
		}else{*/
			receptor.setDirRecep(procesaCaracteres(direccionClie.trim()));
		//}
		
		
		/*if (vecmar.getDescComuna()!=null && vecmar.getDescComuna()!=""){
			String descComuna=vecmar.getDescComuna();
			if (vecmar.getDescComuna().length()>20){
				descComuna = vecmar.getDescComuna().substring(0, 19);
			}
			receptor.setCmnaRecep(procesaCaracteres(descComuna.trim()));
		}else{*/
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
		/*receptor.setRUTRecep(vecmar.getRutProveedor()+"-"+vecmar.getDvProveedor());
		receptor.setRznSocRecep(procesaCaracteres(clmcli.getRazonsocial().trim()));
		receptor.setGiroRecep(clmcli.getTipoNegocio().trim());
		receptor.setDirRecep(procesaCaracteres(vecmar.getDireccionDespacho().trim()));
		String descComuna=vecmar.getDescComuna();
		if (vecmar.getDescComuna().length()>20){
			descComuna = vecmar.getDescComuna().substring(0, 19);
		}
		receptor.setCmnaRecep(procesaCaracteres(descComuna.trim()));
		String descCiudad = clmcli.getDescCiudad();
		if (clmcli.getDescCiudad().length()>20){
			descCiudad = clmcli.getDescCiudad().substring(0, 19);
		}
       receptor.setCiudadRecep(procesaCaracteres(descCiudad.trim()));*/
		return receptor;
	}
	public Totales totales(List clcdia, VecmarDTO vecmar, int docelec, CalculoExentoNetoDTO montos, ClcmcoDTO clcmcoDTO){
		Totales totales = new Totales();
		int impuesto=0;
		String netoString = String.valueOf(clcmcoDTO.getValorNeto());
		BigInteger neto = new BigInteger(netoString);
		totales.setMntNeto(neto);
		double tasaImp=0;
		//totales.setMntExe(BigInteger.valueOf(0));
		//totales.setTasaIVA(BigDecimal.valueOf(19));
		//totales.setIVA(BigInteger.valueOf(vecmar.getTotalIva()));
		totales.setMntTotal(BigInteger.valueOf(clcmcoDTO.getTotalDocumento()));
		
		if (docelec==33 ){
			totales.setIVA(BigInteger.valueOf(clcmcoDTO.getTotalIva()));
			totales.setTasaIVA(BigDecimal.valueOf(19));
		}else if (docelec==34){
			totales.setMntExe(BigInteger.valueOf(vecmar.getTotalDocumento()));
		}
		ImptoReten imp = null;
		ClcdiaDTO clc = new ClcdiaDTO();
		if (docelec==33 ){
			Iterator iter = clcdia.iterator();
			while (iter.hasNext()){
				clc = (ClcdiaDTO)iter.next();
				imp = new ImptoReten();
				//System.out.println("Valor" + clc.getMontoImpuesto());
				if (clc.getMontoImpuesto()!=0 && clc.getCodigoImpuesto()!=2){
					BigInteger valor = BigInteger.valueOf(clc.getMontoImpuesto());
					imp.setMontoImp(valor);
					impuesto=impuesto+clc.getMontoImpuesto();
					
					//Definir TipoImpuesto en sistema por cada impuesto
					imp.setTipoImp(recuperaTasaSII(String.valueOf(clc.getCodigoImpuesto())));
					imp.setTasaImp(BigDecimal.valueOf(clc.getImpuesto()));
					tasaImp=tasaImp+clc.getImpuesto();
					totales.getImptoReten().add(imp);
				}
				
			}
		}
		if (montos.getMontoExento()!=0){
//			montos.setMontoImp1(impuesto);
//			tasaImp=tasaImp+19;
//			tasaImp=(tasaImp/100)+1;
//			double netoFinal = montos.getMontoNeto()/tasaImp;
//			netoFinal = netoFinal*(java.lang.Math.pow(10, 0));
//			netoFinal = java.lang.Math.round(netoFinal);
//			int neto2 = (int) netoFinal;
//			totales.setMntExe(BigInteger.valueOf(montos.getMontoExento()));
//			totales.setMntNeto(BigInteger.valueOf(neto2));
			//System.out.println("Mueva Valores del exento");
			
			totales.setMntExe(BigInteger.valueOf(clcmcoDTO.getTotalExento()));
			if (clcmcoDTO.getCodDocumento()!=36){
				totales.setMntNeto(BigInteger.valueOf(clcmcoDTO.getValorNeto()));
			}else
			{
				totales.setMntNeto(BigInteger.valueOf(0));
			}
			
			//System.out.println("Mueva Neto:"+clcmcoDTO.getValorNeto());
		}
		
		
		return totales;
		
	}
	public Detalle detalleDoc2(List cldmco, List clcdia){
		Detalle detalle=null;
		CalculoExentoNetoDTO calculo =new CalculoExentoNetoDTO();
		CldmcoDTO cld = null;
		Iterator iter = cldmco.iterator();
		int montoExento=0;
		int montoNeto=0;
		int correlativo =0;
		while (iter.hasNext()){
			correlativo = correlativo+1;
			
			cld = (CldmcoDTO) iter.next();
			detalle = new Detalle();
			
			
			CdgItem cdgitem = new CdgItem();
			//System.out.println("Detalle:"+cld.getCodigoArticulo());
			
			if (cld.getCodDocumento()!=39){
				cdgitem.setTpoCodigo("INT1");
				cdgitem.setVlrCodigo(String.valueOf(cld.getCodigoArticulo()+"-"+cld.getDigitoverificador()));
				detalle.getCdgItem().add(cdgitem);
			}
			
			detalle.setNroLinDet(correlativo);
			
			if (cld.getCodDocumento()==39){
				cld.setDescArticulo(cld.getGlosa());
			}
			//String desc = procesaCaracteres(cld.getDescArticulo());
			//desc = desc.replace("ñ", "");
			//desc = cld.getDescArticulo().replaceAll("ñ", "");
			detalle.setNmbItem(procesaCaracteres(cld.getDescArticulo().trim()));
			detalle.setQtyItem(BigDecimal.valueOf(cld.getCantidadArticulo()));
			detalle.setPrcItem(BigDecimal.valueOf(cld.getPrecioNeto()));//PRecio
			int netoLinea = (int) cld.getValorNeto();
			detalle.setMontoItem(BigInteger.valueOf(netoLinea));
			//System.out.println("Descuento Entero");
			if (cld.getDescuentoLinea()!=0){
				 int desc = (int) cld.getDescuentoLinea();
				// System.out.println("Descuento Linea Entero"+desc);
				detalle.setDescuentoMonto(BigInteger.valueOf(desc));
			}
			List clddia = cld.getImpuestos();
			
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
					if (extari.getCodImpto()!=1){
						detalle.getCodImpAdic().add(recuperaTasaSII(String.valueOf(extari.getCodImpto())));
						montoNeto=montoNeto+cld.getMontoCompra();
					}
					else{
						montoExento = montoExento+cld.getMontoCompra();
						detalle.setIndExe(BigInteger.valueOf(1));
					}
					
					
				}
				//String []imp ={codigoImp};
				
			}else
			{
				montoNeto=montoNeto+cld.getMontoCompra();
			}
			doc.getDetalle().add(detalle);
			calculo.setMontoExento(montoExento);
			calculo.setMontoNeto(montoNeto);
			//System.out.println("Correlativo:"+cld.getCorrelativo());
		}
		
		return detalle;
	}
	public CalculoExentoNetoDTO detalleDoc(List cldmco, List clcdia){
		Detalle detalle=null;
		CalculoExentoNetoDTO calculo =new CalculoExentoNetoDTO();
		CldmcoDTO cld = null;
		Iterator iter = cldmco.iterator();
		int montoExento=0;
		int montoNeto=0;
		int correlativo =0;
		while (iter.hasNext()){
			correlativo = correlativo+1;
			
			cld = (CldmcoDTO) iter.next();
			if (cld.getCodigoArticulo()>0 && cld.getDigitoverificador()!=""){
			detalle = new Detalle();
			
			
			CdgItem cdgitem = new CdgItem();
			//System.out.println("Detalle:"+cld.getCodigoArticulo());
			
			if (cld.getCodDocumento()!=39){
				cdgitem.setTpoCodigo("INT1");
				cdgitem.setVlrCodigo(String.valueOf(cld.getCodigoArticulo()+"-"+cld.getDigitoverificador()));
				detalle.getCdgItem().add(cdgitem);
			}
			
			detalle.setNroLinDet(correlativo);
			
			if (cld.getCodDocumento()==39){
				cld.setDescArticulo(procesaCaracteres(cld.getGlosa()));
			}
			//String desc = procesaCaracteres(cld.getDescArticulo());
			//desc = desc.replace("ñ", "");
			//desc = cld.getDescArticulo().replaceAll("ñ", "");
			detalle.setNmbItem(procesaCaracteres(cld.getDescArticulo().trim()));
			detalle.setQtyItem(BigDecimal.valueOf(cld.getCantidadArticulo()));
			detalle.setPrcItem(BigDecimal.valueOf(cld.getPrecioNeto()));//PRecio
			int netoLinea = (int) cld.getValorNeto();
			//detalle.setMontoItem(BigInteger.valueOf(netoLinea));
			if (netoLinea!=0){
				detalle.setMontoItem(BigInteger.valueOf(netoLinea));
			}else{
				netoLinea=cld.getMontoCompra();
				detalle.setMontoItem(BigInteger.valueOf(netoLinea));
			}
			if (cld.getDescuentoLinea()!=0){
				 int desc = (int) cld.getDescuentoLinea();
				 //System.out.println("Descuento Linea Entero"+desc);
				detalle.setDescuentoMonto(BigInteger.valueOf(desc));
			}
			List clddia = cld.getImpuestos();
			
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
					if (extari.getCodImpto()!=1){
						detalle.getCodImpAdic().add(recuperaTasaSII(String.valueOf(extari.getCodImpto())));
						montoNeto=montoNeto+cld.getMontoCompra();
					}
					else{
						montoExento = montoExento+cld.getMontoCompra();
						detalle.setIndExe(BigInteger.valueOf(1));
						montoNeto=montoNeto+cld.getMontoCompra();
					}
					
					
				}
				//String []imp ={codigoImp};
				
			}else
			{
				montoNeto=montoNeto+cld.getMontoCompra();
			}
			doc.getDetalle().add(detalle);
			calculo.setMontoExento(montoExento);
			calculo.setMontoNeto(montoNeto);
			//System.out.println("Correlativo:"+cld.getCorrelativo());
		}
		}
		return calculo;
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
	 descripcion = descripcion.replace("Ñ", "N");
	 descripcion = descripcion.replace("º", "");
	 descripcion = descripcion.replace("´", "");
	 descripcion = descripcion.replace("?", "");
	 descripcion = descripcion.replace("N?", "");
	 descripcion = descripcion.replace("N°", "N");
	  
	  return descripcion;
 }
 public String formaXMLString(Encabezado enca, Documento doc, TptempDTO tpt,PrdatcaDTO prdat, VecmarDTO vecmar){
	  StringBuffer xmlCas = new StringBuffer();
	  String xml="";
	  String formapago="";

	  xmlCas.append(recuperaTasaSII("xml1"));
	  xmlCas.append(recuperaTasaSII("xml2"));
	  xmlCas.append(recuperaTasaSII("xml3"));
	  xmlCas.append("<Encabezado>");
	  xmlCas.append("<IdDoc>");
	  xmlCas.append("<TipoDTE>"+enca.getIdDoc().getTipoDTE()+"</TipoDTE>");
	  xmlCas.append("<Folio>"+enca.getIdDoc().getFolio()+"</Folio>");
	  xmlCas.append("<FchEmis>"+enca.getIdDoc().getFchEmis()+"</FchEmis>");
	  if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().length()>0){
		  int forma = Integer.parseInt(vecmar.getFormaPago());
		  //System.out.println("Forma de Pago 0:"+forma);
		  if (forma==0){
			  forma=1;
			//  System.out.println("Forma de Pago 1:"+forma);
		  }
		  
		  
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
	  }else{
		  xmlCas.append("<FmaPago>"+1+"</FmaPago>");
		  formapago="CONTADO";
		  
	  }
	  
	  xmlCas.append("</IdDoc>");
	  xmlCas.append("<Emisor>");
	  xmlCas.append("<RUTEmisor>"+tpt.getRut()+"-"+tpt.getDv().trim()+"</RUTEmisor>");
	  xmlCas.append("<RznSoc>"+tpt.getRazonSocial().trim()+"</RznSoc>");
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
//	  if (enca.getIdDoc().getTipoDTE().longValue()==33){
//		  xmlCas.append("<TasaIVA>"+enca.getTotales().getTasaIVA()+"</TasaIVA>");
//		  xmlCas.append("<IVA>"+enca.getTotales().getIVA()+"</IVA>");
//	  }
	  
	  
	  
	  
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
		  
		  xmlCas.append("<CdgItem>");
		  while (cgi.hasNext()){
			  cdgitem =(CdgItem) cgi.next();
			  xmlCas.append("<TpoCodigo>"+cdgitem.getTpoCodigo()+"</TpoCodigo>");
			  xmlCas.append("<VlrCodigo>"+cdgitem.getVlrCodigo()+"</VlrCodigo>");
		  }
		  xmlCas.append("</CdgItem>");
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
	 
	  xmlCas.append("</Documento>");
	  if (prdat!=null){
		  xmlCas.append("<Personalizados>");
		  xmlCas.append("<DocPersonalizado xmlns="+"\"http://www.sii.cl/SiiDte"+"\" "+"dteID="+"\""+"\">");
		  //xmlCas.append("<DocPersonalizado>");
		  //xmlCas.append("<Impresion>");
		  xmlCas.append("<campoString name="+"\"Personalizado_01"+"\">"+vecmar.getDescBodega()+"</campoString>");
		  //xmlCas.append("<Personalizado_01>"+vecmar.getBodegaOrigen()+"</Personalizado_01>");
		  if (prdat.getTelefono()!=null && prdat.getTelefono().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_02"+"\">"+prdat.getTelefono()+"</campoString>");
			 // xmlCas.append("<Personalizado_02>"+prdat.getTelefono()+"</Personalizado_02>");
		  }
		  if (prdat.getNumOV()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_03"+"\">"+prdat.getNumOV()+"</campoString>");
			  //xmlCas.append("<Personalizado_03>"+prdat.getNumOV()+"</Personalizado_03>");
		  }
		  if (prdat.getDireccion()!=null && prdat.getDireccion().length()>0){
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
			 // xmlCas.append("<campoString name="+"\"Personalizado_07"+"\">"+"NINGUNA"+"</campoString>");
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
		  if (prdat.getContacto()!=null && !prdat.getContacto().trim().equals("") && prdat.getContacto().length()>0){
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
		  
		  xmlCas.append("<campoString name="+"\"Personalizado_16"+"\">"+formapago.trim()+"</campoString>");
		 
		  if (prdat.getNumCarguio()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_17"+"\">"+prdat.getNumCarguio()+"</campoString>");
			
		  }
		  
		  
		
		  xmlCas.append("</DocPersonalizado>");
		  xmlCas.append("</Personalizados>");
		  
	  }
	  xmlCas.append("</DTE>");
	  xml = xmlCas.toString();
	  return xml;
 }
}
