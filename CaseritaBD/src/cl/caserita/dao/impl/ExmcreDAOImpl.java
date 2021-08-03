package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmcreDAO;
import cl.caserita.dto.ExmcreDTO;

public class ExmcreDAOImpl implements ExmcreDAO{
	private static Logger log = Logger.getLogger(ExmcreDAOImpl.class);
	private Connection conn;
	
	public ExmcreDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ExmcreDTO recuperaCorrSolicitud(int empresa, int rutprov, String dvprov, int nrodocto, int fechasol){
		ExmcreDTO exmcre = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT MAX(EXMC22) AS CORR"+
				" from CASEDAT.EXMCRE " + 
				" Where EXMREM = "+empresa+
				" and EXMRU2 = "+rutprov+
				" and EXMDI5 = '"+dvprov+"'"+
				" and EXMNU2 = "+nrodocto+
				" and EXMTI1 = 'C'" +
				" and EXMC22 < 900 " +
				" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			log.info("SQL OBTIENE SOLICITUD DE NC " + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmcre = new ExmcreDTO();
				
				exmcre.setCorrelativo(rs.getInt("CORR"));
				
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
		return exmcre;
	}
	
}
