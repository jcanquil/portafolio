package cl.caserita.enviomail.main;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnvioMailTransportistaInicio {
	
	public void mail(String tipo, String numeroCarg, String mensaje){
		
		
		Properties props = new Properties();

		props.setProperty("mail.smtp.host", "correo.upmchile.com");

		// TLS si esta disponible
		props.setProperty("mail.smtp.starttls.enable", "false");

		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port","25");

		// Nombre del usuario
		props.setProperty("mail.smtp.user", "informes@caserita.cl");

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		
		MimeMessage message = new MimeMessage(session);

		try{
			
			message.setFrom(new InternetAddress("informes@caserita.cl"));
			message.setSubject("CARGUIO "+numeroCarg+" ENVIADO AL TELEFONO");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("jaguilera@caserita.cl"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("sahumada@caserita.cl"));
			message.setText(tipo+"\r\n"+"CARGUIO: "+numeroCarg+"\r\n"+mensaje+"\r\n");
			
			Transport t = session.getTransport("smtp");
			t.connect("informes@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		
		
	}

		

}
