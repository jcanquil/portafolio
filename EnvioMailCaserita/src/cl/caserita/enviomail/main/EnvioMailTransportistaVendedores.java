package cl.caserita.enviomail.main;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CasesmtpDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dto.CasesmtpDTO;
import cl.caserita.dto.RespquadDTO;
import cl.caserita.dto.RutservDTO;


public class EnvioMailTransportistaVendedores {

public void mail(String mensaje, int codigoVendedor, String nombreVendedor, String correoVendedor, int numCarguio){
		
	String encab="";
	//String encab2="";
	
		String items="";
	
		encab="Estimado(a),"+"\r\n"+"Estos son sus despachos :\r\n\r\n";
		
		//encab="Estimado(a)";
		//encab2="Estos son sus despachos :";
				
		items=mensaje+"\r\n"+
		"Saludos.";
		
		Properties props = new Properties();

		props.setProperty("mail.smtp.host", "correo.upmchile.com");   //216.72.23.8
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
			if (("".equals(correoVendedor.trim())) || ("desarrollo@caserita.cl".equals(correoVendedor.trim()))){
				message.setSubject("VENDEDOR SIN CORREO  :  "+correoVendedor);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("fpellicciari@caserita.cl"));
			} else {
				message.setSubject("DESPACHO DE CARGUIO : "+numCarguio);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoVendedor));
			}
				
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("jaguilera@caserita.cl")); //jaguilera
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("sahumada@caserita.cl"));  //sahumada
			
			message.setText(encab+items);
			//message.setText(encab+"<br>"+encab2+"<br>"+"<br>"+"<b>"+titulo1+"</b>"+"<br>"+"<b>"+titulo2+"</b>"+"<br>"+subtitulo+"<br>"+"<i>Saludos.</i>." + " ","ISO-8859-1","html");
			
			Transport t = session.getTransport("smtp");
			t.connect("informes@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			
			System.out.println("_________________________________________________________________________________________________________________");
			System.out.println("_________________________________________________________________________________________________________________");
			System.out.println("_________________________________________________________________________________________________________________");
			System.out.println("\r\n");
			System.out.println("A T E N C I O N !!!!     C O R R E O     D E     V E N D E D O R     N O    E X I S T E   :   "+correoVendedor);
			System.out.println("\r\n");
			System.out.println("CODIGO VENDEDOR\t"+": "+codigoVendedor+"\r\n"+"NOMBRE VENDEDOR\t"+": "+nombreVendedor+"\r\n"+"CARGUIO\t\t"+": "+numCarguio);
			System.out.println("_________________________________________________________________________________________________________________");
			System.out.println("_________________________________________________________________________________________________________________");
			System.out.println("_________________________________________________________________________________________________________________");
			
			
		}
	
				
	}


	public String FormatearRUT(String rut) {
		 
	    int cont = 0;
	    String format;
	    rut = rut.replace(".", "");
	    rut = rut.replace("-", "");
	    format = "-" + rut.substring(rut.length() - 1);
	    for (int i = rut.length() - 2; i >= 0; i--) {
	        format = rut.substring(i, i + 1) + format;
	        cont++;
	        if (cont == 3 && i != 0) {
	            format = "." + format;
	            cont = 0;
	        }
	    }
	    return format;
	}



}



