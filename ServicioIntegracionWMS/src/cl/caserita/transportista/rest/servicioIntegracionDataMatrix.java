package cl.caserita.transportista.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/wsDataMatrix")


public class servicioIntegracionDataMatrix {
	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
		public String getvalidaUsuario(String par000) {
			System.out.println(par000);
			
		return par000;
	}
}
