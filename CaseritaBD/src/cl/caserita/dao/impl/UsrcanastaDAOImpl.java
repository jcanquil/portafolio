package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dto.AdicionalesDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.dto.UsuarioCanastaDTO;

public class UsrcanastaDAOImpl implements  UsrcanastaDAO{

	private static Logger log = Logger.getLogger(UsrcanastaDAOImpl.class);

	private Connection conn;
	
	public UsrcanastaDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaHijos(AdicionalesDTO adicionales){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO "+
        "  CASEDAT.REGHHIT " + 
        " (CLCRU1,CLCDVR,RUTPER,DIGPER,CORING,CLIFEC,NOMHIJ,APELLI,NOMCOM) VALUES("+adicionales.getRutEmpresa()+",'"+adicionales.getDvEmpresa().trim()+"',"+adicionales.getRutPersona()+",'"+adicionales.getDvPersona().trim()+"',"+adicionales.getCorrelativo()+","+adicionales.getFechaNacimiento()+",'"+adicionales.getNombresHijos()+"','"+adicionales.getApellidosHijos()+"','"+adicionales.getNombreCompleto()+"')" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL HIJOS" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
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
	public int actualizaHijos(AdicionalesDTO adicionales){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.REGHHIT " + 
        " set CLIFEC="+adicionales.getFechaNacimiento()+", NOMHIJ='"+adicionales.getNombresHijos()+"',APELLI='"+adicionales.getApellidosHijos()+"',NOMCOM='"+adicionales.getNombreCompleto()+"' WHERE CLCRU1="+adicionales.getRutEmpresa()+" AND RUTPER="+adicionales.getRutPersona()+" AND CORING="+adicionales.getCorrelativo()+"" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL HIJOS" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
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
	
	public List obtieneHijos(int rutCliente, int rut){
		String valida="";
		List lista = new ArrayList();
		AdicionalesDTO adicion =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.REGHHIT " + 
        " Where CLCRU1="+rutCliente+" AND RUTPER="+rut+"  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			log.info("Usuario query" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				adicion = new AdicionalesDTO();
				adicion.setRutEmpresa(rutCliente);
				adicion.setNombresHijos(rs.getString("NOMHIJ").trim());
				adicion.setApellidosHijos(rs.getString("APELLI").trim());
				//adicion.setNombreCompleto(rs.getString("NOMCOM").trim());
				adicion.setCorrelativo(rs.getInt("CORING"));
				adicion.setFechaNacimiento(String.valueOf(rs.getInt("CLIFEC")));
				adicion.setRutPersona(rut);
				lista.add(adicion);
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
	
	public int eliminaHijo(int rutCliente, int rut, int correlativo){
		String valida="";
		int elimina=0;
		AdicionalesDTO adicion =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="DELETE FROM "+
        "  CASEDAT.REGHHIT " + 
        " Where CLCRU1="+rutCliente+" AND RUTPER="+rut+"  AND CORING="+correlativo+" " ;
		log.info("Usuario query" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
			pstmt.executeUpdate();
			
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
		
		
		return elimina;
	}
	
	public int obtieneultimoCorrelativo(int rutCliente, int rut){
		String valida="";
		
		int correlativo=1;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select max(coring) as coring "+
        " from CASEDAT.REGHHIT " + 
        " Where CLCRU1="+rutCliente+" AND RUTPER="+rut+"  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			log.info("Usuario query" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("CORING")+1;
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
		
		
		return correlativo;
	}
	
	public int generaUsuario(UsuarioCanastaDTO usuario){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO "+
        "  CASEDAT.USRSANT " + 
        " (CLCRU1,CLCDVR,CAMNOM,RUTPER,DIGPER,NOMPER,PASUSR,TIPUSR,CODTDE,CLICOR,CONTRET,OBSDES) VALUES("+usuario.getRutCliente()+",'"+usuario.getDvCliente().trim()+"','"+usuario.getNombreCliente().trim()+"',"+usuario.getRutPersonal()+",'"+usuario.getDvPersonal().trim()+"','"+usuario.getNombrePersonal()+"','"+usuario.getPasswordPersonal().trim()+"','"+usuario.getTipoUsuario()+"',"+usuario.getTipoDespacho()+","+usuario.getCorrelativoDirecciones()+",'"+usuario.getContactoRetiro()+"','"+usuario.getObservaciones()+"')" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
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
	
	public int actualizaUsuario(UsuarioCanastaDTO usuario){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE "+
        "  CASEDAT.USRSANT " + 
        " SET VECFE5="+Integer.parseInt(usuario.getFechaDespacho())+" , CODTDE="+usuario.getTipoDespacho()+" , CLICOR="+usuario.getCorrelativoDirecciones()+" , CONTRET='"+usuario.getContactoRetiro()+"' , OBSDES='"+usuario.getObservaciones()+"',  CLIDI1='"+usuario.getDireccionDespacho()+"' , CLINUM='"+usuario.getClidir().getVillaPoblacion().substring(0, 5)+"' , CLITEL='"+usuario.getClidir().getTelefono()+"' , CLITE1='"+usuario.getClidir().getCelular()+"' , CLIFAX='"+usuario.getClidir().getVillaPoblacion().substring(5, 15)+"' , CLICON='"+usuario.getClidir().getNombreContacto()+"' , MAICNT='"+usuario.getClidir().getMail()+"' , CLICO1="+usuario.getClidir().getRegion()+" , CLICO2="+usuario.getClidir().getCiudad()+"  , CLICO3="+usuario.getClidir().getComuna() + " , CLDCOD="+usuario.getCodigoArticulo()+", EXMDES='"+usuario.getDescripcionArticulo()+"' WHERE CLCRU1="+usuario.getRutCliente()+" AND RUTPER="+usuario.getRutPersonal()+"  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL UPDATE USRSANT" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
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
	
	public int actualizaAccesoUsuario(UsuarioCanastaDTO usuario){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE "+
        "  CASEDAT.USRSANT " + 
        " SET FCHUIN="+Integer.parseInt(usuario.getFechaUltimoIngreso())+" , HRULI="+Integer.parseInt(usuario.getHoraUltimoIngreso())+" WHERE CLCRU1="+usuario.getRutCliente()+" AND RUTPER="+usuario.getRutPersonal()+"  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL UPDATE USRSANT" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
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
	
	public int actualizaBDD(UsuarioCanastaDTO usuario){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE "+
        "  CASEDAT.USRSANT " + 
        " SET CLIDI1='"+usuario.getDireccionDespacho()+"'  WHERE  RUTPER="+usuario.getRutPersonal()+"  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL UPDATE USRSANT" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
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
	public String validaUsuario(int rutCliente, int rut, String digito, String password){
		String valida="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRSANT " + 
        " Where CLCRU1="+rutCliente+" AND RUTPER="+rut+" AND DIGPER='"+digito+"' AND PASUSR='"+password+"' FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			log.info("Usuario query" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				valida=rs.getString("TIPUSR").trim()+rs.getString("NOMPER").trim();
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
		
		
		return valida;
	}
	
	public List obtieneReporteUsuariosConDespacho(int rutCliente){
		List usuarios = new ArrayList();
		UsuarioCanastaDTO usuario =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRSANT " + 
        " Where CLCRU1="+rutCliente+" AND CODTDE<>0 FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usuario = new UsuarioCanastaDTO();
				usuario.setRutCliente(rs.getInt("CLCRU1"));
				usuario.setDvCliente(rs.getString("CLCDVR").trim());
				usuario.setRutPersonal(rs.getInt("RUTPER"));
				usuario.setDvPersonal(rs.getString("DIGPER").trim());
				usuario.setNombrePersonal(rs.getString("NOMPER").trim());
				usuario.setDescripciontipoDespacho(rs.getString("DETPDE").trim());
				ClidirDTO clidir = obtieneDireccionConDespacho(rs.getInt("CLCRU1"), rs.getInt("CLICOR"));
				usuario.setDireccionDespacho(clidir.getDireccionCliente());
				usuario.setContactoRetiro(clidir.getNombreContacto());
				usuario.setDescripcionRegion(clidir.getDescripcionRegion().trim());
				usuario.setDescripcionCiudad(clidir.getDescripcionCiudad().trim());
				usuario.setDescripcionComuna(clidir.getDescripcionComuna().trim());
				usuario.setTelefono(String.valueOf(clidir.getTelefono()));
				usuario.setCelular(String.valueOf(clidir.getCelular()));
				usuario.setCodigoRegion(clidir.getRegion());
				usuario.setCodigoCiudad(clidir.getCiudad());
				usuario.setCodigoComuna(clidir.getComuna());
				usuario.setClidir(clidir);
				usuarios.add(usuario);
				
				
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
		
		
		return usuarios;
	}
	
	public List obtieneActualizacionCliente(){
		List usuarios = new ArrayList();
		UsuarioCanastaDTO usuario =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.DATSAN1 " + 
        " Where F2Z="+1+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usuario = new UsuarioCanastaDTO();
				
				usuario.setRutPersonal(rs.getInt("F1Z"));
				
			
				usuario.setDireccionDespacho(rs.getString("F4Z"));
				usuario.setNumero(Integer.parseInt(rs.getString("F5Z").trim()));
				usuario.setDepto(rs.getString("F15Z"));
				usuarios.add(usuario);
				
				
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
		
		
		return usuarios;
	}
	
	public List obtieneReporteUsuariosTodos(int rutCliente){
		List usuarios = new ArrayList();
		UsuarioCanastaDTO usuario =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRSANT1 " + 
        " Where CLCRU1="+rutCliente+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usuario = new UsuarioCanastaDTO();
				usuario.setRutCliente(rs.getInt("CLCRU1"));
				usuario.setDvCliente(rs.getString("CLCDVR").trim());
				usuario.setRutPersonal(rs.getInt("RUTPER"));
				usuario.setDvPersonal(rs.getString("DIGPER").trim());
				usuario.setNombrePersonal(rs.getString("NOMPER").trim());
				usuario.setDescripciontipoDespacho(rs.getString("DETPDE").trim());
				usuario.setTipoDespacho(rs.getInt("CODTDE"));
				
				log.info("Direccion:"+rs.getString("CLIDI1"));
				if (rs.getString("CLIDI1")!=null && rs.getString("CLIDI1").trim().length()>0){
					usuario.setDireccionDespacho(rs.getString("CLIDI1").substring(0, 30).trim());
					usuario.setNumero(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
					
					
					usuario.setDepto(rs.getString("CLIDI1").substring(35, 40).trim());
				}
				if (rs.getString("CLINUM")!=null && !rs.getString("CLINUM").equals("")){
					usuario.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX").trim());
				}
				
				
				usuario.setTelefono(rs.getString("CLITEL").trim());
				usuario.setCelular(rs.getString("CLITE1").trim());
				usuario.setRutEmpresa(rs.getString("RUEMR"));
				usuario.setDigitoEmpresa(rs.getString("DVRUER"));
				usuario.setRazonSocialEmpresa(rs.getString("RZEMR"));
				usuario.setApellidoPaterno(rs.getString("APPAT"));
				usuario.setApellidoMaterno(rs.getString("APMAT"));
				usuario.setNombreCompleto(rs.getString("NOMUSR"));
				usuario.setCodigoOficinaSantander(rs.getString("CODOFSA"));
				usuario.setContactoRetiro(rs.getString("CONTRET").trim());
				usuario.setObservaciones(rs.getString("OBSDES").trim());
				usuario.setFechaDespacho(String.valueOf(rs.getInt("VECFE5")));
				usuario.setPuntoCritico(rs.getString("PTOCRI").trim());
				usuario.setIngresado(rs.getString("INGRES").trim());
				usuario.setFechaUltimoIngreso(String.valueOf(rs.getInt("FCHUIN")));
				usuario.setHoraUltimoIngreso(String.valueOf(rs.getInt("HRULI")));
				usuario.setMail(rs.getString("MAICNT").trim());
				usuario.setCodigoRegion(rs.getInt("CLICO1"));
				usuario.setCodigoCiudad(rs.getInt("CLICO2"));
				usuario.setCodigoComuna(rs.getInt("CLICO3"));
				if (usuario.getCodigoRegion()!=0){
					usuario.setDescripcionRegion(recuperaRegion(usuario.getCodigoRegion()).trim());
				}
				if (usuario.getCodigoCiudad()!=0){
					usuario.setDescripcionCiudad(recuperaCiudad(usuario.getCodigoRegion(), usuario.getCodigoCiudad()).trim());
				}
				if (usuario.getCodigoComuna()!=0){
					usuario.setDescripcionComuna(recuperaComuna(usuario.getCodigoRegion(), usuario.getCodigoCiudad(), usuario.getCodigoComuna()).trim());
				}
				usuarios.add(usuario);
				
				
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
		
		
		return usuarios;
	}
	
	
	public UsuarioCanastaDTO obtieneUsuariosConDespacho(int rutCliente, int rutPersonal){
		List usuarios = new ArrayList();
		UsuarioCanastaDTO usuario =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRSANT1 " + 
        " Where CLCRU1="+rutCliente+" AND RUTPER="+rutPersonal+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usuario = new UsuarioCanastaDTO();
				usuario.setRutCliente(rs.getInt("CLCRU1"));
				usuario.setDvCliente(rs.getString("CLCDVR").trim());
				usuario.setRutPersonal(rs.getInt("RUTPER"));
				usuario.setDvPersonal(rs.getString("DIGPER").trim());
				usuario.setNombrePersonal(rs.getString("NOMPER").trim());
				usuario.setDescripciontipoDespacho(rs.getString("DETPDE").trim());
				usuario.setTipoDespacho(rs.getInt("CODTDE"));
				usuario.setCorrelativoDirecciones(rs.getInt("CLICOR"));
				if (rs.getString("CLIDI1")!=null && rs.getString("CLIDI1").trim().length()>0){
					usuario.setDireccionDespacho(rs.getString("CLIDI1").substring(0, 30).trim());
					usuario.setNumero(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
					
					
					usuario.setDepto(rs.getString("CLIDI1").substring(35, 40).trim());
				}
				
				/*usuario.setDireccionDespacho(rs.getString("CLIDI1").substring(0, 30).trim());
				usuario.setNumero(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
				
				
				usuario.setDepto(rs.getString("CLIDI1").substring(35, 40).trim());*/
				if (rs.getString("CLINUM")!=null && !rs.getString("CLINUM").equals("")){
					usuario.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX").trim());
				}
				
				//usuario.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX").trim());
				
				usuario.setTelefono(rs.getString("CLITEL").trim());
				usuario.setCelular(rs.getString("CLITE1").trim());
				//usuario.setRutEmpresa(rs.getString(""));
				//usuario.setDigitoEmpresa(rs.getString(""));
				//usuario.setRazonSocialEmpresa(rs.getString(""));
				//usuario.setApellidoPaterno(rs.getString(""));
				//usuario.setApellidoMaterno(rs.getString(""));
				//usuario.setNombreCompleto(rs.getString(""));
				//usuario.setCodigoOficinaSantander(rs.getString(""));
				usuario.setContactoRetiro(rs.getString("CONTRET").trim());
				usuario.setObservaciones(rs.getString("OBSDES").trim());
				usuario.setFechaDespacho(String.valueOf(rs.getInt("VECFE5")));
				//usuario.setPuntoCritico(rs.getString(""));
				//usuario.setIngresado(rs.getString(""));
				//usuario.setFechaDespacho(rs.getString(""));
				//usuario.setHoraUltimoIngreso(rs.getString(""));
				usuario.setMail(rs.getString("MAICNT").trim());
				usuario.setCodigoRegion(rs.getInt("CLICO1"));
				usuario.setCodigoCiudad(rs.getInt("CLICO2"));
				usuario.setCodigoComuna(rs.getInt("CLICO3"));
				if (usuario.getCodigoRegion()!=0){
					usuario.setDescripcionRegion(recuperaRegion(usuario.getCodigoRegion()).trim());
				}
				if (usuario.getCodigoCiudad()!=0){
					usuario.setDescripcionCiudad(recuperaCiudad(usuario.getCodigoRegion(), usuario.getCodigoCiudad()).trim());
				}
				if (usuario.getCodigoComuna()!=0){
					usuario.setDescripcionComuna(recuperaComuna(usuario.getCodigoRegion(), usuario.getCodigoCiudad(), usuario.getCodigoComuna()).trim());
				}
				
				usuario.setCodigoArticulo(String.valueOf(rs.getInt("CLDCOD")));
				usuario.setDescripcionArticulo(rs.getString("EXMDES"));
				usuario.setTipoCaja(String.valueOf(rs.getInt("TIPCJA")));
				
				/*ClidirDTO clidir = obtieneDireccionConDespacho(rs.getInt("CLCRU1"), rs.getInt("CLICOR"));
				if (clidir!=null){
					usuario.setDireccionDespacho(clidir.getDireccionCliente());
					usuario.setContactoRetiro(clidir.getNombreContacto());
					usuario.setDescripcionRegion(clidir.getDescripcionRegion().trim());
					usuario.setDescripcionCiudad(clidir.getDescripcionCiudad().trim());
					usuario.setDescripcionComuna(clidir.getDescripcionComuna().trim());
					usuario.setClidir(clidir);
				}*/
				
				
				
				
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
		
		
		return usuario;
	}
	
	public List obtieneReporteUsuariossinDespacho(int rutCliente){
		List usuarios = new ArrayList();
		UsuarioCanastaDTO usuario =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRSANT " + 
        " Where CLCRU1="+rutCliente+" AND CODTDE=0 FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				usuario = new UsuarioCanastaDTO();
				usuario.setRutCliente(rs.getInt("CLCRU1"));
				usuario.setDvCliente(rs.getString("CLCDVR").trim());
				usuario.setRutPersonal(rs.getInt("RUTPER"));
				usuario.setDvPersonal(rs.getString("DIGPER").trim());
				usuario.setNombrePersonal(rs.getString("NOMPER").trim());
				usuario.setDescripciontipoDespacho(rs.getString("DETPDE").trim());
				/*ClidirDTO clidir = obtieneDireccionConDespacho(rs.getInt("CLCRU1"), rs.getInt("CLICOR"));
				usuario.setDireccionDespacho(clidir.getDireccionCliente());
				usuario.setContactoRetiro(clidir.getNombreContacto());
				usuario.setDescripcionRegion(clidir.getDescripcionRegion().trim());
				usuario.setDescripcionCiudad(clidir.getDescripcionCiudad().trim());
				usuario.setDescripcionComuna(clidir.getDescripcionComuna().trim());
				usuario.setClidir(clidir);*/
				
				usuarios.add(usuario);
				
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
		
		
		return usuarios;
	}
	
	public ClidirDTO obtieneDireccionConDespacho(int rut, int correlativo){
		List usuarios = new ArrayList();
		ClidirDTO clidir = null;
		
		UsuarioCanastaDTO usuario =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2" + 
        " Where C1.CLIRUT="+rut+" AND C1.CLICOR="+correlativo+" AND C1.CLIRUT=C2.CLIRUT AND C1.CLICOR=C2.CLICOR AND C2.ESTDIR='A' FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClidirDTO();
				clidir.setDireccionCliente(rs.getString("CLIDI1").substring(0, 30));
				String numero = rs.getString("CLIDI1").substring(30, 35);
				clidir.setNumeroDireccion(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
				clidir.setCiudad(rs.getInt("CLICO2"));
				clidir.setComuna(rs.getInt("CLICO3"));
				clidir.setRegion(rs.getInt("CLICO1"));
				clidir.setDescripcionComuna(recuperaComuna(rs.getInt("CLICO1"), rs.getInt("CLICO2"), rs.getInt("CLICO3")).trim());
				clidir.setDescripcionCiudad(recuperaCiudad(rs.getInt("CLICO1"), rs.getInt("CLICO2")).trim());
				clidir.setDescripcionRegion(recuperaRegion(rs.getInt("CLICO1")));
				clidir.setNombreContacto(rs.getString("CLICON").trim());
				clidir.setMail(rs.getString("CLIEMA").trim());
				clidir.setDepto(Integer.parseInt(rs.getString("CLIDI1").substring(36, 40).trim()));
				clidir.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX"));
				clidir.setTelefono(rs.getString("CLITEL"));
				clidir.setCelular(rs.getString("CLITE1"));
				clidir.setObservacion(rs.getString("GLCMDI"));
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
