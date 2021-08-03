package cl.caserita.dto;
import java.io.Serializable;
import java.util.List;

public class ConfirmacionCarguioDTO implements Serializable {

	private int numeroCarguio;
	private int numeroOV;
	private int fechaConfirmacion;
	private List articulos;
	private int rut;
	private String dv;
	private String camion;
	private String anden;
	private int numeroCarguioTransf;
	private int correlativoDireccion;
	private int fechaRealEmision;
	
	
	public int getFechaRealEmision() {
		return fechaRealEmision;
	}
	public void setFechaRealEmision(int fechaRealEmision) {
		this.fechaRealEmision = fechaRealEmision;
	}
	public int getCorrelativoDireccion() {
		return correlativoDireccion;
	}
	public void setCorrelativoDireccion(int correlativoDireccion) {
		this.correlativoDireccion = correlativoDireccion;
	}
	public int getNumeroCarguioTransf() {
		return numeroCarguioTransf;
	}
	public void setNumeroCarguioTransf(int numeroCarguioTransf) {
		this.numeroCarguioTransf = numeroCarguioTransf;
	}
	public String getAnden() {
		return anden;
	}
	public void setAnden(String anden) {
		this.anden = anden;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getDv() {
		return dv;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public String getCamion() {
		return camion;
	}
	public void setCamion(String camion) {
		this.camion = camion;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public int getNumeroOV() {
		return numeroOV;
	}
	public void setNumeroOV(int numeroOV) {
		this.numeroOV = numeroOV;
	}
	public int getFechaConfirmacion() {
		return fechaConfirmacion;
	}
	public void setFechaConfirmacion(int fechaConfirmacion) {
		this.fechaConfirmacion = fechaConfirmacion;
	}
	public List getArticulos() {
		return articulos;
	}
	public void setArticulos(List articulos) {
		this.articulos = articulos;
	}
	
}
