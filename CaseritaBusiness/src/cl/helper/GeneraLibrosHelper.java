package cl.caserita.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.canastas.helper.UsuarioHelper;
import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClcdiaDAO;
import cl.caserita.dao.iface.ClcmcoDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.Conar1DAO;
import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dao.iface.DetimlibDAO;
import cl.caserita.dao.iface.DetlibDAO;
import cl.caserita.dao.iface.GenlibDAO;
import cl.caserita.dao.iface.LibtpdDAO;
import cl.caserita.dao.iface.ResimlibDAO;
import cl.caserita.dao.iface.ReslibDAO;
import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.Conar1DTO;
import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.DetimLibDTO;
import cl.caserita.dto.DetlibDTO;
import cl.caserita.dto.GenlibDTO;
import cl.caserita.dto.LibroImpuestoDTO;
import cl.caserita.dto.LibroTotalesDTO;
import cl.caserita.dto.LibtpdDTO;
import cl.caserita.dto.ResimlibDTO;
import cl.caserita.dto.ReslibDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.enviomail.main.envioMailAviso;
import cl.caserita.enviomail.main.envioMailCaseritaAdjunto;

public class GeneraLibrosHelper {
	public static DAOFactory factory = DAOFactory.getInstance();
	private  static Logger log = Logger.getLogger(GeneraLibrosHelper.class);

	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	public void generaLibroCompras(String tipoLibro, int ano, int mes, String mail,String usuario, int fechaGeneracion, int horaGeneracion, int periodoInicial, int periodoFinal, int empresa){
		LibtpdDAO libtpd = (LibtpdDAO) factory.getLibtpdDAO();
		ConarcDAO conarc = (ConarcDAO) factory.getConarcDAO();
		Conar1DAO conar1 = (Conar1DAO) factory.getConar1DAO();
		GenlibDAO genLib = (GenlibDAO) factory.getGenlibDAO();
		ReslibDAO resLib = (ReslibDAO) factory.getReslibDAO();
		TptbdgDAO tptbdg = (TptbdgDAO) factory.getTptbdgDAO();
		ResimlibDAO resImLib = (ResimlibDAO) factory.getResimlibDAO();
		DetlibDAO  detLib = (DetlibDAO) factory.getDetlibDAO();
		DetimlibDAO detImLib = (DetimlibDAO) factory.getDetimlibDAO();
		TptempDAO tptemp = (TptempDAO) factory.getTptempDAO();
		TptdeleDAO tpt   = (TptdeleDAO) factory.getTptdeleDAO();
		//Recupera Documentos a Procesar
		List documento = libtpd.recuperaDocumentos(tipoLibro);
		LibroTotalesDTO totales = null;
		Iterator iter = documento.iterator();
		String mesCero="";
		String mesString="";
		String anoString="";
		//String fechaString = String.valueOf(ano);
		//anoString = fechaString.substring(0, 4);
		mesString = String.valueOf(mes);
		int mesInt = mes;
		if (mesInt<9){
			mesCero = String.valueOf(mesInt);
			mesCero = "0"+mesCero;
		}else{
			mesCero = String.valueOf(mesInt);
		}
		int anoInt = ano;
		TptempDTO empresaDTO = tptemp.recuperaEmpresa(empresa);
		generaArchivo(tipoLibro, ano, mes,0);
		generaCA(anoInt, mesString, "COMPRA",empresaDTO);
		GenlibDTO genLibDTO = new GenlibDTO();
		genLibDTO.setCodLibro(tipoLibro);
		genLibDTO.setEmpresa(empresa);
		genLibDTO.setCodigoUsuario(usuario);
		genLibDTO.setFechaGeneracion(fechaGeneracion);
		genLibDTO.setHoraGeneracion(horaGeneracion);
		genLibDTO.setPeriodoInicial(periodoInicial);
		genLibDTO.setPeriodoFinal(periodoFinal);
		genLibDTO.setAno(ano);
		genLibDTO.setMes(mes);
		genLibDTO.setBodega(0);
		genLib.generaGenLib(genLibDTO);
		List documento2 = libtpd.recuperaDocumentos(tipoLibro);
		Iterator iter2 = documento2.iterator();
		while (iter2.hasNext()){
			LibtpdDTO libdto = (LibtpdDTO) iter2.next();
			
			totales = conarc.recuperaTotalesIva(ano, mes, libdto.getCodigoDocumento(), conarc.recuperaTotalesPorDocumento(ano, mes, libdto.getCodigoDocumento()));
			int codPaper = tpt.buscaDocumentoElectronicoCompras(libdto.getCodigoDocumento());
			if (totales!=null && totales.getTotalMonto()!=0){
				generaRPCompra(codPaper, totales.getCantidadDocumentos(), 0, totales.getTotalMontoExento(), totales.getTotalMontoNeto(), totales.getTotalMontoIva(), 0,0,0, totales.getTotalMonto());
				ReslibDTO reslibDTO = new ReslibDTO();
				reslibDTO.setCodLibro(tipoLibro);
				reslibDTO.setEmpresa(empresa);
				reslibDTO.setCodigoUsuario(usuario);
				reslibDTO.setFechaGeneracion(fechaGeneracion);
				reslibDTO.setHoraGeneracion(horaGeneracion);
				reslibDTO.setPeriodoInicial(periodoInicial);
				reslibDTO.setPeriodoFinal(periodoFinal);
				reslibDTO.setCodResumen("RP");
				reslibDTO.setCodDocumento(codPaper);
				reslibDTO.setCantDocumento(totales.getCantidadDocumentos());
				reslibDTO.setTotalDocumentoAnulado( totales.getTotalDocumentosAnulados());
				reslibDTO.setTotalMontoExento(totales.getTotalMontoExento());
				reslibDTO.setTotalMontoNeto(totales.getTotalMontoNeto());
				reslibDTO.setTotalMontoIva(totales.getTotalMontoIva());
				reslibDTO.setTotalMontoIvaFueraPlazo(0);
				reslibDTO.setTotalDocumento(totales.getTotalMonto());
				reslibDTO.setTotalIvaActivoFijo(0);
				reslibDTO.setTotalNetoActivoFijo(0);
				resLib.generaResLibCompras(reslibDTO);
				
			}
			
			List impuestos2 = conar1.acumuladoImpuestos(ano, mes, libdto.getCodigoDocumento());
			
			Iterator impm = impuestos2.iterator();
			LibroImpuestoDTO libroIm=null;
			while (impm.hasNext()){
				libroIm = (LibroImpuestoDTO) impm.next();
				generaRPIR(Integer.parseInt(recuperaTasaSII(String.valueOf(libroIm.getCodigoImpuesto()))), libroIm.getMontoImpuesto(),"RPOI");
				ResimlibDTO resimlibDTO = new ResimlibDTO();
				resimlibDTO.setCodLibro(tipoLibro);
				resimlibDTO.setEmpresa(empresa);
				resimlibDTO.setCodigoUsuario(usuario);
				resimlibDTO.setFechaGeneracion(fechaGeneracion);
				resimlibDTO.setHoraGeneracion(horaGeneracion);
				resimlibDTO.setPeriodoInicial(periodoInicial);
				resimlibDTO.setPeriodoFinal(periodoFinal);
				resimlibDTO.setCodResumen("RP");
				resimlibDTO.setCodResumenImpuesto("RPOI");
				resimlibDTO.setCodDocumento(codPaper);
				resimlibDTO.setCodImpuesto(String.valueOf(libroIm.getCodigoImpuesto()));
				resimlibDTO.setMontoResumenImpuesto(libroIm.getMontoImpuesto());
				resImLib.generaResimlib(resimlibDTO);
			}
		}
		while (iter.hasNext()){
			LibtpdDTO libdto = (LibtpdDTO) iter.next();
			List documentos=null;
			int codPaper = tpt.buscaDocumentoElectronicoCompras(libdto.getCodigoDocumento());
//			totales = conarc.recuperaTotalesIva(ano, mes, libdto.getCodigoDocumento(), conarc.recuperaTotalesPorDocumento(ano, mes, libdto.getCodigoDocumento()));
//			int codPaper = tpt.buscaDocumentoElectronico(libdto.getCodigoDocumento());
//			if (totales!=null){
//				generaRPCompra(codPaper, totales.getCantidadDocumentos(), 0, totales.getTotalMontoExento(), totales.getTotalMontoNeto(), totales.getTotalMontoIva(), 0,0,0, totales.getTotalMonto());
//				
//			}
//			
//			List impuestos = conar1.acumuladoImpuestos(ano, mes, libdto.getCodigoDocumento());
//			
//			Iterator im = impuestos.iterator();
//			LibroImpuestoDTO libroIm=null;
//			while (im.hasNext()){
//				libroIm = (LibroImpuestoDTO) im.next();
//				generaRPIR(Integer.parseInt(recuperaTasaSII(String.valueOf(libroIm.getCodigoImpuesto()))), libroIm.getMontoImpuesto(),"RPOI");
//			}
			
			documentos = conarc.buscaDocumentos(libdto.getCodigoDocumento(),ano, mes);
			ConarcDTO conarcDTO = null;
			Conar1DTO Conar1Iva = null;
			Iterator doc = documentos.iterator();
			List imp=null;
			while (doc.hasNext()){
				conarcDTO = (ConarcDTO) doc.next();
				//int tipoDocumento,int folioDocumento, String indicadorDocumento, int tasaImpuesto,int indicadorServicio,String fechaDocumento,int codigoSucursalSII,String rutProveedor,String razonSocial,int montoExento,int montoNeto,int montoIva,int ivaFueraPlazo,int montoTotal
				String fecha = String.valueOf(conarcDTO.getFechaDocumento());
				String anocorto = fecha.substring(0, 4);
				String mescorto = fecha.substring(4, 6);
				String diacorto = fecha.substring(6, 8);
				String fechaanomes = anocorto+"-"+mescorto+"-"+diacorto;
				Conar1Iva = conar1.recuperaImpuestoIva(conarcDTO.getCodDocumento(), conarcDTO.getRutProveedor(), conarcDTO.getDigitoProveedor(), conarcDTO.getNumeroDocumento());
				int ivaCompras =0;
				if (Conar1Iva!=null){
					ivaCompras=Conar1Iva.getMontoImpuesto();
				}
				/*tptbdgDTO = tptbdg.buscaBodega(conarcDTO.get);
				if (tptbdgDTO!=null){
					if (tptbdgDTO.getCodigoSii()==0){
						tptbdgDTO.setCodigoSii(77315448);
					}
				}*/
				generaDHCompras(codPaper, conarcDTO.getNumeroDocumento(), "", 19, fechaanomes, 1, String.valueOf(conarcDTO.getRutProveedor())+"-"+conarcDTO.getDigitoProveedor(), conarcDTO.getNombreProveedor().trim(), conarcDTO.getValorNetoExento(), conarcDTO.getValorNeto(),ivaCompras,0,0,0, conarcDTO.getValorTotalDocumento());
				DetlibDTO detlibDTO = new DetlibDTO();
				detlibDTO.setCodLibro(tipoLibro);
				detlibDTO.setCodigoUsuario(usuario);
				detlibDTO.setEmpresa(empresa);
				detlibDTO.setFechaGeneracion(fechaGeneracion);
				detlibDTO.setHoraGeneracion(horaGeneracion);
				detlibDTO.setPeriodoInicial(periodoInicial);
				detlibDTO.setPeriodoInicial(periodoInicial);
				detlibDTO.setPeriodoFinal(periodoFinal);
				detlibDTO.setCodDetalleLibro("RH");
				detlibDTO.setCodDocumento(codPaper);
				detlibDTO.setFolioDocumento(conarcDTO.getNumeroDocumento());
				detlibDTO.setIdDocumentoAnulado("");
				detlibDTO.setTasaImpuestoDetalle(19);
				detlibDTO.setIdServicioPeriodico(0);
				detlibDTO.setFechaDocumento(fechaanomes);
				detlibDTO.setCodigoSucursalSII(1);
				detlibDTO.setRutProveedor(conarcDTO.getRutProveedor());
				detlibDTO.setRazonSocial(caracteresEspcial(conarcDTO.getNombreProveedor().trim()));
				detlibDTO.setMontoExento(conarcDTO.getValorNetoExento());
				detlibDTO.setMontoIvaDetalle(ivaCompras);
				detlibDTO.setMontoIvaFueraPlazo(0);
				detlibDTO.setMontoTotal(conarcDTO.getValorTotalDocumento());
				detlibDTO.setMontoNetoActivoFijo(0);
				detlibDTO.setMontoIvaActivoFijo(0);
				detlibDTO.setMontoNetoDetalle(conarcDTO.getValorNeto());
				detLib.genDetlibCompras(detlibDTO);
				imp = conar1.recuperaImpuesto(conarcDTO.getCodDocumento(), conarcDTO.getRutProveedor(), conarcDTO.getDigitoProveedor(), conarcDTO.getNumeroDocumento());
				
				Iterator iterIm = imp.iterator();
				while (iterIm.hasNext()){
					Conar1DTO conar1DTO = (Conar1DTO) iterIm.next();
					if (conar1DTO.getCodigoImpuesto()!=2 && conar1DTO.getCodigoImpuesto()!=1 && conar1DTO.getCodigoImpuesto()!=5){
						//log.info("COdigo Impuesto" + clcdiaDTO.getCodigoImpuesto());
						//log.info("Tasa " +recuperaTasaSII(String.valueOf(clcdiaDTO.getCodigoImpuesto())));
						generaDHIR(Integer.parseInt(recuperaTasaSII(String.valueOf(conar1DTO.getCodigoImpuesto()))), conar1DTO.getTasaImpuesto(), conar1DTO.getMontoImpuesto(),"DHOI");
						DetimLibDTO detimLibDTO = new DetimLibDTO();
						detimLibDTO.setCodLibro(tipoLibro);
						detimLibDTO.setEmpresa(empresa);
						detimLibDTO.setFechaGeneracion(fechaGeneracion);
						detimLibDTO.setHoraGeneracion(horaGeneracion);
						detimLibDTO.setPeriodoInicial(periodoInicial);
						detimLibDTO.setPeriodoFinal(periodoFinal);
						detimLibDTO.setCodigoUsuario(usuario);
						detimLibDTO.setFolioDocumento(conarcDTO.getNumeroDocumento());
						detimLibDTO.setCodDocumento(codPaper);
						detimLibDTO.setCodDetalleLibro("DHIR");
						detimLibDTO.setCodigoImpuesto(conar1DTO.getCodigoImpuesto());
						detimLibDTO.setTasaImpuesto(conar1DTO.getTasaImpuesto());
						detimLibDTO.setMontoImpuesto(conar1DTO.getMontoImpuesto());
						detImLib.genDetimlib(detimLibDTO);
					}
					
					
				}
				
			}
		}
		//envioMailAviso mailC = new envioMailAviso();
		envioMailCaseritaAdjunto adjunto = new envioMailCaseritaAdjunto();
		//mailC.envioMail("Nada",mail,archivoLog);
		adjunto.envioMail(archivoLog, mail,"", "Libro Compras Periodo "+ ano +" - " + mes);
		
	}
	
