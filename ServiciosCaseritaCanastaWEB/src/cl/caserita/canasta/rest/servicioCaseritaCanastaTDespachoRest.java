package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ClienteHelper;
import cl.caserita.canastas.helper.TipoDespachoHelper;

@Path("/tdespacho")
public class servicioCaseritaCanastaTDespachoRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaCliente(String par000) {
 		
 		TipoDespachoHelper despacho = new TipoDespachoHelper();
 		
 		return despacho.obtieneTipoDespacho(par000);
		
	}
}
