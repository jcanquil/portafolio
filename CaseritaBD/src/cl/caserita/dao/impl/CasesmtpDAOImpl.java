package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CasesmtpDAO;
import cl.caserita.dto.CasesmtpDTO;
import cl.caserita.dto.ClcdiaDTO;

public class CasesmtpDAOImpl implements CasesmtpDAO{

	private  static Logger log = Logger.getLogger(CasesmtpDAOImpl.class);

private Connection conn;
	
	public CasesmtpDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public CasesmtpDTO obtieneCorreos(String codigo){
		CasesmtpDTO casesmtpDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CASESMTP " + 
        " Where AAZ5A="+codigo+"  FOR READ ONLY" ;
		List clcdia = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
					casesmtpDTO = new CasesmtpDTO();
					casesmtpDTO.setCodigoSmtp(rs.getString("AAZ5A"));
					casesmtpDTO.setDescripcionSmtp(rs.getString("AAZ6A"));
					casesmtpDTO.setCorreo(rs.getString("AAZAA"));
					casesmtpDTO.setCorreo2(rs.getString("COROR2"));
					casesmtpDTO.setCorreo3(rs.getString("COROR3"));
					casesmtpDTO.setCorreo4(rs.getString("COROR4"));
					casesmtpDTO.setCorreo5(rs.getString("COROR5"));
					casesmtpDTO.setCorreo6(rs.getString("COROR6"));
					casesmtpDTO.setCorreo7(rs.getString("COROR7"));
					casesmtpDTO.setCorreo8(rs.getString("COROR8"));
				
				
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
		return casesmtpDTO;
	}
}
