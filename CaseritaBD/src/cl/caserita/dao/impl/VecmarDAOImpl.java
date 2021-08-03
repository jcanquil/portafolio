package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.VecmarDTO;

public class VecmarDAOImpl implements VecmarDAO{
	private static Logger log = Logger.getLogger(VecmarDAOImpl.class);
	private Connection conn;
	
	public VecmarDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public VecmarDTO obtenerDatosVecmar (int empresa, int tipoMovto, int fechamov, int numdoc, ClmcliDTO clmcli){
		VecmarDTO vecmarDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.VECMAR " + 
        " Where VENEMP="+empresa+" AND VENCOD="+tipoMovto+" AND VENFEC="+fechamov+" AND VENNUM="+numdoc+" FOR READ ONLY" ;
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmarDTO = new VecmarDTO();
				
				vecmarDTO.setCodigoEmpresa(rs.getInt("VENEMP"));
				vecmarDTO.setCodTipoMvto(rs.getInt("VENCOD"));
				vecmarDTO.setNumDocumento(rs.getInt("VENNUM"));
				vecmarDTO.setFechaMvto(rs.getInt("VENFEC"));
				vecmarDTO.setCodigoDocumento(rs.getInt("VENCO5"));
				vecmarDTO.setNumeroTipoDocumento(rs.getInt("VENNU2"));
				vecmarDTO.setFechaDocumento(rs.getInt("VENFE1"));
				vecmarDTO.setNumeroOrdenCompra(rs.getInt("VECNU3"));
				vecmarDTO.setBodegaOrigen(rs.getInt("VENBO2"));
				vecmarDTO.setBodegaDestino(rs.getInt("VENBOD"));
				vecmarDTO.setFormaPago(rs.getString("VECFOR"));
				vecmarDTO.setCantidadLineaDetalle(rs.getInt("VENCAN"));
				vecmarDTO.setTotalBruto(rs.getInt("VECTOT"));
				vecmarDTO.setPorcentajeDescuento(rs.getInt("VENPOR"));
				vecmarDTO.setTotalDescuento(rs.getInt("VENTOD"));
				vecmarDTO.setTotalNeto(rs.getInt("VECTO1"));
				vecmarDTO.setTotalImptoAdicional(rs.getInt("VECTO2"));
				vecmarDTO.setTotalIva(rs.getInt("VENTO1"));
				vecmarDTO.setTotalDocumento(rs.getInt("VENTO2"));
				vecmarDTO.setPesoTotalMovto(rs.getInt("VECPES"));
				vecmarDTO.setVolumenTotalMovto(rs.getInt("VECVOL"));
				vecmarDTO.setRutProveedor(rs.getString("VENRUT"));
				vecmarDTO.setDvProveedor(rs.getString("VENDIG"));
				vecmarDTO.setCodigoVendedor(rs.getInt("VENCO4"));
				vecmarDTO.setCodigoJefeLocal(rs.getInt("VENCO6"));
				vecmarDTO.setSwitchDescto(rs.getInt("VENSWI"));
				vecmarDTO.setSwichProceso(rs.getInt("VENSWP"));
				vecmarDTO.setIndicadorDespacho(rs.getString("VECIND"));
				vecmarDTO.setDireccionDespacho(rs.getString("VECDIR"));
				vecmarDTO.setContactoDespacho(rs.getString("VECCON"));
				vecmarDTO.setFechaDespacho(rs.getInt("VECFE5"));
				vecmarDTO.setSwitchPagoCaja(rs.getString("VECSWI"));
				vecmarDTO.setFechaDespachoReal(rs.getInt("VECFE7"));
				vecmarDTO.setCodigoRegion(rs.getInt("VECCO8"));
				vecmarDTO.setCodigoCiudad(rs.getInt("VECCO9"));
				vecmarDTO.setCodigoComuna(rs.getInt("VECC01"));
				vecmarDTO.setCodigoTipoVendedor(rs.getInt("VECCO7"));
				vecmarDTO.setCodigoBodega(rs.getInt("VECC02"));
				vecmarDTO.setCodigoJaula(rs.getInt("VECC03"));
				vecmarDTO.setRutEmpresaTransporte(rs.getInt("VECRU3"));
				vecmarDTO.setDvEmpresaTransporte(rs.getString("VECDVE"));
				vecmarDTO.setPatente(rs.getString("VECPAT"));
				//log.info("Vendedor:"+vecmarDTO.getCodigoVendedor() );
				//log.info("Region:"+clmcli.getCodRegion() );
				//log.info("Ciudad:"+clmcli.getCodCiudad() );
				//log.info("Comuna:"+clmcli.getCodComuna() );
				
				vecmarDTO.setNombreVendedor(recuperaNombreVendedor(vecmarDTO.getCodigoVendedor()));
				vecmarDTO.setDescComuna(recuperaComuna(clmcli.getCodRegion(), clmcli.getCodCiudad(), clmcli.getCodComuna()));
				//log.info("Bodega:"+vecmarDTO.getBodegaOrigen() );
				TptbdgDTO dto = recuperaBodega(vecmarDTO.getBodegaOrigen());
				vecmarDTO.setDescBodega(dto.getDesBodega());
				//vecmarDTO.setd(direccionDespacho)(dto.getDireccion());
				vecmarDTO.setDescComunaBodega(dto.getDescComuna());
				vecmarDTO.setFechaTransportistaProveedor(rs.getInt("VECFE8"));
				//log.info("RUT VECMAR:" +vecmarDTO.getRutProveedor());
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return vecmarDTO;
	}
	
	
	public String recuperaNombreVendedor(int codigo){
		String nombre="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMVND " + 
        " Where EXMC09="+codigo+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nombre = rs.getString("EXMNO1");
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
		
		
		return nombre;
		
		
	}
	
