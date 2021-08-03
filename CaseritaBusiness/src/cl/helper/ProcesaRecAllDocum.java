package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.comunes.properties.Constants;

public class ProcesaRecAllDocum {
	private  static Logger logi = Logger.getLogger(ProcesaRecAllDocum.class);

	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public static void main (String[]args){
	prop = new Properties();
	try{
		//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
		prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
	}
	catch(Exception e){
		e.printStackTrace();
	}
	pathProperties = Constants.FILE_PROPERTIES;
	
	
	//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=AMS&tipo=N
	//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=RTAPIA&tipo=T
	archivoLog=prop.getProperty("urlRecAllDocumento");
	logi.info("Servlet:"+archivoLog);
	StringBuffer tmp = new StringBuffer(); 
    String texto = new String(); 
	try { 
        // Crea la URL con del sitio introducido, ej: http://google.com 
        URL url = new URL(archivoLog); 
 
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
    
	}
}
