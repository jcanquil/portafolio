package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExtariDAO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.TptimpDTO;

public class ExtariDAOImpl implements ExtariDAO {
	private static Logger log = Logger.getLogger(ExtariDAOImpl.class);

	private Connection conn;
	
	public ExtariDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int buscasiArticuloesExento(int codarticulo, String dvarticulo){
		int res=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerExtari=" SELECT * "+
				" FROM CASEDAT.EXTARI "+
				" WHERE extco1 = "+codarticulo+
				" AND extdi2 = '"+dvarticulo+"'";
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerExtari);
			
			log.info("SQL" + sqlObtenerExtari);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("EXTCO2")==1){
					res=1;
				}
				else{
					res=2;
				}
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
		return res;
		
	}
	
	public double recuperaImpuestos(int codarticulo, String dvarticulo){
		double res=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerExtari=" SELECT * "+
				" FROM CASEDAT.EXTARI "+
				" WHERE extco1 = "+codarticulo+
				" AND extdi2 = '"+dvarticulo+"'";
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerExtari);
			
			log.info("SQL" + sqlObtenerExtari);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				res = res + recuperaValorImpuesto(rs.getInt("EXTCO2"));
				
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
		return res;
		
	}
	
	public double recuperaValorImpuesto(int codImpto){
		double impuesto  = 0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
				" from CASEDAT.TPTIMP " + 
				" Where TPTC32="+codImpto+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			log.info("SQL IMPUESTO " + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				impuesto = rs.getDouble("TPTVA4");
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
		return impuesto;
	}
	public double obtenerCostoNeto(int codarticulo, String dvarticulo, double costobruto){
		double costoNeto=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerExtari=" SELECT SUM(tptva4/100)+1 AS IMPTO "+
				" FROM CASEDAT.EXTARI, CASEDAT.TPTIMP "+
				" WHERE extco1 = "+codarticulo+
				" AND extdi2 = '"+dvarticulo+"'"+
				" AND extco2 = tptc32 ";
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerExtari);
			
			log.info("SQL" + sqlObtenerExtari);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				costoNeto = costobruto / rs.getDouble("IMPTO");
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
		return costoNeto;
	}
	
}
