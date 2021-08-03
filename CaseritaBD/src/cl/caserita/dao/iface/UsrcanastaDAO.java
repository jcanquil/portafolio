package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.AdicionalesDTO;
import cl.caserita.dto.UsuarioCanastaDTO;

public interface UsrcanastaDAO {

	public int generaUsuario(UsuarioCanastaDTO usuario);
	public String validaUsuario(int rutCliente,int rut, String digito, String password);
	public List obtieneReporteUsuariosConDespacho(int rutCliente);
	public List obtieneReporteUsuariossinDespacho(int rutCliente);
	public int actualizaUsuario(UsuarioCanastaDTO usuario);
	public UsuarioCanastaDTO obtieneUsuariosConDespacho(int rutCliente, int rutPersonal);
	public List obtieneReporteUsuariosTodos(int rutCliente);
	public int actualizaAccesoUsuario(UsuarioCanastaDTO usuario);
	public int generaHijos(AdicionalesDTO adicionales);
	public int actualizaHijos(AdicionalesDTO adicionales);
	public List obtieneHijos(int rutCliente, int rut);
	public int obtieneultimoCorrelativo(int rutCliente, int rut);
	public List obtieneActualizacionCliente();
	public int actualizaBDD(UsuarioCanastaDTO usuario);
	public int eliminaHijo(int rutCliente, int rut, int correlativo);
}
