package cl.caserita.read.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ArchordDAO;
import cl.caserita.dao.iface.CardopdfDAO;
import cl.caserita.dao.iface.CarmailDAO;
import cl.caserita.dto.ArchordDTO;
import cl.caserita.dto.CardopdfDTO;
import cl.caserita.dto.CarmailDTO;

public class readMailAdjuntos {

	private static Logger log = Logger.getLogger(readMailDesarrollo.class); 

	private static FileWriter fileWriterlogi;
	private static String archivologi;
	private static Properties prop=null;
	private static String pathProperties;
	private static DAOFactory dao = DAOFactory.getInstance();
	private static ArchordDAO arch = dao.getArchordDAO();
	public static void main (String []args){
		DAOFactory dao =DAOFactory.getInstance();
		CarmailDAO mail = dao.getCarmailDAO();
		int i=1;
		try{
			while (i==1){
				List proce = mail.buscaMail();
				Iterator iter = proce.iterator();
				while (iter.hasNext()){
					CarmailDTO car = (CarmailDTO)iter.next();
					readMailAdjuntos mailAd = new readMailAdjuntos();
					log.info("P R O C E S O  C O R R E O :"+car.getCodigoCuentaCorreo().trim());
					mailAd.procesaCorreo(car.getCodigoEmpresa(), car.getCodigoBodega(), car.getCodigoCuentaCorreo().trim(), dao);
					Thread.sleep(10000);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void procesaCorreo(int empresa, int bodega,String mail, DAOFactory dao){
		Properties prop = new Properties();
		readMailAdjuntos read = new readMailAdjuntos();
		// Deshabilitamos TLS
		prop.setProperty("mail.pop3.starttls.enable", "false");

		// Hay que usar SSL
		/*prop.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
		prop.setProperty("mail.pop3.socketFactory.fallback", "false");*/

		// Puerto 995 para conectarse.
		prop.setProperty("mail.pop3.port","110");
		prop.setProperty("mail.pop3.socketFactory.port", "110");
		prop.setProperty("mail.smtp.host", "216.72.23.8");

		Session sesion = Session.getInstance(prop);
		
		// Para obtener un log más extenso.
		sesion.setDebug(true);
			try{
				Store store = sesion.getStore("pop3");
				store.connect("correo.upmchile.com", mail, "Lastra657");

	/*			store.connect("https://correo.upmchile.com","desarrollo1@caserita.cl","Lastra657");
	*/			Folder folder = store.getFolder("INBOX");
				folder.open(Folder.READ_WRITE);
				
			//	Message [] mensajes = folder.getMessages();
				 Message mensajes[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
				 
				 
				for (int i=0;i<mensajes.length;i++)
				{
					
					if (mensajes[i].getFrom()[0].toString().indexOf("caserita")==-1 && mensajes[i].getFrom()[0].toString().indexOf("CASERITA")==-1){
	                       mensajes[i].setFlag(Flags.Flag.DELETED, true);

					}else{
						if (mensajes[i].getSubject()!=null){
							if (mensajes[i].getSubject().indexOf("RV:")!=-1 || mensajes[i].getSubject().indexOf("RE:")!=-1 || mensajes[i].getSubject().indexOf("Re:")!=-1 || mensajes[i].getSubject().indexOf("Rv:")!=-1 || mensajes[i].getSubject().indexOf("re:")!=-1 || mensajes[i].getSubject().indexOf("rv:")!=-1){
			                       mensajes[i].setFlag(Flags.Flag.DELETED, true);

							}else{
								if ("informes@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "desarrollo@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "asilva@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "jramirez@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "jaguilera@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "alertasistema@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "areyes@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "sahumada@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "rromero@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "izapata@caserita.cl".equals(mensajes[i].getFrom()[0].toString())){
				                       mensajes[i].setFlag(Flags.Flag.DELETED, true);

								}else {
										System.out.println("From:"+mensajes[i].getFrom()[0].toString());
									   System.out.println("Subject:"+mensajes[i].getSubject());
									  //GeneraTICKET
									   int numeroOT=1000;
									//  int  numeroOT=procesaTicketDesarrollo(mensajes[i].getSubject().toString(), mensajes[i].getFrom()[0].toString());
						               Message forward = new MimeMessage(sesion);
						               //Lee Documentos a Procesar
						               CardopdfDAO cardo = dao.getCardopdfDAO();
						               CardopdfDTO docum = cardo.buscaDocumentosPendientesIndividual(empresa, bodega, mail, 0);
						               log.info("N U M E R O  C A R G U I O :"+docum.getNumeroCarguio());
						               log.info("D O C U M E N T O  P R O C E S A D O :"+docum.getNumeroDocumento());
						       
						                       
						                       		String principal= read.procesaContenido(mensajes[i],folder,store, docum.getNumeroDocumento(), dao, cardo,docum, docum.getCodigoBodega(), docum.getNumeroCarguio());
						                       		
						                       		Fecha fch = new Fecha();
							                  
						                       
						                       mensajes[i].setFlag(Flags.Flag.DELETED, true);
								}
								
					                      

								   
							}
						}else{
							System.out.println("From:"+mensajes[i].getFrom()[0].toString());
							   System.out.println("Subject:"+mensajes[i].getSubject());
								  int  numeroOT=0;


							   
				               Message forward = new MimeMessage(sesion);

				               forward.setRecipients(Message.RecipientType.TO,
				                       InternetAddress.parse("desarrollo@caserita.cl"));
								  forward.setSubject("Fwd: "+ "[Ticket numero :"+" "+numeroOT+"]"+mensajes[i].getSubject());

				                      // forward.setSubject("Fwd: " +mensajes[i].getSubject() + "[Ticket numero :"+" "+numeroOT+"]");
				                       forward.setFrom(new InternetAddress("informes@caserita.cl"));

				                       // Create the message part
				                       MimeBodyPart messageBodyPart = new MimeBodyPart();
				                       MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				                       CardopdfDAO cardo = dao.getCardopdfDAO();
						               CardopdfDTO docum = cardo.buscaDocumentosPendientesIndividual(empresa, bodega, mail, 0);
						               log.info("N U M E R O  C A R G U I O :"+docum.getNumeroCarguio());
						               log.info("D O C U M E N T O  P R O C E S A D O :"+docum.getNumeroDocumento());
				                      String principal =  read.procesaContenido(mensajes[i],folder,store, docum.getNumeroDocumento(), dao, cardo, docum, docum.getCodigoBodega(), docum.getNumeroCarguio());

				                     
				                       
				                       mensajes[i].setFlag(Flags.Flag.DELETED, true);
						}
					}
					
					
				}
				
	            folder.close(true);

				//Thread.sleep(60000);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public String procesaContenido(Message mensaje, Folder folder , Store store, int numeroOT, DAOFactory dao, CardopdfDAO cardoDAO, CardopdfDTO dtoPDF, int bodega, int numeroCarguio){
		String cuerpoMensaje="";
		
		String ruta="/home/guia/"+bodega+"/"+numeroCarguio+"/";
		File folder2 = new File(ruta);
		Fecha fch = new Fecha();
		if (folder2.exists()){
			
		}else
		{
			folder2.mkdirs();	
			
		}
		
		try{
			Object o = mensaje.getContent();

		// Si el mensaje es de texto sin adjuntos, ya sea texto HTML o // texto plano, el contenido será de tipo String
		if (o instanceof String) {
		// Si entra por aquí se trata de texto HTML
		if (mensaje.getContentType().indexOf("text/html") != -1) { DataHandler dh = mensaje.getDataHandler(); OutputStream os =
		new FileOutputStream("/home2/ticket/contenido.html"); dh.writeTo(os);
		os.close(); } else
		// Si entra por aquí se trata de texto plano
		System.out.println (o); }
		// Si entra por aquí es que se trata de un mensaje con adjunto/s o // HTML con imágenes embebidas en el mensaje
		else if (o instanceof Multipart) {
		// Se sabe que el contenido del mensaje es de tipo Multipart,
		// así que se le hace un casting para obtener un objeto de este // tipo
		Multipart mp = (Multipart)o;
		// Recorrer todos los Part que componen el mensaje
		int numPart = mp.getCount();
		for (int i=0; i < numPart; i++) {
				// Con cada parte del mensaje hay que ver la disposición que // tiene
				Part part = mp.getBodyPart(i);
				String disposition = part.getDisposition();
				System.out.println("Tipo"+part.getContentType());
				// Si la disposición es null probablemente se trate del // contenido en sí del mensaje
				if (disposition == null) {
					if (part.isMimeType("multipart/alternative") ||part.isMimeType("text/plain") || part.isMimeType("multipart/related")) {
							if (part.isMimeType("multipart/alternative")) {
								Multipart mp2 = (Multipart) part.getContent(); 
								Part part2 = mp2.getBodyPart(0); 
								cuerpoMensaje = (String)part2.getContent();
							}/*else{
								cuerpoMensaje = (String)part.getContent();
							}*/
							if (part.isMimeType("multipart/related")){
								Multipart mp2 = (Multipart) part.getContent(); 
								Part part2 = mp2.getBodyPart(0); 
								if (part2.isMimeType("multipart/related") || part2.isMimeType("multipart/alternative")){
									Multipart mp3 = (Multipart) part2.getContent(); 
									Part part3 = mp3.getBodyPart(0); 
									if (part3.isMimeType("multipart/related")){
										
									}else{
										cuerpoMensaje = (String)part3.getContent();

									}

								}else{
									cuerpoMensaje = (String)part2.getContent();

								}
								
							}
					}
				// Si entra por aquí se trataría de la parte del texto de // un correo HTML con imágenes embebidas
					else {
						if (part.getContentType().indexOf("text/html") != -1) {
								MimeBodyPart mbp = (MimeBodyPart)part;
								mbp.saveFile(ruta+"contenido.html"); 
						}
						if (part.getContentType().indexOf("text/plain") != -1) {
							MimeBodyPart mbp = (MimeBodyPart)part;
							mbp.saveFile(ruta+"contenido.html"); 
					}
					} 
				}
				// Si entra por aquí es que se trata de un adjunto o de una // imagen embebida en el mensaje
				else if ((disposition != null) && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || (disposition.equalsIgnoreCase(Part.INLINE)))) {
							String nombrePart = part.getFileName(); if (nombrePart == null)
							nombrePart = "adjunto" + i; // Procesar el adjunto o imagen
							MimeBodyPart mbp = (MimeBodyPart)part;
							mbp.saveFile(ruta + "GUIA_"+numeroOT+".pdf");
							dtoPDF.setEstadoProcesado(1);
							dtoPDF.setRutaDocumentoPDF(ruta + "GUIA_"+numeroOT+".pdf");
							dtoPDF.setNombrePDF("GUIA_"+numeroOT+".pdf");
							cardoDAO.actualizaDatos(dtoPDF);
							
				}
		} }
		System.out.println(cuerpoMensaje);
		
		String archivologi=ruta+numeroOT+".log";
		log.info("Archivo LOG : "+archivologi);
		File f=new File(archivologi);
		if (f.exists()){
			log.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterlogi=new FileWriter(archivologi,true);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		log.info("Archivo log:"+archivologi);

		fileWriterlogi.write( cuerpoMensaje);
		fileWriterlogi.flush();
		
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		//folder.close(false);
		//store.close();
		} catch (MessagingException me) {
		System.err.println(me.toString()); } catch (IOException ioe) {
		System.err.println(ioe.toString()); }
		return cuerpoMensaje;
		
		
	}
}
