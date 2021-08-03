package cl.caserita.procesos.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.company.user.wsclient.WsClient;

/**
 * Servlet implementation class GeneraNCProblema
 */
public class GeneraNCProblema extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneraNCProblema() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		WsClient ws = new WsClient();
		String xml = request.getParameter("xml");
		String rut=request.getParameter("rut");
		String numDoc=request.getParameter("num");
		int tipo = Integer.parseInt(request.getParameter("tipo"));
		System.out.println("Rut:"+rut);
		System.out.println("Numero Documento:"+numDoc);
		String generacion="NC";
		URL url=
		    new URL("http://50.50.1.240:8080/ServiciosCaserita/"+xml);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String entrada;
		String cadena="";

		while ((entrada = br.readLine()) != null){
			cadena = cadena + entrada;
		}

		System.out.println("XML a Procesar:"+cadena);
		if (tipo==1){
			ws.onlineGeneration(cadena, rut, numDoc, 23, 33, generacion, Integer.parseInt(rut),"");
		}else if (tipo==2){
			ws.onlineGenerationReenvio(cadena, rut, numDoc, 23, 33, generacion, 76288567);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
