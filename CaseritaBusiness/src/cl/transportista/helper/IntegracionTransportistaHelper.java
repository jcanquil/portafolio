package cl.caserita.transportista.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import javax.swing.plaf.synth.SynthScrollBarUI;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONValue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.comunes.fecha.FechaException;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.AdsusrDAO;
import cl.caserita.dao.iface.BBintc00DAO;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dao.iface.CargcestDAO;
import cl.caserita.dao.iface.CargconwDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.CarguiodDAO;
import cl.caserita.dao.iface.ChoftranDAO;
import cl.caserita.dao.iface.ClcmcoDAO;
import cl.caserita.dao.iface.CldmcoDAO;
import cl.caserita.dao.iface.ConnodDAO;
import cl.caserita.dao.iface.ConnohDAO;
import cl.caserita.dao.iface.ConnoiDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.DocncpDAO;
import cl.caserita.dao.iface.ErrorTransportistaDAO;
import cl.caserita.dao.iface.ExdacpDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.NcplogDAO;
import cl.caserita.dao.iface.OrdvddeDAO;
import cl.caserita.dao.iface.OrdvdetDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.RespquadDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.TptDocDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dao.impl.VedmarDAOImpl;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.CargcestDTO;
import cl.caserita.dto.CargconwDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguioTranspDTO;
import cl.caserita.dto.CarguiodTranspDTO;
import cl.caserita.dto.ChoftranDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.ConnodDTO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.DetordTranspDTO;
import cl.caserita.dto.DetordTranspNcpDTO;
import cl.caserita.dto.DocncpDTO;
import cl.caserita.dto.ExdacpDTO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.NcplogDTO;
import cl.caserita.dto.OrdTranspDTO;
import cl.caserita.dto.OrdTranspNcpDTO;
import cl.caserita.dto.OrdvddeDTO;
import cl.caserita.dto.OrdvdetDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.RutservDTO;
import cl.caserita.dto.RespquadDTO;
import cl.caserita.dto.TpacorDTO;
import cl.caserita.dto.VedmarDTO;
import cl.caserita.enviomail.main.EnvioMailErrorFacturacion;
import cl.caserita.enviomail.main.EnvioMailErrorTransportista;
import cl.caserita.enviomail.main.EnvioMailInformeIntegridad;
import cl.caserita.enviomail.main.EnvioMailTransportista;
import cl.caserita.enviomail.main.EnvioMailTransportistaInicio;
import cl.caserita.enviomail.main.EnvioMailTransportistaVendedores;
import cl.caserita.enviomail.main.envioMailCaserita;
import cl.caserita.mail.dto.EmailDTO;
import cl.caserita.password.ObtieneClave;
import cl.caserita.transportista.dto.ConvierteTranspDTO;
//import cl.caserita.transportista.dto.DocumentoTranspDTO;
import cl.caserita.wms.comun.ConvierteStringDTO;
import cl.caserita.wms.helper.IntegraCarguioHelper;
import cl.caserita.wms.out.helper.IntegracionCambEstPaperless;
import sun.security.action.GetBooleanAction;

public class IntegracionTransportistaHelper {
	private static FileWriter fileWriterLog;
	private static Logger logi = Logger.getLogger(IntegracionTransportistaHelper.class);

