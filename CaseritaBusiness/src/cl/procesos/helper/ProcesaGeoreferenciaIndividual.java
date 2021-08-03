package cl.caserita.procesos.helper;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.integracion.ProcesaGeoreferenciaDireccion;

public class ProcesaGeoreferenciaIndividual {

	public static void main (String[]args){
		DAOFactory dao = DAOFactory.getInstance();
		ClidirDAO clidir = dao.getClidirDAO();
		int rut=10115795;
		String dv="4";
		List lista = clidir.obtieneDirecciones(rut, dv.trim());
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			ClidirDTO dto = (ClidirDTO) iter.next();
			ProcesaGeoreferenciaDireccion inicial = new ProcesaGeoreferenciaDireccion();
			inicial.georeferencia(dto.getRutCliente(), dto.getCorrelativo());
			
		}
	}
}
