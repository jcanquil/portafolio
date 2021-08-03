package cl.caserita.error.servidor;

import java.util.Iterator;
import java.util.List;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClcmcoDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VentaprobDAO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VentaprobDTO;

public class BuscaBodegaDocumento {

	public static void main (String []args) {
		
		DAOFactory dao = DAOFactory.getInstance();
		VentaprobDAO venta = dao.getVentaprobDAO();
		OrdvtaDAO orden = dao.getOrdvtaDAO();
		DetordDAO detord = dao.getDetordDAO();
		VecmarDAO vecmar = dao.getVecmarDAO();
		ClcmcoDAO clcmco = dao.getClcmcoDAO();
		
		List lista = venta.ventas();
		Iterator iter = lista.iterator();
		
		while (iter.hasNext()){
			VentaprobDTO dto = (VentaprobDTO) iter.next();
			if (dto.getCodigoDocumento()!=61){
				VecmarDTO vdto = vecmar.obtenerDatosVecmarMer(2, 21, 20170825, dto.getNumeroInterno());
				if (vdto!=null){
					try
					{
					String[] command = {"find"+" /home2/ServiciosCaserita/logs/2017/" + " -name "+"'*"+dto.getNumeroInterno()+"*'"};
					final Process process = Runtime.getRuntime().exec(command);
					int numero=100;
					System.out.println("Proceso");
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
				
				
		}
			
			
	}
	

}
