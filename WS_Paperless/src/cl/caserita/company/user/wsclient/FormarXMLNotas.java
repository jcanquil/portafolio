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
import cl.caserita.dte.DTEDefType.Documento.Referencia;
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
import cl.caserita.dto.ConnodDTO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;


public class FormarXMLNotas {

	private static Documento doc = null;
	 private static Properties prop=null;
		private static String pathProperties;
		private static HashMap impuestosSII =null;
	public String xml(VecmarDTO vecmar, List listaTotal, List connod, ClmcliDTO clmcli, int docelec, ConnohDTO connoh, int codref, int codRefDoc, int fecha, String nota, List acteco, ClcmcoDTO clcmcoDTO,TptempDTO tpt,HashMap impuestoSII, PrdatcaDTO prdat){
		String stringxml="";
		System.out.println("Procesa XML");
		DTEDefType dte = new DTEDefType();
		doc = new Documento();		
		//Documento doc = new Documento();
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(acteco, tpt);
		Receptor rec = receptor(vecmar,clmcli);
		impuestosSII=impuestoSII;
	
		CalculoExentoNetoDTO montos = detalleDoc(connod, connoh);
		
		
		
		enc.setEmisor(emi);
		enc.setIdDoc(idoc);
		enc.setReceptor(rec);
		if  ("C".equals(connoh.getTipoNota())){
			Totales tot = totales(listaTotal, vecmar, docelec, montos, clcmcoDTO);
			enc.setTotales(tot);
		}
		
		
		doc.setID("");

		doc.setEncabezado(enc);
		dte.setVersion(BigDecimal.valueOf(1.0));
		dte.setDocumento(doc);
			
		
		enc.setIdDoc(idoc(vecmar, docelec,prdat));
		Referencia ref = new Referencia();
		ref.setNroLinRef(1);
		ref.setTpoDocRef(String.valueOf(codRefDoc));
		ref.setFolioRef(String.valueOf(connoh.getNumeroDocumento()));
		int notaPar = Integer.parseInt(nota);
		ref.setCodRef(BigInteger.valueOf(notaPar));
		 String fecha2 = String.valueOf(fecha);
		 
		    String a??o = fecha2.substring(0, 4);
		    String mes = fecha2.substring(4, 6);
		    String dia = fecha2.substring(6, 8);
		    fecha2 = a??o +"-"+mes+"-"+dia;
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
			
			//m.setProperty(Marshaller.JAXB_ENCODING, "Unicode");
			//m.setProperty("jaxb.encoding", "Unicode");
			//m.JAXB_ENCODING.
		    
			
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        m.marshal(dte, baos);
	        String xml = new String(baos.toByteArray());
	        stringxml = xml;
	        
	        
	        stringxml = stringxml.replaceAll("dteDefType", "DTE");
	        stringxml = stringxml.replaceAll("dteDefType", "DTE");

	        int largo = stringxml.length();
	        stringxml = stringxml.substring(0, largo-7);
	        stringxml = stringxml + obtienePersonalizados(vecmar, prdat)+"</DTE>";
	        stringxml = stringxml.replaceAll("\n","");
	       // stringxml = procesaCaracteres(stringxml);
			//System.out.println(stringxml);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		return stringxml;
	
	}
	public IdDoc idoc(VecmarDTO vecmar, int codele, PrdatcaDTO prdat){
		IdDoc id = new IdDoc();
		BigInteger tipoDte = BigInteger.valueOf(codele);
		if (codele==33){
			BigInteger pago = new BigInteger(vecmar.getFormaPago());
			if (vecmar.getFormaPago().equals("3")){
				 pago = new BigInteger("1");
			}else if (vecmar.getFormaPago().equals("4")){
				pago = new BigInteger("2");
			}else if (vecmar.getFormaPago().equals("5")){
				pago = new BigInteger("1");
			}else if (vecmar.getFormaPago().equals("6")){
				pago = new BigInteger("1");
			}else if (vecmar.getFormaPago().equals("7")){
				pago = new BigInteger("1");
			}else if (vecmar.getFormaPago().equals("8")){
				pago = new BigInteger("1");
			}
			id.setFmaPago(pago);
		}
			if (codele==39){
				id.setIndServicio(BigInteger.valueOf(3));
			}
		
		id.setTipoDTE(tipoDte);
	    id.setFolio(BigInteger.valueOf(0));
	    //getXMLCalendar
	    //20121203
	    String fecha = String.valueOf(vecmar.getFechaDocumento());
	    String a??o = fecha.substring(0, 4);
	    String mes = fecha.substring(4, 6);
	    String dia = fecha.substring(6, 8);
	    fecha = a??o +"-"+mes+"-"+dia;
	    String fechaDespacho = String.valueOf(vecmar.getFechaDespacho());
	    /*if (!"".equals(fechaDespacho)){
	    	a??o = fechaDespacho.substring(0, 4);
		    mes = fechaDespacho.substring(4, 6);
		    dia = fechaDespacho.substring(6, 8);
		    fechaDespacho = a??o +"-"+mes+"-"+dia;
	    }*/
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
	    
	    if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().length()>0 && vecmar.getFormaPago()!=""){
			  int forma = 1;//
				  //Integer.parseInt(vecmar.getFormaPago());
			  String formapago="";
			  if (forma==1){
				  formapago="CONTADO";
			  }
				  formapago="CREDITO";
				  	//id.setNumCtaPago("123456");
				  	//id.setBcoPago("SANTANDER");
				  	//id.setTermPagoDias(BigInteger.valueOf(12));
				
			
		  }
	    
