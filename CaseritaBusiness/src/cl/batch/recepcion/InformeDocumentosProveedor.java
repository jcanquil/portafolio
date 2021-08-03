package cl.caserita.batch.recepcion;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import cl.caserita.company.user.wsclient.WsClient;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dto.DocumentoElectronicoDTO;
import cl.caserita.dto.InformeProveedorDTO;
import cl.caserita.dto.RecepDocumentoDTO;
import cl.caserita.procesaXML.LeerXMLDocumentos;

public class InformeDocumentosProveedor {
	private static Properties prop=null;
	private static String pathProperties;
	
	public List procesa(String datos, int rutEmpresa, String razonSocial, WsClient ws){
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		pathProperties = Constants.FILE_PROPERTIES;
		String endPoint=prop.getProperty("endpoint");
		
		List doc = new ArrayList();
		StringTokenizer st = new StringTokenizer(datos,"|");
		WsClient wsc = new WsClient();
		 while (st.hasMoreTokens( )){
			 
			 	String tr = st.nextToken();
			 	StringTokenizer stad = new StringTokenizer(tr,",");
			 	InformeProveedorDTO inf = new InformeProveedorDTO();
			 	int num=0;
			 	DocumentoElectronicoDTO doc2=null;
			 	while (stad.hasMoreTokens( )){
			 		//System.out.println("Token: " + stad.nextToken( ));
			 		if (num==0){
			 			inf.setRutProveedor(Integer.parseInt(stad.nextToken()));
			 		}else if (num==1){
			 			inf.setCodigoDocumento(Integer.parseInt(stad.nextToken()));
			 		}else if (num==2){
			 			String numeroDoc = stad.nextToken();
			 			//int nume = Integer.parseInt(numeroDoc);
			 			numeroDoc = numeroDoc.replaceAll("\n", "");
			 			//System.out.println("Numero" + numeroDoc);
			 			inf.setNumeroDocumento(Integer.parseInt(numeroDoc));
			 		}
			 	
			 		
					
					
					
			 		num++;
			 	}
			 	
				if (inf.getNumeroDocumento()>0){
		 			
			 		try{
			 			System.out.println("Rut Proveedor:"+inf.getRutProveedor());
			 			System.out.println("Codigo Documento:"+inf.getCodigoDocumento());
			 			System.out.println("Numero Documento:"+inf.getNumeroDocumento());
			 			System.out.println("Rut Caserita:"+rutEmpresa);
			 			String xml = wsc.onlineRecoveryRec(inf.getRutProveedor(), inf.getCodigoDocumento(), inf.getNumeroDocumento(),rutEmpresa,endPoint);
			 			//String url = ws.onlineRecoveryRecUrl(inf.getRutProveedor(), inf.getCodigoDocumento(), inf.getNumeroDocumento(),rutEmpresa);
			 			System.out.println("Ruta XML a PRocesar:"+xml);
			 			LeerXMLDocumentos leer = new LeerXMLDocumentos();
						doc2 = leer.main(xml);
						
						
			 		}catch (Exception e){
			 			e.printStackTrace();
			 		}
		 		}
				inf.setRazonSocialProveedor(doc2.getRazonSocialProveedor());
				inf.setFechadocumento(doc2.getFecha());
		 		inf.setRutEmpresa(rutEmpresa);
		 		inf.setRazonSocial(razonSocial);
			 	doc.add(inf);
			 
	        
	    }
		return doc;
		
	}
}
