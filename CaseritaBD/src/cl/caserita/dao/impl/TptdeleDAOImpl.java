package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TptdeleDAO;

public class TptdeleDAOImpl implements TptdeleDAO{

	private static Logger log = Logger.getLogger(CldmcoDAOImpl.class);
	private Connection conn;
	
	public TptdeleDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int buscaDocumentoElectronico(int documen){
		
		int docum=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerTptdele="Select * "+
        " from CASEDAT.TPTDELE " + 
        " Where TPACO1="+documen+"  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerTptdele);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL VECMAR" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docum = rs.getInt("CODEVT");
				
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
		return docum;
	}
	
	public int buscaDocumentoElectronicoPP(int documen){
		
		int docum=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerTptdele="Select * "+
        " from CASEDAT.TPTDELE " + 
        " Where CODEVT="+documen+"  fetch first 1 rows only" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerTptdele);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL VECMAR" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docum = rs.getInt("TPACO1");
				
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
		return docum;
	}

public int buscaDocumentoElectronicoCompras(int documen){
		
		int docum=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerTptdele="Select * "+
        " from CASEDAT.TPTDELE " + 
        " Where TPACO1="+documen+"  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerTptdele);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL TPTDELE" + sqlObtenerTptdele);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docum = rs.getInt("CODECO");
				
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
		return docum;
	}
}
