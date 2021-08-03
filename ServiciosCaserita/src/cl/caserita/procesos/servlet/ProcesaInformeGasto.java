package cl.caserita.procesos.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.informe.main.procesaInformeGastos;

/**
 * Servlet implementation class ProcesaInformeGasto
 */
public class ProcesaInformeGasto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaInformeGasto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		String fecha = request.getParameter("fecha");
		String hora = request.getParameter("hora");
		String mail = request.getParameter("mail");
		int periodo = Integer.parseInt(request.getParameter("periodoIni"));
		int periodoFin = Integer.parseInt(request.getParameter("periodoFin"));
		System.out.println("Fecha:"+fecha);
		System.out.println("Hora:"+hora);
		System.out.println("Mail:"+mail);
		System.out.println("PeriodoIni:"+periodo);
		System.out.println("PeriodoFin:"+periodoFin);
		procesaInformeGastos pro = new procesaInformeGastos();
		pro.procesaInforme(fecha, hora, mail, periodoFin, periodoFin);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
