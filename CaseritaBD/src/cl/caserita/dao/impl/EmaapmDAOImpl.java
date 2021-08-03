package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.EmaapmDAO;
import cl.caserita.dto.DocncpDTO;
import cl.caserita.dto.EmaapmDTO;

public class EmaapmDAOImpl implements EmaapmDAO{

	private static Logger log = Logger.getLogger(EmaapmDAOImpl.class);

	private Connection conn;
		
		public EmaapmDAOImpl(Connection conn){
			this.conn=conn;
		}
		
		public List obtieneMailAPP(String codAPP){
			EmaapmDTO emaapmDTO=null;
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			List email = new ArrayList();
			String sqlObtenerVecmar=" SELECT * "+
					" FROM CASEDAT.Emaapm  " + 
					" WHERE CODAPM = '"+codAPP.trim()+"' FOR READ ONLY" ;
			
			log.info("SQL Emaapm" + sqlObtenerVecmar);   
			
			try{
				pstmt = conn.prepareStatement(sqlObtenerVecmar);
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					emaapmDTO = new EmaapmDTO();
					emaapmDTO.setCodApp(rs.getString("CODAPM"));
					emaapmDTO.setEmail(rs.getString("EMAUSR"));
					email.add(emaapmDTO);
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
			return email;
		}
}
