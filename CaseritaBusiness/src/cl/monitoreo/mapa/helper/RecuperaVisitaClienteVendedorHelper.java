package cl.caserita.monitoreo.mapa.helper;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExmvenviDAO;
import cl.caserita.dto.RespuestaCanastaDTO;
import cl.caserita.dto.VendedorMapaDTO;
import cl.caserita.dto.ZonaVendedorDTO;

public class RecuperaVisitaClienteVendedorHelper {
	private static Logger log = Logger.getLogger(RecuperaVisitaClienteVendedorHelper.class);

	public String visitaCliente(String par000){
		String resp="";
		String vendedor="";
		String dia="";
		Gson gson = new Gson();
		RespuestaCanastaDTO resp2 = new RespuestaCanastaDTO();

		try{
			JSONObject object = (JSONObject) new JSONParser().parse(par000);
			 log.info("Parametro Entrada :"+par000);
			 if (object.get("vendedor")!=null){
				 vendedor = object.get("vendedor").toString().replaceAll("\"","");
			 }
			 if (object.get("dia")!=null){
				 dia = object.get("dia").toString().replaceAll("\"","");
			 }
			log.info("Usuario:"+vendedor);
			log.info("Password :"+dia);
			if (vendedor!=null && vendedor!=""){
				DAOFactory dao = DAOFactory.getInstance();
				ExmvenviDAO exmvenvi = dao.getExmvenviDAO();
				List lista = exmvenvi.recuperaEncabezado(Integer.parseInt(vendedor), dia);
				if (lista.size()==0){
					resp2.setCodigoEstado(-3);
					resp2.setDescripcionEstado("No existe informacion para datos ingresados");
					resp = gson.toJson(resp2);

				}else{
					VendedorMapaDTO dto = new VendedorMapaDTO();
					dto.setCodigoVendedor(Integer.parseInt(vendedor));
					dto.setZona(lista);
					resp = gson.toJson(dto);
				}
				
				
			}else{
				resp2.setCodigoEstado(-2);
				resp2.setDescripcionEstado("Debe indicar vendedor y Dia");
				resp = gson.toJson(resp2);

			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return resp;
	}
	
	public static void main (String []args){
		String input2 ="{\"vendedor\":\"1409\",\"dia\":\"JUE\"}";
		RecuperaVisitaClienteVendedorHelper hel = new RecuperaVisitaClienteVendedorHelper();
		hel.visitaCliente(input2);
	}
}
