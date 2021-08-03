package cl.caserita.dto;

import java.io.Serializable;

public class EncriptacionDTO implements Serializable {

	private String usuario;
	private String claveEncriptada;
	public String getClaveEncriptada() {
		return claveEncriptada;
	}
	public void setClaveEncriptada(String claveEncriptada) {
		this.claveEncriptada = claveEncriptada;
	}
}
