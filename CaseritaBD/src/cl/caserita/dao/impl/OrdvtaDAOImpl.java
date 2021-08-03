package cl.caserita.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DimensionesDTO;
import cl.caserita.dto.OrdTranspDTO;
import cl.caserita.dto.OrdTranspNcpDTO;
import cl.caserita.dto.OrdvtaDTO;

public class OrdvtaDAOImpl implements OrdvtaDAO {

	private static Logger log = Logger.getLogger(OrdvtaDAOImpl.class);

	private Connection conn;
	
	public OrdvtaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int insertaOV(OrdvtaDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO  "+
        "  CASEDAT.ORDVTA " + 
        " (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,VECFOR, CAMCOD, EXMFEC, CAMES1, RETPAG,CLICOR, ORDCAN, ORDTCS, TOTCNT, ORDMNT,ORDTNT, CLCTOT, ORDTIV) VALUES("+orden.getCodigoEmpresa()+","+orden.getNumeroOV()+","+orden.getRutCliente()+",'"+orden.getDvCliente()+"',"+orden.getCodigoBodega()+",'"+orden.getFormaPago()+"',"+orden.getCodigoVendedor()+","+orden.getFechaOrden()+",'"+orden.getEstadoOV().trim()+"','1',"+orden.getCorreDireccionOV()+","+orden.getTotalCosto()+","+orden.getTotalCostoNeto()+","+orden.getTotalCosto()+","+orden.getMontoNeto()+","+orden.getMontoNeto()+","+orden.getTotalDocumento()+","+orden.getTotalIva()+") " ;
		log.info("SQL Orden" + sqlObtenerCldmco);
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
	
	public List obtieneOrdenes(int empresa){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA " + 
        " Where ORDEMP="+empresa+" AND CAMES1='Z' AND TPTC18=26 FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				DimensionesDTO dimen = obtieneVolPesoCaja(empresa, 26, ordvtaDTO.getNumeroOV());
				if (dimen!=null){
					ordvtaDTO.setVolumen(dimen.getVolumen());
					ordvtaDTO.setCantidadCaja(dimen.getCantidadCajas());
					ordvtaDTO.setPeso(dimen.getPeso());
				}
				
				int correlativo = obtieneDireccionDespacho(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente());
				
				ClidirDTO clidir = recuperaDatosCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente(), correlativo);
				if (clidir!=null && clidir.getLatitud()!="" && clidir.getLongitud()!=""){
					if (clidir.getLatitud()!=null && clidir.getLongitud()!=null){
						ordvtaDTO.setLatitud(clidir.getLatitud().trim());
						ordvtaDTO.setLongitud(clidir.getLongitud().trim());
					}
				}
				
				ordvta.add(ordvtaDTO);
				
				
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
		return ordvta;
	}
	
