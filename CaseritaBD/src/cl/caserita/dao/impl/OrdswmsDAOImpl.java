package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.OrdswmsDAO;
import cl.caserita.dto.CarswmsDTO;
import cl.caserita.dto.OrdswmsDTO;

public class OrdswmsDAOImpl implements OrdswmsDAO{

	private static Logger log = Logger.getLogger(OrdswmsDAOImpl.class);
	private Connection conn;
	
	public OrdswmsDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaOrden(OrdswmsDTO ord){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.ORDSWMS " + 
        " (CACEMP, CODTSA,FCHPRC,HORPRC,NOMXML,NUMCAR,AA18UA,PRMTI1,NUMVEN,CLDCOD,CNTRAR,CLDCAN,CAMCO1, FCHREA) VALUES("+ord.getCodigoEmpresa()+","+ord.getCodigoTipoSalida()+","+ord.getFechaProceso()+","+ord.getHoraProceso()+",'"+ord.getArchivoXML()+"',"+ord.getNumeroCarguio()+","+ord.getTransferenciaNumeroCarguio()+","+ord.getTipoMovimiento()+","+ord.getNumeroOrdenVenta()+","+ord.getCodigoArticulo()+","+ord.getCantidadRealArticulo()+","+ord.getCantidadArticulo()+","+ord.getCodigoBodega()+","+ord.getFechaRealGeneracion()+")";
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
