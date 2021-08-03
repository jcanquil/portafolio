package cl.caserita.enviomail.main;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class envioMailCaserita {
	
public static void main (String[]args) {
		
		Properties props = new Properties();

		// Nombre del host de correo, es smtp.gmail.com
		props.setProperty("mail.smtp.host", "216.72.23.8");
		//props.setProperty("mail.smtp.host", "mail.caserita.cl");

		// TLS si est� disponible
		props.setProperty("mail.smtp.starttls.enable", "false");

		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port","25");

		// Nombre del usuario
		props.setProperty("mail.smtp.user", "jcanquil@caserita.cl");

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		
		MimeMessage message = new MimeMessage(session);
		
		try{
			message.setFrom(new InternetAddress("jcanquil@caserita.cl"));
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("jcanquil@caserita.cl"));
			message.setSubject("Hola");
			message.setText("Mensajito con Java Mail" +
			"de los buenos." +
			"poque si");
			Transport t = session.getTransport("smtp");
			t.connect("jcanquil@caserita.cl","angela23*");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		
		
		
		
	}
}
