package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ClcdiaDAO;
import cl.caserita.dao.iface.ClddiaDAO;
import cl.caserita.dto.ClcdiaDTO;




public class ClddiaDAOImpl implements ClddiaDAO{

	private  static Logger log = Logger.getLogger(ClddiaDAOImpl.class);

	private Connection conn;
	
	public ClddiaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List recuperaClddia(int codigo, int rutCliente, String dv, int fecha, int hora,int correlativo){
		ClcdiaDTO clcdiaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CLDDIA " + 
        " Where CLDCO6="+codigo+" AND CLDRU3="+rutCliente+" AND CLDDV3='"+dv+"' AND CLDFE4="+fecha+" AND CLDHO1="+hora+" AND CLDCO7="+correlativo+" FOR READ ONLY" ;
		List clcdia = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clcdiaDTO = new ClcdiaDTO();
				clcdiaDTO.setCodDocumento(rs.getInt("CLDCO6"));
				clcdiaDTO.setRutCliente(rs.getString("CLDRU3"));
				clcdiaDTO.setDvCliente(rs.getString("CLDDV3"));
				clcdiaDTO.setFechaMovimiento(rs.getInt("CLDFE4"));
				clcdiaDTO.setHoraMovimiento(rs.getInt("CLDHO1"));				
				clcdiaDTO.setCodigoImpuesto(rs.getInt("CLDCO8"));
				clcdiaDTO.setMontoImpuesto(rs.getInt("CLDMO4"));
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
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
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
}
