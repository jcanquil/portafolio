package cl.caserita.dao.iface;

import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.VecmarDTO;

public interface LogintegracionDAO {

	public int generaLogArticulo(LogintegracionDTO dto);
	public int generaLogProveedor(LogintegracionDTO dto);
	public int generaLogChoferes(LogintegracionDTO dto);
	public int generaLogCliente(LogintegracionDTO dto);
	public int generaLogCarguio(LogintegracionDTO dto, CarguioDTO dto2);
	public int generaLogTraspaso(LogintegracionDTO dto, ExmtraDTO dto2);
	public int generaLogOrdenesCompra(LogintegracionDTO dto, ExmodcDTO dto2);
	public int generaLogMerma(LogintegracionDTO dto, VecmarDTO dto2);
	public int generaLogTptcom(LogintegracionDTO dto);
	public int generaLogTptbdg(LogintegracionDTO dto);
	
}
