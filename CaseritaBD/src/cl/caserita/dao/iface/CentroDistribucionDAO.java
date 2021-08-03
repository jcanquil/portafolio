package cl.caserita.dao.iface;

import java.util.List;

public interface CentroDistribucionDAO {

	public List buscaRegionCiudadComuna(int rut, int codRegion, int codCiudad, int tipoDespacho, int codComuna);
	
	public List buscaTiposDespacho(int rut, int codRegion, int codCiudad, int codComuna);
		
	
}
