package cl.caserita.librosContables;

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
import cl.caserita.helper.GeneraLibrosHelper;
import cl.caserita.helper.ProcesaFacturacion;

public class procesaLibros {
	private static Logger log = Logger.getLogger(ProcesaFacturacion.class); 
	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public static GeneraLibrosHelper genera = new GeneraLibrosHelper();
	public static void main (String args[]){
		int fechaInicio=0;
		int fechaFin=0;
		String mail="";
		String tipoLibro="";
		String usuario="";
		int fechaGeneracion=0;
		int horaGeneracion=0;
		int periodoInicial=0;
		int peridoFinal=0;
		int bodega=0;
		int empresa=0;
		procesaLibros procesa = new procesaLibros();
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Tipo Libro
				tipoLibro=args[i];
			}else if (i==1){
				usuario=args[i];
			}else if(i==2){
				fechaGeneracion=Integer.parseInt(args[i]);
			}else if(i==3){
				horaGeneracion=Integer.parseInt(args[i]);
			}else if(i==4){
				periodoInicial=Integer.parseInt(args[i]);
				fechaInicio=periodoInicial;
			}else if(i==5){
				peridoFinal=Integer.parseInt(args[i]);
				fechaFin=peridoFinal;
			}else if(i==6){
				//Mail
				mail=args[i];
			}
			else if(i==7){
				//Bodega
				bodega=Integer.parseInt(args[i]);
			}
			else if(i==8){
				//Empresa
				empresa=Integer.parseInt(args[i]);
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
		archivoLog=prop.getProperty("urlServletLibros")+"?tipoLib="+tipoLibro+"&"+"usuario="+usuario+"&fgeneracion="+fechaGeneracion+"&"+"hgeneracion="+horaGeneracion+"&"+"fInicio="+fechaInicio+"&fFin="+fechaFin+"&mail="+mail+"&bodega="+bodega+"&empresa="+empresa;
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
        
        
		/*if ("LCOMPRAS".equals(tipoLibro)){
			procesa.procesaLibroCompras(tipoLibro, fechaInicio, fechaFin, mail,usuario,  fechaGeneracion,  horaGeneracion,  periodoInicial,  peridoFinal, empresa);
		}else if ("LVENTAS".equals(tipoLibro)){
			procesa.procesaLibroVentas(tipoLibro, fechaInicio, fechaFin, mail,usuario,  fechaGeneracion,  horaGeneracion,  periodoInicial,  peridoFinal, bodega, empresa);
		}
			
	}
	
	public void procesaLibroCompras(String tipoLibro,int fechaInicio, int fechaFin, String mail, String usuario, int fechaGeneracion, int horaGeneracion, int periodoInicial, int periodoFinal, int empresa){
		
		fechaInicio=periodoInicial;
		fechaFin=periodoFinal;
		genera.generaLibroCompras(tipoLibro, fechaInicio, fechaFin, mail,usuario,  fechaGeneracion,  horaGeneracion,  periodoInicial,  periodoFinal,  empresa );		
	}
	
	public void procesaLibroVentas(String tipoLibro,int fechaInicio, int fechaFin, String mail, String usuario, int fechaGeneracion, int horaGeneracion, int periodoInicial, int periodoFinal, int bodega, int empresa){
		
		genera.generaLibroVentas(tipoLibro, fechaInicio, fechaFin, mail,usuario,  fechaGeneracion,  horaGeneracion,  periodoInicial,  periodoFinal, bodega, empresa);		
	}*/
	}
}
