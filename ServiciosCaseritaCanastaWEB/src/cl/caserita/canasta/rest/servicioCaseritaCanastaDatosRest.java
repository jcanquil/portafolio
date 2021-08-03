package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ReportesHelper;
import cl.caserita.canastas.helper.UsuarioHelper;

@Path("/obtieneDatos")
public class servicioCaseritaCanastaDatosRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String getvalidaUsuario(String par000) {
 		
 		ReportesHelper usuario = new ReportesHelper();
 		
 		return usuario.despachoIndivual(par000);
		
	}
	
}
