package cl.caserita.dto;
import java.io.Serializable;

public class CarguiadDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroCarguio;
	private String patente;
	private int codigoBodega;
	private String codigoEstadoInventario;
	private int rutChofer;
	private String dvChofer;
	
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
	public String getCodigoEstadoInventario() {
		return codigoEstadoInventario;
	}
	public void setCodigoEstadoInventario(String codigoEstadoInventario) {
		this.codigoEstadoInventario = codigoEstadoInventario;
	}
	
}
