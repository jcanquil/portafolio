package cl.caserita.enviomail.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.caserita.dto.DocuNoGeneradoDTO;

public class envioMailDocuNOFacturados {

public void mail(String mensaje, List docu, int numeroCarguio, List Correo){
		
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
			
			message.setFrom(new InternetAddress("alertasistema@caserita.cl"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("desarrollo@caserita.cl"));
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress("jramirez@caserita.cl"));

			
			message.setDescription("Desarrollo");

			message.setSubject("ERROR GENERACION DOCUMENTOS EN CARGUIO PROCESADOS POR WMS");
			String mensajeCorreo="Para el carguio numero : "+numeroCarguio + " NO SE HAN GENERADO LOS SIGUIENTES DOCUMENTOS : \n";
			String mensajeCuerpo="";
			//message.setText("Para el carguio numero : "+numeroCarguio + " no se han generado los siguientes documentos : ");
			Iterator iter = docu.iterator();
			while (iter.hasNext()){
				DocuNoGeneradoDTO dto = (DocuNoGeneradoDTO) iter.next();
				if (mensajeCuerpo.length()==0){
					mensajeCuerpo="\n"+"Numero Orden :"+dto.getNumeroOrden() +"|" +"Numero Documento :"+dto.getNumeroDocumento() + "|"+" Rut Cliente :"+dto.getRutCliente()+"-"+dto.getDvCliente()+"|"+"Fecha Documento :"+dto.getFechaDocumento()+"|"+"Comentario :"+dto.getComentario().trim()+ "\n";

				}else{
					mensajeCuerpo=mensajeCuerpo+"\n"+"Numero Orden :"+dto.getNumeroOrden() +"|" +"Numero Documento :"+dto.getNumeroDocumento() + "|"+" Rut Cliente :"+dto.getRutCliente()+"-"+dto.getDvCliente()+"|"+"Fecha Documento :"+dto.getFechaDocumento()+"|"+"Comentario :"+dto.getComentario().trim()+"\n";

				}
				
				
			}
			message.setText(mensajeCorreo+mensajeCuerpo);
			Transport t = session.getTransport("smtp");
			t.connect("alertasistema@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		
		
		
		
	}
	public static void main (String []args ){
		envioMailDocuNOFacturados doc = new envioMailDocuNOFacturados();
		List lista = new ArrayList();
		DocuNoGeneradoDTO dto = new DocuNoGeneradoDTO();
		dto.setNumeroOrden(100);
		dto.setNumeroDocumento(170000001);
		dto.setFechaDocumento(20161220);
		dto.setRutCliente(15448543);
		dto.setDvCliente("0");
		lista.add(dto);
		DocuNoGeneradoDTO dto2 = new DocuNoGeneradoDTO();
		dto2.setNumeroOrden(101);
		dto2.setNumeroDocumento(170000002);
		dto2.setFechaDocumento(20161220);
		dto2.setRutCliente(17008056);
		dto2.setDvCliente("4");
		lista.add(dto2);
		DocuNoGeneradoDTO dto3 = new DocuNoGeneradoDTO();
		dto3.setNumeroOrden(102);
		dto3.setNumeroDocumento(170000003);
		dto3.setFechaDocumento(20161220);
		dto3.setRutCliente(20190189);
		dto3.setDvCliente("5");
		lista.add(dto3);
		int numeroCarguio=67909;
		doc.mail("", lista, numeroCarguio,lista);		
	}
}
