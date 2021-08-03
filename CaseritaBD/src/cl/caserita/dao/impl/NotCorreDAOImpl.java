package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.NotCorreDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.ClcmcoDTO;

public class NotCorreDAOImpl implements NotCorreDAO{

	private static Logger log = Logger.getLogger(NotCorreDAOImpl.class);
	private Connection conn;
	
	public NotCorreDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int generaNumeroDocAtencion(int empresa, int numeroDocumento, int bodega, int numAtencion){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.NOTCORRE " + 
        " (EMNTCO, CLCNUM,TPTC18,BDGCORR) VALUES("+empresa+","+numeroDocumento+","+bodega+","+numAtencion+") " ;
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
