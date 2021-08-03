package cl.caserita.wms.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExdodcDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmodcDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

	public class IntegracionExdodcHelper {
		private static FileWriter fileWriterLog;
		private  static Logger logi = Logger.getLogger(IntegracionExdodcHelper.class);

		public static void main (String []agrs){
			IntegracionExdodcHelper helper = new IntegracionExdodcHelper();
				//helper.integracionAll();
				helper.integracion(2,395419, "A,PC,PC,JAIME");
			}
			
		/*
		public void integracionAll(){
			DAOFactory dao = DAOFactory.getInstance();
			ExdodcDAO exdodc = dao.getExdodcDAO();
			EnviaFTPWMS ftp = new EnviaFTPWMS();
			List lista = exdodc.buscaAllTransportista();
			Iterator iter = lista.iterator();
			while (iter.hasNext()){
				ExdodcDTO dto = (ExdodcDTO) iter.next();
				
				String xml = formaXML(dto, "A");
				ftp.enviaFTP(generatxt(dto.getRutChofer(),xml));
			}
			
		}
		*/
		
		public void integracion(int codEmpresa, int numOC, String accion){
			DAOFactory dao = DAOFactory.getInstance();
			ExmodcDAO exmodc = dao.getExmodcDAO();
			ExdodcDAO exdodc = dao.getExdodcDAO();
			EnviaFTPWMS ftp = new EnviaFTPWMS();
			LogintegracionDAO log = dao.getLogintegracionDAO();
			ConvierteStringDTO convierte = new ConvierteStringDTO();
			IntegracionDTO integraDTO = convierte.convierte(accion);
			ExmartDAO exmartDAO = dao.getExmartDAO();
			FtpprovDAO ftpDAO = dao.getFtpprovDAO();

			ExmodcDTO exmodcdto = exmodc.buscaCabOrden(codEmpresa, numOC);
			List lista = exdodc.listaExdodc(codEmpresa, numOC);
			
				//String xmlCliente = formaXMLClie(exmodcdto.getNumeroOrden(), accion, "PROV");
				//logi.info(xmlCliente);
				//ftp.enviaFTP(generatxt(exmodcdto.getNumeroOrden(),xmlCliente.trim()));
				
				

				String xml = formaXML(exmodcdto,lista, integraDTO.getAccion().trim(), exmartDAO);
				//ftp.enviaFTP(generatxt(exmodcdto.getNumeroOrden(),xml.trim()));
				String xmlEnvio = generatxt(exmodcdto.getNumeroOrden(),xml.trim());
				if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getYYYYMMDD());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setTipoAccion(integraDTO.getAccion());

						
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("EXMTRA");
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
						
						log.generaLogOrdenesCompra(loginDTO, exmodcdto);
						}
					
				}else{
					generatxt2(exmodcdto.getNumeroOrden(),xml.trim());
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getDDMMYYYY());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setTipoAccion(integraDTO.getAccion());

						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("EXMTRA");
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
						
						log.generaLogOrdenesCompra(loginDTO, exmodcdto);
					}
				}
				
			
		}
		
		public String generatxt(int numoc, String XML){
			Fecha fch = new Fecha();
			String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 14);
			String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
			String mes = fch.getYYYYMMDD().substring(4, 6);
			//logi.info("Mes:"+mes);
			int mesin = Integer.parseInt(mes);

			String mesPal = fch.recuperaMes(mesin);
			String rutaLog="/home/ftp/in/";
			//String carpeta = prop.getProperty("archivos.salida.path")+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/"+generacion+"/"+"Bodega"+"_"+bodega+"/";
			String carpeta = rutaLog+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/";

			File folder = new File(carpeta);
			if (folder.exists()){
				
			}else
			{
				folder.mkdirs();	
				
			}
			logi.info("Ruta:"+carpeta);
			String nombreArchivo = "ordencompra_"+numoc+"_"+fechaStr+".xml";
			String archivoLog=carpeta+"ordencompra_"+numoc+"_"+fechaStr+".xml";
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
		
		public String generatxt2(int numoc, String XML){
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
			String nombreArchivo = "ordencompra_"+numoc+"_"+fechaStr+".xml";
			String archivoLog=carpeta+"ordencompra_"+numoc+"_"+fechaStr+".xml";
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
		
		public String formaXMLClie(int numeroOC, String tipoAccion, String tipoCliente){
			//Forma XML para enviar a WMS
			String resp ="";
			StringBuffer xmlCas = new StringBuffer();
			  String xml="";
			 
			  xmlCas.append("<VC_CUST_INB_IFD>");
			  xmlCas.append("<CTRL_SEG>");
			  xmlCas.append("<TRNNAM>CUS_TRAN</TRNNAM>");
			  xmlCas.append("<TRNVER>2011.1</TRNVER>");
			  xmlCas.append("<WHSE_ID>26</WHSE_ID>");
			  
			  xmlCas.append("<CUST_SEG>");
			  xmlCas.append("<SEGNAM>CUSTOMER</SEGNAM>");
			  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
			  xmlCas.append("<CSTNUM>"+numeroOC+"</CSTNUM>");
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
			  /*xmlCas.append("<ADDR_SEG>");
					  xmlCas.append("<SEGNAM>0</SEGNAM>");
					  xmlCas.append("<TRNTYP></TRNTYP>");
					  xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
					  
					  xmlCas.append("<HOST_EXT_ID>"+dto.getRutCliente()+clidir.getCorrelativo()+"</HOST_EXT_ID>");
					  xmlCas.append("<ADRNAM>"+dto.getRutCliente()+clidir.getCorrelativo()+"</ADRNAM>");
					  xmlCas.append("<ADRTYP>CST</ADRTYP>");
					  
					  xmlCas.append("<ADRLN1>"+clidir.getDireccionCliente()+"</ADRLN1>");
					  xmlCas.append("<ADRCTY>"+clidir.getDescripcionCiudad()+"</ADRCTY>");
					  xmlCas.append("<ADRSTC>"+clidir.getDescripcionComuna()+"</ADRSTC>");
					  xmlCas.append("<CTRY_NAME>CHL</CTRY_NAME>");
					  xmlCas.append("<LAST_NAME>"+dto.getDireccionCliente()+"</LAST_NAME>");
					  xmlCas.append("<FIRST_NAME>"+dto.getDireccionCliente()+"</FIRST_NAME>");

			  xmlCas.append("</ADDR_SEG>");*/
			  
			  
			 
						
				xmlCas.append("</CUST_SEG>");  
				xmlCas.append("</CTRL_SEG>");  
				xmlCas.append("</VC_CUST_INB_IFD>");  
				resp = xmlCas.toString();
					  
			  
			  
			return resp;
		}
		
		public String formaXML(ExmodcDTO dto, List dtolist, String tipoAccion, ExmartDAO exmartDAO){
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
							xmlCas.append("<INVNUM>"+dto.getNumeroOrden()+"</INVNUM>");
							xmlCas.append("<SUPNUM>"+dto.getRutProveedor()+dto.getDigito()+"</SUPNUM>");
							xmlCas.append("<CLIENT_ID>----</CLIENT_ID>");
							xmlCas.append("<RIMSTS>OPEN</RIMSTS>");
							xmlCas.append("<INVTYP>PROV</INVTYP>");
							xmlCas.append("<INVDTE>"+dto.getFechaOrden()+"</INVDTE>");
						
							Iterator iter = dtolist.iterator();
							while (iter.hasNext()){
								ExdodcDTO dtolista = (ExdodcDTO) iter.next();
						
								xmlCas.append("<LINE_SEG>");
									xmlCas.append("<SEGNAM>LINE_SEG</SEGNAM>");
									xmlCas.append("<INVLIN>"+dtolista.getNumLinea()+"</INVLIN>");
									xmlCas.append("<INVSLN>"+dtolista.getNumLinea()+"</INVSLN>");
									ExmartDTO exmart = exmartDAO.recuperaArticulo(dtolista.getCodArticulo(), dtolista.getDvArticulo());
									
									ExdartDTO dto2 = (ExdartDTO) exmart.getCodigos().get("U");
									  if (dto2!=null){
										  int cantidadBP=(int)dto2.getCantBasePallets();
										  exmart.setPallet((int)dto2.getCantTotalPallets());
									  }
									  
									int unidades=0;
									if ("C".equals(dtolista.getFormato())){
										if (exmart.getDisplay()>0){
											unidades=exmart.getCaja()*exmart.getDisplay()*dtolista.getStocLinea();

										}else{
											unidades=exmart.getCaja()*dtolista.getStocLinea();

										}
									}else if ("D".equals(dtolista.getFormato())){
										unidades=exmart.getDisplay()*dtolista.getStocLinea();
									}else if ("P".equals(dtolista.getFormato())){
										if (exmart.getDisplay()>0){
											unidades=(exmart.getPallet()*exmart.getCaja()*exmart.getDisplay())*dtolista.getStocLinea();
										}else{
											unidades=(exmart.getPallet()*exmart.getCaja())*dtolista.getStocLinea();
										}
										
									}else if ("U".equals(dtolista.getFormato())){
										unidades=dtolista.getStocLinea();
									}
									/*if ("P".equals(exdart.getTipoContenedor())){
										  contenedor="PA";
										  //uoml=4;
										  pallet=1;
										  if (dto.getDisplay()>0){
											  unidades=dto.getPallet()*dto.getCaja()*dto.getDisplay();

										  }else{
											  unidades=dto.getPallet()*dto.getCaja();

										  }
									  }else if ("U".equals(exdart.getTipoContenedor())){
										  contenedor="UN";
										  //uoml=1;
										  uni=1;
										  unidades=1;

									  }else if ("D".equals(exdart.getTipoContenedor())){
										  contenedor="DP";
										  //uoml=2;
										  display=1;
										  unidades=dto.getDisplay();
									  }else if ("C".equals(exdart.getTipoContenedor())){
										  contenedor="CJ";
										  //uoml=3;
										  caja=1;
										  unidades=dto.getCaja();
										  bul=1;
									  }*/
									
									
									
									//xmlCas.append("<EXPQTY>"+dtolista.getStocLinea()+"</EXPQTY>");//Se modifica por unidades
									xmlCas.append("<EXPQTY>"+unidades+"</EXPQTY>");
									xmlCas.append("<PRTNUM>"+dtolista.getCodArticulo()+"</PRTNUM>");
									xmlCas.append("<ORGCOD>----</ORGCOD>");
									xmlCas.append("<REVLVL>----</REVLVL>");
									xmlCas.append("<LOTNUM>----</LOTNUM>");
									xmlCas.append("<RCVSTS>"+dto.getEstadoInvOrden().trim()+"</RCVSTS>");
								xmlCas.append("</LINE_SEG>");
							}
							
						xmlCas.append("</HEADER_SEG>");
					xmlCas.append("</CTRL_SEG>");
					xmlCas.append("</VC_RA_INB_IFD>");

					
					
					resp = xmlCas.toString();  
					
					return resp;
		}
	}
