package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DetswmsDAO;
import cl.caserita.dto.DetswmsDTO;
import cl.caserita.dto.EncswmsDTO;

public class DetswmsDAOImpl implements DetswmsDAO{

	private static Logger log = Logger.getLogger(DetswmsDAOImpl.class);
	private Connection conn;
	
	public DetswmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaDetalle(DetswmsDTO det){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DETSWMS " + 
        " (CACEMP, CODTSA,FCHPRC,HORPRC,NOMXML,CLDCOD,CODEIN,AA18TA,CLDCAN,CNTRAR,VECNU3,CLCNUM, FCHREA, NUMDET) VALUES("+det.getCodigoEmpresa()+","+det.getCodigoTipoSalida()+","+det.getFechaProceso()+","+det.getHoraProceso()+",'"+det.getArchivoXML()+"',"+det.getCodigoArticulo()+",'"+det.getCodigoEstadoInventario()+"','"+det.getProximoEstadoInvenatrio()+"',"+det.getCantidad()+","+det.getCantidadReal()+","+det.getNumeroOrden()+","+det.getNumeroDocumento()+","+det.getFechaRealGeneracion()+","+det.getNumeroDetalle()+")";
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
