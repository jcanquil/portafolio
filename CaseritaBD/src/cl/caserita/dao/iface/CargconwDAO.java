package cl.caserita.dao.iface;

import cl.caserita.dto.CargconwDTO;

public interface CargconwDAO {
	
	public int insertaCargconw(CargconwDTO dto);
	public int buscaCargconw(CargconwDTO dto);
	public int eliminaCargconw(CargconwDTO dto);
	public int eliminaCargconwTransp(CargconwDTO carcon);
	public int buscaCargconwTransp(CargconwDTO carcon);
	public int actualizaCargconw(CargconwDTO carcon);
}