	public String recuperaComuna(int region, int ciudad, int comuna){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCOM " + 
        " Where TPTC19="+region+" AND TPTC20="+ciudad+" AND TPTC22="+comuna+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTD20");
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
		
		
		return descomuna;
		
		
	}
	public TptbdgDTO recuperaBodega(int bodega){
		TptbdgDTO descomuna=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTBDG " + 
        " Where TPTC18="+bodega+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = new TptbdgDTO();
				descomuna.setDesBodega(rs.getString("TPTD18"));
				descomuna.setDireccion(rs.getString("TPTDIR"));
				//log.info("Region" + rs.getInt("TPTC21"));
				//log.info("Ciudad" + rs.getInt("TPTC23"));
				//log.info("Comuna" + rs.getInt("TPTC24"));
				descomuna.setDescComuna(recuperaComuna(rs.getInt("TPTC21"), rs.getInt("TPTC23"), rs.getInt("TPTC24")));
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
		
		
		return descomuna;
		
		
	}
	
	public void actualizaVecmar(int empresa, int codigo, int fecha, int numero){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMAR " + 
        " set VECSWI='S' Where VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		log.info("SQL ACTUALIZA VECMAR SWITCH PAGO CAJA :"+sqlObtenerVecmar);
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
	
	public void actualizaVecmarMerma(int empresa, int codigo, int fecha, int numero, int neto, int total){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMAR " + 
        " set VECTOT="+total+", VECTO1="+neto+", VENTO2="+total+" Where VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		log.info("ACTUALIZA MERMA VECMAR:"+sqlObtenerVecmar);
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
	
	
	public void actualizaVecmarGuias(int empresa, int codigo, int fecha, int numero, int numDOcEle){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMAR " + 
        " set VECSWI='S' , VENNU2="+numDOcEle+" Where VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		
		
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
	public int generaCorrelativoVisual(int empresa, int codigo, int fecha, int numero, int correlativo, String timbre, String estado,String ruta, String xml){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DOCGENEL " + 
        " (VENEMP, VENCOD,VENFEC,VENNUM,DOCGEN,TIMELT,CODEPP,CODSII,CAMPDF, CAMRUD) VALUES("+empresa+","+codigo+","+fecha+","+numero+","+correlativo+", '"+timbre+"','"+estado+"','P','"+ruta+"','"+xml+"') " ;
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
	
	public String recuperaTimbre(){
		int res=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String timbre="";
		String sqlObtenerVecmar="SELECT * FROM"+
        "  CASEDAT.DOCGENEL " + 
        " WHERE VENEMP=2 AND VENCOD=21 AND VENNUM=99999999 AND VENFEC=20170601" ;
		//log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				timbre = rs.getString("TIMELT");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return timbre;
	}
	
	public int actualizaRutaXML(int empresa, int codigo, int fecha, int numero, String xml){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.DOCGENEL  SET CAMRUD='"+xml+"'" + 
        " WHERE VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+"  " ;
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
	
	public int actualizaDisponibilidadImpresion(int empresa, int codigo, int fecha, int numero, String estado){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.DOCGENEL  SET ANDEN='"+estado+"'" + 
        " WHERE VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+"  " ;
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
	
	
	public int generaMovimiento(VecmarDTO vecmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VECMAR " + 
        " (VENEMP, VENCOD,VENFEC,VENNUM,VENCO5,VENNU2,VENFE1,VENBO2,VENCAN, VECTO1,VECTO2,VENTO1,VENTO2, VECPES, VENCO6) VALUES("+vecmar.getCodigoEmpresa()+","+vecmar.getCodTipoMvto()+","+vecmar.getFechaMvto()+","+vecmar.getNumDocumento()+","+vecmar.getCodigoDocumento()+", "+vecmar.getNumeroTipoDocumento()+","+vecmar.getFechaDocumento()+","+vecmar.getBodegaOrigen()+","+vecmar.getCantidadLineaDetalle()+","+vecmar.getTotalNeto()+","+vecmar.getTotalImptoAdicional()+","+vecmar.getTotalIva()+","+vecmar.getTotalDocumento()+","+vecmar.getPesoTotalMovto()+","+vecmar.getCodigoJefeLocal()+") " ;
		log.info("INSERTA VECMAR" + sqlObtenerVecmar);
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
	
	public int generaMovimientoCobro(VecmarDTO vecmar){
		int res=0;
		PreparedStatement pstmt =null;
		/*VENEMP, VENCOD, VENFEC, VENNUM, VENCO5, VENBO2, VECFOR,     
		 VENCAN, VECTOT, VECTO1, VECTO2, VENTO1, VENTO2, VENRUT, VENDIG,    
		 VENCO4, VENSWP, VECSWI, VECCO8, VECCO9, VECC01, VECCO7*/
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VECMAR " + 
        " (VENEMP, VENCOD,VENFEC,VENNUM,VENCO5,VENFE1,VENBO2,VECFOR,VENCAN, VECTOT,VECTO1,VECTO2,VENTO1,VENTO2, VENRUT,VENDIG,VENCO4, VENSWP,VECSWI,VECCO8,VECCO9,VECC01,VECCO7) VALUES("+vecmar.getCodigoEmpresa()+","+vecmar.getCodTipoMvto()+","+vecmar.getFechaMvto()+","+vecmar.getNumDocumento()+","+vecmar.getCodigoDocumento()+","+vecmar.getFechaDocumento()+" ,"+vecmar.getBodegaOrigen()+",'"+vecmar.getFormaPago()+"',"+vecmar.getCantidadLineaDetalle()+","+vecmar.getTotalBruto()+","+vecmar.getTotalNeto()+","+vecmar.getTotalImptoAdicional()+","+vecmar.getTotalIva()+","+vecmar.getTotalDocumento()+","+vecmar.getRutProveedor()+",'"+vecmar.getDvProveedor()+"',"+vecmar.getCodigoVendedor()+","+vecmar.getSwichProceso()+",'"+vecmar.getSwitchPagoCaja()+"',"+vecmar.getCodigoRegion()+","+vecmar.getCodigoCiudad()+","+vecmar.getCodigoComuna()+","+vecmar.getCodigoTipoVendedor()+") " ;
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
	
		
	public VecmarDTO obtenerDatosVecmarMer (int empresa, int tipoMovto, int fechamov, int numdoc){
		VecmarDTO vecmarDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.VECMAR " + 
        " Where VENEMP="+empresa+" AND VENCOD="+tipoMovto+" AND VENFEC="+fechamov+" AND VENNUM="+numdoc+" FOR READ ONLY" ;
//		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmarDTO = new VecmarDTO();
				vecmarDTO.setCodigoEmpresa(rs.getInt("VENEMP"));
				vecmarDTO.setCodTipoMvto(rs.getInt("VENCOD"));
				vecmarDTO.setNumDocumento(rs.getInt("VENNUM"));
				vecmarDTO.setFechaMvto(rs.getInt("VENFEC"));
				vecmarDTO.setCodigoDocumento(rs.getInt("VENCO5"));
				vecmarDTO.setNumeroTipoDocumento(rs.getInt("VENNU2"));
				vecmarDTO.setFechaDocumento(rs.getInt("VENFE1"));
				vecmarDTO.setNumeroOrdenCompra(rs.getInt("VECNU3"));
				vecmarDTO.setBodegaOrigen(rs.getInt("VENBO2"));
				vecmarDTO.setBodegaDestino(rs.getInt("VENBOD"));
				vecmarDTO.setFormaPago(rs.getString("VECFOR"));
				vecmarDTO.setCantidadLineaDetalle(rs.getInt("VENCAN"));
				vecmarDTO.setTotalBruto(rs.getInt("VECTOT"));
				vecmarDTO.setPorcentajeDescuento(rs.getInt("VENPOR"));
				vecmarDTO.setTotalDescuento(rs.getInt("VENTOD"));
				vecmarDTO.setTotalNeto(rs.getInt("VECTO1"));
				vecmarDTO.setTotalImptoAdicional(rs.getInt("VECTO2"));
				vecmarDTO.setTotalIva(rs.getInt("VENTO1"));
				vecmarDTO.setTotalDocumento(rs.getInt("VENTO2"));
				vecmarDTO.setPesoTotalMovto(rs.getInt("VECPES"));
				vecmarDTO.setVolumenTotalMovto(rs.getInt("VECVOL"));
				vecmarDTO.setRutProveedor(rs.getString("VENRUT"));
				vecmarDTO.setDvProveedor(rs.getString("VENDIG"));
				vecmarDTO.setCodigoVendedor(rs.getInt("VENCO4"));
				vecmarDTO.setCodigoJefeLocal(rs.getInt("VENCO6"));
				vecmarDTO.setSwitchDescto(rs.getInt("VENSWI"));
				vecmarDTO.setSwichProceso(rs.getInt("VENSWP"));
				vecmarDTO.setIndicadorDespacho(rs.getString("VECIND"));
				vecmarDTO.setDireccionDespacho(rs.getString("VECDIR"));
				vecmarDTO.setContactoDespacho(rs.getString("VECCON"));
				vecmarDTO.setFechaDespacho(rs.getInt("VECFE5"));
				vecmarDTO.setSwitchPagoCaja(rs.getString("VECSWI"));
				vecmarDTO.setFechaDespachoReal(rs.getInt("VECFE7"));
				vecmarDTO.setCodigoRegion(rs.getInt("VECCO8"));
				vecmarDTO.setCodigoCiudad(rs.getInt("VECCO9"));
				vecmarDTO.setCodigoComuna(rs.getInt("VECC01"));
				vecmarDTO.setCodigoTipoVendedor(rs.getInt("VECCO7"));
				vecmarDTO.setCodigoBodega(rs.getInt("VECC02"));
				vecmarDTO.setCodigoJaula(rs.getInt("VECC03"));
				vecmarDTO.setRutEmpresaTransporte(rs.getInt("VECRU3"));
				vecmarDTO.setDvEmpresaTransporte(rs.getString("VECDVE"));
				vecmarDTO.setPatente(rs.getString("VECPAT"));
				//log.info("Vendedor:"+vecmarDTO.getCodigoVendedor() );
				//log.info("Region:"+clmcli.getCodRegion() );
				//log.info("Ciudad:"+clmcli.getCodCiudad() );
				//log.info("Comuna:"+clmcli.getCodComuna() );
				
				vecmarDTO.setNombreVendedor(recuperaNombreVendedor(vecmarDTO.getCodigoVendedor()));
				//vecmarDTO.setDescComuna(recuperaComuna(clmcli.getCodRegion(), clmcli.getCodCiudad(), clmcli.getCodComuna()));
				//log.info("Bodega:"+vecmarDTO.getBodegaOrigen() );
				TptbdgDTO dto = recuperaBodega(vecmarDTO.getBodegaOrigen());
				vecmarDTO.setDescBodega(dto.getDesBodega());
				//vecmarDTO.setd(direccionDespacho)(dto.getDireccion());
				//vecmarDTO.setDescComunaBodega(dto.getDescComuna());
				vecmarDTO.setFechaTransportistaProveedor(rs.getInt("VECFE8"));
				//log.info("RUT VECMAR:" +vecmarDTO.getRutProveedor());
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return vecmarDTO;
	}
	
	public VecmarDTO obtenerDatosVecmarMermasWMS (int empresa, int tipoMovto, int numdoc){
		VecmarDTO vecmarDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.VECMAR " + 
        " Where VENEMP="+empresa+" AND VENCOD="+tipoMovto+" AND  VENNUM="+numdoc+" FOR READ ONLY" ;
		//log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //log.info("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmarDTO = new VecmarDTO();
				vecmarDTO.setCodTipoMvto(rs.getInt("VENCOD"));
				vecmarDTO.setNumDocumento(rs.getInt("VENNUM"));
				vecmarDTO.setFechaMvto(rs.getInt("VENFEC"));
				vecmarDTO.setCodigoDocumento(rs.getInt("VENCO5"));
				vecmarDTO.setNumeroTipoDocumento(rs.getInt("VENNU2"));
				vecmarDTO.setFechaDocumento(rs.getInt("VENFE1"));
				vecmarDTO.setNumeroOrdenCompra(rs.getInt("VECNU3"));
				vecmarDTO.setBodegaOrigen(rs.getInt("VENBO2"));
				vecmarDTO.setBodegaDestino(rs.getInt("VENBOD"));
				vecmarDTO.setFormaPago(rs.getString("VECFOR"));
				vecmarDTO.setCantidadLineaDetalle(rs.getInt("VENCAN"));
				vecmarDTO.setTotalBruto(rs.getInt("VECTOT"));
				vecmarDTO.setPorcentajeDescuento(rs.getInt("VENPOR"));
				vecmarDTO.setTotalDescuento(rs.getInt("VENTOD"));
				vecmarDTO.setTotalNeto(rs.getInt("VECTO1"));
				vecmarDTO.setTotalImptoAdicional(rs.getInt("VECTO2"));
				vecmarDTO.setTotalIva(rs.getInt("VENTO1"));
				vecmarDTO.setTotalDocumento(rs.getInt("VENTO2"));
				vecmarDTO.setPesoTotalMovto(rs.getInt("VECPES"));
				vecmarDTO.setVolumenTotalMovto(rs.getInt("VECVOL"));
				vecmarDTO.setRutProveedor(rs.getString("VENRUT"));
				vecmarDTO.setDvProveedor(rs.getString("VENDIG"));
				vecmarDTO.setCodigoVendedor(rs.getInt("VENCO4"));
				vecmarDTO.setCodigoJefeLocal(rs.getInt("VENCO6"));
				vecmarDTO.setSwitchDescto(rs.getInt("VENSWI"));
				vecmarDTO.setSwichProceso(rs.getInt("VENSWP"));
				vecmarDTO.setIndicadorDespacho(rs.getString("VECIND"));
				vecmarDTO.setDireccionDespacho(rs.getString("VECDIR"));
				vecmarDTO.setContactoDespacho(rs.getString("VECCON"));
				vecmarDTO.setFechaDespacho(rs.getInt("VECFE5"));
				vecmarDTO.setSwitchPagoCaja(rs.getString("VECSWI"));
				vecmarDTO.setFechaDespachoReal(rs.getInt("VECFE7"));
				vecmarDTO.setCodigoRegion(rs.getInt("VECCO8"));
				vecmarDTO.setCodigoCiudad(rs.getInt("VECCO9"));
				vecmarDTO.setCodigoComuna(rs.getInt("VECC01"));
				vecmarDTO.setCodigoTipoVendedor(rs.getInt("VECCO7"));
				vecmarDTO.setCodigoBodega(rs.getInt("VECC02"));
				vecmarDTO.setCodigoJaula(rs.getInt("VECC03"));
				vecmarDTO.setRutEmpresaTransporte(rs.getInt("VECRU3"));
				vecmarDTO.setDvEmpresaTransporte(rs.getString("VECDVE"));
				vecmarDTO.setPatente(rs.getString("VECPAT"));
				//log.info("Vendedor:"+vecmarDTO.getCodigoVendedor() );
				//log.info("Region:"+clmcli.getCodRegion() );
				//log.info("Ciudad:"+clmcli.getCodCiudad() );
				//log.info("Comuna:"+clmcli.getCodComuna() );
				
				vecmarDTO.setNombreVendedor(recuperaNombreVendedor(vecmarDTO.getCodigoVendedor()));
				//vecmarDTO.setDescComuna(recuperaComuna(clmcli.getCodRegion(), clmcli.getCodCiudad(), clmcli.getCodComuna()));
				//log.info("Bodega:"+vecmarDTO.getBodegaOrigen() );
				TptbdgDTO dto = recuperaBodega(vecmarDTO.getBodegaOrigen());
				vecmarDTO.setDescBodega(dto.getDesBodega());
				//vecmarDTO.setd(direccionDespacho)(dto.getDireccion());
				//vecmarDTO.setDescComunaBodega(dto.getDescComuna());
				vecmarDTO.setFechaTransportistaProveedor(rs.getInt("VECFE8"));
				vecmarDTO.setCodigoEmpresa(rs.getInt("VENEMP"));
				//log.info("RUT VECMAR:" +vecmarDTO.getRutProveedor());
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return vecmarDTO;
	}
	
	public VecmarDTO buscarIngresodeOC(VecmarDTO dto){
		VecmarDTO vecmarDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
			" FROM CASEDAT.VECMAR " + 
			" WHERE venemp = "+dto.getCodigoEmpresa()+
			" AND vencod = "+dto.getCodTipoMvto()+
			" AND vecnu3 = "+dto.getNumeroOrdenCompra()+
			" AND vennu2 = "+dto.getNumeroTipoDocumento()+
			" AND venrut = "+dto.getRutProveedor()+
			" AND vendig = '"+dto.getDvProveedor()+"'"+
			" AND venbo2 = "+dto.getCodigoBodega()+
			" FOR READ ONLY";
		
		log.info("SQL VECMAR : "+sqlObtenerVecmar);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmarDTO = new VecmarDTO();
				vecmarDTO.setCodTipoMvto(rs.getInt("VENCOD"));
				vecmarDTO.setNumDocumento(rs.getInt("VENNUM"));
				vecmarDTO.setFechaMvto(rs.getInt("VENFEC"));
				vecmarDTO.setCodigoDocumento(rs.getInt("VENCO5"));
				vecmarDTO.setNumeroTipoDocumento(rs.getInt("VENNU2"));
				vecmarDTO.setFechaDocumento(rs.getInt("VENFE1"));
				vecmarDTO.setNumeroOrdenCompra(rs.getInt("VECNU3"));
				vecmarDTO.setBodegaOrigen(rs.getInt("VENBO2"));
				vecmarDTO.setBodegaDestino(rs.getInt("VENBOD"));
				vecmarDTO.setFormaPago(rs.getString("VECFOR"));
				vecmarDTO.setCantidadLineaDetalle(rs.getInt("VENCAN"));
				vecmarDTO.setTotalBruto(rs.getInt("VECTOT"));
				vecmarDTO.setPorcentajeDescuento(rs.getInt("VENPOR"));
				vecmarDTO.setTotalDescuento(rs.getInt("VENTOD"));
				vecmarDTO.setTotalNeto(rs.getInt("VECTO1"));
				vecmarDTO.setTotalImptoAdicional(rs.getInt("VECTO2"));
				vecmarDTO.setTotalIva(rs.getInt("VENTO1"));
				vecmarDTO.setTotalDocumento(rs.getInt("VENTO2"));
				vecmarDTO.setPesoTotalMovto(rs.getInt("VECPES"));
				vecmarDTO.setVolumenTotalMovto(rs.getInt("VECVOL"));
				vecmarDTO.setRutProveedor(rs.getString("VENRUT"));
				vecmarDTO.setDvProveedor(rs.getString("VENDIG"));
				vecmarDTO.setCodigoVendedor(rs.getInt("VENCO4"));
				vecmarDTO.setCodigoJefeLocal(rs.getInt("VENCO6"));
				vecmarDTO.setSwitchDescto(rs.getInt("VENSWI"));
				vecmarDTO.setSwichProceso(rs.getInt("VENSWP"));
				vecmarDTO.setIndicadorDespacho(rs.getString("VECIND"));
				vecmarDTO.setDireccionDespacho(rs.getString("VECDIR"));
				vecmarDTO.setContactoDespacho(rs.getString("VECCON"));
				vecmarDTO.setFechaDespacho(rs.getInt("VECFE5"));
				vecmarDTO.setSwitchPagoCaja(rs.getString("VECSWI"));
				vecmarDTO.setFechaDespachoReal(rs.getInt("VECFE7"));
				vecmarDTO.setCodigoRegion(rs.getInt("VECCO8"));
				vecmarDTO.setCodigoCiudad(rs.getInt("VECCO9"));
				vecmarDTO.setCodigoComuna(rs.getInt("VECC01"));
				vecmarDTO.setCodigoTipoVendedor(rs.getInt("VECCO7"));
				vecmarDTO.setCodigoBodega(rs.getInt("VECC02"));
				vecmarDTO.setCodigoJaula(rs.getInt("VECC03"));
				vecmarDTO.setRutEmpresaTransporte(rs.getInt("VECRU3"));
				vecmarDTO.setDvEmpresaTransporte(rs.getString("VECDVE"));
				vecmarDTO.setPatente(rs.getString("VECPAT"));
				vecmarDTO.setFechaTransportistaProveedor(rs.getInt("VECFE8"));
				
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
		return vecmarDTO;
	}
	
	public void actualizaSwitchVecmar(int empresa, int codigo, int fecha, int numero, int swi){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMAR " + 
        " set VENSWP="+swi+", VECSWI='P' Where VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		log.info(" A C T U A L I Z A  S W I T C H :"+sqlObtenerVecmar);
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
	
	public void actualizaDatosVecmar(VecmarDTO vecmar){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMAR " + 
        " set VENSWP='"+vecmar.getSwichProceso()+"' , VECSWI='"+vecmar.getSwitchPagoCaja()+"', VENCO5="+vecmar.getCodigoDocumento()+", VENFEC="+vecmar.getFechaDocumento()+" , VENFE1="+vecmar.getFechaDocumento()+" , VENNU2=VENNUM , VENCAN="+vecmar.getCantidadLineaDetalle()+", VECIND='"+vecmar.getIndicadorDespacho().trim()+"', VENTO2="+vecmar.getTotalDocumento()+", VECTO1="+vecmar.getTotalNeto()+", VECTOT="+vecmar.getTotalBruto()+" Where VENEMP="+vecmar.getCodigoEmpresa()+" AND VENCOD="+vecmar.getCodTipoMvto()+" AND VENFEC="+vecmar.getFechaMvto()+" AND VENNUM="+vecmar.getNumDocumento()+" " ;
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
	
	public void actualizaVecmarGuiaOT(int empresa, int codigo, int fecha, int numeroInterno, int nroGuia){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE "+
        "  CASEDAT.VECMAR " + 
        " SET vennu2 = "+nroGuia+
        " WHERE venemp = "+empresa+
        " AND vencod="+codigo+
        " AND venfec="+fecha+
        " AND vennum="+numeroInterno;
		log.info("ACTUALIZA FOLIO GUIA TRASPASO:"+sqlObtenerVecmar);
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
	
	public VecmarDTO buscarIngresodeOT(VecmarDTO dto){
		VecmarDTO vecmarDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
			" FROM CASEDAT.VECMAR " + 
			" WHERE venemp = "+dto.getCodigoEmpresa()+
			" AND vencod = "+dto.getCodTipoMvto()+
			" AND vennum = "+dto.getNumeroTipoDocumento()+
			" AND vecnu3 = "+dto.getNumeroOrdenCompra()+
			" AND venrut = "+dto.getRutProveedor()+
			" AND vendig = '"+dto.getDvProveedor()+"'"+
			" AND venbo2 = "+dto.getCodigoBodega()+
			" AND venbod = "+dto.getBodegaDestino()+
			" FOR READ ONLY";
		
		log.info("ACTUALIZA VECMAR DATOS :"+sqlObtenerVecmar);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmarDTO = new VecmarDTO();
				vecmarDTO.setCodTipoMvto(rs.getInt("VENCOD"));
				vecmarDTO.setNumDocumento(rs.getInt("VENNUM"));
				vecmarDTO.setFechaMvto(rs.getInt("VENFEC"));
				vecmarDTO.setCodigoDocumento(rs.getInt("VENCO5"));
				vecmarDTO.setNumeroTipoDocumento(rs.getInt("VENNU2"));
				vecmarDTO.setFechaDocumento(rs.getInt("VENFE1"));
				vecmarDTO.setNumeroOrdenCompra(rs.getInt("VECNU3"));
				vecmarDTO.setBodegaOrigen(rs.getInt("VENBO2"));
				vecmarDTO.setBodegaDestino(rs.getInt("VENBOD"));
				vecmarDTO.setFormaPago(rs.getString("VECFOR"));
				vecmarDTO.setCantidadLineaDetalle(rs.getInt("VENCAN"));
				vecmarDTO.setTotalBruto(rs.getInt("VECTOT"));
				vecmarDTO.setPorcentajeDescuento(rs.getInt("VENPOR"));
				vecmarDTO.setTotalDescuento(rs.getInt("VENTOD"));
				vecmarDTO.setTotalNeto(rs.getInt("VECTO1"));
				vecmarDTO.setTotalImptoAdicional(rs.getInt("VECTO2"));
				vecmarDTO.setTotalIva(rs.getInt("VENTO1"));
				vecmarDTO.setTotalDocumento(rs.getInt("VENTO2"));
				vecmarDTO.setPesoTotalMovto(rs.getInt("VECPES"));
				vecmarDTO.setVolumenTotalMovto(rs.getInt("VECVOL"));
				vecmarDTO.setRutProveedor(rs.getString("VENRUT"));
				vecmarDTO.setDvProveedor(rs.getString("VENDIG"));
				vecmarDTO.setCodigoVendedor(rs.getInt("VENCO4"));
				vecmarDTO.setCodigoJefeLocal(rs.getInt("VENCO6"));
				vecmarDTO.setSwitchDescto(rs.getInt("VENSWI"));
				vecmarDTO.setSwichProceso(rs.getInt("VENSWP"));
				vecmarDTO.setIndicadorDespacho(rs.getString("VECIND"));
				vecmarDTO.setDireccionDespacho(rs.getString("VECDIR"));
				vecmarDTO.setContactoDespacho(rs.getString("VECCON"));
				vecmarDTO.setFechaDespacho(rs.getInt("VECFE5"));
				vecmarDTO.setSwitchPagoCaja(rs.getString("VECSWI"));
				vecmarDTO.setFechaDespachoReal(rs.getInt("VECFE7"));
				vecmarDTO.setCodigoRegion(rs.getInt("VECCO8"));
				vecmarDTO.setCodigoCiudad(rs.getInt("VECCO9"));
				vecmarDTO.setCodigoComuna(rs.getInt("VECC01"));
				vecmarDTO.setCodigoTipoVendedor(rs.getInt("VECCO7"));
				vecmarDTO.setCodigoBodega(rs.getInt("VECC02"));
				vecmarDTO.setCodigoJaula(rs.getInt("VECC03"));
				vecmarDTO.setRutEmpresaTransporte(rs.getInt("VECRU3"));
				vecmarDTO.setDvEmpresaTransporte(rs.getString("VECDVE"));
				vecmarDTO.setPatente(rs.getString("VECPAT"));
				vecmarDTO.setFechaTransportistaProveedor(rs.getInt("VECFE8"));
				
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
		return vecmarDTO;
	}
	
	public void actualizaVecmarSwitch(int empresa, int codigo, int fecha, int numero, String switchPro){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VECMAR " + 
        " set VECSWI='"+switchPro.trim()+"'  Where VENEMP="+empresa+" AND VENCOD="+codigo+" AND VENFEC="+fecha+" AND VENNUM="+numero+" " ;
		log.info(sqlObtenerVecmar);
		log.info("ACTUALIZA SWITCH :"+sqlObtenerVecmar);
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
