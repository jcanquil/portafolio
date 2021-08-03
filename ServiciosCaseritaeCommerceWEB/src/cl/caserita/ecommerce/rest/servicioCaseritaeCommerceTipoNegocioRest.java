package cl.caserita.ecommerce.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.caserita.ecommerce.helper.ClienteeCommerceHelper;
import cl.caserita.ecommerce.helper.TipoNegocioHelper;



	@Path("/tnegocio")
	public class servicioCaseritaeCommerceTipoNegocioRest {

		@POST
	    @Path("/post")
	    @Produces(MediaType.APPLICATION_JSON)
	    public String listaCliente(String par000) {
	 		
	 		TipoNegocioHelper tnegocio = new TipoNegocioHelper();
	 		
	 		return tnegocio.tipoNegocio();
			
		}
		
}
