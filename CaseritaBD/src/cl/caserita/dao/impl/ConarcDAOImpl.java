package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dto.ExdfcprDTO;
import cl.caserita.dto.LibroTotalesDTO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.ConarcDTO;

public class ConarcDAOImpl implements ConarcDAO {

	private  static Logger log = Logger.getLogger(ConarcDAOImpl.class);

	
	private Connection conn;
	
	public ConarcDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int buscaDocumento(int numeroDocumento, int codDocumento, int fecha, int rut, String dv){
		ConarcDTO conarcDTO = null;
		int num=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CONARC " + 
        " Where CONCO2="+codDocumento+" AND CONNUM="+numeroDocumento+" AND CONFEC="+fecha+" AND CONRUT="+rut+" AND CONDIG='"+dv+"' FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
		
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				num=rs.getInt("CONNUM");
				
				
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
		
		return num;
		
	}
	
	public int buscaDocumentoContabilizado(int numeroDocumento, int codDocumento, int rut, String dv){
		ConarcDTO conarcDTO = null;
		int num=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CONARC " + 
        " Where CONCO2="+codDocumento+" AND CONNUM="+numeroDocumento+" AND CONRUT="+rut+" AND CONDIG='"+dv+"' FOR READ ONLY" ;
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
		
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				num=rs.getInt("CONFOL");
				
				
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
		
		return num;
		
	}
	public int buscaFolioDoc(int empresa,int ano, int mes, int codDocumento){
		
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.CONARC " + 
        " Where CONCEM="+empresa+" AND CONAOD="+ano+" AND CONMES="+mes+" AND CONCO2="+codDocumento+" ORDER BY CONFOL DESC fetch first 1 rows only  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				folio = rs.getInt("CONFOL");
				
				
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
	
	public int buscaFolioExdfcr(int empresa,int numeroDoc, int rut, String dv, int codigoDoc){
		
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.EXDFCPR " + 
        " Where EXMEMP="+empresa+" AND PRMRUT="+rut+" AND PRMDIG='"+dv+"' AND FCTNUM="+numeroDoc+" AND TPACO1="+codigoDoc+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			System.out.println("SQL EXDFCPR" + sqlObtenerConarc);
			
			
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
			
			System.out.println("SQL EXDFCPR" + sqlObtenerConarc);
			
			
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
		System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
		System.out.println("Actualiza PDF Documento" + sqlObtenerVecmar);
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
	
	public int generaImpuestoFacturas(int empresa,String numOrden, int numeroDoc, int rut, String dv, int codigoDoc, double codImpto, double total){
		int res=0;
		PreparedStatement pstmt =null;
		 
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXDFCIM " + 
        " ( EXMEMP,EXDNOC, PRMRUT, PRMDIG, FCTNUM, TPACO1,CLCCO3,CLCMO3) VALUES("+empresa+",'"+numOrden+"',"+rut+",'"+dv+"',"+numeroDoc+","+codigoDoc+","+codImpto+","+total+") " ;
		System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	public int generaDoc(ConarcDTO conarc){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CONARC " + 
        " (CONCEM ,CONCO2,CONRUT,CONDIG,CONNUM,CONFEC,CONNO1,CONVAL,CONVA1,CONTOT,CONFOL,CONAOD,CONMES) VALUES("+conarc.getCodigoEmpresa()+","+conarc.getCodDocumento()+","+conarc.getRutProveedor()+",'"+conarc.getDigitoProveedor()+"',"+conarc.getNumeroDocumento()+", "+conarc.getFechaDocumento()+",'"+conarc.getNombreProveedor()+"',"+conarc.getValorNeto()+", "+conarc.getValorNetoExento()+", "+conarc.getValorTotalDocumento()+", "+conarc.getFolio()+","+conarc.getAno()+","+conarc.getMes()+") " ;
		System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	public LibroTotalesDTO recuperaTotalesPorDocumento(int ano, int mes, int codDocumento){
		LibroTotalesDTO libroTotalesDTO =null;
		List doc = new ArrayList();
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select COUNT(*) AS CANT, SUM ( CONVAL) AS NETO , SUM( CONTOT ) AS TOTAL , SUM(CONVA1) AS EXENTO"+
        " from CASEDAT.CONARC " + 
        " Where CONCO2="+codDocumento+" AND CONAOD="+ano+" AND CONMES="+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				libroTotalesDTO = new LibroTotalesDTO();
				libroTotalesDTO.setCantidadDocumentos(rs.getInt("CANT"));
				libroTotalesDTO.setTotalMonto(rs.getLong("TOTAL"));
				libroTotalesDTO.setTotalMontoNeto(rs.getLong("NETO"));
				libroTotalesDTO.setTotalMontoExento(rs.getInt("EXENTO"));
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
		
		return libroTotalesDTO;
	}
	
	public LibroTotalesDTO recuperaTotalesIva(int ano, int mes, int codDocumento, LibroTotalesDTO dto){
		LibroTotalesDTO libroTotalesDTO =null;
		List doc = new ArrayList();
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select SUM( C2.CONMON ) AS IVA"+
        " from CASEDAT.CONARC C1, CASEDAT.CONAR1 C2" + 
        " Where C1.CONCO2="+codDocumento+" AND C1.CONAOD="+ano+" AND C1.CONMES="+mes+" AND C1.CONCO2= C2.CONCO3 AND C1.CONRUT= C2.CONRU7 AND C1.CONDIG=C2.CONDI4 AND C1.CONNUM= C2.CONNU1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				libroTotalesDTO = new LibroTotalesDTO();
				libroTotalesDTO = dto;
				libroTotalesDTO.setTotalMontoIva(rs.getInt("IVA"));
				//LibroTotalesDTO totales = recuperaTotalesPorDocumento(codDocumento,ano,mes);
				libroTotalesDTO.setCantidadDocumentos(dto.getCantidadDocumentos());
				libroTotalesDTO.setTotalMonto(dto.getTotalMonto());
				libroTotalesDTO.setTotalMontoNeto(dto.getTotalMontoNeto());
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
		
		return libroTotalesDTO;
	}
	
	
	public List buscaDocumentos(int codigo, int ano, int mes){
		ConarcDTO conarcDTO =null;
		List doc = new ArrayList();
		int folio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerConarc="Select * "+
        " from CASEDAT.CONARC " + 
        " Where CONCO2="+codigo+" and CONAOD="+ano+" AND CONMES="+mes+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			//System.out.println("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				conarcDTO = new ConarcDTO();
				conarcDTO.setCodDocumento(rs.getInt("CONCO2"));
				conarcDTO.setRutProveedor(rs.getInt("CONRUT"));
				conarcDTO.setDigitoProveedor(rs.getString("CONDIG"));
				conarcDTO.setFechaDocumento(rs.getInt("CONFEC"));
				conarcDTO.setNombreProveedor(rs.getString("CONNO1"));
				conarcDTO.setFolio(rs.getInt("CONFOL"));
				conarcDTO.setAno(rs.getInt("CONAOD"));
				conarcDTO.setMes(rs.getInt("CONMES"));
				conarcDTO.setValorNeto(rs.getInt("CONVAL"));
				conarcDTO.setValorNetoExento(rs.getInt("CONVA1"));
				conarcDTO.setValorTotalDocumento(rs.getInt("CONTOT"));
				conarcDTO.setNumeroDocumento(rs.getInt("CONNUM"));
				
				doc.add(conarcDTO);
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
	
	public ConarcDTO buscaTotalesDocumento(int empresa, int rut, String dv, int numeroDocto){
		ConarcDTO conarcDTO =null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerConarc="SELECT * "+
        " FROM CASEDAT.CONARC " + 
        " WHERE concem = "+empresa+
        //" AND conco2 = "+tipdocto+
        " AND conrut = "+rut+
        " AND condig = '"+dv+
        "' AND connum = "+numeroDocto+
        " FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConarc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				conarcDTO = new ConarcDTO();
				conarcDTO.setCodDocumento(rs.getInt("CONCO2"));
				conarcDTO.setRutProveedor(rs.getInt("CONRUT"));
				conarcDTO.setDigitoProveedor(rs.getString("CONDIG"));
				conarcDTO.setFechaDocumento(rs.getInt("CONFEC"));
				conarcDTO.setNombreProveedor(rs.getString("CONNO1"));
				conarcDTO.setFolio(rs.getInt("CONFOL"));
				conarcDTO.setAno(rs.getInt("CONAOD"));
				conarcDTO.setMes(rs.getInt("CONMES"));
				conarcDTO.setValorNeto(rs.getInt("CONVAL"));
				conarcDTO.setValorNetoExento(rs.getInt("CONVA1"));
				conarcDTO.setValorTotalDocumento(rs.getInt("CONTOT"));
				conarcDTO.setNumeroDocumento(rs.getInt("CONNUM"));
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
		return conarcDTO;	
	}
	
	public int buscaFolioDisponible(int empresa, int ano, int mes){
		ConarcDTO conarcDTO = null;
		int numFolio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCamtra=" SELECT MAX(CONFOL) + 1 AS FOLIO "+
        " FROM CASEDAT.CONARC " + 
        " WHERE concem = "+empresa+
        " AND conaod = "+ano+
        " AND conmes = "+mes+
        " FOR READ ONLY";
		
		System.out.println("Actualiza Folio y Periodo Documento" + sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				numFolio=rs.getInt("FOLIO");
				
				if(numFolio==0){
					numFolio=1;
				}
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
		return numFolio;	
	}
	
	public int actualizaFolioLibro(int empresa, int tipodocto, int nrodocumento, int rutprov, String dvprov, int folio, int ano, int mes){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar=" UPDATE"+
        " CASEDAT.CONARC " + 
        " SET confol = "+folio+","+
        " conaod = "+ano+","+
        " conmes = "+mes+
        " WHERE concem = "+empresa+
        " AND conco2 = "+tipodocto+
        " AND conrut = "+rutprov+
        " AND condig = '"+dvprov+"'"+
        " AND connum = "+nrodocumento;
		
		System.out.println("Actualiza Folio y Periodo Documento" + sqlObtenerVecmar);
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
