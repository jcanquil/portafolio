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

public class emailCanastaWEB {

	public void mail(String numeroOV, String rut, String razonSocial, String fechaDespacho){
		
		Properties props = new Properties();

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
			
			message.setFrom(new InternetAddress("informes@caserita.cl"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("canastas@caserita.cl"));

			message.addRecipient(Message.RecipientType.CC, new InternetAddress("desarrollo@caserita.cl"));

			message.setSubject("Venta Canasta WEB");
			message.setText("Se ha generado una venta de canasta por la plataforma WEB " +
			"numero de orden de Venta :" +numeroOV + " Cliente : "+ rut + " " + razonSocial + " Fecha Despacho :"+fechaDespacho);
			
			Transport t = session.getTransport("smtp");
			t.connect("informes@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		
		
		
		
	}
	
	public static void main (String []args){
		emailCanastaWEB envio = new emailCanastaWEB();
		envio.mail("77000", "16333326-0", "Sebastian Ahumada", "20160909");
	}
}
