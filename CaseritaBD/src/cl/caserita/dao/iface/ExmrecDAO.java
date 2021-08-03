package cl.caserita.dao.iface;

import cl.caserita.dto.ExmrecDTO;

public interface ExmrecDAO {

	public int generaEncabezado(ExmrecDTO dto);
	public ExmrecDTO buscaEncabezado(ExmrecDTO dto);
	
	public int actualizaEncabezado(ExmrecDTO dto);
	public ExmrecDTO buscaOcRececp(String idCamion);
	
}
