package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedfaltDTO;

public interface VedfaltDAO {
	public int generaMovimiento(VedfaltDTO vedfalt);
	public void actualizaDatosVedfalt(VecmarDTO vecmar);
	public List obtenerNohay(int empresa, int tipoMov, int fecha, int numero);
	public void eliminaDatosVedfalt(VedfaltDTO vecmar);
	public int actualizaArticuloVedfalt(VedfaltDTO vedfalt);
}
