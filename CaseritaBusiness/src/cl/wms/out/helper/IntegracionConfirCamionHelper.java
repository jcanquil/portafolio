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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.plaf.synth.SynthSplitPaneUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dao.iface.CargcestDAO;
import cl.caserita.dao.iface.CargfwmsDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.CarguiodDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.DocGenelDAO;
import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.ExmvndDAO;
import cl.caserita.dao.iface.ExtariDAO;
import cl.caserita.dao.iface.FaccarguDAO;
import cl.caserita.dao.iface.LogcanpeDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedfaltDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dao.impl.ConnohDAOImpl;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.CargcestDTO;
import cl.caserita.dto.CargfwmsDTO;
import cl.caserita.dto.CarguioConfirmacionDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguioTranspDTO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ConfirmacionCarguioDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DocgenelDTO;
import cl.caserita.dto.DocuNoGeneradoDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmrecDTO;
import cl.caserita.dto.ExmvndDTO;
import cl.caserita.dto.FaccarguDTO;
import cl.caserita.dto.GuiasDTO;
import cl.caserita.dto.LogcanpeDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.RutservDTO;
import cl.caserita.dto.StockinventarioDTO;
import cl.caserita.dto.VecfwmsDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedfaltDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.emailInformacionErroresWMS;
import cl.caserita.enviomail.main.emailInformacionWMS;
import cl.caserita.enviomail.main.emailInformeShipProcesado;
import cl.caserita.enviomail.main.enviaMailStockNegativo;
import cl.caserita.enviomail.main.envioMailDocuNOFacturados;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.wms.integracion.helper.ActualizaStockInventarioHelper;

public class IntegracionConfirCamionHelper {
	
	private static Logger logi = Logger.getLogger(IntegracionConfirCamionHelper.class);
	private static emailInformacionErroresWMS email = new emailInformacionErroresWMS();
	private static enviaMailStockNegativo mailNegativo = new enviaMailStockNegativo();
	private static emailInformeShipProcesado mailShip = new emailInformeShipProcesado();

	public void procesaConfirmaCamion(String urlFile, String nameFile, String tipo){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			logi.info("Pruebas de ingreso");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			List ordenes = new ArrayList();
			ConfirmacionCarguioDTO confirma = new ConfirmacionCarguioDTO();
			List detalle = new ArrayList();
			doc.getDocumentElement().normalize();
			int numeroCarguio=0;
			int numeroTransferencia=0;
			String estadoInventario="";
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			List carguios = new ArrayList();
			NodeList nListin = doc.getElementsByTagName("MOVE_SEG");
			logi.info(nListin.getLength());
			logi.info("----------------------------");
			for (int temp = 0; temp < nListin.getLength(); temp++) {

					Node nNode = nListin.item(temp);
							
					logi.info("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if ("RAMPLA".equals(eElement.getElementsByTagName("CARCOD").item(0).getTextContent())){
							
						}else{
							StringTokenizer st = new StringTokenizer(eElement.getElementsByTagName("CARCOD").item(0).getTextContent(),"-");
							int campo=0;
							int rut=0;
							String dv="";
							 while (st.hasMoreTokens( )){
								 
								 	String tr = st.nextToken();
								 	logi.info(tr);
								 	if (isNumeric(tr)){
								 		if (campo==0){
									 		rut=Integer.parseInt(tr);
									 	}else if(campo==1){
									 		dv=tr;
									 	}
								 	}
								 	
								 campo++;
						        
						    }
							 	confirma.setRut(rut);
								confirma.setDv(dv);
						}
						
						 
						
							confirma.setCamion(eElement.getElementsByTagName("TRLR_ID").item(0).getTextContent());
							confirma.setAnden(eElement.getElementsByTagName("VC_ANDEN").item(0).getTextContent());
						
					}
			}
			
			
			NodeList nList = doc.getElementsByTagName("SHIPMENT_ORDER");
					logi.info(nList.getLength());
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					logi.info("NUMOV : " + eElement.getElementsByTagName("ORDNUM").item(0).getTextContent());
					confirma = obtieneDocu(eElement.getElementsByTagName("ORDNUM").item(0).getTextContent());
					logi.info("NUMOV REAL: " + confirma.getNumeroOV());
					int numeroOV=confirma.getNumeroOV();
					numeroCarguio = confirma.getNumeroCarguio();
					NodeList nList2 = doc.getElementsByTagName("SHIPMENT_LINE_SEG");
					//List nList2 = eElement.getChildren();
					//logi.info(nList2.getLength());
					
					logi.info("Estado Inventario :"+eElement.getElementsByTagName("INVSTS").item(0).getTextContent() ); 
					estadoInventario=eElement.getElementsByTagName("INVSTS").item(0).getTextContent();
					
