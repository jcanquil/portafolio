package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.Conar1DTO;

public interface Conar1DAO {

	public int generaConar1(Conar1DTO conar1);
	public List recuperaImpuesto(int codDoc, int rut, String dv, int numeroDoc);
	public List acumuladoImpuestos(int ano, int mes, int codDocumento);
	public Conar1DTO recuperaImpuestoIva(int codDoc, int rut, String dv, int numeroDoc);
}
