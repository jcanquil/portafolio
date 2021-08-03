package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ReportesHelper;
import cl.caserita.canastas.helper.UsuarioHelper;

@Path("/registra")
public class servicioCaseritaCanastaRegistraRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String registraUsuario(String par000) {
 		
 		UsuarioHelper usuario = new UsuarioHelper();
 		
 		return usuario.actulizaDireccion(par000);
		
	}
}
