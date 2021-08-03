package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dto.TptdocDTO;

public class ProcesaEstadoSIIHelper {

	private  static Logger logi = Logger.getLogger(ProcesaEnvioDocumento.class);

	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public static void main (String[]args){
		int fechaProceso=0;
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Codigo Movimiento
				fechaProceso=Integer.parseInt(args[i]);
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
		int empresa=2;
		Fecha fch = new Fecha();
		int fecha =0;
		if (fechaProceso==0){
			fecha = Integer.parseInt(fch.getYYYYMMDD());
		}else{
			fecha = fechaProceso;
		}
		
		ProcesaEstadoSIIHelper procesa = new ProcesaEstadoSIIHelper();
		List codDocu = procesa.generaDocu(4);
		TptdocDTO tpt = null;
		Iterator iter = codDocu.iterator();
		while (iter.hasNext()){
			 tpt = (TptdocDTO) iter.next();
			archivoLog=prop.getProperty("urlServletEstadoSII")+"?empresa="+empresa+"&fecha="+fecha+"&CodDoc="+tpt.getCodDoc();
			logi.info("Servlet Estado SII:"+archivoLog);
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
	public List generaDocu (int cantiDoc){
		
		TptdocDTO tptDoc = null;;
		List doc = new ArrayList();
		int canti=1;
		while (cantiDoc>=1){
			tptDoc = new TptdocDTO();
			if (canti==1){
				tptDoc.setCodDoc(33);
			}else if (canti==2){
				tptDoc.setCodDoc(35);
			}else if (canti==3){
				tptDoc.setCodDoc(36);
			}else if (canti==4){
				tptDoc.setCodDoc(42);
			}
			doc.add(tptDoc);
			canti=canti+1;
			cantiDoc=cantiDoc-1;
			
		}
		
		
		return doc;
	}
}
