package cl.caserita.dto;

import java.io.Serializable;

public class ErrorTransportistaDTO implements Serializable {
	
	private int numeroCarguio;
	private String descripcionError;
	
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public String getDescripcionError() {
		return descripcionError;
	}
	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	
	

}
