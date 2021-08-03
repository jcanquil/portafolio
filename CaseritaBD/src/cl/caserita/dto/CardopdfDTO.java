package cl.caserita.dto;
import java.io.Serializable;
public class CardopdfDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroCarguio;
	private String patente;
	private int codigoBodega;
	private int codigoEmpresaMail;
	private int codigoBodegaMail;
	private String codigoCuentaCorreo;
	private int numeroIngreso;
	private int codigoDocumento;
	private int numeroDocumento;
	private String rutaDocumentoPDF;
	private String nombrePDF;
	private int estadoProcesado;
	private int fechaUsuario;
	private int horaUsuario;
	private String nombreEquipo;
	private String ipEquipo;
	private String usuario;
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getCodigoEmpresaMail() {
		return codigoEmpresaMail;
	}
	public void setCodigoEmpresaMail(int codigoEmpresaMail) {
		this.codigoEmpresaMail = codigoEmpresaMail;
	}
	public int getCodigoBodegaMail() {
		return codigoBodegaMail;
	}
	public void setCodigoBodegaMail(int codigoBodegaMail) {
		this.codigoBodegaMail = codigoBodegaMail;
	}
	public String getCodigoCuentaCorreo() {
		return codigoCuentaCorreo;
	}
	public void setCodigoCuentaCorreo(String codigoCuentaCorreo) {
		this.codigoCuentaCorreo = codigoCuentaCorreo;
	}
	public int getNumeroIngreso() {
		return numeroIngreso;
	}
	public void setNumeroIngreso(int numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getRutaDocumentoPDF() {
		return rutaDocumentoPDF;
	}
	public void setRutaDocumentoPDF(String rutaDocumentoPDF) {
		this.rutaDocumentoPDF = rutaDocumentoPDF;
	}
	public String getNombrePDF() {
		return nombrePDF;
	}
	public void setNombrePDF(String nombrePDF) {
		this.nombrePDF = nombrePDF;
	}
	public int getEstadoProcesado() {
		return estadoProcesado;
	}
	public void setEstadoProcesado(int estadoProcesado) {
		this.estadoProcesado = estadoProcesado;
	}
	public int getFechaUsuario() {
		return fechaUsuario;
	}
	public void setFechaUsuario(int fechaUsuario) {
		this.fechaUsuario = fechaUsuario;
	}
	public int getHoraUsuario() {
		return horaUsuario;
	}
	public void setHoraUsuario(int horaUsuario) {
		this.horaUsuario = horaUsuario;
	}
	public String getNombreEquipo() {
		return nombreEquipo;
	}
	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}
	public String getIpEquipo() {
		return ipEquipo;
	}
	public void setIpEquipo(String ipEquipo) {
		this.ipEquipo = ipEquipo;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}
