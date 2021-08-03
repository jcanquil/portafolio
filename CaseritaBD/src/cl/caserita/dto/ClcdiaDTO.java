package cl.caserita.dto;
import java.io.Serializable;

public class ClcdiaDTO implements Serializable{

	private int codDocumento;
	private String rutCliente;
	private String dvCliente;
	private int fechaMovimiento;
	private int horaMovimiento;
	private int codigoImpuesto;
	private int montoImpuesto;
	private double impuesto;
	
	public double getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(double impuesto) {
		this.impuesto = impuesto;
	}
	public int getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(int codDocumento) {
		this.codDocumento = codDocumento;
	}
	public String getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(String rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getDvCliente() {
		return dvCliente;
	}
	public void setDvCliente(String dvCliente) {
		this.dvCliente = dvCliente;
	}
	public int getFechaMovimiento() {
		return fechaMovimiento;
	}
	public void setFechaMovimiento(int fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	public int getHoraMovimiento() {
		return horaMovimiento;
	}
	public void setHoraMovimiento(int horaMovimiento) {
		this.horaMovimiento = horaMovimiento;
	}
	public int getCodigoImpuesto() {
		return codigoImpuesto;
	}
	public void setCodigoImpuesto(int codigoImpuesto) {
		this.codigoImpuesto = codigoImpuesto;
	}
	public int getMontoImpuesto() {
		return montoImpuesto;
	}
	public void setMontoImpuesto(int montoImpuesto) {
		this.montoImpuesto = montoImpuesto;
	}
	
	
}
