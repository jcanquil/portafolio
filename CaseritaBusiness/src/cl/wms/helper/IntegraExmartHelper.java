package cl.caserita.wms.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.EmaapmDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.enviomail.main.EnvioMailInformeIntegridad;
import cl.caserita.enviomail.main.envioMailIntegracionWMS;
import cl.caserita.ftp.EnviaFTPWMS;
import cl.caserita.wms.comun.ConvierteStringDTO;

public class IntegraExmartHelper {
	private static FileWriter fileWriterLog;
	private  static Logger logi = Logger.getLogger(IntegraExmartHelper.class);

	public static void main (String[]args){
		IntegraExmartHelper helper = new IntegraExmartHelper();
		//helper.procesaPorArticulo(2907, "A","6");
		//helper.procesaPorArticulo(2805, "A","3");
		//helper.procesaPorArticulo(10897, "A","9");
		helper.procesaPorArticulo(12559, "A,192.168.1.20,PCJAIME,JHCANQUIL","8");
		//helper.procesaExmart(0, "A,192.168.1.20,PCJAIME,JHCANQUIL");
		
	}
	public void procesaExmart(int articulo, String tipoAccion){
		//Lista de Articulos para enviar a WMS
		DAOFactory dao = DAOFactory.getInstance();
		FtpprovDAO ftpDAO = dao.getFtpprovDAO();
		EmaapmDAO emmamDAO = dao.getEmaapmDAO();
		EnviaFTPWMS ftp = new EnviaFTPWMS();
		ExmartDAO exmart = dao.getExmartDAO();
		LogintegracionDAO log = dao.getLogintegracionDAO();
		ConvierteStringDTO convierte = new ConvierteStringDTO();
		IntegracionDTO integraDTO = convierte.convierte(tipoAccion);
		
		List 	 lista = exmart.recuperaArticulos();
		ExmartDTO exmartDTO = null;
		Iterator iter = lista.iterator();
		while (iter.hasNext()){
			exmartDTO = (ExmartDTO) iter.next();
			
				String xml = formaXMLNuevo(exmartDTO, integraDTO.getAccion(),exmartDTO.getCodigos());
				
				try{
					Thread.sleep(1000);

				}catch (Exception e){
					e.printStackTrace();
				}
			

				String xmlEnvio = generatxt(xml, exmartDTO.getCodigoArticulo());
				if (ftp.enviaFTP(xmlEnvio,ftpDAO)){
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getYYYYMMDD());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(exmartDTO.getCodigoArticulo());
						loginDTO.setDv(exmartDTO.getDvArticulo());
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTipoAccion(integraDTO.getAccion());

						loginDTO.setTabla("EXMART");
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
						
						log.generaLogArticulo(loginDTO);
						List listaEMail = emmamDAO.obtieneMailAPP("INTARTI");
						envioMailIntegracionWMS integra = new envioMailIntegracionWMS();
						integra.mail(String.valueOf(exmartDTO.getCodigoArticulo())+"-"+exmartDTO.getDvArticulo(), exmartDTO.getDescripcionArticulo(), integraDTO.getUsuario(), integraDTO.getIpEquipo(), listaEMail);
						
					}
					
				}else{
					generatxt2(xml, exmartDTO.getCodigoArticulo());
					LogintegracionDTO loginDTO = new LogintegracionDTO();
					Fecha fch = new Fecha();
					if (integraDTO.getIpEquipo()!=null){
						int fecha = Integer.parseInt(fch.getDDMMYYYY());
						int hora = Integer.parseInt(fch.getHHMMSS());
						loginDTO.setCod(exmartDTO.getCodigoArticulo());
						loginDTO.setDv(exmartDTO.getDvArticulo());
						loginDTO.setFechaArchivo(fecha);
						loginDTO.setHoraArchivo(hora);
						loginDTO.setTabla("EXMART");
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
						loginDTO.setTipoAccion(integraDTO.getAccion());

						loginDTO.setUsuario(integraDTO.getUsuario().trim());
						loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
						loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
						loginDTO.setTipoEnvio("E");
						loginDTO.setEstadoEnvio(1);
						
						log.generaLogArticulo(loginDTO);
					}
				}
			
			
			
		}
	}
	
	public void procesaPorArticulo(int codArticulo, String tipoAccion, String digito){
		//Lista de Articulos para enviar a WMS
				DAOFactory dao = DAOFactory.getInstance();
				EnviaFTPWMS ftp = new EnviaFTPWMS();
				ExmartDAO exmart = dao.getExmartDAO();
				LogintegracionDAO log = dao.getLogintegracionDAO();
				ConvierteStringDTO convierte = new ConvierteStringDTO();
				IntegracionDTO integraDTO = convierte.convierte(tipoAccion);
				FtpprovDAO ftpDAO = dao.getFtpprovDAO();
				EmaapmDAO emmamDAO = dao.getEmaapmDAO();

				logi.info("cod:"+codArticulo);
				logi.info("dv:"+digito);
			
					ExmartDTO exmartDTO = exmart.recuperaArticulo(codArticulo, digito);
					String xml = formaXMLNuevo(exmartDTO, integraDTO.getAccion(), exmartDTO.getCodigos());
					logi.info("xml:"+xml);
					try{
						Thread.sleep(1000);

					}catch (Exception e){
						e.printStackTrace();
					}
					

					String xmlEnvio = generatxt(xml, exmartDTO.getCodigoArticulo());
					if (ftp.enviaFTP(xmlEnvio, ftpDAO)){
						LogintegracionDTO loginDTO = new LogintegracionDTO();
						Fecha fch = new Fecha();
						if (integraDTO.getIpEquipo()!=null){
							int fecha = Integer.parseInt(fch.getYYYYMMDD());
							int hora = Integer.parseInt(fch.getHHMMSS());
							loginDTO.setCod(exmartDTO.getCodigoArticulo());
							loginDTO.setDv(exmartDTO.getDvArticulo());
							loginDTO.setFechaArchivo(fecha);
							loginDTO.setHoraArchivo(hora);
							loginDTO.setTabla("EXMART");
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
							loginDTO.setTipoAccion(integraDTO.getAccion());

							loginDTO.setUsuario(integraDTO.getUsuario().trim());
							loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
							loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
							loginDTO.setTipoEnvio("N");
							loginDTO.setEstadoEnvio(0);
							
							log.generaLogArticulo(loginDTO);
							List listaEMail = emmamDAO.obtieneMailAPP("INTARTI");
							envioMailIntegracionWMS integra = new envioMailIntegracionWMS();
							integra.mail(String.valueOf(exmartDTO.getCodigoArticulo())+"-"+exmartDTO.getDvArticulo(), exmartDTO.getDescripcionArticulo(), integraDTO.getUsuario(), integraDTO.getIpEquipo(), listaEMail);
							
						}
						
					}else{
						generatxt2(xml, exmartDTO.getCodigoArticulo());
						LogintegracionDTO loginDTO = new LogintegracionDTO();
						Fecha fch = new Fecha();
						if (integraDTO.getIpEquipo()!=null){
							int fecha = Integer.parseInt(fch.getDDMMYYYY());
							int hora = Integer.parseInt(fch.getHHMMSS());
							loginDTO.setCod(exmartDTO.getCodigoArticulo());
							loginDTO.setDv(exmartDTO.getDvArticulo());
							loginDTO.setFechaArchivo(fecha);
							loginDTO.setHoraArchivo(hora);
							loginDTO.setTabla("EXMART");
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
							loginDTO.setTipoAccion(integraDTO.getAccion());

							loginDTO.setUsuario(integraDTO.getUsuario().trim());
							loginDTO.setIpEquipo(integraDTO.getIpEquipo().trim());
							loginDTO.setNombreEquipo(integraDTO.getNombreEquipo().trim());
							loginDTO.setTipoEnvio("E");
							loginDTO.setEstadoEnvio(1);
							
							log.generaLogArticulo(loginDTO);
						}
					}
	}
	
	
	public String generatxt(String XML, int codigoArticulo){
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
		String nombreArchivo = "articulos_"+codigoArticulo+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"articulos_"+codigoArticulo+"_"+fechaStr+".xml";
		String archivoLog2="/home/stgin/"+"articulos_"+codigoArticulo+"_"+fechaStr+".xml";
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
		//copyFile(archivoLog, archivoLog2);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return nombreArchivo+"|"+archivoLog;
	}
	
	public String generatxt2(String XML, int codigoArticulo){
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
		String nombreArchivo = "articulos_"+codigoArticulo+"_"+fechaStr+".xml";
		String archivoLog=carpeta+"articulos_"+codigoArticulo+"_"+fechaStr+".xml";
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
	
	public boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        logi.info("ruta1:"+fromFile);
        logi.info("ruta2:"+toFile);

        if (origin.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
	
	
	public String formaXMLNuevo(ExmartDTO dto, String tipoAccion, HashMap<String,ExdartDTO> lista){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		 
		  xmlCas.append("<VC_PART_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>PART_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>----</WHSE_ID>");
		  
		  xmlCas.append("<PART_SEG>");
		  xmlCas.append("<SEGNAM>PART</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<PRTNUM>"+dto.getCodigoArticulo()+"</PRTNUM>");
		  xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
		  xmlCas.append("<PRTFAM>"+dto.getCodigoGrupo()+"</PRTFAM>");
		  xmlCas.append("<LODVLV>S</LODVLV>");
		  xmlCas.append("<LOTFLG>0</LOTFLG>");
		  xmlCas.append("<STKUOM>UN</STKUOM>");
		  xmlCas.append("<ABCCOD>C</ABCCOD>");
		  xmlCas.append("<VELZON>B</VELZON>");
		  xmlCas.append("<RCVSTS>D</RCVSTS>");
		  
		  xmlCas.append("<PRDFLG>1</PRDFLG>");
		  xmlCas.append("<DTE_CODE></DTE_CODE>");
		  xmlCas.append("<AGE_PFLNAM></AGE_PFLNAM>");
		  xmlCas.append("<CATCH_COD></CATCH_COD>");
		  xmlCas.append("<CATCH_UNTTYP></CATCH_UNTTYP>");
		  xmlCas.append("<MIN_CATCH_QTY></MIN_CATCH_QTY>");
		  xmlCas.append("<MAX_CATCH_QTY></MAX_CATCH_QTY>");
		  xmlCas.append("<CNZFLG>0</CNZFLG>");
		  xmlCas.append("<CNZAMT>0</CNZAMT>");
		  xmlCas.append("<WGTCOD></WGTCOD>");
		  xmlCas.append("<PRTADJFLG>1</PRTADJFLG>");
		  
		  xmlCas.append("<CNTBCK_ENA_FLG>0</CNTBCK_ENA_FLG>");
		  xmlCas.append("<CRNCY_CODE></CRNCY_CODE>");
		  xmlCas.append("<BOX_PCK_FLG>0</BOX_PCK_FLG>");
		  xmlCas.append("<AVG_CATCH>0</AVG_CATCH>");
		  
		  xmlCas.append("<DSPUOM>UN</DSPUOM>");
		  xmlCas.append("<IGNORE_CON_FLG></IGNORE_CON_FLG>");
		  xmlCas.append("<THRESH_PCK_VAR></THRESH_PCK_VAR>");
		  xmlCas.append("<PARCEL_FLG></PARCEL_FLG>");
		  xmlCas.append("<SCFPOS>0</SCFPOS>");
		  xmlCas.append("<DCFPOS>0</DCFPOS>");
		  xmlCas.append("<VELZON_RECALC_FLG>1</VELZON_RECALC_FLG>");
		  xmlCas.append("<TIME_TO_WARN_FOR_EXP>"+dto.getVidaUtil()+"</TIME_TO_WARN_FOR_EXP>");

		  xmlCas.append("<PART_DESCRIPTION_SEG>");
			  xmlCas.append("<SEGNAM>PART_DESC</SEGNAM>");
			  xmlCas.append("<LNGDSC>"+dto.getDescripcionArticulo().trim()+"</LNGDSC>");
			  xmlCas.append("<SHORT_DSC>"+dto.getDescripcionCortaArticulo().trim()+"</SHORT_DSC>");
			  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		  xmlCas.append("</PART_DESCRIPTION_SEG>");
		  int cantidadBP=0;
		  

				  ExdartDTO dto2 = (ExdartDTO) lista.get("U");
				  if (dto2!=null){
					  cantidadBP=(int)dto2.getCantBasePallets();
					  dto.setPallet((int)dto2.getCantTotalPallets());
				  }

		
				  ExdartDTO cajaDTO = (ExdartDTO) lista.get("C");
				  if (cajaDTO==null){
					  cantidadBP=1;
				  }

		 
			  xmlCas.append("<PART_FOOTPRINT_SEG>");
			  xmlCas.append("<SEGNAM>PART_FOOTPRINT</SEGNAM>");//A definir codigo
			  xmlCas.append("<FTPCOD>"+dto.getCodigoArticulo()+"</FTPCOD>");
			  xmlCas.append("<LNGDSC>"+dto.getDescripcionArticulo().trim()+"</LNGDSC>");
			  xmlCas.append("<SHORT_DSC>"+dto.getDescripcionCortaArticulo().trim()+"</SHORT_DSC>");
			 // xmlCas.append("<CASLVL>"+exdart.getCantBasePallets()+"</CASLVL>");
			  xmlCas.append("<CASLVL>"+cantidadBP+"</CASLVL>");
			  xmlCas.append("<PAL_STCK_HGT></PAL_STCK_HGT>");
			  xmlCas.append("<DEF_ASSET_TYP></DEF_ASSET_TYP>");
			  xmlCas.append("<DEFFTP_FLG>0</DEFFTP_FLG>");
			  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			  
			  int conta=0;
			  String tipo="U";
			  int uoml=1;
		  		int unidades=0;

			  while (conta<4){
				  ExdartDTO exdart = buscaExdart(tipo.trim(), lista);
				  if (exdart!=null){
					  xmlCas.append("<PART_FOOTPRINT_DTL_SEG>");
				  		xmlCas.append("<SEGNAM>PART_FOOTPRINT_DTL</SEGNAM>");
				  		String contenedor="U";
				  		int caja=0;
				  		int display=0;
				  		int uni=0;
				  		int pallet=0;
				  		int bul=0;
						  if ("P".equals(exdart.getTipoContenedor())){
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
						  }
						  
				  		xmlCas.append("<UOMCOD>"+contenedor+"</UOMCOD>");
				  		xmlCas.append("<UOMLVL>"+uoml+"</UOMLVL>");//se envia valor referente al tipo de contenedor
				  		xmlCas.append("<LEN>"+exdart.getLargo()+"</LEN>");
				  		xmlCas.append("<WID>"+exdart.getAncho()+"</WID>");
				  		xmlCas.append("<HGT>"+exdart.getAlto()+"</HGT>");
				  		xmlCas.append("<GRSWGT>"+exdart.getPeso()+"</GRSWGT>");
				  		xmlCas.append("<NETWGT>0.000000</NETWGT>");
				  		
				  		/*if (dto.getCaja()>0){
				  			caja=1;
				  		}
				  		if (dto.getDisplay()>0){
				  			display=1;
				  		}
				  		if (dto.getUnidades()>0){
				  			uni=1;
				  		}*/
				  		int rcv=0;
				  		/*if (caja>0){
				  			rcv=1;
				  		}*/
				  		/*if (cajaDTO==null){
				  			rcv=1;
				  		}*/
				  		if ("P".equals(exdart.getTipoContenedor())){
				  			rcv=1;
				  		}
				  		xmlCas.append("<PAL_FLG>"+pallet+"</PAL_FLG>");//por definir
				  		xmlCas.append("<CAS_FLG>"+caja+"</CAS_FLG>");//por definir
				  		xmlCas.append("<PAK_FLG>"+display+"</PAK_FLG>");//por definir
				  		xmlCas.append("<STK_FLG>"+uni+"</STK_FLG>");//por definir
				  		xmlCas.append("<RCV_FLG>"+rcv+"</RCV_FLG>");//por definir
				  		if ("CJ".equals(contenedor)){
				  			//unidades=unidades*dto.getCaja();
				  			unidades=dto.getCaja();
				  		}
				  		if ("CJ".equals(contenedor) && dto.getDisplay()>0){
				  			unidades=dto.getCaja()*dto.getDisplay();

				  		}
				  		xmlCas.append("<UNTQTY>"+unidades+"</UNTQTY>");
				  		//xmlCas.append("<UNTQTY>"+dto.getUnidades()+"</UNTQTY>");////por definir calculo que debe ir desde ERP
				  		xmlCas.append("<LEN_MU></LEN_MU>");
				  		xmlCas.append("<WID_MU></WID_MU>");
				  		xmlCas.append("<HGT_MU></HGT_MU>");
				  		xmlCas.append("<NETWGT_MU></NETWGT_MU>");
				  		xmlCas.append("<CTN_FLG>1</CTN_FLG>");
				  		xmlCas.append("<THRESH_PCT>100</THRESH_PCT>");
				  		xmlCas.append("<CTN_DSTR_FLG>0</CTN_DSTR_FLG>");
				  		xmlCas.append("<BULK_PCK_FLG>"+bul+"</BULK_PCK_FLG>");

				  	xmlCas.append("</PART_FOOTPRINT_DTL_SEG>");
				  	uoml++;
				  } else if (("P").equals(tipo) && dto.getPallet()>0){
					   exdart = buscaExdart("C", lista);
					   if (exdart==null){
						   break;
					   }
					  xmlCas.append("<PART_FOOTPRINT_DTL_SEG>");
				  		xmlCas.append("<SEGNAM>PART_FOOTPRINT_DTL</SEGNAM>");
				  		String contenedor="P";
				  		int caja=0;
				  		int display=0;
				  		int uni=0;
				  		int pallet=0;
						  if ("P".equals(tipo)){
							  contenedor="PA";
							  //uoml=4;
							  pallet=1;
							  if (dto.getDisplay()>0){
								  unidades=dto.getPallet()*dto.getCaja()*dto.getDisplay();

							  }else{
								  unidades=dto.getPallet()*dto.getCaja();

							  }
							 // unidades=dto.getPallet();
						  }
						  
				  		xmlCas.append("<UOMCOD>"+contenedor+"</UOMCOD>");
				  		xmlCas.append("<UOMLVL>"+uoml+"</UOMLVL>");//se envia valor referente al tipo de contenedor
				  		xmlCas.append("<LEN>1.00</LEN>");
				  		xmlCas.append("<WID>1.20</WID>");
				  		xmlCas.append("<HGT>"+exdart.getCantAltoPallets()*exdart.getAlto()+"</HGT>");//Cajas de alto
				  		xmlCas.append("<GRSWGT>"+exdart.getPeso()*dto.getPallet()+20+"</GRSWGT>");//Pallet contiene
				  		xmlCas.append("<NETWGT>0.000000</NETWGT>");
				  		
				  		/*if (dto.getCaja()>0){
				  			caja=1;
				  		}
				  		if (dto.getDisplay()>0){
				  			display=1;
				  		}
				  		if (dto.getUnidades()>0){
				  			uni=1;
				  		}*/
				  		int rcv=0;
				  		if (caja>0){
				  			rcv=1;
				  		}
				  		xmlCas.append("<PAL_FLG>"+pallet+"</PAL_FLG>");//por definir
				  		xmlCas.append("<CAS_FLG>"+caja+"</CAS_FLG>");//por definir
				  		xmlCas.append("<PAK_FLG>"+display+"</PAK_FLG>");//por definir
				  		xmlCas.append("<STK_FLG>"+uni+"</STK_FLG>");//por definir
				  		//xmlCas.append("<RCV_FLG>"+rcv+"</RCV_FLG>");//por definir
				  		xmlCas.append("<RCV_FLG>1</RCV_FLG>");//Se corrige por peticion de STG a 1 siempre en PA
				  			if (dto.getDisplay()==0){
					  			unidades=dto.getCaja()*dto.getPallet();

				  			}else {
					  			unidades=dto.getCaja()*dto.getPallet()*dto.getDisplay();
				  			}
				  		
				  		xmlCas.append("<UNTQTY>"+unidades+"</UNTQTY>");
				  		//xmlCas.append("<UNTQTY>"+dto.getUnidades()+"</UNTQTY>");////por definir calculo que debe ir desde ERP
				  		xmlCas.append("<LEN_MU></LEN_MU>");
				  		xmlCas.append("<WID_MU></WID_MU>");
				  		xmlCas.append("<HGT_MU></HGT_MU>");
				  		xmlCas.append("<NETWGT_MU></NETWGT_MU>");
				  		xmlCas.append("<CTN_FLG>1</CTN_FLG>");
				  		xmlCas.append("<THRESH_PCT>100</THRESH_PCT>");
				  		xmlCas.append("<CTN_DSTR_FLG>0</CTN_DSTR_FLG>");
				  		xmlCas.append("<BULK_PCK_FLG>0</BULK_PCK_FLG>");

				  	xmlCas.append("</PART_FOOTPRINT_DTL_SEG>");
				  }
				  
			  	conta++;
			  	if (conta==1){
			  		tipo="D";
			  	}else if (conta==2){
			  		tipo="C";
			  	}else if (conta==3){
			  		tipo="P";
			  	}
			  }
			  
			  
			  
			  
			  xmlCas.append("</PART_FOOTPRINT_SEG>"); 
			  conta=0;
			  tipo="U";
			  int cnt=0;

			  while (conta<4){
				  ExdartDTO exdart = buscaExdart(tipo.trim(), lista);
				  String articulo = String.valueOf(dto.getCodigoArticulo()).trim();
				  if (exdart!=null){
					  if (exdart.getCodigoBarra().compareTo(articulo)!=0){
						  String contenedor="U";
						  if ("P".equals(exdart.getTipoContenedor())){
							  contenedor="PA";
						  }else if ("U".equals(exdart.getTipoContenedor())){
							  contenedor="UN";
							   cnt=1;

						  }else if ("D".equals(exdart.getTipoContenedor())){
							  contenedor="DP";
						  }else if ("C".equals(exdart.getTipoContenedor())){
							  contenedor="CJ";
						  }
						  xmlCas.append("<PART_ALT_PRTNUM_SEG>");  
							xmlCas.append("<SEGNAM>ALT_PRTNUM</SEGNAM>");  
							xmlCas.append("<ALT_PRT_TYP>GTIN</ALT_PRT_TYP>");  
							xmlCas.append("<ALT_PRTNUM>"+exdart.getCodigoBarra()+"</ALT_PRTNUM>");  
							xmlCas.append("<UOMCOD>"+contenedor+"</UOMCOD>");  
							xmlCas.append("<DSP_PRT_FLG>"+cnt+"</DSP_PRT_FLG>");  
						  xmlCas.append("</PART_ALT_PRTNUM_SEG>");  
						  cnt=0;
					  }
					  
				  }
				  
				  conta++;
				  	if (conta==1){
				  		tipo="D";
				  	}else if (conta==2){
				  		tipo="C";
				  	}else if (conta==3){
				  		tipo="P";
				  	}
			  
			  }
			  xmlCas.append("</PART_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</VC_PART_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}
	
	
	/*public String formaXML(ExmartDTO dto, String tipoAccion, List lista){
		//Forma XML para enviar a WMS
		String resp ="";
		StringBuffer xmlCas = new StringBuffer();
		  String xml="";
		  
		  xmlCas.append("<PART_INB_IFD>");
		  xmlCas.append("<CTRL_SEG>");
		  xmlCas.append("<TRNNAM>PART_TRAN</TRNNAM>");
		  xmlCas.append("<TRNVER>2011.1</TRNVER>");
		  xmlCas.append("<WHSE_ID>----</WHSE_ID>");
		  
		  xmlCas.append("<PART_SEG>");
		  xmlCas.append("<SEGNAM>PART</SEGNAM>");
		  xmlCas.append("<TRNTYP>"+tipoAccion.trim()+"</TRNTYP>");
		  xmlCas.append("<PRTNUM>"+dto.getCodigoArticulo()+"</PRTNUM>");
		  xmlCas.append("<PRT_CLIENT_ID>----</PRT_CLIENT_ID>");
		  xmlCas.append("<PRTFAM>"+dto.getCodigoGrupo()+"</PRTFAM>");
		  xmlCas.append("<LODVLV>S</LODVLV>");
		  xmlCas.append("<LOTFLG>0</LOTFLG>");
		  xmlCas.append("<STKUOM>UN</STKUOM>");
		  xmlCas.append("<ABCCOD>C</ABCCOD>");
		  xmlCas.append("<VELZON>B</VELZON>");
		 // xmlCas.append("<RCVSTS>R</RCVSTS>");//MODIFICAR POR PETICION DE STG
		  xmlCas.append("<RCVSTS>D</RCVSTS>");
		  xmlCas.append("<PRDFLG>0</PRDFLG>");
		  xmlCas.append("<DTE_CODE></DTE_CODE>");
		  xmlCas.append("<AGE_PFLNAM></AGE_PFLNAM>");
		  xmlCas.append("<CATCH_COD></CATCH_COD>");
		  xmlCas.append("<CATCH_UNTTYP></CATCH_UNTTYP>");
		  xmlCas.append("<MIN_CATCH_QTY></MIN_CATCH_QTY>");
		  xmlCas.append("<MAX_CATCH_QTY></MAX_CATCH_QTY>");
		  xmlCas.append("<CNZFLG>0</CNZFLG>");
		  xmlCas.append("<CNZAMT>0</CNZAMT>");
		  xmlCas.append("<WGTCOD></WGTCOD>");
		  xmlCas.append("<PRTADJFLG>1</PRTADJFLG>");
		  
		  xmlCas.append("<CNTBCK_ENA_FLG>0</CNTBCK_ENA_FLG>");
		  xmlCas.append("<CRNCY_CODE></CRNCY_CODE>");
		  xmlCas.append("<BOX_PCK_FLG>0</BOX_PCK_FLG>");
		  xmlCas.append("<AVG_CATCH>0</AVG_CATCH>");
		  
		  xmlCas.append("<DSPUOM></DSPUOM>");
		  xmlCas.append("<IGNORE_CON_FLG></IGNORE_CON_FLG>");
		  xmlCas.append("<THRESH_PCK_VAR></THRESH_PCK_VAR>");
		  xmlCas.append("<PARCEL_FLG></PARCEL_FLG>");
		  xmlCas.append("<SCFPOS>0</SCFPOS>");
		  xmlCas.append("<DCFPOS>0</DCFPOS>");
		  xmlCas.append("<VELZON_RECALC_FLG>1</VELZON_RECALC_FLG>");
		  xmlCas.append("<TIME_TO_WARN_FOR_EXP>"+dto.getVidaUtil()+"</TIME_TO_WARN_FOR_EXP>");
		  xmlCas.append("<PART_DESCRIPTION_SEG>");
			  xmlCas.append("<SEGNAM>PART_DESC</SEGNAM>");
			  xmlCas.append("<LNGDSC>"+dto.getDescripcionArticulo().trim()+"</LNGDSC>");
			  xmlCas.append("<SHORT_DSC>"+dto.getDescripcionArticulo().trim()+"</SHORT_DSC>");
			  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
		  xmlCas.append("</PART_DESCRIPTION_SEG>");
		  double cantidadBP=0;
		  Iterator iter3 = lista.iterator();
		  while (iter3.hasNext()){
			  ExdartDTO exdart = (ExdartDTO) iter3.next();
			  if ("P".equals(exdart.getTipoContenedor())){
				  cantidadBP=exdart.getCantAltoPallets();
			  }
		  }

		 
			  xmlCas.append("<PART_FOOTPRINT_SEG>");
			  xmlCas.append("<SEGNAM>PART_FOOTPRINT</SEGNAM>");//A definir codigo
			  xmlCas.append("<FTPCOD>"+dto.getCodigoArticulo()+"</FTPCOD>");
			  xmlCas.append("<LNGDSC>"+dto.getDescripcionArticulo().trim()+"</LNGDSC>");
			  xmlCas.append("<SHORT_DSC>"+dto.getDescripcionCortaArticulo().trim()+"</SHORT_DSC>");
			 // xmlCas.append("<CASLVL>"+exdart.getCantBasePallets()+"</CASLVL>");
			  xmlCas.append("<CASLVL>"+cantidadBP+"</CASLVL>");
			  xmlCas.append("<PAL_STCK_HGT></PAL_STCK_HGT>");
			  xmlCas.append("<DEF_ASSET_TYP></DEF_ASSET_TYP>");
			  xmlCas.append("<DEFFTP_FLG>0</DEFFTP_FLG>");
			  xmlCas.append("<LOCALE_ID>US_ENGLISH</LOCALE_ID>");
			  Iterator iter = lista.iterator();
			  while (iter.hasNext()){
				  ExdartDTO exdart = (ExdartDTO) iter.next();
				  xmlCas.append("<PART_FOOTPRINT_DTL_SEG>");
			  		xmlCas.append("<SEGNAM>PART_FOOTPRINT_DTL</SEGNAM>");
			  		String contenedor="U";
					  if ("P".equals(exdart.getTipoContenedor())){
						  contenedor="PA";
					  }else if ("U".equals(exdart.getTipoContenedor())){
						  contenedor="UN";
					  }else if ("D".equals(exdart.getTipoContenedor())){
						  contenedor="DP";
					  }else if ("C".equals(exdart.getTipoContenedor())){
						  contenedor="CJ";
					  }
					  
			  		xmlCas.append("<UOMCOD>"+contenedor+"</UOMCOD>");
			  		xmlCas.append("<UOMLVL>1</UOMLVL>");//se envia valor referente al tipo de contenedor
			  		xmlCas.append("<LEN>"+exdart.getLargo()+"</LEN>");
			  		xmlCas.append("<WID>"+exdart.getAncho()+"</WID>");
			  		xmlCas.append("<HGT>"+exdart.getAlto()+"</HGT>");
			  		xmlCas.append("<GRSWGT>"+exdart.getPeso()+"</GRSWGT>");
			  		xmlCas.append("<NETWGT>0.000000</NETWGT>");
			  		int caja=0;
			  		int display=0;
			  		int uni=0;
			  		int pallet=1;
			  		if (dto.getCaja()>0){
			  			caja=1;
			  		}
			  		if (dto.getDisplay()>0){
			  			display=1;
			  		}
			  		if (dto.getUnidades()>0){
			  			uni=1;
			  		}
			  		xmlCas.append("<PAL_FLG>"+pallet+"</PAL_FLG>");//por definir
			  		xmlCas.append("<CAS_FLG>"+caja+"</CAS_FLG>");//por definir
			  		xmlCas.append("<PAK_FLG>"+display+"</PAK_FLG>");//por definir
			  		xmlCas.append("<STK_FLG>"+uni+"</STK_FLG>");//por definir
			  		xmlCas.append("<RCV_FLG>"+caja+"</RCV_FLG>");//por definir
			  		xmlCas.append("<UNTQTY>"+dto.getUnidades()+"</UNTQTY>");////por definir calculo que debe ir desde ERP
			  		xmlCas.append("<LEN_MU></LEN_MU>");
			  		xmlCas.append("<WID_MU></WID_MU>");
			  		xmlCas.append("<HGT_MU></HGT_MU>");
			  		xmlCas.append("<NETWGT_MU></NETWGT_MU>");
			  		xmlCas.append("<CTN_FLG>1</CTN_FLG>");
			  		xmlCas.append("<THRESH_PCT>100</THRESH_PCT>");
			  		xmlCas.append("<CTN_DSTR_FLG>0</CTN_DSTR_FLG>");
			  		xmlCas.append("<BULK_PCK_FLG>1</BULK_PCK_FLG>");

			  		xmlCas.append("<THRESH_PCT>0</THRESH_PCT>");
			  	xmlCas.append("</PART_FOOTPRINT_DTL_SEG>");
			  }
			  
			  
			  
			  
			  xmlCas.append("</PART_FOOTPRINT_SEG>"); 
			  int cnt=0;
			  Iterator iter2 = lista.iterator();
			  while (iter2.hasNext()){
				  ExdartDTO exdart = (ExdartDTO) iter2.next();
				  String contenedor="U";
				 
				  if ("P".equals(exdart.getTipoContenedor())){
					  contenedor="PA";
				  }else if ("U".equals(exdart.getTipoContenedor())){
					  contenedor="UN";
					  cnt=1;
				  }else if ("D".equals(exdart.getTipoContenedor())){
					  contenedor="DP";
				  }else if ("C".equals(exdart.getTipoContenedor())){
					  contenedor="CJ";
				  }
				  xmlCas.append("<PART_ALT_PRTNUM_SEG>");  
					xmlCas.append("<SEGNAM>ALT_PRTNUM</SEGNAM>");  
					xmlCas.append("<ALT_PRT_TYP>EAN</ALT_PRT_TYP>");  
					xmlCas.append("<ALT_PRTNUM>"+exdart.getCodigoBarra()+"</ALT_PRTNUM>");  
					xmlCas.append("<UOMCOD>"+contenedor+"</UOMCOD>");  
					xmlCas.append("<DSP_PRT_FLG>"+cnt+"</DSP_PRT_FLG>");  
				  xmlCas.append("</PART_ALT_PRTNUM_SEG>");  
				  cnt=0;
			  }
		  	

			
			
			
			xmlCas.append("</PART_SEG>");  
			xmlCas.append("</CTRL_SEG>");  
			xmlCas.append("</PART_INB_IFD>");  
			resp = xmlCas.toString();
				  
		  
		  
		return resp;
	}*/
	
	
	public ExdartDTO buscaExdart(String tipo, HashMap<String, ExdartDTO> lista){
		  ExdartDTO dto2 = (ExdartDTO) lista.get(tipo);

		return dto2;
	}
	
}
