package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TptcomDAO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptcomDTO;

public class TptcomDAOImpl implements TptcomDAO{

	private static Logger log = Logger.getLogger(TptcomDAOImpl.class);
	private Connection conn;
	
	public TptcomDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List allComuna(){
		TptcomDTO tptcom = null;
		List comuna = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTCOM " + 
        " FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tptcom = new TptcomDTO();
				tptcom.setCodigoRegion(rs.getInt("TPTC19"));
				tptcom.setCodigoComuna(rs.getInt("TPTC22"));
				tptcom.setDescripcionComuna(rs.getString("TPTD20"));
				comuna.add(tptcom);
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
		return comuna;
	}
		public TptcomDTO Comuna(int region, int comuna){
			TptcomDTO tptcom = null;
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			String sqlObtenerVecmar="Select * "+
	        " from CASEDAT.TPTCOM WHERE TPTC19="+region+" AND TPTC22="+comuna+" " + 
	        " FOR READ ONLY" ;
			    
			try{
				pstmt = conn.prepareStatement(sqlObtenerVecmar);
				//pstmt.setString(1, origen);
			
				
				log.info("SQL CLIENTE" + sqlObtenerVecmar);
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					tptcom = new TptcomDTO();
					tptcom.setCodigoRegion(rs.getInt("TPTC19"));
					tptcom.setCodigoComuna(rs.getInt("TPTC22"));
					tptcom.setDescripcionComuna(rs.getString("TPTD20"));
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
		
		
		return tptcom;
	}
}
