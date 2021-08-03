package cl.caserita.helper;

import org.apache.log4j.Logger;

import cl.caserita.dto.ClienteUsuarioDTO;

public class UsuarioDatos {
	private static Logger log = Logger.getLogger(UsuarioDatos.class); 

	public static ClienteUsuarioDTO procesa(String usuario, String password){
		ClienteUsuarioDTO clienteUsuario=null;
		int usuarioRes=0;
		log.info("Usuario:"+usuario);
		log.info("password:"+password);
		if (usuario.equals("JAIME") && password.equals("123")){
			clienteUsuario= new ClienteUsuarioDTO();
			clienteUsuario.setRut("15448543");
			usuarioRes=1;
			log.info("INGRESO VALIDACION");
		}
		
		return clienteUsuario;
	}
}

