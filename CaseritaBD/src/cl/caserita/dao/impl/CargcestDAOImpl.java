package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CargcestDAO;
import cl.caserita.dto.CargcestDTO;

public class CargcestDAOImpl implements CargcestDAO{
	
	private static Logger log = Logger.getLogger(CargcestDAOImpl.class);
	private Connection conn;
	
	public CargcestDAOImpl(Connection conn){
		this.conn=conn;
	}
	

	
	//Insert Table Cargcest
	public int insertaCargcest(CargcestDTO dto){
		int correla =-100;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		
		String cmp ="CACEMP, NUMCAR, VECPAT, CAMCO1, CORDCA, ESTCAM, EXMFE9, HORCAM, USRCAM";
	
		String sqlObtenerCargcest ="INSERT INTO "+
		        " CASEDAT.CARGCEST ("+cmp+") "+
				" VALUES ("+dto.getCodigoEmpresa()+","+dto.getNumcarguio()+",'"+dto.getPatente().trim()+"',"+dto.getCodigoBodega()+","+
		        dto.getCorrelativo()+",'"+dto.getEstado()+"',"+dto.getFechaUsuario()+","+dto.getHoraUsuario()+",'"+dto.getUsuario()+"')";
				log.info("SQL CAMBIO ESTADO CARGUIO :"+sqlObtenerCargcest);
			try{
				pstmt = conn.prepareStatement(sqlObtenerCargcest);
				pstmt.executeUpdate();
				
				//log.info("SQL Orden" + sqlObtenerCargcest);
				//log.info("Ok !! insert cargcest");
				correla=1;
				
			}catch(Exception e){
				e.printStackTrace();
			}finally {
	
				try {
	
					 if (rs != null) { rs.close(); 
					 pstmt.close();
					 }
	
				} catch (SQLException e1) { }
	
			}
	
			return correla;
	}
	
	
	//Get Cargcest
		public int obtieneCorrelativo(int empresa, int numcar, String patente, int codigoBodega){
			int correlativo=-100;
			
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			String sqlObtenerCargcest ="Select MAX(c.cordca)+1 as cordcaa"+
	        " from CASEDAT.CARGCEST c" + 
	        " WHERE c.CACEMP="+empresa+" AND c.NUMCAR="+numcar+" AND c.VECPAT='"+patente+"' AND c.CAMCO1="+codigoBodega+" FOR READ ONLY" ;
			try
			{
				pstmt = conn.prepareStatement(sqlObtenerCargcest);
				
				rs = pstmt.executeQuery();
				
				correlativo=1;
				while (rs.next()) {
					correlativo=rs.getInt("CORDCAA");	
					if (correlativo==0){ correlativo=1; }
					
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

			
					
			return correlativo;
		}

		public int existeCargcestDTO (CargcestDTO cargdto){
			int existereg=-100;
			
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			
			String sqlObtenerNcpes =" SELECT * "+
					" FROM CASEDAT.CARGCEST "+
					" WHERE cacemp=2"+
					" AND numcar="+cargdto.getNumcarguio()+ " "+
					" AND vecpat='"+cargdto.getPatente()+"' "+
					" AND camco1="+cargdto.getCodigoBodega()+" "+
					" AND estcam='"+cargdto.getEstado()+"' "+
					" FOR READ ONLY" ;
			log.info("ANTES DE SELECT * CARGCEST  : "+sqlObtenerNcpes);
			try{
				pstmt = conn.prepareStatement(sqlObtenerNcpes);
				rs = pstmt.executeQuery();
				log.info("O K E Y    SELECT * CARGCEST  : "+sqlObtenerNcpes);
				while (rs.next()) {
					existereg=1;
					break;
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

			
			return existereg;
		}
}
