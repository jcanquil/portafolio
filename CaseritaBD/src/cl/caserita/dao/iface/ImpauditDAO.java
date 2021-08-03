package cl.caserita.dao.iface;

import cl.caserita.dto.ImpauditDTO;

public interface ImpauditDAO {
	public ImpauditDTO buscaColaImpresionAudit(int codemp, int codbod);
}
