package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.OrdTranspDTO;
import cl.caserita.dto.OrdTranspNcpDTO;
import cl.caserita.dto.OrdvtaDTO;

public interface OrdvtaDAO {

	public List obtieneOrdenes(int empresa);
	public int actualizarestadoOV(OrdvtaDTO orden);
	public int actualizarestadoOVIndividuales(OrdvtaDTO orden);
	public List obtieneOrdenesActualizaLatLon(int empresa);
	public int insertaOV(OrdvtaDTO orden);
	public int obtieneCorrelativo(int bodega);
	public int procesaCalculoProcedure(int empresa, int bodega, int numeroOV, int rutclie, String dv);
	public OrdvtaDTO obtieneOrdenVenta(int empresa, int numeroOrden, int codigoBodega, int correlativoDireccion);
	public OrdvtaDTO obtieneOrdenVentaFacturacion(int empresa, int numeroOrden, int codigoBodega);
	public OrdTranspNcpDTO obtieneTotalesorden(int empresa, int bodega, int numeroOV);
	public int LiberaOVMapas(int empresa, int bodega, String estado, String estadoNuevo);
	public OrdvtaDTO obtieneOrdenVentaEspecial(int empresa, int numeroOrden, int codigoBodega, int correlativoDireccion, int carguio);
	public int insertaOVProveedor(OrdvtaDTO orden);
	public int insertaAdicionalesOV(OrdvtaDTO orden);
	public int obtieneCorrelativoWEB(int bodega);
	public OrdTranspNcpDTO obtieneTotalesordenNumdoc(int codigoEmpresa, int bodega, int numeroDoc, int tipoDoc, int numcar, int rutChofer, String patente);
	}
