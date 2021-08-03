package cl.caserita.dto;
import java.io.Serializable;
import java.util.List;

public class DocumentosErrorDTO implements Serializable{

	private int codDocumento;
	private int folio;
	private String fechaDocumento;
	private int formaPago;
	private String rutReceptor;
	
	private List<ClcdiaDTO> lista;
	private List<DetalleDTO> listaDetalle;
	private int totalDocumento;
	private int totalNeto;
	private int montoExento;
	private int cantidadLineas;
	private int totalIva;
	
	
	
	public int getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(int totalIva) {
		this.totalIva = totalIva;
	}
	public int getCantidadLineas() {
		return cantidadLineas;
	}
	public void setCantidadLineas(int cantidadLineas) {
		this.cantidadLineas = cantidadLineas;
	}
	public int getMontoExento() {
		return montoExento;
	}
	public void setMontoExento(int montoExento) {
		this.montoExento = montoExento;
	}
	public int getTotalDocumento() {
		return totalDocumento;
	}
	public void setTotalDocumento(int totalDocumento) {
		this.totalDocumento = totalDocumento;
	}
	public int getTotalNeto() {
		return totalNeto;
	}
	public void setTotalNeto(int totalNeto) {
		this.totalNeto = totalNeto;
	}
	public int getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(int codDocumento) {
		this.codDocumento = codDocumento;
	}
	public int getFolio() {
		return folio;
	}
	public void setFolio(int folio) {
		this.folio = folio;
	}
	public String getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	public int getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(int formaPago) {
		this.formaPago = formaPago;
	}
	public String getRutReceptor() {
		return rutReceptor;
	}
	public void setRutReceptor(String rutReceptor) {
		this.rutReceptor = rutReceptor;
	}
	public List<ClcdiaDTO> getLista() {
		return lista;
	}
	public void setLista(List<ClcdiaDTO> lista) {
		this.lista = lista;
	}
	public List<DetalleDTO> getListaDetalle() {
		return listaDetalle;
	}
	public void setListaDetalle(List<DetalleDTO> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}
	
}
