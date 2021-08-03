package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegraCarguioHelper;

/**
 * Servlet implementation class IntegracionCarguiocWMS
 */
public class IntegracionCarguiocWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionCarguiocWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionCarguiocWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int CodEmp = Integer.parseInt(request.getParameter("emp"));
		String CodEstado = request.getParameter("est");
		int NumCarguio = Integer.parseInt(request.getParameter("carg"));
		int CodBodega = Integer.parseInt(request.getParameter("bod"));
		String TipoPedCarguio = request.getParameter("tipc");
		String accion = request.getParameter("accion");
		
		IntegraCarguioHelper helper = new IntegraCarguioHelper();
		if ("RUTA".equals(TipoPedCarguio) || "CALZ".equals(TipoPedCarguio) || "ESPE".equals(TipoPedCarguio) || "TRAS".equals(TipoPedCarguio)){
			helper.buscaCarguio(CodEmp, CodEstado, NumCarguio, CodBodega, TipoPedCarguio, accion);

		}else if ("RAMP".equals(TipoPedCarguio)){
			helper.buscaCarguioTranfe(CodEmp, CodEstado, NumCarguio, CodBodega, TipoPedCarguio, accion);

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
