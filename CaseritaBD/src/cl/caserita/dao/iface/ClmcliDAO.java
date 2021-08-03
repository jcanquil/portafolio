package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;

public interface ClmcliDAO {
	
	public ClmcliDTO recuperaCliente(String rut, String DV);
	public String recuperaComuna(int region, int ciudad, int comuna);
	public int generaCliente(ClmcliDTO clmcli);
	public ClmcliDTO recuperaCliente2(String rut);
	public String recuperaCiudad(int region, int ciudad);
	public int generaIntegracionGeoref(ClidirDTO clmcli);
	public ClmcliDTO recuperaCliente(int rut, String dv);
	public List recuperaAllCliente();
	public ClmcliDTO recuperaClienteDireccion(int rut, String dv, int correlativo);
	public int generaDatosCliente(ClmcliDTO clmcli);
	public String recuperaRegion(int region);
}
