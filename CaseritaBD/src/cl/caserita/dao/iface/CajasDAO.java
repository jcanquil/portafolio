package cl.caserita.dao.iface;

import java.util.List;

public interface CajasDAO {

	public List listaCaja();
	public int actualizaConcilia(int correlativo, int correlativoConciliado);
	public int buscaConciliadoCheque(String sucursal, int numeroCheque, String monto);
	public int buscaConciliadoParcialCheque(String sucursal, String monto);
	public int actualizaConciliaParcial(int correlativo, int correlativoConciliado);
	public int buscaConciliadoParcialEfectivo(String sucursal, String monto);
	public int buscaConciliadoEfectivo(String sucursal, int numeroCheque, String monto);
}
