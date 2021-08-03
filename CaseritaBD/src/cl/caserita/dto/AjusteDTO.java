package cl.caserita.dto;
import java.io.Serializable;

public class AjusteDTO implements Serializable{

	private int codigoArticulo;
	private String dvArticulo;
	private String tipoContenedor;
	private int cantidadAjuste;
	private int numeroLote;
	private int cantidadCapturada;
	private String referencia1;
	private String referencia2;
	private String estadoInventario;
	private String codigoRazon;
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getDvArticulo() {
		return dvArticulo;
	}
	public void setDvArticulo(String dvArticulo) {
		this.dvArticulo = dvArticulo;
	}
	public String getTipoContenedor() {
		return tipoContenedor;
	}
	public void setTipoContenedor(String tipoContenedor) {
		this.tipoContenedor = tipoContenedor;
	}
	public int getCantidadAjuste() {
		return cantidadAjuste;
	}
	public void setCantidadAjuste(int cantidadAjuste) {
		this.cantidadAjuste = cantidadAjuste;
	}
	public int getNumeroLote() {
		return numeroLote;
	}
	public void setNumeroLote(int numeroLote) {
		this.numeroLote = numeroLote;
	}
	public int getCantidadCapturada() {
		return cantidadCapturada;
	}
	public void setCantidadCapturada(int cantidadCapturada) {
		this.cantidadCapturada = cantidadCapturada;
	}
	public String getReferencia1() {
		return referencia1;
	}
	public void setReferencia1(String referencia1) {
		this.referencia1 = referencia1;
	}
	public String getReferencia2() {
		return referencia2;
	}
	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}
	public String getEstadoInventario() {
		return estadoInventario;
	}
	public void setEstadoInventario(String estadoInventario) {
		this.estadoInventario = estadoInventario;
	}
	public String getCodigoRazon() {
		return codigoRazon;
	}
	public void setCodigoRazon(String codigoRazon) {
		this.codigoRazon = codigoRazon;
	}
	
}
