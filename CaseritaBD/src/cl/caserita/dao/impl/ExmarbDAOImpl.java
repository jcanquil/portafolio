package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dto.ExmarbDTO;


public class ExmarbDAOImpl implements ExmarbDAO {

	private static Logger log = Logger.getLogger(ExmarbDAOImpl.class);
	private Connection conn;
	
	public ExmarbDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ExmarbDTO recuperaArticulo(int codBodega, int codArticulo){
		ExmarbDTO exmarbDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMARB  WHERE EXMC01="+codBodega+" AND EXMC02="+codArticulo+"" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL RECUPERA ARTICULO" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmarbDTO = new ExmarbDTO();
				
				exmarbDTO.setCodigoBodega(rs.getInt("EXMC01"));
				exmarbDTO.setCodigoArticulo(rs.getInt("EXMC02"));
				exmarbDTO.setDvArticulo(rs.getString("EXMDI2"));
				exmarbDTO.setStockLinea(rs.getInt("EXMST3"));
				exmarbDTO.setStockComputacional(rs.getInt("EXMST6"));
				exmarbDTO.setCostoBruto(rs.getDouble("EXMCO2"));
				exmarbDTO.setCostoNeto(rs.getDouble("EXMCOU"));
				exmarbDTO.setPrecioBaseBruto(rs.getDouble("EXMPAO"));
				exmarbDTO.setPrecioBaseNeto(rs.getDouble("EXMPBN"));
				exmarbDTO.setCodigoSector(rs.getInt("EXMC06"));
				exmarbDTO.setCostoMayor(rs.getDouble("EXMCO6"));
				exmarbDTO.setCostoPromedio(rs.getDouble("EXMCO7"));
				exmarbDTO.setCostoMenor(rs.getInt("EXMCO8"));
				exmarbDTO.setCostoAnterior(rs.getDouble("EXMCO9"));
				exmarbDTO.setStockCritico(rs.getInt("EXMSTO"));
				exmarbDTO.setStockPendiente(rs.getInt("EXMST1"));
				exmarbDTO.setFechaCreacion(rs.getInt("EXMFE6"));
				exmarbDTO.setMargenUtil(rs.getDouble("EXMMAR"));
				exmarbDTO.setVariacion(rs.getDouble("EXMVAR"));
				exmarbDTO.setEstado(rs.getString("EXMEST"));
				exmarbDTO.setCompraArt(rs.getString("EXMC21"));
				exmarbDTO.setFechaUltCompra(rs.getInt("EXMFE4"));
				exmarbDTO.setPrecioBaseVenta(rs.getDouble("EXMPAO"));
				exmarbDTO.setStockMinimo(rs.getDouble("EXMST4"));
				exmarbDTO.setStockMaximo(rs.getDouble("EXMST5"));
				exmarbDTO.setFechaUltEntrada(rs.getInt("EXMFE2"));
				exmarbDTO.setFechaUltSalida(rs.getInt("EXMFE3"));
				exmarbDTO.setFechaUltVenta(rs.getInt("EXMFE5"));
				exmarbDTO.setFechaActualizacion(rs.getInt("EXMFE7"));
				exmarbDTO.setFechaInhabilitacion(rs.getInt("EXMFE8"));
				exmarbDTO.setIngresoCompra(rs.getInt("EXMING"));
				exmarbDTO.setIngresoTraspaso(rs.getInt("EXMIN1"));
				exmarbDTO.setIngresoVarios(rs.getInt("EXMIN2"));
				exmarbDTO.setSalidaCompra(rs.getInt("EXMSAL"));
				exmarbDTO.setSalidaTraspaso(rs.getInt("EXMSA1"));
				exmarbDTO.setSalidaVarios(rs.getInt("EXMSA2"));
				exmarbDTO.setPeriodoCierre(rs.getInt("EXMPE1"));
				exmarbDTO.setStockAlCierre(rs.getInt("EXMS01"));
				exmarbDTO.setMontoVtaPend(rs.getInt("EXMMO3"));
				exmarbDTO.setMontoAcumuCompra(rs.getInt("EXMMO4"));
				exmarbDTO.setMontoAcumuVtas(rs.getInt("EXMMO5"));
				exmarbDTO.setUnidVtaAnterior(rs.getInt("EXMUN4"));
				exmarbDTO.setUnidadesTraspaso(rs.getInt("EXMUN5"));
				exmarbDTO.setCodigoClasificacion(rs.getInt("EXMC07"));
				exmarbDTO.setUltimoProveedor(rs.getInt("EXMULT"));
				exmarbDTO.setdvUltimoProveedor(rs.getString("EXMDI1"));
				
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
		return exmarbDTO;
	}
	
