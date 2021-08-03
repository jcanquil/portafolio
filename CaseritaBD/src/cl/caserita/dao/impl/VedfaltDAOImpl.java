package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VedfaltDAO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedfaltDTO;
import cl.caserita.dto.VedmarDTO;

public class VedfaltDAOImpl implements VedfaltDAO {

	private  static Logger log = Logger.getLogger(VedfaltDAOImpl.class);

	private Connection conn;
	
	public VedfaltDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaMovimiento(VedfaltDTO vedfalt){
		int res=0;
		PreparedStatement pstmt =null;
		/*VEDEMP, VENC11, VENFE3, VENNU4, VENCOR,
		VENC15, VENC16, VENDI1, VENFOR, VENCA2,
		VENCA1, VEDSE1, VENPES, VEDPR2, VEDPRN,
		VEDCNT, VEDCTN, VEDMO4, VENMO2, VENMTN,
		VENEXE, VENCO3 */
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VEDFALT " + 
        " (VENEMP, VENCOD,VENFEC,VENNUM,VENCOR,VENC15,VENC16,VENDI1,VENFOR, VENCA2,VENCA1,CANTVED,CANTNH, USRMODIF, FCHMODI,HORMOD,NUMCAR)"
        + " VALUES("+vedfalt.getCodigoEmpresa()+","+vedfalt.getCodigoTipoMovto()+","+vedfalt.getFechaMovto()+","+vedfalt.getNumerDocumento()+","+vedfalt.getCorrelativo()+", "+vedfalt.getCodigoBodega()+","+vedfalt.getCodigoArticulo()+",'"+vedfalt.getDigitoArticulo()+"','"+vedfalt.getFormato()+"',"+vedfalt.getCantidadFormato()+","+vedfalt.getCantidadArticulo()+","+vedfalt.getCantidadVedmar()+","+vedfalt.getCantidadNoHay()+","+vedfalt.getUsuarioModifico()+","+vedfalt.getFechaUsuario()+","+vedfalt.getHoraUsuario()+","+vedfalt.getNumeroCarguio()+") " ;
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
	
	public void actualizaDatosVedfalt(VecmarDTO vecmar){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VEDFALT " + 
        " set  VENFEC="+vecmar.getFechaDocumento()+"  Where VENEMP="+vecmar.getCodigoEmpresa()+" AND VENCOD="+vecmar.getCodTipoMvto()+" AND VENFEC="+vecmar.getFechaMvto()+" AND VENNUM="+vecmar.getNumDocumento()+" " ;
		log.info("ACTUALIZA VECMAR DATOS :"+sqlObtenerVecmar);
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
	
	
	public List obtenerNohay(int empresa, int tipoMov, int fecha, int numero){
		
		VedfaltDTO vedfalt = null;
		PreparedStatement pstmt =null;
		List lista = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDFALT " + 
        " Where VENEMP="+empresa+" AND VENCOD="+tipoMov+" AND VENFEC="+fecha+" AND VENNUM='"+numero+"'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL VEDFALT" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedfalt = new VedfaltDTO();
				vedfalt.setCodigoEmpresa(rs.getInt("VENEMP"));
				vedfalt.setCodigoTipoMovto(rs.getInt("VENCOD"));
				vedfalt.setFechaMovto(rs.getInt("VENFEC"));
				vedfalt.setNumerDocumento(rs.getInt("VENNUM"));
				vedfalt.setCorrelativo(rs.getInt("VENCOR"));
				vedfalt.setCodigoBodega(rs.getInt("VENC15"));
				vedfalt.setCodigoArticulo(rs.getInt("VENC16"));
				vedfalt.setDigitoArticulo(rs.getString("VENDI1"));
				vedfalt.setFormato(rs.getString("VENFOR"));
				vedfalt.setCantidadFormato(rs.getInt("VENCA2"));
				vedfalt.setCantidadArticulo(rs.getInt("VENCA1"));
				vedfalt.setCantidadVedmar(rs.getInt("CANTVED"));
				vedfalt.setCantidadNoHay(rs.getInt("CANTNH"));
				vedfalt.setNumeroCarguio(rs.getInt("NUMCAR"));
				lista.add(vedfalt);
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
		
		
		return lista;
	}
	
	public void eliminaDatosVedfalt(VedfaltDTO vecmar){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="delete from "+
        "  CASEDAT.VEDFALT " + 
        " Where VENEMP="+vecmar.getCodigoEmpresa()+" AND VENCOD="+vecmar.getCodigoTipoMovto()+" AND VENFEC="+vecmar.getFechaMovto()+" AND VENNUM="+vecmar.getNumerDocumento()+" AND VENCOR="+vecmar.getCorrelativo()+" " ;
		log.info("ELIMINA VEDFALT DATOS :"+sqlObtenerVecmar);
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
	
	public int actualizaArticuloVedfalt(VedfaltDTO vedfalt){
		int res=0;
		PreparedStatement pstmt =null;
		
		//" venpes = venpes + "+vedmar.getPesoLinea()+","+
		//" venvol = venvol + "+vedmar.getVolumenArticulo()+","+
		
		String sqlObtenerVecmar="UPDATE CASEDAT.VEDFALT " +
				" SET CANTVED = "+vedfalt.getCantidadVedmar()+","+
				" CANTNH = "+vedfalt.getCantidadNoHay()+
				
				" WHERE VEDEMP="+vedfalt.getCodigoEmpresa()+
				" AND VENCOD="+vedfalt.getCodigoTipoMovto()+
				" AND VENFEC="+vedfalt.getFechaMovto()+
				" AND VENNUM="+vedfalt.getNumerDocumento()+
				" AND VENCOR="+vedfalt.getCorrelativo();
		log.info("ACTUALIZO ARTICULO VEDFALT " + sqlObtenerVecmar);
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
