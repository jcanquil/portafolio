package cl.caserita.dao.iface;

import cl.caserita.dto.ChoftranDTO;

public interface ChoftranDAO {

	public ChoftranDTO obtenerDatos (int rut, String dv);
	public ChoftranDTO obtenerDigitoRut(int rut);
}
