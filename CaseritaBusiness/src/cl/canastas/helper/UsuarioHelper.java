package cl.caserita.canastas.helper;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dto.AdicionalesDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.UsuarioCanastaDTO;
import cl.caserita.helper.UsuarioDatos;
import cl.caserita.password.ObtieneClave;
import cl.caserita.proceso.generaUsuarios;
import cl.caserita.xsd.usuario.Usuario;

public class UsuarioHelper {
	private  static Logger log = Logger.getLogger(UsuarioHelper.class);

	public String validaUsuario(String par000){
		String respuesta="";
		String valida="";
		Gson gson = new Gson();
		log.info("Usuario Canastas:"+par000);
		
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usuario = convierte.convierteUsuario(par000);
		
		log.info("RutCliente"+usuario.getRutCliente());
		log.info("RutPersonal"+usuario.getRutPersona());
		log.info("Password:"+usuario.getPassword());
		ObtieneClave clave = new ObtieneClave();
		String desencriptar = clave.convierteClave(usuario.getPassword());
		log.info("PasswordDesincriptada:"+desencriptar);
		Usuario usuarioRespuesta = new Usuario();

		if (usuario.getRutPersona()!=null && desencriptar!=null){
			DAOFactory dao = DAOFactory.getInstance();
			UsrcanastaDAO datos = dao.getUsrcanastaDAO();
			valida = datos.validaUsuario(Integer.parseInt(usuario.getRutCliente()), Integer.parseInt(usuario.getRutPersona()),usuario.getDigitoPersona().trim(),desencriptar.trim() );
			log.info("Acceso:"+valida);
			if (valida.equals("")){
				usuarioRespuesta.setCodigoEstado("1");
				usuarioRespuesta.setDescripcionEstado("USUARIO O CONTRASENA INVALIDOS");
			}else{
				usuarioRespuesta.setCodigoEstado("0");
				usuarioRespuesta.setDescripcionEstado("USUARIO VALIDO");
				usuarioRespuesta.setTipoUsuario(valida.substring(0, 1));
				UsuarioCanastaDTO dto = new UsuarioCanastaDTO();
				Fecha fch = new Fecha();
				dto.setFechaUltimoIngreso(fch.getYYYYMMDD());
				dto.setHoraUltimoIngreso(fch.getHHMMSS());
				dto.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
				dto.setRutPersonal(Integer.parseInt(usuario.getRutPersona()));
				log.info("RutCliente:"+dto.getRutCliente());
				log.info("RutPersonal:"+dto.getRutPersonal());
				log.info("Fecha:"+dto.getFechaUltimoIngreso());
				log.info("Hora:"+dto.getHoraUltimoIngreso());
				datos.actualizaAccesoUsuario(dto);
				usuarioRespuesta.setNombrePersona(valida.substring(0, valida.length()));
			}
		}
		
		
		respuesta = gson.toJson(usuarioRespuesta);
		return respuesta;
	}
	
	public String registraDireccion(String par000){
		String respuesta ="";
		
		Gson gson = new Gson();
		
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO datos = dao.getUsrcanastaDAO();
		ClidirDAO clidirDAO = dao.getClidirDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usuario = convierte.convierteUsuario(par000);
		ClidirDTO clidir=null;
		ClidiraDTO clidira = null;
		UsuarioCanastaDTO dto = new UsuarioCanastaDTO();
		int correlativo=0;
		if (usuario!=null){
			clidir = new ClidirDTO();
			clidira = new ClidiraDTO();
			correlativo = clidirDAO.obtieneCorrelativo(Integer.parseInt(usuario.getRutCliente()), usuario.getDigitoCliente().trim());
			if (correlativo==0){
				correlativo = 100;
			}else if (correlativo==199){
				correlativo = clidirDAO.obtieneCorrelativo2(Integer.parseInt(usuario.getRutCliente()), usuario.getDigitoCliente().trim());
				if (correlativo==0){
					correlativo = 300;
				}
			}
			dto.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			dto.setDvCliente(usuario.getDigitoCliente());
			dto.setDireccionDespacho(usuario.getDireccion().substring(0, 30)+usuario.getNumero().substring(31,35)+usuario.getDepartamento().substring(36,40));
			dto.setTipoDespacho(Integer.parseInt(usuario.getTipoDespacho()));
			clidir.setDireccionCliente(usuario.getDireccion().substring(0, 30)+usuario.getNumero().substring(31,35)+usuario.getDepartamento().substring(36,40));
			clidir.setCelular(usuario.getCelular().trim());
			clidir.setTelefono(usuario.getTelefono().trim());
			clidir.setRegion(Integer.parseInt(usuario.getCodigoRegion()));
			clidir.setCiudad(Integer.parseInt(usuario.getCodigoCiudad()));
			clidir.setComuna(Integer.parseInt(usuario.getCodigoComuna()));
			clidir.setMail(usuario.getEmail());
			clidir.setCorrelativo(correlativo);
			clidir.setNombreContacto(usuario.getNombrePersona());
			clidir.setVillaPoblacion(usuario.getVillaPoblacion());
			clidir.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			clidir.setDvCliente(usuario.getDigitoCliente());
			clidira.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			clidira.setDvCliente(usuario.getDigitoCliente());
			clidira.setCorrelativo(correlativo);
			clidira.setObservacion(usuario.getObservacionDespacho().trim());
			clidira.setLatitud(usuario.getLatitud().trim());
			clidira.setLongitud(usuario.getLongitud().trim());
			clidirDAO.generaDireccion(clidir);
			clidirDAO.generaClidira(clidira);
			
		}
		datos.generaUsuario(dto);
		
		
		
		return respuesta;
		
	}
	
