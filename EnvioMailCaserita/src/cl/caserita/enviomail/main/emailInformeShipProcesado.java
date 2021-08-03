package cl.caserita.enviomail.main;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.caserita.dto.DocuNoGeneradoDTO;

public class emailInformeShipProcesado {

public void mail(String mensaje, List docu,  List Correo, String nombreArchivoXML){
		
		Properties props = new Properties();

		props.setProperty("mail.smtp.host", "216.72.23.8");
		//props.setProperty("mail.smtp.host", "mail.caserita.cl");

		// TLS si est� disponible
		props.setProperty("mail.smtp.starttls.enable", "false");

		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port","25");

		// Nombre del usuario
		props.setProperty("mail.smtp.user", "alertasistema@caserita.cl");

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		
	
		MimeMessage message = new MimeMessage(session);

		try{
			
			message.setFrom(new InternetAddress("alertasistema@caserita.cl"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("desarrollo@caserita.cl"));
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress("jramirez@caserita.cl"));

			
			message.setDescription("Desarrollo");

			message.setSubject("DOCUMENTOS PROCESADOS DESDE JDA - WMS AL SISTEMA SYSCON");
			String mensajeCorreo="Estimados, Para el archivo generado desde JDA  : "+nombreArchivoXML + " se han procesado los siguientes carguios : \n";
			String mensajeCuerpo="";
			//message.setText("Para el carguio numero : "+numeroCarguio + " no se han generado los siguientes documentos : ");
			Iterator iter = docu.iterator();
			while (iter.hasNext()){
				DocuNoGeneradoDTO dto = (DocuNoGeneradoDTO) iter.next();
				if (mensajeCuerpo.length()==0){
					mensajeCuerpo="\n"+"Numero Carguio :"+dto.getNumeroOrden() +"\n";

				}else{
					mensajeCuerpo=mensajeCuerpo+"\n"+"Numero Carguio :"+dto.getNumeroOrden() +"\n";

				}
				
				
			}
			message.setText(mensajeCorreo+mensajeCuerpo);
			Transport t = session.getTransport("smtp");
			t.connect("alertasistema@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		
		
		
		
	}
}
