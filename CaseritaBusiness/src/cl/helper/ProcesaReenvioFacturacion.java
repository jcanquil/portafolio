package cl.caserita.helper;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.FormaXML;
import cl.caserita.company.user.wsclient.FormaXMLGuia;
import cl.caserita.company.user.wsclient.FormaXMLGuiaMermas;
import cl.caserita.company.user.wsclient.FormaXMLNotasDebito;
import cl.caserita.company.user.wsclient.FormaXMLReenvioFacturas;
import cl.caserita.company.user.wsclient.FormarXMLBoleta;
import cl.caserita.company.user.wsclient.FormarXMLNotas;
import cl.caserita.company.user.wsclient.FormarXMLReenvioGuia;
import cl.caserita.company.user.wsclient.FormarXMLReenvioGuiaMermas;
import cl.caserita.company.user.wsclient.FormarXMLReenvioNotas;
import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.fecha.Fecha;
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
import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.ImpcasiiDAO;
import cl.caserita.dao.iface.NotCorreDAO;
import cl.caserita.dao.iface.PrdatcaDAO;
import cl.caserita.dao.iface.PrmprvDAO;
import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.PrmprvDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.enviomail.main.EnvioMailErrorFacturacion;

public class ProcesaReenvioFacturacion {
	private static Logger log = Logger.getLogger(ProcesaFacturacion.class); 
	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
public static void main (String[]args){
		
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
				//Codigo Movimiento
				empresa=Integer.parseInt(args[i]);
			}else if(i==1){
				//Fecha Movimiento
				codigo=Integer.parseInt(args[i]);
			}else if(i==2){
				//NUmero Documento
				fecha=Integer.parseInt(args[i]);
			}else if(i==3){
				//Codigo Documento 3 o 4
				numero=Integer.parseInt(args[i]);
			}else if(i==4){
				//Rut Cliente
				codigoDocumento=Integer.parseInt(args[i]);
			}else if(i==5){
				//DV Cliente
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
		ProcesaReenvioFacturacion pro = new ProcesaReenvioFacturacion();
		//pro.procesaFacturacion(empresa, codigo, fecha, numero, rut, dv, codigoDocumento, usuario);
		if (codigoDocumento==35 || codigoDocumento==40 || codigoDocumento==41){
//int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario, String tipo, String nota){

			pro.procesaNotasCreditoDebito(empresa,codigo,fecha, numero, rut, dv, codigoDocumento, usuario,tipo, String.valueOf(nota));
		}else if (codigoDocumento==38){
//(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario, String tipo){
			pro.procesaGuia(empresa,codigo,fecha, numero, rut, dv, codigoDocumento, usuario,tipo);
		}else if (codigoDocumento==33 || codigoDocumento==36){
			pro.procesaFacturacion(empresa,codigo,fecha, numero, rut, dv, codigoDocumento, usuario);
		}
		//pro.procesaGuia(empresa, codigo, fecha, numero, rut, dv, codigoDocumento, usuario, tipo);
}
public String procesaFacturacion(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario){
	String estado="D";
	DAOFactory factory = DAOFactory.getInstance();
	
	// Fecha fch81 = new Fecha();
	// log.info("Entra 1:"+fch81.getHHMMSS());
	VecmarDTO vecmarDTO=null;
	try{
		//Inicializa DAO
		VecmarDAO vecmarDAO = (VecmarDAO) factory.getVecmarDAO();	
		TptempDAO tptempDAO = (TptempDAO) factory.getTptempDAO();	
		TptempDTO tptempDTO = tptempDAO.recuperaEmpresa(empresa);	
		ClmcliDAO clmcliDAO = (ClmcliDAO) factory.getClmcliDAO();
		ActecoDAO actecoDAO = (ActecoDAO) factory.getActecoDAO();
		ImpcasiiDAO impuestoSii = (ImpcasiiDAO) factory.getImpcasiiDAO();
		PrdatcaDAO prdatDAO = (PrdatcaDAO) factory.getPrdatcaDAO();
		//Busca ACTECO relacionados con la empresa
		List acteco = actecoDAO.buscaActeco();
		//Busca Cliente
		 ClmcliDTO clmcli = clmcliDAO.recuperaCliente(String.valueOf(rut), dv);
		
		 PrdatcaDTO prdat = new PrdatcaDTO();
		 VecmarDTO vecmar=null;
		 if (codigo!=39){
			 //log.info("Entra por VECMAR:"+codigo);
			 vecmar = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, numDocumento, clmcli);
			 prdat = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numDocumento);

			 if (vecmar.getNumeroTipoDocumento()!=numDocumento){
				 numDocumento = vecmar.getNumeroTipoDocumento();
			 }
		 }
	
