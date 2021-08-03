package cl.caserita.dto;
import java.io.Serializable;
public class CarmailDTO implements Serializable{

	private int codigoEmpresa;
	private int codigoBodega;
	private String codigoCuentaCorreo;
	private String descripcionCuentaCorreo;
	private String estado;
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public String getCodigoCuentaCorreo() {
		return codigoCuentaCorreo;
	}
	public void setCodigoCuentaCorreo(String codigoCuentaCorreo) {
		this.codigoCuentaCorreo = codigoCuentaCorreo;
	}
	public String getDescripcionCuentaCorreo() {
		return descripcionCuentaCorreo;
	}
	public void setDescripcionCuentaCorreo(String descripcionCuentaCorreo) {
		this.descripcionCuentaCorreo = descripcionCuentaCorreo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
