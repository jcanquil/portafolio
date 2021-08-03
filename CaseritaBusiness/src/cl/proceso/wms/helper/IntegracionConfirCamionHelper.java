package cl.caserita.proceso.wms.helper;

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
import cl.caserita.dao.iface.CargfwmsDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.CarguiodDAO;
import cl.caserita.dao.iface.CarswmsDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.DocGenelDAO;
import cl.caserita.dao.iface.EncswmsDAO;
import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.ExmvndDAO;
import cl.caserita.dao.iface.FaccarguDAO;
import cl.caserita.dao.iface.OrdswmsDAO;
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
import cl.caserita.dto.CargfwmsDTO;
import cl.caserita.dto.CarguioConfirmacionDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguioTranspDTO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CarswmsDTO;
import cl.caserita.dto.ConfirmacionCarguioDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DocgenelDTO;
import cl.caserita.dto.EncswmsDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmrecDTO;
import cl.caserita.dto.ExmvndDTO;
import cl.caserita.dto.FaccarguDTO;
import cl.caserita.dto.GuiasDTO;
import cl.caserita.dto.OrdswmsDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.RutservDTO;
import cl.caserita.dto.StockinventarioDTO;
import cl.caserita.dto.VecfwmsDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedfaltDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.enviaMailStockNegativo;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.wms.integracion.helper.ActualizaStockInventarioHelper;

public class IntegracionConfirCamionHelper {
	
