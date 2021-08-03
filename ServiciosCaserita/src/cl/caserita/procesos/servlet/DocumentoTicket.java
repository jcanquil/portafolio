package cl.caserita.procesos.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DocumentoTicket
 */
public class DocumentoTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  private static final int BYTES_DOWNLOAD = 1024;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentoTicket() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String ruta = "/home/ticket/";
		String objeto = request.getParameter("ticket");
		System.out.println("Ruta:"+objeto);
		String pdfFileName = ruta+objeto;
		System.out.println("Ruta2:"+pdfFileName);

		System.out.println("estamos en el get de OpenPdf");
		 
		FileInputStream ficheroInput = null;
		File dir = new File(ruta);
		File[] fList = dir.listFiles();
		int tamanoInput =0;
		/*byte[] datosPDF = null;

			System.out.println("TRae documentos:"+pdfFileName);
			ficheroInput = new FileInputStream(pdfFileName);
			tamanoInput = ficheroInput.available(); 
			datosPDF = new byte[tamanoInput]; 
			ficheroInput.read( datosPDF, 0, tamanoInput);
				response.setHeader("Content-disposition","inline; filename="+pdfFileName);
				response.setContentType("text/plain");
				response.setContentLength(tamanoInput);	
				response.getOutputStream().write(datosPDF); 
				ficheroInput.close();*/
				
				
		response.setContentType("text/html");
		
		PrintWriter writer = response.getWriter();
        String username="jaime";
        String password="jajaja";
        // build HTML code
        String cadena="";
        String htmlRespone = "<html>";
        FileReader f = new FileReader(pdfFileName);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            htmlRespone += "<h4>" + cadena  ;      
        }
        b.close();
        
      //  htmlRespone += "Your password is: " + password + "</h2>";  
        htmlRespone += "</h4>";

        htmlRespone += "</html>";
         
        // return response
        writer.println(htmlRespone);
        
        
		
				
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
