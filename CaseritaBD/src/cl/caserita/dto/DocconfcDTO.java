package cl.caserita.dto;
import java.io.Serializable;

public class DocconfcDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroCarguio;
	private String patente;
	private int codigoBodega;
	private int numeroDocumento;
	private int estado;
	private int fechaConfirmacion;
	
	
	public int getFechaConfirmacion() {
		return fechaConfirmacion;
	}
	public void setFechaConfirmacion(int fechaConfirmacion) {
		this.fechaConfirmacion = fechaConfirmacion;
	}
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	
}
