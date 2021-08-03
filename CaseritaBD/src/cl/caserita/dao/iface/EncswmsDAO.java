package cl.caserita.dao.iface;

import cl.caserita.dto.EncswmsDTO;

public interface EncswmsDAO {

	public int generaEncabezado(EncswmsDTO enc);
	public EncswmsDTO buscaEncabezado(int empresa, String nombreArchivo, int fecha);
}
