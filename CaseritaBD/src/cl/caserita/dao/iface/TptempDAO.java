package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.TptempDTO;

public interface TptempDAO {

	public TptempDTO recuperaEmpresa(int codigoEmpresa);
	public List recuperaEmpresa();
	public TptempDTO recuperaEmpresaPorRut(int Rutempresa);
}
