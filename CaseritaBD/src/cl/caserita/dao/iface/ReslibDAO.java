package cl.caserita.dao.iface;

import cl.caserita.dto.ReslibDTO;

public interface ReslibDAO {

	public int generaResLib(ReslibDTO dto);
	public int generaResLibCompras(ReslibDTO gen);
}
