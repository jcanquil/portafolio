package cl.caserita.dto;

import java.io.Serializable;
import java.util.List;

public class CarguiocTranspDTO implements Serializable{
	
	private int 	numeroCarguio;
	private List 	ordenes;
	
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public List getOrdenes() {
		return ordenes;
	}
	public void setOrdenes(List ordenes) {
		this.ordenes = ordenes;
	}

}
