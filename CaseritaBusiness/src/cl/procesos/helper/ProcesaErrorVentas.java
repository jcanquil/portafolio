package cl.caserita.procesos.helper;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.DocerrorDAO;
import cl.caserita.dto.DetalleDTO;
import cl.caserita.dto.DocerrorDTO;
import cl.caserita.dto.DocumentosErrorDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.procesaXML.LeeXMLVentas;

public class ProcesaErrorVentas {

	public static void main (String[]args){
		DAOFactory dao = DAOFactory.getInstance();
		DocerrorDTO dto = null;
		int cantidad =0;
		int proceso =190;
	/*	DocerrorDAO errorDAO = dao.getDocerrorDAO();
		List lista = errorDAO.listaDocumentosError();
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			cantidad=cantidad+1;
			 dto = (DocerrorDTO) iter.next();
			 LeeXMLVentas lee = new LeeXMLVentas();
			 DocumentosErrorDTO dtoError = lee.main(dto.getUrlXML());
			 VecmarDTO vecmar = new VecmarDTO();
			 vecmar.setCodTipoMvto(dto.getTipoMov());
			 vecmar.setFechaMvto(dto.getFecha());
			 vecmar.setNumDocumento(dto.getNumero());
			 vecmar.setCodigoBodega(9999);
			 int indice = dtoError.getRutReceptor().length();
			 int fin = indice -2;
			 String rut = dtoError.getRutReceptor().substring(0, fin);
			 String dv = dtoError.getRutReceptor().substring(dtoError.getRutReceptor().length()-1, dtoError.getRutReceptor().length());
			 
			 vecmar.setRutProveedor(rut.trim());
			 
			 vecmar.setDvProveedor(dv.trim());
			 vecmar.setSwichProceso(1);
			 vecmar.setCodigoVendedor(9999);
			 vecmar.setCodigoTipoVendedor(9999);
			 vecmar.setCodigoDocumento(dtoError.getCodDocumento());
			 vecmar.setNumeroTipoDocumento(dto.getNumeroDocumento());
			 vecmar.setCantidadLineaDetalle(dtoError.getCantidadLineas());
			 vecmar.setFormaPago(String.valueOf(dtoError.getFormaPago()));
			 vecmar.setTotalBruto(0);
			 vecmar.setTotalDescuento(0);
			 vecmar.setTotalImptoAdicional(0);
			 vecmar.setTotalDocumento(dtoError.getTotalDocumento());
			 vecmar.setTotalNeto(dtoError.getTotalNeto());
			 vecmar.setTotalIva(dtoError.getTotalIva());
			errorDAO.insertaVecmar(vecmar);
			 Iterator it = dtoError.getListaDetalle().iterator();
			 while (it.hasNext()){
				 DetalleDTO detalle = (DetalleDTO) it.next();
				 VedmarDTO vedmar = new VedmarDTO();
				  indice = detalle.getCodigoArticulo().length();
				  fin = indice -2;
				  rut = detalle.getCodigoArticulo().substring(0, fin);
				  dv = detalle.getCodigoArticulo().substring(detalle.getCodigoArticulo().length()-1, detalle.getCodigoArticulo().length());
				 vedmar.setCodigoArticulo(Integer.parseInt(rut.trim()));
				 vedmar.setCantidadArticulo(detalle.getCantidad());
				 vedmar.setCantidadFormato(detalle.getCantidad());
				 vedmar.setDigArticulo(dv.trim());
				 vedmar.setMontoBrutoLinea(0);
				 if (dtoError.getCodDocumento()==41 || dtoError.getCodDocumento()==34){
					 vedmar.setMontoExento(detalle.getMontoNeto());
					 vedmar.setMontoTotalLinea(detalle.getMontoNeto());
					 vedmar.setMontoBrutoLinea(detalle.getMontoNeto());
					 vedmar.setMontoDescuentoNeto(0);
					 vedmar.setMontoDescuentoLinea(detalle.getMontoDescuento());
					 vedmar.setMontoTotalNetoLinea(0);
					 vedmar.setMontoNeto(0);
					 vedmar.setPrecioUnidad(detalle.getPrecioNeto());
					 vedmar.setPrecioNeto(detalle.getPrecioNeto());

				 }else if (dtoError.getCodDocumento()==39 ){
					 vedmar.setMontoExento(0);
					 vedmar.setMontoTotalLinea(detalle.getMontoNeto());
					 vedmar.setMontoBrutoLinea(detalle.getMontoNeto());
					 vedmar.setMontoDescuentoNeto(0);
					 vedmar.setMontoDescuentoLinea(detalle.getMontoDescuento());
					 vedmar.setMontoTotalNetoLinea(0);
					 vedmar.setMontoNeto(0);
					 vedmar.setPrecioUnidad(detalle.getPrecioNeto());
					 vedmar.setPrecioNeto(0);


					 
				 }else{
					 vedmar.setMontoNeto(detalle.getMontoNeto());
					 vedmar.setMontoTotalNetoLinea(detalle.getMontoNeto());
					 vedmar.setMontoDescuentoNeto(detalle.getMontoDescuento());
					 vedmar.setMontoExento(0);
					 vedmar.setMontoTotalLinea(0);
					 vedmar.setPrecioNeto(detalle.getPrecioNeto());
					 vedmar.setPrecioUnidad(detalle.getPrecioBruto());


				 }
				 vedmar.setCodigoBodega(9999);
				 vedmar.setCorrelativo(detalle.getNumeroLinea());
				 vedmar.setCodTipoMvto(dto.getTipoMov());
				 vedmar.setFechaMvto(dto.getFecha());
				 vedmar.setNumDocumento(dto.getNumero());
				 vedmar.setFormato("U");
				errorDAO.insertaVedmar(vedmar);
			 }
		}*/
		
		System.out.println("Termino:"+cantidad);
		
	}
}
