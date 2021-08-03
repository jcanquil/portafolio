package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.properties.Constants;

public class procesaFacturaProveedorHelper {
	private  static Logger logi = Logger.getLogger(procesaFacturaProveedorHelper.class);

	private static Properties prop=null;
	private static String pathProperties;
	private static String procesaServlet="";
	private static String archivoLog;
	public void apruebaRechazaDoc(int rut, int codDoc, int folio,String acepta, int accion, int empresa){
		
		WsClient ws = new WsClient();
		prop = new Properties();
		try{
			//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		logi.info("PROCESA APROBACION");
		pathProperties = Constants.FILE_PROPERTIES;
		archivoLog=prop.getProperty("urlServletAPPDoc");
		
		try{
			//ws.onlineGestionRecApruebaRechaza(rut, codDoc, folio, acepta, accion, empresa);
			//Procesa Aprobacion
			procesaServlet =archivoLog+"?e="+empresa+"&ee="+rut+"&t="+codDoc+"&f="+folio+"&ac=1&mr=ACEPTADA";
			
			logi.info("Procesa Aprobacion Documentos Servlet:"+procesaServlet);
			
			StringBuffer tmp = new StringBuffer(); 
	        String texto = new String(); 
			try { 
	            // Crea la URL con del sitio introducido, ej: http://google.com 
	            URL url = new URL(procesaServlet); 
	     
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
	        logi.info("DOCUMENTO APROBADO");
		
		}catch(Exception e){
			e.printStackTrace();
		}
			
		
		
		
	}
}
