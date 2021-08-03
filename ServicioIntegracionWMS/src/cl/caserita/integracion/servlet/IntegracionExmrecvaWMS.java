package cl.caserita.integracion.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.caserita.wms.helper.IntegracionExmrecvaHelper;

/**
 * Servlet implementation class IntegracionExmrecvaWMS
 */
public class IntegracionExmrecvaWMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IntegracionExmrecvaWMS.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionExmrecvaWMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int empresa = Integer.parseInt(request.getParameter("emp"));
		int numOC = Integer.parseInt(request.getParameter("numoc"));
		int tipDoc = Integer.parseInt(request.getParameter("tipdoc"));
		int numeroDoc = Integer.parseInt(request.getParameter("numdoc"));
		int fechaDoc = Integer.parseInt(request.getParameter("fecdoc"));
		int codBodega = Integer.parseInt(request.getParameter("codbod"));
		String tipo = request.getParameter("tipo");
		String accion = request.getParameter("accion");
		
		System.out.println(empresa);
		System.out.println(numOC);
		System.out.println(tipDoc);
		System.out.println(numeroDoc);
		System.out.println(fechaDoc);
		System.out.println(codBodega);
		System.out.println(tipo);
		System.out.println(accion);
		
		IntegracionExmrecvaHelper helper = new IntegracionExmrecvaHelper();
		//if (numOC>0){
			helper.integracion(empresa, numOC, tipDoc, numeroDoc, fechaDoc, codBodega, tipo, accion);
		//}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
