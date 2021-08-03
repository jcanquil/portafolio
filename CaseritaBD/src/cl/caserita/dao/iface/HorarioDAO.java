package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.HorarioDTO;

public interface HorarioDAO {

	public List horarios (int rutCliente);
	public HorarioDTO recuperaHorario (int rut, int correlativo);
}
