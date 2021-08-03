package cl.caserita.dao.iface;

import java.util.HashMap;
import java.util.List;

import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguioDetalleDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.ExmtraDTO;

public interface CarguioDAO {

	public List listaCarguios(int codigoEmpresa, String estado);
	public int generaCarguioDetalle(CarguioDetalleDTO carguioD);
	public int generaCarguioCabecera(CarguioDTO carguioC);
	public int obtieneNumeroCarguio(int codigoBodega, int tipo);
	public OrdvtaDTO obtieneOrdenes(int empresa, int numeroOV, int codigoBodega);
	public int eliminaOVCarguio(CarguioDetalleDTO orden);
	public int obtieneCorrelativoCarguio(int codigoEmpresa, int numeroCarguio, int codigoBodega);
	public CarguioDTO obtieneCarguio(int codigoEmpresa, String estado, int bodega, int numeroCarguio);
	public int verificaOVCarguio(int codigoEmpresa, int bodega, int numeroCarguio, int numeroOV);
	public int actualizarestadoDetalleCarguio(int empresa, int bodega, int numeroCarguio, String estado);
	public int actualizarestadoCarguio(int empresa, int bodega, int numeroCarguio, String estado);
	public int verificaOrdenExisteCarguio(int empresa, int numeroOV, int codigoBodega, int rutCliente);
	public CarguioDTO listaCarguiosWms(int codigoEmpresa, String estado, int Carguio, int codigoBodega);
	//public CarguioDTO listaCarguiosTranfeWms(int codigoEmpresa, String estado, int CarguioTranf, int codigoBodega);
	public List listarCarguiosTranfeWms(int codigoEmpresa, String estado, int CarguioTranf, int codigoBodega);
	public List obtienedevolucionCarguio(int codigoEmpresa, String estado, int bodega, int numeroCarguio, String tipcar);
	
	public CarguioDTO listaCarguiosOTWms(int codigoEmpresa, String estado, int Carguio, int codigoBodega);
	public ExmtraDTO obtieneOTs(int empresa, int numeroOT, int codigoBodOrigen, int codigoBodDestino);
	public HashMap listaCarguiosValidaConfirmacion(int codigoEmpresa, String estado, int Carguio, int codigoBodega);
	public List listarCarguiosTransp(int rutChofer, String digChofer, int numeritodecarguio);
	public CarguioDTO obtieneCarguioDTO(int codigoEmpresa, int Carguio, int codigoBodega);
	public int actualizaChofer(int empresa, int bodega, int numeroCarguio, int rut, String dv, String patente);
	public int actualizaChoferDetalle(int empresa, int bodega, int numeroCarguio, int rut, String dv, String patente);
	public List buscaCarguiosHijos(int empresa, int bodega, int numeroCarguio, String estado);
	public HashMap listaCarguiosFacturacion(int codigoEmpresa, int Carguio, int codigoBodega);
	public int liberaCarguioMapa(int empresa, int codigoBodega, String estado, String estadoNuevo);
	public HashMap listaCarguiosFacturacionEspeciales(int codigoEmpresa, int Carguio, int codigoBodega);
	public OrdvtaDTO obtieneOrdenesCarguioEspeciales(int empresa, int numeroOV, int codigoBodega, int numeroCarguio);
	public VecmarDTO obtieneVecmar(int empresa, int tipoMov, int fecha, int numero);
	public int buscarCarguioTransp(int rutChofer, String digChofer, int numeroCarguio);
	public int contarCarguiosTransp(int rutChofer, String digChofer, int numeroCarguio);
	public List listarCarguiosTranspRezagados(int rutChofer, String digChofer);
	public int obtieneCarguioTransp(int rutChofer, String digChofer, int bodega, int numeroCarguio);
	public List listarCarguiosTranspRezagados(int rutChofer, String digChofer, int numeroCarguio);
	public List listarCarguiosTranspCorreos(int rutChofer, String digChofer, int numeritodecarguio);
	public int buscarCarguioTranspBodega(int rutChofer, String digChofer, int numerocarguio );
	public String obtieneEstadoCarguioCab(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, String patente);
	public int obtieneCarguioyOrdenes(int empresa, int numeroCarguio, int codBodega, int numeroOV, String patente, int rutClie);
	public int actualizarestadoDetalleCarguioTerminado(int empresa, int bodega, int numeroCarguio, int numeroOV, int rutClie, int swer);
	public int actualizarestadoDetalleCarguio(int empresa, int bodega, int numeroCarguio, String estado, int numeroOV);
	public String verificaEstadoCarguioTransp(int codEmpresa, int numCarguio, String patente, int codBodega, int numOV, int rutCli);
	public String verificaEstadoXOCL1CarguioTransp(int codEmpresa, int numCarguio, String patente, int codBodega, int numOV, int rutCli);
}