	private static Logger logi = Logger.getLogger(IntegracionConfirCamionHelper.class);
	private static enviaMailStockNegativo mailNegativo = new enviaMailStockNegativo();
	
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
							confirma.setFechaRealEmision(Integer.parseInt(eElement.getElementsByTagName("CLOSE_DTE").item(0).getTextContent().substring(0, 8)));
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
				procesaFacturacion(2, 26, numeroCarguio, ordenes,"", estadoInventario,0);

			}else if ("MERMA".equals(tipo)){
				
			}
			//Mueva archivo a carpeta de procesados
			moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void procesaConfirmaCamion2(String urlFile,  String tipo, String nombreArchivo){
		
		String estadoInventario="";
		int fechareal=0;
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse(fXmlFile);
			
			///*
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			//*/
			
			List ordenes = new ArrayList();
			ConfirmacionCarguioDTO confirma = new ConfirmacionCarguioDTO();
			List detalle = new ArrayList();
			List carguios = new ArrayList();
			doc.getDocumentElement().normalize();
			int numeroCarguio=0;
			int numeroCarguioTrans=0;
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nListin = doc.getElementsByTagName("MOVE_SEG");
			logi.info(nListin.getLength());
			logi.info("----------------------------");
			for (int temp = 0; temp < nListin.getLength(); temp++) {

					Node nNode = nListin.item(temp);
					
					logi.info("\nCurrent Element :" + nNode.getNodeName());
					
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
								 	logi.info(tr);
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
						confirma.setFechaRealEmision(Integer.parseInt(eElement.getElementsByTagName("CLOSE_DTE").item(0).getTextContent().substring(0, 8)));
						fechareal = confirma.getFechaRealEmision();
					}
			}
			
			CarguioConfirmacionDTO carguioConfir=new CarguioConfirmacionDTO();

			NodeList nList = doc.getElementsByTagName("SHIPMENT_ORDER");
					logi.info(nList.getLength());
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					logi.info("NUMOV : " + eElement.getElementsByTagName("ORDNUM").item(0).getTextContent());
					ConfirmacionCarguioDTO confirma3 = obtieneDocu(eElement.getElementsByTagName("ORDNUM").item(0).getTextContent());
					confirma.setNumeroCarguio(confirma3.getNumeroCarguio());
					confirma.setNumeroOV(confirma3.getNumeroOV());
					confirma.setNumeroCarguioTransf(confirma3.getNumeroCarguioTransf());
					confirma.setCorrelativoDireccion(confirma3.getCorrelativoDireccion());
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
					logi.info("NUMERO OV ASIGNADO A ARREGLO :"+confirma.getNumeroOV());
					ordenes.add(confirma);
					String anden = confirma.getAnden();
					String camion = confirma.getCamion();
					int fechaRecepcion = confirma.getFechaRealEmision();
					 
					int rut= confirma.getRut();
					String dv = confirma.getDv();
					detalle = new ArrayList();
					confirma = new ConfirmacionCarguioDTO();
					confirma.setAnden(anden);
					confirma.setCamion(camion);
					confirma.setRut(rut);
					confirma.setDv(dv);
					confirma.setNumeroCarguio(numeroCarguio);
					confirma.setFechaRealEmision(fechaRecepcion);
				}
				
			}
			List lista = convierteTransferencia(ordenes);
			if ("CAMION".equals(tipo)){
				Iterator iter5 = lista.iterator();
				while (iter5.hasNext()){
					CarguioConfirmacionDTO dto = (CarguioConfirmacionDTO) iter5.next();
					procesaFacturacion(2, 26, dto.getNumeroCarguio(), dto.getOrdenes(), nombreArchivo, estadoInventario, fechareal);
				}
				//procesaFacturacion(2, 26, numeroCarguio, ordenes, nombreArchivo);

			}else if ("MERMA".equals(tipo)){
				logi.info("PROCESA MERMA GENERACION GUIA");
				procesaMerma(2, 26, numeroCarguio, ordenes, nombreArchivo, fechareal);
			}
			//Mueva archivo a carpeta de procesados
			
		}catch (Exception e){
			e.printStackTrace();
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
			 	logi.info(tr);
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
	
	public void procesaMerma(int empresa, int bodega, int numeroCarguio, List carguioWMS, String nombreArchivo, int fechaReal){
		DAOFactory dao = DAOFactory.getInstance();
		EncswmsDAO encDAO = dao.getEncswmsDAO();
		CarswmsDAO carDAO = dao.getCarswmsDAO();
		OrdswmsDAO ordDAO = dao.getOrdswmsDAO();
		Fecha fch = new Fecha();
		
		EncswmsDTO enc = encDAO.buscaEncabezado(empresa, nombreArchivo, Integer.parseInt(fch.getYYYYMMDD()));
		if (enc==null){
			enc = new EncswmsDTO();
			enc.setCodigoEmpresa(2);
			 enc.setCodigoTipoSalida(6);
			 enc.setDescripcionTipoSalida("CONFIRMACION MERMAS");
			 enc.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
			 enc.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
			 enc.setFechaRealGeneracion(fechaReal);
			 enc.setArchivoXML(nombreArchivo);
			 
			 encDAO.generaEncabezado(enc);
		}
		 
		 
		int rut=0;
		if (numeroCarguio>0){
			ConfirmacionCarguioDTO confirma = null;
			Iterator iter = carguioWMS.iterator();
			while (iter.hasNext()){
				 confirma = (ConfirmacionCarguioDTO) iter.next();
				 if (rut==0){
						CarswmsDTO carDTO = new CarswmsDTO();
						carDTO.setCodigoEmpresa(2);
						carDTO.setCodigoTipoSalida(6);
						carDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
						carDTO.setHoraProceso(enc.getHoraProceso());
						carDTO.setArchivoXML(nombreArchivo);
						carDTO.setNumeroCarguio(numeroCarguio);
						carDTO.setTransferenciaNumeroCarguio(confirma.getNumeroCarguioTransf());
						carDAO.generaDetalle(carDTO);
						rut++;
					}
				Iterator iter2 = confirma.getArticulos().iterator();
				while (iter2.hasNext()){
					ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter2.next();
					logi.info("Procesa Detalle ");
					OrdswmsDTO ordDTO = new OrdswmsDTO();
					ordDTO.setCodigoEmpresa(2);
					ordDTO.setCodigoTipoSalida(6);
					ordDTO.setArchivoXML(nombreArchivo);
					ordDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
					ordDTO.setHoraProceso(enc.getHoraProceso());
					ordDTO.setTipoMovimiento(1);
					ordDTO.setNumeroCarguio(numeroCarguio);
					ordDTO.setTransferenciaNumeroCarguio(confirma.getNumeroCarguioTransf());
					ordDTO.setNumeroOrdenVenta(confirma.getNumeroOV());
					ordDTO.setCodigoArticulo(dto.getCodArticulo());
					ordDTO.setCantidadArticulo(dto.getCantidadDespachada());
					ordDTO.setCantidadRealArticulo(dto.getCantidad());
					ordDTO.setCodigoBodega(26);
					ordDTO.setFechaRealGeneracion(fechaReal);
					ordDAO.generaOrden(ordDTO);
				}
				
					
			}
		
		}
		
	}
	
	
	
	
	
	public int obtieneCantidad(List lista , int articulo){
		int cantidad=0;
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
			if (dto.getCodArticulo()==articulo){
				cantidad=dto.getCantidad();
				break;
			}
		}
		return cantidad;
	}
	
	
	
	public void procesaFacturacion(int empresa, int bodega, int numeroCarguio, List carguioWMS, String nombreArchivo, String estInventario, int fechareal){
		DAOFactory dao = DAOFactory.getInstance();
	
		EncswmsDAO encDAO = dao.getEncswmsDAO();
		CarswmsDAO carDAO = dao.getCarswmsDAO();
		OrdswmsDAO ordDAO = dao.getOrdswmsDAO();
		Fecha fch = new Fecha();
		
		EncswmsDTO enc = encDAO.buscaEncabezado(empresa, nombreArchivo, Integer.parseInt(fch.getYYYYMMDD()));
		if (enc==null){
			enc = new EncswmsDTO();
			enc.setCodigoEmpresa(2);
			 enc.setCodigoTipoSalida(3);
			 enc.setDescripcionTipoSalida("CONFIRMACION CAMION");
			 enc.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
			 enc.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
			 enc.setArchivoXML(nombreArchivo);
			 enc.setFechaRealGeneracion(fechareal);
			 encDAO.generaEncabezado(enc);
		}
		 
	
		
		//HashMap ordenes = carguio.listaCarguiosValidaConfirmacion(empresa, "C", numeroCarguio, bodega);
		
		
		Iterator iterWMS = carguioWMS.iterator();
		int rut=0;

		while (iterWMS.hasNext()){
			
			logi.info("Ingreso Proceso, Nombre Archivo:"+nombreArchivo);
			ConfirmacionCarguioDTO confirma = (ConfirmacionCarguioDTO) iterWMS.next();
			if (rut==0){
				CarswmsDTO carDTO = new CarswmsDTO();
				carDTO.setCodigoEmpresa(2);
				carDTO.setCodigoTipoSalida(3);
				carDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
				carDTO.setHoraProceso(enc.getHoraProceso());
				carDTO.setArchivoXML(nombreArchivo);
				carDTO.setNumeroCarguio(confirma.getNumeroCarguio());
				carDTO.setTransferenciaNumeroCarguio(confirma.getNumeroCarguioTransf());
				carDAO.generaDetalle(carDTO);
				rut++;
			}
			Iterator iter = confirma.getArticulos().iterator();
			while (iter.hasNext()){
				ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
				OrdswmsDTO ordDTO = new OrdswmsDTO();
				ordDTO.setCodigoEmpresa(2);
				ordDTO.setCodigoTipoSalida(3);
				ordDTO.setArchivoXML(nombreArchivo);
				ordDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
				ordDTO.setHoraProceso(enc.getHoraProceso());
				ordDTO.setTipoMovimiento(1);
				ordDTO.setNumeroCarguio(confirma.getNumeroCarguio());
				ordDTO.setTransferenciaNumeroCarguio(confirma.getNumeroCarguioTransf());
				ordDTO.setNumeroOrdenVenta(confirma.getNumeroOV());
				ordDTO.setCodigoArticulo(dto.getCodArticulo());
				ordDTO.setCantidadArticulo(dto.getCantidadDespachada());
				ordDTO.setCantidadRealArticulo(dto.getCantidad());
				ordDTO.setCodigoBodega(26);
				ordDTO.setFechaRealGeneracion(fechareal);
				ordDAO.generaOrden(ordDTO);
			}
			
			
		}
				
		
		
		
	}
	public void comparaDetalles(OrdvtaDTO ordvta, ConfirmacionCarguioDTO confirma, VedmarDAO vedmar, ProcedimientoDAO proce, 
			CarguiodDAO carguioD, CarguioDTO carguioDTO, DetordDAO detordDAO, VecmarDAO vecmar, VedfaltDAO vedfaltDAO, String sub, StockinventarioDAO stockDAO, String nombreArchivo){
		Iterator iter = confirma.getArticulos().iterator();
		HashMap <Integer, VedmarDTO> lista = vedmar.obtenerDatosVedmarNoHay(ordvta.getVecmar().getCodigoEmpresa(), ordvta.getVecmar().getCodTipoMvto(), ordvta.getVecmar().getFechaMvto(), ordvta.getVecmar().getNumDocumento());
		ActualizaStockInventarioHelper actualiza = new ActualizaStockInventarioHelper();
		int correlativoDetalle=0;
		while (iter.hasNext()){
			ConfirmacionCarguioDetalleDTO dto = (ConfirmacionCarguioDetalleDTO) iter.next();
			
			DetordDTO detalle = ordvta.getDetalle().get(dto.getCodArticulo());
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
				VedmarDTO dtoVedmar = lista.get(detalle.getCodigoArticulo());
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
				//Actualiza VECMAR
				VecmarDTO vecmarDTO = vedmar.recuperaTotales(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento());
				vecmar.actualizaVecmarMerma(dtoVedmar.getCodigoEmpresa(), dtoVedmar.getCodTipoMvto(), dtoVedmar.getFechaMvto(), dtoVedmar.getNumDocumento(), vecmarDTO.getTotalNeto(), vecmarDTO.getTotalDocumento());
			}else if (dto.getCantidadDespachada()<detalle.getCantidadArticulo()){
				
				
				
				//Generaproceso NO HAY
				VedmarDTO dtoVedmar = lista.get(detalle.getCodigoArticulo());
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
				vedfalt.setCantidadNoHay(dto.getCantidadDespachada());
				vedfalt.setCantidadVedmar(dtoVedmar.getCantidadArticulo()-dto.getCantidadDespachada());
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
				logi.info(vedma);
				//Actualiza VEDMAR
				//GVEMDVM00 00242  4120160628  16570593 0  26   29076C   1260   1260415       00       00    21400  104004000   00         00       00  1040040S0       0         0S1   776826200   0      1798319      17652   85788720         00         00       00        0  2
				proce.procesaCalculoProcedure(formaStringVedmar(dtoVedmar, ordvta.getVecmar()));
				//Actualiza CARGUIOD
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
			
		}
		//Procesa Promocion si es aplicable a la venta
		logi.info("SUB :"+sub);
		if (sub.trim().length()>0){
			sub = sub.substring(1, sub.length());
			sub ="P"+sub;
			logi.info("PROCESA PROMOCION ANTES FACTURACION:"+sub);
			proce.procesaCalculoProcedure(sub);
		}
		
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
		//GVEMDVM00 00242  2120160922  17049205 7  26  120219U     26     26847   316500    24000    83900    2181400 1120     244300       00    19371S0       0         0S2   7787125044130      7050420      64675    1681550         00         00       00        0  2
		//GVEMDVM00 00242  2120160920  17049205 7  26  120219U     26     26847   316500    24000    83900   20136000 1120    2255300       00   178807S0     0         0S2   7787125044130      7050420      64675   15522000         00         00       00        0  2
		//GVEMDVM00 00242  2120160920  17049205 7  26  120219U     26     26847   316500    24000    83900   20136000 1120    2255300       00   178807S0       0         0S2   7787125044130      7050420      64675   15522000         00         00       00        0  2
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
	
	public static void main(String []args){
		IntegracionConfirCamionHelper helper = new IntegracionConfirCamionHelper();
		
		helper.procesaConfirmaCamion2("/home2/ftp/in/SHIP_LOAD_163418.xml", "MERMA", "SHIP_LOAD_163418.xml");
		/*DAOFactory dao = DAOFactory.getInstance();
		VecmarDAO vecmar = dao.getVecmarDAO();
		CamtraDAO camtra = dao.getCamtraDAO();
		camtra.actualizaEstadoPago(2, 21, 17120204, 20161011);
		vecmar.actualizaSwitchVecmar(2, 21, 20161011, 17120204,0);

		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		VecmarDAO vecmar = dao.getVecmarDAO();
		VedmarDTO vedmarDTO = vedmar.obtenerDatosVedmarNoHayCorrelativo(2, 21, 20160922, 17049205, 7);
		VecmarDTO vecmarDTO = vecmar.obtenerDatosVecmarMer(2, 21, 20160922, 17049205);
		IntegracionConfirCamionHelper hel = new IntegracionConfirCamionHelper();
		vedmarDTO.setCantidadArticulo(240);
		vedmarDTO.setCantidadFormato(240);
		String nom = hel.formaStringVedmar(vedmarDTO, vecmarDTO);
		VedfaltDAO vedfalt = dao.getVedfaltDAO();
		VecmarDTO dto = new VecmarDTO();
		dto.setCodigoEmpresa(2);
		dto.setCodTipoMvto(21);
		dto.setFechaMvto(20160922);
		dto.setNumDocumento(17062091);
		dto.setFechaDocumento(20160923);
		vedfalt.actualizaDatosVedfalt(dto);
*/
		
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
		int correlativoDetalle=0;
		Iterator iter2 = ord.getExdtra().iterator();
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
			correlativoDetalle++;
			logi.info("Actualiza Stock de inventario Articulo :"+det.getCodArticulo()+ "Cantidad :"+det.getCantDespachada());
			
			//Si el stock no es DISPONIBLE aumento stock en linea porque el STORE PROCEDURE rebaja (con esto devuelvo a su origen el stock en linea)
			if ("D".equals(estInventario.trim())){
				
			}
			else{
				
				//Busco datos del articulo en la bodega
				ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(carguio.getCodigoBodega(), det.getCodArticulo()); 
				
				//Guardo Stock en linea del articulo
				stockLineaArt=exmarbDTO.getStockLinea()+det.getCantDespachada();
				
				//actualiza StockLinea, Enviar MAIL por STOCK NEGATIVO
				if (stockLineaArt<0){
					//MAIL
					mailNegativo.envioMail("Procesa GUIA OT :" +det.getCodArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK AHORA : "+stockLineaArt);
				}
				exmarb.actualizaStockLinea(carguio.getCodigoBodega(), det.getCodArticulo(), det.getDigitoVerificador(), stockLineaArt);
				logi.info("Actualiza Stock en linea Bod: "+carguio.getCodigoBodega());	
			}
			
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
}
