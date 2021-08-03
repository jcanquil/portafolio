package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionExmtraHelper;

/**
 * Servlet implementation class IntegracionExmtraWMS
 */
public class IntegracionExmtraWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionExmtraWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionExmtraWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int empresa = Integer.parseInt(request.getParameter("emp"));
		int numTraspaso = Integer.parseInt(request.getParameter("num"));
		String accion = request.getParameter("accion");
		IntegracionExmtraHelper helper = new IntegracionExmtraHelper();
		if (numTraspaso>0){
			helper.integracion(empresa, numTraspaso, accion);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
