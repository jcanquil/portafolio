package cl.caserita.monitoreo.mapa.helper;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.AdsusrDAO;
import cl.caserita.dto.RespuestaCanastaDTO;
import cl.caserita.dto.UsuarioMonitoreoDTO;

public class UsuarioMonitoreoHelper {
	private static Logger log = Logger.getLogger(UsuarioMonitoreoHelper.class);

	public String validaUsuario(String par000){
		String resp="";
		String usuario="";
		String password="";
		Gson gson = new Gson();
		RespuestaCanastaDTO resp2 = new RespuestaCanastaDTO();
		UsuarioMonitoreoDTO dto =new UsuarioMonitoreoDTO();
		try{
			JSONObject object = (JSONObject) new JSONParser().parse(par000);
			 log.info("Parametro Entrada :"+par000);
			 if (object.get("usuario")!=null){
				 usuario = object.get("usuario").toString().replaceAll("\"","");
			 }
			 if (object.get("password")!=null){
				 password = object.get("password").toString().replaceAll("\"","");
			 }
			log.info("Usuario:"+usuario);
			log.info("Password :"+password);
			 if (usuario!=null && password!=null && usuario!="" && password!=""){
				 DAOFactory dao = DAOFactory.getInstance();
				 AdsusrDAO adsDAO = dao.getAdsusrDAO();
				  dto = adsDAO.validaUsuarioMonitoreo(usuario, password.trim());
				 resp = gson.toJson(dto);
				 log.info("Respuesta :"+resp);
			 }else{
				dto.setEstado("-2");
				 dto.setDescripcion("Debe indicar Usuario o Password");
				 resp = gson.toJson(dto);
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
		return resp;
	}
	
	public static void main (String []args){
		UsuarioMonitoreoHelper hel = new UsuarioMonitoreoHelper();
		String input = "{\"usuario2\":\"DASTUDILLO2\",\"password\":\"123\"}";
		hel.validaUsuario(input);
	}
}
