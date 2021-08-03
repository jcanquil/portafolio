package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.RutservDTO;

public class RutservDAOImpl implements RutservDAO {

	
	private Connection conn;
	
	public RutservDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public RutservDTO recuperaEndPointServlet (String nombreTabla){
		RutservDTO rutserv=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.RUTSERV" + 
        " Where CODSERV='"+nombreTabla.trim()+"'  FOR READ ONLY" ;
		System.out.println("SQL rutserv" + sqlObtenerVecmar);   
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		  //System.out.println("PASA POR ACS");
			
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					rutserv = new RutservDTO();
					rutserv.setNombreTabla(rs.getString("CODSERV").trim());
					rutserv.setEndPoint(rs.getString("AA1BLA").trim());
				
				
			}
			//System.out.println("Despues de buscar en VECMAR");
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
		return rutserv;
	}
}
