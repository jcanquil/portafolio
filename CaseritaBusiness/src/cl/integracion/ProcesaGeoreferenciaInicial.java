package cl.caserita.integracion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.CoordenadasDTO;
import cl.caserita.procesos.helper.ProcesaLatitudLongitudHelper;

public class ProcesaGeoreferenciaInicial {

	public void procesaGeoRefenrecia(String direccion, String numero, String comuna, String ciudad, String pais, int rut, String dv, int correlativo){
		String texto="";
		DAOFactory factory = DAOFactory.getInstance();
		ClmcliDAO clmcli = factory.getClmcliDAO();
		String urlString = "https://maps.googleapis.com/maps/api/geocode/json?";
        String direccion2 = (new StringBuilder(String.valueOf(direccion.trim()))).append(" ").append(numero).append(",").append(comuna.trim()).append(",").append(ciudad.trim()).toString()+","+pais.trim();
        if(direccion != null && !"".equals(direccion) && !"".equals(numero) )
            try
            {
                URL url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
                System.out.println("URL:"+url.toString());
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
                  ClidirDTO clidir = new ClidirDTO();
                  clidir.setRutCliente(rut);
                  clidir.setDvCliente(dv);
                  clidir.setCorrelativo(correlativo);
                  clmcli.generaIntegracionGeoref(clidir);
                   
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
