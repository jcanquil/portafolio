package cl.caserita.integracion;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;

public class MigraLatitudLongitudClidira {

	public void integraLatitudLongitud(){
		DAOFactory daoDB2 = DAOFactory.getInstance();
		ClidirDAO clidirDAO = daoDB2.getClidirDAO();
		ClidiraDTO clidiraDTO = null;
		
		try{
			cl.caserita.integracion.dao.base.DAOFactory daoMysql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
			cl.caserita.integracion.dao.iface.IdDireccionDAO iddir = daoMysql.getIdDireccionDAO();
			List direccionMYSQL = iddir.obtieneDireccionNormalizadasPorVendedor();
			Iterator iter = direccionMYSQL.iterator();
			while (iter.hasNext()){
				cl.caserita.integracion.dto.IdDireccionDTO dto = (cl.caserita.integracion.dto.IdDireccionDTO) iter.next();
				if (dto.getLatitud()!=0 && dto.getLongitud()!=0){
					
						clidiraDTO = new ClidiraDTO();
						ClidirDTO clidirDTO = clidirDAO.obtieneDireccion(dto.getRutCliente(), dto.getCorrelativoDirecciones());
						//ClidirDTO clidirDTO = clidirDAO.obtieneDireccion(dto.getRutCliente(), dto.getCorrelativoDirecciones());
						if (clidirDTO!=null){
							clidirDTO.setRutCliente(dto.getRutCliente());
							clidirDTO.setCorrelativo(dto.getCorrelativoDirecciones());
							String direccion = dto.getDireccion()+"                                            ";
							String numero = String.valueOf(dto.getNumeroDireccion() +"                                   ");
							String depto = String.valueOf(clidirDTO.getCelular()+"          ");
							clidirDTO.setDireccionCliente(direccion.substring(0, 30)+numero.substring(0, 5)+depto.substring(0, 5));
							clidiraDTO.setRutCliente(dto.getRutCliente());
							clidiraDTO.setCorrelativo(dto.getCorrelativoDirecciones());
							clidiraDTO.setLatitud(String.valueOf(dto.getLatitud()));
							clidiraDTO.setLongitud(String.valueOf(dto.getLongitud()));
							clidiraDTO.setObservacion("");
							clidirDAO.actualizaClidiraLatLng(clidiraDTO);
							if (dto.getRutCliente()==9009975 || dto.getRutCliente()==11628337 || dto.getRutCliente()==22530007){
								System.out.println("Direccion1:"+clidirDTO.getDireccionCliente());
							}
							if (dto.getRutCliente()==16837024 || dto.getRutCliente()==14526838 || dto.getRutCliente()==9812685){
								System.out.println("Direccion2:"+clidirDTO.getDireccionCliente());
							}
							if (dto.getNombreContacto()!=null){
								if (!dto.getNombreContacto().trim().equals(clidirDTO.getNombreContacto().trim())){
									System.out.println("Nombre Contacto1:"+dto.getNombreContacto());
									System.out.println("Nombre Contacto2:"+clidirDTO.getNombreContacto());

									clidirDTO.setNombreContacto(dto.getNombreContacto());
								}	
							}
							
							
							//clidirDAO.actualizaDireccionClidir(clidirDTO);
							dto.setRevision(9);
							iddir.actualizaEstado(dto);
						}
						
					
					
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main (String []args){
		MigraLatitudLongitudClidira migra = new MigraLatitudLongitudClidira();
		migra.integraLatitudLongitud();
	}
}
