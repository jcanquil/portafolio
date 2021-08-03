package cl.caserita.dto;
import java.io.Serializable;
import java.util.List;

public class DetordTranspNcpDTO implements Serializable{

	private String 	correlativo;
	private String 	codigoArticulo;
	private String 	descripcionArticulo;
	private String 	digitoArticulo;
	private String 	formato;
	private int 	cantidad;
	private int 	cantidadrecepcionada;
	
	private double 	precioUnitario;
	private double 	precioNeto;
	private double 	costoArticulo;
	private double 	costoNetoUnitario;
	private double 	costoTotalNeto;
	private int 	totalDescuento;
	private int 	totalDescuentoNeto;
	private double	porcentajeDescuento;
	private int	    totalBruto;
	private int	    totalNeto;
	private double  montototalNeto;
	private int		totalExento;
	private String	combo;
	private String  foto;
	private String comentario;
	
	
	
	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getCombo() {
		return combo;
	}
	public void setCombo(String combo) {
		this.combo = combo;
	}
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}
	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
	
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public double getPrecioNeto() {
		return precioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		this.precioNeto = precioNeto;
	}
	public double getCostoArticulo() {
		return costoArticulo;
	}
	public void setCostoArticulo(double costoArticulo) {
		this.costoArticulo = costoArticulo;
	}
	public double getCostoNetoUnitario() {
		return costoNetoUnitario;
	}
	public void setCostoNetoUnitario(double costoNetoUnitario) {
		this.costoNetoUnitario = costoNetoUnitario;
	}
	public double getCostoTotalNeto() {
		return costoTotalNeto;
	}
	public void setCostoTotalNeto(double costoTotalNeto) {
		this.costoTotalNeto = costoTotalNeto;
	}
	public int getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(int totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public int getTotalDescuentoNeto() {
		return totalDescuentoNeto;
	}
	public void setTotalDescuentoNeto(int totalDescuentoNeto) {
		this.totalDescuentoNeto = totalDescuentoNeto;
	}
	public int getTotalBruto() {
		return totalBruto;
	}
	public void setTotalBruto(int totalBruto) {
		this.totalBruto = totalBruto;
	}
	public int getTotalNeto() {
		return totalNeto;
	}
	public void setTotalNeto(int totalNeto) {
		this.totalNeto = totalNeto;
	}
	public double getMontototalNeto() {
		return montototalNeto;
	}
	public void setMontototalNeto(double montototalNeto) {
		this.montototalNeto = montototalNeto;
	}
	public int getTotalExento() {
		return totalExento;
	}
	public void setTotalExento(int totalExento) {
		this.totalExento = totalExento;
	}
	
	public String getDigitoArticulo() {
		return digitoArticulo;
	}
	public void setDigitoArticulo(String digitoArticulo) {
		this.digitoArticulo = digitoArticulo;
	}
	public int getCantidadrecepcionada() {
		return cantidadrecepcionada;
	}
	public void setCantidadrecepcionada(int cantidadrecepcionada) {
		this.cantidadrecepcionada = cantidadrecepcionada;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}
