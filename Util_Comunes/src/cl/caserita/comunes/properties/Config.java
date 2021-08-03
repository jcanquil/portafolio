package cl.caserita.comunes.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
/**
 * 
 * <br>Objetivo de la Clase:
 * <br>----------------------
 * Permite tomar properties y almacenar key-value en MAP
 *
 * <br>Como se usa esta Clase:
 * <br>------------------------
 * 
 * 
 * <br>Datos de creación de la clase:
 * <br>-------------------------------
 * 
 * @author Carlos Mallea Lorca, Indra Sistemas Chile
 * @since Apr 2, 2009 
 * 
 * <B>Todos los derechos reservados para Telefónica Chile.</B>
 * <P>
 */
public class Config {
	
    private Hashtable configHashtable;

    public Config( String fName ) throws IOException {
     
        try {
        	
			Properties properties = new Properties();

			properties.load(new FileInputStream(fName));

			configHashtable = new Hashtable(properties);
			
            
        }catch( IOException e ) {
            throw e;
        }

    }

    
	public Config() {
		
		configHashtable = new Hashtable();

	}

    public String getValue( String key ) {
			
		String temp = "";
		
		if (configHashtable.containsKey(key)) {

			temp = (String) configHashtable.get(key);

			if (temp == null)
				temp = "";
					
				
			}
			
		return temp;
			
	}

	public int getIntValue(String key)
	{
		try
		{
			return Integer.parseInt((String)configHashtable.get(key));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	public String toString() {
		if ( configHashtable!=null )
			return configHashtable.toString();
		return null;
	}
}
