package cl.caserita.dao.iface;

import cl.caserita.dto.ExmcreDTO;

public interface ExmcreDAO {
	public ExmcreDTO recuperaCorrSolicitud(int empresa, int rutprov, String dvprov, int nrodocto, int fechasol);
}
