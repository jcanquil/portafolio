package cl.caserita.dto;
import java.io.Serializable;
import java.util.List;

public class ClienteeCommerceDTO implements Serializable{
	
	private String rutCliente;
	private String dvCliente;
	private String razonSocial;
	private String codigoGiro;
	private String fechaDespacho;
	private String formaPago;
	private String proveedor;
	private String tipoDocumento;
	private String idBanco;
	
	
	public String getIdBanco() {
		return idBanco;
	}
	public void setIdBanco(String idBanco) {
		this.idBanco = idBanco;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getFechaDespacho() {
		return fechaDespacho;
	}
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}
	public String getCodigoGiro() {
		return codigoGiro;
	}
	public void setCodigoGiro(String codigoGiro) {
		this.codigoGiro = codigoGiro;
	}
	private String giro;
	private List<ClidirDTO> direcciones;
	public List<ClidirDTO> getDirecciones() {
		return direcciones;
	}
	public void setDirecciones(List<ClidirDTO> direcciones) {
		this.direcciones = direcciones;
	}
	public List<CldmcoDTO> getArticulos() {
		return articulos;
	}
	public void setArticulos(List<CldmcoDTO> articulos) {
		this.articulos = articulos;
	}
	private List<CldmcoDTO> articulos;
	public String getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(String rutCliente) {
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
	public String getGiro() {
		return giro;
	}
	public void setGiro(String giro) {
		this.giro = giro;
	}
	
	
}