	public String procesa(String par000){
		
		logi.info("\r\n");
		logi.info("________________________________________________________________________________");
		logi.info("I N I C I O    P R O Y E C T O    Q U A D M I N D S  \nParametro Entrada "+par000);
		
		String 	resp="";
		
		DAOFactory 	dao = DAOFactory.getInstance();
		
		CarguioDAO 	carguio = dao.getCarguioDAO();
		ChoftranDAO choftrandao = dao.getChoftranDAO();
		BBintc00DAO bbintdao  = dao.getBBintc00DAO();
		TptDocDAO 	tptdocdao = dao.getTptdocDAO();
		CldmcoDAO 	cldmcodao = dao.getCldmcoDAO();
		
		int 	rut=0;
		String 	dv="";
		String 	solicitud="";
		int		numerodeCarguio=0;
		int		numerodeDocumento=0;
		int		codError=0;
		int		diasSumar=90;
		int 	numeritodecarguio=0;
		String	correoVendedor="";
		boolean	esventaBB=true;
		int		entrawhy=0;
		
		//constantes, empresa, bodega, usuario
		int		codigoBodega=26;
		String	userId="CONTROLENT";
		String  estadocar="E";
		int		codigoEmpresa=2;
		String	versionpipe="";
		String	correoDesarrollo="desarrollo@caserita.cl";
		String	nombredeChofer="";
		int		codigoVendedor=0;
		String	nombreVendedor="";
		
		
		Gson gson = new Gson();
		try{
			JSONObject object = (JSONObject) new JSONParser().parse(par000);
			 
			 if (object.get("solicitud")!=null){
				 solicitud = object.get("solicitud").toString().replaceAll("\"","");
			 }
			 
			 if (("".equals(solicitud.trim()))) {
				 codError=1000;
			 } 
			 
			 
		if (codError<=0) {
			 
			   if ("1".equals(solicitud)){
				   	
				     if (("null".equals(object.get("rutChofer")))){
		    			codError=1001;
		    		 }
		    		 if (("null".equals(object.get("dvChofer")))){
		    			//codError=1002;
		    		 }
		    		 
		    		 if (codError<=0) {
		    			 
			    		if (object.get("rutChofer")==null){
				    		codError=1001;
				    	}
				    	if (object.get("dvChofer")==null){
				    		//codError=1002;
				    	}
			    		
			    		 if (codError<=0) {
							 
			    			 if (("".equals(object.get("rutChofer").toString().replaceAll("\"","").trim()))){
			    				 codError=1001;
			    			 }
			    			 if (("".equals(object.get("dvChofer").toString().replaceAll("\"","").trim()))){
			    				 //codError=1002;
			    			 }
						 
			    			 if (codError<=0) {
								 if (object.get("rutChofer")!=null){
									 rut = Integer.parseInt(object.get("rutChofer").toString().replaceAll("\"","").trim());
								 }
								 if (object.get("dvChofer")!=null){
									 dv = object.get("dvChofer").toString().replaceAll("\"","").trim();
								 }
								 if (object.get("solicitud")!=null){
									 solicitud = object.get("solicitud").toString().replaceAll("\"","").trim();
								 }
								 if (object.get("numcarguio")!=null){
									 numeritodecarguio = Integer.parseInt(object.get("numcarguio").toString().replaceAll("\"","").trim());
								 }
								 
			    			 }
			    		 }
					 }
					 
					 
			   }else if ("2".equals(solicitud)){
			
					 CarguioTranspDTO carguio2 = null;
					 JSONArray lang= (JSONArray) object.get("carguio");
					    if (lang!=null){
					    	 Iterator i = lang.iterator();
							    int contadoCarguio=0;
							    while (i.hasNext()){
							    	JSONObject object22 = (JSONObject) i.next();
							    	if (contadoCarguio<lang.size()){   //("Largo Arreglo:"+lang.size());
							    		String listaOrdnees = lang.get(contadoCarguio).toString();
								    	
							    		carguio2 = new CarguioTranspDTO();
							    		
							    		if (("null".equals(object22.get("rutChofer")))){
							    			codError=1001; break;
							    		}
							    		if (("null".equals(object22.get("dvChofer")))){
							    			//codError=1002; break;
							    		}
							    		if (object22.get("rutChofer")==null){
								    		codError=1001; break;
								    	}
								    	if (object22.get("dvChofer")==null){
								    		//codError=1002; break;
								    	}
							    		
							    		if (("".equals(object22.get("rutChofer").toString().replaceAll("\"","").trim()))){
											 codError=1001; break;
										 }
										 if (("".equals(object22.get("dvChofer").toString().replaceAll("\"","").trim()))){
											 //codError=1002; break;
										 }
										 
										if (object22.get("rutChofer")!=null){
							    			rut = Integer.parseInt(object22.get("rutChofer").toString().replaceAll("\"", "").trim());
							    		} 
							    									    		
							    		if (object22.get("dvChofer")!=null){
							    			dv  = object22.get("dvChofer").toString().replaceAll("\"", "").trim();
							    		}
							    		
							    		if (object22.get("version")!=null){
							    			versionpipe = object22.get("version").toString().replaceAll("\"", "").trim();
							    		}
							    	}
							    }
					    }
					    
			   }else if ("3".equals(solicitud)){
				   
				   CarguioTranspDTO carguio2 = null;
					 JSONArray lang= (JSONArray) object.get("carguio");
					    if (lang!=null){
					    	 Iterator i = lang.iterator();
							    int contadoCarguio=0;
							    while (i.hasNext()){
							    	JSONObject object22 = (JSONObject) i.next();
							    	if (contadoCarguio<lang.size()){ //("Largo Arreglo:"+lang.size());
							    		String listaOrdnees = lang.get(contadoCarguio).toString();
								    	
							    		carguio2 = new CarguioTranspDTO();
							    		
							    		if (("null".equals(object22.get("rutChofer")))){
							    			codError=1001; break;
							    		}
							    		if (("null".equals(object22.get("dvChofer")))){
							    			//codError=1002; break;
							    		}
							    		if (object22.get("rutChofer")==null){
							    			codError=1001; break;
							    		}
							    		if (object22.get("dvChofer")==null){
							    			//codError=1002; break;
							    		}
							    		
							      		if (("".equals(object22.get("rutChofer").toString().replaceAll("\"","").trim()))){
											 codError=1001; break;
										 }
										 if (("".equals(object22.get("dvChofer").toString().replaceAll("\"","").trim()))){
											 //codError=1002; break;
										 }
										 
										 if (object22.get("version")!=null){
								    			versionpipe = object22.get("version").toString().replaceAll("\"", "").trim();
								    	 }
								
								    	rut = Integer.parseInt(object22.get("rutChofer").toString().replaceAll("\"", "").trim());
								    	
								    	if (("".equals(object22.get("dvChofer").toString().replaceAll("\"","").trim()))){
								    		
								    	} else {
								    		dv  = object22.get("dvChofer").toString().replaceAll("\"", "").trim();
								    	}
							    	}
							    }
					    }
					    
					    
					    
				   
				   
				   
			   }else if ("4".equals(solicitud)){
				   
			  		if (("".equals(object.get("rutChofer").toString().replaceAll("\"","").trim()))){
						 codError=1001;
					 }
					 if (("".equals(object.get("dvChofer").toString().replaceAll("\"","").trim()))){
						 //codError=1002;
					 }
					 if (("".equals(object.get("numeroCarguio").toString().replaceAll("\"","").trim()))){
						 codError=1010;
					 }
					 if (("".equals(object.get("numeroDocumento").toString().replaceAll("\"","").trim()))){
						 codError=1004;
					 }
					 if (("".equals(object.get("solicitud").toString().replaceAll("\"","").trim()))){
						 codError=1000;
					 }
			
					 if (codError<=0) {
						 
						 if (object.get("rutChofer")!=null){
							 rut = Integer.parseInt(object.get("rutChofer").toString().replaceAll("\"","").trim());
						 }
						 if (object.get("dvChofer")!=null){
							 dv = object.get("dvChofer").toString().replaceAll("\"","").trim();
						 }
						 if (object.get("numeroCarguio")!=null){
							 numeritodecarguio = Integer.parseInt(object.get("numeroCarguio").toString().replaceAll("\"","").trim());
						 }
						 if (object.get("numeroDocumento")!=null){
							 numerodeDocumento = Integer.parseInt(object.get("numeroDocumento").toString().replaceAll("\"","").trim());
						 }
						 if (object.get("solicitud")!=null){
							 solicitud = object.get("solicitud").toString().replaceAll("\"","").trim();
						 }
						 
						 if (object.get("version")!=null){
				    		versionpipe = object.get("version").toString().replaceAll("\"", "").trim();
				    	 }
						 
					 }
					 
			   
			   
			   }else if ("5".equals(solicitud)){

					 CarguioTranspDTO carguio2 = null;
					 JSONArray lang= (JSONArray) object.get("carguio");
					    if (lang!=null){
					    	 Iterator i = lang.iterator();
							    int contadoCarguio=0;
							    while (i.hasNext()){
							    	JSONObject object22 = (JSONObject) i.next();
							    	if (contadoCarguio<lang.size()){ //("Largo Arreglo:"+lang.size());
							    		String listaOrdnees = lang.get(contadoCarguio).toString();
								    	
							    		carguio2 = new CarguioTranspDTO();
							    		
							    		if (("null".equals(object22.get("rutChofer")))){
							    			codError=1001;break;
							    		 }
							    		 if (("null".equals(object22.get("dvChofer")))){
							    			//codError=1002;break;
							    		 }
							    		 
							    		if (object22.get("rutChofer")==null){
								    		codError=1001; break;
								    	}
								    	if (object22.get("dvChofer")==null){
								    		//codError=1002; break;
								    	} 
							    		
							    		if (("".equals(object22.get("rutChofer").toString().replaceAll("\"","").trim()))){
											 codError=1001; break;
										 }
										 if (("".equals(object22.get("dvChofer").toString().replaceAll("\"","").trim()))){
											 //codError=1002; break;
										 }
										 
							    		if (object22.get("rutChofer")!=null){
							    			rut = Integer.parseInt(object22.get("rutChofer").toString().replaceAll("\"", "").trim());
							    		} 
							    									    		
							    		if (object22.get("dvChofer")!=null){
							    			dv  = object22.get("dvChofer").toString().replaceAll("\"", "").trim();
							    		}
							    		
							    		if (object22.get("version")!=null){
							    			versionpipe  = object22.get("version").toString().replaceAll("\"", "").trim();
							    		}
							    	}
							    }
					    }
				   
			   }
				   			 
		}
			 
		}catch(Exception e){
			e.printStackTrace();
		}
			
			
		
		
		if (codError>0){
			ErrorTransportistaDAO errordao = dao.getErrorTransportistaDAO();
			List listita = errordao.buscaErrorTransportista(numeritodecarguio, codError); 
			String respdd = gson.toJson(listita);
			resp=respdd;
			logi.info("E R R O R : "+resp);		
			
			EmailDTO emaildto = new EmailDTO();
			EnvioMailErrorTransportista enviomail = new EnvioMailErrorTransportista();
			enviomail.mail("", numeritodecarguio, resp, par000);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		 * S  O  L  I  C  I  T  U  D          1
		 */
		 
		 /*
		 ChoftranDTO choftrandto2 = choftrandao.obtenerDigitoRut(rut);
		 dv=choftrandto2.getDvChofer().trim();
   		 */
		/*
		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		String str = "ASYSRCD00 00008   0  01   ";
		int numeritoncp = proce.obtieneCorrelativo(str);
		*/
		
		
		if (rut>0 && "1".equals(solicitud)){
			
			
			if ("".equals(dv)){
				ChoftranDTO choftrandto2 = choftrandao.obtenerDigitoRut(rut);
				dv=choftrandto2.getDvChofer().trim();	
			}
			
			//bodega del carguio
			CarguioDAO carguiodao = dao.getCarguioDAO();
			int codbodega = carguiodao.buscarCarguioTranspBodega(rut,dv,numeritodecarguio);
			if (codbodega>0){
				codigoBodega=codbodega;
			}
			
			
						List completa = carguio.listarCarguiosTransp(rut, dv,numeritodecarguio);
						resp = gson.toJson(completa);
						logi.info("GSON: "+resp);
						generatxt(1, resp, String.valueOf(rut), dv,0,numeritodecarguio);
						
						int existerutydv = carguiodao.buscarCarguioTransp(rut,dv,numeritodecarguio);
						if (existerutydv<=0){
							codError=1011;
							logi.info("R U T   D E   C H O F E R   N O   E X I S T E   !!!");
							
							ErrorTransportistaDAO errordao = dao.getErrorTransportistaDAO();
							List listita = errordao.buscaErrorTransportista(0, codError); 
							String respdd = gson.toJson(listita);
							resp=respdd;
							logi.info("E R R O R : "+resp);
							
							EmailDTO emaildto = new EmailDTO();
							EnvioMailErrorTransportista enviomail = new EnvioMailErrorTransportista();
							enviomail.mail("SOLICITUD 1", numeritodecarguio, resp,par000);
							
						} 
						else {
							
							
							
							
							//Correo a cada uno de los vendedores con sus OVes
							logi.info("ENVIANDO CORREO CON LOS PEDIDOS DE CADA UNO DE LOS VENDEDORES DEL CARGUIO...");
							
							List completacorreos = carguio.listarCarguiosTranspCorreos(rut, dv,numeritodecarguio);
							if (completacorreos.isEmpty() || completacorreos==null){
								
							}else{
								
								String 	mensajito="";
								String 	encabezado="";
								//String 	encabezado1="";
								//String 	encabezado2="";
								String 	detallitos="";
								int		codVendeAux=0;	
								int		codVende=0;
								boolean enviacorreo=false;
								Iterator iter101 = completacorreos.iterator();
								while (iter101.hasNext()) {
									CarguioTranspDTO carguu = (CarguioTranspDTO) iter101.next();
									if (carguu.getRutChofer()>0){
										
										
										encabezado="N° CARGUIO: "+numeritodecarguio+"\t\t"+"CHOFER: "+carguu.getRutChofer()+"-"+carguu.getDvChofer()+"  "+carguu.getNombreChofer()+"\t\t"+"PATENTE: "+carguu.getPatente()+"\r\n\r\n";
										encabezado=encabezado+"N° OV\t\t"+"FECHA OV\t"+"FEC DOCTO\t"+"N° DOCTO\t"+"TIPO DOCTO\t\t\t"+"FORMA PAGO\t"+"CLIENTE\t\t\t\t\t\t"+"MONTO"+"\r\n";
										
										//encabezado1="N° CARGUIO:"+"  "+numeritodecarguio+"       "+"CHOFER:"+"  "+carguu.getRutChofer()+"-"+carguu.getDvChofer()+"       "+carguu.getNombreChofer()+"       "+"PATENTE:"+"  "+carguu.getPatente()+" ";
										//encabezado2="N° OV"+"          "+"FECHA OV"+"          "+"FEC DOCTO"+"          "+"N° DOCTO"+"          "+"TIPO DOCTO"+"          "+"FORMA PAGO"+"          "+"CLIENTE"+"                    "+"MONTO"+"\r\n";
										
										Iterator iter102 = carguu.getOrdenes().iterator();
										while (iter102.hasNext()){
											OrdTranspDTO oveses = (OrdTranspDTO) iter102.next();
											enviacorreo=true;
											codVende=oveses.getCodigoVendedor();
											if (codVendeAux!=codVende && !detallitos.equals("")){
												mensajito="";
												mensajito=encabezado+detallitos;
												//correoVendedor="jaguilera@caserita.cl";
												EnvioMailTransportistaVendedores enviomailven = new EnvioMailTransportistaVendedores();
												enviomailven.mail(mensajito, codVendeAux, nombreVendedor, correoVendedor, numeritodecarguio);
												detallitos="";
											}
											nombreVendedor=oveses.getNombreVendedor();
											correoVendedor=oveses.getCorreoVendedor();
											String ano ="";
											String mes="";
											String dia="";
											String fecPEDIDO=Integer.toString(oveses.getFechaDespacho());
											ano = fecPEDIDO.substring(0, 4);
											mes = fecPEDIDO.substring(4, 6);
											dia = fecPEDIDO.substring(6, 8);
											fecPEDIDO = dia+"/"+mes+"/"+ano;
											
											String fecDOCUME=Integer.toString(oveses.getFechaDocumento());
											ano = fecDOCUME.substring(0, 4);
											mes = fecDOCUME.substring(4, 6);
											dia = fecDOCUME.substring(6, 8);
											fecDOCUME = dia+"/"+mes+"/"+ano;
											
											
											String nombrecliente=oveses.getNombreCliente().trim();
											
											int largonom = nombrecliente.length();
											if (largonom>25){
												nombrecliente=nombrecliente.substring(0, 25).trim();
											}
											String descridocto=oveses.getDescripcionDocumento().trim();
											largonom=descridocto.length();
											if (largonom>13){
												descridocto=descridocto.substring(0, 13).trim();
											}
											
											String textoFormateadonomcli = String.format("%-25s", nombrecliente).replace(' ',' ');        
											String textoFormateadorutcli = String.format("%-8s", oveses.getRutCliente()).replace(' ',' ');
											String textoFormateadomonto = String.format("%07d", oveses.getMonto());
											
											detallitos=detallitos+oveses.getNumeroOV()+"\t"+fecPEDIDO+"\t"+fecDOCUME+" "+"\t"+oveses.getNumeroDocumento()+" "+"\t"+oveses.getTipoDocumento()+"·"+descridocto+"  "+"\t"+oveses.getNombreFormaPago()+"\t"+textoFormateadorutcli+"-"+oveses.getDvrutCliente()+"  "+textoFormateadonomcli+"\t"+textoFormateadomonto+"\r\n";
											
											codVendeAux=codVende;
										}
									}
										if (enviacorreo==true){
											mensajito="";
											mensajito=encabezado+detallitos;
											//correoVendedor="jaguilera@caserita.cl";
											EnvioMailTransportistaVendedores enviomailven = new EnvioMailTransportistaVendedores();
											enviomailven.mail(mensajito, codVende, nombreVendedor, correoVendedor, numeritodecarguio);
										}
								}
							}
						}
						
						
						
						
						
						
						
						
						
						
						
						
				
		/*
		 * S  O  L  I  C  I  T  U  D          2
		 */
		}else if (rut>0 && "2".equals(solicitud)){
			
			IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
				
			ConvierteTranspDTO convi = new ConvierteTranspDTO();
			cl.caserita.dto.DocumentoTranspDTO docutransp =  convi.convierte(par000,dv);
				
			if (Integer.parseInt(docutransp.getSolicitud())>999) {
				codError=Integer.parseInt(docutransp.getSolicitud());
			}
			
			int		numeroCarg=0, numerodocto=0, numOV=0;
			int		fecOV=0, horOV=0, fecEnt=0, horEnt=0, fecDoc=0, rutCli=0;
			int		numerNCpe=0, totalNCpe=0, monDoc=0, monEnt=0;
			String	digCli="", nomCli="", dirCli="", comuCli="", tipoDoc="";
			String	nomChof="", estEntr="", estMoti="", estadocargcab="", estadocargdet="", patente="";
			
			
			CarguioDAO 	carguiodao = dao.getCarguioDAO();
 			CarguiodDAO carguioddao = dao.getCarguiodDAO();
			OrdvtaDAO 	ordvtadao = dao.getOrdvtaDAO();
			DetordDAO 	detorddao = dao.getDetordDAO();
			ConnohDAO 	connohdao = dao.getConnohDAO();
			ConnodDAO 	connoddao = dao.getConnodDAO();
			CamtraDAO 	camtradao = dao.getCamtraDAO();
			VecmarDAO 	vecmardao = dao.getVecmarDAO();
			VedmarDAO 	vedmardao = dao.getVedmarDAO();
			TpacorDAO 	tpacordao = dao.getTpacorDAO();
			DocncpDAO 	docncpdao = dao.getDocncpDAO();
			CargcestDAO cargcestdao = dao.getCargcestDAO();
			ExdacpDAO 	exdacpdao = dao.getExdacpDAO();
			OrdvddeDAO 	ordvddedao = dao.getOrdvddeDAO();
			OrdvdetDAO 	ordvdetdao = dao.getOrdvdetDAO();
			CargconwDAO cargcondao = dao.getCargconwDAO();
			NcplogDAO 	ncplogdao = dao.getNcplogDAO();
			
			if ("".equals(dv)){
				ChoftranDTO choftrandto2 = choftrandao.obtenerDigitoRut(rut);
				dv=choftrandto2.getDvChofer().trim();	
			}
			
		if (codError<=0) {
						
			double  pventa=0, pnetos=0;
			int 	numdocu=0,tipodocu=0;
			int 	existeconnoh=0;
			
			Iterator iter = docutransp.getCarguio().iterator();
			while (iter.hasNext()){
				CarguioTranspDTO cargu = (CarguioTranspDTO) iter.next();
				
				codigoBodega=26;
				int codbodega=26;
				/*
				int codbodega = carguiodao.buscarCarguioTranspBodega(rut,dv,cargu.getNumeroCarguio());
				if (codbodega>0){
					codigoBodega=codbodega;
				}
				*/
				
				CarguioDTO carguiocdto = carguiodao.obtieneCarguioDTO(codigoEmpresa,cargu.getNumeroCarguio(),codigoBodega);
				if (carguiocdto==null){
					codigoBodega=21;
					CarguioDTO carguiocdto1 = carguiodao.obtieneCarguioDTO(codigoEmpresa,cargu.getNumeroCarguio(),codigoBodega);
							
					if (carguiocdto1==null){
						codigoBodega=24;
						CarguioDTO carguiocdto2 = carguiodao.obtieneCarguioDTO(codigoEmpresa,cargu.getNumeroCarguio(),codigoBodega);
						carguiocdto=carguiocdto2;
								
						}else{
							codigoBodega=26;
							carguiocdto=carguiocdto1;
						}
					}
						
					if (carguiocdto==null){
						codigoBodega=26;
					}
					
					int existecarguio2 = carguioddao.buscaExisteCarguioc(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), rut,dv);
					if (existecarguio2==0) {
						codError=1010;
						numeroCarg=cargu.getNumeroCarguio();
						logi.info("N U M E R O      C A R G U I O   "+cargu.getNumeroCarguio()+"   N O     E X I S T E    !!!!"); break;
					}
					
					numeroCarg=cargu.getNumeroCarguio();
					numeritodecarguio=numeroCarg;
					codigoEmpresa=carguiocdto.getCodigoEmpresa();
					patente=carguiocdto.getPatente().trim();
					versionpipe=Integer.toString(cargu.getVersion());
					
 					int existecarguioch = carguioddao.buscaExisteChofer(codigoEmpresa, codigoBodega, rut,dv);
					if (existecarguioch==0) {
						codError=1011;
						logi.info("R U T   C H O F E R    N O   E X I S T E    !!!"); break;
					}
					
					int existecarguio = carguioddao.buscaExisteCarguioc(codigoEmpresa, codigoBodega, numeroCarg, rut,dv);
					if (existecarguio==0) {
						codError=1010;
						logi.info("N U M E R O     C A R G U I O    "+numeroCarg+"   N O   E X I S T E     !!!!"); break;
					}
					else if (existecarguio==-1) {
						codError=1011;
						logi.info("R U T    C H O F E R    N O   E X I S T E    !!!"); break;
					}
					else if (existecarguio==-2) {
						codError=1013;
						logi.info("E S T A D O     C A R G U I O     I N C O R R E C TO   !!!!"); break;
					}
					else if (existecarguio==-4) {
						codError=1012;
						logi.info("DV  RUT CHOFER  INCORRECTO  ó   RUT DE CHOFER FUÉ CAMBIADO"); break;
					}
					
					if (codError<=0) {
						ChoftranDTO choftrandto = choftrandao.obtenerDatos(rut, dv);
						userId=choftrandto.getNomChofer().trim();
						nombredeChofer=userId;
						int largousu = userId.length();
						if (largousu>10){
							userId=userId.substring(0, 10).trim();
						}
						userId="CONTROLENT";
					}
					
					int existecarguioest = carguioddao.buscaExisteCarguioTransp(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(),patente);
					if (existecarguioest<=0) {
						logi.info("C A R G U I O   N U M E R O   "+numeroCarg+"   N O   E S T A   C O N   E S T A D O   C O R R E C T O    O   N O   F A C T U R A D O   !!!!");
						codError=1013; break;
					} 
					else {
					
					entrawhy=0;
					logi.info("ANTES DE WHILE ITER2 CARGU SOL2");
					Iterator iter2 = cargu.getOrdenes().iterator();
					while (iter2.hasNext()){
						if (entrawhy==0){
							logi.info("ENTRA A WHILE ITER2 CARGU SOL2");	
						}
						entrawhy=1;
						
						OrdTranspNcpDTO orden = (OrdTranspNcpDTO) iter2.next();
						
						String 	codigoEstadoOV="";
						String 	descriEstadoOV="";
						
						numdocu=orden.getNumDocumento();
						tipodocu=orden.getTipoDocumento();
						codigoEstadoOV=orden.getCodEstado().trim();
						descriEstadoOV=orden.getDescEstado().trim();
						
						numerodocto=numdocu;
						
						if (numdocu<=0) {
							logi.info("N U M E R O     D O C U M E N T O    E N   C E R O     !!!!!");
							codError=1004; break;
						}
						if (tipodocu<=0){
							logi.info("T I P O    D O C T O    E N     C E R O     !!!!!");
							codError=1005; break;
						}
						if (("".equals(codigoEstadoOV))){
							logi.info("C O D I G O    E S T A D O    V A C I O    !!!!");
							codError=1006; break;
						}
						if (("".equals(descriEstadoOV))){
							logi.info("descripcion estado vacio");
							codError=1007; break;
						}
									
						Fecha 	fch = new Fecha();
						int 	fecha = Integer.parseInt(fch.getYYYYMMDD());
						int     horas = Integer.parseInt(fch.getHHMMSS());
						int		numnc=0, swcorre=0;
						int 	cantiarti=0, totbrutocab=0, totnetocab=0, totalbrut=0, totdesctonetocab=0;
						int		totdesctocab=0, desctonetolinea=0;
						int		numeroguia=0, numeroOVe=0, icantidad=0,	rutclient=0,icantirecep=0;
						double 	totcostocab=0, totcostonetocab=0, costototnet=0, totalneto=0, desctobrutlinea=0;
						String 	responsableNota="", digclient="";
						boolean generarNCp = true;
						boolean esrecepMayor = false;
						boolean insertadoconnod=false;
						
			
						OrdTranspNcpDTO ordvtadto = ordvtadao.obtieneTotalesordenNumdoc(codigoEmpresa, codigoBodega, numdocu, orden.getTipoDocumento(),cargu.getNumeroCarguio(),rut,patente);
						if (ordvtadto==null){
							logi.info("N O   S E   H A N    E N C O N T R A D O    D A T O S     D E L      C A R G U I O     N u m e r o : "+cargu.getNumeroCarguio());
							codError=1014; break;
						}
						numdocu=ordvtadto.getNumFactbol();
						tipodocu=ordvtadto.getTipoDocumento();
						numeroguia=ordvtadto.getNumeroGuia();
						numeroOVe=ordvtadto.getNumeroOV();
						rutclient=ordvtadto.getRutCliente();
						digclient=ordvtadto.getDvCliente();
						rutCli=rutclient;
						digCli=digclient;
						nomCli=ordvtadto.getNombreCliente().trim();
						codigoVendedor=ordvtadto.getCodigoVendedor();
						nombreVendedor=ordvtadto.getNombreVendedor();
						correoVendedor=ordvtadto.getCorreoVendedor();
						if (correoVendedor==null || ("".equals(correoVendedor.trim()))){
							logi.info("S  I  N       C  O  R  R  E  O       V  E  N  D  E  D  O  R  !!        SE ENVIARA A DESARROLLO");
							correoVendedor=correoDesarrollo;
						}
						numOV=ordvtadto.getNumeroOV();
						fecOV=ordvtadto.getFechaDespacho();
						horOV=ordvtadto.getHoraOV();
						fecEnt=ordvtadto.getFechaEntrega();
						fecEnt=fecha;
						horEnt=horas;
						fecDoc=ordvtadto.getFechaFactura();
						dirCli=ordvtadto.getDireccionCliente().trim();
						comuCli=ordvtadto.getDescripcionComuna().trim();
						monDoc=ordvtadto.getMontoFactura();
						nomChof=userId;
						monEnt=monDoc;
						estEntr="";
						estMoti="";
						String nombreDocumen=tptdocdao.buscaDocumento(ordvtadto.getTipoDocumento());
						tipoDoc=Integer.toString(ordvtadto.getTipoDocumento())+"-"+nombreDocumen;
						
						logi.info("R U T CLIENTE  -------------------->  "+rutclient);
						logi.info("C L I E N T E  -------------------->  "+ordvtadto.getNombreCliente());
						logi.info("NUMERO  O V E  -------------------->  "+ordvtadto.getNumeroOV());
						logi.info("VERSION  '|'   -------------------->  "+versionpipe);
						logi.info("TIPO DOCUMENTO -------------------->  "+tipoDoc);
						
						estadocargcab = carguiodao.obtieneEstadoCarguioCab(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), patente);
						if ("E".equals(estadocargcab.trim())){
							estadocargcab="U";
						}
						else if ("L".equals(estadocargcab.trim())){
							estadocargcab="O";
						} 
						if (estadocargcab==null || ("".equals(estadocargcab.trim()))){
							estadocargcab=estadocar;
						}
							
						estadocargdet = carguioddao.obtieneEstadoCarguioDet(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(),patente, numeroOVe);
						if ("E".equals(estadocargdet.trim())){
							estadocargdet="K";
						}
						else if ("L".equals(estadocargdet.trim())){
								estadocargdet="H";
						}
						else if ("J".equals(estadocargdet.trim())){
							estadocargdet="F";
						}
						else if ("K".equals(estadocargdet.trim())){
								estadocargdet="H";
						}
					
						if (estadocargdet==null || ("".equals(estadocargdet.trim()))){
							estadocargdet=estadocar;
						}
						
						if (numeroOVe<=0){
							logi.info("O V     E N    C E R O     !!!!");
							codError=1025; break;
						}
						if (rutclient<=0){
							logi.info("R U T    C L I E N T E    E N    C E R O     !!!!!");
							codError=1026; break;
						}
						if (("".equals(digclient))){
							logi.info("D V    C L I E N T E    V A C I O     !!!!");
							codError=1027; break;
						}
									
						/*
						 * VALIDA QUE EL CARGUIO Y OV EXISTA (CASO PARA MAS DE 1 CARGUIO POR RUT DE CHOFER)
						 */
						int escarguiocorrecto = (carguiodao.obtieneCarguioyOrdenes(codigoEmpresa, cargu.getNumeroCarguio(), codigoBodega, numeroOVe, patente,rutclient));
						if (escarguiocorrecto==1){
							//valida redespacho
							String estado = carguioddao.obtieneEstadoCarguioD(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), numdocu,patente,rutclient,numeroOVe);
							if (estado.equals("")){
							/*
							 * validar si es ET: entrega total ; EP: entrega parcial ; RC : rechazo total
							 */
							String 	codigoMotivoOV="", descriMotivoOV="";
							int 	cantiart=0,	cantirec=0, codiarti=0,	corrarti=0;
							String	formarti="", latitud="", longitud="", distancia="";
							String 	estadotipoentrega="ET";
							String 	estadodescentrega="ENTREGADO";
							int tipodespa=1;
							estEntr=estadotipoentrega+"-"+estadodescentrega;
							
							codigoEstadoOV=orden.getCodEstado();
							descriEstadoOV=orden.getDescEstado();
							codigoMotivoOV=orden.getCodMotivo();
							descriMotivoOV=orden.getDesMotivo();
 							numdocu=orden.getNumDocumento();
							tipodocu=orden.getTipoDocumento();
							latitud=orden.getLatitud();
							longitud=orden.getLongitud();
							distancia=orden.getDistancia();
							String fechastamp="";
							if (orden.getTimestamp().trim()!=null){
								String anostamp = orden.getTimestamp().substring(0,4).trim() ;
								String messtamp = orden.getTimestamp().substring(5,7).trim();
								String diastamp = orden.getTimestamp().substring(8,10).trim();
								fechastamp=anostamp+messtamp+diastamp;
							}
							if (fechastamp==""){
								fechastamp=String.valueOf(fecha);
							}
							
							entrawhy=0;
							logi.info("ANTES DE WHILE ITER9 ORDEN SOL2");
							boolean vienecondetalle=false;
							Iterator<?> iter9 = orden.getDetord().iterator();
							while (iter9.hasNext()){
								if (entrawhy==0){
									logi.info("OK ENTRA A WHILE ITER9 ORDEN SOL2");
								}
								entrawhy=1;
								vienecondetalle=true;
								esrecepMayor = false;
								DetordTranspNcpDTO detalleinput = (DetordTranspNcpDTO) iter9.next();
								codiarti=Integer.parseInt(detalleinput.getCodigoArticulo());
								corrarti=Integer.parseInt(detalleinput.getCorrelativo());
								formarti=detalleinput.getFormato();
								cantiart=detalleinput.getCantidad();
								cantirec=detalleinput.getCantidadrecepcionada();
								if (cantirec>cantiart){
									cantirec=cantiart;
									esrecepMayor = true;
								}
								
								OrdvdetDTO ordvdetdto = new OrdvdetDTO();
								ordvdetdto.setEmpresa(codigoEmpresa);
								ordvdetdto.setNumeroOV(numeroOVe);
								ordvdetdto.setRutCliente(rutclient);
								ordvdetdto.setDigCliente(digclient);
								ordvdetdto.setCodigoBodega(codigoBodega);
								ordvdetdto.setDetalledespacho(0);
								ordvdetdto.setTipoDespacho(tipodespa);
								ordvdetdto.setCorrelativo(corrarti);
								ordvdetdto.setCodigoArticulo(codiarti);
								ordvdetdto.setFormatoArticulo(formarti);
								ordvdetdto.setCantidadArticulo(cantiart);
								ordvdetdto.setCantidadRecepcionada(cantirec);
								ordvdetdto.setFechauser(fecha);
								ordvdetdto.setHorauser(horas);
								ordvdetdto.setUsuario(userId);
								int existetbl = ordvdetdao.buscaOrdvdet(ordvdetdto);
								
								if (existetbl==0) {
									//logi.info("ANTES DE INSERTAR ORDVDET");
									ordvdetdao.insertaOrdvdet(ordvdetdto);
								}
								else if (existetbl==1){
									//logi.info("ANTES DE ACTUALIZAR ORDVDET");
									ordvdetdao.actualizaOrdvdet(ordvdetdto);
								}
								
								if (cantirec==0 && "ET".equals(estadotipoentrega)){
									estadotipoentrega="VB";
									estadodescentrega="VB CALL CENTER";
									estEntr=estadotipoentrega+"-"+estadodescentrega;
									estMoti=codigoMotivoOV+"-"+descriMotivoOV;
								}
								else if (cantirec>0 && (cantirec!=cantiart)){
									estadotipoentrega="EP";
									estadodescentrega="ENTREGA PARCIAL";
									monEnt=0;
									estMoti=codigoMotivoOV+"-"+descriMotivoOV;
									estEntr=estadotipoentrega+"-"+estadodescentrega;
								}
								
								
								
								if (cantirec==0 && cantiart>0 && ("EP".equals(codigoEstadoOV))){
									estadotipoentrega="EP";
									estadodescentrega="ENTREGA PARCIAL";
									monEnt=0;
									estMoti=codigoMotivoOV+"-"+descriMotivoOV;
									estEntr=estadotipoentrega+"-"+estadodescentrega;
								}
								//cuando la cantidad del celular es mayor a lo generado en syscon
								if (esrecepMayor==true){
									estadotipoentrega="EP";
									estadodescentrega="ENTREGA PARCIAL";
									monEnt=0;
									estMoti=codigoMotivoOV+"-"+descriMotivoOV;
									estEntr=estadotipoentrega+"-"+estadodescentrega;
								}
								
								
							}
							
								if (vienecondetalle==false){
									logi.info("SIN DETALLE !!!!! EL CARGUIO Numero : "+cargu.getNumeroCarguio());
									codError=1030; break;
								}
							
												
								if ("ET".equals(estadotipoentrega)){
									generarNCp=false;
									codigoMotivoOV="";
									descriMotivoOV="";
									estMoti="";
								} else if ("VB".equals(estadotipoentrega)){
									if ("P".equals(codigoMotivoOV) || "Q".equals(codigoMotivoOV) || "E".equals(codigoMotivoOV)){
										estadotipoentrega="RC";
										estadodescentrega="RECHAZADO";
										generarNCp=true;
										monEnt=0;
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										estEntr=estadotipoentrega+"-"+estadodescentrega;
									}
									else {
										generarNCp=false;
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										estEntr="RC-RECHAZADO";
										monEnt=0;
										estEntr="VB CALL CENTER";
										
										if ("ET".equals(codigoMotivoOV)){
											estEntr="ET-ENTREGADO";
											estadotipoentrega="ET";
											estadodescentrega="ENTREGADO";
											estMoti="";
										}
										
									}
								}
								else {
									generarNCp=true;
								}
								
								
								
								
								
								
								
								
								
								
								
								
							int cantiredespa = carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), numdocu,patente);
							//1
							//logi.info("cambiar esta linea a >1... solo para pruebas 5");
							if (cantiredespa>99){
									logi.info("OV : "+numeroOVe+ " YA TIENE REDESPACHO, CARGUIOD XONM1 !!!");
									codError=1028; break;
							}
							else {
								/*
								* inserta en tabla ordvdde, timestamp, motivo, estado, latitud, longitud, distancia, foto, comentario
								*/
								OrdvddeDTO ordvddedto = new OrdvddeDTO();
								ordvddedto.setEmpresa(codigoEmpresa);
								ordvddedto.setNumeroOV(numeroOVe);
								ordvddedto.setRutCliente(rutclient);
								ordvddedto.setDigCliente(digclient);
								ordvddedto.setCodigoBodega(codigoBodega);
								ordvddedto.setDetalledespacho(0);
								ordvddedto.setTipoDespacho(tipodespa);
								ordvddedto.setFechaConfirmacion(Integer.parseInt(fechastamp));
								ordvddedto.setLatitud(latitud);
								ordvddedto.setLongitud(longitud);
								ordvddedto.setDistancia(distancia);
								ordvddedto.setCodEstado(estadotipoentrega);
								ordvddedto.setDesEstado(estadodescentrega);
								ordvddedto.setCodMotivo(codigoMotivoOV);
								ordvddedto.setDesMotivo(descriMotivoOV);
													
								//FOTOS Y COMENTARIOS ,DESCOMENTAR CUANDO SE GUARDE EN NUEVA TABLA
								int ii=1;
								Iterator iter44 =orden.getDetfoto().iterator();
								while (iter44.hasNext()){
									DetordTranspNcpDTO detallefoto = (DetordTranspNcpDTO) iter44.next();
									if (ii==1){
										ordvddedto.setFoto1(detallefoto.getFoto());
										ordvddedto.setComentario1(detallefoto.getComentario());
									}
									else if (ii==2){
										ordvddedto.setFoto2(detallefoto.getFoto());
										ordvddedto.setComentario2(detallefoto.getComentario()); }
									else if (ii==3){
										ordvddedto.setFoto3(detallefoto.getFoto());
										ordvddedto.setComentario3(detallefoto.getComentario()); }
									else if (ii==4){
										ordvddedto.setFoto4(detallefoto.getFoto());
										ordvddedto.setComentario4(detallefoto.getComentario()); }
									else if (ii==5){
										ordvddedto.setFoto5(detallefoto.getFoto());
										ordvddedto.setComentario5(detallefoto.getComentario()); }
									else if (ii==6){
										ordvddedto.setFoto6(detallefoto.getFoto());
										ordvddedto.setComentario6(detallefoto.getComentario()); }
									ii++;
								}
								ordvddedto.setFechauser(fecha);
								ordvddedto.setHorauser(horas);
								int existetbl = ordvddedao.buscaOrdvdde(ordvddedto);
								if (existetbl==0) {
									//logi.info("ANTES DE INSERTA ORDVDDE");
									ordvddedao.insertaOrdvdde(ordvddedto);
								}
								else if (existetbl==1){
									//logi.info("ANTES DE UPDATE ORDVDDE");
									ordvddedao.actualizaOrdvdde(ordvddedto);
								}
									
								carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe, 1);
								entrawhy=0;
								logi.info("ANTES DE WHILE ITER4 ORDEN SOL2");
								Iterator iter4 = orden.getDetord().iterator();
								while (iter4.hasNext()){
									if (entrawhy==0){
										logi.info("ENTRA A WHILE ITER4 ORDEN SOL2");
									}
									entrawhy=1;
									DetordTranspNcpDTO detalleinput = (DetordTranspNcpDTO) iter4.next();
									icantidad=detalleinput.getCantidad();
									icantirecep=detalleinput.getCantidadrecepcionada();
									
									if (generarNCp==false){
										int respactestado2 = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV, codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, Integer.parseInt("7777777"),numeroguia);
										int respactestado = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV, codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, 0,numeroguia);
										carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe,rutclient,1);
										
									}
									
									if (generarNCp==true){
										
										if (icantirecep<icantidad){
											
											if (swcorre==0){
												
												/*
												ProcedimientoDAO proce = dao.getProcedimientoDAO();
												String str = "ASYSRCD00 00008   0  01   ";
												numnc = proce.obtieneCorrelativo(str);
												*/
												
												numnc=(tpacordao.recupeCorrelativo(0,8));
												if (numnc<=0){	
													logi.info("ERROR!! NO SE HA GENERADO CORRELATIVO PARA LA NCP");
													codError=1029; break;
												}
												else {
													int existenum = connohdao.buscaExisteConnoh(codigoEmpresa, numnc);
													if (existenum>0){
														numnc=(tpacordao.recupeCorrelativo(0,8));
													}
												}
												
												int existenumerodenc=0;
												for(int i=0;i<50;i++)
												{
													existenumerodenc = connohdao.buscaExisteConnoh(codigoEmpresa, numnc);
													if (existenumerodenc>0){
														numnc=(tpacordao.recupeCorrelativo(0,8));
													}
													else {
														break;
													}
												}		
												
												swcorre=1;
											}
											
											entrawhy=0;
											logi.info("ANTES DE WHILE ITER5 LISTADETORDP SOL2");
											List listadetordp;
											listadetordp = detorddao.obtieneTotalesdetalleNumdoc(codigoEmpresa, numeroOVe, codigoBodega, ordvtadto.getRutCliente(), Integer.parseInt(detalleinput.getCodigoArticulo()), icantidad);
											if (listadetordp.isEmpty()){
												logi.info("ANTES DE WHILE ISEMPTY LISTADETORDP SOL2");
												listadetordp = detorddao.obtieneTotalesdetalleNumdoc2(codigoEmpresa, numeroOVe, codigoBodega, ordvtadto.getRutCliente(), Integer.parseInt(detalleinput.getCodigoArticulo()), icantidad);
											}
											Iterator iter5 = listadetordp.iterator();
											while (iter5.hasNext()) {
												if (entrawhy==0){
													logi.info("ENTRA A WHILE ITER5 LISTADETORDP SOL2");
												}
												entrawhy=1;
												
												DetordTranspNcpDTO detorden2 = (DetordTranspNcpDTO) iter5.next();
																	
												ConnodDTO connoddto = new ConnodDTO();
												NcplogDTO ncplogdto = new NcplogDTO();  //logNCp
												
												connoddto.setCodigoEmpresa(carguiocdto.getCodigoEmpresa());
												connoddto.setTipoNota("P");
												connoddto.setNumeroNota(numnc);
												connoddto.setFechaNota(fecha);
												connoddto.setCorrelativo(Integer.parseInt(detorden2.getCorrelativo()));
												connoddto.setCodArticulo(Integer.parseInt(detorden2.getCodigoArticulo()));
												connoddto.setDigArticulo(detorden2.getDigitoArticulo());
												connoddto.setDescripcion(detorden2.getDescripcionArticulo());
												icantirecep=detalleinput.getCantidadrecepcionada();
												if (icantirecep>icantidad){
													icantirecep=icantidad;
												}
												int cantidevo=0;
			 									if (icantirecep<=0){ 
			 										cantidevo=icantidad;
												}
												else { 
													cantidevo=icantidad-icantirecep;
												}
			 									connoddto.setCantidad(cantidevo);
												connoddto.setFormato(detorden2.getFormato());
												connoddto.setPrecioUnitario(detorden2.getPrecioUnitario());
												connoddto.setPrecioNeto(detorden2.getPrecioNeto());
												connoddto.setCostoArticulo(detorden2.getCostoArticulo());
												connoddto.setCostoNetoUnitario(detorden2.getCostoNetoUnitario());
												connoddto.setMontoNeto(detorden2.getMontototalNeto());
												connoddto.setMontoExento(detorden2.getTotalExento());
												if (detorden2.getCostoTotalNeto()<=1){
													costototnet=cantidevo*detorden2.getCostoTotalNeto();
												} else {
													costototnet=detorden2.getCostoTotalNeto();
												}
												totalneto=Math.round(cantidevo*detorden2.getPrecioNeto());
												totalbrut=(int) (cantidevo*detorden2.getPrecioUnitario());
												connoddto.setCostoTotalNeto(costototnet);
												connoddto.setMontoNeto((int)(totalneto));
												connoddto.setTotalNeto((int)(totalneto));
												
												if ("7777777".equals(detalleinput.getCodigoArticulo())){
													List listacldmco = cldmcodao.obtieneFleteCldmco(codigoEmpresa, tipodocu, rutclient, digclient, fecDoc, codigoBodega, numdocu, Integer.parseInt(detalleinput.getCodigoArticulo()));
													Iterator iterclc = listacldmco.iterator();
													while (iterclc.hasNext()) {
														CldmcoDTO cldmco2 = (CldmcoDTO) iterclc.next();
														connoddto.setPrecioUnitario(cldmco2.getPrecio());
														connoddto.setPrecioNeto(cldmco2.getPrecioNeto());
														connoddto.setMontoNeto(cldmco2.getValorNeto());
														connoddto.setMontoExento(cldmco2.getMontoExento());
														totalneto=Math.round(cantidevo*cldmco2.getPrecioNeto());
														totalbrut=cldmco2.getMontoCompra();
														connoddto.setMontoNeto((int)(totalneto));
														connoddto.setTotalNeto((int)(totalneto));
													}
												}
												
												//logi.info("COSTOS: "+detorden2.getCostoArticulo()+ "  "+detorden2.getCostoNetoUnitario()+" " +detorden2.getCostoTotalNeto());
												//logi.info("CONNOD * CODART|CORRE|FORMATO|CANTI|CANTIRECEP  --->  "+detalleinput.getCodigoArticulo()+"|"+detorden2.getCorrelativo()+"|"+detorden2.getFormato()+"|"+detorden2.getDescripcionArticulo()+"|"+icantidad+"|"+icantirecep);
																	
												VedmarDTO vedmardto = new VedmarDTO();
												double  impuestos = vedmardao.calculaImpuestosArticulo(Integer.parseInt(detalleinput.getCodigoArticulo()), detorden2.getDigitoArticulo());
																	
												connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
												//1 peso
												//Sin descuento
												if (detorden2.getTotalDescuento()<=0 && detorden2.getTotalDescuentoNeto()<=0){
													connoddto.setTotalDescuento(detorden2.getTotalDescuento());
													connoddto.setTotalDescuentoNeto(detorden2.getTotalDescuentoNeto());
												} else {
						     						//Con descuento
													if (icantidad!=icantirecep){
														desctonetolinea=(int) Math.round((totalneto*(detorden2.getPorcentajeDescuento()/100)));
														desctobrutlinea=Math.round(desctonetolinea*impuestos);
														connoddto.setTotalDescuento((int) desctobrutlinea);
														connoddto.setTotalDescuentoNeto(desctonetolinea);  
														totalneto=totalneto-desctonetolinea;
														connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
														connoddto.setMontoNeto((int)(totalneto));
														connoddto.setTotalNeto((int)(totalneto)+desctonetolinea);
														totdesctonetocab=totdesctonetocab+desctonetolinea;
														totdesctocab=(int) (totdesctocab+desctobrutlinea);
													}
													else if (icantidad==icantirecep){
														totdesctonetocab=totdesctonetocab+detorden2.getTotalDescuentoNeto();
														totdesctocab=totdesctocab+detorden2.getTotalDescuento();
														connoddto.setTotalDescuento(detorden2.getTotalDescuento());
														connoddto.setTotalDescuentoNeto(detorden2.getTotalDescuentoNeto());  
														totalneto=totalneto-detorden2.getTotalDescuentoNeto();
														connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
														connoddto.setMontoNeto((int)(totalneto));
														connoddto.setTotalNeto((int)(totalneto)+detorden2.getTotalDescuentoNeto());
														}
													}
												
												/*
													//cuando es combo, calcular neto y bruto en tables EXDACP y EXDACB
													if ("C".equals(detorden2.getCombo())) {
														connoddto.setFormato("X");
														int a;
														ExdacpDTO exdacpdto = exdacpdao.calculamontosCombo(codigoEmpresa, Integer.parseInt(detalleinput.getCodigoArticulo()), detorden2.getDigitoArticulo(), codigoBodega,2000 , cantidevo, (a = (int) detorden2.getPorcentajeDescuento()));
														pnetos=exdacpdto.getPrecioNeto();
														pventa=exdacpdto.getPrecioVenta();
														totalneto=Math.round(pnetos);
														totalbrut=(int) (pventa);
														connoddto.setMontoNeto((int)(totalneto));
														connoddto.setTotalNeto((int)(totalneto));
														connoddto.setTotalLinea((int)(Math.round(totalbrut)));
													}
												*/
													
													//Insert in Connod
													ConnohDTO connohdto2 = new ConnohDTO();
													connohdto2.setCodDocumento(tipodocu);
													connohdto2.setNumeroDocumento(numdocu);
													connohdto2.setRutCliente(rutclient);
													connohdto2.setTipoNota("P");
													connohdto2.setCodigoBodega(codigoBodega);
													existeconnoh = connohdao.existeConnoh(connohdto2);
													if (existeconnoh>0){
														connoddto.setNumeroNota(existeconnoh);
														int existeconnod = connoddao.buscaExisteConnod(connoddto);
														if (existeconnod>0) {
															int eliconnod=connoddao.eliminaConnod(connoddto);
														}
														connoddto.setNumeroNota(numnc);
													}	
													int respncp = connoddao.insertaConnod(connoddto);
													insertadoconnod=true;
													if (respncp>=0){
														ncplogdto.setCodigoEmpresa(connoddto.getCodigoEmpresa());
														ncplogdto.setTipo("P");
														ncplogdto.setNumeroNota(connoddto.getNumeroNota());
														ncplogdto.setFechaNota(connoddto.getFechaNota());
														ncplogdto.setLineaNota(connoddto.getCorrelativo());
														ncplogdto.setNumeroCarguio(cargu.getNumeroCarguio());
														ncplogdto.setNumeroOrden(ordvddedto.getNumeroOV());
														ncplogdto.setNumeroDocumento(ordvtadto.getNumFactbol());
														ncplogdto.setCodigoBodega(ordvddedto.getCodigoBodega());
														ncplogdto.setRutCliente(ordvtadto.getRutCliente());
														ncplogdto.setDigCliente(ordvtadto.getDvCliente());
														ncplogdto.setUsuario(userId);
														ncplogdto.setTipoAccion("I");
														ncplogdto.setCodigoArticulo(connoddto.getCodArticulo());
														ncplogdto.setDigitoArticulo(connoddto.getDigArticulo());
														ncplogdto.setCantidad(cantidevo);
														ncplogdto.setCantidadArticulo(connoddto.getCantidad());
														ncplogdto.setFechaUser(fecha);
														ncplogdto.setHoraUser(horas);
														ncplogdto.setIpEquipo(Integer.toString(cargu.getRutChofer()));
														ncplogdto.setNombreEquipo(carguiocdto.getPatente().trim());
														ncplogdto.setCodigoUsuario(userId);
														int correlativoncplog = ncplogdao.buscaUltimaLineaNcplog(ncplogdto);
														ncplogdto.setLineaNota(correlativoncplog);
														int resplog = ncplogdao.insertaNcplog(ncplogdto);
													}
													
													if (respncp<0){	logi.info("ERROR!! NO SE HA GENERADO CONNOD NCP");break; }
														//Actualizar Estado CarguioD
														int respactestad2 = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV,codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, Integer.parseInt("7777777"),numeroguia);
														int respactestado = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV,codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, 0,numeroguia);
														
														
														carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe, 4);
														//Acumular totales para connoH
														cantiarti++;
														totcostocab= totcostocab+Math.floor(detorden2.getCostoArticulo());
														totcostonetocab=totcostonetocab+costototnet;
														if ("C".equals(detorden2.getCombo())) {
															totbrutocab=totbrutocab+(int)(Math.round(totalbrut));
														} else {
															totbrutocab=totbrutocab+(int)(Math.round(totalneto*impuestos));
														}
														totnetocab=(int) (totnetocab+totalneto);
											} //iter5
									} //esconcanti
								} //iter4
							} //generarncp
												
									if (generarNCp==true && insertadoconnod==true){
										ConnohDTO connohdto = new ConnohDTO();
										connohdto.setTipoNota("P");
										connohdto.setNumeroNota(numnc);
										connohdto.setFechaNota(fecha);
										connohdto.setCodDocumento(ordvtadto.getTipoDocumento());
										connohdto.setNumeroDocumento(ordvtadto.getNumFactbol());
										connohdto.setRutCliente(ordvtadto.getRutCliente());
										connohdto.setDivCliente(ordvtadto.getDvCliente());
										connohdto.setCodigoMovimiento(2);
										connohdto.setCodigoBodega(codigoBodega);
										connohdto.setCodigoVendedor(ordvtadto.getCodigoVendedor());
										connohdto.setNombreCliente(ordvtadto.getNombreCliente());
										connohdto.setCantidadLineas(cantiarti);
										connohdto.setTotalCosto((int)(totcostocab));
										connohdto.setTotalCostoNeto((int)(totcostonetocab));
										if (totdesctonetocab>0){
											connohdto.setTotalDescuento(totdesctocab);
											connohdto.setTotalDescuentoNeto(totdesctonetocab);
										} else {
											connohdto.setTotalDescuento(ordvtadto.getTotalDescuento());
											connohdto.setTotalDescuentoNeto(ordvtadto.getTotalDescuentoneto());
										}
										connohdto.setMontoTotal(totbrutocab);
										connohdto.setMontoNeto(totnetocab);
										connohdto.setMontoIva(0);
										connohdto.setMontoExento(ordvtadto.getTotalExento());
										connohdto.setEstado("I");
										String codmotov="";
										monEnt=Math.round(monDoc-totbrutocab);
										codmotov=orden.getCodMotivo();
										if (codigoMotivoOV != null && !codigoMotivoOV.equals("")){
											codmotov=codigoMotivoOV;
										}
										String userId2 = String.format("%-29s", nombredeChofer);
										responsableNota=(userId2.substring(0, 29)+codmotov+cargu.getNumeroCarguio()).trim();
										connohdto.setResponsableNota(responsableNota);
										/*
										connohdto.setNumeroCarguio(cargu.getNumeroCarguio());
										connohdto.setNumeroOV(numeroOVe);
										connohdto.setMotivoNoventa(codigoMotivoOV);
										connohdto.setUsuarioNota(userId.trim());
										connohdto.setUsuarioFecha(fecha);
										connohdto.setUsuarioHora(horas);
										*/
										//Insert in Connoh
										if (existeconnoh>0){
											connohdto.setNumeroNota(existeconnoh);
											int existeconnoh2 = connohdao.existeConnoh(connohdto);
											if (existeconnoh2>0) {
												int eliconnoh=connohdao.eliminaConnoh(connohdto);
											}
											connohdto.setNumeroNota(numnc);
										}
										connohdao.insertaConnoh(connohdto);
										
										numerNCpe=numnc;
										totalNCpe=totbrutocab;
										
										DocncpDTO docncpdto = new DocncpDTO();
										docncpdto.setTipoNota("P");
										docncpdto.setNumeroNota(numnc);
										docncpdto.setFechaNota(fecha);
										docncpdto.setCodigoBodega(codigoBodega);
										docncpdto.setRutCliente(ordvtadto.getRutCliente());
										docncpdto.setDigCliente(ordvtadto.getDvCliente());
										docncpdto.setCorrelativo(1);
										docncpdto.setNumeroOV(numeroOVe);
										docncpdto.setNumeroCarguio(cargu.getNumeroCarguio());
										docncpdto.setNumeroDocumento(ordvtadto.getNumFactbol());
										docncpdto.setNumeroGuia(numeroguia);
										docncpdto.setNumeroNcfinal(0);
										docncpdto.setFechaUsuario(fecha);
										docncpdto.setHoraUsuario(horas);
										docncpdto.setCodigoUsuario(userId);
										docncpdto.setMotivo(codmotov);
										//Insert in DOCNCP
										if (existeconnoh>0){
											docncpdto.setNumeroNota(existeconnoh);
										}
										int existedocncp = docncpdao.buscaDocncpDTO(docncpdto);
										if (existedocncp>0) {
											int elidocncp=docncpdao.eliminaDocncpDTO(docncpdto);
										}
										docncpdto.setNumeroNota(numnc);
										
										docncpdao.insertaDocncp(docncpdto);
															
										CargcestDTO cargcestdto = new CargcestDTO();
										cargcestdto.setCodigoEmpresa(codigoEmpresa);
										cargcestdto.setNumcarguio(cargu.getNumeroCarguio());
										cargcestdto.setPatente(patente);
										cargcestdto.setCodigoBodega(codigoBodega);
										int correcargcest = cargcestdao.obtieneCorrelativo(codigoEmpresa, cargu.getNumeroCarguio(), patente, codigoBodega);
										cargcestdto.setCorrelativo(correcargcest);
										cargcestdto.setEstado(estadocargcab); //estadocar
										cargcestdto.setFechaUsuario(fecha);
										cargcestdto.setHoraUsuario(horas);
										cargcestdto.setUsuario(userId);
										//Insert Table Cargcest
										int exiscargcest = cargcestdao.existeCargcestDTO(cargcestdto);
										if (exiscargcest<=0) {
											cargcestdao.insertaCargcest(cargcestdto);
										}
															
										//update status CarguioC y CarguioD
										/*
										String estadocarguioc="";
										String estadocarguiod="";
										if ("E".equals(estadocar)){
											estadocarguioc="U";
											estadocarguiod="K";
										}
										else if ("L".equals(estadocar)){
											estadocarguioc="O";
											estadocarguiod="H";
										}
										*/
										carguiodao.actualizarestadoCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocargcab);
										carguiodao.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocargdet,numeroOVe);
										
										carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe,rutclient,1);
										
										
										//consolidado
										String todasNCpes = docncpdao.obtenerNcpes(codigoEmpresa, codigoBodega, numeroCarg).trim();
										if (!todasNCpes.equals("")){
											
											CargconwDTO carcondtoeli = new CargconwDTO();
											carcondtoeli.setCodigoEmpresa(codigoEmpresa);
											carcondtoeli.setNumcarguio(numeroCarg);
											carcondtoeli.setPatente(patente);
											carcondtoeli.setCodigoBodega(codigoBodega);
											int elitodo=cargcondao.eliminaCargconwTransp(carcondtoeli);
											
											List listconsoli = connoddao.obtieneConsolidadoNCp(todasNCpes, codigoEmpresa, numeroCarg, codigoBodega);
											Iterator iter6 = listconsoli.iterator();
											while (iter6.hasNext()){
												ConnodDTO listacon = (ConnodDTO) iter6.next();
												
												CargconwDTO carcondto = new CargconwDTO();
												carcondto.setCodigoEmpresa(codigoEmpresa);
												carcondto.setNumcarguio(numeroCarg);
												carcondto.setPatente(patente);
												carcondto.setCodigoBodega(codigoBodega);
												carcondto.setCodigoArticulo(listacon.getCodArticulo());
												carcondto.setDigitoArticulo(listacon.getDigArticulo());
												carcondto.setTipoCarguio("C");
												carcondto.setCantidadArticulo(listacon.getCantidad());
												carcondto.setFechaDevolucion(fecha);
												carcondto.setCantidadConfirmada(0);
												carcondto.setCantidadDiferencia(0);
												int fechaExpiracion = carguioddao.obtieneFechaVencimientoArt(codigoEmpresa, codigoBodega, numeroCarg, listacon.getCodArticulo());
												if (fechaExpiracion<=0){
													fechaExpiracion=fecha;
													//sumar 90 dias
													String fechamas="";
													Date	resulfecha;
													Date date = new Date(fecha);
													resulfecha = sumarfechas(date, diasSumar);
													//logi.info(resulfecha);
													fechamas= (resulfecha.toString());
													String anofec = fechamas.substring(0,4).trim() ;
													String mesfec = fechamas.substring(5,7).trim();
													String diafec = fechamas.substring(8,10).trim();
													fechaExpiracion=Integer.parseInt(anofec+mesfec+diafec);
												}
												carcondto.setFechaExpiracion(fechaExpiracion);
												carcondto.setPrecioNeto(listacon.getPrecioNeto());
												carcondto.setPrecioBruto(listacon.getPrecioUnitario());
												int exitbl = cargcondao.buscaCargconwTransp(carcondto);
												if (exitbl>=0){
													exitbl=exitbl-listacon.getCantidad();
													carcondto.setCantidadArticulo(exitbl);
													int canti=cargcondao.actualizaCargconw(carcondto);
												}
												else {
													cargcondao.insertaCargconw(carcondto);
												}
												
												
											}
										}
										
										logi.info("\n");
										logi.info(" ---------->    O.K.  NCp    G E N E R A D A    <----------  "+numnc);
										logi.info("\n");

										
															
									} //generarncp
											
							} //cantiredespa<=0
						
					
					
					}
						else {
						logi.info("Numdoc: "+numdocu+ " YA QUE TIENE ESTADO ASIGNADO para esta solicitud 2. tabla CARGUIOD XOCL1");
						//codError=1024; break;
					}
					
						
					}
					else {
						logi.info("no existe carguio");
						codError=1010;
						break;
					}  //escarguiocorrecto
						
						
									
												
					} //iter
							
				} //existecarguio
					
				
					if (codError<=0){
						
						logi.info("E S T A D O   -------------------->   "+estEntr);
						logi.info("M O T I V O   -------------------->   "+estMoti);
						
						String numerodocto2="";
						if (Integer.parseInt(versionpipe)>0){
							numerodocto2=Integer.toString(numdocu)+"|"+versionpipe;
						} else {
							numerodocto2=Integer.toString(numdocu);
						}	
						
						logi.info("E N V I A N D O     C O R R E O     A  :  ......   "+correoVendedor);
						EmailDTO emaildto = new EmailDTO();
						EnvioMailTransportista enviomail = new EnvioMailTransportista();
						
						if ((nombredeChofer != null) && (!nombredeChofer.equals(""))){
							nomChof=nombredeChofer;
						}
						enviomail.mail("", codigoVendedor, nombreVendedor, correoVendedor, 0, numOV, fecOV, horOV, fecEnt, horEnt, numerodocto2, tipoDoc, fecDoc, rutCli, digCli, nomCli, dirCli, comuCli, monDoc, monEnt, nomChof, estEntr, estMoti, numerNCpe, totalNCpe,numeroCarg);
						if ("ET-ENTREGADO".equals(estEntr)){
							carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, numeroCarg, numOV,rutCli,1);
						}
						logi.info("correo enviado    S O L I C I T U D   2     !!!!");
						
					}
					
			} //
				
		} //coderror		
				
		if (codError>0){

			ErrorTransportistaDAO errordao = dao.getErrorTransportistaDAO();
			List listita = errordao.buscaErrorTransportista(numeroCarg, codError); 
			String respdd = gson.toJson(listita);
			resp=respdd;
			logi.info("error: "+resp);
			
			EmailDTO emaildto = new EmailDTO();
			EnvioMailErrorTransportista enviomail = new EnvioMailErrorTransportista();
			enviomail.mail("SOLICITUD 2", numeroCarg, resp,par000);
			
			
		}
			else {
					
				generatxt(2, par000, String.valueOf(rut), dv,numerodocto,numeroCarg);
				
				String numerodocto2="";
				if (Integer.parseInt(versionpipe)>0){
					numerodocto2=numerodocto+"|"+versionpipe;
				} else {
					numerodocto2=Integer.toString(numerodocto);
				}	
				List comple = carguioddao.buscaCarguiodTransp(codigoEmpresa, rut, dv, codigoBodega,1, numerodocto2,numeroCarg,patente);
					
				String respd = gson.toJson(comple);
				resp=respd;
				
				logi.info(resp);
				
				generatxt(22, respd, String.valueOf(rut), dv,numerodocto,numeroCarg);
				
				
			}
				
		
				
				
				
				
				
				
				
				
				
				
			
				
				
				
				
				
				
				
				
				
		/*
		 * S  O  L  I  C  I  T  U  D         3
		 */
		}else if (rut>0 && "3".equals(solicitud)){
			
						
			IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
			
			ConvierteTranspDTO convi = new ConvierteTranspDTO();
			cl.caserita.dto.DocumentoTranspDTO docutransp =  convi.convierte(par000,dv);
				
			if (Integer.parseInt(docutransp.getSolicitud())>999) {
				codError=Integer.parseInt(docutransp.getSolicitud());
			}
			
			int		numeroCarg=0;
			int		numerodocto=0;
			int		cantiredespa=0;
			int 	existeconnoh=0;
			
			CarguioDAO 	carguiodao = dao.getCarguioDAO();
 			CarguiodDAO carguioddao = dao.getCarguiodDAO();
			OrdvtaDAO 	ordvtadao = dao.getOrdvtaDAO();
			DetordDAO 	detorddao = dao.getDetordDAO();
			ConnohDAO 	connohdao = dao.getConnohDAO();
			ConnodDAO 	connoddao = dao.getConnodDAO();
			CamtraDAO 	camtradao = dao.getCamtraDAO();
			VecmarDAO 	vecmardao = dao.getVecmarDAO();
			VedmarDAO 	vedmardao = dao.getVedmarDAO();
			TpacorDAO 	tpacordao = dao.getTpacorDAO();
			DocncpDAO 	docncpdao = dao.getDocncpDAO();
			CargcestDAO cargcestdao = dao.getCargcestDAO();
			ExdacpDAO 	exdacpdao = dao.getExdacpDAO();
			OrdvddeDAO 	ordvddedao = dao.getOrdvddeDAO();
			OrdvdetDAO 	ordvdetdao = dao.getOrdvdetDAO();
			CargconwDAO cargcondao = dao.getCargconwDAO();
			NcplogDAO 	ncplogdao = dao.getNcplogDAO();
			
			String	patente="";
			double  pventa=0, pnetos=0;
			int 	numdocu=0,tipodocu=0;
			
			int		numOV=0;
			int		fecOV=0;
			int 	horOV=0;
			int		fecEnt=0;
			int		horEnt=0;
			int		fecDoc=0;
			int		rutCli=0;
			String	digCli="";
			String	nomCli="";
			String	dirCli="";
			String	comuCli="";
			int		monDoc=0;
			int		monEnt=0;
			String	nomChof="";
			String	estEntr="";
			String	estMoti="";
			String	codMotivoTabla="";
			String	desMotivoTabla="";
			String	tipoDoc="";
			int		numerNCpe=0;
			int		totalNCpe=0;
			String	estadocargcab="";
			String 	estadocargdet="";
			
			
			if ("".equals(dv)){
				ChoftranDTO choftrandto2 = choftrandao.obtenerDigitoRut(rut);
				dv=choftrandto2.getDvChofer().trim();	
			}

		if (codError<=0) {
						
					
			Iterator iter = docutransp.getCarguio().iterator();
			while (iter.hasNext()){
				CarguioTranspDTO cargu = (CarguioTranspDTO) iter.next();
				
				codigoBodega=26;
				int codbodega=26;
				
				/*
				int codbodega = carguiodao.buscarCarguioTranspBodega(rut,dv,cargu.getNumeroCarguio());
				if (codbodega>0){
					codigoBodega=codbodega;
				}
				*/
				CarguioDTO carguiocdto = carguiodao.obtieneCarguioDTO(codigoEmpresa,cargu.getNumeroCarguio(),codigoBodega);
				if (carguiocdto==null){
					codigoBodega=21;
					CarguioDTO carguiocdto1 = carguiodao.obtieneCarguioDTO(codigoEmpresa,cargu.getNumeroCarguio(),codigoBodega);
							
					if (carguiocdto1==null){
						codigoBodega=24;
						CarguioDTO carguiocdto2 = carguiodao.obtieneCarguioDTO(codigoEmpresa,cargu.getNumeroCarguio(),codigoBodega);
						carguiocdto=carguiocdto2;
								
						}else{
							codigoBodega=26;
							carguiocdto=carguiocdto1;
						}
					}
						
					if (carguiocdto==null){
						codigoBodega=26;
					}
					
					int existecarguio2 = carguioddao.buscaExisteCarguioc(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), rut,dv);
					if (existecarguio2==0) {
						codError=1010;
						numeroCarg=cargu.getNumeroCarguio();
						logi.info("N U M E R O      C A R G U I O    "+cargu.getNumeroCarguio()+"   N O    E X I S T E       !!!!"); break;
					}
					
					numeroCarg=cargu.getNumeroCarguio();
					numeritodecarguio=numeroCarg;
					codigoEmpresa=carguiocdto.getCodigoEmpresa();
					patente=carguiocdto.getPatente().trim();
					versionpipe=Integer.toString(cargu.getVersion());
										
					int existecarguioch = carguioddao.buscaExisteChofer(codigoEmpresa, codigoBodega, rut,dv);
					if (existecarguioch==0) {
						codError=1011;
						logi.info("R U T      C  H O F E R      N O     E X I S T E        !!!!"); break;
					}
					
					
					int existecarguio = carguioddao.buscaExisteCarguioc(codigoEmpresa, codigoBodega, numeroCarg, rut,dv);
					if (existecarguio==0) {
						codError=1010;
						logi.info("N U M E R O      C A R G  U I O      "+numeroCarg+"    N O     E X I S T E       !!!!"); break;
					}
					else if (existecarguio==-1) {
						codError=1011;
						logi.info("R U T     C H O F E R     N O     E X I ST E    !!!!"); break;
					}
					else if (existecarguio==-2) {
						codError=1013;
						logi.info("E S T A D O       C A R G U I O      I N C O R R E C T O     !!!!"); break;	
					}
					else if (existecarguio==-4) {
						codError=1012;
						logi.info("DV  RUT CHOFER  INCORRECTO  ó   RUT DE CHOFER FUÉ CAMBIADO"); break;
					}
					
					
					if (codError<=0) {
						ChoftranDTO choftrandto = choftrandao.obtenerDatos(rut, dv);
						userId=choftrandto.getNomChofer().trim();
						nombredeChofer=userId;
						int largousu = userId.length();
						if (largousu>10){
							userId=userId.substring(0, 10).trim();
						}
						userId="CONTROLENT";
					}					
					
					int existecarguioest = carguioddao.buscaExisteCarguioTransp(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(),patente);
					if (existecarguioest<=0) {
						logi.info("C A R G U I  O    N U M E R O     "+numeroCarg+" N O    E S T A   C O N     E S T A D O    C O R R E C T O   !!!!");
						codError=1013; break;
					} 
					else {
					
					entrawhy=0;
					logi.info("ANTES DE WHILE ITER2 SOL3");
					Iterator iter2 = cargu.getOrdenes().iterator();
					while (iter2.hasNext()){
						if (entrawhy==0){
							logi.info("ENTRA A WHILE ITER2 SOL3");
						}
						entrawhy=1;
						
						OrdTranspNcpDTO orden = (OrdTranspNcpDTO) iter2.next();
						
						String 	codigoEstadoOV="";
						String 	descriEstadoOV="";
						
						numdocu=orden.getNumDocumento();
						tipodocu=orden.getTipoDocumento();
						codigoEstadoOV=orden.getCodEstado().trim();
						descriEstadoOV=orden.getDescEstado().trim();
								
						numerodocto=numdocu;
						
						if (numdocu<=0) {
							logi.info("N U M E R O      D O C T O      E N      C E R O    !!!!!");
							codError=1004; break;
						}
						if (tipodocu<=0){
							logi.info("T I P O     D O C T O     E  N     C E R O      !!!!!");
							codError=1005; break;
						}
						if (("".equals(codigoEstadoOV))){
							logi.info("C O D I G O      E S T A D O      V A C I O   !!!!!!");
							codError=1006; break;
						}
						if (("".equals(descriEstadoOV))){
							logi.info("D E S C R I P C I O N     E S T A D O      V A C I O  !!!!");
							codError=1007; break;
						}
						
						
						Fecha 	fch = new Fecha();
						int 	fecha = Integer.parseInt(fch.getYYYYMMDD());
						int     horas = Integer.parseInt(fch.getHHMMSS());
						int		numnc=0, swcorre=0;
						int 	cantiarti=0, totbrutocab=0, totnetocab=0, totalbrut=0, totdesctonetocab=0;
						int		totdesctocab=0, desctonetolinea=0;
						int		numeroguia=0, numeroOVe=0, icantidad=0,	rutclient=0,icantirecep=0;
						double 	totcostocab=0, totcostonetocab=0, costototnet=0, totalneto=0, desctobrutlinea=0;
						String 	responsableNota="", digclient="";
						boolean generarNCp = true;
						boolean esrecepMayor = false;
						boolean	insertadoconnod = false;
						
						
						OrdTranspNcpDTO ordvtadto = ordvtadao.obtieneTotalesordenNumdoc(codigoEmpresa, codigoBodega, numdocu, orden.getTipoDocumento(),cargu.getNumeroCarguio(),rut,patente);
						if (ordvtadto==null){
							logi.info("N O     S E     H A N      E N C O N T R A D O    D A T O S     D E L       C A R G U I O     N u m e r o    : "+cargu.getNumeroCarguio());
							codError=1014; break;
						}
						numdocu=ordvtadto.getNumFactbol();
						numeroguia=ordvtadto.getNumeroGuia();
						numeroOVe=ordvtadto.getNumeroOV();
						rutclient=ordvtadto.getRutCliente();
						digclient=ordvtadto.getDvCliente();
						rutCli=rutclient;
						digCli=digclient;
						nomCli=ordvtadto.getNombreCliente().trim();
						codigoVendedor=ordvtadto.getCodigoVendedor();
						nombreVendedor=ordvtadto.getNombreVendedor();
						correoVendedor=ordvtadto.getCorreoVendedor();
						if (correoVendedor==null || ("".equals(correoVendedor.trim()))){
							logi.info("S  I  N       C  O  R  R  E  O       V  E  N  D  E  D  O  R  !!        SE ENVIARA A DESARROLLO");
							correoVendedor=correoDesarrollo;
						}
						numOV=ordvtadto.getNumeroOV();
						fecOV=ordvtadto.getFechaDespacho();
						horOV=ordvtadto.getHoraOV();
						fecEnt=ordvtadto.getFechaEntrega();
						fecEnt=fecha;
						horEnt=horas;
						fecDoc=ordvtadto.getFechaFactura();
						dirCli=ordvtadto.getDireccionCliente().trim();
						comuCli=ordvtadto.getDescripcionComuna().trim();
						monDoc=ordvtadto.getMontoFactura();
						nomChof=userId;
						monEnt=monDoc;
						estEntr="";
						estMoti="";
						numerodocto=numdocu;
						
						String nombreDocumen=tptdocdao.buscaDocumento(ordvtadto.getTipoDocumento());
						tipoDoc=Integer.toString(ordvtadto.getTipoDocumento())+"-"+nombreDocumen;
						
						logi.info("R U T CLIENTE    -------------------->   "+rutclient);
						logi.info("C L I E N T E    -------------------->   "+ordvtadto.getNombreCliente());
						logi.info("NUMERO  O V E    -------------------->   "+ordvtadto.getNumeroOV());
						logi.info("VERSION  '|'     -------------------->   "+versionpipe);
						logi.info("TIPO DOCUMENTO   -------------------->   "+tipoDoc);
						
						estadocargcab = carguiodao.obtieneEstadoCarguioCab(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), patente);
						if ("E".equals(estadocargcab)){
							estadocargcab="U";
						}
						else if ("L".equals(estadocargcab)){
							estadocargcab="O";
						} 
						if (estadocargcab==null || ("".equals(estadocargcab.trim()))){
							estadocargcab=estadocar;
						}
							
						estadocargdet = carguioddao.obtieneEstadoCarguioDet(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(),patente, numeroOVe);
						if ("E".equals(estadocargdet)){
							estadocargdet="K";
						}
						else if ("L".equals(estadocargdet)){
								estadocargdet="H";
						}
						else if ("J".equals(estadocargdet)){
							estadocargdet="F";
						}
						else if ("K".equals(estadocargdet)){
								estadocargdet="H";
						}
					
						if (estadocargdet==null || ("".equals(estadocargdet.trim()))){
							estadocargdet=estadocar;
						}
						
						
						CarguiodTranspDTO carguioddto = carguioddao.obtieneMotivoCarguioD(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), numerodocto, patente);
						codMotivoTabla=carguioddto.getCodMotivo();
						desMotivoTabla=carguioddto.getDesMotivo();
						
						
						if (numeroOVe<=0){
							logi.info("O V    E  N      C E R O       !!!!");
							codError=1025; break;
						}
						if (rutclient<=0){
							logi.info("R U T     C L I E N T E      E N   C E R O     !!!!!");
							codError=1026; break;
						}
						if (("".equals(digclient))){
							logi.info("D V     C L I E N T E    V A C I O     !!!!!");
							codError=1027; break;
						}
									
						
						/*
						 * valida que se haya despachado una vez. (es lo diferente con solicitud 2)
						 */
						int cantiredespac = carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, numeroCarg, numdocu,patente);
						if (cantiredespac==1){
							logi.info("OV : "+numeroOVe+ " NO HA PASADO POR SOLICITUD 2 PRIMERO !!!");
							//break;
						}
						//3
						else if (cantiredespac>3){
							carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe, cantiredespac);
							logi.info("OV : "+numeroOVe+ " YA TIENE REDESPACHO !!!  CANTIDAD: "+cantiredespac);
							//break;    ENVIAR CORREO A DESARROLLO
						} 
						 else {
							 
							/*
							 * VALIDA QUE EL CARGUIO Y OV EXISTA (CASO PARA MAS DE 1 CARGUIO POR RUT DE CHOFER)
							 */
							int escarguiocorrecto = (carguiodao.obtieneCarguioyOrdenes(codigoEmpresa, cargu.getNumeroCarguio(), codigoBodega, numeroOVe,patente,rutclient));
							if (escarguiocorrecto==1){
								
								/*
								 * validar si es ET: entrega total ; EP: entrega parcial ; RC : rechazo total
								 */
								String 	codigoMotivoOV="", descriMotivoOV="";
								int 	cantiart=0,	cantirec=0, codiarti=0,	corrarti=0;
								String	formarti="", latitud="", longitud="", distancia="";
								String 	estadotipoentrega="ET";
								String 	estadodescentrega="ENTREGADO";
								int tipodespa=2;
								estEntr=estadotipoentrega+"-"+estadodescentrega;
								
								if (cantiredespac>3){
									tipodespa=3;
								}
								codigoEstadoOV=orden.getCodEstado();
								descriEstadoOV=orden.getDescEstado();
								codigoMotivoOV=orden.getCodMotivo();
								descriMotivoOV=orden.getDesMotivo();
	 							numdocu=orden.getNumDocumento();
								tipodocu=orden.getTipoDocumento();
								latitud=orden.getLatitud();
								longitud=orden.getLongitud();
								distancia=orden.getDistancia();
								String fechastamp="";
								if (orden.getTimestamp().trim()!=null){
									String anostamp = orden.getTimestamp().substring(0,4).trim() ;
									String messtamp = orden.getTimestamp().substring(5,7).trim();
									String diastamp = orden.getTimestamp().substring(8,10).trim();
									fechastamp=anostamp+messtamp+diastamp;
								}
								if (fechastamp==""){
									fechastamp=String.valueOf(fecha);
								}
								
								entrawhy=0;
								logi.info("ANTES DE WHILE ITER9 orden SOL3");
								boolean vienecondetalle=false;
								Iterator<?> iter9 = orden.getDetord().iterator();
								while (iter9.hasNext()){
									if (entrawhy==0){
										logi.info("ENTRA A WHILE ITER9 orden SOL3");
									}
									entrawhy=1;
									
									vienecondetalle=true;
									esrecepMayor = false;
									DetordTranspNcpDTO detalleinput = (DetordTranspNcpDTO) iter9.next();
									codiarti=Integer.parseInt(detalleinput.getCodigoArticulo());
									corrarti=Integer.parseInt(detalleinput.getCorrelativo());
									formarti=detalleinput.getFormato();
									cantiart=detalleinput.getCantidad();
									cantirec=detalleinput.getCantidadrecepcionada();
									if (cantirec>cantiart){
										cantirec=cantiart;
										esrecepMayor = true;
									}
									OrdvdetDTO ordvdetdto = new OrdvdetDTO();
									ordvdetdto.setEmpresa(codigoEmpresa);
									ordvdetdto.setNumeroOV(numeroOVe);
									ordvdetdto.setRutCliente(rutclient);
									ordvdetdto.setDigCliente(digclient);
									ordvdetdto.setCodigoBodega(codigoBodega);
									ordvdetdto.setDetalledespacho(0);
									ordvdetdto.setTipoDespacho(tipodespa);
									ordvdetdto.setCorrelativo(corrarti);
									ordvdetdto.setCodigoArticulo(codiarti);
									ordvdetdto.setFormatoArticulo(formarti);
									ordvdetdto.setCantidadArticulo(cantiart);
									ordvdetdto.setCantidadRecepcionada(cantirec);
									ordvdetdto.setFechauser(fecha);
									ordvdetdto.setHorauser(horas);
									ordvdetdto.setUsuario(userId);
									int existetbl = ordvdetdao.buscaOrdvdet(ordvdetdto);
									if (existetbl==0) {
										ordvdetdao.insertaOrdvdet(ordvdetdto);
									}
									else if (existetbl==1){
										ordvdetdao.actualizaOrdvdet(ordvdetdto);
									}
									
									if (cantirec==0 && "ET".equals(estadotipoentrega)){
										estadotipoentrega="VB";
										estadodescentrega="VB CALL CENTER";
										estEntr=estadotipoentrega+"-"+estadodescentrega;
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										if ("".equals(descriMotivoOV)){
											estMoti=codMotivoTabla+"-"+desMotivoTabla;
											codigoMotivoOV=codMotivoTabla;
											descriMotivoOV=desMotivoTabla;
										}
									}
									else if (cantirec>0 && (cantirec!=cantiart)){
										estadotipoentrega="EP";
										estadodescentrega="ENTREGA PARCIAL";
										monEnt=0;
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										estEntr=estadotipoentrega+"-"+estadodescentrega;
										}
									
									
									if (cantirec==0 && cantiart>0 && ("EP".equals(codigoEstadoOV))){
										estadotipoentrega="EP";
										estadodescentrega="ENTREGA PARCIAL";
										monEnt=0;
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										estEntr=estadotipoentrega+"-"+estadodescentrega;
									}

									//cuando la cantidad del celular es mayor a lo generado en syscon
									if (esrecepMayor==true){
										estadotipoentrega="EP";
										estadodescentrega="ENTREGA PARCIAL";
										monEnt=0;
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										estEntr=estadotipoentrega+"-"+estadodescentrega;
									}

									
								}
								
								if (vienecondetalle==false){
									logi.info("SIN DETALLE !!!!! EL CARGUIO Numero : "+cargu.getNumeroCarguio());
									codError=1030; break;
								}
													
									if ("ET".equals(estadotipoentrega)){
										generarNCp=false;
										codigoMotivoOV="";
										descriMotivoOV="";
										estMoti="";
										
									} else if ("VB".equals(estadotipoentrega)){
										if ("P".equals(codigoMotivoOV) || "Q".equals(codigoMotivoOV) || "E".equals(codigoMotivoOV)){
											estadotipoentrega="RC";
											estadodescentrega="RECHAZADO";
											generarNCp=true;
											monEnt=0;
											estMoti=codigoMotivoOV+"-"+descriMotivoOV;
											estEntr=estadotipoentrega+"-"+estadodescentrega;
											if ("".equals(descriMotivoOV)){
												estMoti=codMotivoTabla+"-"+desMotivoTabla;
												codigoMotivoOV=codMotivoTabla;
												descriMotivoOV=desMotivoTabla;
											}
										}
										else {
											generarNCp=false;
											estMoti=codigoMotivoOV+"-"+descriMotivoOV;
											estEntr="RC-RECHAZADO";
											estEntr="VB CALL CENTER";
											monEnt=0;
										}
									}
									else {
										generarNCp=true;
									}
									
								estEntr=estadotipoentrega+"-"+estadodescentrega;
								
								if ("ET".equals(codigoMotivoOV)){
									estEntr="ET-ENTREGADO";
									estadotipoentrega="ET";
									estadodescentrega="ENTREGADO";
									estMoti="";
								}
								
								cantiredespa = carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), numdocu,patente);
								carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe, cantiredespac);
								//3
								if (cantiredespa>3){
										logi.info("OV : "+numeroOVe+ " YA TIENE REDESPACHO, CARGUIOD XONM1 !!!");
										codError=1028; break;
								}
								else {
									/*
									* inserta en tabla ordvdde, timestamp, motivo, estado, latitud, longitud, distancia, foto, comentario
									*/
									OrdvddeDTO ordvddedto = new OrdvddeDTO();
									ordvddedto.setEmpresa(codigoEmpresa);
									ordvddedto.setNumeroOV(numeroOVe);
									ordvddedto.setRutCliente(rutclient);
									ordvddedto.setDigCliente(digclient);
									ordvddedto.setCodigoBodega(codigoBodega);
									ordvddedto.setDetalledespacho(0);
									ordvddedto.setTipoDespacho(tipodespa);
									ordvddedto.setFechaConfirmacion(Integer.parseInt(fechastamp));
									ordvddedto.setLatitud(latitud);
									ordvddedto.setLongitud(longitud);
									ordvddedto.setDistancia(distancia);
									ordvddedto.setCodEstado(estadotipoentrega);
									ordvddedto.setDesEstado(estadodescentrega);
									ordvddedto.setCodMotivo(codigoMotivoOV);
									ordvddedto.setDesMotivo(descriMotivoOV);
														
									//FOTOS Y COMENTARIOS ,DESCOMENTAR CUANDO SE GUARDE EN NUEVA TABLA
									int ii=1;
									Iterator iter44 =orden.getDetfoto().iterator();
									while (iter44.hasNext()){
										DetordTranspNcpDTO detallefoto = (DetordTranspNcpDTO) iter44.next();
										if (ii==1){
											ordvddedto.setFoto1(detallefoto.getFoto());
											ordvddedto.setComentario1(detallefoto.getComentario());
										}
										else if (ii==2){
											ordvddedto.setFoto2(detallefoto.getFoto());
											ordvddedto.setComentario2(detallefoto.getComentario()); }
										else if (ii==3){
											ordvddedto.setFoto3(detallefoto.getFoto());
											ordvddedto.setComentario3(detallefoto.getComentario()); }
										else if (ii==4){
											ordvddedto.setFoto4(detallefoto.getFoto());
											ordvddedto.setComentario4(detallefoto.getComentario()); }
										else if (ii==5){
											ordvddedto.setFoto5(detallefoto.getFoto());
											ordvddedto.setComentario5(detallefoto.getComentario()); }
										else if (ii==6){
											ordvddedto.setFoto6(detallefoto.getFoto());
											ordvddedto.setComentario6(detallefoto.getComentario()); }
										ii++;
									}
									ordvddedto.setFechauser(fecha);
									ordvddedto.setHorauser(horas);
									int existetbl = ordvddedao.buscaOrdvdde(ordvddedto);
									if (existetbl==0) {
										ordvddedao.insertaOrdvdde(ordvddedto);
									}
									else if (existetbl>=1){
										ordvddedao.actualizaOrdvdde(ordvddedto);
									}
										
									carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe, cantiredespa);  //2
									
									entrawhy=0;
									logi.info("ANTES DE WHILE ITER4 SOL3");
									Iterator iter4 = orden.getDetord().iterator();
									while (iter4.hasNext()){
										if (entrawhy==0){
											logi.info("ENTRA A WHILE ITER4 SOL3");
										}
										entrawhy=1;
										DetordTranspNcpDTO detalleinput = (DetordTranspNcpDTO) iter4.next();
										icantidad=detalleinput.getCantidad();
										icantirecep=detalleinput.getCantidadrecepcionada();
										if (icantirecep>icantidad){
											icantirecep=icantidad;
										}
										
										if (generarNCp==false){
											int respactestad2 = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV, codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, Integer.parseInt("7777777"),numeroguia);
											int respactestado = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV, codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, 0,numeroguia);
										}
										
										
										if (generarNCp==true){
											
											if (icantirecep<icantidad){
												
											
												if (swcorre==0){
													numnc=(tpacordao.recupeCorrelativo(0,8));
													if (numnc<=0){	
														logi.info("ERROR!! NO SE HA GENERADO CORRELATIVO PARA LA NCP");
														codError=1029; break;
													}
													else {
														int existenum = connohdao.buscaExisteConnoh(codigoEmpresa, numnc);
														if (existenum>0){
															numnc=(tpacordao.recupeCorrelativo(0,8));
														}
													}
													
													int existenumerodenc=0;
													for(int i=0;i<50;i++)
													{
														existenumerodenc = connohdao.buscaExisteConnoh(codigoEmpresa, numnc);
														if (existenumerodenc>0){
															numnc=(tpacordao.recupeCorrelativo(0,8));
														}
														else {
															break;
														}
													}		
													swcorre=1;
												}
												
												
												logi.info("ANTES DE WHILE ITER5 SOL3");
												List listadetordp;
												listadetordp = detorddao.obtieneTotalesdetalleNumdoc(codigoEmpresa, numeroOVe, codigoBodega, ordvtadto.getRutCliente(), Integer.parseInt(detalleinput.getCodigoArticulo()), icantidad);
												if (listadetordp.isEmpty()){
													logi.info("ENTRA A ISEMPTY ITER5 SOL3");
													listadetordp = detorddao.obtieneTotalesdetalleNumdoc2(codigoEmpresa, numeroOVe, codigoBodega, ordvtadto.getRutCliente(), Integer.parseInt(detalleinput.getCodigoArticulo()), icantidad);
												}
												entrawhy=0;
												Iterator iter5 = listadetordp.iterator();
												while (iter5.hasNext()) {
													if (entrawhy==0){
													logi.info("ENTRA A WHILE ITER5 SOL3");
													}
													entrawhy=1;
													
													DetordTranspNcpDTO detorden2 = (DetordTranspNcpDTO) iter5.next();
																		
													ConnodDTO connoddto = new ConnodDTO();
													NcplogDTO ncplogdto = new NcplogDTO();
													
													connoddto.setCodigoEmpresa(carguiocdto.getCodigoEmpresa());
													connoddto.setTipoNota("P");
													connoddto.setNumeroNota(numnc);
													connoddto.setFechaNota(fecha);
													connoddto.setCorrelativo(Integer.parseInt(detorden2.getCorrelativo()));
													connoddto.setCodArticulo(Integer.parseInt(detorden2.getCodigoArticulo()));
													connoddto.setDigArticulo(detorden2.getDigitoArticulo());
													connoddto.setDescripcion(detorden2.getDescripcionArticulo());
													if (icantirecep>icantidad){
														icantirecep=icantidad;
													}
													int cantidevo=0;
				 									if (icantirecep<=0){ 
				 										cantidevo=icantidad;
													}
													else { 
														cantidevo=icantidad-icantirecep;
													}
													connoddto.setCantidad(cantidevo);
													connoddto.setFormato(detorden2.getFormato());
													connoddto.setPrecioUnitario(detorden2.getPrecioUnitario());
													connoddto.setPrecioNeto(detorden2.getPrecioNeto());
													connoddto.setCostoArticulo(detorden2.getCostoArticulo());
													connoddto.setCostoNetoUnitario(detorden2.getCostoNetoUnitario());
													connoddto.setMontoNeto(detorden2.getMontototalNeto());
													connoddto.setMontoExento(detorden2.getTotalExento());
													if (detorden2.getCostoTotalNeto()<=1){
														costototnet=cantidevo*detorden2.getCostoTotalNeto();
													} else {
														costototnet=detorden2.getCostoTotalNeto();
													}
													costototnet=detorden2.getCostoTotalNeto();
													totalneto=Math.round(cantidevo*detorden2.getPrecioNeto());
													totalbrut=(int) (cantidevo*detorden2.getPrecioUnitario());
													connoddto.setCostoTotalNeto(costototnet);
													connoddto.setMontoNeto((int)(totalneto));
													connoddto.setTotalNeto((int)(totalneto));
													
													
													if ("7777777".equals(detalleinput.getCodigoArticulo())){
														List listacldmco = cldmcodao.obtieneFleteCldmco(codigoEmpresa, tipodocu, rutclient, digclient, fecDoc, codigoBodega, numdocu, Integer.parseInt(detalleinput.getCodigoArticulo()));
														Iterator iterclc = listacldmco.iterator();
														while (iterclc.hasNext()) {
															CldmcoDTO cldmco2 = (CldmcoDTO) iterclc.next();
															connoddto.setPrecioUnitario(cldmco2.getPrecio());
															connoddto.setPrecioNeto(cldmco2.getPrecioNeto());
															connoddto.setMontoNeto(cldmco2.getValorNeto());
															connoddto.setMontoExento(cldmco2.getMontoExento());
															totalneto=Math.round(cantidevo*cldmco2.getPrecioNeto());
															totalbrut=cldmco2.getMontoCompra();
															connoddto.setMontoNeto((int)(totalneto));
															connoddto.setTotalNeto((int)(totalneto));
														}
													}
													//logi.info("CODART|CORRE|FORMATO|CANTI|CANTIRECEP  ----->>  "+detalleinput.getCodigoArticulo()+"|"+detorden2.getCorrelativo()+"|"+detorden2.getFormato()+"|"+detorden2.getDescripcionArticulo()+"|"+icantidad+"|"+icantirecep);
													VedmarDTO vedmardto = new VedmarDTO();
													double  impuestos = vedmardao.calculaImpuestosArticulo(Integer.parseInt(detalleinput.getCodigoArticulo()), detorden2.getDigitoArticulo());
																		
													connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
													//1 peso
													//Sin descuento
													if (detorden2.getTotalDescuento()<=0 && detorden2.getTotalDescuentoNeto()<=0){
														connoddto.setTotalDescuento(detorden2.getTotalDescuento());
														connoddto.setTotalDescuentoNeto(detorden2.getTotalDescuentoNeto());
													} else {
							     						//Con descuento
														if (icantidad!=icantirecep){
															desctonetolinea=(int) Math.round((totalneto*(detorden2.getPorcentajeDescuento()/100)));
															desctobrutlinea=Math.round(desctonetolinea*impuestos);
															connoddto.setTotalDescuento((int) desctobrutlinea);
															connoddto.setTotalDescuentoNeto(desctonetolinea);  
															totalneto=totalneto-desctonetolinea;
															connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
															connoddto.setMontoNeto((int)(totalneto));
															connoddto.setTotalNeto((int)(totalneto)+desctonetolinea);
															totdesctonetocab=totdesctonetocab+desctonetolinea;
															totdesctocab=(int) (totdesctocab+desctobrutlinea);
														}
														else if (icantidad==icantirecep){
															totdesctonetocab=totdesctonetocab+detorden2.getTotalDescuentoNeto();
															totdesctocab=totdesctocab+detorden2.getTotalDescuento();
															connoddto.setTotalDescuento(detorden2.getTotalDescuento());
															connoddto.setTotalDescuentoNeto(detorden2.getTotalDescuentoNeto());  
															totalneto=totalneto-detorden2.getTotalDescuentoNeto();
															connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
															connoddto.setMontoNeto((int)(totalneto));
															connoddto.setTotalNeto((int)(totalneto)+detorden2.getTotalDescuentoNeto());
															}
														}
													
													
													/*
														//cuando es combo, calcular neto y bruto en tables EXDACP y EXDACB
														if ("C".equals(detorden2.getCombo())) {
															connoddto.setFormato("X");
															int a;
															ExdacpDTO exdacpdto = exdacpdao.calculamontosCombo(codigoEmpresa, Integer.parseInt(detalleinput.getCodigoArticulo()), detorden2.getDigitoArticulo(), codigoBodega,2000 , cantidevo, (a = (int) detorden2.getPorcentajeDescuento()));
															pnetos=exdacpdto.getPrecioNeto();
															pventa=exdacpdto.getPrecioVenta();
															totalneto=Math.round(pnetos);
															totalbrut=(int) (pventa);
															connoddto.setMontoNeto((int)(totalneto));
															connoddto.setTotalNeto((int)(totalneto));
															connoddto.setTotalLinea((int)(Math.round(totalbrut)));
														}
													*/
														
														//Insert in Connod
														ConnohDTO connohdto2 = new ConnohDTO();
														connohdto2.setCodDocumento(tipodocu);
														connohdto2.setNumeroDocumento(numdocu);
														connohdto2.setRutCliente(rutclient);
														connohdto2.setTipoNota("P");
														connohdto2.setCodigoBodega(codigoBodega);
														existeconnoh = connohdao.existeConnoh(connohdto2);
														if (existeconnoh>0){
															connoddto.setNumeroNota(existeconnoh);
															int existeconnod = connoddao.buscaExisteConnod(connoddto);
															if (existeconnod>0) {
																int eliconnod=connoddao.eliminaConnod(connoddto);
															}
															connoddto.setNumeroNota(numnc);
														}
														int respncp = connoddao.insertaConnod(connoddto);
														insertadoconnod=true;
														if (respncp>=0){
															ncplogdto.setCodigoEmpresa(connoddto.getCodigoEmpresa());
															ncplogdto.setTipo("P");
															ncplogdto.setNumeroNota(connoddto.getNumeroNota());
															ncplogdto.setFechaNota(connoddto.getFechaNota());
															ncplogdto.setLineaNota(connoddto.getCorrelativo());
															ncplogdto.setNumeroCarguio(cargu.getNumeroCarguio());
															ncplogdto.setNumeroOrden(ordvddedto.getNumeroOV());
															ncplogdto.setNumeroDocumento(ordvtadto.getNumFactbol());
															ncplogdto.setCodigoBodega(ordvddedto.getCodigoBodega());
															ncplogdto.setRutCliente(ordvtadto.getRutCliente());
															ncplogdto.setDigCliente(ordvtadto.getDvCliente());
															ncplogdto.setUsuario(userId);
															ncplogdto.setTipoAccion("I");
															ncplogdto.setCodigoArticulo(connoddto.getCodArticulo());
															ncplogdto.setDigitoArticulo(connoddto.getDigArticulo());
															ncplogdto.setCantidad(cantidevo);
															ncplogdto.setCantidadArticulo(connoddto.getCantidad());
															ncplogdto.setFechaUser(fecha);
															ncplogdto.setHoraUser(horas);
															ncplogdto.setIpEquipo(Integer.toString(cargu.getRutChofer()));
															ncplogdto.setNombreEquipo(carguiocdto.getPatente().trim());
															ncplogdto.setCodigoUsuario(userId);
															int correlativoncplog = ncplogdao.buscaUltimaLineaNcplog(ncplogdto);
															ncplogdto.setLineaNota(correlativoncplog);
															int resplog = ncplogdao.insertaNcplog(ncplogdto);
														}
														
														if (respncp<0){	logi.info("ERROR!! NO SE HA GENERADO CONNOD NCP");break; }
															//Actualizar Estado CarguioD
															int respactestad2 = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV,codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, Integer.parseInt("7777777"),numeroguia);
															int respactestado = carguioddao.actualizaEstadoArticuloCarguiodTransp(estadotipoentrega,codigoMotivoOV,codigoEmpresa,cargu.getNumeroCarguio(), patente, codigoBodega, numeroOVe, rutclient, 0,numeroguia);   //Integer.parseInt(detalleinput.getCodigoArticulo())
		
															int tiporede=0;
															tiporede=cantiredespa;
															if (tiporede==2){
																tiporede=4;
															}
															carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe, tiporede);
															
															//Acumular totales para connoH
															cantiarti++;
															totcostocab= totcostocab+Math.floor(detorden2.getCostoArticulo());
															totcostonetocab=totcostonetocab+costototnet;
															if ("C".equals(detorden2.getCombo())) {
																totbrutocab=totbrutocab+(int)(Math.round(totalbrut));
															} else {
																totbrutocab=totbrutocab+(int)(Math.round(totalneto*impuestos));
															}
															totnetocab=(int) (totnetocab+totalneto);
												} //iter5
										}//canti
									} //iter4
								} //generarncp
													
										if (generarNCp==true && insertadoconnod==true){
											ConnohDTO connohdto = new ConnohDTO();
											connohdto.setTipoNota("P");
											connohdto.setNumeroNota(numnc);
											connohdto.setFechaNota(fecha);
											connohdto.setCodDocumento(ordvtadto.getTipoDocumento());
											connohdto.setNumeroDocumento(ordvtadto.getNumFactbol());
											connohdto.setRutCliente(ordvtadto.getRutCliente());
											connohdto.setDivCliente(ordvtadto.getDvCliente());
											connohdto.setCodigoMovimiento(2);
											connohdto.setCodigoBodega(codigoBodega);
											connohdto.setCodigoVendedor(ordvtadto.getCodigoVendedor());
											connohdto.setNombreCliente(ordvtadto.getNombreCliente());
											connohdto.setCantidadLineas(cantiarti);
											connohdto.setTotalCosto((int)(totcostocab));
											connohdto.setTotalCostoNeto((int)(totcostonetocab));
											if (totdesctonetocab>0){
												connohdto.setTotalDescuento(totdesctocab);
												connohdto.setTotalDescuentoNeto(totdesctonetocab);
											} else {
												connohdto.setTotalDescuento(ordvtadto.getTotalDescuento());
												connohdto.setTotalDescuentoNeto(ordvtadto.getTotalDescuentoneto());
											}
											connohdto.setMontoTotal(totbrutocab);
											connohdto.setMontoNeto(totnetocab);
											connohdto.setMontoIva(0);
											connohdto.setMontoExento(ordvtadto.getTotalExento());
											connohdto.setEstado("I");
											monEnt=Math.round(monDoc-totbrutocab);
											String codmotov="";
											codmotov=orden.getCodMotivo();
											if (codigoMotivoOV != null && !codigoMotivoOV.equals("")){
												codmotov=codigoMotivoOV;
											}
											String userId2 = String.format("%-29s", nombredeChofer);
											responsableNota=(userId2.substring(0, 29)+codmotov+cargu.getNumeroCarguio()).trim();
											connohdto.setResponsableNota(responsableNota);
											/*
											connohdto.setNumeroCarguio(cargu.getNumeroCarguio());
											connohdto.setNumeroOV(numeroOVe);
											connohdto.setMotivoNoventa(codigoMotivoOV);
											connohdto.setUsuarioNota(userId.trim());
											connohdto.setUsuarioFecha(fecha);
											connohdto.setUsuarioHora(horas);
											*/
											//Insert in Connoh
											if (existeconnoh>0){
												connohdto.setNumeroNota(existeconnoh);
												int existeconnoh2 = connohdao.existeConnoh(connohdto);
												if (existeconnoh2>0) {
													int eliconnoh=connohdao.eliminaConnoh(connohdto);
												}
												connohdto.setNumeroNota(numnc);
											}
											connohdao.insertaConnoh(connohdto);
											
											numerNCpe=numnc;
											totalNCpe=totbrutocab;
											
											DocncpDTO docncpdto = new DocncpDTO();
											docncpdto.setTipoNota("P");
											docncpdto.setNumeroNota(numnc);
											docncpdto.setFechaNota(fecha);
											docncpdto.setCodigoBodega(codigoBodega);
											docncpdto.setRutCliente(ordvtadto.getRutCliente());
											docncpdto.setDigCliente(ordvtadto.getDvCliente());
											docncpdto.setCorrelativo(1);
											docncpdto.setNumeroOV(numeroOVe);
											docncpdto.setNumeroCarguio(cargu.getNumeroCarguio());
											docncpdto.setNumeroDocumento(ordvtadto.getNumFactbol());
											docncpdto.setNumeroGuia(numeroguia);
											docncpdto.setNumeroNcfinal(0);
											docncpdto.setFechaUsuario(fecha);
											docncpdto.setHoraUsuario(horas);
											docncpdto.setCodigoUsuario(userId);
											docncpdto.setMotivo(codmotov);
											//Insert in DOCNCP
											if (existeconnoh>0){
												docncpdto.setNumeroNota(existeconnoh);
											}
											int existedocncp = docncpdao.buscaDocncpDTO(docncpdto);
											if (existedocncp>0) {
												int elidocncp=docncpdao.eliminaDocncpDTO(docncpdto);
											}
											docncpdto.setNumeroNota(numnc);
											docncpdao.insertaDocncp(docncpdto);
																
											CargcestDTO cargcestdto = new CargcestDTO();
											cargcestdto.setCodigoEmpresa(codigoEmpresa);
											cargcestdto.setNumcarguio(cargu.getNumeroCarguio());
											cargcestdto.setPatente(patente);
											cargcestdto.setCodigoBodega(codigoBodega);
											int correcargcest = cargcestdao.obtieneCorrelativo(codigoEmpresa, cargu.getNumeroCarguio(), patente, codigoBodega);
											cargcestdto.setCorrelativo(correcargcest);
											cargcestdto.setEstado(estadocargcab);
											cargcestdto.setFechaUsuario(fecha);
											cargcestdto.setHoraUsuario(horas);
											cargcestdto.setUsuario(userId);
											//Insert Table Cargcest
											int exiscargcest = cargcestdao.existeCargcestDTO(cargcestdto);
											if (exiscargcest<=0) {
											cargcestdao.insertaCargcest(cargcestdto);
											}
											
											//update status CarguioC y CarguioD
											/*
											String estadocarguioc="";
											String estadocarguiod="";
											if ("E".equals(estadocar)){
												estadocarguioc="U";
												estadocarguiod="K";
											}
											else if ("L".equals(estadocar)){
												estadocarguioc="O";
												estadocarguiod="H";
											}
											if ( (estadocarguioc != null) && (!estadocarguioc.equals("")) ) { carguiodao.actualizarestadoCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocarguioc);}
											if ( (estadocarguiod != null) && (!estadocarguiod.equals("")) ) { carguiodao.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocarguiod,numeroOVe);}
											*/
											
											carguiodao.actualizarestadoCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocargcab);
											carguiodao.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocargdet,numeroOVe);
											
											carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe,rutclient,1);
											
											
											//consolidado
											String todasNCpes = docncpdao.obtenerNcpes(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio()).trim();
											if (!todasNCpes.equals("")){
												
												CargconwDTO carcondtoeli = new CargconwDTO();
												carcondtoeli.setCodigoEmpresa(codigoEmpresa);
												carcondtoeli.setNumcarguio(numeroCarg);
												carcondtoeli.setPatente(patente);
												carcondtoeli.setCodigoBodega(codigoBodega);
												int elitodo=cargcondao.eliminaCargconwTransp(carcondtoeli);
												
												List listconsoli = connoddao.obtieneConsolidadoNCp(todasNCpes, codigoEmpresa, cargu.getNumeroCarguio(), codigoBodega);
												Iterator iter6 = listconsoli.iterator();
												while (iter6.hasNext()){
													ConnodDTO listacon = (ConnodDTO) iter6.next();
													
													CargconwDTO carcondto = new CargconwDTO();
													carcondto.setCodigoEmpresa(codigoEmpresa);
													carcondto.setNumcarguio(cargu.getNumeroCarguio());
													carcondto.setPatente(patente);
													carcondto.setCodigoBodega(codigoBodega);
													carcondto.setCodigoArticulo(listacon.getCodArticulo());
													carcondto.setDigitoArticulo(listacon.getDigArticulo());
													carcondto.setTipoCarguio("C");
													carcondto.setCantidadArticulo(listacon.getCantidad());
													carcondto.setFechaDevolucion(fecha);
													carcondto.setCantidadConfirmada(0);
													carcondto.setCantidadDiferencia(0);
													int fechaExpiracion = carguioddao.obtieneFechaVencimientoArt(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), listacon.getCodArticulo());
													if (fechaExpiracion<=0){
														fechaExpiracion=fecha;
														//sumar 90 dias
														String fechamas="";
														Date	resulfecha;
														Date date = new Date(fecha);
														resulfecha = sumarfechas(date, diasSumar);
														//logi.info(resulfecha);
														fechamas= (resulfecha.toString());
														String anofec = fechamas.substring(0,4).trim() ;
														String mesfec = fechamas.substring(5,7).trim();
														String diafec = fechamas.substring(8,10).trim();
														fechaExpiracion=Integer.parseInt(anofec+mesfec+diafec);
													}
													carcondto.setFechaExpiracion(fechaExpiracion);
													carcondto.setPrecioNeto(listacon.getPrecioNeto());
													carcondto.setPrecioBruto(listacon.getPrecioUnitario());
													int exitbl = cargcondao.buscaCargconwTransp(carcondto);
													if (exitbl>=0){
														exitbl=exitbl-listacon.getCantidad();
														carcondto.setCantidadArticulo(exitbl);
														int canti=cargcondao.actualizaCargconw(carcondto);
													}
													else {
														cargcondao.insertaCargconw(carcondto);
													}
													
												}
											}

											logi.info("\n");
											logi.info(" -------->   O.K.  NCp    G E N E R A D A     <--------   "+numnc);
											logi.info("\n");
																
										} //generarncp
												
								} //cantiredespa<=0
						
						
							}
								else {
									logi.info("N O    E X I S T E      C A R G U I O    !!!");
									codError=1010;
									break;
							}  //escarguiocorrecto
							
						 } //no tenga ncp ya creada
									
												
					} //iter2
							
				} //iter
					
			} //existecarguio
				
		} //coderror		

		
		if (codError>0){
			ErrorTransportistaDAO errordao = dao.getErrorTransportistaDAO();
			List listita = errordao.buscaErrorTransportista(numeroCarg, codError); 
			String respdd = gson.toJson(listita);
			resp=respdd;
			logi.info("E R R O R : "+resp);
			
			EmailDTO emaildto = new EmailDTO();
			EnvioMailErrorTransportista enviomail = new EnvioMailErrorTransportista();
			enviomail.mail("SOLICITUD 3", numeroCarg, resp,par000);
			
		}
		else {
			generatxt(3, par000, String.valueOf(rut), dv,numerodocto, numeroCarg);
			
			String numerodocto2="";
			if (Integer.parseInt(versionpipe)>0){
				numerodocto2=Integer.toString(numerodocto)+"|"+versionpipe;
			} else {
				numerodocto2=Integer.toString(numerodocto);
			}
			
			List comple = carguioddao.buscaCarguiodTransp(codigoEmpresa, rut, dv, codigoBodega,2,numerodocto2, numeroCarg,patente);
			String respd = gson.toJson(comple);
			resp=respd;
			generatxt(33, respd, String.valueOf(rut), dv, numerodocto,numeroCarg);
			
			logi.info("E S T A D O   ------------------->   "+estEntr);
			logi.info("M O T I V O   ------------------->   "+estMoti);

			if (codError<=0){
				logi.info("E N V I A N D O     C O R R E O    A  ......   "+correoVendedor);
				EmailDTO emaildto = new EmailDTO();
				EnvioMailTransportista enviomail = new EnvioMailTransportista();
				
				if ((nombredeChofer != null) && (!nombredeChofer.equals(""))){
					nomChof=nombredeChofer;
				}
				enviomail.mail("", codigoVendedor, nombreVendedor, correoVendedor, 0, numOV, fecOV, horOV, fecEnt, horEnt, numerodocto2, tipoDoc, fecDoc, rutCli, digCli, nomCli, dirCli, comuCli, monDoc, monEnt, nomChof, estEntr, estMoti, numerNCpe, totalNCpe,numeroCarg);
				if ("ET-ENTREGADO".equals(estEntr)){
					carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, numeroCarg, numOV,rutCli,1);
				}
				logi.info("correo enviado   S O L I C I T U D    3   !!!!");
				
			}
			
		}
			
						
								
			

			
			
			
			
			
		/*
		 * S  O  L  I  C  I  T  U  D          4
		 */
		}else if (rut>0 && "4".equals(solicitud) && numerodeCarguio>0 && numerodeDocumento>0){
			
			/*
			CarguioDAO carguiodao = dao.getCarguioDAO();
			CarguiodDAO carguioddao = dao.getCarguiodDAO();
			
			List compleredespa = carguioddao.buscaCarguiodRedespacho(codigoEmpresa, codigoBodega, rut, dv, numerodeCarguio, numerodeDocumento);
			String respredesp = gson.toJson(compleredespa);
			logi.info("ENVIO DE REDESPACHO DE LA CASERITA A QUADMINDS 4: "+respredesp);
			
			int cantiredespa = carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, numerodeCarguio, numerodeDocumento);
			int numeroOVrede = carguioddao.obtieneNumeroOVRedespacho(codigoEmpresa, codigoBodega, rut, dv, numerodeCarguio, numerodeDocumento);
			if (cantiredespa>0 && numeroOVrede>0){
				carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numerodeCarguio, numeroOVrede, cantiredespa);
			}
			resp=respredesp;
			generatxt(4, respredesp, String.valueOf(rut), dv);
			*/
		
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		/*
		 * S  O  L  I  C  I  T  U  D          5
		 */
		}else if (rut>0 && "5".equals(solicitud)){
			
			CarguioDAO 	carguiodao = dao.getCarguioDAO();
			CarguiodDAO carguioddao = dao.getCarguiodDAO();
			OrdvtaDAO 	ordvtadao = dao.getOrdvtaDAO();
			DetordDAO 	detorddao = dao.getDetordDAO();
			ConnohDAO 	connohdao = dao.getConnohDAO();
			ConnodDAO 	connoddao = dao.getConnodDAO();
			CamtraDAO 	camtradao = dao.getCamtraDAO();
			VecmarDAO 	vecmardao = dao.getVecmarDAO();
			VedmarDAO 	vedmardao = dao.getVedmarDAO();
			TpacorDAO 	tpacordao = dao.getTpacorDAO();
			DocncpDAO 	docncpdao = dao.getDocncpDAO();
			CargcestDAO cargcestdao = dao.getCargcestDAO();
			ExdacpDAO 	exdacpdao = dao.getExdacpDAO();
			OrdvddeDAO 	ordvddedao = dao.getOrdvddeDAO();
			OrdvdetDAO 	ordvdetdao = dao.getOrdvdetDAO();
			RespquadDAO respquadDAO = dao.getRespquadDAO();
			CargconwDAO cargcondao = dao.getCargconwDAO();
			NcplogDAO 	ncplogdao = dao.getNcplogDAO();
			
			
			
			if ("".equals(dv)){
				ChoftranDTO choftrandto2 = choftrandao.obtenerDigitoRut(rut);
				dv=choftrandto2.getDvChofer().trim();	
			}
			
	
			IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
			
			ConvierteTranspDTO convi = new ConvierteTranspDTO();
			cl.caserita.dto.DocumentoTranspDTO docutransp =  convi.convierte(par000,dv);
				
			if (Integer.parseInt(docutransp.getSolicitud())>999) {
				codError=Integer.parseInt(docutransp.getSolicitud());
			}
			
			
		String	patente="";
		int 	cantidadredespacho=0;
		boolean solicitudderedespacho = false;
		int		numeritocarguio=0;
		int	 	numeritodocto=0;
		int		existeconnoh=0;
		
		int		numOV=0;
		int		fecOV=0;
		int 	horOV=0;
		int		fecEnt=0;
		int		horEnt=0;
		int		fecDoc=0;
		int		rutCli=0;
		String	digCli="";
		String	nomCli="";
		String	dirCli="";
		String	comuCli="";
		int		monDoc=0;
		int		monEnt=0;
		String	nomChof="";
		String	estEntr="";
		String	estMoti="";
		String	codMotivoTabla="";
		String	desMotivoTabla="";
		String	tipoDoc="";
		int		numerNCpe=0;
		int		totalNCpe=0;
		String 	estadocargcab ="";
		String 	estadocargdet ="";
		
		
		if (codError<=0){
				
				int 	numeroCarg=0;
				int 	numdocu=0;
				int 	tipodocu=0;
				int 	numeroNCPexis=0;
				
				entrawhy=0;
				logi.info("ANTES DE WHILE ITER CARGU SOL5");
				Iterator iter = docutransp.getCarguio().iterator();
				while (iter.hasNext()){
					if (entrawhy==0){
						logi.info("ENTRA A WHILE ITER CARGU SOL5");
					}
					entrawhy=1;
					
					CarguioTranspDTO cargu = (CarguioTranspDTO) iter.next();
					
					codigoBodega=26;
					int codbodega=26;
					
					/*
					int codbodega = carguiodao.buscarCarguioTranspBodega(rut,dv,cargu.getNumeroCarguio());
					if (codbodega>0){
						codigoBodega=codbodega;
					}
					*/
					
					
					CarguioDTO carguiocdto = carguiodao.obtieneCarguioDTO(2,cargu.getNumeroCarguio(),codigoBodega);
					if (carguiocdto==null){
						codigoBodega=21;
						CarguioDTO carguiocdto1 = carguiodao.obtieneCarguioDTO(2,cargu.getNumeroCarguio(),codigoBodega);
						if (carguiocdto1==null){
							codigoBodega=24;
							CarguioDTO carguiocdto2 = carguiodao.obtieneCarguioDTO(2,cargu.getNumeroCarguio(),codigoBodega);
							carguiocdto=carguiocdto2;
						}else{
							codigoBodega=26;
							carguiocdto=carguiocdto1;
						}
					}
						
					if (carguiocdto==null){
						codigoBodega=26;
					}
					
					
					int existecarguio2 = carguioddao.buscaExisteCarguioc(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), rut,dv);
					if (existecarguio2==0) {
						codError=1010;
						logi.info("N U M E R O     C A R G U I O     "+cargu.getNumeroCarguio()+"     N  O     E X I S T E      !!!!"); break;
					}
					
					numeroCarg=cargu.getNumeroCarguio();
					numeritocarguio=numeroCarg;
					codigoEmpresa=carguiocdto.getCodigoEmpresa();
					patente=carguiocdto.getPatente().trim();
					versionpipe=Integer.toString(cargu.getVersion());
					
					
					
					int existecarguioch = carguioddao.buscaExisteChofer(codigoEmpresa, codigoBodega, rut,dv);
					if (existecarguioch==0) {
						codError=1011;
						logi.info("R U T     C  H O F E R    N O      E X I S T E   !!!!"); break;
					}
					
					int existecarguio = carguioddao.buscaExisteCarguioc(codigoEmpresa, codigoBodega, numeroCarg, rut,dv);
					if (existecarguio==0) {
						codError=1010;
						logi.info("N U M E R O     C A R G U I O "+numeroCarg+"  N O    E X I S T E     !!!!"); break;
					}
					else if (existecarguio==-1) {
						codError=1011;
						logi.info("R U T   C H O F E R     N O    E X I S T E    !!!"); break;
					}
					else if (existecarguio==-2) { 
						codError=1013;
						logi.info(" E S T A D O     O V    O   C A R G U I O   I N C O R R E C T O    !!!!"); break;	
					}
					else if (existecarguio==-4) {
						codError=1012;
						logi.info("DV  RUT CHOFER  INCORRECTO  ó   RUT DE CHOFER FUÉ CAMBIADO"); break;
					}
					
					
					
					if (codError<=0) {
						ChoftranDTO choftrandto = choftrandao.obtenerDatos(rut, dv);
						userId=choftrandto.getNomChofer().trim();
						nombredeChofer=userId;
						int largousu = userId.length();
						if (largousu>10){
							userId=userId.substring(0, 10).trim();
						}
						userId="CONTROLENT";
					}
					
					
					
					
					int existecarguioest = carguioddao.buscaExisteCarguioTransp(codigoEmpresa, codigoBodega, rut, dv, numeroCarg, patente);
					if (existecarguioest<=0) {
						logi.info("C A R G U I O    N U M E R O   "+numeroCarg+" N O    E S T A   C O N    E S T A D O    C O R R E C T O   !!!!");
						codError=1013; break;
					} 
					else {
						
						logi.info("ANTES DE WHILE ITER2 CARGU SOL5");
						entrawhy=0;
						Iterator iter2 = cargu.getOrdenes().iterator();
						while (iter2.hasNext()){
							if (entrawhy==0){
								logi.info("ENTRA A WHILE ITER2 CARGU SOL5");
							}
							entrawhy=1;
							
							OrdTranspNcpDTO orden = (OrdTranspNcpDTO) iter2.next();
							
							Fecha 	fch = new Fecha();
							int 	fecha = Integer.parseInt(fch.getYYYYMMDD());
							int     horas = Integer.parseInt(fch.getHHMMSS());
							int		numnc=0, swcorre=0;
							int 	cantiarti=0, totbrutocab=0, totnetocab=0, totalbrut=0, totdesctonetocab=0;
							int		totdesctocab=0, desctonetolinea=0;
							double 	totcostocab=0, totcostonetocab=0, costototnet=0, totalneto=0, desctobrutlinea=0;
							String 	responsableNota="", digclient="";
							String	formarti="", latitud="", longitud="", distancia="";
							int		numeroguia=0, numeroOVe=0, icantidad=0,icantirecep=0, rutclient=0;
							boolean generarNCp = false;
							boolean soliRedesp = false;
							boolean esrecepMayor = false;
							
							numdocu=orden.getNumDocumento();
							OrdTranspNcpDTO ordvtadto = ordvtadao.obtieneTotalesordenNumdoc(codigoEmpresa, codigoBodega, numdocu, orden.getTipoDocumento(),cargu.getNumeroCarguio(),rut,patente);
							if (ordvtadto==null){
								logi.info("NumDoc:"+ orden.getNumDocumento()+" no existe");
								codError=1014;
							}
								else {
									
									numdocu=ordvtadto.getNumFactbol();
									numeroguia=ordvtadto.getNumeroGuia();
									numeroOVe=ordvtadto.getNumeroOV();
									rutclient=ordvtadto.getRutCliente();
									digclient=ordvtadto.getDvCliente();
									rutCli=rutclient;
									digCli=digclient;
									nomCli=ordvtadto.getNombreCliente().trim();
									codigoVendedor=ordvtadto.getCodigoVendedor();
									nombreVendedor=ordvtadto.getNombreVendedor();
									correoVendedor=ordvtadto.getCorreoVendedor();
									if (correoVendedor==null || ("".equals(correoVendedor.trim()))){
										logi.info("S  I  N       C  O  R  R  E  O       V  E  N  D  E  D  O  R  !!        SE ENVIARA A DESARROLLO");
										correoVendedor=correoDesarrollo;
									}
									numOV=ordvtadto.getNumeroOV();
									fecOV=ordvtadto.getFechaDespacho();
									horOV=ordvtadto.getHoraOV();
									fecEnt=ordvtadto.getFechaEntrega();
									fecEnt=fecha;
									horEnt=horas;
									fecDoc=ordvtadto.getFechaFactura();
									dirCli=ordvtadto.getDireccionCliente().trim();
									comuCli=ordvtadto.getDescripcionComuna().trim();
									monDoc=ordvtadto.getMontoFactura();
									nomChof=userId;
									monEnt=monDoc;
									estEntr="";
									estMoti="";
									
									String nombreDocumen=tptdocdao.buscaDocumento(ordvtadto.getTipoDocumento());
									tipoDoc=Integer.toString(ordvtadto.getTipoDocumento())+"-"+nombreDocumen;
									
									numeritodocto=numdocu;
									
									if (numeroOVe<=0){
										logi.info("ov en cero");
										codError=1025; break;
									}
									if (rutclient<=0){
										logi.info("rut cliente en cero");
										codError=1026; break;
									}
									if (("".equals(digclient))){
										logi.info("dv cliente vacio");
										codError=1027; break;
									}
			  
									logi.info("RUT   CLIENTE  -------------------->   "+rutclient+"-"+digclient);
									logi.info("C L I E N T E  -------------------->   "+ordvtadto.getNombreCliente());
									logi.info("NUMERO  O V E  -------------------->   "+ordvtadto.getNumeroOV());
									logi.info("VERSION  '|'   -------------------->   "+versionpipe);
									
									estadocargcab = carguiodao.obtieneEstadoCarguioCab(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), patente);
									if ("E".equals(estadocargcab)){
										estadocargcab="U";
									}
									else if ("L".equals(estadocargcab)){
										estadocargcab="O";
									} 
									if (estadocargcab==null || ("".equals(estadocargcab.trim()))){
										estadocargcab=estadocar;
									}
										
									estadocargdet = carguioddao.obtieneEstadoCarguioDet(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(),patente, numeroOVe);
									if ("E".equals(estadocargdet)){
										estadocargdet="K";
									}
									else if ("L".equals(estadocargdet)){
											estadocargdet="H";
									}
									else if ("J".equals(estadocargdet)){
										estadocargdet="F";
									}
									else if ("K".equals(estadocargdet)){
											estadocargdet="H";
									}
								
									if (estadocargdet==null || ("".equals(estadocargdet.trim()))){
										estadocargdet=estadocar;
									}
									
									CarguiodTranspDTO carguioddto = carguioddao.obtieneMotivoCarguioD(codigoEmpresa, codigoBodega, rut, dv, cargu.getNumeroCarguio(), numeritodocto, patente);
									codMotivoTabla=carguioddto.getCodMotivo();
									desMotivoTabla=carguioddto.getDesMotivo();
									
									//VALIDA QUE EL CARGUIO Y OV EXISTA (CASO PARA MAS DE 1 CARGUIO POR RUT DE CHOFER
									int escarguiocorrecto = (carguiodao.obtieneCarguioyOrdenes(codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,patente,rutclient));
									if (escarguiocorrecto==1){
			
										String codigoEstadoOV="", descriEstadoOV="", codigoMotivoOV="", descriMotivoOV="";
										int 	cantiart=0,	cantirec=0, codiarti=0,	corrarti=0;
										int		tipodespa=2;
										String  url1="", url2="", url3="", url4="", url5="", url6="";
										String  foto1="", foto2="", foto3="", foto4="", foto5="", foto6="";
										int 	fechastampa=0;							
										
										codigoEstadoOV=orden.getCodEstado();
										descriEstadoOV=orden.getDescEstado();
										codigoMotivoOV=orden.getCodMotivo();
										descriMotivoOV=orden.getDesMotivo();
										numdocu=orden.getNumDocumento();
										tipodocu=orden.getTipoDocumento();
										estMoti=codigoMotivoOV+"-"+descriMotivoOV;
										
										if ("".equals(codigoMotivoOV)){
										}
										
										//validacion para solo 2 redespachos
										if (Integer.parseInt(versionpipe)>=1 && ("RD".equals(codigoEstadoOV))){
											codigoEstadoOV="VB";
											descriEstadoOV="VB CALL CENTER";
											//estMoti="";
											estEntr=codigoEstadoOV+"-"+descriEstadoOV;
											estMoti=codigoMotivoOV+"-"+descriMotivoOV;
											
											if ("".equals(descriMotivoOV)){
												estMoti=codMotivoTabla+"-"+desMotivoTabla;
												codigoMotivoOV=codMotivoTabla;
												descriMotivoOV=desMotivoTabla;
											}
											int cantixonm1=carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, numeroCarg, numdocu,patente);
											carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, cantixonm1);	
											carguioddao.actualizaEstadoCarguiodTransp(codigoEstadoOV, codigoMotivoOV, codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,numeroguia);
											estEntr=codigoEstadoOV+"-"+descriEstadoOV;
											solicitudderedespacho=false;
											generarNCp=false;
											soliRedesp =false;
											
											logi.info(" -------------->>   DESDE PLATAFORMA    S O L I C I T U D     5    ESTADO RD  VALIDA COLO 2 REDESPACHOS   "+par000);
											
											break;
										}
										
										
										
										
										
										
										
										
										
										
										
										
										/*
										* DESDE APP DE  *****CALL CENTER ******, REVISAR SI TIENE NCp PARA ELIMINARLA O CAMBIAR ESTADO 
										*/
										numeroNCPexis=0;
										String estadocartransp=carguiodao.verificaEstadoCarguioTransp(codigoEmpresa, numeroCarg, patente, codigoBodega, numeroOVe, rutclient);
										String estadoXOCL1transp=carguiodao.verificaEstadoXOCL1CarguioTransp(codigoEmpresa, numeroCarg, patente, codigoBodega, numeroOVe, rutclient);
										
										if ((estadoXOCL1transp != null) && (!estadoXOCL1transp.equals(""))){
											
											//Valida que el estado del carguio esté en ruta 
											if ("E".equals(estadocartransp.trim()) || "J".equals(estadocartransp.trim()) || 
												"K".equals(estadocartransp.trim()) || "U".equals(estadocartransp.trim())){
												
												if ("VB".equals(estadoXOCL1transp.trim())){
													estadoXOCL1transp="RC";
													
													logi.info(" -------------->>   DESDE PLATAFORMA    S O L I C I T U D     5    ESTADO RC   "+par000);
												}
												
												//tabla tiene RC 	 	y  				 call center envía   EP   !!!NO
												if ("RC".equals(estadoXOCL1transp.trim()) && ("EP".equals(codigoEstadoOV.trim()))){
													codigoEstadoOV="RC";
													descriEstadoOV="RECHAZADO";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
													
													logi.info(" -------------------->>>>>>     DESDE PLATAFORMA    S O L I C I T U D     5    TIENE RC  Y CALL CENTER ENVIA EP   "+par000);
													break;
												}
												
												//tabla tiene ET 	 	y  				 call center envía   EP   !!!NO
												else if ("ET".equals(estadoXOCL1transp.trim()) && ("EP".equals(codigoEstadoOV.trim()))){
													codigoEstadoOV="ET";
													descriEstadoOV="ENTREGADO";
													estMoti="";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
													logi.info(" -------------------->>>>>>     DESDE PLATAFORMA    S O L I C I T U D     5    TIENE ET  Y CALL CENTER ENVIA EP   "+par000);
													break;
												}
												
												
												
												//tabla tiene RC 	 	y  				 call center envía   ET  //ELIMINAR NCP
												else if ("RC".equals(estadoXOCL1transp.trim()) && ("ET".equals(codigoEstadoOV.trim()))){
													
													logi.info("TIENE RC Y CALL CENTER ENVIA ET, ELIMINAR NCP");
													
													logi.info(" -------------------->>>>>>     DESDE PLATAFORMA    S O L I C I T U D     5    TIENE RC  Y CALL CENTER ENVIA ET   "+par000);
													
													ConnohDTO connohdtoeli = new ConnohDTO();
													connohdtoeli.setCodDocumento(tipodocu);
													connohdtoeli.setNumeroDocumento(numdocu);
													connohdtoeli.setRutCliente(rutclient);
													connohdtoeli.setTipoNota("P");
													connohdtoeli.setCodigoBodega(codigoBodega);
													logi.info("ANTES DE EXISTE CONNOH SOL5");
													int numeroNCP = connohdao.existeConnoh(connohdtoeli);
													if (numeroNCP>0){
														ConnodDTO connoddtoeli = new ConnodDTO();
														connoddtoeli.setCodigoEmpresa(codigoEmpresa);
														connoddtoeli.setNumeroNota(numeroNCP);
														connoddtoeli.setTipoNota("P");
														
														entrawhy=0;
														logi.info("ANTES DE WHILE ITERNCP LISTANCPE SOL5");
														List listancpe = connoddao.buscaExisteConnodTransp(connoddtoeli);
														Iterator iterncp = listancpe.iterator();
														while (iterncp.hasNext()) {
															if (entrawhy==0){
																logi.info("ENTRA A WHILE ITERNCP LISTANCPE SOL5");
															}
															entrawhy=1;
															
															ConnodDTO dtoconnod = (ConnodDTO) iterncp.next();
															if (dtoconnod.getNumeroNota()>0) {
																logi.info("NUMERO >0 WHILE ITERNCP LISTANCPE SOL5");
																connoddtoeli.setFechaNota(dtoconnod.getFechaNota());
																connoddtoeli.setCorrelativo(dtoconnod.getCorrelativo());
																connoddtoeli.setCodArticulo(dtoconnod.getCodArticulo());
																connoddtoeli.setDigArticulo(dtoconnod.getDigArticulo());
																connoddtoeli.setCantidad(dtoconnod.getCantidad());
																
																NcplogDTO ncplogdto = new NcplogDTO();
																ncplogdto.setCodigoEmpresa(connoddtoeli.getCodigoEmpresa());
																ncplogdto.setTipo("P");
																ncplogdto.setNumeroNota(connoddtoeli.getNumeroNota());
																ncplogdto.setFechaNota(connoddtoeli.getFechaNota());
																ncplogdto.setNumeroCarguio(numeroCarg);
																ncplogdto.setNumeroOrden(ordvtadto.getNumeroOV());
																ncplogdto.setNumeroDocumento(numdocu);
																ncplogdto.setCodigoBodega(codigoBodega);
																ncplogdto.setRutCliente(rutclient);
																ncplogdto.setDigCliente(digclient);
																ncplogdto.setUsuario(userId);  //"SAASQDM"
																ncplogdto.setTipoAccion("E");
																ncplogdto.setCodigoArticulo(connoddtoeli.getCodArticulo());
																ncplogdto.setDigitoArticulo(connoddtoeli.getDigArticulo());
																ncplogdto.setCantidad(dtoconnod.getCantidad());
																ncplogdto.setCantidadArticulo(dtoconnod.getCantidad());
																ncplogdto.setFechaUser(fecha);
																ncplogdto.setHoraUser(horas);
																ncplogdto.setIpEquipo(Integer.toString(cargu.getRutChofer()));
																ncplogdto.setNombreEquipo(carguiocdto.getPatente().trim());
																ncplogdto.setCodigoUsuario(userId);
																int correlativoncplog = ncplogdao.buscaUltimaLineaNcplog(ncplogdto);
																ncplogdto.setLineaNota(correlativoncplog);
																int resplog = ncplogdao.insertaNcplog(ncplogdto);
																generarNCp=false;
																soliRedesp =false;
																
																CargconwDTO carcondto = new CargconwDTO();
																carcondto.setCodigoEmpresa(codigoEmpresa);
																carcondto.setNumcarguio(numeroCarg);
																carcondto.setPatente(patente);
																carcondto.setCodigoBodega(codigoBodega);
																carcondto.setCodigoArticulo(connoddtoeli.getCodArticulo());
																carcondto.setDigitoArticulo(connoddtoeli.getDigArticulo());
																carcondto.setTipoCarguio("C");
																carcondto.setCantidadArticulo(dtoconnod.getCantidad());
																int exitbl = cargcondao.buscaCargconwTransp(carcondto);
																if (exitbl>=0){
																	exitbl=exitbl-dtoconnod.getCantidad();
																	carcondto.setCantidadArticulo(exitbl);
																	int canti=cargcondao.actualizaCargconw(carcondto);
																}
																else {
																	cargcondao.eliminaCargconw(carcondto);
																}
																
															}
														}
														
														logi.info("ANTES DE ELIMINAR CONNOD RC A ET TIENE NCP SOL5");
														int eliconnod=connoddao.eliminaConnodTransp(connoddtoeli);
														logi.info("ANTES DE ELIMINAR CONNOH RC A ET TIENE NCP SOL5");
														int eliconnoh=connohdao.eliminaConnohTransp(connohdtoeli);
														
														DocncpDTO docncpdto = new DocncpDTO();
														docncpdto.setTipoNota("P");
														docncpdto.setNumeroNota(numeroNCP);
														docncpdto.setFechaNota(fecha);
														docncpdto.setCodigoBodega(codigoBodega);
														docncpdto.setRutCliente(rutclient);
														docncpdto.setDigCliente(digclient);
														docncpdto.setCorrelativo(1);
														docncpdto.setNumeroOV(ordvtadto.getNumeroOV());
														docncpdto.setNumeroCarguio(numeroCarg);
														docncpdto.setNumeroDocumento(numdocu);
														int elidocncp=docncpdao.eliminaDocncpTranspDTO(docncpdto);
													}
													
													codigoEstadoOV="ET";
													descriEstadoOV="ENTREGADO";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
													estMoti="";
													
													carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, 2);	
													carguioddao.actualizaEstadoCarguiodTransp(codigoEstadoOV, codigoMotivoOV, codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,numeroguia);
												
													break;
												
												}
												
												
												
												//tabla tiene EP 	 	y  				 call center envía   ET   , ELIMINA NCP Y CONSOLIDADO
												else if ("EP".equals(estadoXOCL1transp.trim()) && ("ET".equals(codigoEstadoOV.trim()))) {
													
													logi.info(" -------------------->>>>>>     DESDE PLATAFORMA    S O L I C I T U D     5    TIENE EP  Y CALL CENTER ENVIA ET  "+par000);
								
													
													ConnohDTO connohdtoeli = new ConnohDTO();
													connohdtoeli.setCodDocumento(tipodocu);
													connohdtoeli.setNumeroDocumento(numdocu);
													connohdtoeli.setRutCliente(rutclient);
													connohdtoeli.setTipoNota("P");
													connohdtoeli.setCodigoBodega(codigoBodega);
													int numeroNCP = connohdao.existeConnoh(connohdtoeli);
													logi.info("ANTES DE BUSCAR CONNOD DE SOLICITUD 5");
													if (numeroNCP>0){
														logi.info("NUMERO NCP >0 AL BUSCAR CONNOD DE SOLICITUD 5");
														ConnodDTO connoddtoeli = new ConnodDTO();
														connoddtoeli.setCodigoEmpresa(codigoEmpresa);
														connoddtoeli.setNumeroNota(numeroNCP);
														connoddtoeli.setTipoNota("P");
														
														logi.info("ANTES DE BUSCAR CONNOD DE SOLICITUD 5   CONNODTRANSP");
														List listancpe = connoddao.buscaExisteConnodTransp(connoddtoeli);
														Iterator iterncp = listancpe.iterator();
														while (iterncp.hasNext()) {
															logi.info("ENCONTRÓ CONNOD DE SOLICITUD 5   CONNODTRANSP");
															ConnodDTO dtoconnod = (ConnodDTO) iterncp.next();
															if (dtoconnod.getNumeroNota()>0) {
																logi.info("NUMERO >0 WHILE ITERNCP LISTANCPE DTOCONNOD SOL5");
																connoddtoeli.setFechaNota(dtoconnod.getFechaNota());
																connoddtoeli.setCorrelativo(dtoconnod.getCorrelativo());
																connoddtoeli.setCodArticulo(dtoconnod.getCodArticulo());
																connoddtoeli.setDigArticulo(dtoconnod.getDigArticulo());
																connoddtoeli.setCantidad(dtoconnod.getCantidad());
																
																NcplogDTO ncplogdto = new NcplogDTO();
																ncplogdto.setCodigoEmpresa(connoddtoeli.getCodigoEmpresa());
																ncplogdto.setTipo("P");
																ncplogdto.setNumeroNota(connoddtoeli.getNumeroNota());
																ncplogdto.setFechaNota(connoddtoeli.getFechaNota());
																ncplogdto.setNumeroCarguio(numeroCarg);
																ncplogdto.setNumeroOrden(ordvtadto.getNumeroOV());
																ncplogdto.setNumeroDocumento(numdocu);
																ncplogdto.setCodigoBodega(codigoBodega);
																ncplogdto.setRutCliente(rutclient);
																ncplogdto.setDigCliente(digclient);
																ncplogdto.setUsuario(userId); //"SAASQDM"
																ncplogdto.setTipoAccion("E");
																ncplogdto.setCodigoArticulo(connoddtoeli.getCodArticulo());
																ncplogdto.setDigitoArticulo(connoddtoeli.getDigArticulo());
																ncplogdto.setCantidad(dtoconnod.getCantidad());
																ncplogdto.setCantidadArticulo(dtoconnod.getCantidad());
																ncplogdto.setFechaUser(fecha);
																ncplogdto.setHoraUser(horas);
																ncplogdto.setIpEquipo(Integer.toString(cargu.getRutChofer()));
																ncplogdto.setNombreEquipo(carguiocdto.getPatente().trim());
																ncplogdto.setCodigoUsuario(userId);
																int correlativoncplog = ncplogdao.buscaUltimaLineaNcplog(ncplogdto);
																ncplogdto.setLineaNota(correlativoncplog);
																int resplog = ncplogdao.insertaNcplog(ncplogdto);
																generarNCp=false;
																soliRedesp =false;
																
																CargconwDTO carcondto = new CargconwDTO();
																carcondto.setCodigoEmpresa(codigoEmpresa);
																carcondto.setNumcarguio(numeroCarg);
																carcondto.setPatente(patente);
																carcondto.setCodigoBodega(codigoBodega);
																carcondto.setCodigoArticulo(connoddtoeli.getCodArticulo());
																carcondto.setDigitoArticulo(connoddtoeli.getDigArticulo());
																carcondto.setTipoCarguio("C");
																carcondto.setCantidadArticulo(dtoconnod.getCantidad());
																int exitbl = cargcondao.buscaCargconwTransp(carcondto);
																if (exitbl>=0){
																	exitbl=exitbl-dtoconnod.getCantidad();
																	carcondto.setCantidadArticulo(exitbl);
																	int canti=cargcondao.actualizaCargconw(carcondto);
																}
																else {
																	cargcondao.eliminaCargconw(carcondto);
																}
															}
														}
														
														
														logi.info(" -------------------->>>>>>     DESDE PLATAFORMA  ELIMINA NCP CONNOD Y CONNOH  S O L I C I T U D     5    TIENE EP  Y CALL CENTER ENVIA ET  "+par000);
												
														
														int eliconnod=connoddao.eliminaConnodTransp(connoddtoeli);
														int eliconnoh=connohdao.eliminaConnohTransp(connohdtoeli);
														
														DocncpDTO docncpdto = new DocncpDTO();
														docncpdto.setTipoNota("P");
														docncpdto.setNumeroNota(numeroNCP);
														docncpdto.setFechaNota(fecha);
														docncpdto.setCodigoBodega(codigoBodega);
														docncpdto.setRutCliente(rutclient);
														docncpdto.setDigCliente(digclient);
														docncpdto.setCorrelativo(1);
														docncpdto.setNumeroOV(ordvtadto.getNumeroOV());
														docncpdto.setNumeroCarguio(numeroCarg);
														docncpdto.setNumeroDocumento(numdocu);
														int elidocncp=docncpdao.eliminaDocncpTranspDTO(docncpdto);
														
														codigoEstadoOV="ET";
														descriEstadoOV="ENTREGADO";
														estEntr=codigoEstadoOV+"-"+descriEstadoOV;
														estMoti="";
														
														carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, 2);	
														carguioddao.actualizaEstadoCarguiodTransp(codigoEstadoOV, codigoMotivoOV, codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,numeroguia);
														
														break;
														 
													}
													
												}
												
												//tabla tiene EP 	 	y  				 call center envía   RC  // MODIFICA NCP
												else if ("EP".equals(estadoXOCL1transp.trim()) && ("RC".equals(codigoEstadoOV.trim()))){
													
													logi.info(" -------------------->>>>>>     DESDE PLATAFORMA    S O L I C I T U D     5    TIENE EP  Y CALL CENTER ENVIA RC   "+par000);
													
													ConnohDTO connohdtoeli = new ConnohDTO();
													connohdtoeli.setCodDocumento(tipodocu);
													connohdtoeli.setNumeroDocumento(numdocu);
													connohdtoeli.setRutCliente(rutclient);
													connohdtoeli.setTipoNota("P");
													connohdtoeli.setCodigoBodega(codigoBodega);
													numeroNCPexis = connohdao.existeConnoh(connohdtoeli);
													
													codigoEstadoOV="RC";
													descriEstadoOV="RECHAZADO";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
													generarNCp=true;
											
												}
												
												//tabla tiene ET 	 	y  				 call center envía   RC  //CREAR NCP
												else if ("ET".equals(estadoXOCL1transp.trim()) && ("RC".equals(codigoEstadoOV.trim()))){
													
													codigoEstadoOV="RC";
													descriEstadoOV="RECHAZADO";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
													
													logi.info(" -------------------->>>>>>     DESDE PLATAFORMA    S O L I C I T U D     5    TIENE ET  Y CALL CENTER ENVIA RC   "+par000);
													
													
												}
												
											}
											else {
												
												if ("ET".equals(estadoXOCL1transp.trim())) {
													codigoEstadoOV="ET";
													descriEstadoOV="ENTREGADO";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
													estMoti="";
												}
												else if ("EP".equals(estadoXOCL1transp.trim())) {
													codigoEstadoOV="EP";
													descriEstadoOV="ENTREGA PARCIAL";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
												}
												else if ("RC".equals(estadoXOCL1transp.trim())) {
													codigoEstadoOV="RC";
													descriEstadoOV="RECHAZADO";
													estEntr=codigoEstadoOV+"-"+descriEstadoOV;
												}
												logi.info("CALL CENTER NO PUEDE CAMBIAR NCP YA QUE EL ESTADO DEL CARGUIO DEBE SER E,J,K,U");
												break;
											}
																						
										} //no tiene estado carguiod xocl1
										
										/*
										*  E N D        DESDE APP DE  *****CALL CENTER ******, REVISAR SI TIENE NCp PARA ELIMINARLA O CAMBIAR ESTADO 
										*/
										
										
										
										
										
										
										
										
										
										
										
										
										
										
										
						
										
										
										
										
										if ("RC".equals(codigoEstadoOV)){
											generarNCp = true;
											estEntr=codigoEstadoOV+"-"+descriEstadoOV;
										}
										else if ("RD".equals(codigoEstadoOV)) {
											soliRedesp = true;
										}
										else {
											codigoEstadoOV="VB";
											descriEstadoOV="VB CALL CENTER";
											//estMoti="";
											estEntr=codigoEstadoOV+"-"+descriEstadoOV;
											estMoti=codigoMotivoOV+"-"+descriMotivoOV;
											if ("".equals(descriMotivoOV)){
												estMoti=codMotivoTabla+"-"+desMotivoTabla;
												codigoMotivoOV=codMotivoTabla;
												descriMotivoOV=desMotivoTabla;
											}
										}
										
										
										int cantiredespa = carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, numeroCarg, numdocu,patente);
										cantidadredespacho=cantiredespa;
										
										//if (cantiredespa>=4){
											
										//}else{
											cantidadredespacho=2;
											cantiredespa=2;
										//}
										
										if (cantiredespa==1){
											logi.info("OV : "+numeroOVe+ " NO TIENE DESPACHO 1, DEBE SER SOLICITUD 2 PRIMERO !!!");
											//break;
										}
										else if (cantiredespa>3){
											codigoEstadoOV="VB";
											descriEstadoOV="VB CALL CENTER";
											monEnt=0;
											estEntr=codigoEstadoOV+"-"+descriEstadoOV;
											estMoti=codigoMotivoOV+"-"+descriMotivoOV;
											if ("".equals(descriMotivoOV)){
												estMoti=codMotivoTabla+"-"+desMotivoTabla;
												codigoMotivoOV=codMotivoTabla;
												descriMotivoOV=desMotivoTabla;
											}
											generarNCp = false;
											carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, cantiredespa);
											carguioddao.actualizaEstadoCarguiodTransp(codigoEstadoOV, codigoMotivoOV, codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,numeroguia);
											logi.info("OV : "+numeroOVe+ " YA TIENE REDESPACHO ó NCP GENERADA !!!  CANTIDAD: "+cantiredespa);
											
										} 
										 else {
											 
											 /*
											  * si es 2=es primer despacho, si es 3=redespacho, si es mayor a 3=envío de mismo estado
											  */
											 if (cantiredespa==2){
												 int cantixonm1=carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, numeroCarg, numdocu,patente);
												 carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, cantixonm1);	
												 carguioddao.actualizaEstadoCarguiodTransp(codigoEstadoOV, codigoMotivoOV, codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,numeroguia);
												 
												 estEntr=codigoEstadoOV+"-"+descriEstadoOV;
											 }
											 else if (cantiredespa==3){
												 if((!"RC".equals(codigoEstadoOV))){
														soliRedesp = false;
														generarNCp = false;
														codigoEstadoOV="VB";
														descriEstadoOV="VB CALL CENTER";
														//estMoti="";
														estEntr=codigoEstadoOV+"-"+descriEstadoOV;
														estMoti=codigoMotivoOV+"-"+descriMotivoOV;
														if ("".equals(descriMotivoOV)){
															estMoti=codMotivoTabla+"-"+desMotivoTabla;
															codigoMotivoOV=codMotivoTabla;
															descriMotivoOV=desMotivoTabla;
														}
														monEnt=0;
												 }
												 int cantixonm1=carguioddao.obtieneCantidadRedespachos(codigoEmpresa, codigoBodega, rut, dv, numeroCarg, numdocu,patente);
												 carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, cantixonm1);	
											 	 carguioddao.actualizaEstadoCarguiodTransp(codigoEstadoOV, codigoMotivoOV, codigoEmpresa, numeroCarg, codigoBodega, numeroOVe,numeroguia);
											 	 
											 	 estEntr=codigoEstadoOV+"-"+descriEstadoOV;
											 	
											 }
											 else {
											 	logi.info("Error de redespacho, solicitud 5             !!!!!!!!!!!!!!!!!!!!!!!!!");
											 	break;
											 }
											 
											 
											tipodespa=2; 
											if (cantiredespa==2){
												tipodespa=1;
											}
											else if (cantiredespa==3){
												tipodespa=2;
											}
											
											if (!"-".equals(estMoti)){
												estMoti=codigoMotivoOV+"-"+descriMotivoOV;	
											}
											
											entrawhy=0;
											logi.info("ANTES DE WHILE ITER55 LISTAORDVDDE SOL5");
											List listaordvdde = ordvddedao.buscaOrdvddeDatos(codigoEmpresa, numeroOVe, rutclient, digclient, codigoBodega, tipodespa); 
											Iterator iter55 = listaordvdde.iterator();
											while (iter55.hasNext()) {
												if (entrawhy==0){
													logi.info("ENTRA A WHILE ITER55 LISTAORDVDDE SOL5");
												}
												entrawhy=1;
												
												OrdvddeDTO dtoordvdde = (OrdvddeDTO) iter55.next();
												
												latitud=dtoordvdde.getLatitud();
												longitud=dtoordvdde.getLongitud();
												distancia=dtoordvdde.getDistancia();
												fechastampa=dtoordvdde.getFechaConfirmacion();
												if (fechastampa<=0){
													fechastampa=fecha;
												}
												url1=dtoordvdde.getFoto1();
												foto1=dtoordvdde.getComentario1();
												url2=dtoordvdde.getFoto2();
												foto2=dtoordvdde.getComentario2();
												url3=dtoordvdde.getFoto3();
												foto3=dtoordvdde.getComentario3();
												url4=dtoordvdde.getFoto4();
												foto4=dtoordvdde.getComentario4();
												url5=dtoordvdde.getFoto5();
												foto5=dtoordvdde.getComentario5();
												url6=dtoordvdde.getFoto6();
												foto6=dtoordvdde.getComentario6();
											}
											
											OrdvddeDTO ordvddedto = new OrdvddeDTO();
											ordvddedto.setEmpresa(codigoEmpresa);
											ordvddedto.setNumeroOV(numeroOVe);
											ordvddedto.setRutCliente(rutclient);
											ordvddedto.setDigCliente(digclient);
											ordvddedto.setCodigoBodega(codigoBodega);
											ordvddedto.setDetalledespacho(0);
											ordvddedto.setTipoDespacho(cantiredespa);
											ordvddedto.setFechaConfirmacion(fechastampa);
											ordvddedto.setLatitud(latitud);
											ordvddedto.setLongitud(longitud);
											ordvddedto.setDistancia(distancia);
											ordvddedto.setCodEstado(codigoEstadoOV);
											ordvddedto.setDesEstado(descriEstadoOV);
											ordvddedto.setCodMotivo(codigoMotivoOV);
											ordvddedto.setDesMotivo(descriMotivoOV);
											ordvddedto.setFechauser(fecha);
											ordvddedto.setHorauser(horas);
											int existetbl = ordvddedao.buscaOrdvdde(ordvddedto);
											if (existetbl==0) {
												ordvddedao.insertaOrdvdde(ordvddedto);
											}
											else if (existetbl>0){
												ordvddedao.actualizaOrdvdde(ordvddedto);
											}
											
											entrawhy=0;
											logi.info("ANTES DE WHILE ITER88 LISTAORDVDET SOL5");
											List listaordvdet = ordvdetdao.buscaOrdvdetDatos(codigoEmpresa, numeroOVe, rutclient, digclient, codigoBodega, tipodespa); 
											Iterator iter88 = listaordvdet.iterator();
											while (iter88.hasNext()){
												if (entrawhy==0){
													logi.info("ENTRA A WHILE ITER88 LISTAORDVDET SOL5");
												}
												entrawhy=1;
												
												OrdvdetDTO detalleinputt = (OrdvdetDTO) iter88.next();
												codiarti=detalleinputt.getCodigoArticulo();
												corrarti=detalleinputt.getCorrelativo();
												formarti=detalleinputt.getFormatoArticulo();
												cantiart=detalleinputt.getCantidadArticulo();
												cantirec=detalleinputt.getCantidadRecepcionada();
												if (cantirec>cantiart){
													cantirec=cantiart;
												}
												if (numeroNCPexis>0){
													cantirec=cantiart;
												}
												OrdvdetDTO ordvdetdto = new OrdvdetDTO();
												ordvdetdto.setEmpresa(codigoEmpresa);
												ordvdetdto.setNumeroOV(numeroOVe);
												ordvdetdto.setRutCliente(rutclient);
												ordvdetdto.setDigCliente(digclient);
												ordvdetdto.setCodigoBodega(codigoBodega);
												ordvdetdto.setDetalledespacho(0);
												ordvdetdto.setTipoDespacho(cantiredespa);
												ordvdetdto.setCorrelativo(corrarti);
												ordvdetdto.setCodigoArticulo(codiarti);
												ordvdetdto.setFormatoArticulo(formarti);
												ordvdetdto.setCantidadArticulo(cantiart);
												ordvdetdto.setCantidadRecepcionada(cantirec);
												ordvdetdto.setFechauser(fecha);
												ordvdetdto.setHorauser(horas);
												ordvdetdto.setUsuario(userId);
												int existetabl = ordvdetdao.buscaOrdvdet(ordvdetdto);
												if (existetabl==0) {
													ordvdetdao.insertaOrdvdet(ordvdetdto);
												}
												else if (existetabl>0){
													ordvdetdao.actualizaOrdvdet(ordvdetdto);
												}
												
												
											}
											
										tipodespa=cantiredespa;
											
										solicitudderedespacho=soliRedesp;
											 
										if (generarNCp==true && ("RC".equals(codigoEstadoOV))) {
											 	
											if (cantirec<=cantiart){
												
													if (numeroNCPexis>0 && "EP".equals(estadoXOCL1transp.trim()) && "RC".equals(codigoEstadoOV.trim())){
														numnc=numeroNCPexis;
														swcorre=1;
													}
													else {
														if (swcorre==0){
															numnc=(tpacordao.recupeCorrelativo(0,8));
															if (numnc<=0){	
																logi.info("ERROR!! NO SE HA GENERADO CORRELATIVO PARA LA NCP");
																codError=1029; break;
															}
															else {
																int existenum = connohdao.buscaExisteConnoh(codigoEmpresa, numnc);
																if (existenum>0){
																	numnc=(tpacordao.recupeCorrelativo(0,8));
																}
															}
															
															int existenumerodenc=0;
															for(int i=0;i<50;i++)
															{
																existenumerodenc = connohdao.buscaExisteConnoh(codigoEmpresa, numnc);
																if (existenumerodenc>0){
																	numnc=(tpacordao.recupeCorrelativo(0,8));
																}
																else {
																	break;
																}
															}		
															
														swcorre=1;
														}
													}
													
			
														double  pventa=0, pnetos=0;
														logi.info("ANTES DE WHILE ITER99 LISTAORDVDETT SOL5 ");
														List listaordvdett = ordvdetdao.buscaOrdvdetDatos(codigoEmpresa, numeroOVe, rutclient, digclient, codigoBodega, tipodespa); 
														Iterator iter99 = listaordvdett.iterator();
														entrawhy=0;
														while (iter99.hasNext()){
															if (entrawhy==0){
																logi.info("ENTRA A WHILE ITER99 LISTAORDVDETT SOL5");
															}
															entrawhy=1;
															
															OrdvdetDTO detalleinputt = (OrdvdetDTO) iter99.next();
															codiarti=detalleinputt.getCodigoArticulo();
															corrarti=detalleinputt.getCorrelativo();
															formarti=detalleinputt.getFormatoArticulo();
															cantiart=detalleinputt.getCantidadArticulo();
															cantirec=detalleinputt.getCantidadRecepcionada();
															if (cantirec>cantiart){
																cantirec=cantiart;
															}
																
																logi.info("ANTES DE WHILE LISTADETORDP");
																List listadetordp;
																listadetordp = detorddao.obtieneTotalesdetalleNumdoc(codigoEmpresa, numeroOVe, codigoBodega, ordvtadto.getRutCliente(), codiarti, cantiart);
																if (listadetordp.isEmpty()){
																	logi.info("LISTADETORDP EMPTY DE WHILE LISTADETORDP");
																	listadetordp = detorddao.obtieneTotalesdetalleNumdoc2(codigoEmpresa, numeroOVe, codigoBodega, ordvtadto.getRutCliente(), codiarti, cantiart);
																}
																Iterator iter5 = listadetordp.iterator();
																entrawhy=0;
																while (iter5.hasNext()) {
																	if (entrawhy==0){
																		logi.info("ENTRA A WHILE ITER5 LISTADETORDP SOL5");
																	}
																	entrawhy=1;
																	
																	DetordTranspNcpDTO detorden2 = (DetordTranspNcpDTO) iter5.next();
																						
																	ConnodDTO connoddto = new ConnodDTO();
																	NcplogDTO ncplogdto = new NcplogDTO();
																	
																	codiarti=Integer.parseInt(detorden2.getCodigoArticulo());
																	corrarti=Integer.parseInt(detorden2.getCorrelativo());
																	formarti=detorden2.getFormato();
																	
																	connoddto.setCodigoEmpresa(carguiocdto.getCodigoEmpresa());
																	connoddto.setTipoNota("P");
																	connoddto.setNumeroNota(numnc);
																	connoddto.setFechaNota(fecha);
																	connoddto.setCorrelativo(corrarti);
																	connoddto.setCodArticulo(codiarti);
																	connoddto.setDigArticulo(detorden2.getDigitoArticulo());
																	connoddto.setDescripcion(detorden2.getDescripcionArticulo());
																	int cantidevo=0;
								 									if (cantirec<=0){ 
								 										cantidevo=cantiart;
																	}
								 									else if (cantirec==cantiart){
								 										cantidevo=cantiart;
								 									}
																	else { 
																		cantidevo=cantiart-cantirec;
																	}
								 									if (numeroNCPexis>0){
																		cantirec=cantiart;
																		cantidevo=cantiart;
																	}
																	connoddto.setCantidad(cantidevo);
																	connoddto.setFormato(formarti);
																	connoddto.setPrecioUnitario(detorden2.getPrecioUnitario());
																	connoddto.setPrecioNeto(detorden2.getPrecioNeto());
																	connoddto.setCostoArticulo(detorden2.getCostoArticulo());
																	connoddto.setCostoNetoUnitario(detorden2.getCostoNetoUnitario());
																	connoddto.setMontoNeto(detorden2.getMontototalNeto());
																	connoddto.setMontoExento(detorden2.getTotalExento());
																	if (detorden2.getCostoTotalNeto()<=1){
																		costototnet=cantidevo*detorden2.getCostoTotalNeto();
																	} else {
																		costototnet=detorden2.getCostoTotalNeto();
																	}
																	//costototnet=detorden2.getCostoTotalNeto();//cantidevo*detorden2.getCostoTotalNeto();
																	totalneto=Math.round(cantidevo*detorden2.getPrecioNeto());
																	totalbrut=(int) (cantidevo*detorden2.getPrecioUnitario());
																	connoddto.setCostoTotalNeto(costototnet);
																	connoddto.setMontoNeto((int)(totalneto));
																	connoddto.setTotalNeto((int)(totalneto));
																						
																	
																	if ("7777777".equals(detalleinputt.getCodigoArticulo())){
																		List listacldmco = cldmcodao.obtieneFleteCldmco(codigoEmpresa, tipodocu, rutclient, digclient, fecDoc, codigoBodega, numdocu, detalleinputt.getCodigoArticulo());
																		Iterator iterclc = listacldmco.iterator();
																		while (iterclc.hasNext()) {
																			CldmcoDTO cldmco2 = (CldmcoDTO) iterclc.next();
																			connoddto.setPrecioUnitario(cldmco2.getPrecio());
																			connoddto.setPrecioNeto(cldmco2.getPrecioNeto());
																			connoddto.setMontoNeto(cldmco2.getValorNeto());
																			connoddto.setMontoExento(cldmco2.getMontoExento());
																			totalneto=Math.round(cantidevo*cldmco2.getPrecioNeto());
																			totalbrut=cldmco2.getMontoCompra();
																			connoddto.setMontoNeto((int)(totalneto));
																			connoddto.setTotalNeto((int)(totalneto));
																		}
																	}
																	//logi.info("CONNOD *** CODART|CORRE|FORMATO|CANTI|CANTIRECEP  ----->>>  "+codiarti+"|"+corrarti+"|"+formarti+"|"+detorden2.getDescripcionArticulo()+"|"+cantiart+"|"+cantirec);
																						
																	VedmarDTO vedmardto = new VedmarDTO();
																	double  impuestos = vedmardao.calculaImpuestosArticulo(codiarti, detorden2.getDigitoArticulo());
																						
																	connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
																	//1 peso
																	//Sin descuento
																	if (detorden2.getTotalDescuento()<=0 && detorden2.getTotalDescuentoNeto()<=0){
																		connoddto.setTotalDescuento(detorden2.getTotalDescuento());
																		connoddto.setTotalDescuentoNeto(detorden2.getTotalDescuentoNeto());
																	} else {
											     						//Con descuento
																		if (cantiarti!=cantirec){
																			desctonetolinea=(int) Math.round((totalneto*(detorden2.getPorcentajeDescuento()/100)));
																			desctobrutlinea=Math.round(desctonetolinea*impuestos);
																			connoddto.setTotalDescuento((int) desctobrutlinea);
																			connoddto.setTotalDescuentoNeto(desctonetolinea);  
																			totalneto=totalneto-desctonetolinea;
																			connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
																			connoddto.setMontoNeto((int)(totalneto));
																			connoddto.setTotalNeto((int)(totalneto)+desctonetolinea);
																			totdesctonetocab=totdesctonetocab+desctonetolinea;
																			totdesctocab=(int) (totdesctocab+desctobrutlinea);
																		}
																		else if (cantiarti==cantirec){
																			totdesctonetocab=totdesctonetocab+detorden2.getTotalDescuentoNeto();
																			totdesctocab=totdesctocab+detorden2.getTotalDescuento();
																			connoddto.setTotalDescuento(detorden2.getTotalDescuento());
																			connoddto.setTotalDescuentoNeto(detorden2.getTotalDescuentoNeto());  
																			totalneto=totalneto-detorden2.getTotalDescuentoNeto();
																			connoddto.setTotalLinea((int)(Math.round(totalneto*impuestos)));
																			connoddto.setMontoNeto((int)(totalneto));
																			connoddto.setTotalNeto((int)(totalneto)+detorden2.getTotalDescuentoNeto());
																			}
																		}
																	
																	
																		/*
																		//cuando es combo, calcular neto y bruto en tables EXDACP y EXDACB
																		if ("C".equals(detorden2.getCombo())) {
																			connoddto.setFormato("X");
																			int a;
																			ExdacpDTO exdacpdto = exdacpdao.calculamontosCombo(codigoEmpresa, codiarti, detorden2.getDigitoArticulo(), codigoBodega,2000 , cantidevo, (a = (int) detorden2.getPorcentajeDescuento()));
																			pnetos=exdacpdto.getPrecioNeto();
																			pventa=exdacpdto.getPrecioVenta();
																			totalneto=Math.round(pnetos);
																			totalbrut=(int) (pventa);
																			connoddto.setMontoNeto((int)(totalneto));
																			connoddto.setTotalNeto((int)(totalneto));
																			connoddto.setTotalLinea((int)(Math.round(totalbrut)));
																		}
																		*/
																	
																		//Insert in Connod
																		ConnohDTO connohdto2 = new ConnohDTO();
																		connohdto2.setCodDocumento(tipodocu);
																		connohdto2.setNumeroDocumento(numdocu);
																		connohdto2.setRutCliente(rutclient);
																		connohdto2.setTipoNota("P");
																		connohdto2.setCodigoBodega(codigoBodega);
																		existeconnoh = connohdao.existeConnoh(connohdto2);
																		if (existeconnoh>0){
																			connoddto.setNumeroNota(existeconnoh);
																			int existeconnod = connoddao.buscaExisteConnod(connoddto);
																			if (existeconnod>0) {
																				int eliconnod=connoddao.eliminaConnod(connoddto);
																			}
																			connoddto.setNumeroNota(numnc);
																		}
																		int respncp = connoddao.insertaConnod(connoddto);
																		
																		if (respncp>=0){
																			ncplogdto.setCodigoEmpresa(connoddto.getCodigoEmpresa());
																			ncplogdto.setTipo("P");
																			ncplogdto.setNumeroNota(connoddto.getNumeroNota());
																			ncplogdto.setFechaNota(connoddto.getFechaNota());
																			ncplogdto.setLineaNota(connoddto.getCorrelativo());
																			ncplogdto.setNumeroCarguio(cargu.getNumeroCarguio());
																			ncplogdto.setNumeroOrden(ordvddedto.getNumeroOV());
																			ncplogdto.setNumeroDocumento(ordvtadto.getNumFactbol());
																			ncplogdto.setCodigoBodega(ordvddedto.getCodigoBodega());
																			ncplogdto.setRutCliente(ordvtadto.getRutCliente());
																			ncplogdto.setDigCliente(ordvtadto.getDvCliente());
																			ncplogdto.setUsuario(userId);
																			ncplogdto.setTipoAccion("I");
																			ncplogdto.setCodigoArticulo(connoddto.getCodArticulo());
																			ncplogdto.setDigitoArticulo(connoddto.getDigArticulo());
																			ncplogdto.setCantidad(cantidevo);
																			ncplogdto.setCantidadArticulo(connoddto.getCantidad());
																			ncplogdto.setFechaUser(fecha);
																			ncplogdto.setHoraUser(horas);
																			ncplogdto.setIpEquipo(Integer.toString(cargu.getRutChofer()));
																			ncplogdto.setNombreEquipo(carguiocdto.getPatente().trim());
																			ncplogdto.setCodigoUsuario(userId);
																			int correlativoncplog = ncplogdao.buscaUltimaLineaNcplog(ncplogdto);
																			ncplogdto.setLineaNota(correlativoncplog);
																			int resplog = ncplogdao.insertaNcplog(ncplogdto);
																		}
																		
																		if (respncp<0){	logi.info("ERROR!! NO SE HA GENERADO CONNOD NCP");break; }
																			//Actualizar Estado CarguioD
																			int respactestad2 = carguioddao.actualizaEstadoArticuloCarguiodTransp(codigoEstadoOV,codigoMotivoOV,codigoEmpresa,numeroCarg, patente, codigoBodega, numeroOVe, rutclient, 7777777,numeroguia);
																			int respactestado = carguioddao.actualizaEstadoArticuloCarguiodTransp(codigoEstadoOV,codigoMotivoOV,codigoEmpresa,numeroCarg, patente, codigoBodega, numeroOVe, rutclient, 0,numeroguia); //codiarti
																			
																			int tiporede=0;
																			tiporede=cantiredespa;
																			if (tiporede==2){
																				tiporede=3;
																			}
																			carguioddao.actualizaRedespachod(codigoEmpresa, codigoBodega, numeroCarg, numeroOVe, tiporede);
																			//Acumular totales para connoH
																			cantiarti++;
																			totcostocab= totcostocab+Math.floor(detorden2.getCostoArticulo());
																			totcostonetocab=totcostonetocab+costototnet;
																			if ("C".equals(detorden2.getCombo())) {
																				totbrutocab=totbrutocab+(int)(Math.round(totalbrut));
																			} else {
																				totbrutocab=totbrutocab+(int)(Math.round(totalneto*impuestos));
																			}
																			totnetocab=(int) (totnetocab+totalneto);
																} //iter5
														} //iter4
													}
					
												} //generarncp
																	
														if (generarNCp==true && ("RC".equals(codigoEstadoOV))) {
															ConnohDTO connohdto = new ConnohDTO();
															connohdto.setTipoNota("P");
															connohdto.setNumeroNota(numnc);
															connohdto.setFechaNota(fecha);
															connohdto.setCodDocumento(ordvtadto.getTipoDocumento());
															connohdto.setNumeroDocumento(ordvtadto.getNumFactbol());
															connohdto.setRutCliente(ordvtadto.getRutCliente());
															connohdto.setDivCliente(ordvtadto.getDvCliente());
															connohdto.setCodigoMovimiento(2);
															connohdto.setCodigoBodega(codigoBodega);
															connohdto.setCodigoVendedor(ordvtadto.getCodigoVendedor());
															connohdto.setNombreCliente(ordvtadto.getNombreCliente());
															connohdto.setCantidadLineas(cantiarti);
															connohdto.setTotalCosto((int)(totcostocab));
															connohdto.setTotalCostoNeto((int)(totcostonetocab));
															if (totdesctonetocab>0){
																connohdto.setTotalDescuento(totdesctocab);
																connohdto.setTotalDescuentoNeto(totdesctonetocab);
															} else {
																connohdto.setTotalDescuento(ordvtadto.getTotalDescuento());
																connohdto.setTotalDescuentoNeto(ordvtadto.getTotalDescuentoneto());
															}
															connohdto.setMontoTotal(totbrutocab);
															connohdto.setMontoNeto(totnetocab);
															connohdto.setMontoIva(0);
															connohdto.setMontoExento(ordvtadto.getTotalExento());
															connohdto.setEstado("I");
															monEnt=Math.round(monDoc-totbrutocab);
															String codmotov="";
															codmotov=orden.getCodMotivo();
															if (codigoMotivoOV != null && !codigoMotivoOV.equals("")){
																codmotov=codigoMotivoOV;
															}
															String userId2 = String.format("%-29s", nombredeChofer);
															responsableNota=(userId2.substring(0, 29)+codmotov+cargu.getNumeroCarguio()).trim();
															connohdto.setResponsableNota(responsableNota);
															/*
															connohdto.setNumeroCarguio(cargu.getNumeroCarguio());
															connohdto.setNumeroOV(numeroOVe);
															connohdto.setMotivoNoventa(codigoMotivoOV);
															connohdto.setUsuarioNota(userId.trim());
															connohdto.setUsuarioFecha(fecha);
															connohdto.setUsuarioHora(horas);
															*/
															//Insert in Connoh
															if (existeconnoh>0){
																connohdto.setNumeroNota(existeconnoh);
																int existeconnoh2 = connohdao.existeConnoh(connohdto);
																if (existeconnoh2>0) {
																	int eliconnoh=connohdao.eliminaConnoh(connohdto);
																}
																connohdto.setNumeroNota(numnc);
															}
															connohdao.insertaConnoh(connohdto);
															
															numerNCpe=numnc;
															totalNCpe=totbrutocab;
															
															DocncpDTO docncpdto = new DocncpDTO();
															docncpdto.setTipoNota("P");
															docncpdto.setNumeroNota(numnc);
															docncpdto.setFechaNota(fecha);
															docncpdto.setCodigoBodega(codigoBodega);
															docncpdto.setRutCliente(ordvtadto.getRutCliente());
															docncpdto.setDigCliente(ordvtadto.getDvCliente());
															docncpdto.setCorrelativo(1);
															docncpdto.setNumeroOV(numeroOVe);
															docncpdto.setNumeroCarguio(cargu.getNumeroCarguio());
															docncpdto.setNumeroDocumento(ordvtadto.getNumFactbol());
															docncpdto.setNumeroGuia(numeroguia);
															docncpdto.setNumeroNcfinal(0);
															docncpdto.setFechaUsuario(fecha);
															docncpdto.setHoraUsuario(horas);
															docncpdto.setCodigoUsuario(userId);
															docncpdto.setMotivo(codmotov);
															//Insert in DOCNCP
															if (existeconnoh>0){
																docncpdto.setNumeroNota(existeconnoh);
															}
															int existedocncp = docncpdao.buscaDocncpDTO(docncpdto);
															if (existedocncp>0) {
																int elidocncp=docncpdao.eliminaDocncpDTO(docncpdto);
															}
															docncpdto.setNumeroNota(numnc);
															docncpdao.insertaDocncp(docncpdto);
																				
															CargcestDTO cargcestdto = new CargcestDTO();
															cargcestdto.setCodigoEmpresa(codigoEmpresa);
															cargcestdto.setNumcarguio(cargu.getNumeroCarguio());
															cargcestdto.setPatente(patente);
															cargcestdto.setCodigoBodega(codigoBodega);
															int correcargcest = cargcestdao.obtieneCorrelativo(codigoEmpresa, cargu.getNumeroCarguio(), patente, codigoBodega);
															cargcestdto.setCorrelativo(correcargcest);
															cargcestdto.setEstado(estadocargcab);
															cargcestdto.setFechaUsuario(fecha);
															cargcestdto.setHoraUsuario(horas);
															cargcestdto.setUsuario(userId);
															//Insert Table Cargcest
															int exiscargcest = cargcestdao.existeCargcestDTO(cargcestdto);
															if (exiscargcest<=0) {
																cargcestdao.insertaCargcest(cargcestdto);
															}
																				
															//update status CarguioC y CarguioD
															/*
															String estadocarguioc="";
															String estadocarguiod="";
															if ("E".equals(estadocar)){
																estadocarguioc="U";
																estadocarguiod="K";
															}
															else if ("L".equals(estadocar)){
																estadocarguioc="O";
																estadocarguiod="H";
															}
															if ( (estadocarguioc != null) && (!estadocarguioc.equals("")) ) { carguiodao.actualizarestadoCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocarguioc);}
															if ( (estadocarguiod != null) && (!estadocarguiod.equals("")) ) { carguiodao.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocarguiod,numeroOVe);}
															*/
															carguiodao.actualizarestadoCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocargcab);
															carguiodao.actualizarestadoDetalleCarguio(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), estadocargdet,numeroOVe);
															carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), numeroOVe,rutclient,1);
															
															
															
															//consolidado
															String todasNCpes = docncpdao.obtenerNcpes(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio()).trim();
															if (!todasNCpes.equals("")){
																
																CargconwDTO carcondtoeli = new CargconwDTO();
																carcondtoeli.setCodigoEmpresa(codigoEmpresa);
																carcondtoeli.setNumcarguio(numeroCarg);
																carcondtoeli.setPatente(patente);
																carcondtoeli.setCodigoBodega(codigoBodega);
																int elitodo=cargcondao.eliminaCargconwTransp(carcondtoeli);
																
																
																List listconsoli = connoddao.obtieneConsolidadoNCp(todasNCpes, codigoEmpresa, cargu.getNumeroCarguio(), codigoBodega);
																Iterator iter6 = listconsoli.iterator();
																while (iter6.hasNext()){
																	ConnodDTO listacon = (ConnodDTO) iter6.next();
																	
																	CargconwDTO carcondto = new CargconwDTO();
																	carcondto.setCodigoEmpresa(codigoEmpresa);
																	carcondto.setNumcarguio(cargu.getNumeroCarguio());
																	carcondto.setPatente(patente);
																	carcondto.setCodigoBodega(codigoBodega);
																	carcondto.setCodigoArticulo(listacon.getCodArticulo());
																	carcondto.setDigitoArticulo(listacon.getDigArticulo());
																	carcondto.setTipoCarguio("C");
																	carcondto.setCantidadArticulo(listacon.getCantidad());
																	carcondto.setFechaDevolucion(fecha);
																	carcondto.setCantidadConfirmada(0);
																	carcondto.setCantidadDiferencia(0);
																	int fechaExpiracion = carguioddao.obtieneFechaVencimientoArt(codigoEmpresa, codigoBodega, cargu.getNumeroCarguio(), listacon.getCodArticulo());
																	if (fechaExpiracion<=0){
																		fechaExpiracion=fecha;
																		//sumar 90 dias
																		String fechamas="";
																		Date	resulfecha;
																		Date date = new Date(fecha);
																		resulfecha = sumarfechas(date, diasSumar);
																		//logi.info(resulfecha);
																		fechamas= (resulfecha.toString());
																		String anofec = fechamas.substring(0,4).trim() ;
																		String mesfec = fechamas.substring(5,7).trim();
																		String diafec = fechamas.substring(8,10).trim();
																		fechaExpiracion=Integer.parseInt(anofec+mesfec+diafec);
																	}
																	carcondto.setFechaExpiracion(fechaExpiracion);
																	carcondto.setPrecioNeto(listacon.getPrecioNeto());
																	carcondto.setPrecioBruto(listacon.getPrecioUnitario());
																	int exitbl = cargcondao.buscaCargconwTransp(carcondto);
																	if (exitbl>=0){
																		exitbl=exitbl+listacon.getCantidad();
																		carcondto.setCantidadArticulo(exitbl);
																		int canti=cargcondao.actualizaCargconw(carcondto);
																	}
																	else {
																		cargcondao.insertaCargconw(carcondto);
																	}
																	
																	
																}
															}

															
															logi.info("\n");
															logi.info(" ------->   O.K.  NCp    G E N E R A D A    <-------   "+numnc);
															logi.info("\n");
															
																				
														} //generarncp
		
											 } //genera NCp = true
									 
									} //cantiredespa
								
							} //iter2
						}
					} //existecarguioest
				} //iter
			} // error
			
			
		
			if(codError>0){
				ErrorTransportistaDAO errordao = dao.getErrorTransportistaDAO();
				List listita = errordao.buscaErrorTransportista(numeritocarguio, codError); 
				String respdd = gson.toJson(listita);
				resp=respdd;
				logi.info("E R R O R : "+resp);
				
				EmailDTO emaildto = new EmailDTO();
				EnvioMailErrorTransportista enviomail = new EnvioMailErrorTransportista();
				enviomail.mail("SOLICITUD 5", numeritocarguio, resp,par000);
				
				
			}
				else {
					
						//redespacho
						if (solicitudderedespacho==false){
								
								String numeritodocto2="";
								if (Integer.parseInt(versionpipe)>0){
									numeritodocto2=numeritodocto+"|"+versionpipe;
								} else {
									numeritodocto2=Integer.toString(numeritodocto);
								}
								
								logi.info("E S T A D O   ------------------->   "+estEntr);
								logi.info("M O T I V O   ------------------->   "+estMoti);
								
								logi.info("E N V I A N D O     C O R R E O     A  .....   "+correoVendedor);
								EmailDTO emaildto = new EmailDTO();
								EnvioMailTransportista enviomail = new EnvioMailTransportista();
								if ((nombredeChofer != null) && (!nombredeChofer.equals(""))){
									nomChof=nombredeChofer;
								}
								
								enviomail.mail("", codigoVendedor, nombreVendedor, correoVendedor, 0, numOV, fecOV, horOV, fecEnt, horEnt, numeritodocto2, tipoDoc, fecDoc, rutCli, digCli, nomCli, dirCli, comuCli, monDoc, monEnt, nomChof, estEntr, estMoti, numerNCpe, totalNCpe,numeritocarguio);
								if ("ET-ENTREGADO".equals(estEntr)){
									carguiodao.actualizarestadoDetalleCarguioTerminado(codigoEmpresa, codigoBodega, numeritocarguio, numOV,rutCli,1);
								}
								logi.info("correo enviado  S O L I C I T U D      5    !!!!");
								
								
						} else {
							
							
							List compleredespa = carguioddao.buscaCarguiodRedespacho(codigoEmpresa, codigoBodega, rut, dv,numeritocarguio,2,numeritodocto);
							String par0000 = gson.toJson(compleredespa);
							
							
							DAOFactory daoo = DAOFactory.getInstance();
							RutservDAO rutserv = daoo.getRutServDAO();
							RutservDTO dto = rutserv.recuperaEndPointServlet("DATAMATRED");
							logi.info("antes del getendpoint");
							String ws=dto.getEndPoint().trim();
							logi.info("despues del getendpoint");
							logi.info("ws: "+ws);
							try {
								 
								URL url = new URL(ws.trim());
								//URL url = new URL("http://192.168.1.30:8080/ServiciosTransportistaWEB/servicioTransRest/wmsCaserita/post");
								logi.info("url: "+url);
								
								logi.info("antes del urlopenconnection");
								
								HttpURLConnection conn = (HttpURLConnection) url.openConnection();
								conn.setDoOutput(true);
								conn.setRequestMethod("POST");
								logi.info("antes del outputstream");
								OutputStream os = conn.getOutputStream();
								logi.info("despues del outputstream");
								os.write(par0000.getBytes());
								os.flush();
								
						 		if (conn.getResponseCode() != 200) {
									throw new RuntimeException("Failed : HTTP error code : "
											+ conn.getResponseCode());
								}
						 
								BufferedReader br = new BufferedReader(new InputStreamReader(
									(conn.getInputStream())));
						 
								String output;
								String respDTM="";
								logi.info("  ----------------->>>      Output from Server redespaCHO    .... \n");
								
								while ((output = br.readLine()) != null) {
									respDTM=respDTM+output +"\r\n";
									logi.info(output);
								}
								
								logi.info("  ---------------->>>      REDESPACHO  SOL 5  enviado a quadminds : "+par0000);
								
								conn.disconnect();
								
							  } catch (MalformedURLException e) {
								e.printStackTrace();
							  } catch (IOException e) {
								e.printStackTrace();
							  }
							
							
							
						}
					
					
						generatxt(5, par000, String.valueOf(rut), dv,numeritodocto,numeritocarguio);
						
						String numeritodocto2="";
						if (Integer.parseInt(versionpipe)>0){
							numeritodocto2=numeritodocto+"|"+versionpipe;
						} else {
							numeritodocto2=Integer.toString(numeritodocto);
						}
						
						List comple = carguioddao.buscaCarguiodTransp(codigoEmpresa, rut, dv, codigoBodega, cantidadredespacho,numeritodocto2, numeritocarguio,patente);
						String respd = gson.toJson(comple);
						resp=respd;
						generatxt(55, respd, String.valueOf(rut), dv,numeritodocto,numeritocarguio);
						
				}
			
			
			//END solicitud 5
			
		

			
			
		
		
		}else if (rut>0 && "6".equals(solicitud)){
			
			
			List completa = carguio.listarCarguiosTranspRezagados(rut, dv, numeritodecarguio);
			resp = gson.toJson(completa);
			logi.info("GSON REZAGADOS: "+resp);
			generatxt(6, resp, String.valueOf(rut), dv,0,numeritodecarguio);
			
		} //solicitud 6
		
		
		
		
		
		//dao.closeConnection();
		
		return resp;
		
		
		
		
}

	
	
	
	
	



	public String generatxt(int solici, String res, String rutCho, String dvCho, int numDoc, int numCar){
		Fecha fch = new Fecha();
		String fechaStr = fch.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fch.getYYYYMMDDHHMMSS().substring(8, 12);
		String ano = fch.getYYYYMMDDHHMMSS().substring(0, 4);
		String mes = fch.getYYYYMMDD().substring(4, 6);
		int mesin = Integer.parseInt(mes);
		String mesPal = fch.recuperaMes(mesin);

		String rutaLog="/home/ServiciosTransportistaWEB/log/";
		String carpeta = rutaLog+ano+"/"+mesPal+"/"+fch.getYYYYMMDDHHMMSS().substring(6, 8)+"/";
		
		File folder = new File(carpeta);
		if (folder.exists()){
			
		}else
		{
			folder.mkdirs();	
			
		}
		//logi.info("Ruta:"+carpeta);
		String nombreArchivo="";
		String archivoLog="";
		if (solici==1){
			nombreArchivo = numCar+"_"+rutCho+"_"+fechaStr+".log";
			archivoLog=carpeta+numCar+"_"+rutCho+"_"+fechaStr+".log";
		}else if (solici==11){
			nombreArchivo = numCar+"_"+rutCho+"_"+fechaStr+".log";
			archivoLog=carpeta+numCar+"_"+rutCho+"_"+fechaStr+".log";
		}else if (solici==2){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S2"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S2"+".log";
		}else if (solici==22){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S22"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S22"+".log";
		}else if (solici==3){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S3"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S3"+".log";
		}else if (solici==33){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S33"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S33"+".log";
		}else if (solici==4){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S4"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S4"+".log";
		}else if (solici==5){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S5"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S5"+".log";
		}else if (solici==55){
			nombreArchivo = numDoc+"_"+numCar+"_"+fechaStr+"_S55"+".log";
			archivoLog=carpeta+numDoc+"_"+numCar+"_"+fechaStr+"_S55"+".log";
		
		}else if (solici==6){
			nombreArchivo = "sol6_"+numDoc+"_"+numCar+"_"+fechaStr+".log";
			archivoLog=carpeta+"sol6_"+numDoc+"_"+numCar+"_"+fechaStr+".log";
		
		}
		
		File f=new File(archivoLog);
		if (f.exists()){
			logi.info("No borra");
		}
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
		logi.info("E  N  D      P R O Y E C T O     Q U A D M I N D S");
		logi.info("________________________________________________________________________________");
		logi.info("\r\n");
		
		fileWriterLog.write( res+"\n");
		fileWriterLog.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return nombreArchivo+"|"+archivoLog;
		
		
	}


	
	
	
	
	
	
	
	public static void main (String []args){
		
		String respu="";
		IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
		
		/*
		 * json para probar :
		 */
		
		
		//String input2 ="{\"rutChofer\":\"17603958\",\"dvChofer\":\"2\",\"solicitud\":\"1\",\"numcarguio\":\"47358\"}";
		
		//String input2 ="{\"rutChofer\":\"17951951\",\"dvChofer\":\"8\",\"solicitud\":\"1\",\"numcarguio\":\"40675\"}";
		//String input2 ="{\"rutChofer\":\"14277505\",\"dvChofer\":\"\",\"solicitud\":\"1\",\"numcarguio\":\"45230\"}";
		
		
		/*
		 {"carguio":[{"rutChofer":"17951951","dvChofer":"8",
		 "version":"",
		 "numeroCarguio":"40440",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"8832",
		 "comentario":"","timestamp":"2017-04-26 16:45:44",
		 "latitud":"-33.4292714","longitud":"-70.6477608",
		 "distancia":5100062.3064899,
		 "fotos":[],
		 "codEstado":"RC","descEstado":"RECHAZADO",
		 "codMotivo":"R","Descmotivo":"DIRECCION NO ENCONTRADA",
		 "detalle":[{"codigoArticulo":"15890","correlativo":"0","formato":"U","cantidad":"5","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"579","correlativo":"1","formato":"U","cantidad":"3",
		 "cantidadRecepcionada":"0"}]}]}],"solicitud":"3"}
		 */
		
		/*
		String input2 ="{\"solicitud\":\"3\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"40440\",\"version\":\"\",\"rutChofer\":\"17951951\",\"dvChofer\":\"8\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"8832|1\",\"tipoDocumento\":\"33\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"R\",\"Descmotivo\":\"DIRECCION NO ENCONTRADA\","
				+ "\"timestamp\":\"2017-07-31 16:49:32\",\"comentario\":\"\","
				+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"15890\",\"correlativo\":\"18\",\"formato\":\"U\",\"cantidad\":\"5\",\"cantidadRecepcionada\":\"0\"},"
				+ "{\"codigoArticulo\":\"579\",\"correlativo\":\"19\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"0\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		*/
		
		/*
		String input2 ="{\"solicitud\":\"5\","
				+ "\"carguio\":["
				+ "{\"rutChofer\":\"14277505\",\"dvChofer\":\"0\",\"version\":\"\",\"numeroCarguio\":\"45547\","
				+ "\"ordenes\":["
				+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"2048778\","
				+ "\"timestamp\":\"2017-07-31 17:32:28\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"D\",\"Descmotivo\":\"SIN DINERO\""
				+ "}]"
				+ "}]"
				+ "}";
		*/
		
		/*
		String input2 ="{\"solicitud\":\"5\","
				+ "\"carguio\":["
				+ "{\"rutChofer\":\"17951951\",\"dvChofer\":\"8\",\"version\":\"\",\"numeroCarguio\":\"40618\","
				+ "\"ordenes\":["
				+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"9600\","
				+ "\"timestamp\":\"2017-09-07 17:44:41\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"D\",\"Descmotivo\":\"SIN DINERO\""
				+ "}]"
				+ "}]"
				+ "}";
		*/
		
		
		/*
		{
			"solicitud": 5,
			"carguio": [{
				"rutChofer": "14277505",
				"dvChofer": "0",
				"version": "",
				"numeroCarguio": "45547",
				"ordenes": [{
					"tipoDocumento": "33",
					"numeroDocumento": "2048778",
					"timestamp": "2017-07-31 16:30:14",
					"codEstado": "RC",
					"descEstado": "RECHAZADO",
					"codMotivo": "D",
					"Descmotivo": "SIN DINERO"
				}]
			}]
		}
		*/
		
		/*
		{"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"",
		"numeroCarguio":"40503","ordenes":[{"tipoDocumento":"38",
		"numeroDocumento":"1311","comentario":"",
		"timestamp":"2017-05-29 10:55:06","latitud":"-33.4297525","longitud":"-70.6481849",
		"distancia":107458.33583088,"fotos":[],
		"codEstado":"RC","descEstado":"RECHAZADO",
		"codMotivo":"F","Descmotivo":"FORMA DE PAGO INCORRECTA",
		"detalle":[{"codigoArticulo":"16416","correlativo":"7","formato":"U","cantidad":"2","cantidadRecepcionada":"0"},
		{"codigoArticulo":"16413","correlativo":"6","formato":"U","cantidad":"2","cantidadRecepcionada":"0"},
		{"codigoArticulo":"5332","correlativo":"4","formato":"U","cantidad":"24","cantidadRecepcionada":"0"},
		{"codigoArticulo":"16418","correlativo":"8","formato":"U","cantidad":"2","cantidadRecepcionada":"0"},
		{"codigoArticulo":"16411","correlativo":"5","formato":"U","cantidad":"2","cantidadRecepcionada":"0"}
		]}]}],"solicitud":"2"}
		*/
		
		/*
		String input2 ="{\"solicitud\":\"3\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"40503\",\"version\":\"0\",\"rutChofer\":\"17951951\",\"dvChofer\":\"8\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"1313\",\"tipoDocumento\":\"38\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"H\",\"Descmotivo\":\"DIFERENCIA DE PRECIO\","
				+ "\"timestamp\":\"2017-05-29 16:17:58\",\"comentario\":\"\","
				+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"7369\",\"correlativo\":\"18\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"0\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		*/
		/*
		 {"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"","numeroCarguio":"3541",
		 "ordenes":[{"tipoDocumento":"38","numeroDocumento":"1346|1",
		 "comentario":"","timestamp":"2017-07-03 14:34:20","latitud":"-33.4296216","longitud":"-70.6474792",
		 "distancia":16.167696131747,"fotos":[],
		 "codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"Q","Descmotivo":"SIN CODIGO DE AUTORIZACION",
		 "detalle":[{"codigoArticulo":"18255","correlativo":"0","formato":"X","cantidad":"1","cantidadRecepcionada":"0"}
		 ]}]}],"solicitud":"3"}
		 */
		
		//{"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"","numeroCarguio":"40503","ordenes":[{"tipoDocumento":"38","numeroDocumento":"1313","comentario":"","timestamp":"2017-05-29 12:03:33","latitud":"-33.4298853","longitud":"-70.647492","distancia":106293.17364778,"fotos":[],"codEstado":"RC","descEstado":"RECHAZADO","
		//codMotivo":"J","Descmotivo":"PEDIDO NO SOLICITADO",
		//"detalle":[{"codigoArticulo":"233","correlativo":"14","formato":"U","cantidad":"10","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"10272","correlativo":"16","formato":"U","cantidad":"36","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"2043","correlativo":"17","formato":"U","cantidad":"30","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"482","correlativo":"15","formato":"U","cantidad":"20","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"7369","correlativo":"18","formato":"U","cantidad":"6","cantidadRecepcionada":"0"}
		//]}]}],"solicitud":"2"}
		
		 		
		//{"solicitud":5,"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"",
		//"numeroCarguio":"40479","ordenes":[{"tipoDocumento":"33","numeroDocumento":"9029|1",
		//"timestamp":"2017-05-22 10:06:41","codEstado":"RC","descEstado":"RECHAZADO",
		//"codMotivo":"P","Descmotivo":"PRODUCTO VENCIDO"}]}]}
		
		/*
 		String input2 ="{\"solicitud\":\"5\","
				+ "\"carguio\":["
				+ "{\"rutChofer\":\"1111111\",\"dvChofer\":\"4\",\"version\":\"\",\"numeroCarguio\":\"45087\","
				+ "\"ordenes\":["
				+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"2031689\","
				+ "\"timestamp\":\"2017-07-27 23:52:28\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"C\",\"Descmotivo\":\"RECHAZO CLIENTE\""
				+ "}]"
				+ "}]"
				+ "}";
		*/
		
		/*
		 {"solicitud":5,"carguio":[{"rutChofer":"1111111","dvChofer":"4","version":"","numeroCarguio":"45070",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2030835|1","timestamp":"2017-07-21 12:39:39",
		 "codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"P","Descmotivo":"PRODUCTO VENCIDO"}]}]}
		 */
		
		//String input2 ="{\"rutChofer\":\"17951951\",\"dvChofer\":\"8\",\"solicitud\":\"1\",\"numcarguio\":\"40475\"}";
		
		//String input2 ="{\"rutChofer\":\"17951951\",\"dvChofer\":\"8\",\"solicitud\":\"1\",\"numcarguio\":\"40489\"}";
		
		/*
		String input2 ="{\"solicitud\":\"3\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"40504\",\"version\":\"0\",\"rutChofer\":\"17951951\",\"dvChofer\":\"8\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"1319\",\"tipoDocumento\":\"38\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"R\",\"Descmotivo\":\"DIRECCION NO ENCONTRADA\","
				+ "\"timestamp\":\"2017-05-29 16:38:32\",\"comentario\":\"\","
				+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"16415\",\"correlativo\":\"11\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"0\"},"
				+ "{\"codigoArticulo\":\"16418\",\"correlativo\":\"12\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"0\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		*/
		
		/*
		String input2 ="{\"solicitud\":\"2\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"45173\",\"version\":\"0\",\"rutChofer\":\"14277505\",\"dvChofer\":\"\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"2035587\",\"tipoDocumento\":\"33\","
				+ "\"codEstado\":\"ET\",\"descEstado\":\"ENTREGADO\","
				+ "\"codMotivo\":\"ET\",\"Descmotivo\":\"ENTREGADO\","
				+ "\"timestamp\":\"2017-07-25 12:17:16\",\"comentario\":\"\","
				+ "\"latitud\":\"-33.540935\",\"longitud\":\"-70.6612183\",\"distancia\":\"156.95784257965\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"7777777\",\"correlativo\":\"176\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"1\"},"
				+ "{\"codigoArticulo\":\"15214\",\"correlativo\":\"117\",\"formato\":\"U\",\"cantidad\":\"4\",\"cantidadRecepcionada\":\"4\"},"
				+ "{\"codigoArticulo\":\"15215\",\"correlativo\":\"118\",\"formato\":\"U\",\"cantidad\":\"4\",\"cantidadRecepcionada\":\"4\"},"
				+ "{\"codigoArticulo\":\"15217\",\"correlativo\":\"119\",\"formato\":\"U\",\"cantidad\":\"4\",\"cantidadRecepcionada\":\"4\"},"
				+ "{\"codigoArticulo\":\"1322\",\"correlativo\":\"136\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"3\"},"
				+ "{\"codigoArticulo\":\"14374\",\"correlativo\":\"39\",\"formato\":\"U\",\"cantidad\":\"10\",\"cantidadRecepcionada\":\"10\"},"
				+ "{\"codigoArticulo\":\"8908\",\"correlativo\":\"12\",\"formato\":\"U\",\"cantidad\":\"24\",\"cantidadRecepcionada\":\"24\"},"
				+ "{\"codigoArticulo\":\"17641\",\"correlativo\":\"6\",\"formato\":\"U\",\"cantidad\":\"40\",\"cantidadRecepcionada\":\"40\"},"
				+ "{\"codigoArticulo\":\"1307\",\"correlativo\":\"97\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"17239\",\"correlativo\":\"81\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"8249\",\"correlativo\":\"62\",\"formato\":\"U\",\"cantidad\":\"10\",\"cantidadRecepcionada\":\"10\"},"
				+ "{\"codigoArticulo\":\"3309\",\"correlativo\":\"82\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"3296\",\"correlativo\":\"83\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"8461\",\"correlativo\":\"128\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"3\"},"
				+ "{\"codigoArticulo\":\"11620\",\"correlativo\":\"138\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"3\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		*/
		
		/*
		  {"carguio":[{"rutChofer":"14277505","dvChofer":"0","version":"","numeroCarguio":"45173",
		  "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2035587","comentario":"","timestamp":"2017-07-25 12:17:16",
		  "latitud":"-33.540935","longitud":"-70.6612183","distancia":156.95784257965,
			"fotos":[],"codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO",
			"detalle":[{"codigoArticulo":"7777777","correlativo":"176","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
			{"codigoArticulo":"15214","correlativo":"117","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
			{"codigoArticulo":"15215","correlativo":"118","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
			{"codigoArticulo":"15217","correlativo":"119","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
			{"codigoArticulo":"1322","correlativo":"136","formato":"U","cantidad":"3","cantidadRecepcionada":"3"},
			{"codigoArticulo":"14374","correlativo":"39","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
			{"codigoArticulo":"8908","correlativo":"12","formato":"U","cantidad":"24","cantidadRecepcionada":"24"},
			{"codigoArticulo":"17641","correlativo":"6","formato":"U","cantidad":"40","cantidadRecepcionada":"40"},
			{"codigoArticulo":"1307","correlativo":"97","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
			{"codigoArticulo":"17239","correlativo":"81","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
			{"codigoArticulo":"8249","correlativo":"62","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
			{"codigoArticulo":"3309","correlativo":"82","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
			{"codigoArticulo":"3296","correlativo":"83","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
			{"codigoArticulo":"8461","correlativo":"128","formato":"U","cantidad":"3","cantidadRecepcionada":"3"},
			{"codigoArticulo":"11620","correlativo":"138","formato":"U","cantidad":"3","cantidadRecepcionada":"3"}
			]}]}],"solicitud":"2"}
		 */
		
		//{"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"","numeroCarguio":"40609",
		//"ordenes":[{"tipoDocumento":"33","numeroDocumento":"9574","comentario":"",
		//"timestamp":"2017-07-20 17:15:26","latitud":"-33.4293793","longitud":"-70.6478798",
		//"distancia":27335.057444187,"fotos":[],"codEstado":"RC","descEstado":"RECHAZADO",
		//"codMotivo":"E","Descmotivo":"MERCADERIA EN MAL ESTADO",
		//"detalle":[{"codigoArticulo":"7777777","correlativo":"9","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"335","correlativo":"6","formato":"U","cantidad":"4","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"6883","correlativo":"8","formato":"U","cantidad":"3","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"8293","correlativo":"7","formato":"U","cantidad":"6","cantidadRecepcionada":"0"}
		//]}]}],"solicitud":"2"}
		
		
		//{"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"","numeroCarguio":"40602",
		//"ordenes":[{"tipoDocumento":"38","numeroDocumento":"1361","comentario":"","timestamp":"2017-07-18 17:40:14",
		//"latitud":"-33.4292716","longitud":"-70.6477579","distancia":131.3412646823,"fotos":[],
		//"codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"R","Descmotivo":"DIRECCION NO ENCONTRADA",
		//"detalle":[{"codigoArticulo":"387","correlativo":"3","formato":"U","cantidad":"6","cantidadRecepcionada":"0"},
		//{"codigoArticulo":"11117","correlativo":"2","formato":"U","cantidad":"7","cantidadRecepcionada":"0"}]
		//}]}],"solicitud":"2"}
		
		
		/*
		 {"carguio":[{"rutChofer":"17053712","dvChofer":"2","version":"","numeroCarguio":"40569",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"9419","comentario":"","timestamp":"2017-07-03 18:50:08",
		 "latitud":"-33.4293732","longitud":"-70.647708","distancia":3614.3131179479,"fotos":[],
		 "codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"Q","Descmotivo":"SIN CODIGO DE AUTORIZACION",
		 "detalle":[
		 {"codigoArticulo":"7777777","correlativo":"3","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10897","correlativo":"1","formato":"U","cantidad":"792","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10904","correlativo":"2","formato":"U","cantidad":"816","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10909","correlativo":"0","formato":"U","cantidad":"792","cantidadRecepcionada":"0"}
		 ]}]}],"solicitud":"2"}

		 */
		
		/*
		{
			  "solicitud": 5,
			  "carguio": [
			    {
			      "rutChofer": "17603958",
			      "dvChofer": "2",
			      "version": "",
			      "numeroCarguio": "45329",
			      "ordenes": [
			        {
			          "tipoDocumento": "33",
			          "numeroDocumento": "2041924",
			          "timestamp": "2017-07-27 14:52:57",
			          "codEstado": "RC",
			          "descEstado": "RECHAZADO",
			          "codMotivo": "C",
			          "Descmotivo": "RECHAZO CLIENTE"
			        }
			      ]
			    }
			  ]
			}
		*/
		
		
		/*
		String input2 ="{\"solicitud\":\"5\","
				+ "\"carguio\":["
				+ "{\"rutChofer\":\"17603958\",\"dvChofer\":\"2\",\"version\":\"\",\"numeroCarguio\":\"45329\","
				+ "\"ordenes\":["
				+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"2041924\","
				+ "\"timestamp\":\"2017-07-27 23:56:28\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"C\",\"Descmotivo\":\"RECHAZO CLIENTE\""
				+ "}]"
				+ "}]"
				+ "}";
		*/
		
		//{"solicitud":5,"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"",
		//"numeroCarguio":"40560","ordenes":[{"tipoDocumento":"33","numeroDocumento":"9362|1",
		//"timestamp":"2017-06-29 17:34:34","codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"Q","Descmotivo":"SIN CODIGO DE AUTORIZACION"}]}]}
		
		
		/*
		{"carguio":[{"rutChofer":"12489850","dvChofer":"1","version":"",
		"numeroCarguio":"45568","ordenes":[{"tipoDocumento":"33","numeroDocumento":"2049655|1",
		"comentario":"","timestamp":"2017-08-01 18:03:43","latitud":"-33.4509983","longitud":"-70.811995",
		"distancia":63.693617930935,"fotos":[],"codEstado":"RC","descEstado":"RECHAZADO",
		"codMotivo":"R","Descmotivo":"DIRECCION NO ENCONTRADA",
		"detalle":[{"codigoArticulo":"7777777","correlativo":"160","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		{"codigoArticulo":"387","correlativo":"12","formato":"U","cantidad":"24","cantidadRecepcionada":"0"},
		{"codigoArticulo":"5981","correlativo":"51","formato":"U","cantidad":"12","cantidadRecepcionada":"0"},
		{"codigoArticulo":"2907","correlativo":"3","formato":"U","cantidad":"36","cantidadRecepcionada":"0"}
		]}]}],"solicitud":"3"}
		 */
		
		/*
		String input2 ="{\"solicitud\":\"3\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"45568\",\"version\":\"0\",\"rutChofer\":\"12489850\",\"dvChofer\":\"1\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"2049655|1\",\"tipoDocumento\":\"33\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"R\",\"Descmotivo\":\"DIRECCION NO ENCONTRADA\","
				+ "\"timestamp\":\"2017-08-01 18:05:50\",\"comentario\":\"\","
				+ "\"latitud\":\"-33.4509983\",\"longitud\":\"-70.811995\",\"distancia\":\"63.693617930935\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"7777777\",\"correlativo\":\"160\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"0\"},"
				+ "{\"codigoArticulo\":\"387\",\"correlativo\":\"12\",\"formato\":\"U\",\"cantidad\":\"24\",\"cantidadRecepcionada\":\"0\"},"
				+ "{\"codigoArticulo\":\"5981\",\"correlativo\":\"51\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"0\"},"
				+ "{\"codigoArticulo\":\"2907\",\"correlativo\":\"3\",\"formato\":\"U\",\"cantidad\":\"36\",\"cantidadRecepcionada\":\"0\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		*/
		
		
		//{"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"","numeroCarguio":"3541","ordenes":[{"tipoDocumento":"38",
		//"numeroDocumento":"1346|1","comentario":"","timestamp":"2017-07-03 14:34:20","latitud":"-33.4296216","longitud":"-70.6474792",
		//"distancia":16.167696131747,"fotos":[],"codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"Q",
		//"Descmotivo":"SIN CODIGO DE AUTORIZACION","detalle":[{"codigoArticulo":"18255","correlativo":"0",
		//"formato":"X","cantidad":"1","cantidadRecepcionada":"0"}]}]}],"solicitud":"3"}
		
		/*
		{"carguio":[{"rutChofer":"17053712","dvChofer":"2","version":"","numeroCarguio":"40564",
		"ordenes":[{"tipoDocumento":"33","numeroDocumento":"9398","comentario":"","timestamp":"2017-06-30 17:58:04",
		"latitud":"-33.4294079","longitud":"-70.6476666","distancia":946388.37336259,"fotos":[],
		"codEstado":"EP","descEstado":"ENTREGA PARCIAL","codMotivo":"H","Descmotivo":"DIFERENCIA DE PRECIO",
		"detalle":[{"codigoArticulo":"7777777","correlativo":"3","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		{"codigoArticulo":"10897","correlativo":"1","formato":"U","cantidad":"792","cantidadRecepcionada":"0"},
		{"codigoArticulo":"10904","correlativo":"2","formato":"U","cantidad":"816","cantidadRecepcionada":"816"},
		{"codigoArticulo":"10909","correlativo":"0","formato":"U","cantidad":"792","cantidadRecepcionada":"792"}
		]}]}],"solicitud":"2"}
		*/

		
		/*
		  {"carguio":[{"rutChofer":"17053712","dvChofer":"2","version":"",
		  "numeroCarguio":"40553","ordenes":[{"tipoDocumento":"33","numeroDocumento":"9346",
		  "comentario":"","timestamp":"2017-06-28 13:30:50","latitud":"-33.4293538","longitud":"-70.6477507",
		  "distancia":5100074.051365,"fotos":[],
		  "codEstado":"EP","descEstado":"ENTREGA PARCIAL","codMotivo":"H","Descmotivo":"DIFERENCIA DE PRECIO",
		  "detalle":[
		  {"codigoArticulo":"10897","correlativo":"1","formato":"U","cantidad":"792","cantidadRecepcionada":"692"},
		  {"codigoArticulo":"10904","correlativo":"2","formato":"U","cantidad":"816","cantidadRecepcionada":"716"},
		  {"codigoArticulo":"10909","correlativo":"0","formato":"U","cantidad":"792","cantidadRecepcionada":"692"}
		  ]}]}],"solicitud":"2"}
		 */
		
		
		/*
		 {"carguio":[{"rutChofer":"9480888","dvChofer":"K","version":"","numeroCarguio":"47582",
		 "ordenes":[{"tipoDocumento":"34","numeroDocumento":"2983068","comentario":"","timestamp":"2017-09-02 15:31:56","latitud":"0","longitud":"0","distancia":5097196.2465337,"fotos":[],
		 "codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"D","Descmotivo":"SIN DINERO",
		 "detalle":[{"codigoArticulo":"7777777","correlativo":"299","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10897","correlativo":"254","formato":"U","cantidad":"4","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10904","correlativo":"237","formato":"U","cantidad":"5","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10909","correlativo":"265","formato":"U","cantidad":"4","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10898","correlativo":"180","formato":"U","cantidad":"6","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10905","correlativo":"192","formato":"U","cantidad":"6","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10908","correlativo":"181","formato":"U","cantidad":"6","cantidadRecepcionada":"0"}
		 ]}]}],"solicitud":"2"}
		 */
		
		/*	
		 {"carguio":[{"rutChofer":"9480888","dvChofer":"K","version":"","numeroCarguio":"48003",
		 "ordenes":[{"tipoDocumento":"34","numeroDocumento":"3000915","comentario":"","timestamp":"2017-09-09 15:27:13","latitud":"-33.457285",
		 "longitud":"-70.68767","distancia":491.33685279578,"fotos":[],"codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO",
		 "detalle":[
		 {"codigoArticulo":"6716","correlativo":"19","formato":"U","cantidad":"30","cantidadRecepcionada":"30"},
		 {"codigoArticulo":"2051","correlativo":"298","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"2067","correlativo":"294","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"14270","correlativo":"204","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"16475","correlativo":"40","formato":"U","cantidad":"20","cantidadRecepcionada":"20"},
		 {"codigoArticulo":"14271","correlativo":"141","formato":"U","cantidad":"10","cantidadRecepcionada":"10"}
		 ]}]}],"solicitud":"2"}
		*/
		
		String input2 ="{\"solicitud\":\"2\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"48082\",\"version\":\"0\",\"rutChofer\":\"16523102\",\"dvChofer\":\"3\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"2133146\",\"tipoDocumento\":\"33\","
				+ "\"codEstado\":\"ET\",\"descEstado\":\"ENTREGADO\","
				+ "\"codMotivo\":\"ET\",\"Descmotivo\":\"ENTREGADO\","
				+ "\"timestamp\":\"2017-09-09 15:27:32\",\"comentario\":\"\","
				+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"7777777\",\"correlativo\":\"195\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"1\"},"
				+ "{\"codigoArticulo\":\"8323\",\"correlativo\":\"167\",\"formato\":\"U\",\"cantidad\":\"4\",\"cantidadRecepcionada\":\"4\"},"
				+ "{\"codigoArticulo\":\"10908\",\"correlativo\":\"160\",\"formato\":\"U\",\"cantidad\":\"5\",\"cantidadRecepcionada\":\"5\"},"
				+ "{\"codigoArticulo\":\"11603\",\"correlativo\":\"178\",\"formato\":\"U\",\"cantidad\":\"3\",\"cantidadRecepcionada\":\"3\"},"
				+ "{\"codigoArticulo\":\"11471\",\"correlativo\":\"196\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"1\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		
		/*
		 {"carguio":[{"rutChofer":"16523102","dvChofer":"3","version":"","numeroCarguio":"48082",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2133146","comentario":"",
		 "timestamp":"2017-09-11 11:18:53","latitud":"-33.46694","longitud":"-70.6897483","distancia":613.85207535775,
		 "fotos":[],"codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO",
		 "detalle":[{"codigoArticulo":"7777777","correlativo":"195","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"8323","correlativo":"167","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
		 {"codigoArticulo":"10908","correlativo":"160","formato":"U","cantidad":"5","cantidadRecepcionada":"5"},
		 {"codigoArticulo":"11603","correlativo":"178","formato":"U","cantidad":"3","cantidadRecepcionada":"3"},
		 {"codigoArticulo":"11471","correlativo":"196","formato":"U","cantidad":"1","cantidadRecepcionada":"1"}
		 ]}]}],"solicitud":"2"}
		 
		 */
		
		/*
		 {"carguio":[{"rutChofer":"17131424","dvChofer":"0","version":"","numeroCarguio":"48082",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2133148","comentario":"","timestamp":"2017-09-11 10:15:24",
		 "latitud":"-33.4633067","longitud":"-70.722695","distancia":823.14278143923,"fotos":[],
		 "codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO","detalle":[
		 {"codigoArticulo":"7777777","correlativo":"200","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"10907","correlativo":"179","formato":"U","cantidad":"3","cantidadRecepcionada":"3"},
		 {"codigoArticulo":"10897","correlativo":"168","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
		 {"codigoArticulo":"9968","correlativo":"104","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		 {"codigoArticulo":"9967","correlativo":"103","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		 {"codigoArticulo":"1244","correlativo":"166","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
		 {"codigoArticulo":"9060","correlativo":"113","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"923","correlativo":"49","formato":"U","cantidad":"18","cantidadRecepcionada":"18"},
		 {"codigoArticulo":"17893","correlativo":"163","formato":"U","cantidad":"4","cantidadRecepcionada":"4"}
		 ]}]}],"solicitud":"2"}
		 */
		
		/*
		 {"carguio":[{"rutChofer":"17131424","dvChofer":"0","version":"","numeroCarguio":"48082",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2133148","comentario":"","timestamp":"2017-09-11 10:15:24","latitud":"-33.4633067","longitud":"-70.722695","distancia":823.14278143923,
		 "fotos":[],
		 "codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO",
		 "detalle":[{"codigoArticulo":"7777777","correlativo":"200","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"10907","correlativo":"179","formato":"U","cantidad":"3","cantidadRecepcionada":"3"},
		 {"codigoArticulo":"10897","correlativo":"168","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
		 {"codigoArticulo":"9968","correlativo":"104","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		 {"codigoArticulo":"9967","correlativo":"103","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		 {"codigoArticulo":"1244","correlativo":"166","formato":"U","cantidad":"4","cantidadRecepcionada":"4"},
		 {"codigoArticulo":"9060","correlativo":"113","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"923","correlativo":"49","formato":"U","cantidad":"18","cantidadRecepcionada":"18"},
		 {"codigoArticulo":"17893","correlativo":"163","formato":"U","cantidad":"4","cantidadRecepcionada":"4"}
		 ]}]}],"solicitud":"2"}
		 */
		
		/*
		 {"carguio":[{"rutChofer":"9480888","dvChofer":"K","version":"","numeroCarguio":"48003",
		 "ordenes":[{"tipoDocumento":"34","numeroDocumento":"3000915","comentario":"","timestamp":"2017-09-09 15:27:13","latitud":"-33.457285",
		 "longitud":"-70.68767","distancia":491.33685279578,"fotos":[],"codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO",
		 "detalle":[
		 {"codigoArticulo":"6716","correlativo":"19","formato":"U","cantidad":"30","cantidadRecepcionada":"30"},
		 {"codigoArticulo":"2051","correlativo":"298","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"2067","correlativo":"294","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"14270","correlativo":"204","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"16475","correlativo":"40","formato":"U","cantidad":"20","cantidadRecepcionada":"20"},
		 {"codigoArticulo":"14271","correlativo":"141","formato":"U","cantidad":"10","cantidadRecepcionada":"10"}
		 ]}]}],"solicitud":"2"}
		 
		 */
		
		
		
		/*
		 {"carguio":[{"rutChofer":"9480888","dvChofer":"K","version":"","numeroCarguio":"48003",
		 "ordenes":[{"tipoDocumento":"34","numeroDocumento":"3000915","comentario":"","timestamp":"2017-09-09 15:22:18",
		 "latitud":"0","longitud":"0","distancia":5096186.0818742,"fotos":[],"codEstado":"ET","descEstado":"ENTREGADO",
		 "codMotivo":"ET","Descmotivo":"ENTREGADO","detalle":[
		 {"codigoArticulo":"6716","correlativo":"19","formato":"U","cantidad":"30","cantidadRecepcionada":"30"},
		 {"codigoArticulo":"2051","correlativo":"298","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"2067","correlativo":"294","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"14270","correlativo":"204","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"16475","correlativo":"40","formato":"U","cantidad":"20","cantidadRecepcionada":"20"},
		 {"codigoArticulo":"14271","correlativo":"141","formato":"U","cantidad":"10","cantidadRecepcionada":"10"}
		 ]}]}],"solicitud":"2"}
		 
		 */
		
		
		/*
		  {"carguio":[{"rutChofer":"17053712","dvChofer":"2","version":"","numeroCarguio":"47595",
		  "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2118078","comentario":"",
		  "timestamp":"2017-09-02 13:23:56","latitud":"-33.417655","longitud":"-70.657645",
		  "distancia":130.82691035201,"fotos":[],
		  "codEstado":"EP","descEstado":"ENTREGA PARCIAL",
		  "codMotivo":"H","Descmotivo":"DIFERENCIA DE PRECIO",
		  "detalle":[{"codigoArticulo":"1816","correlativo":"113","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		  {"codigoArticulo":"7777777","correlativo":"251","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		  {"codigoArticulo":"82","correlativo":"114","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		  {"codigoArticulo":"17858","correlativo":"110","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		  {"codigoArticulo":"5186","correlativo":"112","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		  {"codigoArticulo":"5185","correlativo":"111","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		  {"codigoArticulo":"2068","correlativo":"139","formato":"U","cantidad":"6","cantidadRecepcionada":"6"}]}
		  ]}],"solicitud":"2"}
		 */
		
		
		/*
		 
		{
		  "solicitud": 5,
		  "carguio": [
		    {
		      "rutChofer": "14277505",
		      "dvChofer": "0",
		      "version": "",
		      "numeroCarguio": "45230",
		      "ordenes": [
		        {
		          "tipoDocumento": "33",
		          "numeroDocumento": "2037541",
		          "timestamp": "2017-07-26 13:51:09",
		          "codEstado": "RC",
		          "descEstado": "RECHAZADO",
		          "codMotivo": "M",
		          "Descmotivo": "SIN MORADORES"
		        }
		      ]
		    }
		  ]
		}
 
		 */
		
		/*
		String input2 ="{\"solicitud\":\"5\","
				+ "\"carguio\":["
				+ "{\"rutChofer\":\"14277505\",\"dvChofer\":\"\",\"version\":\"\",\"numeroCarguio\":\"45230\","
				+ "\"ordenes\":["
				+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"2037541\","
				+ "\"timestamp\":\"2017-07-26 13:55:28\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"M\",\"Descmotivo\":\"SIN MORADORES\""
				+ "}]"
				+ "}]"
				+ "}";
		*/
		
		/*
		  {"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"","numeroCarguio":"40630",
		  "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2051967","comentario":"","timestamp":"2017-08-01 11:36:40",
		  "latitud":"-33.4292157","longitud":"-70.647843","distancia":9665.1057342751,"fotos":[],
		  "codEstado":"ET","descEstado":"ENTREGADO","codMotivo":"ET","Descmotivo":"ENTREGADO",
		  "detalle":[{"codigoArticulo":"17654","correlativo":"1","formato":"U","cantidad":"5","cantidadRecepcionada":"5"},
		  {"codigoArticulo":"16237","correlativo":"0","formato":"U","cantidad":"4","cantidadRecepcionada":"4"}]}]}],
		  "solicitud":"2"}
		 */
		
		/*
		String input2 ="{\"solicitud\":\"5\","
				+ "\"carguio\":["
				+ "{\"rutChofer\":\"14277505\",\"dvChofer\":\"\",\"version\":\"0\",\"numeroCarguio\":\"45962\","
				+ "\"ordenes\":["
				+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"2062247\","
				+ "\"timestamp\":\"2017-08-07 13:50:28\","
				+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
				+ "\"codMotivo\":\"D\",\"Descmotivo\":\"SIN DINERO\""
				+ "}]"
				+ "}]"
				+ "}";
		*/
		
		
		/*
		 {"solicitud":5,"carguio":[{"rutChofer":"14277505","dvChofer":"","version":"","numeroCarguio":"45962",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2062247","timestamp":"2017-08-07 13:11:36",
		 "codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"D","Descmotivo":"SIN DINERO"}]}]}
		 */
		
		
		/*
		String input2 ="{\"solicitud\":\"2\","
				+ "\"carguio\":["
				+ "{\"numeroCarguio\":\"46493\",\"version\":\"0\",\"rutChofer\":\"12489850\",\"dvChofer\":\"1\","
				+ "\"ordenes\":["
				+ "{\"numeroDocumento\":\"2079824\",\"tipoDocumento\":\"33\","
				+ "\"codEstado\":\"ET\",\"descEstado\":\"ENTREGADO\","
				+ "\"codMotivo\":\"ET\",\"Descmotivo\":\"ENTREGADO\","
				+ "\"timestamp\":\"2017-08-16 13:56:32\",\"comentario\":\"\","
				+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
				+ "\"fotos\":[],"
				+ "\"detalle\":["
				+ "{\"codigoArticulo\":\"7777777\",\"correlativo\":\"173\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"1\"},"
				+ "{\"codigoArticulo\":\"84\",\"correlativo\":\"98\",\"formato\":\"U\",\"cantidad\":\"8\",\"cantidadRecepcionada\":\"8\"},"
				+ "{\"codigoArticulo\":\"2706\",\"correlativo\":\"99\",\"formato\":\"U\",\"cantidad\":\"8\",\"cantidadRecepcionada\":\"8\"},"
				+ "{\"codigoArticulo\":\"16965\",\"correlativo\":\"90\",\"formato\":\"U\",\"cantidad\":\"10\",\"cantidadRecepcionada\":\"10\"},"
				+ "{\"codigoArticulo\":\"5332\",\"correlativo\":\"57\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"},"
				+ "{\"codigoArticulo\":\"10248\",\"correlativo\":\"124\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"15256\",\"correlativo\":\"13\",\"formato\":\"U\",\"cantidad\":\"24\",\"cantidadRecepcionada\":\"24\"},"
				+ "{\"codigoArticulo\":\"17239\",\"correlativo\":\"91\",\"formato\":\"U\",\"cantidad\":\"10\",\"cantidadRecepcionada\":\"10\"},"
				+ "{\"codigoArticulo\":\"13965\",\"correlativo\":\"48\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"},"
				+ "{\"codigoArticulo\":\"9865\",\"correlativo\":\"47\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"},"
				+ "{\"codigoArticulo\":\"7002\",\"correlativo\":\"62\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"},"
				+ "{\"codigoArticulo\":\"8845\",\"correlativo\":\"136\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"1472\",\"correlativo\":\"100\",\"formato\":\"U\",\"cantidad\":\"8\",\"cantidadRecepcionada\":\"8\"},"
				+ "{\"codigoArticulo\":\"8207\",\"correlativo\":\"34\",\"formato\":\"U\",\"cantidad\":\"20\",\"cantidadRecepcionada\":\"20\"},"
				+ "{\"codigoArticulo\":\"15804\",\"correlativo\":\"65\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"},"
				+ "{\"codigoArticulo\":\"15805\",\"correlativo\":\"121\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"6\"},"
				+ "{\"codigoArticulo\":\"1643\",\"correlativo\":\"44\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"},"
				+ "{\"codigoArticulo\":\"1641\",\"correlativo\":\"45\",\"formato\":\"U\",\"cantidad\":\"12\",\"cantidadRecepcionada\":\"12\"}"
				+ "]}"
				+ "]}"
				+ "]"
				+ "}";
		*/
		
		
		/*
		 {"carguio":[{"rutChofer":"12489850","dvChofer":"1","version":"","numeroCarguio":"46493",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2079824","comentario":"","timestamp":"2017-08-16 13:52:35",
		 "latitud":"-32.5771733","longitud":"-70.70823","distancia":23428.73037739,"fotos":[],"codEstado":"ET","descEstado":"ENTREGADO",
		 "codMotivo":"ET",
		 "Descmotivo":"ENTREGADO","detalle":[
		 {"codigoArticulo":"7777777","correlativo":"173","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"84","correlativo":"98","formato":"U","cantidad":"8","cantidadRecepcionada":"8"},
		 {"codigoArticulo":"2706","correlativo":"99","formato":"U","cantidad":"8","cantidadRecepcionada":"8"},
		 {"codigoArticulo":"16965","correlativo":"90","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		 {"codigoArticulo":"5332","correlativo":"57","formato":"U","cantidad":"12","cantidadRecepcionada":"12"},
		 {"codigoArticulo":"10248","correlativo":"124","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"15256","correlativo":"13","formato":"U","cantidad":"24","cantidadRecepcionada":"24"},
		 {"codigoArticulo":"17239","correlativo":"91","formato":"U","cantidad":"10","cantidadRecepcionada":"10"},
		 {"codigoArticulo":"13965","correlativo":"48","formato":"U","cantidad":"12","cantidadRecepcionada":"12"},
		 {"codigoArticulo":"9865","correlativo":"47","formato":"U","cantidad":"12","cantidadRecepcionada":"12"},
		 {"codigoArticulo":"7002","correlativo":"62","formato":"U","cantidad":"12","cantidadRecepcionada":"12"},
		 {"codigoArticulo":"8845","correlativo":"136","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"1472","correlativo":"100","formato":"U","cantidad":"8","cantidadRecepcionada":"8"},
		 {"codigoArticulo":"8207","correlativo":"34","formato":"U","cantidad":"20","cantidadRecepcionada":"20"},
		 {"codigoArticulo":"15804","correlativo":"65","formato":"U","cantidad":"12","cantidadRecepcionada":"12"},
		 {"codigoArticulo":"15805","correlativo":"121","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"1643","correlativo":"44","formato":"U","cantidad":"12","cantidadRecepcionada":"12"},
		 {"codigoArticulo":"1641","correlativo":"45","formato":"U","cantidad":"12","cantidadRecepcionada":"12"}
		 ]}]}],"solicitud":"2"}
		 */
		
		/*
		 {"carguio":[{"rutChofer":"17053712","dvChofer":"2","version":"","numeroCarguio":"46102",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2068596","comentario":"","timestamp":"2017-08-09 17:54:10",
		 "latitud":"-33.4258533","longitud":"-70.6461267","distancia":4244.6263172933,"fotos":[],
		 "codEstado":"EP","descEstado":"ENTREGA PARCIAL","codMotivo":"H","Descmotivo":"DIFERENCIA DE PRECIO",
		 "detalle":[{"codigoArticulo":"7777777","correlativo":"304","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"10904","correlativo":"169","formato":"U","cantidad":"6","cantidadRecepcionada":"6"},
		 {"codigoArticulo":"10915","correlativo":"246","formato":"U","cantidad":"3","cantidadRecepcionada":"3"},
		 {"codigoArticulo":"17641","correlativo":"21","formato":"U","cantidad":"40","cantidadRecepcionada":"40"},
		 {"codigoArticulo":"8863","correlativo":"217","formato":"U","cantidad":"5","cantidadRecepcionada":"5"}
		 ]}]}],"solicitud":"2"}
		 */
		
		
		/*
		 {"carguio":[{"rutChofer":"17603958","dvChofer":"2","version":"","numeroCarguio":"46100",
		 "ordenes":[{"tipoDocumento":"33","numeroDocumento":"2068525","comentario":"","timestamp":"2017-08-09 18:04:11",
		 "latitud":"-33.473415","longitud":"-70.55594","distancia":141.07555720202,"fotos":[],
		 "codEstado":"EP","descEstado":"ENTREGA PARCIAL","codMotivo":"H","Descmotivo":"DIFERENCIA DE PRECIO",
		 "detalle":[{"codigoArticulo":"7777777","correlativo":"270","formato":"U","cantidad":"1","cantidadRecepcionada":"0"},
		 {"codigoArticulo":"8564","correlativo":"272","formato":"U","cantidad":"1","cantidadRecepcionada":"1"},
		 {"codigoArticulo":"17641","correlativo":"15","formato":"U","cantidad":"40","cantidadRecepcionada":"40"},
		 {"codigoArticulo":"6505","correlativo":"182","formato":"U","cantidad":"7","cantidadRecepcionada":"7"},
		 {"codigoArticulo":"8863","correlativo":"183","formato":"U","cantidad":"7","cantidadRecepcionada":"7"}]}]}],"solicitud":"2"}
		 */
		
		
		
		
		
		
		helper.procesa(input2);
		
				
	}
		
	
		
	public Date sumarfechas(Date fechy, int dias){
		
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = new GregorianCalendar();
		calendar1.add(Calendar.DATE, dias);
		Date fechaActual = new Date(System.currentTimeMillis());
		fechaActual.setTime(calendar1.getTimeInMillis());
		return fechaActual;
		
	}
		
	
	public String respuestaenvio(String par000, int rut, String dv, int numerodeCarguio){
			
			IntegracionTransportistaHelper helper = new IntegracionTransportistaHelper();
			String json=par000;
			
			DAOFactory dao = DAOFactory.getInstance();
			CarguioDAO carguiodao = dao.getCarguioDAO();
			
			RutservDAO rutserv = dao.getRutServDAO();
			RespquadDAO dtoquad = dao.getRespquadDAO();
			
			RutservDTO dto = rutserv.recuperaEndPointServlet("DATAMATRIX");
			
			
			logi.info("ANtes del getENDpoint");
			String ws=dto.getEndPoint().trim();
			logi.info("DEspues del getENDpoint");
			
			logi.info("ws: "+ws);
			
			try{
			
			
			//URL url = new URL("https://dev.quadminds.com/caserita/interfaz_carguios.php");
			URL url = new URL(ws.trim());
			logi.info("url: "+url);
					
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			logi.info("antes del outputstream");
			OutputStream os = conn.getOutputStream();
			logi.info("despues del outputstream");
			os.write(json.getBytes());
			os.flush();
			
	 		if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
	 
			logi.info("OUTput From ServeR .... \n");
			
			
			int cantidadoves = carguiodao.contarCarguiosTransp(rut, dv,numerodeCarguio);
			
			
			String	output;
			String	respDTM="";
			int		enviacorreoerror=0;
			int 	cantiordenesresp=0;
			
			while ((output = br.readLine()) != null) {
				respDTM=respDTM+output +"\r\n";
				logi.info(output);
				
				if (!output.equals("")){
					int iniciodospuntos = output.indexOf(":");
					String numerorecibo=(output.substring(iniciodospuntos + 2).trim());
					String glosarrecibo=(output.substring(0,iniciodospuntos).trim());
					
					if (("Ordenes exitosas".equals(glosarrecibo))){
						cantiordenesresp=Integer.parseInt(numerorecibo);
					}
					
					int tieneerror = output.indexOf("error");
					if (tieneerror>0 && Integer.parseInt(numerorecibo)>0){
						enviacorreoerror=1;
					}
					
					
				}
			}
			
			
			logi.info("E  N  D      P R O Y E C T O     Q U A D M I N D S.... ANTES DE CORREO");
			logi.info("\r\n");
			
			
		} catch (MalformedURLException e) {
			 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		  }
		
		
	return par000;
		
	}
		
}




















