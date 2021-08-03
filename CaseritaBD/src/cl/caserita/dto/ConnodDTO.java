package cl.caserita.dto;

import java.io.Serializable;
import java.util.List;

public class ConnodDTO implements Serializable{
	
	private String tipoNota;
	private int numeroNota;
	private int fechaNota;
	private  int correlativo;
	private int codArticulo;
	private String digArticulo;
	private String descripcion;
	private int cantidad;
	private String formato;
	private double precioUnitario;
	private int totalLinea;
	private int totalNeto;
	private int cantUnidades;
	private List impuesto;
	private int descLinea;
	private int montoExento;
	private int codigoEmpresa;
	
	
	private double precioNeto;
	public double getPrecioNeto() {
		return precioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		this.precioNeto = precioNeto;
	}
	public double getCostoArticulo() {
		return costoArticulo;
	}
	public void setCostoArticulo(double costoArticulo) {
		this.costoArticulo = costoArticulo;
	}
	public double getCostoNetoUnitario() {
		return costoNetoUnitario;
	}
	public void setCostoNetoUnitario(double costoNetoUnitario) {
		this.costoNetoUnitario = costoNetoUnitario;
	}
	public double getCostoTotalNeto() {
		return costoTotalNeto;
	}
	public void setCostoTotalNeto(double costoTotalNeto) {
		this.costoTotalNeto = costoTotalNeto;
	}
	public int getTotalDescuentoNeto() {
		return totalDescuentoNeto;
	}
	public void setTotalDescuentoNeto(int totalDescuentoNeto) {
		this.totalDescuentoNeto = totalDescuentoNeto;
	}
	public int getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(int totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public double getMontoNeto() {
		return montoNeto;
	}
	public void setMontoNeto(double montoNeto) {
		this.montoNeto = montoNeto;
	}
	private double costoArticulo;
	private double costoNetoUnitario;
	private double costoTotalNeto;
	private int    totalDescuentoNeto;
	private int    totalDescuento;
	private double montoNeto;
	
	
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(int montoExento) {
		this.montoExento = montoExento;
	}
	public int getDescLinea() {
		return descLinea;
	}
	public void setDescLinea(int descLinea) {
		this.descLinea = descLinea;
	}
	public List getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(List impuesto) {
		this.impuesto = impuesto;
	}
	public String getTipoNota() {
		return tipoNota;
	}
	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}
	public int getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(int numeroNota) {
		this.numeroNota = numeroNota;
	}
	public int getFechaNota() {
		return fechaNota;
	}
	public void setFechaNota(int fechaNota) {
		this.fechaNota = fechaNota;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public int getCodArticulo() {
		return codArticulo;
	}
	public void setCodArticulo(int codArticulo) {
		this.codArticulo = codArticulo;
	}
	public String getDigArticulo() {
		return digArticulo;
	}
	public void setDigArticulo(String digArticulo) {
		this.digArticulo = digArticulo;
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
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public int getTotalLinea() {
		return totalLinea;
	}
	public void setTotalLinea(int totalLinea) {
		this.totalLinea = totalLinea;
	}
	public int getTotalNeto() {
		return totalNeto;
	}
	public void setTotalNeto(int totalNeto) {
		this.totalNeto = totalNeto;
	}
	public int getCantUnidades() {
		return cantUnidades;
	}
	public void setCantUnidades(int cantUnidades) {
		this.cantUnidades = cantUnidades;
	}
	
	
}
