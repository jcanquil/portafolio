package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TpacorDAO;

public class TpacorDAOImpl implements TpacorDAO{

	private static Logger log = Logger.getLogger(TpacorDAOImpl.class);
	private Connection conn;
	
	public TpacorDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int recupeCorrelativo(int bodega, int documento){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.TPACOR " + 
        " WHERE TPACO2="+bodega+" and TPACO3="+documento+" FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//log.info("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				numAtencion = rs.getInt("TPANUM");
				int actuNum=numAtencion+1;
				
				actualizaCorrelativo(bodega, documento, actuNum);
				
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
		
		return numAtencion;
		
	}
	
	public int actualizaCorrelativo(int bodega, int documento, int numero){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.TPACOR " + 
        " SET TPANUM="+numero+" Where TPACO2="+bodega+" and TPACO3="+documento+"  " ;
		//log.info("SQL ACTUALIZA CAMTRA"+ sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			pstmt.executeUpdate();
			actualiza=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return actualiza;
	}
}
