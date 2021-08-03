package cl.caserita.helper;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.FormaXML;
import cl.caserita.company.user.wsclient.FormarXMLBoleta;
import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ActecoDAO;
import cl.caserita.dao.iface.BdgcorrDAO;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dao.iface.ClcdiaDAO;
import cl.caserita.dao.iface.ClcmcoDAO;
import cl.caserita.dao.iface.CldmcoDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.EndPointWSDAO;
import cl.caserita.dao.iface.ImpcasiiDAO;
import cl.caserita.dao.iface.NotCorreDAO;
import cl.caserita.dao.iface.OrdvtadDAO;
import cl.caserita.dao.iface.PrdatcaDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.OrdvtadDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.enviomail.main.EnvioMailErrorFacturacion;

public class ProcesaPreEmisionDocumento {

	
	private static Logger log = Logger.getLogger(ProcesaFacturacion.class); 
	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;

	

	public String procesaFacturacion(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario){
		String estado="D";
		DAOFactory factory = DAOFactory.getInstance();
		
		
		VecmarDTO vecmarDTO=null;
		try{
			VecmarDAO vecmarDAO = (VecmarDAO) factory.getVecmarDAO();
			VedmarDAO vedmarDAO = (VedmarDAO) factory.getVedmarDAO();

			TptempDAO tptempDAO = (TptempDAO) factory.getTptempDAO();
			TptbdgDAO tptbdgDAO = (TptbdgDAO) factory.getTptbdgDAO();
			TptempDTO tptempDTO = tptempDAO.recuperaEmpresa(empresa);
			log.info("Pruebas de ingreso de facturacion Log");
			PrdatcaDTO prdatcaDTO = null;
			ClmcliDAO clmcliDAO = (ClmcliDAO) factory.getClmcliDAO();
			ActecoDAO actecoDAO = (ActecoDAO) factory.getActecoDAO();
			PrdatcaDAO prdatDAO = (PrdatcaDAO) factory.getPrdatcaDAO();
			ImpcasiiDAO impuestoSii = (ImpcasiiDAO) factory.getImpcasiiDAO();
			EndPointWSDAO endPointWS = (EndPointWSDAO) factory.getEndPointWSDAO();
			DetordDAO detordDAO = (DetordDAO) factory.getDetordDAO();
			OrdvtadDAO ordvtadDAO = (OrdvtadDAO) factory.getOrdvtadDAO();
			String endPointServer = endPointWS.buscaEndPoint();
			log.info("EndPoint :" + endPointServer);
			if (endPointServer==null  || endPointServer==""){
				return "Proceso Sin EndPoint";
			}
			List acteco = actecoDAO.buscaActeco();
			//Obtiene Datos Clientes
			 ClmcliDTO clmcli = clmcliDAO.recuperaCliente(String.valueOf(rut), dv);
			
			 VecmarDTO vecmar=null;
			 if (codigo!=39 && codigo!=43){
				 //Obtiene datos VECMAR y datos de direccion, comuna, ciudad
				 vecmar = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, numDocumento, clmcli);
				 //Obtiene datos adicionales como Forma pago, numero OV, numero carguio etc
				 prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numDocumento);
				 
				 int numeroOV = detordDAO.recuperaOrdenVenta(empresa, 21, numDocumento, rut);
				 log.info("Recupera Numero Orden :"+numeroOV);
				 if (numeroOV>0){
					 OrdvtadDTO ordvtadDTO = ordvtadDAO.obtieneOrdenes(empresa, numeroOV, 21, rut);
					 if (prdatcaDTO!=null && ordvtadDTO!=null ){
						 prdatcaDTO.setNumeroOrden(ordvtadDTO.getNumeroOC());
						 prdatcaDTO.setFechaOrden(ordvtadDTO.getFechaOC());
						 log.info("Recupera Numero OC :"+prdatcaDTO.getNumeroOrden().trim());
						 log.info("Recupera Fecha OC :"+prdatcaDTO.getFechaOrden().trim());


					 }
				 }
				 //TptbdgDTO tptbg = tptbdgDAO.buscaBodega(vecmar.getBodegaOrigen());
				 //System.out.println("Obtuvo datos CARGUIO");
			 }
			 
			ClcmcoDAO clcmcoDAO = (ClcmcoDAO) factory.getClcmcoDAO();
			CamtraDAO camtraDAO = (CamtraDAO) factory.getCamtraDAO();
			ProcedimientoDAO proce = (ProcedimientoDAO) factory.getProcedimientoDAO();

			//Instacia CLDMCO
			CldmcoDAO cldmcoDAO = (CldmcoDAO) factory.getCldmcoDAO();
			//Instacia CLCDIA
			ClcdiaDAO clcdiaDAO = (ClcdiaDAO) factory.getClcdiaDAO();
			//Tipo Documento electronico
			TptdeleDAO tptdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
			
