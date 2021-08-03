package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegraCarguioHelper;

/**
 * Servlet implementation class IntegracionCarguioOTWMS
 */
public class IntegracionCarguioOTWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionCarguioOTWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionCarguioOTWMS() {
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
		String estado = request.getParameter("est");
		int numCarguio = Integer.parseInt(request.getParameter("numc"));
		int bodega = Integer.parseInt(request.getParameter("bod"));
		String tipo = request.getParameter("tipo");
		String accion = request.getParameter("accion");
		
		IntegraCarguioHelper helper = new IntegraCarguioHelper();
		helper.buscaCarguiosOT(empresa, estado, numCarguio, bodega, tipo, accion);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
