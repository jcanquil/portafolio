package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.VecmarDTO;

public class CamtraDAOImpl implements CamtraDAO{

	private  static Logger log = Logger.getLogger(CamtraDAOImpl.class);

	private Connection conn;
	
	public CamtraDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtenerDatosCamtra(int empresa, int codigo, int fecha, int numero){
		CamtraDTO camtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAMTRA " + 
        " Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numero+" AND CAMFEC="+fecha+" FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				camtraDTO = new CamtraDTO();
				camtraDTO.setCodigoTipoMovto(rs.getInt("CAMCO2"));
				camtraDTO.setFechaDocumento(rs.getInt("CAMFEC"));
				camtraDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				camtraDTO.setCorrelativo(rs.getInt("CAMC12"));
				camtraDTO.setHoraEvento(rs.getInt("CAMHOR"));
				camtraDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				camtraDTO.setCodigoCaja(rs.getInt("CAMC02"));
				camtraDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				camtraDTO.setIngresoSalida(rs.getString("CAMDEB"));
				camtraDTO.setValorDocumento(rs.getInt("CAMVAL"));
				camtraDTO.setRutCliente(rs.getString("CAMRUT"));
				camtraDTO.setDvCliente(rs.getString("CAMDVR"));
				camtraDTO.setNombreCliente(rs.getString("CAMNOM"));
				camtraDTO.setCodigoDocumento(rs.getInt("CAMCO3"));
				camtraDTO.setNumeroBolfactura(rs.getInt("CAMNFA"));
				camtraDTO.setCodigoUsuario(rs.getString("CAMC01"));
				camtraDTO.setEstado(rs.getString("CAMES1"));
				camtraDTO.setRutaObjeto(rs.getString("CAMRUD"));				
				camtra.add(camtraDTO);
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
		
		return camtra;
		
	}
	
	public List obtenerDatosEstadoSII(int empresa, int fecha, int codDoc){
		CamtraDTO camtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAMTRA68 " + 
        " Where CAMEMP="+empresa+" AND CAMFEC>="+fecha+" AND CAMCO3="+codDoc+" AND CODSII IN ('2','4','P') FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				camtraDTO = new CamtraDTO();
				camtraDTO.setCodigoTipoMovto(rs.getInt("CAMCO2"));
				camtraDTO.setFechaDocumento(rs.getInt("CAMFEC"));
				camtraDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				camtraDTO.setCorrelativo(rs.getInt("CAMC12"));
				camtraDTO.setHoraEvento(rs.getInt("CAMHOR"));
				camtraDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				camtraDTO.setCodigoCaja(rs.getInt("CAMC02"));
				camtraDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				camtraDTO.setIngresoSalida(rs.getString("CAMDEB"));
				camtraDTO.setValorDocumento(rs.getInt("CAMVAL"));
				camtraDTO.setRutCliente(rs.getString("CAMRUT"));
				camtraDTO.setDvCliente(rs.getString("CAMDVR"));
				camtraDTO.setNombreCliente(rs.getString("CAMNOM"));
				camtraDTO.setCodigoDocumento(rs.getInt("CAMCO3"));
				camtraDTO.setNumeroBolfactura(rs.getInt("CAMNFA"));
				camtraDTO.setCodigoUsuario(rs.getString("CAMC01"));
				camtraDTO.setEstado(rs.getString("CAMES1"));
				camtraDTO.setRutaObjeto(rs.getString("CAMRUD"));				
				camtraDTO.setCodigoEmpresa(rs.getInt("CAMEMP"));
				camtra.add(camtraDTO);
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
		
		return camtra;
		
	}
	
