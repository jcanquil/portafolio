package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.CentroDespachoHelper;



@Path("/despacho")
public class servicioCaseritaCanastaDespachoRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String getvalidaUsuario(String par000) {
 		
 		CentroDespachoHelper helper = new CentroDespachoHelper();
 		
 		return helper.recuperaCentroDespacho(par000);
		
	}
}
