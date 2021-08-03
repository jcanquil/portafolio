package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DocRecibidosDiaDAO;
import cl.caserita.dto.DocRecibidosDTO;

public class DocRecibidosDiaDAOImpl implements DocRecibidosDiaDAO{
	private static Logger log = Logger.getLogger(DocRecibidosDiaDAOImpl.class);

private Connection conn;
	
	public DocRecibidosDiaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public void procesa(DocRecibidosDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DCRECDIA " + 
        " (CLCEMP,CLDRUT,CONDI4,CLCFE1,CLCCO2,CLCNUM,CLCTOT) VALUES('"+dto.getCodigoEmpresa()+"',"+dto.getRut()+",'"+dto.getDv()+"',"+dto.getFecha()+","+dto.getCodDocumento()+","+dto.getNumeroDocumento()+","+dto.getMonto()+")";
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
		
		
	
	}
	
}
