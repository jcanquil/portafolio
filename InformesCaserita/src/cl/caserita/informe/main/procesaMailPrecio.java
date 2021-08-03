package cl.caserita.informe.main;

import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.enviomail.main.EnvioMailCaseritaAdjuntoGasto;
import cl.caserita.enviomail.main.EnvioMailInformePrecio;
import cl.caserita.informe.process.generaExcelInformeGastos;
import cl.caserita.informe.process.generaExcelPrecioVedmar;

public class procesaMailPrecio {

	public static void main (String []args){
		DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmar = dao.getVedmarDAO();
		List lista = vedmar.consultaArticulosPrecio(2, 21, 20160203, 26);
		generaExcelPrecioVedmar genera = new generaExcelPrecioVedmar();
		String nombre = genera.generaExcelPrecio(lista);
		EnvioMailInformePrecio envio = new EnvioMailInformePrecio();
		envio.envioMail(nombre, "jcanquil@caserita.cl", "", "Informe Precios");
		
	}
	public void enviaMail(int fecha, int hora, List lista){
		
		generaExcelPrecioVedmar genera = new generaExcelPrecioVedmar();
		String nombre = genera.generaExcelPrecio(lista);
		EnvioMailInformePrecio envio = new EnvioMailInformePrecio();
		envio.envioMail(nombre, "desarrollo@caserita.cl", "", "Informe Precios Netos "+fecha);
	}
}
