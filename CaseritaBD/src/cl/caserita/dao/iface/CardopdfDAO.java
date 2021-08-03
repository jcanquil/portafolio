package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.CardopdfDTO;

public interface CardopdfDAO {

	public int actualizaDatos(CardopdfDTO dto);
	public List buscaDocumentosPendientes(int empresa, int bodega, String cuentaCorreo, int estado);
	public CardopdfDTO buscaDocumentosPendientesIndividual(int empresa, int bodega, String cuentaCorreo, int estado);
}
