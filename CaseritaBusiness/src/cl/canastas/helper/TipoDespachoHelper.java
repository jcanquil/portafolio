package cl.caserita.canastas.helper;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CentroDistribucionDAO;
import cl.caserita.dao.iface.TipodespachoDAO;

public class TipoDespachoHelper {
	private  static Logger log = Logger.getLogger(TipoDespachoHelper.class);

	public String obtieneTipoDespacho(String par000){
		String respuesta="";
		Gson gson = new Gson();
		log.info("Tipo Despacho:"+par000);
		JsonObject json = (JsonObject)new JsonParser().parse(par000);
		
		int rut=0, codRegion=0,codCiudad=0,codComuna=0;
		if (json.get("rutCliente")!=null && json.get("codRegion")!=null && json.get("codCiudad")!=null && json.get("codComuna")!=null){
			rut = Integer.parseInt(json.get("rutCliente").toString().replaceAll("\"", ""));
			codRegion=Integer.parseInt(json.get("codRegion").toString().replaceAll("\"", ""));
			codCiudad=Integer.parseInt(json.get("codCiudad").toString().replaceAll("\"", ""));
			codComuna=Integer.parseInt(json.get("codComuna").toString().replaceAll("\"", ""));
			DAOFactory dao = DAOFactory.getInstance();
			TipodespachoDAO tipo = dao.getTipodespachoDAO();
			CentroDistribucionDAO centro = dao.getCentroDistribucionDAO();
			List tipoList = centro.buscaTiposDespacho(rut, codRegion, codCiudad, codComuna);
			respuesta = gson.toJson(tipoList);
		}
		log.info("Respuesta Tipo Despacho:"+respuesta);
		
		
		return respuesta;
	}
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return false;
		} catch (NumberFormatException nfe){
			return true;
		}
	} 
	
	
}
