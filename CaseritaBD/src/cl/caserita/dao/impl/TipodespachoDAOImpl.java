package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TipodespachoDAO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.TipodespachoDTO;

public class TipodespachoDAOImpl implements TipodespachoDAO{

private Connection conn;
private  static Logger log = Logger.getLogger(TipodespachoDAOImpl.class);

	public TipodespachoDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List tiposDespachos (int rut){
		List tipo = new ArrayList();
		TipodespachoDTO tipoDespacho = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TIPDESP " +
				"WHERE CLCRU1="+rut+" ";
      
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					tipoDespacho = new TipodespachoDTO();
					tipoDespacho.setRutCliente(rs.getInt("CLCRU1"));
					tipoDespacho.setDvCliente(rs.getString("CLCDVR"));
					tipoDespacho.setCodigoTipoDespacho(rs.getInt("CODTDE"));
					tipoDespacho.setDescripcionTipoDespacho(rs.getString("DETPDE"));
					tipo.add(tipoDespacho);
				
				
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
		return tipo;
	}
	
	public TipodespachoDTO recuperaTipoDespacho (int codigotipoDespacho){
		
		TipodespachoDTO tipoDespacho = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TIPDESP " +
		" where CODTDE="+codigotipoDespacho+" ";
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					tipoDespacho = new TipodespachoDTO();
					tipoDespacho.setCodigoTipoDespacho(rs.getInt("CODTDE"));
					tipoDespacho.setDescripcionTipoDespacho(rs.getString("DETPDE"));
					
				
				
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
		return tipoDespacho;
	}
	
}
