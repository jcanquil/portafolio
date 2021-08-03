package cl.caserita.procesos.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.RecepDocumentoDTO;
import cl.caserita.dto.RutaDocumentosDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.enviomail.main.EnvioMailDocumentoPDFXML;
import cl.caserita.helper.ProcesaEnvioDocumento;

/**
 * Servlet implementation class ProcesaDocumentoCliente
 */
public class ProcesaDocumentoCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaDocumentoCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int empresa = Integer.parseInt(request.getParameter("empresa"));
		String tipoDocumento = request.getParameter("tipo");
		String numeroDocumento =request.getParameter("numero");
		String rutCliente = request.getParameter("rut");
		String mail = request.getParameter("mail");
		String mailCopia= request.getParameter("mailCopia");
		System.out.println("Tipo:"+tipoDocumento);
		System.out.println("Numero:"+numeroDocumento);
		System.out.println("Rut:" +rutCliente);
		System.out.println("Mail:" +mail);
		
		
		DAOFactory dao = DAOFactory.getInstance();
		TptdeleDAO tpdeleDAO = (TptdeleDAO) dao.getTptdeleDAO();
		TptempDAO tpt = (TptempDAO) dao.getTptempDAO();
		TptempDTO  dto=tpt.recuperaEmpresa(empresa);
		
		int codigoCaserita = tpdeleDAO.buscaDocumentoElectronico(Integer.parseInt(tipoDocumento));
		WsClient ws = new WsClient();
		StringTokenizer st = new StringTokenizer(numeroDocumento,"|");
		RutaDocumentosDTO ruta = null;
		List rutaDoc = new ArrayList();
		 while (st.hasMoreTokens( )){
			 
			 	String tr = st.nextToken();
			 	StringTokenizer stad = new StringTokenizer(tr,",");
			 	
			 	int num=0;
			 	while (stad.hasMoreTokens( )){
			 		//System.out.println("Token: " + stad.nextToken( ));
			 		
			 		
			 		String  numeroFactura = stad.nextToken();
			 		System.out.println("Numeros :"+numeroFactura);
			 		System.out.println("CodCaserita :"+codigoCaserita);
			 		System.out.println("NumeroFactura :"+numeroFactura);
			 		System.out.println("Empresa :"+dto.getRut());
			 		String xml = ws.onlineRecoveryTipoUrl(codigoCaserita, Integer.parseInt(numeroFactura), dto.getRut(), 1);
					String pdf = ws.onlineRecoveryTipoUrl(codigoCaserita, Integer.parseInt(numeroFactura), dto.getRut(), 2);
					int ip = Integer.parseInt(pdf.substring(17, 18));
					if (ip==4){
						pdf = pdf.trim()+"&tmpl="+"76288567"+"/"+codigoCaserita+".jasper";
					}
					System.out.println("URL XML:"+xml);
					System.out.println("URL PDF:"+pdf);
					ProcesaEnvioDocumento proceso = new ProcesaEnvioDocumento();
					String rutaXML = proceso.enviaXML(Integer.parseInt(numeroFactura), Integer.parseInt(rutCliente), Integer.parseInt(tipoDocumento),xml,".xml");
					String rutaPDF = proceso.enviaXML(Integer.parseInt(numeroFactura), Integer.parseInt(rutCliente), Integer.parseInt(tipoDocumento),pdf,".pdf");
					ruta = new RutaDocumentosDTO();
					ruta.setTipoObjeto(1);
					ruta.setRutaObjeto(rutaXML);
					rutaDoc.add(ruta);
					ruta = new RutaDocumentosDTO();
					ruta.setTipoObjeto(2);
					ruta.setRutaObjeto(rutaPDF);
					rutaDoc.add(ruta);
					System.out.println("Ruta XML:"+rutaXML);
					System.out.println("Ruta PDF:"+rutaPDF);
					
			 	}
			 		
			 		num++;
		}
		
		
		
		EnvioMailDocumentoPDFXML envia = new EnvioMailDocumentoPDFXML();
		envia.envioMail("", mail, mailCopia, "Documentos Tributarios", "",rutaDoc);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}

}
