package cl.caserita.wms.out.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExdodcDAO;
import cl.caserita.dao.iface.ExdrecDAO;
import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmodcDAO;
import cl.caserita.dao.iface.ExmrecDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.ExtariDAO;
import cl.caserita.dao.iface.ImpauditDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.StockdifDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.TptimpDAO;
import cl.caserita.dao.iface.VarcostDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.Conar1DAO;
import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dao.iface.CotplcDAO;
import cl.caserita.dao.iface.DocncpDAO;
import cl.caserita.dao.iface.ExdartDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmcreDAO;
import cl.caserita.dao.iface.ExmfwmsDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TiptoleDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dao.iface.ConvafDAO;
import cl.caserita.dao.iface.ExdfcprDAO;
import cl.caserita.dao.iface.ChoftranDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.ChoftranDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.Conar1DTO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.ExdrecDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.ExmrecDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.ImpauditDTO;
import cl.caserita.dto.StockdifDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.ConsolidaasnDTO;
import cl.caserita.dto.ConvafDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmcreDTO;
import cl.caserita.dto.ExmfwmsDTO;
import cl.caserita.dto.StockinventarioDTO;
import cl.caserita.dto.TiptoleDTO;
import cl.caserita.dto.TptimpDTO;
import cl.caserita.dto.VarcostDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.emailInformacionErroresWMS;
import cl.caserita.enviomail.main.enviaMailStockNegativo;
import cl.caserita.dto.CotplcDTO;
import cl.caserita.dto.ExdfcprDTO;
import cl.caserita.dto.DocncpDTO;
import cl.caserita.dto.RutservDTO;

public class IntegracionConfirmaRecepcionHelper {
	private static Logger logi = Logger.getLogger(IntegracionConfirmaRecepcionHelper.class);
	private static emailInformacionErroresWMS email = new emailInformacionErroresWMS();
	private static enviaMailStockNegativo mailNegativo = new enviaMailStockNegativo();

	private Connection conn;
	
	public void procesaReconciliacion(String urlFile, String nameFile){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		DAOFactory dao = DAOFactory.getInstance();
		ExmrecDAO exmrec = dao.getExmrecDAO();
		ExdrecDAO exdrec = dao.getExdrecDAO();
		ExmodcDAO exmodc = dao.getExmodcDAO();
		ExdodcDAO exdodc = dao.getExdodcDAO();
		TpacorDAO tpacor = dao.getTpacorDAO();
		ConarcDAO conarc = dao.getConarcDAO();
		ExmarbDAO exmarb = dao.getExmarbDAO();
		ExdartDAO exdart = dao.getExdartDAO();
		ExmartDAO exmart = dao.getExmartDAO();
		StockinventarioDAO stockinventario = dao.getStockinventarioDAO();
		VecmarDAO vecmar = dao.getVecmarDAO();
		ExmfwmsDAO exmfwms = dao.getExmfwmsDAO();
		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		CotplcDAO cotplc = dao.getCotplcDAO();
		ConvafDAO convaf = dao.getConvafDAO();
		ExdfcprDAO exdfcpr = dao.getExdfcprDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		TptimpDAO tptimp = dao.getTptimpDAO();
		TiptoleDAO tiptole = dao.getTiptoleDAO();
		ExmcreDAO exmcre = dao.getExmcreDAO();
		ExdtraDAO exdtra = dao.getExdtraDAO();
		ExmtraDAO exmtra = dao.getExmtraDAO();
		ImpauditDAO impaudit = dao.getImpauditDAO();
		VarcostDAO varcost = dao.getVarcostDAO();
		ExtariDAO extari = dao.getExtariDAO();
		ExmrecDTO exmrecDTO = null;
		ExdrecDTO exdrecDTO = null;
		
		Fecha fch = new Fecha();
		int ins=0;
		int linea=1;
		int recibido=0;
		int pedido=0;
		String estadoCabeceraOC="";
		String estadoDetalleOC="";
		int correlativoOC=0;
		int fechaMvto=0;
		
		int lineaArt=0;
		int ibuscaDocto=0;
		int codarticulo=0;
		int nroOc=0;
		int stockRecep=0;
		int stockPedido=0;
		int nroDocto=0;
		int rutprov=0;
		String dvrutprov="";
		String rutprovsel="";
		String dvprovsel="";
		String fechaVctoArt="";
		double PesoRecep=0;
		double VoluRecep=0;
		double CantidUnidOC=0;
		int iActualizaOC=0;
		double stockLineaArt=0;
		
		int nroFolio=0;
		int nuevoFolio=0;
		int anoPeriodo=0;
		int mesPeriodo=0;
		int anoDocumento=0;
		int mesDocumento=0;
		int anoPeriodoAnt=0;
		int mesPeriodoAnt=0;
		int fechaSYSFolio=0;
		double stockPedidoOC=0;
		double stockPendienteOC=0;
		double stockRecepcionadoOC=0;
		double dMontoBrutoLinea=0;
		double dTotalBrutoLinea=0;
		String sIdCamion="";
		String sTipCierre="";
		String sTipoRecep="";
		String sEstadoInventario="";
		int iTipDocto=0;
		int iProcesa=0;
		String estadoInventario="D";
		try{
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse(fXmlFile);
			
			
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			
			
			AjusteDTO ajuste = null;
			doc.getDocumentElement().normalize();
			
			logi.info("N O M B R E  A R C H I V O  R E C E P C I O N :" + nameFile);
			
			//Lista XML confirmacion de Recepcion
			NodeList nList = doc.getElementsByTagName("INVENTORY_RECEIPT_IFD_SEG");
			
			logi.info("Largo del XML"+nList.getLength());
			
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				ExmrecDTO exmrecDTO2 = new ExmrecDTO();
				ExmrecDTO exmrecDTO5 = new ExmrecDTO();
				ExdrecDTO exdrecDTO2 = new ExdrecDTO();
				StockinventarioDTO stockinventarioDTO = new StockinventarioDTO();
				VecmarDTO vecmarDTO = new VecmarDTO();
				VedmarDTO vedmarDTO = new VedmarDTO();
				CotplcDTO cotplcDTO = new CotplcDTO();
				ConvafDTO convafDTO = new ConvafDTO();
				ExdfcprDTO exdfcprDTO = new ExdfcprDTO();
				ExmfwmsDTO exmfwmsDTO = new ExmfwmsDTO();
				Node nNode = nList.item(temp);
				
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					logi.info("ASNFLG : " + eElement.getElementsByTagName("ASNFLG").item(0).getTextContent());
					
					logi.info("CATCH_QTY : " + eElement.getElementsByTagName("CATCH_QTY").item(0).getTextContent());
					logi.info("EXPQTY : " + eElement.getElementsByTagName("EXPQTY").item(0).getTextContent());
					
					logi.info("HSTACC : " + eElement.getElementsByTagName("HSTACC").item(0).getTextContent());
					
					logi.info("INVLIN : " + eElement.getElementsByTagName("INVLIN").item(0).getTextContent());
					logi.info("INVNUM : " + eElement.getElementsByTagName("INVNUM").item(0).getTextContent());
					logi.info("INVSLN : " + eElement.getElementsByTagName("INVSLN").item(0).getTextContent());
					
					logi.info("INVTYP : " + eElement.getElementsByTagName("INVTYP").item(0).getTextContent());
					logi.info("POLIN : " + eElement.getElementsByTagName("POLIN").item(0).getTextContent());
					logi.info("PONUM : " + eElement.getElementsByTagName("PONUM").item(0).getTextContent());
					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("RCVQTY : " + eElement.getElementsByTagName("RCVQTY").item(0).getTextContent());
					logi.info("RCVSTS : " + eElement.getElementsByTagName("RCVSTS").item(0).getTextContent());
					logi.info("RIMFLG : " + eElement.getElementsByTagName("RIMFLG").item(0).getTextContent());
					logi.info("SADNUM : " + eElement.getElementsByTagName("SADNUM").item(0).getTextContent());
					logi.info("WAYBILL : " + eElement.getElementsByTagName("WAYBILL").item(0).getTextContent());
					
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("SUPNUM : " + eElement.getElementsByTagName("SUPNUM").item(0).getTextContent());
					logi.info("TRKNUM : " + eElement.getElementsByTagName("TRKNUM").item(0).getTextContent());
					logi.info("TRNDTE : " + eElement.getElementsByTagName("TRNDTE").item(0).getTextContent());
					logi.info("TRNNUM : " + eElement.getElementsByTagName("TRNNUM").item(0).getTextContent());
					logi.info("WAYBILL : " + eElement.getElementsByTagName("WAYBILL").item(0).getTextContent());
					logi.info("WH_ID : " + eElement.getElementsByTagName("WH_ID").item(0).getTextContent());
					logi.info("TRLR_NUM : " + eElement.getElementsByTagName("TRLR_NUM").item(0).getTextContent());
					logi.info("EXPIRE_DTE : " + eElement.getElementsByTagName("EXPIRE_DTE").item(0).getTextContent());
					estadoInventario = eElement.getElementsByTagName("RCVSTS").item(0).getTextContent().trim();
					if (eElement.getElementsByTagName("SADNUM").item(0).getTextContent().trim()!=""){
						nroDocto=Integer.parseInt(eElement.getElementsByTagName("SADNUM").item(0).getTextContent());
						iTipDocto=1;
					}
					else{
						
						if (eElement.getElementsByTagName("WAYBILL").item(0).getTextContent().trim()!=""){
							nroDocto=Integer.parseInt(eElement.getElementsByTagName("WAYBILL").item(0).getTextContent().trim());
							iTipDocto=2;
						}
					}
					
					if (nroDocto == 0){
						logi.info("XML no procesado falta numero de documento FACTURA o GUIA");
					}
					else{
						
						sTipoRecep=eElement.getElementsByTagName("INVTYP").item(0).getTextContent();
						
						codarticulo=Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
						nroOc=Integer.parseInt(eElement.getElementsByTagName("PONUM").item(0).getTextContent());
						stockRecep=Integer.parseInt(eElement.getElementsByTagName("RCVQTY").item(0).getTextContent());
						stockPedido=Integer.parseInt(eElement.getElementsByTagName("EXPQTY").item(0).getTextContent());
						lineaArt=Integer.parseInt(eElement.getElementsByTagName("INVLIN").item(0).getTextContent());
						
						rutprovsel=eElement.getElementsByTagName("SUPNUM").item(0).getTextContent();
						rutprov=Integer.parseInt(rutprovsel.substring(0, 8));
						dvprovsel=rutprovsel.substring(8);
						dvrutprov=dvprovsel;
						
						//fechaVctoArt=eElement.getElementsByTagName("TRNDTE").item(0).getTextContent();
						fechaVctoArt=eElement.getElementsByTagName("EXPIRE_DTE").item(0).getTextContent();
						fechaVctoArt=fechaVctoArt.substring(0, 8);
						
						sIdCamion=eElement.getElementsByTagName("TRLR_NUM").item(0).getTextContent();
						sEstadoInventario=eElement.getElementsByTagName("RCVSTS").item(0).getTextContent();
						
						if ("PROV".equals(sTipoRecep.trim())){
							//Recepcion de OC Proveedor
							
							//Busca los articulos de la OC en EXDODC
							HashMap hash = exdodc.HashMapExdodc(2, nroOc);
							ExdodcDTO exdodcDTO = (ExdodcDTO) hash.get(codarticulo);
							
							//busco datos de Pallet, Display, Caja del articulo					
							ExmartDTO exmartDTO = exmart.recuperaArticulo(exdodcDTO.getCodArticulo(), exdodcDTO.getDvArticulo());
							
							//Transforma a unidades la cantidad del articulo de la OC si esta es distinta a Unidad
							if ("C".equals(exdodcDTO.getFormatoArt().trim())){
								
								if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
								};
								
								if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * exmartDTO.getCaja();
								};
								
								if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * exmartDTO.getDisplay();
								};
								iActualizaOC=1;
							}
							
							if ("D".equals(exdodcDTO.getFormatoArt().trim())){
								if (exmartDTO.getDisplay()>0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * exmartDTO.getDisplay();
								}
								iActualizaOC=1;
							}
							
							if ("P".equals(exdodcDTO.getFormatoArt().trim())){
								
								if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()>0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * (exmartDTO.getPallet() * exmartDTO.getCaja() * exmartDTO.getDisplay());
								}
								
								if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
								};
								
