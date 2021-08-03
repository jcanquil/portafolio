package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.UsrWebDAO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CliDistDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.GenlibDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.UsrWebDTO;
import cl.caserita.dto.VecmarDTO;

public class UsrWebDAOImpl implements UsrWebDAO{

private Connection conn;
private static Logger log = Logger.getLogger(UsrWebDAOImpl.class);

	public UsrWebDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int valida (String rut, String usuario, String password){
		UsrWebDTO usrWebDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int acceso=0;
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRWEB " + 
        " Where CLMRUT="+rut+" AND USRCLI='"+usuario+"' AND PASCLI='"+password+"'  FOR READ ONLY" ;
		//log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				acceso=1;
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return acceso;
	}
	
	public List<ClcmcoDTO> documentos (){
		List <ClcmcoDTO> documentos = new ArrayList();;
		ClcmcoDTO clcmco =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int acceso=0;
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.CLCMCO " + 
        " Where CLCEMP=2 AND CLCCOD=33 AND CLCRUT=80847823 AND CLCFEC>=20150101  FOR READ ONLY" ;
		//log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				clcmco.setCodDocumento(rs.getInt("CLCCO1"));
				clcmco.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmco.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmco.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmco.setValorNeto(rs.getInt("CLCTNT"));
				clcmco.setTotalIva(rs.getInt("CLCTO1"));
				clcmco.setTotalDocumento(rs.getInt("CLCTOT"));
				documentos.add(clcmco);
				
				
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return documentos;
	}
	public int usrDistribucion (int codigo, String password){
		UsrWebDTO usrWebDTO=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		int acceso=0;
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.USRDIST " + 
        " Where CAMCOD="+codigo+" AND CLAUSR='"+password+"' FOR READ ONLY" ;
		//log.info("SQL VECMAR" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				acceso=1;
				
				
			}
			//log.info("Despues de buscar en VECMAR");
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
		return acceso;
	}
	
	public int generaGenDireccion(CliDistDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DIRDIST " + 
        " (CLCRU1,CLCDVR,TPTRAZ,CLITEL,CLITE1,CLICON,CLIDI1,CLICO1,CLICO2,CLICO3,CCOEA ,LATDIR,LONDIR, CAMCOD) VALUES("+gen.getRut()+",'"+gen.getDv()+"','"+gen.getRazonSocial()+"','"+gen.getTelefono1()+"','"+gen.getTelefono2()+"','"+gen.getNombreContacto()+"','"+gen.getDireccion()+"',"+gen.getCodRegion()+","+gen.getCodCiudad()+","+gen.getCodComuna()+","+gen.getTipoNegocio()+",'"+gen.getLatitud()+"','"+gen.getLongitud()+"',"+gen.getCodigoVendedor()+")";
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
	
}
