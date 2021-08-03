package cl.caserita.dao.iface;

import cl.caserita.dto.DetlibDTO;

public interface DetlibDAO {

	public int genDetlib(DetlibDTO dto);
	public int genDetlibCompras(DetlibDTO gen);
}
