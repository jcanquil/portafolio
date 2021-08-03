package cl.caserita.integracion.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import cl.caserita.transportista.helper.IntegracionTransportistaHelper;

@Path("/carguios")
public class IntegracionWMSWeb {

	@POST
    @Path("/post")
	@Produces("application/xml")
	public String getvalidaUsuario(String par000) {
		IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
		System.out.println(par000);
		return helper.procesa(par000);
	}
}