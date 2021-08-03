package cl.caserita.procesaXML;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.dto.ReferenciaDocumentoDTO;

public class LeeXmlEstadoSII {

	public int main(String argv) {
		String entrada;
		int estadoSII=0;
		String cadena="";
		try {
		
		System.out.println("XML a Procesar :"+argv);
		InputStream is = new ByteArrayInputStream(argv.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		//new BufferedReader(new InputStreamReader(url.openStream()));


			while ((entrada = br.readLine()) != null){
				cadena = cadena + entrada;
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource archivo = new InputSource();
			archivo.setCharacterStream(new StringReader(cadena)); 
			
			Document documento = db.parse(archivo);
			documento.getDocumentElement().normalize();
			
			//Recupera Datos IDOC
			NodeList nodeLista = documento.getElementsByTagName("Respuesta");
			//System.out.println("Informacion de los libros");

					for (int s = 0; s < nodeLista.getLength(); s++) {
				
					Node primerNodo = nodeLista.item(s);
					String estado="";
					
					if (primerNodo.getNodeType() == Node.ELEMENT_NODE) {
				
					Element primerElemento = (Element) primerNodo;
				
					NodeList primerNombreElementoLista =
				                        primerElemento.getElementsByTagName("Codigo");
					Element primerNombreElemento =
				                        (Element) primerNombreElementoLista.item(0);
					NodeList primerNombre = primerNombreElemento.getChildNodes();
					estado = ((Node) primerNombre.item(0)).getNodeValue().toString();
					System.out.println("Respuesta WS Estado:"+estado);
					//System.out.println("Titulo : "  + fecha);
					
					}
					/*if ("7".equals(estado) || "8".equals(estado)){
						estado="0";
					}else if("4".equals(estado)){
						estado="1";
						
					}else if("2".equals(estado)){
						estado="2";						
					}*/
					estadoSII = Integer.parseInt(estado);
					}
					


	  }
	  catch (Exception e) {
	    	e.printStackTrace();
	  }
	  return estadoSII;
	 }
}
