package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmodcDAO;
import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.ExmodcDTO;

public class ExmodcDAOImpl implements ExmodcDAO{

	private static Logger log = Logger.getLogger(ExmodcDAOImpl.class);

	private Connection conn;
	
	public ExmodcDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ExmodcDTO buscaOrden(int numeroOrden, int rut, String digito){
		ExmodcDTO exmodcDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.EXMODC " + 
        " Where EXMNUM="+numeroOrden+" AND EXMRU1="+rut+" AND EXMDI4='"+digito+"' FOR READ ONLY " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmodcDTO = new ExmodcDTO();
				exmodcDTO.setNumeroOrden(rs.getInt("EXMNUM"));
				exmodcDTO.setRutProveedor(rs.getInt("EXMRU1"));
				exmodcDTO.setDigito(rs.getString("EXMDI4"));
				exmodcDTO.setTotalOrden(rs.getInt("EXMTOT"));
				exmodcDTO.setEstadoOrden(rs.getString("EXMES3"));
				
				
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
		
		return exmodcDTO;
		
	}
	
	
	public ExmodcDTO buscaCabOrden(int empresa, int numeroOrden){
		ExmodcDTO exmodcDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerExmodc="Select * "+
        " from CASEDAT.EXMODC c"+ 
		" INNER JOIN CASEDAT.EXMOADI e"+
        " ON c.EXMEMP=e.EXMEMP AND c.EXMNUM=e.EXMNUM"+
        " Where c.EXMEMP="+empresa+" AND c.EXMNUM="+numeroOrden+" FOR READ ONLY" ;
		List exmodc = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerExmodc);
			
			log.info("RECUPERA EXMODC" + sqlObtenerExmodc);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmodcDTO = new ExmodcDTO();
				exmodcDTO.setNumeroOrden(rs.getInt("EXMNUM"));
				exmodcDTO.setBodegaOrigen(rs.getInt("EXMBOD"));
				exmodcDTO.setCodEmpresa(rs.getInt("EXMEMP"));
				exmodcDTO.setRutProveedor(rs.getInt("EXMRU1"));
				exmodcDTO.setDigito(rs.getString("EXMDI4"));
				exmodcDTO.setTotalOrden(rs.getInt("EXMTOT"));
				exmodcDTO.setEstadoOrden(rs.getString("EXMES3"));
				exmodcDTO.setFechaOrden(rs.getInt("EXMF01"));
				exmodcDTO.setEstadoInvOrden(rs.getString("CODEIN"));
				exmodcDTO.setFechaOrden(rs.getInt("EXMF01"));
				exmodcDTO.setEstadoInvOrden(rs.getString("CODEIN"));
				exmodcDTO.setTipoenvioOrden(rs.getString("EXMTIP"));
				exmodcDTO.setTipoOrden(rs.getString("EXMTI2"));
				exmodcDTO.setOrigenOrden(rs.getString("EXMORI"));
				exmodcDTO.setContactoOrden(rs.getString("EXMC19"));
				exmodcDTO.setObservacionOrden(rs.getString("EXMOBS"));
				exmodcDTO.setFormaPago(rs.getInt("EXMC17"));
				exmodcDTO.setCodigoUsuarioOrden(rs.getString("EXMC20"));
				exmodcDTO.setFechaOrden(rs.getInt("EXMF01"));
				exmodcDTO.setFechaEmision(rs.getInt("EXMF04"));
				exmodcDTO.setFechaActual(rs.getInt("EXMF06"));
				
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
		
		return exmodcDTO;
		
	}	
	
	public int actualizarCabecera(int codEmp, int numOC, int codBodega, String estadoOC){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE CASEDAT.EXMODC SET exmes3 = '"+estadoOC+"'"+
			" WHERE exmemp = "+codEmp+
			" AND exmnum = "+numOC+
			" AND exmbod = "+codBodega;
		
		log.info("ACTUALIZA ESTADO CABECERA OC " + sqlObtenerVecmar);
		
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
