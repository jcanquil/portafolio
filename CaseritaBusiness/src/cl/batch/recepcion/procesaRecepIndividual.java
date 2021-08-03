package cl.caserita.batch.recepcion;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.helper.procesaFacturaProveedorHelper;

public class procesaRecepIndividual {
	
	public static void main (String[]args){
		int rut=0;
		int codDoc=0;
		int numDoc=0;
		String respuesta="";
		int accion=0;
		int empresa=0;
		
		for(int i=0;i<args.length;i++)
		{
			if (i==0){
				//Codigo Movimiento
				rut=Integer.parseInt(args[i]);
			}else if(i==1){
				//Fecha Movimiento
				codDoc=Integer.parseInt(args[i]);
			}else if(i==2){
				//NUmero Documento
				numDoc=Integer.parseInt(args[i]);
			}else if(i==3){
				//Codigo Documento 3 o 4
				respuesta=args[i];
			}else if(i==4){
				//Rut Cliente
				accion=Integer.parseInt(args[i]);
			}else if(i==5){
				//Rut Cliente
				empresa=Integer.parseInt(args[i]);
			}
		}	
		procesaFacturaProveedorHelper helper = new procesaFacturaProveedorHelper();
		helper.apruebaRechazaDoc(rut, codDoc, numDoc, respuesta, accion, empresa);
		
		
	}

}
