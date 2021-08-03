package cl.caserita.abienteweb.usuario;

import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.UsrWebDAO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CliDistDTO;

public class ProcesaUsuarioWEB {

	public int procesaUsuario(String rut, String usuario, String password){
		int acceso=0;
		DAOFactory dao = DAOFactory.getInstance();
		UsrWebDAO usr = dao.getUsrWebDAO();
		
		if (rut!=null && usuario!="" && password!=""){
			
			acceso = usr.valida(rut, usuario, password);
			System.out.println("Respuesta Acceso:"+acceso);
		}
		
		return acceso;
	}
	public int procesaUsuarioDis(int codVendedor, String password){
		int acceso=0;
		DAOFactory dao = DAOFactory.getInstance();
		UsrWebDAO usr = dao.getUsrWebDAO();
		
		if ( codVendedor!=0 && password!=""){
			
			acceso = usr.usrDistribucion(codVendedor, password);
			System.out.println("Respuesta Acceso:"+acceso);
		}
		
		return acceso;
	}
	public int insertaDireccion(CliDistDTO cli){
		int acceso=0;
		DAOFactory dao = DAOFactory.getInstance();
		UsrWebDAO usr = dao.getUsrWebDAO();
		
		acceso = usr.generaGenDireccion(cli);
		
		return acceso;
	}
	
	public List<ClcmcoDTO> listaDocumento(){
		List <ClcmcoDTO> doc =null;
		DAOFactory dao = DAOFactory.getInstance();
		UsrWebDAO usr = dao.getUsrWebDAO();
		doc = usr.documentos();
		
		return doc;
	}
}
