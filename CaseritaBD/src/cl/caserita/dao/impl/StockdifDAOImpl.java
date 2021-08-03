package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.StockdifDAO;
import cl.caserita.dto.DocconfcDTO;
import cl.caserita.dto.StockdifDTO;

public class StockdifDAOImpl implements StockdifDAO{

	private  static Logger log = Logger.getLogger(StockdifDAOImpl.class);

	private Connection conn;
	
	public StockdifDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int actualizarStockDiferenciado(StockdifDTO dto){
		int correlativo2=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.STOCKDIF " + 
        " SET STOLDF="+dto.getStockLinea()+" Where CODESD="+dto.getCodigoEmpresa()+" AND CODBSD="+dto.getCodigoBodega()+" AND CODASD="+dto.getCodigoArticulo()+" AND DARSDF='"+dto.getDigitoArticulo().trim()+"' AND CODTSD="+dto.getCodigoTipoVendedor()+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
		
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo2;
	}
	
	public StockdifDTO recuperaStockDiferenciado(StockdifDTO gen){
		StockdifDTO stockdifDTO = null;
		
		PreparedStatement pstmt =null;
		List detord = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * from "+
        " CASEDAT.STOCKDIF " + 
        " WHERE CODESD="+gen.getCodigoEmpresa()+" AND CODBSD ="+gen.getCodigoBodega()+" AND CODASD ="+gen.getCodigoArticulo()+" AND DARSDF='"+gen.getDigitoArticulo()+"' AND CODTSD="+gen.getCodigoTipoVendedor()+" FOR READ ONLY" ;
		log.info("Query:"+sqlObtenerCamtra);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				stockdifDTO = new StockdifDTO();
				stockdifDTO.setCodigoEmpresa(rs.getInt("CODESD"));
				stockdifDTO.setCodigoTipoVendedor(rs.getInt("CODTSD"));
				stockdifDTO.setCodigoArticulo(rs.getInt("CODASD"));
				stockdifDTO.setCodigoBodega(rs.getInt("CODBSD"));
				stockdifDTO.setStockLinea(rs.getInt("STOLDF"));
				
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
		return stockdifDTO;
	}
}
