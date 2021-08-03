package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DocerrorDAO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DocerrorDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public class DocerrorDAOImpl implements DocerrorDAO{
	private static Logger log = Logger.getLogger(DocerrorDAOImpl.class);

private Connection conn;
	
	public DocerrorDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int insertaVecmar(VecmarDTO vecmar){
		int correlativo=-100;
		
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO  "+
        "  CASEDAT.VECMAR " + 
        " (VENEMP, VENCOD, VENFEC, VENNUM, VENCO5,VENNU2, VENBO2, VECFOR, VENCAN, VECTOT,VECTO1, VENTO1, VENTO2, VENRUT, VENDIG,VENCO4, VENSWP, VECCO7 ) VALUES(2,"+vecmar.getCodTipoMvto()+","+vecmar.getFechaMvto()+","+vecmar.getNumDocumento()+","+vecmar.getCodigoDocumento()+","+vecmar.getNumeroTipoDocumento()+","+vecmar.getCodigoBodega()+",'"+vecmar.getFormaPago().trim()+"',"+vecmar.getCantidadLineaDetalle()+","+vecmar.getTotalBruto()+","+vecmar.getTotalNeto()+","+vecmar.getTotalIva()+","+vecmar.getTotalDocumento()+","+vecmar.getRutProveedor()+",'"+vecmar.getDvProveedor()+"',"+vecmar.getCodigoVendedor()+","+vecmar.getSwichProceso()+","+vecmar.getCodigoTipoVendedor()+") " ;
		log.info("SQL Detord" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			  
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int insertaVedmar(VedmarDTO vedmar){
		int correlativo=-100;
		
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO  "+
        "  CASEDAT.VEDMAR " + 
        " (VEDEMP, VENC11, VENFE3, VENNU4, VENCOR,VENC15, VENC16, VENDI1, VENFOR, VENCA2,VENCA1, VEDPR2, VEDPRN, VEDCNT, VEDCTN,VEDMO4, VEDMNT, VENDES, VENMON, VENMDN,VENMO2, VENMTN, VENEXE, VENSW1 ) VALUES(2,"+vedmar.getCodTipoMvto()+","+vedmar.getFechaMvto()+","+vedmar.getNumDocumento()+","+vedmar.getCorrelativo()+","+vedmar.getCodigoBodega()+","+vedmar.getCodigoArticulo()+",'"+vedmar.getDigArticulo()+"','"+vedmar.getFormato()+"',"+vedmar.getCantidadFormato()+","+vedmar.getCantidadArticulo()+","+vedmar.getPrecioUnidad()+","+vedmar.getPrecioNeto()+",0,0,"+vedmar.getMontoBrutoLinea()+","+vedmar.getMontoNeto()+","+vedmar.getPorcentajeDesto()+","+vedmar.getMontoDescuentoLinea()+","+vedmar.getMontoDescuentoNeto()+","+vedmar.getMontoTotalLinea()+","+vedmar.getMontoTotalNetoLinea()+","+vedmar.getMontoExento()+","+vedmar.getSwitchProceso()+") " ;
		log.info("SQL Detord" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			     
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	
	public List listaDocumentosError(){
		DetordDTO detordDTO = null;
		
		PreparedStatement pstmt =null;
		List docerror = new ArrayList();
		DocerrorDTO dto = null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * from "+
        " CASEDAT.DOCERROR " + 
        "   FOR READ ONLY" ;
		log.info("Query:"+sqlObtenerCamtra);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				dto = new DocerrorDTO();
				dto.setEmpresa(rs.getInt("VENEMP"));
				dto.setTipoMov(rs.getInt("VENCOD"));
				dto.setFecha(rs.getInt("VENFEC"));
				dto.setNumero(rs.getInt("VENNUM"));
				dto.setNumeroDocumento(rs.getInt("DOCGEN"));
				dto.setUrlXML(rs.getString("CAMRUD"));
				dto.setUrlPDF(rs.getString("CAMPDF"));
				docerror.add(dto);
				
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
		return docerror;
	}
}
