package cl.caserita.ecommerce.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClienteeCommerceDTO;
import cl.caserita.dto.ClmcliDTO;

public class ConvierteJSONDTO {
	private  static Logger log = Logger.getLogger(ConvierteJSONDTO.class);

	public ClienteeCommerceDTO convirteCliente(String par000){
		ClmcliDTO clmcliDTO = new ClmcliDTO();
		ClidirDTO clidirDTO = new ClidirDTO();
		List direcciones = new ArrayList();
		List artic = new ArrayList();
		ClienteeCommerceDTO cliente = new ClienteeCommerceDTO();
		log.info("Parametro Entrada"+par000);
		Gson gson = new Gson();
		try{
			JSONObject object = (JSONObject) new JSONParser().parse(par000);
			 
			 if (object.get("rutCliente")!=null){
				 cliente.setRutCliente(object.get("rutCliente").toString().replaceAll("\"",""));
			 }
			 if (object.get("dvCliente")!=null){
				 cliente.setDvCliente(object.get("dvCliente").toString().replaceAll("\"",""));
			 }
			 if (object.get("razonSocial")!=null){
				 cliente.setRazonSocial(object.get("razonSocial").toString().replaceAll("\"",""));
			 }
			 if (object.get("codigoGiro")!=null){
				 cliente.setCodigoGiro(object.get("codigoGiro").toString().replaceAll("\"",""));
			 }
			 if (object.get("giro")!=null){
				 cliente.setGiro(object.get("giro").toString().replaceAll("\"",""));
			 }
			 if (object.get("idTransaccionBanco")!=null){
				 cliente.setIdBanco(object.get("idTransaccionBanco").toString().replaceAll("\"",""));
			 }
			 if (object.get("fechaDespacho")!=null){
				 cliente.setFechaDespacho(object.get("fechaDespacho").toString().replaceAll("\"",""));
				 if (cliente.getFechaDespacho().length()<8){
						String ano = cliente.getFechaDespacho().substring(0, 4);
						String mes = cliente.getFechaDespacho().substring(4, 6);
						String dia = cliente.getFechaDespacho().substring(6, 7);
						String fechaCompleta = ano+mes+"0"+dia;
						cliente.setFechaDespacho(fechaCompleta);
						
				}
			 }
			 if (object.get("formaPago")!=null){
				 cliente.setFormaPago(object.get("formaPago").toString().replaceAll("\"",""));
			 }
			 if (object.get("proveedor")!=null){
				 cliente.setProveedor(object.get("proveedor").toString().replaceAll("\"",""));
			 }
			 if (object.get("tipoDocumento")!=null){
				 cliente.setTipoDocumento(object.get("tipoDocumento").toString().replaceAll("\"",""));
			 }
			 JSONArray lang= (JSONArray) object.get("direcciones");
			 if (lang!=null){
				 Iterator i = lang.iterator();
				 while (i.hasNext()){
				    	JSONObject objectDire = (JSONObject) i.next();
				    	if (objectDire.get("tipoDireccion")!=null){
				    		clidirDTO.setTipoDireccion(Integer.parseInt(objectDire.get("tipoDireccion").toString().replaceAll("\"","")));
				    	}
				    	
				    	if (objectDire.get("direccionCliente")!=null){
				    		clidirDTO.setDireccionCliente(objectDire.get("direccionCliente").toString().replaceAll("\"",""));
				    	}
				    	
						if (objectDire.get("numeroDireccion")!=null){
							clidirDTO.setNumeroDireccion(Integer.parseInt(objectDire.get("numeroDireccion").toString().replaceAll("\"","")));			    		
										    	}
						if (objectDire.get("depto")!=null){
							//clidirDTO.setDepto(objectDire.get("depto").toString().replaceAll("\"",""));
							clidirDTO.setDepartamentoString(objectDire.get("depto").toString().replaceAll("\"","").trim());
						}
						if (objectDire.get("villaPoblacion")!=null){
							clidirDTO.setVillaPoblacion(objectDire.get("villaPoblacion").toString().replaceAll("\"",""));
						}
						if (objectDire.get("telefono")!=null){
							clidirDTO.setTelefono(objectDire.get("telefono").toString().replaceAll("\"",""));
							/*if (clidirDTO.getTelefono().length()>10){
								clidirDTO.setTelefono(clidirDTO.getTelefono().substring(1,10));
							}*/
						}
						if (objectDire.get("celular")!=null){
							if (objectDire.get("celular").toString().length()<10){
								log.info("celular :"+objectDire.get("celular"));
								clidirDTO.setCelular("0"+objectDire.get("celular").toString().replaceAll("\"",""));

							}else{
								log.info("celular :"+objectDire.get("celular"));

								clidirDTO.setCelular(objectDire.get("celular").toString().replaceAll("\"",""));

							}
							if (clidirDTO.getCelular().length()>10){
								clidirDTO.setCelular(clidirDTO.getCelular().substring(1, 10));
							}
						}
						if (objectDire.get("nombreContacto")!=null){
							clidirDTO.setNombreContacto(objectDire.get("nombreContacto").toString().replaceAll("\"",""));
						}
						if (objectDire.get("mail")!=null){
							clidirDTO.setMail(objectDire.get("mail").toString().replaceAll("\"",""));
						}
						
						if (objectDire.get("region")!=null){
							clidirDTO.setRegion(Integer.parseInt(objectDire.get("region").toString().replaceAll("\"","")));
						}
						if (objectDire.get("ciudad")!=null){
							clidirDTO.setCiudad(Integer.parseInt(objectDire.get("ciudad").toString().replaceAll("\"","")));
						}
						if (objectDire.get("comuna")!=null){
							clidirDTO.setComuna(Integer.parseInt(objectDire.get("comuna").toString().replaceAll("\"","")));
						}
						/*if (objectDire.get("latitud")!=null){
							clidirDTO.setDireccionCliente(objectDire.get("latitud").toString().replaceAll("\"",""));
						}
						if (objectDire.get("longitud")!=null){
							clidirDTO.setDireccionCliente(objectDire.get("longitud").toString().replaceAll("\"",""));
						}*/
						
						if (objectDire.get("observacion")!=null){
							clidirDTO.setObservacion(objectDire.get("observacion").toString().replaceAll("\"",""));
						}
						direcciones.add(clidirDTO);
						 clidirDTO = new ClidirDTO();


				 }
				 cliente.setDirecciones(direcciones);

			 }
			 JSONArray articulos= (JSONArray) object.get("articulos");
			 
			 if (articulos!=null){
				 Iterator i = articulos.iterator();
				 while (i.hasNext()){
				    	JSONObject objectDire = (JSONObject) i.next();
				    	CldmcoDTO cldmco = new CldmcoDTO();
				    	if (objectDire.get("codigoArticulo")!=null){
				    		cldmco.setCodigoArticulo(Integer.parseInt(objectDire.get("codigoArticulo").toString().replaceAll("\"","")));
				    		
				    	}
				    	
				    	if (objectDire.get("cantidadArticulo")!=null){
				    		cldmco.setCantidadArticulo(Integer.parseInt(objectDire.get("cantidadArticulo").toString().replaceAll("\"","")));
				    		
				    	}
				    	
						if (objectDire.get("precioNeto")!=null){
							cldmco.setPrecioNeto(Double.parseDouble(objectDire.get("precioNeto").toString().replaceAll("\"","")));
							//clidirDTO.setDireccionCliente(objectDire.get("precioNeto").toString().replaceAll("\"",""));			
							log.info("Precio Neto:"+cldmco.getPrecioNeto());
										    	}
						if (objectDire.get("precioBruto")!=null){
							cldmco.setPrecio(Double.parseDouble(objectDire.get("precioBruto").toString().replaceAll("\"","")));
							//clidirDTO.setDireccionCliente(objectDire.get("precioNeto").toString().replaceAll("\"",""));		
							log.info("Precio Bruto:"+cldmco.getPrecioNeto());

										    	}
						artic.add(cldmco);
						
				 }
				 cliente.setArticulos(artic);
				  
			 }
			 
			 
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		 
		return cliente;
	}
	public static void main (String []args){
		String telefono="2016125";
		if (telefono.length()<8){
			String ano = telefono.substring(0, 4);
			String mes = telefono.substring(4, 6);
			String dia = telefono.substring(6, 7);
			
		}
		System.out.println("Error");
	}
}
