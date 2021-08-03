package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.PrmprvDAO;

public class calculos {

	public static void main (String []args){
	
	try{
		URL url=
		    new URL("http://192.168.1.22:8080/ServiciosCaserita/xml.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String entrada;
		String cadena="";

		while ((entrada = br.readLine()) != null){
			cadena = cadena + entrada;
		}
		System.out.println("XML:"+cadena);
	}catch(Exception e){
		e.printStackTrace();
	}
		
	}
}
