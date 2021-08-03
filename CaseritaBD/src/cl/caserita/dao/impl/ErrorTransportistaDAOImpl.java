package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.caserita.dao.iface.ErrorTransportistaDAO;
import cl.caserita.dto.ErrorTransportistaDTO;



public class ErrorTransportistaDAOImpl implements ErrorTransportistaDAO {
	
	private Connection conn;
	
	public ErrorTransportistaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	/*
	public List controladordeErrorCarguioTransp(int numcar, int codErr){
		
		String descErr="";
		List lista = new ArrayList();
		
		ErrorTransportistaDTO dto = new ErrorTransportistaDTO();
		
		try{
				
				if (codErr==1000) {
					descErr="Solicitud en 0";
				} else if (codErr==1001) {
					descErr="Rut de Chofer vacío";
				} else if (codErr==1002) {
					descErr="Dv Chofer vacío";
				} else if (codErr==1003) {
					descErr="Numero Carguío vacío";
				} else if (codErr==1004) {
					descErr="Numero Docto vacío";
				} else if (codErr==1005) {
					descErr="Tipo Docto vacío";
				} else if (codErr==1006) {
					descErr="Codigo Estado vacío";
				} else if (codErr==1007) {
					descErr="Descripcion Estado vacío";
				} else if (codErr==1008) {
					descErr="Codigo Motivo vacío";
				} else if (codErr==1009) {
					descErr="Descripcion Motivo vacío";
				} else if (codErr==1010) {
					descErr="Numero Carguio No existe";
				} else if (codErr==1011) {
					descErr="Rut Chofer No existe";
				} else if (codErr==1012) {
					descErr="Digito Rut Chofer Incorrecto";
				} else if (codErr==1013) {
					descErr="EstadoOV Carguio Incorrecto o No facturado";
				} else if (codErr==1014) {
					descErr="Numero Docto No existe";
				} else if (codErr==1015) {
					descErr="Tipo Docto No existe";
				} else if (codErr==1016) {
					descErr="Codigo Estado No existe";
				} else if (codErr==1017) {
					descErr="Codigo Motivo No existe";
				} else if (codErr==1018) {
					descErr="Codigo Artículo Vacío";
				} else if (codErr==1019) {
					descErr="Correlativo Vacío";
				} else if (codErr==1020) {
					descErr="Formato Artículo Vacío";
				} else if (codErr==1021) {
					descErr="Cantidad Artículo Vacío";
				} else if (codErr==1022) {
					descErr="Codigo Artículo No existe";
				} else if (codErr==1023) { 
					descErr="Formato Artículo No existe";
				} else if (codErr==1024) { 
					descErr="Ya tiene Estado Asignado para solicitud 2";
				} else if (codErr==1025) { 
					descErr="OV en cero";
				} else if (codErr==1026) { 
					descErr="Rut Cliente vacío";
				} else if (codErr==1027) { 
					descErr="DV Cliente vacío";
				} else if (codErr==1028) { 
					descErr="Ya tiene Redespacho";
				} else if (codErr==1029) { 
					descErr="No se ha generado Correlativo para NCp";
				} else {
					descErr="Error No exncontrado";
				}
				
				dto.setNumeroCarguio(numcar);
				dto.setDescripcionError(descErr);
				lista.add(dto);
				
		
		}catch(Exception e){
			e.printStackTrace();
		}finally {
	  } 
		
		
		
		
		return lista;
	}
	*/
	
	

	public List buscaErrorTransportista(int numeroCarg, int codigoError) {
		String descErr="";
		List lista = new ArrayList();
		
		ErrorTransportistaDTO dto = new ErrorTransportistaDTO();
		
		try{
				
				if (codigoError==1000) {
					descErr="Solicitud en 0";
				} else if (codigoError==1001) {
					descErr="Rut de Chofer vacio";
				} else if (codigoError==1002) {
					descErr="Dv Chofer vacio";
				} else if (codigoError==1003) {
					descErr="Numero Carguío vacio";
				} else if (codigoError==1004) {
					descErr="Numero Documento vacio";
				} else if (codigoError==1005) {
					descErr="Tipo Documento vacio";
				} else if (codigoError==1006) {
					descErr="Codigo Estado vacio";
				} else if (codigoError==1007) {
					descErr="Descripcion Estado vacio";
				} else if (codigoError==1008) {
					descErr="Codigo Motivo vacio";
				} else if (codigoError==1009) {
					descErr="Descripcion Motivo vacio";
				} else if (codigoError==1010) {
					descErr="Numero Carguio No existe";
				} else if (codigoError==1011) {
					descErr="Rut Chofer No existe";
				} else if (codigoError==1012) {
					descErr="Digito Rut Chofer Incorrecto";
				} else if (codigoError==1013) {
					descErr="EstadoOV Carguio Incorrecto o No facturado";
				} else if (codigoError==1014) {
					descErr="Numero Documento No existe";
				} else if (codigoError==1015) {
					descErr="Tipo Documento No existe";
				} else if (codigoError==1016) {
					descErr="Codigo Estado No existe";
				} else if (codigoError==1017) {
					descErr="Codigo Motivo No existe";
				} else if (codigoError==1018) {
					descErr="Codigo Artículo Vacio";
				} else if (codigoError==1019) {
					descErr="Correlativo Vacio";
				} else if (codigoError==1020) {
					descErr="Formato Artículo Vacio";
				} else if (codigoError==1021) {
					descErr="Cantidad Artículo Vacio";
				} else if (codigoError==1022) {
					descErr="Codigo Artículo No existe";
				} else if (codigoError==1023) { 
					descErr="Formato Artículo No existe";
				} else if (codigoError==1024) { 
					descErr="Ya tiene Estado Asignado para solicitud 2";
				} else if (codigoError==1025) { 
					descErr="OV en cero";
				} else if (codigoError==1026) { 
					descErr="Rut Cliente vacio";
				} else if (codigoError==1027) { 
					descErr="DV Cliente vacio";
				} else if (codigoError==1028) { 
					descErr="Ya tiene Redespacho";
				} else if (codigoError==1029) { 
					descErr="No se ha generado Correlativo para NCp";
				} else {
					descErr="Error No encontrado";
				}
				
				dto.setNumeroCarguio(numeroCarg);
				dto.setDescripcionError(descErr);
				lista.add(dto);
				
		
		}catch(Exception e){
			e.printStackTrace();
		}finally {
	  } 
		
		
		
		
		return lista;

	}
	


}
