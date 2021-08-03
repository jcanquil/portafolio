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

public class ProcesaErroresServidor {

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
					List lclc = clcmco.obtieneClcmco(2, vdto.getCodigoDocumento(), Integer.parseInt(vdto.getRutProveedor()), vdto.getDvProveedor(), vdto.getFechaMvto(), dto.getNumeroDocumento());
					if (lclc.size()>0){
						//System.out.println("NUMERO PROCESADO :"+dto.getNumeroDocumento());
						Iterator ite = lclc.iterator();
						while (ite.hasNext()){
							ClcmcoDTO cdto = (ClcmcoDTO) ite.next();
							if (vdto.getTotalDocumento()!=cdto.getTotalDocumento()){
								System.out.println("BODEGA :"+cdto.getCodigoBodega()+" "+"DIFERENCIA MONTOS :"+cdto.getNumeroDocumento() +" "+" URL :"+dto.getUrlXML());
							}
						}
					}
				}
				
				
			}
			
			
		}
	}
}
