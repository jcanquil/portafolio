package cl.caserita.dao.iface;

import java.util.List;

public interface ExtariDAO {
	public int buscasiArticuloesExento(int codarticulo, String dvarticulo);
	public double obtenerCostoNeto(int codarticulo, String dvarticulo, double costobruto);
	public double recuperaImpuestos(int codarticulo, String dvarticulo);
}
