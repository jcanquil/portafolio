package cl.caserita.wms.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cl.caserita.wms.out.helper.IntegracionAjusteHelper;
import cl.caserita.wms.out.helper.IntegracionConfirCamionHelper;
import cl.caserita.wms.out.helper.IntegracionConfirmaEnvioHelper;
import cl.caserita.wms.out.helper.IntegracionConfirmaRecepcionHelper;
import cl.caserita.wms.out.helper.IntegracionReconInventarioHelper;

public class IntegracionSalidasHelper {
	public static void main (String []args){
	System.out.println("Inicia Proceso");
	int i2=0;
	
	while (i2==0){
		String path = "/home/ftp/out/";
		File directorio = new File(path);
		String [] ficheros = directorio.list();
		String line;
		for (int i = 0; i < ficheros.length; i++) {
		
			System.out.println("archivo:"+path+ficheros[i]);
			if (ficheros[i].indexOf("INV_ADJ")!=-1 ){
				IntegracionAjusteHelper ajuste = new IntegracionAjusteHelper();
				ajuste.procesaAjuste(path+ficheros[i],ficheros[i]);

			}else if (ficheros[i].indexOf("camion")!=-1 || ficheros[i].indexOf("Camion")!=-1){
				IntegracionConfirCamionHelper confirma = new IntegracionConfirCamionHelper();
				//confirma.procesaConfirmaCamion(path+ficheros[i], ficheros[i]);
				
			}else if (ficheros[i].indexOf("Envio")!=-1 || ficheros[i].indexOf("envio")!=-1){
				IntegracionConfirmaEnvioHelper envio = new IntegracionConfirmaEnvioHelper();
				envio.procesaReconciliacion(path+ficheros[i], ficheros[i]);
				
			}else if (ficheros[i].indexOf("Recepc")!=-1 || ficheros[i].indexOf("recep")!=-1 || ficheros[i].indexOf("INV_RCV")!=-1 ){
				IntegracionConfirmaRecepcionHelper recepcion = new IntegracionConfirmaRecepcionHelper();
				recepcion.procesaReconciliacion(path+ficheros[i], ficheros[i]);
				
			}else if (ficheros[i].indexOf("Reconci")!=-1 || ficheros[i].indexOf("reconcili")!=-1){
				IntegracionReconInventarioHelper recon = new IntegracionReconInventarioHelper();
				recon.procesaReconciliacion(path+ficheros[i], ficheros[i]);
				
			}else if (ficheros[i].indexOf("SHIP_LOAD")!=-1 ){
				IntegracionConfirCamionHelper confirma = new IntegracionConfirCamionHelper();
				String tipo="CAMION";
				if (ficheros[i].indexOf("SHIP_LOAD")!=-1){
					tipo="CAMION";
				}else if (ficheros[i].indexOf("Merm")!=-1){
					tipo="MERMA";
				}
				//confirma.procesaConfirmaCamion(path+ficheros[i], ficheros[i], tipo);
				confirma.procesaConfirmaCamion2(path+ficheros[i], tipo, ficheros[i]);
				
			}else if (ficheros[i].indexOf("Inventario")!=-1 || ficheros[i].indexOf("inven")!=-1){
				IntegracionReconInventarioHelper recon = new IntegracionReconInventarioHelper();
				recon.procesaReconciliacion(path+ficheros[i], ficheros[i]);
			
			}else if (ficheros[i].indexOf("TRLR_CLO")!=-1){
				IntegracionConfirmaRecepcionHelper recepcion = new IntegracionConfirmaRecepcionHelper();
				recepcion.procesaReconciliacion(path+ficheros[i], ficheros[i]);
			}
		}
		
		
	}
	}
}
