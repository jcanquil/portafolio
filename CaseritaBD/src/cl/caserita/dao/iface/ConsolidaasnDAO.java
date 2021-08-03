package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ConsolidaasnDTO;

public interface ConsolidaasnDAO {
	
	public ConsolidaasnDTO recuperaConsolidado(int empresa, int carguio, int bodega, int articulo);
	public int actualizaConsolidado(ConsolidaasnDTO dto);
	public List recuperaConsolidadoCompleto(int empresa, int carguio, int bodega);
}
