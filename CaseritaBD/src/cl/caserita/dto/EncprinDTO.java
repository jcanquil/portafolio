package cl.caserita.dto;
import java.io.Serializable;

public class EncprinDTO implements Serializable {

	private int rutCliente;
	private String dvCliente;
	private int codigoEncuesta;
	private String descripcionEncuesta;
	private String estadoEncuesta;
	private String tipoEncuesta;
	
	
	public String getTipoEncuesta() {
		return tipoEncuesta;
	}
	public void setTipoEncuesta(String tipoEncuesta) {
		this.tipoEncuesta = tipoEncuesta;
	}
	public int getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(int rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getDvCliente() {
		return dvCliente;
	}
	public void setDvCliente(String dvCliente) {
		this.dvCliente = dvCliente;
	}
	public int getCodigoEncuesta() {
		return codigoEncuesta;
	}
	public void setCodigoEncuesta(int codigoEncuesta) {
		this.codigoEncuesta = codigoEncuesta;
	}
	public String getDescripcionEncuesta() {
		return descripcionEncuesta;
	}
	public void setDescripcionEncuesta(String descripcionEncuesta) {
		this.descripcionEncuesta = descripcionEncuesta;
	}
	public String getEstadoEncuesta() {
		return estadoEncuesta;
	}
	public void setEstadoEncuesta(String estadoEncuesta) {
		this.estadoEncuesta = estadoEncuesta;
	}
	
	
}
