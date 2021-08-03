package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.LibtpdDAO;
import cl.caserita.dto.LibtpdDTO;

public class LibtpdDAOImpl implements LibtpdDAO{
	
	private static Logger log = Logger.getLogger(LibtpdDAOImpl.class);
	private Connection conn;
	
	public LibtpdDAOImpl(Connection conn){
		this.conn=conn;
	}

	public List recuperaDocumentos(String codigoLibro){
		List libtpd = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.LIBTPD " + 
        " Where CODLCNT='"+codigoLibro+"' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				LibtpdDTO lib = new LibtpdDTO();
				lib.setCodigoLibro(codigoLibro);
				lib.setCodigoDocumento(rs.getInt("TPACO1"));
				libtpd.add(lib);
				
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
		return libtpd;
	}
}
