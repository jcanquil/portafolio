package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ClienteHelper;
import cl.caserita.canastas.helper.HorarioHelper;

@Path("/horario")
public class servicioCaseritaCanastaHorarioRest {

	@POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String listaCliente(String par000) {
 		
 		HorarioHelper horario = new HorarioHelper();
 		
 		return horario.recuperaHorarios(par000);
		
	}
	
}
