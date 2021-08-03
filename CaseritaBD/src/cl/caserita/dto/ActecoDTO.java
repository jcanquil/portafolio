package cl.caserita.dto;
import java.io.Serializable;

public class ActecoDTO implements Serializable{

	private String codigoActeco;
	private String descripcionActeco;
	public String getCodigoActeco() {
		return codigoActeco;
	}
	public void setCodigoActeco(String codigoActeco) {
		this.codigoActeco = codigoActeco;
	}
	public String getDescripcionActeco() {
		return descripcionActeco;
	}
	public void setDescripcionActeco(String descripcionActeco) {
		this.descripcionActeco = descripcionActeco;
	}
	
	
}
