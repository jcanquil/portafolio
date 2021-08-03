package cl.caserita.canastas.helper;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.EncprinDAO;
import cl.caserita.dao.iface.PryenccDAO;
import cl.caserita.dto.PryenccDTO;
import cl.caserita.dto.RespuestaCanastaDTO;
import cl.caserita.xsd.usuario.Usuario;

public class EncuestaHelper {
	private  static Logger log = Logger.getLogger(EncuestaHelper.class);

	public String obtieneEncuesta(String par000){
		String resp="";
		
		Gson gson = new Gson();
		log.info("Centro Despacho:"+par000);
		JsonObject json = (JsonObject)new JsonParser().parse(par000);
		int rut=0;
		int rutPersona=0;
		if (json.get("rutCliente")!=null){
			rut = Integer.parseInt(json.get("rutCliente").toString().replaceAll("\"", "").trim());
		}
		if (json.get("rutPersona")!=null){
			rutPersona = Integer.parseInt(json.get("rutPersona").toString().replaceAll("\"", "").trim());
		}
		RespuestaCanastaDTO respuesta = new RespuestaCanastaDTO();

		if (rut>0){
			DAOFactory dao = DAOFactory.getInstance();
			PryenccDAO pryDAO = dao.getPryenccDAO();

			EncprinDAO encprin = dao.getEncprinDAO();
			if (pryDAO.obtieneEncuesta(rut, rutPersona)==0){
				List lista = encprin.obtieneEncuesta(rut);
				resp = gson.toJson(lista);
			}else{
				respuesta.setCodigoEstado(-2);
				respuesta.setDescripcionEstado("USUARIO YA REALIZO ENCUESTA");
				resp = gson.toJson(respuesta);
			}
			

		}
		
		
		return resp;
	}
	public String registraEncuesta(String par000){
		String resp="";
		Gson gson = new Gson();
		RespuestaCanastaDTO respuesta = new RespuestaCanastaDTO();
		log.info("Centro Despacho:"+par000);
		ConvierteEncuestaDTO conv = new ConvierteEncuestaDTO();
		PryenccDTO pry = conv.convierteEncuesta(par000);
		if (pry!=null){
			DAOFactory dao = DAOFactory.getInstance();
			PryenccDAO pryDAO = dao.getPryenccDAO();
			if (pryDAO.obtieneEncuesta(pry.getRutCliente(), pry.getRutPersonal())==0){
				Iterator iter = pry.getLista().iterator();
				while (iter.hasNext()){
					PryenccDTO dto = (PryenccDTO) iter.next();
					dto.setRutCliente(pry.getRutCliente());
					dto.setDvCliente(pry.getDvCliente());
					dto.setRutPersonal(pry.getRutPersonal());
					dto.setDvPersonal(pry.getDvPersonal());
					log.info("rut:"+dto.getRutCliente());
					log.info("dv :"+dto.getDvCliente());
					log.info("rutPersona:"+dto.getRutPersonal());
					log.info("dvPersona:"+dto.getDvPersonal());
					log.info("cod Encuesta:"+dto.getCodigoEncuesta());
					log.info("estado:"+dto.getEstadoRespuesta());
					log.info("descripcion :"+dto.getSugerenciaEncuesta());
					if (pryDAO.generaRegistroEncuesta(dto)>0){
						respuesta.setCodigoEstado(0);
						respuesta.setDescripcionEstado("ENCUESTA REGISTRADA EXITOSAMENTE");
					}else{
						respuesta.setCodigoEstado(-1);
						respuesta.setDescripcionEstado("ENCUESTA NO REGISTRADA");
					}
				}
			}else{
				respuesta.setCodigoEstado(-2);
				respuesta.setDescripcionEstado("USUARIO YA REALIZO ENCUESTA");
				resp = gson.toJson(respuesta);
			}
			
		}else{
			respuesta.setCodigoEstado(-5);
			respuesta.setDescripcionEstado("RUT EMPRESA O RUT PERSONA NO VALIDOS");
			resp = gson.toJson(respuesta);
		}
		resp = gson.toJson(respuesta);
		return resp;
	}
}
