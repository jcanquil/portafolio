package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.OrdttxtDAO;
import cl.caserita.dto.OrdtrbDTO;
import cl.caserita.dto.OrdttxtDTO;

public class OrdttxtDAOImpl implements OrdttxtDAO{

	private static Logger log = Logger.getLogger(OrdttxtDAOImpl.class);
	private Connection conn;
	
	public OrdttxtDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaOrden(OrdttxtDTO txt){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.ORDTTXT " + 
        " (EMPATE, NUMOT, OBF00A, OBF009) VALUES("+txt.getCodigoEmpresa()+","+txt.getNumeroOrdenTrabajo()+","+txt.getNumeroLinea()+",'"+txt.getTexto()+"')";
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
