package cl.caserita.dao.iface;

import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.VecmarDTO;

public interface VecmarDAO {

	public VecmarDTO obtenerDatosVecmar(int empresa, int codMovto, int fechaMovto, int numDocumento, ClmcliDTO clmcli);
	public void actualizaVecmar(int empresa, int codMovto, int fechaMovto, int numDocumento);
	public int generaCorrelativoVisual(int empresa, int codigo, int fecha, int numero, int correlativo, String timbre,String estado,String ruta, String xml);
	public void actualizaVecmarGuias(int empresa, int codigo, int fecha, int numero, int numDOcEle);
	public int generaMovimiento(VecmarDTO vecmar);
	public VecmarDTO obtenerDatosVecmarMer(int empresa, int codMovto, int fechaMovto, int numDocumento);
	public int actualizaDisponibilidadImpresion(int empresa, int codigo, int fecha, int numero, String estado);
	public VecmarDTO obtenerDatosVecmarMermasWMS (int empresa, int tipoMovto, int numdoc);
	public void actualizaVecmarMerma(int empresa, int codigo, int fecha, int numero, int neto, int total);
	public VecmarDTO buscarIngresodeOC(VecmarDTO dto);
	public int generaMovimientoCobro(VecmarDTO vecmar);
	public void actualizaSwitchVecmar(int empresa, int codigo, int fecha, int numero, int swi);
	public void actualizaDatosVecmar(VecmarDTO vecmar);
	public void actualizaVecmarGuiaOT(int empresa, int codigo, int fecha, int numeroInterno, int nroGuia);
	public VecmarDTO buscarIngresodeOT(VecmarDTO dto);
	public void actualizaVecmarSwitch(int empresa, int codigo, int fecha, int numero, String switchPro);
	public String recuperaTimbre();
}
