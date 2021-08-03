package cl.caserita.helper;

import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CamtraDAO;

public class ConsultaDocumentoHelper {

	
	public List recuperaDocumentos(int empresa, int codTipoMov, int fechaInicial, int fechaFinal){
		List camtra =null;
		DAOFactory factory = DAOFactory.getInstance();
		
		CamtraDAO camtraDAO = (CamtraDAO) factory.getCamtraDAO();
		camtra = camtraDAO.obtenerDatosCamtraPendientes(empresa, codTipoMov, fechaInicial, fechaFinal);
		
		return camtra;
		
	}
}
