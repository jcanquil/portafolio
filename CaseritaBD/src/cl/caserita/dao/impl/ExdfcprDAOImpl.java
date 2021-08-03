package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExdfcprDAO;
import cl.caserita.dto.CotplcDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdfcprDTO;

public class ExdfcprDAOImpl implements ExdfcprDAO{
	private static Logger log = Logger.getLogger(ExdfcprDAOImpl.class);

	private Connection conn;
	
	public ExdfcprDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int buscaFolioExdfcr(int empresa,int numeroDoc, int rut, String dv, int codigoDoc){
		
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.EXDFCPR " + 
        " Where EXMEMP="+empresa+" AND PRMRUT="+rut+" AND PRMDIG='"+dv+"' AND FCTNUM="+numeroDoc+" AND TPACO1="+codigoDoc+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			log.info("SQL EXDFCPR" + sqlObtenerConarc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				folio = rs.getInt("FCTNUM");
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return folio;
	}
	
	public List buscaDocumentosSinPDF(){
		
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List doc = new ArrayList();
		ExdfcprDTO exdfcpr = null;
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.EXDFCPR " + 
        " Where FCTFOC>="+20150101+" AND URLPROV='' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			log.info("SQL EXDFCPR" + sqlObtenerConarc);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdfcpr = new ExdfcprDTO();
				exdfcpr.setEmpresa(rs.getInt("EXMEMP"));
				exdfcpr.setRutProveedor(rs.getInt("PRMRUT"));
				exdfcpr.setCodDocumento(rs.getInt("TPACO1"));
				exdfcpr.setNumeroDocumento(rs.getInt("FCTNUM"));
				doc.add(exdfcpr);
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return doc;
	}
	
	public int generaFacturas(int empresa,String numOrden, int numeroDoc, int rut, String dv, int codigoDoc, int fecha, double neto, double impuesto, double impuAdic, double exento,double total,String tipoIngreso, String url, String razon, String direccion){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXDFCPR " + 
        " ( EXMEMP,EXDNOC, PRMRUT, PRMDIG, FCTNUM, TPACO1,ESFACT,FCTFOC,FCTVNE,FCTINE,FCTIMADC,FCTMEXE,FCTTNE,TIPINGRE,URLPROV, NOMPRO, DIRPRO) VALUES("+empresa+",'"+numOrden+"',"+rut+",'"+dv+"',"+numeroDoc+","+codigoDoc+",'I',"+fecha+","+neto+","+impuesto+","+impuAdic+","+exento+","+total+",'"+tipoIngreso+"','"+url+"','"+razon.trim()+"','"+direccion.trim()+"') " ;
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
	
	public int actualizaPDF(int empresa, int numeroDoc, int rut,  int codigoDoc, String url){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.EXDFCPR " + 
        " SET URLPROV='"+url+"' Where EXMEMP="+empresa+" AND PRMRUT="+rut+" AND TPACO1="+codigoDoc+" AND FCTNUM="+numeroDoc+"  " ;
		log.info("Actualiza PDF Documento" + sqlObtenerVecmar);
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
	
	public int actualizaEstado(int empresa, int rutProv, String dvProv, int codDocto, int numeroDoc, String estado){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.EXDFCPR " + 
        " SET ESFACT = '"+estado+"'"+
        " WHERE exmemp ="+empresa+
        " AND prmrut = "+rutProv+
        " AND prmdig = '"+dvProv+"'"+
        " AND tpaco1 = "+codDocto+
        " AND fctnum = "+numeroDoc;
		
		log.info("Actualiza Estado Documento" + sqlObtenerVecmar);
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
	
	public ExdfcprDTO buscaDocumentoExdfcr(int empresa, int numeroOC, int numeroDoc, int rut, String dv){
		ExdfcprDTO exdfcprDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerConarc=" SELECT * "+
        " FROM CASEDAT.EXDFCPR " + 
        " WHERE exmemp ="+empresa+
       // " AND exdnoc LIKE '%"+numeroOC+"%'"+
        " AND prmrut = "+rut+
        " AND prmdig = '"+dv+"'"+
        " AND fctnum = "+numeroDoc+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			log.info("SQL EXDFCPR" + sqlObtenerConarc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdfcprDTO = new ExdfcprDTO();
				
				exdfcprDTO.setEmpresa(rs.getInt("EXMEMP"));
				exdfcprDTO.setNumeroOrden(rs.getString("EXDNOC"));
				exdfcprDTO.setRutProveedor(rs.getInt("PRMRUT"));
				exdfcprDTO.setDvProveedor(rs.getString("PRMDIG"));
				exdfcprDTO.setCodDocumento(rs.getInt("TPACO1"));
				exdfcprDTO.setNumeroDocumento(rs.getInt("FCTNUM"));
				exdfcprDTO.setEstadoDocto(rs.getString("ESFACT"));
				exdfcprDTO.setFechaDocto(rs.getInt("FCTFOC"));
				exdfcprDTO.setTotalNeto(rs.getInt("FCTVNE"));
				exdfcprDTO.setTotalIva(rs.getInt("FCTINE"));
				exdfcprDTO.setTotalImptos(rs.getInt("FCTIMADC"));
				exdfcprDTO.setTotalExento(rs.getInt("FCTMEXE"));
				exdfcprDTO.setTotalBruto(rs.getInt("FCTTNE"));
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		return exdfcprDTO;
	}
	
	public int actualizaNumeroOrden(int empresa, int rutProv, String dvProv,  int numeroDoc, String numeroOC){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.EXDFCPR " + 
        " SET EXDNOC = '"+numeroOC+"'"+
        " WHERE exmemp ="+empresa+
        " AND prmrut = "+rutProv+
        " AND prmdig = '"+dvProv+"'"+
        //" AND tpaco1 = "+codDocto+
        " AND fctnum = "+numeroDoc;
		
		log.info("Actualiza Estado Documento" + sqlObtenerVecmar);
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
