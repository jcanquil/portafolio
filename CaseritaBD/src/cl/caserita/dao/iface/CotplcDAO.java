package cl.caserita.dao.iface;

import cl.caserita.dto.CotplcDTO;

public interface CotplcDAO {

	public CotplcDTO buscaPeriodo(int ano, int mes);
	public int insertaPeriodo(CotplcDTO cotplc);

}
