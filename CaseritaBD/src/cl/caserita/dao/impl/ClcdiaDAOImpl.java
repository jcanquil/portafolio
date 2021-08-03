package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ClcdiaDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.LibroImpuestoDTO;

public class ClcdiaDAOImpl implements ClcdiaDAO{
	
	private  static Logger log = Logger.getLogger(ClcdiaDAOImpl.class);

	
	private Connection conn;
	
	public ClcdiaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtieneImpuesto(int empresa, int codigo, int rutCliente, String dv, int fecha, int hora){
		ClcdiaDTO clcdiaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CLCDIA " + 
        " Where CLCCEM="+empresa+" and CLCCO2="+codigo+" AND CLCRU1="+rutCliente+" AND CLCDVR='"+dv+"' AND CLCFE1="+fecha+" AND CLCHO1="+hora+" FOR READ ONLY" ;
		List clcdia = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
					clcdiaDTO = new ClcdiaDTO();
					clcdiaDTO.setCodDocumento(rs.getInt("CLCCO2"));
					clcdiaDTO.setRutCliente(rs.getString("CLCRU1"));
					clcdiaDTO.setDvCliente(rs.getString("CLCDVR"));
					clcdiaDTO.setFechaMovimiento(rs.getInt("CLCFE1"));
					clcdiaDTO.setHoraMovimiento(rs.getInt("CLCHO1"));				
					clcdiaDTO.setCodigoImpuesto(rs.getInt("CLCCO3"));
					clcdiaDTO.setMontoImpuesto(rs.getInt("CLCMO3"));
					clcdiaDTO.setImpuesto(obtieneTasa(clcdiaDTO.getCodigoImpuesto()));
					
					clcdia.add(clcdiaDTO);
				
				
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
		return clcdia;
	}
	public double obtieneTasa(int codigo){
		double tasa=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTIMP " + 
        " Where TPTC32="+codigo+"   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tasa = rs.getDouble("TPTVA4");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close();
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return tasa;
		
		
	}
	
	
	public List acumuladoImpuestos(int empresa, int codigoDocumento, int mes, int ano){
		List impuestos = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select C2.CLCCO3 , SUM( C2.CLCMO3 ) AS MONTO "+
        " FROM CASEDAT.CLCMCO C1, CASEDAT.CLCDIA C2 " + 
        " WHERE C1.CLCEMP="+empresa+" AND C1.CLCCO1 ="+codigoDocumento+" AND C1.CLCNUM>5000000 AND C1.CLCFEC BETWEEN "+mes+" AND "+ano+" AND C1.CLCEST=0 AND C1.CLCEMP=C2.CLCCEM AND C1.CLCCO1=C2.CLCCO2 AND C1.CLCRUT= C2.CLCRU1 AND C1.CLCDIG= C2.CLCDVR AND C1.CLCFEC = C2.CLCFE1 AND C1.CLCHOR =C2.CLCHO1 GROUP BY C2.CLCCO3 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLCCO3")!=1 && rs.getInt("CLCCO3")!=2){
					LibroImpuestoDTO libro = new LibroImpuestoDTO();
					libro.setCodigoImpuesto(rs.getInt("CLCCO3"));
					libro.setMontoImpuesto(rs.getInt("MONTO"));
					impuestos.add(libro);
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return impuestos;
		
	}
	
	public List acumuladoImpuestosBodega(int empresa, int codigoDocumento, int mes, int ano, int bodega){
		List impuestos = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select C2.CLCCO3 , SUM( C2.CLCMO3 ) AS MONTO "+
        " FROM CASEDAT.CLCMCO C1, CASEDAT.CLCDIA C2 " + 
        " WHERE C1.CLCEMP="+empresa+" AND C1.CLCCO1 ="+codigoDocumento+" AND C1.CLCNUM>5000000 AND C1.CLCBOD="+bodega+" AND C1.CLCFEC BETWEEN "+mes+" AND "+ano+" AND C1.CLCEST=0 AND C1.CLCEMP=C2.CLCCEM AND C1.CLCCO1=C2.CLCCO2 AND C1.CLCRUT= C2.CLCRU1 AND C1.CLCDIG= C2.CLCDVR AND C1.CLCFEC = C2.CLCFE1 AND C1.CLCHOR =C2.CLCHO1 GROUP BY C2.CLCCO3 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLCCO3")!=1 && rs.getInt("CLCCO3")!=2){
					LibroImpuestoDTO libro = new LibroImpuestoDTO();
					libro.setCodigoImpuesto(rs.getInt("CLCCO3"));
					libro.setMontoImpuesto(rs.getInt("MONTO"));
					impuestos.add(libro);
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return impuestos;
		
	}
	
	public List acumuladoImpuestosNC(int empresa, int codigoDocumento, int mes, int ano){
		List impuestos = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select C2.CLCCO3 , SUM( C2.CLCMO3 ) AS MONTO "+
        " FROM CASEDAT.CLCMCO C1, CASEDAT.CLCDIA C2 " + 
        " WHERE C1.CLCEMP="+empresa+" AND C1.CLCCO1 ="+codigoDocumento+" AND C1.CLCNUM>50000 AND C1.CLCFEC BETWEEN "+mes+" AND "+ano+" AND C1.CLCEST=0 AND C1.CLCEMP=C2.CLCCEM AND C1.CLCCO1=C2.CLCCO2 AND C1.CLCRUT= C2.CLCRU1 AND C1.CLCDIG= C2.CLCDVR AND C1.CLCFEC = C2.CLCFE1 AND C1.CLCHOR =C2.CLCHO1 GROUP BY C2.CLCCO3 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLCCO3")!=1){
					LibroImpuestoDTO libro = new LibroImpuestoDTO();
					libro.setCodigoImpuesto(rs.getInt("CLCCO3"));
					libro.setMontoImpuesto(rs.getInt("MONTO"));
					impuestos.add(libro);
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return impuestos;
		
	}
	
	public List acumuladoImpuestosBodegaNC(int empresa, int codigoDocumento, int mes, int ano, int bodega){
		List impuestos = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select C2.CLCCO3 , SUM( C2.CLCMO3 ) AS MONTO "+
        " FROM CASEDAT.CLCMCO C1, CASEDAT.CLCDIA C2 " + 
        " WHERE C1.CLCEMP="+empresa+" AND C1.CLCCO1 ="+codigoDocumento+" AND C1.CLCNUM>50000 AND C1.CLCBOD="+bodega+" AND C1.CLCFEC BETWEEN "+mes+" AND "+ano+" AND C1.CLCEST=0 AND C1.CLCEMP=C2.CLCCEM AND C1.CLCCO1=C2.CLCCO2 AND C1.CLCRUT= C2.CLCRU1 AND C1.CLCDIG= C2.CLCDVR AND C1.CLCFEC = C2.CLCFE1 AND C1.CLCHOR =C2.CLCHO1 GROUP BY C2.CLCCO3 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLCCO3")!=1){
					LibroImpuestoDTO libro = new LibroImpuestoDTO();
					libro.setCodigoImpuesto(rs.getInt("CLCCO3"));
					libro.setMontoImpuesto(rs.getInt("MONTO"));
					impuestos.add(libro);
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return impuestos;
		
	}
}
