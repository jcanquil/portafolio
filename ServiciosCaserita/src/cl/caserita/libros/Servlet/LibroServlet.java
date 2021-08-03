package cl.caserita.libros.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.helper.GeneraLibrosHelper;

/**
 * Servlet implementation class LibroServlet
 */
public class LibroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LibroServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//+tipoLibro+"&"+"usuario="+usuario+"&fgeneracion="+fechaGeneracion+"&"+"hgeneracion="+horaGeneracion+"&"+"fInicio="+fechaInicio+"&fFin="+fechaFin&mail="+mail+"&bodega="+bodega+"&empresa="+empresa;
		String tipoLibro = request.getParameter("tipoLib");
		String usuario   = request.getParameter("usuario");
		int fechaGeneracion = Integer.parseInt(request.getParameter("fgeneracion"));
		int horaGeneracion = Integer.parseInt(request.getParameter("hgeneracion"));
		int fechaInicio = Integer.parseInt(request.getParameter("fInicio"));
		int fechaFin = Integer.parseInt(request.getParameter("fFin"));
		String mail = request.getParameter("mail");
		int bodega = Integer.parseInt(request.getParameter("bodega"));
		int empresa = Integer.parseInt(request.getParameter("empresa"));
		System.out.println("tipoLibro:"+tipoLibro);
		System.out.println("tipoLibro:"+usuario);
		System.out.println("tipoLibro:"+fechaGeneracion);
		System.out.println("tipoLibro:"+horaGeneracion);
		System.out.println("tipoLibro:"+fechaInicio);
		System.out.println("tipoLibro:"+fechaFin);
		System.out.println("tipoLibro:"+mail);
		System.out.println("tipoLibro:"+bodega);
		System.out.println("tipoLibro:"+empresa);
		
		GeneraLibrosHelper genera = new GeneraLibrosHelper();
		if ("LCOMPRAS".equals(tipoLibro)){
			genera.generaLibroCompras(tipoLibro, fechaInicio, fechaFin, mail,usuario,  fechaGeneracion,  horaGeneracion,  fechaInicio,  fechaFin,  empresa );
		}else if ("LVENTAS".equals(tipoLibro)){
			genera.generaLibroVentas(tipoLibro, fechaInicio, fechaFin, mail,usuario,  fechaGeneracion,  horaGeneracion,  fechaInicio,  fechaFin, bodega, empresa);
		}
	}

	/**s
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