	public int actualizaStockLinea(int codbodega, int codarticulo, String dvarticulo, double stockLinea){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE CASEDAT.EXMARB SET exmst3 = "+stockLinea+ 
				" WHERE exmc01 = "+codbodega+
				" AND exmc02 = "+codarticulo;
		log.info("UPDATE EXMARB :" + sqlObtenerVecmar);
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

	public int actualizaCostoNeto(int codbodega, int codarticulo, String dvarticulo, double costoneto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerExmarb="UPDATE CASEDAT.EXMARB SET exmcou = "+costoneto+ 
				" WHERE exmc01 = "+codbodega+
				" AND exmc02 = "+codarticulo+
				" AND exmdi2 = '"+dvarticulo+"'";
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerExmarb);
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
	
	public ExmarbDTO obtieneCostosArticulo(int codBodega, int codArticulo){
		ExmarbDTO exmarbDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMARB  WHERE EXMC01="+codBodega+" AND EXMC02="+codArticulo+"" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmarbDTO = new ExmarbDTO();
				
				exmarbDTO.setCodigoBodega(rs.getInt("EXMC01"));
				exmarbDTO.setCodigoArticulo(rs.getInt("EXMC02"));
				exmarbDTO.setDvArticulo(rs.getString("EXMDI2"));
				exmarbDTO.setStockLinea(rs.getInt("EXMST3"));
				exmarbDTO.setStockComputacional(rs.getInt("EXMST6"));
				exmarbDTO.setCostoBruto(rs.getDouble("EXMCO2"));
				exmarbDTO.setCostoNeto(rs.getDouble("EXMCOU"));
				exmarbDTO.setPrecioBaseBruto(rs.getDouble("EXMPAO"));
				exmarbDTO.setPrecioBaseNeto(rs.getDouble("EXMPBN"));
				exmarbDTO.setCodigoSector(rs.getInt("EXMC06"));
				exmarbDTO.setCostoMayor(rs.getDouble("EXMCO6"));
				exmarbDTO.setCostoPromedio(rs.getDouble("EXMCO7"));
				exmarbDTO.setCostoMenor(rs.getDouble("EXMCO8"));
				exmarbDTO.setCostoAnterior(rs.getDouble("EXMCO9"));
				exmarbDTO.setStockCritico(rs.getInt("EXMSTO"));
				exmarbDTO.setStockPendiente(rs.getInt("EXMST1"));
				exmarbDTO.setFechaCreacion(rs.getInt("EXMFE6"));
				exmarbDTO.setMargenUtil(rs.getDouble("EXMMAR"));
				exmarbDTO.setVariacion(rs.getDouble("EXMVAR"));
				exmarbDTO.setEstado(rs.getString("EXMEST"));
				exmarbDTO.setCompraArt(rs.getString("EXMC21"));
				exmarbDTO.setFechaUltCompra(rs.getInt("EXMFE4"));
				exmarbDTO.setPrecioBaseVenta(rs.getDouble("EXMPAO"));
				exmarbDTO.setStockMinimo(rs.getDouble("EXMST4"));
				exmarbDTO.setStockMaximo(rs.getDouble("EXMST5"));
				exmarbDTO.setFechaUltEntrada(rs.getInt("EXMFE2"));
				exmarbDTO.setFechaUltSalida(rs.getInt("EXMFE3"));
				exmarbDTO.setFechaUltVenta(rs.getInt("EXMFE5"));
				exmarbDTO.setFechaActualizacion(rs.getInt("EXMFE7"));
				exmarbDTO.setFechaInhabilitacion(rs.getInt("EXMFE8"));
				exmarbDTO.setIngresoCompra(rs.getInt("EXMING"));
				exmarbDTO.setIngresoTraspaso(rs.getInt("EXMIN1"));
				exmarbDTO.setIngresoVarios(rs.getInt("EXMIN2"));
				exmarbDTO.setSalidaCompra(rs.getInt("EXMSAL"));
				exmarbDTO.setSalidaTraspaso(rs.getInt("EXMSA1"));
				exmarbDTO.setSalidaVarios(rs.getInt("EXMSA2"));
				exmarbDTO.setPeriodoCierre(rs.getInt("EXMPE1"));
				exmarbDTO.setStockAlCierre(rs.getInt("EXMS01"));
				exmarbDTO.setMontoVtaPend(rs.getInt("EXMMO3"));
				exmarbDTO.setMontoAcumuCompra(rs.getInt("EXMMO4"));
				exmarbDTO.setMontoAcumuVtas(rs.getInt("EXMMO5"));
				exmarbDTO.setUnidVtaAnterior(rs.getInt("EXMUN4"));
				exmarbDTO.setUnidadesTraspaso(rs.getInt("EXMUN5"));
				exmarbDTO.setCodigoClasificacion(rs.getInt("EXMC07"));
				exmarbDTO.setUltimoProveedor(rs.getInt("EXMULT"));
				exmarbDTO.setdvUltimoProveedor(rs.getString("EXMDI1"));
				
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
		return exmarbDTO;
	}
	
	
	public List obtieneArticulosStockNegativo(int codBodega){
		ExmarbDTO exmarbDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMARB  WHERE EXMC01="+codBodega+" AND EXMST3<=0" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmarbDTO = new ExmarbDTO();
				
				exmarbDTO.setCodigoBodega(rs.getInt("EXMC01"));
				exmarbDTO.setCodigoArticulo(rs.getInt("EXMC02"));
				exmarbDTO.setDvArticulo(rs.getString("EXMDI2"));
				exmarbDTO.setStockLinea(rs.getInt("EXMST3"));
				exmarbDTO.setStockComputacional(rs.getInt("EXMST6"));
				exmarbDTO.setCostoBruto(rs.getDouble("EXMCO2"));
				exmarbDTO.setCostoNeto(rs.getDouble("EXMCOU"));
				exmarbDTO.setPrecioBaseBruto(rs.getDouble("EXMPAO"));
				exmarbDTO.setPrecioBaseNeto(rs.getDouble("EXMPBN"));
				exmarbDTO.setCodigoSector(rs.getInt("EXMC06"));
				exmarbDTO.setCostoMayor(rs.getDouble("EXMCO6"));
				exmarbDTO.setCostoPromedio(rs.getDouble("EXMCO7"));
				exmarbDTO.setCostoMenor(rs.getDouble("EXMCO8"));
				exmarbDTO.setCostoAnterior(rs.getDouble("EXMCO9"));
				exmarbDTO.setStockCritico(rs.getInt("EXMSTO"));
				exmarbDTO.setStockPendiente(rs.getInt("EXMST1"));
				exmarbDTO.setFechaCreacion(rs.getInt("EXMFE6"));
				exmarbDTO.setMargenUtil(rs.getDouble("EXMMAR"));
				exmarbDTO.setVariacion(rs.getDouble("EXMVAR"));
				exmarbDTO.setEstado(rs.getString("EXMEST"));
				exmarbDTO.setCompraArt(rs.getString("EXMC21"));
				exmarbDTO.setFechaUltCompra(rs.getInt("EXMFE4"));
				exmarbDTO.setPrecioBaseVenta(rs.getDouble("EXMPAO"));
				exmarbDTO.setStockMinimo(rs.getDouble("EXMST4"));
				exmarbDTO.setStockMaximo(rs.getDouble("EXMST5"));
				exmarbDTO.setFechaUltEntrada(rs.getInt("EXMFE2"));
				exmarbDTO.setFechaUltSalida(rs.getInt("EXMFE3"));
				exmarbDTO.setFechaUltVenta(rs.getInt("EXMFE5"));
				exmarbDTO.setFechaActualizacion(rs.getInt("EXMFE7"));
				exmarbDTO.setFechaInhabilitacion(rs.getInt("EXMFE8"));
				exmarbDTO.setIngresoCompra(rs.getInt("EXMING"));
				exmarbDTO.setIngresoTraspaso(rs.getInt("EXMIN1"));
				exmarbDTO.setIngresoVarios(rs.getInt("EXMIN2"));
				exmarbDTO.setSalidaCompra(rs.getInt("EXMSAL"));
				exmarbDTO.setSalidaTraspaso(rs.getInt("EXMSA1"));
				exmarbDTO.setSalidaVarios(rs.getInt("EXMSA2"));
				exmarbDTO.setPeriodoCierre(rs.getInt("EXMPE1"));
				exmarbDTO.setStockAlCierre(rs.getInt("EXMS01"));
				exmarbDTO.setMontoVtaPend(rs.getInt("EXMMO3"));
				exmarbDTO.setMontoAcumuCompra(rs.getInt("EXMMO4"));
				exmarbDTO.setMontoAcumuVtas(rs.getInt("EXMMO5"));
				exmarbDTO.setUnidVtaAnterior(rs.getInt("EXMUN4"));
				exmarbDTO.setUnidadesTraspaso(rs.getInt("EXMUN5"));
				exmarbDTO.setCodigoClasificacion(rs.getInt("EXMC07"));
				exmarbDTO.setUltimoProveedor(rs.getInt("EXMULT"));
				exmarbDTO.setdvUltimoProveedor(rs.getString("EXMDI1"));
				lista.add(exmarbDTO);
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
		return lista;
	}
}
