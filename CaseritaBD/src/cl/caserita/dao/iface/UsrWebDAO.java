package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CliDistDTO;
import cl.caserita.dto.UsrWebDTO;

public interface UsrWebDAO {

	public int valida(String rut,String usuario, String password);
	public List<ClcmcoDTO> documentos();
	public int usrDistribucion (int codigo, String password);
	public int generaGenDireccion(CliDistDTO gen);
}
