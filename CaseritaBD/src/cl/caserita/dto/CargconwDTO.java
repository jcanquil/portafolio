package cl.caserita.dto;
import java.io.Serializable;

public class CargconwDTO implements Serializable{

	private int		codigoEmpresa;
	private int		numcarguio;
	private String	patente;
	private int		codigoBodega;
	private int		codigoArticulo;
	private String	digitoArticulo;
	private String	tipoCarguio;
	private int		cantidadArticulo;
	private int		fechaDevolucion;
	private int		cantidadConfirmada;
	private int		cantidadDiferencia;
	private int		fechaExpiracion;
	private double	precioNeto;
	private double	precioBruto;
	
	
	
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getNumcarguio() {
		return numcarguio;
	}
	public void setNumcarguio(int numcarguio) {
		this.numcarguio = numcarguio;
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
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getDigitoArticulo() {
		return digitoArticulo;
	}
	public void setDigitoArticulo(String digitoArticulo) {
		this.digitoArticulo = digitoArticulo;
	}
	public String getTipoCarguio() {
		return tipoCarguio;
	}
	public void setTipoCarguio(String tipoCarguio) {
		this.tipoCarguio = tipoCarguio;
	}
	public int getCantidadArticulo() {
		return cantidadArticulo;
	}
	public void setCantidadArticulo(int cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}
	public int getFechaDevolucion() {
		return fechaDevolucion;
	}
	public void setFechaDevolucion(int fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	public int getCantidadConfirmada() {
		return cantidadConfirmada;
	}
	public void setCantidadConfirmada(int cantidadConfirmada) {
		this.cantidadConfirmada = cantidadConfirmada;
	}
	public int getCantidadDiferencia() {
		return cantidadDiferencia;
	}
	public void setCantidadDiferencia(int cantidadDiferencia) {
		this.cantidadDiferencia = cantidadDiferencia;
	}
	public int getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(int fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	public double getPrecioNeto() {
		return precioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		this.precioNeto = precioNeto;
	}
	public double getPrecioBruto() {
		return precioBruto;
	}
	public void setPrecioBruto(double precioBruto) {
		this.precioBruto = precioBruto;
	}
	
	
	
	
}
