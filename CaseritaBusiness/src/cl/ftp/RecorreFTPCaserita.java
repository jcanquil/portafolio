package cl.caserita.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dto.FtpprovDTO;

public class RecorreFTPCaserita {
	private static Properties prop=null;
	private static String pathProperties;
	
	public static void main (String []args){
		FTPClient ftpClient = new FTPClient();
		/*String server="192.168.1.32";
		int port=21;
		String user="jcanquil";
		String pass="Lastra657";*/
		DAOFactory dao = DAOFactory.getInstance();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();
		FtpprovDTO dto = ftpDAO.buscaFTP("STG");

		String server=dto.getIpServidor().trim();
		int port=21;
		String user=dto.getUsuarioFTP().trim();
		String pass=dto.getUsuarioFTP().trim();
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream("/proceso/"+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String ws=prop.getProperty("wsCaserita");
		
		  
		FileOutputStream fos = null;  
		int cantidad=1;
		while (cantidad>0){
			try{
				
				
				ftpClient.connect(server, port);
				ftpClient.login(user, pass);
				 
				// lists files and directories in the current working directory
				boolean mm = ftpClient.changeWorkingDirectory("/hostout");

				FTPFile[] files = ftpClient.listFiles();
				// iterates over the files and prints details for each
				DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 
				for (FTPFile file : files) {
				    String details = file.getName();
				    int res = details.indexOf("PROC");
				    if (details.indexOf("PROC")!=0.&& file.isFile()){
				    	 //fos = new FileOutputStream("/home/ftp/in/"+details);
				    	 fos = new FileOutputStream("/wms/"+details);  

				    	 ///RedPrairie/LCSWMSTST/LES/files/hostout/
						    boolean download = ftpClient.retrieveFile(details,  
						      fos);  
						    String in =details;
						    String nueva="PROC_"+details;
						    System.out.println(in);
						    System.out.println(nueva);
						    boolean success = ftpClient.rename(in,nueva);
						    
						   
						  
						    fos.close();
						    
				    }
				   
				}
				 fos =null;
				ftpClient.logout();
				ftpClient.disconnect();
				}catch(Exception e){
					e.printStackTrace();
				}
		}
		
	}
}
