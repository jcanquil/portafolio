package cl.caserita.dto;
import java.io.Serializable;

public class ArchordDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroOrden;
	private int numeroVersion;
	private int numeroArchivo;
	private String rutaArchivo;
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
	public int getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public int getNumeroVersion() {
		return numeroVersion;
	}
	public void setNumeroVersion(int numeroVersion) {
		this.numeroVersion = numeroVersion;
	}
	public int getNumeroArchivo() {
		return numeroArchivo;
	}
	public void setNumeroArchivo(int numeroArchivo) {
		this.numeroArchivo = numeroArchivo;
	}
	public String getRutaArchivo() {
		return rutaArchivo;
	}
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
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
