package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.PrmprvDTO;

public interface PrmprvDAO {

	public PrmprvDTO obtieneProveedor(int rut,String dv);
	
	//Metodo para retornar lista de proveedores
	public List recuperaProveedores();
	
}
