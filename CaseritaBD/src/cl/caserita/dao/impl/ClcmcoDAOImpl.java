package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ClcmcoDAO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.LibroTotalesDTO;

public class ClcmcoDAOImpl implements ClcmcoDAO{

	private  static Logger log = Logger.getLogger(ClcmcoDAOImpl.class);

	private Connection conn;
	
	public ClcmcoDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtieneClcmco(int empresa, int codigo, int rutCliente, String dv, int fecha, int numero){
		ClcmcoDTO clcmcoDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO1 " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigo+" AND CLCRUT="+rutCliente+" AND CLCDIG='"+dv+"' AND CLCFEC="+fecha+" AND CLCNUM="+numero+" FOR READ ONLY" ;
		List clcmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clcmco.add(clcmcoDTO);
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return clcmco;
	}
	
	public ClcmcoDTO obtieneClcmcoDTO(int empresa, int codigo, int rutCliente, String dv, int fecha, int numero){
		ClcmcoDTO clcmcoDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO1 " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigo+" AND CLCRUT="+rutCliente+" AND CLCDIG='"+dv+"' AND CLCFEC="+fecha+" AND CLCNUM="+numero+" FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return clcmcoDTO;
	}
	public int actualizaClcmco(int empresa, int codigo, int rutCliente, String dv, int fecha, int hora, int numero){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CLCMCO " + 
        " SET CLCNUM="+numero+"   Where CLCEMP="+empresa+" AND CLCCO1="+codigo+" AND CLCRUT="+rutCliente+" AND CLCDIG='"+dv+"' AND CLCFEC="+fecha+" AND CLCHOR="+hora+" " ;
		
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
		
		
		
		
		return actualiza;
	}
	public int generaClcmco(ClcmcoDTO clcmco){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CLCMCO " + 
        " (CLCEMP, CLCCO1,CLCRUT,CLCDIG,CLCFEC,CLCHOR,CLCNUM,CLCBOD,CLCCOD,CLCCAN,CLCTO4,CLCTO1,CLCTOT) VALUES("+clcmco.getEmpresa()+","+clcmco.getCodDocumento()+","+clcmco.getRutCliente()+",'"+clcmco.getDvCliente()+"',"+clcmco.getFechaMovimiento()+", "+clcmco.getHoraMovimiento()+" ,"+clcmco.getNumeroDocumento()+","+clcmco.getCodigoBodega()+" , "+clcmco.getCodigoVendedor()+") " ;
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
	
	public int obtieneFechaFactura(int empresa, int codigo, int rutCliente, String dv, int numero, int bodega){
		int fecha=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO11 " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigo+" AND CLCRUT="+rutCliente+" AND CLCDIG='"+dv+"' AND CLCBOD="+bodega+" AND CLCNUM="+numero+" FOR READ ONLY" ;
		List clcmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				fecha = rs.getInt("CLCFEC");
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return fecha;
	}
	
	public LibroTotalesDTO totales(int empresa, int codDocumento,int año, int mes){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select count(*) AS CANT, sum( CLCTEN ) AS EXENTO, sum( CLCTNT ) AS NETO, sum( CLCTO1 ) AS IVA,sum(CLCTOT ) AS TOTAL "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND  CLCTO1>0 AND CLCFEC BETWEEN "+año+" AND "+mes+"  and clcest=0 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				libro = new LibroTotalesDTO();
				libro.setCantidadDocumentos(rs.getInt("CANT"));
				libro.setTotalMontoExento(rs.getInt("EXENTO"));
				libro.setTotalMontoNeto(rs.getInt("NETO"));
				libro.setTotalMontoIva(rs.getInt("IVA"));
				libro.setTotalMonto(rs.getInt("TOTAL"));
				libro.setTotalDocumentosAnulados(totaldocNulos(empresa,codDocumento, mes, año));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return libro;
	}
	
	
	
	

	public LibroTotalesDTO totalesBodega(int empresa, int codDocumento,int año, int mes, int bodega){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select count(*) AS CANT, sum( CLCTEN ) AS EXENTO, sum( CLCTNT ) AS NETO, sum( CLCTO1 ) AS IVA,sum(CLCTOT ) AS TOTAL "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1>0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+año+" AND "+mes+" and clcest=0  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				libro = new LibroTotalesDTO();
				libro.setCantidadDocumentos(rs.getInt("CANT"));
				libro.setTotalMontoExento(rs.getInt("EXENTO"));
				libro.setTotalMontoNeto(rs.getInt("NETO"));
				libro.setTotalMontoIva(rs.getInt("IVA"));
				libro.setTotalMonto(rs.getInt("TOTAL"));
				libro.setTotalDocumentosAnulados(totaldocNulosBodega(empresa,codDocumento, mes, año, bodega));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return libro;
	}
	
	
	
	public LibroTotalesDTO totalesNC(int empresa, int codDocumento,int año, int mes){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select count(*) AS CANT, sum( CLCTEN ) AS EXENTO, sum( CLCTNT ) AS NETO, sum( CLCTO1 ) AS IVA,sum(CLCTOT ) AS TOTAL "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1>0 AND CLCFEC BETWEEN "+año+" AND "+mes+"  and clcest=0 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				libro = new LibroTotalesDTO();
				libro.setCantidadDocumentos(rs.getInt("CANT"));
				libro.setTotalMontoExento(rs.getInt("EXENTO"));
				libro.setTotalMontoNeto(rs.getInt("NETO"));
				libro.setTotalMontoIva(rs.getInt("IVA"));
				libro.setTotalMonto(rs.getInt("TOTAL"));
				libro.setTotalDocumentosAnulados(totaldocNulosNC(empresa, codDocumento, mes, año));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return libro;
	}
	
	public LibroTotalesDTO totalesBodegaNC(int empresa, int codDocumento,int año, int mes, int bodega){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select count(*) AS CANT, sum( CLCTEN ) AS EXENTO, sum( CLCTNT ) AS NETO, sum( CLCTO1 ) AS IVA,sum(CLCTOT ) AS TOTAL "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1>0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+año+" AND "+mes+"  and clcest=0 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				libro = new LibroTotalesDTO();
				libro.setCantidadDocumentos(rs.getInt("CANT"));
				libro.setTotalMontoExento(rs.getInt("EXENTO"));
				libro.setTotalMontoNeto(rs.getInt("NETO"));
				libro.setTotalMontoIva(rs.getInt("IVA"));
				libro.setTotalMonto(rs.getInt("TOTAL"));
				libro.setTotalDocumentosAnulados(totaldocNulosNCBodega(empresa, codDocumento, mes, año, bodega));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return libro;
	}
	
	public LibroTotalesDTO totalesExento(int empresa, int codDocumento,int año, int mes){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select count(*) AS CANT, sum( CLCTEN ) AS EXENTO, sum( CLCTNT ) AS NETO, sum( CLCTO1 ) AS IVA,sum(CLCTOT ) AS TOTAL "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1=0 AND CLCFEC BETWEEN "+año+" AND "+mes+"  and clcest=0 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				libro = new LibroTotalesDTO();
				libro.setCantidadDocumentos(rs.getInt("CANT"));
				libro.setTotalMontoExento(rs.getInt("EXENTO"));
				libro.setTotalMontoNeto(rs.getInt("NETO"));
				libro.setTotalMontoIva(rs.getInt("IVA"));
				libro.setTotalMonto(rs.getInt("TOTAL"));
				libro.setTotalDocumentosAnulados(totaldocNulosExentos(empresa, codDocumento, mes, año));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return libro;
	}
	
	public LibroTotalesDTO totalesBodegaExento(int empresa, int codDocumento,int año, int mes, int bodega){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select count(*) AS CANT, sum( CLCTEN ) AS EXENTO, sum( CLCTNT ) AS NETO, sum( CLCTO1 ) AS IVA,sum(CLCTOT ) AS TOTAL "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND  CLCTO1=0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+año+" AND "+mes+"  and clcest=0 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				libro = new LibroTotalesDTO();
				libro.setCantidadDocumentos(rs.getInt("CANT"));
				libro.setTotalMontoExento(rs.getInt("EXENTO"));
				libro.setTotalMontoNeto(rs.getInt("NETO"));
				libro.setTotalMontoIva(rs.getInt("IVA"));
				libro.setTotalMonto(rs.getInt("TOTAL"));
				libro.setTotalDocumentosAnulados(totaldocNulosExentosBodega(empresa, codDocumento, mes, año, bodega));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return libro;
	}
	
	public int totaldocNulos(int empresa, int codDocumento,int año, int mes){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int nulos=0;
		String sqlObtenerCldmco ="Select count(*) AS CANT "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1>0 AND CLCFEC BETWEEN "+mes+" AND "+año+"  AND CLCEST=1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
						
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				
				nulos = rs.getInt("CANT");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return nulos;
	}
	
	public int totaldocNulosBodega(int empresa, int codDocumento,int año, int mes, int bodega){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int nulos=0;
		String sqlObtenerCldmco ="Select count(*) AS CANT "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND  CLCTO1>0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+mes+" AND "+año+"  AND CLCEST=1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
						
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				
				nulos = rs.getInt("CANT");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return nulos;
	}
	
	public int totaldocNulosNC(int empresa, int codDocumento,int año, int mes){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int nulos=0;
		String sqlObtenerCldmco ="Select count(*) AS CANT "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" and CLCCO1="+codDocumento+" AND CLCTO1>0 and CLCFEC BETWEEN "+mes+" AND "+año+"  AND CLCEST=1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
						
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				
				nulos = rs.getInt("CANT");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return nulos;
	}
	
	public int totaldocNulosNCBodega(int empresa, int codDocumento,int año, int mes, int bodega){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int nulos=0;
		String sqlObtenerCldmco ="Select count(*) AS CANT "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1>0 and CLCBOD="+bodega+" and CLCFEC BETWEEN "+mes+" AND "+año+"  AND CLCEST=1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
						
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				
				nulos = rs.getInt("CANT");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return nulos;
	}
	public int totaldocNulosExentos(int empresa, int codDocumento,int año, int mes){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int nulos=0;
		String sqlObtenerCldmco ="Select count(*) AS CANT "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1=0 AND CLCFEC BETWEEN "+mes+" AND "+año+"  AND CLCEST=1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
						
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				
				nulos = rs.getInt("CANT");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return nulos;
	}
	
public int totaldocNulosExentosBodega(int empresa, int codDocumento,int año, int mes, int bodega){
		
		LibroTotalesDTO libro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int nulos=0;
		String sqlObtenerCldmco ="Select count(*) AS CANT "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codDocumento+" AND CLCTO1=0 and CLCBOD="+bodega+" AND CLCFEC BETWEEN "+mes+" AND "+año+"  AND CLCEST=1 FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
						
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				
				nulos = rs.getInt("CANT");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return nulos;
	}
	public List recuperaDocumentos(int empresa, int codigoDocumento, int ano, int mes){
		List clc = new ArrayList();
		ClcmcoDTO clcmcoDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigoDocumento+" AND CLCTO1>0 AND CLCFEC BETWEEN "+ano+" AND "+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clc.add(clcmcoDTO);
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		
		return clc;
		
		
	}
	
	public List recuperaDocumentosBodega(int empresa, int codigoDocumento, int ano, int mes, int bodega){
		List clc = new ArrayList();
		ClcmcoDTO clcmcoDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigoDocumento+" AND CLCTO1>0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+ano+" AND "+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
		
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clc.add(clcmcoDTO);
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		
		return clc;
		
		
	}
	
	public List recuperaDocumentosExento(int empresa, int codigoDocumento, int ano, int mes){
		List clc = new ArrayList();
		ClcmcoDTO clcmcoDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigoDocumento+" AND  CLCTO1=0 AND CLCFEC BETWEEN "+ano+" AND "+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clc.add(clcmcoDTO);
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		
		return clc;
		
		
	}
	
	public List recuperaDocumentosBodegaExento(int empresa, int codigoDocumento, int ano, int mes, int bodega){
		List clc = new ArrayList();
		ClcmcoDTO clcmcoDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigoDocumento+" AND CLCTO1=0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+ano+" AND "+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clc.add(clcmcoDTO);
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		
		return clc;
		
		
	}
	
	
	public List recuperaDocumentosNC(int empresa, int codigoDocumento, int ano, int mes){
		List clc = new ArrayList();
		ClcmcoDTO clcmcoDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigoDocumento+" AND CLCTO1>0 AND CLCFEC BETWEEN "+ano+" AND "+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clc.add(clcmcoDTO);
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		
		return clc;
		
		
	}
	
	public List recuperaDocumentosBodegaNC(int empresa, int codigoDocumento, int ano, int mes, int bodega){
		List clc = new ArrayList();
		ClcmcoDTO clcmcoDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigoDocumento+" AND CLCTO1>0 AND CLCBOD="+bodega+" AND CLCFEC BETWEEN "+ano+" AND "+mes+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				clc.add(clcmcoDTO);
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		
		
		return clc;
		
		
	}
}
