package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.TptbdgDTO;

public class TptbdgDAOImpl implements TptbdgDAO{

	private static Logger log = Logger.getLogger(TptbdgDAOImpl.class);
	private Connection conn;
	
	public TptbdgDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public TptbdgDTO buscaBodega(int bodega){
		TptbdgDTO tptbdg = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTBDG " + 
        " Where TPTC18="+bodega+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tptbdg = new TptbdgDTO();
				tptbdg.setDireccion(rs.getString("TPTDIR"));
				tptbdg.setDescComuna(recuperaComuna(rs.getInt("TPTC21"), rs.getInt("TPTC23"), rs.getInt("TPTC24")));
				tptbdg.setDescCiudad(recuperaCiudad(rs.getInt("TPTC21"), rs.getInt("TPTC23")));
				tptbdg.setCodigoRegion(rs.getInt("TPTC21"));
				tptbdg.setCodigoSii(rs.getInt("TPTVA8"));
				tptbdg.setDesBodega(rs.getString("TPTD18"));
				tptbdg.setCodigoBodega(bodega);
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
		
		
		return tptbdg;
	}
	
	public String recuperaComuna(int region, int ciudad, int comuna){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCOM " + 
        " Where TPTC19="+region+" AND TPTC20="+ciudad+" AND TPTC22="+comuna+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTD20");
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
		
		
		return descomuna;
		
		
	}
	
	public String recuperaCiudad(int region, int ciudad){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCTY " + 
        " Where TPTCO6="+region+" AND TPTCO7="+ciudad+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTDE7");
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
		
		
		return descomuna;
		
		
	}
}
