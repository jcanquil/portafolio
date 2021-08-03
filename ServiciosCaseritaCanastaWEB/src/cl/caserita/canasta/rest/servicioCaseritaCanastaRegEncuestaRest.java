package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.EncuestaHelper;

@Path("/Regencuesta")

public class servicioCaseritaCanastaRegEncuestaRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaCliente(String par000) {
 		
 		EncuestaHelper helper = new EncuestaHelper();
 		
 		return helper.registraEncuesta(par000);
		
	}
}
