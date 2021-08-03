package cl.caserita.dto;
import java.io.Serializable;

public class CamtraDTO implements Serializable{

	private int codigoTipoMovto;
	private int numeroDocumento;
	private int fechaDocumento;
	private int correlativo;
	private int horaEvento;
	private int codigoBodega;
	private int codigoCaja;
	private int codigoVendedor;
	private String ingresoSalida;
	private int valorDocumento;
	private String rutCliente;
	private String dvCliente;
	private String nombreCliente;
	private int codigoDocumento;
	private int numeroBolfactura;
	private String codigoUsuario;
	private String estado;
	private String rutaObjeto;
	private String estadoPaperless;
	private String estadoSII;
	private int codigoEmpresa;
	
	
	
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public String getEstadoPaperless() {
		return estadoPaperless;
	}
	public void setEstadoPaperless(String estadoPaperless) {
		this.estadoPaperless = estadoPaperless;
	}
	public String getEstadoSII() {
		return estadoSII;
	}
	public void setEstadoSII(String estadoSII) {
		this.estadoSII = estadoSII;
	}
	public int getCodigoTipoMovto() {
		return codigoTipoMovto;
	}
	public void setCodigoTipoMovto(int codigoTipoMovto) {
		this.codigoTipoMovto = codigoTipoMovto;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public int getHoraEvento() {
		return horaEvento;
	}
	public void setHoraEvento(int horaEvento) {
		this.horaEvento = horaEvento;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getCodigoCaja() {
		return codigoCaja;
	}
	public void setCodigoCaja(int codigoCaja) {
		this.codigoCaja = codigoCaja;
	}
	public int getCodigoVendedor() {
		return codigoVendedor;
	}
	public void setCodigoVendedor(int codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	public String getIngresoSalida() {
		return ingresoSalida;
	}
	public void setIngresoSalida(String ingresoSalida) {
		this.ingresoSalida = ingresoSalida;
	}
	public int getValorDocumento() {
		return valorDocumento;
	}
	public void setValorDocumento(int valorDocumento) {
		this.valorDocumento = valorDocumento;
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
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public int getNumeroBolfactura() {
		return numeroBolfactura;
	}
	public void setNumeroBolfactura(int numeroBolfactura) {
		this.numeroBolfactura = numeroBolfactura;
	}
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getRutaObjeto() {
		return rutaObjeto;
	}
	public void setRutaObjeto(String rutaObjeto) {
		this.rutaObjeto = rutaObjeto;
	}
}
