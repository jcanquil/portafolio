package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ClienteHelper;
import cl.caserita.canastas.helper.ReportesHelper;

@Path("/cliente")
public class servicioCaseritaCanastaClienteRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaCliente(String par000) {
 		
 		ClienteHelper cliente = new ClienteHelper();
 		
 		return cliente.obtieneCliente(par000);
		
	}
}
