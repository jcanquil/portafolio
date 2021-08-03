package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.LibroTotalesDTO;

public interface ClcmcoDAO {

	public List obtieneClcmco(int empresa, int codigo, int rutCliente, String dv, int fecha, int numero);
	public int actualizaClcmco(int empresa, int codigo, int rutCliente, String dv, int fecha, int hora,int numero);
	public int obtieneFechaFactura(int empresa, int codigo, int rutCliente, String dv, int numero,int bodega);
	public LibroTotalesDTO totales(int empresa, int codDocumento,int año, int mes);
	public List recuperaDocumentos(int empresa, int codigoDocumento, int ano, int mes);
	public List recuperaDocumentosBodega(int empresa, int codigoDocumento, int ano, int mes, int bodega);
	public LibroTotalesDTO totalesBodega(int empresa, int codDocumento,int año, int mes, int bodega);
	public LibroTotalesDTO totalesBodegaExento(int empresa, int codDocumento,int año, int mes, int bodega);
	public LibroTotalesDTO totalesExento(int empresa, int codDocumento,int año, int mes);
	public List recuperaDocumentosExento(int empresa, int codigoDocumento, int ano, int mes);
	public List recuperaDocumentosBodegaExento(int empresa, int codigoDocumento, int ano, int mes, int bodega);
	public LibroTotalesDTO totalesBodegaNC(int empresa, int codDocumento,int año, int mes, int bodega);
	public LibroTotalesDTO totalesNC(int empresa, int codDocumento,int año, int mes);
	public List recuperaDocumentosNC(int empresa, int codigoDocumento, int ano, int mes);
	public List recuperaDocumentosBodegaNC(int empresa,int codigoDocumento, int ano, int mes, int bodega);
	public ClcmcoDTO obtieneClcmcoDTO(int empresa, int codigo, int rutCliente, String dv, int fecha, int numero);
}
