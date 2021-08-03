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

public class ProcesaRecepcionDia {
	private  static Logger logi = Logger.getLogger(ProcesaRecepcionDia.class);

	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public static void main (String[]args){
		

		String fecha="";
		int empresa=0;
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Codigo Movimiento
				fecha=args[i].trim();
			}else if(i==1){
				//Fecha Movimiento
				empresa=Integer.parseInt(args[i]);
			}
		}		
		prop = new Properties();
		try{
			//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		
		
		
		archivoLog=prop.getProperty("urlServletRecDia")+"?fecha="+fecha+"&rutEmpresa="+empresa;
		
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
