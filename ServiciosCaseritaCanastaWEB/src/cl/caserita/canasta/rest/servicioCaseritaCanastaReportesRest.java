package cl.caserita.canasta.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.canastas.helper.ReportesHelper;
import cl.caserita.canastas.helper.UsuarioHelper;
@Path("/reportes")

public class servicioCaseritaCanastaReportesRest {

	
	


		
	 	@POST
	    @Path("/post")
	    @Produces(MediaType.APPLICATION_JSON)
	    public String getvalidaUsuario(String par000) {
	 		
	 		ReportesHelper reporte = new ReportesHelper();
	 		
	 		return reporte.verificaReporte(par000);
			
		}
	
}
