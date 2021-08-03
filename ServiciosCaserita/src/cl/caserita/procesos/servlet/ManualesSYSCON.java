package cl.caserita.procesos.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ManualesSYSCON
 */
public class ManualesSYSCON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManualesSYSCON() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String ruta = "/home/ServiciosCaserita/ManualesSYSCON/";
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

	for(int i = 0; i < fList.length; i++){
		System.out.println("TRae documentos");
		ficheroInput = new FileInputStream(fList[i]);
		tamanoInput = ficheroInput.available(); 
		datosPDF = new byte[tamanoInput]; 
		ficheroInput.read( datosPDF, 0, tamanoInput);
		if (objeto.trim().equals(fList[i].getName())){
			response.setHeader("Content-disposition","inline; filename="+fList[i].getName());
			response.setContentType("application/pdf");
			response.setContentLength(tamanoInput);	
			response.getOutputStream().write(datosPDF); 
		}
		

		ficheroInput.close(); 
	}
	
	
	
	
	/*PrintWriter out = response.getWriter();

    response.setContentType("application/pdf");

    String filepath = pdfFileName;

    response.setHeader("Content-Disposition", "inline; filename="+objeto+"");

    FileOutputStream fileout = new FileOutputStream(filepath);

    fileout.close();

    out.close();*/
    
	//get the 'file' parameter
    /*String fileName = (String) request.getParameter("nombreManual");
    if (fileName == null || fileName.equals(""))
      throw new ServletException(
          "Invalid or non-existent file parameter in SendPdf servlet.");

    // add the .pdf suffix if it doesn't already exist
    if (fileName.indexOf(".pdf") == -1)
      fileName = fileName + ".pdf";

    String pdfDir = ruta;
    System.out.println("Direccion"+pdfDir);
    if (pdfDir == null || pdfDir.equals(""))
      throw new ServletException(
          "Invalid or non-existent 'pdf-Dir' context-param.");

    ServletOutputStream stream = null;
    BufferedInputStream buf = null;
    try {
      stream = response.getOutputStream();
      File pdf = new File(pdfDir + "/" + fileName);
      response.setContentType("application/pdf");

      response.addHeader("Content-Disposition", "attachment; filename="
          + fileName);
      response.setContentLength((int) pdf.length());
      FileInputStream input = new FileInputStream(pdf);
      buf = new BufferedInputStream(input);
      int readBytes = 0;

      while ((readBytes = buf.read()) != -1)
        stream.write(readBytes);
    } catch (IOException ioe) {
      throw new ServletException(ioe.getMessage());
    } finally {
      if (stream != null)
        stream.close();
      if (buf != null)
        buf.close();
    }*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
