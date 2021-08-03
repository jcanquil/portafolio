package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ChoftranDAO;
import cl.caserita.dto.ChoftranDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.VecmarDTO;

public class ChoftranDAOImpl implements ChoftranDAO{

	private  static Logger log = Logger.getLogger(ChoftranDAOImpl.class);

	private Connection conn;
	
	public ChoftranDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ChoftranDTO obtenerDatos(int rut, String dv){
		ChoftranDTO choftranDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CHOFTRAN " + 
        " Where CAMRUT="+rut+" AND EXMDI3='"+dv+"'  FOR READ ONLY" ;
		System.out.println("RECUPERA DATOS CHOFER" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
		
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				choftranDTO = new ChoftranDTO();
				choftranDTO.setRutEmpresa(rs.getInt("TPCRUT"));
				choftranDTO.setDvEmpresa(rs.getString("TPCDVT"));
				choftranDTO.setNomChofer(rs.getString("CAMNOM"));

				
				
			}
			//System.out.println("Despues de buscar en VECMAR");
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
		return choftranDTO;
	}
	
public ChoftranDTO obtenerDigitoRut(int rut){
		
		ChoftranDTO choftranDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerVecmar="Select EXMDI3 "+
        " from CASEDAT.CHOFTRAN " + 
        " Where CAMRUT="+rut+" GROUP BY EXMDI3 FOR READ ONLY" ;
		//log.info("SELECT DV RUT CHOFER : "+sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			//log.info("O K E Y      DV RUT CHOFER");
			while (rs.next()) {
				choftranDTO = new ChoftranDTO();
				choftranDTO.setDvChofer(rs.getString("EXMDI3"));
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
		return choftranDTO;
	}

}
