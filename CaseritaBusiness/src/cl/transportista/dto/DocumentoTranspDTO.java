package cl.caserita.transportista.dto;

import java.io.Serializable;
import java.util.List;

public class DocumentoTranspDTO implements Serializable {
	
	private int 	rutChofer;
	private String	dvChofer;
	private String	solicitud;
	
	private List carguio;

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

	public String getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	public List getCarguio() {
		return carguio;
	}

	public void setCarguio(List carguio) {
		this.carguio = carguio;
	}
	
	
	
	
	
	

}