					for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {

						Node nNode2 = nList2.item(temp2);
		
						logi.info("\nCurrent Element :" + nNode2.getNodeName());
								
						if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
							ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
							
							Element eElement2 = (Element) nNode2;
							//logi.info("TRNDTE : " + eElement2.getElementsByTagName("ORDNUM").item(0).getTextContent());

							//logi.info("TRNNUM : " + eElement2.getElementsByTagName("ORDLIN").item(0).getTextContent());
							ConfirmacionCarguioDTO confirma2 = obtieneDocu(eElement2.getElementsByTagName("ORDNUM").item(0).getTextContent());


							//logi.info("PRTNUM : " + eElement2.getElementsByTagName("PART_NUM").item(0).getTextContent());
							
							//logi.info("STKUOM : " + eElement2.getElementsByTagName("ORDERED_LINE_QTY").item(0).getTextContent());
							//logi.info("UNTQTY : " + eElement2.getElementsByTagName("SHIPMENT_LINE_QTY").item(0).getTextContent());
							detalleDTO.setCodArticulo(Integer.parseInt(eElement2.getElementsByTagName("PART_NUM").item(0).getTextContent()));
							detalleDTO.setCantidadDespachada(Integer.parseInt(eElement2.getElementsByTagName("SHIPMENT_LINE_QTY").item(0).getTextContent()));
							detalleDTO.setCantidad(Integer.parseInt(eElement2.getElementsByTagName("SHIPMENT_LINE_QTY").item(0).getTextContent()));
							detalleDTO.setFechaExpiracion(Integer.parseInt(eElement2.getElementsByTagName("EXPIRE_DTE").item(0).getTextContent()));
							if (confirma2.getNumeroOV()==numeroOV){
								detalle.add(detalleDTO);

							}
						}
					}
					
					confirma.setArticulos(detalle);
					logi.info("NUMERO OV ASIGNADO A ARREGLO :"+confirma.getNumeroOV());
					ordenes.add(confirma);
					detalle = new ArrayList();
				}
				

			}
			if ("CAMION".equals(tipo)){
				//procesaFacturacion(2, 26, numeroCarguio, ordenes,"", estadoInventario);

			}else if ("MERMA".equals(tipo)){
				
			}
			//Mueva archivo a carpeta de procesados
			moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
	}
	public void procesaConfirmaCamion2(String urlFile,  String tipo, String nombreArchivo){
		
		String estadoInventario="";
		
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse(fXmlFile);
			
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
		
			
			List ordenes = new ArrayList();
			ConfirmacionCarguioDTO confirma = new ConfirmacionCarguioDTO();
			List detalle = new ArrayList();
			List carguios = new ArrayList();
			doc.getDocumentElement().normalize();
			int numeroCarguio=0;
			int numeroCarguioTrans=0;
			//logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			logi.info(" P R O C E S O _ F A C T U R A C I O N : "+nombreArchivo);
			NodeList nListin = doc.getElementsByTagName("MOVE_SEG");
			//logi.info(nListin.getLength());
			//logi.info("----------------------------");
			for (int temp = 0; temp < nListin.getLength(); temp++) {

					Node nNode = nListin.item(temp);
					
					//logi.info("\nCurrent Element :" + nNode.getNodeName());
					
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if ("RAMPLA".equals(eElement.getElementsByTagName("CARCOD").item(0).getTextContent())){
							confirma.setRut(0);
							confirma.setDv("");
						}else{
							StringTokenizer st = new StringTokenizer(eElement.getElementsByTagName("CARCOD").item(0).getTextContent(),"-");
							int campo=0;
							int rut=0;
							String dv="";
							 while (st.hasMoreTokens( )){
								 
								 	String tr = st.nextToken();
								 	//logi.info(tr);
								 	if (isNumeric(tr)){
								 		if (tipo.compareTo("MERMA")!=0){
									 		if (campo==0){
										 		rut=Integer.parseInt(tr);
										 	}else if(campo==1){
										 		dv=tr;
										 	}
									 	}
								 	}
								 	
								 	
								 campo++;
						        
						    }
							 
							confirma.setRut(rut);
							confirma.setDv(dv);
						}
						
						confirma.setCamion(eElement.getElementsByTagName("TRLR_ID").item(0).getTextContent());
						confirma.setAnden(eElement.getElementsByTagName("VC_ANDEN").item(0).getTextContent());

					}
			}
			
			CarguioConfirmacionDTO carguioConfir=new CarguioConfirmacionDTO();

			NodeList nList = doc.getElementsByTagName("SHIPMENT_ORDER");
				//	logi.info(nList.getLength());
			//logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				//logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					//logi.info("NUMOV : " + eElement.getElementsByTagName("ORDNUM").item(0).getTextContent());
					ConfirmacionCarguioDTO confirma3 = obtieneDocu(eElement.getElementsByTagName("ORDNUM").item(0).getTextContent());
					confirma.setNumeroCarguio(confirma3.getNumeroCarguio());
					confirma.setNumeroOV(confirma3.getNumeroOV());
					confirma.setNumeroCarguioTransf(confirma3.getNumeroCarguioTransf());
					confirma.setCorrelativoDireccion(confirma3.getCorrelativoDireccion());
					//logi.info("NUMOV REAL: " + confirma.getNumeroOV());
					int numeroOV=confirma.getNumeroOV();
					numeroCarguio = confirma.getNumeroCarguio();
					NodeList nList2 = doc.getElementsByTagName("SHIPMENT_LINE_SEG");
					//List nList2 = eElement.getChildren();
					//logi.info(nList2.getLength());
					
					//logi.info("Estado Inventario :"+eElement.getElementsByTagName("INVSTS").item(0).getTextContent() ); 
					estadoInventario=eElement.getElementsByTagName("INVSTS").item(0).getTextContent();
					
					for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
						
						Node nNode2 = nList2.item(temp2);
		
					//	logi.info("\nCurrent Element :" + nNode2.getNodeName());
								
						if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
							ConfirmacionCarguioDetalleDTO detalleDTO = new ConfirmacionCarguioDetalleDTO();
							
							Element eElement2 = (Element) nNode2;
							//logi.info("TRNDTE : " + eElement2.getElementsByTagName("ORDNUM").item(0).getTextContent());

							//logi.info("TRNNUM : " + eElement2.getElementsByTagName("ORDLIN").item(0).getTextContent());
							ConfirmacionCarguioDTO confirma2=null;
							if ("CAMION".equals(tipo)){
								 confirma2 = obtieneDocu(eElement2.getElementsByTagName("ORDNUM").item(0).getTextContent());
								 
							}else{
								confirma2 = new ConfirmacionCarguioDTO();
								 //confirma2.setNumeroOV(Integer.parseInt(eElement2.getElementsByTagName("ORDNUM").item(0).getTextContent()));
								confirma2.setNumeroOV(confirma.getNumeroCarguio());
							}
							
							//logi.info("PRTNUM : " + eElement2.getElementsByTagName("PART_NUM").item(0).getTextContent());
							
							//logi.info("STKUOM : " + eElement2.getElementsByTagName("ORDERED_LINE_QTY").item(0).getTextContent());
							//logi.info("UNTQTY : " + eElement2.getElementsByTagName("SHIPMENT_LINE_QTY").item(0).getTextContent());
							detalleDTO.setCorrelativo(Integer.parseInt(eElement2.getElementsByTagName("ORDLIN").item(0).getTextContent()));
							detalleDTO.setCodArticulo(Integer.parseInt(eElement2.getElementsByTagName("PART_NUM").item(0).getTextContent()));
							detalleDTO.setCantidadDespachada(Integer.parseInt(eElement2.getElementsByTagName("SHIPMENT_LINE_QTY").item(0).getTextContent()));
							detalleDTO.setCantidad(Integer.parseInt(eElement2.getElementsByTagName("SHIPMENT_LINE_QTY").item(0).getTextContent()));
							if (detalleDTO.getCantidadDespachada()>0){
								if (eElement2.getElementsByTagName("EXPIRE_DTE").item(0)!=null){
									if (eElement2.getElementsByTagName("EXPIRE_DTE").item(0).getTextContent()!=null){
										detalleDTO.setFechaExpiracion(Integer.parseInt(eElement2.getElementsByTagName("EXPIRE_DTE").item(0).getTextContent().substring(0, 8)));

									}else{
										detalleDTO.setFechaExpiracion(0);

									}
								}else{
									detalleDTO.setFechaExpiracion(0);

								}

							}else{
								detalleDTO.setFechaExpiracion(0);

							}

							if (confirma2.getNumeroOV()==numeroOV){
								detalle.add(detalleDTO);

							}
						}
					}
					
					confirma.setArticulos(detalle);
					//logi.info("NUMERO OV ASIGNADO A ARREGLO :"+confirma.getNumeroOV());
					ordenes.add(confirma);
					String anden = confirma.getAnden();
					String camion = confirma.getCamion();
					
					 
					int rut= confirma.getRut();
					String dv = confirma.getDv();
					detalle = new ArrayList();
					confirma = new ConfirmacionCarguioDTO();
					confirma.setAnden(anden);
					confirma.setCamion(camion);
					confirma.setRut(rut);
					confirma.setDv(dv);
					confirma.setNumeroCarguio(numeroCarguio);
				}
				
			}
			List lista = convierteTransferencia(ordenes);
			if ("CAMION".equals(tipo)){
				List carg = new ArrayList();
				Iterator iter5 = lista.iterator();
				int numeroP=0;
				while (iter5.hasNext()){
					DocuNoGeneradoDTO docu = new DocuNoGeneradoDTO();
					CarguioConfirmacionDTO dto = (CarguioConfirmacionDTO) iter5.next();
					logi.info(" P R O C E S A _ C A R G U I O : "+dto.getNumeroCarguio());
					procesaFacturacion(2, 26, dto.getNumeroCarguio(), dto.getOrdenes(), nombreArchivo, estadoInventario, dto.getNumeroCarguioTransferencia());
					docu.setNumeroOrden(dto.getNumeroCarguio());
					docu.setNumeroDocumento(dto.getNumeroCarguioTransferencia());
					if (numeroP==0){
						if (dto.getNumeroCarguioTransferencia()>0 && dto.getNumeroCarguioTransferencia()!=dto.getNumeroCarguio()){
							//Cambio de estado a carguio transferencia
							DAOFactory dao = DAOFactory.getInstance();
							CarguioDAO cargdao = dao.getCarguioDAO();
							cargdao.actualizarestadoCarguio(2, 26, dto.getNumeroCarguioTransferencia(), "E");
							
						}
					}
					
					numeroP++;
					carg.add(docu);
				}
				//procesaFacturacion(2, 26, numeroCarguio, ordenes, nombreArchivo);
			//	mailShip.mail("", carg, Correo, nombreArchivo);
			}else if ("MERMA".equals(tipo)){
				logi.info("PROCESA MERMA GENERACION GUIA");
				procesaMerma(2, 26, numeroCarguio, ordenes, nombreArchivo);
			}
			//Mueva archivo a carpeta de procesados
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());

		}
		
		
	}
	public List convierteTransferencia(List lista){
		List listaTrans = new ArrayList();
		List ordenes = new ArrayList();
		Iterator iter = lista.iterator();
		CarguioConfirmacionDTO confirma = new CarguioConfirmacionDTO();

		int numeroCargui=0;
		while (iter.hasNext()){
			ConfirmacionCarguioDTO dto = (ConfirmacionCarguioDTO) iter.next();
			
			if (numeroCargui==0){
				numeroCargui=dto.getNumeroCarguio();
				confirma.setNumeroCarguioTransferencia(dto.getNumeroCarguioTransf());
				confirma.setNumeroCarguio(dto.getNumeroCarguio());
				confirma.setAnden(dto.getAnden());
				ordenes.add(dto);
			}else{
				if (numeroCargui==dto.getNumeroCarguio()){
					ordenes.add(dto);

				}
				if (numeroCargui!=dto.getNumeroCarguio()){
					confirma.setOrdenes(ordenes);
					listaTrans.add(confirma);
					confirma = new CarguioConfirmacionDTO();
					ordenes = new ArrayList();
					numeroCargui=dto.getNumeroCarguio();
					confirma.setNumeroCarguioTransferencia(dto.getNumeroCarguioTransf());
					confirma.setNumeroCarguio(dto.getNumeroCarguio());
					confirma.setAnden(dto.getAnden());
					ordenes.add(dto);
					
				}
			}
		}
		confirma.setOrdenes(ordenes);
		listaTrans.add(confirma);
		logi.info("RUTA");
		return listaTrans;
	}
	public ConfirmacionCarguioDTO obtieneDocu(String numero){
		ConfirmacionCarguioDTO dto = new ConfirmacionCarguioDTO();
		StringTokenizer st = new StringTokenizer(numero,"-");
		int campo=0;
		String xmlLog="";
		 while (st.hasMoreTokens( )){
			 
			 	String tr = st.nextToken();
			 	//logi.info(tr);
			 	if (campo==0){
			 		if (isNumeric(tr)){
				 		dto.setNumeroCarguioTransf(Integer.parseInt(tr.trim()));

			 		}
			 	}
			 	else if (campo==1){
			 		dto.setNumeroCarguio(Integer.parseInt(tr.trim()));
			 	}else if(campo==2){
			 		dto.setNumeroOV(Integer.parseInt(tr.trim()));

			 	}else if (campo==3){
			 		dto.setCorrelativoDireccion(Integer.parseInt(tr.trim()));
			 	}
			 campo++;
	        
	    }
		return dto;
	}
	
	public void procesaMerma(int empresa, int bodega, int numeroCarguio, List carguioWMS, String nombreArchivo){
		DAOFactory dao = DAOFactory.getInstance();
		VecmarDAO vecmar = dao.getVecmarDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		VecfwmsDAO vecfwms = dao.getVecfwmsDAO();
		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		StockinventarioDAO stockDAO = dao.getStockinventarioDAO();
		ActualizaStockInventarioHelper inventario = new ActualizaStockInventarioHelper();
		Fecha fch = new Fecha();
		int totalMerma=0;
		int totalNeto=0;
		try{
			if (numeroCarguio>0){
				VecmarDTO vecmarDTO = vecmar.obtenerDatosVecmarMermasWMS(empresa, 24, numeroCarguio);
				ConfirmacionCarguioDTO dto = null;
				HashMap detalle = vedmar.obtenerDatosVedmarMerHash(empresa, vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento());
				Iterator iter = carguioWMS.iterator();
				int correlativoDetalle=0;
				while (iter.hasNext()){
					 dto = (ConfirmacionCarguioDTO) iter.next();
					
					Iterator iter2 = dto.getArticulos().iterator();
					while (iter2.hasNext()){
						ConfirmacionCarguioDetalleDTO dto2 = (ConfirmacionCarguioDetalleDTO) iter.next();
						VedmarDTO vedmarDTO = (VedmarDTO) detalle.get(dto2.getCodArticulo());
						if (vedmarDTO.getCantidadArticulo()!=dto2.getCantidad()){
							//Actualiza VEDMAR
							vedmarDTO.setCantidadArticulo(dto2.getCantidad());
							vedmarDTO.setCantidadFormato(dto2.getCantidad());
							vedmarDTO.setCostoTotalNeto(vedmarDTO.getCostoNeto()*dto2.getCantidad());
							vedmarDTO.setMontoBrutoLinea(vedmarDTO.getPrecioUnidad()*dto2.getCantidad());
							vedmarDTO.setMontoNeto(vedmarDTO.getPrecioNeto()*dto2.getCantidad());
							int montoTotalNeto = (int) vedmarDTO.getPrecioNeto()*dto2.getCantidad();
							vedmarDTO.setMontoTotalNetoLinea(montoTotalNeto);
							int montoTotal = (int) vedmarDTO.getPrecioUnidad()*dto2.getCantidad();
							vedmarDTO.setMontoTotalLinea(montoTotal);
							vedmar.actualizaMerma(vedmarDTO);
							totalMerma=totalMerma+montoTotal;
							totalNeto=totalNeto+montoTotalNeto;
						//	logi.info("Total :"+totalMerma);
						//	logi.info("Total Neto :"+totalNeto);

							inventario.actualizaEstado(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodigoBodega(), "D", vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(),dto2.getCantidad(), stockDAO, nombreArchivo, correlativoDetalle);
						}else{
							totalMerma=totalMerma+vedmarDTO.getMontoTotalLinea();
							totalNeto=totalNeto+vedmarDTO.getMontoTotalNetoLinea();
							inventario.actualizaEstado(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodigoBodega(), "D", vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(),vedmarDTO.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);
						//	logi.info("Total2 :"+totalMerma);
						//	logi.info("Total Neto2 :"+totalNeto);
						}
						correlativoDetalle++;
					}
					
						
				}
				//Actualiza VECMAR
				VecmarDTO vecmarDTO2 = vedmar.recuperaTotales(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento());
				//logi.info("Total neto :"+vecmarDTO2.getTotalNeto());
				//logi.info("Total :"+vecmarDTO2.getTotalDocumento());
				vecmar.actualizaVecmarMerma(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento(), vecmarDTO2.getTotalNeto(), vecmarDTO2.getTotalDocumento());
				
				proce.procesaFacturacion(String.valueOf(vecmarDTO.getCodigoEmpresa()), String.valueOf(vecmarDTO.getCodTipoMvto()), String.valueOf(vecmarDTO.getFechaMvto()), String.valueOf(vecmarDTO.getNumDocumento()),  "38", String.valueOf(vecmarDTO.getRutProveedor()), vecmarDTO.getDvProveedor(), "AMS", "M", "0");
				vecmar.actualizaDisponibilidadImpresion(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento(), dto.getAnden().trim());
				VecfwmsDTO vecfwmsDTO = new VecfwmsDTO();
				vecfwmsDTO.setCodigoEmpresa(vecmarDTO.getCodigoEmpresa());
				vecfwmsDTO.setTipoMovto(vecmarDTO.getCodTipoMvto());
				vecfwmsDTO.setFechaMovimiento(vecmarDTO.getFechaMvto());
				vecfwmsDTO.setNumeroDocumento(vecmarDTO.getNumDocumento());
				vecfwmsDTO.setNombreArchivoXML(nombreArchivo);
				vecfwmsDTO.setTipo("M");
				vecfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
				vecfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
				//MODIFICA SWITCH 
				vecmar.actualizaSwitchVecmar(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento(), 0);
				vedmar.actualizaSwitchVecmar(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento(), 0);
			}
		}catch(Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());

		}
		
		
		
		
	}
	
	public void procesaFechaExpiracion(List articulo, int empresa, int bodega, int numeroCarguio, int numeroOV, String patente, CarguiodDAO carguioD, DetordDAO detord, ExmartDAO exmart, int corrDireccionDespa, int TipoVTa){
		
		int cantFormato=0;
		logi.info("Cambio JRAMIREZ 20170203");
		//JR ***********INICIO
				int cantArticulo=0;
				int cantOVCarguio=0;
				int cantDespaXML=0;
				int cantDespaFinal=0;
				int cantOVReal=0;
				//JR ***********FIN
				
				
		try{
			Iterator iter = articulo.iterator();
			while (iter.hasNext()){
				ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
				CarguiodDTO carguiodDTO = new CarguiodDTO();
				
				DetordDTO detordDTO = new DetordDTO();
				
				//JR ***********INICIO
				//Cambio a unidad el articulo en el CARGUIOD
				CarguiodDTO carguiodDTO2 = carguioD.buscaDetalleCarguiodArt(empresa, numeroCarguio, patente, bodega, numeroOV, dto.getCodArticulo(), corrDireccionDespa);
				
				if (carguioD.buscaDetalleCarguiodArt(empresa, numeroCarguio, patente, bodega, numeroOV, dto.getCodArticulo(), corrDireccionDespa)!=null)
				{
					//busco datos de Pallet, Display, Caja del articulo					
					ExmartDTO exmartDTO = exmart.recuperaArticuloSinDigito(dto.getCodArticulo());
					
					if ("C".equals(carguiodDTO2.getFormatoArt().trim())){
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0){
							cantFormato=carguiodDTO2.getCantidad() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0){
							cantFormato=carguiodDTO2.getCantidad() * exmartDTO.getCaja();
						}
						
						if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0){
							cantFormato=carguiodDTO2.getCantidad() * exmartDTO.getDisplay();
						}
					}
					
					if ("D".equals(carguiodDTO2.getFormatoArt().trim())){
						if (exmartDTO.getDisplay()>0){
							cantFormato=carguiodDTO2.getCantidad() * exmartDTO.getDisplay();
						}
					}
					
					if ("P".equals(carguiodDTO2.getFormatoArt().trim())){
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()>0){
							cantFormato=carguiodDTO2.getCantidad() * (exmartDTO.getPallet() * exmartDTO.getCaja() * exmartDTO.getDisplay());
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
							cantFormato=carguiodDTO2.getCantidad() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()==0){
							cantFormato=carguiodDTO2.getCantidad() * exmartDTO.getCaja();
						}
						
						if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
							cantFormato=carguiodDTO2.getCantidad() * exmartDTO.getDisplay();
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()>0){
							cantFormato=carguiodDTO2.getCantidad() * (exmartDTO.getPallet() * exmartDTO.getCaja());
						};
					}
					
					if ("U".equals(carguiodDTO2.getFormatoArt().trim())){
						cantFormato=carguiodDTO2.getCantidad();
					}
					
					//Cantidad en unidades de lo inicialmente despachado por SYSCON para el CARGUIO
					cantOVCarguio=cantFormato;
					
					//Cantidad despachada por WMS
					cantDespaXML=dto.getCantidadDespachada();
					
					cantDespaFinal=cantOVCarguio-cantDespaXML;
					
				}
				//JR ***********FIN
				
				carguiodDTO.setCodigoEmpresa(empresa);
				carguiodDTO.setCodigoBodega(bodega);
				carguiodDTO.setNumeroCarguio(numeroCarguio);
				carguiodDTO.setPatente(patente.trim());
				carguiodDTO.setNumeroOrden(numeroOV);
				carguiodDTO.setCodigoArticulo(dto.getCodArticulo());
				carguiodDTO.setCantidad(dto.getCantidadDespachada());
				carguiodDTO.setFechaExpiracion(dto.getFechaExpiracion());
				carguiodDTO.setFormatoArt("U");
				carguiodDTO.setCorrelativoDireccion(corrDireccionDespa);
				carguioD.actualizaCarguiod(carguiodDTO);
				
				//Valido si las cantidades a despachar son distintas al formato UNIDAD en DETORD
				
				detordDTO.setCodEmpresa(empresa);
				detordDTO.setNumOvVenta(numeroOV);
				detordDTO.setCodigoBodega(bodega);
				detordDTO.setCodigoArticulo(dto.getCodArticulo());
				detordDTO.setCorrelativoDespacho(corrDireccionDespa);
				if (detord.buscaOVCarguio(detordDTO)!=null){
					
					DetordDTO detordDTO2 = detord.buscaOVCarguio(detordDTO);
					
					//busco datos de Pallet, Display, Caja del articulo					
					ExmartDTO exmartDTO = exmart.recuperaArticuloSinDigito(dto.getCodArticulo());
					
					if ("C".equals(detordDTO2.getFormato().trim())){
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0){
							cantFormato=detordDTO2.getCantidadFormato() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
							
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
							//JR ***********FIN
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0){
							cantFormato=detordDTO2.getCantidadFormato() * exmartDTO.getCaja();
							
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * exmartDTO.getCaja();
							//JR ***********FIN
							
						}
						
						if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0){
							cantFormato=detordDTO2.getCantidadFormato() * exmartDTO.getDisplay();
							
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * exmartDTO.getDisplay();
							//JR ***********FIN
						}
					}
					
					if ("D".equals(detordDTO2.getFormato().trim())){
						if (exmartDTO.getDisplay()>0){
							cantFormato=detordDTO2.getCantidadFormato() * exmartDTO.getDisplay();
							
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * exmartDTO.getDisplay();
							//JR ***********FIN
						}
					}
					
					if ("P".equals(detordDTO2.getFormato().trim())){
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()>0){
							cantFormato=detordDTO2.getCantidadFormato() * (exmartDTO.getPallet() * exmartDTO.getCaja() * exmartDTO.getDisplay());
							
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * (exmartDTO.getPallet() * exmartDTO.getCaja() * exmartDTO.getDisplay());
							//JR ***********FIN
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
							cantFormato=detordDTO2.getCantidadFormato() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
							
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * (exmartDTO.getCaja() * exmartDTO.getDisplay());
							//JR ***********FIN
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()==0){
							cantFormato=detordDTO2.getCantidadFormato() * exmartDTO.getCaja();
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * exmartDTO.getCaja();
							//JR ***********FIN
						}
						
						if (exmartDTO.getCaja()==0 && exmartDTO.getDisplay()>0 && exmartDTO.getPallet()==0){
							cantFormato=detordDTO2.getCantidadFormato() * exmartDTO.getDisplay();
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * exmartDTO.getDisplay();
							//JR ***********FIN
						}
						
						if (exmartDTO.getCaja()>0 && exmartDTO.getDisplay()==0 && exmartDTO.getPallet()>0){
							cantFormato=detordDTO2.getCantidadFormato() * (exmartDTO.getPallet() * exmartDTO.getCaja());
							//JR ***********INICIO
							cantArticulo=detordDTO2.getCantidadDespachada() * (exmartDTO.getPallet() * exmartDTO.getCaja());
							//JR ***********FIN
						};
					}
					
					if ("U".equals(detordDTO2.getFormato().trim())){
						cantFormato=detordDTO2.getCantidadFormato();
						//JR ***********INICIO
						cantArticulo=detordDTO2.getCantidadDespachada();
						//JR ***********FIN
					}
				}
				
				//JR ***********INICIO
				if (cantDespaFinal>0){
					cantOVReal=cantArticulo-cantDespaFinal;
				}
				else{
					cantOVReal=cantArticulo;
				}
				//JR ***********FIN
				//Actualiza DETORD
				detordDTO.setCodEmpresa(empresa);
				detordDTO.setCodigoBodega(bodega);
				detordDTO.setNumOvVenta(numeroOV);
				detordDTO.setCodigoArticulo(dto.getCodArticulo());
				detordDTO.setFormato("U");
				
				//JR ***********INICIO
				detordDTO.setCantidadDespachada(cantOVReal);
				//detordDTO.setCantidadDespachada(dto.getCantidadDespachada());
				//JR ***********FIN
				
				//detordDTO.setCantidadDespachada(dto.getCantidadDespachada());
				detordDTO.setCantidadFormato(cantFormato);
				detordDTO.setCorrelativoDespacho(corrDireccionDespa);
				
				if (TipoVTa==1){
					detord.actualizaUnidOVVTAMayorista(detordDTO);
				}
				else{
					detord.actualizaUnidadesOV(detordDTO);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
	}
	
	public void procesaFechaExpiracionOT(List articulo, int empresa, int bodega, int numeroCarguio, int numeroOV, String patente, CarguiodDAO carguioD,
			int bodegaOrigenOT, int bodegaDestinoOT, ExdtraDAO exdtra, ExmartDAO exmart){
		
		double cantDespachada=0;
		double cantDespachoPendiente=0;
		try{
			Iterator iter = articulo.iterator();
			while (iter.hasNext()){
				ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
				CarguiodDTO carguiodDTO = new CarguiodDTO();
				
				ExdtraDTO exdtraDTO = new ExdtraDTO();
				
				carguiodDTO.setCodigoEmpresa(empresa);
				carguiodDTO.setCodigoBodega(bodega);
				carguiodDTO.setNumeroCarguio(numeroCarguio);
				carguiodDTO.setPatente(patente.trim());
				carguiodDTO.setNumeroOrden(numeroOV);
				carguiodDTO.setFechaExpiracion(dto.getFechaExpiracion());
				carguiodDTO.setCodigoBodegaOrigenOT(bodegaOrigenOT);
				carguiodDTO.setCodigoBodegaDestinoOT(bodegaDestinoOT);
				carguiodDTO.setFormatoArt("U");
				carguiodDTO.setCodigoArticulo(dto.getCodArticulo());
				carguiodDTO.setCantidad(dto.getCantidadDespachada());
				
				carguioD.actualizaCarguiodOT(carguiodDTO);
				
				//Valido si las cantidades a despachar son distintas al formato UNIDAD en EXDTRA
				exdtraDTO.setCodEmpresa(empresa);
				exdtraDTO.setNumTraspaso(numeroOV);
				exdtraDTO.setCodArticulo(dto.getCodArticulo());
				if (exdtra.buscaOTCarguio(exdtraDTO)!=null){
					
					ExdtraDTO exdtraDTO2 = exdtra.buscaOTCarguio(exdtraDTO);
					
					//busco datos de Pallet, Display, Caja del articulo					
					ExmartDTO exmartDTO = exmart.recuperaArticuloSinDigito(dto.getCodArticulo());
					
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
				}
				
				//Actualiza EXDTRA
				exdtraDTO.setCodEmpresa(empresa);
				exdtraDTO.setNumTraspaso(numeroOV);
				exdtraDTO.setCodArticulo(dto.getCodArticulo());
				exdtraDTO.setFormato("U");
				exdtraDTO.setCantDespachada(cantDespachada);
				exdtraDTO.setCantPendiente(cantDespachoPendiente);
				exdtraDTO.setCantRecepCarguio(dto.getCantidadDespachada());
				
				exdtra.actualizaUnidadesOT(exdtraDTO);
				
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
	}
	
	
	public int obtieneCantidad(List lista , int articulo){
		int cantidad=0;
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
			/*logi.info("Articulo :"+dto.getCodArticulo());
			logi.info("Articulo 2:"+articulo);
			logi.info("Cantidad :"+dto.getCantidad());*/
			if (dto.getCodArticulo()==articulo){
				cantidad=dto.getCantidad();
				break;
			}
		}
		return cantidad;
	}
	public int procesaGuia(OrdvtaDTO ord, VecmarDAO vecmar, VedmarDAO vedmar, ProcedimientoDAO proce, 
			ExmarbDAO exmarb, TpacorDAO tpacor, String anden, DocGenelDAO docgenel,CarguioDTO carguio, CarguiodDAO carguioD, RutservDTO rutserv, List articulo, String estInventario, StockinventarioDAO stockDAO, String nombreArchivo){
		int resul=0;
		List gui = new ArrayList();
		int fecha=0;
		int numero=0;
		int cantidLineas=0;
		int totalNeto=0;
		Fecha fch = new Fecha();
		int iMovVedmar=0;
		int correlativoDetalle=0;
		try{
			Iterator iter = ord.getDetord().iterator();
			while (iter.hasNext()){
				DetordDTO det = (DetordDTO) iter.next();
				//Mueve creacion VEDMAR
				VedmarDTO ved = new VedmarDTO();
				
				iMovVedmar=0;
				//Valido la cantidad para que no se inserte en VEDMAR si esta en 0
				int can2 = obtieneCantidad(articulo, det.getCodigoArticulo());
				logi.info("cn2 :"+can2);
				logi.info("ccantidad articulo :"+det.getCantidadArticulo());
				if (can2>0){
					iMovVedmar=1;
				}else{
					if(det.getCantidadArticulo()>0){
						iMovVedmar=1;
					}
				}
				logi.info("iMov :"+iMovVedmar);
				if (iMovVedmar==1)
				{
					ExmarbDTO exm = exmarb.recuperaArticulo(ord.getCodigoBodega(), det.getCodigoArticulo());
					if (fecha!=det.getFechaDespacho())
					{
						// numero = tpacor.recupeCorrelativo(0, 46);
						String str = "ASYSRCD00 00008   0  01   ";
						numero = proce.obtieneCorrelativo(str);
						logi.info("Correlativo Documento Guia: "+numero);
						GuiasDTO guia = new GuiasDTO();
						guia.setNumeroGuia(numero);
						//guia.setFechaDespacho(det.getFechaDespacho());
						guia.setFechaDespacho(Integer.parseInt(fch.getYYYYMMDD()));
						gui.add(guia);
						ved.setCodigoEmpresa(det.getCodEmpresa());
						ved.setCodTipoMvto(39);
						//ved.setFechaMvto(det.getFechaDespacho());
						ved.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
						ved.setNumDocumento(numero);
						ved.setCorrelativo(det.getCorrelativoDetalleOV());
						ved.setCodigoBodega(ord.getCodigoBodega());
						int can = obtieneCantidad(articulo, det.getCodigoArticulo());
						if (can>0)
						{
							ved.setCantidadArticulo(can);
							ved.setCantidadFormato(can);
						}else
						{
							//ved.setCantidadArticulo(det.getCantidadArticulo());
							//JR ***********INICIO
							ved.setCantidadArticulo(det.getCantidadArticulo());
							ved.setCantidadFormato(det.getCantidadArticulo());
							
							//ved.setCantidadArticulo(det.getCantidadFormato());
							//ved.setCantidadFormato(det.getCantidadFormato());
							//JR ***********FIN
						}
						ved.setCodigoArticulo(det.getCodigoArticulo());
						ved.setDigArticulo(exm.getDvArticulo().trim());
						ved.setFormato(det.getFormato().trim());
						
						ved.setSectorBodega(exm.getCodigoSector());
						ved.setPrecioUnidad(det.getPrecioBruto());
						ved.setPorcentajeDesto(det.getPorcentaje());
						ved.setPrecioNeto(det.getPrecioNeto());
						ved.setCostoNeto(det.getCostoNeto());
						ved.setCostoTotalNeto(det.getCostoTotal());
						
						
						ved.setMontoBrutoLinea(det.getPrecioNeto()*ved.getCantidadArticulo());
						ved.setMontoNeto(det.getPrecioNeto()*ved.getCantidadArticulo());
						//logi.info("Montos:"+det.getPrecioNeto()+ved.getCantidadArticulo());
						//logi.info("Calculo:"+det.getPrecioNeto()*ved.getCantidadArticulo());
						double montneto =  det.getPrecioNeto()*ved.getCantidadArticulo();
						
						int montoFinal = (int)montneto;
						ved.setMontoTotalNetoLinea(montoFinal);
						ved.setMontoDescuentoLinea(det.getDescuento());
						ved.setMontoDescuentoNeto(det.getDescuentoNeto());
						ved.setMontoTotalLinea(montoFinal);
						ved.setCodIngresoSalida("S");
						totalNeto=totalNeto+ved.getMontoTotalNetoLinea();
						cantidLineas++;
						vedmar.generaMovimiento(ved);
						
						fecha=det.getFechaDespacho();
					}
					else
					{
						//ExmarbDTO exm = exmarb.recuperaArticulo(ord.getCodigoBodega(), det.getCodigoArticulo());
						
						ved.setCodigoEmpresa(det.getCodEmpresa());
						ved.setCodTipoMvto(39);
						//ved.setFechaMvto(det.getFechaDespacho());
						ved.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
						ved.setNumDocumento(numero);
						ved.setCorrelativo(det.getCorrelativoDetalleOV());
						ved.setCodigoBodega(ord.getCodigoBodega());
						ved.setCodigoArticulo(det.getCodigoArticulo());
						ved.setDigArticulo(exm.getDvArticulo().trim());
						ved.setFormato(det.getFormato().trim());
						
						//JR ***********INICIO
						ved.setCantidadArticulo(det.getCantidadArticulo());
						//ved.setCantidadArticulo(det.getCantidadFormato());
						
						ved.setCantidadFormato(det.getCantidadArticulo());
						//ved.setCantidadFormato(det.getCantidadFormato());
						//JR ***********FIN
						
						/*//ved.setCantidadArticulo(det.getCantidadArticulo());
						ved.setCantidadArticulo(det.getCantidadFormato());
						
						ved.setCantidadFormato(det.getCantidadFormato());*/
						ved.setSectorBodega(exm.getCodigoSector());
						ved.setPrecioUnidad(det.getPrecioBruto());
						ved.setPrecioNeto(det.getPrecioNeto());
						ved.setCostoNeto(det.getCostoNeto());
						ved.setCostoTotalNeto(det.getCostoTotal());
						
						/*
						ved.setMontoBrutoLinea(det.getPrecioNeto()*ved.getCantidadArticulo());
						ved.setMontoNeto(det.getPrecioNeto()*ved.getCantidadArticulo());
						double montneto =  det.getPrecioNeto()*ved.getCantidadArticulo();
						*/
						
						/*ved.setMontoBrutoLinea(det.getPrecioNeto()*det.getCantidadFormato());
						ved.setMontoNeto(det.getPrecioNeto()*det.getCantidadFormato());
						double montneto =  det.getPrecioNeto()*det.getCantidadFormato();*/
						
						//JR ***********INICIO
						ved.setMontoBrutoLinea(det.getPrecioNeto()*ved.getCantidadArticulo());
						ved.setMontoNeto(det.getPrecioNeto()*ved.getCantidadArticulo());
						double montneto =  det.getPrecioNeto()*ved.getCantidadArticulo();
						
						/*
						ved.setMontoBrutoLinea(det.getPrecioNeto()*det.getCantidadFormato());
						ved.setMontoNeto(det.getPrecioNeto()*det.getCantidadFormato());
						double montneto =  det.getPrecioNeto()*det.getCantidadFormato();
						*/
						//JR ***********FIN

						int montoFinal = (int)montneto;
						
						//int montneto = (int) det.getPrecioNeto()*ved.getCantidadArticulo();
						//ved.setMontoTotalNetoLinea(montneto);
						ved.setMontoTotalNetoLinea(montoFinal);
						ved.setPorcentajeDesto(det.getPorcentaje());
						ved.setMontoDescuentoLinea(det.getDescuento());
						ved.setMontoDescuentoNeto(det.getDescuentoNeto());
						//ved.setMontoTotalLinea(montneto);
						ved.setMontoTotalLinea(montoFinal);
						ved.setCodIngresoSalida("S");
						totalNeto=totalNeto+ved.getMontoTotalNetoLinea();
						cantidLineas++;
						vedmar.generaMovimiento(ved);
					}
					
					logi.info("ACTUALIZA STOCK DE INVENTARIO");
					//Actualiza el stock en linea por articulo (Rebaja)
					ActualizaStockInventarioHelper actualiza = new ActualizaStockInventarioHelper();
					
					/*actualiza.actualizaEstado(det.getCodEmpresa(), ord.getCodigoBodega(), estInventario, det.getCodigoArticulo(), exm.getDvArticulo().trim(), 
							det.getCantidadFormato(), stockDAO, nombreArchivo, correlativoDetalle);*/
					//Se cambian variables de cantidad JCANQUIL 20170208
					actualiza.actualizaEstado(det.getCodEmpresa(), ord.getCodigoBodega(), estInventario, det.getCodigoArticulo(), exm.getDvArticulo().trim(), 
							det.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);
					correlativoDetalle++;
					//Si stock de inventario de WMS es distinto a DISPONIBLE actualizo el stock el linea
					//if (estInventario.compareTo("D")<0)
					if ("D".equals(estInventario))
					{
						//Actulizar stock en Linea EXMARB
						logi.info("ACTUALIZA STOCK EN LINEA");
						int stockLineaArt = det.getCantidadFormato();
							//MAIL
						ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(ord.getCodigoBodega(), det.getCodigoArticulo());

						if (stockLineaArt<0){
							//mailNegativo.envioMail("Procesa AJUSTE :"+"NOMBRE ARCHIVO :"+ nombreArchivo + det.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ det.getCantidadFormato()+" STOCK AHORA : "+stockLineaArt);
						}
/*						exmarb.actualizaStockLinea(ord.getCodigoBodega(), det.getCodigoArticulo(), exm.getDvArticulo().trim(), det.getCantidadFormato());
*/					//Se cambia por campo cantidadArticulo JCANQUIL 20170208
						exmarb.actualizaStockLinea(ord.getCodigoBodega(), det.getCodigoArticulo(), exm.getDvArticulo().trim(), exmarbDTO.getStockLinea()-det.getCantidadArticulo());

						}
				}
			}
			
			Iterator iterGuia = gui.iterator();
			while (iterGuia.hasNext()){
				//INSERTA VECMAR
				GuiasDTO guia = (GuiasDTO) iterGuia.next();
				VecmarDTO dto = new VecmarDTO();
				//Mueve valores VECMAR
				dto.setCodigoEmpresa(ord.getCodigoEmpresa());
				dto.setCodTipoMvto(39);
				dto.setFechaMvto(guia.getFechaDespacho());
				dto.setFechaDocumento(guia.getFechaDespacho());
				dto.setNumDocumento(guia.getNumeroGuia());
				dto.setCodigoDocumento(38);
				dto.setBodegaOrigen(ord.getCodigoBodega());
				dto.setRutProveedor(String.valueOf(ord.getRutCliente()));
				dto.setDvProveedor(ord.getDvCliente());
				dto.setFormaPago("1");
				dto.setRazonSocialCliente(ord.getNombreCliente());
				dto.setCantidadLineaDetalle(cantidLineas);
				dto.setTotalNeto(totalNeto);
				dto.setTotalDocumento(totalNeto);
				dto.setCodigoVendedor(ord.getCodigoVendedor());
				dto.setSwichProceso(0);
				dto.setIndicadorDespacho("S");
				dto.setDireccionDespacho("");
				dto.setContactoDespacho("");
				dto.setFechaDespacho(0);
				dto.setSwitchPagoCaja("S");
				dto.setCodigoRegion(0);
				dto.setCodigoCiudad(0);
				dto.setCodigoComuna(0);
				vecmar.generaMovimientoCobro(dto);
				/*proce.procesaFacturacion(String.valueOf(dto.getCodigoEmpresa()), String.valueOf(dto.getCodTipoMvto()), String.valueOf(dto.getFechaMvto()), 
						String.valueOf(dto.getNumDocumento()), String.valueOf(dto.getCodigoDocumento()), String.valueOf(dto.getRutProveedor()), dto.getDvProveedor().trim(), "USRWMS", "G", "0");
				*///PROCESA FACTURACION SERVLET
				StringBuffer tmp = new StringBuffer(); 
		        String texto = new String();
				try { 
		            // Crea la URL con del sitio introducido, ej: http://google.com 
		            URL url = new URL(rutserv.getEndPoint()+"?empresa="+String.valueOf(dto.getCodigoEmpresa())+
		            		"&codTipo="+String.valueOf(dto.getCodTipoMvto())+
		            				"&fch="+String.valueOf(dto.getFechaMvto())+"&num="+
		            		String.valueOf(dto.getNumDocumento())+
		            				"&cod="+String.valueOf(dto.getCodigoDocumento())+"&rut="+
		            String.valueOf(dto.getRutProveedor())+"&dv="+dto.getDvProveedor()+"&usuario=CAJABD26&tipo=G&nota=0"); 
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
				
				
				//Actualiza Lineas CARGUIOD
				DocgenelDTO doc= docgenel.recuperaFolio(dto.getCodigoEmpresa(), dto.getCodTipoMvto(), dto.getFechaMvto(), dto.getNumDocumento());
				Iterator iter2 = ord.getDetord().iterator();
				while (iter2.hasNext()){
					DetordDTO det = (DetordDTO) iter2.next();
					CarguiodDTO carg = new CarguiodDTO();
					carg.setNumeroGuia(doc.getFolioDocumento());

					carg.setCodigoEmpresa(det.getCodEmpresa());
					carg.setNumeroCarguio(carguio.getNumeroCarguio());
					carg.setPatente(carguio.getPatente().trim());
					carg.setCodigoBodega(carguio.getCodigoBodega());
					carg.setNumeroOrden(det.getNumOvVenta());
					carg.setCorrelativoOV(det.getCorrelativoDetalleOV());
					carguioD.actualizaGuia(carg);
				}
				
				vecmar.actualizaDisponibilidadImpresion(dto.getCodigoEmpresa(), dto.getCodTipoMvto(), dto.getFechaMvto(), dto.getNumDocumento(), anden.trim());
				
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
		return resul;
	}
	
	public VecmarDTO procesaFactura(OrdvtaDTO ord, VecmarDAO vecmar, VedmarDAO vedmar, ProcedimientoDAO proce, ExmarbDAO exmarb, 
			TpacorDAO tpacor, DetordDAO detord, ExmvndDAO exmvndDAO){
		int resul=0;
		List gui = new ArrayList();
		int fecha=0;
		int numero=0;
		VecmarDTO dto=null;

		try{
			Iterator iter = ord.getDetalle().entrySet().iterator();
			GuiasDTO guia = new GuiasDTO();
			int doc=0;
			while (iter.hasNext()){
				Map.Entry e = (Map.Entry)iter.next();
				
				DetordDTO det = (DetordDTO) e.getValue();
				//Mueve creacion VEDMAR
				VedmarDTO ved = new VedmarDTO();
					System.out.println("Recupera Correlativo Factura :" + doc);
					ExmarbDTO exm = exmarb.recuperaArticulo(ord.getCodigoBodega(), det.getCodigoArticulo());
					 if (doc==0){ 
						// numero = tpacor.recupeCorrelativo(0, 45);
						 String str = "ASYSRCD00 00008   0  01   ";
							numero = proce.obtieneCorrelativo(str);
							logi.info("Correlativo interno Factura : "+numero);
						 guia.setNumeroGuia(numero);
							guia.setFechaDespacho(det.getFechaDespacho());
							gui.add(guia);
					 }
					 System.out.println("Correlativo FACTURA :" +numero);
					doc++;
					ved.setCodigoEmpresa(det.getCodEmpresa());
					ved.setCodTipoMvto(41);
					ved.setFechaMvto(det.getFechaDespacho());
					ved.setNumDocumento(numero);
					ved.setCorrelativo(det.getCorrelativoDetalleOV());
					ved.setCodigoBodega(ord.getCodigoBodega());
					ved.setCodigoArticulo(det.getCodigoArticulo());
					ved.setDigArticulo(exm.getDvArticulo().trim());
					ved.setFormato(det.getFormato().trim());
					ved.setCantidadArticulo(det.getCantidadArticulo());
					ved.setCantidadFormato(det.getCantidadFormato());
					ved.setSectorBodega(exm.getCodigoSector());
					ved.setPrecioUnidad(det.getPrecioBruto());
					ved.setPrecioNeto(det.getPrecioNeto());
					ved.setCostoNeto(det.getCostoNeto());
					ved.setCostoTotalNeto(det.getCostoTotal());
					ved.setMontoBrutoLinea(det.getMontoBruto());
					ved.setMontoNeto(det.getMontoNeto());
					ved.setMontoTotalNetoLinea(det.getTotalNeto());
					ved.setPorcentajeDesto(det.getPorcentaje());
					ved.setMontoDescuentoLinea(det.getDescuento());
					ved.setMontoDescuentoNeto(det.getDescuentoNeto());
					ved.setMontoTotalLinea(det.getMontoTotal());
					ved.setCodIngresoSalida("S");
					vedmar.generaMovimiento(ved);
				
				
			}
			Iterator iterGuia = gui.iterator();
			while (iterGuia.hasNext()){
				//INSERTA VECMAR
				ExmvndDTO dtoven = exmvndDAO.recuperaVendedor(ord.getCodigoVendedor());
				GuiasDTO guiaDoc = (GuiasDTO) iterGuia.next();
				 dto = new VecmarDTO();
				//Mueve valores VECMAR
				dto.setCodigoEmpresa(ord.getCodigoEmpresa());
				dto.setCodTipoMvto(41);
				dto.setFechaMvto(guia.getFechaDespacho());
				dto.setFechaDocumento(guia.getFechaDespacho());
				dto.setNumDocumento(guiaDoc.getNumeroGuia());
				dto.setCodigoDocumento(33);
				dto.setBodegaOrigen(ord.getCodigoBodega());
				dto.setRutProveedor(String.valueOf(ord.getRutCliente()));
				dto.setDvProveedor(ord.getDvCliente());
				dto.setFormaPago("1");
				dto.setRazonSocialCliente(ord.getNombreCliente());
				
				dto.setCantidadLineaDetalle(0);
				dto.setTotalNeto((int)ord.getTotalDocumento());
				dto.setTotalDocumento((int)ord.getTotalDocumento());
				dto.setCodigoVendedor(ord.getCodigoVendedor());
				dto.setCodigoTipoVendedor(dtoven.getCodigoTipoVendedor());
				dto.setSwichProceso(0);
				dto.setIndicadorDespacho("S");
				dto.setDireccionDespacho("");
				dto.setContactoDespacho("");
				dto.setFechaDespacho(0);
				dto.setSwitchPagoCaja("S");
				dto.setCodigoRegion(0);
				dto.setCodigoCiudad(0);
				dto.setCodigoComuna(0);
				vecmar.generaMovimientoCobro(dto);
				//proce.procesaFacturacion(String.valueOf(dto.getCodigoEmpresa()), String.valueOf(dto.getCodTipoMvto()), String.valueOf(dto.getFechaMvto()), String.valueOf(dto.getNumDocumento()), String.valueOf(dto.getCodigoDocumento()), String.valueOf(dto.getRutProveedor()), dto.getDvProveedor().trim(), "USRWMS", "G", "0");
				//Modifica numero documento interno en DETORD
				
				detord.actualizarDocumento(ord.getCodigoEmpresa(), ord.getCodigoBodega(), ord.getNumeroOV(), guia.getNumeroGuia());
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
		
		return dto;
	}
	public int validaDirecciones(OrdvtaDTO ordvta ){
		int gen=0;
		Iterator iter = ordvta.getDetord().iterator();
		while (iter.hasNext()){
			DetordDTO det = (DetordDTO) iter.next();
			if (det.getCorreDirecciones()!=ordvta.getCorreDireccionOV()){
				gen=1;
				break;
			}
		}
		return gen;
	}
	public void procesaFacturacion(int empresa, int bodega, int numeroCarguio, List carguioWMS, String nombreArchivo, String estInventario, int carguioTrans){
		DAOFactory dao = DAOFactory.getInstance();
		CarguioDAO carguio = dao.getCarguioDAO();
		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		VecmarDAO vecmar = dao.getVecmarDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		DetordDAO detordDAO = dao.getDetordDAO();
		DocGenelDAO docgenelDAO = dao.getDocGenelDAO();
		OrdvtaDAO ordvtaDAO = dao.getOrdvtaDAO();
		TpacorDAO tpacorDAO = dao.getTpacorDAO();
		RutservDAO rutservDAO = dao.getRutServDAO();
		CamtraDAO camtra = dao.getCamtraDAO();
		ExmarbDAO exmarbDAO = dao.getExmarbDAO();
		CargfwmsDAO cargfwmsDAO = dao.getCargfwmsDAO();
		CargcestDAO cargcesrDAO = dao.getCargcestDAO();
		CarguiodDAO carguiodDAO = dao.getCarguiodDAO();
		VedfaltDAO vedfaltDAO = dao.getVedfaltDAO();
		FaccarguDAO faccarguDAO = dao.getFaccarguDAO();
		StockinventarioDAO stockDAO = dao.getStockinventarioDAO();
		Fecha fch = new Fecha();
		ExmvndDAO exmvndDAO = dao.getExmvndDAO();
		ExmtraDAO exmtra = dao.getExmtraDAO();
		TpacorDAO tpacor = dao.getTpacorDAO();
		ExdtraDAO exdtra = dao.getExdtraDAO();
		ExmartDAO exmart = dao.getExmartDAO();
		ExtariDAO extari = dao.getExtariDAO();
		LogcanpeDAO logcanpe = dao.getLogcanpeDAO();
		envioMailDocuNOFacturados envio = new envioMailDocuNOFacturados();
		List docu = new ArrayList();
		int iOTCarguio=0;
		int fechaDespacho=0;
		int bodegaOrigenOT=0;
		int bodegaDestinoOT=0;
		int iGeneraVTALucy=0;
		int iVTALucy=0;
		try{
			//String sub ="GVEMCVM00 00340  2120160502  15532590  33         020160502         00001   01  1      437  000        0        0        0        0      437      000      000    98585648 336   000N                                                                      20160502N170510JORGE RAMIREZ                                                                  1000002022                                                                                             ";
			//Recupera EndPoint facturacion electronica
			RutservDTO rutservDTO = rutservDAO.recuperaEndPointServlet("FACTUR");
			//String sub ="GVEMCVM00 00340  2120160502  15532593  33         020160502         00001   01  1      437  000        0        0        0        0      437      000      000    98585648 336   000N                                                                      20160502N185542JORGE RAMIREZ                                                                  1000002022                                                                                             ";
			String sub="";
			
			//HashMap ordenes = carguio.listaCarguiosValidaConfirmacion(empresa, "C", numeroCarguio, bodega);
			HashMap ordenes = carguio.listaCarguiosFacturacion(empresa, numeroCarguio, bodega);
			
			CarguioDTO carguioDTO = carguio.obtieneCarguioDTO(empresa, numeroCarguio, bodega);
			
			Iterator iterWMS = carguioWMS.iterator();
			int rut=0;
			CargfwmsDTO carfwmsDTO = new CargfwmsDTO();
			carfwmsDTO.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
			carfwmsDTO.setNumeroCarguio(carguioDTO.getNumeroCarguio());
			carfwmsDTO.setPatente(carguioDTO.getPatente());
			carfwmsDTO.setCodigoBodega(carguioDTO.getCodigoBodega());
			carfwmsDTO.setNombreArchivoXML(nombreArchivo);
			carfwmsDTO.setTipo("C");
			carfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
			carfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
			cargfwmsDAO.generaArchivoXML(carfwmsDTO);
			
			while (iterWMS.hasNext()){
				
				logi.info("Ingreso Proceso, Nombre Archivo:"+nombreArchivo);
				ConfirmacionCarguioDTO confirma = (ConfirmacionCarguioDTO) iterWMS.next();
				
				DetordDTO detordDTO4 = new DetordDTO();
				
				//Busco el numero interno de la venta generada en DETORD por la OV
				detordDTO4.setCodEmpresa(carguioDTO.getCodigoEmpresa());
				detordDTO4.setNumOvVenta(confirma.getNumeroOV());
				detordDTO4.setCodigoBodega(carguioDTO.getCodigoBodega());
				
				DetordDTO detordDTO3 = detordDAO.buscaInternoOVFact(detordDTO4);
				if (detordDTO3!=null){
					logi.info("Numero Interno DETORD :"+detordDTO3.getNumeroInternoVenta());
					if (detordDTO3.getNumeroInternoVenta()!=0){
						//Como tiene numero interno DETORD Valido si pertenece a una venta lucy(Mayorista, Canastas) o es BB
						VecmarDTO vecmarDTO3 = vecmar.obtenerDatosVecmarMermasWMS(empresa, 41, detordDTO3.getNumeroInternoVenta());
						if (vecmarDTO3==null){
							iGeneraVTALucy=0;
							
							iVTALucy=0;
							//Como no lo encuentra con el movimiento 41 lo busco ahora por movimiento 21 para saber si esta facturado
							VecmarDTO vecmarDTO4 = vecmar.obtenerDatosVecmarMermasWMS(empresa, 21, detordDTO3.getNumeroInternoVenta());
							if (vecmarDTO4==null){
								iGeneraVTALucy=0;
							}
							
						}
						else{
							//Es venta LUCY(Mayorista, Canastas)
							iGeneraVTALucy=1;
							iVTALucy=1;
							logi.info("No genera venta LUCY :"+detordDTO3.getNumeroInternoVenta());

						}
					}else{
						iGeneraVTALucy=0;
						iVTALucy=1;
					}
				}
				
				else{
					iGeneraVTALucy=0;
					iVTALucy=1;
				}
				
				procesaFechaExpiracion(confirma.getArticulos(), carguioDTO.getCodigoEmpresa(), carguioDTO.getCodigoBodega(), carguioDTO.getNumeroCarguio(), confirma.getNumeroOV(), carguioDTO.getPatente(), carguiodDAO, detordDAO, exmart, confirma.getCorrelativoDireccion(),iVTALucy);
				logi.info("Recupera Ordenes");
				
				//JR Busca si la OV corresponde a un traspaso OT
				if (carguiodDAO.buscaOTCarguio(empresa, numeroCarguio, bodega, confirma.getNumeroOV())!=null){
					iOTCarguio=1;
					
					CarguiodDTO carguiodDTO2 = carguiodDAO.buscaOTCarguio(empresa, numeroCarguio, bodega, confirma.getNumeroOV());
					fechaDespacho = carguiodDTO2.getFechaDespacho();
					bodegaOrigenOT = carguiodDTO2.getCodigoBodegaOrigenOT();
					bodegaDestinoOT = carguiodDTO2.getCodigoBodegaDestinoOT();
					
					//Actualiza a unidades el CARGUIO con lo que devolvio WMS
					procesaFechaExpiracionOT(confirma.getArticulos(), carguioDTO.getCodigoEmpresa(), carguioDTO.getCodigoBodega(), carguioDTO.getNumeroCarguio(), confirma.getNumeroOV(), carguioDTO.getPatente(), carguiodDAO, bodegaOrigenOT, bodegaDestinoOT, exdtra, exmart);
					
				}
				//JR
				
				if (iOTCarguio==0){
					
					OrdvtaDTO ordenDTO = (OrdvtaDTO) ordenes.get(confirma.getNumeroOV());
					
					if (ordenDTO.getVecmar()==null){
						
						//Genera VENTAS LUCY sino posee VECMAR
						ordenDTO = ordvtaDAO.obtieneOrdenVenta(carguioDTO.getCodigoEmpresa(), confirma.getNumeroOV(), carguioDTO.getCodigoBodega(), confirma.getCorrelativoDireccion());
						OrdvtaDTO ordenDTO2 = ordvtaDAO.obtieneOrdenVentaEspecial(carguioDTO.getCodigoEmpresa(), confirma.getNumeroOV(), carguioDTO.getCodigoBodega(), confirma.getCorrelativoDireccion(),numeroCarguio);
						
						//Genera Guia si las direcciones son diferentes
							procesaGuia(ordenDTO2, vecmar, vedmar, proce, exmarbDAO, tpacorDAO, confirma.getAnden(), docgenelDAO, carguioDTO,carguiodDAO, rutservDTO, confirma.getArticulos(), estInventario, stockDAO, nombreArchivo);
							OrdvtaDTO ordvta2 = ordvtaDAO.obtieneOrdenVentaFacturacion(carguioDTO.getCodigoEmpresa(), confirma.getNumeroOV(), carguioDTO.getCodigoBodega());
						if (ordvta2.getDetalle().entrySet().size()>0){
							
							if (iGeneraVTALucy==0){
								//Factura LUCY
								if (validaDirecciones(ordenDTO2)==0){
									ordvta2.setVecmar(procesaFactura(ordvta2, vecmar, vedmar, proce, exmarbDAO, tpacorDAO, detordDAO, exmvndDAO));
									ordenDTO = ordvta2;
								}
								
								
								//ordenDTO.setDetalle(ordenes.get(confirma.getNumeroOV()));
							}
						}
					}
					else{
						//Si el documento esta facturado en ventas LUCY solo genero las guias (solo movimientos 41)
						DetordDTO detordDTO = new DetordDTO();
						
						//Busco el numero interno de la venta generada en DETORD por la OV
						detordDTO.setCodEmpresa(carguioDTO.getCodigoEmpresa());
						detordDTO.setNumOvVenta(confirma.getNumeroOV());
						detordDTO.setCodigoBodega(carguioDTO.getCodigoBodega());
						
						DetordDTO detordDTO2 = detordDAO.buscaInternoOVFact(detordDTO);
						
						if (detordDTO2.getNumeroInternoVenta()!=0){
							
							VecmarDTO vecmarDTO = vecmar.obtenerDatosVecmarMermasWMS(empresa, ordenDTO.getVecmar().getCodTipoMvto(), detordDTO2.getNumeroInternoVenta());
							
							if (vecmarDTO.getCodTipoMvto()==41){
								OrdvtaDTO ordenDTO2 = ordvtaDAO.obtieneOrdenVentaEspecial(carguioDTO.getCodigoEmpresa(), confirma.getNumeroOV(), carguioDTO.getCodigoBodega(), confirma.getCorrelativoDireccion(),numeroCarguio);
								
								//Genera Guia si las direcciones son diferentes
									procesaGuia(ordenDTO2, vecmar, vedmar, proce, exmarbDAO, tpacorDAO, confirma.getAnden(), docgenelDAO, carguioDTO,carguiodDAO, rutservDTO, confirma.getArticulos(), estInventario, stockDAO, nombreArchivo);

								
							}
						}
					}
					//logi.info("Recupera Ordenes:"+ordenDTO.getNumeroOV());

					if (ordenDTO!=null && ordenDTO.getVecmar()!=null){
						CamtraDTO camtraDTO = camtra.verificaFacturacion(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento());
						if (camtraDTO==null){
							logi.info("Valido Camtra");
							//Procesa NO HAY
							if (ordenDTO!=null){
								//OrdvtaDTO orden = (OrdvtaDTO) ordenes.get(372240);
								//Verifica si Venta posee Detalle en VEDMAR
								//int detalle = vedmar.verificaArticulosVentas(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), vedmarDTO.getNumDocumento());
								
								int cantidad = vedmar.obtieneCantidadLineas(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento());
								if (cantidad==0){
									//Procesa Detalle Vedmar mediante DETORD
									logi.info("P R O C E S A R A  V E D M A R  D E  P R O D U C T O S  Q U E  N O  E X I S T E N");
									logi.info("Numero Documento Interno :" + ordenDTO.getVecmar().getNumDocumento());
									logi.info("Rut Cliente :" + ordenDTO.getVecmar().getRutProveedor());
									logi.info("Fecha Movimiento :" + ordenDTO.getVecmar().getFechaMvto());
									proce.procesaVedmarOV(String.valueOf(ordenDTO.getNumeroOV()), String.valueOf(ordenDTO.getRutCliente()), String.valueOf(ordenDTO.getDvCliente()), String.valueOf(ordenDTO.getCodigoBodega()));
								}
								if (ordenDTO.getVecmar().getCodTipoMvto()!=41){
									comparaDetalles(ordenDTO,confirma, vedmar, proce, carguiodDAO, carguioDTO, detordDAO, vecmar, vedfaltDAO, sub, stockDAO, nombreArchivo, exmarbDAO, extari);
									
								}
								//PROCESA CAMBIOS
		
								VecmarDTO dtoVecmar = vedmar.recuperaTotales(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento());
								VedmarDTO vedmarDTO = new VedmarDTO();
								vedmarDTO.setCodigoEmpresa(ordenDTO.getVecmar().getCodigoEmpresa());
								vedmarDTO.setCodTipoMvto(ordenDTO.getVecmar().getCodTipoMvto());
								vedmarDTO.setFechaMvto(ordenDTO.getVecmar().getFechaMvto());
								vedmarDTO.setNumDocumento(ordenDTO.getVecmar().getNumDocumento());
								vedmarDTO.setFechaGuiaDespacho(Integer.parseInt(fch.getYYYYMMDD()));
								//ordenDTO.getVecmar().setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
								ordenDTO.getVecmar().setNumeroTipoDocumento(ordenDTO.getVecmar().getNumDocumento());
								ordenDTO.getVecmar().setSwichProceso(0);
								ordenDTO.getVecmar().setIndicadorDespacho("S");
								ordenDTO.getVecmar().setSwitchPagoCaja("P");
								ordenDTO.getVecmar().setFechaDocumento(Integer.parseInt(fch.getYYYYMMDD()));
								ordenDTO.getVecmar().setFechaDespacho(Integer.parseInt(fch.getYYYYMMDD()));
								ordenDTO.getVecmar().setCantidadLineaDetalle(cantidad);
								ordenDTO.getVecmar().setTotalBruto(dtoVecmar.getTotalDocumento());
								ordenDTO.getVecmar().setTotalNeto(dtoVecmar.getTotalNeto());
								ordenDTO.getVecmar().setTotalDocumento(dtoVecmar.getTotalDocumento());
								if (ordenDTO.getVecmar().getCodigoDocumento()==3){
									ordenDTO.getVecmar().setCodigoDocumento(33);
								}else if (ordenDTO.getVecmar().getCodigoDocumento()==4){
									ordenDTO.getVecmar().setCodigoDocumento(34);
		
								}
								vecmar.actualizaDatosVecmar(ordenDTO.getVecmar());
								
								
								if (ordenDTO.getVecmar().getRutProveedor().trim().length()>0){
									if (Integer.parseInt(ordenDTO.getVecmar().getRutProveedor().trim())>0){
										if (rut==Integer.parseInt(ordenDTO.getVecmar().getRutProveedor())){
											try{
												
												Thread.sleep(2000);
												logi.info("tread 2000:");
												logi.info("Rut Cliente:"+ordenDTO.getVecmar().getRutProveedor());
												logi.info("Numero OV:"+ordenDTO.getNumeroOV());
												
											}catch(Exception e){
												e.printStackTrace();
											}
										}
										 rut = Integer.parseInt(ordenDTO.getVecmar().getRutProveedor().trim());

									}
								}
								
								
								
								//ACTUALIZAR FECHAS
								vedfaltDAO.actualizaDatosVedfalt(ordenDTO.getVecmar());
								vedmar.actualizaFecha(vedmarDTO);
								ordenDTO.getVecmar().setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
								sub = formaString(ordenDTO.getVecmar());
								
								logi.info("Procesa NO HAY");
								logi.info("String Generacion:"+sub);
								//String Generacion:VVEMCVM00 00337  4120160628  16570668  33  1657066820160628         0  26   01  2   906479    0        0   761747        0        0   906479        0        0   764216687 615   000S                                                                      20160630P133305COMERCIAL CASANOVA E HIJOS LTDA.                0        0   0        0 0  0  06000  2
								//VALIDA SI VENTA SOLO TIENE FLETE NO DEBE FACTURAR NADA
								
								if (vedmar.verificaVenta(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), vedmarDTO.getNumDocumento())>0){
									logi.info("GENERA DOCUMENTO PAPERLESS"+ "numero Documento : "+vedmarDTO.getNumDocumento() +" Fecha Documento :"+vedmarDTO.getFechaMvto());
									proce.procesaCalculoProcedure(sub);
									
									//Inserta Aprobacion de Carguio
									FaccarguDTO faccarguDTO = new FaccarguDTO();
									faccarguDTO.setCodigoEmpresa(ordenDTO.getCodigoEmpresa());
									faccarguDTO.setNumeroCarguio(numeroCarguio);
									faccarguDTO.setCodigoBodega(bodega);
									faccarguDTO.setPatente(carguioDTO.getPatente().trim());
									faccarguDTO.setCodigoEmpresaOV(ordenDTO.getCodigoEmpresa());
									faccarguDTO.setNumeroOV(ordenDTO.getNumeroOV());
									faccarguDTO.setCodigoBodegaOV(ordenDTO.getCodigoBodega());
									faccarguDTO.setRutCliente(ordenDTO.getRutCliente());
									faccarguDTO.setDvCliente(ordenDTO.getDvCliente().trim());
									faccarguDTO.setUsuario("USRWMS");
									faccarguDTO.setFechaAprobacion(Integer.parseInt(fch.getYYYYMMDD()));
									faccarguDTO.setHoraAprobacion(Integer.parseInt(fch.getHHMMSS()));
									faccarguDTO.setEstado("A");
									faccarguDAO.generaAprobacion(faccarguDTO);
									//logi.info("Procesa Facturacion JAIME");
									proce.procesaPersonalizados(String.valueOf(ordenDTO.getVecmar().getCodigoEmpresa()), String.valueOf(ordenDTO.getVecmar().getCodTipoMvto()),String.valueOf(ordenDTO.getVecmar().getFechaDocumento()), String.valueOf(ordenDTO.getVecmar().getNumDocumento()), String.valueOf(ordenDTO.getVecmar().getCodigoDocumento()));
									//proce.procesaFacturacion(String.valueOf(ordenDTO.getVecmar().getCodigoEmpresa()), String.valueOf(ordenDTO.getVecmar().getCodTipoMvto()), String.valueOf(ordenDTO.getVecmar().getFechaMvto()), String.valueOf(ordenDTO.getVecmar().getNumDocumento()), String.valueOf(ordenDTO.getVecmar().getCodigoDocumento()) , String.valueOf(ordenDTO.getVecmar().getRutProveedor()), ordenDTO.getVecmar().getDvProveedor(), "AMS", "1", "0");
									//PROCESA SERVLET FACTURACION
									StringBuffer tmp = new StringBuffer(); 
							        String texto = new String();
							        logi.info("PROCESA FACTURACION SERVLET 1");
							        int numeroProceso=0;
									try { 
							            // Crea la URL con del sitio introducido, ej: http://google.com 
							            URL url = new URL(rutservDTO.getEndPoint()+"?empresa="+String.valueOf(ordenDTO.getVecmar().getCodigoEmpresa())+
							            		"&codTipo="+String.valueOf(ordenDTO.getVecmar().getCodTipoMvto())+
							            				"&fch="+String.valueOf(ordenDTO.getVecmar().getFechaDocumento())+"&num="+
							            		String.valueOf(ordenDTO.getVecmar().getNumDocumento())+
							            				"&cod="+String.valueOf(ordenDTO.getVecmar().getCodigoDocumento())+"&rut="+
							            String.valueOf(ordenDTO.getVecmar().getRutProveedor())+"&dv="+ordenDTO.getVecmar().getDvProveedor()+"&usuario=CAJABD26&tipo=1&nota=0"); 
							            logi.info("URL SERVLET FACTURACION 1.0:"+url.toString());
							            // Lector para la respuesta del servidor 
							            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
							            String str; 
							            logi.info("Numero Proceso Facturacion:"+numeroProceso);
							            numeroProceso=numeroProceso+1;
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
							        logi.info("PROCESA FACTURACION SERVLET 2");

									vecmar.actualizaSwitchVecmar(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento(),0);
									vedmar.actualizaSwitchVecmar(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento(),0);
									vecmar.actualizaDisponibilidadImpresion(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento(), confirma.getAnden().trim());
									camtra.actualizaEstadoPago(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento());
									 VecmarDTO vecmarDTO2 = vecmar.obtenerDatosVecmarMer(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento());
										logi.info("Fecha Nueva VECMAR :"+vecmarDTO2.getFechaMvto());
										logi.info("Switch Pago Nuevo:"+vecmarDTO2.getSwitchPagoCaja());
										LogcanpeDTO logDTO = new LogcanpeDTO();
										logDTO.setCodigoEmpresa(ordenDTO.getVecmar().getCodigoEmpresa());
										logDTO.setNumeroDocumento(ordenDTO.getVecmar().getNumDocumento());
										logDTO.setCodigoBodega(ordenDTO.getVecmar().getBodegaOrigen());
										logDTO.setNumeroFactura(ordenDTO.getVecmar().getNumDocumento());
										logDTO.setFechaDocumento(vecmarDTO2.getFechaMvto());
										logDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
										logDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
										logDTO.setUsuario("USRWMS");
										logDTO.setIpEquipo("255.255.255.255");
										logDTO.setNombreEquipo("PCWMS");
										logDTO.setCodigoEstadoInicial("S");
										logDTO.setCodigoEstadoFinal(vecmarDTO2.getSwitchPagoCaja());
										logDTO.setNombreEjecutable("PJWMS");
										logDTO.setNombreFormulario("PJFORMWMS");
									//Actualiza VECMAR
									DocgenelDTO doc= docgenelDAO.recuperaFolio(ordenDTO.getVecmar().getCodigoEmpresa(), ordenDTO.getVecmar().getCodTipoMvto(), ordenDTO.getVecmar().getFechaMvto(), ordenDTO.getVecmar().getNumDocumento());
									//Almacena Documentos Procesados para informar por Mail
									if (doc!=null){
										if (doc.getNumeroDocumento()==doc.getFolioDocumento()){
											DocuNoGeneradoDTO dto = new DocuNoGeneradoDTO();
											if (ordenDTO.getVecmar()!=null){
												dto.setNumeroOrden(confirma.getNumeroOV());
												dto.setNumeroDocumento(ordenDTO.getVecmar().getNumDocumento());
												dto.setRutCliente(Integer.parseInt(ordenDTO.getVecmar().getRutProveedor()));
												dto.setDvCliente(ordenDTO.getVecmar().getDvProveedor());
												dto.setFechaDocumento(ordenDTO.getVecmar().getFechaMvto());
												dto.setComentario("ERROR EN FACTURACION");
												docu.add(dto);
											}
											
										}
									}else{
										DocuNoGeneradoDTO dto = new DocuNoGeneradoDTO();
										if (ordenDTO.getVecmar()!=null){
											dto.setNumeroOrden(confirma.getNumeroOV());
											dto.setNumeroDocumento(ordenDTO.getVecmar().getNumDocumento());
											dto.setRutCliente(Integer.parseInt(ordenDTO.getVecmar().getRutProveedor()));
											dto.setDvCliente(ordenDTO.getVecmar().getDvProveedor());
											dto.setFechaDocumento(ordenDTO.getVecmar().getFechaMvto());
											dto.setComentario("NO SE PROCESO DOCGENEL");

											docu.add(dto);
										}
										
									}
									
									logi.info("FINAL PROCESO");
									if (doc!=null){
										logDTO.setNumeroFactura(doc.getFolioDocumento());
									}else{
										logDTO.setNumeroFactura(ordenDTO.getVecmar().getNumDocumento());

									}
									
									logcanpe.generaLogpendientePago(logDTO);

								}else{
									logi.info("VENTA NO SE PROCESARA SOLO POSEE FLETE : " + "numero Documento : "+ordenDTO.getVecmar().getFechaMvto() +" Fecha Documento :"+vedmarDTO.getFechaMvto());
									//break;
									//Actualiza estado en ORDVTA y DETORD ya que los documentos no fueron facturados porque la OV solo posee articulo flete, cupon o promocion
									OrdvtaDTO ordact = new OrdvtaDTO();
									ordact.setCodigoEmpresa(ordenDTO.getCodigoEmpresa());
									ordact.setRutCliente(ordenDTO.getRutCliente());
									ordact.setDvCliente(ordenDTO.getDvCliente());
									ordact.setNumeroOV(ordenDTO.getNumeroOV());
									ordact.setCodigoBodega(ordenDTO.getCodigoBodega());
									ordact.setEstadoOV("W");
									logi.info("A C T U A L I Z A   O V  P A R A  N O  S E R  C O N S I D E R A D A  E N  P R O C E S O S");
									ordvtaDAO.actualizarestadoOV(ordact);
									detordDAO.actualizarestadoDetalleOV(ordenDTO.getCodigoEmpresa(), ordenDTO.getCodigoBodega(), ordenDTO.getNumeroOV(), "W");
									DocuNoGeneradoDTO dto = new DocuNoGeneradoDTO();
									if (ordenDTO.getVecmar()!=null){
										dto.setNumeroOrden(confirma.getNumeroOV());
										dto.setNumeroDocumento(ordenDTO.getVecmar().getNumDocumento());
										dto.setRutCliente(Integer.parseInt(ordenDTO.getVecmar().getRutProveedor()));
										dto.setDvCliente(ordenDTO.getVecmar().getDvProveedor());
										dto.setFechaDocumento(ordenDTO.getVecmar().getFechaMvto());
										dto.setComentario("SIN PRODUCTOS EN VEDMAR / SOLO POSEE FLETE");
										docu.add(dto);
									}
								}
								
							}
							
							/*try{
								Thread.sleep(9000);
							}catch(Exception e){
								e.printStackTrace();
							}*/
							if (confirma!=null && carguioDTO!=null){
								/*if (confirma.getCamion()!=carguioDTO.getPatente()){
									if (confirma.getRut()!=carguioDTO.getRutChofer()){
										//Actualiza Chofer y Camion
										carguio.actualizaChofer(empresa, bodega, numeroCarguio, confirma.getRut(), confirma.getDv(), confirma.getCamion());
										carguio.actualizaChoferDetalle(empresa, bodega, numeroCarguio, confirma.getRut(), confirma.getDv(), confirma.getCamion());
									}else{
										//Actualiza Camion
										carguio.actualizaChofer(empresa, bodega, numeroCarguio, carguioDTO.getRutChofer(), carguioDTO.getDvChofer(), confirma.getCamion());
										carguio.actualizaChoferDetalle(empresa, bodega, numeroCarguio, carguioDTO.getRutChofer(), carguioDTO.getDvChofer(), confirma.getCamion());
									}
								}else if (confirma.getRut()!=carguioDTO.getRutChofer()){
									//Actualiza Chofer
									carguio.actualizaChofer(empresa, bodega, numeroCarguio, confirma.getRut(), confirma.getDv(), carguioDTO.getPatente());
									carguio.actualizaChoferDetalle(empresa, bodega, numeroCarguio, confirma.getRut(), confirma.getDv(), carguioDTO.getPatente());
								}*/
							}
						}else{
							logi.info("DOCUMENTO YA FACTURADO EXISTE CAMTRA");
						}
					}
				}
				else{
					
					logi.info("PROCESO PARA EMITIR LA GUIA DE DESPACHO DEL TRASPASO JORGE RAMIREZ");
					
					ExmtraDTO exmtraDTO = exmtra.obtieneOrdenTraspaso(empresa, confirma.getNumeroOV(), carguioDTO.getCodigoBodega(), numeroCarguio);
					
					if (exmtraDTO.getExdtra()!=null){
						procesaGuiaOT(proce, confirma.getAnden(), docgenelDAO, carguioDTO, carguiodDAO, rutservDTO, confirma.getArticulos(), confirma.getNumeroOV(), empresa, carguioDTO.getCodigoBodega(), vecmar, exmtraDTO, fechaDespacho, tpacor, vedmar, exmtra, exdtra, estInventario, stockDAO, exmarbDAO, bodegaOrigenOT, bodegaDestinoOT, nombreArchivo);
					}
				}
			}
			
			
			//Confirma Carguio
			if (carguioTrans>0){
				if (carguioDTO.getNumeroCarguio()!=carguioTrans){
					carguio.actualizarestadoCarguio(carguioDTO.getCodigoEmpresa(), carguioDTO.getCodigoBodega(), carguioDTO.getNumeroCarguio(), "G");
					carguio.actualizarestadoDetalleCarguio(carguioDTO.getCodigoEmpresa(), carguioDTO.getCodigoBodega(), carguioDTO.getNumeroCarguio(), "G");
				}
			}else{
				carguio.actualizarestadoCarguio(carguioDTO.getCodigoEmpresa(), carguioDTO.getCodigoBodega(), carguioDTO.getNumeroCarguio(), "E");
				carguio.actualizarestadoDetalleCarguio(carguioDTO.getCodigoEmpresa(), carguioDTO.getCodigoBodega(), carguioDTO.getNumeroCarguio(), "E");
			}
			
			
			//Registra Cambio de estado Carguio
			CargcestDTO dtoCargc = new CargcestDTO();
			dtoCargc.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
			dtoCargc.setNumcarguio(carguioDTO.getNumeroCarguio());
			dtoCargc.setCodigoBodega(carguioDTO.getCodigoBodega());
			dtoCargc.setEstado("E");
			dtoCargc.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
			dtoCargc.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
			dtoCargc.setPatente(carguioDTO.getPatente().trim());
			dtoCargc.setCorrelativo(1000);
			dtoCargc.setUsuario("JDA_WMS");
			cargcesrDAO.insertaCargcest(dtoCargc);
			//Envio Mail de Documentos no facturados

			if (docu.size()>0){
				envio.mail("", docu, numeroCarguio,docu);
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
		
	}
	public void comparaDetalles(OrdvtaDTO ordvta, ConfirmacionCarguioDTO confirma, VedmarDAO vedmar, ProcedimientoDAO proce, 
			CarguiodDAO carguioD, CarguioDTO carguioDTO, DetordDAO detordDAO, VecmarDAO vecmar, VedfaltDAO vedfaltDAO, String sub, StockinventarioDAO stockDAO, String nombreArchivo, ExmarbDAO exmarb, ExtariDAO extari){
		Iterator iter = confirma.getArticulos().iterator();
		HashMap <Integer, VedmarDTO> lista = vedmar.obtenerDatosVedmarNoHay(ordvta.getVecmar().getCodigoEmpresa(), ordvta.getVecmar().getCodTipoMvto(), ordvta.getVecmar().getFechaMvto(), ordvta.getVecmar().getNumDocumento());
		ActualizaStockInventarioHelper actualiza = new ActualizaStockInventarioHelper();
		int correlativoDetalle=0;
		try{
			while (iter.hasNext()){
				ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
				
				//DetordDTO detalle = ordvta.getDetalle().get(dto.getCodArticulo());
				DetordDTO detalle = ordvta.getDetalle().get(dto.getCorrelativo());
				if (dto.getCantidadDespachada()==0){
					//Eliminar de VEDMAR y todas las tablas de detalle
					detordDAO.eliminaDetalle(detalle);
					CarguiodDTO carguiodDTO = new CarguiodDTO();
					carguiodDTO.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
					carguiodDTO.setCodigoBodega(carguioDTO.getCodigoBodega());
					carguiodDTO.setNumeroCarguio(carguioDTO.getNumeroCarguio());
					carguiodDTO.setPatente(carguioDTO.getPatente().trim());
					carguiodDTO.setNumeroOrden(detalle.getNumOvVenta());
					carguiodDTO.setCodigoArticulo(detalle.getCodigoArticulo());
					carguioD.eliminaDetalleCarguiod(carguiodDTO);
					VedmarDTO dtoVedmar = lista.get(detalle.getCorrelativoDetalleOV());
					VedfaltDTO vedfalt = new VedfaltDTO();
					vedfalt.setCodigoEmpresa(dtoVedmar.getCodigoEmpresa());
					vedfalt.setCodigoTipoMovto(dtoVedmar.getCodTipoMvto());
					vedfalt.setFechaMovto(dtoVedmar.getFechaMvto());
					vedfalt.setNumerDocumento(dtoVedmar.getNumDocumento());
					vedfalt.setCorrelativo(dtoVedmar.getCorrelativo());
					vedfalt.setCodigoBodega(dtoVedmar.getCodigoBodega());
					vedfalt.setCodigoArticulo(dtoVedmar.getCodigoArticulo());
					vedfalt.setDigitoArticulo(dtoVedmar.getDigArticulo());
					vedfalt.setFormato(dtoVedmar.getFormato());
					vedfalt.setCantidadArticulo(0);
					vedfalt.setCantidadFormato(0);
					vedfalt.setCantidadNoHay(dtoVedmar.getCantidadArticulo());
					vedfalt.setCantidadVedmar(dtoVedmar.getCantidadArticulo());
					vedfaltDAO.generaMovimiento(vedfalt);
					vedmar.eliminaDetalle(dtoVedmar);
					//actualiza SOTCK en LINEA SUMANDO la cantidad
					int stockLineaArt = dtoVedmar.getCantidadFormato();
					//MAIL
				ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(dtoVedmar.getCodigoBodega(), dtoVedmar.getCodigoArticulo());

				if (stockLineaArt<0){
					//mailNegativo.envioMail("Procesa AJUSTE :"+"NOMBRE ARCHIVO :"+ nombreArchivo + dtoVedmar.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ dtoVedmar.getCantidadFormato()+" STOCK AHORA : "+stockLineaArt);
				}
/*						exmarb.actualizaStockLinea(ord.getCodigoBodega(), det.getCodigoArticulo(), exm.getDvArticulo().trim(), det.getCantidadFormato());
*/					//Se cambia por campo cantidadArticulo JCANQUIL 20170208
				int cantDif=dtoVedmar.getCantidadArticulo();
				exmarb.actualizaStockLinea(dtoVedmar.getCodigoBodega(), dtoVedmar.getCodigoArticulo(), dtoVedmar.getDigArticulo(), exmarbDTO.getStockLinea()+cantDif);

				
				
					vedmar.eliminaDetalle(dtoVedmar);
					//Actualiza VECMAR
					VecmarDTO vecmarDTO = vedmar.recuperaTotales(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento());
					vecmar.actualizaVecmarMerma(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento(), vecmarDTO.getTotalNeto(), vecmarDTO.getTotalDocumento());
				}else if (dto.getCantidadDespachada()<detalle.getCantidadArticulo()){
					
					
					
					//Generaproceso NO HAY
					//VedmarDTO dtoVedmar = lista.get(detalle.getCodigoArticulo());
					VedmarDTO dtoVedmar = lista.get(detalle.getCorrelativoDetalleOV());
					//Graba NO HAY VEDFALT
					VedfaltDTO vedfalt = new VedfaltDTO();
					vedfalt.setCodigoEmpresa(dtoVedmar.getCodigoEmpresa());
					vedfalt.setCodigoTipoMovto(dtoVedmar.getCodTipoMvto());
					vedfalt.setFechaMovto(dtoVedmar.getFechaMvto());
					vedfalt.setNumerDocumento(dtoVedmar.getNumDocumento());
					vedfalt.setCorrelativo(dtoVedmar.getCorrelativo());
					vedfalt.setCodigoBodega(dtoVedmar.getCodigoBodega());
					vedfalt.setCodigoArticulo(dtoVedmar.getCodigoArticulo());
					vedfalt.setDigitoArticulo(dtoVedmar.getDigArticulo());
					vedfalt.setFormato(dtoVedmar.getFormato());
					vedfalt.setCantidadArticulo(dtoVedmar.getCantidadArticulo());
					vedfalt.setCantidadFormato(dtoVedmar.getCantidadFormato());
					
					//vedfalt.setCantidadNoHay(dto.getCantidadDespachada());
					//vedfalt.setCantidadVedmar(dtoVedmar.getCantidadArticulo()-dto.getCantidadDespachada());
					int cantDif = dtoVedmar.getCantidadArticulo()-dto.getCantidadDespachada();
					vedfalt.setCantidadNoHay(dtoVedmar.getCantidadArticulo()-dto.getCantidadDespachada());
					vedfalt.setCantidadVedmar(dto.getCantidadDespachada());
					vedfaltDAO.generaMovimiento(vedfalt);
					//Elimina Articulo Promocion, despues al invocar al programa $VEMCVM00 deberia procesarse nuevamente si es necesario
					vedmar.eliminaDetallePromocion(dtoVedmar);
					
					dtoVedmar.setCantidadArticulo(dto.getCantidadDespachada());
					dtoVedmar.setCantidadFormato(dto.getCantidadDespachada());
					String vedma = formaStringVedmar(dtoVedmar, ordvta.getVecmar());
					//GVEMDVM00 00242  2120160505  15532605 1  26  143839U     10     10  1      110      001    8589300000944900  00000000000000      000     9449S0       0         0S2   1291003892000      7217886        000        000     793967        000     7940        0002
					//GVEMDVM00 00242  2120160527  15532517 0  26    3875U     24     24222      240      300    39898   00         00       00       00     957600S0       0         0S1    557916432000      3352740         00         00         00         00       00        0  2
					//GVEMDVM00 00242  2120160530  15532551 0  26   13218U    216    216327    14750 21448400    89000   00         00       00       00   22250000S0       0         0S2    511801272000      7478992         00         00         00         00       00        0  2
					//GVEMDVM00 00242  2120160530  15532551 1  26   13390U    168    168325    11800 16369920    89000   00         00       00       00   14952000S0       0         0S2    511801272000      7478992         00         00         00         00       00        0  2
					//GVEMDVM00 00242  2120160530  15532551 0  26   13218U    216    216327    14750 21448400    89000   22250000   00         00       00   222500S0       0         0S2    511801272000      7478992         00         00         00         00       00        0  2
					//GVEMDVM00 00242  2120160920  17049205 7  26  120219U     26     26847   316500    24000    83900   20136000 1120    2255300       00   178807S0     0         0S2   7787125044130      7050420      64675   15522000         00         00       00        0  2
					//
					logi.info(vedma);
					logi.info("Datos VEDMAR antes de Modificar:");
					logi.info("Empresa : "+dtoVedmar.getCodigoEmpresa());
					logi.info("Tipo Movimiento : "+dtoVedmar.getCodTipoMvto());
					logi.info("Fecha Movimiento : "+dtoVedmar.getFechaMvto());
					logi.info("Numero Documento : "+dtoVedmar.getNumDocumento());
					logi.info("Cantidad 1 : "+dtoVedmar.getCantidadArticulo());
					logi.info("Cantidad 1 : "+dtoVedmar.getCantidadFormato());

					//Actualiza VEDMAR
					//GVEMDVM00 00242  4120160628  16570593 0  26   29076C   1260   1260415       00       00    21400  104004000   00         00       00  1040040S0       0         0S1   776826200   0      1798319      17652   85788720         00         00       00        0  2
					//COMENTADO PARA PROBAR UPDATE A VEDMAR 20170802
					proce.procesaCalculoProcedure(formaStringVedmar(dtoVedmar, ordvta.getVecmar()));
					VedmarDTO vedmarDTOo = vedmar.obtenerDatosVedmarNoHayCorrelativo(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento(), dtoVedmar.getCorrelativo());
					logi.info("Despues de Actualizacion con VEMCVM00");
					logi.info("Cantidad 1 :" +vedmarDTOo.getCantidadArticulo());
					logi.info("Cantidad 2 :"+vedmarDTOo.getCantidadFormato());
					if (dtoVedmar.getCantidadArticulo()!=vedmarDTOo.getCantidadArticulo()){
						vedmarDTOo.setCantidadArticulo(dtoVedmar.getCantidadArticulo());
						vedmarDTOo.setCantidadFormato(dtoVedmar.getCantidadFormato());
						double impuestos = extari.recuperaImpuestos(vedmarDTOo.getCodigoArticulo(), vedmarDTOo.getDigArticulo().trim());
						double neto = vedmarDTOo.getCantidadArticulo()*vedmarDTOo.getPrecioNeto();
						logi.info("Cantidad :"+vedmarDTOo.getCantidadArticulo());
						logi.info("Precio : "+vedmarDTOo.getPrecioNeto());
						logi.info("Neto :"+neto);
						double mneto = Math.round(neto);
						logi.info("Neto redondeado :"+mneto);
						//Calcula Descuento en relacion a nuevas cantidades e impuestos del articulo
						int mbruto = (int)Math.round(vedmarDTOo.getCantidadArticulo()*vedmarDTOo.getPrecioUnidad());
						logi.info("Bruto redondeado sin descuento:"+mbruto);

						int descuento=0;
						int descuentoNeto=0;
						if (vedmarDTOo.getPorcentajeDesto()>0){
							 descuento = (int)Math.round(mbruto*vedmarDTOo.getPorcentajeDesto()/100);
							 logi.info("Porcenatje :"+vedmarDTOo.getPorcentajeDesto());
							 logi.info("Descuento Bruto redondeado:"+descuento);
							 if (impuestos>0){
								 impuestos = impuestos/100+(1);
								 logi.info("Impuesto Articulo Procesa Descuento Neto:"+impuestos);
								 descuentoNeto = (int)Math.round(descuento/impuestos);
								 logi.info("Descuento neto redondeado:"+descuentoNeto);
							 }else{
								 descuentoNeto=descuento;
							 }
							
							
						}
						
						vedmarDTOo.setMontoDescuentoLinea(descuento);
						vedmarDTOo.setMontoDescuentoNeto(descuentoNeto);
						int nneto = (int)Math.round(mneto-vedmarDTOo.getMontoDescuentoNeto());
						logi.info("Neto redondeado INT :"+nneto);

						logi.info("Cantitdad :"+vedmarDTOo.getCantidadArticulo());
						logi.info("Precio Bruto :"+vedmarDTOo.getPrecioUnidad());
						logi.info("Monto Bruto :"+mbruto);
						vedmarDTOo.setMontoBrutoLinea(mbruto);

						mbruto = (int)Math.round(mbruto-vedmarDTOo.getMontoDescuentoLinea());
						logi.info("Descuento con Bruto :"+mbruto);
						vedmarDTOo.setMontoTotalLinea(mbruto);
						vedmarDTOo.setMontoNeto(neto);
						vedmarDTOo.setMontoTotalNetoLinea(nneto);
						vedmar.actualizaArticuloNOHAY(vedmarDTOo);
					}
					//Actualiza CARGUIOD
					//SUMA STOCK EN LINEA POR DIFERENCIA
					logi.info("ACTUALIZA STOCK EN LINEA");
					int stockLineaArt = dtoVedmar.getCantidadFormato();
						//MAIL
					ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(dtoVedmar.getCodigoBodega(), dtoVedmar.getCodigoArticulo());

					if (stockLineaArt<0){
						//mailNegativo.envioMail("Procesa AJUSTE :"+"NOMBRE ARCHIVO :"+ nombreArchivo + dtoVedmar.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ dtoVedmar.getCantidadFormato()+" STOCK AHORA : "+stockLineaArt);
					}
/*						exmarb.actualizaStockLinea(ord.getCodigoBodega(), det.getCodigoArticulo(), exm.getDvArticulo().trim(), det.getCantidadFormato());
*/					//Se cambia por campo cantidadArticulo JCANQUIL 20170208
					cantDif=cantDif+dto.getCantidadDespachada();
					exmarb.actualizaStockLinea(dtoVedmar.getCodigoBodega(), dtoVedmar.getCodigoArticulo(), dtoVedmar.getDigArticulo(), exmarbDTO.getStockLinea()+cantDif);

					CarguiodDTO carguiodDTO = new CarguiodDTO();
					carguiodDTO.setCodigoEmpresa(carguioDTO.getCodigoEmpresa());
					carguiodDTO.setCodigoBodega(carguioDTO.getCodigoBodega());
					carguiodDTO.setNumeroCarguio(carguioDTO.getNumeroCarguio());
					carguiodDTO.setPatente(carguioDTO.getPatente().trim());
					carguiodDTO.setNumeroOrden(detalle.getNumOvVenta());
					carguiodDTO.setCodigoArticulo(detalle.getCodigoArticulo());
					carguiodDTO.setCantidad(dto.getCantidadDespachada());
					carguiodDTO.setFechaExpiracion(0);
					//carguioD.actualizaCarguiod(carguiodDTO);
					//Actualiza DETORD
					detalle.setCantidadArticulo(dto.getCantidadDespachada());
					
					//detordDAO.actualizarestadoDetalle(detalle);
					VecmarDTO vecmarDTO = vedmar.recuperaTotales(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento());
					vecmar.actualizaVecmarMerma(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento(), vecmarDTO.getTotalNeto(), vecmarDTO.getTotalDocumento());
					actualiza.actualizaEstado(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodigoBodega(), "D", dtoVedmar.getCodigoArticulo(), dtoVedmar.getDigArticulo(),dto.getCantidadDespachada(), stockDAO, nombreArchivo, correlativoDetalle);
				}else if (dto.getCantidadDespachada()==detalle.getCantidadArticulo()){
					VedmarDTO dtoVedmar = lista.get(detalle.getCodigoArticulo());
					if (dtoVedmar!=null){
						actualiza.actualizaEstado(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodigoBodega(), "D", dtoVedmar.getCodigoArticulo(), dtoVedmar.getDigArticulo(),dtoVedmar.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);

					}

				}
				correlativoDetalle++;
				
			}
			//Verifica Cantidades de articulo, siempre y cuando se un articulo que pertenece al combo virtual
			//Debe recorrer los articulos no hay
			List falt = vedfaltDAO.obtenerNohay(ordvta.getVecmar().getCodigoEmpresa(), ordvta.getVecmar().getCodTipoMvto(), ordvta.getVecmar().getFechaMvto(), ordvta.getVecmar().getNumDocumento());
			Iterator itfalt = falt.iterator();
			while (itfalt.hasNext()){
				VedfaltDTO vedfalt = (VedfaltDTO) itfalt.next();
				
				VedmarDTO vedm = vedmar.obtenerDatosArticulo(ordvta.getVecmar().getCodigoEmpresa(), ordvta.getVecmar().getCodTipoMvto(), ordvta.getVecmar().getFechaMvto(), ordvta.getVecmar().getNumDocumento(), vedfalt.getCodigoArticulo(), vedfalt.getCorrelativo());
				if (vedm!=null){
						//Quitar cantidades a articulo encontrado
						
						VedmarDTO vedm2 = vedmar.obtenerArticuloDifCorrelativo(ordvta.getVecmar().getCodigoEmpresa(), ordvta.getVecmar().getCodTipoMvto(), ordvta.getVecmar().getFechaMvto(), ordvta.getVecmar().getNumDocumento(), vedfalt.getCodigoArticulo(), vedm.getCorrelativo());
						if (vedm2!=null){
							if (vedfalt.getCantidadNoHay()<=vedm2.getCantidadArticulo()){
								//Aumenta cantidades a articulo combo virtual

								vedm.setCantidadArticulo(vedm.getCantidadArticulo()+vedfalt.getCantidadNoHay());
								vedm.setCantidadFormato(vedm.getCantidadFormato()+vedfalt.getCantidadNoHay());
								proce.procesaCalculoProcedure(formaStringVedmar(vedm, ordvta.getVecmar()));
								//Elimina NO HAY
								vedfaltDAO.eliminaDatosVedfalt(vedfalt);
								//Verifica si se realizo actualizacion con el procedimiento sino procesa otra rutina
								VedmarDTO vedmarDTOo = vedmar.obtenerDatosVedmarNoHayCorrelativo(vedm.getCodigoEmpresa(), vedm.getCodTipoMvto(), vedm.getFechaMvto(), vedm.getNumDocumento(), vedm.getCorrelativo());
								logi.info("Despues de Actualizacion con VEMCVM00");
								logi.info("Cantidad 1 :" +vedmarDTOo.getCantidadArticulo());
								logi.info("Cantidad 2 :"+vedmarDTOo.getCantidadFormato());
								if (vedm.getCantidadArticulo()!=vedmarDTOo.getCantidadArticulo()){
									vedmarDTOo.setCantidadArticulo(vedm.getCantidadArticulo());
									vedmarDTOo.setCantidadFormato(vedm.getCantidadFormato());
									double impuestos = extari.recuperaImpuestos(vedmarDTOo.getCodigoArticulo(), vedmarDTOo.getDigArticulo().trim());
									double neto = vedmarDTOo.getCantidadArticulo()*vedmarDTOo.getPrecioNeto();
									logi.info("Cantidad :"+vedmarDTOo.getCantidadArticulo());
									logi.info("Precio : "+vedmarDTOo.getPrecioNeto());
									logi.info("Neto :"+neto);
									double mneto = Math.round(neto);
									logi.info("Neto redondeado :"+mneto);
									//Calcula Descuento en relacion a nuevas cantidades e impuestos del articulo
									int mbruto = (int)Math.round(vedmarDTOo.getCantidadArticulo()*vedmarDTOo.getPrecioUnidad());
									logi.info("Bruto redondeado sin descuento:"+mbruto);

									int descuento=0;
									int descuentoNeto=0;
									if (vedm.getPorcentajeDesto()>0){
										 descuento = (int)Math.round(mbruto*vedm.getPorcentajeDesto()/100);
										 logi.info("Porcenatje :"+vedm.getPorcentajeDesto());
										 logi.info("Descuento Bruto redondeado:"+descuento);
										 if (impuestos>0){
											 impuestos = impuestos/100+(1);
											 logi.info("Impuesto Articulo Procesa Descuento Neto:"+impuestos);
											 descuentoNeto = (int)Math.round(descuento/impuestos);
											 logi.info("Descuento neto redondeado:"+descuentoNeto);
										 }else{
											 descuentoNeto=descuento;
										 }
										
										
									}
									
									vedm.setMontoDescuentoLinea(descuento);
									vedm.setMontoDescuentoNeto(descuentoNeto);
									int nneto = (int)Math.round(mneto-vedm.getMontoDescuentoNeto());
									logi.info("Neto redondeado INT :"+nneto);

									logi.info("Cantitdad :"+vedm.getCantidadArticulo());
									logi.info("Precio Bruto :"+vedm.getPrecioUnidad());
									logi.info("Monto Bruto :"+mbruto);
									vedm.setMontoBrutoLinea(mbruto);

									mbruto = (int)Math.round(mbruto-vedm.getMontoDescuentoLinea());
									logi.info("Descuento con Bruto :"+mbruto);
									vedm.setMontoTotalLinea(mbruto);
									vedm.setMontoNeto(neto);
									vedm.setMontoTotalNetoLinea(nneto);
									vedmar.actualizaArticuloNOHAY(vedm);
								}
								
								
								
							  //Disminuye cantifades a articulo NO combo virtual
								vedm2.setCantidadArticulo(vedm2.getCantidadArticulo()-vedfalt.getCantidadNoHay());
								vedm2.setCantidadFormato(vedm2.getCantidadFormato()-vedfalt.getCantidadNoHay());
								
								if (vedm2.getCantidadArticulo()==0){
									//elimina linea VEDMAR
									vedmar.eliminaDetalle(vedm2);
								}else{
									//Actualiza linea VEDMAR
									proce.procesaCalculoProcedure(formaStringVedmar(vedm2, ordvta.getVecmar()));
									
									VedmarDTO vedmarDTOo2 = vedmar.obtenerDatosVedmarNoHayCorrelativo(vedm2.getCodigoEmpresa(), vedm2.getCodTipoMvto(), vedm2.getFechaMvto(), vedm2.getNumDocumento(), vedm2.getCorrelativo());
									logi.info("Despues de Actualizacion con VEMCVM00");
									logi.info("Cantidad 1 :" +vedmarDTOo2.getCantidadArticulo());
									logi.info("Cantidad 2 :"+vedmarDTOo2.getCantidadFormato());
									if (vedm2.getCantidadArticulo()!=vedmarDTOo2.getCantidadArticulo()){
										vedmarDTOo2.setCantidadArticulo(vedm2.getCantidadArticulo());
										vedmarDTOo2.setCantidadFormato(vedm2.getCantidadFormato());
										double impuestos = extari.recuperaImpuestos(vedmarDTOo2.getCodigoArticulo(), vedmarDTOo2.getDigArticulo().trim());
										double neto = vedmarDTOo2.getCantidadArticulo()*vedmarDTOo2.getPrecioNeto();
										logi.info("Cantidad :"+vedmarDTOo2.getCantidadArticulo());
										logi.info("Precio : "+vedmarDTOo2.getPrecioNeto());
										logi.info("Neto :"+neto);
										double mneto = Math.round(neto);
										logi.info("Neto redondeado :"+mneto);
										//Calcula Descuento en relacion a nuevas cantidades e impuestos del articulo
										int mbruto = (int)Math.round(vedmarDTOo2.getCantidadArticulo()*vedmarDTOo2.getPrecioUnidad());
										logi.info("Bruto redondeado sin descuento:"+mbruto);

										int descuento=0;
										int descuentoNeto=0;
										if (vedm2.getPorcentajeDesto()>0){
											 descuento = (int)Math.round(mbruto*vedm2.getPorcentajeDesto()/100);
											 logi.info("Porcenatje :"+vedm2.getPorcentajeDesto());
											 logi.info("Descuento Bruto redondeado:"+descuento);
											 if (impuestos>0){
												 impuestos = impuestos/100+(1);
												 logi.info("Impuesto Articulo Procesa Descuento Neto:"+impuestos);
												 descuentoNeto = (int)Math.round(descuento/impuestos);
												 logi.info("Descuento neto redondeado:"+descuentoNeto);
											 }else{
												 descuentoNeto=descuento;
											 }
											
											
										}
										
										vedm2.setMontoDescuentoLinea(descuento);
										vedm2.setMontoDescuentoNeto(descuentoNeto);
										int nneto = (int)Math.round(mneto-vedm2.getMontoDescuentoNeto());
										logi.info("Neto redondeado INT :"+nneto);

										logi.info("Cantitdad :"+vedm2.getCantidadArticulo());
										logi.info("Precio Bruto :"+vedm2.getPrecioUnidad());
										logi.info("Monto Bruto :"+mbruto);
										vedm2.setMontoBrutoLinea(mbruto);

										mbruto = (int)Math.round(mbruto-vedm2.getMontoDescuentoLinea());
										logi.info("Descuento con Bruto :"+mbruto);
										vedm2.setMontoTotalLinea(mbruto);
										vedm2.setMontoNeto(neto);
										vedm2.setMontoTotalNetoLinea(nneto);
										vedmar.actualizaArticuloNOHAY(vedm2);
									}
								}
							}else{
								int cantid = vedfalt.getCantidadNoHay()-vedm2.getCantidadArticulo();
								//Aumenta cantidades a articulo combo virtual
								vedm.setCantidadArticulo(vedm.getCantidadArticulo()+cantid);
								vedm.setCantidadFormato(vedm.getCantidadFormato()+cantid);
								//Actualiza VEDFALT
								vedfalt.setCantidadNoHay(cantid);
								
								vedfalt.setCorrelativo(vedm.getCorrelativo());
								vedfaltDAO.actualizaArticuloVedfalt(vedfalt);
								
								VedmarDTO vedmarDTOo = vedmar.obtenerDatosVedmarNoHayCorrelativo(vedm.getCodigoEmpresa(), vedm.getCodTipoMvto(), vedm.getFechaMvto(), vedm.getNumDocumento(), vedm.getCorrelativo());
								logi.info("Despues de Actualizacion con VEMCVM00");
								logi.info("Cantidad 1 :" +vedmarDTOo.getCantidadArticulo());
								logi.info("Cantidad 2 :"+vedmarDTOo.getCantidadFormato());
								if (vedm.getCantidadArticulo()!=vedmarDTOo.getCantidadArticulo()){
									vedmarDTOo.setCantidadArticulo(vedm.getCantidadArticulo());
									vedmarDTOo.setCantidadFormato(vedm.getCantidadFormato());
									double impuestos = extari.recuperaImpuestos(vedmarDTOo.getCodigoArticulo(), vedmarDTOo.getDigArticulo().trim());
									double neto = vedmarDTOo.getCantidadArticulo()*vedmarDTOo.getPrecioNeto();
									logi.info("Cantidad :"+vedmarDTOo.getCantidadArticulo());
									logi.info("Precio : "+vedmarDTOo.getPrecioNeto());
									logi.info("Neto :"+neto);
									double mneto = Math.round(neto);
									logi.info("Neto redondeado :"+mneto);
									//Calcula Descuento en relacion a nuevas cantidades e impuestos del articulo
									int mbruto = (int)Math.round(vedmarDTOo.getCantidadArticulo()*vedmarDTOo.getPrecioUnidad());
									logi.info("Bruto redondeado sin descuento:"+mbruto);

									int descuento=0;
									int descuentoNeto=0;
									if (vedm.getPorcentajeDesto()>0){
										 descuento = (int)Math.round(mbruto*vedm.getPorcentajeDesto()/100);
										 logi.info("Porcenatje :"+vedm.getPorcentajeDesto());
										 logi.info("Descuento Bruto redondeado:"+descuento);
										 if (impuestos>0){
											 impuestos = impuestos/100+(1);
											 logi.info("Impuesto Articulo Procesa Descuento Neto:"+impuestos);
											 descuentoNeto = (int)Math.round(descuento/impuestos);
											 logi.info("Descuento neto redondeado:"+descuentoNeto);
										 }else{
											 descuentoNeto=descuento;
										 }
										
										
									}
									
									vedm.setMontoDescuentoLinea(descuento);
									vedm.setMontoDescuentoNeto(descuentoNeto);
									int nneto = (int)Math.round(mneto-vedm.getMontoDescuentoNeto());
									logi.info("Neto redondeado INT :"+nneto);

									logi.info("Cantitdad :"+vedm.getCantidadArticulo());
									logi.info("Precio Bruto :"+vedm.getPrecioUnidad());
									logi.info("Monto Bruto :"+mbruto);
									vedm.setMontoBrutoLinea(mbruto);

									mbruto = (int)Math.round(mbruto-vedm.getMontoDescuentoLinea());
									logi.info("Descuento con Bruto :"+mbruto);
									vedm.setMontoTotalLinea(mbruto);
									vedm.setMontoNeto(neto);
									vedm.setMontoTotalNetoLinea(nneto);
									vedmar.actualizaArticuloNOHAY(vedm);
								}
								
								//Disminuye cantifades a articulo NO combo virtual
								vedm2.setCantidadArticulo(vedm2.getCantidadArticulo()-cantid);
								vedm2.setCantidadFormato(vedm2.getCantidadFormato()-cantid);
								if (vedm2.getCantidadArticulo()==0){
									//elimina linea VEDMAR
									vedmar.eliminaDetalle(vedm2);
								}else{
									//Actualiza linea VEDMAR
									
									VedmarDTO vedmarDTOo2 = vedmar.obtenerDatosVedmarNoHayCorrelativo(vedm2.getCodigoEmpresa(), vedm2.getCodTipoMvto(), vedm2.getFechaMvto(), vedm2.getNumDocumento(), vedm2.getCorrelativo());
									logi.info("Despues de Actualizacion con VEMCVM00");
									logi.info("Cantidad 1 :" +vedmarDTOo2.getCantidadArticulo());
									logi.info("Cantidad 2 :"+vedmarDTOo2.getCantidadFormato());
									vedfalt.setCantidadNoHay(cantid);
									
									vedfalt.setCorrelativo(vedm2.getCorrelativo());
									vedfaltDAO.actualizaArticuloVedfalt(vedfalt);
									
									
									if (vedm2.getCantidadArticulo()!=vedmarDTOo2.getCantidadArticulo()){
										vedmarDTOo2.setCantidadArticulo(vedm2.getCantidadArticulo());
										vedmarDTOo2.setCantidadFormato(vedm2.getCantidadFormato());
										double impuestos = extari.recuperaImpuestos(vedmarDTOo2.getCodigoArticulo(), vedmarDTOo2.getDigArticulo().trim());
										double neto = vedmarDTOo2.getCantidadArticulo()*vedmarDTOo2.getPrecioNeto();
										logi.info("Cantidad :"+vedmarDTOo2.getCantidadArticulo());
										logi.info("Precio : "+vedmarDTOo2.getPrecioNeto());
										logi.info("Neto :"+neto);
										double mneto = Math.round(neto);
										logi.info("Neto redondeado :"+mneto);
										//Calcula Descuento en relacion a nuevas cantidades e impuestos del articulo
										int mbruto = (int)Math.round(vedmarDTOo2.getCantidadArticulo()*vedmarDTOo2.getPrecioUnidad());
										logi.info("Bruto redondeado sin descuento:"+mbruto);

										int descuento=0;
										int descuentoNeto=0;
										if (vedm2.getPorcentajeDesto()>0){
											 descuento = (int)Math.round(mbruto*vedm2.getPorcentajeDesto()/100);
											 logi.info("Porcenatje :"+vedm2.getPorcentajeDesto());
											 logi.info("Descuento Bruto redondeado:"+descuento);
											 if (impuestos>0){
												 impuestos = impuestos/100+(1);
												 logi.info("Impuesto Articulo Procesa Descuento Neto:"+impuestos);
												 descuentoNeto = (int)Math.round(descuento/impuestos);
												 logi.info("Descuento neto redondeado:"+descuentoNeto);
											 }else{
												 descuentoNeto=descuento;
											 }
											
											
										}
										
										vedm2.setMontoDescuentoLinea(descuento);
										vedm2.setMontoDescuentoNeto(descuentoNeto);
										int nneto = (int)Math.round(mneto-vedm2.getMontoDescuentoNeto());
										logi.info("Neto redondeado INT :"+nneto);

										logi.info("Cantitdad :"+vedm2.getCantidadArticulo());
										logi.info("Precio Bruto :"+vedm2.getPrecioUnidad());
										logi.info("Monto Bruto :"+mbruto);
										vedm2.setMontoBrutoLinea(mbruto);

										mbruto = (int)Math.round(mbruto-vedm2.getMontoDescuentoLinea());
										logi.info("Descuento con Bruto :"+mbruto);
										vedm2.setMontoTotalLinea(mbruto);
										vedm2.setMontoNeto(neto);
										vedm2.setMontoTotalNetoLinea(nneto);
										vedmar.actualizaArticuloNOHAY(vedm2);
									}
								}
							}
							
							
							//Quita cantidades a articulo combo virtual
						}
					
				}
			}
			
			
			//Procesa Promocion si es aplicable a la venta
			logi.info("SUB :"+sub);
			if (sub.trim().length()>0){
				sub = sub.substring(1, sub.length());
				sub ="P"+sub;
				logi.info("PROCESA PROMOCION ANTES FACTURACION:"+sub);
				proce.procesaCalculoProcedure(sub);
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
	}
	public String formaStringVedmar(VedmarDTO vedmar, VecmarDTO vecmar){
		String facturacion="";
		//String nombre ="GVEMDVM00 00";
		String nombre ="GVEMDVX00 00";
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
		//GVEMDVM00 00242  2120160922  17049205 7  26  120219U     26     26847   316500    24000    83900    2181400 1120     244300       00    19371S0       0         0S2   7787125044130      7050420      64675    1681550         00         00       00        0  2
		//GVEMDVM00 00242  2120160920  17049205 7  26  120219U     26     26847   316500    24000    83900   20136000 1120    2255300       00   178807S0     0         0S2   7787125044130      7050420      64675   15522000         00         00       00        0  2
		//GVEMDVM00 00242  2120160920  17049205 7  26  120219U     26     26847   316500    24000    83900   20136000 1120    2255300       00   178807S0       0         0S2   7787125044130      7050420      64675   15522000         00         00       00        0  2
		paso = agregaBlanco(String.valueOf(vedmar.getCodIngresoSalida()).length()-1,String.valueOf(vedmar.getCodIngresoSalida()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getSwitchProceso()).length()-1,String.valueOf(vedmar.getSwitchProceso()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getFechaGuiaDespacho()).length()-8,String.valueOf(vedmar.getFechaGuiaDespacho()));
		facturacion = facturacion+paso;
		paso = agregaBlanco(String.valueOf(vedmar.getNumeroGuiaDespacho()).length()-10,String.valueOf(vedmar.getNumeroGuiaDespacho()));
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
	
	
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	public int moveFile(String urlFile, String nameFile){
		int numero=0;
		Path origenPath = FileSystems.getDefault().getPath(urlFile);
	    Path destinoPath = FileSystems.getDefault().getPath("/home/ftp/proc/"+nameFile);


	    try {
	        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	    
	    
		return numero;
	}
	
	public int procesaGuiaOT(ProcedimientoDAO proce, 
			String anden, DocGenelDAO docgenel,CarguioDTO carguio, CarguiodDAO carguioD, RutservDTO rutserv, List articulo, int nroTraspaso, int empresa, int bodorigen, VecmarDAO vecmar, ExmtraDTO ord, int fechaDespacho, 
			TpacorDAO tpacor, VedmarDAO vedmar, ExmtraDAO exmtra, ExdtraDAO exdtra, String estInventario, StockinventarioDAO stock, ExmarbDAO exmarb, int bodegaOrigenOT, int bodegaDestinoOT, String nombreArchivo){
		int resul=0;
		
		ExdtraDTO exdtraDTO2 = new ExdtraDTO();
		//CarguioDTO carguiod = new CarguioDTO();
		ActualizaStockInventarioHelper actualiza = new ActualizaStockInventarioHelper();
		try{
			int fechaSYS=0;
			double stockLineaArt=0;
			
			Fecha fch = new Fecha();
			
			fechaSYS = Integer.parseInt(fch.getYYYYMMDD());
			
			logi.info("llamado a procedimiento que genera movimiento 25 en VECMAR");
			
			String internoOT="";
			int correlativoOT=0;
			
			//Busco numero interno del movimiento 01
			//correlativoOT = tpacor.recupeCorrelativo(0, 46);
			String str2 = "ASYSRCD00 00008   0  01   ";
			correlativoOT = proce.obtieneCorrelativo(str2);
			logi.info("Correlativo interno Guia OT : "+correlativoOT);
			
			
			proce.procesaGuiaOT(String.valueOf(empresa), String.valueOf(bodorigen), String.valueOf(carguio.getNumeroCarguio()), String.valueOf(nroTraspaso), String.valueOf(correlativoOT));
			
			//PROCESA FACTURACION SERVLET
			StringBuffer tmp = new StringBuffer(); 
	        String texto = new String();
			try {
	            // Crea la URL con del sitio introducido, ej: http://google.com 
	            URL url = new URL(rutserv.getEndPoint()+"?empresa="+String.valueOf(empresa)+
	            		"&codTipo="+String.valueOf(25)+
	            				"&fch="+String.valueOf(fechaSYS)+"&num="+
	            		String.valueOf(nroTraspaso)+
	            				"&cod="+String.valueOf(38)+"&rut="+
	            String.valueOf(0)+"&dv="+""+"&usuario=CAJABD26&tipo=T&nota=0"); 
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
			
			//Genera VECMAR y VEDMAR con el movimiento 25
			//logi.info("RPG EXBTRA");
			//proce.procesaCalculoProcedure(formaStringExbtra(nroTraspaso,empresa, bodorigen));
			
			//Actualizo el Anden para la impresion del documento
			vecmar.actualizaDisponibilidadImpresion(empresa, 25, fechaSYS, nroTraspaso, anden.trim());
			
			//Recupera numero de guia para actualiza Lineas CARGUIOD
			DocgenelDTO doc= docgenel.recuperaFolio(empresa, 25, fechaSYS, nroTraspaso);
			
			logi.info("Numero de Guia despacho generada :"+doc.getFolioDocumento());
			
			//Actualizar campos de VECMAR
			vecmar.actualizaVecmarGuiaOT(empresa, 25, fechaSYS, correlativoOT, doc.getFolioDocumento());
			
			//Actualiza nro de guia y numero de carguio
			docgenel.actualizaDocgenelGuiaOT(empresa, nroTraspaso, carguio.getCodigoBodega(), doc.getFolioDocumento(), carguio.getNumeroCarguio());
			
			//Actualiza cabecera y detalle del TRASPASO
			exmtra.actualizaEstado(empresa, bodegaOrigenOT, bodegaDestinoOT, nroTraspaso);
			
			exdtraDTO2.setCodEmpresa(empresa);
			exdtraDTO2.setNumTraspaso(nroTraspaso);
			exdtraDTO2.setEstado("P");
			exdtra.actualizaEstado(exdtraDTO2);
			
			logi.info("Actualiza estado a PENDIENTE en EXMTRA y EXDTRA");
			
			Iterator iter2 = ord.getExdtra().iterator();
			int correlativoDetalle=0;
			while (iter2.hasNext()){
				ExdtraDTO det = (ExdtraDTO) iter2.next();
				CarguiodDTO carg = new CarguiodDTO();
				carg.setNumeroGuia(doc.getFolioDocumento());

				carg.setCodigoEmpresa(det.getCodEmpresa());
				carg.setNumeroCarguio(carguio.getNumeroCarguio());
				carg.setPatente(carguio.getPatente().trim());
				carg.setCodigoBodega(carguio.getCodigoBodega());
				carg.setNumeroOrden(det.getNumTraspaso());
				carg.setCorrelativoOV(det.getLinea());
				carguioD.actualizaGuia(carg);
				
				logi.info("Actualiza numero de guia en el carguio");
				
				//Actualiza Stock de Inventario por articulo
				actualiza.actualizaEstado(det.getCodEmpresa(), carguio.getCodigoBodega(), estInventario, det.getCodArticulo(), det.getDigitoVerificador(), (int)det.getCantDespachada(), stock, nombreArchivo, correlativoDetalle);
				logi.info("Actualiza Stock de inventario Articulo :"+det.getCodArticulo()+ "Cantidad :"+det.getCantDespachada());
				correlativoDetalle++;
				//Si el stock no es DISPONIBLE aumento stock en linea porque el STORE PROCEDURE rebaja (con esto devuelvo a su origen el stock en linea)
				if ("D".equals(estInventario.trim())){
					
				}
				else{
					
					//Busco datos del articulo en la bodega
					ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(carguio.getCodigoBodega(), det.getCodArticulo()); 
					
					//Guardo Stock en linea del articulo
					stockLineaArt=exmarbDTO.getStockLinea()+det.getCantDespachada();
					
					//actualiza StockLinea
					//MAIL
				if (stockLineaArt<0){
					//mailNegativo.envioMail("Procesa AJUSTE :"+"NOMBRE ARCHIVO :"+ nombreArchivo + det.getCodArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ det.getCantDespachada()+" STOCK AHORA : "+stockLineaArt);
				}
				
					exmarb.actualizaStockLinea(carguio.getCodigoBodega(), det.getCodArticulo(), det.getDigitoVerificador(), stockLineaArt);
					logi.info("Actualiza Stock en linea Bod: "+carguio.getCodigoBodega());	
				}
				
			}
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
		return resul;
	}
	public String formaStringExbtra(int nroTraspaso, int codemp, int bodOrigen ){
		String facturacion="";
		String nombre ="AEXBTRA00 000";
		Fecha fch = new Fecha();
		
		//1 NRO TRASPASO(7) 
		String paso = agregaBlanco(String.valueOf(nroTraspaso).length()-7,String.valueOf(nroTraspaso));
		facturacion = paso;
		
		//2 BODEGA ORIGEN(4) 
		paso = agregaBlanco(String.valueOf(bodOrigen).length()-4,String.valueOf(bodOrigen));
		facturacion = facturacion+paso;
		
		//3 TRASPASO(9)
		paso = agregaBlanco(String.valueOf("TRASPASO ").length()-9,String.valueOf("TRASPASO "));
		facturacion = facturacion+paso;
		
		//4 (7)
		paso = agregaBlanco(String.valueOf("0").length()-7,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//5 (9)
		paso = agregaBlanco(String.valueOf("0").length()-9,String.valueOf("0"));
		facturacion = facturacion+paso;
		
		//6 CODIGO EMPRESA(3)
		paso = agregaBlanco(String.valueOf(codemp).length()-3,String.valueOf(codemp));
		facturacion = facturacion+paso;
		
		int largo = facturacion.length();
		String largo2=String.valueOf(largo);
		facturacion = nombre+largo2+facturacion;
		
		logi.info(""+facturacion);
		
		return facturacion;
	}
	
	public static void main(String []args){
		IntegracionConfirCamionHelper helper = new IntegracionConfirCamionHelper();
		
	helper.procesaConfirmaCamion2("/home2/ftp/in/SHIP_LOAD_CARGA_10.xml", "CAMION", "SHIP_LOAD_CARGA_10.xml");
		/*DAOFactory dao = DAOFactory.getInstance();
		VecmarDAO vecmar = dao.getVecmarDAO();
		CamtraDAO camtra = dao.getCamtraDAO();
		//camtra.actualizaEstadoPago(2, 21, 17120204, 20161011);
		//vecmar.actualizaSwitchVecmar(2, 21, 20161011, 17120204,0);

		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		//VecmarDAO vecmar = dao.getVecmarDAO();
		//VedmarDTO vedmarDTO = vedmar.obtenerDatosVedmarNoHayCorrelativo(2, 21, 20160922, 17049205, 7);
		VecmarDTO vecmarDTO = vecmar.obtenerDatosVecmarMer(2, 21, 20170715, 19827985);
		ClmcliDAO clmcli = dao.getClmcliDAO();
		ClmcliDTO dto = clmcli.recuperaCliente(vecmarDTO.getRutProveedor(), vecmarDTO.getDvProveedor());
		vecmarDTO.setRazonSocialCliente(dto.getRazonsocial().trim());
		IntegracionConfirCamionHelper hel = new IntegracionConfirCamionHelper();*/
		//vedmarDTO.setCantidadArticulo(240);
		//vedmarDTO.setCantidadFormato(240);
		
		/*String nom = hel.formaString( vecmarDTO);
		proce.procesaCalculoProcedure(nom);*/
		/*VedfaltDAO vedfalt = dao.getVedfaltDAO();
		VecmarDTO dto = new VecmarDTO();
		dto.setCodigoEmpresa(2);
		dto.setCodTipoMvto(21);
		dto.setFechaMvto(20160922);
		dto.setNumDocumento(17062091);
		dto.setFechaDocumento(20160923);
		vedfalt.actualizaDatosVedfalt(dto);
*/
		
	}
}
