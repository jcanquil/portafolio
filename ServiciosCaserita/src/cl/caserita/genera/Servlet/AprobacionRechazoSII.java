package cl.caserita.genera.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.helper.ProcesaAprobacionRecepcionHelper;

/**
 * Servlet implementation class AprobacionRechazoSII
 */
public class AprobacionRechazoSII extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AprobacionRechazoSII() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int codDoc = Integer.parseInt(request.getParameter("cod"));
		int numeroDoc = Integer.parseInt(request.getParameter("num"));
		int rutProv = Integer.parseInt(request.getParameter("prov"));
		int rutEmpresa = Integer.parseInt(request.getParameter("empresa"));
		
		ProcesaAprobacionRecepcionHelper helper = new ProcesaAprobacionRecepcionHelper();
		helper.procesaDocumentoIndividual(codDoc, numeroDoc, rutProv, rutEmpresa);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
