package cl.caserita.transporte.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import cl.caserita.canastas.helper.ClienteHelper;

@Path("/dataMatrix")

public class servicioRespuestaTransporte {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String logica(String par000) {
 		System.out.println(par000);
		String resp ="{\"carguios\":\"9999\",\"carguios_ok\":\"9999\",\"orders\":\"222\",\"orders_ok\":\"111\"}";
		Gson gson = new Gson();
		gson.toJson(resp);
		System.out.println("Respuesta :"+resp);
 		return resp;
		
	}
}
