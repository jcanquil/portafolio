package cl.caserita.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.helper.ProcesaFacturacion;

/**
 * Servlet implementation class CaserServlet
 */
public class CaserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CaserServlet.class);

    /**
     * Default constructor. 
     */
    public CaserServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("DATOS AL ENTRAR A SERVLET");
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
		log.info("Empresa :"+empresa);
		log.info("CodigoMov :"+tipoMov);
		log.info("FechaMov :"+fecha);
		log.info("NumeroDoc :"+numero);
		log.info("CodigoDoc :"+cod);
		log.info("RutCliente :"+rut);
		log.info("DV :"+dv);
		log.info("Usuario :"+usuario);
		log.info("Tipo :"+tipo);
		log.info("Nota :"+nota);
		
		
		ProcesaFacturacion pro = new ProcesaFacturacion();
		pro.procesa(Integer.parseInt(empresa),Integer.parseInt(tipoMov), Integer.parseInt(fecha), Integer.parseInt(numero), Integer.parseInt(rut), dv, Integer.parseInt(cod), usuario,tipo, nota);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
