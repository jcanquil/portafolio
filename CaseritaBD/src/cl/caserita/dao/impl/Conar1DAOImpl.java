package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.Conar1DAO;
import cl.caserita.dto.Conar1DTO;
import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.LibroImpuestoDTO;
import cl.caserita.dto.LibroTotalesDTO;

public class Conar1DAOImpl implements Conar1DAO{
	

	private  static Logger log = Logger.getLogger(Conar1DAOImpl.class);

	private Connection conn;
	
	public Conar1DAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int generaConar1(Conar1DTO conar1){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CONAR1 " + 
        " ( CONCO3, CONRU7, CONDI4, CONNU1, CONFE1,CONCO4,CONMON) VALUES("+conar1.getCodigoDocumento()+","+conar1.getRut()+",'"+conar1.getDv()+"',"+conar1.getNumeroDocumento()+","+conar1.getFechaDocumento()+","+conar1.getCodigoImpuesto()+","+conar1.getMontoImpuesto()+") " ;
		
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
	
	public List recuperaImpuesto(int codDoc, int rut, String dv, int numeroDoc){
		Conar1DTO conar1DTO =null;
		List impuesto = new ArrayList();
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.CONAR1 " + 
        " Where CONCO3="+codDoc+" AND CONRU7="+rut+" AND CONDI4='"+dv+"' AND CONNU1="+numeroDoc+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				conar1DTO= new Conar1DTO();
				conar1DTO.setCodigoDocumento(rs.getInt("CONCO3"));
				conar1DTO.setRut(rs.getInt("CONRU7"));
				conar1DTO.setDv(rs.getString("CONDI4"));
				conar1DTO.setNumeroDocumento(rs.getInt("CONNU1"));
				conar1DTO.setFechaDocumento(rs.getInt("CONFE1"));
				conar1DTO.setCodigoImpuesto(rs.getInt("CONCO4"));
				conar1DTO.setMontoImpuesto(rs.getInt("CONMON"));
				conar1DTO.setTasaImpuesto(obtieneTasa(conar1DTO.getCodigoImpuesto()));
				impuesto.add(conar1DTO);
				
				
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
		
		return impuesto;
	}
	
	public Conar1DTO recuperaImpuestoIva(int codDoc, int rut, String dv, int numeroDoc){
		Conar1DTO conar1DTO =null;
		
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.CONAR1 " + 
        " Where CONCO3="+codDoc+" AND CONRU7="+rut+" AND CONDI4='"+dv+"' AND CONNU1="+numeroDoc+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				if (rs.getInt("CONCO4")==2){
					conar1DTO= new Conar1DTO();
					conar1DTO.setCodigoDocumento(rs.getInt("CONCO3"));
					conar1DTO.setRut(rs.getInt("CONRU7"));
					conar1DTO.setDv(rs.getString("CONDI4"));
					conar1DTO.setNumeroDocumento(rs.getInt("CONNU1"));
					conar1DTO.setFechaDocumento(rs.getInt("CONFE1"));
					conar1DTO.setCodigoImpuesto(rs.getInt("CONCO4"));
					conar1DTO.setMontoImpuesto(rs.getInt("CONMON"));
					conar1DTO.setTasaImpuesto(obtieneTasa(conar1DTO.getCodigoImpuesto()));
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
		
		return conar1DTO;
	}
	
	public List acumuladoImpuestos(int ano, int mes, int codDocumento){
		List impuestos = new ArrayList();
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select C2.CONCO4 ,SUM( C2.CONMON ) AS IVA"+
        " from CASEDAT.CONARC C1, CASEDAT.CONAR1 C2" + 
        " Where C1.CONCO2="+codDocumento+" AND C1.CONAOD="+ano+" AND C1.CONMES="+mes+" AND C1.CONCO2= C2.CONCO3 AND C1.CONRUT= C2.CONRU7 AND C1.CONDIG=C2.CONDI4 AND C1.CONNUM= C2.CONNU1 GROUP BY C2.CONCO4 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				LibroImpuestoDTO libro = new LibroImpuestoDTO();
				if (rs.getInt("CONCO4")!=2 && rs.getInt("CONCO4")!=1){
					libro.setCodigoImpuesto(rs.getInt("CONCO4"));
					libro.setMontoImpuesto(rs.getInt("IVA"));
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
	
	public int obtieneTasa(int codigo){
		int tasa=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTIMP " + 
        " Where TPTC32="+codigo+"   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tasa = rs.getInt("TPTVA4");
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
	
	

}
