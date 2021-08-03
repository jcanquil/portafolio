package cl.caserita.dao.iface;

import cl.caserita.dto.OrdvtadDTO;

public interface OrdvtadDAO {

	public OrdvtadDTO obtieneOrdenes(int empresa, int numeroOV, int bodega, int rut);
}
