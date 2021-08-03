package cl.caserita.enviomail.main;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;
import java.util.logging.ConsoleHandler;

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

import cl.caserita.comunes.properties.Constants;



public class envioMailCaseritaAdjunto {

	
	
	
	
		public boolean envioMail (String nombre, String mail, String mailCopia,String mensajeAsunto) {
		
		Properties props = new Properties();

		// Nombre del host de correo, es smtp.gmail.com
		props.setProperty("mail.smtp.host", "216.72.23.8");
		//props.setProperty("mail.smtp.host", "mail.caserita.cl");

		// TLS si est� disponible
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
			
			BodyPart texto = new MimeBodyPart();
			texto.setText(mensajeAsunto);
			
			BodyPart adjunto = new MimeBodyPart();
			
			adjunto.setDataHandler(new DataHandler(new FileDataSource(nombre)));
			String nombreCorreo = nombre.substring(33, nombre.length());
			System.out.println("Nombre Archivo:"+nombreCorreo);
			adjunto.setFileName(nombreCorreo);
			MimeMultipart multiParte = new MimeMultipart();

		multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);
			
			
			message.setFrom(new InternetAddress("informes@caserita.cl"));
			
			
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
			if (mailCopia!=null && mailCopia.length()>0){
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(mailCopia));
			}
			// Se rellena el subject
			message.setSubject(mensajeAsunto);

			// Se mete el texto y la foto adjunta.
			message.setContent(multiParte);
			
			Transport t = session.getTransport("smtp");
			t.connect("informes@caserita.cl","caserita2012");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		return true;
		
		
		
	}
}
