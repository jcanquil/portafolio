package cl.caserita.mail.dto;
import java.io.Serializable;

public class EmailDTO implements Serializable{

	private String appMail;
	private String email;
	public String getAppMail() {
		return appMail;
	}
	public void setAppMail(String appMail) {
		this.appMail = appMail;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
