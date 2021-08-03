package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.InfogastoDTO;

public interface InfogastoDAO {

	public int insertaGastoRRHH(InfogastoDTO info);
	public List obtieneGasto(int periodo);
	public int obtieneCorrelativo();
}
