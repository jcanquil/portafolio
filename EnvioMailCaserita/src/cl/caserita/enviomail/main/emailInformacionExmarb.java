package cl.caserita.enviomail.main;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.caserita.dto.EmaapmDTO;

public class emailInformacionExmarb {

public void mail(String codigoArticulo,   String bodega, String accion, List email){
		
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



			message.setSubject("Eliminacion Articulo EXMARB");
			message.setText("Estimados, Se ha eliminado el siguiente articulo de la tabla EXMARB\n\n " +
			"Bodega : "+bodega+"\n"+"Codigo Articulo :" +codigoArticulo +"\n"+ "Accion :"+accion);
			
			Transport t = session.getTransport("smtp");
			t.connect("alertasistema@caserita.cl","Lastra657");
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
