package cl.caserita.procesos.helper;

import java.util.ArrayList;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.informe.main.procesaMailPrecio;

public class InformePrecioVEDMAR {

	public void listaPrecio(int empresa, int tipoMov, int fecha, int bodega){
	
		List precio = new ArrayList();
		
		DAOFactory factory = DAOFactory.getInstance();
		VedmarDAO vedmar = factory.getVedmarDAO();
		
		precio = vedmar.consultaArticulosPrecio(empresa, tipoMov, fecha, bodega);
		procesaMailPrecio pro = new procesaMailPrecio();
		pro.enviaMail(20160204, 110000, precio);
		
	}
	
	public static void main (String  []args){
		InformePrecioVEDMAR ved = new InformePrecioVEDMAR();
		ved.listaPrecio(2,21, 20160816, 26);
	}
}
