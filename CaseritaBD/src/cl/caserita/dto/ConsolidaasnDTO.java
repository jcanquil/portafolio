package cl.caserita.dto;
import java.io.Serializable;
public class ConsolidaasnDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroCarguio;
	private int bodega;
	private String patente;
	private int codigoArticulo;
	private String dvArticulo;
	private int cantidad;
	private int cantidadConfirmada;
	private int cantidadDiferencia;
	private double PrecioBruto;
	private double PrecioNeto;
	
	public double getPrecioBruto() {
		return PrecioBruto;
	}
	public void setPrecioBruto(double precioBruto) {
		PrecioBruto = precioBruto;
	}
	public double getPrecioNeto() {
		return PrecioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		PrecioNeto = precioNeto;
	}
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public int getBodega() {
		return bodega;
	}
	public void setBodega(int bodega) {
		this.bodega = bodega;
	}
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
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
	public int getCantidadConfirmada() {
		return cantidadConfirmada;
	}
	public void setCantidadConfirmada(int cantidadConfirmada) {
		this.cantidadConfirmada = cantidadConfirmada;
	}
	public int getCantidadDiferencia() {
		return cantidadDiferencia;
	}
	public void setCantidadDiferencia(int cantidadDiferencia) {
		this.cantidadDiferencia = cantidadDiferencia;
	}
	
	
	
	
}
