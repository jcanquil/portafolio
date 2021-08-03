package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dto.ExmrecDTO;
import cl.caserita.dto.FtpprovDTO;

public class FtpprovDAOImpl implements FtpprovDAO{
	private static Logger log = Logger.getLogger(FtpprovDAOImpl.class);

    private Connection conn;
	
	public FtpprovDAOImpl(Connection conn){
		this.conn=conn;
	}
	public FtpprovDTO buscaFTP(String nombre){
		int res=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		FtpprovDTO ftpprov = null;
		String sqlObtenerVecmar="SELECT * FROM"+
        "  CASEDAT.FTPPROV " + 
        " WHERE NOMFTP='"+nombre+"' ";
		//log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ftpprov = new FtpprovDTO();
				ftpprov.setIpServidor(rs.getString("DIPFTP"));
				ftpprov.setUsuarioFTP(rs.getString("USRFTP"));
				ftpprov.setPasswordFTP(rs.getString("PASSFTP"));
			
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return ftpprov;
	}
}
