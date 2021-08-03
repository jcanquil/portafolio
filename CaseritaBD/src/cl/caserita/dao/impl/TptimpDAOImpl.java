package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TptimpDAO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.TptimpDTO;

public class TptimpDAOImpl implements TptimpDAO{
	
	private static Logger log = Logger.getLogger(TptempDAOImpl.class);
	private Connection conn;
	
	public TptimpDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public TptimpDTO recuperaValorImpuesto(int codImpto){
		TptimpDTO tptimp = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
				" from CASEDAT.TPTIMP " + 
				" Where TPTC32="+codImpto+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			log.info("SQL IMPUESTO " + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tptimp = new TptimpDTO();
				
				tptimp.setCodImpuesto(rs.getInt("TPTC32"));
				tptimp.setDescripcion(rs.getString("TPTD26"));
				tptimp.setValorImpuesto(rs.getDouble("TPTVA4"));
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
		return tptimp;
	}
	
	
}
