package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConvafDAO;
import cl.caserita.dto.ConvafDTO;
import cl.caserita.dto.StockinventarioDTO;

public class ConvafDAOImpl implements ConvafDAO{
	private static Logger log = Logger.getLogger(ConvafDAOImpl.class);

	private Connection conn;
	
	public ConvafDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ConvafDTO lista(int codtipdocto, int numerodocumento, int fechadocumento, int rutprov, String dvprov){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		ConvafDTO enlaceContable = null;
		
		String sqlObtenerCamtra="Select * "+
        " FROM CASEDAT.CONVAF " + 
        " WHERE conc09 = "+codtipdocto+
        " AND conn03 = "+numerodocumento+
        " AND confe6 = "+fechadocumento+
        " AND conru9 = "+rutprov+
        " AND condi6 = '"+dvprov+"'";
		
		log.info("Consulta Enlace Contable" + sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				enlaceContable =  new ConvafDTO();
				
				enlaceContable.setCodigoDocumento(rs.getInt("CONC09"));
				enlaceContable.setNumeroDocumento(rs.getInt("CONN03"));
				enlaceContable.setFechaDocumento(rs.getInt("CONFE6"));
				enlaceContable.setRutProveedor(rs.getInt("CONRU9"));
				enlaceContable.setDvProveedor(rs.getString("CONDI6"));
				enlaceContable.setNumeroOrden(rs.getInt("CONN05"));
				enlaceContable.setNumeroLiquidacion(rs.getInt("CONN04"));
				enlaceContable.setFechaContabiliza(rs.getInt("CONFE9"));
				enlaceContable.setBodega(rs.getInt("CONBO3"));
				enlaceContable.setCodigoTransaccion(rs.getInt("CONC10"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		
		return enlaceContable;
	}
	
	public int eliminaEnlaceContable(ConvafDTO dto){
		
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="DELETE FROM "+
        " CASEDAT.CONVAF " + 
        " WHERE conc09 = "+dto.getCodigoDocumento()+
        " AND conn03 = "+dto.getNumeroDocumento()+
        " AND confe6 = "+dto.getFechaDocumento()+
        " AND conru9 = "+dto.getRutProveedor()+
        " AND condi6 = '"+dto.getDvProveedor()+"'";
		
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
