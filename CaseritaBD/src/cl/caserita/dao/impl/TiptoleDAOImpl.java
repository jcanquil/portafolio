package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TiptoleDAO;
import cl.caserita.dto.TiptoleDTO;

public class TiptoleDAOImpl implements TiptoleDAO{
	private static Logger log = Logger.getLogger(TiptoleDAOImpl.class);
	private Connection conn;
	
	public TiptoleDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public TiptoleDTO recuperaTolerancia(int codigo){
		TiptoleDTO tiptole = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
				" from CASEDAT.TIPTOLE " + 
				" Where COTPTO="+codigo+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			log.info("SQL TOLERANCIA RECEPCION " + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tiptole = new TiptoleDTO();
				
				tiptole.setCodigo(rs.getInt("COTPTO"));
				tiptole.setDescripcion(rs.getString("DETPTO"));
				tiptole.setTolerancia(rs.getInt("MNTTOL"));
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
		return tiptole;
	}
}
