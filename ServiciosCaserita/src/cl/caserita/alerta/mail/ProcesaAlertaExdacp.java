package cl.caserita.alerta.mail;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.EmaapmDAO;
import cl.caserita.dto.EmaapmDTO;
import cl.caserita.enviomail.main.emailnformacionExdacp;

/**
 * Servlet implementation class ProcesaAlertaExdacp
 */
public class ProcesaAlertaExdacp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaAlertaExdacp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String bod = request.getParameter("bodega");
		String tv = request.getParameter("tv");
		String art1 = request.getParameter("art1");
		String art2 = request.getParameter("art2");
		String pre1 = request.getParameter("pre1");
		String pre2 = request.getParameter("pre2");
		String accion = request.getParameter("ac");
		System.out.println("Art 1 :"+art1);
		System.out.println("Art 2 :"+art2);
		System.out.println("Pre 1 :"+pre1);
		System.out.println("Pre 2 :"+pre2);
		System.out.println("Accion :"+accion);

		DAOFactory dao = DAOFactory.getInstance();
		EmaapmDAO ema = dao.getEmaapmDAO();
		List mail = ema.obtieneMailAPP("PRECIO");
		
		emailnformacionExdacp email = new emailnformacionExdacp();
		email.mail(art1, art2, pre1, pre2, tv,bod,accion, mail);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
