package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.TptcomDTO;

public interface TptcomDAO {

	public List allComuna();
	public TptcomDTO Comuna(int region, int comuna);
}
