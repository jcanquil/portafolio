package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ClmcliDTO;

public interface VecmonDAO {

	public List obtenerDatosVecmon (int empresa, int tipoMovto);
	public void actualizaVecmon(int empresa, int codigo, int fecha, int numero);
}
