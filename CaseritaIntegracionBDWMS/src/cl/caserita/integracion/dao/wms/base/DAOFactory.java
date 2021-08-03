// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DAOFactory.java

package cl.caserita.integracion.dao.wms.base;


import java.io.FileInputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class DAOFactory
{

    private DAOFactory()
    {
        getConnection();
    }

    private DAOFactory(String url, String user, String pass)
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch(Exception e)
        {
            log.error((new StringBuilder("Error al conectar a base de datos: ")).append(e.getMessage()).toString());
        }
    }

    public static DAOFactory getInstance()
    {
        prop = new Properties();
        try
        {
            prop.load(new FileInputStream("/Users/jcanquil/Documents/usr/home/ServiciosCaserita/properties/config.properties"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String ipServer = prop.getProperty("ipServerSQL");
        String userDB2 = prop.getProperty("usuarioSQL");
        String passDB2 = prop.getProperty("passSQL");
        String bd = prop.getProperty("bdSQL");
        
        try
        {
            conn = ConexioniSeries(ipServer, bd, userDB2, passDB2);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=pubs;user=sa; password=adminadmin;";   
        return new DAOFactory((new StringBuilder("jdbc:sqlserver://")).append(ipServer).append("/").append("databaseName="+bd).toString(), "user="+userDB2, "password:"+passDB2);
    }

    private void getConnection()
    {
        try
        {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource)ic.lookup(DATASOURCE);
            getInstance();
            conn = ds.getConnection();
        }
        catch(Exception e)
        {
            log.error((new StringBuilder("Error al conectar a base de datos: ")).append(e.getMessage()).toString());
        }
    }

    public void closeConnection()
    {
        try
        {
            if(conn != null)
                conn.close();
        }
        catch(SQLException sqlexception) { }
    }

    public void iniciaTransaccion()
    {
        try
        {
            conn.setAutoCommit(false);
        }
        catch(Exception e)
        {
            log.error("Error al iniciar transaccion");
        }
    }

    public void commitTransaccion()
    {
        try
        {
            conn.commit();
        }
        catch(Exception e)
        {
            log.error("Error al realizar commit");
        }
    }

    public void rollbackTransaccion()
    {
        try
        {
            conn.rollback();
        }
        catch(Exception e)
        {
            log.error("Error al realizar rollback");
        }
    }

    public static Connection ConexioniSeries(String ipServer, String bd, String userDB2, String passDB2)
    {
        Connection conAS = null;
        try
        {
            java.sql.PreparedStatement stmtAS = null;
            java.sql.ResultSet rsetAS = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            String connectionUrl = "jdbc:sqlserver://192.168.1.32:1433;" +
                    "databaseName=master;user=sa; password=;";   
            String connection = (new StringBuilder("jdbc:sqlserver://")).append(ipServer).append("/").append(bd).append("?").append("user=").append("sa").append("&password=").append("").toString();
            System.out.println((new StringBuilder("Primer String:")).append(connection).toString());
            System.out.println("String2:"+connectionUrl);
           
            conAS = DriverManager.getConnection(connectionUrl);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return conAS;
    }

    

    public static void main(String args[])
    {
        DAOFactory dao = new DAOFactory();
        getInstance();
    }

    private static Connection conn;
    private static Logger log = Logger.getLogger(DAOFactory.class);
    private static String DATASOURCE = "";
    private static Properties prop = null;

}
