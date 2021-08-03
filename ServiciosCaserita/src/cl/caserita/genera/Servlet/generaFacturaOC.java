package cl.caserita.genera.Servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.properties.Constants;

/**
 * Servlet implementation class generaFacturaOC
 */
public class generaFacturaOC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public generaFacturaOC() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String xml2 = request.getParameter("xml");
		String empresa = request.getParameter("empresa");
		WsClient client = new WsClient();
		
		Properties prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
			String xml =prop.getProperty("XMLOC");
			System.out.println("XML"+xml);
			//client.onlineGeneration(xml,"15448543","9929292",1,33,"",Integer.parseInt(empresa));
			client.reenvioOnlineGeneration(xml, empresa,"9929292",1,33,"");
			//xml, String rut, String numDoc, int bodega, int codDocumento, String generacion
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
