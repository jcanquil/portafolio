package cl.caserita.dto;
import java.io.Serializable;

public class CargonwDTO implements Serializable{

	private int codEmpresa;
	private int numeroCarguio;
	private String patente;
	private int codBodega;
	private int codArticulo;
	private String dvArticulo;
	private int cantidad;
	private int fechaExpiracion;
	
	
	public int getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(int fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	public int getCodEmpresa() {
		return codEmpresa;
	}
	public void setCodEmpresa(int codEmpresa) {
		this.codEmpresa = codEmpresa;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public int getCodBodega() {
		return codBodega;
	}
	public void setCodBodega(int codBodega) {
		this.codBodega = codBodega;
	}
	public int getCodArticulo() {
		return codArticulo;
	}
	public void setCodArticulo(int codArticulo) {
		this.codArticulo = codArticulo;
	}
	public String getDvArticulo() {
		return dvArticulo;
	}
	public void setDvArticulo(String dvArticulo) {
		this.dvArticulo = dvArticulo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
