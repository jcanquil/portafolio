package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CajasDAO;
import cl.caserita.dto.CajasDTO;
import cl.caserita.dto.CartolasDTO;

public class CajasDAOImpl implements CajasDAO{

	private  static Logger log = Logger.getLogger(CajasDAOImpl.class);

private Connection conn;
	
	public CajasDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List listaCaja(){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List caja = new ArrayList();
		CajasDTO cajas = new CajasDTO();

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAJAS " + 
        " ORDER BY F1" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 cajas = new CajasDTO();
				 cajas.setCorrelativoCaja(rs.getInt("F1"));
				 cajas.setTipo(rs.getString("F2"));
				 cajas.setBodega(rs.getString("F3"));
				 cajas.setCaja(rs.getString("F4"));
				 cajas.setUsuario(rs.getString("F5"));
				 cajas.setTransaccion(rs.getString("F6"));
				 cajas.setDescripcion(rs.getString("F7"));
				 cajas.setFecha(rs.getString("F8"));
				 cajas.setRut(rs.getString("F9"));
				 cajas.setNombreCliente("F10");
				 cajas.setGlosa(rs.getString("F11"));
				 cajas.setNumeroDocumento(rs.getString("F12"));
				 cajas.setBanco(rs.getString("F13"));
				 cajas.setCheque(rs.getString("F14"));
				 cajas.setFechaVencimiento(rs.getString("F15"));
				 cajas.setCuentaCte(rs.getString("F16"));
				 cajas.setCodDocumento(rs.getString("F17"));
				 cajas.setDescripcionDcto(rs.getString("F18"));
				 cajas.setDeposito(rs.getString("F19"));
				 cajas.setFechaDeposito(rs.getString("F20"));
				 cajas.setBancoDeposito(rs.getString("F21"));
				 cajas.setCuentaDeposito(rs.getString("F22"));
				 cajas.setEgreso(rs.getString("F23"));
				 cajas.setIngreso(rs.getString("F24"));
				 cajas.setAbsoluto(rs.getString("F25"));
				 cajas.setConciliado(rs.getString("F26"));
				 cajas.setObservacion(rs.getString("F27"));
				 cajas.setSucursalHomologada(rs.getString("F28"));
				 caja.add(cajas);
				
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
		
		return caja;
		
	}
	
	public int actualizaConcilia(int correlativo, int correlativoConciliado){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CAJAS " + 
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
        " CASEDAT.CAJAS " + 
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
	public int buscaConciliadoCheque(String sucursal, int numeroCheque, String monto){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int id=0;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAJAS " + 
        " WHERE F28='"+sucursal+"' AND F14="+numeroCheque+" AND F23='"+monto.trim()+"' AND F7 LIKE '%CHEQUE%' " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			log.info("Query CAJA:"+sqlObtenerCamtra);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 id = rs.getInt("F1");
				 log.info("id:"+id);
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
		
		return id;
		
	}
	
	public int buscaConciliadoEfectivo(String sucursal, int numeroCheque, String monto){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int id=0;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAJAS " + 
        " WHERE F28='"+sucursal+"' AND F14="+numeroCheque+" AND F23='"+monto.trim()+"' AND F7 LIKE '%EFECTIVO%' " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			log.info("Query CAJA:"+sqlObtenerCamtra);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 id = rs.getInt("F1");
				 log.info("id:"+id);
				 break;
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
		
		return id;
		
	}

	
	public int buscaConciliadoParcialCheque(String sucursal, String monto){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int id=0;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAJAS " + 
        " WHERE F28='"+sucursal+"' AND F23='"+monto.trim()+"' AND F7 LIKE '%CHEQUE%' " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			log.info("Query CAJA:"+sqlObtenerCamtra);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 id = rs.getInt("F1");
				 log.info("id:"+id);
				 break;
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
		
		return id;
		
	}
	
	public int buscaConciliadoParcialEfectivo(String sucursal, String monto){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int id=0;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAJAS " + 
        " WHERE F28='"+sucursal+"' AND F23='"+monto.trim()+"' AND F7 LIKE '%EFECTIVO%' " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			log.info("Query CAJA:"+sqlObtenerCamtra);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				 id = rs.getInt("F1");
				 log.info("id:"+id);
				 break;
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
		
		return id;
		
	}
}
