package cl.caserita.dao.iface;

import cl.caserita.dto.ExmtraDTO;

public interface ExmtraDAO {

	public ExmtraDTO recuperaEncabezado(int empresa, int numTraspaso);
	public int actualizaExmtra(int empresa, int numTraspaso, int numeroGuia);
	public ExmtraDTO obtieneOrdenTraspaso(int empresa, int numeroOT, int codigoBodega, int carguio);
	public int actualizaEstado(int empresa, int bodegaOrigen, int bodegaDestino, int numTraspaso);
	public ExmtraDTO recuperaEncabezadoFE (int empresa, int numTraspaso);
}
