package cl.caserita.procesos.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.batch.recepcion.InformeDocumentosProveedor;
import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.enviomail.main.EnvioMailCaseritaAdjuntoDocumentos;
import cl.caserita.enviomail.main.EnvioMailCaseritaAdjuntoGasto;
import cl.caserita.informe.process.generaExcelInformeDocumentos;

/**
 * Servlet implementation class InformeDocumentoProveedor
 */
public class InformeDocumentoProveedor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InformeDocumentoProveedor() {
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
		InformeDocumentosProveedor inf = new InformeDocumentosProveedor();
		WsClient ws = new WsClient();
		Iterator iter = empresa.iterator();
		TptempDTO empre = null;
		generaExcelInformeDocumentos gen = new generaExcelInformeDocumentos();
		EnvioMailCaseritaAdjuntoDocumentos envio = new EnvioMailCaseritaAdjuntoDocumentos();
		
		while (iter.hasNext()){
			List doc =null;
			empre = (TptempDTO) iter.next();
			String string = ws.onlineGestionRec(empre.getRut(),"");
			System.out.println("String documentos :"+string);
			doc = inf.procesa(string, empre.getRut(), empre.getRazonSocial(),ws);
			String ruta = gen.generaExcelGes(doc, empre.getRut());
			envio.envioMail(ruta, "desarrollo@caserita.cl", "contabilidad@caserita.cl", "DOCUMENTOS RECIBIDOS PAPERLESS"+" "+empre.getRazonSocial());
			
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
