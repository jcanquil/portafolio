package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegraExmartHelper;

/**
 * Servlet implementation class IntegracionExmartWMS
 */
public class IntegracionExmartWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionExmartWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionExmartWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		int codArt = Integer.parseInt(request.getParameter("cod"));
		String dvArt = request.getParameter("dv");
		String accion = request.getParameter("accion");
		
		log.info(codArt);
		log.info(dvArt);
		log.info(accion);
		
		IntegraExmartHelper helper = new IntegraExmartHelper();
		
		if (codArt == 0){
			log.info("Llamado a todos los articulos");
			helper.procesaExmart(codArt, accion);
		} else {
			log.info("Llamado por articulo");
			helper.procesaPorArticulo(codArt, accion,dvArt);
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
