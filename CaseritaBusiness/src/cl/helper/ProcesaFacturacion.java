package cl.caserita.helper;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.print.attribute.standard.NumberOfDocuments;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.FormaXML;
import cl.caserita.company.user.wsclient.FormaXMLGuia;
import cl.caserita.company.user.wsclient.FormaXMLGuiaMermas;
import cl.caserita.company.user.wsclient.FormaXMLNotasDebito;
import cl.caserita.company.user.wsclient.FormarXMLBoleta;
import cl.caserita.company.user.wsclient.FormarXMLNotas;
import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ActecoDAO;
import cl.caserita.dao.iface.BdgcorrDAO;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dao.iface.ClcdiaDAO;
import cl.caserita.dao.iface.ClcmcoDAO;
import cl.caserita.dao.iface.CldmcoDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.ConnodDAO;
import cl.caserita.dao.iface.ConnohDAO;
import cl.caserita.dao.iface.ConnoiDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.EndPointWSDAO;
import cl.caserita.dao.iface.EnvdmailDAO;
import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.ImpcasiiDAO;
import cl.caserita.dao.iface.NotCorreDAO;
import cl.caserita.dao.iface.OrdvtadDAO;
import cl.caserita.dao.iface.PrdatcaDAO;
import cl.caserita.dao.iface.PrmprvDAO;
import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.EnvdmailDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.ImpcasiiDTO;
import cl.caserita.dto.OrdvtadDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.PrmprvDTO;
import cl.caserita.dto.RutaDocumentosDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.enviomail.main.EnvioMailDocumentoPDFXML;
import cl.caserita.enviomail.main.EnvioMailErrorFacturacion;
import cl.caserita.enviomail.main.envioMailAviso;



public class ProcesaFacturacion {

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
		    cl.paperless.respuesta5.Respuesta resp= new cl.paperless.respuesta5.Respuesta();
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
				 //Instancia invocacion de Web Service Paperless para el envio del documento
				 WsClient ws = new WsClient();				
				 String gen ="";
				//	List camtra3 = camtraDAO.obtenerDatosCamtra(empresa, codMovto, fechaMovto, numDocumento);
				 resp.setCodigo(5);
				 resp = ws.onlineGeneration(xml,String.valueOf(rut),String.valueOf(numDocumento), vecmar.getBodegaOrigen(),codigo,gen, tptempDTO.getRut(),endPointServer);			
			
				 if (resp.getCodigo()==0){
					 //System.out.println("Estado Documento:" + resp.getCodigo());
					 //Correlativo del documento
					 String res = resp.getMensaje();
					 if (codigo!=39 && codigo!=43){
						 log.info("Codigo:"+codigo);
						 vecmarDAO.actualizaVecmar(empresa, codMovto, fechaMovto, numDocumento);
						 log.info("A C T U A L I Z A  S W I T C H  P A G O  C A J A:"+" MOVTO :"+codMovto +"FECHA MOVTO :"+fechaMovto + " NUMERO :"+numDocumento);
					 }
					 
					 
					 clcmcoDAO.actualizaClcmco(empresa, clcmcoDTO.getCodDocumento(), Integer.parseInt(clcmcoDTO.getRutCliente()), clcmcoDTO.getDvCliente(), clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), Integer.parseInt(res));
			
					// System.out.println("Entra 6:"+fch5.getHHMMSS());
					 timbre = ws.onlineRecovery(codele, Integer.parseInt(res), tptempDTO.getRut());
			
					 //System.out.println("Entra 7:"+fch6.getHHMMSS());
					 //Main main = new Main();
					 //tedNuevo=main.generaImagen(timbre, vecmar.getRutProveedor(), String.valueOf(codigo), String.valueOf(fechaMovto), res);
					 String timbre2="";
					 //timbre ="";
					 //System.out.println("Numero Documento" + numDocumento);
					 //System.out.println("Numero Documento Paperless" + res);
					 BdgcorrDAO bdgcorr = (BdgcorrDAO) factory.getBdgCorrDAO();
					 NotCorreDAO notCorre = (NotCorreDAO) factory.getNotCorreDAO();
					 int numeroAtencion=bdgcorr.recupeNumAtencion(empresa, clcmcoDTO.getCodigoBodega());
					 notCorre.generaNumeroDocAtencion(empresa, numDocumento, clcmcoDTO.getCodigoBodega(), numeroAtencion);
					 
					 if (codigo!=39 && codigo!=43){
						 camtraDAO.actualizaCamtra(empresa, codMovto, numDocumento, fechaMovto, correlativoCamtra, Integer.parseInt(res), usuario, timbre2,String.valueOf(resp.getCodigo()) );
					 }
					 