	public int obtieneCorrelativo(int bodega){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTANM " + 
        " Where TPTC18="+bodega+" AND OBF007='1' FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("OBF00F");
				actualizarCorrelativo(bodega, rs.getInt("OBF00F")+1);
				
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
		return correlativo;
	}
	public List obtieneOrdenesActualizaLatLon(int empresa){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA " + 
        " Where ORDEMP="+empresa+" AND CAMES1='E' AND TPTC18=26 AND EXMFEC=20150903 FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				
				int correlativo = obtieneDireccionDespacho(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente());
				
				List  clidir = recuperaDatosClienteLista(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente(), correlativo);
				
				ordvtaDTO.setClidir(clidir);
				ordvta.add(ordvtaDTO);
				
				
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
		return ordvta;
	}
	public int obtieneDireccionDespacho(int empresa, int numeroOV, int codBodega, int rutCliente, String dv){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND CLMRUT="+rutCliente+" AND CLMDIG='"+dv+"' AND TPTC18="+codBodega+" fetch first 1 rows only FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("CLICOR");
				
				
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
		return correlativo;
	}
	public int actualizarCorrelativo(int bodega, int correlativo){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTANM " + 
        " SET OBF00F="+correlativo+" Where TPTC18="+bodega+" AND OBF007='1'  " ;
		
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
	public int procesaCalculoProcedure(int empresa, int bodega, int numeroOV, int rutClie, String dv){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		log.info("Procesa Procedimiento");
		
		try{
			CallableStatement cst = conn.prepareCall("{call caseprg.prccalc (?,?,?,?,?)}");
			cst.setString(1, String.valueOf(empresa));
			
			cst.setString(2, String.valueOf(numeroOV));
			cst.setString(3, String.valueOf(rutClie));
			cst.setString(4, dv.trim());
			cst.setString(5, String.valueOf(bodega));
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
			log.info("Procesa Procedimiento PRCCALC");
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
	
	public int actualizarestadoOV(OrdvtaDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTA " + 
        " SET CAMES1='"+orden.getEstadoOV().trim()+"' Where ORDEMP="+orden.getCodigoEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+" AND CLMRUT="+orden.getRutCliente()+" AND CLMDIG='"+orden.getDvCliente()+"' AND TPTC18="+orden.getCodigoBodega()+" " ;
		
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
	public int actualizarestadoOVIndividuales(OrdvtaDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTA " + 
        " SET CAMES1='"+orden.getEstadoOV().trim()+"' Where ORDEMP="+orden.getCodigoEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+" AND  TPTC18="+orden.getCodigoBodega()+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("Actualiza OV" + sqlObtenerCldmco);
			
			
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
	
	public String obtieneNombreVendedor(int codigoVendedor){
		String nombre="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMVND " + 
        " Where EXMC09="+codigoVendedor+"  FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nombre=rs.getString("EXMNO1");
				
				
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
		return nombre;
	}
	public DimensionesDTO obtieneVolPesoCaja(int empresa, int bodega, int numeroOV){
		String nombre="";
		DimensionesDTO dimen = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		double cntcaja=0, volumen=0, peso=0;
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD " + 
        " Where ORDEMP="+empresa+" AND TPTC18="+bodega+" AND NUMVEN="+numeroOV+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLDCOD")!=7777777){
					DimensionesDTO dimension = obtieneVolPesoEXDARTCaja(rs.getInt("CLDCOD"), rs.getString("VENFOR").trim(), rs.getInt("CLDCAN"));
					if (dimension!=null){
						cntcaja = cntcaja + (rs.getInt("CLDCAN") * dimension.getCantidadCajas());
						volumen = volumen + (rs.getInt("CLDCAN") * dimension.getVolumen());
						peso = peso + (rs.getInt("CLDCAN") * dimension.getPeso());
					}
					
				}
				
				
				
			}
			if (cntcaja>0 || volumen>0 || peso>0){
				dimen = new DimensionesDTO();
				dimen.setCantidadCajas(cntcaja);
				dimen.setVolumen(volumen);
				dimen.setPeso(peso);
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
		return dimen;
	}
	
	public DimensionesDTO obtieneVolPesoEXDARTCaja( int codArticulo, String contenedor, int cantidad){
		String nombre="";
		DimensionesDTO dimensiones=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMART E1, CASEDAT.EXDART E2 " + 
        " Where EXMCOD="+codArticulo+" AND E1.EXMCOD=E2.EXDCO1 AND E2.EXDTIP='"+contenedor.trim()+"' ORDER BY EXDFEC DESC FETCH FIRST 1 ROW ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dimensiones = new DimensionesDTO();
				dimensiones.setVolumen(rs.getDouble("EXDVOL"));
				dimensiones.setPeso(rs.getDouble("EXDPES"));
				if (rs.getInt("EXMUNI")>0){
					dimensiones.setCantidadCajas(cantidad/rs.getInt("EXMUNI"));
				}else{
					dimensiones.setCantidadCajas(cantidad/1);
				}
				
				
				
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
		return dimensiones;
	}
	
	public String obtieneNombreCliente(int rut, String dv){
		String nombre="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLMCLI " + 
        " Where CLMRUT="+rut+"  AND CLMDIG='"+dv+"' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nombre=rs.getString("CLMNOM");
				
				
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
		return nombre;
	}
	
	
	public List recuperaDatosClienteLista(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		List direcciones=new ArrayList();
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		String sqlObtenerCldmco ="Select C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1, C2.LATDIR, C2.LONDIR, C1.CLICOR "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"'  C1.CLICOR BETWEEN '100' AND '199' AND C1.CLICOR=C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C2.ESTDIR ='A'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClidirDTO();
				clidir.setRegion(rs.getInt("CLICO1"));
				clidir.setCiudad(rs.getInt("CLICO2"));
				clidir.setComuna(rs.getInt("CLICO3"));
				clidir.setDireccionCliente(rs.getString("CLIDI1"));
				clidir.setLatitud(rs.getString("LATDIR"));
				clidir.setLongitud(rs.getString("LONDIR"));
				clidir.setCorrelativo(rs.getInt("CLICOR"));
				direcciones.add(clidir);
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
		return direcciones;
	}
	
	public ClidirDTO recuperaDatosCliente(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		String sqlObtenerCldmco ="Select C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1, C2.LATDIR, C2.LONDIR "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= "+correlativo+" AND C1.CLICOR=C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C2.ESTDIR ='A'  FOR READ ONLY" ;
		try
		{
			log.info("SQL" + sqlObtenerCldmco);
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClidirDTO();
				clidir.setRegion(rs.getInt("CLICO1"));
				clidir.setCiudad(rs.getInt("CLICO2"));
				clidir.setComuna(rs.getInt("CLICO3"));
				clidir.setDireccionCliente(rs.getString("CLIDI1"));
				clidir.setLatitud(rs.getString("LATDIR"));
				clidir.setLongitud(rs.getString("LONDIR"));
				
			
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
		return clidir;
	}
	
	public OrdvtaDTO obtieneOrdenVenta(int empresa, int numeroOrden, int codigoBodega, int correlativoDireccion){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOrden+" AND TPTC18="+codigoBodega+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				ordvtaDTO.setCorreDireccionOV(rs.getInt("CLICOR"));
				ordvtaDTO.setDetord(obtieneDetord(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), correlativoDireccion));
				
				
				
				
				
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
		return ordvtaDTO;
	}
	
	public OrdvtaDTO obtieneOrdenVentaFacturacion(int empresa, int numeroOrden, int codigoBodega){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOrden+" AND TPTC18="+codigoBodega+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				ordvtaDTO.setCorreDireccionOV(rs.getInt("CLICOR"));
				ordvtaDTO.setDetalle(obtieneDetordFacturacion(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega()));
				
				
				
				
				
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
		return ordvtaDTO;
	}
	
	
	public List obtieneDetord(int empresa, int numeroOrden, int codigoBodega, int correlativoDireccion){
		DetordDTO detordDTO = null;
		List detalle = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD " + 
        " Where ORDEMP="+empresa+" AND  NUMVEN="+numeroOrden+" AND TPTC18="+codigoBodega+" AND CLICOR="+correlativoDireccion+"   FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
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
				detordDTO.setPrecioBruto(rs.getDouble("EXIPRE"));
				detordDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
				detordDTO.setMontoNeto(rs.getDouble("ORDDMN"));
				detordDTO.setMontoBruto(rs.getInt("MONBRU"));
				detordDTO.setMontoTotal(rs.getInt("VENMO2"));
				detordDTO.setDescuento(rs.getDouble("VENMON"));
				detordDTO.setDescuentoNeto(rs.getDouble("ORDDDS"));
				detordDTO.setCostoNeto(rs.getDouble("ORDDCN"));
				detordDTO.setCostoTotal(rs.getDouble("ORDDCT"));
				detordDTO.setCostoBruto(rs.getDouble("ORDDCS"));
				detordDTO.setPorcentaje(rs.getDouble("VENDES"));
				detordDTO.setMontoExento(rs.getInt("ORDDTE"));
				detordDTO.setFechaDespacho(rs.getInt("VECFE5"));
				detalle.add(detordDTO);
					
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
		return detalle;
	}
	
	public HashMap obtieneDetordFacturacion(int empresa, int numeroOrden, int codigoBodega){
		DetordDTO detordDTO = null;
		List detalle = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		HashMap <Integer, DetordDTO> lista = new HashMap();
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD " + 
        " Where ORDEMP="+empresa+" AND  NUMVEN="+numeroOrden+" AND TPTC18="+codigoBodega+"   FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CAMNUM")==0){
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
					detordDTO.setPrecioBruto(rs.getDouble("EXIPRE"));
					detordDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
					detordDTO.setMontoNeto(rs.getDouble("ORDDMN"));
					detordDTO.setMontoBruto(rs.getInt("MONBRU"));
					detordDTO.setMontoTotal(rs.getInt("VENMO2"));
					detordDTO.setDescuento(rs.getDouble("VENMON"));
					detordDTO.setDescuentoNeto(rs.getDouble("ORDDDS"));
					detordDTO.setCostoNeto(rs.getDouble("ORDDCN"));
					detordDTO.setCostoTotal(rs.getDouble("ORDDCT"));
					detordDTO.setCostoBruto(rs.getDouble("ORDDCS"));
					detordDTO.setPorcentaje(rs.getDouble("VENDES"));
					detordDTO.setMontoExento(rs.getInt("ORDDTE"));
					detordDTO.setFechaDespacho(rs.getInt("VECFE5"));
					detordDTO.setTotalNeto(rs.getInt("ORDDTN"));
					lista.put(detordDTO.getCodigoArticulo(), detordDTO);
				}
				
					
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
		return lista;
	}

	public OrdTranspNcpDTO obtieneTotalesorden(int codigoEmpresa, int bodega, int numeroOV){
		OrdTranspNcpDTO ordvtaDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerCamtra=" Select "+
		" o.CLMRUT, o.CLMDIG, o.EXMFEC, o.CAMCOD, o.CAMES1, o.ORDCAN, o.ORDTCS, o.TOTCNT, o.ORDDSC, o.ORDTDN, o.CLCTOT, "+
		" o.ORDMNT, o.ORDTIV, o.ORDTEX, d.CAMNUM, c.CAMCO3, c.CAMNFA, cl.CLMNOM, cd.VEDNU2, COUNT(d.cldcod) as CANTIART, "+
		" SUM(ROUND(d.ORDDCS,-0)) as TOTCOSTO, SUM(d.ORDDCT) as TOTCOSNET, SUM(d.VENMON) as TOTDESC, SUM(d.ORDDDS) as TOTDESCNET, "+
		" SUM(d.ORDDTN) as TOTNETO, SUM(d.ORDDTE) as TOTEXENTO "+
				
		" FROM CASEDAT.ORDVTA o " +
		" INNER JOIN CASEDAT.DETORD d"+
        " ON o.ORDEMP = d.ORDEMP AND o.NUMVEN = d.NUMVEN AND o.CLMRUT = d.CLMRUT AND o.CLMDIG = d.CLMDIG AND o.TPTC18 = d.TPTC18"+
		" INNER JOIN CASEDAT.CAMTRA c"+
        " ON d.ORDEMP = c.CAMEMP AND c.CAMCO2 in (21,41) AND d.CAMNUM = c.CAMNUM AND d.TPTC18 = c.CAMCO1 AND d.CLMRUT = c.CAMRUT AND d.CLMDIG = c.CAMDVR"+
		" INNER JOIN CASEDAT.CLMCLI cl"+
        " ON o.CLMRUT = cl.CLMRUT AND o.CLMDIG = cl.CLMDIG"+
		" LEFT JOIN CASEDAT.CARGUIOD cd "+
        " ON d.ORDEMP=cd.CACEMP AND d.TPTC18=cd.CAMCO1 AND d.NUMVEN=cd.NUMVEN AND d.ORDEMP=cd.ORDEMP AND d.TPTC18=cd.TPTC18 AND d.CLMRUT=cd.CLMRUT"+
        " AND d.CLMDIG=cd.CLMDIG AND d.CLDCOD=cd.CLDCOD AND d.CORDOV=cd.CORDC2"+
		" WHERE o.ORDEMP = "+codigoEmpresa+
        " AND o.NUMVEN ="+numeroOV+
        " AND o.TPTC18 = "+bodega +
        " GROUP BY "+
        "o.CLMRUT, o.CLMDIG, o.EXMFEC, o.CAMCOD, o.CAMES1, o.ORDCAN, o.ORDTCS, o.TOTCNT, o.ORDDSC, o.ORDTDN, o.CLCTOT, "+
		" o.ORDMNT, o.ORDTIV, o.ORDTEX, d.CAMNUM, c.CAMCO3, c.CAMNFA, cl.CLMNOM, cd.VEDNU2 "+
        " FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {				
						ordvtaDTO = new OrdTranspNcpDTO();
						
						ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
						ordvtaDTO.setDvCliente(rs.getString("CLMDIG"));
						ordvtaDTO.setNombreCliente(rs.getString("CLMNOM").trim());
						ordvtaDTO.setFechaDespacho(rs.getInt("EXMFEC"));
						ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
						ordvtaDTO.setEstadoOV(rs.getString("CAMES1").trim());
						ordvtaDTO.setCantidadTotal(rs.getInt("ORDCAN"));
						ordvtaDTO.setTotalCosto(rs.getInt("TOTCOSTO")); //ORDTCS
						ordvtaDTO.setTotalCostoneto(rs.getInt("TOTCOSNET")); //TOTCNT
						ordvtaDTO.setTotalDescuento(rs.getInt("TOTDESC")); //ORDDSC
						ordvtaDTO.setTotalDescuentoneto(rs.getInt("TOTDESCNET")); //ORDTDN
						ordvtaDTO.setTotalNetoOrden(rs.getInt("TOTNETO")); //ORDMNT
						ordvtaDTO.setTotalIvaOrden(rs.getInt("ORDTIV"));
						ordvtaDTO.setTotalOrden(rs.getInt("CLCTOT"));
						ordvtaDTO.setTotalExento(rs.getInt("TOTEXENTO")); //ORDTEX
						ordvtaDTO.setNumDocumento(rs.getInt("CAMNUM"));
						ordvtaDTO.setTipoDocumento(rs.getInt("CAMCO3"));
						ordvtaDTO.setNumFactbol(rs.getInt("CAMNFA"));
						ordvtaDTO.setCantidadArticulos(rs.getInt("CANTIART"));
						ordvtaDTO.setNumeroGuia(rs.getInt("VEDNU2"));
						
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
		return ordvtaDTO;
		
	}

	
	public int LiberaOVMapas(int empresa, int bodega, String estado, String estadoNuevo){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTA " + 
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
	
	public OrdvtaDTO obtieneOrdenVentaEspecial(int empresa, int numeroOrden, int codigoBodega, int correlativoDireccion, int carguio){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOrden+" AND TPTC18="+codigoBodega+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				ordvtaDTO.setCorreDireccionOV(rs.getInt("CLICOR"));
				ordvtaDTO.setDetord(obtieneDetordEspecial(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), correlativoDireccion, carguio));
				
				
				
				
				
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
		return ordvtaDTO;
	}
	
	public List obtieneDetordEspecial(int empresa, int numeroOrden, int codigoBodega, int correlativoDireccion, int numeroCarguio){
		DetordDTO detordDTO = null;
		List detalle = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		/*String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD D1, CASEDAT.CARGUIOD D2 " + 
        " Where D1.ORDEMP="+empresa+" AND  D1.NUMVEN="+numeroOrden+" AND D1.TPTC18="+codigoBodega+" AND D1.CLICOR="+correlativoDireccion+"   "+
         " AND D2.CACEMP="+empresa+" AND D2.NUMCAR="+numeroCarguio+" AND CAMCO1="+codigoBodega+" AND D1.ORDEMP=D2.ORDEMP AND D1.TPTC18=D2.TPTC18 AND D1.NUMVEN=D2.NUMVEN AND D1.CLDCOD=D2.CLDCOD " ;
		*/
		String sqlObtenerCldmco ="Select D2.ORDEMP AS EMP, D2.NUMVEN AS OV, D2.CLDCOD AS COD, D2.CLDCAN AS CAND1, D1.VEDCA4 AS CAND2, D2.VENFOR AS FOR, "+ 
				" D2.CLICOR AS COR, D2.CLICO1 AS REG, D2.CLICO2 AS CIU, D2.CLICO3 AS COM, D1.CORDOV AS COROV, "+
				" D1.EXIPRE AS PRE1, D1.ORDDPR AS PRE2, D1.ORDDMN AS NET, D1.MONBRU AS BRUTO, D1.VENMO2 AS TOT, D1.VENMON AS DESC1, D1.ORDDDS AS DESC2, "+
				" D1.ORDDCN AS COSNETO, D1.ORDDCT AS COSTOT, D1.ORDDCS AS COSBRUTO, D1.VENDES AS PORCEN, D1.ORDDTE AS EXENTO, D1.VECFE5 AS FECDES"+
		        " from CASEDAT.DETORD D1, CASEDAT.CARGUIOD D2 " + 
		        " Where D1.ORDEMP="+empresa+" AND  D1.NUMVEN="+numeroOrden+" AND D1.TPTC18="+codigoBodega+" AND D1.CLICOR="+correlativoDireccion+"   "+
		         " AND D2.CACEMP="+empresa+" AND D2.NUMCAR="+numeroCarguio+" AND CAMCO1="+codigoBodega+" AND D1.ORDEMP=D2.ORDEMP AND D1.TPTC18=D2.TPTC18 AND D1.NUMVEN=D2.NUMVEN AND D1.CLDCOD=D2.CLDCOD AND D1.CLMRUT=D2.CLMRUT AND D1.CLMDIG=D2.CLMDIG AND D1.CORDOV=D2.CORDC2 AND D1.CAMES1<>'A' " ;
		
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				detordDTO = new DetordDTO();				
				detordDTO.setCodEmpresa(rs.getInt("EMP"));
				detordDTO.setNumOvVenta(rs.getInt("OV"));
				detordDTO.setCodigoArticulo(rs.getInt("COD"));
				detordDTO.setCantidadArticulo(rs.getInt("CAND1"));
				detordDTO.setCantidadFormato(rs.getInt("CAND2"));
				detordDTO.setFormato(rs.getString("FOR").trim());
				detordDTO.setCorreDirecciones(rs.getInt("COR"));
				detordDTO.setCodRegion(rs.getInt("REG"));
				detordDTO.setCodCiudad(rs.getInt("CIU"));
				detordDTO.setCodComuna(rs.getInt("COM"));
				detordDTO.setCorrelativoDetalleOV(rs.getInt("COROV"));
				detordDTO.setPrecioBruto(rs.getDouble("PRE1"));
				detordDTO.setPrecioNeto(rs.getDouble("PRE2"));
				detordDTO.setMontoNeto(rs.getDouble("NET"));
				detordDTO.setMontoBruto(rs.getInt("BRUTO"));
				detordDTO.setMontoTotal(rs.getInt("TOT"));
				detordDTO.setDescuento(rs.getDouble("DESC1"));
				detordDTO.setDescuentoNeto(rs.getDouble("DESC2"));
				detordDTO.setCostoNeto(rs.getDouble("COSNETO"));
				detordDTO.setCostoTotal(rs.getDouble("COSTOT"));
				detordDTO.setCostoBruto(rs.getDouble("COSBRUTO"));
				detordDTO.setPorcentaje(rs.getDouble("PORCEN"));
				detordDTO.setMontoExento(rs.getInt("EXENTO"));
				detordDTO.setFechaDespacho(rs.getInt("FECDES"));
				detalle.add(detordDTO);
				
				/*detordDTO = new DetordDTO();				
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
				detordDTO.setPrecioBruto(rs.getDouble("EXIPRE"));
				detordDTO.setPrecioNeto(rs.getDouble("ORDDPR"));
				detordDTO.setMontoNeto(rs.getDouble("ORDDMN"));
				detordDTO.setMontoBruto(rs.getInt("MONBRU"));
				detordDTO.setMontoTotal(rs.getInt("VENMO2"));
				detordDTO.setDescuento(rs.getDouble("VENMON"));
				detordDTO.setDescuentoNeto(rs.getDouble("ORDDDS"));
				detordDTO.setCostoNeto(rs.getDouble("ORDDCN"));
				detordDTO.setCostoTotal(rs.getDouble("ORDDCT"));
				detordDTO.setCostoBruto(rs.getDouble("ORDDCS"));
				detordDTO.setPorcentaje(rs.getDouble("VENDES"));
				detordDTO.setMontoExento(rs.getInt("ORDDTE"));
				detordDTO.setFechaDespacho(rs.getInt("VECFE5"));*/
				//detalle.add(detordDTO);
					
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
		return detalle;
	}
	
	
	public int insertaOVProveedor(OrdvtaDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO  "+
        "  CASEDAT.ORDELEP " + 
        " (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,RUTPEL) VALUES("+orden.getCodigoEmpresa()+","+orden.getNumeroOV()+","+orden.getRutCliente()+",'"+orden.getDvCliente()+"',"+orden.getCodigoBodega()+",'"+orden.getRutProveedor()+"') " ;
		log.info("SQL Orden" + sqlObtenerCldmco);
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
	
	public int insertaAdicionalesOV(OrdvtaDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO  "+
        "  CASEDAT.ORDVTAD " + 
        " (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,XONM5,XOTX1) VALUES("+orden.getCodigoEmpresa()+","+orden.getNumeroOV()+","+orden.getRutCliente()+",'"+orden.getDvCliente()+"',"+orden.getCodigoBodega()+","+orden.getTipoDocumento()+",'"+orden.getIdBanco()+"') " ;
		log.info("SQL Orden" + sqlObtenerCldmco);
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
	
	public int obtieneCorrelativoWEB(int bodega){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTANM " + 
        " Where TPTC18="+bodega+" AND OBF007='3' FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("OBF00F");
				actualizarCorrelativoWEB(bodega, rs.getInt("OBF00F")+1);
				
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
		return correlativo;
	}
	
	public int actualizarCorrelativoWEB(int bodega, int correlativo){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTANM " + 
        " SET OBF00F="+correlativo+" Where TPTC18="+bodega+" AND OBF007='3'  " ;
		
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
	
	public OrdTranspNcpDTO obtieneTotalesordenNumdoc(int codigoEmpresa, int bodega, int numeroDoc, int tipoDoc, int numcar, int rutChofer, String patente){
		OrdTranspNcpDTO ordvtaDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String connumerito =" AND cd.VEDNU2="+numeroDoc+"";
		
		String sqlObtenerCam=" SELECT c.CAMRUT, c.CAMNFA FROM CASEDAT.CAMTRA c"+
		" WHERE c.CAMEMP = "+codigoEmpresa+
        " AND c.CAMCO2 in (2,9,10,21,41) "+
        " AND c.CAMCO1= "+bodega +
        " AND c.CAMNFA="+numeroDoc+
        " AND c.CAMCO3="+tipoDoc+
        " AND c.CAMFEC>=20170601"+
        " GROUP BY c.CAMRUT, c.CAMNFA "+
        " FOR READ ONLY" ;
		log.info("SELECT PRINCIPAL CAMTRA ORDVTA Y DETORD : "+sqlObtenerCam);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCam);
			rs = pstmt.executeQuery();
			log.info("O K E Y   SELECT PRINCIPAL CAMTRA ORDVTA Y DETORD");
			while (rs.next()) {
				connumerito=" AND o.CLMRUT="+rs.getInt("CAMRUT")+" AND c.CAMNFA ="+numeroDoc+ "";
				break;
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
		
		
		String sqlObtenerCamtra=" SELECT "+
		" cd.NUMCAR, o.NUMVEN, o.CLMRUT, o.CLMDIG, o.EXMFEC, o.CAMCOD, o.CAMES1, d.CAMNUM, cd.VEDNU2, d.VECFE5, c.CAMCO3, c.CAMNFA, ddo.TPADE1, cl.CLMNOM, cd.VEDNU2, "+
		" c.CAMFEC, c.CAMVAL, cve.EXMEMA, ttve.EXMNO1, o.ORDCAN, o.ORDTCS, o.TOTCNT, o.ORDDSC, o.ORDTDN, o.CLCTOT, "+
		" o.ORDMNT, o.ORDTIV, o.ORDTEX, bb.bbhorvta, di.CLIDI1, comu.TPTD20, vec.VENCO5, vec.VENNU2, vec.VENFE1,+"
		+ "vec.VENTO2, "+
		" COUNT(d.cldcod) as CANTIART, SUM(ROUND(d.ORDDCS,-0)) as TOTCOSTO, SUM(d.ORDDCT) as TOTCOSNET, SUM(d.VENMON) as TOTDESC,"+
		" SUM(d.ORDDDS) as TOTDESCNET, SUM(d.ORDDTN) as TOTNETO, SUM(d.ORDDTE) as TOTEXENTO"+
		" FROM CASEDAT.ORDVTA o " +
		" INNER JOIN CASEDAT.DETORD d ON o.ORDEMP=d.ORDEMP AND o.NUMVEN=d.NUMVEN AND o.CLMRUT=d.CLMRUT AND o.CLMDIG=d.CLMDIG AND o.TPTC18=d.TPTC18"+
        " INNER JOIN CASEDAT.CLMCLI cl ON o.CLMRUT=cl.CLMRUT AND o.CLMDIG=cl.CLMDIG"+
        " INNER JOIN CASEDAT.CARGUIOD cd ON d.ORDEMP=cd.CACEMP AND d.TPTC18=cd.CAMCO1 AND d.NUMVEN=cd.NUMVEN "+
        " AND d.TPTC18=cd.TPTC18 AND d.CLMRUT=cd.CLMRUT AND d.CLMDIG=cd.CLMDIG AND d.CLDCOD=cd.CLDCOD AND d.CORDOV=cd.CORDC2"+
        " INNER JOIN CASEDAT.CARGUIOC cc ON cd.CACEMP=cc.CACEMP AND cd.NUMCAR=cc.NUMCAR AND cd.VECPAT=cc.VECPAT AND cd.CAMCO1=cc.CAMCO1"+
        " LEFT JOIN CASEDAT.CLIDIR di ON cd.CLMRUT=di.CLIRUT AND cd.CLMDIG=di.CLIDIG AND cd.CLICOR=di.CLICOR"+
        " LEFT JOIN CASEDAT.TPTCOM comu ON cd.CLICO1=comu.TPTC19 AND cd.CLICO2=comu.TPTC20 AND cd.CLICO3=comu.TPTC22"+
		" LEFT JOIN CASEDAT.EXMOTV cve ON o.CAMCOD=cve.EXMC42"+
		" LEFT JOIN CASEDAT.EXMVND ttve ON o.CAMCOD=ttve.EXMC09"+
        " LEFT JOIN CASEDAT.CAMTRA c ON d.ORDEMP=c.CAMEMP AND c.CAMCO2 in (21,41) AND d.CAMNUM=c.CAMNUM AND d.TPTC18=c.CAMCO1 AND d.CLMRUT=c.CAMRUT AND d.CLMDIG=c.CAMDVR AND c.CAMFEC>=20170601 "+
		" left join casedat.tptdoc ddo ON c.CAMCO3=ddo.TPACO1"+
		" LEFT JOIN CASEDAT.BBINTC00 bb ON bb.BBCODDOC IN (3,4) AND o.EXMFEC=bb.BBFECMOV AND d.CAMNUM=bb.BBNUMDOC AND o.TPTC18=bb.BBCODBOD"+
        " AND o.CLMRUT=bb.BBRUTCLI AND o.CLMDIG=bb.BBDIGCLI AND o.CAMCOD=bb.BBCODVEN AND bb.BBFECMOV>=20170601"+
        " LEFT JOIN CASEDAT.VECMAR vec ON o.ORDEMP=vec.VENEMP AND vec.VENCOD=39 AND o.TPTC18=vec.VENBO2 AND o.CLMRUT=vec.VENRUT AND cd.VEDNU2=vec.VENNU2 AND vec.VENFEC>=20170601 "+
        //" left join casedat.tptdoc doo ON vec.VENCO5=doo.TPACO1"+
        " WHERE o.ORDEMP = "+codigoEmpresa+
        " AND o.TPTC18 = "+bodega +connumerito+
        " AND cc.NUMCAR = "+numcar+
        " AND cc.CAMRUT="+rutChofer+
        " AND cc.VECPAT='"+patente+"'"+
        " GROUP BY "+
        " cd.NUMCAR, o.NUMVEN, o.CLMRUT, o.CLMDIG, o.EXMFEC, o.CAMCOD, o.CAMES1, cd.VEDNU2, d.VECFE5, c.CAMCO3, c.CAMNFA, ddo.TPADE1, cl.CLMNOM, cd.VEDNU2, c.CAMFEC, c.CAMVAL, cve.EXMEMA, "+
        " ttve.EXMNO1, o.ORDCAN, o.ORDTCS, o.TOTCNT, o.ORDDSC, o.ORDTDN, o.CLCTOT, "+
		" o.ORDMNT, o.ORDTIV, o.ORDTEX, d.CAMNUM, bb.bbhorvta,di.CLIDI1, comu.TPTD20, vec.VENCO5, vec.VENNU2, vec.VENFE1, vec.VENTO2 "+
        " FOR READ ONLY" ;
		log.info("SELECT PRINCIPAL ORDVTA Y DETORD");
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			rs = pstmt.executeQuery();
			log.info("O K E Y   SELECT PRINCIPAL ORDVTA Y DETORD");
			while (rs.next()) {				
						ordvtaDTO = new OrdTranspNcpDTO();
						
						ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
						ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
						ordvtaDTO.setDvCliente(rs.getString("CLMDIG"));
						ordvtaDTO.setNombreCliente(rs.getString("CLMNOM").trim());
						ordvtaDTO.setFechaDespacho(rs.getInt("EXMFEC"));
						ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
						ordvtaDTO.setNombreVendedor(rs.getString("EXMNO1"));
						ordvtaDTO.setEstadoOV(rs.getString("CAMES1").trim());
						ordvtaDTO.setCantidadTotal(rs.getInt("ORDCAN"));
						ordvtaDTO.setTotalCosto(rs.getInt("TOTCOSTO")); //ORDTCS
						ordvtaDTO.setTotalCostoneto(rs.getInt("TOTCOSNET")); //TOTCNT
						ordvtaDTO.setTotalDescuento(rs.getInt("TOTDESC")); //ORDDSC
						ordvtaDTO.setTotalDescuentoneto(rs.getInt("TOTDESCNET")); //ORDTDN
						ordvtaDTO.setTotalNetoOrden(rs.getInt("TOTNETO")); //ORDMNT
						ordvtaDTO.setTotalIvaOrden(rs.getInt("ORDTIV"));
						ordvtaDTO.setTotalOrden(rs.getInt("CLCTOT"));
						ordvtaDTO.setTotalExento(rs.getInt("TOTEXENTO")); //ORDTEX
						if (rs.getInt("CAMCO3")>0){
						ordvtaDTO.setTipoDocumento(rs.getInt("CAMCO3"));
						ordvtaDTO.setNombreDocumento(rs.getString("TPADE1"));
						}
						if (rs.getString("TPADE1")==null){
							ordvtaDTO.setNombreDocumento(rs.getString("TPADE1"));
						}
						
						if (rs.getInt("CAMNUM")>0){
						ordvtaDTO.setNumDocumento(rs.getInt("CAMNUM"));
						}
						if (rs.getInt("CAMNFA")>0){
						ordvtaDTO.setNumFactbol(rs.getInt("CAMNFA"));
						}
						if (rs.getInt("VEDNU2")>0){
							ordvtaDTO.setNumeroGuia(rs.getInt("VEDNU2"));
							
							if (rs.getInt("VENCO5")>0){
								ordvtaDTO.setTipoDocumento(rs.getInt("VENCO5"));
								ordvtaDTO.setNombreDocumento(rs.getString("TPADE1"));
							}
						}
						if (rs.getInt("VEDNU2")>0 && rs.getInt("BBHORVTA")<=0){
						ordvtaDTO.setNumFactbol(rs.getInt("VEDNU2"));
						}
						ordvtaDTO.setCantidadArticulos(rs.getInt("CANTIART"));
						ordvtaDTO.setFechaEntrega(rs.getInt("VECFE5"));
						if (rs.getInt("CAMFEC")>0){
						ordvtaDTO.setFechaFactura(rs.getInt("CAMFEC"));
						}
						if (rs.getInt("VENFE1")>0 && (rs.getInt("CAMFEC")<=0)){
						ordvtaDTO.setFechaFactura(rs.getInt("VENFE1"));
						}
						if (rs.getInt("CAMVAL")>0){
						ordvtaDTO.setMontoFactura(rs.getInt("CAMVAL"));
						}
						if (rs.getInt("CAMVAL")<=0 && (rs.getInt("VENTO2")>0)){
						ordvtaDTO.setMontoFactura(rs.getInt("VENTO2"));
						}
						ordvtaDTO.setCorreoVendedor(rs.getString("EXMEMA"));
						ordvtaDTO.setHoraOV(rs.getInt("BBHORVTA"));
						ordvtaDTO.setDireccionCliente(rs.getString("CLIDI1"));
						ordvtaDTO.setDescripcionComuna(rs.getString("TPTD20"));
						if (rs.getString("TPADE1")==null){
							ordvtaDTO.setNombreDocumento(rs.getString("TPADE1"));
						}
						
						//System.out.println("SQL CLC" + sqlObtenerCldmco);
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
		return ordvtaDTO;
		
	}
}
