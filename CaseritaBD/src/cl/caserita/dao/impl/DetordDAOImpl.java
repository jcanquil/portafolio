package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DetordTranspDTO;
import cl.caserita.dto.DetordTranspNcpDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.OrdvtaDTO;

public class DetordDAOImpl implements DetordDAO{
	private static Logger log = Logger.getLogger(DetordDAOImpl.class);

	private Connection conn;
	
	public DetordDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int insertaDetalleOV(DetordDTO detord){
		int correlativo=-100;
		
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO  "+
        "  CASEDAT.DETORD " + 
        " (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,CORDOV, CLDCOD, VENFOR, CLDCAN, VEDCA4,VENCA2, EXIPRE, ORDDPR, ORDDCS, ORDDCN,ORDDCT, MONBRU, ORDDMN, ORDDTN, VENMO2,VECFE5, CLICOR, CAMES1, CLICO1, CLICO2,CLICO3 ) VALUES("+detord.getCodEmpresa()+","+detord.getNumOvVenta()+","+detord.getRutCliente()+",'"+detord.getDvCliente()+"',"+detord.getCodigoBodega()+","+detord.getCorrelativoDetalleOV()+","+detord.getCodigoArticulo()+",'"+detord.getFormato().trim()+"',"+detord.getCantidadArticulo()+",0,"+detord.getCantidadFormato()+","+detord.getPrecioBruto()+","+detord.getPrecioNeto()+","+detord.getCostoBruto()+","+detord.getCostoNeto()+","+detord.getCostoTotal()+","+detord.getMontoBruto()+","+detord.getMontoNeto()+","+detord.getTotalNeto()+","+detord.getMontoTotal()+","+detord.getFechaDespacho()+","+detord.getCorrelativoDespacho()+",'"+detord.getEstado().trim()+"',"+detord.getRegion()+","+detord.getCiudad()+","+detord.getComuna()+") " ;
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
	
	public List listaDetalleOrden(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente){
		DetordDTO detordDTO = null;
		
		PreparedStatement pstmt =null;
		List detord = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * from "+
        " CASEDAT.DETORD " + 
        " WHERE ORDEMP="+codigoEmpresa+" AND NUMVEN ="+numeroOV+" AND TPTC18 ='"+codigoBodega+"' AND CLMRUT="+rutCliente+"  ORDER BY CLICOR, VECFE5" ;
		log.info("Query:"+sqlObtenerCamtra);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				detordDTO = new DetordDTO();
				detordDTO.setCodEmpresa(rs.getInt("ORDEMP"));
				detordDTO.setNumOvVenta(rs.getInt("NUMVEN"));
				detordDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
				detordDTO.setCantidadArticulo(rs.getInt("CLDCAN"));
				detordDTO.setCantidadFormato(rs.getInt("VEDCA4"));
				detordDTO.setFormato(rs.getString("VENFOR").trim());
				detordDTO.setCorreDirecciones(rs.getInt("CLICOR"));
				detordDTO.setCodRegion(rs.getInt("CLICO1"));
				detordDTO.setCodCiudad(rs.getInt("CLICO2"));
				detordDTO.setCodComuna(rs.getInt("CLICO3"));
				detordDTO.setCorrelativoDetalleOV(rs.getInt("CORDOV"));
				
				detord.add(detordDTO);
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
		return detord;
	}
	public int actualizarestadoDetalleOV(int empresa, int bodega, int numeroOV, String estado){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.DETORD " + 
        " SET CAMES1='"+estado+"' Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND TPTC18="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
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
	
	
	
	public int actualizarestadoDetalleOVCantidades(int empresa, int bodega, int numeroOV, String estado){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.DETORD " + 
        " SET CAMES1='"+estado+"' , VEDCA4=CLDCAN Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND TPTC18="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
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
	public int actualizarestadoDetalleOVCantidad(int empresa, int bodega, int numeroOV, String estado){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.DETORD " + 
        " SET CAMES1='"+estado+"', VEDCA4=0 Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND TPTC18="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
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
	
	public int actualizarestadoDetalle(DetordDTO dto){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.DETORD " + 
        " SET CLDCAN="+dto.getCantidadArticulo()+", VENCA2="+dto.getCantidadArticulo()+", ORDDCN="+dto.getCostoNeto()+", ORDDCT="+dto.getCostoNeto()*dto.getCantidadArticulo()+", MONBRU="+dto.getMontoBruto()+", VENMON="+dto.getDescuento()+", ORDDDS="+dto.getDescuentoNeto()+", ORDDMN="+dto.getMontoNeto()+",ORDDTN="+dto.getTotalNeto()+",VENMO2="+dto.getMontoTotal()+"  Where ORDEMP="+dto.getCodEmpresa()+" AND NUMVEN="+dto.getNumOvVenta()+" AND TPTC18="+dto.getCodigoBodega()+" AND CORDOV="+dto.getCorrelativoDetalleOV()+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL DETORD" + sqlObtenerCldmco);
			
			
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
	
	public int eliminaDetalle(DetordDTO dto){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  CASEDAT.DETORD " + 
        " Where ORDEMP="+dto.getCodEmpresa()+" AND NUMVEN="+dto.getNumOvVenta()+" AND TPTC18="+dto.getCodigoBodega()+" AND CORDOV="+dto.getCorrelativoDetalleOV()+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL DETORD" + sqlObtenerCldmco);
			
			
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
	
	public int actualizarDocumento(int empresa, int bodega, int numeroOV, int numeroDocumento){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.DETORD " + 
        " SET CAMNUM='"+numeroDocumento+"' Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND TPTC18="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
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
	
public List<DetordTranspDTO> obtieneTotalesdetalle(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente, int codigoArti, int correla){
		
	DetordTranspNcpDTO detordDTO = null;
	
	PreparedStatement pstmt =null;
	List detord = new ArrayList();
	ResultSet rs = null; 
	
	String solouncorrelativo="";
	if (correla>-1){
        solouncorrelativo=" AND d.CORDOV="+correla+"";
    }
    String solounarticulo="";
	if (codigoArti>-1){
        solounarticulo=" AND d.CLDCOD="+codigoArti+"";
    }
	    
	String cmp="d.CORDOV, d.CLDCOD, d.CLDCAN, d.VENFOR, d.EXIPRE, d.ORDDPR, d.ORDDCS, d.ORDDCN, d.ORDDCT, "+
			   "d.VENMON, d.ORDDDS, d.MONBRU, d.ORDDMN, d.ORDDTN, d.VENMO2, d.ORDDTE, a.EXMDIG, a.EXMDES, d.VENDES, a.EXMCOM ";
	String sqlObtenerCamtra="Select "+cmp+ " FROM "+
    " CASEDAT.DETORD d " +
	" INNER JOIN CASEDAT.EXMART a "+
    " ON d.CLDCOD = a.EXMCOD "+
    " WHERE d.ORDEMP="+codigoEmpresa+" AND d.NUMVEN ="+numeroOV+" AND d.TPTC18 ="+codigoBodega+" AND d.CLMRUT="+rutCliente+ " "+
    solouncorrelativo+" "+solounarticulo+""+
    " FOR READ ONLY" ;
	//log.info("Query:"+sqlObtenerCamtra);
	
	try{
		pstmt = conn.prepareStatement(sqlObtenerCamtra);

		rs = pstmt.executeQuery();
		while (rs.next()) {
			
			detordDTO = new DetordTranspNcpDTO();
			
			detordDTO.setCorrelativo(rs.getString("CORDOV"));
			detordDTO.setCodigoArticulo(rs.getString("CLDCOD"));
			detordDTO.setDigitoArticulo(rs.getString("EXMDIG").trim());
			detordDTO.setDescripcionArticulo(rs.getString("EXMDES").trim());
			detordDTO.setCantidad(rs.getInt("CLDCAN"));
			detordDTO.setFormato(rs.getString("VENFOR").trim());
			detordDTO.setPrecioUnitario(rs.getDouble("EXIPRE"));
			detordDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
			detordDTO.setCostoArticulo(rs.getDouble("ORDDCS"));
			detordDTO.setCostoTotalNeto(rs.getDouble("ORDDCN"));
			detordDTO.setTotalDescuento(rs.getInt("VENMON"));
			detordDTO.setTotalDescuentoNeto(rs.getInt("ORDDDS"));
			detordDTO.setTotalBruto(rs.getInt("MONBRU"));
			detordDTO.setTotalNeto(rs.getInt("ORDDMN"));
			detordDTO.setMontototalNeto(rs.getInt("ORDDTN"));
			detordDTO.setTotalExento(rs.getInt("ORDDTE"));
			detordDTO.setPorcentajeDescuento(rs.getDouble("VENDES"));
			detordDTO.setCombo(rs.getString("EXMCOM").trim());
			
			detord.add(detordDTO);
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
	return detord;

	}
	
	public DetordDTO buscaOVCarguio(DetordDTO dto){
		DetordDTO detordDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo OV del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.DETORD " + 
			" WHERE ORDEMP = "+dto.getCodEmpresa()+
	        " AND NUMVEN = "+dto.getNumOvVenta() +
	        " AND TPTC18 = "+dto.getCodigoBodega()+
	        " AND CLDCOD = "+dto.getCodigoArticulo()+
	        " AND CLICOR = "+dto.getCorrelativoDespacho()+
	        " FOR READ ONLY" ;
		log.info("SQL BUSCA OV DEL CARGUIO "+ sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				detordDTO = new DetordDTO();
				
				detordDTO.setCantidadDespachada(rs.getInt("VEDCA4"));
				detordDTO.setCantidadFormato(rs.getInt("VENCA2"));
				detordDTO.setFormato(rs.getString("VENFOR"));
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
		return detordDTO;
	}
	
	public int actualizaUnidadesOV(DetordDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		//llevo todo a unidad solo WMS
		String sqlObtenerExdtra ="UPDATE "+
        " CASEDAT.DETORD " + 
        " SET VENFOR = '"+dto.getFormato()+"',"+
        //" VEDCA4 = "+dto.getCantidadDespachada()+","+
       // " VEDCA4 = VEDCA4,"+
        " VENCA2 = "+dto.getCantidadFormato()+
        " WHERE ORDEMP = "+dto.getCodEmpresa()+
        " AND NUMVEN = "+dto.getNumOvVenta()+
        " AND TPTC18 = "+dto.getCodigoBodega()+
        " AND CLDCOD = "+dto.getCodigoArticulo()+
        " AND CLICOR = "+dto.getCorrelativoDespacho();
		
		log.info("SQL ACTUALIZA DETORD "+ sqlObtenerExdtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdtra);
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
	
	public DetordDTO buscaInternoOVFact(DetordDTO dto){
		DetordDTO detordDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo OV del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.DETORD " + 
			" WHERE ORDEMP = "+dto.getCodEmpresa()+
	        " AND NUMVEN = "+dto.getNumOvVenta() +
	        " AND TPTC18 = "+dto.getCodigoBodega()+
	        " FOR READ ONLY" ;
		log.info("SQL BUSCA INTERNO DE LA VENTA FACTURADA "+ sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				detordDTO = new DetordDTO();
				
				detordDTO.setNumeroInternoVenta(rs.getInt("CAMNUM"));
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
		return detordDTO;
	}
	
	public int LiberaOVMapas(int empresa, int bodega, String estado, String estadoNuevo){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
	    "  CASEDAT.DETORD " + 
	    " SET CAMES1='"+estadoNuevo.trim()+"' Where ORDEMP="+empresa+" AND TPTC18="+bodega+" AND CAMES1='"+estado.trim()+"'  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
		
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
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
		return correlativo2;
	}

	public int actualizaUnidOVVTAMayorista(DetordDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		//llevo todo a unidad solo WMS
		String sqlObtenerExdtra ="UPDATE "+
        " CASEDAT.DETORD " + 
        " SET VENFOR = '"+dto.getFormato()+"',"+
        " VEDCA4 = "+dto.getCantidadDespachada()+","+
        " VENCA2 = "+dto.getCantidadFormato()+
        " WHERE ORDEMP = "+dto.getCodEmpresa()+
        " AND NUMVEN = "+dto.getNumOvVenta()+
        " AND TPTC18 = "+dto.getCodigoBodega()+
        " AND CLDCOD = "+dto.getCodigoArticulo()+
        " AND CLICOR = "+dto.getCorrelativoDespacho();
		
		log.info("SQL ACTUALIZA DETORD "+ sqlObtenerExdtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdtra);
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
	
	public List<DetordTranspNcpDTO> obtieneTotalesdetalleNumdoc(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente, int codigoArti, int cantidad){
		
		DetordTranspNcpDTO detordDTO = null;
		
		PreparedStatement pstmt =null;
		List detord = new ArrayList();
		ResultSet rs = null; 
		
		String solounacantidad="";
		if (cantidad>-1){
	        solounacantidad=" AND d.CLDCAN="+cantidad+"";
	    }
	    String solounarticulo="";
		if (codigoArti>-1){
	        solounarticulo=" AND d.CLDCOD="+codigoArti+"";
	    }
		    
		String cmp="d.CORDOV, d.CLDCOD, d.CLDCAN, d.VENFOR, d.EXIPRE, d.ORDDPR, d.ORDDCS, d.ORDDCN, d.ORDDCT, "+
				   "d.VENMON, d.ORDDDS, d.MONBRU, d.ORDDMN, d.ORDDTN, d.VENMO2, d.ORDDTE, a.EXMDIG, a.EXMDES, d.VENDES, a.EXMCOM ";
		String sqlObtenerCamtra="Select "+cmp+ " FROM "+
	    " CASEDAT.DETORD d " +
		" INNER JOIN CASEDAT.EXMART a "+
	    " ON d.CLDCOD = a.EXMCOD "+
	    " WHERE d.ORDEMP="+codigoEmpresa+" AND d.NUMVEN ="+numeroOV+" AND d.TPTC18 ="+codigoBodega+" AND d.CLMRUT="+rutCliente+ " "+
	    solounacantidad+" "+solounarticulo+""+
	    " FOR READ ONLY" ;
		//System.out.println("Query:"+sqlObtenerCamtra);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				detordDTO = new DetordTranspNcpDTO();
				
				detordDTO.setCorrelativo(rs.getString("CORDOV"));
				detordDTO.setCodigoArticulo(rs.getString("CLDCOD"));
				detordDTO.setDigitoArticulo(rs.getString("EXMDIG").trim());
				detordDTO.setDescripcionArticulo(rs.getString("EXMDES").trim());
				detordDTO.setCantidad(rs.getInt("CLDCAN"));
				detordDTO.setFormato(rs.getString("VENFOR").trim());
				detordDTO.setPrecioUnitario(rs.getDouble("EXIPRE"));
				detordDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
				detordDTO.setCostoArticulo(rs.getDouble("ORDDCS"));
				detordDTO.setCostoTotalNeto(rs.getDouble("ORDDCN"));
				detordDTO.setTotalDescuento(rs.getInt("VENMON"));
				detordDTO.setTotalDescuentoNeto(rs.getInt("ORDDDS"));
				detordDTO.setTotalBruto(rs.getInt("MONBRU"));
				detordDTO.setTotalNeto(rs.getInt("ORDDMN"));
				detordDTO.setMontototalNeto(rs.getInt("ORDDTN"));
				detordDTO.setTotalExento(rs.getInt("ORDDTE"));
				detordDTO.setPorcentajeDescuento(rs.getDouble("VENDES"));
				detordDTO.setCombo(rs.getString("EXMCOM").trim());
				
				detord.add(detordDTO);
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
		return detord;
	}
	
	public int recuperaOrdenVenta(int empresa, int bodega, int numeroInterno, int rutCliente){
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		int numeroDocumento=0;
		//Busca solo OV del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.DETORD " + 
			" WHERE ORDEMP = "+empresa+
	        " AND CLMRUT = "+rutCliente +
	        " AND TPTC18 = "+bodega+
	        " AND CAMNUM = "+numeroInterno+
	        "  FETCH FIRST 1 ROWS ONLY" ;
		log.info("SQL BUSCA OV DEL CARGUIO "+ sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				numeroDocumento=rs.getInt("NUMVEN");
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
		return numeroDocumento;
	}
	
	public List<DetordTranspNcpDTO> obtieneTotalesdetalleNumdoc2(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente, int codigoArti, int cantidad){
		
		DetordTranspNcpDTO detordDTO = null;
		
		PreparedStatement pstmt =null;
		List detord = new ArrayList();
		ResultSet rs = null; 
		
		String solounacantidad="";
		
		String solounarticulo="";
		if (codigoArti>-1){
	        solounarticulo=" AND d.CLDCOD="+codigoArti+"";
	    }
		
		String cmp="d.CORDOV, d.CLDCOD, d.CLDCAN, d.VENFOR, d.EXIPRE, d.ORDDPR, d.ORDDCS, d.ORDDCN, d.ORDDCT, "+
				   "d.VENMON, d.ORDDDS, d.MONBRU, d.ORDDMN, d.ORDDTN, d.VENMO2, d.ORDDTE, a.EXMDIG, a.EXMDES, d.VENDES, a.EXMCOM ";
		String sqlObtenerCamtra="Select "+cmp+ " FROM "+
	    " CASEDAT.DETORD d " +
		" INNER JOIN CASEDAT.EXMART a "+
	    " ON d.CLDCOD = a.EXMCOD "+
	    " WHERE d.ORDEMP="+codigoEmpresa+" AND d.NUMVEN ="+numeroOV+" AND d.TPTC18 ="+codigoBodega+" AND d.CLMRUT="+rutCliente+ " "+
	    solounacantidad+" "+solounarticulo+""+
	    " FOR READ ONLY" ;
		//logi.info("SELECT CORRELATIVO, CANT, PRECIOS  DE DETORD Y EXMART  :  "+sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			rs = pstmt.executeQuery();
			//logi.info("O K E Y   SELECT CORRELATIVO, CANT, PRECIOS  DE DETORD Y EXMART");
			while (rs.next()) {
				
				detordDTO = new DetordTranspNcpDTO();
				
				detordDTO.setCorrelativo(rs.getString("CORDOV"));   //CORDOV
				detordDTO.setCodigoArticulo(rs.getString("CLDCOD")); //CLDCOD
				detordDTO.setDigitoArticulo(rs.getString("EXMDIG").trim());
				detordDTO.setDescripcionArticulo(rs.getString("EXMDES").trim());
				detordDTO.setCantidad(rs.getInt("CLDCAN")); //CLDCAN
				detordDTO.setFormato(rs.getString("VENFOR").trim());  
				detordDTO.setPrecioUnitario(rs.getDouble("EXIPRE"));  //EXIPRE
				detordDTO.setPrecioNeto(rs.getDouble("ORDDPR"));  //ORDDPR
				detordDTO.setCostoArticulo(rs.getDouble("ORDDCS")); //ORDDCS
				detordDTO.setCostoTotalNeto(rs.getDouble("ORDDCN"));  //ORDDCN
				detordDTO.setTotalDescuento(rs.getInt("VENMON"));  //VENMON
				detordDTO.setTotalDescuentoNeto(rs.getInt("ORDDDS"));  //ORDDDS
				detordDTO.setTotalBruto(rs.getInt("MONBRU"));  //MONBRU
				detordDTO.setTotalNeto(rs.getInt("ORDDMN"));  //ORDDMN
				detordDTO.setMontototalNeto(rs.getInt("ORDDTN"));   //ORDDTN
				detordDTO.setTotalExento(rs.getInt("ORDDTE"));    //ORDDTE
				detordDTO.setPorcentajeDescuento(rs.getDouble("VENDES"));  //VENDES
				detordDTO.setCombo(rs.getString("EXMCOM").trim());
				
				
				detord.add(detordDTO);
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
		return detord;
	}


}
