package cl.caserita.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.DocRecibidosDiaDAO;
import cl.caserita.dto.DocRecibidosDTO;
import cl.caserita.dto.RutaDocumentosDTO;

public class ProcesaDocumentosRecepcionDia {
	private  static Logger log = Logger.getLogger(ProcesaDocumentosRecepcionDia.class);

	public void procesaDocumentos(String documentos, int empresa, int fecha){
		DAOFactory dao = DAOFactory.getInstance();
		DocRecibidosDiaDAO diaDAO = dao.getDocRecibidosDiaDAO();
		StringTokenizer st = new StringTokenizer(documentos,"|");
		RutaDocumentosDTO ruta = null;
		List rutaDoc = new ArrayList();
		 while (st.hasMoreTokens( )){
			 	DocRecibidosDTO dto = new DocRecibidosDTO();
			 	String tr = st.nextToken();
			 	StringTokenizer stad = new StringTokenizer(tr,",");
			 	String numeroDoc="";
			 	int num=0;
			 	dto.setCodigoEmpresa(2);
			 	dto.setFecha(fecha);
			 	while (stad.hasMoreTokens( )){
			 		//log.info("Token: " + stad.nextToken( ));
			 		
			 		
			 		//String  numeroFactura = stad.nextToken();
			 		if (num==0){
			 			String rut = stad.nextToken();
			 			String digito="";
			 			/*if (rut.length()==10){
			 				digito=rut.substring(9, 10);
			 				rut = rut.substring(0, 8);
			 			}else if (rut.length()==9){
			 				digito=rut.substring(9, 9);
			 				rut = rut.substring(1, 7);
			 			}*/
			 			dto.setRut(Integer.parseInt(rut));
			 			//dto.setDv(digito);
			 		}else if (num==1){
			 			dto.setCodDocumento(Integer.parseInt(stad.nextToken()));
			 		}else if (num==2){
			 			numeroDoc = stad.nextToken();
			 			numeroDoc = numeroDoc.replaceAll("\n", "");
			 			//log.info("Numero" + numeroDoc);
			 			//recep.setNumeroDocumento(Integer.parseInt(numeroDoc));
			 			dto.setNumeroDocumento(Integer.parseInt(numeroDoc));
			 		}/*else if (num==3){
			 			stad.nextToken();
			 		}else if (num==4){
			 			dto.setMonto(Double.parseDouble(stad.nextToken()));
			 		}*/
			 		num++;
			 	}
			 		
			 	
			 		diaDAO.procesa(dto);
		}
	}
	
	public static void main (String[]args){
		String string ="2014-12-20";
		String ano = string.substring(0, 4);
		String mes = string.substring(5, 7);
		String dia = string.substring(8, 10);
		log.info("Fecha:"+ano+mes+dia);
		
	}
}
