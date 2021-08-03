package cl.caserita.dto;
import java.io.Serializable;
import java.util.List;

public class CldmcoDTO implements Serializable{

	private int codDocumento;
	private String rutCliente;
	private String dvCliente;
	private int fechaMovimiento;
	private int horaMovimiento;
	private int correlativo;
	private String rutProveedor;
	private String dvProveedor;
	private int codigoArticulo;
	private String digitoverificador;
	private int cantidadArticulo;
	private int costoArticulo;
	private double descuentoLinea;
	private int montoIva;
	private int montoFlete;
	private int montoCompra;
	private int sectorBodega;
	private int estado;
	private String glosa;
	private String descArticulo;
	private double precio;
	private int descuentoentero;
	private List impuestos;
	private double valorNeto;
	private double precioNeto;
	private int montoExento;
	
	
	public int getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(int montoExento) {
		this.montoExento = montoExento;
	}
	public double getValorNeto() {
		return valorNeto;
	}
	public void setValorNeto(double valorNeto) {
		this.valorNeto = valorNeto;
	}
	public double getPrecioNeto() {
		return precioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		this.precioNeto = precioNeto;
	}
	public List getImpuestos() {
		return impuestos;
	}
	public void setImpuestos(List impuestos) {
		this.impuestos = impuestos;
	}
	public int getDescuentoentero() {
		return descuentoentero;
	}
	public void setDescuentoentero(int descuentoentero) {
		this.descuentoentero = descuentoentero;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getDescArticulo() {
		return descArticulo;
	}
	public void setDescArticulo(String descArticulo) {
		this.descArticulo = descArticulo;
	}
	public int getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(int codDocumento) {
		this.codDocumento = codDocumento;
	}
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
	public int getFechaMovimiento() {
		return fechaMovimiento;
	}
	public void setFechaMovimiento(int fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	public int getHoraMovimiento() {
		return horaMovimiento;
	}
	public void setHoraMovimiento(int horaMovimiento) {
		this.horaMovimiento = horaMovimiento;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public String getRutProveedor() {
		return rutProveedor;
	}
	public void setRutProveedor(String rutProveedor) {
		this.rutProveedor = rutProveedor;
	}
	public String getDvProveedor() {
		return dvProveedor;
	}
	public void setDvProveedor(String dvProveedor) {
		this.dvProveedor = dvProveedor;
	}
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getDigitoverificador() {
		return digitoverificador;
	}
	public void setDigitoverificador(String digitoverificador) {
		this.digitoverificador = digitoverificador;
	}
	public int getCantidadArticulo() {
		return cantidadArticulo;
	}
	public void setCantidadArticulo(int cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}
	public int getCostoArticulo() {
		return costoArticulo;
	}
	public void setCostoArticulo(int costoArticulo) {
		this.costoArticulo = costoArticulo;
	}
	public double getDescuentoLinea() {
		return descuentoLinea;
	}
	public void setDescuentoLinea(double descuentoLinea) {
		this.descuentoLinea = descuentoLinea;
	}
	public int getMontoIva() {
		return montoIva;
	}
	public void setMontoIva(int montoIva) {
		this.montoIva = montoIva;
	}
	public int getMontoFlete() {
		return montoFlete;
	}
	public void setMontoFlete(int montoFlete) {
		this.montoFlete = montoFlete;
	}
	public int getMontoCompra() {
		return montoCompra;
	}
	public void setMontoCompra(int montoCompra) {
		this.montoCompra = montoCompra;
	}
	public int getSectorBodega() {
		return sectorBodega;
	}
	public void setSectorBodega(int sectorBodega) {
		this.sectorBodega = sectorBodega;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getGlosa() {
		return glosa;
	}
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}
	
}
