package cl.caserita.canastas.helper;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.AdsusrDAO;
import cl.caserita.dao.iface.PaisDAO;
import cl.caserita.dto.PaisDTO;
import cl.caserita.pais.xsd.Pais;
import cl.caserita.pais.xsd.Pais.Datos;
import cl.caserita.password.ObtieneClave;

import com.google.gson.Gson;

public class RegionalesHelper {
	private  static Logger log = Logger.getLogger(RegionalesHelper.class);

	public String regionales(String par000){
		String respuesta="Obtener datos Regionales";
		log.info("Regionales:"+par000);
		Pais pais = null;
		Datos datos = null;
		Gson gson = new Gson();
		try{
			
			final StringReader xmlReader = new StringReader(par000);			
			final JAXBContext jaxbContext = JAXBContext.newInstance(Pais.class);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			//pais = (Pais) unmarshaller.unmarshal(xmlReader);
			pais = gson.fromJson(par000, Pais.class);
			log.info("Usuario:"+pais.getUsuario());
			log.info("password:"+pais.getPassword());
			
			DAOFactory dao = DAOFactory.getInstance();
			PaisDAO paisDAO = dao.getPaisDAO();
			Pais pais2 = new Pais();
			AdsusrDAO adsusr = dao.getAdsusrDAO();
			ObtieneClave clave = new ObtieneClave();
			int valida = 1;
			
			if (pais.getSolicitud()==1 && valida==1){
				List listaRegio = paisDAO.region();
				Iterator iter = listaRegio.iterator();
				while (iter.hasNext()){
					PaisDTO paisDTO = (PaisDTO) iter.next();
					datos = new Datos();
					 datos.setCodigo(paisDTO.getCodigo());
					 datos.setDescripcion(paisDTO.getDescripcion().trim());
					 pais2.getDatos().add(datos);
				}
				final JAXBContext jc = JAXBContext.newInstance(Pais.class);
				Marshaller m = jc.createMarshaller();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				m.marshal(pais2, baos);
				
				respuesta = new String( baos.toByteArray());
				respuesta=gson.toJson(pais2);
			}else if (pais.getSolicitud()==2 && valida==1){
				List listaRegio = paisDAO.ciudad(pais.getCodigoRegionF());
				Iterator iter = listaRegio.iterator();
				while (iter.hasNext()){
					PaisDTO paisDTO = (PaisDTO) iter.next();
					datos = new Datos();
					 datos.setCodigo(paisDTO.getCodigo());
					 datos.setDescripcion(paisDTO.getDescripcion().trim());
					 pais2.getDatos().add(datos);
				}
				final JAXBContext jc = JAXBContext.newInstance(Pais.class);
				Marshaller m = jc.createMarshaller();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				m.marshal(pais2, baos);
				
				respuesta = new String( baos.toByteArray());
				respuesta=gson.toJson(pais2);
			}else if (pais.getSolicitud()==3 && valida==1){
				List listaRegio = paisDAO.comuna(pais.getCodigoRegionF(), pais.getCodigoCiudadF());
				Iterator iter = listaRegio.iterator();
				while (iter.hasNext()){
					PaisDTO paisDTO = (PaisDTO) iter.next();
					datos = new Datos();
					 datos.setCodigo(paisDTO.getCodigo());
					 datos.setDescripcion(paisDTO.getDescripcion().trim());
					 pais2.getDatos().add(datos);
				}
				final JAXBContext jc = JAXBContext.newInstance(Pais.class);
				Marshaller m = jc.createMarshaller();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				m.marshal(pais2, baos);
				
				log.info("respuesta:"+gson.toJson(pais2));
				
				respuesta = new String( baos.toByteArray());
				respuesta=gson.toJson(pais2);
			}else if ( valida==1){
				
				datos = new Datos();
				 datos.setCodigo(1);
				 datos.setDescripcion("usuario o contrasena invalido");
				 pais2.getDatos().add(datos);
				 respuesta=gson.toJson(pais2);
			}
			
			
			
		}catch(Exception e){
			//usuario invalido
			
			e.printStackTrace();
		}

		return respuesta;
	}
}
