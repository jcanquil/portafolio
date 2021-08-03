package cl.caserita.dto;
import java.io.Serializable;

public class ConfirmacionCarguioDetalleDTO implements Serializable{

	private int codArticulo;
	private int cantidad;
	private int cantidadDespachada;
	private String unidad;
	private String estadoInventario;
	private String estadoInventarioNuevo;
	private int codigoEmpresa;
	private int codigoBodega;
	private int fechaExpiracion;
	private int correlativo;
	
	
	
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public int getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(int fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public String getEstadoInventario() {
		return estadoInventario;
	}
	public void setEstadoInventario(String estadoInventario) {
		this.estadoInventario = estadoInventario;
	}
	public String getEstadoInventarioNuevo() {
		return estadoInventarioNuevo;
	}
	public void setEstadoInventarioNuevo(String estadoInventarioNuevo) {
		this.estadoInventarioNuevo = estadoInventarioNuevo;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public int getCodArticulo() {
		return codArticulo;
	}
	public void setCodArticulo(int codArticulo) {
		this.codArticulo = codArticulo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getCantidadDespachada() {
		return cantidadDespachada;
	}
	public void setCantidadDespachada(int cantidadDespachada) {
		this.cantidadDespachada = cantidadDespachada;
	}
	
}
