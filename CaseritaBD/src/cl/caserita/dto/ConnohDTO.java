package cl.caserita.dto;

import java.io.Serializable;

public class ConnohDTO implements Serializable{

	private String tipoNota;
	private int numeroNota;
	private int fechaNota;
	private int codDocumento;
	private int numeroDocumento;
	private int rutCliente;
	private String divCliente;
	private int codigoMovimiento;
	private int codigoBodega;
	private int codigoVendedor;
	private String nombreCliente;
	private int montoTotal;
	private int montoNeto;
	private int montoIva;
	private int montoImptoAdicional;
	private String estado;
	private String responsableNota;
	
	private int 	cantidadLineas;
	private double 	totalCosto;
	private double 	totalCostoNeto;
	private int 	totalDescuento;
	private int 	totalDescuentoNeto;
	private int 	montoExento;
	
	
	
	public int getCantidadLineas() {
		return cantidadLineas;
	}
	public void setCantidadLineas(int cantidadLineas) {
		this.cantidadLineas = cantidadLineas;
	}
	public double getTotalCosto() {
		return totalCosto;
	}
	public void setTotalCosto(double totalCosto) {
		this.totalCosto = totalCosto;
	}
	public double getTotalCostoNeto() {
		return totalCostoNeto;
	}
	public void setTotalCostoNeto(double totalCostoNeto) {
		this.totalCostoNeto = totalCostoNeto;
	}
	public int getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(int totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public int getTotalDescuentoNeto() {
		return totalDescuentoNeto;
	}
	public void setTotalDescuentoNeto(int totalDescuentoNeto) {
		this.totalDescuentoNeto = totalDescuentoNeto;
	}
	public int getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(int montoExento) {
		this.montoExento = montoExento;
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
	public int getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(int codDocumento) {
		this.codDocumento = codDocumento;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(int rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getDivCliente() {
		return divCliente;
	}
	public void setDivCliente(String divCliente) {
		this.divCliente = divCliente;
	}
	public int getCodigoMovimiento() {
		return codigoMovimiento;
	}
	public void setCodigoMovimiento(int codigoMovimiento) {
		this.codigoMovimiento = codigoMovimiento;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getCodigoVendedor() {
		return codigoVendedor;
	}
	public void setCodigoVendedor(int codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public int getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(int montoTotal) {
		this.montoTotal = montoTotal;
	}
	public int getMontoNeto() {
		return montoNeto;
	}
	public void setMontoNeto(int montoNeto) {
		this.montoNeto = montoNeto;
	}
	public int getMontoIva() {
		return montoIva;
	}
	public void setMontoIva(int montoIva) {
		this.montoIva = montoIva;
	}
	public int getMontoImptoAdicional() {
		return montoImptoAdicional;
	}
	public void setMontoImptoAdicional(int montoImptoAdicional) {
		this.montoImptoAdicional = montoImptoAdicional;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getResponsableNota() {
		return responsableNota;
	}
	public void setResponsableNota(String responsableNota) {
		this.responsableNota = responsableNota;
	}
	
	
}
