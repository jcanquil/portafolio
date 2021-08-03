package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TptDocDAO;
import cl.caserita.dao.iface.TptbdgDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.TptdocDTO;

public class TptdocDAOImpl implements TptDocDAO{

	private static Logger log = Logger.getLogger(TptdocDAOImpl.class);
	private Connection conn;
	
	public TptdocDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	private static Logger logi = Logger.getLogger(TptdocDAOImpl.class);
	
	public String buscaDocumento(int docto){
		
		String nombreDocumento="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TPTDOC " + 
        " Where TPACO1="+docto+" FOR READ ONLY" ;
		//logi.info("SELECT NOMBRE DOCUMENTO : "+sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			//logi.info("O K E Y  SELECT NOMBRE DOCUMENTO");
			while (rs.next()) {
				nombreDocumento=(rs.getString("TPADE1").trim());
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
		
		
		return nombreDocumento;
	}
	
}
