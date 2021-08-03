package cl.caserita.canastas.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.UsuarioCanastaDTO;
import cl.caserita.xsd.usuario.Usuario;

public class ReportesHelper {
	private  static Logger log = Logger.getLogger(ReportesHelper.class);

	public String verificaReporte(String par000){
		
		Gson gson = new Gson();
		String respuesta="";
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO usr = dao.getUsrcanastaDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usrCon = convierte.convierteUsuario(par000);
		ReportesHelper helper = new ReportesHelper();
		List res=new ArrayList();
		if (usrCon.getSolicitud().equals("1")){
			//respuesta = helper.reporteConDespacho(par000);
			res = usr.obtieneReporteUsuariosTodos(Integer.parseInt(usrCon.getRutCliente()));
		}else if (usrCon.getSolicitud().equals("2")){
			respuesta = helper.reporteSinDespacho(par000);
		}
		respuesta = gson.toJson(res);
		return respuesta;
	}
	
	public String reporteSinDespacho(String par000){
		//List reporte = new ArrayList();
		Gson gson = new Gson();
		String respuesta="";
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO usr = dao.getUsrcanastaDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usrCon = convierte.convierteUsuario(par000);
		List reporte = usr.obtieneReporteUsuariossinDespacho(Integer.parseInt(usrCon.getRutCliente()));
		List reporte2= new ArrayList();
		Iterator iter = reporte.iterator();
		Usuario usuario = null;
		
		while (iter.hasNext()){
			usuario = new Usuario();
			UsuarioCanastaDTO usrDTO= new UsuarioCanastaDTO();
			usrDTO = (UsuarioCanastaDTO) iter.next();
			usuario.setRutCliente(String.valueOf(usrDTO.getRutCliente()));
			usuario.setDigitoCliente(usrDTO.getDvCliente());
			usuario.setRutPersona(String.valueOf(usrDTO.getRutPersonal()));
			usuario.setDigitoPersona(usrDTO.getDvPersonal());
			usuario.setNombrePersona(usrDTO.getNombrePersonal());
			//usuario.setDireccion(usrDTO.getDireccionDespacho().trim());
			//ClidirDTO clidir = usrDTO.getClidir();
			//usuario.setNumero(String.valueOf(clidir.getNumeroDireccion()));
			//usuario.setCelular(String.valueOf(clidir.getCelular()));
			//usuario.setTelefono(String.valueOf(clidir.getTelefono()));
			//usuario.setDescripcionRegion(clidir.getDescripcionRegion().trim());
			//usuario.setDescripcionCiudad(clidir.getDescripcionCiudad().trim());
			//usuario.setDescripcionComuna(clidir.getDescripcionComuna().trim());
			reporte2.add(usuario);
			
		}
		respuesta = gson.toJson(reporte);
		return respuesta;
	}
	
