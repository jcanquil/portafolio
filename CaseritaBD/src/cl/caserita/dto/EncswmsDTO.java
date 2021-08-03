package cl.caserita.dto;
import java.io.Serializable;

public class EncswmsDTO implements Serializable{

	private int codigoEmpresa;
	private int codigoTipoSalida;
	private String descripcionTipoSalida;
	private int fechaProceso;
	private int horaProceso;
	private String archivoXML;
	private int fechaRealGeneracion;
	
	public int getFechaRealGeneracion() {
		return fechaRealGeneracion;
	}
	public void setFechaRealGeneracion(int fechaRealGeneracion) {
		this.fechaRealGeneracion = fechaRealGeneracion;
	}
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
	public String getDescripcionTipoSalida() {
		return descripcionTipoSalida;
	}
	public void setDescripcionTipoSalida(String descripcionTipoSalida) {
		this.descripcionTipoSalida = descripcionTipoSalida;
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
	
}