			//Obtiene Impuestos Caserita/SII
			HashMap<Integer, Integer> impuestoSiiDa = impuestoSii.obtieneImpuestoSII("S");
			
			
			//Obtiene registros CLCMCO 
			List clcmco = clcmcoDAO.obtieneClcmco(empresa, codigo, rut, dv, fechaMovto, numDocumento);
			List camtra2 = camtraDAO.obtenerDatosCamtra(empresa, codMovto, fechaMovto, numDocumento);
			
			if (codigo!=39 && codigo!=43){
				if (camtra2.size()<=0){
					return "error";
				}
			}
			/*if (vedmarDAO.verificaVenta(empresa, codMovto, fechaMovto, numDocumento)==0){
				log.info("Solo posee flete");

				return "Solo posee FLETE ";
			}*/
			
			log.info("DATOS AL INGRESAR A PROCESO");
			log.info("Empresa:"+empresa);
			log.info("Codigo Doc:"+codigo);
			log.info("Rut :"+rut);
			log.info("DV :"+dv);
			log.info("Fecha :"+fechaMovto);
			log.info("Numero Documento :"+numDocumento);
			log.info("Largo Arreglo CLCMCO :"+clcmco.size());
			
		    Iterator iter = clcmco.iterator();
		    int correlativo=0;
		    String timbre="";
		    String tedNuevo="";
		    cl.paperless.respuesta5.Respuesta resp=null;
		    //System.out.println("Procesa Facturacion");
		    int correlativoCamtra=0;
		    //Recorre proceso de documentos, depende de la cantidad de registros encontrados en CLCMCO es la cantidad de documentos a generar
		    while (iter.hasNext()){
		    	//System.out.println("Procesa0");
		    	ClcmcoDTO clcmcoDTO = new ClcmcoDTO();
		    	clcmcoDTO = (ClcmcoDTO) iter.next();
		    	//System.out.println("NETO CLCMCO:"+clcmcoDTO.getValorNeto());
		    	//vecmar.setTotalNeto(clcmcoDTO.getValorNeto());
		    	List cldmco = new ArrayList();
				List clcdia = new ArrayList();
				 if (codigo==39 || codigo==43){
					 vecmar = new VecmarDTO();
	    			 vecmar.setFechaDocumento(clcmcoDTO.getFechaMovimiento());
	    			 vecmar.setRutProveedor(String.valueOf(clmcli.getRutCliente()));
	    			 vecmar.setDvProveedor(clmcli.getDvCliente());
	    			 vecmar.setDireccionDespacho(clmcli.getDireccionCliente());
	    			 vecmar.setBodegaOrigen(clcmcoDTO.getCodigoBodega());
	    			 vecmar.setCodigoTipoVendedor(0);
	    			 vecmar.setDescComuna(clmcli.getDescComuna());
	    			 vecmar.setTotalNeto(clcmcoDTO.getTotalDocumento()-clcmcoDTO.getTotalIva());
	    			 vecmar.setTotalDocumento(clcmcoDTO.getTotalDocumento());
	    			 vecmar.setTotalIva(clcmcoDTO.getTotalIva());
	    			 vecmar.setFormaPago("1");
	    			// System.out.println("Bodega:"+clcmcoDTO.getCodigoBodega());
	    			 vecmar.setBodegaOrigen(clcmcoDTO.getCodigoBodega());
	    		 }
				 
				
				
				//Obtiene articulo de venta 
		    	cldmco = cldmcoDAO.obtieneArticulos(empresa, codigo, rut, dv, clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), vecmar.getBodegaOrigen(), vecmar.getCodigoTipoVendedor(),codMovto, numDocumento);
		    	
		    	
				//Obtiene impuestos generales de la venta
		    	clcdia = clcdiaDAO.obtieneImpuesto(empresa, codigo, rut, dv, clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento());
		    	
		    	
				
		    	String xml="";
		    	 int codele=0;
		    	 if (codigo==33 || codigo==36 || codigo==39 || codigo==43){
		    		 FormaXML forma = new FormaXML();
		    		 //Obtiene codigo doc electronico de Paperless
		    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
		    		 //Forma XML a Enviar
					 xml =forma.xml(vecmar, clcdia, cldmco, clmcli, codele,clcmcoDTO, acteco, tptempDTO,prdatcaDTO,impuestoSiiDa);
				
		    	 }else if(codigo==34 || codigo==37){
		    		 FormarXMLBoleta forma = new FormarXMLBoleta();
		    		//Obtiene codigo doc electronico de Paperless
		    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
		    		//Forma XML a Enviar
		    		 xml =forma.xml(vecmar, clcdia, cldmco, clmcli, codele, tptempDTO,clcmcoDTO, prdatcaDTO,impuestoSiiDa);
		    	 }
				 
				 
		    	 //XML a enviar a Paperless
		    	 log.info("XML :" +xml);
			
				 
				
		    }
		  
		    
		   
		}
		catch(Exception e){
			EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
			String mail = "jcanquil@caserita.cl";
			String datos="Factura/Boleta:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto+" Codigo Documento :"+codigo;
			mailC.envioMail("Nada",mail,datos);
			e.printStackTrace();
		}
		
		
		return estado; 
	}
}
