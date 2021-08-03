package cl.caserita.dto;
import java.io.Serializable;

public class DimensionesDTO implements Serializable{

	private double peso;
	private double cantidadCajas;
	private double volumen;
	
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public double getCantidadCajas() {
		return cantidadCajas;
	}
	public void setCantidadCajas(double cantidadCajas) {
		this.cantidadCajas = cantidadCajas;
	}
	public double getVolumen() {
		return volumen;
	}
	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}
	
	
	
}
