package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dto.CartolasDTO;
import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.LogestinDTO;
import cl.caserita.dto.ReslibDTO;
import cl.caserita.dto.StockinventarioDTO;

public class StockinventarioDAOImpl implements StockinventarioDAO {

	private  static Logger log = Logger.getLogger(StockinventarioDAOImpl.class);

	private Connection conn;
	
	public StockinventarioDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int actualizaStock(StockinventarioDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE "+
        "  CASEDAT.STKESINV SET EXMST8="+dto.getCantidad()+
        " WHERE CODESY="+dto.getCodigoEmpresa()+
        " AND CAMCO1="+dto.getCodigoBodega()+
        " AND CODEIN='"+dto.getEstado().trim()+"'"+
        " AND VENC16="+dto.getCodigoArticulo()+" ";
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	
	public StockinventarioDTO lista(int empresa, int bodega, String estado, int articulo){
		
		int numAtencion=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		StockinventarioDTO inventario = null;

		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.STKESINV " + 
        "  WHERE CODESY="+empresa+" AND CAMCO1="+bodega+" AND CODEIN='"+estado+"' AND VENC16="+articulo+"  " ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				inventario =  new StockinventarioDTO();
				inventario.setCodigoEmpresa(rs.getInt("CODESY"));
				inventario.setCodigoBodega(rs.getInt("CAMCO1"));
				inventario.setCodigoArticulo(rs.getInt("VENC16"));
				inventario.setDvArticulo(rs.getString("CONDI3"));
				inventario.setCantidad(rs.getInt("EXMST8"));
				inventario.setEstado(rs.getString("CODEIN").trim());
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
		
		return inventario;
		
	}
	
	public int creaStockInvWMS(StockinventarioDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.STKESINV " + 
        " (CODESY, CAMCO1, CODEIN, VENC16, CONDI3, EXMST8, EXMST7) " +
        " VALUES("+dto.getCodigoEmpresa()+","+dto.getCodigoBodega()+",'"+dto.getEstado().trim()+"',"+dto.getCodigoArticulo()+",'"+dto.getDvArticulo()+"',"+dto.getCantidad()+",0)";
		
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
	
	public int generaLoginventario(LogestinDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.LOGESTIN " + 
        " (CODESY, CAMCO1,VENC16,CODEIN,NOMXML,NUMDET,FCHUSR, HORLOG,EXDSTO,STINFO,EXMST8) VALUES("+dto.getCodigoEmpresa()+","+dto.getCodigoBodega()+","+dto.getCodigoArticulo()+",'"+dto.getCodigoEstadoInventario()+"','"+dto.getNombreArchivoXML().trim()+"',"+dto.getNumeroLineaDetalle()+","+dto.getFechaUsuario()+","+dto.getHoraUsuario()+","+dto.getStockActual()+","+dto.getStockInformado()+","+dto.getStockLinea()+") " ;
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