	public List obtenerDatosEstadoRechazados(int empresa, int fecha, int codDoc){
		CamtraDTO camtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAMTRA68 " + 
        " Where CAMEMP="+empresa+" AND CAMFEC>="+fecha+" AND CAMCO3="+codDoc+" AND CODSII IN ('4') FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
	
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				camtraDTO = new CamtraDTO();
				camtraDTO.setCodigoTipoMovto(rs.getInt("CAMCO2"));
				camtraDTO.setFechaDocumento(rs.getInt("CAMFEC"));
				camtraDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				camtraDTO.setCorrelativo(rs.getInt("CAMC12"));
				camtraDTO.setHoraEvento(rs.getInt("CAMHOR"));
				camtraDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				camtraDTO.setCodigoCaja(rs.getInt("CAMC02"));
				camtraDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				camtraDTO.setIngresoSalida(rs.getString("CAMDEB"));
				camtraDTO.setValorDocumento(rs.getInt("CAMVAL"));
				camtraDTO.setRutCliente(rs.getString("CAMRUT"));
				camtraDTO.setDvCliente(rs.getString("CAMDVR"));
				camtraDTO.setNombreCliente(rs.getString("CAMNOM"));
				camtraDTO.setCodigoDocumento(rs.getInt("CAMCO3"));
				camtraDTO.setNumeroBolfactura(rs.getInt("CAMNFA"));
				camtraDTO.setCodigoUsuario(rs.getString("CAMC01"));
				camtraDTO.setEstado(rs.getString("CAMES1"));
				camtraDTO.setRutaObjeto(rs.getString("CAMRUD"));				
				camtraDTO.setCodigoEmpresa(rs.getInt("CAMEMP"));
				camtra.add(camtraDTO);
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
		
		return camtra;
		
	}
	public CamtraDTO obtenerDatosCamtraNC(int empresa, int codigo, int fecha, int numero){
		CamtraDTO camtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAMTRA " + 
        " Where CAMEMP="+empresa+" AND CAMCO3="+codigo+" AND CAMNFA="+numero+" AND CAMFEC="+fecha+" FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				camtraDTO = new CamtraDTO();
				camtraDTO.setCodigoTipoMovto(rs.getInt("CAMCO2"));
				camtraDTO.setFechaDocumento(rs.getInt("CAMFEC"));
				camtraDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				camtraDTO.setCorrelativo(rs.getInt("CAMC12"));
				camtraDTO.setHoraEvento(rs.getInt("CAMHOR"));
				camtraDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				camtraDTO.setCodigoCaja(rs.getInt("CAMC02"));
				camtraDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				camtraDTO.setIngresoSalida(rs.getString("CAMDEB"));
				camtraDTO.setValorDocumento(rs.getInt("CAMVAL"));
				camtraDTO.setRutCliente(rs.getString("CAMRUT"));
				camtraDTO.setDvCliente(rs.getString("CAMDVR"));
				camtraDTO.setNombreCliente(rs.getString("CAMNOM"));
				camtraDTO.setCodigoDocumento(rs.getInt("CAMCO3"));
				camtraDTO.setNumeroBolfactura(rs.getInt("CAMNFA"));
				camtraDTO.setCodigoUsuario(rs.getString("CAMC01"));
				camtraDTO.setEstado(rs.getString("CAMES1"));
				camtraDTO.setRutaObjeto(rs.getString("CAMRUD"));				
				
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
		
		return camtraDTO;
		
	}
	
