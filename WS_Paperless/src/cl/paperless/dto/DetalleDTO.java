package cl.paperless.dto;

import java.io.Serializable;

import cl.paperless.boleta.BOLETADefType.Documento.Detalle;

public class DetalleDTO implements Serializable{

	private Detalle detalle;
	private double descuento;
	public Detalle getDetalle() {
		return detalle;
	}
	public void setDetalle(Detalle detalle) {
		this.detalle = detalle;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	
}
