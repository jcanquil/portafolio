package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConnoidcDAO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.ConnoidcDTO;

public class ConnoidcDAOImpl implements ConnoidcDAO{
	
	private static Logger log = Logger.getLogger(ConnoidcDAOImpl.class);

	private Connection conn;
	
	public ConnoidcDAOImpl(Connection conn){
		this.conn=conn;
	}

	public List recuperaImptoNC(String tipoNota, int numDoc, int fechaNota){
		ConnoidcDTO connoidc = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CONNOIDC " + 
        " Where CONTI4='"+tipoNota+"' AND CONNU4="+numDoc+" AND CONFE4="+fechaNota+" FOR READ ONLY" ;
		List connoi = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				connoidc = new ConnoidcDTO();
				connoidc.setTipoNota(rs.getString("CONTI4"));
				connoidc.setNumNota(rs.getInt("CONNU4"));
				connoidc.setFechaNota(rs.getInt("CONFE4"));
				connoidc.setCorrelativo(rs.getInt("CONCO9"));
				connoidc.setCodImpto(rs.getInt("CONCO8"));
				connoidc.setMontoImpto(rs.getDouble("AAK7A"));
				connoi.add(connoidc);
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
		return connoi;
	}
	}
	

