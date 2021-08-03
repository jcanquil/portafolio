package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TiponegocioDAO;
import cl.caserita.dto.TiponegocioDTO;
import cl.caserita.dto.TptbdgDTO;

public class TiponegocioDAOImpl implements TiponegocioDAO {

private Connection conn;
private  static Logger log = Logger.getLogger(TiponegocioDAOImpl.class);

	public TiponegocioDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List tipoNegocio(){
		TiponegocioDTO tipoNegocio = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List tipoNeg=new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.TIPNEGOC " + 
        "  FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipoNegocio = new TiponegocioDTO();
				tipoNegocio.setCodigoTipoNegocio(rs.getInt("CCOEA"));
				tipoNegocio.setDescripcionTipoNegocio(rs.getString("CCOFA"));
				tipoNeg.add(tipoNegocio);
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
		
		
		return tipoNeg;
	}
}
