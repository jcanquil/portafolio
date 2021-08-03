package cl.caserita.wms.generaImpresion;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.DocGenelDAO;

public class Timbre {
    public static byte[] pngImageData;

	final static int CAPACIDAD_LIMITE  = 1000;
    public static void main(String[] args){
    	DAOFactory dao = DAOFactory.getInstance();
    	DocGenelDAO doc = dao.getDocGenelDAO();
    	String ted = doc.buscaEndPoint();
        Timbre t = new Timbre();
        byte [] datos = ted.getBytes();
        //byte [] uno = ted.getBytes();
        String ret = t.getBase64(datos);
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
		System.out.println(ret);
		DocFlavor flavor = DocFlavor.STRING.TEXT_PLAIN;
		Doc doc2 = new SimpleDoc(ret, flavor, null);  
		  
		// creamos el printjob  
		DocPrintJob job = printService.createPrintJob();  
		// imprimimos  
		try{
			job.print(doc2, null);  

		}catch(Exception e){
			e.printStackTrace();
		}
		
        
    }

   public byte[] GetPNG(){
       return pngImageData;
   }

   public String getBase64(byte[] datos){
       return Base64E.encode(datos);
   }
   
}
