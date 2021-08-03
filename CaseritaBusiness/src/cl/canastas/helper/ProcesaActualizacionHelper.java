package cl.caserita.canastas.helper;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dto.UsuarioCanastaDTO;

public class ProcesaActualizacionHelper {
	private  static Logger log = Logger.getLogger(ProcesaActualizacionHelper.class);

	public static void main (String[]args){
		log.info("ACTUALIZA:");
		DAOFactory dao = DAOFactory.getInstance();
		UsrcanastaDAO usr = dao.getUsrcanastaDAO();
		List lista = usr.obtieneActualizacionCliente();
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			UsuarioCanastaDTO dto = (UsuarioCanastaDTO) iter.next();
			String direccion = dto.getDireccionDespacho()+"                                              ";
			String numero = String.valueOf(dto.getNumero())+"           ";
			String dpto = dto.getDepto() + "               ";
			dto.setDireccionDespacho(direccion.substring(0, 30)+numero.substring(0, 5)+dpto.subSequence(0, 5));
			log.info("direccion:"+dto.getDireccionDespacho());
			usr.actualizaBDD(dto);
		}
	}
}
