package cl.caserita.dto;

import java.io.Serializable;

public class ClienteUsuarioDTO implements Serializable {

	private String rut;
	private String usuario;
	private String password;
	private String estado;
	private int fechaIngreso;
	private int horaIngreso;
	private int fechaDesactivacion;
	private int horaDesactivacion;
	private String usuarioDesactivacion;
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(int fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public int getHoraIngreso() {
		return horaIngreso;
	}
	public void setHoraIngreso(int horaIngreso) {
		this.horaIngreso = horaIngreso;
	}
	public int getFechaDesactivacion() {
		return fechaDesactivacion;
	}
	public void setFechaDesactivacion(int fechaDesactivacion) {
		this.fechaDesactivacion = fechaDesactivacion;
	}
	public int getHoraDesactivacion() {
		return horaDesactivacion;
	}
	public void setHoraDesactivacion(int horaDesactivacion) {
		this.horaDesactivacion = horaDesactivacion;
	}
	public String getUsuarioDesactivacion() {
		return usuarioDesactivacion;
	}
	public void setUsuarioDesactivacion(String usuarioDesactivacion) {
		this.usuarioDesactivacion = usuarioDesactivacion;
	}
	
	
}
