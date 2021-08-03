package cl.caserita.dto;
import java.io.Serializable;
public class DocgenelDTO implements Serializable{

	private int codigoEmpresa;
	private int codigoTipoMovimiento;
	private int fechaMovimiento;
	private int numeroDocumento;
	private int folioDocumento;
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getCodigoTipoMovimiento() {
		return codigoTipoMovimiento;
	}
	public void setCodigoTipoMovimiento(int codigoTipoMovimiento) {
		this.codigoTipoMovimiento = codigoTipoMovimiento;
	}
	public int getFechaMovimiento() {
		return fechaMovimiento;
	}
	public void setFechaMovimiento(int fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getFolioDocumento() {
		return folioDocumento;
	}
	public void setFolioDocumento(int folioDocumento) {
		this.folioDocumento = folioDocumento;
	}
	
}
