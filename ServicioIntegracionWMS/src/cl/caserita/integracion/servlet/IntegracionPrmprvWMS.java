package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;

import cl.caserita.helper.ProcesaAprobacionRecepcionHelper;
import cl.caserita.wms.helper.IntegraPrmprvHelper;

/**
 * Servlet implementation class IntegracionPrmprvWMS
 */
public class IntegracionPrmprvWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionMermaWMS.class);

    /**
     * Default constructor. 
     */
    public IntegracionPrmprvWMS() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		int rutProv = Integer.parseInt(request.getParameter("prov"));
		String dvProv = request.getParameter("dv");
		String accion = request.getParameter("accion");
		
		log.info(rutProv);
		log.info(dvProv);
		log.info(accion);
		
		IntegraPrmprvHelper helper = new IntegraPrmprvHelper();
		
		if (rutProv == 0){
			log.info("Llamado a todos los proveedores");
			helper.procesaPrmprv();
		} else {
			log.info("Llamado por proveedores");
			helper.procesaPorProveedor(rutProv, dvProv, accion);
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
