package cl.caserita.dto;
import java.io.Serializable;

public class CarswmsDTO implements Serializable {

	private int codigoEmpresa;
	private int codigoTipoSalida;
	private int fechaProceso;
	private int horaProceso;
	private String archivoXML;
	private int numeroCarguio;
	private int transferenciaNumeroCarguio;
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getCodigoTipoSalida() {
		return codigoTipoSalida;
	}
	public void setCodigoTipoSalida(int codigoTipoSalida) {
		this.codigoTipoSalida = codigoTipoSalida;
	}
	public int getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(int fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public int getHoraProceso() {
		return horaProceso;
	}
	public void setHoraProceso(int horaProceso) {
		this.horaProceso = horaProceso;
	}
	public String getArchivoXML() {
		return archivoXML;
	}
	public void setArchivoXML(String archivoXML) {
		this.archivoXML = archivoXML;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public int getTransferenciaNumeroCarguio() {
		return transferenciaNumeroCarguio;
	}
	public void setTransferenciaNumeroCarguio(int transferenciaNumeroCarguio) {
		this.transferenciaNumeroCarguio = transferenciaNumeroCarguio;
	}
	
}
