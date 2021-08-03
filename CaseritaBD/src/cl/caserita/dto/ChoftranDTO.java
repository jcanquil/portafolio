package cl.caserita.dto;
import java.io.Serializable;

public class ChoftranDTO implements Serializable{

	private int rutEmpresa;
	private String dvEmpresa;
	private int rutChofer;
	private String dvChofer;
	private String	nomChofer;

	
	
	public String getNomChofer() {
		return nomChofer;
	}
	public void setNomChofer(String nomChofer) {
		this.nomChofer = nomChofer;
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
	public int getRutChofer() {
		return rutChofer;
	}
	public void setRutChofer(int rutChofer) {
		this.rutChofer = rutChofer;
	}
	public String getDvChofer() {
		return dvChofer;
	}
	public void setDvChofer(String dvChofer) {
		this.dvChofer = dvChofer;
	}
	
	
}
