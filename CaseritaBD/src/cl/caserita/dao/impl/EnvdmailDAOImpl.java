package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.EnvdmailDAO;
import cl.caserita.dto.EndPointWSDTO;
import cl.caserita.dto.EnvdmailDTO;

public class EnvdmailDAOImpl implements EnvdmailDAO {

	private static Logger log = Logger.getLogger(EnvdmailDAOImpl.class);
	private Connection conn;
	
	public EnvdmailDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public EnvdmailDTO rescataDocumentos(int empresa, int codigo, int vendedor){
		EndPointWSDTO endPoint = null;
		String endPointURL="";
		EnvdmailDTO dto = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.ENVDMAIL  " + 
        " Where AA12A="+empresa+" AND CLCCO2="+codigo+" FOR READ ONLY" ;
		log.info("SQL :"+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dto = new EnvdmailDTO();
				dto.setCodigoEmpresa(rs.getInt("AA12A"));
				dto.setCodigoDocumento(rs.getInt("CLCCO2"));
				dto.setMail(recuperaMail(vendedor));
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
		return dto;
	}
	
	public String recuperaMail(int vendedor){
		String mail ="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMOTV  " + 
        " Where EXMC42="+vendedor+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mail=rs.getString("EXMEMA");
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
		log.info("mail :"+mail);
		return mail;
	}
	
	
}
