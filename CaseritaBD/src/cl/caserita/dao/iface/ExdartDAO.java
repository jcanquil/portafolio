package cl.caserita.dao.iface;

import cl.caserita.dto.ExdartDTO;

public interface ExdartDAO {
	public ExdartDTO buscaCodBarraArt(int codart, String dvart, String formato);
}
