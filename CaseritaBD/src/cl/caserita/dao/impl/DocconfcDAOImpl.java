package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DocconfcDAO;
import cl.caserita.dto.DetlibDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DocconfcDTO;

public class DocconfcDAOImpl implements DocconfcDAO{
	private static Logger log = Logger.getLogger(DocconfcDAOImpl.class);

	private Connection conn;
	
	public DocconfcDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int insertaDocumentoCarguio(DocconfcDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DOCCONFC " + 
        " (CACEMP,NUMCAR,VECPAT,CAMCO1,CLCNUM, FCHCONF) VALUES("+gen.getCodigoEmpresa()+","+gen.getNumeroCarguio()+",'"+gen.getPatente()+"',"+gen.getCodigoBodega()+","+gen.getNumeroDocumento()+","+gen.getFechaConfirmacion()+")";
		//System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	
	public DocconfcDTO recuperaDocumento(DocconfcDTO gen){
		DocconfcDTO docconfcDTO = null;
		
		PreparedStatement pstmt =null;
		List detord = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * from "+
        " CASEDAT.DOCCONFC " + 
        " WHERE CACEMP="+gen.getCodigoEmpresa()+" AND NUMCAR ="+gen.getNumeroCarguio()+" AND VECPAT ='"+gen.getPatente()+"' AND CAMCO1="+gen.getCodigoBodega()+"  FOR READ ONLY" ;
		System.out.println("Query:"+sqlObtenerCamtra);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				docconfcDTO = new DocconfcDTO();
				docconfcDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				docconfcDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				docconfcDTO.setPatente(rs.getString("VECPAT"));
				docconfcDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				docconfcDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				docconfcDTO.setFechaConfirmacion(rs.getInt("FCHCONF"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return docconfcDTO;
	}
	
}
