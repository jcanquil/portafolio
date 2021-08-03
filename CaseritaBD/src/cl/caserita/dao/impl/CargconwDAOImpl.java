package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.caserita.dao.iface.CargconwDAO;
import cl.caserita.dto.CargconwDTO;
import cl.caserita.dto.OrdvddeDTO;

public class CargconwDAOImpl implements CargconwDAO{
	
	private Connection conn;
	
	public CargconwDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int insertaCargconw(CargconwDTO carcon){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="INSERT INTO CASEDAT.CARGCONW " + 
        " (CACEMP, NUMCAR, VECPAT, CAMCO1, CLDCOD, "+
		" CONDI3, TIPCAR, CLDCAN, FCHDEV, CNTCONF, CNTDIF, "+
        " ADSFEC, ORDDPR, CONPRE) " +
		" VALUES("+carcon.getCodigoEmpresa()+","+carcon.getNumcarguio()+",'"
        +carcon.getPatente()+"',"+carcon.getCodigoBodega()+","+carcon.getCodigoArticulo()+",'"
		+carcon.getDigitoArticulo()+"','"+carcon.getTipoCarguio()+"',"+carcon.getCantidadArticulo()+","
        +carcon.getFechaDevolucion()+","+carcon.getCantidadConfirmada()+","+carcon.getCantidadDiferencia()+","
        +carcon.getFechaExpiracion()+","+carcon.getPrecioNeto()+","+carcon.getPrecioBruto()+") " ;
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			//System.out.println("INSERTA EN TABLA cargconw  : " + carcon.getNumcarguio());
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	

	
	public int buscaCargconw(CargconwDTO carcon){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT NUMCAR FROM CASEDAT.CARGCONW "+
			" WHERE CACEMP="+carcon.getCodigoEmpresa()+" AND NUMCAR="+carcon.getNumcarguio()+" "+
			" AND VECPAT='"+carcon.getPatente()+"'"+
			" AND CAMCO1="+carcon.getCodigoBodega()+ " "+
			" GROUP BY NUMCAR FOR READ ONLY " ;
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=1;
			}		
				}catch(Exception e){
					e.printStackTrace();
				}finally {

					try {
						 if (rs != null) { rs.close(); 
						 pstmt.close();
						 }

					} catch (SQLException e1) { }

			  } 

		return correlativo;
		
	}

	
	public int eliminaCargconw(CargconwDTO carcon){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="DELETE FROM CASEDAT.CARGCONW " + 
				" WHERE CACEMP="+carcon.getCodigoEmpresa()+" AND NUMCAR="+carcon.getNumcarguio()+" "+
				" AND VECPAT='"+carcon.getPatente()+"'"+
				" AND CAMCO1="+carcon.getCodigoBodega()+" "+
				" AND CLDCOD="+carcon.getCodigoArticulo()+ " ";
				
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int eliminaCargconwTransp(CargconwDTO carcon){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="DELETE FROM CASEDAT.CARGCONW " + 
				" WHERE CACEMP="+carcon.getCodigoEmpresa()+" AND NUMCAR="+carcon.getNumcarguio()+" "+
				" AND VECPAT='"+carcon.getPatente()+"'"+
				" AND CAMCO1="+carcon.getCodigoBodega()+" ";
			
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int buscaCargconwTransp(CargconwDTO carcon){
		int correlativo=-1;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT CLDCAN FROM CASEDAT.CARGCONW "+
			" WHERE CACEMP="+carcon.getCodigoEmpresa()+" AND NUMCAR="+carcon.getNumcarguio()+" "+
			" AND VECPAT='"+carcon.getPatente()+"'"+
			" AND CAMCO1="+carcon.getCodigoBodega()+ " "+
			" AND CLDCOD="+carcon.getCodigoArticulo()+" "+
			" FOR READ ONLY " ;
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("CLDCAN");
				break;
			}		
				}catch(Exception e){
					e.printStackTrace();
				}finally {

					try {
						 if (rs != null) { rs.close(); 
						 pstmt.close();
						 }

					} catch (SQLException e1) { }

			  } 

		return correlativo;
		
	}

	
	public int actualizaCargconw(CargconwDTO carcon){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="UPDATE CASEDAT.CARGCONW SET " + 
				" CLDCAN="+carcon.getCantidadArticulo()+" "+
				" WHERE CACEMP="+carcon.getCodigoEmpresa()+" AND NUMCAR="+carcon.getNumcarguio()+" "+
				" AND VECPAT='"+carcon.getPatente()+"'"+
				" AND CAMCO1="+carcon.getCodigoBodega()+" "+
				" AND CLDCOD="+carcon.getCodigoArticulo()+ " ";
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
		
}
