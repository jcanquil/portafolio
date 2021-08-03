package cl.caserita.dao.iface;

import java.util.List;

import cl.caserita.dto.CamtraDTO;

public interface CamtraDAO {

	public List obtenerDatosCamtra(int empresa,int codigo, int fecha, int numero);
	public int actualizaCamtra(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, int numero, String usuario,String timbre, String estadoPP);
	public List obtenerDatosCamtraPendientes(int empresa, int codigo, int fechainicial, int fechaFinal);
	public int actualizaEstadoCamtra(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, String estadoPP);
	public CamtraDTO obtenerDatosCamtraNC(int empresa, int codigo, int fecha, int numero);
	public List obtenerDatosEstadoSII(int empresa, int fecha, int codDoc);
	public int actualizaEstadoCamtraSII(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, String estadoSII);
	public int actualizaEstadoCamtraReenvio(int empresa, int codigo, int numeroDoc, int fecha, int correlativo, String reenvio);
	public List obtenerDatosEstadoRechazados(int empresa, int fecha, int codDoc);
	public CamtraDTO verificaFacturacion(int empresa, int codigo, int fecha, int numero);
	public int actualizaEstadoPago(int empresa, int codigo, int numeroDoc, int fecha);
}
