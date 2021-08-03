package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmfwmsDAO;
import cl.caserita.dto.ExmfwmsDTO;
import cl.caserita.dto.VecfwmsDTO;

public class ExmfwmsDAOImpl implements ExmfwmsDAO{

	private static Logger log = Logger.getLogger(ExmfwmsDAOImpl.class);

private Connection conn;
	
	public ExmfwmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaArchivoXML(ExmfwmsDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXMFWMS " + 
        " (EXMEMP, EXMNUM,NOMXML,FCHUSR, HORUSR) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroOrdenCompra()+",'"+dto.getNombreArchivoXML()+"', "+dto.getFechaUsuario()+","+dto.getHoraUsuario()+") " ;
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
