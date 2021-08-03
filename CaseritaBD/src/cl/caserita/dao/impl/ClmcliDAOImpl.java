package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.VecmarDTO;

public class ClmcliDAOImpl implements ClmcliDAO{

	private static Logger log = Logger.getLogger(ClmcliDAOImpl.class);
	private Connection conn;
	
	public ClmcliDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaCliente(ClmcliDTO clmcli){
		int res=0;
		PreparedStatement pstmt =null;
		     
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CLMCLI " + 
        " (CLMRUT, CLMDIG, CLMNOM, CLMCOD, CLMCO4,CLMCO3, CLMCO2, CLMCO1, CLMAFE, CLMCO7,CLMCO5, CLMCO6, CLMFE4, CLMHOR) VALUES("+clmcli.getRutCliente()+",'"+clmcli.getDvCliente()+"','"+clmcli.getRazonsocial()+"',"+clmcli.getCodigoBodega()+","+clmcli.getCodigoTipoCliente()+","+clmcli.getCodigoClaseCliente()+","+clmcli.getCodigocalidadCliente()+","+clmcli.getCodigoBloqueo()+",'"+clmcli.getAfectoPromo().trim()+"',"+clmcli.getBodegaUltimaCompra()+","+clmcli.getCodigoVendedor()+","+clmcli.getCodigoTipoVendedor()+","+clmcli.getFechaAccesoUsuario()+","+clmcli.getHoraAccesoUsuario()+") " ;
		log.info("INSERTA CLIENTE" + sqlObtenerVecmar);
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
	
	public ClmcliDTO recuperaCliente(String rut, String dv){
		ClmcliDTO clmcli = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLMCLI " + 
        " Where CLMRUT="+rut+" AND CLMDIG='"+dv+"' FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clmcli = new ClmcliDTO();
				clmcli.setRazonsocial(rs.getString("CLMNOM"));				
				clmcli.setTipoNegocio(obtieneTipoNegocio(rut, dv));
				//log.info("Recupera Comuna");
				ClidirDTO clidir = recuperaDatosCliente(Integer.parseInt(rut), dv);
				//log.info("Recupera Comuna2");
				if (clidir!=null){
					clmcli.setCodRegion(clidir.getRegion());
					clmcli.setCodCiudad(clidir.getCiudad());
					clmcli.setCodComuna(clidir.getComuna());
					clmcli.setDireccionCliente(clidir.getDireccionCliente());
					clmcli.setDescComuna(recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna()));
					clmcli.setDescCiudad(recuperaCiudad(clidir.getRegion(),clidir.getCiudad()));
					clmcli.setEmailCliente(clidir.getMail());
				}
				
				clmcli.setRutCliente(rs.getInt("CLMRUT"));
				clmcli.setDvCliente(rs.getString("CLMDIG"));
				
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
		
		
		return clmcli;
	}
	
	public List recuperaAllCliente(){
		ClmcliDTO clmcli = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLMCLI fetch first 100 rows only" ; 
        List listaCliente = new ArrayList();
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);		
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clmcli = new ClmcliDTO();
				clmcli.setRutCliente(rs.getInt("CLMRUT"));
				clmcli.setDvCliente(rs.getString("CLMDIG"));
				clmcli.setRazonsocial(rs.getString("CLMNOM"));				
				clmcli.setDirecciones((recuperaDirecciones(clmcli.getRutCliente(), clmcli.getDvCliente())));

