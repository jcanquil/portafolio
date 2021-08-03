package cl.caserita.rest.graba;

import java.io.File;
import java.io.FileWriter;

import cl.caserita.comunes.fecha.Fecha;

public class generaTxt {
	private static FileWriter fileWriterLog;
	
	public void generatxt(String XML, String res, String rut, String numDoc, int bodega, int codDocumento, String generacion){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//System.out.println("Mes:"+mes);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);

		//generacion= descripcionTD(codDocumento);
		String archivoLog="";
		
		String rutaLog="/home/ServiciosCaseritaWEB/log/";
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutaLog+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/";

		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		System.out.println("Ruta:"+carpeta);
		archivoLog=carpeta+codDocumento+"_"+rut+"_"+numDoc+"_"+fechaStr+".log";;
		File f=new File(archivoLog);
		if (f.exists()){
			System.out.println("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterLog=new FileWriter(archivoLog,true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		System.out.println("Archivo LOG:"+archivoLog);

		fileWriterLog.write( XML+","+ res+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
}
