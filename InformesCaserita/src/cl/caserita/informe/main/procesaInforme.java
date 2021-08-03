package cl.caserita.informe.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import cl.caserita.comunes.db.ConectorDB2;
import cl.caserita.enviomail.main.envioMailCaseritaAdjunto;
import cl.caserita.informe.dao.ConsultaDatosDAO;
import cl.caserita.informe.process.generaExcel;

public class procesaInforme {

	private static String usr="";
	private static String pass="";
	private static String servidor="";
	private static String bdd ="";
	private static Properties prop=null;
	private static String pathProperties;
	private static String mail1 ="";
	private static String mail2 ="";
	private static String mail3 ="";
	
	public static void main (String[]args) throws IOException{
		Connection conn=null;
		prop = new Properties();
		prop.load(new FileInputStream(Constantes.RUTA_PROPERTIES+"config.properties"));
		pathProperties = Constantes.RUTA_PROPERTIES;
		
		
		cargaProperties();
		ConectorDB2 conector=new ConectorDB2();
		conector.setUserDB2(usr);
		conector.setPassDB2(pass);
		conector.setIpServer(servidor);
		
		try{
			conn = conector.ConexioniSeries();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ConsultaDatosDAO dao = new ConsultaDatosDAO();
		dao.SetConnection(conn);
		try{
			List lista = dao.datosExmges();
			generaExcel excel = new generaExcel();
			String nombre = excel.generaExcelGes(lista);
			envioMailCaseritaAdjunto envio = new envioMailCaseritaAdjunto();
			boolean exito = envio.envioMail(nombre,"jcanquil@caserita.cl","","INFORME GASTO");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void cargaProperties(){
		usr="jhcanquil";
		pass="guitarrero";
		servidor="192.168.1.10";
		bdd="CASEDAT";
		
	}
	
	private void loadProperties() throws Exception {
		try{			
			System.out.println("rutaArchivo de propiedades " + pathProperties);
			prop.load(new FileInputStream(pathProperties+"config.properties"));
			ConectorDB2.setUrlDB2("jdbc:db2:"+prop.getProperty("BD"));
			ConectorDB2.setUserDB2(prop.getProperty("USER"));
			ConectorDB2.setPassDB2(prop.getProperty("CLAVE"));	
			mail1=prop.getProperty("MAIL1");
			mail2=prop.getProperty("MAIL2");
			mail3=prop.getProperty("MAIL3");
			

//			File f=new File(archivoLog);
//			if (f.exists())
//				f.delete();	
//			log.debug("Archivo FileWriter : "+archivoLog);
//			fileWriterLog=new FileWriter(archivoLog,true);
			
		}catch(Exception ex){
			System.out.println("ERROR : "+ex);
			throw new Exception(" loadProperties "+ex.getMessage());
		}
	}
	
}
