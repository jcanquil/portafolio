package cl.caserita.dao.iface;

public interface ProcedimientoDAO {

	public int procesaCalculoProcedure(String vemcvm);
	public int procesaFacturacion(String empresa, String tipoMov, String fechMov, String numDoc, String codDoc, String rut, String dv, String usuario, String tipo, String nota);
	public int migraInfo1(int fecha);
	public int migraInfo2(int fecha);
	public int migraInfo3(int fecha);
	public int migraInfo4(int fecha);
	public int migraInfo5(int fecha, int numero2);
	public int migraInfo11(int fecha);
	public int migraInfo12(int fecha);
	public int migraInfo13(int fecha);
	public int migraInfo14(int fecha);
	public int migraInfo15(int fecha, int numero2);
	public int recuperaNumeroOV(int fecha);
	public int procesaBorraFiles(String vemcvm);
	public int procesaBorraLib(String vemcvm);
	public int procesaEnviaLib(String vemcvm);
	public int recuperaNumeroOVMaximo(int fecha);
	public int procesaGuiaOT(String empresa, String codBod, String numeroCarguio, String numeroTraspaso, String correlativoOT);
	public int obtieneCorrelativo(String vemcvm);
	public int procesaPersonalizados(String empresa, String tipoMov, String fecha, String numeroDoc, String codDoc);
	public int procesaVedmarOV(String numOV, String rut, String dv, String bodega);
}
