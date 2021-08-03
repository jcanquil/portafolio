package cl.caserita.dao.iface;

import java.util.HashMap;
import java.util.List;

import cl.caserita.dto.ExdodcDTO;

public interface ExdodcDAO {
	
	public List listaExdodc(int empresa, int numoc);
	public HashMap HashMapExdodc(int empresa, int numoc);
	public ExdodcDTO buscaDetOrden(int empresa, int numeroOrden, int articulo);
	public int eliminaArticuloOrden(int empresa, int nrooc, int linea, int articulo);
	public int actualizarFormatoArticulo(int empresa, int nrooc, int linea, int articulo, double cantunid);
	public ExdodcDTO recuperEstadoparaCab(int empresa, int numoc);
	public ExdodcDTO buscaDetalleOrden(int empresa, int numeroOrden, int articulo);
}
