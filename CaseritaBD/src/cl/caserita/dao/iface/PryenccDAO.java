package cl.caserita.dao.iface;

import cl.caserita.dto.PryenccDTO;

public interface PryenccDAO {

	public int generaRegistroEncuesta(PryenccDTO pry);
	public int obtieneEncuesta(int rut, int rutPersonal);
}