/*
 * para hacer pruebas con json
 */


//String input2 ="{\"rutChofer\":\"15154311\",\"dvChofer\":\"1\",\"solicitud\":\"1\",\"numcarguio\":\"38185\"}";
//String input2 ="{\"rutChofer\":\"10111222\",\"dvChofer\":\"5\",\"solicitud\":\"1\",\"numcarguio\":\"40401\"}";


/*
String input2 ="{\"solicitud\":\"2\","
		+ "\"carguio\":["
		+ "{\"numeroCarguio\":\"40150\",\"version\":\"0\",\"rutChofer\":\"17951951\",\"dvChofer\":\"8\","
		+ "\"ordenes\":["
		+ "{\"numeroDocumento\":\"7775\",\"tipoDocumento\":\"33\","
		+ "\"codEstado\":\"EP\",\"descEstado\":\"ENTREGA PARCIAL\","
		+ "\"codMotivo\":\"N\",\"Descmotivo\":\"PEDIDO REPETIDO\","
		+ "\"timestamp\":\"2016-12-30 13:16:32\",\"comentario\":\"\","
		+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
		+ "\"fotos\":[],"
		+ "\"detalle\":["
		+ "{\"codigoArticulo\":\"16226\",\"correlativo\":\"1\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"0\"},"
		+ "{\"codigoArticulo\":\"443\",\"correlativo\":\"4\",\"formato\":\"U\",\"cantidad\":\"1\",\"cantidadRecepcionada\":\"0\"}"
		+ "]}"
		+ "]}"
		+ "]"
		+ "}";
*/


