package cl.caserita.ecommerce.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ClienteHelper;
import cl.caserita.ecommerce.helper.ClienteeCommerceHelper;

@Path("/cliente")
public class servicioCaseritaeCommerceClienteRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaCliente(String par000) {
 		
 		ClienteeCommerceHelper cliente = new ClienteeCommerceHelper();
 		
 		return cliente.recuperaDireccionCliente(par000);
		
	}
	
}
