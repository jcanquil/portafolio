package cl.caserita.procesos.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.CoordenadasDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.integracion.ProcesaGeoreferenciaDireccion;
import cl.caserita.integracion.ProcesaGeoreferenciaInicial;

import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;

public class ProcesaLatitudLongitudHelper {

	public static void main (String arg[]) {
		
		
				String direccion = "Lastra657Recoleta";
				String key="&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4";
				System.out.println("Direccion:"+direccion);
				final Geocoder geocoder = new Geocoder();
				   GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(direccion+","+ "Chile").setLanguage("es").getGeocoderRequest();
				   GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
				   StringBuffer tmp = new StringBuffer(); 
			        String texto = new String(); 
			        String archivoLog="http://maps.googleapis.com/maps/api/geocode/json";
			      // String archivo="https://maps.googleapis.com/maps/api/geocode/xml?address=Lastra 657, Recoleta&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4";
			        String archivo="https://maps.googleapis.com/maps/api/geocode/xml?address=Lastra 657, Recoleta&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false";
			      
			        
					try { 
						
					        
					       URL url = new URL(archivo);
					    		   
					    		   BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
					      				
					      	 
					      			String output;
					      			System.out.println("Output from Server .... \n");
					      			while ((output = br.readLine()) != null) {
					      				System.out.println(output);
					      			}
					      			
					        	  
					        	  
					        	  //GoogleResponse response = (GoogleResponse)mapper.readValue(in,GoogleResponse.class);
					        	//  in.close();
					        	  
					        	  
					        	  
					        
			            // Crea la URL con del sitio introducido, ej: http://google.com 
						 URL url2 = new URL(archivoLog); 
						 
					  
						 //BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) (new URL(urlString)).openConnection()).getInputStream(), Charset.forName("UTF-8")));

			     
			            // Lector para la respuesta del servidor 
			            BufferedReader in2 = new BufferedReader(new InputStreamReader(((HttpURLConnection) (new URL(archivoLog)).openConnection()).getInputStream(), Charset.forName("UTF-8"))); 
			            String str; 
			            while ((str = in2.readLine()) != null) { 
			                tmp.append(str); 
			            } 
			            //url.openStream().close();
			            in2.close(); 
			            texto = tmp.toString(); 
			           
			        }catch (MalformedURLException e) { 
			            texto = "<h2>No esta correcta la URL</h2>".toString(); 
			        } catch (IOException e) { 
			        	e.printStackTrace();
			            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
			            } 
				 
					 
						   
		
	}
	public void procesaSolo(){
		String direccion = "Lastra 657,Recoleta&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4";
		String key="&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4";
		System.out.println("Direccion:"+direccion);
		String archivo="https://maps.googleapis.com/maps/api/geocode/xml?address=Lastra 657, Recoleta&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false";
	      StringBuffer tmp = new StringBuffer();
        
		try { 
			
		        
		       URL url = new URL(archivo);
		    		   System.out.println("URL:"+url);
		    		   BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		      				
		      	 
		      			String output;
		      			System.out.println("Output from Server .... \n");
		      			while ((output = br.readLine()) != null) {
		      				tmp.append(output);
		      			}
		      			System.out.println("Texto"+tmp.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void procesa(){
		DAOFactory dao = DAOFactory.getInstance();
		OrdvtaDAO ordvta = dao.getOrdvtaDAO();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		ClidirDAO clidirDAO = dao.getClidirDAO();
		
		List ord = ordvta.obtieneOrdenesActualizaLatLon(2);
		Iterator iter = ord.iterator();
		while (iter.hasNext()){
			OrdvtaDTO ordDTO = (OrdvtaDTO) iter.next();
			List cli = ordDTO.getClidir();
			Iterator itercli = cli.iterator();
			System.out.println("Numero OV:"+ordDTO.getNumeroOV());
			while (itercli.hasNext()){
				ClidirDTO clidir = (ClidirDTO) itercli.next();
				ClidiraDTO clidiraDTO = new ClidiraDTO();
				clidiraDTO.setRutCliente(ordDTO.getRutCliente());
				clidiraDTO.setDvCliente(ordDTO.getDvCliente());
				clidiraDTO.setCorrelativo(clidir.getCorrelativo());
				String comuna = clmcliDAO.recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna());
				System.out.println("Direccion:"+clidir.getDireccionCliente());
				String direccion = clidir.getDireccionCliente().substring(0, 29).trim()+" " + clidir.getDireccionCliente().substring(30, 34)+","+comuna;
				System.out.println("Direccion:"+direccion);
				if (clidir.getLatitud().equals("") && clidir.getLongitud().equals("")){
					final Geocoder geocoder = new Geocoder();
						
					   GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(direccion+","+ "Chile").setLanguage("es").getGeocoderRequest();
					   
					   GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
					    
					   Iterator iterRe = geocoderResponse.getResults().iterator();
					    while (iterRe.hasNext()){
					    	GeocoderResult resul = (GeocoderResult) iterRe.next();
					    	GeocoderGeometry geo = resul.getGeometry();
					    	LatLng loca = geo.getLocation();
					    	System.out.println("Latitud:"+loca.getLat());
					    	System.out.println("Longitud:"+loca.getLng());
					    	clidiraDTO.setLatitud(String.valueOf(loca.getLat()));
					    	clidiraDTO.setLongitud(String.valueOf(loca.getLng()));
					    	clidiraDTO.setObservacion("");
					    	clidirDAO.actualizaClidira(clidiraDTO);
					    }
				}
				
			}
			
		}
	}
	public CoordenadasDTO convierte(String par000)
    {
        CoordenadasDTO dto = null;
        String lang2 = null;
        Gson gson = new Gson();
        System.out.println((new StringBuilder("Param:")).append(par000).toString());
        try
        {
            JsonObject json = (JsonObject)(new JsonParser()).parse(par000);
            if(json.get("results") != null)
            {
                JsonArray lang = (JsonArray)json.get("results");
                if(lang != null)
                {
                    Iterator i = lang.iterator();
                    for(int contado = 0; i.hasNext(); contado++)
                    {
                        JsonObject json2 = (JsonObject)i.next();
                        if(json2.get("geometry") != null)
                            lang2 = json2.get("geometry").toString();
                        if(lang2 != null)
                        {
                            JsonObject json3 = (JsonObject)(new JsonParser()).parse(lang2);
                            if(json3.get("bounds") != null)
                            {
                                lang2 = json3.get("bounds").toString();
                                JsonObject json5 = (JsonObject)(new JsonParser()).parse(lang2);
                                if(json5.get("southwest") != null)
                                {
                                    lang2 = json5.get("southwest").toString();
                                    JsonObject json6 = (JsonObject)(new JsonParser()).parse(lang2);
                                    dto = new CoordenadasDTO();
                                    if(json6.get("lat") != null)
                                        dto.setLatitud(json6.get("lat").toString());
                                    if(json6.get("lng") != null)
                                        dto.setLongitud(json6.get("lng").toString());
                                }
                            } else
                            if(json3.get("location") != null)
                            {
                                lang2 = json3.get("location").toString();
                                JsonObject json6 = (JsonObject)(new JsonParser()).parse(lang2);
                                dto = new CoordenadasDTO();
                                if(json6.get("lat") != null)
                                    dto.setLatitud(json6.get("lat").toString());
                                if(json6.get("lng") != null)
                                    dto.setLongitud(json6.get("lng").toString());
                            }
                        }
                        System.out.println("Pruebas");
                    }

                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return dto;
    }
	public void procesaDireccion(){
		DAOFactory dao = DAOFactory.getInstance();
		ClidirDAO clidir = dao.getClidirDAO();
		int rut=0;
		String dv="";
		List lista = clidir.obtieneDirecciones(rut, dv);
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			ClidirDTO dto = (ClidirDTO) iter.next();
			ProcesaGeoreferenciaDireccion inicial = new ProcesaGeoreferenciaDireccion();
			inicial.georeferencia(dto.getRutCliente(), dto.getCorrelativo());
			
		}
	}
	
}
