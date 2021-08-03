package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.PaisDAO;
import cl.caserita.dto.PaisDTO;
import cl.caserita.dto.TiponegocioDTO;

public class PaisDAOImpl implements PaisDAO{

private Connection conn;
private static Logger log = Logger.getLogger(PaisDAOImpl.class);

	public PaisDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List region(){
		PaisDTO paisDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List pais=new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTREG " + 
        "  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				paisDTO = new PaisDTO();
				paisDTO.setCodigo(rs.getInt("TPTCO1"));
				paisDTO.setDescripcion(rs.getString("TPTDE1"));
				pais.add(paisDTO);
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
		
		
		return pais;
	}
	public List ciudad(int region){
		PaisDTO paisDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List pais=new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTCTY " + 
        "  WHERE TPTCO6="+region+" AND TPTES1='A' FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				paisDTO = new PaisDTO();
				paisDTO.setCodigo(rs.getInt("TPTCO7"));
				paisDTO.setDescripcion(rs.getString("TPTDE7"));
				pais.add(paisDTO);
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
		
		
		return pais;
	}
	
	public List comuna(int region, int ciudad){
		PaisDTO paisDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List pais=new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTCOM " + 
        "  WHERE TPTC19="+region+" AND TPTC20="+ciudad+" AND TPTES2='A' FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				paisDTO = new PaisDTO();
				paisDTO.setCodigo(rs.getInt("TPTC22"));
				paisDTO.setDescripcion(rs.getString("TPTD20"));
				pais.add(paisDTO);
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
		
		
		return pais;
	}
	
	
	
}