/*
String input2 ="{\"solicitud\":\"5\","
		+ "\"carguio\":["
		+ "{\"rutChofer\":\"17951951\",\"dvChofer\":\"8\",\"version\":\"\",\"numeroCarguio\":\"40148\","
		+ "\"ordenes\":["
		+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"7753|1\","
		+ "\"timestamp\":\"2016-12-29 16:05:41\","
		+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
		+ "\"codMotivo\":\"E\",\"Descmotivo\":\"MERCADERIA EN MAL ESTADO\""
		+ "}]"
		+ "}]"
		+ "}";
	*/	


		/*
		{"solicitud":5,"carguio":[{"rutChofer":"17951951","dvChofer":"8","version":"",
			"numeroCarguio":"40148","ordenes":[{"tipoDocumento":"33","numeroDocumento":"7753|1",
				"timestamp":"2016-12-29 16:01:20",
				"codEstado":"RC","descEstado":"RECHAZADO",
				"codMotivo":"E","Descmotivo":"MERCADERIA EN MAL ESTADO"}]}]}
		*/


/*
{"carguio":[{"rutChofer":"18999000","dvChofer":"6","version":"",
"numeroCarguio":"40220","ordenes":[{"tipoDocumento":"33","numeroDocumento":"8087",
"comentario":"","timestamp":"2017-01-19 13:15:47","latitud":"-33.4293061","longitud":"-70.6477751","distancia":5100067.2524514,
"fotos":[],"codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"R","Descmotivo":"DIRECCION NO ENCONTRADA",
"detalle":[{"codigoArticulo":"16227","correlativo":"1","formato":"U","cantidad":"5","cantidadRecepcionada":"0"},
{"codigoArticulo":"9275","correlativo":"2","formato":"U","cantidad":"7","cantidadRecepcionada":"0"},
{"codigoArticulo":"9311","correlativo":"0","formato":"U","cantidad":"10","cantidadRecepcionada":"0"}
]}]}],"solicitud":"2"}
*/


