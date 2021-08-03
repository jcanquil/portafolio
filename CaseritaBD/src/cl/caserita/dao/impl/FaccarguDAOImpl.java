package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.FaccarguDAO;
import cl.caserita.dto.ExtfwmsDTO;
import cl.caserita.dto.FaccarguDTO;

public class FaccarguDAOImpl implements FaccarguDAO{

	private static Logger log = Logger.getLogger(FaccarguDAOImpl.class);

private Connection conn;
	
	public FaccarguDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaAprobacion(FaccarguDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.FACCARGU " + 
        " (CACEMP, NUMCAR,VECPAT,CAMCO1,ORDEMP,NUMVEN, CLMRUT,CLMDIG, TPTC18, USRAPRO, FCHAPRO,HORAPRO,ESAPVT) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroCarguio()+",'"+dto.getPatente()+"',"+dto.getCodigoBodega()+",'"+dto.getCodigoEmpresaOV()+"', "+dto.getNumeroOV()+","+dto.getRutCliente()+",'"+dto.getDvCliente()+"',"+dto.getCodigoBodegaOV()+",'"+dto.getUsuario()+"',"+dto.getFechaAprobacion()+","+dto.getHoraAprobacion()+",'"+dto.getEstado()+"') " ;
		log.info("INSERTA FACCARGU" + sqlObtenerVecmar);
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
