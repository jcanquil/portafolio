package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CarguiodDAO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguiocTranspDTO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CarguiodTranspDTO;
import cl.caserita.dto.RedespachocDTO;
import cl.caserita.dto.RedespachodDTO;

public class CarguiodDAOImpl implements CarguiodDAO{

	private  static Logger log = Logger.getLogger(CarguiodDAOImpl.class);

	private Connection conn;
	
	public CarguiodDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int actualizaCarguiod(CarguiodDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		log.info("Correlativo OV "+ dto.getCorrelativoOV());
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARGUIOD " + 
        " SET CLDCAN="+dto.getCantidad()+" , VENCA2="+dto.getCantidad()+", ADSFEC="+dto.getFechaExpiracion()+", VENFOR='"+dto.getFormatoArt()+"'"+
        " Where CACEMP="+dto.getCodigoEmpresa()+" AND CAMCO1="+dto.getCodigoBodega()+" AND NUMCAR="+dto.getNumeroCarguio()+" AND VECPAT='"+dto.getPatente()+"' AND NUMVEN="+dto.getNumeroOrden()+
        " AND CLDCOD="+dto.getCodigoArticulo()+
        " AND CLICOR="+dto.getCorrelativoDireccion()+"  " ;
		log.info("SQL ACTUALIZA CARGUIOD "+ sqlObtenerCldmco);
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
	
	public int eliminaDetalleCarguiod(CarguiodDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="DELETE FROM "+
        " CASEDAT.CARGUIOD " + 
        "  Where CACEMP="+dto.getCodigoEmpresa()+" AND CAMCO1="+dto.getCodigoBodega()+" AND NUMCAR="+dto.getNumeroCarguio()+" AND VECPAT='"+dto.getPatente()+"' AND NUMVEN="+dto.getNumeroOrden()+" AND CLDCOD="+dto.getCodigoArticulo()+"  " ;
		log.info("DETALLE ACTUALIZA CARGUIOD "+ sqlObtenerCldmco);
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
	
	public int actualizaGuia(CarguiodDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARGUIOD " + 
        " SET VEDNU2="+dto.getNumeroGuia()+" Where CACEMP="+dto.getCodigoEmpresa()+" AND CAMCO1="+dto.getCodigoBodega()+" AND NUMCAR="+dto.getNumeroCarguio()+" AND VECPAT='"+dto.getPatente()+"' AND NUMVEN="+dto.getNumeroOrden()+" AND CORDC2="+dto.getCorrelativoOV()+"  " ;
		log.info("SQL ACTUALIZA CARGUIOD "+ sqlObtenerCldmco);
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
	
	public CarguiodDTO buscaOTCarguio(int empresa, int carguio, int bodega, int nroOT){
		CarguiodDTO carguiodDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.CARGUIOD " + 
		        " WHERE CACEMP = "+empresa +
		        " AND NUMCAR = "+carguio +
		        " AND CAMCO1 = "+bodega+
		        " AND CACEMP = EXIMEM "+
		        " AND EXINUM = "+nroOT+
		        " FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				carguiodDTO = new CarguiodDTO();
				carguiodDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				carguiodDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				carguiodDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				carguiodDTO.setPatente(rs.getString("VECPAT"));
				carguiodDTO.setNumeroTraspaso(rs.getInt("EXINUM"));
				carguiodDTO.setFechaDespacho(rs.getInt("VECFE5"));
				carguiodDTO.setCodigoBodegaOrigenOT(rs.getInt("EXIBO4"));
				carguiodDTO.setCodigoBodegaDestinoOT(rs.getInt("EXIBO5"));
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
		return carguiodDTO;

	}
	
	public int actualizaCarguiodOT(CarguiodDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		//llevo todo a unidad solo WMS
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARGUIOD " + 
        " SET VENFOR = '"+dto.getFormatoArt()+"',"+
        " CLDCAN="+dto.getCantidad()+" , VENCA2="+dto.getCantidad()+", ADSFEC="+dto.getFechaExpiracion()+
        " WHERE CACEMP = "+dto.getCodigoEmpresa()+
        " AND CAMCO1 = "+dto.getCodigoBodega()+
        " AND NUMCAR = "+dto.getNumeroCarguio()+
        " AND VECPAT = '"+dto.getPatente()+"'"+
        " AND EXIMEM = CACEMP "+
        " AND EXINUM = "+dto.getNumeroOrden()+
        " AND EXIBO4 = "+dto.getCodigoBodegaOrigenOT()+
        " AND EXIBO5 = "+dto.getCodigoBodegaDestinoOT()+
        " AND CLDCOD = "+dto.getCodigoArticulo();
		log.info("SQL ACTUALIZA CARGUIOD "+ sqlObtenerCldmco);
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
	
	public int liberaDetalleCarguioMapa(int empresa, int codigoBodega, String estado, String estadoNuevo){
		int actualiza=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.CARGUIOD " + 
        " SET CAMES1='"+estadoNuevo.trim()+"' Where CACEMP="+empresa+" AND CAMCO1="+codigoBodega+" AND CAMES1='"+estado.trim()+"'  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("LIBERA CARGUIOS" + sqlObtenerCldmco);
			
			
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
		return actualiza;
	}

	public int buscaExisteChofer(int empresa, int codBodega, int rutChof, String digChof){
		int existerutchof=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco =" SELECT ch.CAMRUT, ch.EXMDI3 "+
		" FROM CASEDAT.CARGUIOC c "+
		" INNER JOIN CASEDAT.CHOFTRAN ch "+ 
		" ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3 "+ 
		" WHERE c.CACEMP="+empresa+" AND c.CAMCO1="+codBodega+
		" AND ch.CAMRUT="+rutChof+
		" GROUP BY ch.CAMRUT, ch.EXMDI3 "+
		" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				existerutchof=1;
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
		return existerutchof;
	}
	
	public int buscaExisteCarguioc(int empresa, int codBodega, int numCarguio, int rutChof, String digChof){
		int existecarguio=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco =" SELECT c.NUMCAR, ch.CAMRUT, c.CAMES1, ch.EXMDI3 "+
		" FROM CASEDAT.CARGUIOC c "+
		" LEFT JOIN CASEDAT.CHOFTRAN ch "+ 
		" ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3 "+ 
		" WHERE c.CACEMP="+empresa+" AND c.CAMCO1="+codBodega+
		" AND c.NUMCAR="+numCarguio+
		" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				if (rs.getInt("CAMRUT")<=0) {
					existecarguio=-1;
				}
				
				if (("E".equals(rs.getString("CAMES1"))) || ("G".equals(rs.getString("CAMES1"))) || ("U".equals(rs.getString("CAMES1")))) {
					existecarguio=-3;
				}
				else {
					existecarguio=-2;
				}
				
				if (existecarguio==-2) {
					if  (rs.getInt("CAMRUT")!=rutChof) {
						existecarguio=-1;
					}
				}
				
				if  (digChof.equals(rs.getString("EXMDI3"))) {
				}
				else {
					existecarguio=-4;
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
		return existecarguio;
	}
	
	public int buscaExisteCarguioTransp(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, String patente){
		int existecarguio=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco =" SELECT c.NUMCAR "+
		" FROM CASEDAT.CARGUIOC c "+
		" INNER JOIN CASEDAT.CARGUIOD d"+ 
		" ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1  "+
		" INNER JOIN CASEDAT.CHOFTRAN ch "+ 
		" ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3 "+ 
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 "+ 
		" AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig  "+
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven "+
		" AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18  "+
		" AND o.cames1=de.cames1  "+
		" INNER JOIN CASEDAT.VECMAR ve "+ 
		" ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut "+ 
		" AND de.clmdig=ve.vendig AND de.camnum=ve.vennum  "+
		" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) "+ 
		" AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut  "+
		" AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa "+
		" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig "+ 
		" WHERE c.CACEMP="+empresa+
		" AND c.CAMCO1="+codBodega+
		" AND c.CAMRUT="+rutChofer+
		" AND c.EXMDI3='"+digChofer+"'"+
		" AND c.NUMCAR="+numCarguio+
		" AND c.VECPAT='"+patente+"'"+
		" AND c.CAMES1 <>'X' "+
		" GROUP BY c.NUMCAR FOR READ ONLY" ;
		//" AND c.CAMES1 IN ('E','G','U') "+
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				existecarguio=1;
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
		return existecarguio;
	}
	
	public String obtieneEstadoCarguioD(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente){
		
		String estado="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco ="SELECT d.XOCL1 "+
		" FROM CASEDAT.CARGUIOC c" +
		" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
		" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
		" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
		" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
		" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
		" WHERE c.CACEMP="+empresa+
		" AND c.NUMCAR="+numCarguio+
		" AND c.CAMRUT="+rutChofer+
		" AND c.EXMDI3='"+digChofer+"'"+
		" AND c.CAMCO1="+codBodega+
		" AND c.VECPAT='"+patente+"'"+
		" AND ca.CAMNFA="+numDocto+
		" GROUP BY d.XOCL1 "+
		" FOR READ ONLY" ;
		//" AND d.XOCL1 IN ('VB','RD') "+
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				estado="";//  rs.getString("XOCL1").trim();
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
		return estado;
	}

	public int obtieneCantidadRedespachos(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente){
		int cantiredespa=1;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String digitoruti="";
		if (digChofer==null || ("".equals(digChofer.trim()))){
			
		} else {
			digitoruti=" AND c.exmdi3='"+digChofer+"'";
		}
		
		String sqlObtenerCldmco ="SELECT d.XONM1 "+
		" FROM CASEDAT.CARGUIOC c" +
		" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
		" left JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum "+
		" AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
		" WHERE c.CACEMP="+empresa+
		" AND c.NUMCAR="+numCarguio+
		" AND c.VECPAT='"+patente+"'"+
		" AND c.CAMRUT="+rutChofer+digitoruti+
		" AND c.CAMCO1="+codBodega+
		" AND (ca.CAMNFA="+numDocto+ " OR d.vednu2="+numDocto+" )"+
		" GROUP BY d.XONM1 "+
		" FOR READ ONLY" ;
		//logi.info("SELECT XONM1 CARGUIOD : "+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			//logi.info("O K E Y   SELECT XONM1 CARGUIOD : "+sqlObtenerCldmco);
			while (rs.next()) {
				cantiredespa = rs.getInt("XONM1");
				cantiredespa++;
				//break;
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
		return cantiredespa;
	}
	
	
	public int actualizaRedespachod(int codEmpresa, int codBodega, int numCarguio, int numOV, int cantiredespa){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARGUIOD d" + 
        " SET XONM1="+cantiredespa+" "+
        " WHERE d.CACEMP="+codEmpresa+
		" AND d.NUMCAR="+numCarguio+
		" AND d.CAMCO1="+codBodega+
		" AND d.NUMVEN="+numOV ;
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

	
	
	
	

	public int actualizaEstadoArticuloCarguiodTransp(String codEstado, String codMotivo, int codEmpresa, int numcarguio, String patente, int codBodega, int numOV, int rutClie, int codArti, int numdocus){
		int actualiza=0;
		PreparedStatement pstmt =null;
		String concodigoart="";
		
		String numguia="";
		if (numdocus>0){
        	numguia = " AND vednu2="+numdocus+" ";
        }
		
		String sqlObtenerCldmco ="UPDATE "+
        " CASEDAT.CARGUIOD " + 
        " SET XOCL1='"+codEstado+"', XOCL2='"+codMotivo+"' "+
        " WHERE cacemp="+codEmpresa+" AND numcar="+numcarguio+
        " AND vecpat='"+patente+"' AND camco1="+codBodega+
        " " +numguia+ " "+
        " AND numven="+numOV+" AND clmrut="+rutClie+concodigoart + " ";
		//logi.info("ANTES DE UPDATE XOCL1 CARGUIOD : "+sqlObtenerCldmco);
        try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			pstmt.executeUpdate();
			actualiza=1;
			//logi.info("O K E Y  UPDATE XOCL1 CARGUIOD  : "+sqlObtenerCldmco);
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


public List buscaCarguiodTransp(int empresa, int rutChofer, String digChofer, int bodega, int tipodespacho, String numerodocto, int numCarg, String patente){
		
		String connumero="";
		
		String cadena=numerodocto;
    	int paip = cadena.indexOf("|");
    	if (paip>0){
			String pipe="";
    		pipe = cadena.substring(paip+1);
    		String numdocsinpaip=cadena.substring(0,paip);
    		paip=Integer.parseInt(pipe);
    		connumero =" AND (ca.CAMNFA="+numdocsinpaip+" OR d.VEDNU2="+numdocsinpaip+") ";
    	
    	} else {
    		
	    	if (Integer.parseInt(numerodocto)>0){
				connumero =" AND (ca.CAMNFA="+numerodocto+" OR d.VEDNU2="+numerodocto+") "; 
			}
    	}
    	
    	String ppatente="";
		if (patente==null || ("".equals(patente.trim()))){
			
		} else {
			ppatente=" AND c.VECPAT='"+patente+"' ";
		}

	    	
		int numcaraux=0;
		int conti=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		List cargu = new ArrayList();
		CarguiocTranspDTO dto = new CarguiocTranspDTO();
		
		List ordenes = new ArrayList();
		
		String sqlObtenerCamtra=" SELECT c.NUMCAR, ca.CAMNFA, ca.CAMCO3, d.VEDNU2, vec.VENCO5, d.XOCL1, d.XOCL2 "+
				" FROM CASEDAT.CARGUIOD d" +
				" INNER JOIN CASEDAT.CARGUIOC c ON d.cacemp=c.cacemp AND d.numcar=c.numcar AND d.vecpat=c.vecpat AND d.camco1=c.camco1 " +
				" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
				" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
				" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
				" left JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut "+
				" AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
				" left JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 "+
				" AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
				" left JOIN CASEDAT.VECMAR vec ON de.ordemp=vec.venemp AND de.tptc18=vec.venbo2 AND vec.vencod=39"+
				" AND d.vednu2=vec.vennu2 AND de.clmrut=vec.venrut AND de.clmdig=veC.vendig " +
				" WHERE c.CACEMP="+empresa+
				" AND c.NUMCAR="+numCarg+ppatente+
				" AND c.CAMCO1="+bodega+
				" AND c.CAMRUT="+rutChofer+
				" AND c.EXMDI3='"+digChofer+"'"+
				" AND c.cames1 <> 'X'" +
				" AND d.XOCL1<>'' "+ connumero + " " +
				" GROUP BY c.NUMCAR, ca.CAMNFA, ca.CAMCO3, d.VEDNU2, vec.VENCO5, d.XOCL1, d.XOCL2 "+
				" ORDER BY c.NUMCAR ASC"+
		        " FOR READ ONLY" ;
		
		log.info("SELECT PARA ENVIAR OK POR CORREO   : "+sqlObtenerCamtra);
        
		try{
				pstmt = conn.prepareStatement(sqlObtenerCamtra);
				rs = pstmt.executeQuery();
				log.info("O K E Y    SELECT PARA ENVIAR OK POR CORREO  : "+sqlObtenerCamtra);
			while (rs.next()) {
				
				if (rs.getInt("NUMCAR") != numcaraux && (conti>0)){
					conti=0;
					dto.setOrdenes(ordenes);
					cargu.add(dto);
					dto = new CarguiocTranspDTO();
					ordenes = new ArrayList();
				}
				
				if (conti==0){
					ordenes = new ArrayList();
					dto.setNumeroCarguio(rs.getInt("NUMCAR"));
					numcaraux=rs.getInt("NUMCAR");
					conti++;
				}
				
				
				CarguiodTranspDTO ordendto = new CarguiodTranspDTO();
				
				if (rs.getString("CAMNFA")!=null){
					if (paip>0){
						ordendto.setNumeroDocumento(rs.getString("CAMNFA")+"|"+(paip));
					} else {
						ordendto.setNumeroDocumento(rs.getString("CAMNFA"));
					}
				}
				if (rs.getInt("VEDNU2")>0){
					if (paip>0){
						ordendto.setNumeroDocumento(rs.getString("VEDNU2")+"|"+(paip));
					} else {
						ordendto.setNumeroDocumento(rs.getString("VEDNU2"));
					}
				}
				if (rs.getInt("CAMCO3")>0){
				ordendto.setTipoDocumento(rs.getInt("CAMCO3"));
				}
				if (rs.getInt("VENCO5")>0){
				ordendto.setTipoDocumento(rs.getInt("VENCO5"));
				}
				
				ordendto.setCodEstado(rs.getString("XOCL1").trim());
				ordendto.setCodMotivo(rs.getString("XOCL2").trim());
				ordenes.add(ordendto);
			
				ordendto = new CarguiodTranspDTO();
				
				
			}
			
			dto.setOrdenes(ordenes);
			cargu.add(dto);
			
			
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
		return cargu;

	}

public List buscaCarguiodRedespacho(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int tipodespacho){
	
	int numcaraux=0;
	int conti=0;
	
	List cargu = new ArrayList();
	RedespachocDTO dto = new RedespachocDTO();
	
	List ordenes = new ArrayList();
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	String sqlObtenerDetord="SELECT c.NUMCAR, c.CAMRUT, c.EXMDI3, ca.CAMNFA, ca.CAMCO3 "+
	" FROM CASEDAT.CARGUIOD d "+
    " INNER JOIN CASEDAT.CARGUIOC c ON d.cacemp=c.cacemp AND d.numcar=c.numcar AND d.vecpat=c.vecpat AND d.camco1=c.camco1 " +
	" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
	" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
	" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
	" WHERE d.CACEMP="+empresa+
	" AND d.NUMCAR="+numCarguio+
	" AND c.CAMRUT="+rutChofer+
	" AND c.EXMDI3='"+digChofer+"'"+
	" And d.CAMCO1="+codBodega+
	" AND d.XONM1="+tipodespacho+
    " GROUP BY c.NUMCAR, c.NUMCAR, c.CAMRUT, c.EXMDI3, ca.CAMNFA, ca.CAMCO3 "+
	" ORDER BY c.NUMCAR ASC"+
    " FOR READ ONLY" ;
	
	//" AND d.XOCL1 <> ''"+
	
	
    try{
		pstmt = conn.prepareStatement(sqlObtenerDetord);
		
		rs = pstmt.executeQuery();
		while (rs.next()) {
				
			if (rs.getInt("NUMCAR") != numcaraux && (conti>0)){
				conti=0;
				dto.setOrdenes(ordenes);
				cargu.add(dto);
				dto = new RedespachocDTO();
				ordenes = new ArrayList();
			}
			
			if (conti==0){
				ordenes = new ArrayList();
				dto.setNumeroCarguio(rs.getInt("NUMCAR"));
				dto.setRutChofer(rs.getInt("CAMRUT"));
				dto.setDvChofer(rs.getString("EXMDI3"));
				numcaraux=rs.getInt("NUMCAR");
				conti++;
			}
			
			RedespachodDTO ordendto = new RedespachodDTO();
			ordendto.setNumeroDocumento(rs.getInt("CAMNFA"));
			ordendto.setTipoDocumento(rs.getInt("CAMCO3"));
			ordenes.add(ordendto);
			ordendto = new RedespachodDTO();
			}
			
			if (numcaraux>0) {
				dto.setOrdenes(ordenes);
				cargu.add(dto);
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

		return cargu;	
}


	public int obtieneFechaVencimientoArt(int codEmpresa, int codBodega, int numCarguio, int codArticulo){
	int fechavenciart=0;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	
	String sqlObtenerCarguiod =" SELECT ADSFEC "+
	" FROM CASEDAT.CARGUIOD "+
	" WHERE CACEMP="+codEmpresa+" AND NUMCAR="+numCarguio+
	" AND CAMCO1="+codBodega+" AND CLDCOD="+codArticulo+
	" AND adsfec>0 "+
	" ORDER BY adsfec ASC "+
	" FOR READ ONLY" ;
	try{
		pstmt = conn.prepareStatement(sqlObtenerCarguiod);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			fechavenciart=rs.getInt("ADSFEC");
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
	
	
	return fechavenciart;
	
}
	
	public List buscaCarguiodRedespacho(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int tipodespacho, int numerodocto){
		
		String connumero="";
		if (numerodocto>0){
			connumero =" AND ca.CAMNFA="+numerodocto+" "; 
		}
        
		int numcaraux=0;
		int conti=0;
		
		List cargu = new ArrayList();
		RedespachocDTO dto = new RedespachocDTO();
		
		List ordenes = new ArrayList();
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerDetord="SELECT c.NUMCAR, c.CAMRUT, c.EXMDI3, ca.CAMNFA, ca.CAMCO3 "+
		" FROM CASEDAT.CARGUIOD d "+
        " INNER JOIN CASEDAT.CARGUIOC c ON d.cacemp=c.cacemp AND d.numcar=c.numcar AND d.vecpat=c.vecpat AND d.camco1=c.camco1 " +
		" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
		" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
		" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
		" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
		" WHERE d.CACEMP="+empresa+
		" AND d.NUMCAR="+numCarguio+
		" AND c.CAMRUT="+rutChofer+
		" AND c.EXMDI3='"+digChofer+"'"+
		" And d.CAMCO1="+codBodega+
		" AND d.XONM1="+tipodespacho+" "+connumero + " "+
		" GROUP BY c.NUMCAR, c.NUMCAR, c.CAMRUT, c.EXMDI3, ca.CAMNFA, ca.CAMCO3 "+
		" ORDER BY c.NUMCAR ASC"+
        " FOR READ ONLY" ;
		
		//" AND d.XOCL1 <> ''"+
		
		
        try{
			pstmt = conn.prepareStatement(sqlObtenerDetord);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					
				if (rs.getInt("NUMCAR") != numcaraux && (conti>0)){
					conti=0;
					dto.setOrdenes(ordenes);
					cargu.add(dto);
					dto = new RedespachocDTO();
					ordenes = new ArrayList();
				}
				
				if (conti==0){
					ordenes = new ArrayList();
					dto.setNumeroCarguio(rs.getInt("NUMCAR"));
					dto.setRutChofer(rs.getInt("CAMRUT"));
					dto.setDvChofer(rs.getString("EXMDI3"));
					numcaraux=rs.getInt("NUMCAR");
					conti++;
				}
				
				RedespachodDTO ordendto = new RedespachodDTO();
				ordendto.setNumeroDocumento(rs.getInt("CAMNFA"));
				ordendto.setTipoDocumento(rs.getInt("CAMCO3"));
				ordenes.add(ordendto);
				ordendto = new RedespachodDTO();
				}
				
				if (numcaraux>0) {
					dto.setOrdenes(ordenes);
					cargu.add(dto);
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

			return cargu;	
    }
	
	public CarguiodDTO buscaDetalleCarguiodArt(int empresa, int carguio, String patente, int bodega, int nroOV, int codArticulo, int corrDirecDespa){
		CarguiodDTO carguiodDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCarguiod=" Select * "+
			" FROM CASEDAT.CARGUIOD " + 
		        " WHERE CACEMP = "+empresa +
		        " AND NUMCAR = "+carguio +
		        " AND VECPAT = '"+patente +"'"+
		        " AND CAMCO1 = "+bodega+
		        " AND ORDEMP = CACEMP "+
		        " AND NUMVEN = "+nroOV+
		        " AND TPTC18 = CAMCO1 "+
		        " AND CLDCOD = "+codArticulo+
		        " AND CLICOR = "+corrDirecDespa+
		        " FOR READ ONLY" ;
				
		try{
			pstmt = conn.prepareStatement(sqlObtenerCarguiod);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				carguiodDTO = new CarguiodDTO();
				carguiodDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				carguiodDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				carguiodDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				carguiodDTO.setPatente(rs.getString("VECPAT"));
				carguiodDTO.setCantidad(rs.getInt("CLDCAN"));
				carguiodDTO.setFechaDespacho(rs.getInt("VECFE5"));
				carguiodDTO.setFormatoArt(rs.getString("VENFOR"));
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
		return carguiodDTO;
	}
	
	public String obtieneEstadoCarguioDet(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, String patente, int numOVe){
		String estadod="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String digitoruti="";
		if (digChofer==null || ("".equals(digChofer.trim()))){
			
		} else {
			digitoruti=" AND c.exmdi3='"+digChofer+"'";
		}
		
		String sqlObtenerCldmco ="SELECT d.CAMES1 "+
		" FROM CASEDAT.CARGUIOC c" +
		" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
		" WHERE c.CACEMP="+empresa+
		" AND c.NUMCAR="+numCarguio+
		" AND c.VECPAT='"+patente+"'"+
		" AND c.CAMCO1="+codBodega+
		" AND c.CAMRUT="+rutChofer+digitoruti+
		" AND d.NUMVEN="+numOVe+
		" GROUP BY d.CAMES1 "+
		" FOR READ ONLY" ;
		//logi.info("SELECT DET CAMES1 CARGUIOD : "+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			//logi.info("O K E Y   SELECT DET CAMES1 CARGUIOD");
			while (rs.next()) {
				estadod=rs.getString("CAMES1").trim();
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
		
		
		return estadod;
	}

public String obtieneEstadoCarguioD(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente, int rutClie, int numeroOVe){
		
		String estado="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String digitoruti="";
		
		if (digChofer==null || ("".equals(digChofer.trim()))){
			
		} else {
			digitoruti=" AND c.exmdi3='"+digChofer+"'";
		}
		
		String sqlObtenerCldmco ="SELECT d.XOCL1 "+
		" FROM CASEDAT.CARGUIOC c" +
		" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
		" LEFT JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut "+
		" AND de.clmdig=ca.camdvr AND de.camnum=ca.camnum AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
		" WHERE c.CACEMP="+empresa+
		" AND c.NUMCAR="+numCarguio+
		" AND c.VECPAT='"+patente+"'"+
		" AND c.CAMCO1="+codBodega+
		" AND c.CAMRUT="+rutChofer+digitoruti+
		" AND d.NUMVEN="+numeroOVe+
		" AND d.CLMRUT="+rutClie+
		" AND d.TPTC18="+codBodega+
		" AND (ca.CAMNFA="+numDocto+ " OR d.VEDNU2="+numDocto+ ") "+
		" GROUP BY d.XOCL1 "+
		" FOR READ ONLY" ;
		//logi.info("SELECT XOCL1 CARGUIOD : "+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			//logi.info("O K E Y   SELECT XOCL1 CARGUIOD");
			while (rs.next()) {
				estado="";//  rs.getString("XOCL1").trim();
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
		return estado;
	}

public int actualizaEstadoCarguiodTransp(String codEstado, String codMotivo, int codEmpresa, int numcarguio, int codBodega, int numOV, int numdocu){
	int actualiza=0;
	PreparedStatement pstmt =null;
	
	String numguia="";
	if (numdocu>0){
    	numguia = " AND vednu2="+numdocu+" ";
    }
	
	String sqlObtenerCldmco ="UPDATE "+
    " CASEDAT.CARGUIOD " + 
    " SET XOCL1='"+codEstado+"', XOCL2='"+codMotivo+"' "+
    " WHERE cacemp="+codEmpresa+" AND numcar="+numcarguio+
    " AND camco1="+codBodega+
    " " +numguia+ " "+
    " AND numven="+numOV ;
    //logi.info("ANTES DE UPDATE CARGUIOD XOCL1 : "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		pstmt.executeUpdate();
		actualiza=1;
		//logi.info("O K E Y   UPDATE CARGUIOD XOCL1  : "+sqlObtenerCldmco);
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

public CarguiodTranspDTO obtieneMotivoCarguioD(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente){
	
	CarguiodTranspDTO carguiodDTO = null;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	String digitoruti="";
	if (digChofer==null || ("".equals(digChofer.trim()))){
		
	} else {
		digitoruti=" AND c.exmdi3='"+digChofer+"' ";
	}
	
	String sqlObtenerCldmco ="SELECT d.XOCL2, est.EYCDSEST "+
	" FROM CASEDAT.CARGUIOC c" +
	" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" left JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum "+
	" AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
	" LEFT JOIN CASEDAT.ORVTESTA est ON d.XOCL2=est.EYCCDEST AND est.aatpa=10 AND est.eyctpest='N'"+
	" WHERE c.CACEMP="+empresa+
	" AND c.NUMCAR="+numCarguio+
	" AND c.VECPAT='"+patente+"'"+
	" AND c.CAMCO1="+codBodega+
	" AND c.CAMRUT="+rutChofer+digitoruti+
	" AND (ca.CAMNFA="+numDocto+ " OR d.VEDNU2="+numDocto+")"+
	" GROUP BY d.XOCL2, est.EYCDSEST "+
	" FOR READ ONLY" ;        
	//logi.info("SELECT XOCL2, EYCDSEST CARGUIOD : "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		rs = pstmt.executeQuery();
		//logi.info("O K E Y   SELECT XOCL2, EYCDSEST CARGUIOD");
		while (rs.next()) {
			carguiodDTO = new CarguiodTranspDTO();
			carguiodDTO.setCodMotivo(rs.getString("XOCL2"));
			carguiodDTO.setDesMotivo(rs.getString("EYCDSEST"));
			
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
	return carguiodDTO;

}


}
