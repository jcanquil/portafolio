package cl.caserita.dao.iface;

import java.util.List;

public interface ClcdiaDAO {

	public List obtieneImpuesto(int empresa, int codigo, int rutCliente, String dv, int fecha, int hora);
	public List acumuladoImpuestos(int empresa, int codigoDocumento, int mes, int ano);
	public List acumuladoImpuestosBodega(int empresa, int codigoDocumento, int mes, int ano, int bodega);
	public List acumuladoImpuestosNC(int empresa, int codigoDocumento, int mes, int ano);
	public List acumuladoImpuestosBodegaNC(int empresa, int codigoDocumento, int mes, int ano, int bodega);
}
