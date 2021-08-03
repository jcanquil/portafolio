package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmtraDTO;

public class ExmartDAOImpl implements ExmartDAO{

	private static Logger log = Logger.getLogger(ExmartDAOImpl.class);
	private Connection conn;
	
	public ExmartDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List recuperaArticulos(){
		ExmartDTO exmartDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMART E1, CASEDAT.EXMAGA1 E2 WHERE E1.EXMCOD=E2.EXMCOD AND E1.EXMDIG=E2.EXMDIG AND E2.CODGAL<>'' " ;
		List exdtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmartDTO = new ExmartDTO();
				exmartDTO.setCodigoArticulo(rs.getInt("EXMCOD"));
				exmartDTO.setDvArticulo(rs.getString("EXMDIG"));
				exmartDTO.setDescripcionArticulo(rs.getString("EXMDES"));
				exmartDTO.setCodigoCategoria(rs.getInt("EXMCO5"));
				exmartDTO.setCodigoFamilia(rs.getInt("EXMCO3"));
				exmartDTO.setDescripcionCortaArticulo(rs.getString("EXMNOM"));
				exmartDTO.setCodigoGrupo(rs.getInt("CODGAL"));
				exmartDTO.setDescripcionGrupo(rs.getString("DSGRAL"));
				exmartDTO.setVidaUtil(rs.getInt("XONM4"));
				exmartDTO.setCaja(rs.getInt("EXMCAJ"));
				exmartDTO.setDisplay(rs.getInt("EXMDIS"));
				exmartDTO.setUnidades(rs.getInt("EXMUNI"));
				exmartDTO.setCodigosBarras((buscaExdart(exmartDTO.getCodigoArticulo(),exmartDTO.getDvArticulo())));
				exmartDTO.setCodigos(buscaExdartOrdenada(exmartDTO.getCodigoArticulo(),exmartDTO.getDvArticulo()));

				exdtra.add(exmartDTO);
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
		return exdtra;
	}
	
	public ExmartDTO recuperaArticulo(int codArticulo, String digito){
		ExmartDTO exmartDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMART E1, CASEDAT.EXMAGA1 E2 WHERE E1.EXMCOD="+codArticulo+" AND E1.EXMDIG='"+digito+"' AND E1.EXMCOD=E2.EXMCOD AND E1.EXMDIG=E2.EXMDIG" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				 exmartDTO = new ExmartDTO();

				exmartDTO.setCodigoArticulo(rs.getInt("EXMCOD"));
				exmartDTO.setDvArticulo(rs.getString("EXMDIG"));
				exmartDTO.setDescripcionArticulo(rs.getString("EXMDES"));
				exmartDTO.setCodigoCategoria(rs.getInt("EXMCO5"));
				exmartDTO.setCodigoFamilia(rs.getInt("EXMCO3"));
				exmartDTO.setDescripcionCortaArticulo(rs.getString("EXMNOM"));
				exmartDTO.setCodigoGrupo(rs.getInt("CODGAL"));
				exmartDTO.setDescripcionGrupo(rs.getString("DSGRAL"));
				exmartDTO.setVidaUtil(rs.getInt("XONM4"));
				exmartDTO.setCaja(rs.getInt("EXMCAJ"));
				exmartDTO.setDisplay(rs.getInt("EXMDIS"));
				exmartDTO.setUnidades(rs.getInt("EXMUNI"));
				exmartDTO.setPallet(rs.getInt("EXMPAL"));
				
				exmartDTO.setCodigosBarras((buscaExdart(exmartDTO.getCodigoArticulo(),exmartDTO.getDvArticulo())));
				exmartDTO.setCodigos(buscaExdartOrdenada(exmartDTO.getCodigoArticulo(),exmartDTO.getDvArticulo()));
				
				
				
			}
			//exmartDTO.setCodigosBarras(lista);
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
		return exmartDTO;
	}
	
	public List buscaExdart(int codigo, String dv){
		ExdartDTO exdartDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXDART WHERE EXDCO1="+codigo+" AND EXDDI1='"+dv+"' " ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdartDTO = new ExdartDTO();
				exdartDTO.setCodigoArticulo(rs.getInt("EXDCO1"));
				exdartDTO.setDvArticulo(rs.getString("EXDDI1"));
				exdartDTO.setCodigoBarra(rs.getString("EXDC01"));
				exdartDTO.setAlto(rs.getDouble("EXDALT"));
				exdartDTO.setLargo(rs.getDouble("EXDLAR"));
				exdartDTO.setAncho(rs.getDouble("EXDANC"));
				exdartDTO.setCantAltoPallets(rs.getDouble("EXDCAP"));
				exdartDTO.setCantBasePallets(rs.getDouble("EXDCBP"));
				exdartDTO.setCantTotalPallets(rs.getDouble("EXDTCP"));
				exdartDTO.setPeso(rs.getDouble("EXDPES"));
				exdartDTO.setVolumen(rs.getDouble("EXDVOL"));
				exdartDTO.setTipoContenedor(rs.getString("EXDTIP"));
				lista.add(exdartDTO);
				
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
		return lista;
	}
	
	public HashMap buscaExdartOrdenada(int codigo, String dv){
		ExdartDTO exdartDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		HashMap<String,ExdartDTO> codigos = new HashMap<String,ExdartDTO>();
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXDART WHERE EXDCO1="+codigo+" AND EXDDI1='"+dv+"' " ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdartDTO = new ExdartDTO();
				exdartDTO.setCodigoArticulo(rs.getInt("EXDCO1"));
				exdartDTO.setDvArticulo(rs.getString("EXDDI1"));
				exdartDTO.setCodigoBarra(rs.getString("EXDC01").trim());
				exdartDTO.setAlto(rs.getDouble("EXDALT"));
				exdartDTO.setLargo(rs.getDouble("EXDLAR"));
				exdartDTO.setAncho(rs.getDouble("EXDANC"));
				exdartDTO.setCantAltoPallets(rs.getDouble("EXDCAP"));
				exdartDTO.setCantBasePallets(rs.getDouble("EXDCBP"));
				exdartDTO.setCantTotalPallets(rs.getDouble("EXDTCP"));
				exdartDTO.setPeso(rs.getDouble("EXDPES"));
				exdartDTO.setVolumen(rs.getDouble("EXDVOL"));
				exdartDTO.setTipoContenedor(rs.getString("EXDTIP"));
					codigos.put(exdartDTO.getTipoContenedor(), exdartDTO);

				
				//lista.add(exdartDTO);
				
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
		return codigos;
	}
	
	public ExmartDTO recuperaArticuloSinDigito(int codArticulo){
		ExmartDTO exmartDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMART  WHERE EXMCOD="+codArticulo+" " ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				 exmartDTO = new ExmartDTO();

				exmartDTO.setCodigoArticulo(rs.getInt("EXMCOD"));
				exmartDTO.setDvArticulo(rs.getString("EXMDIG"));
				exmartDTO.setDescripcionArticulo(rs.getString("EXMDES"));
				exmartDTO.setCodigoCategoria(rs.getInt("EXMCO5"));
				exmartDTO.setCodigoFamilia(rs.getInt("EXMCO3"));
				exmartDTO.setDescripcionCortaArticulo(rs.getString("EXMNOM"));
				exmartDTO.setCaja(rs.getInt("EXMCAJ"));
				exmartDTO.setDisplay(rs.getInt("EXMDIS"));
				exmartDTO.setUnidades(rs.getInt("EXMUNI"));
				
			}
			//exmartDTO.setCodigosBarras(lista);
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
		return exmartDTO;
	}
	
}
