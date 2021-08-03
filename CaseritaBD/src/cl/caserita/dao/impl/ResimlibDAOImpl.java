package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ResimlibDAO;
import cl.caserita.dto.GenlibDTO;
import cl.caserita.dto.ResimlibDTO;

public class ResimlibDAOImpl implements ResimlibDAO{

	private static Logger log = Logger.getLogger(ResimlibDAOImpl.class);
	private Connection conn;
	
	public ResimlibDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaResimlib(ResimlibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.RESIMLIB " + 
        " (CODLCNT,TPTC30,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,CODRES,TPACO1,CODREIM,CODIMRES,MNIMRES) VALUES('"+gen.getCodLibro()+"',"+gen.getEmpresa()+",'"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+",'"+gen.getCodResumen()+"',"+gen.getCodDocumento()+",'"+gen.getCodResumenImpuesto()+"','"+gen.getCodImpuesto()+"',"+gen.getMontoResumenImpuesto()+")";
		//System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
