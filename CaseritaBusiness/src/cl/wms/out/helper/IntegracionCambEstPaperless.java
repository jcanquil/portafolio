package cl.caserita.wms.out.helper;

import org.apache.log4j.Logger;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.dao.base.DAOFactory;

public class IntegracionCambEstPaperless {
	private  static Logger logi = Logger.getLogger(IntegracionCambEstPaperless.class);

	public void procesaCambioEstado(String rut, String numdoc, int coddoc){
		
		DAOFactory dao = DAOFactory.getInstance();
		
		try{
			
			WsClient paperless = new WsClient();
			
			String estado = paperless.recuperaEstadoSii(rut, numdoc, coddoc);
			
			logi.info("Estado Paperless:"+estado);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IntegracionCambEstPaperless helper = new IntegracionCambEstPaperless();
		helper.procesaCambioEstado("76288567", "1419997", 33);
		
	}

}
