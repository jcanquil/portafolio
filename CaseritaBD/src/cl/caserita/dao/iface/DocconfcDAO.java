package cl.caserita.dao.iface;

import cl.caserita.dto.DocconfcDTO;

public interface DocconfcDAO {

	public int insertaDocumentoCarguio(DocconfcDTO gen);
	public DocconfcDTO recuperaDocumento(DocconfcDTO gen);
}
