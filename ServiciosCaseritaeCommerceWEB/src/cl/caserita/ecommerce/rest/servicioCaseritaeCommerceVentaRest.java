package cl.caserita.ecommerce.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import cl.caserita.canastas.helper.RegionalesHelper;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClienteeCommerceDTO;
import cl.caserita.ecommerce.helper.ClienteeCommerceHelper;
import cl.caserita.ecommerce.helper.ProcesaVentaHelper;
@Path("/ventas")
public class servicioCaseritaeCommerceVentaRest {

	
	

		@POST
	    @Path("/post")
	    @Produces(MediaType.APPLICATION_JSON)
	    public String listaCliente(String par000) {
	 		
	 		ProcesaVentaHelper helper = new ProcesaVentaHelper();
	 		System.out.println("Parametro Entrada:"+par000);
	 		
	 		return helper.generaVenta(par000);
			
		}
	
	public static void main (String [] args){
		String respuesta="";
		ClidirDTO clidir = new ClidirDTO();
		CldmcoDTO cldmco = new CldmcoDTO();
		ClienteeCommerceDTO cliente = new ClienteeCommerceDTO();
		List direc = new ArrayList();
		List articulo = new ArrayList();
		
		
		cliente.setRutCliente("15448543");
		cliente.setDvCliente("0");
		cliente.setRazonSocial("JAIME CANQUIL");
		cliente.setGiro("ALMACEN");
		
		
		clidir.setTipoDireccion(1);
		clidir.setDireccionCliente("LASTRA");
		clidir.setNumeroDireccion(657);
		clidir.setTelefono("0225457970");
		clidir.setCelular("97009844");
		clidir.setDepto(123);
		clidir.setVillaPoblacion("12 oct.");
		clidir.setRegion(13);
		clidir.setCiudad(1);
		clidir.setComuna(1);
		clidir.setMail("jcanquil@caserita.cl");
		
		clidir.setNombreContacto("JAIME CANQUIL");
		
		
		
		ClidirDTO clidir2 = new ClidirDTO();
		clidir2.setTipoDireccion(2);
		clidir2.setDireccionCliente("LASTRA");
		clidir2.setNumeroDireccion(657);
		clidir2.setTelefono("0225457970");
		clidir2.setCelular("97009844");
		clidir2.setDepto(123);
		clidir2.setVillaPoblacion("12 oct.");
		clidir2.setRegion(13);
		clidir2.setCiudad(1);
		clidir2.setComuna(1);
		clidir2.setMail("jcanquil@caserita.cl");
		clidir2.setNombreContacto("JAIME CANQUIL");
		direc.add(clidir);
		direc.add(clidir2);
		cliente.setDirecciones(direc);
		
		CldmcoDTO cldmco3 = new CldmcoDTO();
		cldmco3.setCodigoArticulo(7777777);
		cldmco3.setCantidadArticulo(50);
		cldmco3.setPrecioNeto(19900);
	
		
		cldmco.setCodigoArticulo(17812);
		cldmco.setCantidadArticulo(100);
		
		
		CldmcoDTO cldmco2 = new CldmcoDTO();
		cldmco2.setCodigoArticulo(17813);
		cldmco2.setCantidadArticulo(50);
		articulo.add(cldmco);
		articulo.add(cldmco2);
		articulo.add(cldmco3);
		cliente.setArticulos(articulo);
		
		
		Gson gson = new Gson();
		respuesta = gson.toJson(cliente);
		
	}
}