	    try{
	    	
	    	id.setFchEmis(getXMLCalendar(fecha));
	    	
	    	if (codele==33){
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
	public Emisor emisor(List acteco,  TptempDTO tptemp){
		Emisor emisor = new Emisor();
		
		emisor.setRUTEmisor(tptemp.getRut()+"-"+tptemp.getDv());
		emisor.setRznSoc(procesaCaracteres(tptemp.getRazonSocial().trim()));
		
		emisor.setGiroEmis(tptemp.getGiro().trim());
		
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
		
		int rut = Integer.parseInt(vecmar.getRutProveedor());
		//SE QUITA opcion de generacion de Notas de credito con RUT 6666666, segun emilio deben generse con el mismo rut de emisor de boleta o factura
		/*if (rut<40000){
			vecmar.setRutProveedor("66666666");
			vecmar.setDvProveedor("6");
			
		}*/
		receptor.setRUTRecep(vecmar.getRutProveedor()+"-"+vecmar.getDvProveedor());
		receptor.setRznSocRecep(procesaCaracteres(clmcli.getRazonsocial().trim()));
		receptor.setGiroRecep(procesaCaracteres(clmcli.getTipoNegocio().trim()));
		//receptor.setDirRecep(procesaCaracteres(vecmar.getDireccionDespacho().trim()));
		String direccionClie = clmcli.getDireccionCliente().substring(0, 30);
		receptor.setDirRecep(procesaCaracteres(direccionClie.trim()));
		//String descComuna=vecmar.getDescComuna();
		String descComuna=clmcli.getDescComuna();
		if (vecmar.getDescComuna().length()>20){
			descComuna = vecmar.getDescComuna().substring(0, 19);
		}
		receptor.setCmnaRecep(procesaCaracteres(descComuna.trim()));
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
		
		/*String descCiudad = clmcli.getDescCiudad();
		if (clmcli.getDescCiudad().length()>20){
			descCiudad = clmcli.getDescCiudad().substring(0, 19);
		}
        receptor.setCiudadRecep(procesaCaracteres(descCiudad.trim()));*/
		//receptor.setCmnaRecep(procesaCaracteres(vecmar.getDescComuna().trim()));

		return receptor;
	}
	public Totales totales(List clcdia, VecmarDTO vecmar, int docelec,CalculoExentoNetoDTO montos, ClcmcoDTO clcmco){
		Totales totales = new Totales();
		double tasaImp=0;
		int impuesto=0;
		int iva=0;
		//if (clcmco.getCodDocumento()!=42){
		String netoString = String.valueOf(clcmco.getValorNeto());
		BigInteger neto = new BigInteger(netoString);
		totales.setMntNeto(neto);
		//}
		//totales.setMntExe(BigInteger.valueOf(0));
		//totales.setTasaIVA(BigDecimal.valueOf(19));
		//totales.setIVA(BigInteger.valueOf(vecmar.getTotalIva()));
		totales.setMntTotal(BigInteger.valueOf(clcmco.getTotalDocumento()));
		if (clcmco.getTotalIva()>0){
			totales.setIVA(BigInteger.valueOf(clcmco.getTotalIva()));
			totales.setTasaIVA(BigDecimal.valueOf(19));
		}
		ImptoReten imp = null;
		ClcdiaDTO clc = new ClcdiaDTO();
		if (docelec==61){
			Iterator iter = clcdia.iterator();
			while (iter.hasNext()){
				clc = (ClcdiaDTO)iter.next();
				if (clc.getCodigoImpuesto()!=2 && clc.getMontoImpuesto()!=0){
					imp = new ImptoReten();
					//System.out.println("Valor" + clc.getMontoImpuesto());
					BigInteger valor = BigInteger.valueOf(clc.getMontoImpuesto());
					imp.setMontoImp(valor);
					impuesto=impuesto+clc.getMontoImpuesto();
					//Definir TipoImpuesto en sistema por cada impuesto
					//imp.setTipoImp(recuperaTasaSII(String.valueOf(clc.getCodigoImpuesto())));
					imp.setTipoImp(String.valueOf(obtieneImpuestoSII(clc.getCodigoImpuesto())));
					imp.setTasaImp(BigDecimal.valueOf(clc.getImpuesto()));
					totales.getImptoReten().add(imp);
					tasaImp=tasaImp+clc.getImpuesto();
				}
				else
				{
					iva = clc.getMontoImpuesto();
					impuesto=impuesto+clc.getMontoImpuesto();
				}
				
			}
		}
		if (vecmar.getTotalIva()>0){
			totales.setIVA(BigInteger.valueOf(iva));
		}
		if (clcmco.getTotalExento()>0){
//			montos.setMontoImp1(impuesto);
//			tasaImp=tasaImp+19;
//			tasaImp=(tasaImp/100)+1;
//			double netoFinal = montos.getMontoNeto()/tasaImp;
//			netoFinal = netoFinal*(java.lang.Math.pow(10, 0));
//			netoFinal = java.lang.Math.round(netoFinal);
//			int neto2 = (int) netoFinal;
			totales.setMntExe(BigInteger.valueOf(clcmco.getTotalExento()));
			//if (clcmco.getCodDocumento()!=42){
				totales.setMntNeto(BigInteger.valueOf(clcmco.getValorNeto()));
			//}
			
		}else
		{
			
//			double netoFinal = vecmar.getTotalDocumento()-impuesto;
//			netoFinal = netoFinal*(java.lang.Math.pow(10, 0));
//			netoFinal = java.lang.Math.round(netoFinal);
//			int neto2 = (int) netoFinal;
			totales.setMntNeto(BigInteger.valueOf(clcmco.getValorNeto()));
		}
		
		return totales;
		
	}
	public CalculoExentoNetoDTO detalleDoc(List connod, ConnohDTO connoh){
		Detalle detalle=null;
		ConnodDTO cld = null;
		CalculoExentoNetoDTO calculo =new CalculoExentoNetoDTO();
		Iterator iter = connod.iterator();
		int montoExento=0;
		int montoNeto=0;
		int correlativo =0;
		while (iter.hasNext()){
			correlativo = correlativo+1;
			cld = (ConnodDTO) iter.next();
			detalle = new Detalle();
			CdgItem cdgitem = new CdgItem();
			//System.out.println("Detalle:"+cld.getCodigoArticulo());
			cdgitem.setTpoCodigo("INT1");
			cdgitem.setVlrCodigo(String.valueOf(cld.getCodArticulo()+"-"+cld.getDigArticulo()));
			detalle.setNroLinDet(correlativo);
			//detalle.setNroLinDet(cld.getCorrelativo()+1);
			detalle.getCdgItem().add(cdgitem);
			//String desc2 = procesaCaracteres(cld.getDescripcion());
			//desc = cld.getDescArticulo().replaceAll("??", "");
			detalle.setNmbItem(procesaCaracteres(cld.getDescripcion().trim()));
			//detalle.setNmbItem(cld.getDescripcion().trim());
			
			if (cld.getCantidad()!=0){
				detalle.setQtyItem(BigDecimal.valueOf(cld.getCantidad()));
			}else if (cld.getCantidad()==0){
				//sE COMENTARIZA POR nc DIFERENCIA PRECIO
				detalle.setQtyItem(BigDecimal.valueOf(1));
			}
			if (cld.getCodArticulo()==22 && cld.getPrecioNeto()==0){
				cld.setPrecioNeto(1);
				cld.setPrecioUnitario(1);
			}else if (cld.getCodArticulo()==4632 && cld.getPrecioNeto()==0){
				cld.setPrecioNeto(1);
				cld.setPrecioUnitario(1);
				
				cld.setDescLinea(1);
			}
			if ("C".equals(connoh.getTipoNota())){
				if (cld.getPrecioUnitario()!=0 ){
					detalle.setPrcItem(BigDecimal.valueOf(cld.getPrecioUnitario()));//PRecio
				}
				
				if (cld.getTotalNeto()!=0){
					detalle.setMontoItem(BigInteger.valueOf(cld.getTotalNeto()));
				}else if (cld.getMontoExento()>0){
					detalle.setMontoItem(BigInteger.valueOf(cld.getMontoExento()));
				}else if (cld.getTotalNeto()==0 && cld.getDescLinea()!=0){
					detalle.setMontoItem(BigInteger.valueOf(cld.getTotalNeto()));
				}else if (cld.getCantidad()==0 && cld.getTotalNeto()==0){
					detalle.setMontoItem(BigInteger.valueOf(0));
				}
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
			
			//System.out.println("Correlativo:"+cld.getCorrelativo());
			}
			doc.getDetalle().add(detalle);
			calculo.setMontoExento(montoExento);
			calculo.setMontoNeto(montoNeto);
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
	  
	 descripcion = Normalizer.normalize(descripcion, Normalizer.Form.NFD);
	 descripcion = descripcion.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

	 descripcion = descripcion.replace("??", "N");
	 descripcion = descripcion.replace("??", "N");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("<", "&lt;");
	 descripcion = descripcion.replace(">", "&gt;");
	 descripcion = descripcion.replace("&", "&amp;");
	 descripcion = descripcion.replace("\"", "&quot;");
	 descripcion = descripcion.replace("'", "&apos;");
	 descripcion = descripcion.replace("??", "A");
	 descripcion = descripcion.replace("??", "E");
	 descripcion = descripcion.replace("??", "I");
	 descripcion = descripcion.replace("??", "O");
	 descripcion = descripcion.replace("??", "U");
	 descripcion = descripcion.replace("??", "a");
	 descripcion = descripcion.replace("??", "e");
	 descripcion = descripcion.replace("??", "i");
	 descripcion = descripcion.replace("??", "o");
	 descripcion = descripcion.replace("??", "u");
	 descripcion = descripcion.replace("N??", "N");
	 descripcion = descripcion.replace("G??", "G");
	 descripcion = descripcion.replace("??", "N");
	 descripcion = descripcion.replace("??", "N");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("??", "N");
	 descripcion = descripcion.replace("??", "N");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("N??", "");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("?", "");
	 descripcion = descripcion.replace("N?", "N");
	 descripcion = descripcion.replace("N??", "N");
	 descripcion = descripcion.replace("??", "");
	 descripcion = descripcion.replace("??", "D");
	 descripcion = descripcion.replaceAll("[^\\p{ASCII}]", "");
	 return descripcion;
}
 public String obtienePersonalizados(VecmarDTO vecmar, PrdatcaDTO prdat){
	 StringBuffer xmlCas = new StringBuffer();
	  String xml="";
	  System.out.println("Fecha:"+prdat.getNumeroOficinaPri());
	  if (prdat!=null){
		  String formapago="";
		  if (vecmar.getFormaPago()!=null && vecmar.getFormaPago().length()>0){
			  int forma = Integer.parseInt(vecmar.getFormaPago());
			  
			  if (forma==1){
				  formapago="CONTADO";
			  }else if (forma==2){
				  formapago="CREDITO";
				  
			  }
		  }
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
		  if (prdat.getContacto()!=null && prdat.getContacto().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_10"+"\">"+prdat.getContacto().trim()+"</campoString>");
			
		  }
		  if (prdat.getTelefono()!=null && prdat.getTelefono().length()>0){
			  xmlCas.append("<campoString name="+"\"Personalizado_11"+"\">"+prdat.getTelefono().trim()+"</campoString>");  
			 
		  }
		  
		  //xmlCas.append("<campoString name="+"\"Personalizado_12"+"\">"+101+"</campoString>");
		  
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
	  
	 xml = xmlCas.toString();
	  return xml;
	 
 }
 
 public static void main (String []args){
	 
	 //String desc="PAP. TOALLA ??OVA CL??SICA N??24 X2";
	 String desc="PISCO ARTESANOS 35?? 190CC ";
	 FormarXMLNotas not = new FormarXMLNotas();
	 System.out.println("String corregido :" +not.procesaCaracteres(desc));
	 
 }
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
