package cl.caserita.dto;
import java.io.Serializable;
import java.util.List;
public class CarguioConfirmacionDTO implements Serializable{

	private int numeroCarguioTransferencia;
	private String anden;
	private int numeroCarguio;
	private List<ConfirmacionCarguioDTO> ordenes;
	public int getNumeroCarguioTransferencia() {
		return numeroCarguioTransferencia;
	}
	public void setNumeroCarguioTransferencia(int numeroCarguioTransferencia) {
		this.numeroCarguioTransferencia = numeroCarguioTransferencia;
	}
	public String getAnden() {
		return anden;
	}
	public void setAnden(String anden) {
		this.anden = anden;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public List getOrdenes() {
		return ordenes;
	}
	public void setOrdenes(List ordenes) {
		this.ordenes = ordenes;
	}
	
	
}
