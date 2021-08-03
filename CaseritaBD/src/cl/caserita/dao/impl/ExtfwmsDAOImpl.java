package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExtfwmsDAO;
import cl.caserita.dto.ExtfwmsDTO;
import cl.caserita.dto.VecfwmsDTO;

public class ExtfwmsDAOImpl implements ExtfwmsDAO{
	private static Logger log = Logger.getLogger(ExtfwmsDAOImpl.class);

	private Connection conn;
	
	public ExtfwmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaArchivoXML(ExtfwmsDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXTFWMS " + 
        " (EXIMEM, EXINUM,EXIBO4,EXIBO5,NOMXML,FCHUSR, HORUSR) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroTraspaso()+","+dto.getCodigoBodegaOrigen()+","+dto.getCodigoBodegaDestino()+",'"+dto.getNombreArchivoXML()+"', "+dto.getFechaUsuario()+","+dto.getHoraUsuario()+") " ;
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
