package cl.caserita.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

;

public class CarguioDTO implements Serializable{
	
	private int numeroCarguio;
	private int numeroRuta;
	private int codigoBodega;
	private int fechaCarguio;
	private List<OrdvtaDTO> ordenes;
	private int codigoEmpresa;
	private int fechaCreacion;
	private String estado;
	private String estado2;
	private int horaLlegada;
	private String patente;
	private List<CarguiodDTO> carguioD;
	private int rutChofer;
	private String dvChofer;
	private String estadoInvWMS;
	private String tipoPedidoWMS;
	private int numeroCarguioTransf;
	private int codigoBodOrigen;
	private int codigoBodDestino;
	private int numeroOT;
	
	private List<ExmtraDTO> ordenesOT;
	
	public int getRutChofer() {
		return rutChofer;
	}
	public void setRutChofer(int rutChofer) {
		this.rutChofer = rutChofer;
	}
	public String getDvChofer() {
		return dvChofer;
	}
	public void setDvChofer(String dvChofer) {
		this.dvChofer = dvChofer;
	}
	public List<CarguiodDTO> getCarguioD() {
		return carguioD;
	}
	public void setCarguioD(List<CarguiodDTO> carguioD) {
		this.carguioD = carguioD;
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
	public int getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(int fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getEstado2() {
		return estado2;
	}
	public void setEstado2(String estado2) {
		this.estado2 = estado2;
	}
	public int getHoraLlegada() {
		return horaLlegada;
	}
	public void setHoraLlegada(int horaLlegada) {
		this.horaLlegada = horaLlegada;
	}
	public List<OrdvtaDTO> getOrdenes() {
		if (ordenes==null){
			ordenes = new ArrayList<OrdvtaDTO>();
		}
		return ordenes;
	}
	public void setOrdenes(List<OrdvtaDTO> ordenes) {
		this.ordenes = ordenes;
	}
	
	public List<ExmtraDTO> getOrdenesOT(){
		if (ordenesOT==null){
			ordenesOT = new ArrayList<ExmtraDTO>();
		}
		return ordenesOT;
	}
	public void setOrdenesOT(List<ExmtraDTO> ordenesOT) {
		this.ordenesOT = ordenesOT;
	}
	
	public int getNumeroCarguio() {
		return numeroCarguio;
	}
	public void setNumeroCarguio(int numeroCarguio) {
		this.numeroCarguio = numeroCarguio;
	}
	public int getNumeroRuta() {
		return numeroRuta;
	}
	public void setNumeroRuta(int numeroRuta) {
		this.numeroRuta = numeroRuta;
	}
	public int getCodigoBodega() {
		return codigoBodega;
	}
	public void setCodigoBodega(int codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	public int getFechaCarguio() {
		return fechaCarguio;
	}
	public void setFechaCarguio(int fechaCarguio) {
		this.fechaCarguio = fechaCarguio;
	}
	
	public String getEstadoInvWMS() {
		return estadoInvWMS;
	}
	public void setEstadoInvWMS(String estadoInvWMS) {
		this.estadoInvWMS = estadoInvWMS;
	}
	
	public String getTipoPedidoWMS() {
		return tipoPedidoWMS;
	}
	public void setTipoPedidoWMS(String tipoPedidoWMS) {
		this.tipoPedidoWMS = tipoPedidoWMS;
	}
	
	public int getnumeroCarguioTransf() {
		return numeroCarguioTransf;
	}
	public void setnumeroCarguioTransf(int numeroCarguioTransf) {
		this.numeroCarguioTransf = numeroCarguioTransf;
	}
	
	public int getcodigoBodOrigen() {
		return codigoBodOrigen;
	}
	public void setcodigoBodOrigen(int codigoBodOrigen) {
		this.codigoBodOrigen = codigoBodOrigen;
	}
	
	public int getcodigoBodDestino() {
		return codigoBodDestino;
	}
	public void setcodigoBodDestino(int codigoBodDestino) {
		this.codigoBodDestino = codigoBodDestino;
	}
	
	public int getnumeroOT() {
		return numeroOT;
	}
	public void setnumeroOT(int numeroOT) {
		this.numeroOT = numeroOT;
	}
}
