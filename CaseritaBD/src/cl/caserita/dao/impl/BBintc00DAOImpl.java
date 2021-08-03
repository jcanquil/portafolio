package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.BBintc00DAO;
import cl.caserita.dto.BBintc00DTO;
import cl.caserita.dto.CamtraDTO;

public class BBintc00DAOImpl implements BBintc00DAO{

	private  static Logger log = Logger.getLogger(BBintc00DAOImpl.class);

	private Connection conn;
	
	public BBintc00DAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public BBintc00DTO obtieneVentaBB( int codigo, int fecha, int numero){
		BBintc00DTO bbintc00DTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.BBINTC00 " + 
        " Where BBCODDOC="+codigo+" AND BBFECMOV="+fecha+" AND BBNUMDOC="+numero+" FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bbintc00DTO = new BBintc00DTO();
				bbintc00DTO.setCodigoDocumento(rs.getInt("BBCODDOC"));
				bbintc00DTO.setFechaDocumento(rs.getInt("BBFECMOV"));
				bbintc00DTO.setNumeroDocumento(rs.getInt("BBNUMDOC"));
				bbintc00DTO.setCodigoBodega(rs.getInt("BBCODBOD"));
				bbintc00DTO.setTotalDocumento(rs.getInt("BBTOTDOC"));
				bbintc00DTO.setRutCliente(rs.getInt("BBRUTCLI"));
				bbintc00DTO.setDvCliente(rs.getString("BBDIGCLI").trim());
				bbintc00DTO.setCodigoVendedor(rs.getInt("BBCODVEN"));
				bbintc00DTO.setIndicadorDespacho(rs.getInt("BBINDDES"));
				bbintc00DTO.setIndicadorFacturacion(rs.getInt("BBINDFAC"));
				bbintc00DTO.setFormaPago(rs.getString("BBFORPAG"));
				bbintc00DTO.setEstado(rs.getString("BBESTADO"));
				bbintc00DTO.setErrorCabecera(rs.getString("BBERRCAB"));
				bbintc00DTO.setNombreDispositivo(rs.getString("BBNOMDIS"));
				bbintc00DTO.setHoraGeneracion(rs.getInt("BBHORVTA"));
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
		
		return bbintc00DTO;
		
	}
}
