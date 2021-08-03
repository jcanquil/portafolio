package cl.caserita.canastas.helper;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.HorarioDAO;

public class HorarioHelper {
	private  static Logger log = Logger.getLogger(HorarioHelper.class);

	public String recuperaHorarios(String par000){
		String respuesta ="";
		Gson gson = new Gson();
		log.info("HORARIO:"+par000);
		JsonObject json = (JsonObject)new JsonParser().parse(par000);
		
		int rut=0;
		if (json.get("rutCliente")!=null){
			rut = Integer.parseInt(json.get("rutCliente").toString().replaceAll("\"", ""));
			DAOFactory dao = DAOFactory.getInstance();
			HorarioDAO horario = dao.getHorarioDAO();
			
			List horarios = horario.horarios(rut);
			
			respuesta = gson.toJson(horarios);
		}
		
		return respuesta;
	}
}
