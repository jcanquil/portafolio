package cl.caserita.wms.dto;
import java.io.Serializable;
public class ConfirmacionDTO implements Serializable{

	private String xml;
	private String nameFile;
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getNameFile() {
		return nameFile;
	}
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
	
	
}
