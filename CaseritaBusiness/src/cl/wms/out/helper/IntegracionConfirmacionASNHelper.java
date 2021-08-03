package cl.caserita.wms.out.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CargfwmsDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.ChoftranDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.ConsolidaasnDAO;
import cl.caserita.dao.iface.DocconfcDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.StockdifDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.CargfwmsDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ChoftranDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.ConsolidaasnDTO;
import cl.caserita.dto.DocconfcDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.RutservDTO;
import cl.caserita.dto.StockdifDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.emailInformacionErroresWMS;
import cl.caserita.enviomail.main.enviaMailStockNegativo;

public class IntegracionConfirmacionASNHelper {
	private static Logger logi = Logger.getLogger(IntegracionConfirmacionASNHelper.class);
	private static emailInformacionErroresWMS email = new emailInformacionErroresWMS();
	private static enviaMailStockNegativo mailNegativo = new enviaMailStockNegativo();

	public void procesaReconciliacion(String urlFile, String nameFile, String tipo){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			//Document doc = dBuilder.parse(fXmlFile);
			
			
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			
			
			AjusteDTO ajuste = null;
			doc.getDocumentElement().normalize();
			int numeroCarguio=0;
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			logi.info("Nombre Archivo ASN:"+nameFile);
			if ("CIERRE".equals(tipo)){
				NodeList nList = doc.getElementsByTagName("RCV_TRLR_CLOSE_IFD_VC");
				
				logi.info("----------------------------");
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					logi.info("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
						Element eElement = (Element) nNode;
						

						logi.info("TRKNUM : " + eElement.getElementsByTagName("TRKNUM").item(0).getTextContent());
						logi.info("YARD_LOC : " + eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent());
						numeroCarguio=Integer.parseInt(eElement.getElementsByTagName("TRKNUM").item(0).getTextContent().substring(3, eElement.getElementsByTagName("TRKNUM").item(0).getTextContent().length()));
						
						String andenRecepcion=eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent();
						
						procesaCobro( numeroCarguio, nameFile, andenRecepcion);

					}
				}
			}else{
				NodeList nList = doc.getElementsByTagName("INVENTORY_RECEIPT_IFD_SEG");
				
				logi.info("----------------------------");
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					logi.info("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
						Element eElement = (Element) nNode;
						

						logi.info("TRKNUM : " + eElement.getElementsByTagName("TRKNUM").item(0).getTextContent());
						

						logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
						
						logi.info("RCVQTY : " + eElement.getElementsByTagName("RCVQTY").item(0).getTextContent());
						
						logi.info("RCVSTS : " + eElement.getElementsByTagName("RCVSTS").item(0).getTextContent());
						logi.info("EXPQTY : " + eElement.getElementsByTagName("EXPQTY").item(0).getTextContent());
						
						detalleDTO.setCodArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
						detalleDTO.setCantidad(Integer.parseInt(eElement.getElementsByTagName("RCVQTY").item(0).getTextContent()));
						detalleDTO.setCantidadDespachada(Integer.parseInt(eElement.getElementsByTagName("EXPQTY").item(0).getTextContent()));
						
						StringTokenizer st = new StringTokenizer(eElement.getElementsByTagName("TRKNUM").item(0).getTextContent(),"-");
						int campo=0;
						
						String correlativo="";
						 while (st.hasMoreTokens( )){
							 
							 	String tr = st.nextToken();
							 	logi.info(tr);
							 	if (campo==0){
							 		numeroCarguio=Integer.parseInt(tr.substring(3, tr.length()));
							 	}else if(campo==1){
							 		correlativo=tr;
							 	}
							 campo++;
					        
					    }
						 
						 procesaASN(detalleDTO, numeroCarguio, nameFile);

					}
				}
			}
			
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
	}
	public void procesaCobro(int carguio, String nombreArchivo, String andenRecep){
		DAOFactory dao = DAOFactory.getInstance();
		CarguioDAO carguioDAO = dao.getCarguioDAO();
		VecmarDAO vecmarDAO = dao.getVecmarDAO();
		VedmarDAO vedmarDAO = dao.getVedmarDAO();
		ConsolidaasnDAO consolida = dao.getConsolidaasnDAO();
		CargfwmsDAO carfwmsDAO = dao.getCargfwmsDAO();
		ChoftranDAO choftranDAO = dao.getChoftranDAO();
		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		ClmcliDAO clmcli = dao.getClmcliDAO();
		TpacorDAO tpacorDAO = dao.getTpacorDAO();
		ExmarbDAO exmarbDAO = dao.getExmarbDAO();
		StockdifDAO stockdif = dao.getStockdifDAO();
		logi.info("N O M B R E  A R C H I V O : " +nombreArchivo);
		DocconfcDAO docconfDAO = dao.getDocconfcDAO();
		RutservDAO rutservDAO = dao.getRutServDAO();
		try{
			//Recupera EndPoint facturacion electronica
			RutservDTO rutservDTO = rutservDAO.recuperaEndPointServlet("FACTUR");
			
			
			int numeroDoc=0;
			int codTipVendedor=0;
			int docGenerado=0;
			int rutCobro=0;
			String dvCobro="";
			
			Fecha fch = new Fecha();
			List lista = consolida.recuperaConsolidadoCompleto(2, carguio, 26);
			CarguioDTO carguioDTO = carguioDAO.obtieneCarguioDTO(2, carguio, 26);
			DocconfcDTO docconf = new DocconfcDTO();
			docconf.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
			docconf.setNumeroCarguio(carguioDTO.getNumeroCarguio());
			docconf.setPatente(carguioDTO.getPatente().trim());
			docconf.setCodigoBodega(carguioDTO.getCodigoBodega());
			// Si el chofer pertenece a Caserita solo se emite boleta sino es factura
			ChoftranDTO chof = choftranDAO.obtenerDatos(carguioDTO.getRutChofer(), carguioDTO.getDvChofer());
			int codDoc=0;
			if (chof.getRutEmpresa()==76288567 || chof.getRutEmpresa()==96509850){
				codDoc=34;
				
			}else{
				codDoc=33;
			}
			rutCobro=chof.getRutEmpresa();
			dvCobro=chof.getDvEmpresa();
			
			VecmarDTO vecmar = null;
			Iterator iter = lista.iterator();
			while (iter.hasNext()){
				ConsolidaasnDTO dto = (ConsolidaasnDTO) iter.next();
				//Genera Boleta
				//Verifica si tiene documento  generado
				 docconf = docconfDAO.recuperaDocumento(docconf);
				 
				 if (docconf!=null && docconf.getNumeroDocumento()>0){
					numeroDoc = docconf.getNumeroDocumento();
					vecmar = vecmarDAO.obtenerDatosVecmarMer(carguioDTO.getCodigoEmpresa(), 21, docconf.getFechaConfirmacion(), numeroDoc);
					logi.info("RECUPERA VECMAR");
					codTipVendedor=vecmar.getCodigoTipoVendedor();
					docGenerado=1;
				 }else{
					//numeroDoc = tpacorDAO.recupeCorrelativo(0, 45);
					String str = "ASYSRCD00 00008   0  01   ";
					numeroDoc = proce.obtieneCorrelativo(str);
					logi.info("Correlativo Documento Cobro: "+numeroDoc);
					docconf = new DocconfcDTO();
					docconf.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
					docconf.setNumeroCarguio(carguioDTO.getNumeroCarguio());
					docconf.setPatente(carguioDTO.getPatente());
					docconf.setCodigoBodega(carguioDTO.getCodigoBodega());
					docconf.setNumeroDocumento(numeroDoc);
					docconf.setFechaConfirmacion(Integer.parseInt(fch.getYYYYMMDD()));
					docconfDAO.insertaDocumentoCarguio(docconf);
					VecmarDTO vecmarDTO = new VecmarDTO();

					vecmarDTO.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
					vecmarDTO.setCodTipoMvto(21);
					vecmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setNumDocumento(numeroDoc);
					vecmarDTO.setCodigoDocumento(codDoc);
					vecmarDTO.setFechaDocumento(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setBodegaOrigen(carguioDTO.getCodigoBodega());
					vecmarDTO.setFormaPago("1");
					vecmarDTO.setCantidadLineaDetalle(1);
					vecmarDTO.setTotalBruto(0);
					vecmarDTO.setTotalNeto(0);
					vecmarDTO.setTotalImptoAdicional(0);
					vecmarDTO.setTotalIva(0);
					vecmarDTO.setTotalDocumento(0);
					
					vecmarDTO.setRutProveedor(String.valueOf(rutCobro));
					vecmarDTO.setDvProveedor(dvCobro.trim());
					//Vendedor en duro hasta saber que digan lo contrario
					vecmarDTO.setCodigoVendedor(274);
					vecmarDTO.setSwitchDescto(0);
					vecmarDTO.setSwichProceso(0);
					vecmarDTO.setIndicadorDespacho("N");
					vecmarDTO.setSwitchPagoCaja("P");
					vecmarDTO.setCodigoTipoVendedor(4000);
					codTipVendedor=4000;
					vecmarDAO.generaMovimientoCobro(vecmarDTO);
				 }
				 
				if (dto.getCantidadDiferencia() > 0)
				{ 
					 //Genera VECMAR
					VedmarDTO vedmarDTO = new VedmarDTO();
					ExmarbDTO exmarbDTO = new ExmarbDTO();
					
					//Genera VEDMAR
					ExmarbDTO exmarb = exmarbDAO.recuperaArticulo(carguioDTO.getCodigoBodega(),dto.getCodigoArticulo() );
					vedmarDTO.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
					vedmarDTO.setCodTipoMvto(21);
					vedmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vedmarDTO.setNumDocumento(numeroDoc);
					int correlativo = vedmarDAO.obtenerCorrelativo(carguioDTO.getCodigoEmpresa(), 21, Integer.parseInt(fch.getYYYYMMDD()), numeroDoc);
					vedmarDTO.setCorrelativo(correlativo);
					vedmarDTO.setCodigoBodega(carguioDTO.getCodigoBodega());
					vedmarDTO.setCodigoArticulo(dto.getCodigoArticulo());
					vedmarDTO.setDigArticulo(exmarb.getDvArticulo());
					vedmarDTO.setFormato("U");
					vedmarDTO.setVolumenArticulo(0);
					vedmarDTO.setPesoLinea(0);
					
					//vedmarDTO.setCantidadArticulo(dto.getCantidad());
					//vedmarDTO.setCantidadFormato(dto.getCantidad());
					vedmarDTO.setCantidadArticulo(dto.getCantidadDiferencia());
					vedmarDTO.setCantidadFormato(dto.getCantidadDiferencia());
					
					vedmarDTO.setSectorBodega(exmarb.getCodigoSector());
					vedmarDTO.setPrecioUnidad(dto.getPrecioBruto());
					vedmarDTO.setPrecioNeto(dto.getPrecioNeto());
					vedmarDTO.setCostoNeto(exmarb.getCostoNeto());
					vedmarDTO.setVolumenArticulo(0);
					vedmarDTO.setMontoFlete(0);
					
					//vedmarDTO.setCostoTotalNeto(exmarb.getCostoNeto()*dto.getCantidad());
					vedmarDTO.setCostoTotalNeto(exmarb.getCostoNeto()*dto.getCantidadDiferencia());
					
					vedmarDTO.setMontoBrutoLinea(vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoNeto(vedmarDTO.getPrecioNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setPorcentajeDesto(0);
					vedmarDTO.setMontoDescuentoLinea(0);
					vedmarDTO.setMontoDescuentoNeto(0);
					vedmarDTO.setMontoTotalLinea((int)vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					//int netoTOtal = vedmarDTO.getPrecioNeto()*vedmarDTO.getCantidadArticulo();
					//int totalNeto = (int) vedmarDTO.getMontoNeto();
					double totalNeto =  vedmarDTO.getPrecioNeto()*(int)vedmarDTO.getCantidadArticulo();
					logi.info("Neto Linea :"+totalNeto);
					int tneto = (int)Math.round(totalNeto);
					vedmarDTO.setMontoTotalNetoLinea((int)tneto);
					
					//double totalNeto =  vedmarDTO.getMontoNeto();
					logi.info("Neto Linea :"+totalNeto);
					//vedmarDTO.setMontoTotalNetoLinea((int)totalNeto);
					//Verificar EXENTO
					vedmarDTO.setMontoExento(0);
					vedmarDTO.setCodIngresoSalida("S");
					vedmarDTO.setSwitchProceso(0);
					vedmarDAO.generaMovimiento(vedmarDTO);
					//Actualiza STOCK
					int stockLineaArt=exmarb.getStockLinea()-vedmarDTO.getCantidadArticulo();
					if (stockLineaArt<0){
						//mailNegativo.envioMail("Procesa ASN LINEA 321 :"+"NOMBRE ARCHIVO :"+ nombreArchivo + vedmarDTO.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarb.getStockLinea() + "STOCK A MOVER : "+ vedmarDTO.getCantidadArticulo()+" STOCK AHORA : "+stockLineaArt);
					}
					
					
					exmarbDAO.actualizaStockLinea(vedmarDTO.getCodigoBodega(), vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo().trim(), exmarb.getStockLinea()-vedmarDTO.getCantidadArticulo());
					
					StockdifDTO stock = new StockdifDTO();
					stock.setCodigoEmpresa(vedmarDTO.getCodigoEmpresa());
					stock.setCodigoBodega(vedmarDTO.getCodigoBodega());
					stock.setCodigoArticulo(vedmarDTO.getCodigoArticulo());
					stock.setDigitoArticulo(vedmarDTO.getDigArticulo().trim());
					stock.setCodigoTipoVendedor(codTipVendedor);
					
					StockdifDTO stockDIF = stockdif.recuperaStockDiferenciado(stock);
					if (stockDIF!=null){
						if (stockDIF.getStockLinea()>vedmarDTO.getCantidadArticulo()){
							stock.setStockLinea(stockDIF.getStockLinea()-vedmarDTO.getCantidadArticulo());
						}else{
							stock.setStockLinea(0);
						}
						stockdif.actualizarStockDiferenciado(stock);
					}
					VecmarDTO vecmarDTO = vedmarDAO.recuperaTotales(carguioDTO.getCodigoEmpresa(), 21, vedmarDTO.getFechaMvto(), numeroDoc);
					vecmarDAO.actualizaVecmarMerma(carguioDTO.getCodigoEmpresa(), 21, vedmarDTO.getFechaMvto(), numeroDoc, vecmarDTO.getTotalNeto(), vecmarDTO.getTotalDocumento());
				} else if (dto.getCantidadDiferencia() == 0 && dto.getCantidadConfirmada()==0)
				{ 
					 //Genera VECMAR
					VedmarDTO vedmarDTO = new VedmarDTO();
					ExmarbDTO exmarbDTO = new ExmarbDTO();
					
					//Genera VEDMAR
					ExmarbDTO exmarb = exmarbDAO.recuperaArticulo(carguioDTO.getCodigoBodega(),dto.getCodigoArticulo() );
					vedmarDTO.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
					vedmarDTO.setCodTipoMvto(21);
					vedmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vedmarDTO.setNumDocumento(numeroDoc);
					int correlativo = vedmarDAO.obtenerCorrelativo(carguioDTO.getCodigoEmpresa(), 21, Integer.parseInt(fch.getYYYYMMDD()), numeroDoc);
					vedmarDTO.setCorrelativo(correlativo);
					vedmarDTO.setCodigoBodega(carguioDTO.getCodigoBodega());
					vedmarDTO.setCodigoArticulo(dto.getCodigoArticulo());
					vedmarDTO.setDigArticulo(exmarb.getDvArticulo());
					vedmarDTO.setFormato("U");
					vedmarDTO.setVolumenArticulo(0);
					vedmarDTO.setPesoLinea(0);
					
					//vedmarDTO.setCantidadArticulo(dto.getCantidad());
					//vedmarDTO.setCantidadFormato(dto.getCantidad());
					vedmarDTO.setCantidadArticulo(dto.getCantidad());
					vedmarDTO.setCantidadFormato(dto.getCantidad());
					
					vedmarDTO.setSectorBodega(exmarb.getCodigoSector());
					vedmarDTO.setPrecioUnidad(dto.getPrecioBruto());
					vedmarDTO.setPrecioNeto(dto.getPrecioNeto());
					vedmarDTO.setCostoNeto(exmarb.getCostoNeto());
					vedmarDTO.setVolumenArticulo(0);
					vedmarDTO.setMontoFlete(0);
					
					//vedmarDTO.setCostoTotalNeto(exmarb.getCostoNeto()*dto.getCantidad());
					vedmarDTO.setCostoTotalNeto(exmarb.getCostoNeto()*dto.getCantidad());
					
					vedmarDTO.setMontoBrutoLinea(vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoNeto(vedmarDTO.getPrecioNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setPorcentajeDesto(0);
					vedmarDTO.setMontoDescuentoLinea(0);
					vedmarDTO.setMontoDescuentoNeto(0);
					vedmarDTO.setMontoTotalLinea((int)vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					//int totalNeto = (int) vedmarDTO.getMontoNeto();
					double totalNeto =  vedmarDTO.getPrecioNeto()*(int)vedmarDTO.getCantidadArticulo();
					logi.info("Neto Linea :"+totalNeto);
					int tneto = (int)Math.round(totalNeto);
					vedmarDTO.setMontoTotalNetoLinea((int)tneto);
					//vedmarDTO.setMontoTotalNetoLinea((int)vedmarDTO.getPrecioNeto()*vedmarDTO.getCantidadArticulo());
					//Verificar EXENTO
					vedmarDTO.setMontoExento(0);
					vedmarDTO.setCodIngresoSalida("S");
					vedmarDTO.setSwitchProceso(0);
					vedmarDAO.generaMovimiento(vedmarDTO);
					//Actualiza STOCK
					int stockLineaArt=exmarb.getStockLinea()-vedmarDTO.getCantidadArticulo();
					if (stockLineaArt<0){
						//mailNegativo.envioMail("Procesa ASN LINEA 396 :"+"NOMBRE ARCHIVO :"+ nombreArchivo + vedmarDTO.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarb.getStockLinea() + "STOCK A MOVER : "+ vedmarDTO.getCantidadArticulo()+" STOCK AHORA : "+stockLineaArt);
					}
					
					exmarbDAO.actualizaStockLinea(vedmarDTO.getCodigoBodega(), vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo().trim(), exmarb.getStockLinea()-vedmarDTO.getCantidadArticulo());
					
					StockdifDTO stock = new StockdifDTO();
					stock.setCodigoEmpresa(vedmarDTO.getCodigoEmpresa());
					stock.setCodigoBodega(vedmarDTO.getCodigoBodega());
					stock.setCodigoArticulo(vedmarDTO.getCodigoArticulo());
					stock.setDigitoArticulo(vedmarDTO.getDigArticulo().trim());
					stock.setCodigoTipoVendedor(codTipVendedor);
					
					StockdifDTO stockDIF = stockdif.recuperaStockDiferenciado(stock);
					if (stockDIF!=null){
						if (stockDIF.getStockLinea()>vedmarDTO.getCantidadArticulo()){
							stock.setStockLinea(stockDIF.getStockLinea()-vedmarDTO.getCantidadArticulo());
						}else{
							stock.setStockLinea(0);
						}
						stockdif.actualizarStockDiferenciado(stock);
					}
					VecmarDTO vecmarDTO = vedmarDAO.recuperaTotales(carguioDTO.getCodigoEmpresa(), 21, vedmarDTO.getFechaMvto(), numeroDoc);
					vecmarDAO.actualizaVecmarMerma(carguioDTO.getCodigoEmpresa(), 21, vedmarDTO.getFechaMvto(), numeroDoc, vecmarDTO.getTotalNeto(), vecmarDTO.getTotalDocumento());
				} 
			}
			
			if (docGenerado==0){
				vecmar = vecmarDAO.obtenerDatosVecmarMer(carguioDTO.getCodigoEmpresa(), 21, docconf.getFechaConfirmacion(), numeroDoc);
				logi.info("RECUPERA VECMAR");
			}
			
			//Actualiza DATOS SE AGREGO HOY 20160701
			int cantidad = vedmarDAO.obtieneCantidadLineas(vecmar.getCodigoEmpresa(), vecmar.getCodTipoMvto(), vecmar.getFechaMvto(), vecmar.getNumDocumento());
			VecmarDTO dtoVecmar = vedmarDAO.recuperaTotales(vecmar.getCodigoEmpresa(), vecmar.getCodTipoMvto(), vecmar.getFechaMvto(), vecmar.getNumDocumento());
			VedmarDTO vedmarDTO = new VedmarDTO();
			vedmarDTO.setCodigoEmpresa(vecmar.getCodigoEmpresa());
			vedmarDTO.setCodTipoMvto(vecmar.getCodTipoMvto());
			vedmarDTO.setFechaMvto(vecmar.getFechaMvto());
			vedmarDTO.setNumDocumento(vecmar.getNumDocumento());
			vedmarDTO.setFechaGuiaDespacho(Integer.parseInt(fch.getYYYYMMDD()));
			//ordenDTO.getVecmar().setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
			vecmar.setNumeroTipoDocumento(vecmar.getNumDocumento());
			vecmar.setSwichProceso(0);
			vecmar.setIndicadorDespacho("S");
			vecmar.setSwitchPagoCaja("P");
			vecmar.setFechaDocumento(Integer.parseInt(fch.getYYYYMMDD()));
			vecmar.setFechaDespacho(Integer.parseInt(fch.getYYYYMMDD()));
			vecmar.setCantidadLineaDetalle(cantidad);
			vecmar.setTotalBruto(dtoVecmar.getTotalDocumento());
			vecmar.setTotalNeto(dtoVecmar.getTotalNeto());
			vecmar.setTotalDocumento(dtoVecmar.getTotalDocumento());
			if (vecmar.getCodigoDocumento()==3){
				vecmar.setCodigoDocumento(33);
			}else if (vecmar.getCodigoDocumento()==4){
				vecmar.setCodigoDocumento(34);
			}
			vecmarDAO.actualizaDatosVecmar(vecmar);
			
			//vedmarDAO.actualizaFecha(vedmarDTO);
			//Actualiza DATOS SE AGREGO HOY 20160701
			
			ClmcliDTO clmcliDTO = clmcli.recuperaCliente(rutCobro, dvCobro.trim());
			
			if (clmcliDTO!=null)
			{	
				vecmar.setRazonSocialCliente(clmcliDTO.getRazonsocial().trim());
				vecmar.setCodigoEmpresa(2);
				String sub = formaString(vecmar);
				logi.info("PROCESA FACTURACION:"+sub);
				
				proce.procesaCalculoProcedure(sub);
				
				//proce.procesaFacturacion(String.valueOf(carguioDTO.getCodigoEmpresa()), String.valueOf(vecmar.getCodTipoMvto()),String.valueOf(vecmar.getFechaMvto()), String.valueOf(vecmar.getNumDocumento()), String.valueOf(vecmar.getCodigoDocumento()), String.valueOf(vecmar.getRutProveedor()), vecmar.getDvProveedor().trim(), "AMS", "1", "0");
				
				///PROCESA FACTURACION SERVLET
				StringBuffer tmp = new StringBuffer(); 
				String texto = new String();
				try { 
					// Crea la URL con del sitio introducido, ej: http://google.com 
					URL url = new URL(rutservDTO.getEndPoint()+"?empresa="+String.valueOf(carguioDTO.getCodigoEmpresa())+
	            		"&codTipo="+String.valueOf(vecmar.getCodTipoMvto())+
	            				"&fch="+String.valueOf(vecmar.getFechaMvto())+"&num="+
	            		String.valueOf(vecmar.getNumDocumento())+
	            				"&cod="+String.valueOf(vecmar.getCodigoDocumento())+"&rut="+
	            	String.valueOf(vecmar.getRutProveedor())+"&dv="+vecmar.getDvProveedor().trim()+"&usuario=CAJABD26&tipo=1&nota=0"); 
					logi.info("URL SERVLET FACTURACION:"+url.toString());
					// Lector para la respuesta del servidor 
					BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
					String str; 
					while ((str = in.readLine()) != null) 
					{ 
						tmp.append(str); 
					} 
					//url.openStream().close();
					in.close(); 
					texto = tmp.toString(); 
	        	}catch (MalformedURLException e) { 
	        		texto = "<h2>No esta correcta la URL</h2>".toString(); 
	        	} catch (IOException e) { 
	        			texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
	        	}
				vecmarDAO.actualizaDatosVecmar(vecmar);

				vecmarDAO.actualizaDisponibilidadImpresion(carguioDTO.getCodigoEmpresa(), vecmar.getCodTipoMvto(), vecmar.getFechaMvto(), vecmar.getNumDocumento(), andenRecep);
			}
			else{
				logi.info("TRANSPORTISTA NO ENCONTRADO EN MAESTRO DE CLIENTES RUT :"+rutCobro+"-"+dvCobro.trim());
				logi.info("DOCUMENTO DE COBRO NO GENEREADO QUEDO CON REINTENTAR");
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
	}
	
	public void procesaASN(ConfirmacionCarguioDetalleDTO dto, int carguio, String nombreArchivo){
		DAOFactory dao = DAOFactory.getInstance();
		CarguioDAO carguioDAO = dao.getCarguioDAO();
		
		ConsolidaasnDAO consolida = dao.getConsolidaasnDAO();
		CargfwmsDAO carfwmsDAO = dao.getCargfwmsDAO();
		ChoftranDAO choftranDAO = dao.getChoftranDAO();
		Fecha fch = new Fecha();
		try{
			ConsolidaasnDTO consolidaDTO = consolida.recuperaConsolidado(2, carguio, 26, dto.getCodArticulo());
			CarguioDTO carguioDTO = carguioDAO.obtieneCarguioDTO(2, consolidaDTO.getNumeroCarguio(), consolidaDTO.getBodega());
			
			//if (consolidaDTO.getCantidad()!=dto.getCantidad()){
			if (consolidaDTO!=null){
				logi.info("PROCESA ACTUALIZACION CONSOLIDADO");
				consolidaDTO.setCantidadConfirmada(dto.getCantidad());
				consolidaDTO.setCantidadDiferencia(consolidaDTO.getCantidad()-dto.getCantidad());
				consolida.actualizaConsolidado(consolidaDTO);
				//Procesa Facturacion a Chofer
				CargfwmsDTO cargfwmsDTO = new CargfwmsDTO();
				cargfwmsDTO.setCodigoEmpresa(consolidaDTO.getCodigoEmpresa());
				cargfwmsDTO.setPatente(consolidaDTO.getPatente());
				cargfwmsDTO.setNumeroCarguio(consolidaDTO.getNumeroCarguio());
				cargfwmsDTO.setCodigoBodega(consolidaDTO.getBodega());
				cargfwmsDTO.setNombreArchivoXML(nombreArchivo);
				cargfwmsDTO.setTipo("S");
				cargfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
				cargfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
				carfwmsDAO.generaArchivoXML(cargfwmsDTO);
				
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
		
	}
	
	public String formaString(VecmarDTO vecmar){
		//                  
		//String facturacion="VVEMCVM00 00337  2120160503  16275017  33         020160503         0  26   01 14    40295  000        0        0        0        0    40295      000      000    693975811083   010S                                                                             0N      FLOR  MARIA  PEREZ SALINAS                                              0  0  02070002                                                                                               ";
		//String facturacion="VVEMCVM00 00337  2120160503  16274301  33         020160503         0  26   01  0        0    0        0        0        0        0        0        0        0    557733431110   001                                                                              0N                                                      0        0   0        0 0  0  02500  2"
		String facturacion="";
		String nombre ="VVEMCVM00 00";
		Fecha fch = new Fecha();
		String paso = agregaBlanco(String.valueOf(vecmar.getCodTipoMvto()).length()-4,String.valueOf(vecmar.getCodTipoMvto()));
		facturacion = paso;
		paso = agregaBlanco(String.valueOf(vecmar.getFechaMvto()).length()-8,String.valueOf(vecmar.getFechaMvto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getNumDocumento()).length()-10,String.valueOf(vecmar.getNumDocumento()));
		facturacion = facturacion+paso;
		if (vecmar.getCodigoDocumento()==3){
			vecmar.setCodigoDocumento(33);
		}else if (vecmar.getCodigoDocumento()==4){
			vecmar.setCodigoDocumento(34);

		}
		paso = agregaBlanco(String.valueOf(vecmar.getCodigoDocumento()).length()-4,String.valueOf(vecmar.getCodigoDocumento()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getNumeroTipoDocumento()).length()-10,String.valueOf(vecmar.getNumeroTipoDocumento()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getFechaMvto()).length()-8,String.valueOf(vecmar.getFechaMvto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getNumeroOrdenCompra()).length()-10,String.valueOf(vecmar.getNumeroOrdenCompra()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getBodegaOrigen()).length()-4,String.valueOf(vecmar.getBodegaOrigen()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getBodegaDestino()).length()-4,String.valueOf(vecmar.getBodegaDestino()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getFormaPago()).length()-1,String.valueOf(vecmar.getFormaPago()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getCantidadLineaDetalle()).length()-3,String.valueOf(vecmar.getCantidadLineaDetalle()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getTotalBruto()).length()-9,String.valueOf(vecmar.getTotalBruto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getPorcentajeDescuento()).length()-5,String.valueOf(vecmar.getPorcentajeDescuento()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getTotalDescuento()).length()-9,String.valueOf(vecmar.getTotalDescuento()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getTotalNeto()).length()-9,String.valueOf(vecmar.getTotalNeto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getTotalImptoAdicional()).length()-9,String.valueOf(vecmar.getTotalImptoAdicional()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getTotalIva()).length()-9,String.valueOf(vecmar.getTotalIva()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getTotalDocumento()).length()-9,String.valueOf(vecmar.getTotalDocumento()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getPesoTotalMovto()).length()-9,String.valueOf(vecmar.getPesoTotalMovto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getVolumenTotalMovto()).length()-9,String.valueOf(vecmar.getVolumenTotalMovto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getRutProveedor()).length()-11,String.valueOf(vecmar.getRutProveedor()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getDvProveedor()).length()-1,String.valueOf(vecmar.getDvProveedor()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getCodigoVendedor()).length()-4,String.valueOf(vecmar.getCodigoVendedor()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getCodigoJefeLocal()).length()-4,String.valueOf(vecmar.getCodigoJefeLocal()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getSwitchDescto()).length()-1,String.valueOf(vecmar.getSwitchDescto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getSwichProceso()).length()-1,String.valueOf(vecmar.getSwichProceso()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getIndicadorDespacho()).length()-1,String.valueOf(vecmar.getIndicadorDespacho()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("").length()-40,String.valueOf(""));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("").length()-30,String.valueOf(""));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getFechaDespacho()).length()-8,String.valueOf(vecmar.getFechaDespacho()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getSwitchPagoCaja()).length()-1,String.valueOf(vecmar.getSwitchPagoCaja()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(fch.getHHMMSS().length()-6,fch.getHHMMSS());
		facturacion = facturacion+paso;
		paso = agregaBlancoRazon(String.valueOf(vecmar.getRazonSocialCliente()).length()-40,String.valueOf(vecmar.getRazonSocialCliente()));//nombre del cliente
		facturacion = facturacion+paso;
		
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));//monto pie
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));//monto interes
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));//cantidad cheques
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));//monto cheques
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-2,String.valueOf("0"));//codigo region
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-3,String.valueOf("0"));//ciudad
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-3,String.valueOf("0"));//comuna
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getCodigoTipoVendedor()).length()-4,String.valueOf(vecmar.getCodigoTipoVendedor()));//monto pie
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getCodigoEmpresa()).length()-3,String.valueOf(vecmar.getCodigoEmpresa()));//codigo empresa
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		return facturacion;
	}
	
	public String formaStringVedmar(VedmarDTO vedmar, VecmarDTO vecmar){
		String facturacion="";
		String nombre ="GVEMDVM00 00";
		Fecha fch = new Fecha();
		DecimalFormat formateadorVedmar = new DecimalFormat("###,###.00");

		String paso = agregaBlanco(String.valueOf(vedmar.getCodTipoMvto()).length()-4,String.valueOf(vedmar.getCodTipoMvto()));
		facturacion = paso;
		paso = agregaBlanco(String.valueOf(vedmar.getFechaMvto()).length()-8,String.valueOf(vedmar.getFechaMvto()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getNumDocumento()).length()-10,String.valueOf(vedmar.getNumDocumento()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getCorrelativo()).length()-2,String.valueOf(vedmar.getCorrelativo()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getCodigoBodega()).length()-4,String.valueOf(vedmar.getCodigoBodega()));
		facturacion = facturacion+paso;
		
		paso = agregaBlanco(String.valueOf(vedmar.getCodigoArticulo()).length()-7,String.valueOf(vedmar.getCodigoArticulo()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getDigArticulo()).length()-1,String.valueOf(vedmar.getDigArticulo()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getFormato()).length()-1,String.valueOf(vedmar.getFormato()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getCantidadArticulo()).length()-7,String.valueOf(vedmar.getCantidadArticulo()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getCantidadArticulo()).length()-7,String.valueOf(vedmar.getCantidadArticulo()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getSectorBodega()).length()-3,String.valueOf(vedmar.getSectorBodega()));
		facturacion = facturacion+paso;
		
		double dprecioBruto=vedmar.getPesoLinea();
		String precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getVolumenArticulo();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getPrecioUnidad();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getMontoBrutoLinea();
		precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-11,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getPorcentajeDesto();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-5,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getMontoDescuentoLinea();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-11,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getMontoFlete();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		
		paso = agregaBlanco(String.valueOf(vedmar.getMontoTotalLinea()).length()-9,String.valueOf(vedmar.getMontoTotalLinea()));
		facturacion = facturacion+paso;
		
		paso = agregaBlanco(String.valueOf(vedmar.getCodIngresoSalida()).length()-1,String.valueOf(vedmar.getCodIngresoSalida()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getSwitchProceso()).length()-1,String.valueOf(vedmar.getSwitchProceso()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getFechaGuiaDespacho()).length()-8,String.valueOf(vedmar.getFechaGuiaDespacho()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("0").length()-10,String.valueOf("0"));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf("S").length()-1,String.valueOf("S"));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getFormaPago()).length()-1,String.valueOf(vecmar.getFormaPago()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getRutProveedor()).length()-11,String.valueOf(vecmar.getRutProveedor()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getDvProveedor()).length()-1,String.valueOf(vecmar.getDvProveedor()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vecmar.getCodigoTipoVendedor()).length()-4,String.valueOf(vecmar.getCodigoTipoVendedor()));
		facturacion = facturacion+paso;
		formateadorVedmar = new DecimalFormat("###,###.0000");
		dprecioBruto=vedmar.getPrecioNeto();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-13,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		formateadorVedmar = new DecimalFormat("###,###.00");

		dprecioBruto=vedmar.getCostoNeto();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-11,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getCostoTotalNeto();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-11,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getMontoNeto();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-11,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getMontoDescuentoNeto();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-11,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		dprecioBruto=vedmar.getMontoTotalNetoLinea();
		 precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		
		paso = agregaBlanco(String.valueOf("2").length()-3,String.valueOf("2"));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		return facturacion;
	}
	
	
	public String agregaBlanco(int cant , String string){
		if (cant<0){
			cant=cant * (0-1);
		}
		String palabra=string;
		int contador=0;
		while (cant>contador){
			palabra = " "+palabra;
			contador++;
		}
		return palabra;
	}
	public String agregaBlancoRazon(int cant , String string){
		if (cant<0){
			cant=cant * (0-1);
		}
		String palabra=string;
		int contador=0;
		while (cant>contador){
			palabra = palabra+" ";
			contador++;
		}
		return palabra;
	}
	public static void main (String []args){
		IntegracionConfirmacionASNHelper helper = new IntegracionConfirmacionASNHelper();
		helper.procesaReconciliacion("/home2/ftp/out/RCV_TRLR_CLO_1106320v2.xml", "RCV_TRLR_CLO_1106320v2.xml", "CIERRE");
		/*DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmar = dao.getVedmarDAO();
		int numero = vedmar.obtenerCorrelativo(2, 21, 20160920, 18000598);
		System.out.println("Correlativo:"+numero);*/
		/*double precioNeto=805.8824;
		int cantidad = 170;
		double neto =precioNeto*cantidad;
		int total = (int)neto;*/
		/*DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmar = dao.getVedmarDAO();
		int numero= vedmar.verificaVenta(2, 21, 20170210, 18910144);
		int dos=0;*/
		
	}
}
