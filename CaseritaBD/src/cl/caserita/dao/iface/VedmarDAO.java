package cl.caserita.dao.iface;

import java.util.HashMap;
import java.util.List;

import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public interface VedmarDAO {

	public List obtenerDatosVedmar(int empresa, int codMovto, int fechaMovto, int numDocumento);
	public List obtenerDatosVedmarGuia(int empresa, int codMovto, int fechaMovto, int numDocumento);
	public List consultaArticulosPrecio(int empresa, int tipoMov, int fecha, int bodega);
	public List ListaPedidossinVedmar( int fecha);
	public int generaMovimiento(VedmarDTO vedmar);
	public List obtenerDatosVedmarMer(int empresa, int codMovto, int fechaMovto, int numDocumento);
	public HashMap obtenerDatosVedmarNoHay(int empresa, int tipoMov, int fecha, int numero);
	public HashMap obtenerDatosVedmarMerHash(int empresa, int tipoMov, int fecha, int numero);
	public int actualizaMerma(VedmarDTO vedmar);
	public int eliminaDetalle(VedmarDTO vedmar);
	public VecmarDTO recuperaTotales(int empresa, int tipoMov, int fecha, int numero);
	public int eliminaDetallePromocion(VedmarDTO vedmar);
	public int obtenerCorrelativo(int empresa, int tipoMov, int fecha, int numero);
	public VedmarDTO buscaArticuloVedmar(VedmarDTO dto);
	public int actualizaArticulo(VedmarDTO vedmar);
	public double calculaImpuestosArticulo(int articulo, String dv);
	public void actualizaSwitchVecmar(int empresa, int codigo, int fecha, int numero, int swi);
	public int obtieneCantidadLineas(int empresa, int tipoMov, int fecha, int numero);
	public int actualizaFecha(VedmarDTO vedmar);
	public VedmarDTO obtenerDatosVedmarNoHayCorrelativo(int empresa, int tipoMov, int fecha, int numero, int correlativo);
	public int verificaVenta(int empresa, int tipoMov, int fecha, int numero);
	public int verificaArticulosVentas(int empresa, int tipoMov, int fecha, int numero);
	public int actualizaArticuloNOHAY(VedmarDTO vedmar);
	public VedmarDTO obtenerDatosArticulo(int empresa, int tipoMov, int fecha, int numero, int articulo, int correlativo);
	public VedmarDTO obtenerArticuloDifCorrelativo(int empresa, int tipoMov, int fecha, int numero, int articulo, int correlativo);
}
