package cl.caserita.dao.iface;

import cl.caserita.dto.DocgenelDTO;

public interface DocGenelDAO {
	public String buscaEndPoint();
	public DocgenelDTO recuperaFolio(int empresa, int tipoMov, int fecha, int numero);
	public void actualizaDocgenelGuiaOT(int empresa, int numeroInterno, int bodega, int nroGuia, int numeroCarguio);
}
