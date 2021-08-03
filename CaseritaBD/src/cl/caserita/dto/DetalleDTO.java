package cl.caserita.dto;
import java.io.Serializable;

public class DetalleDTO implements Serializable{

	private int numeroLinea;
	private String codigoArticulo;
	private String descripcion;
	private int cantidad;
	private double precioNeto;
	private double precioBruto;
	private int montoNeto;
	private double montoDescuento;
	
	
	public double getMontoDescuento() {
		return montoDescuento;
	}
	public void setMontoDescuento(double montoDescuento) {
		this.montoDescuento = montoDescuento;
	}
	public int getNumeroLinea() {
		return numeroLinea;
	}
	public void setNumeroLinea(int numeroLinea) {
		this.numeroLinea = numeroLinea;
	}
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecioNeto() {
		return precioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		this.precioNeto = precioNeto;
	}
	public double getPrecioBruto() {
		return precioBruto;
	}
	public void setPrecioBruto(double precioBruto) {
		this.precioBruto = precioBruto;
	}
	public int getMontoNeto() {
		return montoNeto;
	}
	public void setMontoNeto(int montoNeto) {
		this.montoNeto = montoNeto;
	}
	
	
	
}
