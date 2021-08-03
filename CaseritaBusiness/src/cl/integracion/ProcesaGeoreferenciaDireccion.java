package cl.caserita.integracion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.CoordenadasDTO;
import cl.caserita.procesos.helper.ProcesaLatitudLongitudHelper;

public class ProcesaGeoreferenciaDireccion {

	public void georeferencia(int rut, int correlativo){
		DAOFactory dao = DAOFactory.getInstance();
		ClidirDAO clidir = dao.getClidirDAO();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		String texto="";
		ClidirDTO dto = clidir.obtieneDireccionCliente(rut, correlativo);
		if (dto!=null){
			String direccion = dto.getDireccionCliente();
            String numero = String.valueOf(dto.getNumeroDireccion());
            String comuna = clmcliDAO.recuperaComuna(dto.getRegion(), dto.getCiudad(), dto.getComuna());
            String ciudad = clmcliDAO.recuperaCiudad(dto.getRegion(), dto.getCiudad());
            String region = clmcliDAO.recuperaRegion(dto.getRegion());
            String pais ="Chile";
            System.out.println((new StringBuilder("Direccion:")).append(direccion).toString());
            System.out.println((new StringBuilder("Numero:")).append(numero).toString());
            System.out.println((new StringBuilder("Comuna:")).append(comuna).toString());
            String urlString = "https://maps.googleapis.com/maps/api/geocode/json?";
            String direccion2 = (new StringBuilder(String.valueOf(direccion.trim()))).append(" ").append(numero).append(",").append(comuna.trim()).append(",").append(ciudad.trim()).toString()+","+pais.trim();
            if(dto != null && !"".equals(direccion2) && !"".equals(numero) )
                try
                {
                    URL url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
                    System.out.println("URL:"+url.toString());
                 /*   System.setProperty ( "javax.net.ssl.trustStore", "/home/server.crt"); 
                    System.setProperty ( "javax.net.ssl.trustStorePassword", "lastra657"); */
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                    String str = "";
                    StringBuffer tmp = new StringBuffer();
                    while((str = in.readLine()) != null) 
                        tmp.append(str);
                    in.close();
                    System.out.println((new StringBuilder("String:")).append(tmp.toString()).toString());
                    texto = tmp.toString();
                    ProcesaLatitudLongitudHelper lon = new ProcesaLatitudLongitudHelper();
                    CoordenadasDTO coordenada = lon.convierte(texto);
                    if(coordenada != null)
                    {
                       ClidiraDTO clidira = new ClidiraDTO();
                       clidira.setRutCliente(rut);
                       clidira.setCorrelativo(correlativo);
                       clidira.setLatitud(coordenada.getLatitud());
                       clidira.setLongitud(coordenada.getLongitud());
                       clidir.actualizaClidiraLatLng(clidira);
                       
                    }else{
                         direccion2 = (new StringBuilder(String.valueOf(comuna.trim()))).append(",").append(region.trim()).toString()+","+pais.trim();
                          url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
                         System.out.println("URL:"+url.toString());
                      /*   System.setProperty ( "javax.net.ssl.trustStore", "/home/server.crt"); 
                         System.setProperty ( "javax.net.ssl.trustStorePassword", "lastra657"); */
                          in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                          str = "";
                          tmp = new StringBuffer();
                         while((str = in.readLine()) != null) 
                             tmp.append(str);
                         in.close();
                         System.out.println((new StringBuilder("String:")).append(tmp.toString()).toString());
                         texto = tmp.toString();
                          lon = new ProcesaLatitudLongitudHelper();
                          coordenada = lon.convierte(texto);
                         if(coordenada != null)
                         {
                            ClidiraDTO clidira = new ClidiraDTO();
                            clidira.setRutCliente(rut);
                            clidira.setCorrelativo(correlativo);
                            clidira.setLatitud(coordenada.getLatitud());
                            clidira.setLongitud(coordenada.getLongitud());
                            clidir.actualizaClidiraLatLng(clidira);
                         }
                    }
                }
                catch(MalformedURLException e)
                {
                    texto = "<h2>No esta correcta la URL</h2>".toString();
                    e.printStackTrace();
                }
                catch(IOException e)
                {
                	e.printStackTrace();
                    texto = "<h2>Error: No se encontro el l pagina solicitada".toString();
                }
            	catch(Exception e){
            		e.printStackTrace();
            	}
        }

		
	}
	public static void main (String []args){
		ProcesaGeoreferenciaDireccion geo = new ProcesaGeoreferenciaDireccion();
		geo.georeferencia(11374603, 100);
	}
}
