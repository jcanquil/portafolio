package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DetimlibDAO;
import cl.caserita.dto.DetimLibDTO;
import cl.caserita.dto.DetlibDTO;

public class DetimlibDAOImpl implements DetimlibDAO{
	
	private static Logger log = Logger.getLogger(DetimlibDAOImpl.class);
	private Connection conn;
	
	public DetimlibDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int genDetimlib(DetimLibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DETIMLIB " + 
        " (CODLCNT,TPTC30,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,CODDLI,TPACO1,FOLDOC,COIMDT,TSIMDET,MNIMPDT) VALUES('"+gen.getCodLibro()+"',"+gen.getEmpresa()+",'"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+",'"+gen.getCodDetalleLibro()+"',"+gen.getCodDocumento()+","+gen.getFolioDocumento()+","+gen.getCodigoImpuesto()+","+gen.getTasaImpuesto()+","+gen.getMontoImpuesto()+")";
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
