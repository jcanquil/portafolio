package cl.caserita.informe.dto;

import java.io.Serializable;

public class ExmgesDTO implements Serializable{

	private String codBodega;
	private String codArticulo;
	private String descArticulo;
	private int stockComputacional;
	private int costo;
	private Double total;
	private String descripcion1;
	private String descFamilia;
	private String descripcion2;
	private int rut;
	private String razonSocial;
	
	public String getCodBodega() {
		return codBodega;
	}
	public void setCodBodega(String codBodega) {
		this.codBodega = codBodega;
	}
	public String getCodArticulo() {
		return codArticulo;
	}
	public void setCodArticulo(String codArticulo) {
		this.codArticulo = codArticulo;
	}
	public String getDescArticulo() {
		return descArticulo;
	}
	public void setDescArticulo(String descArticulo) {
		this.descArticulo = descArticulo;
	}
	public int getStockComputacional() {
		return stockComputacional;
	}
	public void setStockComputacional(int stockComputacional) {
		this.stockComputacional = stockComputacional;
	}
	public int getCosto() {
		return costo;
	}
	public void setCosto(int costo) {
		this.costo = costo;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getDescripcion1() {
		return descripcion1;
	}
	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}
	public String getDescFamilia() {
		return descFamilia;
	}
	public void setDescFamilia(String descFamilia) {
		this.descFamilia = descFamilia;
	}
	public String getDescripcion2() {
		return descripcion2;
	}
	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	
	
	
}
