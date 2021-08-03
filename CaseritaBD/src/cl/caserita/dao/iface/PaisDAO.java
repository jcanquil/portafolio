package cl.caserita.dao.iface;

import java.util.List;

public interface PaisDAO {

	public List region();
	public List ciudad(int region);
	public List comuna(int region, int ciudad);
	
}