/*
String input2 ="{\"solicitud\":\"2\","
		+ "\"carguio\":["
		+ "{\"numeroCarguio\":\"40220\",\"version\":\"0\",\"rutChofer\":\"18999000\",\"dvChofer\":\"6\","
		+ "\"ordenes\":["
		+ "{\"numeroDocumento\":\"8087\",\"tipoDocumento\":\"33\","
		+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
		+ "\"codMotivo\":\"R\",\"Descmotivo\":\"DIRECCION NO ENCONTRADA\","
		+ "\"timestamp\":\"2017-01-19 13:23:32\",\"comentario\":\"\","
		+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
		+ "\"fotos\":[],"
		+ "\"detalle\":["
		+ "{\"codigoArticulo\":\"16227\",\"correlativo\":\"1\",\"formato\":\"U\",\"cantidad\":\"5\",\"cantidadRecepcionada\":\"0\"},"
		+ "{\"codigoArticulo\":\"9275\",\"correlativo\":\"2\",\"formato\":\"U\",\"cantidad\":\"7\",\"cantidadRecepcionada\":\"0\"},"
		+ "{\"codigoArticulo\":\"9311\",\"correlativo\":\"0\",\"formato\":\"U\",\"cantidad\":\"10\",\"cantidadRecepcionada\":\"0\"}"
		+ "]}"
		+ "]}"
		+ "]"
		+ "}";
*/


