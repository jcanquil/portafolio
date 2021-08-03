package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.RespquadDTO;

public interface RespquadDAO {
	
	public int insertaRespquad(RespquadDTO dto);
	public int actualizaRespquad(RespquadDTO dto);
	public int buscaRespquad(RespquadDTO dto);

}
