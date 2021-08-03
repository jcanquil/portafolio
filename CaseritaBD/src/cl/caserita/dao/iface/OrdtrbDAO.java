package cl.caserita.dao.iface;

import cl.caserita.dto.OrdtrbDTO;

public interface OrdtrbDAO {

	public int generaOrden(OrdtrbDTO ord);
	public int buscaNumeroOrden();
}
