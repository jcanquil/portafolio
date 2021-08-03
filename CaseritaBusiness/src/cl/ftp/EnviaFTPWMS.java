package cl.caserita.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.log4j.Logger;

import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dto.FtpprovDTO;
import cl.caserita.dto.RecepDocumentoDTO;
import cl.caserita.wms.helper.IntegraCarguioHelper;

public class EnviaFTPWMS {
	private static Properties prop=null;
	private static String pathProperties;
	private  static Logger logi = Logger.getLogger(EnviaFTPWMS.class);

	public boolean enviaFTP(String xml, FtpprovDAO ftpDAO){
		int num=0;
		boolean res=false;
		/*prop = new Properties();
		try{
			//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;*/
		/*String ip=prop.getProperty("ipFtp");
		String usr=prop.getProperty("usrFtp");

		String password=prop.getProperty("passFtp");*/
		FtpprovDTO dto = ftpDAO.buscaFTP("STG");
		String ip=dto.getIpServidor().trim();
		String usr=dto.getUsuarioFTP().trim();

		String password=dto.getPasswordFTP().trim();
		
		logi.info("ipFtp:"+ip);
		logi.info("usrFtp:"+usr);
		logi.info("passFtp:"+password);
		
		try
        {
			 FTPClient ftpClient = new FTPClient();
           // FTPClient ftpClient = new FTPClient();
           /* ftpClient.connect(InetAddress.getByName("192.168.1.23"));
            ftpClient.login("docu","Ca$er5808");*/
            //ftpClient.connect(InetAddress.getByName("192.168.1.32"));
            //ftpClient.login("jcanquil","Lastra657");
            ftpClient.connect(ip.trim(),21);
            ftpClient.login(usr,password);
            //Recupera String con nombre de Archivo y ruta completa del archivo
            StringTokenizer st = new StringTokenizer(xml,"|");
            int numString=0;
            String rutaArchivo = "";
            String nomFile ="";
	   		 while (st.hasMoreTokens( )){
	   			 
	   			 	String tr = st.nextToken();
	   			 	if (numString==0){
	   			 		nomFile=tr;

	   			 	}else if (numString==1){
	   			 	rutaArchivo=tr;
	   			 	}
	   			 	numString++;
	   			 	logi.info("String:"+tr);
	   		 }
            //Verificar conexión con el servidor.
            int reply = ftpClient.getReplyCode();
            logi.info("Respuesta recibida de conexión FTP:" + reply);
            
            if(FTPReply.isPositiveCompletion(reply))
            {
                logi.info("Conectado Satisfactoriamente");    
            }
            else
                {
            	
                    logi.info("Imposible conectarse al servidor:"+FTPReply.isPositiveCompletion(reply));
                }
           
            //Verificar si se cambia de direcotirio de trabajo
            
            boolean mm = ftpClient.changeWorkingDirectory("/hostin");//Cambiar directorio de trabajo
            logi.info("Se cambió satisfactoriamente el directorio");
            //Activar que se envie cualquier tipo de archivo
            logi.info("Cambio directorio"+mm);
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
           
           // BufferedInputStream buffIn = null;
           // buffIn = new BufferedInputStream(new FileInputStream(xml));//Ruta del archivo para enviar
            //ftpClient.enterLocalPassiveMode();
         // Set protection buffer size comentado ahoritas
           // ftpClient.execPBSZ(0);
            // Set data channel protection to private comentando ahoritas
           // ftpClient.execPROT("P");
            // Enter local passive mode
            ftpClient.enterLocalPassiveMode();
            
            File downloadFile1 = new File(rutaArchivo);
            InputStream inputStream = new FileInputStream(downloadFile1);
            String firstRemoteFile = nomFile;

             res = ftpClient.storeFile(firstRemoteFile, inputStream);//Ruta completa de alojamiento en el FTP
            logi.info("Respuesta:"+res);
            //buffIn.close(); //Cerrar envio de arcivos al FTP
            ftpClient.logout(); //Cerrar sesión
            ftpClient.disconnect();//Desconectarse del servidor
        }
        catch(Exception e)
                {
                    logi.info(e.getMessage());
                    logi.info("Algo malo sucedió");
                }
		
		return res;
	}
	
	
	public boolean enviaFTP2(String xml, FTPClient ftpClient){
		int num=0;
		boolean res=false;
		/*prop = new Properties();
		try{
			//logi.info("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;*/
		/*String ip=prop.getProperty("ipFtp");
		String usr=prop.getProperty("usrFtp");

		String password=prop.getProperty("passFtp");*/
		
		
		try
        {
           
            //Recupera String con nombre de Archivo y ruta completa del archivo
            StringTokenizer st = new StringTokenizer(xml,"|");
            int numString=0;
            String rutaArchivo = "";
            String nomFile ="";
	   		 while (st.hasMoreTokens( )){
	   			 
	   			 	String tr = st.nextToken();
	   			 	if (numString==0){
	   			 		nomFile=tr;

	   			 	}else if (numString==1){
	   			 	rutaArchivo=tr;
	   			 	}
	   			 	numString++;
	   			 	logi.info("String:"+tr);
	   		 }
            //Verificar conexión con el servidor.
            
           
            //Verificar si se cambia de direcotirio de trabajo
            
           
            
            File downloadFile1 = new File(rutaArchivo);
            InputStream inputStream = new FileInputStream(downloadFile1);
            String firstRemoteFile = nomFile;
            ftpClient.setConnectTimeout(50000);
            
             res = ftpClient.storeFile(firstRemoteFile, inputStream);//Ruta completa de alojamiento en el FTP
            logi.info("Respuesta:"+res);
            //buffIn.close(); //Cerrar envio de arcivos al FTP
           // ftpClient.logout(); //Cerrar sesión
           // ftpClient.disconnect();//Desconectarse del servidor
        }
        catch(Exception e)
                {

                    logi.info(e.getMessage());
                    logi.info("Algo malo sucedió");
                }
		
		return res;
	}
	
	
	
}
