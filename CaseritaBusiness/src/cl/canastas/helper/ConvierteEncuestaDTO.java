package cl.caserita.canastas.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import cl.caserita.dto.AdicionalesDTO;
import cl.caserita.dto.PryenccDTO;
import cl.caserita.xsd.usuario.Usuario;

public class ConvierteEncuestaDTO {

	private  static Logger log = Logger.getLogger(ConvierteStringDTO.class);

	public PryenccDTO convierteEncuesta(String par000){
		PryenccDTO pryenccDTO = new PryenccDTO();
		log.info("Parametro:"+par000);
		try{
			JSONObject json = (JSONObject)new JSONParser().parse(par000);
			//Lista
			JSONParser jsonParser = new JSONParser();
			if (json.get("rutCliente")!=null){
				pryenccDTO.setRutCliente(Integer.parseInt(json.get("rutCliente").toString().replaceAll("\"", "")));
				log.info("usuario=" + json.get("rutCliente"));
			}
			if (json.get("digitoCliente")!=null){
				pryenccDTO.setDvCliente(String.valueOf(json.get("digitoCliente")).replaceAll("\"", ""));
			}
		    if (json.get("rutPersona")!=null){
		    	pryenccDTO.setRutPersonal(Integer.parseInt(json.get("rutPersona").toString().replaceAll("\"", "")));
		    }
		    if (json.get("digitoPersona")!=null){
		    	pryenccDTO.setDvPersonal(String.valueOf(json.get("digitoPersona")).replaceAll("\"", ""));
		    }
		    log.info("Ingresa Convierte");
		    List encuenta=new ArrayList();
		    JSONArray lang= (JSONArray) json.get("encuesta");
			  List hijos = new ArrayList();
				 if (lang!=null){
					 Iterator i = lang.iterator();
					 while (i.hasNext()){
						 	PryenccDTO pryenccDTO2=new PryenccDTO();
					    	JSONObject adicion = (JSONObject) i.next();
					    	if (adicion.get("codigoEncuesta")!=null){
								pryenccDTO2.setCodigoEncuesta(Integer.parseInt(adicion.get("codigoEncuesta").toString().replaceAll("\"", "")));
						    }
						    if (adicion.get("descripcionEncuesta")!=null){
						    	pryenccDTO2.setDescripcionEncuesta(String.valueOf(adicion.get("descripcionEncuesta")).replaceAll("\"", ""));
						    }
						    if (adicion.get("estadoEncuesta")!=null){
						    	pryenccDTO2.setEstadoRespuesta(String.valueOf(adicion.get("estadoEncuesta")).replaceAll("\"", ""));
						    }
						    
						    if (adicion.get("sugerenciaEncuesta")!=null){
						    	pryenccDTO2.setSugerenciaEncuesta(String.valueOf(adicion.get("sugerenciaEncuesta")).replaceAll("\"", ""));
						    }
						    log.info("Procesa datos encuesta:"+pryenccDTO2.getCodigoEncuesta());
						    log.info("Procesa datos encuesta1:"+pryenccDTO2.getEstadoRespuesta());
						    log.info("Procesa datos encuesta2:"+pryenccDTO2.getSugerenciaEncuesta());
						    log.info("Procesa datos encuesta3:");

						    encuenta.add(pryenccDTO2);

					 }
					
				 }
				 
				pryenccDTO.setLista(encuenta);
				 
		    
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return pryenccDTO;
		
	}
	public static void main (String []args){
		List lista = new ArrayList();
		PryenccDTO pryenccDTO = new PryenccDTO();
		pryenccDTO.setRutCliente(991919);
		pryenccDTO.setDvCliente("9");
		pryenccDTO.setRutPersonal(818181);
		pryenccDTO.setDvPersonal("9");
		PryenccDTO pryenccDTO2 = new PryenccDTO();
		pryenccDTO2.setCodigoEncuesta(2);
		pryenccDTO2.setEstadoRespuesta("S");
		pryenccDTO2.setSugerenciaEncuesta("");
		lista.add(pryenccDTO2);
		 
		 PryenccDTO pryenccDTO3 = new PryenccDTO();
			pryenccDTO3.setCodigoEncuesta(3);
			pryenccDTO3.setEstadoRespuesta("S");
			pryenccDTO3.setSugerenciaEncuesta("");
			lista.add(pryenccDTO3);

			 
			 
			 PryenccDTO pryenccDTO4 = new PryenccDTO();
				pryenccDTO4.setCodigoEncuesta(4);
				pryenccDTO4.setEstadoRespuesta("S");
				pryenccDTO4.setSugerenciaEncuesta("");
				lista.add(pryenccDTO4);

				 pryenccDTO.setLista(lista);
				 System.out.println("Lista");
				 
				 

	}
}
