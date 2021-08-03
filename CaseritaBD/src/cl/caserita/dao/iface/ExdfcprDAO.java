package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ExdfcprDTO;

public interface ExdfcprDAO {
	public int buscaFolioExdfcr(int empresa,int numeroDoc, int rut, String dv, int codigoDoc);
	public List buscaDocumentosSinPDF();
	public int generaFacturas(int empresa,String numOrden, int numeroDoc, int rut, String dv, int codigoDoc, int fecha, double neto, double impuesto, double impuAdic, double exento,double total,String tipoIngreso, String url, String razon, String direccion);
	public int actualizaPDF(int empresa, int numeroDoc, int rut,  int codigoDoc, String url);
	public int actualizaEstado(int empresa, int rutProv, String dvProv, int codDocto, int numeroDoc, String estado);
	public ExdfcprDTO buscaDocumentoExdfcr(int empresa, int numeroOC, int numeroDoc, int rut, String dv);
	public int actualizaNumeroOrden(int empresa, int rutProv, String dvProv, int numeroDoc, String numeroOC);
}
