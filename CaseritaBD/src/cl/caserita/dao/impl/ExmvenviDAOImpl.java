package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmvenviDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClientezonaDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.ZonaVendedorDTO;

public class ExmvenviDAOImpl implements ExmvenviDAO{

	private static Logger log = Logger.getLogger(ExmvenviDAOImpl.class);
	private Connection conn;
	
	public ExmvenviDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List recuperaEncabezado (int codVendedor, String dia){
		List lista = new ArrayList();
		List clie = new ArrayList();
		ZonaVendedorDTO zona=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.EXMVENVI E1, CASEDAT.ZONAVISI E2" + 
        " Where E1.EXMC09="+codVendedor+" and E1.CODZONA=E2.CODZONA AND VCLIDI='"+dia.trim()+"' ORDER BY E1.CODZONA FOR READ ONLY" ;
		log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			int cod=0;
			int con=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
					if (cod!=rs.getInt("CODZONA") && cod!=0){
						zona.setCliente(clie);

						lista.add(zona);
						clie = new ArrayList();
						cod = rs.getInt("CODZONA");
						zona = new ZonaVendedorDTO();
						zona.setCodigoZona(rs.getInt("CODZONA"));
						zona.setDescripcionZona(rs.getString("DESZONA").trim());
						zona.setTipoVendedor(rs.getInt("CLMCO6"));
						zona.setDia(rs.getString("VCLIDI").trim());
						clie.add(recuperaDireccionesCorrrelativo(rs.getInt("CLMRUT"), rs.getString("CLMDIG"), rs.getInt("CLICOR")));
						con=1;

					}else{
						cod = rs.getInt("CODZONA");
						if (con==0){
							zona = new ZonaVendedorDTO();

						}
						con++;
						zona.setCodigoZona(rs.getInt("CODZONA"));
						zona.setDescripcionZona(rs.getString("DESZONA").trim());
						zona.setTipoVendedor(rs.getInt("CLMCO6"));
						zona.setDia(rs.getString("VCLIDI"));
						clie.add(recuperaDireccionesCorrrelativo(rs.getInt("CLMRUT"), rs.getString("CLMDIG"), rs.getInt("CLICOR")));
					}
					
					
				
			}
			zona.setCliente(clie);

			lista.add(zona);

			
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
		return lista;
	}
	
	public ClientezonaDTO recuperaDireccionesCorrrelativo(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClientezonaDTO clidir = null;
		List lista = new ArrayList();
		String sqlObtenerCldmco ="Select *  "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2 , CASEDAT.CLMCLI C3" + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= "+correlativo+" AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C1.CLICOR=C2.CLICOR AND  C1.CLIRUT= C3.CLMRUT AND C1.CLIDIG= C3.CLMDIG FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClientezonaDTO();
				clidir.setRutCliente(rs.getInt("CLMRUT"));
				clidir.setDvCliente(rs.getString("CLMDIG"));
				clidir.setRazonSocial(rs.getString("CLMNOM").trim());
				clidir.setCorrelativoDireccion(rs.getInt("CLICOR"));
				clidir.setDireccion(rs.getString("CLIDI1").substring(0, 30).trim());
				String dire = rs.getString("CLIDI1");
				/*System.out.println("Direccion :"+dire);
				System.out.println("Rut :"+rut);*/

				if (rs.getString("CLIDI1").substring(30, 35).trim().length()>0){
					if (isNumeric(rs.getString("CLIDI1").substring(30, 35).trim())){
						
						clidir.setNumeroDireccion(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));

					}

				}
				
				
				clidir.setCodigoRegion(rs.getInt("CLICO1"));
				clidir.setDescripcionRegion(recuperaRegion(clidir.getCodigoRegion()).trim());
				clidir.setCodigoCiudad(rs.getInt("CLICO2"));
				clidir.setDescripcionCiudad(recuperaCiudad(clidir.getCodigoRegion(), clidir.getCodigoCiudad()).trim());
				clidir.setCodigoComuna(rs.getInt("CLICO3"));
				clidir.setDescripcionComuna(recuperaComuna(clidir.getCodigoRegion(), clidir.getCodigoCiudad(), clidir.getCodigoComuna()).trim());
				if (rs.getString("CLIDI1").substring(35, 39).trim().length()>0){
					if (isNumeric(rs.getString("CLIDI1").substring(35, 40).trim())){
						clidir.setDepto(Integer.parseInt(rs.getString("CLIDI1").substring(35, 40).trim()));

					}
				}
				
				clidir.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX"));
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
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	} 
	
	public String recuperaRegion(int region){
		String desregion="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTREG " + 
        " Where TPTCO1="+region+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				desregion = rs.getString("TPTDE1");
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
		
		
		return desregion;
		
		
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
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
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
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
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
