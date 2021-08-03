package cl.caserita.dto;
import java.io.Serializable;

public class CentroDespachoDTO implements Serializable{

	private int rutCliente;
	private String dvCliente;
	private int codRegion;
	private String descripcionRegion;
	private int codCiudad;
	private String descripcionCiudad;
	private int codComuna;
	private String descripcionComuna;
	private int correlativo;
	private String direccion;
	private int numero;
	private String telefono;
	private String celular;
	private int codigoTipoDespacho;
	private String latitud;
	private String longitud;
	private String codigoEstado;
	private String descripcionEstado;
	private String descripcionOficina;
	
	
	
	
	public String getDescripcionOficina() {
		return descripcionOficina;
	}
	public void setDescripcionOficina(String descripcionOficina) {
		this.descripcionOficina = descripcionOficina;
	}
	public String getCodigoEstado() {
		return codigoEstado;
	}
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	public String getDescripcionEstado() {
		return descripcionEstado;
	}
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	public int getCodigoTipoDespacho() {
		return codigoTipoDespacho;
	}
	public void setCodigoTipoDespacho(int codigoTipoDespacho) {
		this.codigoTipoDespacho = codigoTipoDespacho;
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
	public int getCodRegion() {
		return codRegion;
	}
	public void setCodRegion(int codRegion) {
		this.codRegion = codRegion;
	}
	public String getDescripcionRegion() {
		return descripcionRegion;
	}
	public void setDescripcionRegion(String descripcionRegion) {
		this.descripcionRegion = descripcionRegion;
	}
	public int getCodCiudad() {
		return codCiudad;
	}
	public void setCodCiudad(int codCiudad) {
		this.codCiudad = codCiudad;
	}
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}
	public int getCodComuna() {
		return codComuna;
	}
	public void setCodComuna(int codComuna) {
		this.codComuna = codComuna;
	}
	public String getDescripcionComuna() {
		return descripcionComuna;
	}
	public void setDescripcionComuna(String descripcionComuna) {
		this.descripcionComuna = descripcionComuna;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	
}
