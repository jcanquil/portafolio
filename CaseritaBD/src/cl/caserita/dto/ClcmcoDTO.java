package cl.caserita.dto;
import java.io.Serializable;


public class ClcmcoDTO implements Serializable{

	private int codDocumento;
	private String rutCliente;
	private String dvCliente;
	private int fechaMovimiento;
	private int horaMovimiento;
	private int numeroDocumento;
	private int codigoBodega;
	private int codigoVendedor;
	private int cantidadLineaDetalle;
	private int totalCosto;
	private int totalIva;
	private int totalDescuento;
	private int totalFlete;
	private int totalDocumento;
	private String formaPago;
	private int montoPie;
	private int MontoInteres;
	private int cantidadCheques;
	private int montoCheques;
	private int mesMovimiento;
	private int añoMovimiento;
	private int estado;
	private int fechaTranProveedor;
	private int totalCostoNeto;
	private int totalDescuentoNeto;
	private int valorNeto;
	private int totalExento;
	private int totalNeto;
	private int empresa;
	
	
	
	public int getEmpresa() {
		return empresa;
	}
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}
	public int getTotalNeto() {
		return totalNeto;
	}
	public void setTotalNeto(int totalNeto) {
		this.totalNeto = totalNeto;
	}
	public int getTotalCostoNeto() {
		return totalCostoNeto;
	}
	public void setTotalCostoNeto(int totalCostoNeto) {
		this.totalCostoNeto = totalCostoNeto;
	}
	public int getTotalDescuentoNeto() {
		return totalDescuentoNeto;
	}
	public void setTotalDescuentoNeto(int totalDescuentoNeto) {
		this.totalDescuentoNeto = totalDescuentoNeto;
	}
	public int getValorNeto() {
		return valorNeto;
	}
	public void setValorNeto(int valorNeto) {
		this.valorNeto = valorNeto;
	}
	public int getTotalExento() {
		return totalExento;
	}
	public void setTotalExento(int totalExento) {
		this.totalExento = totalExento;
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
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getCodigoVendedor() {
		return codigoVendedor;
	}
	public void setCodigoVendedor(int codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	public int getCantidadLineaDetalle() {
		return cantidadLineaDetalle;
	}
	public void setCantidadLineaDetalle(int cantidadLineaDetalle) {
		this.cantidadLineaDetalle = cantidadLineaDetalle;
	}
	public int getTotalCosto() {
		return totalCosto;
	}
	public void setTotalCosto(int totalCosto) {
		this.totalCosto = totalCosto;
	}
	public int getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(int totalIva) {
		this.totalIva = totalIva;
	}
	public int getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(int totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public int getTotalFlete() {
		return totalFlete;
	}
	public void setTotalFlete(int totalFlete) {
		this.totalFlete = totalFlete;
	}
	public int getTotalDocumento() {
		return totalDocumento;
	}
	public void setTotalDocumento(int totalDocumento) {
		this.totalDocumento = totalDocumento;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public int getMontoPie() {
		return montoPie;
	}
	public void setMontoPie(int montoPie) {
		this.montoPie = montoPie;
	}
	public int getMontoInteres() {
		return MontoInteres;
	}
	public void setMontoInteres(int montoInteres) {
		MontoInteres = montoInteres;
	}
	public int getCantidadCheques() {
		return cantidadCheques;
	}
	public void setCantidadCheques(int cantidadCheques) {
		this.cantidadCheques = cantidadCheques;
	}
	public int getMontoCheques() {
		return montoCheques;
	}
	public void setMontoCheques(int montoCheques) {
		this.montoCheques = montoCheques;
	}
	public int getMesMovimiento() {
		return mesMovimiento;
	}
	public void setMesMovimiento(int mesMovimiento) {
		this.mesMovimiento = mesMovimiento;
	}
	public int getAñoMovimiento() {
		return añoMovimiento;
	}
	public void setAñoMovimiento(int añoMovimiento) {
		this.añoMovimiento = añoMovimiento;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getFechaTranProveedor() {
		return fechaTranProveedor;
	}
	public void setFechaTranProveedor(int fechaTranProveedor) {
		this.fechaTranProveedor = fechaTranProveedor;
	}
	
	
}
