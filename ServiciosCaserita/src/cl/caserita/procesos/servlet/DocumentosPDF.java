package cl.caserita.procesos.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DocumentosPDF
 */
public class DocumentosPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentosPDF() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ruta = "/home/docu/";
		String objeto = request.getParameter("nombreManual");
		System.out.println("Ruta:"+objeto);
		String pdfFileName = ruta+objeto;
		System.out.println("Ruta2:"+pdfFileName);

		System.out.println("estamos en el get de OpenPdf");
		 
		FileInputStream ficheroInput = null;
		File dir = new File(ruta);
		File[] fList = dir.listFiles();
		int tamanoInput =0;
		byte[] datosPDF = null;

			System.out.println("TRae documentos:"+pdfFileName);
			ficheroInput = new FileInputStream(pdfFileName);
			tamanoInput = ficheroInput.available(); 
			datosPDF = new byte[tamanoInput]; 
			ficheroInput.read( datosPDF, 0, tamanoInput);
				response.setHeader("Content-disposition","inline; filename="+pdfFileName);
				response.setContentType("application/pdf");
				response.setContentLength(tamanoInput);	
				response.getOutputStream().write(datosPDF); 
				ficheroInput.close(); 
			
		
		}	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
