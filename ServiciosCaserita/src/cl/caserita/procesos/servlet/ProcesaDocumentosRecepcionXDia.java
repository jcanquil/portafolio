package cl.caserita.procesos.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.helper.ProcesaDocumentosRecepcionDia;

/**
 * Servlet implementation class ProcesaDocumentosRecepcionXDia
 */
public class ProcesaDocumentosRecepcionXDia extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaDocumentosRecepcionXDia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fecha = request.getParameter("fecha");
		int rutEmpresa = Integer.parseInt(request.getParameter("rutEmpresa"));
		WsClient ws = new WsClient();
		ProcesaDocumentosRecepcionDia proc = new ProcesaDocumentosRecepcionDia();
		
		String string = ws.onlineRecoveryRecList(rutEmpresa, "", fecha);
		fecha = fecha.replaceAll("-", fecha);
		System.out.println("Empresa:"+rutEmpresa);
		System.out.println("Fecha Documento:"+fecha);
		System.out.println("Documentos:"+string);
		proc.procesaDocumentos(string, rutEmpresa, Integer.parseInt(fecha));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
