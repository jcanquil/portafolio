package cl.caserita.wms.out.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.AjusteDTO;
import cl.caserita.dto.ExmarbDTO;
import cl.caserita.dto.VecfwmsDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.emailInformacionErroresWMS;
import cl.caserita.enviomail.main.enviaMailStockNegativo;
import cl.caserita.wms.helper.IntegraPrmprvHelper;
import cl.caserita.wms.integracion.helper.ActualizaStockInventarioHelper;
import sun.misc.IOUtils;

public class IntegracionAjusteHelper {
	private  static Logger logi = Logger.getLogger(IntegracionAjusteHelper.class);
	private static emailInformacionErroresWMS email = new emailInformacionErroresWMS();
	private static enviaMailStockNegativo mailNegativo = new enviaMailStockNegativo();

	public void procesaAjuste(String urlFile, String nameFile){
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
			DAOFactory dao = DAOFactory.getInstance();
			VecmarDAO vecmar = dao.getVecmarDAO();
			VedmarDAO vedmar = dao.getVedmarDAO();
			ExmarbDAO exmarb = dao.getExmarbDAO();
			TpacorDAO tpacor = dao.getTpacorDAO();
			VecfwmsDAO vecfwms = dao.getVecfwmsDAO();
			StockinventarioDAO stock = dao.getStockinventarioDAO();
			
			Fecha fch = new Fecha();
			int correlativo=0;
			int correlativo2=0;
			VedmarDTO vedmarDTO = new VedmarDTO();

			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
					
			NodeList nList = doc.getElementsByTagName("INVENTORY_ADJUST_IFD");
			int inser=0;		
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					logi.info("ADJ_REF1 : " + eElement.getElementsByTagName("ADJ_REF1").item(0).getTextContent());

					logi.info("ADJ_REF2 : " + eElement.getElementsByTagName("ADJ_REF2").item(0).getTextContent());
					logi.info("INVSTS : " + eElement.getElementsByTagName("INVSTS").item(0).getTextContent());
					logi.info("LOTNUM : " + eElement.getElementsByTagName("LOTNUM").item(0).getTextContent());
					/*if (eElement.getElementsByTagName("READCOD").item(0).getTextContent()!=null){
						logi.info("READCOD : " + eElement.getElementsByTagName("READCOD").item(0).getTextContent());

					}*/

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("ADJQTY : " + eElement.getElementsByTagName("ADJQTY").item(0).getTextContent());
					logi.info("CATCH_QTY : " + eElement.getElementsByTagName("CATCH_QTY").item(0).getTextContent());
					inser++;
					 correlativo = tpacor.recupeCorrelativo(0, 9999);
					 correlativo2 = tpacor.recupeCorrelativo(0, 9);

					vedmarDTO.setCodigoEmpresa(2);
					vedmarDTO.setCodTipoMvto(7);
					vedmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vedmarDTO.setNumDocumento(correlativo);
					vedmarDTO.setCorrelativo(0);
					vedmarDTO.setCodigoBodega(26);
					vedmarDTO.setCodigoArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(26, vedmarDTO.getCodigoArticulo());
					vedmarDTO.setDigArticulo(exmarbDTO.getDvArticulo().trim());
					vedmarDTO.setFormato("U");
					vedmarDTO.setCantidadArticulo(Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent()));
					vedmarDTO.setCantidadFormato(Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent()));
					
					if (vedmarDTO.getCantidadArticulo()<0){
						vedmarDTO.setCantidadArticulo(vedmarDTO.getCantidadArticulo()*(0-1));
						vedmarDTO.setCantidadArticulo(vedmarDTO.getCantidadFormato()*(0-1));
					}
					vedmarDTO.setSectorBodega(exmarbDTO.getCodigoSector());
					vedmarDTO.setPesoLinea(1000);
					vedmarDTO.setPrecioUnidad(exmarbDTO.getCostoBruto());
					vedmarDTO.setPrecioNeto(exmarbDTO.getCostoNeto());
					vedmarDTO.setCostoNeto(exmarbDTO.getCostoNeto());
					vedmarDTO.setCostoTotalNeto(exmarbDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoBrutoLinea(vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoTotalLinea((int)vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoNeto(vedmarDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoTotalNetoLinea((int)vedmarDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoExento(0);//VERIFICAR FUNCIONALIDAD PARA EXENTOS
					vedmarDTO.setCodIngresoSalida("I");
					vedmar.generaMovimiento(vedmarDTO);
					VecfwmsDTO vecfwmsDTO = new VecfwmsDTO();
					
					
				}
				if (inser>0){
					VecmarDTO vecmarDTO = new VecmarDTO();
					vecmarDTO.setCodigoEmpresa(2);
					vecmarDTO.setCodTipoMvto(7);
					vecmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setNumDocumento(correlativo);
					vecmarDTO.setCodigoDocumento(9);
					vecmarDTO.setNumeroTipoDocumento(correlativo2);
					vecmarDTO.setFechaDocumento(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setBodegaOrigen(26);
					vecmarDTO.setCantidadLineaDetalle(1);
					vecmarDTO.setTotalNeto(vedmarDTO.getMontoTotalNetoLinea());
					vecmarDTO.setTotalImptoAdicional(0);
					vecmarDTO.setTotalIva(0);
					vecmarDTO.setTotalDocumento(vedmarDTO.getMontoTotalLinea());
					vecmarDTO.setPesoTotalMovto(0);
					vecmarDTO.setCodigoJefeLocal(50);
					vecmar.generaMovimiento(vecmarDTO);
				}
			}
			//Mueva archivo a carpeta de procesados
			moveFile(urlFile, nameFile);
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
	}
	
	public void procesaAjuste2(String urlFile, String nombreArchivo){
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
			DAOFactory dao = DAOFactory.getInstance();
			VecmarDAO vecmar = dao.getVecmarDAO();
			VedmarDAO vedmar = dao.getVedmarDAO();
			ExmarbDAO exmarb = dao.getExmarbDAO();
			TpacorDAO tpacor = dao.getTpacorDAO();
			StockinventarioDAO stockDAO = dao.getStockinventarioDAO();
			ActualizaStockInventarioHelper actualiza = new ActualizaStockInventarioHelper();
			VecfwmsDAO vecfwms = dao.getVecfwmsDAO();
			int tipoMov=0;
			StockinventarioDAO stock = dao.getStockinventarioDAO();
			
			Fecha fch = new Fecha();
			int correlativo=0;
			int correlativo2=0;
			VedmarDTO vedmarDTO = new VedmarDTO();
			int correlativoDetalle=0;
			logi.info("Root element :" + doc.getDocumentElement().getNodeName());
			logi.info("Procesa Elemento:" );
			logi.info("NOMBRE ARCHIVO AJUSTES : " + nombreArchivo);
			NodeList nList = doc.getElementsByTagName("INVENTORY_ADJUST_IFD");
			int inser=0;		
			logi.info("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
						
				logi.info("\nCurrent Element :" + nNode.getNodeName());
						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					ajuste = new AjusteDTO();
					Element eElement = (Element) nNode;
					logi.info("ADJ_REF1 : " + eElement.getElementsByTagName("ADJ_REF1").item(0).getTextContent());

					logi.info("ADJ_REF2 : " + eElement.getElementsByTagName("ADJ_REF2").item(0).getTextContent());
					logi.info("INVSTS : " + eElement.getElementsByTagName("INVSTS").item(0).getTextContent());
					logi.info("LOTNUM : " + eElement.getElementsByTagName("LOTNUM").item(0).getTextContent());
					/*if (eElement.getElementsByTagName("READCOD").item(0).getTextContent()!=null){
						logi.info("READCOD : " + eElement.getElementsByTagName("READCOD").item(0).getTextContent());

					}*/

					logi.info("PRTNUM : " + eElement.getElementsByTagName("PRTNUM").item(0).getTextContent());
					
					logi.info("STKUOM : " + eElement.getElementsByTagName("STKUOM").item(0).getTextContent());
					logi.info("ADJQTY : " + eElement.getElementsByTagName("ADJQTY").item(0).getTextContent());
					logi.info("CATCH_QTY : " + eElement.getElementsByTagName("CATCH_QTY").item(0).getTextContent());
					inser++;
					 correlativo = tpacor.recupeCorrelativo(0, 9999);
					 correlativo2 = tpacor.recupeCorrelativo(0, 9);
					 int cantidad=Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent());
					 String ingreso="";
					 if (cantidad>0){
						 //tipoMov=7;
						tipoMov=3;
						 ingreso="I";
					 }else{
						// tipoMov=28;
						 tipoMov=23;
						 ingreso="S";
					 }
					vedmarDTO.setCodigoEmpresa(2);
					vedmarDTO.setCodTipoMvto(tipoMov);
					vedmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vedmarDTO.setNumDocumento(correlativo);
					vedmarDTO.setCorrelativo(0);
					vedmarDTO.setCodigoBodega(26);
					vedmarDTO.setCodigoArticulo(Integer.parseInt(eElement.getElementsByTagName("PRTNUM").item(0).getTextContent()));
					ExmarbDTO exmarbDTO = exmarb.recuperaArticulo(26, vedmarDTO.getCodigoArticulo());
					vedmarDTO.setDigArticulo(exmarbDTO.getDvArticulo().trim());
					vedmarDTO.setFormato("U");
					vedmarDTO.setCantidadArticulo(Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent()));
					vedmarDTO.setCantidadFormato(Integer.parseInt(eElement.getElementsByTagName("ADJQTY").item(0).getTextContent()));
					if (vedmarDTO.getCantidadArticulo()<0){
						vedmarDTO.setCantidadArticulo(vedmarDTO.getCantidadArticulo()*(0-1));
						vedmarDTO.setCantidadFormato(vedmarDTO.getCantidadFormato()*(0-1));
					}
					vedmarDTO.setSectorBodega(exmarbDTO.getCodigoSector());
					vedmarDTO.setPesoLinea(1000);
					vedmarDTO.setPrecioUnidad(exmarbDTO.getCostoBruto());
					vedmarDTO.setPrecioNeto(exmarbDTO.getCostoNeto());
					vedmarDTO.setCostoNeto(exmarbDTO.getCostoNeto());
					vedmarDTO.setCostoTotalNeto(exmarbDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoBrutoLinea(vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoTotalLinea((int)vedmarDTO.getPrecioUnidad()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoNeto(vedmarDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoTotalNetoLinea((int)vedmarDTO.getCostoNeto()*vedmarDTO.getCantidadArticulo());
					vedmarDTO.setMontoExento(0);//VERIFICAR FUNCIONALIDAD PARA EXENTOS
					vedmarDTO.setCodIngresoSalida(ingreso);
					vedmar.generaMovimiento(vedmarDTO);
					//IF 7 suma y 28 resta solo cuando es D
					logi.info("ESTADO :"+eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim());
					if ("D".equals(eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim())){
						logi.info("PROCESA Actualizacion Inventario");
						if (tipoMov==3){
							actualiza.actualizaEstadoxEstado(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodigoBodega(), 
									eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim(), vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(),
									vedmarDTO.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);
									int stockLineaArt=exmarbDTO.getStockLinea()+vedmarDTO.getCantidadArticulo();
							if (stockLineaArt<0){
								//MAIL
								//mailNegativo.envioMail("Procesa AJUSTE 280 :"+"NOMBRE ARCHIVO : "+ " "+nombreArchivo +"Articulo :  "+vedmarDTO.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ vedmarDTO.getCantidadArticulo()+" STOCK AHORA : "+stockLineaArt);
							}
							
							exmarb.actualizaStockLinea(26, vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(), exmarbDTO.getStockLinea()+vedmarDTO.getCantidadArticulo());
						}else if (tipoMov==23){
							actualiza.actualizaEstado(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodigoBodega(), 
									eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim(), vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(),
							
									vedmarDTO.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);
							int stockLineaArt=exmarbDTO.getStockLinea()-vedmarDTO.getCantidadArticulo();

							if (stockLineaArt<0){
								//MAIL
								//mailNegativo.envioMail("Procesa AJUSTE 293 :"+ " NOMBRE ARCHIVO :" + " "+nombreArchivo +" Articulo: "+vedmarDTO.getCodigoArticulo() + " STOCK ANTERIOR :" + exmarbDTO.getStockLinea() + "STOCK A MOVER : "+ vedmarDTO.getCantidadArticulo()+ " STOCK AHORA : "+stockLineaArt);
							}
							exmarb.actualizaStockLinea(26, vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(), exmarbDTO.getStockLinea()-vedmarDTO.getCantidadArticulo());

						}
					}else{
						logi.info("NO PASO POR ESTADO :"+eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim());
						if (tipoMov==3){
							actualiza.actualizaEstadoxEstado(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodigoBodega(), 
									eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim(), vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(),
									vedmarDTO.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);
						}else if (tipoMov==23){
							actualiza.actualizaEstado(vedmarDTO.getCodigoEmpresa(), vedmarDTO.getCodigoBodega(), 
									eElement.getElementsByTagName("INVSTS").item(0).getTextContent().trim(), vedmarDTO.getCodigoArticulo(), vedmarDTO.getDigArticulo(),
									vedmarDTO.getCantidadArticulo(), stockDAO, nombreArchivo, correlativoDetalle);

						}
					}
					correlativoDetalle++;
					
					
				}
				if (inser>0){
					logi.info("INSERTA VECMAR");
					VecmarDTO vecmarDTO = new VecmarDTO();
					vecmarDTO.setCodigoEmpresa(2);
					vecmarDTO.setCodTipoMvto(tipoMov);
					vecmarDTO.setFechaMvto(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setNumDocumento(correlativo);
					vecmarDTO.setCodigoDocumento(9);
					vecmarDTO.setNumeroTipoDocumento(correlativo2);
					vecmarDTO.setFechaDocumento(Integer.parseInt(fch.getYYYYMMDD()));
					vecmarDTO.setBodegaOrigen(26);
					vecmarDTO.setCantidadLineaDetalle(1);
					vecmarDTO.setTotalNeto(vedmarDTO.getMontoTotalNetoLinea());
					vecmarDTO.setTotalImptoAdicional(0);
					vecmarDTO.setTotalIva(0);
					vecmarDTO.setTotalDocumento(vedmarDTO.getMontoTotalLinea());
					vecmarDTO.setPesoTotalMovto(0);
					vecmarDTO.setCodigoJefeLocal(50);
					vecmar.generaMovimiento(vecmarDTO);
					VecfwmsDTO vecfwmsDTO = new VecfwmsDTO();
					vecfwmsDTO.setCodigoEmpresa(vedmarDTO.getCodigoEmpresa());
					vecfwmsDTO.setTipoMovto(vedmarDTO.getCodTipoMvto());
					vecfwmsDTO.setFechaMovimiento(vedmarDTO.getFechaMvto());
					vecfwmsDTO.setNumeroDocumento(vedmarDTO.getNumDocumento());
					vecfwmsDTO.setNombreArchivoXML(nombreArchivo);
					vecfwmsDTO.setTipo("A");
					vecfwmsDTO.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
					vecfwmsDTO.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
					vecfwms.generaArchivoXML(vecfwmsDTO);
				}
			}
			//Mueva archivo a carpeta de procesados
			
		}catch (Exception e){
			e.printStackTrace();
			email.mail(e.getMessage());
		}
		
		
	}
	public static void main(String []args){
		IntegracionAjusteHelper helper = new IntegracionAjusteHelper();
		helper.procesaAjuste2("/home2/ftp/in/INV_RCV_3748.xml", "INV_RCV_3748.xml");
		/*List lista = new ArrayList();
		helper.procesaFacturacion(2, 26, 13183,lista ,"");*/
				
	}
	public int moveFile(String urlFile, String nameFile){
		int numero=0;
		Path origenPath = FileSystems.getDefault().getPath(urlFile);
	    Path destinoPath = FileSystems.getDefault().getPath("/home2/ftp/proc/"+nameFile);


	    try {
	        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        System.err.println(e);
	    }
	    
	    
		return numero;
	}
}
