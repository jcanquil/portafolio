package cl.caserita.procesos.helper;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CajasDAO;
import cl.caserita.dao.iface.CartolasDAO;
import cl.caserita.dto.CartolasDTO;

public class ProcesaConciliacion {

	public void procesaCheque(){
		
		//Procesa conciliacion de tablas CAJAS y CARTOLAS
		DAOFactory dao = DAOFactory.getInstance();
		CajasDAO cajas = dao.getCajasDAO();
		CartolasDAO cartolas = dao.getCartolasDAO();
		
		List listaCartola = cartolas.listaCartolasCheque();
		Iterator iter = listaCartola.iterator();
		while (iter.hasNext()){
			CartolasDTO dto = (CartolasDTO) iter.next();
			//Busca en CAJAS
			if (dto.getValorAbsoluto()!=null && !"".equals(dto.getValorAbsoluto()) ){
				if (!"0".equals(dto.getNumDocumento().trim()) && !"".equals(dto.getNumDocumento().trim())){
					System.out.println("valor:"+dto.getValorAbsoluto());
					System.out.println("numeroDocumento:"+dto.getNumDocumento());
					if (isNumeric(dto.getNumDocumento().trim())){
						 if (isNumeric(dto.getValorAbsoluto().replace(".","").trim())){
							int id = cajas.buscaConciliadoCheque(dto.getSucursalHomologada().trim(), Integer.parseInt(dto.getNumDocumento().trim()), dto.getValorAbsoluto().replace(".", ""));
							if (id>0){
								//Asocia contraparte de la conciliacion en tabla CARTOLA y CAJA
								cartolas.actualizaConcilia(dto.getCorrelativoCartola(),id);
								cajas.actualizaConcilia(id, dto.getCorrelativoCartola());
							}
						}
					}
					
				
				}
			}
			
			
			
		}
		System.out.println("Termino");
		
	}
	
	public void procesaChequeParcial(){
		
		//Procesa conciliacion de tablas CAJAS y CARTOLAS
		DAOFactory dao = DAOFactory.getInstance();
		CajasDAO cajas = dao.getCajasDAO();
		CartolasDAO cartolas = dao.getCartolasDAO();
		
		List listaCartola = cartolas.listaCartolasCheque();
		Iterator iter = listaCartola.iterator();
		while (iter.hasNext()){
			CartolasDTO dto = (CartolasDTO) iter.next();
			//Busca en CAJAS
			if (dto.getValorAbsoluto()!=null && !"".equals(dto.getValorAbsoluto()) ){
				if (!"0".equals(dto.getNumDocumento().trim()) && !"".equals(dto.getNumDocumento().trim())){
					System.out.println("valor:"+dto.getValorAbsoluto());
					System.out.println("numeroDocumento:"+dto.getNumDocumento());
					
					if (isNumeric(dto.getValorAbsoluto().replace(".","").trim())){
							int id = cajas.buscaConciliadoParcialCheque(dto.getSucursalHomologada().trim(), dto.getValorAbsoluto().replace(".", ""));
							if (id>0){
								//Asocia contraparte de la conciliacion en tabla CARTOLA y CAJA
								cartolas.actualizaConciliaParcial(dto.getCorrelativoCartola(),id);
								cajas.actualizaConciliaParcial(id, dto.getCorrelativoCartola());
							}
					}
					
					
				
				}
			}
			
			
			
		}
		System.out.println("Termino");
		
	}
	
	public void procesaEfectivo(){
		
		//Procesa conciliacion de tablas CAJAS y CARTOLAS
		DAOFactory dao = DAOFactory.getInstance();
		CajasDAO cajas = dao.getCajasDAO();
		CartolasDAO cartolas = dao.getCartolasDAO();
		
		List listaCartola = cartolas.listaCartolasCheque();
		Iterator iter = listaCartola.iterator();
		while (iter.hasNext()){
			CartolasDTO dto = (CartolasDTO) iter.next();
			//Busca en CAJAS
			if (dto.getValorAbsoluto()!=null && !"".equals(dto.getValorAbsoluto()) ){
				if (!"0".equals(dto.getNumDocumento().trim()) && !"".equals(dto.getNumDocumento().trim())){
					System.out.println("valor:"+dto.getValorAbsoluto());
					System.out.println("numeroDocumento:"+dto.getNumDocumento());
					if (isNumeric(dto.getNumDocumento().trim())){
						 if (isNumeric(dto.getValorAbsoluto().replace(".","").trim())){
							int id = cajas.buscaConciliadoEfectivo(dto.getSucursalHomologada().trim(), Integer.parseInt(dto.getNumDocumento().trim()), dto.getValorAbsoluto().replace(".", ""));
							if (id>0){
								//Asocia contraparte de la conciliacion en tabla CARTOLA y CAJA
								cartolas.actualizaConcilia(dto.getCorrelativoCartola(),id);
								cajas.actualizaConcilia(id, dto.getCorrelativoCartola());
							}
						}
					}
					
				
				}
			}
			
			
			
		}
		System.out.println("Termino");
		
	}

	public void procesaEfectivoParcial(){
		
		//Procesa conciliacion de tablas CAJAS y CARTOLAS
		DAOFactory dao = DAOFactory.getInstance();
		CajasDAO cajas = dao.getCajasDAO();
		CartolasDAO cartolas = dao.getCartolasDAO();
		
		List listaCartola = cartolas.listaCartolasCheque();
		Iterator iter = listaCartola.iterator();
		while (iter.hasNext()){
			CartolasDTO dto = (CartolasDTO) iter.next();
			//Busca en CAJAS
			if (dto.getValorAbsoluto()!=null && !"".equals(dto.getValorAbsoluto()) ){
				if (!"0".equals(dto.getNumDocumento().trim()) && !"".equals(dto.getNumDocumento().trim())){
					System.out.println("valor:"+dto.getValorAbsoluto());
					System.out.println("numeroDocumento:"+dto.getNumDocumento());
					
					if (isNumeric(dto.getValorAbsoluto().replace(".","").trim())){
							int id = cajas.buscaConciliadoParcialEfectivo(dto.getSucursalHomologada().trim(), dto.getValorAbsoluto().replace(".", ""));
							if (id>0){
								//Asocia contraparte de la conciliacion en tabla CARTOLA y CAJA
								cartolas.actualizaConciliaParcial(dto.getCorrelativoCartola(),id);
								cajas.actualizaConciliaParcial(id, dto.getCorrelativoCartola());
							}
					}
					
					
				
				}
			}
			
			
			
		}
		System.out.println("Termino");
		
	}

	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	} 
	
	public static void main (String[]args){
		ProcesaConciliacion procesa = new ProcesaConciliacion();
		procesa.procesaEfectivo();
		procesa.procesaEfectivoParcial();
		
		
	}
}
