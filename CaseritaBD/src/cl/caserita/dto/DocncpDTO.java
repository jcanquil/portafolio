package cl.caserita.dto;

import java.io.Serializable;

public class DocncpDTO implements Serializable {
	
	private String 	tipoNota;
	private int		numeroNota;
	private int		fechaNota;
	private int		codigoBodega;
	private int		rutCliente;
	private String	digCliente;
	private int		correlativo;
	private int		numeroOV;
	private int		numeroCarguio;
	private int		numeroDocumento;
	private int 	numeroGuia;
	private int		numeroNcfinal;
	private int		fechaUsuario;
	private int		horaUsuario;
	private String	codigoUsuario;
	private String	motivo;
	
	
	
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public String getTipoNota() {
		return tipoNota;
	}
	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}
	public int getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(int numeroNota) {
		this.numeroNota = numeroNota;
	}
	public int getFechaNota() {
		return fechaNota;
	}
	public void setFechaNota(int fechaNota) {
		this.fechaNota = fechaNota;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(int rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getDigCliente() {
		return digCliente;
	}
	public void setDigCliente(String digCliente) {
		this.digCliente = digCliente;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public int getNumeroOV() {
		return numeroOV;
	}
	public void setNumeroOV(int numeroOV) {
		this.numeroOV = numeroOV;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getNumeroGuia() {
		return numeroGuia;
	}
	public void setNumeroGuia(int numeroGuia) {
		this.numeroGuia = numeroGuia;
	}
	public int getNumeroNcfinal() {
		return numeroNcfinal;
	}
	public void setNumeroNcfinal(int numeroNcfinal) {
		this.numeroNcfinal = numeroNcfinal;
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
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	
	
	
}