				listaCliente.add(clmcli);
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
		
		
		return listaCliente;
	}
	public ClmcliDTO recuperaCliente(int rut, String dv){
		ClmcliDTO clmcli = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLMCLI  " +
        "WHERE CLMRUT="+rut+" AND CLMDIG='"+dv+"' ";
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);		
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clmcli = new ClmcliDTO();
				clmcli.setRutCliente(rs.getInt("CLMRUT"));
				clmcli.setDvCliente(rs.getString("CLMDIG"));
				clmcli.setRazonsocial(rs.getString("CLMNOM"));				
				clmcli.setDirecciones(recuperaDirecciones(clmcli.getRutCliente(), clmcli.getDvCliente()));
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
		return clmcli;
	}
	
	public ClmcliDTO recuperaClienteDireccion(int rut, String dv, int correlativo){
		ClmcliDTO clmcli = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLMCLI  " +
        "WHERE CLMRUT="+rut+" AND CLMDIG='"+dv+"' ";
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);		
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clmcli = new ClmcliDTO();
				clmcli.setRutCliente(rs.getInt("CLMRUT"));
				clmcli.setDvCliente(rs.getString("CLMDIG"));
				clmcli.setRazonsocial(rs.getString("CLMNOM"));				
				clmcli.setDirecciones(recuperaDireccionesCorrrelativo(clmcli.getRutCliente(), clmcli.getDvCliente(), correlativo));
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
		return clmcli;
	}
	
	
	public ClmcliDTO recuperaCliente2(String rut){
		ClmcliDTO clmcli = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLMCLI " + 
        " Where CLMRUT="+rut+"  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clmcli = new ClmcliDTO();
				clmcli.setRazonsocial(rs.getString("CLMNOM"));				
				
				clmcli.setRutCliente(rs.getInt("CLMRUT"));
				clmcli.setDvCliente(rs.getString("CLMDIG"));
				
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
		
		
		return clmcli;
	}
	
	
	public String obtieneTipoNegocio(String rut, String dv){
		String tipoNegocio="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select T.CCOFA "+
        " from CASEDAT.CLMCLI C , CASEDAT.CLIDATEM P , CASEDAT.tipnegoc t " + 
        " Where C.CLMRUT="+rut+" AND C.CLMDIG='"+dv+"' AND C.CLMRUT= P.CLMRUT and C.CLMDIG= P.CLMDIG and P.CCOEA= T.CCOEA FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipoNegocio = rs.getString("CCOFA");
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
		
		
		return tipoNegocio;
	}
	public ClidirDTO recuperaDatosCliente(int rut, String dv){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		String sqlObtenerCldmco ="Select C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1, C1.CLIEMA "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
       // " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C2.ESTDIR ='A'  fetch first 1 rows only FOR READ ONLY" ;
       " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG   fetch first 1 rows only FOR READ ONLY" ;
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
				clidir.setMail(rs.getString("CLIEMA"));
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
	
	public List recuperaDirecciones(int rut, String dv){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		List lista = new ArrayList();
		String sqlObtenerCldmco ="Select C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1 "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C2.ESTDIR ='A'  FOR READ ONLY" ;
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
				clidir.setDescripcionCiudad(recuperaCiudad(clidir.getRegion(), clidir.getCiudad()));
				clidir.setDescripcionComuna(recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna()));
				clidir.setDireccionCliente(rs.getString("CLIDI1"));
				lista.add(clidir);
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
	
	public List recuperaDireccionesCorrrelativo(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		List lista = new ArrayList();
		String sqlObtenerCldmco ="Select C1.CLICOR, C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1 "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= "+correlativo+" AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C1.CLICOR=C2.CLICOR AND C2.ESTDIR ='A'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClidirDTO();
				clidir.setCorrelativo(rs.getInt("CLICOR"));
				clidir.setRegion(rs.getInt("CLICO1"));
				clidir.setCiudad(rs.getInt("CLICO2"));
				clidir.setComuna(rs.getInt("CLICO3"));
				clidir.setDescripcionCiudad(recuperaCiudad(clidir.getRegion(), clidir.getCiudad()));
				clidir.setDescripcionComuna(recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna()));
				clidir.setDireccionCliente(rs.getString("CLIDI1"));
				lista.add(clidir);
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
	
	public int generaIntegracionGeoref(ClidirDTO clidir){
		int res=0;
		PreparedStatement pstmt =null;
		     
		String sqlObtenerClienmap="INSERT INTO"+
        "  CASEDAT.CLIENMAP " + 
        " (CLIRUT, CLIDIG, CLICOR) VALUES("+clidir.getRutCliente()+",'"+clidir.getDvCliente()+"',"+clidir.getCorrelativo()+") " ;
		log.info("INSERTA CLIENTE" + sqlObtenerClienmap);
		try{
			pstmt = conn.prepareStatement(sqlObtenerClienmap);
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
	
	public int generaDatosCliente(ClmcliDTO clmcli){
		int res=0;
		PreparedStatement pstmt =null;
		     
		String sqlObtenerClienmap="INSERT INTO"+
        "  CASEDAT.CLIDATEM " + 
        " (CLMRUT, CLMDIG, CCOEA, CODSGC) VALUES("+clmcli.getRutCliente()+",'"+clmcli.getDvCliente()+"',"+Integer.parseInt(clmcli.getTipoNegocio().trim())+",4) " ;
		log.info("INSERTA CLIDATEM" + sqlObtenerClienmap);
		try{
			pstmt = conn.prepareStatement(sqlObtenerClienmap);
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
	
	public String recuperaRegion(int region){
		String dregion="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTREG " + 
        " Where TPTCO1="+region+"   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dregion = rs.getString("TPTDE1");
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
		
		
		return dregion;
		
		
	}
	
}
