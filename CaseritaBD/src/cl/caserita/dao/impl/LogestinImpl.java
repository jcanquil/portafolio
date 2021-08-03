package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dto.LogestinDTO;
import cl.caserita.dto.LogintegracionDTO;

public class LogestinImpl {

	private static Logger log = Logger.getLogger(LogestinImpl.class);
	private Connection conn;
	
	public LogestinImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaLoginventario(LogestinDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.LOGESTIN " + 
        " (CODESY, CAMCO1,VENC16,CODEIN,NOMXML,NUMDET,EXDSTO,STINFO,EXMST8) VALUES("+dto.getCodigoEmpresa()+","+dto.getCodigoBodega()+","+dto.getCodigoArticulo()+",'"+dto.getCodigoEstadoInventario()+"','"+dto.getNombreArchivoXML()+"',"+dto.getStockActual()+","+dto.getStockInformado()+","+dto.getStockLinea()+") " ;
		log.info("INSERTA LOG ESTADO INVENTARIO" + sqlObtenerVecmar);
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
