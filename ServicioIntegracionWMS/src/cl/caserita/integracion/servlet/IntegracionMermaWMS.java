package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionMermasHelper;

/**
 * Servlet implementation class IntegracionMermaWMS
 */
public class IntegracionMermaWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionMermaWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionMermaWMS() {
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
		int tip = Integer.parseInt(request.getParameter("tip"));
		int fecha = Integer.parseInt(request.getParameter("fch"));
		int num = Integer.parseInt(request.getParameter("num"));
		String accion = request.getParameter("accion");
		IntegracionMermasHelper helper = new IntegracionMermasHelper();
		helper.integracion(emp, tip, fecha, num, accion);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
