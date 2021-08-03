package cl.caserita.dto;

import java.io.Serializable;
import java.util.List;

public class CarguiodTranspDTO implements Serializable{
	
	private String 	numeroDocumento;
	private int 	tipoDocumento;
	private String 	codEstado;
	private String 	codMotivo;
	
	private String	desEstado;
	private String	desMotivo;
	
	
	
	public String getDesEstado() {
		return desEstado;
	}
	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}
	public String getDesMotivo() {
		return desMotivo;
	}
	public void setDesMotivo(String desMotivo) {
		this.desMotivo = desMotivo;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(int tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getCodEstado() {
		return codEstado;
	}
	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}
	public String getCodMotivo() {
		return codMotivo;
	}
	public void setCodMotivo(String codMotivo) {
		this.codMotivo = codMotivo;
	}
	
	
				
}
	
