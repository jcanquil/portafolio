package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConnoiDAO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.ConnoiDTO;

public class ConnoiDAOImpl implements ConnoiDAO {

	private static Logger log = Logger.getLogger(ConnoiDAOImpl.class);

	private Connection conn;
	
	public ConnoiDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List buscaImpto(String tipoNota, int numeroNota, int fechaEmision){
		ConnoiDTO connoiDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CONNOI " + 
        " Where CONTI4='"+tipoNota+"' AND CONNU4="+numeroNota+" AND CONFE4="+fechaEmision+" FOR READ ONLY" ;
		List connoi = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				if (rs.getInt("CONCO8")!=2){
					connoiDTO = new ConnoiDTO();
					connoiDTO.setTipoNota(rs.getString("CONTI4"));
					connoiDTO.setNumNota(rs.getInt("CONNU4"));
					connoiDTO.setFechaNota(rs.getInt("CONFE4"));
					connoiDTO.setCorrelativo(rs.getInt("CONCO9"));
					connoiDTO.setCodImpto(rs.getInt("CONCO8"));
					connoiDTO.setMontoImpuesto(rs.getInt("CONMO3"));
					connoiDTO.setTasaImpto(obtieneTasa(connoiDTO.getCodImpto()));
					connoi.add(connoiDTO);
				}
				
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
		return connoi;
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

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		
		return tasa;
		
		
	}
	public int actualizaConnoi(String tipoNota, int numNota, int fechaEmision, int numero){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerConnoh ="UPDATE "+
        " CASEDAT.CONNOI " + 
        " SET CONNU4="+numero+" Where CONTI4='"+tipoNota+"' AND CONNU4="+numNota+" AND CONFE4="+fechaEmision+" " ;
		log.info(sqlObtenerConnoh);
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnoh);
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
}


