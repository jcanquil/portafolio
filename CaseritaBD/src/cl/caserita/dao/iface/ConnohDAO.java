package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ConnohDTO;

public interface ConnohDAO {

	public ConnohDTO buscaConnoh(int empresa, String tipoNota, int numNota, int fechaEmision);
	public int actualizaConnoh(int empresa, String tipoNota, int numNota, int fechaEmision, int numeroNotaFE);
	public int insertaConnoh(ConnohDTO dto);
	public int buscaExisteConnoh(int empresa, int numNota);
	public int existeConnoh(ConnohDTO nota);
	public int eliminaConnoh (ConnohDTO nota);
	public int eliminaConnohTransp (ConnohDTO nota);
}
