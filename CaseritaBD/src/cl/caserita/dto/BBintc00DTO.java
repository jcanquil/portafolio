package cl.caserita.dto;
import java.io.Serializable;
public class BBintc00DTO implements Serializable {

	private int codigoDocumento;
	private int fechaDocumento;
	private int numeroDocumento;
	private int codigoBodega;
	private int totalDocumento;
	private int rutCliente;
	private String dvCliente;
	private int codigoVendedor;
	private int indicadorDespacho;
	private int indicadorFacturacion;
	private String formaPago;
	private String estado;
	private String errorCabecera;
	private String nombreDispositivo;
	private int horaGeneracion;
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public int getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
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
	public int getTotalDocumento() {
		return totalDocumento;
	}
	public void setTotalDocumento(int totalDocumento) {
		this.totalDocumento = totalDocumento;
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
	public int getCodigoVendedor() {
		return codigoVendedor;
	}
	public void setCodigoVendedor(int codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	public int getIndicadorDespacho() {
		return indicadorDespacho;
	}
	public void setIndicadorDespacho(int indicadorDespacho) {
		this.indicadorDespacho = indicadorDespacho;
	}
	public int getIndicadorFacturacion() {
		return indicadorFacturacion;
	}
	public void setIndicadorFacturacion(int indicadorFacturacion) {
		this.indicadorFacturacion = indicadorFacturacion;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getErrorCabecera() {
		return errorCabecera;
	}
	public void setErrorCabecera(String errorCabecera) {
		this.errorCabecera = errorCabecera;
	}
	public String getNombreDispositivo() {
		return nombreDispositivo;
	}
	public void setNombreDispositivo(String nombreDispositivo) {
		this.nombreDispositivo = nombreDispositivo;
	}
	public int getHoraGeneracion() {
		return horaGeneracion;
	}
	public void setHoraGeneracion(int horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}
	
}
