package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public interface DocerrorDAO {

	public int insertaVecmar(VecmarDTO vecmar);
	public int insertaVedmar(VedmarDTO vedmar);
	public List listaDocumentosError();
}
