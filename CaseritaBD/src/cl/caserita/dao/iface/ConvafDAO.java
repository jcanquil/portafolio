package cl.caserita.dao.iface;

import cl.caserita.dto.ConvafDTO;

public interface ConvafDAO {
	public ConvafDTO lista(int codtipdocto, int numerodocumento, int fechadocumento, int rutprov, String dvprov);
	public int eliminaEnlaceContable(ConvafDTO dto);
}
