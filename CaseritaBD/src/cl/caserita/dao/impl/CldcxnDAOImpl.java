package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.CldcxnDTO;

public class CldcxnDAOImpl {
	private  static Logger log = Logger.getLogger(CldcxnDAOImpl.class);

	private Connection conn;
	
	public CldcxnDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public List recuperaClddia(int empresa, int fecha){
		CldcxnDTO cldcxnDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CLDCXN " + 
        " Where CLDDEM="+empresa+" AND CLDF02="+fecha+"  FOR READ ONLY" ;
		List clcdia = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cldcxnDTO = new CldcxnDTO();
				cldcxnDTO.setCodigoEmpresa(rs.getInt(""));
				cldcxnDTO.setNumeroCheque(rs.getInt(""));
				
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
		return clcdia;
	}
}
