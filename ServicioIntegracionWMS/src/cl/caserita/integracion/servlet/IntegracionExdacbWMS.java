package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionExdacbHelper;

/**
 * Servlet implementation class IntegracionExdacbWMS
 */
public class IntegracionExdacbWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionExdacbWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionExdacbWMS() {
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
		String dv = request.getParameter("dv");
		String accion = request.getParameter("accion");
		int cantidad = Integer.parseInt(request.getParameter("cnt"));
		int fecha = Integer.parseInt(request.getParameter("fec"));
		IntegracionExdacbHelper helper = new IntegracionExdacbHelper();
		helper.procesa(codigo, dv, accion, cantidad, fecha);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
