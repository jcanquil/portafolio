package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExdacbDAO;
import cl.caserita.dto.ExdacbDTO;
import cl.caserita.dto.ExdodcDTO;

public class ExdacbDAOImpl implements ExdacbDAO {
	private static Logger log = Logger.getLogger(ExdacbDAOImpl.class);

private Connection conn;
	
	public ExdacbDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List listaExdodc(int codArticulo, String dvArticulo){
		ExdacbDTO exdacb = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerExdodc="Select EXDCOD, EXDDIG, EXDCO2, EXDDI2,  SUM(EXDCAN) AS CANTID"+
        " from CASEDAT.EXDACB " + 
        " Where EXDCOD="+codArticulo+" AND EXDDIG="+dvArticulo+" GROUP BY EXDCOD, EXDDIG, EXDCO2, EXDDI2 FOR READ ONLY" ;
		List listadetoc = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdodc);
			
			//log.info("SQL" + sqlObtenerExdodc);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdacb = new ExdacbDTO();
					exdacb.setCodigoArticulo(codArticulo);
					exdacb.setDigitoArticulo(dvArticulo);
					exdacb.setCodigoArticuloHijo(rs.getInt("EXDCO2"));
					exdacb.setDigitoHijoArticulo(rs.getString("EXDDI2"));
					//exdacb.setCobro(rs.getString("EXDCOB"));
					exdacb.setCantidad(rs.getInt("CANTID"));
					listadetoc.add(exdacb);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return listadetoc;
	}
	
}
