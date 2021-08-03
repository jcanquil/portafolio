package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.BdgcorrDAO;
import cl.caserita.dto.CamtraDTO;



public class BdgCorrDAOImpl implements BdgcorrDAO{

	private  static Logger log = Logger.getLogger(BdgCorrDAOImpl.class);

	private Connection conn;
	
	public BdgCorrDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int recupeNumAtencion(int empresa, int bodega){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.BDGCOR " + 
        " WHERE EMPATE="+empresa+" and TPTC18="+bodega+" FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//System.out.println("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				numAtencion = rs.getInt("BDGCORR");
				int actuNum=numAtencion;
				if (numAtencion==99){
					actuNum=0;
				}
				actualizaCamtra(empresa, bodega, actuNum);
				
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
	
	public int actualizaCamtra(int empresa, int bodega, int numero){
		int actualiza=0;
		PreparedStatement pstmt =null;
		numero=numero + 1;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.BDGCOR " + 
        " SET BDGCORR="+numero+" Where EMPATE="+empresa+" and TPTC18="+bodega+"  " ;
		//System.out.println("SQL ACTUALIZA CAMTRA"+ sqlObtenerCldmco);
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
	public int generaNumeroDocAtencion(int empresa, int numeroDocumento, int bodega, int numAtencion){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.NOTCORRE " + 
        " (EMPATE,CLCNUM,TPTC18,BDGCORR) VALUES("+empresa+","+numeroDocumento+","+bodega+","+numAtencion+") " ;
		//System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
}
