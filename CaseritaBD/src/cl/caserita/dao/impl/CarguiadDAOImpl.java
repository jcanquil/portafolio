package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CarguiadDAO;
import cl.caserita.dto.CargfwmsDTO;
import cl.caserita.dto.CargonwDTO;
import cl.caserita.dto.CarguiadDTO;
import cl.caserita.dto.OrdvtaDTO;

public class CarguiadDAOImpl implements CarguiadDAO{

	private  static Logger log = Logger.getLogger(CarguiadDAOImpl.class);

	private Connection conn;
	
	public CarguiadDAOImpl(Connection conn){
	this.conn=conn;
	}
	
	public int generaAdicional(CarguiadDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
	    "  CASEDAT.CARGUIAD " + 
	    " (CACEMP, NUMCAR,VECPAT,CAMCO1,CODEIN) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroCarguio()+",'',"+dto.getCodigoBodega()+",'"+dto.getCodigoEstadoInventario().trim()+"') " ;
		System.out.println("INSERTA CARGFWMS" + sqlObtenerVecmar);
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
	
	public List recuperaCarguioTransporte(int codigoEmpresa,  int bodega, String estado){
		CarguiadDTO carguiadDTO = null;
		OrdvtaDTO ordvtaDTO = null;
		PreparedStatement pstmt =null;
		List carguio = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCarguiad="Select  "+
        " C1.CAMRUT, C1.EXMDI3 FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIAD C2 " + 
        " WHERE C1.CACEMP= C2.CACEMP AND C1.NUMCAR= C2.NUMCAR AND C1.CAMCO1= C2.CAMCO1 AND C1.VECPAT= C2.VECPAT AND C1.CACEMP="+codigoEmpresa+" AND C1.CAMCO1="+bodega+" AND C2.ESTPTR ='"+estado.trim()+"' GROUP BY C1.CAMRUT, C1.EXMDI3"  + " FOR READ ONLY" ;
		log.info("SQL:"+sqlObtenerCarguiad);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCarguiad);
			
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				carguiadDTO = new CarguiadDTO();
						carguiadDTO.setRutChofer(rs.getInt("CAMRUT"));
						carguiadDTO.setDvChofer(rs.getString("EXMDI3"));
						carguio.add(carguiadDTO);
						
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
		return carguio;
	}
}
