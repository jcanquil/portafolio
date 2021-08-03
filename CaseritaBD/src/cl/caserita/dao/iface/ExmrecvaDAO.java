package cl.caserita.dao.iface;

import java.util.List;

public interface ExmrecvaDAO {
	public List listaExmrecva(int empresa, int numoc, int tipDocto, int numeroDocto, int fechaDocto, int codigoBodega, String tipo);
}
