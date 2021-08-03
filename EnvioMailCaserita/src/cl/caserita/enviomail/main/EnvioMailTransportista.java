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


public class EnvioMailTransportista {

public void mail(String mensaje, int codigoVendedor, String nombreVendedor, String correoVendedor, int tipoerror, int numOV, int fecOV, int horOV, int fecEnt, int horEnt, String numDoc, String tipoDoc, int fecDoc, int rutCli, String digCli, String nomCli, String dirCli, String comuCli, int monDoc, int monEnt, String nomChof, String estEnt, String estMot, int numNCp, int monNCp, int numCargui){
		
		String horaOVE="";
		String horaEntr="";
		String hori="";
		String minu="";
		String segu="";
		
		String fechaPE="";
		String fechaEN="";
		String fechaDO="";
		String ano ="";
		String mes="";
		String dia="";
		
		fechaPE=Integer.toString(fecOV);
		fechaEN=Integer.toString(fecEnt);
		fechaDO=Integer.toString(fecDoc);
		
		ano = fechaPE.substring(0, 4);
		mes = fechaPE.substring(4, 6);
		dia = fechaPE.substring(6, 8);
		String fecPEDIDO = dia+"/"+mes+"/"+ano;
		
		ano = fechaEN.substring(0, 4);
		mes = fechaEN.substring(4, 6);
		dia = fechaEN.substring(6, 8);
		String fecENTREG = dia+"/"+mes+"/"+ano;
		
		ano = fechaDO.substring(0, 4);
		mes = fechaDO.substring(4, 6);
		dia = fechaDO.substring(6, 8);
		String fecDOCUME = dia+"/"+mes+"/"+ano;
		
		if (horOV>0){
			horaOVE=Integer.toString(horOV);
			if (horaOVE.length()==5){
				hori = "0"+horaOVE.substring(0, 1);
				minu = horaOVE.substring(1, 3);
				segu = horaOVE.substring(3, 5);
				horaOVE = hori+":"+minu+":"+segu;
			} else {
				hori = horaOVE.substring(0, 2);
				minu = horaOVE.substring(2, 4);
				segu = horaOVE.substring(4, 6);
				horaOVE = hori+":"+minu+":"+segu;
			}
		}
		
		if (horEnt>0){
			horaEntr=Integer.toString(horEnt);
			if (horaEntr.length()==5){
				hori = "0"+horaEntr.substring(0, 1);
				minu = horaEntr.substring(1, 3);
				segu = horaEntr.substring(3, 5);
				horaEntr = hori+":"+minu+":"+segu;
			} else {
				hori = horaEntr.substring(0, 2);
				minu = horaEntr.substring(2, 4);
				segu = horaEntr.substring(4, 6);
				horaEntr = hori+":"+minu+":"+segu;
			}
		}
		
		
		String direCci=dirCli;
		direCci=direCci.substring(0,30).trim();
		String direNum=dirCli.substring(30);
		
		
		String encab="";
		String items="";
		String itemNCp="";
		
		String rutClientito=FormatearRUT(Integer.toString(rutCli)+digCli);
		
		if (numNCp>0){
			itemNCp="N° NCp\t\t"+": "+numNCp+"\r\n"+"MONTO NCp\t\t"+": "+monNCp+"\r\n";
		}
		
		
		encab="Estimado(a)\r\n"+"Su Pedido ha sido gestionado, los datos de la entrega son:\r\n\r\n";
		
		items="N° CARGUIO\t\t"+": "+numCargui+"\r\n"+"N° OV\t\t\t"+": "+numOV+"\r\n"+
		"FECHA PEDIDO\t"+": "+fecPEDIDO+"  HORA: "+horaOVE+"\r\n"+
		"FECHA ENTREGA\t"+": "+fecENTREG+"  HORA: "+horaEntr+"\r\n"+
		"N° DOCUMENTO\t"+": "+numDoc+"\r\n"+
		"TIPO DOCUMENTO\t"+": "+tipoDoc.trim()+"\r\n"+
		"FECHA DOCUMENTO\t"+": "+fecDOCUME+"\r\n"+
		"RUT CLIENTE\t\t"+": "+rutClientito+"\r\n"+
		"CLIENTE\t\t"+": "+nomCli+"\r\n"+
		"DIRECCION\t\t"+": "+direCci+"    # "+direNum+"\r\n"+
		"COMUNA\t\t"+": "+comuCli+"\r\n"+
		"MONTO DOCUMENTO\t"+": "+monDoc+"\r\n"+
		"MONTO ENTREGADO\t"+": "+monEnt+"\r\n"+
		"CHOFER\t\t"+": "+nomChof+"\r\n"+
		"ESTADO ENTREGA\t"+": "+estEnt+"\r\n"+
		"MOTIVO\t\t"+": "+estMot+"\r\n"+itemNCp+"\r\n"+
		"Saludos.";
		
		/*
		DAOFactory dao = DAOFactory.getInstance();
		CasesmtpDAO casesmtp = dao.getCasesmtpDAO();
		CasesmtpDTO dto = casesmtp.obtieneCorreos("11");
		*/
		
		
		Properties props = new Properties();

		props.setProperty("mail.smtp.host", "correo.upmchile.com");   //216.72.23.8
		//props.setProperty("mail.smtp.host", "mail.caserita.cl");

		// TLS si estï¿½ disponible
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
				message.setSubject("GESTION DE PEDIDO · "+numDoc+"  ¡¡VENDEDOR SIN CORREO!!");
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("fpellicciari@caserita.cl"));
			} else {
			
				message.setSubject("GESTION DE PEDIDO · "+numDoc);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoVendedor));
			}
				
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("jaguilera@caserita.cl")); //jaguilera
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("sahumada@caserita.cl"));  //sahumada
			
			message.setText(encab+items);
			
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
			System.out.println("CODIGO VENDEDOR\t"+": "+codigoVendedor+"\r\n"+"NOMBRE VENDEDOR\t"+": "+nombreVendedor+"\r\n"+"CARGUIO\t\t"+": "+numCargui+"\r\n"+"DOCUMENTO\t"+": "+numDoc+"\r\n"+"OVE\t\t"+": "+numOV+"\r\n"+"ESTADO\t\t"+": "+estMot);
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