					 correlativo=Integer.parseInt(res);
					 correlativoCamtra++;
					 
				 }else{
					 if (codigo!=39 && codigo!=43){
						 camtraDAO.actualizaEstadoCamtra(empresa, codMovto, numDocumento, fechaMovto, correlativoCamtra, String.valueOf(resp.getCodigo()) );
						 EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
							String mail = "jcanquil@caserita.cl";
							String datos="Factura/Boleta:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto+" "+"Rut:"+clmcli.getRutCliente()+" "+"DV:"+clmcli.getDvCliente()+" "+"Bodega:"+clcmcoDTO.getCodigoBodega()+"Codigo Documento :"+codigo;
							datos = datos + "XML:"+resp.getMensaje();
							mailC.envioMail("Nada",mail,datos);
					 }
				 }
				 String url="";
				 url = ws.onlineRecoveryUrl(codele, correlativo, tptempDTO.getRut());
				 String rutaXML = ws.onlineRecoveryTipoUrl(codele, correlativo, tptempDTO.getRut(),1);
				 log.info("URL PDF FC VARIAS"+url);
				 /*if (codigo==39){
					 url = ws.onlineRecoveryUrl(codele, correlativo, tptempDTO.getRut());
					 System.out.println("URL PDF FC VARIAS"+url);
				 }*/
				//Generar registro para leer correlativo desde el visual
				    if (resp.getCodigo()==0 ){
				    	//vecmarDAO.generaCorrelativoVisual(codMovto, fechaMovto, numDocumento, correlativo, timbre, String.valueOf(resp.getCodigo()));
				    	vecmarDAO.generaCorrelativoVisual(empresa, codMovto, fechaMovto, numDocumento, correlativo, timbre, String.valueOf(resp.getCodigo()),url.trim(), rutaXML.trim());
				    }
		    }
		  
		    
		   
		}
		catch(Exception e){
			EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
			String mail = "jcanquil@caserita.cl";
			String datos="Factura/Boleta:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto+" Codigo Documento :"+codigo;
			mailC.envioMail("Nada",mail,datos);
			e.printStackTrace();
		}
		
		//factory.closeConnection();
		return estado; 
	}
	
	public String procesaNotasCreditoDebito(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario, String tipo, String nota){
		String estado="D";
		String estadoDoc="";
		DAOFactory factory = DAOFactory.getInstance();
		VecmarDTO vecmarDTO=null;
		PrdatcaDTO prdatcaDTO = null;
		try{
			VecmarDAO vecmarDAO = (VecmarDAO) factory.getVecmarDAO();
			TptempDAO tptempDAO = (TptempDAO) factory.getTptempDAO();
			TptempDTO tptempDTO = tptempDAO.recuperaEmpresa(empresa);
			//vecmarDTO = vecmarDAO.obtenerDatosVecmar(codMovto, fechaMovto, numDocumento);
			ClmcliDAO clmcliDAO = (ClmcliDAO) factory.getClmcliDAO();
			CamtraDAO camtraDAO = (CamtraDAO) factory.getCamtraDAO();
			PrdatcaDAO prdatDAO = (PrdatcaDAO) factory.getPrdatcaDAO();
			ImpcasiiDAO impuestoSii = (ImpcasiiDAO) factory.getImpcasiiDAO();
			EnvdmailDAO envdMail = (EnvdmailDAO) factory.getEnvdmailDAO();
			 ClmcliDTO clmcli = clmcliDAO.recuperaCliente(String.valueOf(rut), dv);
			
			 //System.out.println("Paso a rescatar cliente");
			 CamtraDTO cam = camtraDAO.obtenerDatosCamtraNC(empresa, codigo, fechaMovto, numDocumento);
			 EndPointWSDAO endPointWS = (EndPointWSDAO) factory.getEndPointWSDAO();
				String endPointServer = endPointWS.buscaEndPoint();
				if (endPointServer==null  || endPointServer==""){
					return "Proceso Sin EndPoint";
				}
			 VecmarDTO vecmar =null;
			 int numNC=0;
			 if (codMovto!=0){
				 numNC = cam.getNumeroDocumento();
				 vecmar = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, numNC, clmcli);
				 prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numNC);
				 
			 }else{
				 prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numDocumento);
			 }
			//Obtiene Impuestos Caserita/SII
			 HashMap<Integer, Integer> impuestoSiiDa=null;
			 
				
				
			//Instacia CLCMCO
			ClcmcoDAO clcmcoDAO = (ClcmcoDAO) factory.getClcmcoDAO();
			
			//Instacia CLDMCO
			CldmcoDAO cldmcoDAO = (CldmcoDAO) factory.getCldmcoDAO();
			//Instacia CLCDIA
			ClcdiaDAO clcdiaDAO = (ClcdiaDAO) factory.getClcdiaDAO();
			ActecoDAO actecoDAO = (ActecoDAO) factory.getActecoDAO();
			List acteco = actecoDAO.buscaActeco();
			//Tipo Documento electronico
			int codele=0;
			String url="";
			String rutaXML="";
			TptdeleDAO tptdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
			ConnodDAO connodDAO = (ConnodDAO) factory.getConnodDAO();
			ConnohDAO connohDAO = (ConnohDAO) factory.getConnohDAO();
			ConnoiDAO connoiDAO = (ConnoiDAO) factory.getConnoiDAO();
			List clcmco = clcmcoDAO.obtieneClcmco(empresa, codigo, rut, dv, fechaMovto, numDocumento);
			if (vecmar==null && "C".equals(tipo)){
				vecmar = new VecmarDTO();
				vecmar.setRutProveedor(String.valueOf(clmcli.getRutCliente()));
				vecmar.setDvProveedor(clmcli.getDvCliente());
				vecmar.setDescComuna(clmcli.getDescComuna());
				vecmar.setFechaDocumento(fechaMovto);
				vecmar.setFechaDespacho(fechaMovto);
				
			}
			if ("E".equals(tipo)){
				tipo ="D";
			}else if("D".equals(tipo)){
				estadoDoc=tipo;
				tipo="C";
			}
			
			ConnohDTO connoh = connohDAO.buscaConnoh(empresa, tipo, numDocumento, fechaMovto);
			int fecha = clcmcoDAO.obtieneFechaFactura(empresa, connoh.getCodDocumento(), rut, dv, connoh.getNumeroDocumento(), connoh.getCodigoBodega());
			if (fecha>=20141001){
				 impuestoSiiDa = impuestoSii.obtieneImpuestoSII("S");
			 }else{
				  impuestoSiiDa = impuestoSii.obtieneImpuestoSII("A");
			 }
			List connod = new ArrayList();
			List clcdia = new ArrayList();
		    Iterator iter = clcmco.iterator();
		    int correlativo=0;
		    String timbre="";
		    cl.paperless.respuesta5.Respuesta resp=new cl.paperless.respuesta5.Respuesta();
		    log.info("Procesa Facturacion");
		    int correlativoCamtra=0;
		    if (codigo==35 ){
		    	 while (iter.hasNext()){
				    	ClcmcoDTO clcmcoDTO = new ClcmcoDTO();
				    	clcmcoDTO = (ClcmcoDTO) iter.next();
				    	//System.out.println("Hora Documento " + clcmcoDTO.getHoraMovimiento());
				    	connod = connodDAO.buscaConnod(empresa, tipo, numDocumento, fechaMovto, codigo);
				    	if (codigo==35){
				    		clcdia = clcdiaDAO.obtieneImpuesto(empresa, codigo, rut, dv, clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento());
				    	}else if (codigo==40){
				    		clcdia = connoiDAO.buscaImpto(tipo, numDocumento, fechaMovto);
				    	}else if (codigo==41){
				    		clcdia = connoiDAO.buscaImpto(tipo, numDocumento, fechaMovto);
				    	}
				    	
				    	
				    	String xml="";
				    	 
				    	 if (codigo==35 ){
				    		 FormarXMLNotas forma = new FormarXMLNotas();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml(vecmar, clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, nota, acteco,clcmcoDTO, tptempDTO,impuestoSiiDa,prdatcaDTO);
				    	 }else if (codigo ==40){
				    		 FormaXMLNotasDebito forma = new FormaXMLNotasDebito();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml( clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, acteco, tptempDTO,impuestoSiiDa,nota,prdatcaDTO);
				    	 }
						 
						 //System.out.println("Genero XML");
				    	 log.info("XML :" +xml);
						 WsClient ws = new WsClient();
						 String gen="NotasCredito";
						 resp.setCodigo(5);

						 resp = ws.onlineGeneration(xml,String.valueOf(rut), String.valueOf(numDocumento), vecmar.getBodegaOrigen(),codigo, gen,tptempDTO.getRut(),endPointServer);
						 log.info("Respuesta WS PP:"+resp.getCodigo());
						 if (resp.getCodigo()==0){
							 //System.out.println("Estado Documento:" + resp.getCodigo());
							 //Correlativo del documento
							 String res = resp.getMensaje();
							 vecmarDAO.actualizaVecmar(empresa, codMovto, fechaMovto, numNC);
							 
							 clcmcoDAO.actualizaClcmco(empresa, clcmcoDTO.getCodDocumento(), Integer.parseInt(clcmcoDTO.getRutCliente()), clcmcoDTO.getDvCliente(), clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), Integer.parseInt(res));
							 timbre = ws.onlineRecovery(codele, Integer.parseInt(res),tptempDTO.getRut());
							 String timbre2 ="";
							 BdgcorrDAO bdgcorr = (BdgcorrDAO) factory.getBdgCorrDAO();
							 NotCorreDAO notCorre = (NotCorreDAO) factory.getNotCorreDAO();
							 int numeroAtencion=bdgcorr.recupeNumAtencion(empresa, clcmcoDTO.getCodigoBodega());
							 notCorre.generaNumeroDocAtencion(empresa, numDocumento, clcmcoDTO.getCodigoBodega(), numeroAtencion);
							 url = ws.onlineRecoveryUrl(codele, Integer.parseInt(res), tptempDTO.getRut());
							 rutaXML = ws.onlineRecoveryTipoUrl(codele, correlativo, tptempDTO.getRut(),1);
							 log.info("URL PDF FC VARIAS"+url);
							 //System.out.println("Numero Documento" + numDocumento);
							 //System.out.println("Numero Documento Paperless" + res);
							 
							 if("D".equals(estadoDoc)){
									usuario="";
								}
							 if ("1".equals(nota) || "3".equals(nota)){
								 //System.out.println("Correlativo Camtra Entra"+cam.getCorrelativo());
								 if (cam!=null){
									 camtraDAO.actualizaCamtra(empresa, codMovto,numNC, fechaMovto, cam.getCorrelativo(), Integer.parseInt(res), usuario, timbre2,String.valueOf(resp.getCodigo()) );

								 }else{
									 numNC = numDocumento;
								 }
							 }else{
								 numNC = numDocumento;
							 }
							 
							 connodDAO.actualizaConnod(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 connohDAO.actualizaConnoh(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 connoiDAO.actualizaConnoi(tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 correlativo=Integer.parseInt(res);
							 correlativoCamtra=correlativoCamtra+1;
							 //System.out.println("Correlativo Camtra Suma"+correlativoCamtra);
							 enviaMail(factory, clmcli, vecmar, String.valueOf(codigo), res,  ws,  envdMail, empresa,  tptempDTO);
							 
						 }
						 else{
							 if (codigo!=39){
								 camtraDAO.actualizaEstadoCamtra(empresa, codMovto, numNC, fechaMovto, correlativoCamtra, String.valueOf(resp.getCodigo()) );
								 EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
									String mail = "jcanquil@caserita.cl";
									String datos="Nota Credito:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numNC+" "+"FechaMov:"+fechaMovto+" "+"Rut:"+clmcli.getRutCliente()+" "+"DV:"+clmcli.getDvCliente()+" "+"Bodega:"+connoh.getCodigoBodega()+" Codigo Documento :"+codigo;
									datos = datos +"XML:"+resp.getMensaje();
									mailC.envioMail("Nada",mail,datos);
							 }
						 }
						 
				    }
		    }
		    else if (codigo ==40 || codigo==41){
		    	
				    	
				    	connod = connodDAO.buscaConnod(empresa, tipo, numDocumento, fechaMovto, codigo);
				    	if (codigo==40 || codigo==41){
				    		clcdia = connoiDAO.buscaImpto(tipo, numDocumento, fechaMovto);
				    	}

				    	ClcmcoDTO clcmcoDTO = clcmcoDAO.obtieneClcmcoDTO(empresa, codigo, rut, dv, fechaMovto, numDocumento) ;
				    	/*clcmcoDTO.setValorNeto(connoh.getMontoNeto());
				    	clcmcoDTO.setTotalIva(connoh.getMontoIva());
				    	clcmcoDTO.setTotalDocumento(connoh.getMontoTotal());*/
				    	String xml="";
				    //	 int codele=0;
				    	 if (codigo==35 ){
				    		 FormarXMLNotas forma = new FormarXMLNotas();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml(vecmar, clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, nota, acteco,clcmcoDTO, tptempDTO,impuestoSiiDa,prdatcaDTO);
				    	 }else if (codigo ==40 || codigo==41){
				    		 FormaXMLNotasDebito forma = new FormaXMLNotasDebito();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml( clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, acteco, tptempDTO,impuestoSiiDa,nota,prdatcaDTO);
				    	 }
						 
						 //System.out.println("Genero XML");
				    	 log.info("XML :" +xml);
						 WsClient ws = new WsClient();
						 String gen="Notas";
						 resp.setCodigo(5);

						 resp = ws.onlineGeneration(xml, String.valueOf(rut), String.valueOf(numDocumento), connoh.getCodigoBodega(), codigo, gen,tptempDTO.getRut(),endPointServer);
						 if (resp.getCodigo()==0){
							 log.info("Estado Documento:" + resp.getCodigo());
							 //Correlativo del documento
							 String res = resp.getMensaje();
							 vecmarDAO.actualizaVecmar(empresa, codMovto, fechaMovto, numNC);
							 numNC = numDocumento;
							 correlativo = Integer.parseInt(res);
							
							 timbre = ws.onlineRecovery(codele, Integer.parseInt(res),tptempDTO.getRut());
							 String timbre2 ="";
							 url = ws.onlineRecoveryUrl(codele, Integer.parseInt(res), tptempDTO.getRut());
							 rutaXML = ws.onlineRecoveryTipoUrl(codele, correlativo, tptempDTO.getRut(),1);
							 log.info("URL PDF FC VARIAS"+url);
							 //System.out.println("Numero Documento" + numDocumento);
							 //System.out.println("Numero Documento Paperless" + res);
							 log.info("Estado Nota Credito:"+estadoDoc);
							 if("D".equals(estadoDoc)){
									usuario="";
								}
							 if (codigo!=41 && codMovto!=0){
								 log.info("Correlativo Camtra Entrada"+cam.getCorrelativo());
								 camtraDAO.actualizaCamtra(empresa, codMovto, numNC, fechaMovto, cam.getCorrelativo(), Integer.parseInt(res), usuario, timbre2,String.valueOf(resp.getCodigo()) );
								 connodDAO.actualizaConnod(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 connohDAO.actualizaConnoh(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 connoiDAO.actualizaConnoi(tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 correlativo=Integer.parseInt(res);
								 correlativoCamtra=correlativoCamtra+1;
							 }else if (codigo==41){
								 clcmcoDAO.actualizaClcmco(empresa, clcmcoDTO.getCodDocumento(), Integer.parseInt(clcmcoDTO.getRutCliente()), clcmcoDTO.getDvCliente(), clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), Integer.parseInt(res));
								 connodDAO.actualizaConnod(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 connohDAO.actualizaConnoh(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 connoiDAO.actualizaConnoi(tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 }else if (codigo==40){
								 
								 clcmcoDAO.actualizaClcmco(empresa, clcmcoDTO.getCodDocumento(), Integer.parseInt(clcmcoDTO.getRutCliente()), clcmcoDTO.getDvCliente(), clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), Integer.parseInt(res));
								 connodDAO.actualizaConnod(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 connohDAO.actualizaConnoh(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 connoiDAO.actualizaConnoi(tipo, numDocumento, fechaMovto, Integer.parseInt(res));
								 log.info("Actualiza Nota Debito");
							 }
							 
							 
							 
							 
						 }else{
							 if (codigo!=39){
								 camtraDAO.actualizaEstadoCamtra(empresa, codMovto, numNC, fechaMovto, correlativoCamtra, String.valueOf(resp.getCodigo()) );
								 //Enviar Mail de error
								EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
								String mail = "jcanquil@caserita.cl";
								String datos="Nota Credito:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numNC+" "+"FechaMov:"+fechaMovto+" "+"Rut:"+clmcli.getRutCliente()+" "+"DV:"+clmcli.getDvCliente()+" "+"Bodega:"+connoh.getCodigoBodega()+" Codigo Documento :"+codigo;
								datos = datos + "XML:"+resp.getMensaje();
								mailC.envioMail("Nada",mail,datos);
							 }
						 }
						 
				    
		    }
		   
		  //Generar registro para leer correlativo desde el visual
		    
		    
		    if (resp.getCodigo()==0 ){
		    	log.info("Genera Correlativo DOCGENEL");
		    	vecmarDAO.generaCorrelativoVisual(empresa, codMovto, fechaMovto, numNC, correlativo, timbre,String.valueOf(resp.getCodigo()),url.trim(),rutaXML.trim());
		    }
		    
		   
		}
		catch(Exception e){
			EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
			String mail = "jcanquil@caserita.cl";
			String datos="Nota Credito:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto+" Codigo Documento :"+codigo;
			//datos = datos + "Error:"+e.toString();
			mailC.envioMail("Nada",mail,datos);
			e.printStackTrace();
		}
		//factory.closeConnection();
		//factory.getInstance().closeConnection();
		//factory.closeConnection();
		return estado; 
	}
	
	public String procesaGuia(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario, String tipo){
		String estado="D";
		DAOFactory factory = DAOFactory.getInstance();
		VecmarDTO vecmarDTO=null;
		String url="";
		String rutaXML="";
		try{
			
			//Tipo Documento electronico
			VecmarDAO vecmarDAO = (VecmarDAO) factory.getVecmarDAO();
			ImpcasiiDAO impuestoSii = (ImpcasiiDAO) factory.getImpcasiiDAO();
			TptdeleDAO tptdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
			TptbdgDAO tptbdgDAO = (TptbdgDAO) factory.getTptbdgDAO();
			TptempDAO tptempDAO = (TptempDAO) factory.getTptempDAO();			
			TptempDTO tptempDTO = tptempDAO.recuperaEmpresa(empresa);
			EndPointWSDAO endPointWS = (EndPointWSDAO) factory.getEndPointWSDAO();
			String endPointServer = endPointWS.buscaEndPoint();
			if (endPointServer==null  || endPointServer==""){
				return "Proceso Sin EndPoint";
			}
			ExmtraDAO exmtraDAO =null;
			ExdtraDAO exdtraDAO=null;
			ExmtraDTO emtraDTO =null;
			VedmarDAO vedmarDAO =null;
			PrmprvDAO prmprvDAO =null;
			TptbdgDTO tptbdg = null;
			PrdatcaDAO prdatDAO=null;
			PrmprvDTO prmprvDTO =null;
			 ClmcliDTO clmcli=null;
			 PrdatcaDTO prdatcaDTO=null;
			 ClmcliDAO clmcliDAO=null;
			 
			//Obtiene Impuestos Caserita/SII
			HashMap<Integer, Integer> impuestoSiiDa = impuestoSii.obtieneImpuestoSII("S");
				
			List ved =null;
			if ("T".equals(tipo)){
				exmtraDAO = (ExmtraDAO) factory.getExmtraDAO();
				exdtraDAO = (ExdtraDAO) factory.getExdtraDAO();
				
				emtraDTO = exmtraDAO.recuperaEncabezadoFE(empresa, numDocumento);
			}else if ("G".equals(tipo)){
				prdatDAO = (PrdatcaDAO) factory.getPrdatcaDAO();
				clmcliDAO = (ClmcliDAO) factory.getClmcliDAO();
				clmcli = clmcliDAO.recuperaCliente(String.valueOf(rut), dv);
				prmprvDTO = new PrmprvDTO();
				prmprvDTO.setDescComunaProv(clmcli.getDescComuna());
				prmprvDTO.setDescCiudadProv(clmcli.getDescCiudad());
				prmprvDTO.setDireccionProv(clmcli.getDireccionCliente());
				prmprvDTO.setRazonSocialProv(clmcli.getRazonsocial());
				vedmarDAO = (VedmarDAO) factory.getVedmarDAO();
				prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numDocumento);
				
			}else if("M".equals(tipo)){
				//Proveedor para mermas				
				clmcli = new ClmcliDTO();
				prmprvDAO = (PrmprvDAO) factory.getPrmprvDAO();
				prmprvDTO = prmprvDAO.obtieneProveedor(rut, dv);
				clmcli.setCodRegion(13);
				clmcli.setCodComuna(10);
				clmcli.setCodCiudad(1);
				vedmarDAO = (VedmarDAO) factory.getVedmarDAO();
			}
			ActecoDAO actecoDAO = (ActecoDAO) factory.getActecoDAO();
			List acteco = actecoDAO.buscaActeco();
		    List exdtra = null;
		    int correlativo=0;
		    String timbre="";
		    cl.paperless.respuesta5.Respuesta resp= new cl.paperless.respuesta5.Respuesta();
		    //System.out.println("Procesa Facturacion");
		    int correlativoCamtra=0;
		    log.info("Tipo de Generacion :"+tipo);
		    vecmarDTO = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, numDocumento, clmcli);
		    	
		    if ("T".equals(tipo)){
		    	exdtra = exdtraDAO.recuperaDetalle(empresa,numDocumento);
		    	tptbdg = tptbdgDAO.buscaBodega(emtraDTO.getBodegaDestino());
		    }else if ("M".equals(tipo) || "G".equals(tipo)){
		    	
				ved = vedmarDAO.obtenerDatosVedmarGuia(empresa, codMovto, fechaMovto, numDocumento);
				tptbdg = tptbdgDAO.buscaBodega(vecmarDTO.getBodegaOrigen());
				//prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numDocumento);
			}
		    
		    			    	
		    	String xml="";
		    	 int codele=0;
		    	 if (codigo==38 && "T".equals(tipo) ){
		    		 FormaXMLGuia forma = new FormaXMLGuia();
		    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
		    		 int codeleref = 0;
					 xml =forma.xml(emtraDTO, exdtra, codele,codeleref,tptbdg, acteco,tptempDTO,impuestoSiiDa);
		    	 }else if (codigo==38 ){
		    		 if ("M".equals(tipo) || "G".equals(tipo)){
		    		 FormaXMLGuiaMermas forma = new FormaXMLGuiaMermas();
		    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
		    		 emtraDTO = new ExmtraDTO();
		    		 emtraDTO.setBodegaOrigen(vecmarDTO.getBodegaOrigen());
		    		 int codeleref = 0;
		    		 xml =forma.xml(vecmarDTO, ved,prmprvDTO, codele,codeleref, acteco, tptempDTO, tptbdg,prdatcaDTO);
		    		 }
		    	 
		    	 }
				 
				 //System.out.println("Genero XML");
		    	 log.info("XML :" +xml);
				 WsClient ws = new WsClient();
				 String gen="Guias";
				 resp = ws.onlineGeneration(xml, String.valueOf(rut), String.valueOf(numDocumento), 1, codigo, gen,tptempDTO.getRut(),endPointServer);
				 if (resp.getCodigo()==0){
					 //System.out.println("Estado Documento:" + resp.getCodigo());
					 //Correlativo del documento
					 String res = resp.getMensaje();
					 url = ws.onlineRecoveryUrl(codele, Integer.parseInt(res), tptempDTO.getRut());
					 rutaXML = ws.onlineRecoveryTipoUrl(codele, correlativo, tptempDTO.getRut(),1);
					 log.info("URL PDF FC VARIAS"+url);
					 
					 vecmarDAO.actualizaVecmarGuias(empresa, codMovto, fechaMovto, numDocumento, Integer.parseInt(res));
					 BdgcorrDAO bdgcorr = (BdgcorrDAO) factory.getBdgCorrDAO();
					 NotCorreDAO notCorre = (NotCorreDAO) factory.getNotCorreDAO();
					 int numeroAtencion=bdgcorr.recupeNumAtencion(empresa, emtraDTO.getBodegaOrigen());
					 notCorre.generaNumeroDocAtencion(empresa, numDocumento, emtraDTO.getBodegaOrigen(), numeroAtencion);
					 if (codigo==38 && "T".equals(tipo)){
						 exmtraDAO.actualizaExmtra(empresa, emtraDTO.getNumTraspaso(), Integer.parseInt(res));
					 }
					 
					 timbre = ws.onlineRecovery(codele, Integer.parseInt(res),tptempDTO.getRut());
					 //timbre ="";
					 //System.out.println("Numero Documento" + numDocumento);
					 //System.out.println("Numero Documento Paperless" + res);
					
					 correlativo=Integer.parseInt(res);
					 correlativoCamtra++;
					 
				 }
				 
		   
		  //Generar registro para leer correlativo desde el visual
				
		    if (resp.getCodigo()==0 ){
		    	vecmarDAO.generaCorrelativoVisual(empresa, codMovto, fechaMovto, numDocumento, correlativo, timbre,String.valueOf(resp.getCodigo()),url.trim(),rutaXML.trim());
		    }
		    
		   
		}
		catch(Exception e){
			EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
			String mail = "jcanquil@caserita.cl";
			String datos="Guias:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto+" Codigo Documento :"+codigo;
			mailC.envioMail("Nada",mail,datos);
			e.printStackTrace();
		}
		//factory.closeConnection();
		//factory.getInstance().closeConnection();
		//factory.closeConnection();
		return estado; 
	}
	public static void main (String args[]){
		
		ProcesaFacturacion procesa = new ProcesaFacturacion();
	/*	int codigo=0;
		int fecha=0;
		int numero=0;
		int rut=0;
		String dv="";
		String usuario=""; 
		String tipo="";
		int codigoDocumento=0;
		
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Codigo Movimiento
				codigo=Integer.parseInt(args[i]);
			}else if(i==1){
				//Fecha Movimiento
				fecha=Integer.parseInt(args[i]);
			}else if(i==2){
				//NUmero Documento
				numero=Integer.parseInt(args[i]);
			}else if(i==3){
				//Codigo Documento 3 o 4
				codigoDocumento=Integer.parseInt(args[i]);
			}else if(i==4){
				//Rut Cliente
				rut=Integer.parseInt(args[i]);
			}else if(i==5){
				//DV Cliente
				dv=args[i];
			}else if(i==6){
				usuario=args[i];
			}else if(i==7){
				tipo=args[i];
			}
		}		*/
		
		int codigo=0;
		int fecha=0;
		int numero=0;
		int rut=0;
		String dv="";
		String usuario=""; 
		String tipo="";
		int nota =0;
		int codigoDocumento=0;
		int empresa=0;
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Empresa
				empresa=Integer.parseInt(args[i]);
			}else if(i==1){
				//Tipo Mov
				codigo=Integer.parseInt(args[i]);
			}else if(i==2){
				//Fecha
				fecha=Integer.parseInt(args[i]);
			}else if(i==3){
				//Numero
				numero=Integer.parseInt(args[i]);
			}else if(i==4){
				//Cod Documento
				codigoDocumento=Integer.parseInt(args[i]);
			}else if(i==5){
				//Rut
				rut=Integer.parseInt(args[i]);
			}else if(i==6){
				dv=args[i];
			}else if(i==7){
				usuario=args[i];
			}else if(i==8){
				tipo=args[i];
			}else if(i==9){
				nota=Integer.parseInt(args[i]);
			}
		}		
		
		procesa.procesa(empresa, codigo, fecha, numero, rut, dv, codigoDocumento, usuario, tipo, String.valueOf(nota));
		//int empresa, int tipoD, int fechaDoc, int numeroDoc, int rutCl, String dvCl, int codigoDoc, String usuarioFc, String tipoMov, String nota){
		//System.out.println("Codigo :" + codigo);
		//System.out.println("fecha :" + fecha);
		//System.out.println("numero :" + numero);
		//System.out.println("rut :" + rut);
		//System.out.println("DV :" + dv);
		//System.out.println("codigoDocumento" + codigoDocumento);
		
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		
		log.info("Entra Proceso:"+fch.getHHMMSS() );
		String fechaLOG = fch.getFechaconFormato();
		String nombreArchivoAProcesar=fechaLOG;
		String archivo="LOGDocumento";
		archivoLog=prop.getProperty("archivos.salida.path")+archivo+"_"+fechaStr+".log";;
		File f=new File(archivoLog);
		if (f.exists()){
			//System.out.println("No borra");
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
		try{
		//System.out.println("Archivo LOG:"+archivoLog);
		File archivoEntrada = new File(nombreArchivoAProcesar+".txt");
		fileWriterLog.write( codigo+","+ fecha+","  + numero+"," +rut +"," +dv +","  +codigoDocumento +"," +usuario+"," +tipo+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	/*	if (codigoDocumento==35 || codigoDocumento==40 || codigoDocumento==41){
			//procesa.procesaNotasCreditoDebito(codigo, fecha, numero, rut, dv, codigoDocumento, usuario,tipo);
		}else if (codigoDocumento==33 || codigoDocumento==34 || codigoDocumento==33 || codigoDocumento==36 || codigoDocumento==37 || codigoDocumento==39){
			procesa.procesaFacturacion(codigo, fecha, numero, rut, dv, codigoDocumento, usuario);
		}else if (codigoDocumento==38){
			procesa.procesaGuia(codigo, fecha, numero, rut, dv, codigoDocumento, usuario, tipo);
		}*/
		Fecha fch2 = new Fecha();
		log.info("Termino:" +fch2.getHHMMSS());
		
	}
	
	public void procesa(int empresa, int tipoD, int fechaDoc, int numeroDoc, int rutCl, String dvCl, int codigoDoc, String usuarioFc, String tipoMov, String nota){
		
		ProcesaFacturacion procesa = new ProcesaFacturacion();
		int codigo=tipoD;
		int fecha=fechaDoc;
		int numero=numeroDoc;
		int rut=rutCl;
		String dv=dvCl;
		String usuario=usuarioFc; 
		String tipo=tipoMov;
		int codigoDocumento=codigoDoc;
		
				
		//System.out.println("Codigo :" + codigo);
		//System.out.println("fecha :" + fecha);
		//System.out.println("numero :" + numero);
		//System.out.println("rut :" + rut);
		//System.out.println("DV :" + dv);
		//System.out.println("codigoDocumento" + codigoDocumento);
		
		/*prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		
		System.out.println("Entra Proceso:"+fch.getHHMMSS() );
		String fechaLOG = fch.getFechaconFormato();
		String nombreArchivoAProcesar=fechaLOG;
		String archivo="LOGDocumento";
		archivoLog=prop.getProperty("archivos.salida.path")+archivo+"_"+fechaStr+".log";;
		File f=new File(archivoLog);
		if (f.exists()){
			//System.out.println("No borra");
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
		try{
		//System.out.println("Archivo LOG:"+archivoLog);
		File archivoEntrada = new File(nombreArchivoAProcesar+".txt");
		fileWriterLog.write( codigo+","+ fecha+","  + numero+"," +rut +"," +dv +","  +codigoDocumento +"," +usuario+"," +tipo+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}*/
		if (codigoDocumento==35 || codigoDocumento==40 || codigoDocumento==41){
			procesa.procesaNotasCreditoDebito(empresa, codigo, fecha, numero, rut, dv, codigoDocumento, usuario,tipo, nota);
		}else if (codigoDocumento==33 || codigoDocumento==34 || codigoDocumento==33 || codigoDocumento==36 || codigoDocumento==37 || codigoDocumento==39 || codigoDocumento==43){
			procesa.procesaFacturacion(empresa, codigo, fecha, numero, rut, dv, codigoDocumento, usuario);
		}else if (codigoDocumento==38){
			procesa.procesaGuia(empresa, codigo, fecha, numero, rut, dv, codigoDocumento, usuario, tipo);
		}
		Fecha fch2 = new Fecha();
		log.info("Termino:" +fch2.getHHMMSS());
		
	}
	
	public int enviaMail(DAOFactory factory, ClmcliDTO clmcli, VecmarDTO vecmar, String tipo, String res, WsClient ws, EnvdmailDAO envdMail, int empresa, TptempDTO tptempDTO){
		int resp=0;
		log.info("Vendedor :"+vecmar.getCodigoVendedor());
		log.info("Tipo Documento :"+tipo);
		 EnvdmailDTO envDMAIL = envdMail.rescataDocumentos(empresa, Integer.parseInt(tipo), vecmar.getCodigoVendedor());
		 if (envDMAIL!=null){
			 if ( envDMAIL.getCodigoDocumento()!=0 && clmcli.getEmailCliente().trim().length()>0){
				 
					TptdeleDAO tpdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
					TptempDAO tpt = (TptempDAO) factory.getTptempDAO();
					TptempDTO  dto=tpt.recuperaEmpresa(empresa);
					
					int codigoCaserita = tpdeleDAO.buscaDocumentoElectronico(Integer.parseInt(tipo));
					RutaDocumentosDTO ruta = null;
					List rutaDoc = new ArrayList();
					try{
						
						log.info("Numeros :"+Integer.parseInt(res));
						log.info("CodCaserita :"+codigoCaserita);
						log.info("NumeroFactura :"+Integer.parseInt(res));
						log.info("Empresa :"+dto.getRut());
				 		String xml = ws.onlineRecoveryTipoUrl(codigoCaserita, Integer.parseInt(res), tptempDTO.getRut(), 1);
						String pdf = ws.onlineRecoveryTipoUrl(codigoCaserita, Integer.parseInt(res), tptempDTO.getRut(), 2);
						log.info("pdf :"+pdf);
						int ip = Integer.parseInt(pdf.substring(17, 18));
						if (ip==4){
							pdf = pdf.trim()+"&tmpl="+"76288567"+"/"+codigoCaserita+".jasper";
						}
						log.info("URL XML:"+xml);
						log.info("URL PDF:"+pdf);
						ProcesaEnvioDocumento proceso = new ProcesaEnvioDocumento();
						String rutaXML = proceso.enviaXML(Integer.parseInt(res), clmcli.getRutCliente(), Integer.parseInt(tipo),xml,".xml");
						String rutaPDF = proceso.enviaXML(Integer.parseInt(res), clmcli.getRutCliente(), Integer.parseInt(tipo),pdf,".pdf");
						
						log.info("Ruta XML:"+rutaXML);
						log.info("Ruta PDF:"+rutaPDF);
						ruta = new RutaDocumentosDTO();
						ruta.setTipoObjeto(1);
						ruta.setRutaObjeto(rutaXML);
						rutaDoc.add(ruta);
						ruta = new RutaDocumentosDTO();
						ruta.setTipoObjeto(2);
						ruta.setRutaObjeto(rutaPDF);
						rutaDoc.add(ruta);
						
						EnvioMailDocumentoPDFXML envia = new EnvioMailDocumentoPDFXML();
						envia.envioMail("",clmcli.getEmailCliente().trim(), envDMAIL.getMail().trim(), "Documentos Tributarios -  Nota de Credito Electronica", "",rutaDoc);
					}catch (Exception e){
						e.printStackTrace();
					}
						 		
						 		
			 }
		 }
			return resp;

		
	}
	
}
