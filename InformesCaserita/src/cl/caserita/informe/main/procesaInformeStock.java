package cl.caserita.informe.main;

import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.enviomail.main.EnviaMailInformeStockBodega;
import cl.caserita.informe.process.generaExcelStockBodega;

public class procesaInformeStock {

	public void procesaInforme(int bodega, String mail){
		DAOFactory dao = DAOFactory.getInstance();
		ExmarbDAO exmarbdao = dao.getExmarbDAO();
		List lista = exmarbdao.obtieneArticulosStockNegativo(bodega);
		generaExcelStockBodega stock = new generaExcelStockBodega();
		EnviaMailInformeStockBodega infstock = new EnviaMailInformeStockBodega();
		infstock.envioMail(		stock.generaExcelPrecio(lista)
, "desarrollo@caserita.cl", "", "Informe Articulos con STOCK NEGATIVO");
		
	}
}
