package cl.caserita.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.VedmarDTO;

public class ProcedimientoDAOImpl implements ProcedimientoDAO{

	private  static Logger log = Logger.getLogger(ProcedimientoDAOImpl.class);

	private Connection conn;
	
	public ProcedimientoDAOImpl(Connection conn){
		this.conn=conn;
	}

	
	public int procesaCalculoProcedure(String vemcvm){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			log.info("PROCESA MOVIMIENTOS PROCEDIMIENTO:"+vemcvm);
			CallableStatement cst = conn.prepareCall("{call zsnwi.p70 (?)}");
			cst.setString(1, vemcvm);
	
			//cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int procesaBorraFiles(String vemcvm){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.PCLRPFM (?)}");
			cst.setString(1, vemcvm);
	
			//cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     

			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int procesaBorraLib(String vemcvm){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.PDLTLIB (?)}");
			cst.setString(1, vemcvm);
	
			//cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     

			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int procesaEnviaLib(String vemcvm){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.PSAVLIB (?)}");
			cst.setString(1, vemcvm);
	
			//cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     

			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
		
	public int procesaFacturacion(String empresa, String tipoMov, String fechMov, String numDoc, String codDoc, String rut, String dv, String usuario, String tipo, String nota){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.genfele (?,?,?,?,?,?,?,?,?,?)}");
			cst.setString(1, String.valueOf(empresa));
			cst.setString(2, String.valueOf(tipoMov));
			cst.setString(3, String.valueOf(fechMov));
			cst.setString(4, String.valueOf(numDoc));
			cst.setString(5, String.valueOf(codDoc));
			cst.setString(6, String.valueOf(rut));
			cst.setString(7, String.valueOf(dv));
			cst.setString(8, String.valueOf(usuario));
			cst.setString(9, String.valueOf(tipo));
			cst.setString(10, String.valueOf(nota));

			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int migraInfo1(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO JAIME32.VECMAR (VENEMP, VENCOD, VENFEC, VENNUM, VENCO5, "+
				"VENNU2, VENFE1, VECNU3, VENBO2, VENBOD, VECFOR, VENCAN, VECTOT,    "+
				"VENPOR, VENTOD, VECTO1, VECTO2, VENTO1, VENTO2, VECPES, VECVOL, "+   
				"VENRUT, VENDIG, VENCO4, VENCO6, VENSWI, VENSWP, VECIND, VECDIR, "+   
				"VECCON, VECFE5, VECSWI, VECFE7, VECDE2, VECCO8, VECCO9, VECC01,  "+  
				"VECCO7, VECC02, VECC03, VECRU3, VECDVE, VECPAT, VECFE8) SELECT *   "+
				"FROM casedat.vecmar WHERE VENEMP =2 and VENCOD =21 and      "+       
				"VENFEC="+fecha+" and VENBO2 =26 ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo2(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO JAIME32.VEDMAR (VEDEMP, VENC11, VENFE3, VENNU4, VENCOR,"+
							"VENC15, VENC16, VENDI1, VENFOR, VENCA2, VENCA1, VEDSE1, VENPES,   " +
							"VENVOL, VEDPR2, VEDPRN, VEDCNT, VEDCTN, VEDMO4, VEDMNT, VENDES,   "+ 
							"VENMON, VENMDN, VEDMO3, VENMO2, VENMTN, VENEXE, VENCO3, VENSW1,   "+ 
							"VEDFE1, VEDNU2, VEDAPL) SELECT * FROM casedat.vedmar WHERE VEDEMP "+ 
							" =2 and VENC11 =21 and VENFE3 ="+fecha+" and VENC15 =26 ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo3(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO JAIME32.ORDVTA (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,"+
							"VECFOR, CAMCOD, EXMFEC, CAMES1, RETPAG, CLICOR, ORDCAN, ORDTCS,   "+ 
							"TOTCNT, ORDDSC, ORDTDN, ORDMNT, ORDTNT, TOTCAJ, CLCTOT, ORDTIV,   "+ 
							"ORDTEX, AAJ0A) SELECT * FROM casedat.ordvta WHERE ORDEMP =2 and   "+ 
							"TPTC18 =26 and EXMFEC="+fecha+"     ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo4(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO JAIME32.BBINTC00 (BBCODDOC, BBFECMOV, BBNUMDOC,  "+      
				"BBCODBOD, BBTOTDOC, BBRUTCLI, BBDIGCLI, BBCODVEN, BBINDDES,        "+
				"BBINDFAC, BBFORPAG, BBESTADO, BBERRCAB, BBNOMDIS, BBDISCLI,        "+
				"BBCANCHE, BBMONPIE, BBMONINT, BBFECVEN, BBCODBAN, BBCTACTE) SELECT "+
				"* FROM casedat.bbintc00 WHERE BBFECMOV ="+fecha+" and BBCODBOD =26  	   ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo5(int numero, int numero2){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO JAIME32.DETORD (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,"+
					"CORDOV, CLDCOD, VENFOR, CLDCAN, VEDCA4, VENCA2, EXIPRE, ORDDPR, "+   
					"ORDDCS, ORDDCN, ORDDCT, MONBRU, VENDES, VENMON, ORDDDS, ORDDMN,  "+  
					"ORDDTN, VENMO2, ORDDTE, VECFE5, CLICOR, CAMES1, CLICO1, CLICO2,   "+ 
					"CLICO3, CAMNUM, AATRA, FCHPRO, XOTX1, XOTX2, XOCL1, XOCL2, XOCL3, "+ 
					"XOCL4, XOCL5, XONM1, XONM2, XONM3, XONM4, XONM5) SELECT * FROM    "+ 
					"casedat.detord WHERE ORDEMP =2 and TPTC18 =26 and VECFE5="+numero+" 	   ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	
	public int migraInfo11(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO CASEDAT.VECMAR (VENEMP, VENCOD, VENFEC, VENNUM, VENCO5, "+
				"VENNU2, VENFE1, VECNU3, VENBO2, VENBOD, VECFOR, VENCAN, VECTOT,    "+
				"VENPOR, VENTOD, VECTO1, VECTO2, VENTO1, VENTO2, VECPES, VECVOL, "+   
				"VENRUT, VENDIG, VENCO4, VENCO6, VENSWI, VENSWP, VECIND, VECDIR, "+   
				"VECCON, VECFE5, VECSWI, VECFE7, VECDE2, VECCO8, VECCO9, VECC01,  "+  
				"VECCO7, VECC02, VECC03, VECRU3, VECDVE, VECPAT, VECFE8) SELECT *   "+
				"FROM JAIME32.vecmar WHERE VENEMP =2 and VENCOD =21 and      "+       
				"VENFEC="+fecha+" and VENBO2 =26 ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo12(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO CASEDAT.VEDMAR (VEDEMP, VENC11, VENFE3, VENNU4, VENCOR,"+
							"VENC15, VENC16, VENDI1, VENFOR, VENCA2, VENCA1, VEDSE1, VENPES,   " +
							"VENVOL, VEDPR2, VEDPRN, VEDCNT, VEDCTN, VEDMO4, VEDMNT, VENDES,   "+ 
							"VENMON, VENMDN, VEDMO3, VENMO2, VENMTN, VENEXE, VENCO3, VENSW1,   "+ 
							"VEDFE1, VEDNU2, VEDAPL) SELECT * FROM JAIME32.vedmar WHERE VEDEMP "+ 
							" =2 and VENC11 =21 and VENFE3 ="+fecha+" and VENC15 =26 ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo13(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO CASEDAT.ORDVTA (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,"+
							"VECFOR, CAMCOD, EXMFEC, CAMES1, RETPAG, CLICOR, ORDCAN, ORDTCS,   "+ 
							"TOTCNT, ORDDSC, ORDTDN, ORDMNT, ORDTNT, TOTCAJ, CLCTOT, ORDTIV,   "+ 
							"ORDTEX, AAJ0A) SELECT * FROM JAIME32.ordvta WHERE ORDEMP =2 and   "+ 
							"TPTC18 =26 and EXMFEC="+fecha+"     ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo14(int fecha){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO CASEDAT.BBINTC00 (BBCODDOC, BBFECMOV, BBNUMDOC,  "+      
				"BBCODBOD, BBTOTDOC, BBRUTCLI, BBDIGCLI, BBCODVEN, BBINDDES,        "+
				"BBINDFAC, BBFORPAG, BBESTADO, BBERRCAB, BBNOMDIS, BBDISCLI,        "+
				"BBCANCHE, BBMONPIE, BBMONINT, BBFECVEN, BBCODBAN, BBCTACTE) SELECT "+
				"* FROM JAIME32.bbintc00 WHERE BBFECMOV ="+fecha+" and BBCODBOD =26  	   ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	
	public int migraInfo15(int numero, int numero2){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO CASEDAT.DETORD (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,"+
					"CORDOV, CLDCOD, VENFOR, CLDCAN, VEDCA4, VENCA2, EXIPRE, ORDDPR, "+   
					"ORDDCS, ORDDCN, ORDDCT, MONBRU, VENDES, VENMON, ORDDDS, ORDDMN,  "+  
					"ORDDTN, VENMO2, ORDDTE, VECFE5, CLICOR, CAMES1, CLICO1, CLICO2,   "+ 
					"CLICO3, CAMNUM, AATRA, FCHPRO, XOTX1, XOTX2, XOCL1, XOCL2, XOCL3, "+ 
					"XOCL4, XOCL5, XONM1, XONM2, XONM3, XONM4, XONM5) SELECT ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18,      "+ 
					"CORDOV, CLDCOD, VENFOR, CLDCAN, 0, VENCA2, EXIPRE, ORDDPR, "+   
					"ORDDCS, ORDDCN, ORDDCT, MONBRU, VENDES, VENMON, ORDDDS, ORDDMN,  "+  
					"ORDDTN, VENMO2, ORDDTE, VECFE5, CLICOR, CAMES1, CLICO1, CLICO2,   "+ 
					"CLICO3, CAMNUM, AATRA, FCHPRO, XOTX1, XOTX2, XOCL1, XOCL2, XOCL3,XOCL4, XOCL5, XONM1, XONM2, XONM3, XONM4, XONM5 FROM "+
					"JAIME32.detord WHERE ORDEMP =2 and TPTC18 =26 and vecfe5="+numero+"  	   ";
		
		
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return res;
	}
	public int recuperaNumeroOV(int fecha){
		int numero=0;
		VecmarDTO vecmar = null;
		PreparedStatement pstmt =null;
		HashMap <Integer, VedmarDTO> lista= new HashMap();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT MIN(NUMVEN) AS NUMOV FROM casedat.ORDVTA WHERE ORDEMP =2 AND TPTC18 "+
					"=26 AND EXMFEC="+fecha+"" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					numero=rs.getInt("NUMOV");
				
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
		
		
		return numero;
	}
	
	public int recuperaNumeroOVMaximo(int fecha){
		int numero=0;
		VecmarDTO vecmar = null;
		PreparedStatement pstmt =null;
		HashMap <Integer, VedmarDTO> lista= new HashMap();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT MAX(NUMVEN) AS NUMOV FROM casedat.ORDVTA WHERE ORDEMP =2 AND TPTC18 "+
					"=26 AND EXMFEC="+fecha+"" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					numero=rs.getInt("NUMOV");
				
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
		
		
		return numero;
	}
	
	public int procesaGuiaOT(String empresa, String codBod, String numeroCarguio, String numeroTraspaso, String correlativoOT){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.gencgd (?,?,?,?,?)}");
			cst.setString(1, String.valueOf(empresa));
			cst.setString(2, String.valueOf(codBod));
			cst.setString(3, String.valueOf(numeroCarguio));
			cst.setString(4, String.valueOf(numeroTraspaso));
			cst.setString(5, String.valueOf(correlativoOT));
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int deleteClidir(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.CLIDIR " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteClidira(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.CLIDIRA " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteClmcli(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.CLMCLI " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteTPCTRA(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.TPCTRA " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	public int deleteTPDTRA(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.TPDTRA " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteEXMVND(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.EXMVND " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteTPTCTY(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.TPTCTY " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteTPTCOM(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.TPTCOM " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteCHOFTRAN(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.CHOFTRAN " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteEXMARB(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.EXMARB " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteEXMPTV(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.EXMPTV " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	public int deleteEXDACB(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.EXDACB " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int deleteEXDACP(){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  JAIME32.EXDACP " ;
        
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			 pstmt.executeUpdate();
			
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
	
	public int obtieneCorrelativo(String vemcvm){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			log.info("PROCESA MOVIMIENTOS PROCEDIMIENTO:"+vemcvm);
			CallableStatement cst = conn.prepareCall("{call zsnwi.p69 (?)}");
			cst.setString(1, vemcvm);
			cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
			String numero = cst.getString(1).trim();

			numero = numero.substring(numero.length()-10, numero.length());
			correlativo2 = Integer.parseInt(numero);
			log.info("Salida:"+numero);

		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int procesaPersonalizados(String empresa, String tipoMov, String fecha, String numeroDoc, String codDoc){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.PRCPERSON (?,?,?,?,?)}");
			cst.setString(1, empresa);
			cst.setString(2, tipoMov);
			cst.setString(3, fecha);
			cst.setString(4, numeroDoc);
			cst.setString(5, codDoc);

			
	
			//cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     

			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int procesaGestion(String empresa, String tipoMov, String fecha, String numeroDoc, String codDoc){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.PRCPERSON (?,?,?,?,?)}");
			cst.setString(1, empresa);
			cst.setString(2, tipoMov);
			cst.setString(3, fecha);
			cst.setString(4, numeroDoc);
			cst.setString(5, codDoc);

			
	
			//cst.registerOutParameter(1, java.sql.Types.VARCHAR);                     

			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public int procesaVedmarOV(String numOV, String rut, String dv, String bodega){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		try{
			CallableStatement cst = conn.prepareCall("{call CASEPRG.PRCVEDM (?,?,?,?)}");
			cst.setString(1, String.valueOf(numOV));
			cst.setString(2, String.valueOf(rut));
			cst.setString(3, String.valueOf(dv));
			cst.setString(4, String.valueOf(bodega));
			

			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			cst.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}

}
