package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionTptbdgHelper;

/**
 * Servlet implementation class IntegracionTptbdgWMS
 */
public class IntegracionTptbdgWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionTpctraWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionTptbdgWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int codigo = Integer.parseInt(request.getParameter("cod"));
		String accion = request.getParameter("accion");
		IntegracionTptbdgHelper integra = new IntegracionTptbdgHelper();
		integra.procesaBodega(codigo, accion);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
