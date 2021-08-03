package cl.caserita.canastas.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import cl.caserita.dto.AdicionalesDTO;
import cl.caserita.xsd.usuario.Usuario;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConvierteStringDTO {
	private  static Logger log = Logger.getLogger(ConvierteStringDTO.class);

	public Usuario convierteUsuario(String par000){
		Usuario usuario = new Usuario();
		log.info("Parametro:"+par000);
		try{
			JSONObject json = (JSONObject)new JSONParser().parse(par000);
			
			
			

			//Lista
			JSONParser jsonParser = new JSONParser();
			if (json.get("rutCliente")!=null){
				usuario.setRutCliente((json.get("rutCliente").toString().replaceAll("\"", "")));
				log.info("usuario=" + json.get("rutCliente"));
			}
			if (json.get("digitoCliente")!=null){
				usuario.setDigitoCliente(String.valueOf(json.get("digitoCliente")).replaceAll("\"", ""));
			}
		    if (json.get("rutPersona")!=null){
		    	usuario.setRutPersona(json.get("rutPersona").toString().replaceAll("\"", ""));
		    }
		    if (json.get("digitoPersona")!=null){
		    	usuario.setDigitoPersona(String.valueOf(json.get("digitoPersona")).replaceAll("\"", ""));
		    }
			if (json.get("password")!=null){
				 usuario.setPassword(String.valueOf(json.get("password")).replaceAll("\"", ""));
				 log.info("password=" + json.get("password"));
			}
			if (json.get("solicitud")!=null){
				usuario.setSolicitud(json.get("solicitud").toString().replaceAll("\"", ""));
			}
			if (json.get("direccion")!=null){
				usuario.setDireccion(String.valueOf(json.get("direccion")).replaceAll("\"", ""));
			}
		    if (json.get("numero")!=null){
		    	usuario.setNumero(String.valueOf(json.get("numero")).replaceAll("\"", ""));
		    }
		    if (json.get("telefono")!=null){
		    	 usuario.setTelefono(String.valueOf(json.get("telefono")).replaceAll("\"", ""));
		    }
		   if (json.get("celular")!=null){
			   usuario.setCelular(String.valueOf(json.get("celular")).replaceAll("\"", ""));
		   }
		   if (json.get("email")!=null){
		    	usuario.setEmail(String.valueOf(json.get("email")).replaceAll("\"", ""));
		   }
		   if (json.get("nombrePersona")!=null){
			   usuario.setNombrePersona(String.valueOf(json.get("nombrePersona")).replaceAll("\"", ""));
		   }
		   if (json.get("tipoDespacho")!=null){
			   usuario.setTipoDespacho(String.valueOf(json.get("tipoDespacho")).replaceAll("\"", ""));
		   }
		   if (json.get("codigoRegion")!=null){
			   usuario.setCodigoRegion(String.valueOf(json.get("codigoRegion")).replaceAll("\"", ""));
		   }
		   if (json.get("codigoCiudad")!=null){
			   usuario.setCodigoCiudad(String.valueOf(json.get("codigoCiudad")).replaceAll("\"", ""));
		   }
		   if (json.get("codigoComuna")!=null){
			   usuario.setCodigoComuna(String.valueOf(json.get("codigoComuna")).replaceAll("\"", ""));
		   }
		   if (json.get("departamento")!=null){
			   usuario.setDepartamento(String.valueOf(json.get("departamento")).replaceAll("\"", "")); 
		   }
		   if (json.get("villaPoblacion")!=null){
			   usuario.setVillaPoblacion(String.valueOf(json.get("villaPoblacion")).replaceAll("\"", ""));
		   }
		   if (json.get("horario")!=null){
			   usuario.setHorario(String.valueOf(json.get("horario")).replaceAll("\"", ""));
		   }
		   if (json.get("observacionDespacho")!=null){
			   usuario.setObservacionDespacho(String.valueOf(json.get("observacionDespacho")).replaceAll("\"", ""));
		   }
		   if (json.get("contactoRetiro")!=null){
			   usuario.setContactoRetiro(String.valueOf(json.get("contactoRetiro")).replaceAll("\"", ""));
		   }
		    
		   if (json.get("latitud")!=null){
			   usuario.setLatitud(String.valueOf(json.get("latitud")).replaceAll("\"", ""));
		   }
		   if (json.get("longitud")!=null){
			   usuario.setLongitud(String.valueOf(json.get("longitud")).replaceAll("\"", ""));
		   }
		  if (json.get("descripcionTipoDespacho")!=null){
			  usuario.setDescripcionTipoDespacho(String.valueOf(json.get("descripcionTipoDespacho")).replaceAll("\"", ""));
		  }
		  if (json.get("correlativo")!=null){
			  usuario.setCorrelativo(String.valueOf(json.get("correlativo")).replaceAll("\"", ""));
		  }
		  if (json.get("fechaDespacho")!=null){
			  usuario.setFechaDespacho(String.valueOf(json.get("fechaDespacho")).replaceAll("\"", ""));
		  }
		  if (json.get("codigoArticulo")!=null){
			  usuario.setCodigoArticulo(String.valueOf(json.get("codigoArticulo")).replaceAll("\"", ""));
		  }
		  if (json.get("descripcionArticulo")!=null){
			  usuario.setDescripcionArticulo(String.valueOf(json.get("descripcionArticulo")).replaceAll("\"", ""));
		  }
		 
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return usuario;
		
	}
	
	public List convierteAdicionaes(String par000, Usuario usuario){
		log.info("Parametro:"+par000);
		List adicionales = new ArrayList();
		try{
			JSONObject json = (JSONObject)new JSONParser().parse(par000);

			//Lista
			JSONParser jsonParser = new JSONParser();
			
		  JSONArray lang= (JSONArray) json.get("adicionales");
		  List hijos = new ArrayList();
			 if (lang!=null){
				 Iterator i = lang.iterator();
				 while (i.hasNext()){
					
					 AdicionalesDTO adicionalesDTO = new AdicionalesDTO();
				    	JSONObject adicion = (JSONObject) i.next();
				    	if (adicion.get("correlativo")!=null){
				    		adicionalesDTO.setCorrelativo(Integer.parseInt(adicion.get("correlativo").toString().replaceAll("\"","")));
							
						 }
				    	if (adicion.get("fechaNacimiento")!=null){
				    		adicionalesDTO.setFechaNacimiento(adicion.get("fechaNacimiento").toString().replaceAll("\"",""));
							
						 }
						 if (adicion.get("nombresHijos")!=null){
							 adicionalesDTO.setNombresHijos(adicion.get("nombresHijos").toString().replaceAll("\"",""));
							 log.info("Nombre"+adicionalesDTO.getNombresHijos());
						 }
						 if (adicion.get("apellidosHijos")!=null){
							 adicionalesDTO.setApellidosHijos(adicion.get("apellidosHijos").toString().replaceAll("\"",""));
							 log.info("Apellidos"+adicionalesDTO.getApellidosHijos());
						 }
						 if (adicionalesDTO.getNombresHijos()!=null && adicionalesDTO.getApellidosHijos()!=null){
							 adicionalesDTO.setNombreCompleto(adicionalesDTO.getNombresHijos() + " " + adicionalesDTO.getApellidosHijos());
						 }
						 adicionalesDTO.setRutEmpresa(Integer.parseInt(usuario.getRutCliente()));
						 adicionalesDTO.setDvEmpresa(usuario.getDigitoCliente().trim());
						 adicionalesDTO.setRutPersona(Integer.parseInt(usuario.getRutPersona()));
						 adicionalesDTO.setDvPersona(usuario.getDigitoPersona());
						 adicionales.add(adicionalesDTO);
						 
				 }
				
			 }
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return adicionales;
		
	}

}
