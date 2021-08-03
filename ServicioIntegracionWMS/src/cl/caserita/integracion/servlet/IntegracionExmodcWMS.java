package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionExdodcHelper;

/**
 * Servlet implementation class IntegracionExmodcWMS
 */
public class IntegracionExmodcWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionExmodcWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionExmodcWMS() {
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
		int numOC = Integer.parseInt(request.getParameter("numoc"));
		String accion = request.getParameter("accion");
		IntegracionExdodcHelper helper = new IntegracionExdodcHelper();
		if (numOC>0){
			helper.integracion(empresa, numOC, accion);
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
