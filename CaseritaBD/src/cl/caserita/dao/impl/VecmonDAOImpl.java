package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VecmonDAO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VecmonDTO;

public class VecmonDAOImpl implements VecmonDAO{

	private static Logger log = Logger.getLogger(VecmonDAOImpl.class);
	private Connection conn;
	
	public VecmonDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtenerDatosVecmon (int empresa, int tipoMovto){
		List lista = new ArrayList();
		VecmonDTO vecmonDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.VECMON" + 
        " Where VENEMP="+empresa+" AND VENCOD="+tipoMovto+" AND PROCES=0 FOR READ ONLY" ;
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmonDTO = new VecmonDTO();
				vecmonDTO.setCodigoEmpresa(rs.getInt("VENEMP"));
				vecmonDTO.setCodigoTipoMovimiento(rs.getInt("VENCOD"));
				vecmonDTO.setFechaMovimiento(rs.getInt("VENFEC"));
				vecmonDTO.setNumeroDocumento(rs.getInt("VENNUM"));
				vecmonDTO.setHoraMovimiento(rs.getInt("HORREG"));
				vecmonDTO.setProcesado(rs.getInt("PROCES"));
				lista.add(vecmonDTO);
				
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
		return lista;
	}
	
	
	public void actualizaVecmon(int empresa, int codigo, int fecha, int numero){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMON " + 
        " set PROCES=1 Where VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		
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
	}
	
	
	
}
