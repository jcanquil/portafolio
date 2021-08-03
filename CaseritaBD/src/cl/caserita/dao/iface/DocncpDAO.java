package cl.caserita.dao.iface;

import cl.caserita.dto.DocncpDTO;

public interface DocncpDAO {
	
	public int insertaDocncp(DocncpDTO dto);
	public DocncpDTO buscaDocncp(int empresa, String tipoNota, int numeroNota, int codigoBodega);
	public DocncpDTO obtenerDatosChofer(int empresa, String tipo, int nroTraspaso, int rutEmpTrans, String dvEmpTrans);
	public String obtenerNcpes(int codEmpresa, int codBodega, int numCarguio);
	public int buscaDocncpDTO (DocncpDTO docdto);
	public int eliminaDocncpDTO (DocncpDTO docdto);
	public int eliminaDocncpTranspDTO (DocncpDTO docdto);
}
