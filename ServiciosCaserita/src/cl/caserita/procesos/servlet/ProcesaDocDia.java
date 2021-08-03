package cl.caserita.procesos.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.helper.ProcesaDocumentosRecepcionDia;

/**
 * Servlet implementation class ProcesaDocDia
 */
public class ProcesaDocDia extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaDocDia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fecha = request.getParameter("fecha").trim();
		int rutEmpresa = Integer.parseInt(request.getParameter("rutEmpresa"));
		WsClient ws = new WsClient();
		ProcesaDocumentosRecepcionDia proc = new ProcesaDocumentosRecepcionDia();
		
		String string = ws.onlineRecoveryRecList(rutEmpresa, "", fecha);
		System.out.println("Fecha :"+fecha);
		//String ano = fecha.substring(0, 4);
		//String mes = fecha.substring(5, 7);
		//String dia = fecha.substring(8, 10);
		System.out.println("Empresa:"+rutEmpresa);
		System.out.println("Fecha Documento:"+fecha);
		System.out.println("Documentos:"+string);
		//proc.procesaDocumentos(string, rutEmpresa, Integer.parseInt(ano+mes+dia));
		proc.procesaDocumentos(string, rutEmpresa, Integer.parseInt("20150304"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
