package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExdartDAO;
import cl.caserita.dto.ExdartDTO;

public class ExdartDAOImpl implements ExdartDAO{
	private static Logger log = Logger.getLogger(ExdartDAOImpl.class);

	private Connection conn;
	
	public ExdartDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ExdartDTO buscaCodBarraArt(int codart, String dvart, String formato){
		ExdartDTO exdartDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " FROM CASEDAT.EXDART " + 
        " WHERE exdco1 ="+codart+
        " AND exddi1 = '"+dvart+"'"+
        " AND exdtip = '"+formato+"'"+
        " FOR READ ONLY " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdartDTO = new ExdartDTO();
				
				exdartDTO.setCodigoArticulo(rs.getInt("EXDCO1"));
				exdartDTO.setDvArticulo(rs.getString("EXDDI1"));
				exdartDTO.setCodigoBarra(rs.getString("EXDC01"));
				exdartDTO.setAlto(rs.getDouble("EXDALT"));
				exdartDTO.setAncho(rs.getDouble("EXDANC"));
				exdartDTO.setLargo(rs.getDouble("EXDLAR"));
				exdartDTO.setCantBasePallets(rs.getDouble("EXDCBP"));
				exdartDTO.setCantAltoPallets(rs.getDouble("EXDCAP"));
				exdartDTO.setCantTotalPallets(rs.getDouble("EXDTCP"));
				exdartDTO.setPeso(rs.getDouble("EXDPES"));
				exdartDTO.setVolumen(rs.getDouble("EXDVOL"));
				exdartDTO.setTipoContenedor(rs.getString("EXDTIP"));
				
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
		return exdartDTO;
	}
}
