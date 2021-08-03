package cl.caserita.canastas.helper;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.password.ObtieneClave;
import cl.caserita.xsd.usuario.Usuario;

public class EncriptaHelper {
	private  static Logger log = Logger.getLogger(EncriptaHelper.class);

	public String clave(String par000){
		Gson gson = new Gson();
		String respuesta="";
		log.info(" Encriptada:"+par000);
		
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Usuario usrCon = convierte.convierteUsuario(par000);
		ObtieneClave clave = new ObtieneClave();
		Usuario usr2 = new Usuario();

		if (usrCon.getPassword()!=null ){
			DAOFactory dao = DAOFactory.getInstance();
			UsrcanastaDAO usr = dao.getUsrcanastaDAO();
			String claveEncriptada = clave.encriptaClave(usrCon.getPassword().trim());
			
			usr2.setPassword(claveEncriptada);
			log.info("Clave Encriptada:"+claveEncriptada);
		}
		
		
		respuesta = gson.toJson(usr2);
		
		return respuesta;
	}
	
}
