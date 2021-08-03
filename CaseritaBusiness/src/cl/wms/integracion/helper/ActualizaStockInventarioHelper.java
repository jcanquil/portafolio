package cl.caserita.wms.integracion.helper;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dto.LogestinDTO;
import cl.caserita.dto.StockinventarioDTO;

public class ActualizaStockInventarioHelper {

	public int actualizaEstado(int empresa, int bodega, String estado, int articulo, String dv, int cantidad,StockinventarioDAO stock, String nombre, int correlativo){
		int resu=0;
		int detalle=0;
		LogestinDTO dtoLog = new LogestinDTO();
		Fecha fch = new Fecha();
		StockinventarioDTO dto = stock.lista(empresa, bodega, estado, articulo);
		int actual = 0;
		if (dto!=null){
			actual=dto.getCantidad();
			/*
			if (dto.getCantidad()>cantidad){
				dto.setCantidad(dto.getCantidad()-cantidad);
			}else{
				dto.setCantidad(0);

			}
			*/
			dto.setCantidad(dto.getCantidad()-cantidad);
			
			stock.actualizaStock(dto);
			//Genera LOG
			dtoLog.setCodigoEmpresa(empresa);
			dtoLog.setCodigoBodega(bodega);
			dtoLog.setCodigoArticulo(articulo);
			dtoLog.setNombreArchivoXML(nombre);
			dtoLog.setNumeroLineaDetalle(correlativo);
			dtoLog.setCodigoEstadoInventario(estado);
			dtoLog.setStockActual(actual);
			dtoLog.setStockInformado(cantidad);
			dtoLog.setStockLinea(dto.getCantidad());
			dtoLog.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
			String fechatime = fch.getTimestamp().toString().replaceAll("-", "");
			fechatime = fechatime.replace(".", "");
			fechatime = fechatime.replace(" ", "");
			fechatime = fechatime.replace(":", "");

			dtoLog.setHoraUsuario(Long.parseLong(fechatime));
			stock.generaLoginventario(dtoLog);
		}else{
			dto = new StockinventarioDTO();

			dto.setCodigoEmpresa(empresa);
			dto.setCodigoBodega(bodega);
			dto.setEstado(estado);
			dto.setCodigoArticulo(articulo);
			dto.setCantidad(cantidad);
			dto.setDvArticulo(dv);
			stock.creaStockInvWMS(dto);
			
			//Genera LOG
			dtoLog.setCodigoEmpresa(empresa);
			dtoLog.setCodigoBodega(bodega);
			dtoLog.setCodigoArticulo(articulo);
			dtoLog.setNombreArchivoXML(nombre);
			dtoLog.setCodigoEstadoInventario(estado);

			dtoLog.setNumeroLineaDetalle(correlativo);
			dtoLog.setStockActual(actual);
			dtoLog.setStockInformado(cantidad);
			dtoLog.setStockLinea(dto.getCantidad());
			dtoLog.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
			String fechatime = fch.getTimestamp().toString().replaceAll("-", "");
			fechatime = fechatime.replace(".", "");
			fechatime = fechatime.replace(" ", "");
			fechatime = fechatime.replace(":", "");

			dtoLog.setHoraUsuario(Long.parseLong(fechatime));
			stock.generaLoginventario(dtoLog);
		}
		
		return resu;
	}
	
	public int actualizaEstadoxEstado(int empresa, int bodega, String toEstado, int articulo, String dv, int cantidad,StockinventarioDAO stock, String nombre, int correlativo){
		int resu=0;
		LogestinDTO dtoLog = new LogestinDTO();
		Fecha fch = new Fecha();

		StockinventarioDTO dto = stock.lista(empresa, bodega, toEstado, articulo);
		int actual = 0;

		if (dto!=null){
			actual=dto.getCantidad();
			/*
			if (dto.getCantidad()>cantidad){
				dto.setCantidad(dto.getCantidad()+cantidad);
			}
			*/
			dto.setCantidad(dto.getCantidad()+cantidad);
			stock.actualizaStock(dto);
			//Genera LOG
			dtoLog.setCodigoEmpresa(empresa);
			dtoLog.setCodigoBodega(bodega);
			dtoLog.setCodigoArticulo(articulo);
			dtoLog.setNombreArchivoXML(nombre);
			dtoLog.setCodigoEstadoInventario(toEstado);

			dtoLog.setNumeroLineaDetalle(correlativo);
			dtoLog.setStockActual(actual);
			dtoLog.setStockInformado(cantidad);
			dtoLog.setStockLinea(dto.getCantidad());
			dtoLog.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
			String fechatime = fch.getTimestamp().toString().replaceAll("-", "");
			fechatime = fechatime.replace(".", "");
			fechatime = fechatime.replace(" ", "");
			fechatime = fechatime.replace(":", "");

			dtoLog.setHoraUsuario(Long.parseLong(fechatime));

			stock.generaLoginventario(dtoLog);
		}else{
			dto = new StockinventarioDTO();
			dto.setCodigoEmpresa(empresa);
			dto.setCodigoBodega(bodega);
			dto.setEstado(toEstado);
			dto.setCodigoArticulo(articulo);
			dto.setCantidad(cantidad);
			dto.setDvArticulo(dv);

			stock.creaStockInvWMS(dto);
			//Genera LOG
			dtoLog.setCodigoEmpresa(empresa);
			dtoLog.setCodigoBodega(bodega);
			dtoLog.setCodigoArticulo(articulo);
			dtoLog.setCodigoEstadoInventario(toEstado);

			dtoLog.setNombreArchivoXML(nombre);
			dtoLog.setNumeroLineaDetalle(correlativo);
			dtoLog.setStockActual(actual);
			dtoLog.setStockInformado(cantidad);
			dtoLog.setStockLinea(dto.getCantidad());
			dtoLog.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
			String fechatime = fch.getTimestamp().toString().replaceAll("-", "");
			fechatime = fechatime.replace(".", "");
			fechatime = fechatime.replace(" ", "");
			fechatime = fechatime.replace(":", "");

			dtoLog.setHoraUsuario(Long.parseLong(fechatime));
			stock.generaLoginventario(dtoLog);
		}
		
		return resu;
	}
	public static void main (String []args){
		Fecha fch = new Fecha();
		String fecha = fch.getTimestamp().toString().replaceAll("-", "");
		fecha = fecha.replace(".", "");
		fecha = fecha.replace(" ","");
		fecha = fecha.replace(":", "");
		long num  = Long.parseLong(fecha);
		
		System.out.println("num:"+fch.getTime());
	}
}
