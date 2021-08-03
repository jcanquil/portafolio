package cl.caserita.dao.iface;

import cl.caserita.dto.ExdacpDTO;

public interface ExdacpDAO {
	
	public ExdacpDTO calculamontosCombo(int codigoEmpresa, int codCombo, String dvCombo, int codBodega, int codTven, int cantidev, int porcendescto);
	
}
