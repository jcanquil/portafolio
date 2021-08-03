package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CotplcDAO;
import cl.caserita.dto.ConnoidcDTO;
import cl.caserita.dto.CotplcDTO;

public class CotplcDAOImpl implements CotplcDAO{

	private static Logger log = Logger.getLogger(CotplcDAOImpl.class);
	private Connection conn;
	
	public CotplcDAOImpl(Connection conn){
		this.conn=conn;
	}
	public CotplcDTO buscaPeriodo(int ano, int mes){
		CotplcDTO cotplcDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.COTPLC " + 
        " Where COTAOX='"+ano+"' AND COTMES="+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cotplcDTO = new CotplcDTO();
				cotplcDTO.setAno(rs.getInt("COTAOX"));
				cotplcDTO.setMes(rs.getInt("COTMES"));
				cotplcDTO.setEstado(rs.getString("COTEST"));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return cotplcDTO;
	}
	
	public int insertaPeriodo(CotplcDTO cotplc){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
			" CASEDAT.COTPLC (cotaox, cotmes, cotest) " +
			" VALUES("+cotplc.getAno()+","+cotplc.getMes()+", '"+cotplc.getEstado()+"') " ;
		log.info("INSERTA PERIODO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		return res;
	}
	
}
