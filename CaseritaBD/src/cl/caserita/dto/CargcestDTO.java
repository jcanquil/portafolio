package cl.caserita.dto;

import java.io.Serializable;

public class CargcestDTO implements Serializable {
	
	private int		codigoEmpresa;
	private int		numcarguio;
	private String	patente;
	private int		codigoBodega;
	private int		correlativo;
	private String	estado;
	private int		fechaUsuario;
	private int		horaUsuario;
	private String	usuario;
	
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getNumcarguio() {
		return numcarguio;
	}
	public void setNumcarguio(int numcarguio) {
		this.numcarguio = numcarguio;
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
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	


}
