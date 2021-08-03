package cl.caserita.wms.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.google.gson.Gson;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.ecommerce.helper.ProcesaVentaHelper;
import cl.caserita.enviomail.main.emailInformacionWMS;
import cl.caserita.wms.dto.ConfirmacionDTO;
import cl.caserita.wms.out.helper.IntegracionAjusteHelper;
import cl.caserita.wms.out.helper.IntegracionConfirCamionHelper;
import cl.caserita.wms.out.helper.IntegracionConfirmaEnvioHelper;
import cl.caserita.wms.out.helper.IntegracionConfirmaRecepcionHelper;
import cl.caserita.wms.out.helper.IntegracionReconInventarioHelper;

public class IntegracionSalidaWEBHelper {
	private static Properties prop=null;
	private static String pathProperties;
	private  static Logger log = Logger.getLogger(IntegracionSalidaWEBHelper.class);
	
	private static emailInformacionWMS email = new emailInformacionWMS();

	public static void main (String []args){
		int i2=0;
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream("/proceso/"+"config.properties"));
			//prop.load(new FileInputStream("proceso/"+"config.properties"));
		}
		catch(Exception e){
			String mensaje = "Mensaje 1 :"+e.getMessage();
			email.mail(mensaje);
			e.printStackTrace();
		}
		try{
			String ws=prop.getProperty("wsCaserita");
			String dir1=prop.getProperty("dirInicial");
			String dir2=prop.getProperty("dirFinal");
			while (i2==0){
				//String path = "/home2/ftp/out/";
				//String path = "/RedPrairie/LCSWMSTST/LES/files/hostout/";
				String path = dir1;

				String files;

				File directorio = new File(path);
				File [] ficheros = directorio.listFiles();
				String line;
				//log.info(ws);
				if (ficheros!=null){
					for (int i = 0; i < ficheros.length; i++) {
						
			            if (ficheros[i].isFile()) {
			            	files = ficheros[i].getName();
			                if (files.endsWith(".xml") || files.endsWith(".XML")){
			                	log.info("archivo:"+path+ficheros[i].getName());
			    				try{
			    					
			    					BufferedReader bf = new BufferedReader(new FileReader(path+ficheros[i].getName()));
			    					String linea="";
			    					String xml="";
			    					int lin=0;
			    					int band=1;
			    					String xml1="";
			    					Fecha fecha = new Fecha();

			    					while((linea=bf.readLine())!=null){
			    						xml=xml+linea;
			    						lin++;
			    						band=1;
			    						if (lin==5000){

			    							xml1=xml1+xml;
			    							xml="";
			    							lin=0;
			    							band=0;
			    						}
			    					}
			    					//Concatena XML 
			    					if (band==1){
			    						xml1=xml1+xml;
			    					}
			    					Fecha fecha2 = new Fecha();
			    					
			    					log.info(xml1);
			    					//System.out.println(xml);
			    					bf.close();
			    					ConfirmacionDTO confirmacion = new ConfirmacionDTO();
			    					confirmacion.setXml(xml1);
			    					confirmacion.setNameFile(ficheros[i].getName());
			    					Gson gson = new Gson();
			    					String json = gson.toJson(confirmacion);
			    					//System.out.println(json);
			    					try {
			    						 
			    						URL url = new URL(ws.trim());
			    						
			    						//URL url = new URL("http://192.168.1.30:8080/ServiciosTransportistaWEB/servicioTransRest/wmsCaserita/post");

			    						
			    						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			    						conn.setDoOutput(true);
			    						conn.setRequestMethod("POST");
			    						OutputStream os = conn.getOutputStream();
			    						os.write(json.getBytes());
			    						//os.write(xml.getBytes());
			    						os.flush();
			    						
			    				 
			    						if (conn.getResponseCode() != 200) {
			    							throw new RuntimeException("Failed : HTTP error code : "
			    									+ conn.getResponseCode());
			    						}
			    				 
			    						BufferedReader br = new BufferedReader(new InputStreamReader(
			    							(conn.getInputStream())));
			    				 
			    						String output;
			    						log.info("Output from Server .... \n");
			    						/*while ((output = br.readLine()) != null) {
			    							System.out.println(output);
			    						}*/
			    				 
			    						conn.disconnect();
			    						xml="";
					    				xml1="";
					    				log.info("Hora Inicial:"+fecha.getHHMMSS());

				    					log.info("Hora Final:"+fecha2.getHHMMSS());
			    					  } catch (MalformedURLException e) {
			    						  String mensaje = "Mensaje 2: "+e.getMessage();
			    							email.mail(mensaje);
			    						e.printStackTrace();
			    				 
			    					  } catch (IOException e) {
			    						  String mensaje = "Mensaje 3: "+e.getMessage();
			    							email.mail(mensaje);
			    						e.printStackTrace();
			    				 
			    					  }
			    					

			    				}catch(Exception e){
			    					String mensaje = "Mensaje 4: "+e.getMessage();
			    					email.mail(mensaje);
			    					e.printStackTrace();
			    				}
			    				IntegracionSalidaWEBHelper salida = new IntegracionSalidaWEBHelper();
			    				salida.moveFile(path+ficheros[i].getName(), ficheros[i].getName(),dir2);
			    				
			                }
			            }

						
					
					}
				}
				
				
				
			}
		}catch(Exception e){
			String mensaje = "Mensaje 5: "+e.getMessage();
			email.mail(mensaje);			
			e.printStackTrace();
		}
		
	}
	public int moveFile(String urlFile, String nameFile, String dir){
		int numero=0;
		Path origenPath = FileSystems.getDefault().getPath(urlFile);
		Fecha fch = new Fecha();
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4)+"/";
		String mes = fch.getYYYYMMDD().substring(4, 6);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);
		String dia = fch.getYYYYMMDDHHMMSS().substring(6, 8);
		//String path = "/home2/ftp/out/";
	    //Path destinoPath = FileSystems.getDefault().getPath("/home2/ftp/out/proc/"+nameFile);

	   // Path destinoPath = FileSystems.getDefault().getPath("/RedPrairie/LCSWMSTST/LES/files/hostout/caserita/"+nameFile);
		File folder = new File(dir+ano+mesPal+"/"+dia+"/");
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
		}
		
	    Path destinoPath = FileSystems.getDefault().getPath(dir+ano+mesPal+"/"+dia+"/"+nameFile);


	    try {
	        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	    	String mensaje = "Mensaje 6: "+e.getMessage();
			email.mail(mensaje);	
	        log.info(e);
	        
	    }
	    
	    
		return numero;
	}
	
	public String recuperaMes(int mes){
		String mesPal="";
		//System.out.println("Recupera mes:"+mes);
		if (mes== 1)
			mesPal="Enero";
		if (mes== 2)
			mesPal="Febrero";
		if (mes==3)
			mesPal="Marzo";
		if (mes== 4)
			mesPal="Abril";
		if (mes== 5)
			mesPal="Mayo";
		if (mes== 6)
			mesPal="Junio";
		if (mes== 7)
			mesPal="Julio";
		if (mes==8)
			mesPal="Agosto";
		if (mes==9)
			mesPal="Septiembre";
		if (mes== 10)
			mesPal="Octubre";
		if (mes== 11)
			mesPal="Noviembre";
		if (mes== 12)
			mesPal="Diciembre";
			
			
		//System.out.println("Mes en palabra:"+mesPal);
		return mesPal;
	}
	
}
