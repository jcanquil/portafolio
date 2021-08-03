package cl.caserita.dto;
import java.io.Serializable;

public class DetordDTO implements Serializable{

	private int codEmpresa;
	private int numOvVenta;
	private int correDirecciones;
	private int codRegion;
	private int codCiudad;
	private int codComuna;
	private int codigoArticulo;
	private String formato;
	private int CantidadArticulo;
	private int cantidadFormato;
	private int correlativoDetalleOV;
	private int rutCliente;
	private String dvCliente;
	private int codigoBodega;
	private double precioBruto;
	private double precioNeto;
	private double costoBruto;
	private double costoNeto;
	private double costoTotal;
	private double montoBruto;
	private double montoNeto;
	private int totalNeto;
	private int montoTotal;
	private int fechaDespacho;
	private int correlativoDespacho;
	private String estado;
	private int region;
	private int ciudad;
	private int comuna;
	private String descripcionArticulo;
	private double descuento;
	private double descuentoNeto;
	private double porcentaje;
	private int montoExento;
	private int cantidadDespachada;
	private int numeroInternoVenta;
	
	public int getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(int montoExento) {
		this.montoExento = montoExento;
	}
	public double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public double getDescuentoNeto() {
		return descuentoNeto;
	}
	public void setDescuentoNeto(double descuentoNeto) {
		this.descuentoNeto = descuentoNeto;
	}
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	public double getPrecioBruto() {
		return precioBruto;
	}
	public void setPrecioBruto(double precioBruto) {
		this.precioBruto = precioBruto;
	}
	public double getPrecioNeto() {
		return precioNeto;
	}
	public void setPrecioNeto(double precioNeto) {
		this.precioNeto = precioNeto;
	}
	public double getCostoBruto() {
		return costoBruto;
	}
	public void setCostoBruto(double costoBruto) {
		this.costoBruto = costoBruto;
	}
	public double getCostoNeto() {
		return costoNeto;
	}
	public void setCostoNeto(double costoNeto) {
		this.costoNeto = costoNeto;
	}
	public double getCostoTotal() {
		return costoTotal;
	}
	public void setCostoTotal(double costoTotal) {
		this.costoTotal = costoTotal;
	}
	public double getMontoBruto() {
		return montoBruto;
	}
	public void setMontoBruto(double montoBruto) {
		this.montoBruto = montoBruto;
	}
	public double getMontoNeto() {
		return montoNeto;
	}
	public void setMontoNeto(double montoNeto) {
		this.montoNeto = montoNeto;
	}
	public int getTotalNeto() {
		return totalNeto;
	}
	public void setTotalNeto(int totalNeto) {
		this.totalNeto = totalNeto;
	}
	public int getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(int montoTotal) {
		this.montoTotal = montoTotal;
	}
	public int getFechaDespacho() {
		return fechaDespacho;
	}
	public void setFechaDespacho(int fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}
	public int getCorrelativoDespacho() {
		return correlativoDespacho;
	}
	public void setCorrelativoDespacho(int correlativoDespacho) {
		this.correlativoDespacho = correlativoDespacho;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	public int getCiudad() {
		return ciudad;
	}
	public void setCiudad(int ciudad) {
		this.ciudad = ciudad;
	}
	public int getComuna() {
		return comuna;
	}
	public void setComuna(int comuna) {
		this.comuna = comuna;
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
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getCorrelativoDetalleOV() {
		return correlativoDetalleOV;
	}
	public void setCorrelativoDetalleOV(int correlativoDetalleOV) {
		this.correlativoDetalleOV = correlativoDetalleOV;
	}
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public int getCantidadArticulo() {
		return CantidadArticulo;
	}
	public void setCantidadArticulo(int cantidadArticulo) {
		CantidadArticulo = cantidadArticulo;
	}
	public int getCantidadFormato() {
		return cantidadFormato;
	}
	public void setCantidadFormato(int cantidadFormato) {
		this.cantidadFormato = cantidadFormato;
	}
	public int getCodEmpresa() {
		return codEmpresa;
	}
	public void setCodEmpresa(int codEmpresa) {
		this.codEmpresa = codEmpresa;
	}
	public int getNumOvVenta() {
		return numOvVenta;
	}
	public void setNumOvVenta(int numOvVenta) {
		this.numOvVenta = numOvVenta;
	}
	public int getCorreDirecciones() {
		return correDirecciones;
	}
	public void setCorreDirecciones(int correDirecciones) {
		this.correDirecciones = correDirecciones;
	}
	public int getCodRegion() {
		return codRegion;
	}
	public void setCodRegion(int codRegion) {
		this.codRegion = codRegion;
	}
	public int getCodCiudad() {
		return codCiudad;
	}
	public void setCodCiudad(int codCiudad) {
		this.codCiudad = codCiudad;
	}
	public int getCodComuna() {
		return codComuna;
	}
	public void setCodComuna(int codComuna) {
		this.codComuna = codComuna;
	}
	
	public int getCantidadDespachada() {
		return cantidadDespachada;
	}
	public void setCantidadDespachada(int cantidadDespachada) {
		this.cantidadDespachada = cantidadDespachada;
	}
	
	public int getNumeroInternoVenta(){
		return numeroInternoVenta;
	}
	public void setNumeroInternoVenta(int numeroInternoVenta){
		this.numeroInternoVenta = numeroInternoVenta;
	}
	
}
