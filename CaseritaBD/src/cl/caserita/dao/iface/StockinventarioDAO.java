package cl.caserita.dao.iface;

import cl.caserita.dto.ConfirmacionCarguioDetalleDTO;
import cl.caserita.dto.LogestinDTO;
import cl.caserita.dto.StockinventarioDTO;

public interface StockinventarioDAO {

	public int actualizaStock(StockinventarioDTO dto);
	public StockinventarioDTO lista(int empresa, int bodega, String estado, int articulo);
	public int creaStockInvWMS(StockinventarioDTO dto);
	public int generaLoginventario(LogestinDTO dto);
}
