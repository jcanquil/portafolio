package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.PryenccDAO;
import cl.caserita.dto.DocncpDTO;
import cl.caserita.dto.EncprinDTO;
import cl.caserita.dto.PryenccDTO;
import cl.caserita.dto.ReslibDTO;

public class PryenccDAOImpl implements PryenccDAO {

	private  static Logger log = Logger.getLogger(PryenccDAOImpl.class);

	private Connection conn;
	
	public PryenccDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaRegistroEncuesta(PryenccDTO pry){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.PRYENCC " + 
        " (CLCRU1,CLCDVR,RUTPER,DIGPER,CODENCU,ESTRES,SUGENC) VALUES("+pry.getRutCliente()+",'"+pry.getDvCliente()+"',"+pry.getRutPersonal()+",'"+pry.getDvPersonal()+"',"+pry.getCodigoEncuesta()+",'"+pry.getEstadoRespuesta().trim()+"','"+pry.getSugerenciaEncuesta()+"')";
		log.info("INSERTA PRYENCC" + sqlObtenerVecmar);
		try{
		
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
			res=1;
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
	
	public int obtieneEncuesta(int rut, int rutPersonal){
		int existe=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List encuesta = new ArrayList();
		String sqlObtenerVecmar=" SELECT * "+
				" FROM CASEDAT.PRYENCC  " + 
				" WHERE CLCRU1 = "+rut+" AND RUTPER="+rutPersonal+" FOR READ ONLY" ;
		
		log.info("SQL ENCPRIN" + sqlObtenerVecmar);   
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				existe=1;
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
		return existe;
	}
	
}
