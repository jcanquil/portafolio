package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.ExdodcDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dao.iface.TptempDAO;
import cl.caserita.dao.impl.CentroDistribucionDAOImpl;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptempDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.FtpprovDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.ExdtraDTO;

public class IntegraCarguioHelper {

	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegraCarguioHelper.class);

	
	public static void main (String []args){
		IntegraCarguioHelper integra = new IntegraCarguioHelper();
		integra.buscaCarguio(2, "C", 46582, 26, "CARG","A");
		
		//integra.buscaCarguioTranfe(2, "I", 43367, 26, "RAMP","A");
		
		//integra.buscaCarguiosOT(2, "C", 13115, 26, "TRAS","A");
	}
	
	public FTPClient ftp(FtpprovDAO ftpDAO){
		FtpprovDTO dto = ftpDAO.buscaFTP("STG");
		String ip=dto.getIpServidor().trim();
		String usr=dto.getUsuarioFTP().trim();

		String password=dto.getPasswordFTP().trim();
		
		logi.info("ipFtp:"+ip);
		logi.info("usrFtp:"+usr);
		logi.info("passFtp:"+password);
        FTPClient ftpClient = new FTPClient();

		try
        {
           /* ftpClient.connect(InetAddress.getByName("192.168.1.23"));
            ftpClient.login("docu","Ca$er5808");*/
            //ftpClient.connect(InetAddress.getByName("192.168.1.32"));
            //ftpClient.login("jcanquil","Lastra657");
            ftpClient.connect(ip.trim());
            ftpClient.login(usr,password);
            int reply = ftpClient.getReplyCode();
            logi.info("Respuesta recibida de conexión FTP:" + reply);
            
            if(FTPReply.isPositiveCompletion(reply))
            {
                logi.info("Conectado Satisfactoriamente");    
            }
            else
                {
            	
                    logi.info("Imposible conectarse al servidor:"+FTPReply.isPositiveCompletion(reply));
                }
           
            //Verificar si se cambia de direcotirio de trabajo
            
            boolean mm = ftpClient.changeWorkingDirectory("/hostin");//Cambiar directorio de trabajo
            logi.info("Se cambió satisfactoriamente el directorio");
            //Activar que se envie cualquier tipo de archivo
            logi.info("Cambio directorio"+mm);
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
           
           // BufferedInputStream buffIn = null;
           // buffIn = new BufferedInputStream(new FileInputStream(xml));//Ruta del archivo para enviar
            //ftpClient.enterLocalPassiveMode();
         // Set protection buffer size
           // ftpClient.execPBSZ(0);
            // Set data channel protection to private
          //  ftpClient.execPROT("P");
            // Enter local passive mode
            ftpClient.enterLocalPassiveMode();
            
            
        }catch (Exception e){
        	e.printStackTrace();
        }
		return ftpClient;
	}
	
	public void buscaCarguiosOT(int codigoEmpresa, String estado, int numeroCarguio, int codigoBodega, String TipoPedCarguio, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		CarguioDAO carguio = dao.getCarguioDAO();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();
		ExmtraDTO dto = null;
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		
		//Lista solo las OT que contenga el carguio
		CarguioDTO lista = carguio.listaCarguiosOTWms(codigoEmpresa, estado, numeroCarguio, codigoBodega);
		FTPClient ftpClient = ftp(ftpDAO);

		Iterator iter = lista.getOrdenesOT().iterator();
		while (iter.hasNext()){
			dto = (ExmtraDTO) iter.next();
			
			String xml = formaXMLOTs(dto, integraDTO.getAccion().trim(), lista, TipoPedCarguio);
			
			//ftp.enviaFTP(generatxt(xml, String.valueOf(numeroCarguio) ));
			 
			String xmlEnvio = generatxt(xml, String.valueOf(numeroCarguio),String.valueOf(dto.getNumTraspaso()));
			if (ftp.enviaFTP2(xmlEnvio,ftpClient)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha2 = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha2);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("CARGUIOOT");
					loginDTO.setTipoAccion(integraDTO.getAccion());

					StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
					int campo=0;
					String xmlLog="";
					 while (st.hasMoreTokens( )){
						 
						 	String tr = st.nextToken();
						 	logi.info(tr);
						 	if (campo==1){
						 		xmlLog= tr.trim();
						 	}
						 campo++;
				        
				    }
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("N");
					loginDTO.setEstadoEnvio(0);
					lista.setNumeroRuta(dto.getNumTraspaso());
					log.generaLogCarguio(loginDTO, lista);
					try{
						
						Thread.sleep(400);
						
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}else{
				generatxt2(xml, String.valueOf(numeroCarguio));
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha2 = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha2);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("CARGUIOOT");
					loginDTO.setTipoAccion(integraDTO.getAccion());

					StringTokenizer st = new StringTokenizer(xmlEnvio,"!");
					int campo=0;
					String xmlLog="";
					 while (st.hasMoreTokens( )){
						 
						 	String tr = st.nextToken();
						 	logi.info(tr);
						 	if (campo==1){
						 		xmlLog= tr.trim();
						 	}
						 campo++;
				        
				    }
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("E");
					loginDTO.setEstadoEnvio(1);
					lista.setNumeroRuta(dto.getNumTraspaso());
					
					log.generaLogCarguio(loginDTO, lista);
				}
			}
			
			
			try{
				ftpClient.logout(); //Cerrar sesión
		         ftpClient.disconnect();//Desconectarse del servidor
				/*
				Thread.sleep(1000);
				log.info("tread 1000:");
				*/
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Actualiza el estado de la cabecera del Carguio 
		carguio.actualizarestadoCarguio(codigoEmpresa, codigoBodega, numeroCarguio, "G");
		
		//Actualiza el estado del detalle del Carguio
		carguio.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, numeroCarguio, "G");
	}
	
	public void buscaCarguioTranfe(int codigoEmpresa, String estado, int numeroCarguioTranf, int codigoBodega, String TipoPedCarguio, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		CarguioDAO carguio = dao.getCarguioDAO();
		TptempDAO tptempDAO = dao.getTptempDAO();
		TptbdgDAO tptbdgDAO = dao.getTptbdgDAO();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();
		
		ClidirDAO clidirDAO = dao.getClidirDAO();

		OrdvtaDTO dto = null;
		ClidirDTO clidir =null;
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(accion);
		TptempDTO empresa = tptempDAO.recuperaEmpresa(codigoEmpresa);
		
		List listaCarguios = carguio.listarCarguiosTranfeWms(codigoEmpresa, estado, numeroCarguioTranf, codigoBodega);
		FTPClient ftpClient = ftp(ftpDAO);

		Iterator iter = listaCarguios.iterator();
		while (iter.hasNext()){
			
			CarguioDTO lista = (CarguioDTO) iter.next() ;
			
			logi.info("CARGUIOTRANF:"+lista.getnumeroCarguioTransf());
			logi.info("CARGUIO:"+lista.getNumeroCarguio());
			/*String xmlCliente2 = formaXMLCliente2(lista,integraDTO.getAccion().trim(),"CLIE");
			 ftp.enviaFTP(generatxt(xmlCliente2, String.valueOf("Cliente2"+lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()) ));*/
			 
			Iterator iter2 = lista.getOrdenes().iterator();
			while (iter2.hasNext()){
				 dto = (OrdvtaDTO) iter2.next();
				 
				 Iterator dir = dto.getClidir().iterator();
				 while (dir.hasNext()){
					 clidir = (ClidirDTO) dir.next();
					 
				 }
				 //Crear Clientes
					//public String formaXMLCliente(OrdvtaDTO dto, String tipoAccion, String tipoCliente, String tipoCarguio, CarguioDTO carguio){
					//public String formaXMLCliente2(CarguioDTO dto, String tipoAccion, String tipoCliente){
					//public String formaXMLCliente3( ClidirDTO clidir, String tipoAccion, String tipoCliente){

				/* String xmlCliente = formaXMLCliente(dto,integraDTO.getAccion().trim(),"CARG",TipoPedCarguio,lista);
				 ftp.enviaFTP(generatxt(xmlCliente, String.valueOf("Cliente"+lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()) ));*/
				 
				 
				 
				 /*String xmlCliente3 = formaXMLCliente3(clidir,integraDTO.getAccion().trim(),"COMU");
				 ftp.enviaFTP(generatxt(xmlCliente3, String.valueOf("Cliente3"+lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()) ));*/
				 TptbdgDTO tptbdgDTO = null;

				 String xml = formaXML(dto, integraDTO.getAccion().trim(), lista, clidir, TipoPedCarguio, empresa, tptbdgDTO, clidirDAO);
				 
				 logi.info("OV:"+dto.getNumeroOV());
				 
				// ftp.enviaFTP(generatxt(xml, String.valueOf(lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()) ));
				 

				 String xmlEnvio = generatxt(xml, String.valueOf(lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()),String.valueOf(dto.getNumeroOV()));
					if (ftp.enviaFTP2(xmlEnvio, ftpClient)){
						LogintegracionDTO loginDTO = new LogintegracionDTO();
						Fecha fch = new Fecha();
						if (integraDTO.getIpEquipo()!=null){
							int fecha2 = Integer.parseInt(fch.getYYYYMMDD());
							int hora = Integer.parseInt(fch.getHHMMSS());
							
							loginDTO.setFechaArchivo(fecha2);
							loginDTO.setHoraArchivo(hora);
							loginDTO.setTabla("CARGUIOC");
							loginDTO.setTipoAccion(integraDTO.getAccion());

							StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
							int campo=0;
							String xmlLog="";
							 while (st.hasMoreTokens( )){
								 
								 	String tr = st.nextToken();
								 	logi.info(tr);
								 	if (campo==1){
								 		xmlLog= tr.trim();
								 	}
								 campo++;
						        
						    }
							loginDTO.setNombreArchivo(xmlLog);
							loginDTO.setUsuario(integraDTO.getUsuario().trim());
							loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
							loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
							loginDTO.setTipoEnvio("N");
							loginDTO.setEstadoEnvio(0);
							lista.setNumeroRuta(dto.getNumeroOV());
							log.generaLogCarguio(loginDTO, lista);
							try{
								
								Thread.sleep(400);
								
								
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
					}else{
						generatxt2(xml, String.valueOf(lista.getnumeroCarguioTransf())+"_"+String.valueOf(lista.getNumeroCarguio()));
						LogintegracionDTO loginDTO = new LogintegracionDTO();
						Fecha fch = new Fecha();
						if (integraDTO.getIpEquipo()!=null){
							int fecha2 = Integer.parseInt(fch.getDDMMYYYY());
							int hora = Integer.parseInt(fch.getHHMMSS());
							
							loginDTO.setFechaArchivo(fecha2);
							loginDTO.setHoraArchivo(hora);
							loginDTO.setTabla("CARGUIOC");
							loginDTO.setTipoAccion(integraDTO.getAccion());

							StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
							int campo=0;
							String xmlLog="";
							 while (st.hasMoreTokens( )){
								 
								 	String tr = st.nextToken();
								 	logi.info(tr);
								 	if (campo==1){
								 		xmlLog= tr.trim();
								 	}
								 campo++;
						        
						    }
							loginDTO.setNombreArchivo(xmlLog);
							loginDTO.setUsuario(integraDTO.getUsuario().trim());
							loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
							loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
							loginDTO.setTipoEnvio("E");
							loginDTO.setEstadoEnvio(1);
							lista.setNumeroRuta(dto.getNumeroOV());
							
							log.generaLogCarguio(loginDTO, lista);
						}
					}
					
				 
				 try{
					 /*
					 Thread.sleep(1000);
					 log.info("tread 1000:");
					 */
				 }catch(Exception e){
					 e.printStackTrace();
				 }
			}
		}
		
		//Actualiza el estado de la cabecera del Carguio de Transferencia (el proceso de confirmacion cambiara a EN RUTA el carguio de transferencia)
		//carguio.actualizarestadoCarguio(codigoEmpresa, codigoBodega, numeroCarguioTranf, "E");
		
		//Actualiza los carguios hijos del carguio de transferencia (solo si estos estan confirmados)
		List listaCarguiosHijos = carguio.buscaCarguiosHijos(codigoEmpresa, codigoBodega, numeroCarguioTranf, "C");
		
		Iterator iter2 = listaCarguiosHijos.iterator();
		while (iter2.hasNext()){
			CarguioDTO lista = (CarguioDTO) iter2.next() ;
			
			//Actualiza cabecera carguio
			carguio.actualizarestadoCarguio(lista.getCodigoEmpresa(), lista.getCodigoBodega(), lista.getNumeroCarguio(), "G");
			
			carguio.actualizarestadoDetalleCarguio(lista.getCodigoEmpresa(), lista.getCodigoBodega(), lista.getNumeroCarguio(), "G");
		}
		
	}
	
	public void buscaCarguio(int codigoEmpresa, String estado, int numeroCarguio, int codigoBodega, String TipoPedCarguio, String accion){
		DAOFactory dao = DAOFactory.getInstance();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		CarguioDAO carguio = dao.getCarguioDAO();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		TptempDAO tpempDAO = dao.getTptempDAO();
		TptbdgDAO tptbdgDAO = dao.getTptbdgDAO();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();

		ExmtraDAO exmtraDAO = dao.getExmtraDAO();
		
		ClidirDAO clidirDAO = dao.getClidirDAO();
		
		IntegracionDTO integraDTO = convierte.convierte(accion);
		TptempDTO empresa = tpempDAO.recuperaEmpresa(codigoEmpresa);
		OrdvtaDTO dto = null;
		ClidirDTO clidir =null;
		CarguioDTO lista = carguio.listaCarguiosWms(codigoEmpresa, estado, numeroCarguio, codigoBodega);
		/*String xmlCliente2 = formaXMLCliente2(lista,integraDTO.getAccion().trim(),"CLIE");
		 ftp.enviaFTP(generatxt(xmlCliente2, String.valueOf("Cliente2"+"_"+String.valueOf(lista.getNumeroCarguio())) ));*/
		FTPClient ftpClient = ftp(ftpDAO);
		Iterator iter = lista.getOrdenes().iterator();
		while (iter.hasNext()){
			 
			dto = (OrdvtaDTO) iter.next();
			Iterator dir = dto.getClidir().iterator();
			while (dir.hasNext()){
				clidir = (ClidirDTO) dir.next();
			}
			 
			/*String xmlCliente = formaXMLCliente(dto,integraDTO.getAccion().trim(),"CARG",TipoPedCarguio,lista);
			ftp.enviaFTP(generatxt(xmlCliente, String.valueOf("Cliente"+"_"+String.valueOf(lista.getNumeroCarguio())) ));*/
			 
			/*String xmlCliente3 = formaXMLCliente3(clidir,integraDTO.getAccion().trim(),"COMU");
			ftp.enviaFTP(generatxt(xmlCliente3, String.valueOf("Cliente3"+"_"+String.valueOf(lista.getNumeroCarguio()))));*/
			TptbdgDTO tptbdgDTO = null;
			if ("TRAS".equals(TipoPedCarguio)){
				 ExmtraDTO exmtraDTO = exmtraDAO.recuperaEncabezado(codigoEmpresa, dto.getNumeroOV());
				 if (exmtraDTO!=null){
					 tptbdgDTO = tptbdgDAO.buscaBodega(exmtraDTO.getBodegaDestino());
				 }
			 }
			 String xml = formaXML(dto, integraDTO.getAccion().trim(), lista, clidir, TipoPedCarguio, empresa, tptbdgDTO, clidirDAO);
			
			//ftp.enviaFTP(generatxt(xml, String.valueOf(numeroCarguio) ));
			 
			String xmlEnvio = generatxt(xml, String.valueOf(numeroCarguio),String.valueOf(dto.getNumeroOV()));
			if (ftp.enviaFTP2(xmlEnvio,ftpClient)){
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha2 = Integer.parseInt(fch.getYYYYMMDD());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha2);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("CARGUIOC");
					loginDTO.setTipoAccion(integraDTO.getAccion());

					StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
					int campo=0;
					String xmlLog="";
					 while (st.hasMoreTokens( )){
						 
						 	String tr = st.nextToken();
						 	logi.info(tr);
						 	if (campo==1){
						 		xmlLog= tr.trim();
						 	}
						 campo++;
				        
				    }
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("N");
					loginDTO.setEstadoEnvio(0);
					lista.setNumeroRuta(dto.getNumeroOV());
					log.generaLogCarguio(loginDTO, lista);
				}
				try{
					
					Thread.sleep(400);
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				generatxt2(xml, String.valueOf(numeroCarguio));
				LogintegracionDTO loginDTO = new LogintegracionDTO();
				Fecha fch = new Fecha();
				if (integraDTO.getIpEquipo()!=null){
					int fecha2 = Integer.parseInt(fch.getDDMMYYYY());
					int hora = Integer.parseInt(fch.getHHMMSS());
					
					loginDTO.setFechaArchivo(fecha2);
					loginDTO.setHoraArchivo(hora);
					loginDTO.setTabla("CARGUIOC");
					loginDTO.setTipoAccion(integraDTO.getAccion());

					StringTokenizer st = new StringTokenizer(xmlEnvio,"|");
					int campo=0;
					String xmlLog="";
					 while (st.hasMoreTokens( )){
						 
						 	String tr = st.nextToken();
						 	logi.info(tr);
						 	if (campo==1){
						 		xmlLog= tr.trim();
						 	}
						 campo++;
				        
				    }
					loginDTO.setNombreArchivo(xmlLog);
					loginDTO.setUsuario(integraDTO.getUsuario().trim());
					loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
					loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
					loginDTO.setTipoEnvio("E");
					loginDTO.setEstadoEnvio(1);
					lista.setNumeroRuta(dto.getNumeroOV());
					
					log.generaLogCarguio(loginDTO, lista);
				}
			}
			
			try{
				//ftpClient.logout(); //Cerrar sesión
		          //  ftpClient.disconnect();//Desconectarse del servidor
				/*
				Thread.sleep(1000);
				log.info("tread 1000:");
				*/
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//Actualiza el estado de la cabecera del Carguio 
		carguio.actualizarestadoCarguio(codigoEmpresa, codigoBodega, numeroCarguio, "G");
		
		//Actualiza el estado del detalle del Carguio
		carguio.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, numeroCarguio, "G");
	}
	
	public String generatxt(String XML, String nombreCarguio, String numeroOV){
		Fecha fch = new Fecha();
		//String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 14);
		//String fechaStr = String.valueOf(fch.getTimestamp()).substring(0,10) + "_" + String.valueOf(fch.getTimestamp()).substring(11,23);
		String fechaStr = String.valueOf(fch.getTimestamp()).substring(0,10) + "_" + String.valueOf(fch.getTimestamp()).substring(11);
		logi.info(fechaStr);
		
		fechaStr = fechaStr.replace("-", "");
		fechaStr = fechaStr.replace("/", "");
		fechaStr = fechaStr.replace(":", "");
		fechaStr = fechaStr.replace(".", "");
		
		logi.info(fechaStr);
		
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//log.info("Mes:"+mes);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);
		String rutaLog="/home/ftp/in/";
		String rutaLog2="/home/stgin/";
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutaLog+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/";
		
		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		logi.info("Ruta:"+carpeta);
		String nombreArchivo = "carguio"+nombreCarguio+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"carguio"+nombreCarguio+"_"+fechaStr+"_"+numeroOV+".xml";
		String archivoLog2 = rutaLog2+"carguio"+nombreCarguio+"_"+fechaStr+".xml";
		File f=new File(archivoLog);
		if (f.exists()){
			logi.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterLog=new FileWriter(archivoLog,true);
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		logi.info("Archivo LOG:"+archivoLog);
		fileWriterLog.write( XML+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return nombreArchivo+"|"+archivoLog;
	}
	
	public String generatxt2(String XML, String nombreCarguio){
		Fecha fch = new Fecha();
		//String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 14);
		//String fechaStr = String.valueOf(fch.getTimestamp()).substring(0,10) + "_" + String.valueOf(fch.getTimestamp()).substring(11,23);
		String fechaStr = String.valueOf(fch.getTimestamp()).substring(0,10) + "_" + String.valueOf(fch.getTimestamp()).substring(11);
		logi.info(fechaStr);
		
		fechaStr = fechaStr.replace("-", "");
		fechaStr = fechaStr.replace("/", "");
		fechaStr = fechaStr.replace(":", "");
		fechaStr = fechaStr.replace(".", "");
		
		logi.info(fechaStr);
		
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		//log.info("Mes:"+mes);
		int mesin = Integer.parseInt(mes);

		String mesPal = fch.recuperaMes(mesin);
		String rutaLog="/home/stgin/";
		
		//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
		String carpeta = rutaLog;
		
		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		logi.info("Ruta:"+carpeta);
		String nombreArchivo = "carguio"+nombreCarguio+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"carguio"+nombreCarguio+"_"+fechaStr+".xml";
		File f=new File(archivoLog);
		if (f.exists()){
			logi.info("No borra");
		}
			//f.delete();	
		else{
			try{
				fileWriterLog=new FileWriter(archivoLog,true);
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
		logi.info("Archivo LOG:"+archivoLog);
		fileWriterLog.write( XML+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return nombreArchivo+"|"+archivoLog;
	}
	public String formaXMLCliente(OrdvtaDTO dto, String tipoAccion, String tipoCliente, String tipoCarguio, CarguioDTO carguio){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		  /*if (TipoPedCarguio!="RAMPLA"){
				xmlCas.append("<STCUST>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</STCUST>");
			}else{
				xmlCas.append("<STCUST>"+carguio.getnumeroCarguioTransf()+"</STCUST>");
			}*/
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  if (!"RAMPLA".equals(tipoCarguio)){
			  xmlCas.append("<CSTNUM>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</CSTNUM>");

		  }else{
			  xmlCas.append("<CSTNUM>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</CSTNUM>");

		  }
		  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		  xmlCas.append("<BCKFLG>0</BCKFLG>");
		  xmlCas.append("<PARFLG>0</PARFLG>");
		  xmlCas.append("<CARFLG>UN</CARFLG>");
		  xmlCas.append("<SPLFLG>C</SPLFLG>");
		  xmlCas.append("<STDFLG>B</STDFLG>");
		  xmlCas.append("<SHPLBL>R</SHPLBL>");
		  
		  xmlCas.append("<SHIPBY></SHIPBY>");
		  xmlCas.append("<DEPTNO></DEPTNO>");
		  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		  xmlCas.append("<ORDINV></ORDINV>");
		  xmlCas.append("<CSTTYP>"+tipoCliente.trim()+"</CSTTYP>");
		  xmlCas.append("<INVSTS_PRG></INVSTS_PRG>");

		  xmlCas.append("<ROUTE_TO_ADDRESS></ROUTE_TO_ADDRESS>");
		  xmlCas.append("<ADDR_SEG>");
				  xmlCas.append("<SEGNAM>0</SEGNAM>");
				  xmlCas.append("<TRNTYP></TRNTYP>");
				  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				  if (!"RAMPLA".equals(tipoCarguio)){
					  xmlCas.append("<HOST_EXT_ID>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</HOST_EXT_ID>");
					  xmlCas.append("<ADRNAM>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</ADRNAM>");
				  }else{
					  xmlCas.append("<HOST_EXT_ID>"+carguio.getnumeroCarguioTransf()+"</HOST_EXT_ID>");
					  xmlCas.append("<ADRNAM>"+carguio.getnumeroCarguioTransf()+"</ADRNAM>");

				  }
				  
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  if (!"RAMPLA".equals(tipoCarguio)){
					  xmlCas.append("<ADRLN1>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</ADRLN1>");
					  xmlCas.append("<ADRCTY>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</ADRCTY>");
					  xmlCas.append("<ADRSTC>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</ADRSTC>");
				  }else{
					  
					  xmlCas.append("<ADRLN1>"+carguio.getnumeroCarguioTransf()+"</ADRLN1>");
					  xmlCas.append("<ADRCTY>"+carguio.getnumeroCarguioTransf()+"</ADRCTY>");
					  xmlCas.append("<ADRSTC>"+carguio.getnumeroCarguioTransf()+"</ADRSTC>");
				  }
				  
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  if (!"RAMPLA".equals(tipoCarguio)){
					  xmlCas.append("<LAST_NAME>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</LAST_NAME>");
					  xmlCas.append("<FIRST_NAME>"+dto.getRutCliente()+"-"+dto.getCorreDireccionOV()+"</FIRST_NAME>");
				  }else{
					  
					  xmlCas.append("<LAST_NAME>"+carguio.getnumeroCarguioTransf()+"</LAST_NAME>");
					  xmlCas.append("<FIRST_NAME>"+carguio.getnumeroCarguioTransf()+"</FIRST_NAME>");
				  }
				  

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	public String formaXMLCliente2(CarguioDTO dto, String tipoAccion, String tipoCliente){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
/*			xmlCas.append("<RTCUST>"+carguio.getNumeroCarguio()+"</RTCUST>");
*/
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<CSTNUM>"+"CARGUIO-"+dto.getNumeroCarguio()+"</CSTNUM>");
		  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		  xmlCas.append("<BCKFLG>0</BCKFLG>");
		  xmlCas.append("<PARFLG>0</PARFLG>");
		  xmlCas.append("<CARFLG>UN</CARFLG>");
		  xmlCas.append("<SPLFLG>C</SPLFLG>");
		  xmlCas.append("<STDFLG>B</STDFLG>");
		  xmlCas.append("<SHPLBL>R</SHPLBL>");
		  
		  xmlCas.append("<SHIPBY></SHIPBY>");
		  xmlCas.append("<DEPTNO></DEPTNO>");
		  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		  xmlCas.append("<ORDINV></ORDINV>");
		  xmlCas.append("<CSTTYP>"+tipoCliente.trim()+"</CSTTYP>");
		  xmlCas.append("<INVSTS_PRG></INVSTS_PRG>");

		  xmlCas.append("<ROUTE_TO_ADDRESS></ROUTE_TO_ADDRESS>");
		  xmlCas.append("<ADDR_SEG>");
				  xmlCas.append("<SEGNAM>0</SEGNAM>");
				  xmlCas.append("<TRNTYP></TRNTYP>");
				  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				  
				  xmlCas.append("<HOST_EXT_ID>"+"CARGUIO-"+dto.getNumeroCarguio()+"</HOST_EXT_ID>");
				  xmlCas.append("<ADRNAM>"+"CARGUIO-"+dto.getNumeroCarguio()+"</ADRNAM>");
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
				  xmlCas.append("<ADRLN1>"+"CARGUIO-"+dto.getNumeroCarguio()+"</ADRLN1>");
				  xmlCas.append("<ADRCTY>"+"CARGUIO-"+dto.getNumeroCarguio()+"</ADRCTY>");
				  xmlCas.append("<ADRSTC>"+"CARGUIO-"+dto.getNumeroCarguio()+"</ADRSTC>");
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  xmlCas.append("<LAST_NAME>"+"CARGUIO-"+dto.getNumeroCarguio()+"</LAST_NAME>");
				  xmlCas.append("<FIRST_NAME>"+"CARGUIO-"+dto.getNumeroCarguio()+"</FIRST_NAME>");

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	public String formaXMLCliente3( ClidirDTO clidir, String tipoAccion, String tipoCliente){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_CUST_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
/*			xmlCas.append("<BTCUST>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</BTCUST>");
*/
		  xmlCas.append("<CUST_SEG>");
		  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<CSTNUM>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</CSTNUM>");
		  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		  xmlCas.append("<BCKFLG>0</BCKFLG>");
		  xmlCas.append("<PARFLG>0</PARFLG>");
		  xmlCas.append("<CARFLG>UN</CARFLG>");
		  xmlCas.append("<SPLFLG>C</SPLFLG>");
		  xmlCas.append("<STDFLG>B</STDFLG>");
		  xmlCas.append("<SHPLBL>R</SHPLBL>");
		  
		  xmlCas.append("<SHIPBY></SHIPBY>");
		  xmlCas.append("<DEPTNO></DEPTNO>");
		  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		  xmlCas.append("<ORDINV></ORDINV>");
		  xmlCas.append("<CSTTYP>"+tipoCliente.trim()+"</CSTTYP>");
		  xmlCas.append("<INVSTS_PRG></INVSTS_PRG>");

		  xmlCas.append("<ROUTE_TO_ADDRESS></ROUTE_TO_ADDRESS>");
		  xmlCas.append("<ADDR_SEG>");
				  xmlCas.append("<SEGNAM>0</SEGNAM>");
				  xmlCas.append("<TRNTYP></TRNTYP>");
				  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				  
				  xmlCas.append("<HOST_EXT_ID>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</HOST_EXT_ID>");
				  xmlCas.append("<ADRNAM>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRNAM>");
				  xmlCas.append("<ADRTYP>CST</ADRTYP>");
				  
				  xmlCas.append("<ADRLN1>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRLN1>");
				  xmlCas.append("<ADRCTY>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRCTY>");
				  xmlCas.append("<ADRSTC>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</ADRSTC>");
				  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
				  xmlCas.append("<LAST_NAME>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</LAST_NAME>");
				  xmlCas.append("<FIRST_NAME>"+clidir.getRegion()+"-"+clidir.getDescripcionComuna()+"</FIRST_NAME>");

		  xmlCas.append("</ADDR_SEG>");
		  
		  
		 
					
			xmlCas.append("</CUST_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_CUST_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	public String formaXMLOTs(ExmtraDTO dto, String tipoAccion, CarguioDTO carguio, String TipoPedCarguio){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		
		xmlCas.append("<VC_RA_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
			xmlCas.append("<TRNNAM>RA_TRAN</TRNNAM>");
			xmlCas.append("<TRNVER>2010.2</TRNVER>");
			xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
			xmlCas.append("<HEADER_SEG>");
				xmlCas.append("<SEGNAM>HEADER_SEG</SEGNAM>");
				xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
				
				xmlCas.append("<INVNUM>"+carguio.getNumeroCarguio()+"_"+dto.getNumTraspaso()+"</INVNUM>");
				xmlCas.append("<SUPNUM>"+carguio.getNumeroCarguio()+"_"+dto.getNumTraspaso()+"</SUPNUM>");
				
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<RIMSTS>OPEN</RIMSTS>");
				xmlCas.append("<INVTYP>PROV</INVTYP>");
				xmlCas.append("<INVDTE>"+dto.getFechaTraspaso()+"</INVDTE>");
				
				Iterator car = dto.getExdtra().iterator();
				while (car.hasNext()){
					ExdtraDTO dtolista = (ExdtraDTO) car.next();
					
					xmlCas.append("<LINE_SEG>");
						xmlCas.append("<SEGNAM>LINE_SEG</SEGNAM>");
						xmlCas.append("<INVLIN>"+dtolista.getLinea() +"</INVLIN>");
						xmlCas.append("<INVSLN>"+dtolista.getLinea() +"</INVSLN>");
						xmlCas.append("<EXPQTY>"+dtolista.getCantDespachada() +"</EXPQTY>");
						xmlCas.append("<PRTNUM>"+dtolista.getCodArticulo()+"</PRTNUM>");
						xmlCas.append("<ORGCOD>----</ORGCOD>");
						xmlCas.append("<REVLVL>----</REVLVL>");
						xmlCas.append("<LOTNUM>----</LOTNUM>");
						xmlCas.append("<RCVSTS>"+carguio.getEstadoInvWMS()+"</RCVSTS>");
					xmlCas.append("</LINE_SEG>");
				}
				
			xmlCas.append("</HEADER_SEG>");
		xmlCas.append("</CTRL_SEG>");
		xmlCas.append("</VC_RA_INB_IFD>");
		
		resp = xmlCas.toString();  
		
		return resp;
	}
	
	public String formaXML(OrdvtaDTO dto, String tipoAccion, CarguioDTO carguio, ClidirDTO clidir, String TipoPedCarguio, TptempDTO empresa, TptbdgDTO tptbdg, ClidirDAO dao){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		String xml="";
		int iCorrDespa=0;
		String scomuna ="";
		
		//ClidirDTO clidir2 =null;
		//ClidirDTO clidirDTO new = clidirDTO;ExdodcDAO exdodc = dao.getExdodcDAO();
		
		//xmlCas.append("<PART_INB_IFD>");
		xmlCas.append("<VC_ORDER_INB_IFD>");
		xmlCas.append("<CTRL_SEG>");
		xmlCas.append("<TRNNAM>ORDER_TRAN</TRNNAM>");
		xmlCas.append("<TRNVER>8.2</TRNVER>");
		xmlCas.append("<WHSE_ID>CD26</WHSE_ID>");
		
		Iterator car2 = dto.getDetord().iterator();
		int correlativo2=1;
		while (car2.hasNext())
		{
			DetordDTO carDTO = (DetordDTO) car2.next();
			
			//Recupera comuna
			scomuna = dao.recuperaComuna(carDTO.getCodRegion(), carDTO.getCodCiudad(), carDTO.getCodComuna()); 
			iCorrDespa = carDTO.getCorrelativoDespacho();
			
			correlativo2++;
		}
			
		//Clientes
		if ("RAMP".equals(TipoPedCarguio))
		{
			xmlCas.append("<ST_CUST_SEG>");
			xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<HOST_EXT_ID>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</HOST_EXT_ID>");
			xmlCas.append("<ADRNAM>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</ADRNAM>");
			xmlCas.append("<ADRTYP>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</ADRTYP>");
			xmlCas.append("<ADRLN1>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</ADRLN1>");
			xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			xmlCas.append("</ST_CUST_SEG>");
		}else {
			if ("TRAS".equals(TipoPedCarguio))
			{
				xmlCas.append("<ST_CUST_SEG>");
				xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<HOST_EXT_ID>"+empresa.getRut()+"-"+iCorrDespa+"</HOST_EXT_ID>");
				xmlCas.append("<ADRNAM>"+empresa.getRut()+"-"+iCorrDespa+"</ADRNAM>");
				xmlCas.append("<ADRTYP>"+empresa.getRut()+"-"+iCorrDespa+"</ADRTYP>");
				xmlCas.append("<ADRLN1>"+empresa.getRut()+"-"+iCorrDespa+"</ADRLN1>");
				xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
				xmlCas.append("</ST_CUST_SEG>");
			}else{
				
				xmlCas.append("<ST_CUST_SEG>");
				xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<HOST_EXT_ID>"+dto.getRutCliente()+"-"+iCorrDespa+"</HOST_EXT_ID>");
				xmlCas.append("<ADRNAM>"+dto.getRutCliente()+"-"+iCorrDespa+"</ADRNAM>");
				xmlCas.append("<ADRTYP>"+dto.getRutCliente()+"-"+iCorrDespa+"</ADRTYP>");
				xmlCas.append("<ADRLN1>"+dto.getRutCliente()+"-"+iCorrDespa+"</ADRLN1>");
				xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
				xmlCas.append("</ST_CUST_SEG>");
			}
		}
			
		//Clientes
		if ("RAMP".equals(TipoPedCarguio))
		{		
			xmlCas.append("<RT_CUST_SEG>");
			xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<HOST_EXT_ID>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</HOST_EXT_ID>");
			xmlCas.append("<ADRNAM>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRNAM>");
			xmlCas.append("<ADRTYP>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRTYP>");
			xmlCas.append("<ADRLN1>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRLN1>");
			xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			xmlCas.append("</RT_CUST_SEG>");
		}else {
			if ("TRAS".equals(TipoPedCarguio)){
				xmlCas.append("<RT_CUST_SEG>");
				xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<HOST_EXT_ID>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</HOST_EXT_ID>");
				xmlCas.append("<ADRNAM>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRNAM>");
				xmlCas.append("<ADRTYP>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRTYP>");
				xmlCas.append("<ADRLN1>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRLN1>");
				xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
				xmlCas.append("</RT_CUST_SEG>");
			}
			else{
				xmlCas.append("<RT_CUST_SEG>");
				xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<HOST_EXT_ID>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</HOST_EXT_ID>");
				xmlCas.append("<ADRNAM>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRNAM>");
				xmlCas.append("<ADRTYP>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRTYP>");
				xmlCas.append("<ADRLN1>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</ADRLN1>");
				xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
				xmlCas.append("</RT_CUST_SEG>");
			}
		}
		
		//Clientes
		if ("RAMP".equals(TipoPedCarguio))
		{	
			xmlCas.append("<BT_CUST_SEG>");
			xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
			xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
			xmlCas.append("<HOST_EXT_ID>"+clidir.getRegion()+"-"+scomuna+"</HOST_EXT_ID>");
			xmlCas.append("<ADRNAM>"+clidir.getRegion()+"-"+scomuna+"</ADRNAM>");
			xmlCas.append("<ADRTYP>"+clidir.getRegion()+"-"+scomuna+"</ADRTYP>");
			xmlCas.append("<ADRLN1>"+clidir.getRegion()+"-"+scomuna+"</ADRLN1>");
			xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			xmlCas.append("</BT_CUST_SEG>");
		}else {
			if ("TRAS".equals(TipoPedCarguio))
			{
				if (tptbdg!=null)
				{
					xmlCas.append("<BT_CUST_SEG>");
					xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
					xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
					xmlCas.append("<HOST_EXT_ID>"+tptbdg.getCodigoRegion()+"-"+tptbdg.getDescComuna().trim()+"</HOST_EXT_ID>");
					xmlCas.append("<ADRNAM>"+tptbdg.getCodigoRegion()+"-"+tptbdg.getDescComuna().trim()+"</ADRNAM>");
					xmlCas.append("<ADRTYP>"+tptbdg.getCodigoRegion()+"-"+tptbdg.getDescComuna().trim()+"</ADRTYP>");
					xmlCas.append("<ADRLN1>"+tptbdg.getCodigoRegion()+"-"+tptbdg.getDescComuna().trim()+"</ADRLN1>");
					xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
					xmlCas.append("</BT_CUST_SEG>");
				}
			
			}else{
				xmlCas.append("<BT_CUST_SEG>");
				xmlCas.append("<SEGNAM>ST_CUST_SEG</SEGNAM>");
				xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
				xmlCas.append("<HOST_EXT_ID>"+clidir.getRegion()+"-"+scomuna+"</HOST_EXT_ID>");
				xmlCas.append("<ADRNAM>"+clidir.getRegion()+"-"+scomuna+"</ADRNAM>");
				xmlCas.append("<ADRTYP>"+clidir.getRegion()+"-"+scomuna+"</ADRTYP>");
				xmlCas.append("<ADRLN1>"+clidir.getRegion()+"-"+scomuna+"</ADRLN1>");
				xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
				xmlCas.append("</BT_CUST_SEG>");
			}
		}
		
		xmlCas.append("<ORDER_SEG>");
		xmlCas.append("<SEGNAM>ORDER</SEGNAM>");
		xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
		
		///xmlCas.append("<ORDNUM>"+carguio.getnumeroCarguioTransf()+"-"+carguio.getNumeroCarguio()+"-"+dto.getNumeroOV()+"-"+dto.getCorreDireccionOV()+"</ORDNUM>");
		xmlCas.append("<ORDNUM>"+carguio.getnumeroCarguioTransf()+"-"+carguio.getNumeroCarguio()+"-"+dto.getNumeroOV()+"-"+iCorrDespa+"</ORDNUM>");
		
		xmlCas.append("<ORDTYP>"+TipoPedCarguio.trim()+"</ORDTYP>");
		xmlCas.append("<ENTDTE>"+dto.getFechaOrden()+"000000"+"</ENTDTE>");
		
		if ("RAMP".equals(TipoPedCarguio)){
			xmlCas.append("<STCUST>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</STCUST>");
		}else{
			if ("TRAS".equals(TipoPedCarguio))
			{
				xmlCas.append("<STCUST>"+empresa.getRut()+"-"+iCorrDespa+"</STCUST>");
			}
			else{
				xmlCas.append("<STCUST>"+dto.getRutCliente()+"-"+iCorrDespa+"</STCUST>");
			}
		}
		
		if ("RAMP".equals(TipoPedCarguio)){
			xmlCas.append("<ST_HOST_ADR_ID>"+"RAMPLA-"+carguio.getnumeroCarguioTransf()+"</ST_HOST_ADR_ID>");
		}else{
			if ("TRAS".equals(TipoPedCarguio))
			{
				xmlCas.append("<ST_HOST_ADR_ID>"+empresa.getRut()+"-"+iCorrDespa+"</ST_HOST_ADR_ID>");
			}
			else{
				xmlCas.append("<ST_HOST_ADR_ID>"+dto.getRutCliente()+"-"+iCorrDespa+"</ST_HOST_ADR_ID>");
			}
		}
		
		xmlCas.append("<RTCUST>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</RTCUST>");
		xmlCas.append("<RT_HOST_ADR_ID>"+"CARGUIO-"+carguio.getNumeroCarguio()+"</RT_HOST_ADR_ID>");
		
		if ("TRAS".equals(TipoPedCarguio))
		{
			xmlCas.append("<BTCUST>"+tptbdg.getCodigoRegion()+"-"+tptbdg.getDescComuna().trim()+"</BTCUST>");
			xmlCas.append("<BT_HOST_ADR_ID>"+tptbdg.getCodigoRegion()+"-"+tptbdg.getDescComuna().trim()+"</BT_HOST_ADR_ID>");
		}
		else{
			xmlCas.append("<BTCUST>"+clidir.getRegion()+"-"+scomuna+"</BTCUST>");
			xmlCas.append("<BT_HOST_ADR_ID>"+clidir.getRegion()+"-"+scomuna+"</BT_HOST_ADR_ID>");
		}
		
		xmlCas.append("<SHIPBY>RTCUST</SHIPBY>");
		xmlCas.append("<WAVE_FLG>1</WAVE_FLG>");
		xmlCas.append("<DEPTNO>"+carguio.getPatente()+"</DEPTNO>");
		xmlCas.append("</ORDER_SEG>");
		
		Iterator car = dto.getDetord().iterator();
		int correlativo=1;
		while (car.hasNext())
		{
			DetordDTO carDTO = (DetordDTO) car.next();
			
			xmlCas.append("<ORDER_LINE_SEG>");
			
			xmlCas.append("<SEGNAM>ORDER_LINE</SEGNAM>");
			///xmlCas.append("<ORDNUM>"+carguio.getnumeroCarguioTransf()+"-"+carguio.getNumeroCarguio()+"-"+dto.getNumeroOV()+"-"+dto.getCorreDireccionOV()+"</ORDNUM>");
			xmlCas.append("<ORDNUM>"+carguio.getnumeroCarguioTransf()+"-"+carguio.getNumeroCarguio()+"-"+dto.getNumeroOV()+"-"+carDTO.getCorrelativoDespacho()+"</ORDNUM>");
			
			//xmlCas.append("<ORDLIN>"+carDTO.getCorrelativoDetalleOV()+"</ORDLIN>");//Se modifica para comenzar en 0
			xmlCas.append("<ORDLIN>"+correlativo+"</ORDLIN>");
			xmlCas.append("<ORDSLN>"+"S"+correlativo+"</ORDSLN>");
			xmlCas.append("<PRTNUM>"+carDTO.getCodigoArticulo()+"</PRTNUM>");
			
			xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
			xmlCas.append("<INVSTS>"+carguio.getEstadoInvWMS()+"</INVSTS>");
			xmlCas.append("<ORDQTY>"+carDTO.getCantidadArticulo()+"</ORDQTY>");
			if ("RAMP".equals(TipoPedCarguio)){
				xmlCas.append("<CARCOD>RAMPLA</CARCOD>");

			}else{
				xmlCas.append("<CARCOD>"+carguio.getRutChofer()+"-"+carguio.getDvChofer()+"</CARCOD>");
			}
			//xmlCas.append("<CARCOD>"+carguio.getRutChofer()+"-"+carguio.getDvChofer()+"</CARCOD>");
			xmlCas.append("<UNTPAK>0</UNTPAK>");
			xmlCas.append("<UNTPAL>0</UNTPAL>");
			xmlCas.append("<UNTCAS>0</UNTCAS>");
			xmlCas.append("<LATE_SHPDTE>"+dto.getFechaOrden()+"000000"+"</LATE_SHPDTE>");//verificar por el momento se enviara el mismo dia del pedido
			xmlCas.append("<SRVLVL>1</SRVLVL>");
			xmlCas.append("<DSTARE>SDESP</DSTARE>");
			xmlCas.append("</ORDER_LINE_SEG>");
			
			correlativo++;
		}
		
		xmlCas.append("</CTRL_SEG>");  
		xmlCas.append("</VC_ORDER_INB_IFD>");  
		//xmlCas.append("</PART_INB_IFD>");
		resp = xmlCas.toString();
		
		return resp;
	}
}
