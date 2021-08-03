package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.LogcanpeDAO;
import cl.caserita.dto.LogcanpeDTO;
import cl.caserita.dto.LogestinDTO;

public class LogcanpeDAOImpl implements LogcanpeDAO{

	private static Logger log = Logger.getLogger(LogestinImpl.class);
	private Connection conn;
	
	public LogcanpeDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaLogpendientePago(LogcanpeDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.LOGCANPE " + 
        " (VENEMP,VENNUM,VENBO2,VENNU2,VENFE1,FCHUSR,HORUSR,USRSYS,IPEQUI,NMEQUI,CODINI,CODFIN,NOMEJE,NOMFOR) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroDocumento()+","+dto.getCodigoBodega()+",'"+dto.getNumeroFactura()+"','"+dto.getFechaDocumento()+"',"+dto.getFechaUsuario()+","+dto.getHoraUsuario()+",'"+dto.getUsuario().trim()+"','"+dto.getIpEquipo().trim()+"','"+dto.getNombreEquipo().trim()+"','"+dto.getCodigoEstadoInicial().trim()+"','"+dto.getCodigoEstadoFinal().trim()+"','"+dto.getNombreEjecutable().trim()+"','"+dto.getNombreFormulario().trim()+"')" ;
		log.info("INSERTA LOG PENDIENTE" + sqlObtenerVecmar);
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
