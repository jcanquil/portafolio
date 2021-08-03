package cl.caserita.batch.recepcion;

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
import cl.caserita.helper.ProcesaFacturacion;
import cl.caserita.helper.procesaRecepcionHelper;

public class procesaRecepcion {
	private static Logger log = Logger.getLogger(ProcesaFacturacion.class); 
	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public static void main (String[]args){
		int codDoc=0;		
		int rut=0;
		int empresa=0;
		int numDoc=0;
		int numOC=0;
		String fecha="";
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Codigo Movimiento
				codDoc=Integer.parseInt(args[i]);
			}else if(i==1){
				//Fecha Movimiento
				rut=Integer.parseInt(args[i]);
			}else if(i==2){
				//Codigo Documento 3 o 4
				numDoc=Integer.parseInt(args[i]);
			}else if(i==3){
				//Codigo Documento 3 o 4
				empresa=Integer.parseInt(args[i]);
			}else if(i==4){
				//Codigo Documento 3 o 4
				numOC=Integer.parseInt(args[i]);
			}else if(i==5){
				//Codigo Documento 3 o 4
				fecha=args[i].trim();
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
		
		
		//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=AMS&tipo=N
		//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=RTAPIA&tipo=T
		archivoLog=prop.getProperty("urlServletRecepcion")+"?codDoc="+codDoc+"&"+"rut="+rut+"&numDoc="+numDoc+"&"+"empresa="+empresa+"&"+"numOC="+numOC+"&"+"fecha="+fecha;
		System.out.println("Procesa Servlet:"+archivoLog);
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
            in.close(); 
            texto = tmp.toString(); 
        }catch (MalformedURLException e) { 
            texto = "<h2>No esta correcta la URL</h2>".toString(); 
        } catch (IOException e) { 
            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
            } 
        
        
		
		/*procesaRecepcionHelper procesa = new procesaRecepcionHelper();
		if (numDoc==0){
			procesa.procesaRecepcin();
		}
		else{
			procesa.procesaDocumentoIndividual(codDoc, numDoc, rut);
		}*/
		
	}
}
