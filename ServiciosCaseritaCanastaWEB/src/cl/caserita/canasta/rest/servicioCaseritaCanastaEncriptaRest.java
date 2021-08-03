package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import cl.caserita.canastas.helper.ClienteHelper;
import cl.caserita.canastas.helper.EncriptaHelper;
import cl.caserita.dto.EncriptacionDTO;


@Path("/encripta")
public class servicioCaseritaCanastaEncriptaRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String encripta(String par000) {

		EncriptaHelper encripta = new EncriptaHelper();
		
		return encripta.clave(par000);
		
		
	}
}
