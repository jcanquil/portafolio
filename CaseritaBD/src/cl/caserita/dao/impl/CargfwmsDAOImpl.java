package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CargfwmsDAO;
import cl.caserita.dto.CargfwmsDTO;
import cl.caserita.dto.VecfwmsDTO;

public class CargfwmsDAOImpl implements CargfwmsDAO{
	private  static Logger log = Logger.getLogger(CargfwmsDAOImpl.class);

	private Connection conn;
	
	public CargfwmsDAOImpl(Connection conn){
	this.conn=conn;
}

public int generaArchivoXML(CargfwmsDTO dto){
	int res=0;
	PreparedStatement pstmt =null;
	
	String sqlObtenerVecmar="INSERT INTO"+
    "  CASEDAT.CARGFWMS " + 
    " (CACEMP, NUMCAR,VECPAT,CAMCO1,NOMXML,TIPCAR,FCHUSR, HORUSR) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroCarguio()+",'"+dto.getPatente().trim()+"',"+dto.getCodigoBodega()+",'"+dto.getNombreArchivoXML().trim()+"','"+dto.getTipo().trim()+"', "+dto.getFechaUsuario()+","+dto.getHoraUsuario()+") " ;
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
}
