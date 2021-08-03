package cl.caserita.dao.iface;

import cl.caserita.dto.NcplogDTO;

public interface NcplogDAO {
	
	public int insertaNcplog(NcplogDTO dto);
	public int buscaUltimaLineaNcplog(NcplogDTO dto);

}
