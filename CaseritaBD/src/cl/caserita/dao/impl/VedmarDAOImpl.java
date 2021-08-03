package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.PrecioDescDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public class VedmarDAOImpl implements VedmarDAO{

	private static Logger log = Logger.getLogger(VedmarDAOImpl.class);
	private Connection conn;
	
	public VedmarDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtenerDatosVedmar(int empresa, int tipoMov, int fecha, int numero){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"'  AND VEDPRN<10 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				ved.add(vedmar);
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
		
		
		return ved;
	}
	
	
	public List ListaPedidossinVedmar( int fecha){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT VENEMP, VENCOD, VENFEC, VENNUM, VENBO2, VENRUT, VENDIG FROM "+
        " CASEDAT.VECMAR WHERE VENNUM NOT IN ( SELECT VENNU4 FROM " + 
        "  CASEDAT.VEDMAR) AND VENCOD =21 AND VENFEC ="+fecha+" AND VENEMP =2" ;
		
		/*SELECT VENEMP, VENCOD, VENFEC, VENNUM, VENBO2, VENRUT, VENDIG FROM
		CASEDAT/VECMAR WHERE VENNUM NOT IN ( SELECT VENNU4 FROM           
		CASEDAT/VEDMAR) AND VENCOD =21 AND VENFEC =20160217 AND VENEMP =2 */
		
		
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENCOD"));
				vedmar.setFechaMvto(rs.getInt("VENFEC"));
				vedmar.setNumDocumento(rs.getInt("VENNUM"));
				vedmar.setCodigoBodega(rs.getInt("VENBO2"));
				ved.add(vedmar);
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
		
		
		return ved;
	}
	
	
	public List consultaArticulosPrecio(int empresa, int tipoMov, int fecha, int bodega){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENC15="+bodega+" AND VEDPRN<10   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				ved.add(vedmar);
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
		
		
		return ved;
	}
	public String obtieneDescripcion(int codigo, String dv){
		String des="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMART " + 
        " Where EXMCOD="+codigo+" AND EXMDIG='"+dv+"'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				des = rs.getString("EXMDES");
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
		
		
		return des;
	}
	public List obtenerDatosVedmarGuia(int empresa, int tipoMov, int fecha, int numero){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"'   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				
				vedmar.setPrecioUnidad(calculaPrecio(vedmar.getCodigoArticulo(), vedmar.getDigArticulo(), rs.getDouble("VEDPR2")));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				ved.add(vedmar);
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
		
		
		return ved;
	}
	
	public double calculaPrecio(int articulo, String dv, double valorUni){
		ExtariDTO extari=null;
		double impuesto=0;
		double tasa=0;
		PreparedStatement pstmt =null;
		List extariList = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXTARI E, CASEDAT.TPTIMP T " + 
        " WHERE E.EXTCO1="+articulo+" AND E.EXTDI2='"+dv+"' AND E.EXTCO2= T.TPTC32 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				extari = new ExtariDTO();
					tasa = tasa + rs.getDouble("TPTVA4");	
			}
			tasa = tasa / 100 + 1;
			impuesto = valorUni / tasa ;
			
			
			
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
		
		
		return impuesto;
	}
	
	public int generaMovimiento(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		/*VEDEMP, VENC11, VENFE3, VENNU4, VENCOR,
		VENC15, VENC16, VENDI1, VENFOR, VENCA2,
		VENCA1, VEDSE1, VENPES, VEDPR2, VEDPRN,
		VEDCNT, VEDCTN, VEDMO4, VENMO2, VENMTN,
		VENEXE, VENCO3 */
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VEDMAR " + 
        " (VEDEMP, VENC11,VENFE3,VENNU4,VENCOR,VENC15,VENC16,VENDI1,VENFOR, VENCA2,VENCA1,VEDSE1,VENPES, VEDPR2, VEDPRN,VEDCNT,VEDCTN,VEDMO4,VENMO2,VEDMNT, VENMTN,VENEXE,VENCO3, VENDES, VENMON,VENMDN) VALUES("+vedmar.getCodigoEmpresa()+","+vedmar.getCodTipoMvto()+","+vedmar.getFechaMvto()+","+vedmar.getNumDocumento()+","+vedmar.getCorrelativo()+", "+vedmar.getCodigoBodega()+","+vedmar.getCodigoArticulo()+",'"+vedmar.getDigArticulo()+"','"+vedmar.getFormato()+"',"+vedmar.getCantidadFormato()+","+vedmar.getCantidadArticulo()+","+vedmar.getSectorBodega()+","+vedmar.getPesoLinea()+","+vedmar.getPrecioUnidad()+","+vedmar.getPrecioNeto()+","+vedmar.getCostoNeto()+","+vedmar.getCostoTotalNeto()+","+vedmar.getMontoBrutoLinea()+","+vedmar.getMontoTotalLinea()+","+vedmar.getMontoNeto()+","+vedmar.getMontoTotalNetoLinea()+","+vedmar.getMontoExento()+",'"+vedmar.getCodIngresoSalida()+"',"+vedmar.getPorcentajeDesto()+","+vedmar.getMontoDescuentoLinea()+","+vedmar.getMontoDescuentoNeto()+") " ;
		log.info("INSERTA VEDMAR" + sqlObtenerVecmar);
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
	
	public List obtenerDatosVedmarMer(int empresa, int tipoMov, int fecha, int numero){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				ved.add(vedmar);
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
		
		
		return ved;
	}
	
	public HashMap obtenerDatosVedmarMerHash(int empresa, int tipoMov, int fecha, int numero){
		HashMap <Integer, VedmarDTO> ved= new HashMap();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				ved.put(vedmar.getCodigoArticulo(), vedmar);
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
		
		
		return ved;
	}
	
	
	public HashMap obtenerDatosVedmarNoHay(int empresa, int tipoMov, int fecha, int numero){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		HashMap <Integer, VedmarDTO> lista= new HashMap();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setCostoNeto(rs.getDouble("VEDCNT"));
				vedmar.setCostoTotalNeto(rs.getDouble("VEDCTN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				vedmar.setCodigoEmpresa(rs.getInt("VEDEMP"));
				//lista.put(vedmar.getCodigoArticulo(), vedmar);
				lista.put(vedmar.getCorrelativo(), vedmar);
				//ved.add(vedmar);
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
		
		
		return lista;
	}
	
	public VecmarDTO recuperaTotales(int empresa, int tipoMov, int fecha, int numero){
		List ved= new ArrayList();
		VecmarDTO vecmar = null;
		PreparedStatement pstmt =null;
		HashMap <Integer, VedmarDTO> lista= new HashMap();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select sum(VENMTN) AS NETO, SUM(VENMO2) AS TOTAL "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
//			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmar = new VecmarDTO();
				log.info(rs.getDouble("TOTAL"));
				int total = (int ) rs.getDouble("TOTAL");
						int neto = (int)rs.getDouble("NETO");
				vecmar.setTotalDocumento(total);
				vecmar.setTotalNeto(neto);
				
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
		
		
		return vecmar;
	}
	
	
	public int generaMovimientoNoHay(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		/*VEDEMP, VENC11, VENFE3, VENNU4, VENCOR,
		VENC15, VENC16, VENDI1, VENFOR, VENCA2,
		VENCA1, VEDSE1, VENPES, VEDPR2, VEDPRN,
		VEDCNT, VEDCTN, VEDMO4, VENMO2, VENMTN,
		VENEXE, VENCO3 */
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VEDFALT " + 
        " (VENEMP,VENCOD,VENFEC,VENNUM,VENCOR,VENC15, VENC16,VENDI1,VENFOR) VALUES("+vedmar.getCodigoEmpresa()+","+vedmar.getCodTipoMvto()+") " ;
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
	
	public int actualizaMerma(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.VEDMAR " + 
        " SET VENCA2="+vedmar.getCantidadFormato()+", VENCA1="+vedmar.getCantidadArticulo()+",VEDCTN="+vedmar.getCostoTotalNeto()+", VEDMO4="+vedmar.getMontoBrutoLinea()+",VEDMNT="+vedmar.getMontoNeto()+",VENMO2="+vedmar.getMontoTotalNetoLinea()+",VENMTN="+vedmar.getMontoTotalLinea()+" WHERE VEDEMP="+vedmar.getCodigoEmpresa()+" AND VENC11="+vedmar.getCodTipoMvto()+" AND VENFE3="+vedmar.getFechaMvto()+" AND VENNU4="+vedmar.getNumDocumento()+"  " ;
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
	
	public int eliminaDetalle(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerVecmar="DELETE FROM"+
        "  CASEDAT.VEDMAR " + 
        " WHERE VEDEMP="+vedmar.getCodigoEmpresa()+" AND VENC11="+vedmar.getCodTipoMvto()+" AND VENFE3="+vedmar.getFechaMvto()+" AND VENNU4="+vedmar.getNumDocumento()+" AND VENCOR="+vedmar.getCorrelativo()+"  " ;
		log.info("E L I M I N A   D E T A L L E " + sqlObtenerVecmar);
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
	
	
	public int eliminaDetallePromocion(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerVecmar="DELETE FROM"+
        "  CASEDAT.VEDMAR " + 
        " WHERE VEDEMP="+vedmar.getCodigoEmpresa()+" AND VENC11="+vedmar.getCodTipoMvto()+" AND VENFE3="+vedmar.getFechaMvto()+" AND VENNU4="+vedmar.getNumDocumento()+" AND VENC16<>"+vedmar.getCodigoArticulo()+" AND VEDMO3<>0  " ;
		log.info("BORRA ARTICULO PROMO" + sqlObtenerVecmar);
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
	
	public int obtenerCorrelativo(int empresa, int tipoMov, int fecha, int numero){
		PreparedStatement pstmt =null;
		int correlativo=0;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select MAX(VENCOR) AS VENCOR "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo = rs.getInt("VENCOR")+1;
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
		log.info("Correlativo Obtenido :" +correlativo);
		
		return correlativo;
	}
	
	public VedmarDTO buscaArticuloVedmar(VedmarDTO dto){
		VedmarDTO vecmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco ="Select * "+
				" from CASEDAT.VEDMAR " + 
				" Where VEDEMP="+dto.getCodigoEmpresa()+
				" AND VENC11="+dto.getCodTipoMvto()+
				" AND VENNU4="+dto.getNumDocumento()+
				" AND VENC15="+dto.getCodigoBodega()+
				" AND VENC16="+dto.getCodigoArticulo()+
				" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmar = new VedmarDTO();
				
				vecmar.setCodigoArticulo(rs.getInt("VENC16"));
				vecmar.setCantidadArticulo(rs.getInt("VENCA2"));;
				vecmar.setCostoTotalNeto(rs.getDouble("VEDCTN"));
				vecmar.setMontoNeto(rs.getDouble("VEDMNT"));
				vecmar.setMontoTotalNetoLinea(rs.getInt("VENMTN"));
				vecmar.setPrecioNeto(rs.getDouble("VEDPRN"));
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
		return vecmar;
	}
	
	public int actualizaArticulo(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		//" venpes = venpes + "+vedmar.getPesoLinea()+","+
		//" venvol = venvol + "+vedmar.getVolumenArticulo()+","+
		
		String sqlObtenerVecmar="UPDATE CASEDAT.VEDMAR " +
				" SET venca2 = venca2 + "+vedmar.getCantidadFormato()+","+
				" venca1 = venca1 + "+vedmar.getCantidadArticulo()+","+
				" vedmo4 = vedmo4 + "+vedmar.getMontoBrutoLinea()+","+
				" venmo2 = venmo2 + "+vedmar.getMontoTotalLinea()+","+
				" vedctn = vedctn + "+vedmar.getCostoTotalNeto()+","+
				" vedmnt = vedmnt + "+vedmar.getMontoNeto()+","+
				" venmtn = venmtn + "+vedmar.getMontoTotalNetoLinea()+
				" WHERE VEDEMP="+vedmar.getCodigoEmpresa()+
				" AND VENC11="+vedmar.getCodTipoMvto()+
				" AND VENNU4="+vedmar.getNumDocumento()+
				" AND VENC15="+vedmar.getCodigoBodega()+
				" AND VENC16="+vedmar.getCodigoArticulo();
		log.info("ACTUALIZO ARTICULO VEDMAR " + sqlObtenerVecmar);
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
	
	public int actualizaArticuloNOHAY(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		//" venpes = venpes + "+vedmar.getPesoLinea()+","+
		//" venvol = venvol + "+vedmar.getVolumenArticulo()+","+
		
		String sqlObtenerVecmar="UPDATE CASEDAT.VEDMAR " +
				" SET venca2 =  "+vedmar.getCantidadFormato()+","+
				" venca1 = "+vedmar.getCantidadArticulo()+","+
				" vedmo4 =  "+vedmar.getMontoBrutoLinea()+","+
				" venmo2 ="+vedmar.getMontoTotalLinea()+","+
				" vedctn =  "+vedmar.getCostoTotalNeto()+","+
				" vedmnt = "+vedmar.getMontoNeto()+","+
				" venmtn =  "+vedmar.getMontoTotalNetoLinea()+","+
				" venmon = "+vedmar.getMontoDescuentoLinea()+","+
				" venmdn = "+vedmar.getMontoDescuentoNeto()+
				" WHERE VEDEMP="+vedmar.getCodigoEmpresa()+
				" AND VENC11="+vedmar.getCodTipoMvto()+
				" AND VENNU4="+vedmar.getNumDocumento()+
				" AND VENC15="+vedmar.getCodigoBodega()+
				" AND VENC16="+vedmar.getCodigoArticulo();
		log.info("ACTUALIZO ARTICULO VEDMAR " + sqlObtenerVecmar);
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
	
	public double calculaImpuestosArticulo(int articulo, String dv){
		ExtariDTO extari=null;
		double impuesto=0;
		double tasa=0;
		PreparedStatement pstmt =null;
		List extariList = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXTARI E, CASEDAT.TPTIMP T " + 
        " WHERE E.EXTCO1="+articulo+" AND E.EXTDI2='"+dv+"' AND E.EXTCO2= T.TPTC32 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				extari = new ExtariDTO();
					tasa = tasa + rs.getDouble("TPTVA4");	
			}
			tasa = tasa / 100 + 1;
			impuesto = tasa ;
			
			
			
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
		
		
		return impuesto;
	}
	
	public void actualizaSwitchVecmar(int empresa, int codigo, int fecha, int numero, int swi){
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="update "+
        "  CASEDAT.VEDMAR " + 
        " set VENSW1="+swi+" Where VEDEMP="+empresa+" AND VENC11="+codigo+" AND VENFE3="+fecha+" AND VENNU4="+numero+" " ;
		
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
	
	
	public int obtieneCantidadLineas(int empresa, int tipoMov, int fecha, int numero){
		int cantidad=0;
	PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select COUNT(*) AS CANTI "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"'   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
//			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cantidad= rs.getInt("CANTI");
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
		
		
		return cantidad;
	}
	
	public int actualizaFecha(VedmarDTO vedmar){
		int res=0;
		PreparedStatement pstmt =null;
		
		
		String sqlObtenerVecmar="UPDATE"+
        "  CASEDAT.VEDMAR " + 
        " SET VENFE3="+vedmar.getFechaGuiaDespacho()+" WHERE VEDEMP="+vedmar.getCodigoEmpresa()+" AND VENC11="+vedmar.getCodTipoMvto()+" AND VENFE3="+vedmar.getFechaMvto()+" AND VENNU4="+vedmar.getNumDocumento()+"  " ;
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
	
	public VedmarDTO obtenerDatosVedmarNoHayCorrelativo(int empresa, int tipoMov, int fecha, int numero, int correlativo){
		List ved= new ArrayList();
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' AND VENCOR="+correlativo+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL obtenerDatosVedmarNoHayCorrelativo" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setCostoNeto(rs.getDouble("VEDCNT"));
				vedmar.setCostoTotalNeto(rs.getDouble("VEDCTN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setMontoDescuentoNeto(rs.getDouble("VENMDN"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				vedmar.setCodigoEmpresa(rs.getInt("VEDEMP"));
				//ved.add(vedmar);
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
		
		
		return vedmar;
	}
	
	public int verificaVenta(int empresa, int tipoMov, int fecha, int numero){
		PreparedStatement pstmt =null;
		int resp=0;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				if (rs.getInt("VENC16")!=7777777 && rs.getInt("VENC16")!=22 && rs.getInt("VENC16")!=4632){
					resp=resp+1;
					break;
				}
				
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
		
		
		return resp;
	}
	
	public int verificaArticulosVentas(int empresa, int tipoMov, int fecha, int numero){
		PreparedStatement pstmt =null;
		int resp=0;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				
					resp=resp+1;
					
				
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
		
		
		return resp;
	}
	
	public VedmarDTO obtenerDatosArticulo(int empresa, int tipoMov, int fecha, int numero, int articulo, int correlativo){
		
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		HashMap <Integer, VedmarDTO> lista= new HashMap();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' AND VENC16="+articulo+" AND VEDNU2!=0 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setCostoNeto(rs.getDouble("VEDCNT"));
				vedmar.setCostoTotalNeto(rs.getDouble("VEDCTN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				vedmar.setCodigoEmpresa(rs.getInt("VEDEMP"));
				
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
		
		
		return vedmar;
	}
	
	public VedmarDTO obtenerArticuloDifCorrelativo(int empresa, int tipoMov, int fecha, int numero, int articulo, int correlativo){
		
		VedmarDTO vedmar = null;
		PreparedStatement pstmt =null;
		HashMap <Integer, VedmarDTO> lista= new HashMap();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VEDEMP="+empresa+" AND VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' AND VENC16="+articulo+" AND VENCOR!="+correlativo+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vedmar = new VedmarDTO();
				vedmar.setCodTipoMvto(rs.getInt("VENC11"));
				vedmar.setFechaMvto(rs.getInt("VENFE3"));
				vedmar.setNumDocumento(rs.getInt("VENNU4"));
				vedmar.setCorrelativo(rs.getInt("VENCOR"));
				vedmar.setCodigoBodega(rs.getInt("VENC15"));
				vedmar.setCodigoArticulo(rs.getInt("VENC16"));
				vedmar.setDigArticulo(rs.getString("VENDI1"));
				vedmar.setFormato(rs.getString("VENFOR"));
				vedmar.setCantidadFormato(rs.getInt("VENCA2"));
				vedmar.setCantidadArticulo(rs.getInt("VENCA1"));
				vedmar.setSectorBodega(rs.getInt("VEDSE1"));
				vedmar.setPesoLinea(rs.getDouble("VENPES"));
				vedmar.setVolumenArticulo(rs.getDouble("VENVOL"));
				vedmar.setPrecioNeto(rs.getDouble("VEDPRN"));
				vedmar.setCostoNeto(rs.getDouble("VEDCNT"));
				vedmar.setCostoTotalNeto(rs.getDouble("VEDCTN"));
				vedmar.setPrecioUnidad(rs.getDouble("VEDPR2"));
				vedmar.setMontoBrutoLinea(rs.getDouble("VEDMO4"));
				vedmar.setMontoDescuentoLinea(rs.getDouble("VENMON"));
				vedmar.setPorcentajeDesto(rs.getDouble("VENDES"));
				vedmar.setMontoFlete(rs.getDouble("VEDMO3"));
				vedmar.setMontoTotalLinea(rs.getInt("VENMO2"));
				vedmar.setCodIngresoSalida(rs.getString("VENCO3"));
				vedmar.setSwitchProceso(rs.getInt("VENSW1"));
				vedmar.setFechaGuiaDespacho(rs.getInt("VEDFE1"));
				vedmar.setNumeroGuiaDespacho(rs.getInt("VEDNU2"));
				vedmar.setAplicaDescuento(rs.getString("VEDAPL"));
				vedmar.setDescArticulo(obtieneDescripcion(vedmar.getCodigoArticulo(),vedmar.getDigArticulo()));
				vedmar.setCodigoEmpresa(rs.getInt("VEDEMP"));
				
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
		
		
		return vedmar;
	}
}
