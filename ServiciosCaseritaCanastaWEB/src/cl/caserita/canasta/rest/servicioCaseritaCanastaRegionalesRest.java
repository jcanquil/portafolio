package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ClienteHelper;
import cl.caserita.canastas.helper.RegionalesHelper;

@Path("/regiones")
public class servicioCaseritaCanastaRegionalesRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaCliente(String par000) {
 		
 		RegionalesHelper helper = new RegionalesHelper();
 		
 		return helper.regionales(par000);
		
	}
}
