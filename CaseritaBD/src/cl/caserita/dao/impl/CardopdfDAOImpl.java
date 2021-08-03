package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CardopdfDAO;
import cl.caserita.dto.CardopdfDTO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CarmailDTO;

public class CardopdfDAOImpl implements  CardopdfDAO{

	private  static Logger log = Logger.getLogger(CardopdfDAOImpl.class);

	private Connection conn;
	
	public CardopdfDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List buscaDocumentosPendientes(int empresa, int bodega, String cuentaCorreo, int estado){
		CardopdfDTO cardoPDFDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.CARDOPDF WHERE CACEMP="+empresa+" AND CAMCO1='"+bodega+"' AND ASTC01="+empresa+" AND EXMC11="+bodega+" AND CODCTCO='"+cuentaCorreo+"' AND ESTPROC=0  ORDER BY NUMING FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				cardoPDFDTO = new CardopdfDTO();
				cardoPDFDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				cardoPDFDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				cardoPDFDTO.setPatente(rs.getString("VECPAT"));
				cardoPDFDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				cardoPDFDTO.setCodigoEmpresaMail(rs.getInt("AA12A"));
				cardoPDFDTO.setCodigoBodegaMail(rs.getInt("EXMC11"));
				cardoPDFDTO.setCodigoCuentaCorreo(rs.getString("CODCTCO"));
				cardoPDFDTO.setNumeroIngreso(rs.getInt("NUMING"));
				cardoPDFDTO.setCodigoDocumento(rs.getInt("CLCCO2"));
				cardoPDFDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				cardoPDFDTO.setRutaDocumentoPDF(rs.getString("CAMPDF"));
				cardoPDFDTO.setNombrePDF(rs.getString("NOMPDF"));
				cardoPDFDTO.setEstadoProcesado(rs.getInt("ESTPROC"));
				cardoPDFDTO.setFechaUsuario(rs.getInt("FCHUSR"));
				cardoPDFDTO.setHoraUsuario(rs.getInt("HORUSR"));
				cardoPDFDTO.setNombreEquipo(rs.getString("NMEQUI"));
				cardoPDFDTO.setIpEquipo(rs.getString("IPEQUI"));
				cardoPDFDTO.setUsuario(rs.getString("USRSYS"));
				lista.add(cardoPDFDTO);
				
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
		return lista;

	}
	
	public CardopdfDTO buscaDocumentosPendientesIndividual(int empresa, int bodega, String cuentaCorreo, int estado){
		CardopdfDTO cardoPDFDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.CARDOPDF WHERE CACEMP="+empresa+" AND CAMCO1='"+bodega+"' AND AA12A="+empresa+" AND EXMC11="+bodega+" AND CODCTCO='"+cuentaCorreo+"' AND ESTPROC=0  ORDER BY NUMING fetch first 1 rows only" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				cardoPDFDTO = new CardopdfDTO();
				cardoPDFDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				cardoPDFDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				cardoPDFDTO.setPatente(rs.getString("VECPAT").trim());
				cardoPDFDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				cardoPDFDTO.setCodigoEmpresaMail(rs.getInt("AA12A"));
				cardoPDFDTO.setCodigoBodegaMail(rs.getInt("EXMC11"));
				cardoPDFDTO.setCodigoCuentaCorreo(rs.getString("CODCTCO"));
				cardoPDFDTO.setNumeroIngreso(rs.getInt("NUMING"));
				cardoPDFDTO.setCodigoDocumento(rs.getInt("CLCCO2"));
				cardoPDFDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				cardoPDFDTO.setRutaDocumentoPDF(rs.getString("CAMPDF"));
				cardoPDFDTO.setNombrePDF(rs.getString("NOMPDF"));
				cardoPDFDTO.setEstadoProcesado(rs.getInt("ESTPROC"));
				cardoPDFDTO.setFechaUsuario(rs.getInt("FCHUSR"));
				cardoPDFDTO.setHoraUsuario(rs.getInt("HORUSR"));
				cardoPDFDTO.setNombreEquipo(rs.getString("NMEQUI"));
				cardoPDFDTO.setIpEquipo(rs.getString("IPEQUI"));
				cardoPDFDTO.setUsuario(rs.getString("USRSYS"));
				
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
		return cardoPDFDTO;

	}
	
	
	
	public int actualizaDatos(CardopdfDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARDOPDF " + 
        " SET CAMPDF='"+dto.getRutaDocumentoPDF()+"', NOMPDF='"+dto.getNombrePDF()+"', ESTPROC=1 Where CACEMP="+dto.getCodigoEmpresa()+" AND CAMCO1="+dto.getCodigoBodega()+" AND NUMCAR="+dto.getNumeroCarguio()+" AND VECPAT='"+dto.getPatente()+"' AND NUMING="+dto.getNumeroIngreso()+" AND CLCNUM="+dto.getNumeroDocumento()+"  " ;
		log.info("SQL ACTUALIZA CARDOPDF "+ sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			pstmt.executeUpdate();
			actualiza=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		pstmt=null;
		
		
		
		return actualiza;
	}
}
