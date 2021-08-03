/*
 * Creado el 19-ago-10
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.caserita.comunes.properties;

import java.io.FileInputStream;
import java.util.Properties;


/**
 * @author fpinchei
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class PropertiesAccouting {

	private static boolean loaded=false;

	static {
		if(!loaded){
			try{
			
				Properties properties = new Properties();
				properties.load(new FileInputStream(Constants.FILE_PROPERTIES));
				
				//URL_AGENDA = properties.getProperty("URL_AGENDA");
			}catch(Exception e){
				e.printStackTrace();
			}
			loaded=true;				
		}
	}

}