	public String actulizaDireccion(String par000){
		String respuesta ="";
		
		Gson gson = new Gson();
		int respuestaAcceso=0;
		int respuestaAcceso2=0;
		int respuestaAcceso3=0;
		log.info("Registra:"+par000);
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO datos = dao.getUsrcanastaDAO();
		ClidirDAO clidirDAO = dao.getClidirDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usuario = convierte.convierteUsuario(par000);
		List adicionales = convierte.convierteAdicionaes(par000, usuario);
		ClidirDTO clidir=null;
		ClidiraDTO clidira = null;
		UsuarioCanastaDTO dto = new UsuarioCanastaDTO();
		int correlativo=0;
		if (usuario!=null){
			clidir = new ClidirDTO();
			clidira = new ClidiraDTO();
			if (usuario.getSolicitud().equals("1")){
				correlativo = clidirDAO.obtieneCorrelativo(Integer.parseInt(usuario.getRutCliente()), usuario.getDigitoCliente().trim());
				if (correlativo==0){
					correlativo = 100;
				}else if (correlativo==199){
					correlativo = clidirDAO.obtieneCorrelativo2(Integer.parseInt(usuario.getRutCliente()), usuario.getDigitoCliente().trim());
					if (correlativo==0){
						correlativo = 300;
					}else{
						correlativo=correlativo+1;
					}
				}else {
					correlativo=correlativo+1;
				}
			}else {
				if (usuario.getCorrelativo()!=null){
					correlativo = Integer.parseInt(usuario.getCorrelativo());
				}
				
			}
			
			dto.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			dto.setDvCliente(usuario.getDigitoCliente());
			usuario.setDireccion(usuario.getDireccion()+"                                       ");
			usuario.setNumero(usuario.getNumero()+"     ");
			usuario.setDepartamento(usuario.getDepartamento()+"        ");
			dto.setDireccionDespacho(usuario.getDireccion().substring(0, 30)+usuario.getNumero().substring(0, 5)+usuario.getDepartamento().substring(0, 5));
			dto.setTipoDespacho(Integer.parseInt(usuario.getTipoDespacho()));
			
			clidir.setDireccionCliente(usuario.getDireccion().substring(0, 30)+usuario.getNumero().substring(0, 5)+usuario.getDepartamento().substring(0, 5));
			clidir.setCelular(usuario.getCelular());
			clidir.setTelefono(usuario.getTelefono());
			clidir.setRegion(Integer.parseInt(usuario.getCodigoRegion()));
			clidir.setCiudad(Integer.parseInt(usuario.getCodigoCiudad()));
			clidir.setComuna(Integer.parseInt(usuario.getCodigoComuna()));
			clidir.setMail(usuario.getEmail());
			clidir.setCorrelativo(correlativo);
			clidir.setNombreContacto(usuario.getNombrePersona());
			clidir.setVillaPoblacion(usuario.getVillaPoblacion()+"                                         ");
			clidir.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			clidir.setDvCliente(usuario.getDigitoCliente());
			clidira.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			clidira.setDvCliente(usuario.getDigitoCliente());
			clidira.setCorrelativo(correlativo);
			clidira.setObservacion(usuario.getObservacionDespacho().trim());
			clidira.setLatitud(usuario.getLatitud().trim());
			clidira.setLongitud(usuario.getLongitud().trim());
			dto.setRutCliente(Integer.parseInt(usuario.getRutCliente()));
			dto.setRutPersonal(Integer.parseInt(usuario.getRutPersona()));
			dto.setObservaciones(usuario.getObservacionDespacho());
			dto.setDescripciontipoDespacho(usuario.getDescripcionTipoDespacho());
			dto.setContactoRetiro(usuario.getContactoRetiro());
			if (usuario.getCorrelativo().trim().length()>0){
				dto.setCorrelativoDirecciones(Integer.parseInt(usuario.getCorrelativo()));
			}
			
			//dto.setCorrelativoDirecciones(correlativo);
			dto.setTipoDespacho(Integer.parseInt(usuario.getTipoDespacho()));
			/*if (usuario.getFechaDespacho()!=null ){
				if (usuario.getFechaDespacho().toString().length()>0){
					String dia=usuario.getFechaDespacho().substring(0, 2);
					String mes=usuario.getFechaDespacho().substring(3, 5);
					String ano=usuario.getFechaDespacho().substring(6, 10);
					usuario.setFechaDespacho(ano.trim()+mes.trim()+dia.trim());
				}
			}*/
			
			if (usuario.getFechaDespacho()==null ){
				
				dto.setFechaDespacho("0");
			}else if (usuario.getFechaDespacho().trim().length()==0){
				dto.setFechaDespacho("0");
			}else if ( usuario.getFechaDespacho().trim().length()>0){
				dto.setFechaDespacho(usuario.getFechaDespacho());
			}
			if (usuario.getCodigoArticulo()!=null){
				if (usuario.getCodigoArticulo().trim().length()>0){
					dto.setCodigoArticulo(usuario.getCodigoArticulo());
					dto.setDescripcionArticulo(usuario.getDescripcionArticulo());
				}
			}else{
				dto.setCodigoArticulo("11");
				dto.setDescripcionArticulo("HITES");
			}
			
			if (usuario.getSolicitud().equals("1")){
				//respuestaAcceso=clidirDAO.generaDireccion(clidir);
				//respuestaAcceso2=clidirDAO.generaClidira(clidira);
			}else if (usuario.getSolicitud().equals("2")){
				//respuestaAcceso=clidirDAO.actualizaDireccionClidir(clidir);
				//respuestaAcceso2=clidirDAO.actualizaClidira(clidira);
			}
		
			
		}
		dto.setClidir(clidir);
		respuestaAcceso3=datos.actualizaUsuario(dto);
		Iterator iter = adicionales.iterator();
		int correlativoHijos=datos.obtieneultimoCorrelativo(Integer.parseInt(usuario.getRutCliente()), Integer.parseInt(usuario.getRutPersona()));
		while (iter.hasNext()){
			AdicionalesDTO adicio = (AdicionalesDTO) iter.next();
			if (adicio.getCorrelativo()==0){
				adicio.setCorrelativo(correlativoHijos);
				datos.generaHijos(adicio);
			}else{
				//Actualiza Datos
				int fecha = Integer.parseInt(adicio.getFechaNacimiento());
				if (fecha>0){
					datos.actualizaHijos(adicio);
				}else{
					//Elimina Hijo
					datos.eliminaHijo(adicio.getRutEmpresa(), adicio.getRutPersona(), adicio.getCorrelativo());
				}
				
			}
			
			
			correlativoHijos=correlativoHijos+1;
		}
		Usuario usr = new Usuario();
		log.info("Respuesta1:"+respuestaAcceso);
		log.info("Respuesta2:"+respuestaAcceso2);
		log.info("Respuesta3:"+respuestaAcceso3);
		if (respuestaAcceso==0&&respuestaAcceso2 ==0 && respuestaAcceso3==0){
			if (usuario.getSolicitud().equals("1")){
				usr.setCodigoEstado("0");
				usr.setDescripcionEstado("DIRECCION REGISTRADA EXITOSAMENTE");
			}else if (usuario.getSolicitud().equals("2")){
				usr.setCodigoEstado("0");
				usr.setDescripcionEstado("DIRECCION ACTUALIZADA EXISTOSAMENTE");
			}
			
		}else {
			if (usuario.getSolicitud().equals("1")){
				usr.setCodigoEstado("1");
				usr.setDescripcionEstado("DIRECCION NO REGISTRADA EXITOSAMENTE");
			}else if (usuario.getSolicitud().equals("2")){
				usr.setCodigoEstado("1");
				usr.setDescripcionEstado("DIRECCION NO ACTUALIZADA EXISTOSAMENTE");
			}
		}
		respuesta = gson.toJson(usr);
		log.info("Respuesta Registra:"+respuesta);

		return respuesta;
		
	}
	public static void main (String[]args){
		Usuario usuario = new Usuario();
		usuario.setCelular("98392839");
		usuario.setFechaDespacho("30/09/2015");
		String dia=usuario.getFechaDespacho().substring(0, 2);
		String mes=usuario.getFechaDespacho().substring(3, 5);
		String ano=usuario.getFechaDespacho().substring(6, 10);
		usuario.setFechaDespacho(ano.trim()+mes.trim()+dia.trim());
		if (usuario.getFechaDespacho()==null ){
			log.info("Error");
		}else if (usuario.getFechaDespacho().trim().length()==0){
			log.info("Correcto cero");
		}else if ( usuario.getFechaDespacho().trim().length()>0){
			log.info("Distinto cero");
		}
	}
}
