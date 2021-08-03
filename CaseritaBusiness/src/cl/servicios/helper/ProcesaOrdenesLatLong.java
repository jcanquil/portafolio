package cl.caserita.servicios.helper;

import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.OrdvtaDAO;

public class ProcesaOrdenesLatLong {

	public static void main (String[]args){
		DAOFactory dao = DAOFactory.getInstance();
		OrdvtaDAO ordvta = dao.getOrdvtaDAO();
		DetordDAO detordDAO = dao.getDetordDAO();
		List ordenList = ordvta.obtieneOrdenes(2);
	}
}
