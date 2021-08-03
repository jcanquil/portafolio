package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dto.VecfwmsDTO;

public class VecfwmsDAOImpl implements VecfwmsDAO {
		private Connection conn;
		private static Logger log = Logger.getLogger(VecfwmsDAOImpl.class);

		public VecfwmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaArchivoXML(VecfwmsDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VECFWMS " + 
        " (VENEMP, VENCOD,VENFEC,VENNUM,NOMXML,TIPWMS,FCHUSR, HORUSR) VALUES("+dto.getCodigoEmpresa()+","+dto.getTipoMovto()+","+dto.getFechaMovimiento()+","+dto.getNumeroDocumento()+",'"+dto.getNombreArchivoXML().trim()+"','"+dto.getTipo().trim()+"', "+dto.getFechaUsuario()+","+dto.getHoraUsuario()+") " ;
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
