package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.TpctraDTO;

public interface TpctraDAO {

	public List buscaAllTransportista();
	public TpctraDTO buscaTransportista(int rut);
}
