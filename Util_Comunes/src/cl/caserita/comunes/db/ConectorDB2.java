package cl.caserita.comunes.db;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.sql.DataSource;
/**
 * @author Leonardo Caldera
 * Clase Generica para Conectar a DB2 
 */
public class ConectorDB2
{
	private static String urlDB2;
	private static String userDB2;
	private static String passDB2;
	private static String ipServer;
	
	public static String getIpServer() {
		return ipServer;
	}

	public static void setIpServer(String ipServer) {
		ConectorDB2.ipServer = ipServer;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver"); 

				} catch (Exception e) {
					e.printStackTrace();
				}
		return DriverManager.getConnection(urlDB2,userDB2,passDB2);//url: jdbc:db2://localhost:50000/nameDB
	}
	
	public static Connection ConexioniSeries()
	  {
	  	Connection conAS=null;
	  	try 
	  	{
	  		
	  	PreparedStatement stmtAS = null;
	  	ResultSet rsetAS=null;
	  	
	  	Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
	  	//Connection conAS = null;
	  	conAS = DriverManager.getConnection("jdbc:as400://"+ipServer,userDB2,passDB2);
	  	
	   }
	   catch(ClassNotFoundException e){ System.out.println(e); }
	   catch(SQLException e){ System.out.println(e); }
	   catch(Exception e){ System.out.println(e); }
	   return conAS;
	  }
	 
	public static String getPassDB2()
	{
		return passDB2;
	}

	public static String getUrlDB2()
	{
		return urlDB2;
	}

	public static String getUserDB2()
	{
		return userDB2;
	}

	public static void setPassDB2(String string)
	{
		passDB2= string;
	}

	public static void setUrlDB2(String string)
	{
		urlDB2= string;
	}

	public static void setUserDB2(String string)
	{
		userDB2= string;
	}

}

