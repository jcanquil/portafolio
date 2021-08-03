package cl.caserita.comunes.db;
import java.sql.*;
import java.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
class Conexiones
{
  String ipServer;
  String username;
  String password;
  String Database;
  
  Conexiones (String ipServer, String username, String password, String Database)
  {
  	this.ipServer=ipServer;
  	this.username=username;
  	this.password=password;
  	this.Database=Database;
  }
  
  String getipServer()
  {
  	return ipServer;
  }
  
  String getUsername()
  {
  	return username;
  }
  
  String getPassword()
  {
  	return password;
  }
  
  String getDatabase()
  {
  	return Database;
  }
  
  void setipServer(String ipServer)
  {
  	this.ipServer=ipServer;
  }
  
  void setUsername(String username)
  {
  	this.username=username;
  }
  
  void setPassword(String password)
  {
  	this.password=password;
  }
  
  Connection ConexioniSeries()
  {
  	Connection conAS=null;
  	try 
  	{
  		
  	PreparedStatement stmtAS = null;
  	ResultSet rsetAS=null;
  	
  	Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
  	//Connection conAS = null;
  	conAS = DriverManager.getConnection("jdbc:as400://"+ipServer,username,password);
  	
   }
   catch(ClassNotFoundException e){ System.out.println(e); }
   catch(SQLException e){ System.out.println(e); }
   catch(Exception e){ System.out.println(e); }
   return conAS;
  }
  Connection ConexionTsm()
  {
  	
    Connection conTSM=null;
    
    try
    {
    	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
    	conTSM = DriverManager.getConnection("jdbc:odbc:TSM",username,password);
    }
    catch(ClassNotFoundException e){ System.out.println(e); }
    catch(SQLException e){ System.out.println(e); }
    catch(Exception e){ System.out.println(e); }
    return conTSM;
  }
  Date TransFecha(String fecha5)
  {
  	//Date hoy=new Date();
    //Date d = new Date();
    Date fecha2=null;
    Time hora;
    //fecha2=getDate();
    String fecha = fecha5;
    String ano = fecha.substring (0,4);
    String mes = fecha.substring(5,7);    
    String dia = fecha.substring(8,10);
    String fechachar = dia + "-" + mes + "-" + ano;
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    Date date2;
    try {
    	String fecha9 ="01-01-2009";
      Date date = format.parse("31-12-2000");
      System.out.println(date);
      date2=format.parse(fecha9);
      System.out.println(format.format(date2));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // formatting
    ;
    System.out.println(format.format(new Date()));
    Date fecha90=null;//=format.format(date2);
    return fecha90;
    
  	
  	
  	
  	
  }
  
    	
  
}