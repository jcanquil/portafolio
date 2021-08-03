package cl.caserita.read.mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class prueba {

	public static void main (String [] args) {
		// Comprobar que el número de argumentos es el correcto
		if (args.length != 2) {
		System.out.println("Ha de enviar dos parámetros\n" +"java EnviarCorreo fromAddress toAddress");
		System.exit(1); }
		// Obtener el from y el to recibidos como parámetros
		String from = args [0];
		String to = args [1];
		// Obtener las propiedades del sistema y establecer el servidor // SMTP que vamos a usar
		String smtpHost = "smtp.ono.com"; 
		Properties props = System.getProperties(); 
		props.put("mail.smtp.host",smtpHost);
		// Obtener una sesión con las propiedades anteriormente definidas
		Session sesion = Session.getDefaultInstance(props,null);
		// Capturar las excepciones
		try {
		Message mensaje = new MimeMessage(sesion);
		// Rellenar los atributos y el contenido // Asunto
		mensaje.setSubject("Mensaje Google Completo"); // Emisor del mensaje
		mensaje.setFrom(new InternetAddress(from)); // Receptor del mensaje
		mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		// Crear un Multipart de tipo multipart/related
		Multipart multipart = new MimeMultipart ("related"); // Leer el fichero HTML
		String fichero = "";
		String linea;
		BufferedReader br = new BufferedReader (
		new FileReader ("c:/Temp/google.htm")); while ((linea = br.readLine()) != null)
		fichero += linea; br.close();
		// Rellenar el MimeBodyPart con el fichero e indicar que es un fichero HTML
		BodyPart texto = new MimeBodyPart (); texto.setContent(fichero,"text/html"); multipart.addBodyPart(texto);
		// Procesar la imagen
		MimeBodyPart imagen = new MimeBodyPart(); imagen.attachFile("c:/Temp/Google_archivos/logo.gif"); imagen.setHeader("Content-ID","<figura1>"); multipart.addBodyPart(imagen); mensaje.setContent(multipart);
		// Enviar el mensaje
		Transport.send(mensaje);
		} catch (MessagingException e) {
		System.err.println(e.getMessage()); } catch (IOException ioe) {
		System.err.println(ioe.toString()); }
	} 
	
}
