package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.EncswmsDAO;
import cl.caserita.dto.EncswmsDTO;
import cl.caserita.dto.EndPointWSDTO;
import cl.caserita.dto.GenlibDTO;

public class EncswmsDAOImpl implements EncswmsDAO{

	private static Logger log = Logger.getLogger(EncswmsDAOImpl.class);
	private Connection conn;
	
	public EncswmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaEncabezado(EncswmsDTO enc){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.ENCSWMS " + 
        " (CACEMP, CODTSA, DESTSA,FCHPRC,HORPRC,NOMXML,FCHREA) VALUES("+enc.getCodigoEmpresa()+","+enc.getCodigoTipoSalida()+",'"+enc.getDescripcionTipoSalida()+"',"+enc.getFechaProceso()+","+enc.getHoraProceso()+",'"+enc.getArchivoXML()+"',"+enc.getFechaRealGeneracion()+")";
		//log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	
	public EncswmsDTO buscaEncabezado(int empresa, String nombreArchivo, int fecha){
		EncswmsDTO encDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.ENCSWMS " + 
        " Where CACEMP="+empresa+" AND NOMXML='"+nombreArchivo.trim()+"' AND FCHPRC="+fecha+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				encDTO = new EncswmsDTO();
				encDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				encDTO.setFechaProceso(rs.getInt("FCHPRC"));
				encDTO.setHoraProceso(rs.getInt("HORPRC"));
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
		return encDTO;
	}
	
	
}
