package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.EncprinDAO;
import cl.caserita.dto.EncprinDTO;

public class EncprinDAOImpl implements EncprinDAO{

	private static Logger log = Logger.getLogger(EncprinDAOImpl.class);

	private Connection conn;
		
		public EncprinDAOImpl(Connection conn){
			this.conn=conn;
		}
		
		public List obtieneEncuesta(int rut){
			EncprinDTO enprintDTO=null;
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			List encuesta = new ArrayList();
			String sqlObtenerVecmar=" SELECT * "+
					" FROM CASEDAT.ENCPRIN  " + 
					" WHERE CLCRU1 = "+rut+" FOR READ ONLY" ;
			
			log.info("SQL ENCPRIN" + sqlObtenerVecmar);   
			
			try{
				pstmt = conn.prepareStatement(sqlObtenerVecmar);
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					enprintDTO = new EncprinDTO();
					enprintDTO.setRutCliente(rs.getInt("CLCRU1"));
					enprintDTO.setDvCliente(rs.getString("CLCDVR"));
					enprintDTO.setCodigoEncuesta(rs.getInt("CODENCU"));
					enprintDTO.setDescripcionEncuesta(rs.getString("DSCENC"));
					enprintDTO.setEstadoEncuesta(rs.getString("ESTENC"));
					enprintDTO.setTipoEncuesta(rs.getString("TIPTIT"));
					encuesta.add(enprintDTO);
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
			return encuesta;
		}
}
