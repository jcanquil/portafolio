package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import cl.caserita.dao.iface.VarcostDAO;
import cl.caserita.dto.VarcostDTO;

public class VarcostDAOImpl implements VarcostDAO{
	private static Logger log = Logger.getLogger(VarcostDAOImpl.class);

	private Connection conn;
	
	public VarcostDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int insertaVariacionCostos(VarcostDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVarcost ="INSERT INTO "+
				"CASEDAT.VARCOST " +
				"(camco1, cldcod, condi3, clcfe1, clcho1, exmst7, exmc32, cosnuev, difcomp, difere) " +
		        "VALUES("+dto.getCodBodega()+","+dto.getCodArticulo()+",'"+dto.getDvArticulo()+"',"+dto.getFechaMov()+","+dto.getHoraMov()+","+dto.getStockCompu()+","+dto.getCostoAnterior()+","+dto.getCostoNuevo()+","+dto.getDifCompra()+","+dto.getDiferencia()+")";
				
		log.info("INSERT : "+sqlObtenerVarcost);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVarcost);
			pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

		} 
		return res;
	}
}
