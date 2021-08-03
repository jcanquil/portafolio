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

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.TptempDTO;

public class ProcesaPDFHelper {
	private  static Logger logi = Logger.getLogger(ProcesaPDFHelper.class);

	private static FileWriter fileWriterLog;
	private static String archivoLog;
	private static Properties prop=null;
	private static String pathProperties;
	
	public void ProcesaPDF(int empresa, int rutProveedor, int codDoc, int numeroDoc){
		DAOFactory dao =  DAOFactory.getInstance();
		ConarcDAO conarc = dao.getConarcDAO();
		WsClient ws = new WsClient();
		try{
			logi.info("Empresa:"+empresa);
			logi.info("Rut:"+rutProveedor);
			logi.info("CodDoc:"+codDoc);
			logi.info("NumeroDoc:"+numeroDoc);
			TptempDAO emp = dao.getTptempDAO();
			TptempDTO dto = emp.recuperaEmpresa(empresa);
			TptdeleDAO tptdele = dao.getTptdeleDAO();
			int codele = tptdele.buscaDocumentoElectronico(codDoc);
			logi.info("Codigo Paperless:"+codele);
			String url = ws.onlineRecoveryRecUrl(rutProveedor, codele, numeroDoc, dto.getRut(),"");
			logi.info("URL Documento:"+url);
			
			
			if (url!=null || url!=""){
				if (rutProveedor==88502900){
					codDoc=36;
				}
				conarc.actualizaPDF(empresa, numeroDoc, rutProveedor, codDoc, url);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main (String []args){
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
				empresa=Integer.parseInt(args[i]);
			}else if(i==1){
				//Fecha Movimiento
				rut=Integer.parseInt(args[i]);
			}else if(i==2){
				//Codigo Documento 3 o 4
				codDoc=Integer.parseInt(args[i]);
			}else if(i==3){
				//Codigo Documento 3 o 4
				numDoc=Integer.parseInt(args[i]);
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
		
		
		//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=AMS&tipo=N
		//http://192.168.1.22:8080/ServiciosCaserita/CaserServlet?tipo=21&fch=20130530&num=9752486&cod=33&rut=8084782&dv=3&usuario=RTAPIA&tipo=T
		archivoLog=prop.getProperty("urlServletPDF")+"?codDoc="+codDoc+"&"+"rut="+rut+"&numDoc="+numDoc+"&"+"empresa="+empresa+"&"+"numOC="+numOC+"&"+"fecha="+fecha;
		logi.info("Procesa Servlet:"+archivoLog);
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
        
	}
}
