package cl.caserita.dto;
import java.io.Serializable;

public class DocGastoDTO implements Serializable{

	private int rut;
	private int numeroDoc;
	private int monto;
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public int getNumeroDoc() {
		return numeroDoc;
	}
	public void setNumeroDoc(int numeroDoc) {
		this.numeroDoc = numeroDoc;
	}
	public int getMonto() {
		return monto;
	}
	public void setMonto(int monto) {
		this.monto = monto;
	}
	
	
}
