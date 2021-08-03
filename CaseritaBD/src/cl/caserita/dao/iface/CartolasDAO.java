package cl.caserita.dao.iface;

import java.util.List;

public interface CartolasDAO {

	public List listaCartolasCheque();
	public int actualizaConcilia(int correlativo, int correlativoConciliado);
	public int actualizaConciliaParcial(int correlativo, int correlativoConciliado);
	public List listaCartolasEfectivo();
}
