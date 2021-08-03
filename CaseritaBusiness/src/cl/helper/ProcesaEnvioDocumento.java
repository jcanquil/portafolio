package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.comunes.properties.Constants;

public class ProcesaEnvioDocumento {
	private  static Logger logi = Logger.getLogger(ProcesaEnvioDocumento.class);

	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public static void main (String []args){
		int empresa=0;
		int codigo=0;
		String numero="";
		String mail="";
		String mailCopia="";
		int rut=0;
		
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Codigo Movimiento
				empresa=Integer.parseInt(args[i]);
			}else if(i==1){
				//Fecha Movimiento
				codigo=Integer.parseInt(args[i]);
			}else if(i==2){
				//Codigo Documento 3 o 4
				
				rut=Integer.parseInt(args[i]);
			}else if(i==3){
				mail=args[i];
			}else if(i==4){
				mailCopia=args[i];
			}else if(i==5){
				numero=args[i];
			}
		}
		logi.info("Empresa:"+empresa);
		logi.info("Codigo:"+codigo);
		logi.info("numero:"+numero);
		logi.info("mail1:"+mail);
		logi.info("mailCopia:"+mailCopia);
		logi.info("Rut:"+rut);
		prop = new Properties();
		try{
			//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		
		
		
		archivoLog=prop.getProperty("urlServletEnvio")+"?empresa="+empresa+"&tipo="+codigo+"&mail="+mail+"&mailCopia="+mailCopia+"&rut="+rut+"&numero="+numero;
		String log = prop.getProperty("urlServletEnvio")+"?empresa="+empresa+"&tipo="+codigo+"&mail="+mail.trim()+"&mailCopia="+mailCopia.trim()+"&rut="+rut+"&numero="+numero.trim();
		//String archivoLog2="&mail="+mail.trim()+"&mailCopia="+mailCopia.trim()+"&rut="+rut;
		//archivoLog=archivoLog+archivoLog2;
		logi.info("Servlet:"+archivoLog);
		logi.info("Servlet2:"+log);
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
	public String enviaXML(int numeroDoc, int rutCliente, int tipoDocumento, String rutaDocumento, String tipoArchivo){
		String ruta="";
		String url = rutaDocumento; //direcci�n url del recurso a descargar
		//url ="http://192.168.1.4:8081/Facturacion/PDFServlet?docId=338X/DI3Cu8eMl1ARqkx2eLW3c4qbQ47";
		String name = String.valueOf(numeroDoc)+"_"+String.valueOf(rutCliente)+tipoArchivo; //nombre del archivo destino
		//name="pdf.pdf";
		//Directorio destino para las descargas
		String folder = "/home/descargas/";
		 
		//Crea el directorio de destino en caso de que no exista
		File dir = new File(folder);
		ruta=folder+name;
		
		File file = new File(folder + name);
		try{
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			logi.info("\nempezando descarga: \n");
			logi.info(">> URL: " + url);
			logi.info(">> Nombre: " + name);
			logi.info(">> tama�o: " + conn.getContentLength() + " bytes");
			
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(file);
			int b = 0;
			while (b != -1) {
			  b = in.read();
			  if (b != -1)
			    out.write(b);
			}
			out.close();
			in.close();
		}catch(Exception e){
			
			
		}
		
		return ruta;
	}
}
