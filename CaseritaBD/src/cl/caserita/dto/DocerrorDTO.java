package cl.caserita.dto;

import java.io.Serializable;

public class DocerrorDTO implements Serializable{

	private int empresa;
	private int tipoMov;
	private int fecha;
	private int numero;
	private int numeroDocumento;
	private String urlXML;
	private String urlPDF;
	private int codigoDocumento;
	
	
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public int getEmpresa() {
		return empresa;
	}
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}
	public int getTipoMov() {
		return tipoMov;
	}
	public void setTipoMov(int tipoMov) {
		this.tipoMov = tipoMov;
	}
	public int getFecha() {
		return fecha;
	}
	public void setFecha(int fecha) {
		this.fecha = fecha;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getUrlXML() {
		return urlXML;
	}
	public void setUrlXML(String urlXML) {
		this.urlXML = urlXML;
	}
	public String getUrlPDF() {
		return urlPDF;
	}
	public void setUrlPDF(String urlPDF) {
		this.urlPDF = urlPDF;
	}
	
	
}
