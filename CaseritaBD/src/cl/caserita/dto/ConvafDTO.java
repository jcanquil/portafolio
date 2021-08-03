package cl.caserita.dto;

import java.io.Serializable;

public class ConvafDTO implements Serializable{
	
	private int codigoDocumento;
	private int numeroDocumento;
	private int fechaDocumento;
	private int rutProveedor;
	private String dvProveedor;
	private int numeroOrden;
	private int numeroLiquidacion;
	private int fechaContabiliza;
	private int bodega;
	private int codigoTransaccion;
	
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	public int getRutProveedor() {
		return rutProveedor;
	}
	public void setRutProveedor(int rutProveedor) {
		this.rutProveedor = rutProveedor;
	}
	public String getDvProveedor() {
		return dvProveedor;
	}
	public void setDvProveedor(String dvProveedor) {
		this.dvProveedor = dvProveedor;
	}
	public int getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public int getNumeroLiquidacion() {
		return numeroLiquidacion;
	}
	public void setNumeroLiquidacion(int numeroLiquidacion) {
		this.numeroLiquidacion = numeroLiquidacion;
	}
	public int getFechaContabiliza() {
		return fechaContabiliza;
	}
	public void setFechaContabiliza(int fechaContabiliza) {
		this.fechaContabiliza = fechaContabiliza;
	}
	public int getBodega() {
		return bodega;
	}
	public void setBodega(int bodega) {
		this.bodega = bodega;
	}
	public int getCodigoTransaccion() {
		return codigoTransaccion;
	}
	public void setCodigoTransaccion(int codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}
}
