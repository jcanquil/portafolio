package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DocncpDAO;
import cl.caserita.dto.ChoftranDTO;
import cl.caserita.dto.DocncpDTO;

public class DocncpDAOImpl implements DocncpDAO{
	
	
	private static Logger log = Logger.getLogger(DocncpDAOImpl.class);
	private Connection conn;
	
	public DocncpDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	
	
	//Get Docncp
	public DocncpDTO buscaDocncp(int empresa, String tipoNota, int numeroNota, int codigoBodega){
		return null;
	}
	
	
	//Insert Table Docncp
	public int insertaDocncp(DocncpDTO dto){
		int correlativo =-100;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		
		String cmp ="CONEMP, CONTIP, CONNU2, CONFE2, CAMCO1, CLCRU1, CLCDVR, CAMC12,"+
				"NUMVEN, NUMCAR, CAMNFA, VEDNU2, PRMNR3, FCHUSR, HORUSR, USRSYS, AA109A";
	
		String sqlObtenerDocncp ="INSERT INTO  "+
		        " CASEDAT.DOCNCP ("+cmp+") "+
				" VALUES (2,'"+dto.getTipoNota()+"',"+dto.getNumeroNota()+","+dto.getFechaNota()+","+dto.getCodigoBodega()+","+
		        dto.getRutCliente()+",'"+dto.getDigCliente()+"',"+dto.getCorrelativo()+","+dto.getNumeroOV()+","+
				dto.getNumeroCarguio()+","+dto.getNumeroDocumento()+","+dto.getNumeroGuia()+","+dto.getNumeroNcfinal()+","+
		        dto.getFechaUsuario()+","+dto.getHoraUsuario()+",'"+dto.getCodigoUsuario()+"','"+dto.getMotivo()+"')";
				
			try{
				pstmt = conn.prepareStatement(sqlObtenerDocncp);
				pstmt.executeUpdate();
				
				//System.out.println("SQL Orden" + sqlObtenerCldmco);
				//System.out.println("Ok !! insert docncp");
				correlativo=1;
				
			}catch(Exception e){
				e.printStackTrace();
			}finally {
	
				try {
	
					 if (rs != null) { rs.close(); 
					 pstmt.close();
					 }
	
				} catch (SQLException e1) { }
	
			}
	
			return correlativo;
	}
	
	public DocncpDTO obtenerDatosChofer(int empresa, String tipo, int nroTraspaso, int rutEmpTrans, String dvEmpTrans){
		DocncpDTO docncpDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar=" SELECT * "+
				" FROM CASEDAT.DOCNCP, CASEDAT.CHOFTRAN " + 
				" WHERE conemp = "+empresa+
				" AND contip ='"+tipo+"'"+
				" AND connu2 = "+nroTraspaso+
				" AND tpcrut = "+rutEmpTrans+
				" AND tpcdvt = '"+dvEmpTrans+"'"+
				" AND clcru1 = camrut "+
				" AND clcdvr = exmdi3 "+
				" AND cames1 = 'A'"+
				" FOR READ ONLY" ;
		
		System.out.println("SQL DOCNCP CHOFER" + sqlObtenerVecmar);   
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docncpDTO = new DocncpDTO();
				docncpDTO.setRutCliente(rs.getInt("CLCRU1"));
				docncpDTO.setDigCliente(rs.getString("CLCDVR"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return docncpDTO;
	}

	public String obtenerNcpes(int codEmpresa, int codBodega, int numCarguio){
		String numerosNCP="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerNcpes =" SELECT DISTINCT(connu2) "+
				" FROM CASEDAT.DOCNCP "+
				" WHERE conemp="+codEmpresa+
				" AND contip='P'"+
				" AND camco1="+codBodega+
				" AND numcar="+numCarguio+
				" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerNcpes);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (!numerosNCP.equals("")){
            		numerosNCP = numerosNCP + ",";
            	}
            	numerosNCP = numerosNCP + rs.getInt("CONNU2");
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 

		
	
		return numerosNCP;
	}
	
	public int buscaDocncpDTO (DocncpDTO docdto){
		int existereg=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerNcpes =" SELECT * "+
				" FROM CASEDAT.DOCNCP "+
				" WHERE conemp=2"+
				" AND contip='P'"+
				" AND camco1="+docdto.getCodigoBodega()+" "+
				" AND numcar="+docdto.getNumeroCarguio()+ " "+
				" AND clcru1="+docdto.getRutCliente()+" "+
				" AND confe2="+docdto.getFechaNota()+" "+
				" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerNcpes);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				existereg=1;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 

		
		return existereg;
	}
	
	public int eliminaDocncpDTO (DocncpDTO docdto){
		int elimina=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerDocncp ="DELETE FROM "+
        " CASEDAT.DOCNCP " + 
		" WHERE conemp=2"+
		" AND contip='P'"+
		" AND camco1="+docdto.getCodigoBodega()+" "+
		" AND numcar="+docdto.getNumeroCarguio()+ " "+
		" AND clcru1="+docdto.getRutCliente()+" "+
		" AND confe2="+docdto.getFechaNota()+" ";
		try{
			pstmt = conn.prepareStatement(sqlObtenerDocncp);
			pstmt.executeUpdate();
			elimina=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return elimina;
	}
	
	public int eliminaDocncpTranspDTO (DocncpDTO docdto){
		int elimina=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerDocncp ="DELETE FROM "+
        " CASEDAT.DOCNCP " + 
		" WHERE conemp=2"+
		" AND contip='P'"+
		" AND camco1="+docdto.getCodigoBodega()+" "+
		" AND numcar="+docdto.getNumeroCarguio()+ " "+
		" AND numven="+docdto.getNumeroOV()+ " "+
		" AND clcru1="+docdto.getRutCliente()+" "+
		" AND connu2="+docdto.getNumeroNota()+" ";
		log.info("ANTES DE DELETE DOCNCP  :  "+sqlObtenerDocncp);
		try{
			pstmt = conn.prepareStatement(sqlObtenerDocncp);
			pstmt.executeUpdate();
			elimina=1;
			log.info("O K E Y   ANTES DE DELETE DOCNCP  : "+sqlObtenerDocncp);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return elimina;
	}
	
}
