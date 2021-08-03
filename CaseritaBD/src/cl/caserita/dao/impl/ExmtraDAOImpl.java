package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.OrdvtaDTO;

public class ExmtraDAOImpl implements ExmtraDAO{
	private static Logger log = Logger.getLogger(ExmtraDAOImpl.class);
	private Connection conn;
	
	public ExmtraDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public ExmtraDTO recuperaEncabezado (int empresa, int numTraspaso){
		ExmtraDTO exmtraDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.EXMTRA E1, CASEDAT.EXMTRAD E2" + 
        " Where E1.EXIMEM="+empresa+" and E1.EXINUM="+numTraspaso+" AND E1.EXIMEM=E2.EXIMEM AND E1.EXINUM=E2.EXINUM FOR READ ONLY" ;
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					exmtraDTO = new ExmtraDTO();
					exmtraDTO.setCodigoEmpresa(rs.getInt("EXIMEM"));
					exmtraDTO.setNumTraspaso(rs.getInt("EXINUM"));
					exmtraDTO.setBodegaOrigen(rs.getInt("EXIBO4"));
					exmtraDTO.setBodegaDestino(rs.getInt("EXIBO5"));
					exmtraDTO.setNumeroSello(rs.getString("EXINU1"));
					exmtraDTO.setNumGuiaDespacho(rs.getInt("EXINU3"));
					
					exmtraDTO.setFechaTraspaso(rs.getInt("EXIFEC"));
					exmtraDTO.setHoraTopeTraspaso(rs.getInt("EXIHOR"));
					exmtraDTO.setKilosMercaderia(rs.getDouble("EXIKIL"));
					exmtraDTO.setValorTTraspaso(rs.getDouble("EXIVA1"));
					exmtraDTO.setValorVTraspaso(rs.getDouble("EXIVOL"));
					exmtraDTO.setEstadoTraspaso(rs.getString("EXIEST"));
					exmtraDTO.setCodigoUsuario(rs.getString("EXICOD"));
					exmtraDTO.setRutEmpresa(rs.getInt("EXMRU6"));
					exmtraDTO.setDvEmpresa(rs.getString("EXMDVE"));
					exmtraDTO.setPatente(rs.getString("EXMPAT"));
					exmtraDTO.setCodigoInventario(rs.getString("CODEIN"));
				
				
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
		return exmtraDTO;
	}
	public int actualizaExmtra(int empresa, int numTraspaso, int numeroGuia){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.EXMTRA " + 
        " SET EXINU3="+numeroGuia+" Where EXIMEM="+empresa+" AND EXINUM="+numTraspaso+"  " ;
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
	
	public ExmtraDTO obtieneOrdenTraspaso(int empresa, int numeroOT, int codigoBodega, int carguio){
		ExmtraDTO exmtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerCldmco ="Select * "+
				" from CASEDAT.EXMTRA " + 
				" Where EXIMEM="+empresa+" AND EXINUM="+numeroOT+" AND EXIBO4="+codigoBodega+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmtraDTO = new ExmtraDTO();
				
				exmtraDTO.setNumTraspaso(rs.getInt("EXINUM"));
				exmtraDTO.setBodegaOrigen(rs.getInt("EXIBO4"));
				exmtraDTO.setBodegaDestino(rs.getInt("EXIBO5"));
				exmtraDTO.setNumeroSello(rs.getString("EXINU1"));
				exmtraDTO.setNumGuiaDespacho(rs.getInt("EXINU3"));
				exmtraDTO.setEstadoTraspaso(rs.getString("EXIEST"));
				exmtraDTO.setCodigoUsuario(rs.getString("EXICOD"));
				exmtraDTO.setRutEmpresa(rs.getInt("EXMRU6"));
				exmtraDTO.setDvEmpresa(rs.getString("EXMDVE"));
				exmtraDTO.setPatente(rs.getString("EXMPAT"));
				exmtraDTO.setExdtra(obtieneExdtra(empresa, exmtraDTO.getNumTraspaso(), exmtraDTO.getBodegaOrigen(), carguio));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

		} 
		return exmtraDTO;
	}
	
