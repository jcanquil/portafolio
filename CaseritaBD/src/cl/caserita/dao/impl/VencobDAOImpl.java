package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VencobDAO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.dto.VencobDTO;

public class VencobDAOImpl implements VencobDAO{

	private static Logger log = Logger.getLogger(VencobDAOImpl.class);
	private Connection conn;
	
	public VencobDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public VencobDTO obtenerVendedorSupervisor(int codigoVendedor){
		List ved= new ArrayList();
		VencobDTO vencob = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VENDCOB V1, CASEDAT.SUPEVEND S1 " + 
        " Where V1.EXMC09="+codigoVendedor+" AND V1.CODSUP=S1.CODSUP FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vencob = new VencobDTO();
				vencob.setCodigoSupervisor(rs.getInt("CODSUP"));
				vencob.setCodigoVendedor(rs.getInt("EXMC09"));
				vencob.setNombreVendedor(rs.getString("NOMVND"));
				vencob.setNombreSupervisor(rs.getString("NOMSUP"));
				
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
		
		
		return vencob;
	}
	
	public List obtenerVendedor(int supervisor){
		List ved= new ArrayList();
		//VencobDTO vencob = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List lista = new ArrayList();

		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VENDCOB V1, CASEDAT.SUPEVEND S1 " + 
        " Where V1.CODSUP="+supervisor+" AND V1.CODSUP=S1.CODSUP FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				VencobDTO vencob = new VencobDTO();
				vencob.setCodigoSupervisor(rs.getInt("CODSUP"));
				vencob.setCodigoVendedor(rs.getInt("EXMC09"));
				vencob.setNombreVendedor(rs.getString("NOMVND"));
				vencob.setNombreSupervisor(rs.getString("NOMSUP"));
				lista.add(vencob);
				
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
}
