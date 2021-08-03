package cl.caserita.dto;

import java.io.Serializable;
import java.util.List;

public class DocumentoElectronicoDTO implements Serializable{
	
	private String folio;
	private String fecha;
	private String rutProveedor;
	private String digitoProveedor;
	private String razonSocialProveedor;
	private String codDocumento;
	private String codDocumentoPP;
	
	private String neto;
	private String iva;
	private String total;
	private List impuestosAdicionales;
	private List referencias;
	private String montoExento;
	private String direccionProveedor;
	
	
	
	public String getDireccionProveedor() {
		return direccionProveedor;
	}
	public void setDireccionProveedor(String direccionProveedor) {
		this.direccionProveedor = direccionProveedor;
	}
	public String getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(String montoExento) {
		this.montoExento = montoExento;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getRutProveedor() {
		return rutProveedor;
	}
	public void setRutProveedor(String rutProveedor) {
		this.rutProveedor = rutProveedor;
	}

	public String getCodDocumentoPP() {
		return codDocumentoPP;
	}
	public void setCodDocumentoPP(String codDocumentoPP) {
		this.codDocumentoPP = codDocumentoPP;
	}
	public String getDigitoProveedor() {
		return digitoProveedor;
	}
	public void setDigitoProveedor(String digitoProveedor) {
		this.digitoProveedor = digitoProveedor;
	}
	public String getRazonSocialProveedor() {
		return razonSocialProveedor;
	}
	public void setRazonSocialProveedor(String razonSocialProveedor) {
		this.razonSocialProveedor = razonSocialProveedor;
	}
	public String getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}
	public String getNeto() {
		return neto;
	}
	public void setNeto(String neto) {
		this.neto = neto;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List getImpuestosAdicionales() {
		return impuestosAdicionales;
	}
	public void setImpuestosAdicionales(List impuestosAdicionales) {
		this.impuestosAdicionales = impuestosAdicionales;
	}
	public List getReferencias() {
		return referencias;
	}
	public void setReferencias(List referencias) {
		this.referencias = referencias;
	}
	

}
