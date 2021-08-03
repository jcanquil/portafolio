package cl.caserita.enviomail.main;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class emailInformacionWMS {

public void mail(String mensaje){
		
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

			message.addRecipient(Message.RecipientType.CC, new InternetAddress("informaticacaserita@gmail.com"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("dmancilla@caserita.cl"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("cfranceschi@caserita.cl"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("mcofre@caserita.cl"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("czuniga@caserita.cl"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("jf_bodega26@caserita.cl"));

			message.addRecipient(Message.RecipientType.CC, new InternetAddress("jcanquil@gmail.com"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("jramvale@gmail.com"));


			message.setSubject("***********SYSCON SISTEMA ERROR, COMUNICARSE INFORMATICA CASERITA************");
			message.setText("Se ha producido un error en el proceso de Confirmaciones desde WMS a SYSCON por lo cual no se procesaran ajustes, facturaciones, traspasos, Revisar proceso automatico o Comunicarse con informatica de Caserita :"+mensaje);
			
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