/*
String input2 ="{\"solicitud\":\"3\","
		+ "\"carguio\":["
		+ "{\"numeroCarguio\":\"40078\",\"version\":\"0\",\"rutChofer\":\"17951951\",\"dvChofer\":\"8\","
		+ "\"ordenes\":["
		+ "{\"numeroDocumento\":\"7355\",\"tipoDocumento\":\"33\","
		+ "\"codEstado\":\"ET\",\"descEstado\":\"ENTREGADO\","
		+ "\"codMotivo\":\"ET\",\"Descmotivo\":\"ENTREGADO\","
		+ "\"timestamp\":\"2016-12-13 17:22:12\",\"comentario\":\"\","
		+ "\"latitud\":\"-33.429329\",\"longitud\":\"-70.6476862\",\"distancia\":\"5100070.5165003\","
		+ "\"fotos\":\"https://storage.googleapis.com/quadminds-fotos/Array\","
		+ "\"detalle\":["
		+ "]}"
		+ "]}"
		+ "]"
		+ "}";
*/
    		


 	//{"solicitud":5,"carguio":[{"rutChofer":"10111222","dvChofer":"5",
//"version":"","numeroCarguio":"40350","ordenes":
//[{"tipoDocumento":"33","numeroDocumento":"8365","timestamp":"2017-02-24 11:17:46",
//"codEstado":"RC","descEstado":"RECHAZADO","codMotivo":"P","Descmotivo":"PRODUCTO VENCIDO"}]}]}

