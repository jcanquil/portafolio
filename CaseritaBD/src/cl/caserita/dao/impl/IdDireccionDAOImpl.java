// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IdDireccionDAOImpl.java

package cl.caserita.dao.impl;

import cl.caserita.dao.iface.IdDireccionDAO;
import cl.caserita.dto.ExmvndDTO;
import cl.caserita.dto.IdDireccionDTO;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class IdDireccionDAOImpl
    implements IdDireccionDAO
{
	private static Logger log = Logger.getLogger(IdDireccionDAOImpl.class);


    public IdDireccionDAOImpl(Connection conn)
    {
        this.conn = conn;
    }

    public List direcciones()
    {
        PreparedStatement pstmt;
        ResultSet rs;
        List dir;
        String sqlObtenerVecmar;
        IdDireccionDTO idDireccion = null;
        pstmt = null;
        rs = null;
        dir = new ArrayList();
        sqlObtenerVecmar = "Select *  from CASEDAT.IDDIRCLI   FOR READ ONLY";
        log.info((new StringBuilder("SQL IDDIRCLI")).append(sqlObtenerVecmar).toString());
        try
        {
            pstmt = conn.prepareStatement(sqlObtenerVecmar);
            
            for(rs = pstmt.executeQuery(); rs.next(); dir.add(idDireccion))
            {
                idDireccion = new IdDireccionDTO();
                idDireccion.setIdDireccion(rs.getInt("IDDIREC"));
                idDireccion.setCorrelativoDirecciones(rs.getInt("CLICOR"));
                idDireccion.setCodRegion(rs.getInt("CLICO1"));
                idDireccion.setDescripcionRegion(rs.getString("TPTDE1").trim());
                idDireccion.setCodCiudad(rs.getInt("CLICO2"));
                idDireccion.setDescripcionCiudad(rs.getString("TPTDE7").trim());
                idDireccion.setCodComuna(rs.getInt("CLICO3"));
                idDireccion.setDescripcionComuna(rs.getString("TPTD20").trim());
                idDireccion.setDireccion(rs.getString("CLIDI1").trim());
                idDireccion.setNumeroDireccion(rs.getInt("NUMDIRE"));
                idDireccion.setCodVendedor(rs.getInt("CAMCOD"));
                idDireccion.setNombreVendedor(rs.getString("NOMVND").trim());
                idDireccion.setRevision(0);
                idDireccion.setRutCliente(rs.getInt("CLCRU1"));
                idDireccion.setNombreCliente(rs.getString("CAMNOM").trim());
                idDireccion.setNombreContacto(rs.getString("CLICON").trim());
            }

           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception) { }
        
        Exception exception;
       
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception1) { }
       
        return dir;
    }
    
    public List direccionesPorRut(int rut)
    {
        PreparedStatement pstmt;
        ResultSet rs;
        List dir;
        String sqlObtenerVecmar;
        IdDireccionDTO idDireccion = null;
        pstmt = null;
        rs = null;
        dir = new ArrayList();
        sqlObtenerVecmar = "Select *  from CASEDAT.IDDIRCLI   WHERE IDDIREC>="+rut+" FOR READ ONLY";
        log.info((new StringBuilder("SQL IDDIRCLI")).append(sqlObtenerVecmar).toString());
        try
        {
            pstmt = conn.prepareStatement(sqlObtenerVecmar);
            
            for(rs = pstmt.executeQuery(); rs.next(); dir.add(idDireccion))
            {
                idDireccion = new IdDireccionDTO();
                idDireccion.setIdDireccion(rs.getInt("IDDIREC"));
                idDireccion.setCorrelativoDirecciones(rs.getInt("CLICOR"));
                idDireccion.setCodRegion(rs.getInt("CLICO1"));
                idDireccion.setDescripcionRegion(rs.getString("TPTDE1").trim());
                idDireccion.setCodCiudad(rs.getInt("CLICO2"));
                idDireccion.setDescripcionCiudad(rs.getString("TPTDE7").trim());
                idDireccion.setCodComuna(rs.getInt("CLICO3"));
                idDireccion.setDescripcionComuna(rs.getString("TPTD20").trim());
                idDireccion.setDireccion(rs.getString("CLIDI1").trim());
                idDireccion.setNumeroDireccion(rs.getInt("NUMDIRE"));
                idDireccion.setCodVendedor(rs.getInt("CAMCOD"));
                idDireccion.setNombreVendedor(rs.getString("NOMVND").trim());
                idDireccion.setRevision(0);
                idDireccion.setRutCliente(rs.getInt("CLCRU1"));
                idDireccion.setNombreCliente(rs.getString("CAMNOM").trim());
                idDireccion.setNombreContacto(rs.getString("CLICON").trim());
            }

           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception) { }
        
        Exception exception;
       
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception1) { }
       
        return dir;
    }
    
    public List vendedoresNuevos()
    {
        PreparedStatement pstmt;
        ResultSet rs;
        List dir = new ArrayList();
        String sqlObtenerVecmar;
        IdDireccionDTO idDireccion = null;
        pstmt = null;
        rs = null;
        dir = new ArrayList();
        sqlObtenerVecmar = "Select *  from CASEDAT.TPVENDED   FOR READ ONLY";
        log.info((new StringBuilder("SQL IDDIRCLI")).append(sqlObtenerVecmar).toString());
        try
        {
            pstmt = conn.prepareStatement(sqlObtenerVecmar);
            
            for(rs = pstmt.executeQuery(); rs.next(); dir.add(idDireccion))
            {
            	idDireccion = new IdDireccionDTO();
            	idDireccion.setCodVendedor(rs.getInt("CAMCOD"));
                dir.add(idDireccion);                
            }

           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception) { }
        
        Exception exception;
       
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception1) { }
       
        return dir;
    }
    public int obtieneTVendedor(int codVendedor){
		int res=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.EXMVND " + 
        "  WHERE EXMC09="+codVendedor+" FOR READ ONLY" ;
		log.info("SQL IDDIRCLI" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				res=rs.getInt("EXMC10");	
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return res;
	}
    public List buscaVendedores (int tipoVendedor){
		IdDireccionDTO idDireccion=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ExmvndDTO exmvnd = null;
		List dir = new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.EXMVND " + 
        "  WHERE EXMC10="+tipoVendedor+" FOR READ ONLY" ;
		log.info("SQL IDDIRCLI" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmvnd = new ExmvndDTO();
				exmvnd.setCodigoVendedor(rs.getInt("EXMC09"));
				exmvnd.setNombreVendedor(rs.getString("EXMNO1"));
				dir.add(exmvnd);	
				
				
			}
			//log.info("Despues de buscar en VECMAR");
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return dir;
	}
	
    
    public IdDireccionDTO buscaDireccion(int iddir, int correlativo)
    {
        PreparedStatement pstmt;
        ResultSet rs;
        List dir;
        String sqlObtenerVecmar;
        IdDireccionDTO idDireccion = null;
        pstmt = null;
        rs = null;
        dir = new ArrayList();
        sqlObtenerVecmar = "Select *  from CASEDAT.IDDIRCLI   WHERE IDDIREC="+iddir+" AND CLICOR="+correlativo+" ";
        log.info((new StringBuilder("SQL IDDIRCLI")).append(sqlObtenerVecmar).toString());
        try
        {
            pstmt = conn.prepareStatement(sqlObtenerVecmar);
            
            for(rs = pstmt.executeQuery(); rs.next(); dir.add(idDireccion))
            {
                idDireccion = new IdDireccionDTO();
                idDireccion.setIdDireccion(rs.getInt("IDDIREC"));
                idDireccion.setCorrelativoDirecciones(rs.getInt("CLICOR"));
                idDireccion.setCodRegion(rs.getInt("CLICO1"));
                idDireccion.setDescripcionRegion(rs.getString("TPTDE1").trim());
                idDireccion.setCodCiudad(rs.getInt("CLICO2"));
                idDireccion.setDescripcionCiudad(rs.getString("TPTDE7").trim());
                idDireccion.setCodComuna(rs.getInt("CLICO3"));
                idDireccion.setDescripcionComuna(rs.getString("TPTD20").trim());
                idDireccion.setDireccion(rs.getString("CLIDI1").trim());
                idDireccion.setNumeroDireccion(rs.getInt("NUMDIRE"));
                idDireccion.setCodVendedor(rs.getInt("CAMCOD"));
                idDireccion.setNombreVendedor(rs.getString("NOMVND").trim());
                idDireccion.setRevision(0);
                idDireccion.setRutCliente(rs.getInt("CLCRU1"));
                idDireccion.setNombreCliente(rs.getString("CAMNOM").trim());
                idDireccion.setNombreContacto(rs.getString("CLICON").trim());
            }

           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception) { }
        
        Exception exception;
       
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }
        catch(SQLException sqlexception1) { }
       
        return idDireccion;
    }
    
    public int actualiza(IdDireccionDTO gen)
    {
        int res;
        PreparedStatement pstmt;
        String sqlObtenerVecmar;
        res = 0;
        pstmt = null;
        sqlObtenerVecmar = (new StringBuilder("UPDATE   CASEDAT.IDDIRCLI  (LATDIR,LONDIR) VALUES('")).append(gen.getLatitud()).append("',").append(gen.getLongitud()).append(") WHERE IDDIREC=").append(gen.getIdDireccion()).append(" AND CLICOR=").append(gen.getCorrelativoDirecciones()).append(" +").toString();
        try
        {
            pstmt = conn.prepareStatement(sqlObtenerVecmar);
            pstmt.executeUpdate();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            pstmt.close();
        }
        catch(SQLException sqlexception) { }
        
        Exception exception;
       
        try
        {
            pstmt.close();
        }
        catch(SQLException sqlexception1) { }
        
        return res;
    }

    private Connection conn;
}
