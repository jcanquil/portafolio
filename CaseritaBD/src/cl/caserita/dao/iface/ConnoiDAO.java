package cl.caserita.dao.iface;

import java.util.List;

public interface ConnoiDAO {

	public List buscaImpto(String tipoNota, int numeroNota, int fechaEmision);
	public int actualizaConnoi(String tipoNota, int numNota, int fechaEmision, int numero);
}
