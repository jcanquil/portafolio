package cl.caserita.dto;
import java.io.Serializable;

public class ClientezonaDTO implements Serializable{

	private int rutCliente;
	private String dvCliente;
	private String razonSocial;
	private String direccion;
	private int numeroDireccion;
	private String villaPoblacion;
	private int depto;
	private int codigoRegion;
	private String descripcionRegion;
	private int codigoCiudad;
	private String descripcionCiudad;
	private int codigoComuna;
	private String descripcionComuna;
	private int correlativoDireccion;
	private String latitud;
	private String longitud;
	public int getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(int rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getDvCliente() {
		return dvCliente;
	}
	public void setDvCliente(String dvCliente) {
		this.dvCliente = dvCliente;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getNumeroDireccion() {
		return numeroDireccion;
	}
	public void setNumeroDireccion(int numeroDireccion) {
		this.numeroDireccion = numeroDireccion;
	}
	public String getVillaPoblacion() {
		return villaPoblacion;
	}
	public void setVillaPoblacion(String villaPoblacion) {
		this.villaPoblacion = villaPoblacion;
	}
	public int getDepto() {
		return depto;
	}
	public void setDepto(int depto) {
		this.depto = depto;
	}
	public int getCodigoRegion() {
		return codigoRegion;
	}
	public void setCodigoRegion(int codigoRegion) {
		this.codigoRegion = codigoRegion;
	}
	public String getDescripcionRegion() {
		return descripcionRegion;
	}
	public void setDescripcionRegion(String descripcionRegion) {
		this.descripcionRegion = descripcionRegion;
	}
	public int getCodigoCiudad() {
		return codigoCiudad;
	}
	public void setCodigoCiudad(int codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}
	public int getCodigoComuna() {
		return codigoComuna;
	}
	public void setCodigoComuna(int codigoComuna) {
		this.codigoComuna = codigoComuna;
	}
	public String getDescripcionComuna() {
		return descripcionComuna;
	}
	public void setDescripcionComuna(String descripcionComuna) {
		this.descripcionComuna = descripcionComuna;
	}
	public int getCorrelativoDireccion() {
		return correlativoDireccion;
	}
	public void setCorrelativoDireccion(int correlativoDireccion) {
		this.correlativoDireccion = correlativoDireccion;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	
}
