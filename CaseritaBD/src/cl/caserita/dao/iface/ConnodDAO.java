package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ConnodDTO;

public interface ConnodDAO {

	public List buscaConnod(int empresa, String tipoNota, int numNota, int fechaEmision, int codDoc);
	public int actualizaConnod(int empresa, String tipoNota, int numNota, int fechaEmision, int numero);
	public int insertaConnod(ConnodDTO dto);
	public List obtieneConsolidadoNCp(String numerosNCpes, int codEmpresa,int numeroCarguio, int codBodega);
	public int buscaExisteConnod(ConnodDTO nota);
	public int eliminaConnod (ConnodDTO nota);
	public List<ConnodDTO> buscaExisteConnodTransp(ConnodDTO nota);
	public int eliminaConnodTransp (ConnodDTO nota);
}
