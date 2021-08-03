package cl.caserita.dto;
import java.io.Serializable;

public class DetimLibDTO implements Serializable{

	private String codLibro;
	private String codigoUsuario;
	private int fechaGeneracion;
	private int horaGeneracion;
	private int periodoInicial;
	private int periodoFinal;
	private String codDetalleLibro;
	private int codDocumento;
	private int folioDocumento;
	private int codigoImpuesto;
	private int tasaImpuesto;
	private long montoImpuesto;
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
	public int getCodigoImpuesto() {
		return codigoImpuesto;
	}
	public void setCodigoImpuesto(int codigoImpuesto) {
		this.codigoImpuesto = codigoImpuesto;
	}
	public int getTasaImpuesto() {
		return tasaImpuesto;
	}
	public void setTasaImpuesto(int tasaImpuesto) {
		this.tasaImpuesto = tasaImpuesto;
	}
	public long getMontoImpuesto() {
		return montoImpuesto;
	}
	public void setMontoImpuesto(long montoImpuesto) {
		this.montoImpuesto = montoImpuesto;
	}
	
}
