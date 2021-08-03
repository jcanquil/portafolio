package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.TipodespachoDTO;

public interface TipodespachoDAO {

	public TipodespachoDTO recuperaTipoDespacho (int codigotipoDespacho);
	public List tiposDespachos (int rut);
}