	public void generaLibroVentas(String tipoLibro, int ano, int mes, String mail,String usuario, int fechaGeneracion, int horaGeneracion, int periodoInicial, int periodoFinal, int bodega, int empresa){
		
		LibtpdDAO libtpd = (LibtpdDAO) factory.getLibtpdDAO();
		ClcmcoDAO clcmco = (ClcmcoDAO) factory.getClcmcoDAO();
		ClcdiaDAO clcdia = (ClcdiaDAO) factory.getClcdiaDAO();
		ClmcliDAO clmcli = (ClmcliDAO) factory.getClmcliDAO();
		TptbdgDAO tptbdg = (TptbdgDAO) factory.getTptbdgDAO();
		TptempDAO tptemp = (TptempDAO) factory.getTptempDAO();
		TptdeleDAO tpt   = (TptdeleDAO) factory.getTptdeleDAO();
		LibroTotalesDTO totales = null;
		GenlibDAO genLib = (GenlibDAO) factory.getGenlibDAO();
		ReslibDAO resLib = (ReslibDAO) factory.getReslibDAO();
		ResimlibDAO resImLib = (ResimlibDAO) factory.getResimlibDAO();
		DetlibDAO  detLib = (DetlibDAO) factory.getDetlibDAO();
		DetimlibDAO detImLib = (DetimlibDAO) factory.getDetimlibDAO();
		//Recupera Documentos a Procesar
		List documento = libtpd.recuperaDocumentos(tipoLibro);
		
		Iterator iter = documento.iterator();
		TptempDTO empresaDTO = tptemp.recuperaEmpresa(empresa);
		generaArchivo(tipoLibro, ano, mes, bodega);
		String mesCero="";
		String mesString="";
		String anoString="";
		String fechaString = String.valueOf(ano);
		anoString = fechaString.substring(0, 4);
		mesString = fechaString.substring(4, 6);
		int mesInt = Integer.parseInt(mesString);
		if (mesInt<9){
			mesCero = String.valueOf(mesInt);
			mesCero = "0"+mesCero;
		}else{
			mesCero = String.valueOf(mesInt);
		}
		int anoInt = Integer.parseInt(fechaString.substring(0, 4));
		generaCA(anoInt, mesString, "VENTA",empresaDTO);
		GenlibDTO genLibDTO = new GenlibDTO();
		genLibDTO.setCodLibro(tipoLibro);
		genLibDTO.setEmpresa(empresa);
		genLibDTO.setCodigoUsuario(usuario);
		genLibDTO.setFechaGeneracion(fechaGeneracion);
		genLibDTO.setHoraGeneracion(horaGeneracion);
		genLibDTO.setPeriodoInicial(periodoInicial);
		genLibDTO.setPeriodoFinal(periodoFinal);
		genLibDTO.setAno(Integer.parseInt(anoString));
		genLibDTO.setMes(Integer.parseInt(mesString));
		genLibDTO.setBodega(bodega);
		genLib.generaGenLib(genLibDTO);
		List documento2 = libtpd.recuperaDocumentos(tipoLibro);
		Iterator iterDoc = documento2.iterator();
		
		while (iterDoc.hasNext()){
			LibtpdDTO libdto = (LibtpdDTO) iterDoc.next();
			//tpt.buscaDocumentoElectronico(libdto.codigoDocumento);
			
			//Busca Totales finales por documento
			if (bodega!=0){
				if (libdto.getCodigoDocumento()==3 || libdto.getCodigoDocumento()==4 || libdto.getCodigoDocumento()==33 || libdto.getCodigoDocumento()==34){
					totales = clcmco.totalesBodega(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
				}else if (libdto.getCodigoDocumento()==8 || libdto.getCodigoDocumento()==35){
					totales = clcmco.totalesBodegaNC(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
				}else if (libdto.getCodigoDocumento()==50 ||libdto.getCodigoDocumento()==51 ){
					int codigoDoc = libdto.getCodigoDocumento();
					if (libdto.getCodigoDocumento()==50){
						libdto.setCodigoDocumento(3);
					}else if (libdto.getCodigoDocumento()==51){
						libdto.setCodigoDocumento(4);
					}
					
					totales = clcmco.totalesBodegaExento(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
					totales.setTotalMontoNeto(0);
					libdto.setCodigoDocumento(codigoDoc);
				}
				
			}else{
				if (libdto.getCodigoDocumento()==3 || libdto.getCodigoDocumento()==4 || libdto.getCodigoDocumento()==33 || libdto.getCodigoDocumento()==34){
					totales = clcmco.totales(empresa, libdto.getCodigoDocumento(), ano, mes);
				}else if (libdto.getCodigoDocumento()==8 || libdto.getCodigoDocumento()==35){
					totales = clcmco.totalesNC(empresa, libdto.getCodigoDocumento(), ano, mes);
				}else if (libdto.getCodigoDocumento()==50 ||libdto.getCodigoDocumento()==51 ){
					int codigoDoc = libdto.getCodigoDocumento();
					if (libdto.getCodigoDocumento()==50){
						libdto.setCodigoDocumento(3);
					}else if (libdto.getCodigoDocumento()==51){
						libdto.setCodigoDocumento(4);
					}
					totales = clcmco.totalesExento(empresa, libdto.getCodigoDocumento(), ano, mes);
					totales.setTotalMontoNeto(0);
					libdto.setCodigoDocumento(codigoDoc);
				}
				
			}
			
			int codPaper = tpt.buscaDocumentoElectronico(libdto.getCodigoDocumento());
			if (totales!=null){
				
				generaRP(codPaper, totales.getCantidadDocumentos(), totales.getTotalDocumentosAnulados(), totales.getTotalMontoExento(), totales.getTotalMontoNeto(), totales.getTotalMontoIva(), 0, totales.getTotalMonto());
				ReslibDTO reslibDTO = new ReslibDTO();
				reslibDTO.setCodLibro(tipoLibro);
				reslibDTO.setCodigoUsuario(usuario);
				reslibDTO.setEmpresa(empresa);
				reslibDTO.setCodDocumento(codPaper);
				reslibDTO.setFechaGeneracion(fechaGeneracion);
				reslibDTO.setHoraGeneracion(horaGeneracion);
				reslibDTO.setPeriodoInicial(periodoInicial);
				reslibDTO.setPeriodoFinal(periodoFinal);
				reslibDTO.setCodResumen("RP");
				reslibDTO.setCantDocumento(totales.getCantidadDocumentos());
				reslibDTO.setTotalDocumentoAnulado( totales.getTotalDocumentosAnulados());
				 reslibDTO.setTotalMontoExento(totales.getTotalMontoExento());
				 reslibDTO.setTotalMontoNeto(totales.getTotalMontoNeto());
				 reslibDTO.setTotalMontoIva(totales.getTotalMontoIva());
				 reslibDTO.setTotalMontoIvaFueraPlazo(0);
				 reslibDTO.setTotalDocumento(totales.getTotalMonto());
				resLib.generaResLib(reslibDTO);
			}
			//Busca el acumulado de impuestos
			List impuestos2=null;
			if (bodega!=0){
				
				if (libdto.getCodigoDocumento()==8 || libdto.getCodigoDocumento()==35){
					impuestos2 = clcdia.acumuladoImpuestosBodegaNC(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
				}else{
					impuestos2 = clcdia.acumuladoImpuestosBodega(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
				}
			}else
			{
				if (libdto.getCodigoDocumento()==8 || libdto.getCodigoDocumento()==35){
					impuestos2 = clcdia.acumuladoImpuestosNC(empresa, libdto.getCodigoDocumento(), ano, mes);
				}else{
					impuestos2 = clcdia.acumuladoImpuestos(empresa, libdto.getCodigoDocumento(), ano, mes);
				}
						
			}
			
			
			Iterator imp = impuestos2.iterator();
			LibroImpuestoDTO libroIm=null;
			while (imp.hasNext()){
				libroIm = (LibroImpuestoDTO) imp.next();
				generaRPIR(Integer.parseInt(recuperaTasaSII(String.valueOf(libroIm.getCodigoImpuesto()))), libroIm.getMontoImpuesto(),"RPIR");
				ResimlibDTO resimlibDTO = new ResimlibDTO();
				resimlibDTO.setCodLibro(tipoLibro);
				resimlibDTO.setEmpresa(empresa);
				resimlibDTO.setCodigoUsuario(usuario);
				resimlibDTO.setCodDocumento(codPaper);
				resimlibDTO.setFechaGeneracion(fechaGeneracion);
				resimlibDTO.setHoraGeneracion(horaGeneracion);
				resimlibDTO.setPeriodoInicial(periodoInicial);
				resimlibDTO.setPeriodoFinal(periodoFinal);
				resimlibDTO.setCodResumen("RP");
				resimlibDTO.setCodResumenImpuesto("RPIR");
				resimlibDTO.setCodDocumento(codPaper);
				resimlibDTO.setCodImpuesto(String.valueOf(libroIm.getCodigoImpuesto()));
				resimlibDTO.setMontoResumenImpuesto(libroIm.getMontoImpuesto());
				resImLib.generaResimlib(resimlibDTO);
			}
		}
		
		while (iter.hasNext()){
			LibtpdDTO libdto = (LibtpdDTO) iter.next();
			//tpt.buscaDocumentoElectronico(libdto.codigoDocumento);
			int codPaper = tpt.buscaDocumentoElectronico(libdto.getCodigoDocumento());
			//Busca Totales finales por documento
//			totales = clcmco.totales(libdto.getCodigoDocumento(), ano, mes);
//			int codPaper = tpt.buscaDocumentoElectronico(libdto.getCodigoDocumento());
//			if (totales!=null){
//				generaRP(codPaper, totales.getCantidadDocumentos(), 0, totales.getTotalMontoExento(), totales.getTotalMontoNeto(), totales.getTotalMontoIva(), 0, totales.getTotalMonto());
//				
//			}
//			//Busca el acumulado de impuestos
//			
//			List impuestos = clcdia.acumuladoImpuestos(libdto.getCodigoDocumento(), mes, ano);
//			
//			Iterator im = impuestos.iterator();
//			LibroImpuestoDTO libroIm=null;
//			while (im.hasNext()){
//				libroIm = (LibroImpuestoDTO) im.next();
//				generaRPIR(libroIm.getCodigoImpuesto(), libroIm.getMontoImpuesto(),"RPIR");
//			}
			List documentos =null;
			if (bodega!=0){
				if (libdto.getCodigoDocumento()==8 || libdto.getCodigoDocumento()==35){
					documentos = clcmco.recuperaDocumentosBodegaNC(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
				}else{
					documentos = clcmco.recuperaDocumentosBodega(empresa, libdto.getCodigoDocumento(), ano, mes, bodega);
				}
				 
			}else{
				if (libdto.getCodigoDocumento()==8 || libdto.getCodigoDocumento()==35){
					documentos = clcmco.recuperaDocumentosNC(empresa, libdto.getCodigoDocumento(), ano, mes);
				}else{
					 documentos = clcmco.recuperaDocumentos(empresa, libdto.getCodigoDocumento(), ano, mes);
				}
				
			}
			
			ClcmcoDTO clc = null;
			Iterator doc = documentos.iterator();
			TptbdgDTO tptbdgDTO = null;
			while (doc.hasNext()){
				clc = (ClcmcoDTO) doc.next();
				if (clc.getNumeroDocumento()==11056170){
					log.info("Pruebas");
				}
				//int tipoDocumento,int folioDocumento, String indicadorDocumento, int tasaImpuesto,int indicadorServicio,String fechaDocumento,int codigoSucursalSII,String rutProveedor,String razonSocial,int montoExento,int montoNeto,int montoIva,int ivaFueraPlazo,int montoTotal
				String fecha = String.valueOf(clc.getFechaMovimiento());
				String anocorto = fecha.substring(0, 4);
				String mescorto = fecha.substring(4, 6);
				String diacorto = fecha.substring(6, 8);
				String fechaanomes = anocorto+"-"+mescorto+"-"+diacorto;
				ClmcliDTO clm = clmcli.recuperaCliente(String.valueOf(clc.getRutCliente()), clc.getDvCliente());
				
				if (clm!=null){
					String anulado="";
					if (clc.getEstado()==1){
						anulado="A";
					}
					tptbdgDTO = tptbdg.buscaBodega(clc.getCodigoBodega());
					if (tptbdgDTO!=null){
						if (tptbdgDTO.getCodigoSii()==0){
							tptbdgDTO.setCodigoSii(77315448);
						}
					}
					generaDH(codPaper, clc.getNumeroDocumento(), anulado, 19, 0, fechaanomes, tptbdgDTO.getCodigoSii(), String.valueOf(clc.getRutCliente())+"-"+clc.getDvCliente(), clm.getRazonsocial().trim(), clc.getTotalExento(), clc.getTotalNeto(), clc.getTotalIva(), 0, clc.getTotalDocumento());
					DetlibDTO detlibDTO = new DetlibDTO();
					detlibDTO.setCodLibro(tipoLibro);
					detlibDTO.setCodDetalleLibro("DH");
					detlibDTO.setEmpresa(empresa);
					detlibDTO.setCodigoUsuario(usuario);
					detlibDTO.setFechaGeneracion(fechaGeneracion);
					detlibDTO.setHoraGeneracion(horaGeneracion);
					detlibDTO.setPeriodoInicial(periodoInicial);
					detlibDTO.setPeriodoFinal(periodoFinal);
					detlibDTO.setCodDocumento(codPaper);
					detlibDTO.setFolioDocumento(clc.getNumeroDocumento());
					detlibDTO.setIdDocumentoAnulado(anulado);
					detlibDTO.setTasaImpuestoDetalle(19);
					detlibDTO.setIdServicioPeriodico(0);
					detlibDTO.setFechaDocumento(fechaanomes);
					
					
					detlibDTO.setCodigoSucursalSII(tptbdgDTO.getCodigoSii());
					detlibDTO.setRutProveedor(Integer.parseInt(clc.getRutCliente()));
					detlibDTO.setRazonSocial(clm.getRazonsocial());
					detlibDTO.setMontoExento(clc.getTotalExento());
					detlibDTO.setMontoIvaDetalle(clc.getTotalIva());
					detlibDTO.setMontoIvaFueraPlazo(0);
					detlibDTO.setMontoTotal(clc.getTotalDocumento());
					detlibDTO.setMontoNetoDetalle(clc.getTotalNeto());
					detLib.genDetlib(detlibDTO);
					List imp = clcdia.obtieneImpuesto(empresa, clc.getCodDocumento(), Integer.parseInt(clc.getRutCliente()), clc.getDvCliente(), clc.getFechaMovimiento(), clc.getHoraMovimiento());
					if (imp!=null){
						Iterator iterIm = imp.iterator();
						while (iterIm.hasNext()){
							ClcdiaDTO clcdiaDTO = (ClcdiaDTO) iterIm.next();
							if (clcdiaDTO.getCodigoImpuesto()!=2 && clcdiaDTO.getCodigoImpuesto()!=1){
								//log.info("COdigo Impuesto" + clcdiaDTO.getCodigoImpuesto());
								//log.info("Tasa " +recuperaTasaSII(String.valueOf(clcdiaDTO.getCodigoImpuesto())));
								
								generaDHIR(Integer.parseInt(recuperaTasaSII(String.valueOf(clcdiaDTO.getCodigoImpuesto()))), clcdiaDTO.getImpuesto(), clcdiaDTO.getMontoImpuesto(),"DHIR");
								DetimLibDTO detimLibDTO = new DetimLibDTO();
								detimLibDTO.setCodLibro(tipoLibro);
								detimLibDTO.setEmpresa(empresa);
								detimLibDTO.setCodigoUsuario(usuario);
								detimLibDTO.setCodDetalleLibro("DHIR");
								detimLibDTO.setFechaGeneracion(fechaGeneracion);
								detimLibDTO.setHoraGeneracion(horaGeneracion);
								detimLibDTO.setPeriodoInicial(periodoInicial);
								detimLibDTO.setPeriodoFinal(periodoFinal);
								detimLibDTO.setCodigoUsuario(usuario);
								detimLibDTO.setCodDocumento(codPaper);
								detimLibDTO.setCodDetalleLibro("DHIR");
								detimLibDTO.setCodigoImpuesto(clcdiaDTO.getCodigoImpuesto());
								detimLibDTO.setMontoImpuesto(clcdiaDTO.getMontoImpuesto());
								detImLib.genDetimlib(detimLibDTO);
							}
							
							
						}
					}
					
				}
				
				
			}
			
			
		}
		//envioMailAviso mailC = new envioMailAviso();
		//mailC.envioMail("Nada",mail,archivoLog);
		envioMailCaseritaAdjunto adjunto = new envioMailCaseritaAdjunto();
		adjunto.envioMail(archivoLog, mail, "","Libro de Ventas Periodo "+ ano +" - " + mes);
		
	}
	public void generaArchivo(String tipoLibro, int ano, int mes, int bodega){
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String descBodega="TODAS LAS BODEGAS";
		if (bodega!=0){
			TptbdgDAO tptbdg = (TptbdgDAO) factory.getTptbdgDAO();
			TptbdgDTO tptbdgDTO = tptbdg.buscaBodega(bodega);
			descBodega=tptbdgDTO.getDesBodega().trim();
		}else
		
		
	
		pathProperties = Constants.FILE_PROPERTIES;
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String fechaLOG = fch.getFechaconFormato();
		String nombreArchivoAProcesar=fechaLOG;
		String archivo=tipoLibro;
		if ("LVENTAS".equals(tipoLibro)){
			archivoLog=prop.getProperty("archivos.salidalventas.path")+archivo+"_"+ano+mes+"_"+fechaStr+"_"+descBodega+".txt";;
		}else if ("LCOMPRAS".equals(tipoLibro)){
			archivoLog=prop.getProperty("archivos.salidalcompras.path")+archivo+"_"+ano+mes+"_"+fechaStr+".txt";;
		}
		
		
		
		File f=new File(archivoLog);
		if (f.exists()){
			log.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterLog=new FileWriter(archivoLog,true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	public void generaCA(int ano, String mes, String tipo, TptempDTO empresa){
		try{
			fileWriterLog.write( "CA"+ "|"+empresa.getRut()+"-"+empresa.getDv()+ "|"+ano +"-"+mes +"|"  +tipo+"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void generaRPCompra(int tipoDocumento,int cantidadDocumento,int totalDocAnulados,int totalMontoExento,long totalMontoNeto,int totalMontoIva,int netoActivoFijo,int ivaActivoFijo,int ivaUsoComun,long totalMonto){
		try{
			fileWriterLog.write( "RP"+ "|"+tipoDocumento  + "|"+cantidadDocumento +"|" +totalDocAnulados +"|" +  totalMontoExento+"|"+  totalMontoNeto+"|"+totalMontoIva +"|"+  netoActivoFijo+"|" + ivaActivoFijo +"|" + ivaUsoComun+"|"+totalMonto+"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void generaRP(int tipoDocumento,int cantidadDocumento,int totalDocAnulados,int totalMontoExento,long totalMontoNeto,int totalMontoIva,int totalIvaFueraPlazo,long totalMonto){
		try{
			fileWriterLog.write( "RP"+ "|"+tipoDocumento  + "|"+cantidadDocumento +"|" +totalDocAnulados +"|" +  totalMontoExento+"|"+ totalMontoNeto+"|"+ totalMontoIva +"|"+  totalIvaFueraPlazo+"|"  +totalMonto+"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void generaRPIR(int codigoImpuesto,int valorImpuesto, String enc){
		try{
			fileWriterLog.write( enc+ "|"+codigoImpuesto  + "|"  +valorImpuesto+"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void generaDHCompras(int tipoDocumento,int folioDocumento, String indicadorDocumento, int tasaImpuesto,String fechaDocumento,int codigoSucursalSII,String rutProveedor,String razonSocial,int montoExento,int montoNeto,int montoIva,int netoActivoFijo,int ivaActivoFijo,int montoIvaUsoComun,int montoTotal){
		try{
			fileWriterLog.write( "DH"+ "|"+tipoDocumento  + "|"  +folioDocumento +"|"+ indicadorDocumento+"|"+  tasaImpuesto +"|" + fechaDocumento+"|" + codigoSucursalSII+"|"+  rutProveedor+"|"+razonSocial  +"|"+ montoExento+"|" + montoNeto+ "|"+montoIva+"|"+ netoActivoFijo +"|"+ivaActivoFijo+"|" + montoIvaUsoComun+"|" +montoTotal +"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void generaDH(int tipoDocumento,int folioDocumento, String indicadorDocumento, int tasaImpuesto,int indicadorServicio,String fechaDocumento,int codigoSucursalSII,String rutProveedor,String razonSocial,int montoExento,int montoNeto,int montoIva,int ivaFueraPlazo,int montoTotal){
		try{
			fileWriterLog.write( "DH"+ "|"+tipoDocumento  + "|"  +folioDocumento +"|"+ indicadorDocumento+"|"+  tasaImpuesto +"|" + indicadorServicio+"|"+ fechaDocumento+"|" + codigoSucursalSII+"|"+  rutProveedor+"|"+razonSocial  +"|"+ montoExento+"|" +montoNeto+"|"  +montoIva+"|"+ ivaFueraPlazo+"|" +montoTotal +"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void generaDHIR(int codigoImpuesto,double tasaImpuesto,int valorImpuesto, String enc){
		try{
			fileWriterLog.write( enc+ "|"+codigoImpuesto  + "|"  +tasaImpuesto+"|"+valorImpuesto+"\n");
			fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public String recuperaTasaSII(String codigo){
		  
		  String tasas="";
		  prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			pathProperties = Constants.FILE_PROPERTIES;
			tasas=prop.getProperty(codigo);
			
		  return tasas;
	  }
	public void generaMail(){
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		envioMailCaseritaAdjunto adj = new envioMailCaseritaAdjunto();
		String nombre=prop.getProperty("rutaCompras")+"LCOMPRAS_2013060120130610_20130710_1123.txt";
		//adj.envioMail(nombre, "jcanquil@gmail.com");
	}
	public String caracteresEspcial(String nombre){
		
		
		nombre = nombre.replaceAll("'", "''");
		
		return nombre;
	}
}
