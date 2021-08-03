package cl.caserita.migraInfo;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ProcedimientoDAO;

public class MigraInfoCaser1ToCaser2 {

	public int procesaMirgracion(int fecha){
		int numero=0;
		//TOMA CONEXION PRODUCCION
		DAOFactory dao = DAOFactory.getInstance3();
		ProcedimientoDAO procesa = dao.getProcedimientoDAO();
		//TOMA CONEXION QA
		DAOFactory dao2 = DAOFactory.getInstance2();
		ProcedimientoDAO procesa2 = dao2.getProcedimientoDAO();
		
		//Borra LIB en QA
		procesa2.procesaBorraLib("");
		//Procesa en Maquina Produccion
		procesa.procesaBorraFiles("");
		procesa.migraInfo1(fecha);
		procesa.migraInfo2(fecha);
		procesa.migraInfo3(fecha);
		procesa.migraInfo4(fecha);
		//int numeroOV = procesa.recuperaNumeroOV(fecha);
		int numeroOV2 = 0;//procesa.recuperaNumeroOVMaximo(fecha);

		procesa.migraInfo5(fecha, numeroOV2);
		
		procesa.procesaEnviaLib("");
		
		//Procesa en Maquina QA
		procesa2.migraInfo11(fecha);
		procesa2.migraInfo12(fecha);
		procesa2.migraInfo13(fecha);
		procesa2.migraInfo14(fecha);
		//int numeroOV = procesa.recuperaNumeroOV(fecha);
		System.out.println("Numero OV detalle Inicial:");
		System.out.println("Numero OV detalle Final:"+numeroOV2);

		procesa2.migraInfo15(fecha, numeroOV2);

		
		
		
		return numero;
	}
	
	public static void main (String []args){
		MigraInfoCaser1ToCaser2 migra = new MigraInfoCaser1ToCaser2();
		migra.procesaMirgracion(20160805);
	}
}