	public String reporteConDespacho(String par000){
		//List reporte = new ArrayList();
		Gson gson = new Gson();
		String respuesta="";
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO usr = dao.getUsrcanastaDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usrCon = convierte.convierteUsuario(par000);
		List reporte = usr.obtieneReporteUsuariosConDespacho(Integer.parseInt(usrCon.getRutCliente()));
		List reporte2= new ArrayList();
		Iterator iter = reporte.iterator();
		Usuario usuario = null;
		
		while (iter.hasNext()){
			usuario = new Usuario();
			UsuarioCanastaDTO usrDTO= new UsuarioCanastaDTO();
			usrDTO = (UsuarioCanastaDTO) iter.next();
			usuario.setRutCliente(String.valueOf(usrDTO.getRutCliente()));
			usuario.setDigitoCliente(usrDTO.getDvCliente());
			usuario.setRutPersona(String.valueOf(usrDTO.getRutPersonal()));
			usuario.setDigitoPersona(usrDTO.getDvPersonal());
			usuario.setNombrePersona(usrDTO.getNombrePersonal());
			usuario.setDireccion(usrDTO.getDireccionDespacho().trim());
			ClidirDTO clidir = usrDTO.getClidir();
			usuario.setNumero(String.valueOf(clidir.getNumeroDireccion()));
			usuario.setCelular(String.valueOf(clidir.getCelular()));
			usuario.setTelefono(String.valueOf(clidir.getTelefono()));
			usuario.setDescripcionRegion(clidir.getDescripcionRegion().trim());
			usuario.setDescripcionCiudad(clidir.getDescripcionCiudad().trim());
			usuario.setDescripcionComuna(clidir.getDescripcionComuna().trim());
			usuario.setDescripcionTipoDespacho(usrDTO.getDescripciontipoDespacho());
			reporte2.add(usuario);
			
		}
		respuesta = gson.toJson(reporte2);
		return respuesta;
	}
	public String despachoIndivual(String par000){
		//List reporte = new ArrayList();
		Gson gson = new Gson();
		String respuesta="";
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO usr = dao.getUsrcanastaDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usrCon = convierte.convierteUsuario(par000);
		//List reporte = usr.obtieneUsuariosConDespacho(Integer.parseInt(usrCon.getRutCliente()),Integer.parseInt(usrCon.getRutPersona()));
		List reporte = new ArrayList();
		List reporte2= new ArrayList();
		Iterator iter = reporte.iterator();
		Usuario usuario = null;
		
		while (iter.hasNext()){
			usuario = new Usuario();
			UsuarioCanastaDTO usrDTO= new UsuarioCanastaDTO();
			usrDTO = (UsuarioCanastaDTO) iter.next();
			usuario.setRutCliente(String.valueOf(usrDTO.getRutCliente()));
			usuario.setDigitoCliente(usrDTO.getDvCliente());
			usuario.setRutPersona(String.valueOf(usrDTO.getRutPersonal()));
			usuario.setDigitoPersona(usrDTO.getDvPersonal());
			usuario.setNombrePersona(usrDTO.getNombrePersonal());
			usuario.setDireccion(usrDTO.getDireccionDespacho().trim());
			usuario.setTipoDespacho(String.valueOf(usrDTO.getTipoDespacho()));
			ClidirDTO clidir = usrDTO.getClidir();
			usuario.setNumero(String.valueOf(clidir.getNumeroDireccion()));
			usuario.setCelular(String.valueOf(clidir.getCelular()));
			usuario.setTelefono(String.valueOf(clidir.getTelefono()));
			usuario.setDescripcionRegion(clidir.getDescripcionRegion().trim());
			usuario.setDescripcionCiudad(clidir.getDescripcionCiudad().trim());
			usuario.setDescripcionComuna(clidir.getDescripcionComuna().trim());
			usuario.setDescripcionTipoDespacho(usrDTO.getDescripciontipoDespacho().trim());
			usuario.setTelefono(clidir.getTelefono().trim());
			usuario.setCelular(clidir.getCelular().trim());
			usuario.setLatitud(clidir.getLatitud().trim());
			usuario.setLongitud(clidir.getLongitud().trim());
			usuario.setCodigoRegion(String.valueOf(clidir.getRegion()));
			usuario.setCodigoCiudad(String.valueOf(clidir.getCiudad()));
			usuario.setCodigoComuna(String.valueOf(clidir.getComuna()));
			usuario.setVillaPoblacion(clidir.getVillaPoblacion());
			usuario.setDepartamento(String.valueOf((clidir.getDepto())));
			usuario.setObservacionDespacho(clidir.getObservacion().trim());
			reporte2.add(usuario);
			
		}
		respuesta = gson.toJson(reporte2);
		return respuesta;
	}
	
	public static void main (String[]args){
		ReportesHelper report = new ReportesHelper();
		
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO usr = dao.getUsrcanastaDAO();
		usr.obtieneReporteUsuariosTodos(97036000);
	}
}
