package cl.caserita.procesos.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.helper.ProcesaFacturacion;
import cl.caserita.helper.ProcesaReenvioFacturacion;

/**
 * Servlet implementation class ProcesaReenvioDocumento
 */
public class ProcesaReenvioDocumento extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaReenvioDocumento() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String empresa = request.getParameter("empresa");
		String tipoMov= request.getParameter("codTipo");
		String fecha= request.getParameter("fch");
		String numero= request.getParameter("num");
		String cod= request.getParameter("cod");
		String rut= request.getParameter("rut");
		String dv= request.getParameter("dv");
		String usuario= request.getParameter("usuario");
		String tipo= request.getParameter("tipo");
		String nota= request.getParameter("nota");
		System.out.println("Reenvio Documentos Electronicos");
		System.out.println("Empresa :"+empresa);
		System.out.println("CodigoMov :"+tipoMov);
		System.out.println("FechaMov :"+fecha);
		System.out.println("NumeroDoc :"+numero);
		System.out.println("CodigoDoc :"+cod);
		System.out.println("RutCliente :"+rut);
		System.out.println("DV :"+dv);
		System.out.println("Usuario :"+usuario);
		System.out.println("Tipo :"+tipo);
		System.out.println("Nota :"+nota);
		
		int tipoMovimiento = Integer.parseInt(tipoMov);
		int codDocumento = Integer.parseInt(cod);
		ProcesaReenvioFacturacion pro = new ProcesaReenvioFacturacion();
		if (codDocumento==35 || codDocumento==40 || codDocumento==41){
			pro.procesaNotasCreditoDebito(Integer.parseInt(empresa),Integer.parseInt(tipoMov), Integer.parseInt(fecha), Integer.parseInt(numero), Integer.parseInt(rut), dv, Integer.parseInt(cod), usuario,tipo, nota);
		}else if (codDocumento==38){
//(int empresa, int codMovto, int fechaMovto, int numDocumento, int rut, String dv, int codigo, String usuario, String tipo){
			pro.procesaGuia(Integer.parseInt(empresa),Integer.parseInt(tipoMov), Integer.parseInt(fecha), Integer.parseInt(numero), Integer.parseInt(rut), dv, Integer.parseInt(cod), usuario,tipo);
		}else if (codDocumento==33 || codDocumento==36){
			pro.procesaFacturacion(Integer.parseInt(empresa),Integer.parseInt(tipoMov), Integer.parseInt(fecha), Integer.parseInt(numero), Integer.parseInt(rut), dv, Integer.parseInt(cod), usuario);
		}

		
		//pro.procesa(Integer.parseInt(empresa),Integer.parseInt(tipoMov), Integer.parseInt(fecha), Integer.parseInt(numero), Integer.parseInt(rut), dv, Integer.parseInt(cod), usuario,tipo, nota);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
