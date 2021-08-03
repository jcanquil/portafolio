package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConsolidaasnDAO;
import cl.caserita.dto.ConnoidcDTO;
import cl.caserita.dto.ConsolidaasnDTO;
import cl.caserita.dto.VecmarDTO;

public class ConsolidaasnDAOImpl implements ConsolidaasnDAO{

	private static Logger log = Logger.getLogger(ConsolidaasnDAOImpl.class);

private Connection conn;
	
	public ConsolidaasnDAOImpl(Connection conn){
		this.conn=conn;
	}

	public ConsolidaasnDTO recuperaConsolidado(int empresa, int carguio, int bodega, int articulo){
		ConsolidaasnDTO consolidaasnDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CARGCONW " + 
        " Where CACEMP='"+empresa+"' AND NUMCAR="+carguio+" AND CAMCO1="+bodega+" AND CLDCOD="+articulo+" AND TIPCAR='C' FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				consolidaasnDTO = new ConsolidaasnDTO();
				consolidaasnDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				consolidaasnDTO.setBodega(rs.getInt("CAMCO1"));
				consolidaasnDTO.setPatente(rs.getString("VECPAT"));
				consolidaasnDTO.setCantidad(rs.getInt("CLDCAN"));
				consolidaasnDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				consolidaasnDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
				consolidaasnDTO.setDvArticulo(rs.getString("CONDI3").trim());
				consolidaasnDTO.setPrecioBruto(rs.getDouble("CONPRE"));
				consolidaasnDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return consolidaasnDTO;
	}
	
	public int actualizaConsolidado(ConsolidaasnDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.CARGCONW " + 
        " SET  CNTCONF="+dto.getCantidadConfirmada()+" , CNTDIF="+dto.getCantidadDiferencia()+" Where CACEMP='"+dto.getCodigoEmpresa()+"' AND NUMCAR="+dto.getNumeroCarguio()+" AND CAMCO1="+dto.getBodega()+" AND CLDCOD="+dto.getCodigoArticulo()+"   " ;
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	
	public List recuperaConsolidadoCompleto(int empresa, int carguio, int bodega){
		ConsolidaasnDTO consolidaasnDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CARGCONW " + 
        " Where CACEMP='"+empresa+"' AND NUMCAR="+carguio+" AND CAMCO1="+bodega+"  AND TIPCAR='C' FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				consolidaasnDTO = new ConsolidaasnDTO();
				consolidaasnDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				consolidaasnDTO.setBodega(rs.getInt("CAMCO1"));
				consolidaasnDTO.setPatente(rs.getString("VECPAT"));
				consolidaasnDTO.setCantidad(rs.getInt("CLDCAN"));
				consolidaasnDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				consolidaasnDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
				consolidaasnDTO.setDvArticulo(rs.getString("CONDI3").trim());
				consolidaasnDTO.setPrecioBruto(rs.getDouble("CONPRE"));
				consolidaasnDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
				consolidaasnDTO.setCantidadDiferencia(rs.getInt("CNTDIF"));
				consolidaasnDTO.setCantidadConfirmada(rs.getInt("CNTCONF"));
				lista.add(consolidaasnDTO);
			}
		}catch(Exception e){
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
}
