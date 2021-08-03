package cl.caserita.canastas.helper;

import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClienteCanastaDAO;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dto.UsuarioCanastaDTO;
import cl.caserita.xsd.cliente.Clientes;
import cl.caserita.xsd.usuario.Usuario;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClienteHelper {
	private  static Logger log = Logger.getLogger(ClienteHelper.class);

	public String obtieneCliente(String par000){
		String respuesta="";

		int rutCliente=0,  rutUsuario=0;
		log.info("Cliente :"+par000);

		
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		Gson gson = new Gson();
		JsonObject json = (JsonObject)new JsonParser().parse(par000);
		if (json.get("rutCliente")!=null){
			rutCliente = Integer.parseInt(json.get("rutCliente").toString().replaceAll("\"", "").trim());
		}
		if (json.get("rutUsuario")!=null){
			rutUsuario = Integer.parseInt(json.get("rutUsuario").toString().replaceAll("\"", "").trim());
		}
		
		if (rutUsuario!=0){
			DAOFactory dao = DAOFactory.getInstance();
			ClienteCanastaDAO datos = dao.getClienteCanastaDAO();
			UsrcanastaDAO usuario = dao.getUsrcanastaDAO();
			UsuarioCanastaDTO usr = usuario.obtieneUsuariosConDespacho(rutCliente, rutUsuario);
			List hijos = usuario.obtieneHijos(rutCliente, rutUsuario);
			if (hijos!=null && hijos.size()>0){
				usr.setAdicionales(hijos);
			}
			if (usr.getFechaDespacho()!=null){
				if (usr.getFechaDespacho().toString().length()>0){
					if (Integer.parseInt(usr.getFechaDespacho().toString())>0){
						String dia=usr.getFechaDespacho().substring(6, 8);
						String mes=usr.getFechaDespacho().substring(4, 6);
						String ano=usr.getFechaDespacho().substring(0, 4);
						usr.setFechaDespacho(dia.toString()+"/"+mes.toString()+"/"+ano);
					}
					
				}
			}
			respuesta=gson.toJson(usr);
		}/*else{
			List clientes = datos.obtieneClienteCanasta();
			respuesta = gson.toJson(clientes);
		}*/
		
		log.info("Respuesta Cliente:"+respuesta);
		
		
		
		
		
		return respuesta;
	}
}
