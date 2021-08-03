package cl.caserita.genera.Servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.helper.ProcesaAprobacionRecepcionHelper;

/**
 * Servlet implementation class ProcesaAprobacion
 */
public class ProcesaAprobacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaAprobacion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DAOFactory dao = DAOFactory.getInstance();
		TptempDAO tpt = dao.getTptempDAO();
		List empresa = tpt.recuperaEmpresa();
		Iterator iter = empresa.iterator();
		ProcesaAprobacionRecepcionHelper helper = new ProcesaAprobacionRecepcionHelper();
		while (iter.hasNext()){
			TptempDTO dto = (TptempDTO)iter.next();
			helper.procesaRecepcin(dto.getRut());
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
