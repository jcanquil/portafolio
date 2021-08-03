package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DocGenelDAO;
import cl.caserita.dto.DocgenelDTO;
import cl.caserita.dto.EndPointWSDTO;
import cl.caserita.dto.VecmarDTO;

public class DocGenelDAOImpl implements DocGenelDAO {
	private static Logger log = Logger.getLogger(DocGenelDAOImpl.class);

	private Connection conn;
	
	public DocGenelDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public String buscaEndPoint(){
		EndPointWSDTO endPoint = null;
		String endPointURL="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.DOCGENEL " + 
        " Where VENEMP=2 AND VENCOD=2 AND VENFEC=20160427 FETCH FIRST 1 ROWS ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				endPointURL = rs.getString("TIMELT");
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
		return endPointURL;
	}
	
	public DocgenelDTO recuperaFolio(int empresa, int tipoMov, int fecha, int numero){
		EndPointWSDTO endPoint = null;
		DocgenelDTO dto = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.DOCGENEL " + 
        " Where VENEMP="+empresa+" AND VENCOD="+tipoMov+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dto = new DocgenelDTO();
				dto.setCodigoEmpresa(rs.getInt("VENEMP"));
				dto.setCodigoTipoMovimiento(rs.getInt("VENCOD"));
				dto.setFechaMovimiento(rs.getInt("VENFEC"));
				dto.setNumeroDocumento(rs.getInt("VENNUM"));
				dto.setFolioDocumento(rs.getInt("DOCGEN"));
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
		return dto;
	}
	
	public void actualizaDocgenelGuiaOT(int empresa, int numeroInterno, int bodega, int nroGuia, int numeroCarguio){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE "+
        " CASEDAT.DOCNCP " + 
        " SET vednu2 = "+nroGuia+","+
        " numcar = "+numeroCarguio+
        " WHERE conemp = "+empresa+
        " AND contip='G'"+
        " AND connu2="+numeroInterno;
		
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
}
