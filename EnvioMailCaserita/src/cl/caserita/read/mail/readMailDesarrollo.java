package cl.caserita.read.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
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
import cl.caserita.dao.iface.OrdtrbDAO;
import cl.caserita.dao.iface.OrdttxtDAO;
import cl.caserita.dto.ArchordDTO;
import cl.caserita.dto.EmaapmDTO;
import cl.caserita.dto.OrdtrbDTO;
import cl.caserita.dto.OrdttxtDTO;

public class readMailDesarrollo {
	private static Logger log = Logger.getLogger(readMailDesarrollo.class); 

	private static FileWriter fileWriterlogi;
	private static String archivologi;
	private static Properties prop=null;
	private static String pathProperties;
	private static DAOFactory dao = DAOFactory.getInstance();
	private static ArchordDAO arch = dao.getArchordDAO();
	public static void main (String []args){
	

			
		int numero=1;
		readMailDesarrollo read = new readMailDesarrollo();
		while (numero==1){
			try{
				read.correoDesarrollo();
				read.correoSoporte();
				Thread.sleep(60000);
			}catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
		
		
		
		
		
	}
	public void correoDesarrollo(){
		Properties prop = new Properties();

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
				store.connect("correo.upmchile.com", "desarrollo1@caserita.cl", "Lastra657");

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

								}else{
										System.out.println("From:"+mensajes[i].getFrom()[0].toString());
									   System.out.println("Subject:"+mensajes[i].getSubject());
									  //GeneraTICKET
									  int  numeroOT=procesaTicketDesarrollo(mensajes[i].getSubject().toString(), mensajes[i].getFrom()[0].toString());
						               Message forward = new MimeMessage(sesion);

						               forward.setRecipients(Message.RecipientType.TO,InternetAddress.parse("desarrollo@caserita.cl"));
						              // forward.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mensajes[i].getFrom()[0].toString()));
										  forward.setSubject("Fwd: "+ "[Ticket numero :"+" "+numeroOT+"]"+mensajes[i].getSubject());

						                      // forward.setSubject("Fwd: " +mensajes[i].getSubject() + "[Ticket numero :"+" "+numeroOT+"]");
						                       forward.setFrom(new InternetAddress("informes@caserita.cl"));

						                       // Create the message part
						                       MimeBodyPart messageBodyPart = new MimeBodyPart();
						                       MimeBodyPart messageBodyPart2 = new MimeBodyPart();

						                       // Create a multipart message
						                       Multipart multipart = new MimeMultipart();
						                       
						                       		String principal= procesaContenido(mensajes[i],folder,store, numeroOT);
						                       		Fecha fch = new Fecha();
							                       String mensaje = "Comprobante Ingreso Ticket\n\nEstimado(a), \n\nLe informamos que con fecha " +fch.getFechaconFormato()+" Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud :\n\n" +principal;
							                       messageBodyPart2.setText(mensaje);
							                       forward.setText(mensaje );

							                       messageBodyPart.setContent(mensajes[i].getContent(), "message/rfc822");

							                       // Add part to multi part
							                       multipart.addBodyPart(messageBodyPart2);
							                       multipart.addBodyPart(messageBodyPart);
							                       // Associate multi-part with message
							                      // forward.setContent(multipart);
							                       forward.saveChanges();

							                       // Send the message by authenticating the SMTP server
							                       // Create a Transport instance and call the sendMessage
							                       Transport t = sesion.getTransport("smtp");
							                       
							                       try {
							                          //connect to the smpt server using transport instance
							        		  //change the user and password accordingly
							                          t.connect("informes@caserita.cl", "Lastra657");
							                          t.sendMessage(forward, forward.getAllRecipients());
							                       } finally {
							                          t.close();
							                       }
						                       
						                       mensajes[i].setFlag(Flags.Flag.DELETED, true);
								}
								
					                      

								   
							}
						}else{
							System.out.println("From:"+mensajes[i].getFrom()[0].toString());
							   System.out.println("Subject:"+mensajes[i].getSubject());
								  int  numeroOT=procesaTicketDesarrollo(mensajes[i].getSubject().toString(), mensajes[i].getFrom()[0].toString());

							   
				               Message forward = new MimeMessage(sesion);

				               forward.setRecipients(Message.RecipientType.TO,
				                       InternetAddress.parse("desarrollo@caserita.cl"));
								  forward.setSubject("Fwd: "+ "[Ticket numero :"+" "+numeroOT+"]"+mensajes[i].getSubject());

				                      // forward.setSubject("Fwd: " +mensajes[i].getSubject() + "[Ticket numero :"+" "+numeroOT+"]");
				                       forward.setFrom(new InternetAddress("informes@caserita.cl"));

				                       // Create the message part
				                       MimeBodyPart messageBodyPart = new MimeBodyPart();
				                       MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				                       String principal =  procesaContenido(mensajes[i],folder,store, numeroOT);

				                       // Create a multipart message
				                       Multipart multipart = new MimeMultipart();
				                       // set content
				                     
				                       Fecha fch = new Fecha();
				                       String mensaje = "Comprobante Ingreso Ticket\n\nEstimado(a), \n\nLe informamos que con fecha " +fch.getFechaconFormato()+" Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud :\n\n" +principal;
				                       
					                      // String mensaje = "Estimados, Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud \r\n:" +principal;
					                       messageBodyPart2.setText(mensaje);
					                       forward.setText(mensaje );

					                       messageBodyPart.setContent(mensajes[i].getContent(), "message/rfc822");

					                       // Add part to multi part
					                       multipart.addBodyPart(messageBodyPart2);
					                       multipart.addBodyPart(messageBodyPart);
					                       // Associate multi-part with message
					                      // forward.setContent(multipart);
					                       forward.saveChanges();

					                       // Send the message by authenticating the SMTP server
					                       // Create a Transport instance and call the sendMessage
					                       Transport t = sesion.getTransport("smtp");
					                       
					                       try {
					                          //connect to the smpt server using transport instance
					        		  //change the user and password accordingly
					                          t.connect("informes@caserita.cl", "Lastra657");
					                        t.sendMessage(forward, forward.getAllRecipients());
					                       } finally {
					                          t.close();
					                       }
				                       
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
	public void correoSoporte(){
		Properties prop = new Properties();

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
				store.connect("correo.upmchile.com", "soporte1@caserita.cl", "Lastra657");

	/*			store.connect("https://correo.upmchile.com","desarrollo1@caserita.cl","Lastra657");
	*/			Folder folder = store.getFolder("INBOX");
				folder.open(Folder.READ_WRITE);
				
			//	Message [] mensajes = folder.getMessages();
				 Message mensajes[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
				 
				 
				for (int i=0;i<mensajes.length;i++)
				{

					System.out.println(mensajes[i].getSubject());
					System.out.println(mensajes[i].getFrom()[0].toString());

					if (mensajes[i].getFrom()[0].toString().indexOf("caserita")==-1 && mensajes[i].getFrom()[0].toString().indexOf("CASERITA")==-1){
	                       mensajes[i].setFlag(Flags.Flag.DELETED, true);

					}else{
						if (mensajes[i].getSubject()!=null){
							if (mensajes[i].getSubject().indexOf("RV:")!=-1 || mensajes[i].getSubject().indexOf("RE:")!=-1 || mensajes[i].getSubject().indexOf("Re:")!=-1 || mensajes[i].getSubject().indexOf("Rv:")!=-1 || mensajes[i].getSubject().indexOf("re:")!=-1 || mensajes[i].getSubject().indexOf("rv:")!=-1){
			                       mensajes[i].setFlag(Flags.Flag.DELETED, true);

							}else{
								if ("informes@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "desarrollo@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "asilva@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "jramirez@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "jaguilera@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "alertasistema@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "areyes@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "rromero@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "izapata@caserita.cl".equals(mensajes[i].getFrom()[0].toString()) || "sahumada@caserita.cl".equals(mensajes[i].getFrom()[0].toString())){
				                       mensajes[i].setFlag(Flags.Flag.DELETED, true);

								}else{
									System.out.println("From:"+mensajes[i].getFrom()[0].toString());
									   System.out.println("Subject:"+mensajes[i].getSubject());
										  int  numeroOT=procesaTicketSoporte(mensajes[i].getSubject().toString(), mensajes[i].getFrom()[0].toString());

									  /* DAOFactory dao = DAOFactory.getInstance();
									   OrdtrbDAO ordtrbDAO = dao.getOrdtrbDAO();
									   OrdttxtDAO ordttxtDAO = dao.getOrdttxtDAO();
									   OrdttxtDTO ordttxDTO = new OrdttxtDTO();
									   OrdtrbDTO dto = new OrdtrbDTO();
									   Fecha fch = new Fecha();
									   int numeroOT = ordtrbDAO.buscaNumeroOrden();
									   numeroOT++;
									   dto.setCodigoEmpresa(2);
									   dto.setNumeroOrdenTrabajo(numeroOT);
									   dto.setFechaOrdenTrabajo(Integer.parseInt(fch.getYYYYMMDD()));
									   dto.setFechaSolicitud(Integer.parseInt(fch.getYYYYMMDD()));
									   dto.setFechaCierreOrden(0);
									   dto.setNombreSolicitante(mensajes[i].getFrom()[0].toString());
									   dto.setDiaSinSolucion(0);
									   dto.setCodigoDepartamento("SOPOR");
									   dto.setCodigoEstado("DG");
									   dto.setCodigoTecnico(0);
									   dto.setFechaCreacion(Integer.parseInt(fch.getYYYYMMDD()));
									   dto.setHoraCreacion(Integer.parseInt(fch.getHHMMSS()));
									   dto.setPrioridadOrden(0);
									   dto.setHoraOrden(Integer.parseInt(fch.getHHMMSS()));

									   ordtrbDAO.generaOrden(dto);
									   ordttxDTO.setCodigoEmpresa(2);
									   ordttxDTO.setTexto(mensajes[i].getSubject().trim().toString());
									   ordttxDTO.setNumeroOrdenTrabajo(numeroOT);
									   ordttxDTO.setNumeroLinea(1);
										 
				                       
									   ordttxtDAO.generaOrden(ordttxDTO);*/
										  
										  Message forward = new MimeMessage(sesion);
							               forward.setRecipients(Message.RecipientType.TO,
							                       InternetAddress.parse("soporte@caserita.cl"));
							                      // forward.setSubject("Fwd: " +mensajes[i].getSubject() + "[Ticket numero :"+" "+numeroOT+"]");
											  forward.setSubject("Fwd: "+ "[Ticket numero :"+" "+numeroOT+"]"+mensajes[i].getSubject());

							                       forward.setFrom(new InternetAddress("informes@caserita.cl"));
					                   Multipart multipart = new MimeMultipart();
					                   MimeBodyPart messageBodyPart = new MimeBodyPart();
					                   MimeBodyPart messageBodyPart2 = new MimeBodyPart();
						               String principal =  procesaContenido(mensajes[i],folder,store, numeroOT);
						               Fecha fch = new Fecha();
				                       String mensaje = "Comprobante Ingreso Ticket\n\nEstimado(a), \n\nLe informamos que con fecha " +fch.getFechaconFormato()+" Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud :\n\n" +principal;
				                       
						               //String mensaje = "Estimados, Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud \r\n:" +principal;
				                       messageBodyPart2.setText(mensaje);
				                       forward.setText(mensaje );

				                       messageBodyPart.setContent(mensajes[i].getContent(), "message/rfc822");

				                       // Add part to multi part
				                       multipart.addBodyPart(messageBodyPart2);
				                       multipart.addBodyPart(messageBodyPart);
				                       // Associate multi-part with message
				                      // forward.setContent(multipart);
				                       forward.saveChanges();

				                       // Send the message by authenticating the SMTP server
				                       // Create a Transport instance and call the sendMessage
				                       Transport t = sesion.getTransport("smtp");
				                       
				                       try {
				                          //connect to the smpt server using transport instance
				        		  //change the user and password accordingly
				                          t.connect("informes@caserita.cl", "Lastra657");
				                        t.sendMessage(forward, forward.getAllRecipients());
				                       } finally {
				                          t.close();
				                       }
						                      
						                       mensajes[i].setFlag(Flags.Flag.DELETED, true);
								}
								
					                      

								   
							}
						}else{
							System.out.println("From:"+mensajes[i].getFrom()[0].toString());
							   System.out.println("Subject:"+mensajes[i].getSubject());
								  int  numeroOT=procesaTicketSoporte(mensajes[i].getSubject().toString(), mensajes[i].getFrom()[0].toString());

							  /* DAOFactory dao = DAOFactory.getInstance();
							   OrdtrbDAO ordtrbDAO = dao.getOrdtrbDAO();
							   OrdttxtDAO ordttxtDAO = dao.getOrdttxtDAO();
							   OrdttxtDTO ordttxDTO = new OrdttxtDTO();
							   OrdtrbDTO dto = new OrdtrbDTO();
							   Fecha fch = new Fecha();
							   int numeroOT = ordtrbDAO.buscaNumeroOrden();
							   numeroOT++;
							   dto.setCodigoEmpresa(2);
							   dto.setNumeroOrdenTrabajo(numeroOT);
							   dto.setFechaOrdenTrabajo(Integer.parseInt(fch.getYYYYMMDD()));
							   dto.setFechaSolicitud(Integer.parseInt(fch.getYYYYMMDD()));
							   dto.setFechaCierreOrden(0);
							   dto.setNombreSolicitante(mensajes[i].getFrom()[0].toString());
							   dto.setDiaSinSolucion(0);
							   dto.setCodigoDepartamento("SOPOR");
							   dto.setCodigoEstado("DG");
							   dto.setCodigoTecnico(0);
							   dto.setFechaCreacion(Integer.parseInt(fch.getYYYYMMDD()));
							   dto.setHoraCreacion(Integer.parseInt(fch.getHHMMSS()));
							   dto.setHoraOrden(Integer.parseInt(fch.getHHMMSS()));

							   dto.setPrioridadOrden(0);
							   ordtrbDAO.generaOrden(dto);
							   ordttxDTO.setCodigoEmpresa(2);
							   ordttxDTO.setTexto(mensajes[i].getSubject().trim().toString());
							   ordttxDTO.setNumeroOrdenTrabajo(numeroOT);
							   ordttxDTO.setNumeroLinea(1);
							   ordttxtDAO.generaOrden(ordttxDTO);*/
								  Message forward = new MimeMessage(sesion);

					               forward.setRecipients(Message.RecipientType.TO,
					                       InternetAddress.parse("soporte@caserita.cl"));
					                       forward.setSubject("Fwd: "+ "[Ticket numero :"+" "+numeroOT+"]"+mensajes[i].getSubject());
					                       forward.setFrom(new InternetAddress("informes@caserita.cl"));
								  Multipart multipart = new MimeMultipart();
				                   MimeBodyPart messageBodyPart = new MimeBodyPart();
				                   MimeBodyPart messageBodyPart2 = new MimeBodyPart();
					               String principal =  procesaContenido(mensajes[i],folder,store, numeroOT);
					               Fecha fch = new Fecha();
			                       String mensaje = "Comprobante Ingreso Ticket\n\nEstimado(a), \n\nLe informamos que con fecha " +fch.getFechaconFormato()+" Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud :\n\n" +principal;
			                       
					               //String mensaje = "Estimados, Se ha generado el numero de ticket "+numeroOT+" en relacion a su solicitud \r\n:" +principal;
			                       messageBodyPart2.setText(mensaje);
			                       forward.setText(mensaje );

			                       messageBodyPart.setContent(mensajes[i].getContent(), "message/rfc822");

			                       // Add part to multi part
			                       multipart.addBodyPart(messageBodyPart2);
			                       multipart.addBodyPart(messageBodyPart);
			                       // Associate multi-part with message
			                      // forward.setContent(multipart);
			                       forward.saveChanges();

			                       // Send the message by authenticating the SMTP server
			                       // Create a Transport instance and call the sendMessage
			                       Transport t = sesion.getTransport("smtp");
			                       
			                       try {
			                          //connect to the smpt server using transport instance
			        		  //change the user and password accordingly
			                          t.connect("informes@caserita.cl", "Lastra657");
			                        t.sendMessage(forward, forward.getAllRecipients());
			                       } finally {
			                          t.close();
			                       }
				                       
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
	public void generaTexto(int numeroOT, String descripcion){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//log.info("Mes:"+mes);
		int mesin = Integer.parseInt(mes);
		
		String mesPal = fch.recuperaMes(mesin);
		log.info("Ruta carpeta");
		String rutalogi="/home2/ticket/"+numeroOT+"/";
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutalogi;
		log.info("Ruta :" + carpeta);
		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		
		String archivologi=carpeta+numeroOT+".log";
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

		fileWriterlogi.write( descripcion);
		fileWriterlogi.flush();
		ArchordDTO dto = new ArchordDTO();
		dto.setCodigoEmpresa(2);
		dto.setNumeroOrden(numeroOT);
		dto.setNumeroVersion(0);
		dto.setNumeroArchivo(1);
		dto.setRutaArchivo(archivologi);
		dto.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
		dto.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
		dto.setNombreEquipo("JAVA");
		dto.setIpEquipo("JAVA");
		dto.setUsuario("JAVA");
		
		arch.generaarchivoTicket(dto);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public int procesaTicketDesarrollo(String texto, String from){
		
		 DAOFactory dao = DAOFactory.getInstance();
		   OrdtrbDAO ordtrbDAO = dao.getOrdtrbDAO();
		   OrdttxtDAO ordttxtDAO = dao.getOrdttxtDAO();
		   OrdttxtDTO ordttxDTO = new OrdttxtDTO();
		   OrdtrbDTO dto = new OrdtrbDTO();
		   Fecha fch = new Fecha();
		   int numeroOT = ordtrbDAO.buscaNumeroOrden();
		   numeroOT++;
		   dto.setCodigoEmpresa(2);
		   dto.setNumeroOrdenTrabajo(numeroOT);
		   dto.setFechaOrdenTrabajo(Integer.parseInt(fch.getYYYYMMDD()));
		   dto.setFechaSolicitud(Integer.parseInt(fch.getYYYYMMDD()));
		   dto.setFechaCierreOrden(0);
		   dto.setNombreSolicitante(from);
		   dto.setHoraOrden(Integer.parseInt(fch.getHHMMSS()));
		   dto.setDiaSinSolucion(0);
		   dto.setCodigoDepartamento("DESA");
		   dto.setCodigoEstado("DG");
		   dto.setCodigoTecnico(0);
		   dto.setFechaCreacion(Integer.parseInt(fch.getYYYYMMDD()));
		   dto.setHoraCreacion(Integer.parseInt(fch.getHHMMSS()));
		   dto.setPrioridadOrden(0);
		   ordtrbDAO.generaOrden(dto);
		   ordttxDTO.setCodigoEmpresa(2);
		   ordttxDTO.setTexto(texto);
		   ordttxDTO.setNumeroOrdenTrabajo(numeroOT);
		   ordttxDTO.setNumeroLinea(1);
		   log.info("TExto :"+ordttxDTO.getTexto().trim());
		   ordttxtDAO.generaOrden(ordttxDTO);
		   return numeroOT;
	}
	public int procesaTicketSoporte(String texto, String from){
		
		 DAOFactory dao = DAOFactory.getInstance();
		   OrdtrbDAO ordtrbDAO = dao.getOrdtrbDAO();
		   OrdttxtDAO ordttxtDAO = dao.getOrdttxtDAO();
		   OrdttxtDTO ordttxDTO = new OrdttxtDTO();
		   OrdtrbDTO dto = new OrdtrbDTO();
		   Fecha fch = new Fecha();
		   int numeroOT = ordtrbDAO.buscaNumeroOrden();
		   numeroOT++;
		   dto.setCodigoEmpresa(2);
		   dto.setNumeroOrdenTrabajo(numeroOT);
		   dto.setFechaOrdenTrabajo(Integer.parseInt(fch.getYYYYMMDD()));
		   dto.setFechaSolicitud(Integer.parseInt(fch.getYYYYMMDD()));
		   dto.setFechaCierreOrden(0);
		   dto.setNombreSolicitante(from);
		   dto.setHoraOrden(Integer.parseInt(fch.getHHMMSS()));
		   dto.setDiaSinSolucion(0);
		   dto.setCodigoDepartamento("SOPOR");
		   dto.setCodigoEstado("DG");
		   dto.setCodigoTecnico(0);
		   dto.setFechaCreacion(Integer.parseInt(fch.getYYYYMMDD()));
		   dto.setHoraCreacion(Integer.parseInt(fch.getHHMMSS()));
		   dto.setPrioridadOrden(0);
		   ordtrbDAO.generaOrden(dto);
		   ordttxDTO.setCodigoEmpresa(2);
		   ordttxDTO.setTexto(texto);
		   ordttxDTO.setNumeroOrdenTrabajo(numeroOT);
		   ordttxDTO.setNumeroLinea(1);
		   log.info("TExto :"+ordttxDTO.getTexto().trim());
		   ordttxtDAO.generaOrden(ordttxDTO);
		   return numeroOT;
	}
	public String procesaContenido(Message mensaje, Folder folder , Store store, int numeroOT){
		String cuerpoMensaje="";
		String ruta="/home/ticket/"+numeroOT+"/";
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
		new FileOutputStream("/home/ticket/contenido.html"); dh.writeTo(os);
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
							mbp.saveFile(ruta + nombrePart);
							
							
							
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
		ArchordDTO dto = new ArchordDTO();
		dto.setCodigoEmpresa(2);
		dto.setNumeroOrden(numeroOT);
		dto.setNumeroVersion(0);
		dto.setNumeroArchivo(1);
		dto.setRutaArchivo(archivologi);
		dto.setFechaUsuario(Integer.parseInt(fch.getYYYYMMDD()));
		dto.setHoraUsuario(Integer.parseInt(fch.getHHMMSS()));
		dto.setNombreEquipo("JAVA");
		dto.setIpEquipo("JAVA");
		dto.setUsuario("JAVA");
		
		arch.generaarchivoTicket(dto);
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
	public void procesaMail(){
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

	/*	try{
			
			message.setFrom(new InternetAddress("informes@caserita.cl"));
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



			message.setSubject("Articulo Nuevo WMS");
			message.setText("Estimados, Se ha generado un nuevo articulo en el sistema WMS " +
			"Codigo Articulo :" +codigoArticulo + " Descripcion : "+ descripcionArticulo +" Generado Por :"+usuario + " Direccion IP :"+ ip);
			
			Transport t = session.getTransport("smtp");
			t.connect("informes@caserita.cl","Lastra657");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}*/
	
		
	}
}
