package cl.caserita.enviomail.main;

import java.util.Iterator;
import java.util.List;
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

import cl.caserita.dto.RutaDocumentosDTO;

public class EnvioMailDocumentoPDFXML {

public boolean envioMail (String nombre, String mail, String mailCopia,String mensajeAsunto, String adj2, List ruta) {
		
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
			texto.setText("Adjunto encontrara los "+mensajeAsunto);
			
			//BodyPart adjunto = new MimeBodyPart();
			
			//adjunto.setDataHandler(new DataHandler(new FileDataSource(nombre)));
			
			//String nombreCorreo = nombre;
			//System.out.println("Nombre Archivo:"+nombreCorreo);
			
			//adjunto.setFileName(nombreCorreo.substring(16, nombreCorreo.length()));
			
			MimeMultipart multiParte = new MimeMultipart();
			
			//Otro Adjunto
			//BodyPart adjunto2 = new MimeBodyPart();
			//String nombreCorreo2 = adj2;
			//adjunto2.setDataHandler(new DataHandler(new FileDataSource(adj2)));
			//adjunto2.setFileName(adj2);
			//adjunto2.setFileName(nombreCorreo2.substring(16, nombreCorreo2.length()));
			
		    multiParte.addBodyPart(texto);
			//multiParte.addBodyPart(adjunto);
			
			//multiParte.addBodyPart(adjunto2);
			
			//Procesa Arreglo de Adjuntos
			Iterator iter = ruta.iterator();
			while (iter.hasNext()){
				RutaDocumentosDTO rutaObjeto = (RutaDocumentosDTO) iter.next();
				BodyPart todos = new MimeBodyPart();
				String correo = rutaObjeto.getRutaObjeto();
				todos.setDataHandler(new DataHandler(new FileDataSource(rutaObjeto.getRutaObjeto())));
				todos.setFileName(correo.substring(16, correo.length()));
				multiParte.addBodyPart(todos);
				
			}
			
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
			t.connect("informes@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		return true;
}
public static void main (String[]args){
	String desc ="http://192.168.1.4:8080/Facturacion/PDFServlet?docId=338X/DI3Cu/dxchIM(MaS)hjJA==";
	desc = desc.substring(17, 	18);
	System.out.println("Ruta:"+desc);
}
}
