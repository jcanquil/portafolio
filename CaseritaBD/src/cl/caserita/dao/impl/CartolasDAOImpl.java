package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CartolasDAO;
import cl.caserita.dto.CajasDTO;
import cl.caserita.dto.CartolasDTO;

public class CartolasDAOImpl implements CartolasDAO {

	private  static Logger log = Logger.getLogger(CartolasDAOImpl.class);

private Connection conn;
	
	public CartolasDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List listaCartolasCheque(){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List cartola = new ArrayList();
		CartolasDTO cartolas = null;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CARTOLA " + 
        " WHERE  F7 LIKE '%CHEQUE%' " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 cartolas = new CartolasDTO();
				 cartolas.setCorrelativoCartola(rs.getInt("F1"));
				 cartolas.setTipo(rs.getString("F2"));
				 cartolas.setBodega(rs.getString("F3"));
				 cartolas.setCaja(rs.getString("F4"));
				 cartolas.setUsuario(rs.getString("F5"));
				 cartolas.setTransaccion(rs.getString("F6"));
				 cartolas.setDescripcion(rs.getString("F7"));
				 cartolas.setFecha(rs.getString("F8"));
				 cartolas.setRut(rs.getString("F9"));
				 cartolas.setNombre("F10");
				 cartolas.setGlosa(rs.getString("F11"));
				 cartolas.setNumDocumento(rs.getString("F12"));
				 cartolas.setBanco(rs.getString("F13"));
				 cartolas.setCheque(rs.getString("F14"));
				 cartolas.setFechaVencimiento(rs.getString("F15"));
				 cartolas.setCuentacte(rs.getString("F16"));
				 cartolas.setCodDocumento(rs.getString("F17"));
				 cartolas.setDocumento(rs.getString("F18"));
				 cartolas.setDeposito(rs.getString("F19"));
				 cartolas.setFechaDeposito(rs.getString("F20"));
				 cartolas.setBcoDeposito(rs.getString("F21"));
				 cartolas.setCuentaDeposito(rs.getString("F22"));
				 cartolas.setEgreso(rs.getString("F23"));
				 cartolas.setIngreso(rs.getString("F24"));
				 cartolas.setValorAbsoluto(rs.getString("F25"));
				 cartolas.setConciliacion(rs.getString("F26"));
				 cartolas.setObservacion(rs.getString("F27"));
				 cartolas.setSucursalHomologada(rs.getString("F28"));
				 cartola.add(cartolas);
				
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
		
		return cartola;
		
	}
	
	public List listaCartolasEfectivo(){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List cartola = new ArrayList();
		CartolasDTO cartolas = null;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CARTOLA " + 
        " WHERE  F7 LIKE '%EFECTIVO%' " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 cartolas = new CartolasDTO();
				 cartolas.setCorrelativoCartola(rs.getInt("F1"));
				 cartolas.setTipo(rs.getString("F2"));
				 cartolas.setBodega(rs.getString("F3"));
				 cartolas.setCaja(rs.getString("F4"));
				 cartolas.setUsuario(rs.getString("F5"));
				 cartolas.setTransaccion(rs.getString("F6"));
				 cartolas.setDescripcion(rs.getString("F7"));
				 cartolas.setFecha(rs.getString("F8"));
				 cartolas.setRut(rs.getString("F9"));
				 cartolas.setNombre("F10");
				 cartolas.setGlosa(rs.getString("F11"));
				 String numero = rs.getString("F12");
				 int index = numero.length();
				 index = index-4;
				 if (numero.length()>=4){
					 cartolas.setNumDocumento(numero.substring(index, numero.length()));

				 }else{
					 cartolas.setNumDocumento(rs.getString("F12"));
				 }
				// cartolas.setNumDocumento(rs.getString("F12"));
				 cartolas.setBanco(rs.getString("F13"));
				 cartolas.setCheque(rs.getString("F14"));
				 cartolas.setFechaVencimiento(rs.getString("F15"));
				 cartolas.setCuentacte(rs.getString("F16"));
				 cartolas.setCodDocumento(rs.getString("F17"));
				 cartolas.setDocumento(rs.getString("F18"));
				 cartolas.setDeposito(rs.getString("F19"));
				 cartolas.setFechaDeposito(rs.getString("F20"));
				 cartolas.setBcoDeposito(rs.getString("F21"));
				 cartolas.setCuentaDeposito(rs.getString("F22"));
				 cartolas.setEgreso(rs.getString("F23"));
				 cartolas.setIngreso(rs.getString("F24"));
				 cartolas.setValorAbsoluto(rs.getString("F25"));
				 cartolas.setConciliacion(rs.getString("F26"));
				 cartolas.setObservacion(rs.getString("F27"));
				 cartolas.setSucursalHomologada(rs.getString("F28"));
				 cartola.add(cartolas);
				
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
		
		return cartola;
		
	}
	
	public int actualizaConcilia(int correlativo, int correlativoConciliado){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARTOLA " + 
        " SET F29="+correlativoConciliado+" Where f1="+correlativo+"   " ;
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
	
	public int actualizaConciliaParcial(int correlativo, int correlativoConciliado){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARTOLA " + 
        " SET F30="+correlativoConciliado+" Where f1="+correlativo+"   " ;
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
