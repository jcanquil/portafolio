package cl.caserita.dao.iface;

import java.util.HashMap;
import java.util.List;

import cl.caserita.dto.ExmartDTO;

public interface ExmartDAO {

	public List recuperaArticulos();
	public ExmartDTO recuperaArticulo(int codArticulo, String digito);
	public HashMap buscaExdartOrdenada(int codigo, String dv);
	public ExmartDTO recuperaArticuloSinDigito(int codArticulo);
}
