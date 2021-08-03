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

public class ReenvioFacturacionServlet {

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
		prop = new Properties();
		try{
			//log.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		
		
		//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=AMS&tipo=N
		//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=RTAPIA&tipo=T
		archivoLog=prop.getProperty("urlServletReenvio")+"?empresa="+empresa+"&codTipo="+codigo+"&fch="+fecha+"&num="+numero+"&cod="+codigoDocumento+"&rut="+rut+"&dv="+dv+"&usuario="+usuario+"&tipo="+tipo+"&nota="+nota;
		log.info("Servlet:"+archivoLog);
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
