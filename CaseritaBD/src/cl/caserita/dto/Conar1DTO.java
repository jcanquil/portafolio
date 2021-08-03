package cl.caserita.dto;
import java.io.Serializable;

public class Conar1DTO implements Serializable{

	private int codigoDocumento;
	private int rut;
	private String dv;
	private int numeroDocumento;
	private int fechaDocumento;
	private int codigoImpuesto;
	private int montoImpuesto;
	private int tasaImpuesto;
	public int getTasaImpuesto() {
		return tasaImpuesto;
	}
	public void setTasaImpuesto(int tasaImpuesto) {
		this.tasaImpuesto = tasaImpuesto;
	}
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
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
