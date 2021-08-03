package cl.caserita.procesos.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.informe.main.procesaMailPrecio;


/**
 * Servlet implementation class ProcesaInformePrecio
 */
public class ProcesaInformePrecio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaInformePrecio() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String fecha = request.getParameter("fecha");
		DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmar = dao.getVedmarDAO();
		
		
		List lista = vedmar.consultaArticulosPrecio(2, 21, Integer.parseInt(fecha), 26);
		procesaMailPrecio pro = new procesaMailPrecio();
		pro.enviaMail(20160204, 110000, lista);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
