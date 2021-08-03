package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;

public class TptempDAOImpl implements TptempDAO{

	private static Logger log = Logger.getLogger(TptempDAOImpl.class);
	private Connection conn;
	
	public TptempDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public TptempDTO recuperaEmpresa(int empresa){
		TptempDTO tptemp = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTEMP " + 
        " Where TPTC30="+empresa+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tptemp = new TptempDTO();
				tptemp.setCodigoEmpresa(rs.getInt("TPTC30"));
				tptemp.setRazonSocial(rs.getString("TPTRAZ"));
				tptemp.setRut(rs.getInt("TPTRUT"));
				tptemp.setDv(rs.getString("TPTDIG"));
				tptemp.setDireccionCasaMatriz(rs.getString("TPTDI1"));
				tptemp.setRepresentante(rs.getString("TPTREP"));
				tptemp.setGiro(rs.getString("TPTD01"));
				tptemp.setComuna(recuperaComuna(rs.getInt("TPTC01"), rs.getInt("TPTC02"), rs.getInt("TPTC12")));
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
		
		
		return tptemp;
	}
	
	public TptempDTO recuperaEmpresaPorRut(int Rutempresa){
		TptempDTO tptemp = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTEMP " + 
        " Where TPTRUT="+Rutempresa+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tptemp = new TptempDTO();
				tptemp.setCodigoEmpresa(rs.getInt("TPTC30"));
				tptemp.setRazonSocial(rs.getString("TPTRAZ"));
				tptemp.setRut(rs.getInt("TPTRUT"));
				tptemp.setDv(rs.getString("TPTDIG"));
				tptemp.setDireccionCasaMatriz(rs.getString("TPTDI1"));
				tptemp.setRepresentante(rs.getString("TPTREP"));
				tptemp.setGiro(rs.getString("TPTD01"));
				tptemp.setComuna(recuperaComuna(rs.getInt("TPTC01"), rs.getInt("TPTC02"), rs.getInt("TPTC12")));
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
		
		
		return tptemp;
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
	
	public List recuperaEmpresa(){
		TptempDTO tptemp = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List empresa = new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTEMP " + 
        " FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tptemp = new TptempDTO();
				tptemp.setCodigoEmpresa(rs.getInt("TPTC30"));
				tptemp.setRazonSocial(rs.getString("TPTRAZ"));
				tptemp.setRut(rs.getInt("TPTRUT"));
				tptemp.setDv(rs.getString("TPTDIG"));
				tptemp.setDireccionCasaMatriz(rs.getString("TPTDI1"));
				tptemp.setRepresentante(rs.getString("TPTREP"));
				tptemp.setGiro(rs.getString("TPTD01"));
				tptemp.setComuna(recuperaComuna(rs.getInt("TPTC01"), rs.getInt("TPTC02"), rs.getInt("TPTC12")));
				empresa.add(tptemp);
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
		
		
		return empresa;
	}
	
}
