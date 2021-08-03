package cl.caserita.integracion;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.IdDireccionDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.IdDireccionDTO;
import cl.caserita.dto.OrdvtaDTO;


public class IntegraLatLoniSeriesHelper {

	public static void main (String[]args){
		//DB2
		String texto = new String();
        DAOFactory daoDB2 = DAOFactory.getInstance();
        IdDireccionDAO idDireccionDAO = daoDB2.getIdDireccionDAO();
        OrdvtaDAO ordvta = daoDB2.getOrdvtaDAO();
        ClidirDAO clidir = daoDB2.getClidirDAO();
        int actualiza = 0;
        try
        {
           
            cl.caserita.integracion.dao.base.DAOFactory daoMySql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
            cl.caserita.integracion.dao.iface.IdDireccionDAO idDAO = daoMySql.getIdDireccionDAO();
           List ordenes = ordvta.obtieneOrdenesActualizaLatLon(2);
           Iterator iter = ordenes.iterator();
           while (iter.hasNext()){
        	   OrdvtaDTO dto = (OrdvtaDTO) iter.next();
        	   List direcciones = idDAO.obtieneDireccion(dto.getRutCliente());
        	   Iterator iter2 = direcciones.iterator();
        	   while (iter2.hasNext()){
        		   cl.caserita.integracion.dto.IdDireccionDTO dto2 = (cl.caserita.integracion.dto.IdDireccionDTO) iter2.next();
        		   if (dto2!=null){
        			   if (dto2.getLatitud()!=0 && dto2.getLongitud()!=0){
        				   ClidiraDTO clidira = new ClidiraDTO();
        				   clidira.setRutCliente(dto2.getRutCliente());
        				   clidira.setCorrelativo(dto2.getCorrelativoDirecciones());
        				   clidira.setObservacion("");
        				   clidira.setLatitud(String.valueOf(dto2.getLatitud()));
        				   clidira.setLongitud(String.valueOf(dto2.getLongitud()));
        				   clidir.actualizaClidira(clidira);
        			   }
        		   }
        	   }
           }
           

        }
        catch(Exception e)
        {
           
        }
		
	}
}
