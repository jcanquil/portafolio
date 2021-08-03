package cl.caserita.dao.iface;

import java.util.List;

public interface CldmcoDAO {

	public List obtieneArticulos(int empresa, int codigo, int rutCliente, String dv, int fecha, int hora, int tipoVendedor, int bodega,int tipoMov, int numero);
	public List obtieneFleteCldmco(int empresa, int coddoc, int rutclie, String digclie, int fecha, int codbod, int numdoc, int codarti);
}
