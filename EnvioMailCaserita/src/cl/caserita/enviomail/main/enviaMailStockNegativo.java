package cl.caserita.enviomail.main;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class enviaMailStockNegativo {

public void envioMail (String mensaje) {
		
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
		message.addRecipient(Message.RecipientType.TO, new InternetAddress("jcanquil@caserita.cl"));
		//message.addRecipient(Message.RecipientType.TO, new InternetAddress("jramirez@caserita.cl"));

		


		message.setSubject("***********SYSCON SISTEMA STOCK NEGATIVO INTEGRACION WMS************");
		message.setText("Se ha generado una actualizacion de stock negativo :"+mensaje);
		
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
