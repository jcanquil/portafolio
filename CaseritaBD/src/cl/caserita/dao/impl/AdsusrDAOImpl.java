package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.AdsusrDAO;
import cl.caserita.dto.ActecoDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.UsuarioMonitoreoDTO;

public class AdsusrDAOImpl implements AdsusrDAO {

	private  static Logger log = Logger.getLogger(AdsusrDAOImpl.class);
	private Connection conn;

	public AdsusrDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int validaUsuarioSyscon(String usuario, String password){
		int usuarioRetorno=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.ADSUSR " + 
        " WHERE ADSCO4='"+usuario+"' AND ADSPAS='"+password+"' FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			System.out.println("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usuarioRetorno=rs.getInt("ADSC03");
				
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
		
		return usuarioRetorno;
		
	}
	
	public String obtieneLetra(String palabra){
		ActecoDTO acteco = null;
		String letra="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.LETRAS " + 
        " WHERE F2='"+palabra+"' FOR READ ONLY" ;
		List actecoList = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//System.out.println("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				letra=rs.getString("F1");
				
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
		
		return letra;
		
	}
	
	public String obtieneLetraEncriptada(String palabra){
		ActecoDTO acteco = null;
		String letra="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.LETRAS " + 
        " WHERE F1='"+palabra+"' FOR READ ONLY" ;
		List actecoList = new ArrayList();
		log.info("SQL LETRAS :"+sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//System.out.println("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				letra=rs.getString("F2");
				
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
		
		return letra;
		
	}
	
	public List letras(){
		ActecoDTO acteco = null;
		String letra="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List letrasABC=new ArrayList();
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.LETRAS " ;
        
		List actecoList = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//System.out.println("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String let = rs.getString("F1").trim();
				letrasABC.add(let);
				
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
		
		return letrasABC;
		
	}
	
	public int actualizaLetra(String letra , String palabra){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.LETRAS " + 
        " SET F2='"+palabra.trim()+"' Where F1='"+letra.trim()+"'  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL CLC" + sqlObtenerCldmco);
			
			
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
	
	public int validaUsuarioMapas(String usuario, String password){
		int usuarioRetorno=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.ADSUSR " + 
        " WHERE ADSCO4='"+usuario+"' AND ADSPAS='"+password.trim()+"' FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			System.out.println("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if ("SUUS".equals(rs.getString("ADSTI1")) || "ADMI".equals(rs.getString("ADSTI1")) ){
					//usuarioRetorno=rs.getInt("ADSC03");
					usuarioRetorno=1;
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
		
		return usuarioRetorno;
		
	}
	public UsuarioMonitoreoDTO obtieneSupervisor(int codigoSup){
		UsuarioMonitoreoDTO usuarioDTO = new UsuarioMonitoreoDTO();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.SUPEVEND S1" + 
        " WHERE S1.CODSUP="+codigoSup+" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			System.out.println("SQL" + sqlObtenerCamtra);
			usuarioDTO.setEstado("-1");
			usuarioDTO.setDescripcion("USUARIO o PASSWORD INCORRECTA");
			usuarioDTO.setTipoUsuario("");
			rs = pstmt.executeQuery();
			while (rs.next()) {
					
						usuarioDTO.setCodigoSupervisor(String.valueOf(rs.getInt("CODSUP")));
						usuarioDTO.setNombreSupervisor(rs.getString("NOMSUP").trim());
					
				
				
				
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
		return usuarioDTO;
	}
	
	public UsuarioMonitoreoDTO Obtienevendedor(int vendedor){
		UsuarioMonitoreoDTO usuarioDTO = new UsuarioMonitoreoDTO();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.EXMVND S1" + 
        " WHERE S1.EXMC09="+vendedor+" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			System.out.println("SQL" + sqlObtenerCamtra);
			usuarioDTO.setEstado("-1");
			usuarioDTO.setDescripcion("USUARIO o PASSWORD INCORRECTA");
			usuarioDTO.setTipoUsuario("");
			rs = pstmt.executeQuery();
			while (rs.next()) {
					
						usuarioDTO.setCodigoSupervisor(String.valueOf(rs.getInt("EXMC09")));
						usuarioDTO.setNombreSupervisor(rs.getString("EXMNO1").trim());
					
				
				
				
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
		return usuarioDTO;
	}
	public UsuarioMonitoreoDTO validaUsuarioMonitoreo(String usuario, String password){
		int usuarioRetorno=0;
		UsuarioMonitoreoDTO usuarioDTO = new UsuarioMonitoreoDTO();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.ADSUSR A1" + 
       // " WHERE A1.ADSCO4='"+usuario+"' AND A1.ADSPAS='"+password.trim()+"' AND S1.TPTC18=26 AND A1.ADSC03=S1.CODSUP FOR READ ONLY" ;
       " WHERE A1.ADSCO4='"+usuario+"' AND A1.ADSPAS='"+password.trim()+"'  FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			System.out.println("SQL" + sqlObtenerCamtra);
			usuarioDTO.setEstado("-1");
			usuarioDTO.setDescripcion("USUARIO o PASSWORD INCORRECTA");
			usuarioDTO.setTipoUsuario("");
			rs = pstmt.executeQuery();
			while (rs.next()) {
					//if ("SUUS".equals(rs.getString("ADSTI1")) || "ADMI".equals(rs.getString("ADSTI1")) || "SUPE".equals(rs.getString("ADSTI1"))){
				if ("ADMI".equals(rs.getString("ADSTI1")) ){
						//usuarioRetorno=rs.getInt("ADSC03");
						usuarioDTO.setEstado("0");
						usuarioDTO.setDescripcion("USUARIO EXITOSO");
						usuarioDTO.setTipoUsuario(rs.getString("ADSTI1").trim());
						usuarioDTO.setCodigoSupervisor(rs.getString("ADSCO4"));
						usuarioDTO.setNombreSupervisor(rs.getString("ADSNO3").trim());
				}else if ( "SUPE".equals(rs.getString("ADSTI1"))){
					usuarioDTO.setEstado("0");
					usuarioDTO.setDescripcion("USUARIO EXITOSO");
					usuarioDTO.setTipoUsuario(rs.getString("ADSTI1").trim());
					UsuarioMonitoreoDTO dto = obtieneSupervisor(rs.getInt("ADSC03"));
					usuarioDTO.setCodigoSupervisor(String.valueOf(dto.getCodigoSupervisor()));
					usuarioDTO.setNombreSupervisor(dto.getNombreSupervisor().trim());
				}else {
						
					usuarioDTO.setEstado("0");
					usuarioDTO.setDescripcion("USUARIO EXITOSO");
					usuarioDTO.setTipoUsuario(rs.getString("ADSTI1").trim());
					UsuarioMonitoreoDTO dto = Obtienevendedor(rs.getInt("ADSC03"));
					usuarioDTO.setCodigoSupervisor(String.valueOf(dto.getCodigoSupervisor()));
					usuarioDTO.setNombreSupervisor(dto.getNombreSupervisor().trim());
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
		
		return usuarioDTO;
		
	}
	
}
