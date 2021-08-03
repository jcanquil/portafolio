package cl.caserita.wms.comun;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cl.caserita.dto.IntegracionDTO;
import cl.caserita.dto.RecepDocumentoDTO;
import cl.caserita.wms.helper.IntegraCarguioHelper;

public class ConvierteStringDTO {
	private  static Logger logi = Logger.getLogger(ConvierteStringDTO.class);

	public IntegracionDTO convierte(String dato){
		IntegracionDTO dto = new IntegracionDTO();
		StringTokenizer st = new StringTokenizer(dato,",");
		int campo=0;
		 while (st.hasMoreTokens( )){
			 
			 	String tr = st.nextToken();
			 	logi.info(tr);
			 	if (campo==0){
			 		dto.setAccion(tr.trim());
			 	}else if (campo==1){
			 		dto.setIpEquipo(tr.trim());
			 	}else if (campo==2){
			 		dto.setNombreEquipo(tr.trim());
			 	}else if (campo==3){
			 		dto.setUsuario(tr.trim());
			 	}
			 campo++;
	        
	    }
		 
		return dto;
	}
	
	public static void main (String []args){
		ConvierteStringDTO con = new ConvierteStringDTO();
		con.convierte("A");
		
		
	}
}
