package cl.caserita.enviomail.main;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.caserita.comunes.properties.Constants;
import cl.caserita.dto.EmaapmDTO;

public class envioMailAvisosVarios {

	
	private static Properties prop=null;
	private static String pathProperties;
	
public boolean envioMail (String mail, String cuerpo, String mail2, String mail3,List email) {
		
		Properties props = new Properties();
		
		// Nombre del host de correo, es smtp.gmail.com
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
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		pathProperties = Constants.FILE_PROPERTIES;
		String ruta = prop.getProperty("archivos.salidalventas.path");
		
		
		try{
			
			message.setFrom(new InternetAddress("alertasistema@caserita.cl"));
			Iterator iter = email.iterator();
			int contador=0;
			while (iter.hasNext()){
				EmaapmDTO emailDTO = (EmaapmDTO) iter.next();
				if (contador==0){
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDTO.getEmail()));

				}else{
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailDTO.getEmail()));

				}
				contador++;
			}
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail2));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail3));

			//message.addRecipient(Message.RecipientType.TO, new InternetAddress("jcanquil@caserita.cl"));
			message.setSubject("Proceso Actualizacion Manual de Gestion SYSCON");
			message.setText(cuerpo);
			Transport t = session.getTransport("smtp");
			t.connect("informes@caserita.cl","caserita2012");
			//t.connect("jcanquil@caserita.cl","angela23*");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			

			
			
			
			
			
		
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		return true;
		
		
		
	}

}
