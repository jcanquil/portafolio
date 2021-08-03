package cl.caserita.integracion.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IntegracionVisualizaXML
 */
public class IntegracionVisualizaXML extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegracionVisualizaXML() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ruta = "/home2/ftp/in/";
		String objeto = request.getParameter("nombreManual");
		String tipo = request.getParameter("tipo");
		System.out.println("Ruta:"+objeto);
		
		String pdfFileName = objeto;
		System.out.println("Ruta2:"+pdfFileName);
		if ("2".equals(tipo)){
			ruta ="/home/stg/caserita/";
			pdfFileName = ruta+objeto;
		}
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
				response.setContentType("text/xml");
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
