package cl.caserita.company.user.wsclient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

//import com.sun.jmx.remote.internal.Unmarshal;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dte.DTEDefType;
import cl.caserita.dte.ObjectFactory;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.procesaXML.LeerXml;
import cl.caserita.ws.OnlineStub;
import cl.caserita.ws.OnlineStub.AnulaGuia;
import cl.caserita.ws.OnlineStub.GestionRec;
import cl.caserita.ws.OnlineStub.GestionRecResponse;
import cl.caserita.ws.OnlineStub.OnlineGeneration;
import cl.caserita.ws.OnlineStub.OnlineRecovery;
import cl.caserita.ws.OnlineStub.OnlineRecoveryRec;
import cl.caserita.ws.OnlineStub.OnlineRecoveryRecList;
import cl.caserita.ws.paperless.OnlineStub.AprobRechLeyMasivo;
import cl.caserita.ws.paperless.OnlineStub.AprobRechLeyMasivoResponse;
import cl.caserita.ws.paperless.OnlineStub.AprobacionRechazoMasivo;
import cl.caserita.ws.paperless.OnlineStub.AprobacionRechazoMasivoResponse;
import cl.sii.envioDTE.EnvioDTE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class WsClient {
	private static Logger log = Logger.getLogger(WsClient.class); 

	private static FileWriter fileWriterlogi;
	private static String archivologi;
	private static Properties prop=null;
	private static String pathProperties;
	
	public cl.paperless.respuesta5.Respuesta onlineGeneration(String xml, String rut, String numDoc, int bodega, int codDocumento, String generacion, int rutEmpresa,String endPointWS) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		
		
		int connectionTimeOutInMs = 5000;
		// log.info("Entra 4.1:"+fch3.getHHMMSS());
		//OnlineStub online = new OnlineStub(endPoint);
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineGeneration generation = new cl.caserita.ws.paperless.OnlineStub.OnlineGeneration();
		
		generation.setArgs0(rutEmpresa);
		if (rutEmpresa==76288567){
			generation.setArgs1("adm_comercial");
			generation.setArgs2("abc123");
		}else{
			generation.setArgs1("adm_caserita");
			generation.setArgs2("abc123");
		}
		
		generation.setArgs3(xml);
		generation.setArgs4(1);
		generation.setArgs5(6);
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		res.setCodigo(7);
		try{
			
			cl.caserita.ws.paperless.OnlineStub.OnlineGenerationResponse responseStub = onlineStub.onlineGeneration(generation);
			
		try
		{
//			final JAXBContext jc = JAXBContext.newInstance(cl.paperless.respuesta5.Respuesta.class);
//			Unmarshaller u = jc.createUnmarshaller();
//			
//			StringReader str = new StringReader(response.get_return().toString());
			
//			log.info("Envia Factura");
//			res = (cl.paperless.respuesta5.Respuesta) u.unmarshal(str);
//			respuesta = res.getMensaje();
		//	log.info("Respuesta:"+response.get_return().toString());
			
			res = respuesta(responseStub.get_return().toString());
			//res= respuesta(response.get_return().toString());
			generatxt(xml,responseStub.get_return().toString(), rut, numDoc,bodega,codDocumento,generacion);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
		//log.info("Respuesta :" +response.get_return().toString());
		}catch(Exception r){
			r.printStackTrace();
		}
		
		return res;
		
	}
	
	public cl.paperless.respuesta5.Respuesta onlineGenerationReenvio(String xml, String rut, String numDoc, int bodega, int codDocumento, String generacion, int rutEmpresa) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		
		

		/*OnlineStub online = new OnlineStub(endPoint);
		

		OnlineGeneration gene = new OnlineGeneration();
		gene.setParam0(rutEmpresa);
		gene.setParam1("gen_paperles");
		gene.setParam2("abc123");
		//xml=prop.getProperty("xml");
		gene.setParam3(xml);
		gene.setParam4(2);
		gene.setParam5(5);
		
		
		online._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		OnlineStub.OnlineGenerationResponse response = online.onlineGeneration(gene);*/
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineGeneration generation = new cl.caserita.ws.paperless.OnlineStub.OnlineGeneration();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(xml);
		generation.setArgs4(2);
		generation.setArgs5(5);
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		
		cl.caserita.ws.paperless.OnlineStub.OnlineGenerationResponse response = onlineStub.onlineGeneration(generation);
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try
		{
//			final JAXBContext jc = JAXBContext.newInstance(cl.paperless.respuesta5.Respuesta.class);
//			Unmarshaller u = jc.createUnmarshaller();
//			
//			StringReader str = new StringReader(response.get_return().toString());
			
//			log.info("Envia Factura");
//			res = (cl.paperless.respuesta5.Respuesta) u.unmarshal(str);
//			respuesta = res.getMensaje();
		//	log.info("Respuesta:"+response.get_return().toString());
			res= respuesta(response.get_return().toString());
			generatxt(xml,response.get_return().toString(), rut, numDoc,bodega,codDocumento,generacion);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
		//log.info("Respuesta :" +response.get_return().toString());
		
		return res;
		
	}
	public String onlineRecovery(int codele, int numdoc, int rutEmpresa) throws RemoteException{
		String respuesta="";
		String keystoreLocation = "/QIBM/ProdData/Java400/jdk15/lib/security/cacerts"; 
		System.setProperty ("javax.net.ssl.trustStore", keystoreLocation); 
		System.setProperty ("javax.net.ssl.KeyStore", keystoreLocation); 
		System.setProperty ("javax.net.ssl.trustStorePassword", "MY_PASS"); 
		System.setProperty ("javax.net.ssl.keyStorePassword", "MY_PASS"); 
		System.setProperty ("java.protocol.handler.pkgs", "com.ibm.net.ssl.internal.www.protocol"); 
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		//String empresa=prop.getProperty("empresa");
	
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecovery generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecovery();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(codele);
		generation.setArgs4(numdoc);
		generation.setArgs5(3);
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
				
		
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryResponse response2 = onlineStub.onlineRecovery(generation);
		
		cl.caserita.respuestaLarga.Respuesta res = new cl.caserita.respuestaLarga.Respuesta();
		
		try{
			
			
			
			String prueba = response2.get_return().toString().substring(86, response2.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			
			respuesta = prueba;
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return respuesta;
		
	}
	
	public String onlineRecoveryUrl(int codele, int numdoc, int rutEmpresa) throws RemoteException{
		String respuesta="";
		String keystoreLocation = "/QIBM/ProdData/Java400/jdk15/lib/security/cacerts"; 
		System.setProperty ("javax.net.ssl.trustStore", keystoreLocation); 
		System.setProperty ("javax.net.ssl.KeyStore", keystoreLocation); 
		System.setProperty ("javax.net.ssl.trustStorePassword", "MY_PASS"); 
		System.setProperty ("javax.net.ssl.keyStorePassword", "MY_PASS"); 
		System.setProperty ("java.protocol.handler.pkgs", "com.ibm.net.ssl.internal.www.protocol"); 
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		//String empresa=prop.getProperty("empresa");
	
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecovery generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecovery();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(codele);
		generation.setArgs4(numdoc);
		generation.setArgs5(2);
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
				
		
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryResponse response2 = onlineStub.onlineRecovery(generation);
		
		try{
			
			
			
			String prueba = response2.get_return().toString().substring(86, response2.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			
			respuesta = prueba;
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return respuesta;
		
	}
	
	public String onlineRecoveryRec(int rutEmpresa, int codDoc, int numDoc, int empresa , String endPointServicio) throws RemoteException{
		String respuesta="";
		String endPoint=null;
		if (endPointServicio==null || endPointServicio==""){
			prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			pathProperties = Constants.FILE_PROPERTIES;
			 endPoint=prop.getProperty("endpoint").trim();
		}else{
			endPoint=endPointServicio.trim();
		}
		//log.info("EndPoint :" + endPoint.trim());
		
		/*OnlineStub online = new OnlineStub(endPoint);
		//int numero = Integer.parseInt(numDoc);
				
		OnlineRecoveryRec onlineRecovery = new OnlineRecoveryRec();
		
		
		onlineRecovery.setParam0(empresa);
		onlineRecovery.setParam1("gen_paperless");
		onlineRecovery.setParam2("abc123");
		onlineRecovery.setParam3(rutEmpresa);
		onlineRecovery.setParam4(codDoc);
		onlineRecovery.setParam5(numDoc);
		onlineRecovery.setParam6(1);
		
		cl.sii.envioDTE.EnvioDTE res = new cl.sii.envioDTE.EnvioDTE();
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);*/
		log.info("EndPoint:"+endPoint);
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRec generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRec();
		
		if (empresa==2){
			empresa=76288567;
		}else if (empresa==1){
			empresa=96509850;
		}
		log.info("Empresa:"+empresa);
		generation.setArgs0(empresa);
		if (empresa==76288567){
			generation.setArgs1("adm_comercial");
			generation.setArgs2("abc123");
		}else if (empresa==96509850){
			generation.setArgs1("adm_caserita");
			generation.setArgs2("abc123");
		}
		generation.setArgs3(rutEmpresa);
		generation.setArgs4(codDoc);
		generation.setArgs5(numDoc);
		generation.setArgs6(1);
		
		log.info("Empresa:"+generation.getArgs0());
		log.info("Empresa:"+generation.getArgs1());
		log.info("Empresa:"+generation.getArgs2());
		log.info("Empresa:"+generation.getArgs3());
		log.info("Empresa:"+generation.getArgs4());
		log.info("Empresa:"+generation.getArgs5());
		log.info("Empresa:"+generation.getArgs6());
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		
		
		cl.paperless.respuesta5.Respuesta resp = new cl.paperless.respuesta5.Respuesta();
		
		try{
			
			//cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecResponse response2 = onlineStub.onlineRecoveryRec(generation);
			//OnlineStub.OnlineRecoveryRecResponse response2 = onlineStub.onlineRecoveryRec(generation);
			cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecResponse response2 = onlineStub.onlineRecoveryRec(generation);
			//log.info("Respuesta "+ response2.get_return());
			
			prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			String prueba = response2.get_return().toString().substring(86, response2.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			
			respuesta = prueba;
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return respuesta;
		
	}
	
	public String onlineRecoveryRecUrl(int rutEmpresa, int codDoc, int numDoc, int empresa , String endPointServicio) throws RemoteException{
		String respuesta="";
		String endPoint=null;
		if (endPointServicio==null || endPointServicio==""){
			prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			pathProperties = Constants.FILE_PROPERTIES;
			 endPoint=prop.getProperty("endpoint");
		}
		else{
			endPoint=endPointServicio;
		}
		
		/*OnlineStub online = new OnlineStub(endPoint);
		//int numero = Integer.parseInt(numDoc);
				
		OnlineRecoveryRec onlineRecovery = new OnlineRecoveryRec();
		onlineRecovery.setParam0(empresa);
		onlineRecovery.setParam1("gen_paperless");
		onlineRecovery.setParam2("abc123");
		onlineRecovery.setParam3(rutEmpresa);
		onlineRecovery.setParam4(codDoc);
		onlineRecovery.setParam5(numDoc);
		onlineRecovery.setParam6(2);
		
		cl.sii.envioDTE.EnvioDTE res = new cl.sii.envioDTE.EnvioDTE();
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);*/
		log.info("EndPoint:"+endPoint);
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRec generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRec();
		if (empresa==2){
			empresa=76288567;
		}else if (empresa==1){
			empresa=96509850;
		}
		generation.setArgs0(empresa);
		if (empresa==76288567){
			generation.setArgs1("adm_comercial");
			generation.setArgs2("abc123");
		}else if (empresa==96509850){
			generation.setArgs1("adm_caserita");
			generation.setArgs2("abc123");
		}
		generation.setArgs3(rutEmpresa);
		generation.setArgs4(codDoc);
		generation.setArgs5(numDoc);
		generation.setArgs6(2);
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		
		
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try{
			//OnlineStub.OnlineRecoveryRecResponse response2 = online.onlineRecoveryRec(onlineRecovery);
			cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecResponse response2 = onlineStub.onlineRecoveryRec(generation);
			//log.info("Respuesta "+ response2.get_return());
			
			prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			String prueba = response2.get_return().toString().substring(86, response2.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			
			respuesta = prueba;
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return respuesta;
		
	}
	
	public String onlineRecoveryTipoUrl(int codele, int numdoc, int rutEmpresa, int tipo) throws RemoteException{
		String respuesta="";
		String keystoreLocation = "/QIBM/ProdData/Java400/jdk15/lib/security/cacerts"; 
		System.setProperty ("javax.net.ssl.trustStore", keystoreLocation); 
		System.setProperty ("javax.net.ssl.KeyStore", keystoreLocation); 
		System.setProperty ("javax.net.ssl.trustStorePassword", "MY_PASS"); 
		System.setProperty ("javax.net.ssl.keyStorePassword", "MY_PASS"); 
		System.setProperty ("java.protocol.handler.pkgs", "com.ibm.net.ssl.internal.www.protocol"); 
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		//String empresa=prop.getProperty("empresa");

		
		cl.caserita.respuestaLarga.Respuesta res = new cl.caserita.respuestaLarga.Respuesta();
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecovery generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecovery();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(codele);
		generation.setArgs4(numdoc);
		generation.setArgs5(tipo);
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
				
		
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryResponse response2 = onlineStub.onlineRecovery(generation);
		
		try{
			
			
			
			String prueba = response2.get_return().toString().substring(86, response2.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			
			respuesta = prueba;
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return respuesta;
		
	}
	public String onlineRecoveryRecList() throws RemoteException{
		String respuesta="";
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		String empresa=prop.getProperty("empresa");
		/*OnlineStub online = new OnlineStub(endPoint);
		
				
		OnlineRecoveryRecList onlineRecovery = new OnlineRecoveryRecList();
		onlineRecovery.setParam0(1);
		onlineRecovery.setParam1("");
		onlineRecovery.setParam2("");
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);*/
		
		
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecList generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecList();
		
		generation.setArgs0(1);
		generation.setArgs1("");
		generation.setArgs2("");
		
	
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
				
		
		
		
		try{
			cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecListResponse response2 = onlineStub.onlineRecoveryRecList(generation);
			//log.info("Respuesta "+ response2.get_return());
			
	       
	        //log.info("Respuesta" + response2.toString());
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return respuesta;
		
	}
	public String onlineGestionRec(int rutEmpresa, String endPointServicio) throws RemoteException{
		String desc ="";
		String endPoint=null;
		if (endPointServicio==null || endPointServicio==""){
			prop = new Properties();
			try{
				//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
				prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			pathProperties = Constants.FILE_PROPERTIES;
		    endPoint=prop.getProperty("endpoint");
			String empresa=prop.getProperty("empresa");
		}
		else{
			 endPoint=endPointServicio;
		}
	/*	OnlineStub onlinea = new OnlineStub(endPoint);
		
		GestionRec gestion = new GestionRec();
		
		gestion.setParam0(rutEmpresa);
		gestion.setParam1("gen_paperless");
		gestion.setParam2("abc123");
		gestion.setParam3(0);
		gestion.setParam4(0);
		gestion.setParam5(0);
		gestion.setParam6("0");
		gestion.setParam7(0);*/
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.GestionRec generation = new cl.caserita.ws.paperless.OnlineStub.GestionRec();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(0);
		generation.setArgs4(0);
		generation.setArgs5(0);
		generation.setArgs6("0");
		generation.setArgs7(0);
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(250000);
				
		
		
		
		
		
		try{
			cl.caserita.ws.paperless.OnlineStub.GestionRecResponse gestionRec  = onlineStub.gestionRec(generation);
			log.info("Respuesta ws"+gestionRec.get_return());
			String prueba = gestionRec.get_return().toString().substring(86, gestionRec.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			desc = prueba;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return desc;
		
	}
	
	public String onlineGestionRecApruebaRechaza(int rut, int codDoc,int folio, String acepta, int accion, int rutEmpresa) throws RemoteException{
		String desc ="";
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		
		/*OnlineStub onlinea = new OnlineStub(endPoint);
		
		
		
		GestionRec gestion = new GestionRec();
		
		gestion.setParam0(rutEmpresa);
		gestion.setParam1("gen_paperless");
		gestion.setParam2("abc123");
		gestion.setParam3(rut);
		gestion.setParam4(codDoc);
		gestion.setParam5(folio);
		gestion.setParam6(acepta);
		gestion.setParam7(accion);
		
		onlinea._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);*/
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.GestionRec generation = new cl.caserita.ws.paperless.OnlineStub.GestionRec();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(rut);
		generation.setArgs4(codDoc);
		generation.setArgs5(folio);
		generation.setArgs6(acepta);
		generation.setArgs7(accion);
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
				
		
		try{
			cl.caserita.ws.paperless.OnlineStub.GestionRecResponse gestionRec  = onlineStub.gestionRec(generation);
			String prueba = gestionRec.get_return().toString().substring(86, gestionRec.get_return().toString().length());
			prueba = prueba.replaceAll("</Mensaje>", "");
			prueba = prueba.replaceAll("</Respuesta>", "");
			desc = prueba;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return desc;
		
	}
	
	public void generatxt(String XML, String res, String rut, String numDoc, int bodega, int codDocumento, String generacion){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//log.info("Mes:"+mes);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);

		generacion= descripcionTD(codDocumento);
		String rutalogi="/home/ServiciosCaserita/log/";
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutalogi+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";

		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		log.info("Ruta:"+carpeta);
		archivologi=carpeta+codDocumento+"_"+rut+"_"+numDoc+"_"+fechaStr+".log";;
		File f=new File(archivologi);
		if (f.exists()){
			log.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterlogi=new FileWriter(archivologi,true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		log.info("Archivo log:"+archivologi);

		fileWriterlogi.write( XML+","+ res+"\n");
		fileWriterlogi.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	public static void main (String args[]){
		
		WsClient ws = new WsClient();
		String xml = "REEMFC1001012.xml";
		
		int tipo = 1;
		
		String generacion="NC";
		try{
			URL url=
				    new URL("http://50.50.1.240:8080/ServiciosCaserita/"+xml);
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
				String entrada;
				String cadena="";

				while ((entrada = br.readLine()) != null){
					cadena = cadena + entrada;
				}

				log.info("XML a Procesar:"+cadena);
				if (tipo==1){
					ws.onlineGeneration(cadena, "4444", "6666", 23, 33, generacion, 999,"");
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	public cl.paperless.respuesta5.Respuesta respuesta(String xml){
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		res.setCodigo(7);
		StringBuffer StringBuffer1 = new StringBuffer(xml);
		InputStream inputStream=null;
		String entrada;
		String cadena="";
		try{
			inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		
		
		//inputStream = new ByteArrayInputStream(StringBuffer1.toString().getBytes("UTF-8"));
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		while ((entrada = br.readLine()) != null){
			cadena = cadena + entrada;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		InputSource archivo = new InputSource();
		archivo.setCharacterStream(new StringReader(cadena)); 
		
		Document documento = db.parse(archivo);
		documento.getDocumentElement().normalize();
		
		//Recupera Datos IDOC
		NodeList nodeLista = documento.getElementsByTagName("Respuesta");
		

				for (int s = 0; s < nodeLista.getLength(); s++) {
			
				Node primerNodo = nodeLista.item(s);
				String codigo;
				String msj;
				String codDoc;
			
				if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
			
				Element primerElemento = (Element) primerNodo;
			
				NodeList primerNombreElementoLista =
			                        primerElemento.getElementsByTagName("Codigo");
				Element primerNombreElemento =
			                        (Element) primerNombreElementoLista.item(0);
				NodeList primerNombre = primerNombreElemento.getChildNodes();
				codigo = ((Node) primerNombre.item(0)).getNodeValue().toString();
				
				
				NodeList segundoNombreElementoLista =
			        primerElemento.getElementsByTagName("Mensaje");
			Element segundoNombreElemento =
			        (Element) segundoNombreElementoLista.item(0);
			NodeList segundoNombre = segundoNombreElemento.getChildNodes();
			msj = ((Node) segundoNombre.item(0)).getNodeValue().toString();
			
				
			res.setCodigo(Integer.parseInt(codigo));
			res.setMensaje(msj);
			
			
				}
			      }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		log.info("R E S P U E S T A :"+res.getCodigo());
		return res;
	}
	public cl.paperless.respuesta5.Respuesta reenvioOnlineGeneration(String xml, String rut, String numDoc, int bodega, int codDocumento, String generacion) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		String empresa=prop.getProperty("empresa");
		
		/*OnlineStub online = new OnlineStub(endPoint);
		
//		String keystoreLocation = "/qibm/ProdData/Java400/jdk15/lib/security/cacerts"; 
//		System.setProperty ("javax.net.ssl.trustStore", keystoreLocation); 
//		System.setProperty ("javax.net.ssl.KeyStore", keystoreLocation); 
//		
//		System.setProperty ("javax.net.ssl.trustStorePassword", "changeit"); 
//		System.setProperty ("javax.net.ssl.keyStorePassword", "changeit"); 
//		System.setProperty ("java.protocol.handler.pkgs", "com.ibm.net.ssl.internal.www.protocol"); 
		
		
		OnlineGeneration gene = new OnlineGeneration();
		gene.setParam0(Integer.parseInt(rut));
		gene.setParam1("gen_paperles");
		gene.setParam2("abc123");
		//xml=prop.getProperty("xml");
		gene.setParam3(xml);
		gene.setParam4(2);
		gene.setParam5(5);
		
		
		online._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		OnlineStub.OnlineGenerationResponse response = online.onlineGeneration(gene);*/
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineGeneration generation = new cl.caserita.ws.paperless.OnlineStub.OnlineGeneration();
		
		generation.setArgs0(Integer.parseInt(rut));
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(xml);
		generation.setArgs4(2);
		generation.setArgs5(5);
		
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		cl.caserita.ws.paperless.OnlineStub.OnlineGenerationResponse response = onlineStub.onlineGeneration(generation);
		// log.info("Entra 4.3:"+fch5.getHHMMSS());
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try
		{
//			final JAXBContext jc = JAXBContext.newInstance(cl.paperless.respuesta5.Respuesta.class);
//			Unmarshaller u = jc.createUnmarshaller();
//			
//			StringReader str = new StringReader(response.get_return().toString());
			
//			log.info("Envia Factura");
//			res = (cl.paperless.respuesta5.Respuesta) u.unmarshal(str);
//			respuesta = res.getMensaje();
		//	log.info("Respuesta:"+response.get_return().toString());
			res= respuesta(response.get_return().toString());
			generatxt(xml,response.get_return().toString(), rut, numDoc,bodega,codDocumento,generacion);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
		log.info("Respuesta Reenvio Documentos Electronicos :" +response.get_return().toString());
		
		return res;
		
	}
	
	public String recuperaEstadoSii(String rut, String numDoc, int codDocumento) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		String empresa=prop.getProperty("empresa");
	

		/*OnlineStub online = new OnlineStub(endPoint);
		

		
		log.info("Rut:"+rut);
		log.info("Codigo Doc."+codDocumento);
		log.info("numDoc:"+numDoc);
		
		OnlineRecovery gene = new OnlineRecovery();
		gene.setParam0(Integer.parseInt(rut));
		gene.setParam1("gen_paperles");
		gene.setParam2("abc123");
		//xml=prop.getProperty("xml");
		gene.setParam3(codDocumento);
		gene.setParam4(Integer.parseInt(numDoc));
		gene.setParam5(5);
		
		
		online._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		OnlineStub.OnlineRecoveryResponse response = online.onlineRecovery(gene);
	*/
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecovery generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecovery();
		
		generation.setArgs0(Integer.parseInt(rut));
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(codDocumento);
		generation.setArgs4(Integer.parseInt(numDoc));
		generation.setArgs5(5);
		
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryResponse response = onlineStub.onlineRecovery(generation);
		
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try
		{
			//final JAXBContext jc = JAXBContext.newInstance(cl.paperless.respuesta5.Respuesta.class);
			//Unmarshaller u = jc.createUnmarshaller();
			
			//StringReader str = new StringReader(response.get_return().toString());
			
		//log.info("Envia Factura");
			//res = (cl.paperless.respuesta5.Respuesta) u.unmarshal(str);
			respuesta = res.getMensaje();
		log.info("Respuesta:"+response.get_return().toString());
			res= respuesta(response.get_return().toString());
			//generatxt(xml,response.get_return().toString(), rut, numDoc,bodega,codDocumento,generacion);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
		log.info("Respuesta Reenvio Documentos Electronicos :" +response.get_return().toString());
		
		return response.get_return().toString();
		
	}
	
	public cl.paperless.respuesta5.Respuesta anulaGuia(String rutEmpresa, String numDoc) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		

		/*OnlineStub online = new OnlineStub(endPoint);
		
	

		AnulaGuia anula = new AnulaGuia();
		anula.setParam0(Integer.parseInt(rutEmpresa));
		anula.setParam1("gen_paperles");
		anula.setParam2("abc123");
		//xml=prop.getProperty("xml");
		anula.setParam3(52);
		anula.setParam4(Integer.parseInt(numDoc));
		
		
		
		online._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		OnlineStub.AnulaGuiaResponse response = online.anulaGuia(anula);*/
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.AnulaGuia generation = new cl.caserita.ws.paperless.OnlineStub.AnulaGuia();
		
		generation.setArgs0(Integer.parseInt(rutEmpresa));
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(52);
		generation.setArgs4(Integer.parseInt(numDoc));
		
		
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		cl.caserita.ws.paperless.OnlineStub.AnulaGuiaResponse response = onlineStub.anulaGuia(generation);
		
		
		
		// log.info("Entra 4.3:"+fch5.getHHMMSS());
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try
		{
//			final JAXBContext jc = JAXBContext.newInstance(cl.paperless.respuesta5.Respuesta.class);
//			Unmarshaller u = jc.createUnmarshaller();
//			
//			StringReader str = new StringReader(response.get_return().toString());
			
//			log.info("Envia Factura");
//			res = (cl.paperless.respuesta5.Respuesta) u.unmarshal(str);
//			respuesta = res.getMensaje();
		//	log.info("Respuesta:"+response.get_return().toString());
			res= respuesta(response.get_return().toString());
			log.info("Respuesta anula Guia:"+response.get_return().toString());
			//generatxt(xml,response.get_return().toString(), rut, numDoc,bodega,codDocumento,generacion);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
		//log.info("Respuesta :" +response.get_return().toString());
		
		return res;
		
	}
	
	public cl.paperless.respuesta5.Respuesta procesaAprobacionRechazo(String rutEmpresa, String numDoc, String doc) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		log.info("EndPoint"+endPoint);

		//OnlineStub online = new OnlineStub(endPoint);
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		AprobRechLeyMasivo generation = new AprobRechLeyMasivo();

	
		generation.setArgs0(Integer.parseInt(rutEmpresa));
		generation.setArgs1("jcanquil");
		generation.setArgs2("desarrollo#");
		generation.setArgs3(doc);
		log.info("PRocesa DOC Aprobacion:"+rutEmpresa);
		log.info("PRocesa DOC Aprobacion:"+doc);
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		AprobRechLeyMasivoResponse response = onlineStub.aprobRechLeyMasivo(generation);
		
		// log.info("Entra 4.3:"+fch5.getHHMMSS());
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try
		{
//			final JAXBContext jc = JAXBContext.newInstance(cl.paperless.respuesta5.Respuesta.class);
//			Unmarshaller u = jc.createUnmarshaller();
//			
//			StringReader str = new StringReader(response.get_return().toString());
			
//			log.info("Envia Factura");
//			res = (cl.paperless.respuesta5.Respuesta) u.unmarshal(str);
//			respuesta = res.getMensaje();
		//	log.info("Respuesta:"+response.get_return().toString());
			//res= respuesta(response.get_return().toString());
			log.info("Respuesta anula Guia:"+response.get_return().toString());
			//generatxt(xml,response.get_return().toString(), rut, numDoc,bodega,codDocumento,generacion);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
		//log.info("Respuesta :" +response.get_return().toString());
		
		return res;
		
	}
	
	public String onlineRecoveryRecList( int rutEmpresa,String endPointWS, String fecha) throws RemoteException{
		String respuesta="";
	//	log.info("Entro al onlineGeneration");
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		//String endPoint=endPointWS;
		
		int connectionTimeOutInMs = 5000;
		// log.info("Entra 4.1:"+fch3.getHHMMSS());
	/*	OnlineStub online = new OnlineStub(endPoint);
		
		OnlineRecoveryRecList gene = new OnlineRecoveryRecList();
		
		gene.setParam0(rutEmpresa);
		gene.setParam1("gen_paperles");
		gene.setParam2("abc123");
		
		gene.setParam3(fecha);

		online._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		online._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);*/
		
		cl.caserita.ws.paperless.OnlineStub onlineStub = new cl.caserita.ws.paperless.OnlineStub(endPoint);
		cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecList generation = new cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecList();
		
		generation.setArgs0(rutEmpresa);
		generation.setArgs1("adm_comercial");
		generation.setArgs2("abc123");
		generation.setArgs3(fecha);
		
		
		
		
		onlineStub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
		onlineStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(10000);
		
		
		cl.paperless.respuesta5.Respuesta res = new cl.paperless.respuesta5.Respuesta();
		try{
			cl.caserita.ws.paperless.OnlineStub.OnlineRecoveryRecListResponse response = onlineStub.onlineRecoveryRecList(generation);
			
		
			
		try
		{

			res= respuesta(response.get_return().toString());
			respuesta = res.getMensaje();
			log.info("Respuesta Por Fecha:"+respuesta);
	
		}
		catch(Exception e){
			e.printStackTrace();
		}
      
		}catch(Exception r){
			r.printStackTrace();
		}
		
		return respuesta;
		
	}
	
	public String descripcionTD(int codigo){
		/*33=Factura
		34=Boleta
		35=NotaCredito
		36=FacturaExenta
		37=BoletaExenta
		38=Guia
		39=FacturaVarias
		40=NotaDebito
		41=NotaFactVarias*/
		String desc="";
		if (codigo==33){
			desc="Factura";
		}else if (codigo==34){
			desc="Boleta";
		}else if (codigo==35){
			desc="NotaCredito";
		}else if (codigo==36){
			desc="FacturaExenta";
		}else if (codigo==37){
			desc="BoletaExenta";
		}else if (codigo==38){
			desc="Guia";
		}else if (codigo==39){
			desc="FacturaVarias";
		}
		else if (codigo==40){
			desc="NotaDebito";
		}		
		else if (codigo==41){
			desc="NotaFactVarias";
		}
		
		
		return desc;
	}
	
	
	
	
}
