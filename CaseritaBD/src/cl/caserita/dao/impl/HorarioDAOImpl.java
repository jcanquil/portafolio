package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.HorarioDAO;
import cl.caserita.dto.HorarioDTO;
import cl.caserita.dto.TipodespachoDTO;

public class HorarioDAOImpl implements HorarioDAO{

	private static Logger log = Logger.getLogger(HorarioDAOImpl.class);

private Connection conn;
	
	public HorarioDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List horarios (int rutCliente){
		List horarioList = new ArrayList();
		HorarioDTO horario = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.HORACLI "+
				" WHERE CLCRU1="+rutCliente+" ";
      
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					horario = new HorarioDTO();
					horario.setRutCliente(rs.getInt("CLCRU1"));
					horario.setDvCliente(rs.getString("CLCDVR"));
					horario.setCorrelativo(rs.getInt("CORHOR"));
					horario.setDescripcionHorario(rs.getString("DESHOR"));
					horarioList.add(horario);
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return horarioList;
	}
	
	public HorarioDTO recuperaHorario (int rut, int correlativo){
		
		HorarioDTO horario = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.HORACLI " +
		" where CLCRU1="+rut+" AND CORHOR="+correlativo+" ";
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				horario = new HorarioDTO();
				horario.setRutCliente(rs.getInt("CLCRU1"));
				horario.setDvCliente(rs.getString("CLCDVR"));
				horario.setCorrelativo(rs.getInt("CORHOR"));
				horario.setDescripcionHorario(rs.getString("DESHOR"));
					
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return horario;
	}
	
	
}
