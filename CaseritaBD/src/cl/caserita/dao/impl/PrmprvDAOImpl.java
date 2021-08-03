package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.PrmprvDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.PrmprvDTO;

public class PrmprvDAOImpl implements PrmprvDAO{

	private static Logger log = Logger.getLogger(PrmprvDAOImpl.class);
	private Connection conn;
	
	public PrmprvDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public PrmprvDTO obtieneProveedor(int rut, String dv){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		PrmprvDTO prmprv = null;
		String sqlObtenerPrmprv ="Select * "+
        " from CASEDAT.PRMPRV   " + 
        " Where PRMRUT="+rut+" AND PRMDIG='"+dv+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerPrmprv);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				prmprv = new PrmprvDTO();
				
				log.info("rut"+rs.getInt("PRMRUT"));
				
				prmprv.setDvProv(rs.getString("PRMDIG"));
				prmprv.setRazonSocialProv(rs.getString("PRMNOM"));
				prmprv.setRutProv(rs.getInt("PRMRUT"));
				prmprv.setDescCiudadProv(recuperaCiudad(rs.getInt("PRMCOD"), rs.getInt("PRMCO2")));
				prmprv.setDescComunaProv(recuperaComuna(rs.getInt("PRMCOD"), rs.getInt("PRMCO2"), rs.getInt("PRMCO3")));
				prmprv.setDireccionProv(rs.getString("PRMDIR"));
				prmprv.setNombreEmpresa(rs.getString("PRMNO1"));
				
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
		return prmprv;
	}
	
	public String recuperaComuna(int region, int ciudad, int comuna){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCOM " + 
        " Where TPTC19="+region+" AND TPTC20="+ciudad+" AND TPTC22="+comuna+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTD20");
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
		return descomuna;
	}
	
	public String recuperaCiudad(int region, int ciudad){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCTY " + 
        " Where TPTCO6="+region+" AND TPTCO7="+ciudad+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTDE7");
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
		return descomuna;	
	}
	
	public String recuperaRegion(int region){
		String desregion="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTREG " + 
        " Where TPTCO1="+region+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				desregion = rs.getString("TPTDE1");
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
		return desregion;
	}
	
	public List recuperaProveedores(){
		PrmprvDTO prmprvDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.PRMPRV " +
		" WHERE prmti2 <> 'S'" +
        " ORDER BY prmrut ";
		
		List prmprv = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL " + sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				prmprvDTO = new PrmprvDTO();
				
				//log.info("rut : "+rs.getInt("PRMRUT") + " tipo : "+rs.getString("PRMTI2"));
				
				prmprvDTO.setRutProv(rs.getInt("PRMRUT"));
				prmprvDTO.setDvProv(rs.getString("PRMDIG"));
				prmprvDTO.setRazonSocialProv(rs.getString("PRMNOM"));
				prmprvDTO.setDireccionProv(rs.getString("PRMDIR"));
				prmprvDTO.setNombreEmpresa(rs.getString("PRMNO1"));
				
				prmprvDTO.setDescRegionProv(recuperaRegion(rs.getInt("PRMCOD")));
				prmprvDTO.setDescCiudadProv(recuperaCiudad(rs.getInt("PRMCOD"), rs.getInt("PRMCO2")));
				prmprvDTO.setDescComunaProv(recuperaComuna(rs.getInt("PRMCOD"), rs.getInt("PRMCO2"), rs.getInt("PRMCO3")));
				
				prmprv.add(prmprvDTO);
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
	return prmprv;
	}
}
