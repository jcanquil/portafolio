package cl.caserita.dao.iface;

import cl.caserita.dto.ExmodcDTO;

public interface ExmodcDAO {

	public ExmodcDTO buscaOrden(int numeroOrden, int rut, String digito);
	
	public ExmodcDTO buscaCabOrden(int empresa, int numeroOrden);
	public int actualizarCabecera(int codEmp, int numOC, int codBodega, String estadoOC);
		
}
