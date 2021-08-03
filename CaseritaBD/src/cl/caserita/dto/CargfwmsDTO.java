package cl.caserita.dto;
import java.io.Serializable;
public class CargfwmsDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroCarguio;
	private String patente;
	private int codigoBodega;
	private String nombreArchivoXML;
	private String tipo;
	private int fechaUsuario;
	private int horaUsuario;
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
	public String getNombreArchivoXML() {
		return nombreArchivoXML;
	}
	public void setNombreArchivoXML(String nombreArchivoXML) {
		this.nombreArchivoXML = nombreArchivoXML;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	
	
}
