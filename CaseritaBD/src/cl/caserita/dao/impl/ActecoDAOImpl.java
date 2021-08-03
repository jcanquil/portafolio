package cl.caserita.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ActecoDAO;
import cl.caserita.dto.ActecoDTO;

public class ActecoDAOImpl implements ActecoDAO{

	private  static Logger log = Logger.getLogger(ActecoDAOImpl.class);
	private Connection conn;

	public ActecoDAOImpl(Connection conn){
		this.conn=conn;
	}

	public List buscaActeco(){
		ActecoDTO acteco = null;
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.ACTECO " + 
        " FOR READ ONLY" ;
		List actecoList = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//log.info("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				acteco = new ActecoDTO();
				acteco.setCodigoActeco(rs.getString("CODACT"));
				acteco.setDescripcionActeco(rs.getString("DESACT"));
				
				actecoList.add(acteco);
				
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
		
		return actecoList;
		
	}
	
	public void buscapcomboo(){
		ActecoDTO acteco = null;
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.PCOMBOO " + 
        " FOR READ ONLY" ;
		List actecoList = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//log.info("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				double precio = rs.getDouble("PRECOM");
				
				//precio = precio*(java.lang.Math.pow(10, 4));
				//precio = java.lang.Math.round(precio,4);
				DecimalFormat df = new DecimalFormat("#.####");
				log.info("double"+df.format(precio));
				String precioDouble = df.format(precio).replaceAll(",", ".");
				Double precio2 = Double.parseDouble(df.format(precio));
				actualizaCamtra(rs.getInt("CODCOM"),rs.getInt("CODCOMB2"),precio);
				
				
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
		
		
		
	}
	
	public int actualizaCamtra(int codiar, int codigo, double precio){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.EXDACP " + 
        " SET  EXIPBN="+precio+" Where ASTC01=2 AND CAMCO1=21 AND CLMCO6=1000 AND EXDCOD="+codiar+" AND EXDCO2="+codigo+" " ;
		log.info("SQL ACTUALIZA CAMTRA NC"+ sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
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
		pstmt=null;
		
		
		
		return actualiza;
	}
}
