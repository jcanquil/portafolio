package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ExmarbDTO;

public interface ExmarbDAO {

	public ExmarbDTO recuperaArticulo(int codBodega, int codArticulo);
	public int actualizaStockLinea(int codbodega, int codarticulo, String dvarticulo, double stockLinea);
	public int actualizaCostoNeto(int codbodega, int codarticulo, String dvarticulo, double costoneto);
	public ExmarbDTO obtieneCostosArticulo(int codBodega, int codArticulo);
	public List obtieneArticulosStockNegativo(int codBodega);
}