		//Instacia CLCMCO
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
		
		List clcmco = clcmcoDAO.obtieneClcmco(empresa, codigo, rut, dv, fechaMovto, numDocumento);
		log.info("PRUEBAS LOG");
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
	    //log.info("Procesa Facturacion");
	    int correlativoCamtra=0;
	    while (iter.hasNext()){
	    	//log.info("Procesa0");
	    	ClcmcoDTO clcmcoDTO = new ClcmcoDTO();
	    	clcmcoDTO = (ClcmcoDTO) iter.next();
	    	//log.info("NETO CLCMCO:"+clcmcoDTO.getValorNeto());
	    	//vecmar.setTotalNeto(clcmcoDTO.getValorNeto());
	    	List cldmco = new ArrayList();
			List clcdia = new ArrayList();
			 if (codigo==39){
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
    			// log.info("Bodega:"+clcmcoDTO.getCodigoBodega());
    			 vecmar.setBodegaOrigen(clcmcoDTO.getCodigoBodega());
    		 }
			 
			
			
			 
	    	cldmco = cldmcoDAO.obtieneArticulos(empresa, codigo, rut, dv, clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), vecmar.getBodegaOrigen(), vecmar.getCodigoTipoVendedor(),codMovto, numDocumento);
	    	
	    	
			 //log.info("Entra 3.0:"+fch91.getHHMMSS());
	    	clcdia = clcdiaDAO.obtieneImpuesto(empresa, codigo, rut, dv, clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento());
	    	
	    	
			 //log.info("Entra 3.1:"+fch9.getHHMMSS());
	    	String xml="";
	    	 int codele=0;
	    	 if (codigo==33 || codigo==36 || codigo==39){
	    		 FormaXMLReenvioFacturas forma = new FormaXMLReenvioFacturas();
	    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
	    	
				 xml =forma.xml(vecmar, clcdia, cldmco, clmcli, codele,clcmcoDTO, acteco, tptempDTO, prdat);
			
	    	 }else if(codigo==34 || codigo==37){
	    		 FormarXMLBoleta forma = new FormarXMLBoleta();
	    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
	    		 xml =forma.xml(vecmar, clcdia, cldmco, clmcli, codele, tptempDTO, clcmcoDTO,prdat,impuestoSiiDa);
	    	 }
			 
			 
			 //log.info("Genero XML");
			 log.info("XML :" +xml);
			 WsClient ws = new WsClient();
			
			 String gen ="";

			 resp = ws.onlineGenerationReenvio(xml,String.valueOf(rut),String.valueOf(numDocumento), vecmar.getBodegaOrigen(),codigo,gen, tptempDTO.getRut());
		//	 log.info("Respuesta Paperless Codigo"+resp.getCodigo());
		//	 log.info("Respuesta Paperless Descripcion"+resp.getMensaje());
		
