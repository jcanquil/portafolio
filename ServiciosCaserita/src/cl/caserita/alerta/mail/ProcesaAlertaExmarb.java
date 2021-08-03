package cl.caserita.alerta.mail;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.EmaapmDAO;
import cl.caserita.enviomail.main.emailInformacionExmarb;
import cl.caserita.enviomail.main.emailnformacionExdacp;

/**
 * Servlet implementation class ProcesaAlertaExmarb
 */
public class ProcesaAlertaExmarb extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaAlertaExmarb() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String bod = request.getParameter("bod");
		
		String art1 = request.getParameter("art");
		
		String accion = request.getParameter("ac");
		System.out.println("Art 1 :"+art1);
		

		DAOFactory dao = DAOFactory.getInstance();
		EmaapmDAO ema = dao.getEmaapmDAO();
		List mail = ema.obtieneMailAPP("EXMARB");
		
		emailInformacionExmarb email = new emailInformacionExmarb();
		email.mail(art1,bod,accion, mail);
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
