package cl.caserita.dao.iface;

import cl.caserita.dto.EnvdmailDTO;

public interface EnvdmailDAO {

	public EnvdmailDTO rescataDocumentos(int empresa, int codigo, int vendedor);
}