/*
String input2 ="{\"solicitud\":\"2\","
		+ "\"carguio\":["
		+ "{\"numeroCarguio\":\"40220\",\"version\":\"0\",\"rutChofer\":\"18999000\",\"dvChofer\":\"6\","
		+ "\"ordenes\":["
		+ "{\"numeroDocumento\":\"8087\",\"tipoDocumento\":\"33\","
		+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
		+ "\"codMotivo\":\"R\",\"Descmotivo\":\"DIRECCION NO ENCONTRADA\","
		+ "\"timestamp\":\"2017-01-19 13:23:32\",\"comentario\":\"\","
		+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
		+ "\"fotos\":[],"
		+ "\"detalle\":["
		+ "{\"codigoArticulo\":\"16227\",\"correlativo\":\"1\",\"formato\":\"U\",\"cantidad\":\"5\",\"cantidadRecepcionada\":\"0\"},"
		+ "{\"codigoArticulo\":\"9275\",\"correlativo\":\"2\",\"formato\":\"U\",\"cantidad\":\"7\",\"cantidadRecepcionada\":\"0\"},"
		+ "{\"codigoArticulo\":\"9311\",\"correlativo\":\"0\",\"formato\":\"U\",\"cantidad\":\"10\",\"cantidadRecepcionada\":\"0\"}"
		+ "]}"
		+ "]}"
		+ "]"
		+ "}";
*/

	/*
 	String input2 ="{\"solicitud\":\"5\","
			+ "\"carguio\":["
			+ "{\"rutChofer\":\"17358358\",\"dvChofer\":\"3\",\"version\":\"\",\"numeroCarguio\":\"40375\","
			+ "\"ordenes\":["
			+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"8522\","
			+ "\"timestamp\":\"2017-03-24 16:36:59\","
			+ "\"codEstado\":\"RC\",\"descEstado\":\"RECHAZADO\","
			+ "\"codMotivo\":\"P\",\"Descmotivo\":\"PRODUCTO VENCIDO\""
			+ "}]"
			+ "}]"
			+ "}";
	*/

 	/*
 	 {"solicitud":5,"carguio":[{"rutChofer":"17358358","dvChofer":"3","version":"",
 	 "numeroCarguio":"40351",
 	 "ordenes":[{"tipoDocumento":"33",
 	 "numeroDocumento":"8372|1",
 	 "timestamp":"2017-02-24 15:02:37",
 	 "codEstado":"RD",
 	 "descEstado":"CON REDESPACHO",
 	 "codMotivo":"","Descmotivo":""}]}]}
 	 
 	*/


