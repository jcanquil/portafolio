package cl.caserita.ecommerce.helper;

import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.ObordenDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dao.impl.VedfaltDAOImpl;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.ClienteeCommerceDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.ObordenDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.enviomail.main.EnvioMailErrorCanastaWEB;
import cl.caserita.enviomail.main.emailCanastaWEB;
import cl.caserita.enviomail.main.emailInformacionErroresWMS;

public class ProcesaVentaHelper {
	private  static Logger log = Logger.getLogger(ProcesaVentaHelper.class);
	private static EnvioMailErrorCanastaWEB email = new EnvioMailErrorCanastaWEB();

	public String generaCliente(){
		String respuesta ="";
		
		
		return respuesta;
	}
	
	public String generaDireccion(){
		String respuesta="";
		
		return respuesta;
	}
	public String generaVenta(String par000){
		String respuesta="";
		RespuestaDTO respuestaDTO = new RespuestaDTO();
		ConvierteJSONDTO convierte = new ConvierteJSONDTO();
		ClienteeCommerceDTO cliente = convierte.convirteCliente(par000);
		DAOFactory dao = DAOFactory.getInstance();
		ClmcliDAO clmcliDAO = dao.getClmcliDAO();
		ClidirDAO clidirDAO = dao.getClidirDAO();
		OrdvtaDAO ordvtaDAO = dao.getOrdvtaDAO();
		DetordDAO detordDAO = dao.getDetordDAO();
		ObordenDAO oborden = dao.getObordenDAO();
		int numov=0;
		int correlativoFacturacion=0;
		int correlativoDespacho=0;
		try{
			if (cliente!=null){
				//Genera Cliente en CLMCLI
				ClmcliDTO clmcli = new ClmcliDTO();
				clmcli.setRutCliente(Integer.parseInt(cliente.getRutCliente()));
				clmcli.setDvCliente(cliente.getDvCliente());
				clmcli.setRazonsocial(cliente.getRazonSocial());
				clmcli.setCodigoBodega(21);
				clmcli.setCodigoTipoCliente(1);
				clmcli.setCodigoClaseCliente(0);
				clmcli.setCodigocalidadCliente(0);
				clmcli.setCodigoBloqueo(0);
				clmcli.setAfectoPromo("S");
				clmcli.setBodegaUltimaCompra(21);
				clmcli.setCodigoVendedor(771);
				clmcli.setCodigoTipoVendedor(1000);
				clmcli.setFechaAccesoUsuario(20151020);
				clmcli.setHoraAccesoUsuario(120000);
				ClmcliDTO dto = clmcliDAO.recuperaCliente(String.valueOf(clmcli.getRutCliente()), clmcli.getDvCliente().trim());
				if (dto!=null){
					log.info("Cliente Existe");
				}else{
					log.info("Crea Cliente");
					clmcliDAO.generaCliente(clmcli);
					clmcli.setTipoNegocio(cliente.getCodigoGiro());
					clmcliDAO.generaDatosCliente(clmcli);
				}
				
				//Genera direcciones en CLIDIR y CLIDIRA
				int region=0;
				int ciudad=0;
				int comuna=0;
				String obs="";
				Iterator iter = cliente.getDirecciones().iterator();
				//genera movimiento en tabla ORDVTA
				 numov = ordvtaDAO.obtieneCorrelativoWEB(21);
				while (iter.hasNext()){
					ClidirDTO clidir = (ClidirDTO) iter.next();
					region = clidir.getRegion();
					ciudad = clidir.getCiudad();
					comuna = clidir.getComuna();
					String numero="     ";
					String depart="     ";
					if (clidir.getNumeroDireccion()!=0){
						numero=String.valueOf(clidir.getNumeroDireccion())+"     ";
						if (numero!=null && numero.trim().length()>0){
							numero=numero.substring(0, 5);
						}
					}
					if (clidir.getDepartamentoString()!=null){
						if (clidir.getDepartamentoString().length()>0){
							depart= clidir.getDepartamentoString()+"     ";
							if (depart!=null && depart.trim().length()>0){
								depart=depart.substring(0, 5);
							}
						}
					}
					
					clidir.setDireccionCliente(clidir.getDireccionCliente()+"                                                        ");
					clidir.setDireccionCliente(clidir.getDireccionCliente().substring(0, 30)+numero+depart);
					log.info("Direccion:"+clidir.getDireccionCliente());
					log.info("Numero:"+numero);
					log.info("depart:"+depart);
					clidir.setVillaPoblacion(clidir.getVillaPoblacion().trim()+"                           ");
					ClidiraDTO clidira = new ClidiraDTO();
					clidir.setRutCliente(Integer.parseInt(cliente.getRutCliente()));
					clidir.setDvCliente(cliente.getDvCliente());
					clidira.setRutCliente(Integer.parseInt(cliente.getRutCliente()));
					clidira.setDvCliente(cliente.getDvCliente());
					clidira.setObservacion(clidir.getObservacion().trim());
					obs = clidira.getObservacion();
					Gson gson = new Gson();
					log.info("Tipo Direccion:"+clidir.getTipoDireccion());
					//Direccion Facturacion
					if (clidir.getTipoDireccion()==1){
						clidir.setCorrelativo(clidirDAO.obtieneCorrelativoFacturacion(clidir.getRutCliente(), clidir.getDvCliente().trim())+1);
						correlativoFacturacion = clidir.getCorrelativo();
						clidira.setCorrelativo(correlativoFacturacion);
						clidirDAO.generaDireccion(clidir);
						log.info("CLidir:"+gson.toJson(clidir));
						clidira.setLatitud("");
						clidira.setLongitud("");
						clidirDAO.generaClidira(clidira);
					}//Direccion Despacho
					else if (clidir.getTipoDireccion()==2){
						clidir.setCorrelativo(clidirDAO.obtieneCorrelativo(clidir.getRutCliente(), clidir.getDvCliente().trim())+1);
						correlativoDespacho = clidir.getCorrelativo();
						clidira.setCorrelativo(correlativoDespacho);
						clidirDAO.generaDireccion(clidir);
						log.info("CLidir:"+gson.toJson(clidir));
						clidira.setLatitud("");
						clidira.setLongitud("");
						
						clidirDAO.generaClidira(clidira);
					}
				}
				
				log.info("CORRELATIVO OV:"+numov);
				OrdvtaDTO ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setCodigoEmpresa(2);
				ordvtaDTO.setCodigoBodega(21);
				ordvtaDTO.setNumeroOV(numov);
				ordvtaDTO.setRutCliente(Integer.parseInt(cliente.getRutCliente()));
				ordvtaDTO.setDvCliente(cliente.getDvCliente());
				ordvtaDTO.setFormaPago(cliente.getFormaPago());
				int vendedor =0;
				if (cliente.getProveedor()!=null){
					vendedor=1342;
				}else{
					vendedor=1341;
				}
				ordvtaDTO.setCodigoVendedor(vendedor);
				ordvtaDTO.setFechaOrden(Integer.parseInt(cliente.getFechaDespacho()));
				ordvtaDTO.setEstadoOV("L");
				ordvtaDTO.setRetiroPago(1);
				ordvtaDTO.setCorreDireccionOV(correlativoFacturacion);
				ordvtaDTO.setTotalCosto(0);
				ordvtaDTO.setTotalCostoNeto(0);
				ordvtaDTO.setMontoNeto(100000);
				ordvtaDTO.setTotalDocumento(2000000);
				ordvtaDTO.setTotalIva(90000);
				if (cliente.getProveedor()!=null){
					ordvtaDTO.setRutProveedor(cliente.getProveedor().trim());

				}else{
					ordvtaDTO.setRutProveedor("0");

				}
				ordvtaDTO.setTipoDocumento(Integer.parseInt(cliente.getTipoDocumento()));
				ordvtaDTO.setIdBanco(cliente.getIdBanco());
				ordvtaDAO.insertaOV(ordvtaDTO);
				ordvtaDAO.insertaOVProveedor(ordvtaDTO);
				ordvtaDAO.insertaAdicionalesOV(ordvtaDTO);
				//Genera movimiento detalle en DETORD
				Iterator cld = cliente.getArticulos().iterator();
				int correlativoDetalleOV=0;
				while (cld.hasNext()){
					CldmcoDTO cldmcoDTO = (CldmcoDTO) cld.next();
					
					DetordDTO detord = new DetordDTO();
					//"+detord.getPrecioBruto()+","+detord.getPrecioNeto()+","+detord.getCostoBruto()+","+detord.getCostoNeto()+","+detord.getMontoBruto()+","+detord.getMontoNeto()+","+detord.getTotalNeto()+","+detord.getMontoTotal()+","+detord.getFechaDespacho()+","+detord.getCorrelativoDespacho()+",'"+detord.getEstado().trim()+"',"+detord.getRegion()+","+detord.getCiudad()+","+detord.getComuna()+") 
					
					detord.setCodEmpresa(2);
					detord.setNumOvVenta(numov);
					detord.setRutCliente(ordvtaDTO.getRutCliente());
					detord.setDvCliente(ordvtaDTO.getDvCliente().trim());
					detord.setCodigoBodega(21);
					detord.setCorrelativoDetalleOV(correlativoDetalleOV);
					detord.setCodigoArticulo(cldmcoDTO.getCodigoArticulo());
					detord.setCantidadArticulo(cldmcoDTO.getCantidadArticulo());
					detord.setCantidadFormato(cldmcoDTO.getCantidadArticulo());
					detord.setFormato("X");
					detord.setPrecioBruto(cldmcoDTO.getPrecio());
					log.info("Precio Bruto:"+detord.getPrecioBruto());
					if (cldmcoDTO.getPrecioNeto()!=0){
						//DecimalFormat formateadorVecmar = new DecimalFormat("###,###.00");
						
						detord.setPrecioNeto(cldmcoDTO.getPrecioNeto());
						log.info("Precio NEto2:"+detord.getPrecioNeto());
						detord.setCostoBruto(10000);
						detord.setCostoNeto(7000);
						detord.setCostoTotal(14000);
						detord.setMontoBruto(19900);
						detord.setMontoNeto(15000);
						detord.setTotalNeto(15000);
						detord.setMontoTotal(15000);
						detord.setFechaDespacho(Integer.parseInt(cliente.getFechaDespacho()));
						detord.setCorrelativoDespacho(correlativoDespacho);
						detord.setEstado("L");
						detord.setRegion(region);
						detord.setCiudad(ciudad);
						detord.setComuna(comuna);
						detordDAO.insertaDetalleOV(detord);
						ObordenDTO obordenDTO = new ObordenDTO();
						obordenDTO.setCodigoEmpresa(detord.getCodEmpresa());
						obordenDTO.setNumeroOrdenVenta(numov);
						obordenDTO.setRutCliente(detord.getRutCliente());
						obordenDTO.setDvCliente(detord.getDvCliente());
						obordenDTO.setCodigoBodega(detord.getCodigoBodega());
						obordenDTO.setCorrelativoDetalleOV(correlativoDespacho);
						obordenDTO.setObservacion(obs);
						oborden.generaObservacionOrden(obordenDTO);
						correlativoDetalleOV=correlativoDetalleOV+1;
					}else{
						detord.setPrecioNeto(15000);
						//break;
					}
					
				}
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
			email.mail("Error en generacion venta Canastas WEB :"+cliente.getRutCliente()+cliente.getProveedor()+cliente.getFechaDespacho());
		}
		
		
		respuestaDTO.setCodigoEstado("0");
		respuestaDTO.setDescripcionEstado("Venta Generada Existosamente");
		respuestaDTO.setNumeroOrden(String.valueOf(numov));
		Gson gson = new Gson();
		respuesta = gson.toJson(respuestaDTO);
		ordvtaDAO.procesaCalculoProcedure(2, 21, numov, Integer.parseInt(cliente.getRutCliente()), cliente.getDvCliente());
		emailCanastaWEB envia = new emailCanastaWEB();
		envia.mail(String.valueOf(numov), cliente.getRutCliente()+"-"+cliente.getDvCliente(), cliente.getRazonSocial(), String.valueOf(cliente.getFechaDespacho()));
		return respuesta;
	}
	
	public String procesoGeneral(){
		String respuesta="";
		
		return respuesta;
	}
	
	public static void main (String []args){
		String dep ="         ";
		log.info("Depto:"+dep.substring(0, 5));
		log.info("FInal");
	}
}
