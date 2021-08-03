package cl.caserita.dao.iface;

import cl.caserita.dto.StockdifDTO;

public interface StockdifDAO {

	public int actualizarStockDiferenciado(StockdifDTO dto);
	public StockdifDTO recuperaStockDiferenciado(StockdifDTO gen);
}
