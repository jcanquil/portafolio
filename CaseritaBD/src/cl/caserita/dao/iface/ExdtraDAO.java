package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ExdtraDTO;

public interface ExdtraDAO {
	public List recuperaDetalle(int empresa, int numTraspaso);
	public int actualizaEstado(ExdtraDTO dto);
	public int actualizaUnidadesOT(ExdtraDTO dto);
	public ExdtraDTO buscaOTCarguio(ExdtraDTO dto);
}
