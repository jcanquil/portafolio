package cl.caserita.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.VecmarDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.RutservDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.wms.out.helper.IntegracionConfirCamionHelper;

public class ProcesaOrdenNoFacturadaHelper {

	public static void main(String []args){
		//IntegracionConfirCamionHelper helper = new IntegracionConfirCamionHelper();
		
	//helper.procesaConfirmaCamion2("/home2/ftp/in/SHIP_LOAD_50153_1919.xml", "CAMION", "SHIP_LOAD_50153_1919.xml");
		DAOFactory dao = DAOFactory.getInstance();
		VecmarDAO vecmar = dao.getVecmarDAO();
		CamtraDAO camtra = dao.getCamtraDAO();
		//camtra.actualizaEstadoPago(2, 21, 17120204, 20161011);
		//vecmar.actualizaSwitchVecmar(2, 21, 20161011, 17120204,0);
		RutservDAO rutservDAO = dao.getRutServDAO();
		RutservDTO rutservDTO = rutservDAO.recuperaEndPointServlet("FACTUR");

		ProcedimientoDAO proce = dao.getProcedimientoDAO();
		VedmarDAO vedmar = dao.getVedmarDAO();
		//VecmarDAO vecmar = dao.getVecmarDAO();
		//VedmarDTO vedmarDTO = vedmar.obtenerDatosVedmarNoHayCorrelativo(2, 21, 20160922, 17049205, 7);
		VecmarDTO vecmarDTO = vecmar.obtenerDatosVecmarMer(2, 21, 20170715, 19827985);
		ClmcliDAO clmcli = dao.getClmcliDAO();
		ClmcliDTO dto = clmcli.recuperaCliente(vecmarDTO.getRutProveedor(), vecmarDTO.getDvProveedor());
		vecmarDTO.setRazonSocialCliente(dto.getRazonsocial().trim());
		IntegracionConfirCamionHelper hel = new IntegracionConfirCamionHelper();
		//vedmarDTO.setCantidadArticulo(240);
		//vedmarDTO.setCantidadFormato(240);
		
		String nom = hel.formaString( vecmarDTO);
		proce.procesaCalculoProcedure(nom);
		/*VedfaltDAO vedfalt = dao.getVedfaltDAO();
		VecmarDTO dto = new VecmarDTO();
		dto.setCodigoEmpresa(2);
		dto.setCodTipoMvto(21);
		dto.setFechaMvto(20160922);
		dto.setNumDocumento(17062091);
		dto.setFechaDocumento(20160923);
		vedfalt.actualizaDatosVedfalt(dto);
*/
		
		StringBuffer tmp = new StringBuffer(); 
        String texto = new String();
       
        int numeroProceso=0;
		try { 
            // Crea la URL con del sitio introducido, ej: http://google.com 
            URL url = new URL(rutservDTO.getEndPoint()+"?empresa="+String.valueOf(vecmarDTO.getCodigoEmpresa())+
            		"&codTipo="+String.valueOf(vecmarDTO.getCodTipoMvto())+
            				"&fch="+String.valueOf(vecmarDTO.getFechaDocumento())+"&num="+
            		String.valueOf(vecmarDTO.getNumDocumento())+
            				"&cod="+String.valueOf(vecmarDTO.getCodigoDocumento())+"&rut="+
            String.valueOf(vecmarDTO.getRutProveedor())+"&dv="+vecmarDTO.getDvProveedor()+"&usuario=CAJABD26&tipo=1&nota=0"); 
            
            // Lector para la respuesta del servidor 
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
            String str; 
            
            numeroProceso=numeroProceso+1;
            while ((str = in.readLine()) != null) { 
                tmp.append(str); 
            } 
            //url.openStream().close();
            in.close(); 
            texto = tmp.toString(); 
           
			
			
        }catch (MalformedURLException e) { 
            texto = "<h2>No esta correcta la URL</h2>".toString(); 
        } catch (IOException e) { 
            texto = "<h2>Error: No se encontro el l pagina solicitada".toString(); 
            } 
       

		vecmar.actualizaSwitchVecmar(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento(),0);
		vedmar.actualizaSwitchVecmar(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(),vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento(),0);
		camtra.actualizaEstadoPago(vecmarDTO.getCodigoEmpresa(), vecmarDTO.getCodTipoMvto(), vecmarDTO.getFechaMvto(), vecmarDTO.getNumDocumento());
		
		
	}
}
