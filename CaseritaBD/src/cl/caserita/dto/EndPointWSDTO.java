package cl.caserita.dto;

import java.io.Serializable;

public class EndPointWSDTO implements Serializable{

	private String ipServidorWS;
	private String endPointWS;
	private String estadoEndPointWS;
	public String getIpServidorWS() {
		return ipServidorWS;
	}
	public void setIpServidorWS(String ipServidorWS) {
		this.ipServidorWS = ipServidorWS;
	}
	public String getEndPointWS() {
		return endPointWS;
	}
	public void setEndPointWS(String endPointWS) {
		this.endPointWS = endPointWS;
	}
	public String getEstadoEndPointWS() {
		return estadoEndPointWS;
	}
	public void setEstadoEndPointWS(String estadoEndPointWS) {
		this.estadoEndPointWS = estadoEndPointWS;
	}
	
}
