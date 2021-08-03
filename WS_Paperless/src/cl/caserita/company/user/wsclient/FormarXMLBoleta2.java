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
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.paperless.boleta.BOLETADefType;
import cl.paperless.boleta.EnvioBOLETA;

import cl.paperless.boleta.PersonalizadosDefType;
import cl.paperless.boleta.BOLETADefType.Documento;
import cl.paperless.boleta.BOLETADefType.Documento.DscRcgGlobal;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.IdDoc;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.Emisor;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.Receptor;
import cl.paperless.boleta.BOLETADefType.Documento.Encabezado.Totales;
import cl.paperless.boleta.BOLETADefType.Documento.Detalle;
import cl.paperless.boleta.BOLETADefType.Documento.Detalle.CdgItem;
import cl.paperless.boleta.EnvioBOLETA.SetDTE;
import cl.paperless.boleta.PersonalizadosDefType.DocPersonalizado;
import cl.paperless.boleta.PersonalizadosDefType.DocPersonalizado.Impresion;
import cl.paperless.dto.DetalleDTO;

public class FormarXMLBoleta2 {

	private static Documento doc = null;
	private static SetDTE setDte=null;
	 private static Properties prop=null;
		private static String pathProperties;
		
	public String xml(VecmarDTO vecmar, List listaTotal, List cldmco, ClmcliDTO clmcli, int docelec, TptempDTO tpt, ClcmcoDTO clc){
		
		String stringxml="";
		System.out.println("Procesa XML");
		BOLETADefType boleta = new BOLETADefType();
		EnvioBOLETA b = new EnvioBOLETA();
		DocPersonalizado d = new DocPersonalizado();
		Impresion imp = new Impresion();
	
		doc = new Documento();
		setDte = new SetDTE();
		Encabezado enc = new Encabezado();
		IdDoc idoc = new IdDoc();
		Emisor emi = emisor(tpt);
		PersonalizadosDefType perso = new PersonalizadosDefType();
		
		perso.getDocPersonalizado();
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
		setDte.getDTE().add(boleta);
		b.setSetDTE(setDte);
		enc.setIdDoc(idoc(vecmar, docelec));
        StringWriter writer = new StringWriter();
        //genera archivo TXT
       
        try{
			final JAXBContext jc = JAXBContext.newInstance(EnvioBOLETA.class);
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
		//nOreceptor.setDirRecep(vecmar.getDireccionDespacho().trim());
		//NOreceptor.setCmnaRecep(vecmar.getDescComuna().trim());

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
	
	
	public IdDoc idoc(VecmarDTO vecmar, int codele){
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
	    
	    try{
	    	//System.out.println(getXMLCalendar(fecha));
	    	id.setFchEmis(getXMLCalendar(fecha));
	    	
	    	if (vecmar.getFechaDespacho()!=0 && codele==39){
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
}
