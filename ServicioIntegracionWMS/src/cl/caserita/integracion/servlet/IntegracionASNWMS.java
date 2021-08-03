package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.transportista.rest.servicioCaseritaXMLWMS;
import cl.caserita.wms.helper.IntegracionASNHelper;

/**
 * Servlet implementation class IntegracionASNWMS
 */
public class IntegracionASNWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionASNWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionASNWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		int emp = Integer.parseInt(request.getParameter("emp"));
		int numc = Integer.parseInt(request.getParameter("numc"));
		int bod = Integer.parseInt(request.getParameter("bod"));
		int fecha = Integer.parseInt(request.getParameter("fch"));
		String accion = request.getParameter("accion");
		String patente = request.getParameter("pat");
		String tipcar = request.getParameter("tip");
		
		log.info(emp);
		log.info(numc);
		log.info(bod);
		log.info(fecha);
		log.info(accion);
		log.info(patente);
		log.info(tipcar);
		
		IntegracionASNHelper helper = new IntegracionASNHelper();
		helper.procesaConsolidado(emp, numc, bod, accion, fecha, tipcar);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
