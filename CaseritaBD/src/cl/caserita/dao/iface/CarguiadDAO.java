package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.CarguiadDTO;

public interface CarguiadDAO {

	public int generaAdicional(CarguiadDTO dto);
	public List recuperaCarguioTransporte(int codigoEmpresa,  int bodega, String estado);
}
