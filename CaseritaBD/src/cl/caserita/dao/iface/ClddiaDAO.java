package cl.caserita.dao.iface;

import java.util.List;

public interface ClddiaDAO {

	public List recuperaClddia(int codDocmento, int rut, String dv, int fecha, int hora, int corre);
}
