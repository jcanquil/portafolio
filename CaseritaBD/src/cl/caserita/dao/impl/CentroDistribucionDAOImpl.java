package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CentroDistribucionDAO;
import cl.caserita.dto.CasesmtpDTO;
import cl.caserita.dto.CentroDespachoDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.TipodespachoDTO;

public class CentroDistribucionDAOImpl implements CentroDistribucionDAO {

	private  static Logger log = Logger.getLogger(CentroDistribucionDAOImpl.class);

	private Connection conn;
	
	public CentroDistribucionDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List buscaRegionCiudadComuna(int rut, int codRegion, int codCiudad,int tipoDespacho, int codComuna){
		List lista = new ArrayList();
		CentroDespachoDTO centro = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CENTDIST " + 
        " Where CLCRU1="+rut+" AND CLICO1="+codRegion+" AND CLICO2="+codCiudad+" AND CLICO3="+codComuna+" AND  CODTDE="+tipoDespacho+" FOR READ ONLY" ;
		List clcdia = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
					centro = new CentroDespachoDTO();
					centro.setRutCliente(rs.getInt("CLCRU1"));
					centro.setCodRegion(rs.getInt("CLICO1"));
					centro.setCodCiudad(rs.getInt("CLICO2"));
					centro.setCodComuna(rs.getInt("CLICO3"));
					centro.setCorrelativo(rs.getInt("CLICOR"));
					centro.setDescripcionOficina(rs.getString("DEOFSA"));
					ClidirDTO clidirDTO = recuperaDatosCliente(rs.getInt("CLCRU1"), rs.getString("CLCDVR"), rs.getInt("CLICOR"));
					centro.setTelefono(clidirDTO.getTelefono().trim());
					centro.setCelular(clidirDTO.getCelular().trim());
					centro.setDireccion(clidirDTO.getDireccionCliente().trim());
					centro.setNumero(clidirDTO.getNumeroDireccion());
					centro.setDescripcionRegion(clidirDTO.getDescripcionRegion().trim());
					centro.setDescripcionCiudad(clidirDTO.getDescripcionCiudad().trim());
					centro.setDescripcionComuna(clidirDTO.getDescripcionComuna().trim());
					centro.setCodigoTipoDespacho(tipoDespacho);
					centro.setLatitud(clidirDTO.getLatitud().trim());
					centro.setLongitud(clidirDTO.getLongitud().trim());
					lista.add(centro);
				
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
	
	public List buscaTiposDespacho(int rut, int codRegion, int codCiudad, int codComuna){
		List lista = new ArrayList();
		TipodespachoDTO tipo = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select CLCRU1,CODTDE, DETPDE "+
        " from CASEDAT.CENTDIST " + 
        " Where CLCRU1="+rut+" AND CLICO1="+codRegion+" AND CLICO2="+codCiudad+" AND CLICO3="+codComuna+" GROUP BY CLCRU1,CODTDE, DETPDE FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipo = new TipodespachoDTO();
				tipo.setCodigoTipoDespacho(rs.getInt("CODTDE"));
				tipo.setDescripcionTipoDespacho(rs.getString("DETPDE"));
				lista.add(tipo);	
				
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
	
	public ClidirDTO recuperaDatosCliente(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		String sqlObtenerCldmco ="Select C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1, C1.CLITEL, C1.CLITE1, C2.LATDIR, C2.LONDIR "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR="+correlativo+" AND C1.CLICOR= C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C2.ESTDIR ='A'  fetch first 1 rows only FOR READ ONLY" ;
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
				clidir.setDescripcionCiudad(recuperaCiudad(clidir.getRegion(), clidir.getCiudad()));
				clidir.setDescripcionComuna(recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna()));
				clidir.setDescripcionRegion(recuperaRegion(clidir.getRegion()));
				clidir.setDireccionCliente(rs.getString("CLIDI1").substring(0, 30));
				log.info("Numero:"+rs.getString("CLIDI1").substring(30, 35).trim());
				clidir.setNumeroDireccion(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
				clidir.setTelefono(rs.getString("CLITEL").trim());
				clidir.setCelular(rs.getString("CLITE1").trim());
				clidir.setLatitud(rs.getString("LATDIR").trim());
				clidir.setLongitud(rs.getString("LONDIR").trim());
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
	
	public String recuperaRegion(int region){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTREG " + 
        " Where TPTCO1="+region+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTDE1");
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
	
	public String recuperaCiudad(int region, int ciudad){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCTY " + 
        " Where TPTCO6="+region+" AND TPTCO7="+ciudad+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTDE7");
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
	
}
