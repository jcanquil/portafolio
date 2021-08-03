package cl.caserita.transportista.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CarguiadDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dto.CarguiadDTO;
import cl.caserita.dto.RutservDTO;

public class IntegracionNocturnaTransportistaHelper {

	public static void main (String []args){
		IntegracionNocturnaTransportistaHelper helper = new IntegracionNocturnaTransportistaHelper();
		helper.procesoNocturnoTransportista();
		
	}
	public String procesoNocturnoTransportista(){
		String retorno="";
		DAOFactory dao = DAOFactory.getInstance();
		CarguiadDAO carguiad = dao.getCarguiadDAO();
		RutservDAO rutserv = dao.getRutServDAO();

		List lista = carguiad.recuperaCarguioTransporte(2, 26, "D");
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			CarguiadDTO dto = (CarguiadDTO) iter.next();
			String solicitud="6";
			
			String par000="{\"rutChofer\":\""+dto.getRutChofer()+"\",\"dvChofer\":\""+dto.getDvChofer()+"\",\"solicitud\":\""+solicitud+"\"}";
			IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
			String json = helper.procesa(par000);
			RutservDTO dtoRut = rutserv.recuperaEndPointServlet("DATAMATRED");
			System.out.println("antes del getendpoint");
			String ws=dtoRut.getEndPoint().trim();
			System.out.println("despues del getendpoint");
			
			//String ws="http://192.168.1.26:8080/ServicioIntegracionWMS/servicioIntegraRest/wsDataMatrix/post";
			System.out.println("ws: "+ws);
			
			try {
				 
				URL url = new URL(ws.trim());
				//URL url = new URL("http://192.168.1.30:8080/ServiciosTransportistaWEB/servicioTransRest/wmsCaserita/post");
				System.out.println("url: "+url);
				//System.setProperty("javax.net.ssl.trustStore", "/home/store/truststore.ts");
				//System.setProperty("javax.net.ssl.trustStorePassword", "desarrollo");
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				System.out.println("antes del outputstream");
				OutputStream os = conn.getOutputStream();
				System.out.println("despues del outputstream");
				os.write(json.getBytes());
				os.flush();
				
		 		if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}
		 
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
		 
				String output;
				String respDTM="";
				System.out.println("Output from Server .... \n");
				
				while ((output = br.readLine()) != null) {
					respDTM=respDTM+output +"\r\n";
					System.out.println("ss:"+output);
				}
					 
				conn.disconnect();
				
				
			  } catch (MalformedURLException e) {
		 
				e.printStackTrace();
		 
			  } catch (IOException e) {
		 
				e.printStackTrace();
		 
			  }
		}
		
		
		return retorno;
	}
}
