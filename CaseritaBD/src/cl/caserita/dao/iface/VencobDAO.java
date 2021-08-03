package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.VencobDTO;

public interface VencobDAO {

	public VencobDTO obtenerVendedorSupervisor(int codigoVendedor);
	public List obtenerVendedor(int supervisor);
}
