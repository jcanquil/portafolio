package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionClmcliHelper;

/**
 * Servlet implementation class IntegracionClidirWMS
 */
public class IntegracionClidirWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionClidirWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionClidirWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int rut = Integer.parseInt(request.getParameter("rut"));
		String dv = request.getParameter("dv");
		String accion = request.getParameter("accion");
		int correlativo=-1;
		if (request.getParameter("corre")!=null){
			 correlativo = Integer.parseInt(request.getParameter("corre"));

		}
		IntegracionClmcliHelper integra = new IntegracionClmcliHelper();
		if (rut==0){
			integra.procesaClmcli();
		}else {
			if (correlativo>=0){
				integra.procesaClmcliPorRutCorrelativo(rut, dv, correlativo, accion);

			}
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
