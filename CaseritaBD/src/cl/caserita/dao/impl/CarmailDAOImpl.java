package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CarmailDAO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CarmailDTO;

public class CarmailDAOImpl implements CarmailDAO{

	private  static Logger log = Logger.getLogger(CarmailDAOImpl.class);

	private Connection conn;
	
	public CarmailDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List buscaMail(){
		CarmailDTO carmailDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.CARMAIL  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				carmailDTO = new CarmailDTO();
				carmailDTO.setCodigoEmpresa(rs.getInt("AA12A"));
				carmailDTO.setCodigoBodega(rs.getInt("EXMC11"));
				carmailDTO.setCodigoCuentaCorreo(rs.getString("CODCTCO"));
				carmailDTO.setDescripcionCuentaCorreo(rs.getString("DSCCTCO"));
				carmailDTO.setEstado(rs.getString("CONCES"));
				lista.add(carmailDTO);
				
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
		return lista;

	}
}
