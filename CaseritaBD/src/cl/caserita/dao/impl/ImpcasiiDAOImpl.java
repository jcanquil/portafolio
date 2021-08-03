package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ImpcasiiDAO;
import cl.caserita.dto.ImpcasiiDTO;
import cl.caserita.dto.LibtpdDTO;

public class ImpcasiiDAOImpl implements ImpcasiiDAO{

	private static Logger log = Logger.getLogger(ImpcasiiDAOImpl.class);

	private Connection conn;
	
	public ImpcasiiDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public HashMap obtieneImpuestoSII(String tipoImpuesto){
		HashMap<Integer,Integer> impuestos = new HashMap<Integer,Integer>();
		
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ImpcasiiDTO imp =null;
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.IMPCASII " + 
        " Where TIPIMPS='"+tipoImpuesto+"' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				imp = new ImpcasiiDTO();
				imp.setTipoImpuesto(rs.getString("TIPIMPS"));
				imp.setImpuestoCase(rs.getInt("CODIMP1"));
				imp.setImpuestoSii(rs.getInt("CLDCO8"));
				impuestos.put(imp.getImpuestoCase(), imp.getImpuestoSii());
				
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
		return impuestos;
	}
	
	public HashMap obtieneImpuestoCase(String tipoImpuesto){
		HashMap<Integer,Integer> impuestos = new HashMap<Integer,Integer>();
		
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ImpcasiiDTO imp =null;
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.IMPCASII " + 
        " Where TIPIMPS='"+tipoImpuesto+"' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				imp = new ImpcasiiDTO();
				imp.setTipoImpuesto(rs.getString("TIPIMPS"));
				imp.setImpuestoCase(rs.getInt("CODIMP1"));
				imp.setImpuestoSii(rs.getInt("CLDCO8"));
				impuestos.put(imp.getImpuestoSii(), imp.getImpuestoCase());
				
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
		return impuestos;
	}
}
