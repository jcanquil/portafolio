package cl.caserita.dto;

import java.io.Serializable;

public class BdgcorDTO implements Serializable {

	private int bodega;
	private int numAtencion;
	public int getBodega() {
		return bodega;
	}
	public void setBodega(int bodega) {
		this.bodega = bodega;
	}
	public int getNumAtencion() {
		return numAtencion;
	}
	public void setNumAtencion(int numAtencion) {
		this.numAtencion = numAtencion;
	}
	
	
}
