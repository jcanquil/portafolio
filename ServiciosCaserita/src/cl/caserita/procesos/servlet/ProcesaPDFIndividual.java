package cl.caserita.procesos.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ConarcDAO;
import cl.caserita.dto.ExdfcprDTO;
import cl.caserita.helper.ProcesaPDFHelper;

/**
 * Servlet implementation class ProcesaPDFIndividual
 */
public class ProcesaPDFIndividual extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaPDFIndividual() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int empresa = Integer.parseInt(request.getParameter("empresa"));
		int rutProveedor = Integer.parseInt(request.getParameter("rut"));
		int CodigoDoc = Integer.parseInt(request.getParameter("codDoc"));
		int NumeroDoc = Integer.parseInt(request.getParameter("numDoc"));
		System.out.println("Ingresa Servlet Actualiza PDF Documento");
		System.out.println("Empresa:"+empresa);
		System.out.println("RutProveedor:"+rutProveedor);
		System.out.println("CodigoDoc:"+CodigoDoc);
		System.out.println("NumeroDoc:"+NumeroDoc);
		ProcesaPDFHelper pdf = new ProcesaPDFHelper();
		
		
		if (empresa==0){
			DAOFactory dao = DAOFactory.getInstance();
			ConarcDAO conarc = dao.getConarcDAO();
			List doc = conarc.buscaDocumentosSinPDF();
			Iterator iter = doc.iterator();
			ExdfcprDTO exdfc=null;
			while (iter.hasNext()){
				exdfc = (ExdfcprDTO)iter.next();
				pdf.ProcesaPDF(exdfc.getEmpresa(), exdfc.getRutProveedor(), exdfc.getCodDocumento(), exdfc.getNumeroDocumento());
			}
			
		}else{
			pdf.ProcesaPDF(empresa, rutProveedor, CodigoDoc, NumeroDoc);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
