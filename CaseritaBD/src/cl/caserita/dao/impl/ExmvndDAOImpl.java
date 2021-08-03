package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmvndDAO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.ExmvndDTO;

public class ExmvndDAOImpl implements ExmvndDAO{
	private static Logger log = Logger.getLogger(ExmvndDAOImpl.class);

	private Connection conn;
	
	public ExmvndDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ExmvndDTO recuperaVendedor (int codVendedor){
		ExmvndDTO exmvndDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.EXMVND " + 
        " Where EXMC09="+codVendedor+" FOR READ ONLY" ;
		log.info("SQL EXMVND" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					exmvndDTO = new ExmvndDTO();
					exmvndDTO.setCodigoVendedor(rs.getInt("EXMC09"));
					exmvndDTO.setNombreVendedor(rs.getString("EXMNO1"));
					exmvndDTO.setCodigoTipoVendedor(rs.getInt("EXMC10"));
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return exmvndDTO;
	}
}
