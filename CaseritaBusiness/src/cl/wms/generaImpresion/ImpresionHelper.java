package cl.caserita.wms.generaImpresion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;

public class ImpresionHelper {

	void imprimirFactura() throws PrintException{
		 
		// aca obtenemos la printer default  
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
		  
		String zplCommand = "^XA\n" +  
		"^FO10,0^ARN,11,7^FD SOME TEXT ^FS\n" +  
		"^FO300,0^ARN,11,7^FD SOME VALUE ^FS\n" +  
		"^FO10,35^ARN,11,7^FD SOME TEXT ^FS\n" +  
		"^FO300,35^ARN,11,7^FD SOME VALUE ^FS\n" +  
		"^FO10,70^ARN,11,7^FD SOME CODE ^FS\n" +  
		"^FO10,115^ARN,11,7^BCN,60,Y,Y,N^FD 23749237439827 ^FS\n" +  
		"^XZ";  
		  
		// convertimos el comando a bytes  
		byte[] by = zplCommand.getBytes();  
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;  
		Doc doc = new SimpleDoc(by, flavor, null);  
		  
		// creamos el printjob  
		DocPrintJob job = printService.createPrintJob();  
		  
		// imprimimos  
		job.print(doc, null);  
    }
	
	public static void main (String []args){
		ImpresionHelper helper = new ImpresionHelper();
		try{
			helper.imprimirFactura();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
