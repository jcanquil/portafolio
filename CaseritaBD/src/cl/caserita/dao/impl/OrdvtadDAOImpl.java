package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.OrdvtadDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.DimensionesDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.OrdvtadDTO;

public class OrdvtadDAOImpl implements OrdvtadDAO{
	
	private static Logger log = Logger.getLogger(OrdvtadDAOImpl.class);

	private Connection conn;
	
	public OrdvtadDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public OrdvtadDTO obtieneOrdenes(int empresa, int numeroOV, int bodega, int rut){
		OrdvtadDTO ordvtadDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTAD " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND TPTC18="+bodega+" AND CLMRUT="+rut+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtadDTO = new OrdvtadDTO();
				
				String datos = rs.getString("XOTX2");
				log.info("Datos OC :"+datos);
				if (datos!=null){
					if (datos.length()>0){
						ordvtadDTO.setFechaOC(datos.substring(0, 10));
						ordvtadDTO.setNumeroOC(datos.substring(10, datos.length()));
					}
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return ordvtadDTO;
	}
	
	public static void main (String []args){
		String numero ="20161202  NUMEROOC-8181891";
		String fecha =numero.substring(0, 10);
		String numeroOC=numero.substring(10, numero.length());
		log.info(fecha);
		
	}
}
