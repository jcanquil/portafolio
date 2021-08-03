package cl.caserita.dto;
import java.io.Serializable;

public class CarguiodDTO implements Serializable{

	private int codigoEmpresa;
	private int numeroCarguio;
	private int numeroOrden;
	private int numeroTraspaso;
	private int codigoBodega;
	private int correlativo;
	private int codigoArticulo;
	private int cantidad;
	private int correlativoDireccion;
	private int rutCliente;
	private String dvCliente;
	private String razonSocial;
	private int codigoRegion;
	private int codigoCiudad;
	private int codigoComuna;
	private String descripcionRegion;
	private String descripcionCiudad;
	private String descripcionComuna;
	private String patente;
	private int fechaExpiracion;
	private int numeroGuia;
	private int numeroFactura;
	private int correlativoOV;
	private int fechaDespacho;
	private int codigoBodegaOrigenOT;
	private int codigoBodegaDestinoOT;
	private String formatoArt;
	
	public int getCorrelativoOV() {
		return correlativoOV;
	}
	public void setCorrelativoOV(int correlativoOV) {
		this.correlativoOV = correlativoOV;
	}
	public int getNumeroGuia() {
		return numeroGuia;
	}
	public void setNumeroGuia(int numeroGuia) {
		this.numeroGuia = numeroGuia;
	}
	public int getNumeroFactura() {
		return numeroFactura;
	}
	public void setNumeroFactura(int numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	public int getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(int fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public int getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public int getNumeroTraspaso() {
		return numeroTraspaso;
	}
	public void setNumeroTraspaso(int numeroTraspaso) {
		this.numeroTraspaso = numeroTraspaso;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getCorrelativoDireccion() {
		return correlativoDireccion;
	}
	public void setCorrelativoDireccion(int correlativoDireccion) {
		this.correlativoDireccion = correlativoDireccion;
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
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public int getCodigoRegion() {
		return codigoRegion;
	}
	public void setCodigoRegion(int codigoRegion) {
		this.codigoRegion = codigoRegion;
	}
	public int getCodigoCiudad() {
		return codigoCiudad;
	}
	public void setCodigoCiudad(int codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}
	public int getCodigoComuna() {
		return codigoComuna;
	}
	public void setCodigoComuna(int codigoComuna) {
		this.codigoComuna = codigoComuna;
	}
	public String getDescripcionRegion() {
		return descripcionRegion;
	}
	public void setDescripcionRegion(String descripcionRegion) {
		this.descripcionRegion = descripcionRegion;
	}
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}
	public String getDescripcionComuna() {
		return descripcionComuna;
	}
	public void setDescripcionComuna(String descripcionComuna) {
		this.descripcionComuna = descripcionComuna;
	}
	public int getFechaDespacho() {
		return fechaDespacho;
	}
	public void setFechaDespacho(int fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}
	
	public int getCodigoBodegaOrigenOT(){
		return codigoBodegaOrigenOT;
	}
	public void setCodigoBodegaOrigenOT(int codigoBodegaOrigenOT){
		this.codigoBodegaOrigenOT = codigoBodegaOrigenOT;
	}
	
	public int getCodigoBodegaDestinoOT(){
		return codigoBodegaDestinoOT;
	}
	public void setCodigoBodegaDestinoOT(int codigoBodegaDestinoOT){
		this.codigoBodegaDestinoOT = codigoBodegaDestinoOT;
	}
	public void setFormatoArt(String formatoArt) {
		this.formatoArt = formatoArt;
	}
	public String getFormatoArt() {
		return formatoArt;
	}
}
