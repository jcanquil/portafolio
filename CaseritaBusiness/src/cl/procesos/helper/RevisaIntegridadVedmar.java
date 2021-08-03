package cl.caserita.procesos.helper;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.enviomail.main.EnvioMailInformeIntegridad;
import cl.caserita.informe.process.generaExcelIntegridadVedmar;

public class RevisaIntegridadVedmar {

	public void integridad(int fecha){
		DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmar = dao.getVedmarDAO();
		
		List lista = vedmar.ListaPedidossinVedmar(fecha);
		generaExcelIntegridadVedmar integridad = new generaExcelIntegridadVedmar();
		String nombre = integridad.generaExcelPrecio(lista);
		EnvioMailInformeIntegridad envio = new EnvioMailInformeIntegridad();
		envio.envioMail(nombre, "desarrollo@caserita.cl", "", "INFORME INTEGRIDAD VEDMAR");
		
		
	}
	public static void main (String[]args){
		RevisaIntegridadVedmar revisa = new RevisaIntegridadVedmar();
		revisa.integridad(20160217);
	}
}
