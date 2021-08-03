package cl.caserita.ecommerce.helper;

import java.util.List;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.TiponegocioDAO;

public class TipoNegocioHelper {

	public String tipoNegocio(){
		Gson gson = new Gson();
		
		DAOFactory dao = DAOFactory.getInstance();
		TiponegocioDAO tipone = dao.getTiponegocioDAO();
		List negocio = tipone.tipoNegocio();
		return gson.toJson(negocio);
	}
}
