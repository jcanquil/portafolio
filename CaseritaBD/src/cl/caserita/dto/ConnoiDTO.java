package cl.caserita.dto;

import java.io.Serializable;

public class ConnoiDTO implements Serializable {

	private String tipoNota;
	private int numNota;
	private int fechaNota;
	private int correlativo;
	private int codImpto;
	private int montoImpuesto;
	private int tasaImpto;
	
	public int getTasaImpto() {
		return tasaImpto;
	}
	public void setTasaImpto(int tasaImpto) {
		this.tasaImpto = tasaImpto;
	}
	public String getTipoNota() {
		return tipoNota;
	}
	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}
	public int getNumNota() {
		return numNota;
	}
	public void setNumNota(int numNota) {
		this.numNota = numNota;
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
	public int getCodImpto() {
		return codImpto;
	}
	public void setCodImpto(int codImpto) {
		this.codImpto = codImpto;
	}
	public int getMontoImpuesto() {
		return montoImpuesto;
	}
	public void setMontoImpuesto(int montoImpuesto) {
		this.montoImpuesto = montoImpuesto;
	}
	
	
}
