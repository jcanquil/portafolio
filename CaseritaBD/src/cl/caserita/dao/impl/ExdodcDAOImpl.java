package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExdodcDAO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdodcDTO;
//import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ExmodcDTO;

public class ExdodcDAOImpl implements ExdodcDAO {
	private static Logger log = Logger.getLogger(ExdodcDAOImpl.class);

	private Connection conn;
	
	public ExdodcDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List listaExdodc(int empresa, int numoc){
		ExdodcDTO detoc = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerExdodc="Select * "+
        " from CASEDAT.EXDODC " + 
        " Where EXDEMP="+empresa+" AND EXDNUM="+numoc+" FOR READ ONLY" ;
		List listadetoc = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdodc);
			
			//log.info("SQL" + sqlObtenerExdodc);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
					detoc = new ExdodcDTO();
					detoc.setNumLinea(rs.getInt("EXDLIN"));
					detoc.setStocLinea(rs.getInt("EXDST1"));
					detoc.setCodArticulo(rs.getInt("EXDCO3"));
					detoc.setDvArticulo(rs.getString("EXDDI3").trim());
					detoc.setFormato(rs.getString("EXDFOR").trim());
					listadetoc.add(detoc);
				
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
		return listadetoc;
	}
	
	public HashMap HashMapExdodc(int empresa, int numoc){
		ExdodcDTO detoc = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerExdodc="Select * "+
        " from CASEDAT.EXDODC E1, CASEDAT.EXMART E2 " + 
        " Where E1.EXDEMP="+empresa+" AND E1.EXDNUM="+numoc+" AND E1.EXDCO3=E2.EXMCOD AND E1.EXDDI3=E2.EXMDIG FOR READ ONLY" ;
		HashMap <Integer,ExdodcDTO> listadetoc = new HashMap<Integer,ExdodcDTO>();
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdodc);
			
			log.info("SQL" + sqlObtenerExdodc);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
					detoc = new ExdodcDTO();
					detoc.setNumLinea(rs.getInt("EXDLIN"));
					detoc.setStocLinea(rs.getInt("EXDST1"));
					detoc.setCodArticulo(rs.getInt("EXDCO3"));
					detoc.setPrecioBruto(rs.getDouble("EXDPRE"));
					detoc.setPrecioNeto(rs.getDouble("EXDPRN"));
					detoc.setMontoNeto(rs.getDouble("EXDMNT"));
					detoc.setMontoBruto(rs.getDouble("EXDMBT"));
					detoc.setDvArticulo(rs.getString("EXDDI3"));
					detoc.setDescripcionArticulo(rs.getString("EXMDES").trim());
					detoc.setFechaMaxEntrega(rs.getInt("EXDFE1"));
					detoc.setStockActual(rs.getDouble("EXDSTO"));
					detoc.setFormatoArt(rs.getString("EXDFOR"));
					detoc.setStockPedidoOC(rs.getDouble("EXDST1"));
					detoc.setStockPendienteOC(rs.getDouble("EXDSOT"));
					detoc.setStockRecepcionadoOC(rs.getDouble("EXDST2"));
					
					listadetoc.put(Integer.valueOf((detoc.getCodArticulo())),detoc);
				
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
		return listadetoc;
	}
	
	public ExdodcDTO buscaDetOrden(int empresa, int numeroOrden, int articulo){
		ExdodcDTO exdodcDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerExdodc=" SELECT * "+
        " FROM CASEDAT.EXDODC "+ 
		" WHERE exdemp = "+empresa+
		" AND exdnum = "+numeroOrden+
		" AND exdco3 = "+articulo +
		" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdodc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdodcDTO = new ExdodcDTO();
				exdodcDTO.setCodArticulo(rs.getInt("EXDCO3"));
				exdodcDTO.setDvArticulo(rs.getString("EXDDI3"));
				exdodcDTO.setPrecioBruto(rs.getInt("EXDPRN"));
				exdodcDTO.setPrecioNeto(rs.getInt("EXDPRE"));
				exdodcDTO.setFechaMaxEntrega(rs.getInt("EXDFE1"));
				exdodcDTO.setStockActual(rs.getDouble("EXDSTO"));
				exdodcDTO.setStockPedidoOC(rs.getDouble("EXDST1"));
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
		
		return exdodcDTO;
	}
	
	public int eliminaArticuloOrden(int empresa, int nrooc, int linea, int articulo){
		int res=0;
		PreparedStatement pstmt =null;
		
		//" AND exdlin = "+linea+
		
		String sqlObtenerVecmar="DELETE FROM " +
			" CASEDAT.EXDODC "+
			" WHERE exdemp = "+empresa+
			" AND exdnum = "+nrooc+
			" AND exdco3 = "+articulo;
			
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
	
	public int actualizarFormatoArticulo(int empresa, int nrooc, int linea, int articulo, double cantunid){
		int res=0;
		PreparedStatement pstmt =null;
		
		//" AND exdlin = "+linea+
		
		String sqlObtenerVecmar="UPDATE CASEDAT.EXDODC SET exdfor = 'U',"+
			" exdst1 = "+cantunid+","+
			" exdsot = "+cantunid+
			" WHERE exdemp = "+empresa+
			" AND exdnum = "+nrooc+
			" AND exdco3 = "+articulo;
			
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
	
	public ExdodcDTO recuperEstadoparaCab(int empresa, int numoc){
		ExdodcDTO exdodcDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		//Busco si quedan linea pendientes por recepcionar
		String sqlObtenerExdodc=" SELECT * "+
        " FROM CASEDAT.EXDODC "+ 
		" WHERE exdemp = "+empresa+
		" AND exdnum = "+numoc+
		" AND exdest = 'P'"+
		" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdodc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdodcDTO = new ExdodcDTO();
				exdodcDTO.setCodArticulo(rs.getInt("EXDCO3"));
				exdodcDTO.setDvArticulo(rs.getString("EXDDI3"));
				exdodcDTO.setEstadoLinea("EXDEST");
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
		return exdodcDTO;
	}
	
	public ExdodcDTO buscaDetalleOrden(int empresa, int numeroOrden, int articulo){
		ExdodcDTO exdodcDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerExdodc=" SELECT * "+
        " FROM CASEDAT.EXDODC "+ 
		" WHERE exdemp = "+empresa+
		" AND exdnum = "+numeroOrden+
		" AND exdco3 = "+articulo +
		" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdodc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdodcDTO = new ExdodcDTO();
				exdodcDTO.setCodArticulo(rs.getInt("EXDCO3"));
				exdodcDTO.setDvArticulo(rs.getString("EXDDI3"));
				exdodcDTO.setPrecioBruto(rs.getDouble("EXDPRN"));
				exdodcDTO.setPrecioNeto(rs.getDouble("EXDPRE"));
				exdodcDTO.setFechaMaxEntrega(rs.getInt("EXDFE1"));
				exdodcDTO.setStockActual(rs.getDouble("EXDSTO"));
				exdodcDTO.setStockPedidoOC(rs.getDouble("EXDST1"));
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
		
		return exdodcDTO;
	}
	
}
