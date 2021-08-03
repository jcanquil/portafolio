package cl.caserita.informes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import cl.caserita.comunes.properties.Constants;

public class ProcesaInformeGasto {
	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	public static void main (String []args){
		
		String fecha="";
		String hora="";
		String mail="";
		String periodoInicial="";
		String periodoFinal="";
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//fechaGeneracion
				fecha=args[i];
			}else if(i==1){
				//Hora generacion
				hora=args[i];
			}else if(i==2){
				//Mail
				mail=args[i];
			}else if(i==3){
				//Periodo Inicial
				periodoInicial=args[i];
			}else if(i==4){
				//Periodo Final
				periodoFinal=args[i];
			}
		}		
		
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		
		
		
		archivoLog=prop.getProperty("urlServletInfGasto")+"?fecha="+fecha+"&hora="+hora+"&mail="+mail+"&periodoIni="+periodoInicial+"&periodoFin="+periodoFinal;
		
		System.out.println("Servlet:"+archivoLog);
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
