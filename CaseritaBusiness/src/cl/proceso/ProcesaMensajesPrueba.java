package cl.caserita.proceso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ProcedimientoDAO;

public class ProcesaMensajesPrueba {

	public static void main (String[]args) throws InterruptedException{
		DAOFactory dao = DAOFactory.getInstance();
		ProcedimientoDAO pro = dao.getProcedimientoDAO();
		
		
		
		String str = "ASYSRCD00 00008   0  01   ";
		System.out.println(pro.obtieneCorrelativo(str));
	}

}
