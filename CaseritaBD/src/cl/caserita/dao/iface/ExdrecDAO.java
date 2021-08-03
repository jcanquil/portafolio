package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ExdrecDTO;

public interface ExdrecDAO {

	public int generaDetalle(ExdrecDTO dto);
	public ExdrecDTO buscaDetalle(int empresa, int nrooc, int fecha, int hora, int codbod, int codart);
	public int actualizaDetalle(ExdrecDTO dto);
	public ExdrecDTO buscaDetalleCobro(int empresa, int nroot, int fecha, int hora, int codbod);
	public List recuperaDetalleCompletoCobro(int empresa, int nroot, int fecha, int hora, int codbod);
}
