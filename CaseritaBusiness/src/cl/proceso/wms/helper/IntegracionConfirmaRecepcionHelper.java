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
import cl.caserita.dao.iface.DetswmsDAO;
import cl.caserita.dao.iface.DocncpDAO;
import cl.caserita.dao.iface.EncswmsDAO;
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
import cl.caserita.dto.CotplcDTO;
import cl.caserita.dto.DetswmsDTO;
import cl.caserita.dto.ExdfcprDTO;
import cl.caserita.dto.DocncpDTO;
import cl.caserita.dto.EncswmsDTO;
import cl.caserita.dto.RutservDTO;

public class IntegracionConfirmaRecepcionHelper {
	private static Logger logi = Logger.getLogger(IntegracionConfirmaRecepcionHelper.class);

	private Connection conn;
	
	public void procesaReconciliacion(String urlFile, String nameFile){
		//Agregar Logica para Ajuste realizado en WMS
		File fXmlFile = new File(urlFile);
		DAOFactory dao = DAOFactory.getInstance();
		EncswmsDAO encDAO = dao.getEncswmsDAO();
		DetswmsDAO detDAO = dao.getDetswmsDAO();			

		
		
		Fecha fch = new Fecha();
		int nroDocto=0,iTipDocto=0;
		String sTipoRecep;
		int codarticulo;
		int nroOc;
		int stockRecep;
		int stockPedido;
		int lineaArt;
		
		String rutprovsel;
		int rutprov;
		String dvprovsel;
		String dvrutprov;
		
		String fechaVctoArt;
		
		String sIdCamion;
		String sEstadoInventario;
		String sTipCierre="";
		try{
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse(fXmlFile);
			
			///*
			InputStream is = new ByteArrayInputStream(urlFile.getBytes());
			Document doc = dBuilder.parse(is);
			//*/
			
			AjusteDTO ajuste = null;
			doc.getDocumentElement().normalize();
			
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			
			//Lista XML confirmacion de Recepcion
			NodeList nList = doc.getElementsByTagName("INVENTORY_RECEIPT_IFD_SEG");
			
			logi.info("Largo del XML"+nList.getLength());
			EncswmsDTO enc = new EncswmsDTO();
			 enc.setCodigoEmpresa(2);
			 enc.setCodigoTipoSalida(5);
			 enc.setDescripcionTipoSalida("RECEPCION DOCUMENTOS");
			 enc.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
			 enc.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
			 enc.setArchivoXML(nameFile);
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				
				
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
						
						fechaVctoArt=eElement.getElementsByTagName("TRNDTE").item(0).getTextContent();
						fechaVctoArt=fechaVctoArt.substring(0, 8);
						
						sIdCamion=eElement.getElementsByTagName("TRLR_NUM").item(0).getTextContent();
						sEstadoInventario=eElement.getElementsByTagName("RCVSTS").item(0).getTextContent();
						DetswmsDTO detDTO = new DetswmsDTO();
						 detDTO.setCodigoEmpresa(2);
						 detDTO.setCodigoTipoSalida(5);
						 detDTO.setArchivoXML(nameFile);
						 detDTO.setFechaProceso(Integer.parseInt(fch.getYYYYMMDD()));
						 detDTO.setHoraProceso(Integer.parseInt(fch.getHHMMSS()));
						 detDTO.setCodigoEstadoInventario("");
						 detDTO.setProximoEstadoInvenatrio("");
						 detDTO.setCantidad(stockRecep);
						 detDTO.setCodigoArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
						 detDTO.setCantidadReal(stockPedido);
						 detDTO.setNumeroOrden(nroOc);
						 detDTO.setNumeroDocumento(nroDocto);
						 detDTO.setFechaRealGeneracion(Integer.parseInt(eElement.getElementsByTagName("TRNDTE").item(0).getTextContent().substring(0, 8)));
						 enc.setFechaRealGeneracion(Integer.parseInt(eElement.getElementsByTagName("TRNDTE").item(0).getTextContent().substring(0, 8)));
						 detDAO.generaDetalle(detDTO);
					
				}
			}
			
			
			
			//XML cierre de camion Recepcion
			//Lista XML confirmacion de Recepcion
			NodeList nList2 = doc.getElementsByTagName("RCV_TRLR_SEG");
			
			logi.info("----------------------------");

			
			for ( temp = 0; temp < nList2.getLength(); temp++) 
			{
				 nNode = nList2.item(temp);
				
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
					String andenRecepcion=eElement.getElementsByTagName("YARD_LOC").item(0).getTextContent();
					
					//Busco numero de (OC/OT) con el IDCamion en la cabecera del EXMREC
					
					logi.info("TIPO DE CIERRE : "+sTipCierre);
					
					if ("PROVEEDOR".equals(sTipCierre.trim()))
					{
						} //Fin dek CARCOD PROVEEDOR
				}
					
					if ("TRASPASO".equals(sTipCierre.trim()))
					{}//Fin del CARCOD OT
					
			} //FOR
				
			}
			//Mueva archivo a carpeta de procesados
			//moveFile(urlFile, nameFile);
			 encDAO.generaEncabezado(enc);

		}catch (Exception e){
			logi.info("ERROR");
			e.printStackTrace();
		}
		
		
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
}
