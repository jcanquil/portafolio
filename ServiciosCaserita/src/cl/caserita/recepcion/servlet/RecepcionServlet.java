package cl.caserita.recepcion.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.helper.procesaRecepcionHelper;

/**
 * Servlet implementation class RecepcionServlet
 */
public class RecepcionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecepcionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		procesaRecepcionHelper procesa = new procesaRecepcionHelper();
		int codDoc= Integer.parseInt(request.getParameter("codDoc"));
		int rut= Integer.parseInt(request.getParameter("rut"));
		int numero= Integer.parseInt(request.getParameter("numDoc"));
		int empresa= Integer.parseInt(request.getParameter("empresa"));
		int numOC = Integer.parseInt(request.getParameter("numOC"));
		String fechaProceso = request.getParameter("fecha");
		System.out.println("Codigo Doc:"+codDoc);
		System.out.println("Rut:"+rut);
		System.out.println("Num Doc:"+numero);
		System.out.println("Empresa:"+empresa);
		System.out.println("Numero OC:"+numOC);
		DAOFactory factory = DAOFactory.getInstance();
		TptempDAO tptemp = (TptempDAO) factory.getTptempDAO();
		List empresaList = tptemp.recuperaEmpresa();
		
		if (codDoc>0){
			Iterator iter = empresaList.iterator();
			System.out.println("PROCESA INDIVIDUAL");
			TptempDTO tptempDTO=tptemp.recuperaEmpresa(empresa);
			/*while (iter.hasNext()){
				tptempDTO =(TptempDTO) iter.next();
				procesa.procesaDocumentoIndividual(codDoc, numero, rut, tptempDTO.getRut());
			}*/
			procesa.procesaDocumentoIndividual(codDoc, numero, rut, empresa,numOC);
		}else{
			
			Iterator iter = empresaList.iterator();
			TptempDTO tptempDTO=null;
			while (iter.hasNext()){
				tptempDTO =(TptempDTO) iter.next();
				System.out.println("Procesa Empresa:"+tptempDTO.getRut());
				System.out.println("Procesa Fecha:"+fechaProceso);
				procesa.procesaRecepcin(tptempDTO.getRut(),fechaProceso);
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
