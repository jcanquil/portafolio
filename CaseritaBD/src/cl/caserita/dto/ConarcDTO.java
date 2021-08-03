package cl.caserita.dto;
import java.io.Serializable;

public class ConarcDTO implements Serializable{

	private int CodDocumento;
	private int rutProveedor;
	private String digitoProveedor;
	private int numeroDocumento;
	private int fechaDocumento;
	private String nombreProveedor;
	private int valorNeto;
	private int valorNetoExento;
	private int valorTotalDocumento;
	private int folio;
	private int ano;
	private int mes;
	private int codigoEmpresa;
	private String direccion;
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getCodDocumento() {
		return CodDocumento;
	}
	public void setCodDocumento(int codDocumento) {
		CodDocumento = codDocumento;
	}
	public int getRutProveedor() {
		return rutProveedor;
	}
	public void setRutProveedor(int rutProveedor) {
		this.rutProveedor = rutProveedor;
	}
	public String getDigitoProveedor() {
		return digitoProveedor;
	}
	public void setDigitoProveedor(String digitoProveedor) {
		this.digitoProveedor = digitoProveedor;
	}
	public int getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public int getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	public String getNombreProveedor() {
		return nombreProveedor;
	}
	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}
	public int getValorNeto() {
		return valorNeto;
	}
	public void setValorNeto(int valorNeto) {
		this.valorNeto = valorNeto;
	}
	public int getValorNetoExento() {
		return valorNetoExento;
	}
	public void setValorNetoExento(int valorNetoExento) {
		this.valorNetoExento = valorNetoExento;
	}
	public int getValorTotalDocumento() {
		return valorTotalDocumento;
	}
	public void setValorTotalDocumento(int valorTotalDocumento) {
		this.valorTotalDocumento = valorTotalDocumento;
	}
	public int getFolio() {
		return folio;
	}
	public void setFolio(int folio) {
		this.folio = folio;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	
	
}
