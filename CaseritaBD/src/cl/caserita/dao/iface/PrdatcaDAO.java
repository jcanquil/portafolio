package cl.caserita.dao.iface;

import cl.caserita.dto.PrdatcaDTO;

public interface PrdatcaDAO {

	public PrdatcaDTO obtieneDatosDocumento(int empresa, int codTipoMovto, int fechaMovto, int numDocumento);
}
