package cl.caserita.proceso;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ActecoDAO;

public class procesoPrecio {

	public static void main (String args[]){
		DAOFactory factory = DAOFactory.getInstance();
		ActecoDAO acteco = (ActecoDAO) factory.getActecoDAO();
		acteco.buscapcomboo();
		
	}
}
