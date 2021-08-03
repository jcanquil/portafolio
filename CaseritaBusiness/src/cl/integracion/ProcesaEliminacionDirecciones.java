package cl.caserita.integracion;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.iface.IdDireccionDAO;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dto.ExmvndDTO;
import cl.caserita.dto.IdDireccionDTO;

public class ProcesaEliminacionDirecciones {

	public void procesaDIrecciones(){
		DAOFactory dao = DAOFactory.getInstance();
		IdDireccionDAO idDirDAO = dao.getIdDireccionDAO();
		cl.caserita.integracion.dao.base.DAOFactory daoMYSQL = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
		cl.caserita.integracion.dao.iface.IdDireccionDAO id = 		daoMYSQL.getIdDireccionDAO();

		cl.caserita.integracion.dto.IdDireccionDTO dto2 = new cl.caserita.integracion.dto.IdDireccionDTO();
		List vendedores = idDirDAO.buscaVendedores(3000);
		Iterator iter = vendedores.iterator();
		while (iter.hasNext()){
			ExmvndDTO dto = (ExmvndDTO) iter.next();
			List direcciones = id.obtieneDatosVendedor(dto.getCodigoVendedor());
			Iterator iter2 = direcciones.iterator();
			while (iter2.hasNext()){
				dto2 = (cl.caserita.integracion.dto.IdDireccionDTO) iter2.next();
				System.out.println("CodVendedor:"+dto.getCodigoVendedor());
				System.out.println("idCorrelativo:"+dto2.getIdDireccion());
				System.out.println("Correlativo:"+dto2.getCorrelativoDirecciones());
				id.borrarDireccion(dto2.getIdDireccion(), dto2.getCorrelativoDirecciones());
				
			}
		}
	}
	public static void main (String []args){
		ProcesaEliminacionDirecciones procesa = new ProcesaEliminacionDirecciones();
		procesa.procesaDIrecciones();
	}
}
