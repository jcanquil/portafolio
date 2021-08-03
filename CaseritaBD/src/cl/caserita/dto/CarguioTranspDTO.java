package cl.caserita.dto;

import java.io.Serializable;
import java.util.List;

public class CarguioTranspDTO implements Serializable{
	
	
	private int 	numeroCarguio;
	private int 	version;
	private int 	rutChofer;
	private String  dvChofer;
	private String  nombreChofer;
	private String  patente;
	private int 	fechaCarguio;
	private List 	ordenes;
	
	
	
	public List getOrdenes() {
		return ordenes;
	}
	public void setOrdenes(List ordenes) {
		this.ordenes = ordenes;
	}
	
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getRutChofer() {
		return rutChofer;
	}
	public void setRutChofer(int rutChofer) {
		this.rutChofer = rutChofer;
	}
	
	public String getDvChofer() {
		return dvChofer;
	}
	public void setDvChofer(String dvChofer) {
		this.dvChofer = dvChofer;
	}
	
	public String getNombreChofer() {
		return nombreChofer;
	}
	public void setNombreChofer(String nombreChofer) {
		this.nombreChofer = nombreChofer;
	}
	
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	
	public int getFechaCarguio() {
		return fechaCarguio;
	}
	public void setFechaCarguio(int fechaCarguio) {
		this.fechaCarguio = fechaCarguio;
	}
	
}
	
