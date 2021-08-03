package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DetordTranspDTO;
import cl.caserita.dto.DetordTranspNcpDTO;

public interface DetordDAO {

	public List listaDetalleOrden(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente);
	public int actualizarestadoDetalleOV(int empresa, int bodega, int numeroOV, String estado);
	public int actualizarestadoDetalleOVCantidad(int empresa, int bodega, int numeroOV, String estado);
	public int actualizarestadoDetalleOVCantidades(int empresa, int bodega, int numeroOV, String estado);
	public int insertaDetalleOV(DetordDTO detord);
	public int actualizarestadoDetalle(DetordDTO dto);
	public int eliminaDetalle(DetordDTO dto);
	public int actualizarDocumento(int empresa, int bodega, int numeroOV, int numeroDocumento);
	public List<DetordTranspDTO> obtieneTotalesdetalle(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente, int codigoArti, int correla);
	public DetordDTO buscaOVCarguio(DetordDTO dto);
	public int actualizaUnidadesOV(DetordDTO dto);
	public DetordDTO buscaInternoOVFact(DetordDTO dto);
	public int LiberaOVMapas(int empresa, int bodega, String estado, String estadoNuevo);
	public int actualizaUnidOVVTAMayorista(DetordDTO dto);
	public List<DetordTranspNcpDTO> obtieneTotalesdetalleNumdoc(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente, int codigoArti, int cantidad);
	public int recuperaOrdenVenta(int empresa, int bodega, int numeroInterno, int rutCliente);
	public List<DetordTranspNcpDTO> obtieneTotalesdetalleNumdoc2(int codigoEmpresa, int numeroOV, int codigoBodega, int rutCliente, int codigoArti, int cantidad);

}
