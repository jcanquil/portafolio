package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;

public interface ClidirDAO {

	public int generaDireccion(ClidirDTO clidir);
	public int generaClidira(ClidiraDTO clidira);
	public int obtieneCorrelativo(int rut, String dv);
	public int obtieneCorrelativo2(int rut, String dv);
	public int actualizaDireccionClidir(ClidirDTO clidir);
	public int actualizaClidira(ClidiraDTO clidira);
	public List obtieneDirecciones(int rut, String dv);
	public int obtieneCorrelativoFacturacion(int rut, String dv);
	public ClidirDTO obtieneDireccionCliente(int rut, int correlativo);
	public int actualizaClidiraLatLng(ClidiraDTO clidira);
    public ClidirDTO obtieneDireccion(int rut,  int correlativo);
    public String recuperaComuna(int region, int ciudad, int comuna);
    public ClidirDTO recuperaDireccionesCorrrelativo(int rut, String dv, int correlativo);
}
