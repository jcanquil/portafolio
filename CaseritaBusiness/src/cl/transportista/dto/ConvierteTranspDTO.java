package cl.caserita.transportista.dto;

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

import cl.caserita.dto.CarguioTranspDTO;
import cl.caserita.dto.DetordTranspDTO;
import cl.caserita.dto.DetordTranspNcpDTO;
import cl.caserita.dto.DocumentoTranspDTO;
import cl.caserita.dto.OrdTranspDTO;
import cl.caserita.dto.OrdTranspNcpDTO;


public class ConvierteTranspDTO {
	private static Logger logi = Logger.getLogger(ConvierteTranspDTO.class);

	public DocumentoTranspDTO convierte(String par000, String dv){
		DocumentoTranspDTO transp = new DocumentoTranspDTO();
		JsonObject json = (JsonObject) new JsonParser().parse(par000);
		
		try{
		JSONObject object21 = (JSONObject) new JSONParser().parse(par000);
		CarguioTranspDTO carguio2 = null;
		 JSONArray lang33= (JSONArray) object21.get("carguio");
		    if (lang33!=null){
		    	 Iterator i = lang33.iterator();
				    int contadoCarguio=0;
				    while (i.hasNext()){
				    	JSONObject object22 = (JSONObject) i.next();
				    	if (contadoCarguio<lang33.size()){
				    		String listaOrdnees = lang33.get(contadoCarguio).toString();
					    	
				    		carguio2 = new CarguioTranspDTO();
				    		
				    		if (("null".equals(object22.get("rutChofer")))){
				    			transp.setSolicitud("1001");
					    		break;
				    		 }
				    		 if (("null".equals(object22.get("dvChofer")))){
				    			//transp.setSolicitud("1002");
						    	//break;
				    		 }
					    	String rut =String.valueOf(object22.get("rutChofer")).replaceAll("\"", "").trim();
					    	transp.setRutChofer(Integer.parseInt(rut));
					    	transp.setDvChofer(dv);
					    	//transp.setDvChofer(String.valueOf(object22.get("dvChofer")).replaceAll("\"", ""));
					    	transp.setSolicitud(String.valueOf(json.get("solicitud")).replaceAll("\"", ""));   	
				    	}
				    }
		    }
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		

		JSONParser jsonParser = new JSONParser();
	  
	    List orden = new ArrayList();
	    List carguios = new ArrayList();
	    List detalle = null;
	    List detfoto = null;
	    
	    CarguioTranspDTO carguio = null;
	    OrdTranspNcpDTO ordenes = null;
	    DetordTranspNcpDTO detord = null;
	    DetordTranspNcpDTO detordfoto = null;
	    
	   try{
		   JSONObject jsonObject = (JSONObject) jsonParser.parse(par000);
		   
		  
		    JSONArray lang= (JSONArray) jsonObject.get("carguio");
		    if (lang!=null){
		    	 Iterator i = lang.iterator();
		    	 	carguio = new CarguioTranspDTO();		    	 
				    int contadoCarguio=0;
 				    while (i.hasNext()){
				    	JSONObject object = (JSONObject) i.next();
				    	if (contadoCarguio<lang.size()){
				    		String listaOrdnees = lang.get(contadoCarguio).toString();
					    	
					    	carguio = new CarguioTranspDTO();
				    		
					    	if (("null".equals(object.get("numeroCarguio")))){
				    			transp.setSolicitud("1003");
					    		break;
				    		 }
				    		 if (("null".equals(object.get("rutChofer")))){
				    			transp.setSolicitud("1001");
						    	break;
				    		 }
				    		 
				    		 
					    	if ("".equals((object.get("numeroCarguio").toString().replaceAll("\"", "").trim()))){
					    		transp.setSolicitud("1003");
					    		break;
					    	}
					    	if ("".equals((object.get("rutChofer").toString().replaceAll("\"", "")))){
					    		transp.setSolicitud("1001");
					    		break;
					    	}
					    	
				    		carguio.setNumeroCarguio(Integer.parseInt(object.get("numeroCarguio").toString().replaceAll("\"", "")));
				    		
					    	if ("".equals((object.get("version").toString().replaceAll("\"", "")))){
					    		carguio.setVersion(0);
					    	}
					    	else {
					    	carguio.setVersion(Integer.parseInt(object.get("version").toString().replaceAll("\"", "")));
					    	}
						    	
				    		
					    	carguio.setRutChofer(Integer.parseInt(object.get("rutChofer").toString().replaceAll("\"", "").trim()));
					    	
					    	logi.info("NUMERO CARGUIO ------------------>    "+object.get("numeroCarguio").toString().replaceAll("\"", ""));
					    	
					    	String icodart="";
					    	String icorrel="";
					    	String iformat="";
					    	String icantid="";
					    	String irecepc="";
					    	
					    	if (object.get("numeroCarguio")!=null){
					    		JSONObject jsonObject2 = (JSONObject) jsonParser.parse(listaOrdnees);
					    		JSONArray lang2= (JSONArray) jsonObject2.get("ordenes");
					    		if (lang2!=null){
					    			Iterator it = lang2.iterator();
					    			while (it.hasNext()){
						    			JSONObject object2 = (JSONObject) it.next();
						    			ordenes = new OrdTranspNcpDTO();
						    			
						    			if ("".equals(object2.get("numeroDocumento").toString().replaceAll("\"", ""))) {
						    				transp.setSolicitud("1004");
						    				break;
						    			}
						    			if ("".equals(object2.get("tipoDocumento").toString().replaceAll("\"", ""))) {
						    				transp.setSolicitud("1005");
						    				break;
						    			}
						    			if ("".equals(object2.get("codEstado").toString().replaceAll("\"", ""))) {
						    				transp.setSolicitud("1006");
						    				break;
						    			}
						    			if ("".equals(object2.get("descEstado").toString().replaceAll("\"", ""))) {
						    				transp.setSolicitud("1007");
						    				break;
						    			}
						    			
						    			if (object2.get("numeroDocumento")!=null){
						    				
						    				String cadena=object2.get("numeroDocumento").toString().replaceAll("\"", "");
									    	int paip = cadena.indexOf("|");
									    	if (paip>0){
								    			String pipe="";
									    		pipe = cadena.substring(paip+1);
									    		String numdocsinpaip=cadena.substring(0,paip);
									    		carguio.setVersion(Integer.parseInt(pipe));
									    		ordenes.setNumDocumento(Integer.parseInt(numdocsinpaip));
									    		
										    } else {
									    		
										    	ordenes.setNumDocumento(Integer.parseInt(object2.get("numeroDocumento").toString().replaceAll("\"", "")));
										    }
									    	
						    				logi.info("NUMERO DOCTO  ------------------>    "+object2.get("numeroDocumento").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("tipoDocumento")!=null){
						    				ordenes.setTipoDocumento(Integer.parseInt(object2.get("tipoDocumento").toString().replaceAll("\"", "")));
						    				
						    				logi.info("TIPO DOCTO    ------------------>     "+object2.get("tipoDocumento"));
						    			}
						    			
						    			
						    			if (object2.get("codEstado")!=null){
					    					ordenes.setCodEstado(object2.get("codEstado").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("descEstado")!=null){
						    				ordenes.setDescEstado(object2.get("descEstado").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("codMotivo")!=null){
					    					ordenes.setCodMotivo(object2.get("codMotivo").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("Descmotivo")!=null){
						    				ordenes.setDesMotivo(object2.get("Descmotivo").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("timestamp")!=null){
						    				ordenes.setTimestamp(object2.get("timestamp").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("comentario")!=null){
						    				ordenes.setComentario(object2.get("comentario").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("latitud")!=null){
						    				ordenes.setLatitud(object2.get("latitud").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("longitud")!=null){
						    				ordenes.setLongitud(object2.get("longitud").toString().replaceAll("\"", ""));
						    			}
						    			
						    			if (object2.get("distancia")!=null){
						    				ordenes.setDistancia(object2.get("distancia").toString().replaceAll("\"", ""));
						    			}
						    			
						    			String ifoto="";
						    			String icome="";
						    			
						    			JSONArray lang4= (JSONArray) object2.get("fotos");
						    			if (lang4!=null){
							    			Iterator it4 = lang4.iterator();
							    		    detfoto = new ArrayList();

							    			while (it4.hasNext()){
							    				detordfoto = new DetordTranspNcpDTO();
								    			JSONObject object4 = (JSONObject) it4.next();
								    			if (object4.get("foto")!=null){
								    				detordfoto.setFoto(object4.get("foto").toString().replaceAll("\"", ""));
								    				ifoto=object4.get("foto").toString().replaceAll("\"", "");
								    			}
								    			if (object4.get("comentario")!=null){
								    				detordfoto.setComentario(object4.get("comentario").toString().replaceAll("\"", ""));
								    				icome=object4.get("comentario").toString().replaceAll("\"", "");
								    			}
								    			detfoto.add(detordfoto);
								    			
								    		}
						    				
						    			}
						    			
						    				
						    			JSONArray lang3= (JSONArray) object2.get("detalle");
							    		
							    		if (lang3!=null){
							    			Iterator it3 = lang3.iterator();
							    		    detalle = new ArrayList();

							    			while (it3.hasNext()){
							    				detord = new DetordTranspNcpDTO();
								    			JSONObject object3 = (JSONObject) it3.next();
								    			if (object3.get("correlativo")!=null){
								    				detord.setCorrelativo( object3.get("correlativo").toString().replaceAll("\"", ""));
								    				icorrel=object3.get("correlativo").toString().replaceAll("\"", "");
								    			}
								    			if (object3.get("codigoArticulo")!=null){
								    				detord.setCodigoArticulo(object3.get("codigoArticulo").toString().replaceAll("\"", ""));
								    				
								    				icodart=object3.get("codigoArticulo").toString().replaceAll("\"", "");
								    			}
								    			if (object3.get("descripcionArticulo")!=null){
								    				detord.setDescripcionArticulo( object3.get("descripcionArticulo").toString().replaceAll("\"", ""));
								    			}
								    			if (object3.get("formato")!=null){
								    				detord.setFormato(object3.get("formato").toString().replaceAll("\"", ""));
								    				
								    				iformat=object3.get("formato").toString().replaceAll("\"", "");
								    			}
								    			if (object3.get("cantidad")!=null){
								    				detord.setCantidad(Integer.parseInt(object3.get("cantidad").toString().replaceAll("\"", "")));
								    				
								    				icantid=object3.get("cantidad").toString().replaceAll("\"", "");
								    			}
								    			if (object3.get("cantidadRecepcionada")!=null){
								    				detord.setCantidadrecepcionada(Integer.parseInt(object3.get("cantidadRecepcionada").toString().replaceAll("\"", "")));
								    				
								    				irecepc=object3.get("cantidadRecepcionada").toString().replaceAll("\"", "");
								    			}
								    				
								    			//logi.info("linea : "+icodart+" | "+icorrel+" | "+iformat+ " | "+icantid+" | "+irecepc);
								    				
								    			detalle.add(detord);
								    			
								    		}
							    					    		
							    		}
							    			
							    		ordenes.setDetfoto(detfoto);
						    			ordenes.setDetord(detalle);
						    			orden.add(ordenes);
						    			carguio.setOrdenes(orden);

						    		}
						    		contadoCarguio=contadoCarguio+1;
					    			carguios.add(carguio);
						    		
					    		}
					    		
						    	transp.setCarguio(carguios);

					    	}
				    	}	
				    }
		    }
		   
		    
		    
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
		  
		return transp;
		
	}
	
	
	
}
