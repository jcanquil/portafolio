package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CarswmsDAO;
import cl.caserita.dto.CarswmsDTO;
import cl.caserita.dto.DetswmsDTO;

public class CarswmsDAOImpl implements CarswmsDAO{

	
	private static Logger log = Logger.getLogger(CarswmsDAOImpl.class);
	private Connection conn;
	
	public CarswmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaDetalle(CarswmsDTO car){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CARSWMS " + 
        " (CACEMP, CODTSA,FCHPRC,HORPRC,NOMXML,NUMCAR,AA18UA) VALUES("+car.getCodigoEmpresa()+","+car.getCodigoTipoSalida()+","+car.getFechaProceso()+","+car.getHoraProceso()+",'"+car.getArchivoXML()+"',"+car.getNumeroCarguio()+","+car.getTransferenciaNumeroCarguio()+")";
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
}
