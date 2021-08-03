package cl.caserita.dto;
import java.io.Serializable;

public class EmaapmDTO implements Serializable {
	private String codApp;
	private String email;
	public String getCodApp() {
		return codApp;
	}
	public void setCodApp(String codApp) {
		this.codApp = codApp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
