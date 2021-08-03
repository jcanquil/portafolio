package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ConarcDTO;
import cl.caserita.dto.LibroTotalesDTO;

public interface ConarcDAO {

	public int buscaDocumento(int numeroDocumento, int codDocumento, int fechaDoc, int rutProveedor, String dv);
	public int generaDoc(ConarcDTO conarc);
	public int buscaFolioDoc(int empresa, int ano, int mes, int codDocumento);
	//public int generaFacturas(int codigoEmpresa,String numOrden, int numeroDoc, int rut, String dv, int codigoDoc);
	public int generaFacturas(int empresa,String numOrden, int numeroDoc, int rut, String dv, int codigoDoc, int fecha, double neto, double impuesto, double impuAdic, double exento,double total, String tipoIngreso, String url, String razon, String direccion);
	public List buscaDocumentos(int codigo,int ano, int mes);
	public LibroTotalesDTO recuperaTotalesPorDocumento(int ano, int mes, int codDocumento);
	public LibroTotalesDTO recuperaTotalesIva(int ano, int mes, int codDocumento, LibroTotalesDTO dto);
	public int generaImpuestoFacturas(int empresa,String numOrden, int numeroDoc, int rut, String dv, int codigoDoc, double codImpto, double total);
	public int buscaFolioExdfcr(int empresa, int numeroDoc, int rut, String dv, int codigoDoc);
	public int buscaDocumentoContabilizado(int numeroDocumento, int codDocumento, int rut, String dv);
	public int actualizaPDF(int empresa, int numeroDoc, int rut,  int codigoDoc, String url);
	public List buscaDocumentosSinPDF();
	public ConarcDTO buscaTotalesDocumento(int empresa, int rut, String dv, int numeroDocto);
	public int buscaFolioDisponible(int empresa, int ano, int mes);
	public int actualizaFolioLibro(int empresa, int tipodocto, int nrodocumento, int rutprov, String dvprov, int folio, int ano, int mes);
}