								if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()==0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * exmartDTO.getCaja();
								};
								
								if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * exmartDTO.getDisplay();
								};
								
								if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()>0){
									CantidUnidOC=exdodcDTO.getStockPedidoOC() * (exmartDTO.getPallet() * exmartDTO.getCaja());
								};
								
								iActualizaOC=1;
							}
							
							if ("U".equals(exdodcDTO.getFormatoArt().trim())){
								CantidUnidOC=exdodcDTO.getStockPedidoOC();
								logi.info("Procesa Cantidad Exdodc:"+exdodcDTO.getStockPedidoOC());
								iActualizaOC=0;
							}
							
							//Si la OC no esta por unidad se actualiza para dejar igual que la recepcion de WMS
							if (iActualizaOC==1){
								exdodc.actualizarFormatoArticulo(2, nroOc, lineaArt, exdodcDTO.getCodArticulo(), CantidUnidOC);
								logi.info("ACTUALIZA FORMATO DEL ARTICULO A UNIDAD");
							}
							
							exmrecDTO2.setCodEmpresa(2);
							exmrecDTO2.setNumeroOrden(nroOc);
							exmrecDTO2.setRutProveedor(rutprov);
							exmrecDTO2.setDvProveedor(dvrutprov);
							exmrecDTO2.setNumeroDocumento(nroDocto);
							
							if (exmrec.buscaEncabezado(exmrecDTO2)==null){
								ExmodcDTO exmodcDTO = exmodc.buscaCabOrden(2, exmrecDTO2.getNumeroOrden());
								
								//Inserta Encabezado y Luego detalle de confirmacion de recepcion
								exmrecDTO2.setFechaConfirmacionRecepcion(Integer.parseInt(fch.getYYYYMMDD()));
								exmrecDTO2.setHoraConfirmacionRecepcion(Integer.parseInt(fch.getHHMMSS()));
								exmrecDTO2.setCodigoBodega(26);
								exmrecDTO2.setNombreArchivoConfirmacion(nameFile);
								exmrecDTO2.setRutProveedor(exmodcDTO.getRutProveedor());
								exmrecDTO2.setDvProveedor(exmodcDTO.getDigito().trim());
								exmrecDTO2.setIdCamion(sIdCamion);
								
								//Si el numero de documento es factura se graba este numero en caso contrario
								//se toma el TAG de la guia de despacho
								exmrecDTO2.setNumeroDocumento(nroDocto);
								
								if (ins==0){
									//Inserta solo una vez
									exmrec.generaEncabezado(exmrecDTO2);
									logi.info("INSERTA CABECERA DEL DOCUMENTO PARA QUE SEA VALORIZADO EN SYSCON");
								}
								
								//Genera Detalle
								exdrecDTO2.setCodigoEmpresa(2);
								exdrecDTO2.setNumeroOrden(exmrecDTO2.getNumeroOrden());
								exdrecDTO2.setFechaConfirmacionRecepcion(exmrecDTO2.getFechaConfirmacionRecepcion());
								exdrecDTO2.setHoraConfirmacionRecepcion(exmrecDTO2.getHoraConfirmacionRecepcion());
								exdrecDTO2.setLinea(lineaArt);
								exdrecDTO2.setCodigoBodega(26);
								exdrecDTO2.setCodigoArticulo(exdodcDTO.getCodArticulo());
								exdrecDTO2.setDvArticulo(exdodcDTO.getDvArticulo().trim());
								exdrecDTO2.setDescripcionArticulo(exdodcDTO.getDescripcionArticulo());
								exdrecDTO2.setStockRecepcionado(stockRecep);
								exdrecDTO2.setStockSolicitado(CantidUnidOC);
								exdrecDTO2.setFechaVencimiento(Integer.parseInt(fechaVctoArt));
								exdrecDTO2.setEstadoInventario(sEstadoInventario);
								exdrecDTO2.setPrecioNeto(exdodcDTO.getPrecioNeto());
								exdrecDTO2.setPrecioBruto(exdodcDTO.getPrecioBruto());
								exdrecDTO2.setMontoNeto(exdodcDTO.getMontoNeto());
								exdrecDTO2.setMontoTotal(exdodcDTO.getMontoNeto());
								exdrecDTO2.setMontoBruto(stockRecep*exdodcDTO.getMontoBruto());
								exdrecDTO2.setMontoTotal(stockRecep*exdodcDTO.getMontoBruto());
								
								if (exdrec.buscaDetalle(2, nroOc, exmrecDTO2.getFechaConfirmacionRecepcion(), exmrecDTO2.getHoraConfirmacionRecepcion(), 26, codarticulo)!=null){
									exdrec.actualizaDetalle(exdrecDTO2);
									logi.info("ACTUALIZA ARTICULO DEL DOCUMENTO PARA QUE SEA RECEPCIONADO EN SYSCON");
								}
								else{
									exdrec.generaDetalle(exdrecDTO2);
									logi.info("INSERTA ARTICULO DEL DOCUMENTO PARA QUE SEA RECEPCIONADO EN SYSCON");
								}
								
								linea++;
								ins++;
								ibuscaDocto=1;
							}
							
							if (ibuscaDocto==0){
							
								//Si ya esta valorizado el documento en SYSCON genera el ingreso del movimiento 01
								
								//Busco datos del documento EXMREC
								exmrecDTO2.setCodEmpresa(2);
								exmrecDTO2.setNumeroOrden(nroOc);
								ExmrecDTO exmrecDTO3 = exmrec.buscaEncabezado(exmrecDTO2);
								
								//Busco datos de la cabecera de la OC
								ExmodcDTO exmodcDTO = exmodc.buscaCabOrden(exmrecDTO3.getCodEmpresa(), nroOc);
								
								//Busco datos del articulo en la bodega
								ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(exmodcDTO.getBodegaOrigen(), exdodcDTO.getCodArticulo()); 
								
								//Guardo Stock en linea del articulo
								stockLineaArt=exmarbDTO.getStockLinea();
								
								//Obtengo Peso y Volumen del articulo
								if (exdart.buscaCodBarraArt(exdodcDTO.getCodArticulo(), exdodcDTO.getDvArticulo(), "U")!=null){
									
									ExdartDTO exdartDTO = exdart.buscaCodBarraArt(exdodcDTO.getCodArticulo(), exdodcDTO.getDvArticulo(), "U");
									
									//Calcula Peso y Volumen
									PesoRecep=exdartDTO.getPeso()*stockRecep;
									VoluRecep=exdartDTO.getVolumen()*stockRecep;
								}
								
								//Actualizo el nombre del archivo que fue recepcionado en la OC y Factura
								exmrecDTO5.setCodEmpresa(exmrecDTO3.getCodEmpresa());
								exmrecDTO5.setNumeroOrden(exmrecDTO3.getNumeroOrden());
								exmrecDTO5.setFechaConfirmacionRecepcion(exmrecDTO3.getFechaConfirmacionRecepcion());
								exmrecDTO5.setHoraConfirmacionRecepcion(exmrecDTO3.getHoraConfirmacionRecepcion());
								exmrecDTO5.setCodigoBodega(exmrecDTO3.getCodigoBodega());
								exmrecDTO5.setRutProveedor(exmrecDTO3.getRutProveedor());
								exmrecDTO5.setDvProveedor(exmrecDTO3.getDvProveedor());
								exmrecDTO5.setNumeroDocumento(exmrecDTO3.getNumeroDocumento());
								exmrecDTO5.setNombreArchivoConfirmacion(nameFile);
								exmrecDTO5.setIdCamion(sIdCamion);
								
								exmrec.actualizaEncabezado(exmrecDTO5);
								
								exdrecDTO2.setCodigoEmpresa(exmrecDTO3.getCodEmpresa());
								exdrecDTO2.setNumeroOrden(exmrecDTO3.getNumeroOrden());
								exdrecDTO2.setFechaConfirmacionRecepcion(exmrecDTO3.getFechaConfirmacionRecepcion());
								exdrecDTO2.setHoraConfirmacionRecepcion(exmrecDTO3.getHoraConfirmacionRecepcion());
								exdrecDTO2.setLinea(lineaArt);
								exdrecDTO2.setCodigoBodega(exmrecDTO3.getCodigoBodega());
								exdrecDTO2.setCodigoArticulo(exdodcDTO.getCodArticulo());
								exdrecDTO2.setDvArticulo(exdodcDTO.getDvArticulo().trim());
								exdrecDTO2.setDescripcionArticulo(exdodcDTO.getDescripcionArticulo());
								exdrecDTO2.setStockRecepcionado(stockRecep);
								exdrecDTO2.setStockSolicitado(CantidUnidOC);
								exdrecDTO2.setFechaVencimiento(Integer.parseInt(fechaVctoArt));
								exdrecDTO2.setEstadoInventario(sEstadoInventario);
								exdrecDTO2.setPrecioNeto(exdodcDTO.getPrecioNeto());
								exdrecDTO2.setPrecioBruto(exdodcDTO.getPrecioBruto());
								exdrecDTO2.setMontoNeto(exdodcDTO.getMontoNeto());
								exdrecDTO2.setMontoTotal(exdodcDTO.getMontoNeto());
								exdrecDTO2.setMontoBruto(stockRecep*exdodcDTO.getPrecioBruto());
								exdrecDTO2.setMontoTotal(stockRecep*exdodcDTO.getPrecioBruto());
								
								dMontoBrutoLinea=stockRecep*exdodcDTO.getPrecioBruto();
								dTotalBrutoLinea=stockRecep*exdodcDTO.getPrecioBruto();
								
								//Valido si el articulo ya esta insertado en el detalle de la OC
								if (exdrec.buscaDetalle(exmrecDTO3.getCodEmpresa(), nroOc, exmrecDTO3.getFechaConfirmacionRecepcion(), exmrecDTO3.getHoraConfirmacionRecepcion(),exmodcDTO.getBodegaOrigen(), codarticulo)!=null){
									//Actualiza la cantidad recepcionada del articulo
									exdrec.actualizaDetalle(exdrecDTO2);
								}
								else{
									//Inserta el detalle del articulo recepcionado en EXDREC
									exdrec.generaDetalle(exdrecDTO2);
								}
								
								//Genera Movimiento de XML Procesado
								exmfwmsDTO.setCodigoEmpresa(exmodcDTO.getCodEmpresa());
								exmfwmsDTO.setNumeroOrdenCompra(exmodcDTO.getNumeroOrden());
								exmfwmsDTO.setNombreArchivoXML(nameFile);
								exmfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
								exmfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
								exmfwms.generaArchivoXML(exmfwmsDTO);
								
								int lfechaDoctoCONARC=0;
								int lCodDoctoCONARC=0;
								int lNumDoctoCONARC=0;
								
								//Busco los datos del documento en el libro de compras solo para las facturas
								if (iTipDocto==1)
								{
									//Es Factura
									
									//Busco Totales del documento Valorizado
									ConarcDTO conarcDTO = conarc.buscaTotalesDocumento(exmrecDTO3.getCodEmpresa(), rutprov, dvrutprov, nroDocto);
									
									if (conarcDTO!=null)
									{
										logi.info("DOCUMENTO VALORIZADO SE PROCESA EL XML DE RECEPCION");
										iProcesa=1;
										lfechaDoctoCONARC=conarcDTO.getFechaDocumento();
										lCodDoctoCONARC=conarcDTO.getCodDocumento();
										lNumDoctoCONARC=conarcDTO.getNumeroDocumento();
										
									}
									else
									{
										logi.info("DOCUMENTO NO VALORIZADO O NO ENCONTRADO EN CONARC");
										iProcesa=0;
									}
								}
								else if (iTipDocto==2)
								{
									//Es Guia de despacho
									logi.info("DOCUMENTO NO VALORIZADO PORQUE SE ESTA RECEPCIONANDO POR GUIA DE DESPACHO");
									iProcesa=1;
								}
								
								if (iProcesa==1){
									
									//Busco datos del documento en la Interfaz
									ExdfcprDTO exdfcprDTO2 = exdfcpr.buscaDocumentoExdfcr(exmrecDTO3.getCodEmpresa(), nroOc, nroDocto, rutprov, dvrutprov);
									if (exdfcprDTO2.getNumeroOrden().trim().equals(nroOc)){
										logi.info("Numero de Orden iguales");
									}else{
										logi.info("Modifica Orden de Compra por ser diferentes");
										exdfcpr.actualizaNumeroOrden(exmrecDTO3.getCodEmpresa(), rutprov, dvrutprov, nroDocto, String.valueOf(nroOc));
									}
									//Busco datos del articulo EXDREC
									ExdrecDTO exdrecDTO3 = exdrec.buscaDetalle(exmrecDTO3.getCodEmpresa(), nroOc, exmrecDTO3.getFechaConfirmacionRecepcion(), exmrecDTO3.getHoraConfirmacionRecepcion(), exmodcDTO.getBodegaOrigen(), codarticulo);
									
									if (exdfcprDTO2!=null){
										
										//Folio del documento (Solo folian los documentos Facturas Electronicas, Facturas Exentas y Facturas Normales)
										if (exdfcprDTO2.getCodDocumento()==33 || exdfcprDTO2.getCodDocumento()==36 || exdfcprDTO2.getCodDocumento()==3){
										
											logi.info("FOLIO DOCUMENTO");
											
											//Busco si documento ya esta Foliado
											nroFolio = conarc.buscaDocumentoContabilizado(nroDocto, exdfcprDTO2.getCodDocumento(), rutprov, dvrutprov);
											
											if (nroFolio == 0){
												
												String sFechaDocumento="";
												
												anoPeriodo = Integer.parseInt(fch.getYYYYMMDD().substring(0,4));
												mesPeriodo = Integer.parseInt(fch.getYYYYMMDD().substring(4,6));
												
												sFechaDocumento = String.valueOf(lfechaDoctoCONARC);
												anoDocumento = Integer.parseInt(sFechaDocumento.substring(0, 4));
												mesDocumento = Integer.parseInt(sFechaDocumento.substring(4, 6));
												
												//Valido si fecha documento pertenece al mes en curso
												if (anoDocumento == anoPeriodo){
													//Valido si los meses son iguales
													if (mesDocumento == mesPeriodo){
														
														//Valido si existe periodo (si no existe lo crea)
														if (cotplc.buscaPeriodo(anoPeriodo, mesPeriodo)==null){
															//Creo el periodo
															cotplcDTO.setAno(anoPeriodo);
															cotplcDTO.setMes(mesPeriodo);
															cotplcDTO.setEstado("A");
															cotplc.insertaPeriodo(cotplcDTO);
														}
													}
													else if (mesDocumento < mesPeriodo){
														
														//Si es del mismo año pero es un mes anterior el del documento
														//Valido si el mes anterior esta ACTIVO para insertar el Folioo en ese periodo
														
														mesPeriodoAnt = mesPeriodo -1;
														anoPeriodoAnt = anoPeriodo;
														
														if (mesPeriodoAnt == 0){
															mesPeriodoAnt = 12;
															anoPeriodoAnt = anoPeriodoAnt - 1;
														}
														
														//Valido si periodo anterior esta ACTIVO
														CotplcDTO cotplcDTO2 = cotplc.buscaPeriodo(anoPeriodoAnt, mesPeriodoAnt);
														
														if ("A".equals(cotplcDTO2.getEstado().trim())){
															mesPeriodo = mesPeriodoAnt;
															anoPeriodo = anoPeriodoAnt;
														}
														else{
															mesPeriodo = mesPeriodo;
															anoPeriodo = anoPeriodo;
														}
													}
												}
												else if (anoDocumento < anoPeriodo){
													//Si es factura del año anterior retrocedo al periodo anterior del Actual
													mesPeriodoAnt = mesPeriodo - 1;
													anoPeriodoAnt = anoPeriodo;
													
													if (mesPeriodoAnt == 0){
														mesPeriodoAnt = 12;
														anoPeriodoAnt = anoPeriodoAnt - 1;
													}
													
													//Valido si periodo anterior esta ACTIVO
													CotplcDTO cotplcDTO2 = cotplc.buscaPeriodo(anoPeriodoAnt, mesPeriodoAnt);
													
													if ("A".equals(cotplcDTO2.getEstado().trim())){
														mesPeriodo = mesPeriodoAnt;
														anoPeriodo = anoPeriodoAnt;
													}
													else{
														mesPeriodo = mesPeriodo;
														anoPeriodo = anoPeriodo;
													}
												}
												
												//Obtengo Folio para el documento
												nuevoFolio=conarc.buscaFolioDisponible(exmrecDTO3.getCodEmpresa(), anoPeriodo, mesPeriodo);
												
												logi.info("ACTUALIZACION DE FOLIO DOCUMENTO");
												//Actualizo Libro de compras
												conarc.actualizaFolioLibro(exmrecDTO3.getCodEmpresa(), lCodDoctoCONARC, lNumDoctoCONARC, rutprov, dvrutprov, nuevoFolio, anoPeriodo, mesPeriodo);
											}
										}
										
										//Actualizar costos 
										logi.info("RPG ACTUALIZACION DE COSTOS");
										
										String sRPGExmarb = formaStringExmarb(exdrecDTO3, exmodcDTO, exmarb, exdodc, varcost, extari);
										
										if ("".equals(sRPGExmarb)){
											logi.info("NO ACTUALIZA COSTOS AL ARTICULO");
										}
										else{
											proce.procesaCalculoProcedure(sRPGExmarb);
										}
										
										//Guardo el stock pedido de la OC
										
										HashMap hash1 = exdodc.HashMapExdodc(2, nroOc);
										ExdodcDTO exdodcDTO2 = (ExdodcDTO) hash1.get(codarticulo);
										
										stockPedidoOC=exdodcDTO2.getStockPedidoOC();
										stockPendienteOC=exdodcDTO2.getStockPendienteOC();
										stockRecepcionadoOC=exdodcDTO2.getStockRecepcionadoOC();
										
										//Estado del detalle de la OC
										//if (stockPedidoOC-(stockRecepcionadoOC+stockRecep)==0){
										if ((stockPendienteOC-stockRecep)==0){
											estadoDetalleOC="R";
										}
										else{
											estadoDetalleOC="P";
										}
										
										logi.info("ELIMINA ARTICULO DE LA OC");
										//Elimina el articulo de la OC en EXDODC para que el proceso lo vuelva a insertar
										exdodc.eliminaArticuloOrden(exmrecDTO3.getCodEmpresa(), exmrecDTO3.getNumeroOrden(), lineaArt, exdodcDTO.getCodArticulo());
										
										//Actualiza EXDODC
										logi.info("RPG EXDODC");
										proce.procesaCalculoProcedure(formaStringExdodc(exdrecDTO3, estadoDetalleOC, exdodcDTO, stockPedidoOC, stockPendienteOC, stockRecepcionadoOC,stockRecep));
										
										/* Ahora lo realiza el cierre de camion de la recepcion
										//Estado de la cabecera de la OC
										if (exdodc.recuperEstadoparaCab(exmrecDTO3.getCodEmpresa(), nroOc)==null){
											estadoCabeceraOC="R";
										}
										else{
											estadoCabeceraOC="P";
										}
										*/
										estadoCabeceraOC="P";
										
										
										logi.info("RPG EXMODC");
										//Actualizacion EXMODC (Proceso SYSCON validaPeso y Estado)
										proce.procesaCalculoProcedure(formaStringExmodc(exmodcDTO, estadoCabeceraOC, exmrecDTO3.getNumeroDocumento()));
										
										logi.info("BUSCO SI LA FACTURA DE LA OC YA TIENE INGRESO VECMAR");
										
										//Busco si la OC ya tiene un ingreso anterior generado para tomar el correlativo interno y solo generar VEDMAR
										vecmarDTO.setCodigoEmpresa(exmrecDTO3.getCodEmpresa());
										vecmarDTO.setCodTipoMvto(1);
										vecmarDTO.setNumeroOrdenCompra(exmrecDTO3.getNumeroOrden());
										vecmarDTO.setNumeroTipoDocumento(nroDocto);
										vecmarDTO.setRutProveedor(String.valueOf(exmrecDTO3.getRutProveedor()));
										vecmarDTO.setDvProveedor(exmrecDTO3.getDvProveedor());
										vecmarDTO.setCodigoBodega(exmrecDTO3.getCodigoBodega());
										
										VecmarDTO cabecera = vecmar.buscarIngresodeOC(vecmarDTO);
										if (cabecera!=null){
											correlativoOC=cabecera.getNumDocumento();
										}
										else{
											//Busco numero interno del movimiento 01
											//correlativoOC = tpacor.recupeCorrelativo(0, 44);
											String str = "ASYSRCD00 00008   0  30   ";
											correlativoOC = proce.obtieneCorrelativo(str);
											logi.info("RPG VECMAR");
											//Guardar Recepcion VECMAR
											proce.procesaCalculoProcedure(formaStringVecmar(1, exmodcDTO.getCodEmpresa(), nroDocto, exmrecDTO3.getCodigoBodega(), 0,correlativoOC, exdfcprDTO2.getCodDocumento(), exdfcprDTO2.getFechaDocto(), exdfcprDTO2.getTotalBruto(), exdfcprDTO2.getTotalNeto(), exmodcDTO.getNumeroOrden(), exmodcDTO.getFormaPago(), exmodcDTO.getCantLineas(), exmodcDTO.getRutProveedor(), exmodcDTO.getDigito() , PesoRecep, VoluRecep));
										}
										
										//Busco si el articulo ya esta insertado en VEDMAR (si es asi actualizo lo recepcionado)
										vedmarDTO.setCodigoEmpresa(exmrecDTO3.getCodEmpresa());
										vedmarDTO.setCodTipoMvto(1);
										vedmarDTO.setNumDocumento(correlativoOC);
										vedmarDTO.setCodigoBodega(exmrecDTO3.getCodigoBodega());
										vedmarDTO.setCodigoArticulo(exdodcDTO.getCodArticulo());
										
										VedmarDTO detalleoc = vedmar.buscaArticuloVedmar(vedmarDTO);
										if (detalleoc!=null){
											//Actualiza articulo en VEDMAR
											logi.info("ACTUALIZA ARTICULO EN VEDMAR");
											
											vedmarDTO.setCodigoEmpresa(exmrecDTO3.getCodEmpresa());
											vedmarDTO.setCodTipoMvto(1);
											vedmarDTO.setNumDocumento(correlativoOC);
											vedmarDTO.setCodigoBodega(exmrecDTO3.getCodigoBodega());
											vedmarDTO.setCodigoArticulo(exdodcDTO.getCodArticulo());
											vedmarDTO.setCantidadFormato(stockRecep);
											vedmarDTO.setCantidadArticulo(stockRecep);
											vedmarDTO.setPesoLinea(PesoRecep);
											vedmarDTO.setVolumenArticulo(VoluRecep);
											vedmarDTO.setMontoBrutoLinea(dMontoBrutoLinea);
											vedmarDTO.setMontoTotalLinea((int)dTotalBrutoLinea);
											
											//Calcula costos Netos
											double ddCostoTotalNeto=0;
											
											ddCostoTotalNeto=stockRecep*detalleoc.getPrecioNeto();
											
											vedmarDTO.setCostoTotalNeto(ddCostoTotalNeto);
											vedmarDTO.setMontoNeto(ddCostoTotalNeto);
											vedmarDTO.setMontoTotalNetoLinea((int)ddCostoTotalNeto);
											
											vedmar.actualizaArticulo(vedmarDTO);
											
											logi.info("ACTUALIZA STOCK EN LINEA DE LA BODEGA DEL ARTICULO : "+exdodcDTO.getCodArticulo());
											//Actualiza stock en linea del articulo que ya se encuentra en VEDMAR
											double stockLineaActualizar = stockLineaArt + stockRecep;
											
											if (stockLineaActualizar<0){
											//	mailNegativo.envioMail("Procesa RECEPCION 769 :"+"NOMBRE ARCHIVO :"+ nameFile + " "+exdodcDTO.getCodArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ stockRecep+" STOCK AHORA : "+stockLineaActualizar);
											}
											exmarb.actualizaStockLinea(exmrecDTO3.getCodigoBodega(), exdodcDTO.getCodArticulo(), exdodcDTO.getDvArticulo(), stockLineaActualizar);
											logi.info("Estado Inventario :" +estadoInventario);
											if (estadoInventario.trim().equals("D")){
												logi.info("MUEVE STOCK");
											}else{
												logi.info("VUELVE ATRAS STOCK EN LINEA");
												ExmarbDTO exm = exmarb.recuperaArticulo(exmrecDTO3.getCodigoBodega(), exdodcDTO.getCodArticulo());
												exmarb.actualizaStockLinea(exmrecDTO3.getCodigoBodega(), exdodcDTO.getCodArticulo(),exdodcDTO.getDvArticulo().trim(), exm.getStockLinea()-stockRecep);

											}
										}
										else{
										
											//Guardar Recepcion VEDMAR
											logi.info("RPG VEDMAR");
											proce.procesaCalculoProcedure(formaStringVedmar(1, exmrecDTO3.getCodEmpresa(), exmrecDTO3.getRutProveedor(), exmrecDTO3.getDvProveedor(), correlativoOC, (int)exdrecDTO3.getPrecioNeto(), (int)exdrecDTO3.getMontoNeto(), exmrecDTO3.getCodigoBodega(), exdodcDTO.getCodArticulo(), exdodcDTO.getDvArticulo().trim(), lineaArt, exdodcDTO.getPrecioBruto(), exmarbDTO.getCodigoSector(), PesoRecep, VoluRecep, stockRecep, dMontoBrutoLinea, dTotalBrutoLinea, "I", "0", exmarb));
											//Valida estado inventario si no es D se devuelven las cantidades al EXMARB JHCANQUIL
											logi.info("Estado Inventario :" +estadoInventario);
											if (estadoInventario.trim().equals("D")){
												logi.info("MUEVE STOCK");
											}else{
												logi.info("VUELVE ATRAS STOCK EN LINEA");
												ExmarbDTO exm = exmarb.recuperaArticulo(exmrecDTO3.getCodigoBodega(), exdodcDTO.getCodArticulo());
												exmarb.actualizaStockLinea(exmodcDTO.getBodegaOrigen(), exdodcDTO.getCodArticulo(),exdodcDTO.getDvArticulo().trim(), exm.getStockLinea()-stockRecep);

											}
											
										}
										//procedimiento vencimiento articulos
										
										
										logi.info("ACTUALIZACION STOCK DE INVENTARIO");
										StockinventarioDTO listastock = stockinventario.lista(exmrecDTO3.getCodEmpresa(), exmrecDTO3.getCodigoBodega(), exdrecDTO3.getEstadoInventario(), exdodcDTO.getCodArticulo());
										if (listastock!=null){
											//Actualiza el stock del articulo por el tipo de stock de inventario
											stockinventarioDTO.setCodigoEmpresa(exmrecDTO3.getCodEmpresa());
											stockinventarioDTO.setCodigoArticulo(exdodcDTO.getCodArticulo());
											stockinventarioDTO.setCodigoBodega(exmrecDTO3.getCodigoBodega());
											stockinventarioDTO.setDvArticulo(exdodcDTO.getDvArticulo().trim());
											stockinventarioDTO.setEstado(exdrecDTO3.getEstadoInventario());
											stockinventarioDTO.setCantidad(listastock.getCantidad()+stockRecep);
											stockinventario.actualizaStock(stockinventarioDTO);
										}
										else{
											//Inserta el articulo
											stockinventarioDTO.setCodigoEmpresa(exmrecDTO3.getCodEmpresa());
											stockinventarioDTO.setCodigoArticulo(exdodcDTO.getCodArticulo());
											stockinventarioDTO.setCodigoBodega(exmrecDTO3.getCodigoBodega());
											stockinventarioDTO.setDvArticulo(exdodcDTO.getDvArticulo().trim());
											stockinventarioDTO.setEstado(exdrecDTO3.getEstadoInventario());
											stockinventarioDTO.setCantidad(stockRecep);
											stockinventario.creaStockInvWMS(stockinventarioDTO);
										}
										
										//Si stock de inventario de WMS es distinto a DISPONIBLE actualizo el stock el linea al capturado antes de generar el movimiento 01
										if (stockinventarioDTO.getEstado().compareTo("D")<0){
											//Actulizar stock en Linea EXMARB (guardado antes de generar movimiento 01)
											logi.info("ACTUALIZA STOCK EN LINEA PARA DEJARLO COMO ESTABA ANTES DE LA RECEPCION");
											logi.info("Estado Inventario :" +estadoInventario);
											
											if (stockLineaArt<0){
												//mailNegativo.envioMail("Procesa RECEPCION 812 :"+"NOMBRE ARCHIVO :"+ nameFile +  " "+exdodcDTO.getCodArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ stockRecep+" STOCK AHORA : "+stockLineaArt);
											}
											exmarb.actualizaStockLinea(exmodcDTO.getBodegaOrigen(), exdodcDTO.getCodArticulo(),exdodcDTO.getDvArticulo().trim(), stockLineaArt);
										}
										
										//Ahora lo realiza el cierre de camion de la recepcion
										/*
										logi.info("ACTUALIZA ESTADO EN LA INTERFAZ");
										//ACTUALIZAR ESTADO A "R" EN TABLA INTERFAZ EXDFCPR
										if (exdfcpr.buscaFolioExdfcr(exmrecDTO3.getCodEmpresa(), nroDocto, rutprov, dvrutprov, exdfcprDTO2.getCodDocumento())!=0){
											exdfcpr.actualizaEstado(exmrecDTO3.getCodEmpresa(), rutprov, dvrutprov, exdfcprDTO2.getCodDocumento(), nroDocto, "R");
										}
										*/
										
										
										/* Lo realizara el Visual al momento de valorizar el documento
										//Graba enlace contable
										if (convaf.lista(conarcDTO.getCodDocumento(), conarcDTO.getNumeroDocumento(), conarcDTO.getFechaDocumento(), rutprov, dvrutprov)!=null){
											//Elimina el registro antes de ser creado nuevamente
											convafDTO.setCodigoDocumento(conarcDTO.getCodDocumento());
											convafDTO.setNumeroDocumento(conarcDTO.getNumeroDocumento());
											convafDTO.setFechaDocumento(conarcDTO.getFechaDocumento());
											convafDTO.setRutProveedor(rutprov);
											convafDTO.setDvProveedor(dvrutprov);
											convaf.eliminaEnlaceContable(convafDTO);
										}
										
										logi.info("RPG CONVAF");
										proce.procesaCalculoProcedure(formaStringConvaf(exmrecDTO3.getCodEmpresa(), conarcDTO.getCodDocumento(), conarcDTO.getNumeroDocumento(), conarcDTO.getFechaDocumento(), rutprov, dvrutprov, nroOc));
										
										*/
										
									}
									else{
										logi.info("DOCUMENTO NO SE ENCUENTRA EN LA INTERFAZ");
										//Enviar Mail informando el problema
										email.mail("Error Documento no se encuentra en la interfaz :"+" Nombre Archivo : "+nameFile + " Numero OC : "+  nroOc + " Numero Documento : " +nroDocto +" Rut Prov : " + rutprov + " DV :" +dvrutprov);

									}
								}
							}
						}
						else{
							//OT
							double cantDespachada=0;
							double cantDespachoPendiente=0;
							double montoNeto=0;
							int rutProv=0;
							int fechaRecepcion=0;
							int horaRecepcion=0;
							String dvProv="";
							double cantCobroChofer=0;
							double stockLineaArt2=0;
							
							ExdtraDTO exdtraDTO = new ExdtraDTO();
							
							exmrecDTO2.setCodEmpresa(2);
							exmrecDTO2.setNumeroOrden(nroOc);
							exmrecDTO2.setRutProveedor(rutProv);
							exmrecDTO2.setDvProveedor(dvProv);
							exmrecDTO2.setNumeroDocumento(nroDocto);
							
							if (exmrec.buscaEncabezado(exmrecDTO2)==null){
								fechaRecepcion=Integer.parseInt(fch.getYYYYMMDD());
								horaRecepcion=Integer.parseInt(fch.getHHMMSS());
								
								//Inserta Encabezado y Luego detalle de confirmacion de recepcion
								exmrecDTO2.setFechaConfirmacionRecepcion(fechaRecepcion);
								exmrecDTO2.setHoraConfirmacionRecepcion(horaRecepcion);
								exmrecDTO2.setCodigoBodega(26);
								exmrecDTO2.setNombreArchivoConfirmacion(nameFile);
								exmrecDTO2.setRutProveedor(rutProv);
								exmrecDTO2.setDvProveedor(dvProv);
								exmrecDTO2.setIdCamion(sIdCamion);
								exmrecDTO2.setNumeroDocumento(nroDocto);
								//Inserta solo una vez
								exmrec.generaEncabezado(exmrecDTO2);
								logi.info("INSERTA CABECERA DE LA OT PARA GUARDAR ID CAMION CERRADO");
							}
							else{
								
								//Busco datos del documento EXMREC
								exmrecDTO2.setCodEmpresa(2);
								exmrecDTO2.setNumeroOrden(nroOc);
								ExmrecDTO exmrecDTO3 = exmrec.buscaEncabezado(exmrecDTO2);
								
								fechaRecepcion=exmrecDTO3.getFechaConfirmacionRecepcion();
								horaRecepcion=exmrecDTO3.getHoraConfirmacionRecepcion();
								
								//Actualizo el nombre del archivo que fue recepcionado en la OC y Factura
								exmrecDTO5.setCodEmpresa(exmrecDTO3.getCodEmpresa());
								exmrecDTO5.setNumeroOrden(exmrecDTO3.getNumeroOrden());
								exmrecDTO5.setFechaConfirmacionRecepcion(fechaRecepcion);
								exmrecDTO5.setHoraConfirmacionRecepcion(horaRecepcion);
								exmrecDTO5.setCodigoBodega(exmrecDTO3.getCodigoBodega());
								exmrecDTO5.setRutProveedor(exmrecDTO3.getRutProveedor());
								exmrecDTO5.setDvProveedor(exmrecDTO3.getDvProveedor());
								exmrecDTO5.setNumeroDocumento(exmrecDTO3.getNumeroDocumento());
								exmrecDTO5.setNombreArchivoConfirmacion(nameFile);
								exmrecDTO5.setIdCamion(sIdCamion);
								
								exmrec.actualizaEncabezado(exmrecDTO5);
								logi.info("ACTUALIZA CABECERA DE LA OT PARA GUARDAR ID CAMION CERRADO");
							}
							
							//Genera Movimiento de XML Procesado
							exmfwmsDTO.setCodigoEmpresa(2);
							exmfwmsDTO.setNumeroOrdenCompra(nroOc);
							exmfwmsDTO.setNombreArchivoXML(nameFile);
							exmfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
							exmfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
							exmfwms.generaArchivoXML(exmfwmsDTO);
							
							//Valido si las cantidades a despachar son distintas al formato UNIDAD en EXDTRA
							exdtraDTO.setCodEmpresa(2);
							exdtraDTO.setNumTraspaso(nroOc);
							exdtraDTO.setCodArticulo(codarticulo);
							if (exdtra.buscaOTCarguio(exdtraDTO)!=null){
								
								ExmtraDTO exmtraDTO2 = exmtra.recuperaEncabezado(2, nroOc);
								ExdtraDTO exdtraDTO2 = exdtra.buscaOTCarguio(exdtraDTO);
								
								//Busca costo neto del articulo en la bodega destino
								ExmarbDTO exmarbDTO2 = exmarb.recuperaArticulo(exmtraDTO2.getBodegaDestino(), codarticulo);
								
								//Guardo Stock en linea del articulo
								stockLineaArt=exmarbDTO2.getStockLinea();
								
								//busco datos de Pallet, Display, Caja del articulo					
								ExmartDTO exmartDTO = exmart.recuperaArticuloSinDigito(codarticulo);
								
								if ("C".equals(exdtraDTO2.getFormato().trim())){
									
									if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0){
										cantDespachada=exdtraDTO2.getCantDespachada() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
									}
									
									if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0){
										cantDespachada=exdtraDTO2.getCantDespachada() * exmartDTO.getCaja();
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * exmartDTO.getCaja();
									}
									
									if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0){
										cantDespachada=exdtraDTO2.getCantDespachada() * exmartDTO.getDisplay();
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * exmartDTO.getDisplay();
									}
								}
								
								if ("D".equals(exdtraDTO2.getFormato().trim())){
									if (exmartDTO.getDisplay()>0){
										cantDespachada=exdtraDTO2.getCantDespachada() * exmartDTO.getDisplay();
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * exmartDTO.getDisplay();
									}
								}
								
								if ("P".equals(exdtraDTO2.getFormato().trim())){
									
									if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()>0){
										cantDespachada=exdtraDTO2.getCantDespachada() * (exmartDTO.getPallet() * exmartDTO.getCaja() * exmartDTO.getDisplay());
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * (exmartDTO.getPallet() * exmartDTO.getCaja() * exmartDTO.getDisplay());
									}
									
									if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
										cantDespachada=exdtraDTO2.getCantDespachada() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
									}
									
									if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()==0){
										cantDespachada=exdtraDTO2.getCantDespachada() * exmartDTO.getCaja();
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * exmartDTO.getCaja();
									}
									
									if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
										cantDespachada=exdtraDTO2.getCantDespachada() * exmartDTO.getDisplay();
										cantDespachoPendiente=exdtraDTO2.getCantPendiente() * exmartDTO.getDisplay();
									}
									
									if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()>0){
										cantDespachada=exdtraDTO2.getCantDespachada() * (exmartDTO.getPallet() * exmartDTO.getCaja());
										cantDespachoPendiente=exdtraDTO2.getCantDespachada() * (exmartDTO.getPallet() * exmartDTO.getCaja());
									};
								}
								
								if ("U".equals(exdtraDTO2.getFormato().trim())){
									cantDespachada=exdtraDTO2.getCantDespachada();
									cantDespachoPendiente=exdtraDTO2.getCantPendiente();
								}
								
								//Actualiza EXDTRA
								exdtraDTO.setCodEmpresa(exmtraDTO2.getCodigoEmpresa());
								exdtraDTO.setNumTraspaso(nroOc);
								exdtraDTO.setCodArticulo(codarticulo);
								exdtraDTO.setFormato("U");
								exdtraDTO.setCantDespachada(cantDespachada);
								exdtraDTO.setCantPendiente(cantDespachoPendiente);
								exdtraDTO.setCantRecepCarguio((int)cantDespachada);
								exdtra.actualizaUnidadesOT(exdtraDTO);
								
								/*
								//Si existe diferencia entre lo despachado en la OT y lo recepcionado desde WMS
								//se inserta el articulo en la tabla EXDREC para luego hacer el cobro al transportista
								//al momento de realizar el cierre de camion
								if (cantDespachada!=stockRecep)
								{	
									cantCobroChofer=cantDespachada-stockRecep;
									
									//Genera Detalle
									exdrecDTO2.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
									exdrecDTO2.setNumeroOrden(nroOc);
									exdrecDTO2.setFechaConfirmacionRecepcion(fechaRecepcion);
									exdrecDTO2.setHoraConfirmacionRecepcion(horaRecepcion);
									exdrecDTO2.setLinea(exdtraDTO2.getLinea());
									exdrecDTO2.setCodigoBodega(exmtraDTO2.getBodegaDestino());
									exdrecDTO2.setCodigoArticulo(codarticulo);
									exdrecDTO2.setDvArticulo(exmartDTO.getDvArticulo().trim());
									exdrecDTO2.setDescripcionArticulo(exmartDTO.getDescripcionArticulo().trim());
									exdrecDTO2.setStockRecepcionado(stockRecep);
									exdrecDTO2.setStockSolicitado(stockRecep);
									exdrecDTO2.setFechaVencimiento(Integer.parseInt(fechaVctoArt));
									exdrecDTO2.setEstadoInventario(sEstadoInventario);
									
									exdrecDTO2.setPrecioNeto(exmarbDTO2.getPrecioBaseBruto());
									exdrecDTO2.setPrecioBruto(exmarbDTO2.getPrecioBaseNeto());
									exdrecDTO2.setMontoNeto(0);
									exdrecDTO2.setMontoTotal(0);
									exdrecDTO2.setMontoBruto(0);
									exdrecDTO2.setMontoTotal(0);
									
									if (exdrec.buscaDetalle(exmtraDTO2.getCodigoEmpresa(), nroOc, fechaRecepcion, horaRecepcion, exmtraDTO2.getBodegaDestino(), codarticulo)!=null){
										exdrec.actualizaDetalle(exdrecDTO2);
										logi.info("ACTUALIZA ARTICULO DEL DOCUMENTO PARA QUE SEA COBRADO EN SYSCON");
									}
									else{
										exdrec.generaDetalle(exdrecDTO2);
										logi.info("INSERTA ARTICULO DEL DOCUMENTO PARA QUE SEA COBRADO EN SYSCON");
									}
								}
								*/
								
								//Calcula Peso y Volumen
								logi.info("PESO ARTICULO OT"+exdtraDTO2.getPesoArticulo());
								logi.info("VOLUMEN ARTICULO OT"+exdtraDTO2.getVolumenArticulo());
								
								//PesoRecep=exdtraDTO2.getPesoArticulo()*cantDespachada;
								//VoluRecep=exdtraDTO2.getVolumenArticulo()*cantDespachada;
								
								PesoRecep=exdtraDTO2.getPesoArticulo()*stockRecep;
								VoluRecep=exdtraDTO2.getVolumenArticulo()*stockRecep;
								
								logi.info("PESO CALCULADO"+PesoRecep);
								logi.info("VOLUMEN CALCULADO"+VoluRecep);
								
								/*
								//Se lleva la actualizacion de costos al Cierre de camion
								if (cantDespachada>0 && stockLineaArt<cantDespachada){
									//Actualizacion de Costos
									logi.info("RPG EXMARB ACTUALIZACION COSTOS");
									proce.procesaCalculoProcedure(formaStringExmarbOT(exmarbDTO2, exmtraDTO2.getCodigoEmpresa(), exdtraDTO2.getValorUnitario()));
								}
								*/
								
								//Guarda detalle de recepcion
								logi.info("RPG EXDTRA RECEPCION OT");
								
								proce.procesaCalculoProcedure(formaStringDetalleRecepOT(exmtraDTO2.getCodigoEmpresa(),nroOc,fechaVctoArt,codarticulo, exdtraDTO2.getDigitoVerificador(), exdtraDTO2.getLinea(), exdtraDTO2.getCodBarra(), exdtraDTO2.getValorUnitario(), exdtraDTO2.getVolumenArticulo(), 
											exdtraDTO2.getPesoArticulo(), cantDespachada, cantDespachada, cantDespachada ));
								
								//logi.info("RPG EXBTRA AUMENTA STOCK EN BODEGA DESTINO");
								//proce.procesaCalculoProcedure(formaStringActStockBodDestino(2, nroOc, exmtraDTO2.getBodegaDestino()));
								
								/*
								//Se lleva la actualizacion del stock en linea al Cierre de camion 
								stockLineaArt2=stockLineaArt+stockRecep;
								exmarb.actualizaStockLinea(exmtraDTO2.getBodegaDestino(), codarticulo,exdtraDTO2.getDigitoVerificador().trim(), stockLineaArt2);
								*/
								
								logi.info("BUSCO SI LA FACTURA DE LA OT YA TIENE INGRESO VECMAR");
								
								//montoNeto=cantDespachada*exmarbDTO2.getCostoNeto();
								montoNeto=stockRecep*exmarbDTO2.getCostoNeto();
								
								//Busco si el TRASPASO ya tiene un ingreso anterior generado para tomar el correlativo interno y solo generar VEDMAR
								vecmarDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
								vecmarDTO.setCodTipoMvto(4);
								vecmarDTO.setNumeroOrdenCompra(nroOc);
								vecmarDTO.setNumeroTipoDocumento(nroDocto);
								vecmarDTO.setRutProveedor(String.valueOf(rutProv));
								vecmarDTO.setDvProveedor(dvProv);
								vecmarDTO.setCodigoBodega(exmtraDTO2.getBodegaOrigen());
								vecmarDTO.setBodegaDestino(exmtraDTO2.getBodegaDestino());
								
								VecmarDTO cabecera = vecmar.buscarIngresodeOC(vecmarDTO);
								if (cabecera!=null){
									correlativoOC=cabecera.getNumDocumento();
								}
								else{
									correlativoOC = nroDocto;
									
									Fecha fch2 = new Fecha();
									
									int fechaSYS04=0;
									fechaSYS04 = Integer.parseInt(fch2.getYYYYMMDD());
									
									logi.info("RPG VECMAR");
									//Guardar Recepcion VECMAR
									proce.procesaCalculoProcedure(formaStringVecmar(4, exmtraDTO2.getCodigoEmpresa(), nroDocto, exmtraDTO2.getBodegaOrigen(), exmtraDTO2.getBodegaDestino(), correlativoOC, 3, fechaSYS04, 0, 0, nroOc, 0, 0, rutProv, dvProv, PesoRecep, VoluRecep));
								}
								
								//Busco si el articulo ya esta insertado en VEDMAR (si es asi actualizo lo recepcionado)
								vedmarDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
								vedmarDTO.setCodTipoMvto(4);
								vedmarDTO.setNumDocumento(correlativoOC);
								vedmarDTO.setCodigoBodega(exmtraDTO2.getBodegaDestino());
								vedmarDTO.setCodigoArticulo(codarticulo);
								
								VedmarDTO detalleoc = vedmar.buscaArticuloVedmar(vedmarDTO);
								if (detalleoc!=null){
									//Actualiza articulo en VEDMAR
									logi.info("ACTUALIZA ARTICULO EN VEDMAR");
									
									vedmarDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
									vedmarDTO.setCodTipoMvto(4);
									vedmarDTO.setNumDocumento(correlativoOC);
									vedmarDTO.setCodigoBodega(exmtraDTO2.getBodegaDestino());
									vedmarDTO.setCodigoArticulo(codarticulo);
									
									//vedmarDTO.setCantidadFormato((int)cantDespachada);
									//vedmarDTO.setCantidadArticulo((int)cantDespachada);
									
									vedmarDTO.setCantidadFormato((int)stockRecep);
									vedmarDTO.setCantidadArticulo((int)stockRecep);
									
									
									vedmarDTO.setPesoLinea(PesoRecep);
									vedmarDTO.setVolumenArticulo(VoluRecep);
									vedmarDTO.setMontoBrutoLinea(dMontoBrutoLinea);
									vedmarDTO.setMontoTotalLinea((int)dTotalBrutoLinea);
									
									//Calcula costos Netos
									double ddCostoTotalNeto=0;
									
									//ddCostoTotalNeto=cantDespachada*detalleoc.getPrecioNeto();
									ddCostoTotalNeto=stockRecep*detalleoc.getPrecioNeto();
									
									vedmarDTO.setCostoTotalNeto(ddCostoTotalNeto);
									vedmarDTO.setMontoNeto(ddCostoTotalNeto);
									vedmarDTO.setMontoTotalNetoLinea((int)ddCostoTotalNeto);
									
									vedmar.actualizaArticulo(vedmarDTO);
								}
								else{
									//Guardar Recepcion VEDMAR
									logi.info("RPG VEDMAR");
									proce.procesaCalculoProcedure(formaStringVedmar(4, exmtraDTO2.getCodigoEmpresa(), rutProv, dvProv, correlativoOC, (int)exmarbDTO2.getCostoNeto(), (int)montoNeto, exmtraDTO2.getBodegaDestino(), codarticulo, exdtraDTO2.getDigitoVerificador().trim(), exdtraDTO2.getLinea(), exdtraDTO2.getValorUnitario(), exmarbDTO2.getCodigoSector(), PesoRecep, VoluRecep, stockRecep, dMontoBrutoLinea, dTotalBrutoLinea, "I", "N", exmarb));
									logi.info("Estado Inventario :"+estadoInventario);
									if (estadoInventario.trim().equals("D")){
										logi.info("DEBE MOVER STOCK");
									}else{
										logi.info("VUELVE ATRAS STOCK EN LINEA");
										ExmarbDTO exm = exmarb.recuperaArticulo(exmtraDTO2.getBodegaDestino(), codarticulo);
										exmarb.actualizaStockLinea(exmtraDTO2.getBodegaDestino(), codarticulo,exdtraDTO2.getDigitoVerificador().trim(), exm.getStockLinea()-stockRecep);

									}
								
								}
								
								/*
								//Se lleva la actualizacion del stock de inventario al Cierre de camion 
								logi.info("ACTUALIZACION STOCK DE INVENTARIO");
								StockinventarioDTO listastock = stockinventario.lista(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getBodegaDestino(), sEstadoInventario, codarticulo);
								if (listastock!=null){
									//Actualiza el stock del articulo por el tipo de stock de inventario
									stockinventarioDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
									stockinventarioDTO.setCodigoArticulo(codarticulo);
									stockinventarioDTO.setCodigoBodega(exmtraDTO2.getBodegaDestino());
									stockinventarioDTO.setDvArticulo(exdtraDTO2.getDigitoVerificador().trim());
									stockinventarioDTO.setEstado(sEstadoInventario);
									//stockinventarioDTO.setCantidad(listastock.getCantidad()+(int)cantDespachada);
									stockinventarioDTO.setCantidad(listastock.getCantidad()+(int)stockRecep);
									stockinventario.actualizaStock(stockinventarioDTO);
								}
								else{
									//Inserta el articulo
									stockinventarioDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
									stockinventarioDTO.setCodigoArticulo(codarticulo);
									stockinventarioDTO.setCodigoBodega(exmtraDTO2.getBodegaDestino());
									stockinventarioDTO.setDvArticulo(exdtraDTO2.getDigitoVerificador().trim());
									stockinventarioDTO.setEstado(sEstadoInventario);
									//stockinventarioDTO.setCantidad((int)cantDespachada);
									stockinventarioDTO.setCantidad((int)stockRecep);
									stockinventario.creaStockInvWMS(stockinventarioDTO);
								}
								
								//Si stock de inventario de WMS es distinto a DISPONIBLE actualizo el stock el linea al capturado antes de generar el movimiento 01
								if (stockinventarioDTO.getEstado().compareTo("D")<0){
									//Actulizar stock en Linea EXMARB (guardado antes de generar movimiento 01)
									logi.info("ACTUALIZA STOCK EN LINEA PARA DEJARLO COMO ESTABA ANTES DE LA RECEPCION");
									exmarb.actualizaStockLinea(exmtraDTO2.getBodegaDestino(), codarticulo,exdtraDTO2.getDigitoVerificador().trim(), stockLineaArt);
								}
								*/
							}
							
							
						}
					}
				}
			}
			
			Conar1DAO conar1 = dao.getConar1DAO();
			ChoftranDAO choftran = dao.getChoftranDAO();
			StockdifDAO stockdif = dao.getStockdifDAO();
			ClmcliDAO clmcli = dao.getClmcliDAO();
			DocncpDAO docncp = dao.getDocncpDAO();
			RutservDAO rutservDAO = dao.getRutServDAO();
			
			ExmodcDTO exdodcDTO = new ExmodcDTO();
			VecmarDTO vecmarDTO = new VecmarDTO();
			VedmarDTO vedmarDTO = new VedmarDTO();
			TptimpDTO tptimpDTO = new TptimpDTO();
			TiptoleDTO tiptoleDTO = new TiptoleDTO();
			ExmcreDTO exmcreDTO = new ExmcreDTO();
			ExmtraDTO exmtraDTO = new ExmtraDTO();
			
			ExmfwmsDTO exmfwmsDTO = new ExmfwmsDTO();
			
			double montoTotal=0;
			double montoTotalNeto=0;
			double lFactorImpuestos=0;
			int valorTolerancia=0;
			int correlativoSolicitud=0;
			String andenRecepcion="";
			
			//XML cierre de camion Recepcion
			//Lista XML confirmacion de Recepcion
			NodeList nList2 = doc.getElementsByTagName("RCV_TRLR_SEG");
			
			logi.info("----------------------------");

			
			for (int temp = 0; temp < nList2.getLength(); temp++) 
			{
				Node nNode = nList2.item(temp);
				
				logi.info("\nCurrent Element :" + nNode.getNodeName());
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					
					logi.info("SITCOD : " + eElement.getElementsByTagName("SITCOD").item(0).getTextContent());
					logi.info("CARCOD : " + eElement.getElementsByTagName("CARCOD").item(0).getTextContent());
					logi.info("TRLR_NUM : " + eElement.getElementsByTagName("TRLR_NUM").item(0).getTextContent());
					logi.info("TRLR_STAT : " + eElement.getElementsByTagName("TRLR_STAT").item(0).getTextContent());
					logi.info("YARD_LOC : " + eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent());
					logi.info("CLOSE_DTE : " + eElement.getElementsByTagName("CLOSE_DTE").item(0).getTextContent());
					logi.info("STOLOC_WH_ID : " + eElement.getElementsByTagName("STOLOC_WH_ID").item(0).getTextContent());
					logi.info("TRKNUM : " + eElement.getElementsByTagName("TRKNUM").item(0).getTextContent());
					
					sIdCamion=eElement.getElementsByTagName("TRLR_NUM").item(0).getTextContent();
					sTipCierre=eElement.getElementsByTagName("CARCOD").item(0).getTextContent();
					andenRecepcion=eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent();
					
					//Busco numero de (OC/OT) con el IDCamion en la cabecera del EXMREC
					ExmrecDTO exmrecDTO2 = exmrec.buscaOcRececp(sIdCamion);
					
					logi.info("TIPO DE CIERRE : "+sTipCierre);
					
					if ("PROVEEDOR".equals(sTipCierre.trim()))
					{	
						//Busco numero de (OC/OT) recepcionado 
						if (exmrec.buscaOcRececp(sIdCamion)!=null)
						{
							//Busco si el cierre de camion corresponde a OC
							if (exmodc.buscaCabOrden(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroOrden())!=null)
							{ 	
								//Cierre camion OC
								
								//Busco datos del documento en la Interfaz
								ExdfcprDTO exdfcprDTO2 = exdfcpr.buscaDocumentoExdfcr(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroOrden(), exmrecDTO2.getNumeroDocumento(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor());
								//ExdfcprDTO exdfcprDTO2 = exdfcpr.buscaDocumentoExdfcr(exmrecDTO3.getCodEmpresa(), nroOc, nroDocto, rutprov, dvrutprov);
								if (exdfcprDTO2.getNumeroOrden().trim().equals(nroOc)){
									logi.info("Numero de Orden iguales");
								}else{
									logi.info("Modifica Orden de Compra por ser diferentes");
									exdfcpr.actualizaNumeroOrden(exmrecDTO2.getCodEmpresa(), rutprov, dvrutprov, nroDocto, String.valueOf(nroOc));
								}
								//Estado de la cabecera de la OC
								if (exdodc.recuperEstadoparaCab(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroOrden())==null)
								{
									estadoCabeceraOC="R";
								}
								else
								{
									estadoCabeceraOC="P";
								}
								
								//Actualizar cabecera OC
								exmodc.actualizarCabecera(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroOrden(), exmrecDTO2.getCodigoBodega(), estadoCabeceraOC);
								
								logi.info("ACTUALIZA ESTADO EN LA INTERFAZ");
								//ACTUALIZAR ESTADO A "R" EN TABLA INTERFAZ EXDFCPR
								if (exdfcpr.buscaFolioExdfcr(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroDocumento(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor(), exdfcprDTO2.getCodDocumento())!=0)
								{
									exdfcpr.actualizaEstado(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor(), exdfcprDTO2.getCodDocumento(), exmrecDTO2.getNumeroDocumento(), "R");
								}
								
								//Busco si la OC ya tiene un ingreso anterior generado para tomar el correlativo interno y solo generar VEDMAR
								vecmarDTO.setCodigoEmpresa(exmrecDTO2.getCodEmpresa());
								vecmarDTO.setCodTipoMvto(1);
								vecmarDTO.setNumeroOrdenCompra(exmrecDTO2.getNumeroOrden());
								vecmarDTO.setNumeroTipoDocumento(exmrecDTO2.getNumeroDocumento());
								vecmarDTO.setRutProveedor(String.valueOf(exmrecDTO2.getRutProveedor()));
								vecmarDTO.setDvProveedor(exmrecDTO2.getDvProveedor());
								vecmarDTO.setCodigoBodega(exmrecDTO2.getCodigoBodega());
								
								VecmarDTO cabecera = vecmar.buscarIngresodeOC(vecmarDTO);
								if (cabecera!=null)
								{
									correlativoOC=cabecera.getNumDocumento();
									fechaMvto=cabecera.getFechaMvto();
									
									logi.info("RECUPERA TOTALES DEL VEDMAR");
									//Recupera totales del VEDMAR
									VecmarDTO detalle = vedmar.recuperaTotales(exmrecDTO2.getCodEmpresa(), 1, fechaMvto, correlativoOC);
									if (detalle!=null)
									{	
										//total factura menos el total recepcionado
										montoTotal=exdfcprDTO2.getTotalBruto()-detalle.getTotalDocumento();
										
										//Recupero impuestos del documento desde el libro de compras
										List lista = conar1.recuperaImpuesto(exdfcprDTO2.getCodDocumento(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor(), exmrecDTO2.getNumeroDocumento());
										
										Iterator imptos = lista.iterator();
										while (imptos.hasNext())
										{
											Conar1DTO dto = (Conar1DTO) imptos.next();
											
											int codImpto = dto.getCodigoImpuesto();
											
											//Busco valor del impuesto
											TptimpDTO valimpto = tptimp.recuperaValorImpuesto(codImpto);
											if (valimpto!=null)
											{
												lFactorImpuestos=lFactorImpuestos+valimpto.getValorImpuesto();
											}
										}
										
										//calcula total neto 
										montoTotalNeto=montoTotal / ((lFactorImpuestos / 100) + 1);
										
										logi.info("ACTUALIZA CABECERA VECMAR");
										//Actualiza totales de la recepcion en VECMAR
										vecmar.actualizaVecmarMerma(exmrecDTO2.getCodEmpresa(), 1, fechaMvto, correlativoOC, detalle.getTotalNeto(), detalle.getTotalDocumento());
									}
								}
								logi.info("OBTIENE COLA DE IMPRESORA CONFIGURADA"); 
								
								String ColaImp="";
								//Actualiza SWITCH PROCESO 0 para imprimir auditoria en SYSCON JHCANQUIL 20161117
								vecmar.actualizaVecmarSwitch(exmrecDTO2.getCodEmpresa(), 1, fechaMvto, correlativoOC, "0");

								ImpauditDTO audito = impaudit.buscaColaImpresionAudit(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getCodigoBodega());
								if (audito!=null){
									ColaImp = audito.getColaImp();
									
									logi.info("IMPRESION AUDITORIA");
									//Seleccion de Cola de Impresion
									proce.procesaCalculoProcedure(formaColaImp(ColaImp));
									
									//Impresion Auditoria
									proce.procesaCalculoProcedure(formaStringExpaud(1, exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroDocumento(), exmrecDTO2.getRutProveedor(), exdfcprDTO2.getCodDocumento(), fechaMvto));
								}
								else{
									logi.info("AUDITORIA NO IMPRESA POR NO ESTAR CONFIGURADA LA BODEGA EN LA TABLA IMPAUDIT");
								}
								
								//Busco la tolerancia para la recepcion
								TiptoleDTO tolerancia = tiptole.recuperaTolerancia(3);
								if (tolerancia!=null)
								{
									valorTolerancia=tolerancia.getTolerancia();
								}
								
								//Si la diferencia entre la factura y lo recepcionado es mayor a la tolerancia genera Solicitud de NC
								if (montoTotal>valorTolerancia)
								{
									logi.info("SOLICITUD DE NOTA DE CREDITO CABECERA");
									
									proce.procesaCalculoProcedure(formaStringSolNC(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroDocumento(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor(), montoTotal));
									
									//falta buscar el correlativo con el que se inserto la cabecera
									ExmcreDTO correlat = exmcre.recuperaCorrSolicitud(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor(), exmrecDTO2.getNumeroDocumento(), exdfcprDTO2.getFechaDocto());
									if (correlat!=null)
									{
										correlativoSolicitud = correlat.getCorrelativo();
									}
									
									logi.info("SOLICITUD DE NOTA DE CREDITO DETALLE");
									
									proce.procesaCalculoProcedure(formaStringSolNCDet(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroDocumento(), exmrecDTO2.getRutProveedor(), exmrecDTO2.getDvProveedor(), montoTotalNeto, correlativoSolicitud));
									
								}
								
								//Genera Movimiento de XML Procesado
								exmfwmsDTO.setCodigoEmpresa(exmrecDTO2.getCodEmpresa());
								exmfwmsDTO.setNumeroOrdenCompra(exmrecDTO2.getNumeroOrden());
								exmfwmsDTO.setNombreArchivoXML(nameFile);
								exmfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
								exmfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
								exmfwms.generaArchivoXML(exmfwmsDTO);
							}
						}
					} //Fin dek CARCOD PROVEEDOR
					
					if ("TRASPASO".equals(sTipCierre.trim()))
					{
						//Busco numero de (OC/OT) recepcionado 
						if (exmrec.buscaOcRececp(sIdCamion)!=null)
						{
							//Cierre camion OT de traspaso
							int insertVecmarCobro=0;
							int numeroDoc=0;
							int numeroDoc2=0;
							int fechaMvtoVenta=0;
							int correlativo =0;
							int rutCobroVTA=0;
							String dvCobroVTA="";
							double stockLineaArt3=0;
							int fechaConfirmacion=0;
							int horaConfirmacion=0;
							double cantCobroChofer2=0;
							
							ExdrecDTO exdrecDTO5 = new ExdrecDTO();
							ExdtraDTO exdtraDTO2 = new ExdtraDTO();
							StockinventarioDTO stockinventarioDTO3 = new StockinventarioDTO();
							
							logi.info("BUSCA LOS DATOS DE LA OT");
							
							//Recupera EndPoint facturacion electronica
							RutservDTO rutservDTO = rutservDAO.recuperaEndPointServlet("FACTUR");
							
							//Busca datos de la cabecera del traspaso para actualizar estado
							ExmtraDTO exmtraDTO2 = exmtra.recuperaEncabezado(exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroOrden());
							
							//Obtengo los datos de cabecera de EXMREC 
							exmrecDTO2.setCodEmpresa(exmtraDTO2.getCodigoEmpresa());
							exmrecDTO2.setNumeroOrden(exmtraDTO2.getNumTraspaso());
							exmrecDTO2.setRutProveedor(0);
							exmrecDTO2.setDvProveedor("");
							exmrecDTO2.setNumeroDocumento(exmtraDTO2.getNumGuiaDespacho());
							
							if (exmrec.buscaEncabezado(exmrecDTO2)!=null)
							{
								ExmrecDTO exmrecDTO4 = exmrec.buscaEncabezado(exmrecDTO2);
								fechaConfirmacion=exmrecDTO4.getFechaConfirmacionRecepcion();
								horaConfirmacion=exmrecDTO4.getHoraConfirmacionRecepcion();
							}
							
							//Paso los datos al DTO para enviar al RPG
							exmtraDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
							exmtraDTO.setNumTraspaso(exmtraDTO2.getNumTraspaso());
							exmtraDTO.setBodegaOrigen(exmtraDTO2.getBodegaOrigen());
							exmtraDTO.setBodegaDestino(exmtraDTO2.getBodegaDestino());
							exmtraDTO.setNumeroSello(exmtraDTO2.getNumeroSello());
							exmtraDTO.setNumGuiaDespacho(exmtraDTO2.getNumGuiaDespacho());
							exmtraDTO.setFechaTraspaso(exmtraDTO2.getFechaTraspaso());
							exmtraDTO.setHoraTopeTraspaso(exmtraDTO2.getHoraTopeTraspaso());
							exmtraDTO.setKilosMercaderia(exmtraDTO2.getKilosMercaderia());
							exmtraDTO.setValorTTraspaso(exmtraDTO2.getValorTTraspaso());
							exmtraDTO.setValorVTraspaso(exmtraDTO2.getValorVTraspaso());
							exmtraDTO.setEstadoTraspaso("R");
							exmtraDTO.setCodigoUsuario(exmtraDTO2.getCodigoUsuario());
							exmtraDTO.setRutEmpresa(exmtraDTO2.getRutEmpresa());
							exmtraDTO.setDvEmpresa(exmtraDTO2.getDvEmpresa());
							exmtraDTO.setPatente(exmtraDTO2.getPatente());
							
							//Guarda cabecera de recepcion
							logi.info("RPG EXMTRA RECEPCION OT");
							proce.procesaCalculoProcedure(formaStringCabeceraRecepOT(exmtraDTO));
							
							//Busco si la OC ya tiene un ingreso anterior generado para tomar el correlativo interno y solo generar VEDMAR
							vecmarDTO.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
							vecmarDTO.setCodTipoMvto(4);
							vecmarDTO.setNumeroOrdenCompra(exmtraDTO2.getNumTraspaso());
							vecmarDTO.setNumeroTipoDocumento(exmtraDTO2.getNumGuiaDespacho());
							vecmarDTO.setRutProveedor("0");
							vecmarDTO.setDvProveedor("");
							vecmarDTO.setCodigoBodega(exmtraDTO2.getBodegaOrigen());
							vecmarDTO.setBodegaDestino(exmtraDTO2.getBodegaDestino());
							
							VecmarDTO cabecera = vecmar.buscarIngresodeOT(vecmarDTO);
							if (cabecera!=null)
							{
								fechaMvto=cabecera.getFechaMvto();
								
								logi.info("OBTIENE COLA DE IMPRESORA CONFIGURADA"); 
								
								String ColaImp2="";
								
								ImpauditDTO audito = impaudit.buscaColaImpresionAudit(exmrecDTO2.getCodEmpresa(), exmtraDTO2.getBodegaDestino());
								if (audito!=null){
									ColaImp2 = audito.getColaImp();
									
									logi.info("IMPRESION AUDITORIA");
									//Seleccion de Cola de Impresion
									proce.procesaCalculoProcedure(formaColaImp(ColaImp2));
									
									//Impresion Auditoria
									proce.procesaCalculoProcedure(formaStringExpaud(4, exmrecDTO2.getCodEmpresa(), exmrecDTO2.getNumeroDocumento(), 0, 0, fechaMvto));
								}
								else{
									logi.info("AUDITORIA NO IMPRESA POR NO ESTAR CONFIGURADA LA BODEGA EN LA TABLA IMPAUDIT");
								}
								
								//Busco el detalle del movimiento 04
								List lista2 = vedmar.obtenerDatosVedmarGuia(exmrecDTO2.getCodEmpresa(), 4, cabecera.getFechaMvto(), cabecera.getNumDocumento());
								
								Iterator iter2 = lista2.iterator();
								while (iter2.hasNext())
								{
									VedmarDTO dto = (VedmarDTO) iter2.next();
									
									//Busco datos del articulo en la bodega
									ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(dto.getCodigoBodega(), dto.getCodigoArticulo()); 
									
									//busco datos de Pallet, Display, Caja del articulo					
									ExmartDTO exmartDTO = exmart.recuperaArticuloSinDigito(dto.getCodigoArticulo());
									
									//Guardo Stock en linea del articulo
									stockLineaArt=exmarbDTO.getStockLinea();
									
									if (dto.getCantidadArticulo()>0 && stockLineaArt<dto.getCantidadArticulo()){
										//Actualizacion de Costos
										logi.info("RPG EXMARB ACTUALIZACION COSTOS");
										proce.procesaCalculoProcedure(formaStringExmarbOT(exmarbDTO, exmrecDTO2.getCodEmpresa(), dto.getPrecioUnidad()));
									}
									
									logi.info("ACTUALIZACION STOCK EN LINEA INGRESO POR TRASPASO 04");
									//Actualizacion Stock Linea Ingreso por traspaso (04)
									stockLineaArt3=stockLineaArt+dto.getCantidadArticulo();
									if (stockLineaArt3<0){
										//mailNegativo.envioMail("Procesa RECEPCION 1536 :"+"NOMBRE ARCHIVO :"+ nameFile +  " "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ dto.getCantidadArticulo()+" STOCK AHORA : "+stockLineaArt3);
									}
									exmarb.actualizaStockLinea(dto.getCodigoBodega(), dto.getCodigoArticulo(), dto.getDigArticulo().trim(), stockLineaArt3);
									logi.info("Estado Inventario :" +estadoInventario);
									if (estadoInventario.trim().equals("D")){
										logi.info("MUEVE STOCK");
									}else{
										logi.info("NO VUELVE ATRAS STOCK EN LINEA");
										ExmarbDTO exm = exmarb.recuperaArticulo(dto.getCodigoBodega(), dto.getCodigoArticulo());
										exmarb.actualizaStockLinea(dto.getCodigoBodega(), dto.getCodigoArticulo(),dto.getDigArticulo().trim(), exm.getStockLinea()-dto.getCantidadArticulo());

									}	
									//Busco datos del articulo EXDREC
									ExdrecDTO exdrecDTO3 = exdrec.buscaDetalle(exmrecDTO2.getCodEmpresa(), nroOc, fechaConfirmacion, horaConfirmacion, dto.getCodigoBodega(), dto.getCodigoArticulo());
									
									logi.info("ACTUALIZACION STOCK DE INVENTARIO INGRESO POR TRASPASO 04");
									StockinventarioDTO listastock = stockinventario.lista(exmrecDTO2.getCodEmpresa(), dto.getCodigoBodega(), exmtraDTO2.getCodigoInventario(), dto.getCodigoArticulo());
									if (listastock!=null){
										//Actualiza el stock del articulo por el tipo de stock de inventario
										stockinventarioDTO3.setCodigoEmpresa(exmrecDTO2.getCodEmpresa());
										stockinventarioDTO3.setCodigoArticulo(dto.getCodigoArticulo());
										stockinventarioDTO3.setCodigoBodega(dto.getCodigoBodega());
										stockinventarioDTO3.setDvArticulo(dto.getDigArticulo().trim());
										stockinventarioDTO3.setEstado(exmtraDTO2.getCodigoInventario());
										stockinventarioDTO3.setCantidad(listastock.getCantidad()+(int)dto.getCantidadArticulo());
										stockinventario.actualizaStock(stockinventarioDTO3);
									}
									else{
										//Inserta el articulo
										stockinventarioDTO3.setCodigoEmpresa(exmrecDTO2.getCodEmpresa());
										stockinventarioDTO3.setCodigoArticulo(dto.getCodigoArticulo());
										stockinventarioDTO3.setCodigoBodega(dto.getCodigoBodega());
										stockinventarioDTO3.setDvArticulo(dto.getDigArticulo().trim());
										stockinventarioDTO3.setEstado(exmtraDTO2.getCodigoInventario());
										stockinventarioDTO3.setCantidad((int)dto.getCantidadArticulo());
										stockinventario.creaStockInvWMS(stockinventarioDTO3);
									}
									
									//Si stock de inventario de WMS es distinto a DISPONIBLE actualizo el stock el linea al capturado antes de generar el movimiento 01
									if (stockinventarioDTO3.getEstado().compareTo("D")<0){
										//Actulizar stock en Linea EXMARB (guardado antes de generar movimiento 01)
										logi.info("ACTUALIZA STOCK EN LINEA PARA DEJARLO COMO ESTABA ANTES DE LA RECEPCION");
										logi.info("Estado Inventario :" +estadoInventario);
										
										if (stockLineaArt<0){
											//mailNegativo.envioMail("Procesa RECEPCION 812 :"+"NOMBRE ARCHIVO :"+ nameFile +  " "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ stockLineaArt+" STOCK AHORA : "+stockLineaArt);
										}
										exmarb.actualizaStockLinea(dto.getCodigoBodega(), dto.getCodigoArticulo(), dto.getDigArticulo().trim(), stockLineaArt);
									}
									
									//Valido si las cantidades a despachar son distintas al formato UNIDAD en EXDTRA
									exdtraDTO2.setCodEmpresa(exmrecDTO2.getCodEmpresa());
									exdtraDTO2.setNumTraspaso(exmtraDTO2.getNumTraspaso());
									exdtraDTO2.setCodArticulo(dto.getCodigoArticulo());
									if (exdtra.buscaOTCarguio(exdtraDTO2)!=null){
										
										ExdtraDTO exdtraDTO3 = exdtra.buscaOTCarguio(exdtraDTO2);
										
										logi.info("VALIDACION PARA COBRO DE TRANSPORTISTA");
										logi.info("ARTICULO : "+dto.getCodigoArticulo());
										logi.info("CANTIDAD VEDMAR MOV 04 : "+dto.getCantidadArticulo());
										logi.info("CANTIDAD DETALLE TRASPASO : "+exdtraDTO3.getCantDespachada());
										
										//Valido si se le debe cobrar al chofer
										//Si la cantidad del movimiento 04 del VEDMAR es distinto a la cantidad del Traspaso se cobrara la diferencia
										if (dto.getCantidadArticulo()!=exdtraDTO3.getCantDespachada())
										{
											cantCobroChofer2=exdtraDTO3.getCantDespachada()-dto.getCantidadArticulo();
											if (cantCobroChofer2<0){
												cantCobroChofer2 = cantCobroChofer2*(0-1);
											}
											//ExdrecDTO exdrecDTO4 = exdrec.buscaDetalle(exmrecDTO2.getCodEmpresa(), exmtraDTO2.getNumTraspaso(), fechaConfirmacion, horaConfirmacion, dto.getCodigoBodega(), dto.getCodigoArticulo());
											
											fechaVctoArt="0";
											
											//Actualizar el articulo en la tabla EXDREC
											exdrecDTO5.setCodigoEmpresa(exmrecDTO2.getCodEmpresa());
											exdrecDTO5.setNumeroOrden(exmtraDTO2.getNumTraspaso());
											exdrecDTO5.setFechaConfirmacionRecepcion(fechaConfirmacion);
											exdrecDTO5.setHoraConfirmacionRecepcion(horaConfirmacion);
											exdrecDTO5.setLinea(exdtraDTO3.getLinea());
											exdrecDTO5.setCodigoBodega(dto.getCodigoBodega());
											exdrecDTO5.setCodigoArticulo(dto.getCodigoArticulo());
											exdrecDTO5.setDvArticulo(dto.getDigArticulo().trim());
											exdrecDTO5.setDescripcionArticulo(exmartDTO.getDescripcionArticulo().trim());
											exdrecDTO5.setStockRecepcionado(cantCobroChofer2);
											exdrecDTO5.setStockSolicitado(cantCobroChofer2);
											exdrecDTO5.setFechaVencimiento(Integer.parseInt(fechaVctoArt));
											exdrecDTO5.setEstadoInventario(exmtraDTO2.getCodigoInventario());
											
											exdrecDTO5.setPrecioNeto(exmarbDTO.getPrecioBaseNeto());
											exdrecDTO5.setPrecioBruto(exmarbDTO.getPrecioBaseBruto());
											exdrecDTO5.setMontoNeto(0);
											exdrecDTO5.setMontoTotal(0);
											exdrecDTO5.setMontoBruto(0);
											exdrecDTO5.setMontoTotal(0);
											
											if (exdrec.buscaDetalle(exmrecDTO2.getCodEmpresa(), exmtraDTO2.getNumTraspaso(), fechaConfirmacion, horaConfirmacion, dto.getCodigoBodega(), dto.getCodigoArticulo())!=null)
											{	
												exdrec.actualizaDetalle(exdrecDTO5);
												logi.info("ACTUALIZA ARTICULO PARA QUE SEA COBRADO AL TRASPORTISTA");
											}
											else
											{
												exdrec.generaDetalle(exdrecDTO5);
												logi.info("INSERTA ARTICULO PARA QUE SEA COBRADO AL TRANSPORTISTA");
											}
										}	
									}
								}
								
								//Cobro al chofer
								logi.info("INICIA PROCESO DE COBRO A CHOFER");
								
								DocncpDTO chofCaserita = docncp.obtenerDatosChofer(exmrecDTO2.getCodEmpresa(), "G", exmtraDTO2.getNumTraspaso(), exmtraDTO2.getRutEmpresa(), exmtraDTO2.getDvEmpresa());
								
								// Si el chofer pertenece a Caserita solo se emite boleta sino es factura
								ChoftranDTO chof = null;

								if (chofCaserita!=null){
									 chof = choftran.obtenerDatos(chofCaserita.getRutCliente(), chofCaserita.getDigCliente());

								}
								int codDoc=0;
								if (chof!=null){
									if (chof.getRutEmpresa()==76288567 || chof.getRutEmpresa()==96509850){
										codDoc=34;
										//Al ser cobro a empresa de transporte de CASERITA la venta se genera al rut del chofer
										rutCobroVTA=chofCaserita.getRutCliente();
										dvCobroVTA=chofCaserita.getDigCliente();
										
									}else{
										codDoc=33;
										rutCobroVTA=exmtraDTO2.getRutEmpresa();
										dvCobroVTA=exmtraDTO2.getDvEmpresa();
									}
								}else{
									codDoc=33;
									rutCobroVTA=exmtraDTO2.getRutEmpresa();
									dvCobroVTA=exmtraDTO2.getDvEmpresa();
								}
								
								
								//Obtengo los datos de cabecera de EXMREC 
								exmrecDTO2.setCodEmpresa(exmtraDTO2.getCodigoEmpresa());
								exmrecDTO2.setNumeroOrden(exmtraDTO2.getNumTraspaso());
								exmrecDTO2.setRutProveedor(0);
								exmrecDTO2.setDvProveedor("");
								exmrecDTO2.setNumeroDocumento(exmtraDTO2.getNumGuiaDespacho());
								
								if (exmrec.buscaEncabezado(exmrecDTO2)!=null)
								{
									logi.info("TIENE COBRO PARA EL CHOFER");
									
									ExmrecDTO exmrecDTO3 = exmrec.buscaEncabezado(exmrecDTO2);
									
									//Busco si tengo articulos en el detalle para generar cobro al chofer
									if (exdrec.buscaDetalleCobro(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getNumTraspaso(), exmrecDTO3.getFechaConfirmacionRecepcion(), exmrecDTO3.getHoraConfirmacionRecepcion(), exmtraDTO2.getBodegaDestino())!=null){
										//Si tengo detalle busco los articulos para generar el cobro
										
										List lista = exdrec.recuperaDetalleCompletoCobro(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getNumTraspaso(), exmrecDTO3.getFechaConfirmacionRecepcion(), exmrecDTO3.getHoraConfirmacionRecepcion(), exmtraDTO2.getBodegaDestino());
										
										Iterator iter = lista.iterator();
										while (iter.hasNext())
										{
											ExdrecDTO dto = (ExdrecDTO) iter.next();
											
											if (insertVecmarCobro==0){
												
												fechaMvtoVenta=Integer.parseInt(fch.getYYYYMMDD());
												
											//	numeroDoc = tpacor.recupeCorrelativo(0, 1);
												String str = "ASYSRCD00 00008   0  01   ";
												numeroDoc = proce.obtieneCorrelativo(str);
												
												//Genera VECMAR con la Salida por el cobro al transportista
												GeneraVecmar(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, codDoc, exmtraDTO2.getBodegaDestino(), rutCobroVTA, dvCobroVTA, vecmar);
												
												String str2 = "ASYSRCD00 00008   0  01   ";
												numeroDoc2 = proce.obtieneCorrelativo(str2);
												
												//Genera VECMAR con el Ingreso por los articulos no entregados por el transportista
												GeneraVecmar(exmtraDTO2.getCodigoEmpresa(), 11, fechaMvtoVenta, numeroDoc2, codDoc, exmtraDTO2.getBodegaDestino(), rutCobroVTA, dvCobroVTA, vecmar);
												
												insertVecmarCobro=1;
											}
											
											 //Genera VECMAR
											VedmarDTO vedmarDTO2 = new VedmarDTO();
											ExmarbDTO exmarbDTO = new ExmarbDTO();
											
											StockinventarioDTO stockinventarioDTO2 = new StockinventarioDTO();
											
											//Genera VEDMAR
											ExmarbDTO exmarb2 = exmarb.recuperaArticulo(exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo());
											
											stockLineaArt = exmarb2.getStockLinea();
											
											//Genera VEDMAR con la Salida por el cobro al transportista
											GeneraVedmar(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, correlativo, exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo(), exmarb2.getDvArticulo(), dto.getStockRecepcionado(), exmarb2.getCodigoSector(), dto.getPrecioBruto(), dto.getPrecioNeto(), exmarb2.getCostoNeto(), "S", vedmar);
																					
											//Genera VEDMAR con el Ingreso por los articulos no entregados por el transportista
											GeneraVedmar(exmtraDTO2.getCodigoEmpresa(), 11, fechaMvtoVenta, numeroDoc2, correlativo, exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo(), exmarb2.getDvArticulo(), dto.getStockRecepcionado(), exmarb2.getCodigoSector(), dto.getPrecioBruto(), dto.getPrecioNeto(), exmarb2.getCostoNeto(), "I", vedmar);
											
											//Actualiza STOCK
											logi.info("REBAJA STOCK EN LINEA POR EL COBRO AL TRANSPORISTA");
											if (stockLineaArt<0){
												//mailNegativo.envioMail("Procesa RECEPCION 812 :"+"NOMBRE ARCHIVO :"+ nameFile +  " "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ stockLineaArt+" STOCK AHORA : "+stockLineaArt);
											}
											exmarb.actualizaStockLinea(exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo(), dto.getDvArticulo().trim(), exmarb2.getStockLinea()-(int)dto.getStockRecepcionado());
											
											
											//recupera nuevamente el stock en linea para actualizarlo con el ingreso por articulos no entregados
											ExmarbDTO exmarb3 = exmarb.recuperaArticulo(exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo());
											
											logi.info("AUMENTA STOCK EN LINEA POR EL ARTICULO NO ENTREGADO POR EL TRANSPORISTA");
											if (stockLineaArt<0){
												//mailNegativo.envioMail("Procesa RECEPCION 812 :"+"NOMBRE ARCHIVO :"+ nameFile +  " "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ stockLineaArt+" STOCK AHORA : "+stockLineaArt);
											}
											exmarb.actualizaStockLinea(exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo(), dto.getDvArticulo().trim(), exmarb3.getStockLinea()+(int)dto.getStockRecepcionado());
											
											logi.info("REBAJA STOCK DIFERENCIADO POR EL COBRO AL TRANSPORISTA");
											StockdifDTO stock = new StockdifDTO();
											stock.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
											stock.setCodigoBodega(exmtraDTO2.getBodegaDestino());
											stock.setCodigoArticulo(dto.getCodigoArticulo());
											stock.setDigitoArticulo(dto.getDvArticulo().trim());
											stock.setCodigoTipoVendedor(4000);  //Falta definir el tipo vendedor
											
											StockdifDTO stockDIF = stockdif.recuperaStockDiferenciado(stock);
											if (stockDIF!=null){
												if (stockDIF.getStockLinea()>(int)dto.getStockRecepcionado()){
													stock.setStockLinea(stockDIF.getStockLinea()-(int)dto.getStockRecepcionado());
												}else{
													stock.setStockLinea(0);
												}
												stockdif.actualizarStockDiferenciado(stock);
											}
											
											logi.info("REBAJA STOCK DIFERENCIADO POR EL ARTICULO NO ENTREGADO POR EL TRANSPORISTA");
											StockdifDTO stock2 = new StockdifDTO();
											stock2.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
											stock2.setCodigoBodega(exmtraDTO2.getBodegaDestino());
											stock2.setCodigoArticulo(dto.getCodigoArticulo());
											stock2.setDigitoArticulo(dto.getDvArticulo().trim());
											stock2.setCodigoTipoVendedor(4000);  //Falta definir el tipo vendedor
											
											StockdifDTO stockDIF2 = stockdif.recuperaStockDiferenciado(stock2);
											if (stockDIF2!=null){
												if (stockDIF2.getStockLinea()>(int)dto.getStockRecepcionado()){
													stock.setStockLinea(stockDIF2.getStockLinea()+(int)dto.getStockRecepcionado());
												}else{
													stock.setStockLinea(0);
												}
												stockdif.actualizarStockDiferenciado(stock2);
											}
											
											logi.info("ACTUALIZACION DE TOTALES EN VECMAR POR COBRO TRANSPORTISTA");
											//Actualiza VECMAR con la Salida por el cobro al transportista
											VecmarDTO vecmarDTO3 = vedmar.recuperaTotales(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc);
											vecmar.actualizaVecmarMerma(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, vecmarDTO3.getTotalNeto(), vecmarDTO3.getTotalDocumento());
											
											logi.info("ACTUALIZACION STOCK DE INVENTARIO COBRO TRANSPORTISTA");
											StockinventarioDTO listastock = stockinventario.lista(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getBodegaDestino(), dto.getEstadoInventario(), dto.getCodigoArticulo());
											if (listastock!=null){
												//Actualiza el stock del articulo por el tipo de stock de inventario
												stockinventarioDTO2.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
												stockinventarioDTO2.setCodigoArticulo(dto.getCodigoArticulo());
												stockinventarioDTO2.setCodigoBodega(exmtraDTO2.getBodegaDestino());
												stockinventarioDTO2.setDvArticulo(dto.getDvArticulo().trim());
												stockinventarioDTO2.setEstado(dto.getEstadoInventario());
												stockinventarioDTO2.setCantidad(listastock.getCantidad()-(int)dto.getStockRecepcionado());
												stockinventario.actualizaStock(stockinventarioDTO2);
											}
											else{
												//Inserta el articulo
												stockinventarioDTO2.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
												stockinventarioDTO2.setCodigoArticulo(dto.getCodigoArticulo());
												stockinventarioDTO2.setCodigoBodega(exmtraDTO2.getBodegaDestino());
												stockinventarioDTO2.setDvArticulo(dto.getDvArticulo().trim());
												stockinventarioDTO2.setEstado(dto.getEstadoInventario());
												stockinventarioDTO2.setCantidad((int)dto.getStockRecepcionado());
												stockinventario.creaStockInvWMS(stockinventarioDTO2);
											}
											
											logi.info("ACTUALIZACION DE TOTALES EN VECMAR CON MERCADERIA NO ENTREGADA POR COBRO TRANSPORTISTA");
											//Actualiza VECMAR con el Ingreso por la mercaderia no cobrada al transportista
											VecmarDTO vecmarDTO4 = vedmar.recuperaTotales(exmtraDTO2.getCodigoEmpresa(), 11, fechaMvtoVenta, numeroDoc2);
											vecmar.actualizaVecmarMerma(exmtraDTO2.getCodigoEmpresa(), 11, fechaMvtoVenta, numeroDoc2, vecmarDTO4.getTotalNeto(), vecmarDTO4.getTotalDocumento());
											
											logi.info("ACTUALIZACION STOCK DE INVENTARIO COBRO TRANSPORTISTA");
											StockinventarioDTO listastock2 = stockinventario.lista(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getBodegaDestino(), dto.getEstadoInventario(), dto.getCodigoArticulo());
											if (listastock2!=null){
												//Actualiza el stock del articulo por el tipo de stock de inventario
												stockinventarioDTO2.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
												stockinventarioDTO2.setCodigoArticulo(dto.getCodigoArticulo());
												stockinventarioDTO2.setCodigoBodega(exmtraDTO2.getBodegaDestino());
												stockinventarioDTO2.setDvArticulo(dto.getDvArticulo().trim());
												stockinventarioDTO2.setEstado(dto.getEstadoInventario());
												stockinventarioDTO2.setCantidad(listastock2.getCantidad()+(int)dto.getStockRecepcionado());
												stockinventario.actualizaStock(stockinventarioDTO2);
											}
											else{
												//Inserta el articulo
												stockinventarioDTO2.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
												stockinventarioDTO2.setCodigoArticulo(dto.getCodigoArticulo());
												stockinventarioDTO2.setCodigoBodega(exmtraDTO2.getBodegaDestino());
												stockinventarioDTO2.setDvArticulo(dto.getDvArticulo().trim());
												stockinventarioDTO2.setEstado(dto.getEstadoInventario());
												stockinventarioDTO2.setCantidad((int)dto.getStockRecepcionado());
												stockinventario.creaStockInvWMS(stockinventarioDTO2);
											}
											
											//Si stock de inventario de WMS es distinto a DISPONIBLE actualizo el stock el linea al capturado antes de generar el movimiento 21
											if (stockinventarioDTO2.getEstado().compareTo("D")<0){
												//Actulizar stock en Linea EXMARB (guardado antes de generar movimiento 01)
												logi.info("ACTUALIZA STOCK EN LINEA PARA DEJARLO COMO ESTABA ANTES DE LA RECEPCION");
												if (stockLineaArt<0){
													//mailNegativo.envioMail("Procesa RECEPCION 1842 :"+"NOMBRE ARCHIVO :"+ nameFile +  " "+dto.getCodigoArticulo() + " STOCK ANTERIOR :" + stockLineaArt + "STOCK A MOVER : "+ stockLineaArt+" STOCK AHORA : "+stockLineaArt);
												}
												logi.info("Estado Inventario :" +estadoInventario);
												
												exmarb.actualizaStockLinea(exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo(),dto.getDvArticulo().trim(), stockLineaArt);
											}
											
											correlativo=correlativo+1;
											
										}//Cierre While
										
										//Actualiza DATOS SE AGREGO HOY 20160701
										int cantidad = vedmar.obtieneCantidadLineas(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc);
										
										VecmarDTO dtoVecmar = vedmar.recuperaTotales(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc);
										
										VedmarDTO vedmarDTO4 = new VedmarDTO();
										
										vedmarDTO4.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
										vedmarDTO4.setCodTipoMvto(21);
										vedmarDTO4.setFechaMvto(fechaMvtoVenta);
										vedmarDTO4.setNumDocumento(numeroDoc);
										vedmarDTO4.setFechaGuiaDespacho(fechaMvtoVenta);
										
										VecmarDTO vecmarDTO3 = new VecmarDTO();
										vecmarDTO3.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
										vecmarDTO3.setCodTipoMvto(21);
										vecmarDTO3.setNumDocumento(numeroDoc);
										vecmarDTO3.setFechaMvto(fechaMvtoVenta);
										vecmarDTO3.setNumeroTipoDocumento(numeroDoc);
										vecmarDTO3.setSwichProceso(0);
										vecmarDTO3.setIndicadorDespacho("S");
										vecmarDTO3.setSwitchPagoCaja("P");
										vecmarDTO3.setFechaDocumento(fechaMvtoVenta);
										vecmarDTO3.setFechaDespacho(fechaMvtoVenta);
										vecmarDTO3.setCantidadLineaDetalle(cantidad);
										vecmarDTO3.setTotalBruto(dtoVecmar.getTotalDocumento());
										vecmarDTO3.setTotalNeto(dtoVecmar.getTotalNeto());
										vecmarDTO3.setTotalDocumento(dtoVecmar.getTotalDocumento());
										if (vecmarDTO3.getCodigoDocumento()==3){
											vecmarDTO3.setCodigoDocumento(33);
										}else if (vecmarDTO3.getCodigoDocumento()==4){
											vecmarDTO3.setCodigoDocumento(34);
										}
										vecmar.actualizaDatosVecmar(vecmarDTO3);
										
										ClmcliDTO clmcliDTO = clmcli.recuperaCliente(rutCobroVTA, dvCobroVTA);
										//vecmarDTO3.setRazonSocialCliente(clmcliDTO.getRazonsocial().trim());
										//vecmarDTO3.setCodigoEmpresa(exmtraDTO2.getCodigoEmpresa());
										
										//Busco datos del VECMAR
										VecmarDTO vecmarDTO4 = vecmar.obtenerDatosVecmar(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, clmcliDTO);
										
										String sub = formaString(vecmarDTO4, clmcliDTO.getRazonsocial());
										logi.info("PROCESA FACTURACION:"+sub);
										proce.procesaCalculoProcedure(sub);
										
										//****************
										
										//PROCESA SERVLET FACTURACION
										StringBuffer tmp = new StringBuffer(); 
								        String texto = new String();
								        
										try { 
								            // Crea la URL con del sitio introducido, ej: http://google.com 
								            URL url = new URL(rutservDTO.getEndPoint()+"?empresa="+String.valueOf(exmtraDTO2.getCodigoEmpresa())+
								            		"&codTipo="+String.valueOf(21)+
								            				"&fch="+String.valueOf(fechaMvtoVenta)+"&num="+
								            		String.valueOf(numeroDoc)+
								            				"&cod="+String.valueOf(codDoc)+"&rut="+
								            String.valueOf(rutCobroVTA)+"&dv="+dvCobroVTA+"&usuario=CAJABD26&tipo=1&nota=0"); 
								            logi.info("URL SERVLET FACTURACION:"+url.toString());
								            // Lector para la respuesta del servidor 
								            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
								            String str; 
								            while ((str = in.readLine()) != null) { 
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
										
										//vecmar.actualizaDisponibilidadImpresion(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento(), confirma.getAnden().trim());
										vecmar.actualizaDisponibilidadImpresion(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, andenRecepcion);
										
										//vecmar.actualizaDisponibilidadImpresion(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, andenRecepcion);
										vecmar.actualizaDatosVecmar(vecmarDTO3);

									}else{
										//Mueve Stock en Linea para bodega destino
										List exdtraList = exdtra.recuperaDetalle(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getNumTraspaso());
										ExmtraDTO exmtradto = exmtra.recuperaEncabezado(exmtraDTO2.getCodigoEmpresa(), exmtraDTO2.getNumTraspaso());
										Iterator iter = exdtraList.iterator();
										while (iter.hasNext()){
											ExdtraDTO exdtraDTO = (ExdtraDTO) iter.next();
											ExmarbDTO exmarb3 = exmarb.recuperaArticulo(exmtradto.getBodegaDestino(), exdtraDTO.getCodArticulo());

											//exmarb.actualizaStockLinea(exmtradto.getBodegaDestino(), exdtraDTO.getCodArticulo(), exdtraDTO.getDigitoVerificador().trim(), exmarb3.getStockLinea()+exdtraDTO.getCantRecibida());
											
											
											
										}
									}										
								}
							}
							//Genera Movimiento de XML Procesado
							exmfwmsDTO.setCodigoEmpresa(exmrecDTO2.getCodEmpresa());
							exmfwmsDTO.setNumeroOrdenCompra(exmrecDTO2.getNumeroOrden());
							exmfwmsDTO.setNombreArchivoXML(nameFile);
							exmfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
							exmfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
							exmfwms.generaArchivoXML(exmfwmsDTO);
						}
					}//Fin del CARCOD OT
					
				} //FOR
				
			}
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			logi.info("ERROR");
			e.printStackTrace();
			email.mail(e.getMessage());
			
		}
		
		
	}
	
	public int GeneraVecmar(int codEmpresa, int codmovto, int fechaMvtoVenta, int numerodocto, int coddocto, int codbodega, int rutCobroVTA, String dvCobroVTA, VecmarDAO vecmar2 ){
		int resul=0;
		
		VecmarDTO vecmarDTO2 = new VecmarDTO();
		
		try{
			
			vecmarDTO2.setCodigoEmpresa(codEmpresa);
			vecmarDTO2.setCodTipoMvto(codmovto);
			vecmarDTO2.setFechaMvto(fechaMvtoVenta);
			vecmarDTO2.setNumDocumento(numerodocto);
			vecmarDTO2.setCodigoDocumento(coddocto);
			vecmarDTO2.setFechaDocumento(fechaMvtoVenta);
			vecmarDTO2.setBodegaOrigen(codbodega);
			vecmarDTO2.setFormaPago("1");
			vecmarDTO2.setCantidadLineaDetalle(1);
			vecmarDTO2.setTotalBruto(0);
			vecmarDTO2.setTotalNeto(0);
			vecmarDTO2.setTotalImptoAdicional(0);
			vecmarDTO2.setTotalIva(0);
			vecmarDTO2.setTotalDocumento(0);
			
			vecmarDTO2.setRutProveedor(String.valueOf(rutCobroVTA));
			vecmarDTO2.setDvProveedor(dvCobroVTA);
			
			//Vendedor en duro hasta saber que digan lo contrario
			vecmarDTO2.setCodigoVendedor(274);
			vecmarDTO2.setSwitchDescto(0);
			vecmarDTO2.setSwichProceso(0);
			vecmarDTO2.setIndicadorDespacho("N");
			vecmarDTO2.setSwitchPagoCaja("S");
			vecmarDTO2.setCodigoTipoVendedor(4000);
			
			vecmar2.generaMovimientoCobro(vecmarDTO2);
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		return resul;
	}
	
	public int GeneraVedmar(int codempresa, int codmovto, int fechaMvtoVenta, int numerodocto, int correlativo, int codbodega, int codarticulo, String dvarticulo, double cantidad, int codigosector, double precioBruto, double precioNeto, double costoNeto, String IngresoSalida, VedmarDAO vedmar2){
		int resul=0;
		
		VedmarDTO vedmarDTO2 = new VedmarDTO();
		
		try{
			
			vedmarDTO2.setCodigoEmpresa(codempresa);
			vedmarDTO2.setCodTipoMvto(codmovto);
			vedmarDTO2.setFechaMvto(fechaMvtoVenta);
			vedmarDTO2.setNumDocumento(numerodocto);
			
			vedmarDTO2.setCorrelativo(correlativo);
			vedmarDTO2.setCodigoBodega(codbodega);
			vedmarDTO2.setCodigoArticulo(codarticulo);
			vedmarDTO2.setDigArticulo(dvarticulo);
			vedmarDTO2.setFormato("U");
			vedmarDTO2.setVolumenArticulo(0);
			vedmarDTO2.setPesoLinea(0);
			
			vedmarDTO2.setCantidadArticulo((int)cantidad);
			vedmarDTO2.setCantidadFormato((int)cantidad);
			vedmarDTO2.setSectorBodega(codigosector);
			vedmarDTO2.setPrecioUnidad(precioBruto);
			vedmarDTO2.setPrecioNeto(precioNeto);
			vedmarDTO2.setCostoNeto(costoNeto);
			vedmarDTO2.setVolumenArticulo(0);
			vedmarDTO2.setMontoFlete(0);
			vedmarDTO2.setCostoTotalNeto(costoNeto*(int)cantidad);
			vedmarDTO2.setMontoBrutoLinea(precioBruto*cantidad);
			vedmarDTO2.setMontoNeto(precioNeto*cantidad);
			vedmarDTO2.setPorcentajeDesto(0);
			vedmarDTO2.setMontoDescuentoLinea(0);
			vedmarDTO2.setMontoDescuentoNeto(0);
			vedmarDTO2.setMontoTotalLinea((int)precioBruto*(int)cantidad);
			double totNeto = precioNeto*(int)cantidad;
			//vedmarDTO2.setMontoTotalNetoLinea((int)precioNeto*(int)cantidad);
			vedmarDTO2.setMontoTotalNetoLinea((int)totNeto);
			//Verificar EXENTO
			vedmarDTO2.setMontoExento(0);
			vedmarDTO2.setCodIngresoSalida(IngresoSalida);
			vedmarDTO2.setSwitchProceso(0);
			
			vedmar2.generaMovimiento(vedmarDTO2);
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		return resul;
	}
	
	
	public String formaString(VecmarDTO vecmar, String razonSocial){
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
		paso = agregaBlancoRazon(String.valueOf(razonSocial).length()-40,String.valueOf(razonSocial));//nombre del cliente
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
		
		logi.info(""+facturacion);
		
		return facturacion;
	}

	
	public String formaStringConvaf(int empresa, int codDocto, int numDocto, int fechaDocto, int rutProv, String dvProv, int nroOrden){
		String facturacion="";
		String nombre ="ACONVAF00 000";
		
		//1 CODIGO DOCUMENTO(4)
		String paso = agregaBlanco(String.valueOf(codDocto).length()-4,String.valueOf(codDocto));
		facturacion = paso;
		
		//2 NUMERO DOCUMENTO(10)
		paso = agregaBlanco(String.valueOf(numDocto).length()-10,String.valueOf(numDocto));
		facturacion = facturacion+paso;
		
		//3 FECHA DOCUMENTO(8)
		paso = agregaBlanco(String.valueOf(fechaDocto).length()-8,String.valueOf(fechaDocto));
		facturacion = facturacion+paso;
		
		//4 RUT PROVEEDOR(11)
		paso = agregaBlanco(String.valueOf(rutProv).length()-11,String.valueOf(rutProv));
		facturacion = facturacion+paso;
		
		//5 DV PROVEEDOR(1)
		paso = agregaBlanco(String.valueOf(dvProv).length()-1,String.valueOf(dvProv));
		facturacion = facturacion+paso;
		
		//6 FECHA RECEPCION DOCUMENTO(10)
		paso = agregaBlanco(String.valueOf("").length()-10,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//7 MONTO NETO GUIA(8)
		paso = agregaBlanco(String.valueOf("").length()-8,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//8 NUMERO FACTURA ASIGNADA A LA GUIA(4)
		paso = agregaBlanco(String.valueOf("").length()-4,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//9 NUMERO GUIA(4)
		paso = agregaBlanco(String.valueOf("").length()-4,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//10 NRO ORDEN COMPRA(7) 
		paso = agregaBlanco(String.valueOf(nroOrden).length()-7,String.valueOf(nroOrden));
		facturacion = facturacion+paso;
		
		//11 NUMERO DOCUMENTO(10)
		paso = agregaBlanco(String.valueOf(numDocto).length()-10,String.valueOf(numDocto));
		facturacion = facturacion+paso;
		
		//12 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(empresa).length()-3,String.valueOf(empresa));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringVecmar(int codmvto, int codEmpresa, int nroDocto, int codbodega, int codbodegadest, int correlativoOC, int codDocto, int fechaDocto, int totalBruto, int totalNeto, int nroOrden, int formaPago, int cantLineas, int rutProv, String dvProv, double dPesoRecep, double dVoluRecep){
		String facturacion="";
		String nombre ="AEXMREC00 00";
		Fecha fch = new Fecha();
		
		int fechaSYS=0;
		String PesoRecep="";
		String VoluRecep="";
		String ssTotalBruto="";
		String ssTotalNeto="";
		
		fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
		
		DecimalFormat formateadorVecmar = new DecimalFormat("###,###.00");
		
		//Forma String VECMAR 50 Parametros
		//1 CODIGO MOVIMIENTO(4)
		String paso = agregaBlanco(String.valueOf(codmvto).length()-4,String.valueOf(codmvto));
		facturacion = paso;
		
		//2 FECHA SISTEMA(8)
		paso = agregaBlanco(String.valueOf(fechaSYS).length()-8,String.valueOf(fechaSYS));
		facturacion = facturacion+paso;
		
		//3 NRO INTERNO(10)
		paso = agregaBlanco(String.valueOf(correlativoOC).length()-10,String.valueOf(correlativoOC));
		facturacion = facturacion+paso;
		
		//4 COD TIPO DOCUMENTO(4)
		paso = agregaBlanco(String.valueOf(codDocto).length()-4,String.valueOf(codDocto));
		facturacion = facturacion+paso;
		
		//5 NUMERO TIPO DOCUMENTO(10)
		paso = agregaBlanco(String.valueOf(nroDocto).length()-10,String.valueOf(nroDocto));
		facturacion = facturacion+paso;
		
		//6 FECHA DOCUMENTO(8)
		paso = agregaBlanco(String.valueOf(fechaDocto).length()-8,String.valueOf(fechaDocto));
		facturacion = facturacion+paso;
		
		//7 NUMERO ORDEN DE COMPRA(10)
		paso = agregaBlanco(String.valueOf(nroOrden).length()-10,String.valueOf(nroOrden));
		facturacion = facturacion+paso;
		
		//8 CODIGO BODEGA ORIGEN(4)
		paso = agregaBlanco(String.valueOf(codbodega).length()-4,String.valueOf(codbodega));
		facturacion = facturacion+paso;
		
		//9 CODIGO BODEGA DESTINO(4)
		paso = agregaBlanco(String.valueOf(codbodegadest).length()-4,String.valueOf(codbodegadest));
		facturacion = facturacion+paso;
		
		//10 FORMA PAGO(1)
		paso = agregaBlanco(String.valueOf(formaPago).length()-1,String.valueOf(formaPago));
		facturacion = facturacion+paso;
		
		//11 CANTIDAD DE LINEAS(3)
		paso = agregaBlanco(String.valueOf(cantLineas).length()-3,String.valueOf(cantLineas));
		facturacion = facturacion+paso;
		
		//12 TOTAL BRUTO(9)
		//ssTotalBruto=formateadorVecmar.format(totalBruto);
		//ssTotalBruto=ssTotalBruto.replace(",", "");
		//ssTotalBruto=ssTotalBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(totalBruto).length()-9,String.valueOf(totalBruto));
		facturacion = facturacion+paso;
		
		//13 % DESCUENTO(5)
		paso = agregaBlanco(String.valueOf("0").length()-5,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//14 TOTAL DESCUENTO(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//15 TOTAL NETO(9)
		//ssTotalNeto=formateadorVecmar.format(totalNeto);
		//ssTotalNeto=ssTotalNeto.replace(",", "");
		//ssTotalNeto=ssTotalNeto.replace(".", "");
		paso = agregaBlanco(String.valueOf(totalNeto).length()-9,String.valueOf(totalNeto));
		facturacion = facturacion+paso;
		
		//16 TOTAL IMPUESTOS(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//17 TOTAL IVA(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//18 TOTAL DOCUMENTO(9)
		//ssTotalBruto=formateadorVecmar.format(totalBruto);
		//ssTotalBruto=ssTotalBruto.replace(",", "");
		//ssTotalBruto=ssTotalBruto.replace(".", "");
		paso = agregaBlanco(String.valueOf(totalBruto).length()-9,String.valueOf(totalBruto));
		facturacion = facturacion+paso;
		
		//19 PESO TOTAL MOVIMIENTO(9)
		dPesoRecep=dPesoRecep/1000;
		PesoRecep=formateadorVecmar.format(dPesoRecep);
		PesoRecep=PesoRecep.replace(",", "");
		PesoRecep=PesoRecep.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(PesoRecep).length()-9,String.valueOf(PesoRecep));
		facturacion = facturacion+paso;
		
		//20 VOLUMEN TOTAL(9)
		dVoluRecep=dVoluRecep/1000;
		VoluRecep=formateadorVecmar.format(dVoluRecep);
		VoluRecep=VoluRecep.replace(",", "");
		VoluRecep=VoluRecep.replace(".", "");
		
		if (VoluRecep.length()>6){
			VoluRecep=VoluRecep.substring(0, 7);
		}
		
		paso = agregaBlanco(String.valueOf(VoluRecep).length()-9,String.valueOf(VoluRecep));
		facturacion = facturacion+paso;
		
		//21 RUT PROVEEDOR(11)
		paso = agregaBlanco(String.valueOf(rutProv).length()-11,String.valueOf(rutProv));
		facturacion = facturacion+paso;
		
		//22 DV PROVEEDOR(1)
		paso = agregaBlanco(String.valueOf(dvProv).length()-1,String.valueOf(dvProv));
		facturacion = facturacion+paso;
		
		//23 CODIGO VENDEDOR(4)
		paso = agregaBlanco(String.valueOf("").length()-4,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//24 CODIGO JEFE LOCAL(4)
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//ESTOS PARAMETROS VAN EN BLANCO
		//25 SWITCH DESCUENTO(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//26 SWITCH PROCESO(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//27 INDICADOR DE DESPACHO(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//28 DIRECCION DESPACHO(40)
		paso = agregaBlanco(String.valueOf("").length()-40,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//29 CONTACTO DESPACHO(30)
		paso = agregaBlanco(String.valueOf("").length()-30,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//30 FECHA DESPACHO(8)
		paso = agregaBlanco(String.valueOf("0").length()-8,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//31 SWITCH PAGO CAJA(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//32 FECHA DESPACHO REAL(8)
		paso = agregaBlanco(String.valueOf("0").length()-8,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//33 DESPACHADO SI/NO(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//34 CODIGO REGION(2)
		paso = agregaBlanco(String.valueOf("0").length()-2,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//35 CODIGO CIUDAD(3)
		paso = agregaBlanco(String.valueOf("0").length()-3,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//36 CODIGO COMUNA(3)
		paso = agregaBlanco(String.valueOf("0").length()-3,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//37 CODIGO TIPO VENDEDOR(4)
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//38 CODIGO BODEGA(4)
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//39 CODIGO JAULA(4)
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//40 RUT EMPRESA TRANSPORTE(11)
		paso = agregaBlanco(String.valueOf("0").length()-11,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//41 DV EMPRESA TRANSPORTE(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//42 PATENTE TRANSPORTE(10)
		paso = agregaBlanco(String.valueOf("").length()-10,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//43 FECHA TRASPASO(8)
		paso = agregaBlanco(String.valueOf("0").length()-8,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//44 ESTADO INGRESO/SALIDA(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//45 FECHA INGRESO/SALIDA(8)
		paso = agregaBlanco(String.valueOf("0").length()-8,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//46 HORA INGRESO/SALIDA(6)
		paso = agregaBlanco(String.valueOf("0").length()-6,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//47 USUARIO INGRESO/SALIDA(10)
		paso = agregaBlanco(String.valueOf("").length()-10,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//48 CODIGO INGRESO/SALIDA(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//49 FACTURA BOLETA EN DEVOLUCION(1)
		paso = agregaBlanco(String.valueOf("0").length()-1,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//50 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codEmpresa).length()-3,String.valueOf(codEmpresa));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringExdodc(ExdrecDTO exdrec, String estadoCabeceraOC, ExdodcDTO exdodc, double stockPedidoOC, double stockPendienteOC, double stockRecepcionadoOC, double ddstockRecep){
		String facturacion="";
		String nombre ="AEXDODC00 00";
		Fecha fch = new Fecha();
		
		String stockActual="";
		String stockPedido="";
		String stockRecep="";
		String precioBruto="";
		String stockPendiente="";
		double dstockPedido=0;
		double dstockRecep=0;
		double dstockActual=0;
		double dprecioBruto=0;
		double dstockPendiente=0;
		
		DecimalFormat formateadorExdodc = new DecimalFormat("###,###.00");
		
		//Forma String EXDODC (Actualizacion de articulos) 20 Parametros
		
		//1 NRO OC(7)
		String paso = agregaBlanco(String.valueOf(exdrec.getNumeroOrden()).length()-7,String.valueOf(exdrec.getNumeroOrden()));
		facturacion = paso;
		
		//2 NRO LINEA(3)
		paso = agregaBlanco(String.valueOf(exdrec.getLinea()).length()-3,String.valueOf(exdrec.getLinea()));
		facturacion = facturacion+paso;
		
		//3 STOCK ACTUAL(9)
		dstockActual=exdodc.getStockActual();
		stockActual=formateadorExdodc.format(dstockActual);
		stockActual=stockActual.replace(",", "");
		stockActual=stockActual.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(stockActual).length()-9,String.valueOf(stockActual));
		facturacion = facturacion+paso;
		
		//4 STOCK PEDIDO(9)
		//dstockPedido=exdrec.getStockSolicitado();
		stockPedido=formateadorExdodc.format(stockPedidoOC);
		stockPedido=stockPedido.replace(",", "");
		stockPedido=stockPedido.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(stockPedido).length()-9,String.valueOf(stockPedido));
		facturacion = facturacion+paso;
		
		//5 STOCK REPECIONADO(9)
		dstockRecep=ddstockRecep+stockRecepcionadoOC;
		stockRecep=formateadorExdodc.format(dstockRecep);
		stockRecep=stockRecep.replace(",", "");
		stockRecep=stockRecep.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(stockRecep).length()-9,String.valueOf(stockRecep));
		facturacion = facturacion+paso;
		
		//Stock Pendiente exdrec.getStockRecepcionado()
		dstockPendiente = stockPendienteOC - ddstockRecep;
		stockPendiente=formateadorExdodc.format(dstockPendiente);
		stockPendiente=stockPendiente.replace(",", "");
		stockPendiente=stockPendiente.replace(".", "");
		
		//6 STOCK DEVUELTO(9)
		paso = agregaBlanco(String.valueOf("").length()-9,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//7 FORMATO(1)
		paso = agregaBlanco(String.valueOf("U").length()-1,String.valueOf("U"));
		facturacion = facturacion+paso;
		
		//8 PRECIO(9)
		dprecioBruto=exdrec.getPrecioBruto();
		precioBruto=formateadorExdodc.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		//9 TOTAL(9)
		paso = agregaBlanco(String.valueOf((int)exdrec.getMontoBruto()).length()-9,String.valueOf((int)exdrec.getMontoBruto()));
		facturacion = facturacion+paso;
		
		//10 DESCTO(5)
		paso = agregaBlanco(String.valueOf("").length()-5,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//11 FECHA MAXIMA ENTREGA(8)
		paso = agregaBlanco(String.valueOf(exdodc.getFechaMaxEntrega()).length()-8,String.valueOf(exdodc.getFechaMaxEntrega()));
		facturacion = facturacion+paso;
		
		//12 ESTADO(1)
		logi.info("Estado:"+estadoCabeceraOC);
		paso = agregaBlanco(String.valueOf(estadoCabeceraOC).length()-1,String.valueOf(estadoCabeceraOC));
		facturacion = facturacion+paso;
		
		//13 SWITCH(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//14 CODIGO ARTICULO(7)
		paso = agregaBlanco(String.valueOf(exdrec.getCodigoArticulo()).length()-7,String.valueOf(exdrec.getCodigoArticulo()));
		facturacion = facturacion+paso;
		
		//15 DV ARTICULO(1)
		paso = agregaBlanco(String.valueOf(exdrec.getDvArticulo()).length()-1,String.valueOf(exdrec.getDvArticulo()));
		facturacion = facturacion+paso;
		
		//16 BODEGA(4)
		paso = agregaBlanco(String.valueOf(exdrec.getCodigoBodega()).length()-4,String.valueOf(exdrec.getCodigoBodega()));
		facturacion = facturacion+paso;
		
		//17 JEFE LOCAL(4)
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//18 STOCK PENDIENTE(9)
		paso = agregaBlanco(String.valueOf(stockPendiente).length()-9,String.valueOf(stockPendiente));
		facturacion = facturacion+paso;
		
		//19 FECHA VENCIMIENTO(8)
		paso = agregaBlanco(String.valueOf(exdrec.getFechaVencimiento()).length()-8,String.valueOf(exdrec.getFechaVencimiento()));
		facturacion = facturacion+paso;
		
		//20 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(exdrec.getCodigoEmpresa()).length()-3,String.valueOf(exdrec.getCodigoEmpresa()));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	
	public String formaStringExmarb(ExdrecDTO exdrec, ExmodcDTO exmodc, ExmarbDAO exmarb, ExdodcDAO exdodc, VarcostDAO varcost, ExtariDAO extari){
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String facturacion="";
		String nombre ="GEXMARB00 00";
		Fecha fch = new Fecha();
		
		double CostoArt=0;
		double CostoCompra=0;
		double CostoMayor=0;
		double CostoPromedio=0;
		double CostoMenor=0;
		double CostoAnterior=0;
		double StockComputacional=0;
		double dCostoBruto=0;
		double dCostoNew=0;
		double dDiferenCompr=0;
		double dDiferen=0;
		int fechasys=0;
		int horasys=0;
		int iCostoMayor=0;
		int iCostoPromedio=0;
		int iCostoMenor=0;
		int iCostoAnterior=0;
		int iCostoArt=0;
		int iIngresoCompra=0;
		int iIngresoTraspaso=0;
		int iIngresoVarios=0;
		int iSalidaCompra=0;
		int iSalidaTraspaso=0;
		int iSalidaVarios=0;
		int iVariacion=0;
		int iPrecioBaseBruto=0;
		int iStockMinimo=0;
		int iStockMaxino=0;
		
		DecimalFormat formateadorExmarb2 = new DecimalFormat("###,###.00");
		
		String ssStockCritico="";
		String ssStockLinea="";
		String ssStockComputacional="";
		String ssStockPendiente="";
		String ssCostoTraspaso="";
		String ssMargenUtilidad="";
		String ssVariacion="";
		String ssCostoMayor="";
		String ssCostoPromedio="";
		String ssCostoMenor="";
		String ssCostoAnterior="";
		String ssPrecioBase="";
		String ssStockMinimo="";
		String ssStockMaximo="";
		String ssIngresoCompra="";
		String ssIngresoTraspaso="";
		String ssIngresoVarios="";
		String ssSalidaCompra="";
		String ssSalidaTraspaso="";
		String ssSalidaVarios="";
		
		VarcostDTO varcostDTO = new VarcostDTO();
		
		fechasys = Integer.parseInt(fch.getYYYYMMDD());
		horasys = Integer.parseInt(fch.getHHMMSS());
		
		//Busco datos del articulo en la bodega
		ExmarbDTO exmarbDTO = exmarb.obtieneCostosArticulo(exdrec.getCodigoBodega(), exdrec.getCodigoArticulo());
		
		if (exmarbDTO!=null)
		{
			//Busco datos del articulo en el detalle de la OC
			ExdodcDTO exdodcDTO = exdodc.buscaDetalleOrden(exdrec.getCodigoEmpresa(), exdrec.getNumeroOrden(), exdrec.getCodigoArticulo());
			
			CostoCompra=exdodcDTO.getPrecioNeto();
			CostoArt=exmarbDTO.getCostoBruto();
			CostoMayor=exmarbDTO.getCostoMayor();		
			
			CostoMenor=exmarbDTO.getCostoMenor();
			CostoAnterior=exmarbDTO.getCostoAnterior();
			CostoPromedio=exmarbDTO.getCostoPromedio();
			StockComputacional=exmarbDTO.getStockComputacional();
			
			if (CostoCompra!=CostoArt)
			{
				if (CostoMayor==0){
					CostoMayor=CostoArt;
				}
				if (CostoMenor==0){
					CostoMenor=CostoArt;
				}
				if (CostoAnterior==0){
					CostoAnterior=CostoArt;
				}
				if (CostoPromedio==0){
					CostoPromedio=CostoArt;
				}
				if (CostoCompra!=CostoArt){
					CostoAnterior=CostoArt;
					CostoArt=CostoCompra;
					if (CostoCompra>CostoMayor){
						CostoMayor=CostoCompra;
					}
					if (CostoCompra<CostoMenor){
						CostoMenor=CostoCompra;
					}
				}
				
				CostoPromedio = (CostoMayor+CostoMenor)/2;
				
				dCostoBruto=CostoArt;
				
				//Guarda datos de diferencia de costos en tabla VARCOST
				if (StockComputacional>0 && dCostoBruto!=CostoAnterior)
				{	
					//Calculo Diferencia
					dCostoNew = dCostoBruto;
					dDiferenCompr = dCostoNew-CostoAnterior;
					dDiferen= (dCostoNew-CostoAnterior)*StockComputacional;
					
					DecimalFormat dDiferenCompr2 = new DecimalFormat("#.##");
					DecimalFormat dDiferen2 = new DecimalFormat("#.##");
					
					String sDiferenCompr="";
					String sDiferen="";
					
					sDiferenCompr = dDiferenCompr2.format(dDiferenCompr);
					sDiferen = dDiferen2.format(dDiferen);
					
					sDiferenCompr=sDiferenCompr.replace(",", ".");
					sDiferen=sDiferen.replace(",", ".");
					
					//Inserta VARCOST
					varcostDTO.setCodBodega(exdrec.getCodigoBodega());
					varcostDTO.setCodArticulo(exdrec.getCodigoArticulo());
					varcostDTO.setDvArticulo(exdrec.getDvArticulo());
					varcostDTO.setFechaMov(fechasys);
					varcostDTO.setHoraMov(horasys);
					varcostDTO.setStockCompu((int)StockComputacional);
					varcostDTO.setCostoAnterior(CostoAnterior);
					varcostDTO.setCostoNuevo(dCostoNew);
					varcostDTO.setDifCompra(Double.parseDouble(sDiferenCompr));
					varcostDTO.setDiferencia(Double.parseDouble(sDiferen));
					
					varcost.insertaVariacionCostos(varcostDTO);
					
				}
					
				//Forma String EXMARB (Actualizacion de Costos) 44 Parametros
				iCostoMayor=(int)CostoMayor;
				iCostoPromedio=(int)CostoPromedio;
				iCostoMenor=(int)CostoMenor;
				iCostoAnterior=(int)CostoAnterior;
				iCostoArt=(int)CostoArt;
				
				iIngresoCompra=(int)exmarbDTO.getIngresoCompra();
				iIngresoTraspaso=(int)exmarbDTO.getIngresoTraspaso();
				iIngresoVarios=(int)exmarbDTO.getIngresoVarios();
				iSalidaCompra=(int)exmarbDTO.getSalidaCompra();
				iSalidaTraspaso=(int)exmarbDTO.getSalidaTraspaso();
				iSalidaVarios=(int)exmarbDTO.getSalidaVarios();
				iVariacion=(int)exmarbDTO.getVariacion();
				iPrecioBaseBruto=(int)exmarbDTO.getPrecioBaseBruto();
				iStockMinimo=(int)exmarbDTO.getStockMinimo();
				iStockMaxino=(int)exmarbDTO.getStockMaximo();
				
				//1 BODEGA
				String paso = agregaBlanco(String.valueOf(exdrec.getCodigoBodega()).length()-4,String.valueOf(exdrec.getCodigoBodega()));
				facturacion = paso;
				
				//2 CODIGO ARTICULO
				paso = agregaBlanco(String.valueOf(exdrec.getCodigoArticulo()).length()-7,String.valueOf(exdrec.getCodigoArticulo()));
				facturacion = facturacion+paso;
				
				//3 DV ARTICULO
				paso = agregaBlanco(String.valueOf(exdrec.getDvArticulo()).length()-1,String.valueOf(exdrec.getDvArticulo()));
				facturacion = facturacion+paso;
				
				//4 STOCK CRITICO(9)
				ssStockCritico=formateadorExmarb2.format(exmarbDTO.getStockCritico());
				ssStockCritico=ssStockCritico.replace(",", "");
				ssStockCritico=ssStockCritico.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssStockCritico).length()-9,String.valueOf(ssStockCritico));
				facturacion = facturacion+paso;
				
				//5 STOCK LINEA(9)
				ssStockLinea=formateadorExmarb2.format(exmarbDTO.getStockLinea());
				ssStockLinea=ssStockLinea.replace(",", "");
				ssStockLinea=ssStockLinea.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssStockLinea).length()-9,String.valueOf(ssStockLinea));
				facturacion = facturacion+paso;
				
				//6 STOCK COMPUTACIONAL(9)
				ssStockComputacional=formateadorExmarb2.format(exmarbDTO.getStockComputacional());
				ssStockComputacional=ssStockComputacional.replace(",", "");
				ssStockComputacional=ssStockComputacional.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssStockComputacional).length()-9,String.valueOf(ssStockComputacional));
				facturacion = facturacion+paso;
				
				//7 STOCK PENDIENTE(9)
				ssStockPendiente=formateadorExmarb2.format(exmarbDTO.getStockPendiente());
				ssStockPendiente=ssStockPendiente.replace(",", "");
				ssStockPendiente=ssStockPendiente.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssStockPendiente).length()-9,String.valueOf(ssStockPendiente));
				facturacion = facturacion+paso;
				
				//8 FECHA CREACION(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaCreacion()).length()-8,String.valueOf(exmarbDTO.getFechaCreacion()));
				facturacion = facturacion+paso;
				
				//9 COSTO(9)
				ssCostoTraspaso=formateadorExmarb2.format(CostoArt);
				ssCostoTraspaso=ssCostoTraspaso.replace(",", "");
				ssCostoTraspaso=ssCostoTraspaso.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssCostoTraspaso).length()-9,String.valueOf(ssCostoTraspaso));
				facturacion = facturacion+paso;
				
				//10 MARGEN UTILIDAD(6)
				ssMargenUtilidad=formateadorExmarb2.format(exmarbDTO.getMargenUtil());
				ssMargenUtilidad=ssMargenUtilidad.replace(",", "");
				ssMargenUtilidad=ssMargenUtilidad.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssMargenUtilidad).length()-6,String.valueOf(ssMargenUtilidad));
				facturacion = facturacion+paso;
				
				//11 VARIACION LOCAL(5)
				ssVariacion=formateadorExmarb2.format(iVariacion);
				ssVariacion=ssVariacion.replace(",", "");
				ssVariacion=ssVariacion.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssVariacion).length()-5,String.valueOf(ssVariacion));
				facturacion = facturacion+paso;
				
				//12 ESTADO(1)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getEstado()).length()-1,String.valueOf(exmarbDTO.getEstado()));
				facturacion = facturacion+paso;
				
				//13 COMPRAR ARTICULO(1)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getCompraArt()).length()-1,String.valueOf(exmarbDTO.getCompraArt()));
				facturacion = facturacion+paso;
				
				//14 FECHA ULTIMA COMPRA(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaUltCompra()).length()-8,String.valueOf(exmarbDTO.getFechaUltCompra()));
				facturacion = facturacion+paso;
				
				//15 COSTO MAYOR(9)
				ssCostoMayor=formateadorExmarb2.format(CostoMayor);
				ssCostoMayor=ssCostoMayor.replace(",", "");
				ssCostoMayor=ssCostoMayor.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssCostoMayor).length()-9,String.valueOf(ssCostoMayor));
				facturacion = facturacion+paso;
				
				//16 COSTO PROMEDIO(9)
				ssCostoPromedio=formateadorExmarb2.format(CostoPromedio);
				ssCostoPromedio=ssCostoPromedio.replace(",", "");
				ssCostoPromedio=ssCostoPromedio.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssCostoPromedio).length()-9,String.valueOf(ssCostoPromedio));
				facturacion = facturacion+paso;
				
				//17 COSTO MENOR(9)
				ssCostoMenor=formateadorExmarb2.format(CostoMenor);
				ssCostoMenor=ssCostoMenor.replace(",", "");
				ssCostoMenor=ssCostoMenor.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssCostoMenor).length()-9,String.valueOf(ssCostoMenor));
				facturacion = facturacion+paso;
				
				//18 COSTO ANTERIOR(9)
				ssCostoAnterior=formateadorExmarb2.format(CostoAnterior);
				ssCostoAnterior=ssCostoAnterior.replace(",", "");
				ssCostoAnterior=ssCostoAnterior.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssCostoAnterior).length()-9,String.valueOf(ssCostoAnterior));
				facturacion = facturacion+paso;
				
				//19 RUT PROVEEDOR(11)
				paso = agregaBlanco(String.valueOf(exmodc.getRutProveedor()).length()-11,String.valueOf(exmodc.getRutProveedor()));
				facturacion = facturacion+paso;
				
				//20 DV PROVEEDOR(1)
				paso = agregaBlanco(String.valueOf(exmodc.getDigito()).length()-1,String.valueOf(exmodc.getDigito()));
				facturacion = facturacion+paso;
				
				//21 PRECIO BASE(9)
				ssPrecioBase=formateadorExmarb2.format(iPrecioBaseBruto);
				ssPrecioBase=ssPrecioBase.replace(",", "");
				ssPrecioBase=ssPrecioBase.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssPrecioBase).length()-9,String.valueOf(ssPrecioBase));
				facturacion = facturacion+paso;
				
				//22 STOCK MINIMO(9)
				ssStockMinimo=formateadorExmarb2.format(iStockMinimo);
				ssStockMinimo=ssStockMinimo.replace(",", "");
				ssStockMinimo=ssStockMinimo.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssStockMinimo).length()-9,String.valueOf(ssStockMinimo));
				facturacion = facturacion+paso;
				
				//23 STOCK MAXIMO(9)
				ssStockMaximo=formateadorExmarb2.format(iStockMaxino);
				ssStockMaximo=ssStockMaximo.replace(",", "");
				ssStockMaximo=ssStockMaximo.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssStockMaximo).length()-9,String.valueOf(ssStockMaximo));
				facturacion = facturacion+paso;
				
				//24 FECHA ULTIMA ENTRADA(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaUltEntrada()).length()-8,String.valueOf(exmarbDTO.getFechaUltEntrada()));
				facturacion = facturacion+paso;
				
				//25 FECHA ULTIMA SALIDA(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaUltSalida()).length()-8,String.valueOf(exmarbDTO.getFechaUltSalida()));
				facturacion = facturacion+paso;
				
				//26 FECHA ULTIMA VENTA(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaUltVenta()).length()-8,String.valueOf(exmarbDTO.getFechaUltVenta()));
				facturacion = facturacion+paso;
				
				//27 FECHA ACTUALIZACION(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaActualizacion()).length()-8,String.valueOf(exmarbDTO.getFechaActualizacion()));
				facturacion = facturacion+paso;
				
				//28 FECHA INHABILITACION(8)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getFechaInhabilitacion()).length()-8,String.valueOf(exmarbDTO.getFechaInhabilitacion()));
				facturacion = facturacion+paso;
				
				//29 INGRESO/COMPRAS(9)
				ssIngresoCompra=formateadorExmarb2.format(iIngresoCompra);
				ssIngresoCompra=ssIngresoCompra.replace(",", "");
				ssIngresoCompra=ssIngresoCompra.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssIngresoCompra).length()-9,String.valueOf(ssIngresoCompra));
				facturacion = facturacion+paso;
				
				//30 INGRESO/TRASPASO(9)
				ssIngresoTraspaso=formateadorExmarb2.format(iIngresoTraspaso);
				ssIngresoTraspaso=ssIngresoTraspaso.replace(",", "");
				ssIngresoTraspaso=ssIngresoTraspaso.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssIngresoTraspaso).length()-9,String.valueOf(ssIngresoTraspaso));
				facturacion = facturacion+paso;
				
				//31 INGRESO/VARIOS(9)
				ssIngresoVarios=formateadorExmarb2.format(iIngresoVarios);
				ssIngresoVarios=ssIngresoVarios.replace(",", "");
				ssIngresoVarios=ssIngresoVarios.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssIngresoVarios).length()-9,String.valueOf(ssIngresoVarios));
				facturacion = facturacion+paso;
				
				//32 SALIDA/COMPRAS(9)
				ssSalidaCompra=formateadorExmarb2.format(iSalidaCompra);
				ssSalidaCompra=ssSalidaCompra.replace(",", "");
				ssSalidaCompra=ssSalidaCompra.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssSalidaCompra).length()-9,String.valueOf(ssSalidaCompra));
				facturacion = facturacion+paso;
				
				//33 SALIDA/TRASPASO(9)
				ssSalidaTraspaso=formateadorExmarb2.format(iSalidaTraspaso);
				ssSalidaTraspaso=ssSalidaTraspaso.replace(",", "");
				ssSalidaTraspaso=ssSalidaTraspaso.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssSalidaTraspaso).length()-9,String.valueOf(ssSalidaTraspaso));
				facturacion = facturacion+paso;
				
				//34 SALIDA/VARIOS(9)
				ssSalidaVarios=formateadorExmarb2.format(iSalidaVarios);
				ssSalidaVarios=ssSalidaVarios.replace(",", "");
				ssSalidaVarios=ssSalidaVarios.replace(".", "");
				
				paso = agregaBlanco(String.valueOf(ssSalidaVarios).length()-9,String.valueOf(ssSalidaVarios));
				facturacion = facturacion+paso;
				
				//35 PERIODO CIERRE(6)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getPeriodoCierre()).length()-6,String.valueOf(exmarbDTO.getPeriodoCierre()));
				facturacion = facturacion+paso;
				
				//36 STOCK CIERRE(9)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getStockAlCierre()).length()-9,String.valueOf(exmarbDTO.getStockAlCierre()));
				facturacion = facturacion+paso;
				
				//37 MONTO VTA PENDIENTE(9)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getMontoVtaPend()).length()-9,String.valueOf(exmarbDTO.getMontoVtaPend()));
				facturacion = facturacion+paso;
				
				//38 MONTO ACUMULADO COMPRAS(9)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getMontoAcumuCompra()).length()-9,String.valueOf(exmarbDTO.getMontoAcumuCompra()));
				facturacion = facturacion+paso;
				
				//39 MONTO ACUMULADO VENTAS(9)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getMontoAcumuVtas()).length()-9,String.valueOf(exmarbDTO.getMontoAcumuVtas()));
				facturacion = facturacion+paso;
				
				//40 UNIDAD VENTA ANTERIOR(9)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getUnidVtaAnterior()).length()-9,String.valueOf(exmarbDTO.getUnidVtaAnterior()));
				facturacion = facturacion+paso;
				
				//41 UNIDAD TRASPASOS(9)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getUnidadesTraspaso()).length()-9,String.valueOf(exmarbDTO.getUnidadesTraspaso()));
				facturacion = facturacion+paso;
				
				//42 SECTOR BODEGA(3)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getCodigoSector()).length()-3,String.valueOf(exmarbDTO.getCodigoSector()));
				facturacion = facturacion+paso;
				
				//43 CLASIFICACION(4)
				paso = agregaBlanco(String.valueOf(exmarbDTO.getCodigoClasificacion()).length()-4,String.valueOf(exmarbDTO.getCodigoClasificacion()));
				facturacion = facturacion+paso;
				
				//44 CODIGO EMPRESA(3)
				paso = agregaBlanco(String.valueOf(exdrec.getCodigoEmpresa()).length()-3,String.valueOf(exdrec.getCodigoEmpresa()));
				facturacion = facturacion+paso;
				
				int largo = facturacion.length();
				String largo2=String.valueOf(largo);
				facturacion = nombre+largo2+facturacion;
				
				logi.info(""+facturacion);
				
				//Calcula costo neto del articulo recepcionado
				logi.info("CALCULA COSTO NETO DEL ARTICULO");
				
				int iExento=0;
				double dcostoneto=0;
				
				//Busco si articulo es exento (si es exento le muevo el bruto al neto)
				iExento=extari.buscasiArticuloesExento(exdrec.getCodigoArticulo(), exdrec.getDvArticulo());
				
				if (iExento==1){
					dcostoneto=CostoArt;
				}
				else{
					dcostoneto=extari.obtenerCostoNeto(exdrec.getCodigoArticulo(), exdrec.getDvArticulo(), CostoArt);
				}
				
				DecimalFormat dcostoneto2 = new DecimalFormat("#.##");
				
				String sdcostoneto="";
				
				sdcostoneto = dcostoneto2.format(dcostoneto);
				sdcostoneto=sdcostoneto.replace(",", ".");
				
				//Actualizar costo neto EXMCOU
				logi.info("RPG ACTUALIZACION DE COSTO NETO DEL ARTICULO");
				exmarb.actualizaCostoNeto(exdrec.getCodigoBodega(), exdrec.getCodigoArticulo(), exdrec.getDvArticulo(), Double.parseDouble(sdcostoneto));
					
			}
		
		}
		return facturacion;
	}
	
	
	public String formaStringExmodc(ExmodcDTO exmodc, String estadocaboc, int nroDocto){
		String facturacion="";
		String nombre ="GEXMODC00 00";
		Fecha fch = new Fecha();
		
		int fechaSYSRecep=0;
		int horaSYSRecep=0;
		
		fechaSYSRecep = Integer.parseInt(fch.getYYYYMMDD());
		horaSYSRecep = Integer.parseInt(fch.getHHMMSS());
		
		//1 NUMERO OC (7)
		String paso = agregaBlanco(String.valueOf(exmodc.getNumeroOrden()).length()-7,String.valueOf(exmodc.getNumeroOrden()));
		facturacion = paso;
		
		//2 BODEGA ORIGEN (4)
		paso = agregaBlanco(String.valueOf(exmodc.getBodegaOrigen()).length()-4,String.valueOf(exmodc.getBodegaOrigen()));
		facturacion = facturacion+paso;
		
		//3 NRO FACTURA (10)
		paso = agregaBlanco(String.valueOf(nroDocto).length()-10,String.valueOf(nroDocto));
		facturacion = facturacion+paso;
		
		//4 LINEAS ORDEN (4)
		paso = agregaBlanco(String.valueOf(exmodc.getCantLineas()).length()-4,String.valueOf(exmodc.getCantLineas()));
		facturacion = facturacion+paso;
		
		//5 FECHA CREACION ORDEN (8)
		paso = agregaBlanco(String.valueOf(exmodc.getFechaOrden()).length()-8,String.valueOf(exmodc.getFechaOrden()));
		facturacion = facturacion+paso;
		
		//6 FECHA EMISION ORDEN (8)
		paso = agregaBlanco(String.valueOf(exmodc.getFechaEmision()).length()-8,String.valueOf(exmodc.getFechaEmision()));
		facturacion = facturacion+paso;
		
		//7 FECHA RECEPCION ORDEN (8)
		paso = agregaBlanco(String.valueOf(fechaSYSRecep).length()-8,String.valueOf(fechaSYSRecep));
		facturacion = facturacion+paso;
		
		//8 FECHA ACTUALIZACION ORDEN (8)
		paso = agregaBlanco(String.valueOf(exmodc.getFechaActual()).length()-8,String.valueOf(exmodc.getFechaActual()));
		facturacion = facturacion+paso;
		
		//9 HORA RECEPCION (6)
		paso = agregaBlanco(String.valueOf(horaSYSRecep).length()-6,String.valueOf(horaSYSRecep));
		facturacion = facturacion+paso;
		
		//10 TOTAL ORDEN (9)
		paso = agregaBlanco(String.valueOf(exmodc.getTotalOrden()).length()-9,String.valueOf(exmodc.getTotalOrden()));
		facturacion = facturacion+paso;
		
		//11 DESCTO ORDEN (9)
		paso = agregaBlanco(String.valueOf(exmodc.getDesctoOrden()).length()-9,String.valueOf(exmodc.getDesctoOrden()));
		facturacion = facturacion+paso;
		
		//12 ESTADO ORDEN (1)
		paso = agregaBlanco(String.valueOf(estadocaboc).length()-1,String.valueOf(estadocaboc));
		facturacion = facturacion+paso;
		
		//13 TIPO ENVIO (3)
		paso = agregaBlanco(String.valueOf(exmodc.getTipoenvioOrden()).length()-3,String.valueOf(exmodc.getTipoenvioOrden()));
		facturacion = facturacion+paso;
		
		//14 TIPO ORDEN (1)
		paso = agregaBlanco(String.valueOf(exmodc.getTipoOrden()).length()-1,String.valueOf(exmodc.getTipoOrden()));
		facturacion = facturacion+paso;
		
		//15 ORIGEN ORDEN (1)
		paso = agregaBlanco(String.valueOf(exmodc.getOrigenOrden()).length()-1,String.valueOf(exmodc.getOrigenOrden()));
		facturacion = facturacion+paso;
		
		//16 CONTACTO ORDEN (30)
		paso = agregaBlanco(String.valueOf(exmodc.getContactoOrden()).length()-30,String.valueOf(exmodc.getContactoOrden()));
		facturacion = facturacion+paso;
		
		//17 OBSERVACION ORDEN (80)
		paso = agregaBlanco(String.valueOf(exmodc.getObservacionOrden()).length()-80,String.valueOf(exmodc.getObservacionOrden()));
		facturacion = facturacion+paso;
		
		//18 RUT PROVEEDOR (11)
		paso = agregaBlanco(String.valueOf(exmodc.getRutProveedor()).length()-11,String.valueOf(exmodc.getRutProveedor()));
		facturacion = facturacion+paso;
		
		//19 DV PROVEEDOR (1)
		paso = agregaBlanco(String.valueOf(exmodc.getDigito()).length()-1,String.valueOf(exmodc.getDigito()));
		facturacion = facturacion+paso;
		
		//20 FORMA DE PAGO (4)
		paso = agregaBlanco(String.valueOf(exmodc.getFormaPago()).length()-4,String.valueOf(exmodc.getFormaPago()));
		facturacion = facturacion+paso;
		
		//21 USUARIO (10)
		paso = agregaBlanco(String.valueOf(exmodc.getCodigoUsuarioOrden()).length()-10,String.valueOf(exmodc.getCodigoUsuarioOrden()));
		facturacion = facturacion+paso;
		
		//22 CODIGO EMPRESA (3)
		paso = agregaBlanco(String.valueOf(exmodc.getCodEmpresa()).length()-3,String.valueOf(exmodc.getCodEmpresa()));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
		
	}
	
	public String formaStringVedmar(int codmvto, int codempresa, int rutproveedor, String dvproveedor, int correlativoOC, int precioneto, int montoneto, int codbodorigen, int codart, String dvcodart, int lineaArt, double preciobruto, int codsector, double dPesoRecep, double dVoluRecep, double ddstockRecep, double dMontoBrutoLinea, double dTotalBrutoLinea, String sEntradaSalida, String codFormaPago, ExmarbDAO exmarb){
		String facturacion="";
		String nombre ="AEXDREC00 00";
		Fecha fch = new Fecha();
		int fechaSYS=0;
		
		String precioBruto="";
		double dprecioBruto=0;
		String PesoRecep="";
		String VoluRecep="";
		String sstockRecep="";
		
		double dCostoTotalNeto=0;
		String sCostoTotalNeto="";
		double dCostoNeto=0;
		String sCostoNeto="";
		
		String sMontoBrutoLinea="";
		
		DecimalFormat formateadorVedmar = new DecimalFormat("###,###.00");
		
		fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
		
		//1 CODIGO MOVIMIENTO(4)
		String paso = agregaBlanco(String.valueOf(codmvto).length()-4,String.valueOf(codmvto));
		facturacion = paso;
		
		//2 FECHA SISTEMA(8)
		paso = agregaBlanco(String.valueOf(fechaSYS).length()-8,String.valueOf(fechaSYS));
		facturacion = facturacion+paso;
		
		//3 NUMERO INTERNO(10)
		paso = agregaBlanco(String.valueOf(correlativoOC).length()-10,String.valueOf(correlativoOC));
		facturacion = facturacion+paso;
		
		//4 CORRELATIVO LINEA(2)
		paso = agregaBlanco(String.valueOf(lineaArt).length()-2,String.valueOf(lineaArt));
		facturacion = facturacion+paso;
		
		//5 CODIGO BODEGA(4)
		paso = agregaBlanco(String.valueOf(codbodorigen).length()-4,String.valueOf(codbodorigen));
		facturacion = facturacion+paso;
		
		//6 CODIGO ARTICULO(7)
		paso = agregaBlanco(String.valueOf(codart).length()-7,String.valueOf(codart));
		facturacion = facturacion+paso;
		
		//7 DIGITO ARTICULO(1)
		paso = agregaBlanco(String.valueOf(dvcodart).length()-1,String.valueOf(dvcodart));
		facturacion = facturacion+paso;
		
		//8 FORMATO ARTICULO(1)
		paso = agregaBlanco(String.valueOf("U").length()-1,String.valueOf("U"));
		facturacion = facturacion+paso;
		
		//9 CANTIDAD FORMATO(7)
		sstockRecep=formateadorVedmar.format(ddstockRecep);
		sstockRecep=sstockRecep.replace(",", "");
		sstockRecep=sstockRecep.replace(".", "");
		
		paso = agregaBlanco(String.valueOf((int)ddstockRecep).length()-7,String.valueOf((int)ddstockRecep));
		facturacion = facturacion+paso;
		
		//10 CANTIDAD ARTICULO(7)
		paso = agregaBlanco(String.valueOf((int)ddstockRecep).length()-7,String.valueOf((int)ddstockRecep));
		facturacion = facturacion+paso;
		
		//11 SECCION ARTICULO(3)
		paso = agregaBlanco(String.valueOf(codsector).length()-3,String.valueOf(codsector));
		facturacion = facturacion+paso;
		
		//12 PESO ARTICULO(9)
		dPesoRecep=dPesoRecep/1000;
		PesoRecep=formateadorVedmar.format(dPesoRecep);
		PesoRecep=PesoRecep.replace(",", "");
		PesoRecep=PesoRecep.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(PesoRecep).length()-9,String.valueOf(PesoRecep));
		facturacion = facturacion+paso;
		
		//13 VOLUMEN ARTICULO(9)
		dVoluRecep=dVoluRecep/1000;
		VoluRecep=formateadorVedmar.format(dVoluRecep);
		VoluRecep=VoluRecep.replace(",", "");
		VoluRecep=VoluRecep.replace(".", "");
		
		if (VoluRecep.length()>6){
			VoluRecep=VoluRecep.substring(0, 7);
		}
		
		paso = agregaBlanco(String.valueOf(VoluRecep).length()-9,String.valueOf(VoluRecep));
		facturacion = facturacion+paso;
		
		//14 PRECIO(9)
		dprecioBruto=preciobruto;
		
		precioBruto=formateadorVedmar.format(dprecioBruto);
		precioBruto=precioBruto.replace(",", "");
		precioBruto=precioBruto.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(precioBruto).length()-9,String.valueOf(precioBruto));
		facturacion = facturacion+paso;
		
		//15 MONTO BRUTO LINEA(11)
		sMontoBrutoLinea=formateadorVedmar.format(dMontoBrutoLinea);
		sMontoBrutoLinea=sMontoBrutoLinea.replace(",", "");
		sMontoBrutoLinea=sMontoBrutoLinea.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(sMontoBrutoLinea).length()-11,String.valueOf(sMontoBrutoLinea));
		facturacion = facturacion+paso;
		
		//16 PORCENTAJE DESCUENTO(5)
		paso = agregaBlanco(String.valueOf("").length()-5,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//17 MONTO DESCUENTO(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//18 MONTO FLETE(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//19 MONTO TOTAL LINEA(11)
		paso = agregaBlanco(String.valueOf((int)dTotalBrutoLinea).length()-11,String.valueOf((int)dTotalBrutoLinea));
		facturacion = facturacion+paso;
		
		//20 CODIGO INGRESO/SALIDA(1)
		paso = agregaBlanco(String.valueOf(sEntradaSalida).length()-1,String.valueOf(sEntradaSalida));
		facturacion = facturacion+paso;
		
		//21 SWITCH PROCESO(1)
		paso = agregaBlanco(String.valueOf("0").length()-1,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//22 CODIGO FORMA PAGO(1)
		paso = agregaBlanco(String.valueOf(codFormaPago).length()-1,String.valueOf(codFormaPago));
		facturacion = facturacion+paso;
		
		//23 RUT PROVEEDOR(10)
		paso = agregaBlanco(String.valueOf(rutproveedor).length()-10,String.valueOf(rutproveedor));
		facturacion = facturacion+paso;
		
		//24 DV RUT PROVEEDOR(1)
		paso = agregaBlanco(String.valueOf(dvproveedor).length()-1,String.valueOf(dvproveedor));
		facturacion = facturacion+paso;
		
		
		if (codmvto == 1){
			
			ExmarbDTO exmarbDTO = exmarb.obtieneCostosArticulo(codbodorigen, codart);
			
			//25 PRECIO NETO(13)
			dCostoNeto=exmarbDTO.getCostoNeto();
			sCostoNeto=formateadorVedmar.format(dCostoNeto);
			sCostoNeto=sCostoNeto.replace(",", "");
			sCostoNeto=sCostoNeto.replace(".", "");
			
			paso = agregaBlanco(String.valueOf(sCostoNeto+"00").length()-13,String.valueOf(sCostoNeto+"00"));
			facturacion = facturacion+paso;
			
			//26 COSTO NETO(11)
			paso = agregaBlanco(String.valueOf(sCostoNeto).length()-11,String.valueOf(sCostoNeto));
			facturacion = facturacion+paso;
			
			dCostoTotalNeto=(int)ddstockRecep*exmarbDTO.getCostoNeto();
			sCostoTotalNeto=formateadorVedmar.format(dCostoTotalNeto);
			sCostoTotalNeto=sCostoTotalNeto.replace(",", "");
			sCostoTotalNeto=sCostoTotalNeto.replace(".", "");
			
			//27 COSTO TOTAL NETO(11)
			paso = agregaBlanco(String.valueOf(sCostoTotalNeto).length()-11,String.valueOf(sCostoTotalNeto));
			facturacion = facturacion+paso;
			
			//28 MONTO NETO(11)
			paso = agregaBlanco(String.valueOf(sCostoTotalNeto).length()-11,String.valueOf(sCostoTotalNeto));
			facturacion = facturacion+paso;
			
		}
		else{
			
			//25 PRECIO NETO(13)
			paso = agregaBlanco(String.valueOf(precioneto).length()-13,String.valueOf(precioneto));
			facturacion = facturacion+paso;
			
			//26 COSTO NETO(11)
			paso = agregaBlanco(String.valueOf("0").length()-11,String.valueOf("0"));
			facturacion = facturacion+paso;
			
			//27 COSTO TOTAL NETO(11)
			paso = agregaBlanco(String.valueOf("0").length()-11,String.valueOf("0"));
			facturacion = facturacion+paso;
			
			//28 MONTO NETO(11)
			paso = agregaBlanco(String.valueOf(montoneto).length()-11,String.valueOf(montoneto));
			facturacion = facturacion+paso;
		}
		
		//29 MONTO DESCUENTO(11)
		paso = agregaBlanco(String.valueOf("0").length()-11,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//30 MONTO EXENTO(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//31 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codempresa).length()-3,String.valueOf(codempresa));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringExpaud(int codMvto, int codEmp, double nroDocto, int rutProv, int codTipDocto, int fechaMvto){
		String facturacion="";
		String nombre ="AEXPAUD00 000";
		//Fecha fch = new Fecha();
		//int fechaSYS=0;
		String sTipoRecep="";
		
		//fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
		//fechaSYS=20160616;
		
		if (codMvto==1){
			if (codTipDocto==33 || codTipDocto==36 || codTipDocto==3){
				sTipoRecep="F";
			}
			else{
				sTipoRecep="G";
			}
		}
			
		//1 CODIGO MOVIMIENTO(4)
		String paso = agregaBlanco(String.valueOf(codMvto).length()-4,String.valueOf(codMvto));
		facturacion = paso;
				
		//2 NUMERO FACTURA(10)
		paso = agregaBlanco(String.valueOf((int)nroDocto).length()-10,String.valueOf((int)nroDocto));
		facturacion = facturacion+paso;
		
		//3 NUMERO FACTURA(10)
		paso = agregaBlanco(String.valueOf((int)nroDocto).length()-10,String.valueOf((int)nroDocto));
		facturacion = facturacion+paso;
		
		//4 FECHA ACTUAL(8)
		paso = agregaBlanco(String.valueOf(fechaMvto).length()-8,String.valueOf(fechaMvto));
		facturacion = facturacion+paso;
		
		//5 NOMBRE DEL PC(15)
		//paso = agregaBlanco(String.valueOf("").length()-15,String.valueOf(""));
		paso = agregaBlanco(String.valueOf("CASERITA       ").length()-15,String.valueOf("CASERITA       "));
		facturacion = facturacion+paso;
		
		//6 RUT PROVEEDOR(11)
		paso = agregaBlanco(String.valueOf(rutProv).length()-11,String.valueOf(rutProv));
		//paso = agregaBlanco(String.valueOf("0").length()-11,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//7 INDICA SI SE RECEPCIONO CON FACTURA O GUIA DESPACHO(1)
		paso = agregaBlanco(String.valueOf(sTipoRecep).length()-1,String.valueOf(sTipoRecep));
		//paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//8 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codEmp).length()-3,String.valueOf(codEmp));
		facturacion = facturacion+paso;
		
		//9 CODIGO DOCUMENTO(4)
		paso = agregaBlanco(String.valueOf(codTipDocto).length()-4,String.valueOf(codTipDocto));
		//paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringSolNC(int codEmp, double nroDocto, int rutProv, String dvProv, double TotDocto){
		String facturacion="";
		String nombre ="AEXMCRE00 000";
		Fecha fch = new Fecha();
		int fechaSYS=0;
		int correlativo=0;
		String sTotSolicitud="";
		
		fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
		
		DecimalFormat formateadorExmcre = new DecimalFormat("###,###.00");
		
		//1 RUT PROVEEDOR(11)
		String paso = agregaBlanco(String.valueOf(rutProv).length()-11,String.valueOf(rutProv));
		facturacion = paso;
		
		//2 DIGITO PROVEEDOR(1)
		paso = agregaBlanco(String.valueOf(dvProv).length()-1,String.valueOf(dvProv));
		facturacion = facturacion+paso;
		
		//3 NUMERO DOCUMENTO FACTURA-GUIA(10)
		paso = agregaBlanco(String.valueOf((int)nroDocto).length()-10,String.valueOf((int)nroDocto));
		facturacion = facturacion+paso;
		
		//4 TIPO NOTA(1)
		paso = agregaBlanco(String.valueOf("C").length()-1,String.valueOf("C"));
		facturacion = facturacion+paso;
		
		//5 CORRELATIVO(3)
		paso = agregaBlanco(String.valueOf(correlativo).length()-3,String.valueOf(correlativo));
		facturacion = facturacion+paso;
		
		//6 FECHA SOLICITUD(8)
		paso = agregaBlanco(String.valueOf(fechaSYS).length()-8,String.valueOf(fechaSYS));
		facturacion = facturacion+paso;
		
		//7 FECHA RECEPCION(8)
		paso = agregaBlanco(String.valueOf("0").length()-8,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//8 NRO NOTA CREDITO(10)
		paso = agregaBlanco(String.valueOf("0").length()-10,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//9 MONTO TOTAL(11)
		sTotSolicitud=formateadorExmcre.format(TotDocto);
		sTotSolicitud=sTotSolicitud.replace(",", "");
		sTotSolicitud=sTotSolicitud.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(sTotSolicitud).length()-11,String.valueOf(sTotSolicitud));
		facturacion = facturacion+paso;
		
		//10 ESTADO(1)
		paso = agregaBlanco(String.valueOf("P").length()-1,String.valueOf("P"));
		facturacion = facturacion+paso;
		
		//11 FECHA TRASPASO PROVEEDOR(8)
		paso = agregaBlanco(String.valueOf("0").length()-8,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//12 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codEmp).length()-3,String.valueOf(codEmp));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringSolNCDet(int codEmp, double nroDocto, int rutProv, String dvProv, double montoTotalNeto, int correlativoSolicitud){
		String facturacion="";
		String nombre ="AEXDCRE00 00";
		Fecha fch = new Fecha();
		int fechaSYS=0;
		int correlativo=0;
		String descripSolicitud="";
		String sTotNetoSol="";
		
		fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
		
		DecimalFormat formateadorExdcre = new DecimalFormat("###,###.00");
		
		descripSolicitud="POR DIFERENCIA EN FACTURA     ";
		
		//1 RUT PROVEEDOR(11)
		String paso = agregaBlanco(String.valueOf(rutProv).length()-11,String.valueOf(rutProv));
		facturacion = paso;
		
		//2 DV PROVEEDOR(1)
		paso = agregaBlanco(String.valueOf(dvProv).length()-1,String.valueOf(dvProv));
		facturacion = facturacion+paso;
		
		//3 NUMERO DOCUMENTO FACTURA-GUIA(10)
		paso = agregaBlanco(String.valueOf((int)nroDocto).length()-10,String.valueOf((int)nroDocto));
		facturacion = facturacion+paso;
		
		//4 TIPO NOTA(1)
		paso = agregaBlanco(String.valueOf("C").length()-1,String.valueOf("C"));
		facturacion = facturacion+paso;
		
		//5 CORRELATIVO NOTA(3)
		paso = agregaBlanco(String.valueOf(correlativoSolicitud).length()-3,String.valueOf(correlativoSolicitud));
		facturacion = facturacion+paso;
		
		//6 CORRELATIVO(4)
		paso = agregaBlanco(String.valueOf("1").length()-4,String.valueOf("1"));
		facturacion = facturacion+paso;
		
		//7 CODIGO ARTICULO(7)
		paso = agregaBlanco(String.valueOf("0").length()-7,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//8 DV ARTICULO(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//9 FORMATO ARTICULO(1)
		paso = agregaBlanco(String.valueOf("").length()-1,String.valueOf(""));
		facturacion = facturacion+paso;
		
		//10 CANTIDAD(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//11 CANTIDAD UNITARIA(9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//12 DESCRIPCION(30)
		paso = agregaBlanco(String.valueOf(descripSolicitud).length()-30,String.valueOf(descripSolicitud));
		facturacion = facturacion+paso;
		
		//13 MONTO NETO(11)
		sTotNetoSol=formateadorExdcre.format(montoTotalNeto);
		sTotNetoSol=sTotNetoSol.replace(",", "");
		sTotNetoSol=sTotNetoSol.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(sTotNetoSol).length()-11,String.valueOf(sTotNetoSol));
		facturacion = facturacion+paso;
		
		//14 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codEmp).length()-3,String.valueOf(codEmp));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaColaImp(String colaImpresora){
		String facturacion="";
		String nombre ="ASYSDII00 000";
		
		//1 NRO COPIAS(2)
		String paso = agregaBlanco(String.valueOf("01").length()-2,String.valueOf("01"));
		facturacion = paso;
		
		//2 NOMBRE IMPRESORA(10)
		paso = agregaBlanco(String.valueOf(colaImpresora).length()-10,String.valueOf(colaImpresora));
		facturacion = facturacion+paso;
		
		//3 NOMBRE DEL INFORME(8)
		paso = agregaBlanco(String.valueOf("EXPAUD  ").length()-8,String.valueOf("EXPAUD  "));
		facturacion = facturacion+paso;
		
		//4 ESTADO(1)
		paso = agregaBlanco(String.valueOf("I").length()-1,String.valueOf("I"));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringDetalleRecepOT(int codEmp, int nroTraspaso, String fechaVctoArt, int codArticulo, String digArticulo, int LineaArt, String codBarra, double valorUnit, 
				double volumen, double peso, double cantDespachadaFmt, double cantUnidDespa, double cantRecep){
		String facturacion="";
		String nombre ="GEXDTRA00 00";
		Fecha fch = new Fecha();
		int fechaSYS=0;
		int correlativo=0;
		String descripSolicitud="";
		String sTotNetoSol="";
		String sEstadoLinea="";
		String ssvalorUnit="";
		String ssPeso="";
		String ssVolumen="";
		String ssCantDespaFmt="";
		String ssCantRecep="";
		String ssCantUnidDespa="";
		
		double cantidadPendiente=0;
		
		fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
		
		DecimalFormat formateadorExdtra = new DecimalFormat("###,###.00");
		
		//1 NRO TRASPASO(7)
		String paso = agregaBlanco(String.valueOf(nroTraspaso).length()-7,String.valueOf(nroTraspaso));
		facturacion = paso;
		
		//2 NUMERO LINEA (3)
		paso = agregaBlanco(String.valueOf(LineaArt).length()-3,String.valueOf(LineaArt));
		facturacion = facturacion+paso;
		
		//3 CODIGO BARRA (60)
		paso = agregaBlanco(String.valueOf(codBarra).length()-60,String.valueOf(codBarra));
		facturacion = facturacion+paso;
		
		//4 FORMATO (1)
		paso = agregaBlanco(String.valueOf("U").length()-1,String.valueOf("U"));
		facturacion = facturacion+paso;
		
		//5 VALOR UNITARIO (9)
		ssvalorUnit=formateadorExdtra.format(valorUnit);
		ssvalorUnit=ssvalorUnit.replace(",", "");
		ssvalorUnit=ssvalorUnit.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssvalorUnit).length()-9,String.valueOf(ssvalorUnit));
		facturacion = facturacion+paso;
		
		//6 PESO ARTICULO (9)
		ssPeso=formateadorExdtra.format(peso);
		ssPeso=ssPeso.replace(",", "");
		ssPeso=ssPeso.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssPeso).length()-9,String.valueOf(ssPeso));
		facturacion = facturacion+paso;
		
		//7 VOLUMEN ARTICULO (9)
		ssVolumen=formateadorExdtra.format(volumen);
		ssVolumen=ssVolumen.replace(",", "");
		ssVolumen=ssVolumen.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssVolumen).length()-9,String.valueOf(ssVolumen));
		facturacion = facturacion+paso;
		
		//8 CANTIDAD DESPACHADA FORMATO (9)
		ssCantDespaFmt=formateadorExdtra.format(cantDespachadaFmt);
		ssCantDespaFmt=ssCantDespaFmt.replace(",", "");
		ssCantDespaFmt=ssCantDespaFmt.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCantDespaFmt).length()-9,String.valueOf(ssCantDespaFmt));
		facturacion = facturacion+paso;
		
		//9 CANTIDAD RECIBIDA FORMATO (9)
		ssCantRecep=formateadorExdtra.format(cantRecep);
		ssCantRecep=ssCantRecep.replace(",", "");
		ssCantRecep=ssCantRecep.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCantRecep).length()-9,String.valueOf(ssCantRecep));
		facturacion = facturacion+paso;
		
		//10 CANTIDAD UNITARIA RECEPCIONADA (9)
		paso = agregaBlanco(String.valueOf(ssCantRecep).length()-9,String.valueOf(ssCantRecep));
		facturacion = facturacion+paso;
		
		//11 CANTIDAD UNIDAD DESPACHADA (9)
		ssCantUnidDespa=formateadorExdtra.format(cantUnidDespa);
		ssCantUnidDespa=ssCantUnidDespa.replace(",", "");
		ssCantUnidDespa=ssCantUnidDespa.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCantUnidDespa).length()-9,String.valueOf(ssCantUnidDespa));
		facturacion = facturacion+paso;
		
		//12 FECHA RECEPCION (8)
		paso = agregaBlanco(String.valueOf(fechaSYS).length()-8,String.valueOf(fechaSYS));
		facturacion = facturacion+paso;
		
		//13 ARTICULO (7)
		paso = agregaBlanco(String.valueOf(codArticulo).length()-7,String.valueOf(codArticulo));
		facturacion = facturacion+paso;
		
		//14 DV ARTICULO (1)
		paso = agregaBlanco(String.valueOf(digArticulo).length()-1,String.valueOf(digArticulo));
		facturacion = facturacion+paso;
		
		//15 CODIGO JEFE LOCAL (4)
		paso = agregaBlanco(String.valueOf("0").length()-4,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//16 FECHA VENCIMIENTO (8)
		paso = agregaBlanco(String.valueOf(fechaVctoArt).length()-8,String.valueOf(fechaVctoArt));
		facturacion = facturacion+paso;
		
		//17 CODIGO EMPRESA (3)
		paso = agregaBlanco(String.valueOf(codEmp).length()-3,String.valueOf(codEmp));
		facturacion = facturacion+paso;
		
		//18 CANTIDAD UNITARIOA DESPACHADA CARGUIO (7)
		paso = agregaBlanco(String.valueOf("").length()-7,String.valueOf(""));
		facturacion = facturacion+paso;
		
		sEstadoLinea="R";
		
		//19 ESTADO LINEA (1)
		paso = agregaBlanco(String.valueOf(sEstadoLinea).length()-1,String.valueOf(sEstadoLinea));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringCabeceraRecepOT(ExmtraDTO dto){
		String facturacion="";
		String nombre ="GEXMTRA00 00";
		
		String ssKilosTraspa="";
		String ssValorTraspa="";
		String ssVolumenTraspa="";
		
		DecimalFormat formateadorExmtra = new DecimalFormat("###,###.00");
		
		//1 NRO TRASPASO(7)
		String paso = agregaBlanco(String.valueOf(dto.getNumTraspaso()).length()-7,String.valueOf(dto.getNumTraspaso()));
		facturacion = paso;
		
		//2 CODIGO BODEGA ORIGEN (4)
		paso = agregaBlanco(String.valueOf(dto.getBodegaOrigen()).length()-4,String.valueOf(dto.getBodegaOrigen()));
		facturacion = facturacion+paso;
		
		//3 CODIGO BODEGA DESTINO (4)
		paso = agregaBlanco(String.valueOf(dto.getBodegaDestino()).length()-4,String.valueOf(dto.getBodegaDestino()));
		facturacion = facturacion+paso;
		
		//4 NRO SELLO TRASPASO (7)
		paso = agregaBlanco(String.valueOf(dto.getNumeroSello()).length()-7,String.valueOf(dto.getNumeroSello()));
		facturacion = facturacion+paso;
		
		//5 FECHA TRASPASO (8)
		paso = agregaBlanco(String.valueOf(dto.getFechaTraspaso()).length()-8,String.valueOf(dto.getFechaTraspaso()));
		facturacion = facturacion+paso;
		
		//6 HORA TRASPASO (6)
		paso = agregaBlanco(String.valueOf(dto.getHoraTopeTraspaso()).length()-6,String.valueOf(dto.getHoraTopeTraspaso()));
		facturacion = facturacion+paso;
		
		//7 KILOS (9)
		ssKilosTraspa=formateadorExmtra.format(dto.getKilosMercaderia());
		ssKilosTraspa=ssKilosTraspa.replace(",", "");
		ssKilosTraspa=ssKilosTraspa.replace(".", "");
		paso = agregaBlanco(String.valueOf(ssKilosTraspa).length()-9,String.valueOf(ssKilosTraspa));
		facturacion = facturacion+paso;
		
		//8 VALOR TOTAL TRASPASO (9)
		//ssValorTraspa=formateadorExmtra.format(dto.getValorTTraspaso());
		//ssValorTraspa=ssValorTraspa.replace(",", "");
		//ssValorTraspa=ssValorTraspa.replace(".", "");
		paso = agregaBlanco(String.valueOf((int)dto.getValorTTraspaso()).length()-9,String.valueOf((int)dto.getValorTTraspaso()));
		facturacion = facturacion+paso;
		
		//9 VOLUMEN (9)
		ssVolumenTraspa=formateadorExmtra.format(dto.getValorVTraspaso());
		ssVolumenTraspa=ssVolumenTraspa.replace(",", "");
		ssVolumenTraspa=ssVolumenTraspa.replace(".", "");
		paso = agregaBlanco(String.valueOf(ssVolumenTraspa).length()-9,String.valueOf(ssVolumenTraspa));
		facturacion = facturacion+paso;
		
		//10 ESTADO OT (1)
		paso = agregaBlanco(String.valueOf(dto.getEstadoTraspaso()).length()-1,String.valueOf(dto.getEstadoTraspaso()));
		facturacion = facturacion+paso;
		
		//11 CODIGO USUARIO (10)
		paso = agregaBlanco(String.valueOf(dto.getCodigoUsuario()).length()-10,String.valueOf(dto.getCodigoUsuario()));
		facturacion = facturacion+paso;
		
		//12 NRO GUIA (10)
		paso = agregaBlanco(String.valueOf(dto.getNumGuiaDespacho()).length()-10,String.valueOf(dto.getNumGuiaDespacho()));
		facturacion = facturacion+paso;
		
		//13 RUT TRANSPORTE (11)
		paso = agregaBlanco(String.valueOf(dto.getRutEmpresa()).length()-11,String.valueOf(dto.getRutEmpresa()));
		facturacion = facturacion+paso;
		
		//14 DV ARTICULO (1)
		paso = agregaBlanco(String.valueOf(dto.getDvEmpresa()).length()-1,String.valueOf(dto.getDvEmpresa()));
		facturacion = facturacion+paso;
		
		//15 PATENTE (10)
		paso = agregaBlanco(String.valueOf(dto.getPatente()).length()-10,String.valueOf(dto.getPatente()));
		facturacion = facturacion+paso;
		
		//16 CODIGO EMPRESA (3)
		paso = agregaBlanco(String.valueOf(dto.getCodigoEmpresa()).length()-3,String.valueOf(dto.getCodigoEmpresa()));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public String formaStringActStockBodDestino(int codEmp, int nroTraspaso, int bodegaDestino){
		String facturacion="";
		String nombre ="GEXBTRA00 000";
		
		//1 NRO TRASPASO(7)
		String paso = agregaBlanco(String.valueOf(nroTraspaso).length()-7,String.valueOf(nroTraspaso));
		facturacion = paso;
		
		//2 BODEGA DESTINO (4)
		paso = agregaBlanco(String.valueOf(bodegaDestino).length()-4,String.valueOf(bodegaDestino));
		facturacion = facturacion+paso;
		
		//3 DESTINO (9)
		paso = agregaBlanco(String.valueOf("DESTINO  ").length()-9,String.valueOf("DESTINO  "));
		facturacion = facturacion+paso;
				
		//4 ARTICULO (7)
		paso = agregaBlanco(String.valueOf("0").length()-7,String.valueOf("0"));
		facturacion = facturacion+paso;
				
		//5 UNIDADES (9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
			
		//6 CODIGO EMPRESA (3)
		paso = agregaBlanco(String.valueOf(codEmp).length()-3,String.valueOf(codEmp));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
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
	
	
	public String formaStringExmarbOT(ExmarbDTO exmarb, int codEmpresa, double costoTraspaso){
		String facturacion="";
		String nombre ="GEXMARB00 00";
		Fecha fch = new Fecha();
		
		DecimalFormat formateadorExmarb = new DecimalFormat("###,###.00");
		
		String ssStockCritico="";
		String ssStockLinea="";
		String ssStockComputacional="";
		String ssStockPendiente="";
		String ssCostoTraspaso="";
		String ssMargenUtilidad="";
		String ssVariacion="";
		String ssCostoMayor="";
		String ssCostoPromedio="";
		String ssCostoMenor="";
		String ssCostoAnterior="";
		String ssPrecioBase="";
		String ssStockMinimo="";
		String ssStockMaximo="";
		String ssIngresoCompra="";
		String ssIngresoTraspaso="";
		String ssIngresoVarios="";
		String ssSalidaCompra="";
		String ssSalidaTraspaso="";
		String ssSalidaVarios="";
		
		
		//1 BODEGA
		String paso = agregaBlanco(String.valueOf(exmarb.getCodigoBodega()).length()-4,String.valueOf(exmarb.getCodigoBodega()));
		facturacion = paso;
		
		//2 CODIGO ARTICULO
		paso = agregaBlanco(String.valueOf(exmarb.getCodigoArticulo()).length()-7,String.valueOf(exmarb.getCodigoArticulo()));
		facturacion = facturacion+paso;
		
		//3 DV ARTICULO
		paso = agregaBlanco(String.valueOf(exmarb.getDvArticulo()).length()-1,String.valueOf(exmarb.getDvArticulo()));
		facturacion = facturacion+paso;
		
		//4 STOCK CRITICO(9)
		ssStockCritico=formateadorExmarb.format(exmarb.getStockCritico());
		ssStockCritico=ssStockCritico.replace(",", "");
		ssStockCritico=ssStockCritico.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssStockCritico).length()-9,String.valueOf(ssStockCritico));
		facturacion = facturacion+paso;
		
		//5 STOCK LINEA(9)
		ssStockLinea=formateadorExmarb.format(exmarb.getStockLinea());
		ssStockLinea=ssStockLinea.replace(",", "");
		ssStockLinea=ssStockLinea.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssStockLinea).length()-9,String.valueOf(ssStockLinea));
		facturacion = facturacion+paso;
		
		//6 STOCK COMPUTACIONAL(9)
		ssStockComputacional=formateadorExmarb.format(exmarb.getStockComputacional());
		ssStockComputacional=ssStockComputacional.replace(",", "");
		ssStockComputacional=ssStockComputacional.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssStockComputacional).length()-9,String.valueOf(ssStockComputacional));
		facturacion = facturacion+paso;
		
		//7 STOCK PENDIENTE(9)
		ssStockPendiente=formateadorExmarb.format(exmarb.getStockPendiente());
		ssStockPendiente=ssStockPendiente.replace(",", "");
		ssStockPendiente=ssStockPendiente.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssStockPendiente).length()-9,String.valueOf(ssStockPendiente));
		facturacion = facturacion+paso;
		
		//8 FECHA CREACION(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaCreacion()).length()-8,String.valueOf(exmarb.getFechaCreacion()));
		facturacion = facturacion+paso;
		
		//9 COSTO(9)
		ssCostoTraspaso=formateadorExmarb.format(costoTraspaso);
		ssCostoTraspaso=ssCostoTraspaso.replace(",", "");
		ssCostoTraspaso=ssCostoTraspaso.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCostoTraspaso).length()-9,String.valueOf(ssCostoTraspaso));
		facturacion = facturacion+paso;
		
		//10 MARGEN UTILIDAD(6)
		ssMargenUtilidad=formateadorExmarb.format(exmarb.getMargenUtil());
		ssMargenUtilidad=ssMargenUtilidad.replace(",", "");
		ssMargenUtilidad=ssMargenUtilidad.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssMargenUtilidad).length()-6,String.valueOf(ssMargenUtilidad));
		facturacion = facturacion+paso;
		
		//11 VARIACION LOCAL(5)
		ssVariacion=formateadorExmarb.format(exmarb.getVariacion());
		ssVariacion=ssVariacion.replace(",", "");
		ssVariacion=ssVariacion.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssVariacion).length()-5,String.valueOf(ssVariacion));
		facturacion = facturacion+paso;
		
		//12 ESTADO(1)
		paso = agregaBlanco(String.valueOf(exmarb.getEstado()).length()-1,String.valueOf(exmarb.getEstado()));
		facturacion = facturacion+paso;
		
		//13 COMPRAR ARTICULO(1)
		paso = agregaBlanco(String.valueOf(exmarb.getCompraArt()).length()-1,String.valueOf(exmarb.getCompraArt()));
		facturacion = facturacion+paso;
		
		//14 FECHA ULTIMA COMPRA(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaUltCompra()).length()-8,String.valueOf(exmarb.getFechaUltCompra()));
		facturacion = facturacion+paso;
		
		//15 COSTO MAYOR(9)
		ssCostoMayor=formateadorExmarb.format(exmarb.getCostoMayor());
		ssCostoMayor=ssCostoMayor.replace(",", "");
		ssCostoMayor=ssCostoMayor.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCostoMayor).length()-9,String.valueOf(ssCostoMayor));
		facturacion = facturacion+paso;
		
		//16 COSTO PROMEDIO(9)
		ssCostoPromedio=formateadorExmarb.format(exmarb.getCostoPromedio());
		ssCostoPromedio=ssCostoPromedio.replace(",", "");
		ssCostoPromedio=ssCostoPromedio.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCostoPromedio).length()-9,String.valueOf(ssCostoPromedio));
		facturacion = facturacion+paso;
		
		//17 COSTO MENOR(9)
		ssCostoMenor=formateadorExmarb.format(exmarb.getCostoMenor());
		ssCostoMenor=ssCostoMenor.replace(",", "");
		ssCostoMenor=ssCostoMenor.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCostoMenor).length()-9,String.valueOf(ssCostoMenor));
		facturacion = facturacion+paso;
		
		//18 COSTO ANTERIOR(9)
		ssCostoAnterior=formateadorExmarb.format(exmarb.getCostoAnterior());
		ssCostoAnterior=ssCostoAnterior.replace(",", "");
		ssCostoAnterior=ssCostoAnterior.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssCostoAnterior).length()-9,String.valueOf(ssCostoAnterior));
		facturacion = facturacion+paso;
		
		//19 RUT PROVEEDOR(11)
		paso = agregaBlanco(String.valueOf(exmarb.getUltimoProveedor()).length()-11,String.valueOf(exmarb.getUltimoProveedor()));
		facturacion = facturacion+paso;
		
		//20 DV PROVEEDOR(1)
		paso = agregaBlanco(String.valueOf(exmarb.getdvUltimoProveedor()).length()-1,String.valueOf(exmarb.getdvUltimoProveedor()));
		facturacion = facturacion+paso;
		
		//21 PRECIO BASE(9)
		ssPrecioBase=formateadorExmarb.format(exmarb.getPrecioBaseBruto());
		ssPrecioBase=ssPrecioBase.replace(",", "");
		ssPrecioBase=ssPrecioBase.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssPrecioBase).length()-9,String.valueOf(ssPrecioBase));
		facturacion = facturacion+paso;
		
		//22 STOCK MINIMO(9)
		ssStockMinimo=formateadorExmarb.format(exmarb.getStockMinimo());
		ssStockMinimo=ssStockMinimo.replace(",", "");
		ssStockMinimo=ssStockMinimo.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssStockMinimo).length()-9,String.valueOf(ssStockMinimo));
		facturacion = facturacion+paso;
		
		//23 STOCK MAXIMO(9)
		ssStockMaximo=formateadorExmarb.format(exmarb.getStockMaximo());
		ssStockMaximo=ssStockMaximo.replace(",", "");
		ssStockMaximo=ssStockMaximo.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssStockMaximo).length()-9,String.valueOf(ssStockMaximo));
		facturacion = facturacion+paso;
		
		//24 FECHA ULTIMA ENTRADA(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaUltEntrada()).length()-8,String.valueOf(exmarb.getFechaUltEntrada()));
		facturacion = facturacion+paso;
		
		//25 FECHA ULTIMA SALIDA(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaUltSalida()).length()-8,String.valueOf(exmarb.getFechaUltSalida()));
		facturacion = facturacion+paso;
		
		//26 FECHA ULTIMA VENTA(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaUltVenta()).length()-8,String.valueOf(exmarb.getFechaUltVenta()));
		facturacion = facturacion+paso;
		
		//27 FECHA ACTUALIZACION(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaActualizacion()).length()-8,String.valueOf(exmarb.getFechaActualizacion()));
		facturacion = facturacion+paso;
		
		//28 FECHA INHABILITACION(8)
		paso = agregaBlanco(String.valueOf(exmarb.getFechaInhabilitacion()).length()-8,String.valueOf(exmarb.getFechaInhabilitacion()));
		facturacion = facturacion+paso;
		
		//29 INGRESO/COMPRAS(9)
		ssIngresoCompra=formateadorExmarb.format(exmarb.getIngresoCompra());
		ssIngresoCompra=ssIngresoCompra.replace(",", "");
		ssIngresoCompra=ssIngresoCompra.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssIngresoCompra).length()-9,String.valueOf(ssIngresoCompra));
		facturacion = facturacion+paso;
		
		//30 INGRESO/TRASPASO(9)
		ssIngresoTraspaso=formateadorExmarb.format(exmarb.getIngresoTraspaso());
		ssIngresoTraspaso=ssIngresoTraspaso.replace(",", "");
		ssIngresoTraspaso=ssIngresoTraspaso.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssIngresoTraspaso).length()-9,String.valueOf(ssIngresoTraspaso));
		facturacion = facturacion+paso;
		
		//31 INGRESO/VARIOS(9)
		ssIngresoVarios=formateadorExmarb.format(exmarb.getIngresoVarios());
		ssIngresoVarios=ssIngresoVarios.replace(",", "");
		ssIngresoVarios=ssIngresoVarios.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssIngresoVarios).length()-9,String.valueOf(ssIngresoVarios));
		facturacion = facturacion+paso;
		
		//32 SALIDA/COMPRAS(9)
		ssSalidaCompra=formateadorExmarb.format(exmarb.getSalidaCompra());
		ssSalidaCompra=ssSalidaCompra.replace(",", "");
		ssSalidaCompra=ssSalidaCompra.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssSalidaCompra).length()-9,String.valueOf(ssSalidaCompra));
		facturacion = facturacion+paso;
		
		//33 SALIDA/TRASPASO(9)
		ssSalidaTraspaso=formateadorExmarb.format(exmarb.getSalidaTraspaso());
		ssSalidaTraspaso=ssSalidaTraspaso.replace(",", "");
		ssSalidaTraspaso=ssSalidaTraspaso.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssSalidaTraspaso).length()-9,String.valueOf(ssSalidaTraspaso));
		facturacion = facturacion+paso;
		
		//34 SALIDA/VARIOS(9)
		ssSalidaVarios=formateadorExmarb.format(exmarb.getSalidaVarios());
		ssSalidaVarios=ssSalidaVarios.replace(",", "");
		ssSalidaVarios=ssSalidaVarios.replace(".", "");
		
		paso = agregaBlanco(String.valueOf(ssSalidaVarios).length()-9,String.valueOf(ssSalidaVarios));
		facturacion = facturacion+paso;
		
		//35 PERIODO CIERRE(6)
		paso = agregaBlanco(String.valueOf(exmarb.getPeriodoCierre()).length()-6,String.valueOf(exmarb.getPeriodoCierre()));
		facturacion = facturacion+paso;
		
		//36 STOCK CIERRE(9)
		paso = agregaBlanco(String.valueOf(exmarb.getStockAlCierre()).length()-9,String.valueOf(exmarb.getStockAlCierre()));
		facturacion = facturacion+paso;
		
		//37 MONTO VTA PENDIENTE(9)
		paso = agregaBlanco(String.valueOf(exmarb.getMontoVtaPend()).length()-9,String.valueOf(exmarb.getMontoVtaPend()));
		facturacion = facturacion+paso;
		
		//38 MONTO ACUMULADO COMPRAS(9)
		paso = agregaBlanco(String.valueOf(exmarb.getMontoAcumuCompra()).length()-9,String.valueOf(exmarb.getMontoAcumuCompra()));
		facturacion = facturacion+paso;
		
		//39 MONTO ACUMULADO VENTAS(9)
		paso = agregaBlanco(String.valueOf(exmarb.getMontoAcumuVtas()).length()-9,String.valueOf(exmarb.getMontoAcumuVtas()));
		facturacion = facturacion+paso;
		
		//40 UNIDAD VENTA ANTERIOR(9)
		paso = agregaBlanco(String.valueOf(exmarb.getUnidVtaAnterior()).length()-9,String.valueOf(exmarb.getUnidVtaAnterior()));
		facturacion = facturacion+paso;
		
		//41 UNIDAD TRASPASOS(9)
		paso = agregaBlanco(String.valueOf(exmarb.getUnidadesTraspaso()).length()-9,String.valueOf(exmarb.getUnidadesTraspaso()));
		facturacion = facturacion+paso;
		
		//42 SECTOR BODEGA(3)
		paso = agregaBlanco(String.valueOf(exmarb.getCodigoSector()).length()-3,String.valueOf(exmarb.getCodigoSector()));
		facturacion = facturacion+paso;
		
		//43 CLASIFICACION(4)
		paso = agregaBlanco(String.valueOf(exmarb.getCodigoClasificacion()).length()-4,String.valueOf(exmarb.getCodigoClasificacion()));
		facturacion = facturacion+paso;
		
		//44 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codEmpresa).length()-3,String.valueOf(codEmpresa));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	
	
	public int moveFile(String urlFile, String nameFile){
		int numero=0;
		Path origenPath = FileSystems.getDefault().getPath(urlFile);
	    Path destinoPath = FileSystems.getDefault().getPath("/home/ftp/proc/"+nameFile);
	    //Path destinoPath = FileSystems.getDefault().getPath("/home/ftp/proc/"+nameFile);

	    try {
	        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	    
	    
		return numero;
	}
	public static void main (String []args){
		//IntegracionConfirmaRecepcionHelper help = new IntegracionConfirmaRecepcionHelper();
		//help.rutina();
		//help.procesaReconciliacion("/home2/ftp/in/INV_RCV_714295.xml", "INV_RCV_714295.xml");
		String estado = "D";
		if ("D"!=estado){
			int numero =100;
		}
		
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
	
	public void rutina(){
		DAOFactory dao = DAOFactory.getInstance();
		ExdrecDAO exdrec = dao.getExdrecDAO();
		ExmarbDAO exmarb = dao.getExmarbDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		List lista = exdrec.recuperaDetalleCompletoCobro(2, 67540, 20170426, 171806, 26);
		int insertVecmarCobro=1;
		int fechaMvtoVenta=0;
		int numeroDoc=0;
		Fecha fch = new Fecha();
		int correlativo=0;
		int stockLineaArt=0;
		Iterator iter = lista.iterator();
		IntegracionConfirmaRecepcionHelper hel = new IntegracionConfirmaRecepcionHelper();
		while (iter.hasNext())
		{
			ExdrecDTO dto = (ExdrecDTO) iter.next();
			
			/*if (insertVecmarCobro==0){
				
				fechaMvtoVenta=Integer.parseInt(fch.getYYYYMMDD());
				
			//	numeroDoc = tpacor.recupeCorrelativo(0, 1);
				String str = "ASYSRCD00 00008   0  01   ";
				numeroDoc = proce.obtieneCorrelativo(str);
				
				//Genera VECMAR con la Salida por el cobro al transportista
				GeneraVecmar(exmtraDTO2.getCodigoEmpresa(), 21, fechaMvtoVenta, numeroDoc, codDoc, exmtraDTO2.getBodegaDestino(), rutCobroVTA, dvCobroVTA, vecmar);
				
				String str2 = "ASYSRCD00 00008   0  01   ";
				numeroDoc2 = proce.obtieneCorrelativo(str2);
				
				//Genera VECMAR con el Ingreso por los articulos no entregados por el transportista
				GeneraVecmar(exmtraDTO2.getCodigoEmpresa(), 11, fechaMvtoVenta, numeroDoc2, codDoc, exmtraDTO2.getBodegaDestino(), rutCobroVTA, dvCobroVTA, vecmar);
				
				insertVecmarCobro=1;
			}*/
			
			 //Genera VECMAR
			VedmarDTO vedmarDTO2 = new VedmarDTO();
			ExmarbDTO exmarbDTO = new ExmarbDTO();
			
			StockinventarioDTO stockinventarioDTO2 = new StockinventarioDTO();
			
			//Genera VEDMAR
			ExmarbDTO exmarb2 = exmarb.recuperaArticulo(26, dto.getCodigoArticulo());
			
			stockLineaArt = exmarb2.getStockLinea();
			//VedmarDTO vedmar = new VedmarDTO();
			//Genera VEDMAR con la Salida por el cobro al transportista
			GeneraVedmar(2, 21, fechaMvtoVenta, numeroDoc, 101010, 26, dto.getCodigoArticulo(), exmarb2.getDvArticulo(), dto.getStockRecepcionado(), exmarb2.getCodigoSector(), dto.getPrecioBruto(), dto.getPrecioNeto(), exmarb2.getCostoNeto(), "S", vedmar);
													
			//Genera VEDMAR con el Ingreso por los articulos no entregados por el transportista
			//GeneraVedmar(exmtraDTO2.getCodigoEmpresa(), 11, fechaMvtoVenta, numeroDoc2, correlativo, exmtraDTO2.getBodegaDestino(), dto.getCodigoArticulo(), exmarb2.getDvArticulo(), dto.getStockRecepcionado(), exmarb2.getCodigoSector(), dto.getPrecioBruto(), dto.getPrecioNeto(), exmarb2.getCostoNeto(), "I", vedmar);
			
		
			
			correlativo=correlativo+1;
			
		}//Cierre While
	}
}
