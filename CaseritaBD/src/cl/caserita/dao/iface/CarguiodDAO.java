package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CarguiodTranspDTO;

public interface CarguiodDAO {

	public int actualizaCarguiod(CarguiodDTO dto);
	public int eliminaDetalleCarguiod(CarguiodDTO dto);
	public int actualizaGuia(CarguiodDTO dto);
	public CarguiodDTO buscaOTCarguio(int empresa, int carguio, int bodega, int nroOT);
	public int actualizaCarguiodOT(CarguiodDTO dto);
	public int liberaDetalleCarguioMapa(int empresa, int codigoBodega, String estado, String estadoNuevo);
	public int buscaExisteCarguioc(int empresa, int codBodega, int numCarguio, int rutChof, String digChof);
	public int buscaExisteChofer(int empresa, int codBodega, int rutChof, String digChof);
	public int buscaExisteCarguioTransp(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, String patente);
	public String obtieneEstadoCarguioD(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente);
	public int actualizaRedespachod(int codEmpresa, int codBodega, int numCarguio, int numOV, int cantiredespa);
	public List buscaCarguiodRedespacho(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int tipodespacho);
	public int obtieneFechaVencimientoArt(int codEmpresa, int codBodega, int numCarguio, int codArticulo);
	public List buscaCarguiodRedespacho(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int tipodespacho, int numerodocto);
	public CarguiodDTO buscaDetalleCarguiodArt(int empresa, int carguio, String patente, int bodega, int nroOV, int codArticulo, int corrDirecDespa);
	public String obtieneEstadoCarguioDet(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, String patente, int numOVe);
	public String obtieneEstadoCarguioD(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente, int rutClie, int numeroOVe);
	public List buscaCarguiodTransp(int empresa, int rutChofer, String digChofer, int bodega, int tipodespacho, String numerodocto, int numCarg, String patente);
	public int obtieneCantidadRedespachos(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente);
	public CarguiodTranspDTO obtieneMotivoCarguioD(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, int numDocto, String patente);
	public int actualizaEstadoCarguiodTransp(String codEstado, String codMotivo, int codEmpresa, int numcarguio, int codBodega, int numOV, int numdocu);
	public int actualizaEstadoArticuloCarguiodTransp(String codEstado, String codMotivo, int codEmpresa, int numcarguio, String patente, int codBodega, int numOV, int rutClie, int codArti, int numdocus);
}
