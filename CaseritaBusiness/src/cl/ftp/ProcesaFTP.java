package cl.caserita.ftp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;


public class ProcesaFTP {

	public static void main (String[]args){
		String server = "192.168.55.71";
		String username = "lcs.jda";
		String password = "L*j4wms1";
		//String username = "jorge";
		//String password = "jorge12";
		try
		{
			FTPClient ftp = new FTPClient();
			
			ftp.connect(server.trim());
			if(!ftp.login(username, password))
			{
				ftp.logout();
			}
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply))
			{
				ftp.disconnect();
			}
			InputStream in = new FileInputStream("/home2/ServiciosCaserita/properties/Cotizacion5555.pdf");
			ftp.setFileType(ftp.BINARY_FILE_TYPE);
			String remoteFile1 = "/home2/ServiciosCaserita/properties/Cotizacion5555.pdf";
            File downloadFile1 = new File("/home2/ServiciosCaserita/properties/Cotizacion5555.pdf");
            InputStream inputStream = new FileInputStream(downloadFile1);
            String firstRemoteFile = "Cotizacion5555.pdf";
            boolean done = ftp.storeFile(firstRemoteFile, inputStream);
			//OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
			
			in.close();
			ftp.logout();
			ftp.disconnect();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
}
