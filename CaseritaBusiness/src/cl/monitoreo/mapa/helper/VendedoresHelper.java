package cl.caserita.monitoreo.mapa.helper;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.AdsusrDAO;
import cl.caserita.dao.iface.VencobDAO;
import cl.caserita.dto.RespuestaCanastaDTO;
import cl.caserita.dto.UsuarioMonitoreoDTO;

public class VendedoresHelper {

	private static Logger log = Logger.getLogger(UsuarioMonitoreoHelper.class);

	public String vendedor(String par000){
		String resp="";
		String supervisor="";
		String password="";
		Gson gson = new Gson();
		RespuestaCanastaDTO resp2 = new RespuestaCanastaDTO();
		try{
			JSONObject object = (JSONObject) new JSONParser().parse(par000);
			 log.info("Parametro Entrada :"+par000);
			 if (object.get("codigoSupervisor")!=null){
				 supervisor = object.get("codigoSupervisor").toString().replaceAll("\"","");
			 }
			 
			log.info("Codigo Supervisor:"+supervisor);
			
			 if (supervisor!=null && supervisor!="" ){
				 DAOFactory dao = DAOFactory.getInstance();
				 VencobDAO vencob = dao.getVencobDAO();
				 List lista = vencob.obtenerVendedor(Integer.parseInt(supervisor));
				 if (lista.size()==0){
					 resp2.setCodigoEstado(-2);
					 resp2.setDescripcionEstado("No existe informacion de Vendedores Para supervisor indicado");
					 resp = gson.toJson(resp2);
				 }else{
					 resp = gson.toJson(lista);
					 log.info("Respuesta :"+resp);
				 }
				 
			 }else{
				 resp2.setCodigoEstado(-1);
				 resp2.setDescripcionEstado("Debe indicar un supervisor");
				 resp = gson.toJson(resp2);

			 }
		}catch(Exception e){
			resp2.setCodigoEstado(-1);
			 resp2.setDescripcionEstado("Debe indicar un supervisor");
			 resp = gson.toJson(resp2);
			e.printStackTrace();
		}
		return resp;
	}
	
	public static void main (String []args){
		VendedoresHelper hel = new VendedoresHelper();
		String input = "{\"codigoSupervisor22\":\"344\"}";
		hel.vendedor(input);
	}
	
}
