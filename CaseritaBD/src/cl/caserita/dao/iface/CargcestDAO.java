package cl.caserita.dao.iface;

import cl.caserita.dto.CargcestDTO;

public interface CargcestDAO {
	
	public int insertaCargcest(CargcestDTO dto);
	public int obtieneCorrelativo(int codigoEmpresa, int numeroCarguio, String patente, int codigoBodega);
	public int existeCargcestDTO (CargcestDTO cargdto);

}