	public CamtraDTO verificaFacturacion(int empresa, int codigo, int fecha, int numero){
		CamtraDTO camtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAMTRA " + 
        " Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numero+" AND CAMFEC="+fecha+" FOR READ ONLY" ;
		log.info("VALIDA DOCUMENTO CAMTRA:"+sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				camtraDTO = new CamtraDTO();
				camtraDTO.setCodigoTipoMovto(rs.getInt("CAMCO2"));
				camtraDTO.setFechaDocumento(rs.getInt("CAMFEC"));
				camtraDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				camtraDTO.setCorrelativo(rs.getInt("CAMC12"));
				camtraDTO.setHoraEvento(rs.getInt("CAMHOR"));
				camtraDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				camtraDTO.setCodigoCaja(rs.getInt("CAMC02"));
				camtraDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				camtraDTO.setIngresoSalida(rs.getString("CAMDEB"));
				camtraDTO.setValorDocumento(rs.getInt("CAMVAL"));
				camtraDTO.setRutCliente(rs.getString("CAMRUT"));
				camtraDTO.setDvCliente(rs.getString("CAMDVR"));
				camtraDTO.setNombreCliente(rs.getString("CAMNOM"));
				camtraDTO.setCodigoDocumento(rs.getInt("CAMCO3"));
				camtraDTO.setNumeroBolfactura(rs.getInt("CAMNFA"));
				camtraDTO.setCodigoUsuario(rs.getString("CAMC01"));
				camtraDTO.setEstado(rs.getString("CAMES1"));
				camtraDTO.setRutaObjeto(rs.getString("CAMRUD"));
				
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
		
		return camtraDTO;
		
	}
	
	
	
	public int actualizaCamtra(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, int numero, String usuario, String timbre, String estadoPP){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CAMTRA " + 
        " SET CAMNFA="+numero+" , CAMC01='"+usuario+"', CAMTIM='"+timbre+"', CODEPP='"+estadoPP+"' , CODSII='P' Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numeroDoc+" AND CAMFEC="+fecha+" AND CAMC12="+correlativo+" " ;
		log.info("SQL ACTUALIZA CAMTRA NC"+ sqlObtenerCldmco);
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
		pstmt=null;
		
		
		
		return actualiza;
	}
	
	public List obtenerDatosCamtraPendientes(int empresa, int codigo, int fechainicial, int fechaFinal){
		CamtraDTO camtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CAMTRA " + 
        " WHERE CAMEMP="+empresa+" AND CAMCO2=21 AND CAMFEC BETWEEN "+fechainicial+" AND "+fechaFinal+" AND CODSII='P' FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				camtraDTO = new CamtraDTO();
				camtraDTO.setCodigoTipoMovto(rs.getInt("CAMCO2"));
				camtraDTO.setFechaDocumento(rs.getInt("CAMFEC"));
				camtraDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				camtraDTO.setCorrelativo(rs.getInt("CAMC12"));
				camtraDTO.setHoraEvento(rs.getInt("CAMHOR"));
				camtraDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				camtraDTO.setCodigoCaja(rs.getInt("CAMC02"));
				camtraDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				camtraDTO.setIngresoSalida(rs.getString("CAMDEB"));
				camtraDTO.setValorDocumento(rs.getInt("CAMVAL"));
				camtraDTO.setRutCliente(rs.getString("CAMRUT"));
				camtraDTO.setDvCliente(rs.getString("CAMDVR"));
				camtraDTO.setNombreCliente(rs.getString("CAMNOM"));
				camtraDTO.setCodigoDocumento(rs.getInt("CAMCO3"));
				camtraDTO.setNumeroBolfactura(rs.getInt("CAMNFA"));
				camtraDTO.setCodigoUsuario(rs.getString("CAMC01"));
				camtraDTO.setEstado(rs.getString("CAMES1"));
				camtraDTO.setRutaObjeto(rs.getString("CAMRUD"));	
				camtraDTO.setEstadoPaperless(rs.getString("CODEPP"));
				camtraDTO.setEstadoSII(rs.getString("CODSII"));
				camtra.add(camtraDTO);
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
		
		return camtra;
		
	}
	public int actualizaEstadoCamtra(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, String estadoPP){
		int actualiza=0;
		PreparedStatement pstmt =null;
		 
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CAMTRA " + 
        " SET CODEPP='"+estadoPP+"' , CODSII='P' Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numeroDoc+" AND CAMFEC="+fecha+" AND CAMC12="+correlativo+" " ;
		log.info("SQL ACTUALIZA CAMTRA"+ sqlObtenerCldmco);
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
		pstmt=null;
		
		
		
		return actualiza;
	}
	
	public int actualizaEstadoCamtraSII(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, String estadoSII){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CAMTRA " + 
        " SET CODSII='"+estadoSII+"' Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numeroDoc+" AND CAMFEC="+fecha+" AND CAMC12="+correlativo+" " ;
	
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
		pstmt=null;
		
		
		
		return actualiza;
	}
	
	public int actualizaEstadoCamtraReenvio(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, String reenvio){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CAMTRA " + 
        " SET CAMOP1='"+reenvio+"' Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numeroDoc+" AND CAMFEC="+fecha+" AND CAMC12="+correlativo+" " ;
		
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
		pstmt=null;
		
		
		
		return actualiza;
	}
	
	public int actualizaEstadoPago(int empresa, int codigo, int numeroDoc, int fecha){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CAMTRA " + 
        " SET CAMES1='P' Where CAMEMP="+empresa+" AND CAMCO2="+codigo+" AND CAMNUM="+numeroDoc+" AND CAMFEC="+fecha+" " ;
	
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
		pstmt=null;
		
		
		
		return actualiza;
	}
}