/*

/*
String input2 ="{\"solicitud\":\"2\","
		+ "\"carguio\":["
		+ "{\"numeroCarguio\":\"40398\",\"version\":\"0\",\"rutChofer\":\"10111222\",\"dvChofer\":\"5\","
		+ "\"ordenes\":["
		+ "{\"numeroDocumento\":\"8654\",\"tipoDocumento\":\"33\","
		+ "\"codEstado\":\"ET\",\"descEstado\":\"ENTREGADO\","
		+ "\"codMotivo\":\"ET\",\"Descmotivo\":\"ENTREGADO\","
		+ "\"timestamp\":\"2017-04-05 17:48:32\",\"comentario\":\"\","
		+ "\"latitud\":\"-34.5944364\",\"longitud\":\"-58.4288867\",\"distancia\":\"22607.073002501\","
		+ "\"fotos\":[],"
		+ "\"detalle\":["
		+ "{\"codigoArticulo\":\"10461\",\"correlativo\":\"0\",\"formato\":\"U\",\"cantidad\":\"6\",\"cantidadRecepcionada\":\"0\"},"
		+ "{\"codigoArticulo\":\"1307\",\"correlativo\":\"1\",\"formato\":\"U\",\"cantidad\":\"4\",\"cantidadRecepcionada\":\"0\"}"
		+ "]}"
		+ "]}"
		+ "]"
		+ "}";
*/

/*
		String input2 ="{\"solicitud\":\"5\","
		+ "\"carguio\":["
		+ "{\"rutChofer\":\"17951951\",\"dvChofer\":\"8\",\"version\":\"\",\"numeroCarguio\":\"40448\","
		+ "\"ordenes\":["
		+ "{\"tipoDocumento\":\"33\",\"numeroDocumento\":\"8889\","
		+ "\"timestamp\":\"2017-05-04 10:34:41\","
		+ "\"codEstado\":\"RD\",\"descEstado\":\"CON REDESPACHO\","
		+ "\"codMotivo\":\"\",\"Descmotivo\":\"\""
		+ "}]"
		+ "}]"
		+ "}";
*/	
		

		
 	