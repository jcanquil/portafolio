package cl.caserita.dto;
import java.io.Serializable;

public class DocuNoGeneradoDTO implements Serializable {

	private int numeroOrden;
	private int numeroDocumento;
	private int RutCliente;
	private String dvCliente;
	private int tipoDocumento;
	private int fechaDocumento;
	private String comentario;
	
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public int getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getRutCliente() {
		return RutCliente;
	}
	public void setRutCliente(int rutCliente) {
		RutCliente = rutCliente;
	}
	public String getDvCliente() {
		return dvCliente;
	}
	public void setDvCliente(String dvCliente) {
		this.dvCliente = dvCliente;
	}
	public int getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(int tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public int getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	
	
}
