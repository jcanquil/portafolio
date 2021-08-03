package cl.caserita.monitoreo.mapa.helper;

import java.util.Iterator;
import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Equals;

import cl.caserita.comunes.fecha.Fecha;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.BBintc00DAO;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.ExmvndDAO;
import cl.caserita.dao.iface.GeovtaveDAO;
import cl.caserita.dao.iface.TpttvdDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VecmonDAO;
import cl.caserita.dao.iface.VencobDAO;
import cl.caserita.dto.BBintc00DTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExmvndDTO;
import cl.caserita.dto.GeovtaveDTO;
import cl.caserita.dto.TpttvdDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VecmonDTO;
import cl.caserita.dto.VencobDTO;
import cl.caserita.integracion.dao.iface.VentaBinarioDAO;
import cl.caserita.integracion.dto.VentaBinarioDTO;

public class ProcesaMonitoreoVentasHelper {

	public static void main (String []args){
		DAOFactory dao = DAOFactory.getInstance();
		VecmonDAO vecmonDAO = dao.getVecmonDAO();
		Fecha fch = new Fecha();
		GeovtaveDAO geovtaveDAO = dao.getGeovtaveDAO();
		VecmarDAO vecmar = dao.getVecmarDAO();
		BBintc00DAO bbintc = dao.getBBintc00DAO();
		VencobDAO vencob = dao.getVencobDAO();
		ExmvndDAO exmvnd = dao.getExmvndDAO();
		ClmcliDAO clmcli = dao.getClmcliDAO();
		ClidirDAO clidir = dao.getClidirDAO();
		DetordDAO detord = dao.getDetordDAO();
		TpttvdDAO tpttvdDAO = dao.getTpttvdDAO();
		int numeroNO=0;
		int numeroN1=0;
		int numeroN2=0;
		int numeroN3=0;
		
		cl.caserita.integracion.dao.base.DAOFactory daoBinario = cl.caserita.integracion.dao.base.DAOFactory.getInstanceBinario();
		VentaBinarioDAO venta = daoBinario.getVentaBinarioDAO();
		List list = vecmonDAO.obtenerDatosVecmon(2, 21);
		try{
			Iterator iter = list.iterator();
			while (iter.hasNext()){
				VecmonDTO vecmonDTO = (VecmonDTO) iter.next();
				
				//Leer datos desde MYSQL
				VentaBinarioDTO ventaBinario = venta.ventasBinario(vecmonDTO.getFechaMovimiento(), vecmonDTO.getNumeroDocumento());
				/*if (ventaBinario!=null){*/
					/*if (ventaBinario.getGeoLocLat().compareTo("0.0")!=0){*/
						VecmarDTO vecmarDTO = vecmar.obtenerDatosVecmarMer(2, vecmonDTO.getCodigoTipoMovimiento(), vecmonDTO.getFechaMovimiento(), vecmonDTO.getNumeroDocumento());
						TpttvdDTO tpttvd = tpttvdDAO.recuperaTipoVendedor(vecmarDTO.getCodigoTipoVendedor());
						if (vecmarDTO!=null){
							if (vecmarDTO.getCodigoDocumento()==33){
								vecmarDTO.setCodigoDocumento(3);
							}else if (vecmarDTO.getCodigoDocumento()==34){
								vecmarDTO.setCodigoDocumento(4);
							}
							BBintc00DTO bbintDTO = bbintc.obtieneVentaBB(vecmarDTO.getCodigoDocumento(), vecmonDTO.getFechaMovimiento(), vecmonDTO.getNumeroDocumento());
							//ClidirDTO clidirDTO = clidir.obtieneDireccion(bbintDTO.getRutCliente(), bbintDTO.getIndicadorDespacho());
							if (bbintDTO!=null && bbintDTO.getCodigoBodega()==26){
								ExmvndDTO exmvndDTO = exmvnd.recuperaVendedor(vecmarDTO.getCodigoVendedor());
								VencobDTO ven = vencob.obtenerVendedorSupervisor(exmvndDTO.getCodigoVendedor());
								if (ven==null){
									ven = new VencobDTO();
									ven.setCodigoVendedor(1);
									ven.setNombreVendedor("NINGUNO");
									ven.setCodigoSupervisor(1);
									ven.setNombreSupervisor("NINGUNO");
								}
								ClmcliDTO clmcliDTO = clmcli.recuperaCliente(bbintDTO.getRutCliente(), bbintDTO.getDvCliente());
								ClidirDTO clidirDTO = clidir.recuperaDireccionesCorrrelativo(bbintDTO.getRutCliente(), bbintDTO.getDvCliente(), bbintDTO.getIndicadorDespacho());
								if (clidirDTO!=null){
									if (ventaBinario==null){
										ventaBinario = new VentaBinarioDTO();
										ventaBinario.setGeoLocLat("0.0");
									}
									if (ventaBinario.getGeoLocLat().compareTo("0.0")==0){
										if (clidirDTO!=null){
											if (clidirDTO.getLatitud().trim().compareTo("")!=0 && clidirDTO.getLongitud()!=null){
												
												ventaBinario.setGeoLocLat(clidirDTO.getLatitud());
												ventaBinario.setGeoLocLng(clidirDTO.getLongitud());
											}else{
												ventaBinario.setGeoLocLat("0.0");
												ventaBinario.setGeoLocLng("0.0");
											}
										}
									}
										
									System.out.println(bbintDTO.getNumeroDocumento());
									int orden = detord.recuperaOrdenVenta(2, bbintDTO.getCodigoBodega(), bbintDTO.getNumeroDocumento(), bbintDTO.getRutCliente());
									//Procesar DATOS hacia tabla GEOVTAVE para lectura de Gradiente
									System.out.println("Lat : "+ventaBinario.getGeoLocLat());
									System.out.println("Lon : "+ventaBinario.getGeoLocLng());

									GeovtaveDTO dto = new GeovtaveDTO();
									dto.setCodigoEmpresa(vecmonDTO.getCodigoEmpresa());
									dto.setCodigoBodega(26);
									dto.setTipoMovimiento("1");
									dto.setFechaRegistro(bbintDTO.getFechaDocumento());
									dto.setHoraRegistro(bbintDTO.getHoraGeneracion());
									dto.setCodigoSupervisor(ven.getCodigoSupervisor());
									dto.setNombreSupervisor(ven.getNombreSupervisor().trim());
									dto.setCodigoVendedor(exmvndDTO.getCodigoVendedor());
									dto.setNombreVendedor(exmvndDTO.getNombreVendedor().trim());
									dto.setCodigoDocumento(vecmarDTO.getCodigoDocumento());
									dto.setNumeroDocumento(vecmarDTO.getNumDocumento());
									dto.setRutCliente(bbintDTO.getRutCliente());
									dto.setDigitoCliente(bbintDTO.getDvCliente());
									dto.setNombreCliente(clmcliDTO.getRazonsocial().trim());
									dto.setNombreVendedor(exmvndDTO.getNombreVendedor().trim());
									dto.setLatitud(ventaBinario.getGeoLocLat());
									dto.setLongitud(ventaBinario.getGeoLocLng());
									dto.setCorrelativoDireccion(bbintDTO.getIndicadorDespacho());
									dto.setDireccionCliente(clidirDTO.getDireccionCliente().trim());
									dto.setNumeroDireccion(clidirDTO.getNumeroDireccion());
									dto.setTelefono(clidirDTO.getTelefono().trim());
									dto.setCelular(clidirDTO.getCelular());
									dto.setCodigoRegion(clidirDTO.getRegion());
									dto.setDescripcionRegion(clidirDTO.getDescripcionRegion().trim());
									dto.setCodigoCiudad(clidirDTO.getCiudad());
									dto.setDescripcionCiudad(clidirDTO.getDescripcionCiudad().trim());
									dto.setCodigoComuna(clidirDTO.getComuna());
									dto.setDescripcionComuna(clidirDTO.getDescripcionComuna());
									dto.setCodigoTipoVendedor(vecmarDTO.getCodigoTipoVendedor());
									dto.setDescripcionTipoVendedor(tpttvd.getDescripcionTipoVendedor().trim());
									dto.setCodigoAntena("NADA");
									dto.setDescripcionAntena("NADA");
									dto.setCantidadArticulos(vecmarDTO.getCantidadLineaDetalle());
									dto.setTotalNeto(vecmarDTO.getTotalNeto());
									dto.setTotalDocumento(vecmarDTO.getTotalDocumento());
									dto.setNumeroOrdenVenta(orden);
									dto.setCodigoTipoNegocio(15);
									dto.setDescripcionTipoNegocio("OTROS");
									dto.setCodigoRazonNoVenta("NINGUNA");
									dto.setDescripcionRazonNoVenta("NINGUNA");
									geovtaveDAO.generaGeovtave(dto);
								}
								
							}else{
								numeroN3++;
							}
							
						}else{
							numeroN2++;
						}
						
					/*//}/*else{
						System.out.println("Numero Interno : "+vecmonDTO.getNumeroDocumento());
						numeroN1++;

					}*/
					
				
				
				
				
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		/*System.out.println("Numero de no procesados : "+numeroNO);
		System.out.println("Numero de no procesados SIN GEO N1: "+numeroN1);
		System.out.println("Numero de no procesados SIN VECMAR N2: "+numeroN2);
		System.out.println("Numero de no procesados DIFERENTE BODEGA 26 N3: "+numeroN3);
*/
	}
	
	
}
