package cl.caserita.dto;
import java.io.Serializable;
public class CliDistDTO implements Serializable {

	private String rut;
	private String dv;
	private String razonSocial;
	private String telefono1;
	private String telefono2;
	private String nombreContacto;
	private String direccion;
	private int codRegion;
	private int codCiudad;
	private int codComuna;
	private int tipoNegocio;
	private String latitud;
	private String longitud;
	private String contacto;
	private int codigoVendedor;
	
	public int getCodigoVendedor() {
		return codigoVendedor;
	}
	public void setCodigoVendedor(int codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getDv() {
		return dv;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getTelefono1() {
		return telefono1;
	}
	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}
	public String getTelefono2() {
		return telefono2;
	}
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	public String getNombreContacto() {
		return nombreContacto;
	}
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getCodRegion() {
		return codRegion;
	}
	public void setCodRegion(int codRegion) {
		this.codRegion = codRegion;
	}
	public int getCodCiudad() {
		return codCiudad;
	}
	public void setCodCiudad(int codCiudad) {
		this.codCiudad = codCiudad;
	}
	public int getCodComuna() {
		return codComuna;
	}
	public void setCodComuna(int codComuna) {
		this.codComuna = codComuna;
	}
	public int getTipoNegocio() {
		return tipoNegocio;
	}
	public void setTipoNegocio(int tipoNegocio) {
		this.tipoNegocio = tipoNegocio;
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
