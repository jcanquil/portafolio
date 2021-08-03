package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ArchordDAO;
import cl.caserita.dto.ArchordDTO;
import cl.caserita.dto.CarswmsDTO;

public class ArchordDAOImpl implements ArchordDAO{

	private  static Logger log = Logger.getLogger(ArchordDAOImpl.class);

	private Connection conn;
	
	public ArchordDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaarchivoTicket(ArchordDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.Archord " + 
        " (EMPATE, NUMOT, NUMVOR, NUMARC, RTAROR, FCHUSR, HORUSR, NMEQUI, IPEQUI, USRSYS) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroOrden()+","+dto.getNumeroVersion()+","+dto.getNumeroArchivo()+",'"+dto.getRutaArchivo().trim()+"',"+dto.getFechaUsuario()+","+dto.getHoraUsuario()+",'"+dto.getNombreEquipo()+"','"+dto.getIpEquipo()+"','"+dto.getUsuario()+"')";
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
