package cl.caserita.ecommerce.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.EstadoDTO;

public class ClienteeCommerceHelper {
	private  static Logger log = Logger.getLogger(ClienteeCommerceHelper.class);

	public String recuperaDireccionCliente(String par000){
		String respuesta ="";
		DAOFactory dao = DAOFactory.getInstance();
		ClidirDAO clidirDAO = dao.getClidirDAO();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		EstadoDTO estado = null;
		Gson gson = new Gson();
		JsonObject object = (JsonObject) new JsonParser().parse(par000);
		int rutCliente=0;
		String dvCliente="";
		 if (object.get("rutCliente")!=null){
			 rutCliente = Integer.parseInt(object.get("rutCliente").toString().replaceAll("\"", ""));
		 }
		 if (object.get("dvCliente")!=null){
			 dvCliente = object.get("dvCliente").toString().replaceAll("\"", "");
		 }
		 ClmcliDTO clmcliDTO = clmcliDAO.recuperaCliente(String.valueOf(rutCliente), dvCliente);
		 if (clmcliDTO!=null){
			 estado = new EstadoDTO();
			 	estado.setCodigoEstado("0");
			 	estado.setDescripcionEstado("Cliente Existe");
			 	List lista = clidirDAO.obtieneDirecciones(rutCliente, dvCliente);
			 	if (lista.equals(null)){
			 		estado.setCodigoEstado("2");
				 	estado.setDescripcionEstado("Cliente Existe pero no posee direcciones");
			 	}
			 	lista.add(estado);
				respuesta = gson.toJson(lista);
		 }else{
			 estado = new EstadoDTO();
			 estado.setCodigoEstado("1");
			 estado.setDescripcionEstado("Cliente no existe");
			 List lista = new ArrayList();
			 lista.add(estado);
			 respuesta = gson.toJson(lista);
		 }
		
		return respuesta;
	}
	
	public static void main (String []args){
		ClienteeCommerceHelper cliente = new ClienteeCommerceHelper();
		//String param="";
		String param = "{\"rutCliente\":8084782,\"dvCliente\":\"3\"}";
		//cliente.recuperaDireccionCliente(param);
	
	}
}
