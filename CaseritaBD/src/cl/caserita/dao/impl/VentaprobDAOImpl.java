package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VentaprobDAO;
import cl.caserita.dto.VencobDTO;
import cl.caserita.dto.VentaprobDTO;

public class VentaprobDAOImpl implements VentaprobDAO{

	private static Logger log = Logger.getLogger(VentaprobDAOImpl.class);
	private Connection conn;
	
	public VentaprobDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List ventas(){
		List ved= new ArrayList();
		VentaprobDTO venta = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List ventas = new ArrayList();
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VENTAPROB " ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				venta = new VentaprobDTO();
				venta.setCodigoEmpresa(rs.getInt("VENEMP"));
				venta.setFechaMovimiento(rs.getInt("VENFEC"));
				venta.setNumeroDocumento(rs.getInt("VENNUM"));
				venta.setNumeroInterno(rs.getInt("VENNU2"));
				venta.setDescripcionTipoDocumento(rs.getString("TPADE1"));
				venta.setCodigoDocumento(rs.getInt("VENCO5"));
				venta.setCodigoBodega(rs.getInt("VENBO2"));
				venta.setUrlXML(rs.getString("CAMRUD"));
				ventas.add(venta);
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
		
		
		return ventas;
	}
}
