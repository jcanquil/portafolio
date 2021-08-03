package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.NcplogDAO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.NcplogDTO;

public class NcplogDAOImpl implements NcplogDAO {
	
	private  static Logger log = Logger.getLogger(NcplogDAOImpl.class);
	
	private Connection conn;
	
	public NcplogDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	private static Logger logi = Logger.getLogger(NcplogDAOImpl.class);
	
	public int insertaNcplog(NcplogDTO dto){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="INSERT INTO CASEDAT.NCPLOG "+
				"(CONEMP, CONTIP, CONNU2, CONFE2, EXDLIN, NUMCAR, NUMVEN, CLCNUM, CONCO1, CONRU1, CONDI1, ADSCO4, TIPACC, "+
				"CONCO7, CONDI3, CONCAN, CLDCAN, FCHUSR, HORUSR, IPEQUI, NMEQUI, USRSYS)"+
				" VALUES ("+dto.getCodigoEmpresa()+",'"+dto.getTipo()+"',"+dto.getNumeroNota()+","+dto.getFechaNota()+","+dto.getLineaNota()+","+
		        dto.getNumeroCarguio()+","+dto.getNumeroOrden()+","+dto.getNumeroDocumento()+","+dto.getCodigoBodega()+","+
				dto.getRutCliente()+",'"+dto.getDigCliente()+"','"+dto.getCodigoUsuario()+"','"+dto.getTipoAccion()+"',"+
				dto.getCodigoArticulo()+",'"+dto.getDigitoArticulo()+"',"+dto.getCantidad()+","+dto.getCantidadArticulo()+","+
				dto.getFechaUser()+","+dto.getHoraUser()+",'"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"')";
				logi.info("ANTES DE INSERT NCPLOG  :  "+sqlObtener);
				try{
					pstmt = conn.prepareStatement(sqlObtener);
					pstmt.executeUpdate();
					correlativo=1;
					logi.info("O K E Y  INSERT NCPLOG   :  "+sqlObtener);
					
				}catch(Exception e){
					e.printStackTrace();
				}finally {

					try {

						 if (rs != null) { rs.close(); 
						 pstmt.close();
						 }

					} catch (SQLException e1) { }

			  } 
		
		
		return correlativo;
		
		
	}
	
	
	
	
	
	public int buscaUltimaLineaNcplog(NcplogDTO dto){
		int existenumero=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtener ="SELECT EXDLIN "+
        " FROM CASEDAT.NCPLOG " + 
        " WHERE CONEMP=2 "+" AND CONTIP='"+dto.getTipo()+"' "+
        " AND CONNU2="+dto.getNumeroNota()+" "+
        " AND NUMCAR="+dto.getNumeroCarguio()+" "+
        " AND NUMVEN="+dto.getNumeroOrden()+" "+
        " AND CLCNUM="+dto.getNumeroDocumento()+" "+
        " AND CONRU1="+dto.getRutCliente()+" AND CONCO1="+dto.getCodigoBodega()+" ORDER BY EXDLIN DESC";
        try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				existenumero=rs.getInt("EXDLIN")+1;
				break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

			
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return existenumero;
	}
	

	
	
	

}
