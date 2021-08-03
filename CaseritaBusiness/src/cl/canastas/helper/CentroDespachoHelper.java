package cl.caserita.canastas.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CentroDistribucionDAO;
import cl.caserita.ecommerce.helper.ClienteeCommerceHelper;

public class CentroDespachoHelper {
	private  static Logger log = Logger.getLogger(CentroDespachoHelper.class);

	public String recuperaCentroDespacho(String par000){
		String respuesta="";
		int rut=0,  codRegion=0,  codCiudad=0,  tipoDespacho=0, codComuna=0;
		List lista = new ArrayList();
		Gson gson = new Gson();
		log.info("Centro Despacho:"+par000);
		JsonObject json = (JsonObject)new JsonParser().parse(par000);
		if (json.get("rutCliente")!=null){
			rut = Integer.parseInt(json.get("rutCliente").toString().replaceAll("\"", "").trim());
		}
		if (json.get("codRegion")!=null){
			codRegion = Integer.parseInt(json.get("codRegion").toString().replaceAll("\"", "").trim());
		}
		if (json.get("codCiudad")!=null){
			codCiudad = Integer.parseInt(json.get("codCiudad").toString().replaceAll("\"", "").trim());
		}
		
		if (json.get("codTipoDespacho")!=null){
			tipoDespacho = Integer.parseInt(json.get("codTipoDespacho").toString().replaceAll("\"", "").trim());
		}
		if (json.get("codComuna")!=null){
			codComuna = Integer.parseInt(json.get("codComuna").toString().replaceAll("\"", "").trim());
		}
		
		if (rut!=0 && codRegion!=0 && codCiudad!=0 && tipoDespacho!=0){
			DAOFactory dao = DAOFactory.getInstance();
			CentroDistribucionDAO centro = dao.getCentroDistribucionDAO();
			lista = centro.buscaRegionCiudadComuna(rut, codRegion, codCiudad, tipoDespacho, codComuna);
			if (lista!=null && lista.size()>0){
				respuesta = gson.toJson(lista);
			}
			
		}else{
			respuesta = gson.toJson(lista);
		}
		log.info("Respuesta Centro Despacho:"+respuesta);
		
		return respuesta;
	}
	public static void main (String ar[]){
		String respuesta="";
		
		CentroDespachoHelper helper = new CentroDespachoHelper();
		//String par000="";
		String par000 ="{\"rutCliente\":\"97036000\",\"codRegion\":\"1\",\"codCiudad\":\"21\",\"codTipoDespacho\":\"3\",\"codComuna\":\"57\"}";
		helper.recuperaCentroDespacho(par000);
		
	}
}
