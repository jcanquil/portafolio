package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.UsuarioMonitoreoDTO;

public interface AdsusrDAO {

	public int validaUsuarioSyscon(String usuario, String password);
	public String obtieneLetra(String palabra);
	public List letras();
	public int actualizaLetra(String letra , String palabra);
	public String obtieneLetraEncriptada(String palabra);
	public int validaUsuarioMapas(String usuario, String password);
	public UsuarioMonitoreoDTO validaUsuarioMonitoreo(String usuario, String password);

}