			 if (resp.getCodigo()==0){
				 //log.info("Estado Documento:" + resp.getCodigo());
				 //Correlativo del documento
				 String res = resp.getMensaje();
				 
				 correlativoCamtra++;
				 
			 }else{
				 if (codigo!=39){
					// camtraDAO.actualizaEstadoCamtra(empresa, codMovto, numDocumento, fechaMovto, correlativoCamtra, String.valueOf(resp.getCodigo()) );
					 EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
						String mail = "jcanquil@caserita.cl";
						String datos="Factura/Boleta:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto+" "+"Rut:"+clmcli.getRutCliente()+" "+"DV:"+clmcli.getDvCliente()+" "+"Bodega:"+clcmcoDTO.getCodigoBodega();
						datos = datos + "XML:"+resp.getMensaje();
						//mailC.envioMail("Nada",mail,datos);
				 }
			 }
			//Generar registro para leer correlativo desde el visual
			    if (resp.getCodigo()==0 ){
			    	//vecmarDAO.generaCorrelativoVisual(codMovto, fechaMovto, numDocumento, correlativo, timbre, String.valueOf(resp.getCodigo()));
			    	//vecmarDAO.generaCorrelativoVisual(empresa, codMovto, fechaMovto, numDocumento, correlativo, timbre, String.valueOf(resp.getCodigo()));
			    }
	    }
	  
	    
	   
	}
	catch(Exception e){
		EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
		String mail = "jcanquil@caserita.cl";
		String datos="Factura/Boleta:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto;
		//mailC.envioMail("Nada",mail,datos);
		e.printStackTrace();
	}
	//factory.closeConnection();
	//factory.getInstance().closeConnection();
	
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
			ImpcasiiDAO impuestoSii = (ImpcasiiDAO) factory.getImpcasiiDAO();
			//vecmarDTO = vecmarDAO.obtenerDatosVecmar(codMovto, fechaMovto, numDocumento);
			ClmcliDAO clmcliDAO = (ClmcliDAO) factory.getClmcliDAO();
			CamtraDAO camtraDAO = (CamtraDAO) factory.getCamtraDAO();
			PrdatcaDAO prdatDAO = (PrdatcaDAO) factory.getPrdatcaDAO();

			 ClmcliDTO clmcli = clmcliDAO.recuperaCliente(String.valueOf(rut), dv);
			 //log.info("Paso a rescatar cliente");
			 CamtraDTO cam = camtraDAO.obtenerDatosCamtraNC(empresa, codigo, fechaMovto, numDocumento);
			 log.info("DATOS CAMTRA");
			 log.info("DATOS CAMTRA:"+cam.getNumeroDocumento());
			 log.info("DATOS CAMTRA:"+cam.getNumeroBolfactura());
			 log.info("Numero NC CAMTRA:"+numDocumento);
			 VecmarDTO vecmar =null;
			 int numNC=0;
			 if (codMovto!=0){
				 numNC = cam.getNumeroDocumento();
				 vecmar = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, numNC, clmcli);
				 prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numNC);
				 
			 }else{
				 prdatcaDTO = prdatDAO.obtieneDatosDocumento(empresa, codMovto, fechaMovto, numDocumento);
			 }
			 
			 if (codMovto!=0){
				 numNC = cam.getNumeroDocumento();
				 vecmar = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, cam.getNumeroDocumento(), clmcli);
			 }
			
			//Instacia CLCMCO
			ClcmcoDAO clcmcoDAO = (ClcmcoDAO) factory.getClcmcoDAO();
			
			//Instacia CLDMCO
			CldmcoDAO cldmcoDAO = (CldmcoDAO) factory.getCldmcoDAO();
			//Instacia CLCDIA
			ClcdiaDAO clcdiaDAO = (ClcdiaDAO) factory.getClcdiaDAO();
			ActecoDAO actecoDAO = (ActecoDAO) factory.getActecoDAO();
			
			//Obtiene Impuestos Caserita/SII
			HashMap<Integer, Integer> impuestoSiiDa = impuestoSii.obtieneImpuestoSII("S");
			
			List acteco = actecoDAO.buscaActeco();
			//Tipo Documento electronico
			TptdeleDAO tptdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
			ConnodDAO connodDAO = (ConnodDAO) factory.getConnodDAO();
			ConnohDAO connohDAO = (ConnohDAO) factory.getConnohDAO();
			ConnoiDAO connoiDAO = (ConnoiDAO) factory.getConnoiDAO();
			List clcmco = clcmcoDAO.obtieneClcmco(empresa, codigo, rut, dv, fechaMovto, cam.getNumeroBolfactura());
			if ("E".equals(tipo)){
				tipo ="D";
			}else if("D".equals(tipo)){
				estadoDoc=tipo;
				tipo="C";
			}
			log.info("Empresa:"+empresa+"Tipo:"+tipo+"NumFC/BL:"+cam.getNumeroBolfactura()+"fechaMvto:"+fechaMovto);
			ConnohDTO connoh = connohDAO.buscaConnoh(empresa, tipo, cam.getNumeroBolfactura(), fechaMovto);
			log.info("Empresa:"+empresa+"CodDoc:"+connoh.getCodDocumento()+"Rut:"+rut+"DV:"+dv+"NumDoc:"+connoh.getNumeroDocumento()+"Bodega:"+connoh.getCodigoBodega());
			int fecha = clcmcoDAO.obtieneFechaFactura(empresa, connoh.getCodDocumento(), rut, dv, connoh.getNumeroDocumento(), connoh.getCodigoBodega());
			List connod = new ArrayList();
			List clcdia = new ArrayList();
		    Iterator iter = clcmco.iterator();
		    int correlativo=0;
		    String timbre="";
		    cl.paperless.respuesta5.Respuesta resp=null;
		    //log.info("Procesa Facturacion");
		    int correlativoCamtra=0;
		    if (codigo==35 ){
		    	 while (iter.hasNext()){
				    	ClcmcoDTO clcmcoDTO = new ClcmcoDTO();
				    	clcmcoDTO = (ClcmcoDTO) iter.next();
				    	//log.info("Hora Documento " + clcmcoDTO.getHoraMovimiento());
				    	connod = connodDAO.buscaConnod(empresa, tipo, cam.getNumeroBolfactura(), fechaMovto, codigo);
				    	if (codigo==35){
				    		clcdia = clcdiaDAO.obtieneImpuesto(empresa, codigo, rut, dv, clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento());
				    	}else if (codigo==40){
				    		clcdia = connoiDAO.buscaImpto(tipo, numDocumento, fechaMovto);
				    	}else if (codigo==41){
				    		clcdia = connoiDAO.buscaImpto(tipo, numDocumento, fechaMovto);
				    	}
				    	
				    	
				    	String xml="";
				    	 int codele=0;
				    	 if (codigo==35 ){
				    		 FormarXMLReenvioNotas forma = new FormarXMLReenvioNotas();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml(vecmar, clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, nota, acteco,clcmcoDTO, tptempDTO, prdatcaDTO);
				    	 }else if (codigo ==40){
				    		 FormaXMLNotasDebito forma = new FormaXMLNotasDebito();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml( clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, acteco, tptempDTO,impuestoSiiDa,nota,prdatcaDTO);
				    	 }
						 
						 //log.info("Genero XML");
						 log.info("XML :" +xml);
						 WsClient ws = new WsClient();
						 String gen="NotasCredito";
						 resp = ws.onlineGenerationReenvio(xml,String.valueOf(rut), String.valueOf(numDocumento), vecmar.getBodegaOrigen(),codigo, gen,tptempDTO.getRut());
						 if (resp.getCodigo()==0){ 
							 //log.info("Estado Documento:" + resp.getCodigo());
							 //Correlativo del documento
							 String res = resp.getMensaje();
							 //vecmarDAO.actualizaVecmar(empresa, codMovto, fechaMovto, numNC);
							 
							 //clcmcoDAO.actualizaClcmco(empresa, clcmcoDTO.getCodDocumento(), Integer.parseInt(clcmcoDTO.getRutCliente()), clcmcoDTO.getDvCliente(), clcmcoDTO.getFechaMovimiento(), clcmcoDTO.getHoraMovimiento(), Integer.parseInt(res));
							 //timbre = ws.onlineRecovery(codele, Integer.parseInt(res),tptempDTO.getRut());
							 String timbre2 ="";
							
							 log.info("Estado Nota Credito:"+estadoDoc);
							 if("D".equals(estadoDoc)){
									usuario="";
								}
							 camtraDAO.actualizaEstadoCamtraReenvio(empresa, codMovto,cam.getNumeroBolfactura(), fechaMovto, correlativoCamtra, "R" );
							 //connodDAO.actualizaConnod(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 //connohDAO.actualizaConnoh(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 //connoiDAO.actualizaConnoi(tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 correlativo=Integer.parseInt(res);
							 correlativoCamtra++;
							 
						 }
						 else{
							 if (codigo!=39){
								 //camtraDAO.actualizaEstadoCamtra(empresa, codMovto, numNC, fechaMovto, correlativoCamtra, String.valueOf(resp.getCodigo()) );
								 EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
									String mail = "jcanquil@caserita.cl";
									String datos="Nota Credito:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numNC+" "+"FechaMov:"+fechaMovto+" "+"Rut:"+clmcli.getRutCliente()+" "+"DV:"+clmcli.getDvCliente()+" "+"Bodega:"+connoh.getCodigoBodega();
									datos = datos +"XML:"+resp.getMensaje();
								//	mailC.envioMail("Nada",mail,datos);
							 }
						 }
						 
				    }
		    }
		    else if (codigo ==40 || codigo==41){
		    	
				    	
				    	connod = connodDAO.buscaConnod(empresa, tipo, numDocumento, fechaMovto, codigo);
				    	if (codigo==40 || codigo==41){
				    		clcdia = connoiDAO.buscaImpto(tipo, numDocumento, fechaMovto);
				    	}
				    	
				    	ClcmcoDTO clcmcoDTO = new ClcmcoDTO(); 
				    	clcmcoDTO.setValorNeto(connoh.getMontoNeto());
				    	clcmcoDTO.setTotalIva(connoh.getMontoIva());
				    	clcmcoDTO.setTotalDocumento(connoh.getMontoTotal());
				    	String xml="";
				    	 int codele=0;
				    	 if (codigo==35 ){
				    		 FormarXMLReenvioNotas forma = new FormarXMLReenvioNotas();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml(vecmar, clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, nota, acteco,clcmcoDTO, tptempDTO, prdatcaDTO);
				    	 }else if (codigo ==40 || codigo==41){
				    		 FormaXMLNotasDebito forma = new FormaXMLNotasDebito();
				    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
				    		 int codeleref = tptdeleDAO.buscaDocumentoElectronico(connoh.getCodDocumento());
							 xml =forma.xml( clcdia, connod, clmcli, codele, connoh, codele,codeleref, fecha, acteco, tptempDTO,impuestoSiiDa,nota,prdatcaDTO);
				    	 }
						 
						 //log.info("Genero XML");
						 //log.info("XML :" +xml);
						 WsClient ws = new WsClient();
						 String gen="Notas";
						 resp = ws.onlineGenerationReenvio(xml, String.valueOf(rut), String.valueOf(numDocumento), connoh.getCodigoBodega(), codigo, gen,tptempDTO.getRut());
						 if (resp.getCodigo()==0){
							 //log.info("Estado Documento:" + resp.getCodigo());
							 //Correlativo del documento
							 String res = resp.getMensaje();
							 //vecmarDAO.actualizaVecmar(empresa, codMovto, fechaMovto, numNC);
							 
							
							 timbre = ws.onlineRecovery(codele, Integer.parseInt(res),tptempDTO.getRut());
							 String timbre2 ="";
							 //log.info("Numero Documento" + numDocumento);
							 //log.info("Numero Documento Paperless" + res);
							 log.info("Estado Nota Credito:"+estadoDoc);
							 if("D".equals(estadoDoc)){
									usuario="";
								}
							 
							 //camtraDAO.actualizaCamtra(empresa, codMovto, numNC, fechaMovto, correlativoCamtra, Integer.parseInt(res), usuario, timbre2,String.valueOf(resp.getCodigo()) );
							 //connodDAO.actualizaConnod(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 //connohDAO.actualizaConnoh(empresa, tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 //connoiDAO.actualizaConnoi(tipo, numDocumento, fechaMovto, Integer.parseInt(res));
							 correlativo=Integer.parseInt(res);
							 correlativoCamtra++;
							 
						 }else{
							 if (codigo!=39){
								 //camtraDAO.actualizaEstadoCamtra(empresa, codMovto, numNC, fechaMovto, correlativoCamtra, String.valueOf(resp.getCodigo()) );
								 //Enviar Mail de error
								EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
								String mail = "jcanquil@caserita.cl";
								String datos="Nota Credito:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numNC+" "+"FechaMov:"+fechaMovto+" "+"Rut:"+clmcli.getRutCliente()+" "+"DV:"+clmcli.getDvCliente()+" "+"Bodega:"+connoh.getCodigoBodega();
								datos = datos + "XML:"+resp.getMensaje();
								//mailC.envioMail("Nada",mail,datos);
							 }
						 }
						 
				    
		    }
		   
		  //Generar registro para leer correlativo desde el visual
		    if (resp.getCodigo()==0 ){
		    	//vecmarDAO.generaCorrelativoVisual(empresa, codMovto, fechaMovto, numNC, correlativo, timbre,String.valueOf(resp.getCodigo()));
		    }
		    
		   
		}
		catch(Exception e){
			EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
			String mail = "jcanquil@caserita.cl";
			String datos="Nota Credito:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto;
			//datos = datos + "Error:"+e.toString();
			//mailC.envioMail("Nada",mail,datos);
			e.printStackTrace();
		}
		//factory.closeConnection();
		//factory.getInstance().closeConnection();
		return estado; 
	}
	
	public String procesaGuia(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario, String tipo){
		String estado="D";
		DAOFactory factory = DAOFactory.getInstance();
		VecmarDTO vecmarDTO=null;
		try{
			
			//Tipo Documento electronico
			VecmarDAO vecmarDAO = (VecmarDAO) factory.getVecmarDAO();
			
			TptdeleDAO tptdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
			TptbdgDAO tptbdgDAO = (TptbdgDAO) factory.getTptbdgDAO();
			TptempDAO tptempDAO = (TptempDAO) factory.getTptempDAO();			
			TptempDTO tptempDTO = tptempDAO.recuperaEmpresa(empresa);
			ExmtraDAO exmtraDAO =null;
			ExdtraDAO exdtraDAO=null;
			ExmtraDTO emtraDTO =null;
			VedmarDAO vedmarDAO =null;
			PrdatcaDAO prdatDAO=null;
			PrmprvDAO prmprvDAO =null;

			TptbdgDTO tptbdg = null;
			PrmprvDTO prmprvDTO =null;
			 PrdatcaDTO prdatcaDTO=null;

			 ClmcliDTO clmcli=null;
			 ClmcliDAO clmcliDAO=null;
			List ved =null;
			/*if ("T".equals(tipo)){
				exmtraDAO = (ExmtraDAO) factory.getExmtraDAO();
				exdtraDAO = (ExdtraDAO) factory.getExdtraDAO();
				
				emtraDTO = exmtraDAO.recuperaEncabezadoFE(empresa, numDocumento);
			}else if ("M".equals(tipo) || "G".equals(tipo)){
				clmcliDAO = (ClmcliDAO) factory.getClmcliDAO();
				clmcli = clmcliDAO.recuperaCliente(String.valueOf(rut), dv);
				vedmarDAO = (VedmarDAO) factory.getVedmarDAO();
			}*/
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
		    cl.paperless.respuesta5.Respuesta resp=null;
		    //log.info("Procesa Facturacion");
		    int correlativoCamtra=0;
		    log.info("Tipo de Generacion :"+tipo);
		    vecmarDTO = vecmarDAO.obtenerDatosVecmar(empresa, codMovto, fechaMovto, numDocumento, clmcli);
		    	
		    if ("T".equals(tipo)){
		    	exdtra = exdtraDAO.recuperaDetalle(empresa,numDocumento);
		    	tptbdg = tptbdgDAO.buscaBodega(emtraDTO.getBodegaDestino());
		    }else if ("M".equals(tipo) || "G".equals(tipo)){
				ved = vedmarDAO.obtenerDatosVedmarGuia(empresa, codMovto, fechaMovto, numDocumento);
				
			}
		    
		    			    	
		    	String xml="";
		    	 int codele=0;
		    	 if (codigo==38 && "T".equals(tipo) ){
		    		 FormarXMLReenvioGuia forma = new FormarXMLReenvioGuia();
		    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
		    		 int codeleref = 0;
					 xml =forma.xml(emtraDTO, exdtra, codele,codeleref,tptbdg, acteco,tptempDTO);
					 log.info("XML Guia:"+xml);
		    	 }else if (codigo==38 ){
		    		 if ("M".equals(tipo) || "G".equals(tipo)){
		    		 FormarXMLReenvioGuiaMermas forma = new FormarXMLReenvioGuiaMermas();
		    		 codele = tptdeleDAO.buscaDocumentoElectronico(codigo);
		    		 emtraDTO = new ExmtraDTO();
		    		 emtraDTO.setBodegaOrigen(vecmarDTO.getBodegaOrigen());
		    		 int codeleref = 0;
		    		 xml =forma.xml(vecmarDTO, ved,prmprvDTO, codele,codeleref, acteco, tptempDTO);
		    		 log.info("XML Guia Merma:"+xml);
		    		 }
		    	 
		    	 }
				 
				 //log.info("Genero XML");
				 //log.info("XML :" +xml);
				 WsClient ws = new WsClient();
				 String gen="Guias";
				 resp = ws.onlineGenerationReenvio(xml, String.valueOf(rut), String.valueOf(numDocumento), 1, codigo, gen,tptempDTO.getRut());
				 if (resp.getCodigo()==0){
					 //log.info("Estado Documento:" + resp.getCodigo());
					 //Correlativo del documento
					 String res = resp.getMensaje();
					
					 
					 //vecmarDAO.actualizaVecmarGuias(empresa, codMovto, fechaMovto, numDocumento, Integer.parseInt(res));
					 //BdgcorrDAO bdgcorr = (BdgcorrDAO) factory.getBdgCorrDAO();
					 //NotCorreDAO notCorre = (NotCorreDAO) factory.getNotCorreDAO();
					 //int numeroAtencion=bdgcorr.recupeNumAtencion(empresa, emtraDTO.getBodegaOrigen());
					 //notCorre.generaNumeroDocAtencion(empresa, numDocumento, emtraDTO.getBodegaOrigen(), numeroAtencion);
					 if (codigo==38 && "T".equals(tipo)){
						// exmtraDAO.actualizaExmtra(empresa, emtraDTO.getNumTraspaso(), Integer.parseInt(res));
					 }
					 
					 //timbre = ws.onlineRecovery(codele, Integer.parseInt(res),tptempDTO.getRut());
					 //timbre ="";
					 //log.info("Numero Documento" + numDocumento);
					 //log.info("Numero Documento Paperless" + res);
					
					 //correlativo=Integer.parseInt(res);
					 correlativoCamtra++;
					 
				 }
				 
		   
		  //Generar registro para leer correlativo desde el visual
		    if (resp.getCodigo()==0 ){
		    	//vecmarDAO.generaCorrelativoVisual(empresa, codMovto, fechaMovto, numDocumento, correlativo, timbre,String.valueOf(resp.getCodigo()));
		    }
		    
		   
		}
		catch(Exception e){
			EnvioMailErrorFacturacion mailC = new EnvioMailErrorFacturacion();
			String mail = "jcanquil@caserita.cl";
			String datos="Guias:"+"Empresa:"+empresa +" "+"codMovto:"+codMovto+" " +"NumDocumento:"+numDocumento+" "+"FechaMov:"+fechaMovto;
			//mailC.envioMail("Nada",mail,datos);
			e.printStackTrace();
		}
		//factory.closeConnection();
		//factory.getInstance().closeConnection();
		return estado; 
	}
	
}
