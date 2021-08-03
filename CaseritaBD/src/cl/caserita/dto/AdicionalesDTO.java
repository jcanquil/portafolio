package cl.caserita.dto;
import java.io.Serializable;
public class AdicionalesDTO implements Serializable {

	private String fechaNacimiento;
	private String nombresHijos;
	private String apellidosHijos;
	
	
	private String nombreCompleto;
	private int rutEmpresa;
	private String dvEmpresa;
	private int rutPersona;
	private String dvPersona;
	private int correlativo;
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getNombresHijos() {
		return nombresHijos;
	}
	public void setNombresHijos(String nombresHijos) {
		this.nombresHijos = nombresHijos;
	}
	public String getApellidosHijos() {
		return apellidosHijos;
	}
	public void setApellidosHijos(String apellidosHijos) {
		this.apellidosHijos = apellidosHijos;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public int getRutEmpresa() {
		return rutEmpresa;
	}
	public void setRutEmpresa(int rutEmpresa) {
		this.rutEmpresa = rutEmpresa;
	}
	public String getDvEmpresa() {
		return dvEmpresa;
	}
	public void setDvEmpresa(String dvEmpresa) {
		this.dvEmpresa = dvEmpresa;
	}
	public int getRutPersona() {
		return rutPersona;
	}
	public void setRutPersona(int rutPersona) {
		this.rutPersona = rutPersona;
	}
	public String getDvPersona() {
		return dvPersona;
	}
	public void setDvPersona(String dvPersona) {
		this.dvPersona = dvPersona;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	



	
	
}
