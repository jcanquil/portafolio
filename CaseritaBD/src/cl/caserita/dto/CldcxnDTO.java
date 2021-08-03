package cl.caserita.dto;
import java.io.Serializable;
public class CldcxnDTO implements Serializable{

	private int codigoEmpresa;
	private int rutCliente;
	private String dvCliente;
	private int codigoBanco;
	private String numeroCuenta;
	private int numeroCheque;
	private int tipoMovimiento;
	private int fechaMovimiento;
	private int numeroDocumento;
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(int rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getDvCliente() {
		return dvCliente;
	}
	public void setDvCliente(String dvCliente) {
		this.dvCliente = dvCliente;
	}
	public int getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(int codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public int getNumeroCheque() {
		return numeroCheque;
	}
	public void setNumeroCheque(int numeroCheque) {
		this.numeroCheque = numeroCheque;
	}
	public int getTipoMovimiento() {
		return tipoMovimiento;
	}
	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
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
	
	
}
