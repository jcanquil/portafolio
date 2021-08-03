package cl.caserita.dto;
import java.io.Serializable;

public class DetlibDTO implements Serializable {

	private String codLibro;
	private String codigoUsuario;
	private int fechaGeneracion;
	private int horaGeneracion;
	private int periodoInicial;
	private int periodoFinal;
	private String codDetalleLibro;
	private int codDocumento;
	private int folioDocumento;
	private String idDocumentoAnulado;
	private int tasaImpuestoDetalle;
	private int idServicioPeriodico;
	private String fechaDocumento;
	private int codigoSucursalSII;
	private int rutProveedor;
	private String razonSocial;
	private long montoExento;
	private long montoNetoDetalle;
	private long montoNetoActivoFijo;
	
	private long montoIvaActivoFijo;
	
	private long montoIvaDetalle;
	private long montoIvaFueraPlazo;
	private long montoTotal;
	private int empresa;
	
	public int getEmpresa() {
		return empresa;
	}
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}
	public String getCodLibro() {
		return codLibro;
	}
	public void setCodLibro(String codLibro) {
		this.codLibro = codLibro;
	}
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public int getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(int fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public int getHoraGeneracion() {
		return horaGeneracion;
	}
	public void setHoraGeneracion(int horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}
	public int getPeriodoInicial() {
		return periodoInicial;
	}
	public void setPeriodoInicial(int periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	public int getPeriodoFinal() {
		return periodoFinal;
	}
	public void setPeriodoFinal(int periodoFinal) {
		this.periodoFinal = periodoFinal;
	}
	public String getCodDetalleLibro() {
		return codDetalleLibro;
	}
	public void setCodDetalleLibro(String codDetalleLibro) {
		this.codDetalleLibro = codDetalleLibro;
	}
	public int getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(int codDocumento) {
		this.codDocumento = codDocumento;
	}
	public int getFolioDocumento() {
		return folioDocumento;
	}
	public void setFolioDocumento(int folioDocumento) {
		this.folioDocumento = folioDocumento;
	}
	public String getIdDocumentoAnulado() {
		return idDocumentoAnulado;
	}
	public void setIdDocumentoAnulado(String idDocumentoAnulado) {
		this.idDocumentoAnulado = idDocumentoAnulado;
	}
	public int getTasaImpuestoDetalle() {
		return tasaImpuestoDetalle;
	}
	public void setTasaImpuestoDetalle(int tasaImpuestoDetalle) {
		this.tasaImpuestoDetalle = tasaImpuestoDetalle;
	}
	public int getIdServicioPeriodico() {
		return idServicioPeriodico;
	}
	public void setIdServicioPeriodico(int idServicioPeriodico) {
		this.idServicioPeriodico = idServicioPeriodico;
	}
	public String getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	public int getCodigoSucursalSII() {
		return codigoSucursalSII;
	}
	public void setCodigoSucursalSII(int codigoSucursalSII) {
		this.codigoSucursalSII = codigoSucursalSII;
	}
	public int getRutProveedor() {
		return rutProveedor;
	}
	public void setRutProveedor(int rutProveedor) {
		this.rutProveedor = rutProveedor;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public long getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(long montoExento) {
		this.montoExento = montoExento;
	}
	public long getMontoNetoDetalle() {
		return montoNetoDetalle;
	}
	public void setMontoNetoDetalle(long montoNetoDetalle) {
		this.montoNetoDetalle = montoNetoDetalle;
	}
	public long getMontoIvaDetalle() {
		return montoIvaDetalle;
	}
	public void setMontoIvaDetalle(long montoIvaDetalle) {
		this.montoIvaDetalle = montoIvaDetalle;
	}
	public long getMontoIvaFueraPlazo() {
		return montoIvaFueraPlazo;
	}
	public void setMontoIvaFueraPlazo(long montoIvaFueraPlazo) {
		this.montoIvaFueraPlazo = montoIvaFueraPlazo;
	}
	public long getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(long montoTotal) {
		this.montoTotal = montoTotal;
	}
	public long getMontoNetoActivoFijo() {
		return montoNetoActivoFijo;
	}
	public void setMontoNetoActivoFijo(long montoNetoActivoFijo) {
		this.montoNetoActivoFijo = montoNetoActivoFijo;
	}
	public long getMontoIvaActivoFijo() {
		return montoIvaActivoFijo;
	}
	public void setMontoIvaActivoFijo(long montoIvaActivoFijo) {
		this.montoIvaActivoFijo = montoIvaActivoFijo;
	}
	
}
