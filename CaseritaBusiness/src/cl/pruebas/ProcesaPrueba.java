package cl.caserita.pruebas;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExmartDTO;

import java.text.Normalizer;
import java.text.Normalizer;

public class ProcesaPrueba {

	public void procesa(){
		DAOFactory dao = DAOFactory.getInstance();
		ExmartDAO exmart = dao.getExmartDAO();
		ClmcliDAO clmcli = dao.getClmcliDAO();
		ExmartDTO exmartDTO = exmart.recuperaArticuloSinDigito(5405);
		
		String cadena = exmartDTO.getDescripcionArticulo();
        System.out.println(cadena + " = " + cleanString(cadena));
		exmartDTO = exmart.recuperaArticuloSinDigito(13022);

        cadena =  exmartDTO.getDescripcionArticulo();
        System.out.println(cadena + " = " + cleanString(cadena)); 
        
        ClmcliDTO dto = clmcli.recuperaCliente(11774870, "7");
        cadena =dto.getRazonsocial();
        System.out.println(cadena + " = " + cleanString(cadena));
        
        exmartDTO = exmart.recuperaArticuloSinDigito(5249);

        cadena =  exmartDTO.getDescripcionArticulo();
        System.out.println(cadena + " = " + cleanString(cadena)); 
        exmartDTO = exmart.recuperaArticuloSinDigito(16896);

        cadena =  exmartDTO.getDescripcionArticulo();
        System.out.println(cadena + " = " + cleanString(cadena)); 
        exmartDTO = exmart.recuperaArticuloSinDigito(367);

        cadena =  exmartDTO.getDescripcionArticulo();
        System.out.println(cadena + " = " + cleanString(cadena)); 
	}
	
	public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        texto = texto.replace("ñ", "N");
        texto = texto.replace("ñ", "N");
        texto = texto.replace("ñ", "");
        texto = texto.replace("<", "&lt;");
        texto = texto.replace(">", "&gt;");
        texto = texto.replace("&", "&amp;");
        texto = texto.replace("\"", "&quot;");
        texto = texto.replace("'", "&apos;");
        texto = texto.replace("ñ", "A");
        texto = texto.replace("ñ", "E");
        texto = texto.replace("ñ", "I");
        texto = texto.replace("ñ", "O");
        texto = texto.replace("ñ", "U");
        texto = texto.replace("ñ", "a");
        texto = texto.replace("ñ", "e");
        texto = texto.replace("ñ", "i");
        texto = texto.replace("ñ", "o");
        texto = texto.replace("ñ", "u");
        texto = texto.replace("Nñ", "N");
        texto = texto.replace("Gñ", "G");
        texto = texto.replace("ñ", "N");
        texto = texto.replace("ñ", "N");
        texto = texto.replace("ñ", "");
        texto = texto.replace("ñ", "");
        texto = texto.replace("ñ", "");
        texto = texto.replace("ñ", "");
        texto = texto.replace("Ñ", "N");
        texto = texto.replace("Ñ", "N");
        texto = texto.replace("º", "");
        texto = texto.replace("Nº", "");
        texto = texto.replace("´", "");
        texto = texto.replace("?", "");
        texto = texto.replace("N?", "N");
        texto = texto.replace("N°", "N");
        texto = texto.replace("Ã", "");
        texto = texto.replace("Ð", "D");
         texto = texto.replaceAll("[^\\p{ASCII}]", "");
        

      	 

      
        return texto;
    }
	public static String cleanString2(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{ASCII}]", "");
        return texto;
    }
	
	public static void main (String []args){
		/*ProcesaPrueba pro = new ProcesaPrueba();
		pro.procesa();*/
		
		String hola ="RV SIGUIENTE MENSAJE";
		int num = hola.indexOf("RV");
		if (hola.indexOf("RV")!=-1){
			System.out.println("CONTIENE RV");

		}else{
			System.out.println("NO CONTIENE RV");

		}
		
		
		
	}
	
}
