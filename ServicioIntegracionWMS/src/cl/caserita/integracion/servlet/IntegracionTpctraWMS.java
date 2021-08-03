package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionTpctraHelper;

/**
 * Servlet implementation class IntegracionTpctraWMS
 */
public class IntegracionTpctraWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionTpctraWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionTpctraWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int rut = Integer.parseInt(request.getParameter("rut"));
		String accion = request.getParameter("accion");
		
		IntegracionTpctraHelper integra = new IntegracionTpctraHelper();
		if (rut==0){
			integra.integracionAll();
		}else{
			integra.integracion(rut, accion);
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
