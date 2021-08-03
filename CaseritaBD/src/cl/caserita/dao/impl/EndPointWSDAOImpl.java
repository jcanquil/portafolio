package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.EndPointWSDAO;
import cl.caserita.dto.EndPointWSDTO;
import cl.caserita.dto.ExdtraDTO;

public class EndPointWSDAOImpl implements EndPointWSDAO{

	private static Logger log = Logger.getLogger(ExdtraDAOImpl.class);
	private Connection conn;
	
	public EndPointWSDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public String buscaEndPoint(){
		EndPointWSDTO endPoint = null;
		String endPointURL="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.ENDPWS " + 
        " Where ESTENDP='A' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				endPoint = new EndPointWSDTO();
				endPoint.setIpServidorWS(rs.getString("IPSERWS"));
				endPoint.setEndPointWS(rs.getString("ENDPWS"));
				endPoint.setEstadoEndPointWS(rs.getString("ESTENDP"));
				endPointURL = endPoint.getEndPointWS().trim();
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
		return endPointURL;
	}
	
}
