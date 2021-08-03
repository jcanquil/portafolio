package cl.caserita.transporte.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
@Path("/dataMatrix2")

public class servicioRespuestaTransporte2 {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String logica(String par000) {
 		System.out.println(par000);
		String resp ="{\"carguios\":\"9999\",\"carguios_ok\":\"9999\"}";
		Gson gson = new Gson();
		gson.toJson(resp);
		System.out.println("Respuesta :"+resp);
 		return resp;
		
	}
}
