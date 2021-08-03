package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.GenlibDAO;
import cl.caserita.dto.GenlibDTO;

public class GenlibDAOImpl implements GenlibDAO{

	private static Logger log = Logger.getLogger(GenlibDAOImpl.class);
	private Connection conn;
	
	public GenlibDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaGenLib(GenlibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.GENLIB " + 
        " (CODLCNT,TPTC30,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,ANOGLIB,MESGLIB, COBDGL) VALUES('"+gen.getCodLibro()+"',"+gen.getEmpresa()+",'"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+","+gen.getAno()+","+gen.getMes()+","+gen.getBodega()+")";
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
