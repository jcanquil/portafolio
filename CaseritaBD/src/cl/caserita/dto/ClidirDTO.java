package cl.caserita.dto;

import java.io.Serializable;

public class ClidirDTO implements Serializable{

	private int region;
	private int ciudad;
	private int comuna;
	private String direccionCliente;
	private String latitud;
	private String longitud;
	private String descripcionRegion;
	private String descripcionCiudad;
	private String descripcionComuna;
	private int numeroDireccion;
	private int depto;
	private String telefono;
	private String celular;
	private String villaPoblacion;
	private String nombreContacto;
	private String mail;
	private int rutCliente;
	private String dvCliente;
	private int correlativo;
	private String observacion;
	private int tipoDireccion;
	private String departamentoString;
	
	
	
	
	
	public String getDepartamentoString() {
		return departamentoString;
	}
	public void setDepartamentoString(String departamentoString) {
		this.departamentoString = departamentoString;
	}
	public int getTipoDireccion() {
		return tipoDireccion;
	}
	public void setTipoDireccion(int tipoDireccion) {
		this.tipoDireccion = tipoDireccion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
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
	public String getDescripcionRegion() {
		return descripcionRegion;
	}
	public void setDescripcionRegion(String descripcionRegion) {
		this.descripcionRegion = descripcionRegion;
	}
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}
	public String getDescripcionComuna() {
		return descripcionComuna;
	}
	public void setDescripcionComuna(String descripcionComuna) {
		this.descripcionComuna = descripcionComuna;
	}
	public int getNumeroDireccion() {
		return numeroDireccion;
	}
	public void setNumeroDireccion(int numeroDireccion) {
		this.numeroDireccion = numeroDireccion;
	}
	public int getDepto() {
		return depto;
	}
	public void setDepto(int depto) {
		this.depto = depto;
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
	public String getVillaPoblacion() {
		return villaPoblacion;
	}
	public void setVillaPoblacion(String villaPoblacion) {
		this.villaPoblacion = villaPoblacion;
	}
	public String getNombreContacto() {
		return nombreContacto;
	}
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
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
	public String getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	public int getCiudad() {
		return ciudad;
	}
	public void setCiudad(int ciudad) {
		this.ciudad = ciudad;
	}
	public int getComuna() {
		return comuna;
	}
	public void setComuna(int comuna) {
		this.comuna = comuna;
	}
	
}
