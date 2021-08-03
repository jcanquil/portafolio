package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConnohDAO;
import cl.caserita.dto.ConnohDTO;

public class ConnohDAOImpl implements ConnohDAO{

	private static Logger log = Logger.getLogger(ConnohDAOImpl.class);
	private Connection conn;
	
	public ConnohDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ConnohDTO buscaConnoh(int empresa, String tipNota, int numNota, int fechaEmision){
		ConnohDTO connoh = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CONNOH " + 
        " Where CONEMP="+empresa+" and CONTIP='"+tipNota+"' AND CONNU2="+numNota+" AND CONFE2='"+fechaEmision+"' FOR READ ONLY" ;
		List cldmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				connoh = new ConnohDTO();
				connoh.setTipoNota(rs.getString("CONTIP"));
				connoh.setNumeroNota(rs.getInt("CONNU2"));
				connoh.setFechaNota(rs.getInt("CONFE2"));
				connoh.setCodDocumento(rs.getInt("CONTI1"));
				connoh.setCodigoBodega(rs.getInt("CONCO1"));
				connoh.setCodigoMovimiento(rs.getInt("CONCOD"));
				connoh.setNumeroDocumento(rs.getInt("CONNRO"));
				connoh.setRutCliente(rs.getInt("CONRU1"));
				connoh.setDivCliente(rs.getString("CONDI1"));
				connoh.setCodigoVendedor(rs.getInt("CONCO6"));
				connoh.setNombreCliente(rs.getString("CONNO2"));
				connoh.setMontoTotal(rs.getInt("CONMO1"));
				connoh.setMontoNeto(rs.getInt("CONMO2"));
				connoh.setMontoIva(rs.getInt("CONMO4"));
				connoh.setMontoImptoAdicional(rs.getInt("CONMO5"));
				connoh.setEstado(rs.getString("CONEST"));
				connoh.setResponsableNota(rs.getString("CONRES"));
				
				
				
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
		
		return connoh;
		
	}
	public int actualizaConnoh(int empresa, String tipoNota, int numNota, int fechaEmision, int numero){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerConnoh ="UPDATE "+
        " CASEDAT.CONNOH " + 
        " SET CONNU2="+numero+" Where CONEMP="+empresa+" and CONTIP='"+tipoNota+"' AND CONNU2="+numNota+" AND CONFE2="+fechaEmision+" " ;
		log.info(sqlObtenerConnoh);
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnoh);
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
		
		
		
		
		return actualiza;
	}
	
	
	
	
	public int insertaConnoh(ConnohDTO dto){
		int correlativo =-100;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String cmp ="CONEMP, CONTIP, CONNU2, CONFE2, CONTI1, CONNRO, CONRU1, CONDI1, CONCOD, CONCO1, CONCO6, CONNO2,"+
					"CONCTL, CONTCS, CONTCN, CONTDS, CONTDN, CONMO1, CONMO2, CONMO4, CONMO5, CONTEN, CONEST, CONRES";
		
		String sqlObtenerCldmco ="INSERT INTO  "+
		        " CASEDAT.CONNOH ("+cmp+") "+
				" VALUES (2,'"+dto.getTipoNota()+"',"+dto.getNumeroNota()+","+dto.getFechaNota()+","+dto.getCodDocumento()+","+
		        dto.getNumeroDocumento()+","+dto.getRutCliente()+",'"+dto.getDivCliente()+"',2,26,"+dto.getCodigoVendedor()+",'"+
				dto.getNombreCliente().trim()+"',"+dto.getCantidadLineas()+","+dto.getTotalCosto()+","+dto.getTotalCostoNeto()+","+
		        dto.getTotalDescuento()+","+dto.getTotalDescuentoNeto()+","+dto.getMontoTotal()+","+dto.getMontoNeto()+","+
				dto.getMontoIva()+","+dto.getMontoImptoAdicional()+","+dto.getMontoExento()+",'"+dto.getEstado().trim()+"','"+dto.getResponsableNota()+"')";
				
			try{
				pstmt = conn.prepareStatement(sqlObtenerCldmco);
				pstmt.executeUpdate();
				
				//log.info("SQL Orden" + sqlObtenerCldmco);
				//log.info("Ok !! insert connoh");
				correlativo=1;
				
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
	
	public int buscaExisteConnoh(int empresa, int numNota){
		int existenumero=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerConnoh ="SELECT * "+
        " FROM CASEDAT.CONNOH " + 
        " WHERE CONEMP="+empresa+" AND CONNU2="+numNota+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnoh);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			existenumero=1;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

			
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return existenumero;
	}


	public int existeConnoh(ConnohDTO nota){
		int existenumero=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerConnoh ="SELECT * "+
        " FROM CASEDAT.CONNOH " + 
        " WHERE CONEMP=2 "+" AND CONTIP='"+nota.getTipoNota()+"' "+
        " AND CONNRO="+nota.getNumeroDocumento()+" "+
        " AND CONTI1="+nota.getCodDocumento()+" "+
        " AND CONRU1="+nota.getRutCliente()+" AND CONCO1="+nota.getCodigoBodega()+" "+
        " ORDER BY CONNU2 DESC " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnoh);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			existenumero=rs.getInt("CONNU2");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

			
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return existenumero;
	}
	
	public int eliminaConnoh (ConnohDTO nota){
		int elimina=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerConnod ="DELETE FROM "+
        " CASEDAT.CONNOH " + 
        " WHERE CONEMP=2 "+" AND CONTIP='"+nota.getTipoNota()+"' AND CONFE2="+nota.getFechaNota()+" "+
        " AND CONNRO="+nota.getNumeroDocumento()+" "+
        " AND CONRU1="+nota.getRutCliente()+" AND CONCO1="+nota.getCodigoBodega()+"" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnod);
			pstmt.executeUpdate();
			elimina=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return elimina;
	}
	
	public int eliminaConnohTransp (ConnohDTO nota){
		int elimina=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerConnod ="DELETE FROM "+
        " CASEDAT.CONNOH " + 
        " WHERE CONEMP=2 "+" AND CONTIP='"+nota.getTipoNota()+"' AND CONNRO="+nota.getNumeroDocumento()+" "+
        " AND CONRU1="+nota.getRutCliente()+" AND CONCO1="+nota.getCodigoBodega()+"" ;
		log.info("ANTES DE DELETE CONNOH  :  "+sqlObtenerConnod);
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnod);
			pstmt.executeUpdate();
			elimina=1;
			log.info("O K E Y   DELETE CONNOH  :  "+sqlObtenerConnod);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return elimina;
	}
}
