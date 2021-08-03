package cl.caserita.canastas.helper;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CentroDistribucionDAO;

public class PruebaHelper {

	public static void main (String[]args){
		DAOFactory fac = DAOFactory.getInstance();
		CentroDistribucionDAO centro = fac.getCentroDistribucionDAO();
		centro.buscaRegionCiudadComuna(97036000, 13, 1, 3, 6);
	}
}
