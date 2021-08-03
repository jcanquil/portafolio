package cl.caserita.dto;
import java.io.Serializable;

public class DetswmsDTO implements Serializable{

	private int codigoEmpresa;
	private int codigoTipoSalida;
	private int fechaProceso;
	private int horaProceso;
	private String archivoXML;
	private int codigoArticulo;
	private String codigoEstadoInventario;
	private String proximoEstadoInvenatrio;
	private int cantidad;
	private int cantidadReal;
	private int numeroOrden;
	private int numeroDocumento;
	private int fechaRealGeneracion;
	private int numeroDetalle;
	
	
	public int getNumeroDetalle() {
		return numeroDetalle;
	}
	public void setNumeroDetalle(int numeroDetalle) {
		this.numeroDetalle = numeroDetalle;
	}
	public int getFechaRealGeneracion() {
		return fechaRealGeneracion;
	}
	public void setFechaRealGeneracion(int fechaRealGeneracion) {
		this.fechaRealGeneracion = fechaRealGeneracion;
	}
	public int getCantidadReal() {
		return cantidadReal;
	}
	public void setCantidadReal(int cantidadReal) {
		this.cantidadReal = cantidadReal;
	}
	public int getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getCodigoTipoSalida() {
		return codigoTipoSalida;
	}
	public void setCodigoTipoSalida(int codigoTipoSalida) {
		this.codigoTipoSalida = codigoTipoSalida;
	}
	public int getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(int fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public int getHoraProceso() {
		return horaProceso;
	}
	public void setHoraProceso(int horaProceso) {
		this.horaProceso = horaProceso;
	}
	public String getArchivoXML() {
		return archivoXML;
	}
	public void setArchivoXML(String archivoXML) {
		this.archivoXML = archivoXML;
	}
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getCodigoEstadoInventario() {
		return codigoEstadoInventario;
	}
	public void setCodigoEstadoInventario(String codigoEstadoInventario) {
		this.codigoEstadoInventario = codigoEstadoInventario;
	}
	public String getProximoEstadoInvenatrio() {
		return proximoEstadoInvenatrio;
	}
	public void setProximoEstadoInvenatrio(String proximoEstadoInvenatrio) {
		this.proximoEstadoInvenatrio = proximoEstadoInvenatrio;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}
