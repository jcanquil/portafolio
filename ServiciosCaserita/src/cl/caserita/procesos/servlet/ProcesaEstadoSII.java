package cl.caserita.procesos.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dao.iface.CasesmtpDAO;
import cl.caserita.dao.iface.TptdeleDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.CasesmtpDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.enviomail.main.envioMailCaseritaAdjunto;
import cl.caserita.informe.process.generaExcelInformeGastos;
import cl.caserita.informe.process.generaExcelRechazados;
import cl.caserita.procesaXML.LeeXmlEstadoSII;

/**
 * Servlet implementation class ProcesaEstadoSII
 */
public class ProcesaEstadoSII extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcesaEstadoSII() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String empresa = request.getParameter("empresa");
		String fecha = request.getParameter("fecha");
		String codigoDoc = request.getParameter("CodDoc");
		DAOFactory factory = DAOFactory.getInstance();
		CamtraDAO camtra = (CamtraDAO) factory.getCamtraDAO();
		CasesmtpDAO smtp = (CasesmtpDAO) factory.getCasesmtpDAO();
		
		
		System.out.println("Empresa:"+empresa);
		System.out.println("Fecha:"+fecha);
		System.out.println("CodigoDoc:"+codigoDoc);
		List datosCamtra = camtra.obtenerDatosEstadoSII(Integer.parseInt(empresa), Integer.parseInt(fecha), Integer.parseInt(codigoDoc));
		Iterator iter = datosCamtra.iterator();
		TptempDAO tptem = (TptempDAO) factory.getTptempDAO();
		TptempDTO tpt = tptem.recuperaEmpresa(Integer.parseInt(empresa));
		WsClient ws = new WsClient();
		TptdeleDAO tptdeleDAO = (TptdeleDAO) factory.getTptdeleDAO();
		int codigoPP = tptdeleDAO.buscaDocumentoElectronico(Integer.parseInt(codigoDoc));
		LeeXmlEstadoSII xml = new LeeXmlEstadoSII();
		while (iter.hasNext()){
			CamtraDTO dto = (CamtraDTO) iter.next();
			String respuesta = ws.recuperaEstadoSii(String.valueOf(tpt.getRut()), String.valueOf(dto.getNumeroBolfactura()), codigoPP);
			int estado = xml.main(respuesta);
			System.out.println("Estado Actualizar PP:"+estado);
			camtra.actualizaEstadoCamtraSII(dto.getCodigoEmpresa(), dto.getCodigoTipoMovto(), dto.getNumeroDocumento(), dto.getFechaDocumento(), dto.getCorrelativo(), String.valueOf(estado));
			
		}
		generaExcelRechazados rechazados = new generaExcelRechazados();
		List rechazadosDoc= camtra.obtenerDatosEstadoRechazados(Integer.parseInt(empresa), Integer.parseInt(fecha), Integer.parseInt(codigoDoc));
		String nombre = rechazados.generaExcelGes(rechazadosDoc,Integer.parseInt(codigoDoc));
		envioMailCaseritaAdjunto envio = new envioMailCaseritaAdjunto();
		Fecha fch = new Fecha();
		//nombre = nombre.substring(34, nombre.length());
		//Obtiene Correo 2 de Caserita tabla SMTP
		CasesmtpDTO casesmtp = smtp.obtieneCorreos("1");
		System.out.println("Correo a Enviar:"+casesmtp.getCorreo2().trim());
		boolean exito = envio.envioMail(nombre, casesmtp.getCorreo2().trim(),casesmtp.getCorreo3().trim(),"Informe Documentos Rechazados en SII" + fch.getYYYYMMDD());
		
	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse respoechanse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
