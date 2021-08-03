package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TpttvdDAO;
import cl.caserita.dto.TptimpDTO;
import cl.caserita.dto.TpttvdDTO;

public class TpttvdDAOImpl implements TpttvdDAO{

	private static Logger log = Logger.getLogger(TpttvdDAOImpl.class);
	private Connection conn;
	
	public TpttvdDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public TpttvdDTO recuperaTipoVendedor(int codigoTipoVendedor){
		TpttvdDTO tpttvd = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
				" from CASEDAT.TPTTVD " + 
				" Where TPTC03="+codigoTipoVendedor+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			//log.info("SQL IMPUESTO " + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tpttvd = new TpttvdDTO();
				
				tpttvd.setCodigoTipoVendedor(rs.getInt("TPTC03"));
				tpttvd.setDescripcionTipoVendedor(rs.getString("TPTD03"));
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
		return tpttvd;
	}
}