	public List obtieneExdtra(int empresa, int numeroOT, int codigoBodega, int numeroCarguio){
		
		ExdtraDTO exdtraDTO = null;
		List detalle = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco ="Select * "+
		" FROM CASEDAT.CARGUIOD D1, CASEDAT.EXMTRA D2, CASEDAT.EXDTRA D3 "+
		" WHERE D1.CACEMP = "+empresa+
		" AND D1.NUMCAR = "+numeroCarguio+
		" AND D1.CAMCO1 = "+codigoBodega+
		" AND D1.EXIMEM = "+empresa+
		" AND D1.EXINUM = "+numeroOT+
		" AND D1.EXIBO4 = "+codigoBodega+
		" AND D1.EXIMEM = D2.EXIMEM"+
		" AND D1.EXINUM = D2.EXINUM"+
		" AND D1.EXIBO4 = D2.EXIBO4"+
		" AND D1.EXIBO5 = D2.EXIBO5"+
		" AND D2.EXIMEM = D3.EXIEMP"+
		" AND D2.EXINUM = D3.EXINU2"+
		" AND D1.CORDC2 = D3.EXILIN";
		
		List exmtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL traspaso en Carguio " + sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				if (rs.getInt("CLDCAN")!=0){
					exdtraDTO = new ExdtraDTO();
					
					exdtraDTO.setCodEmpresa(rs.getInt("EXIMEM"));
					exdtraDTO.setNumTraspaso(rs.getInt("EXINUM"));
					exdtraDTO.setLinea(rs.getInt("EXILIN"));
					exdtraDTO.setCodArticulo(rs.getInt("EXICO1"));
					exdtraDTO.setDigitoVerificador(rs.getString("EXIDIG"));
					exdtraDTO.setFormato(rs.getString("EXIFOR"));
					exdtraDTO.setCantDespachada(rs.getInt("VENCA2"));
					
					detalle.add(exdtraDTO);
				}
				
					
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return detalle;
		
	}
	
	public int actualizaEstado(int empresa, int bodegaOrigen, int bodegaDestino, int numTraspaso){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.EXMTRA " + 
        " SET exiest = 'P'"+
        " WHERE eximem = "+empresa+
        " AND exibo4 = "+bodegaOrigen+
        " AND exibo5 = "+bodegaDestino+
        " AND exinum="+numTraspaso;
		
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
	
	public ExmtraDTO recuperaEncabezadoFE (int empresa, int numTraspaso){
		ExmtraDTO exmtraDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.EXMTRA " + 
        " Where EXIMEM="+empresa+" and EXINUM="+numTraspaso+"  FOR READ ONLY" ;
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					exmtraDTO = new ExmtraDTO();
					exmtraDTO.setCodigoEmpresa(rs.getInt("EXIMEM"));
					exmtraDTO.setNumTraspaso(rs.getInt("EXINUM"));
					exmtraDTO.setBodegaOrigen(rs.getInt("EXIBO4"));
					exmtraDTO.setBodegaDestino(rs.getInt("EXIBO5"));
					exmtraDTO.setNumeroSello(rs.getString("EXINU1"));
					exmtraDTO.setNumGuiaDespacho(rs.getInt("EXINU3"));
					
					exmtraDTO.setFechaTraspaso(rs.getInt("EXIFEC"));
					exmtraDTO.setHoraTopeTraspaso(rs.getInt("EXIHOR"));
					exmtraDTO.setKilosMercaderia(rs.getDouble("EXIKIL"));
					exmtraDTO.setValorTTraspaso(rs.getDouble("EXIVA1"));
					exmtraDTO.setValorVTraspaso(rs.getDouble("EXIVOL"));
					exmtraDTO.setEstadoTraspaso(rs.getString("EXIEST"));
					exmtraDTO.setCodigoUsuario(rs.getString("EXICOD"));
					exmtraDTO.setRutEmpresa(rs.getInt("EXMRU6"));
					exmtraDTO.setDvEmpresa(rs.getString("EXMDVE"));
					exmtraDTO.setPatente(rs.getString("EXMPAT"));
				
				
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
		return exmtraDTO;
	}
	
}
