package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ObordenDAO;
import cl.caserita.dto.ObordenDTO;

public class ObordenDAOImpl implements ObordenDAO{

	private static Logger log = Logger.getLogger(NotCorreDAOImpl.class);
	private Connection conn;
	
	public ObordenDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int generaObservacionOrden(ObordenDTO obOrden){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.OBORDEN " + 
        " (ORDEMP,NUMVEN,CLMRUT,CLMDIG,TPTC18,CORDOV,OBSORD) VALUES("+obOrden.getCodigoEmpresa()+","+obOrden.getNumeroOrdenVenta()+","+obOrden.getRutCliente()+",'"+obOrden.getDvCliente()+"',"+obOrden.getCodigoBodega()+","+obOrden.getCorrelativoDetalleOV()+",'"+obOrden.getObservacion()+"') " ;
		log.info("INSERTA OBSERVACION" + sqlObtenerVecmar);
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
