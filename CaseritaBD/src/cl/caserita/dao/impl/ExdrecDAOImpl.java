package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExdrecDAO;
import cl.caserita.dto.ConsolidaasnDTO;
import cl.caserita.dto.ExdrecDTO;
import cl.caserita.dto.ExmrecDTO;
import cl.caserita.dto.GenlibDTO;

public class ExdrecDAOImpl implements ExdrecDAO {
	private static Logger log = Logger.getLogger(ExdrecDAOImpl.class);

	private Connection conn;
	
	public ExdrecDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaDetalle(ExdrecDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXDREC " + 
        " (EXMEMP,EXMNUM,FCHCON,HORCON,EXDLIN,CAMCO1,EXDCO3,EXDDI3,EXMDES,EXDST2,EXDFVA,CODEIN,EXDPRN,EXDPRE,EXDMNT,EXDMTN,EXDMBT,EXDTOT) VALUES("+dto.getCodigoEmpresa()+","+dto.getNumeroOrden()+","+dto.getFechaConfirmacionRecepcion()+","+dto.getHoraConfirmacionRecepcion()+","+dto.getLinea()+","+dto.getCodigoBodega()+","+dto.getCodigoArticulo()+",'"+dto.getDvArticulo()+"','"+dto.getDescripcionArticulo()+"',"+dto.getStockRecepcionado()+","+dto.getFechaVencimiento()+",'"+dto.getEstadoInventario()+"',"+dto.getPrecioNeto()+","+dto.getPrecioBruto()+","+dto.getMontoNeto()+","+dto.getMontoTotalNeto()+","+dto.getMontoBruto()+","+dto.getMontoTotal()+")";
		//log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
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
	
	public ExdrecDTO buscaDetalle(int empresa, int nrooc, int fecha, int hora, int codbod, int codart){
		int res=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ExdrecDTO exdrec = null;
		
		String sqlObtenerVecmar="SELECT * FROM"+
		        "  CASEDAT.EXDREC " + 
		        " WHERE EXMEMP = "+empresa+
		        " AND EXMNUM = "+nrooc+
		        " AND FCHCON = "+fecha+
		        " AND HORCON = "+hora+
		        " AND CAMCO1 = "+codbod+
		        " AND EXDCO3 = "+codart;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdrec = new ExdrecDTO();
				
				exdrec.setCodigoEmpresa(rs.getInt("EXMEMP"));
				exdrec.setNumeroOrden(rs.getInt("EXMNUM"));
				exdrec.setFechaConfirmacionRecepcion(rs.getInt("FCHCON"));
				exdrec.setHoraConfirmacionRecepcion(rs.getInt("HORCON"));
				exdrec.setLinea(rs.getInt("EXDLIN"));
				exdrec.setCodigoBodega(rs.getInt("CAMCO1"));
				exdrec.setCodigoArticulo(rs.getInt("EXDCO3"));
				exdrec.setDvArticulo(rs.getString("EXDDI3"));
				exdrec.setDescripcionArticulo(rs.getString("EXMDES"));
				exdrec.setStockRecepcionado(rs.getDouble("EXDST2"));
				exdrec.setStockSolicitado(rs.getDouble("STREWM"));
				exdrec.setFechaVencimiento(rs.getInt("EXDFVA"));
				exdrec.setEstadoInventario(rs.getString("CODEIN"));
				exdrec.setPrecioBruto(rs.getDouble("EXDPRE"));
				exdrec.setPrecioNeto(rs.getDouble("EXDPRN"));
				exdrec.setMontoBruto(rs.getDouble("EXDMBT"));
				exdrec.setMontoTotal(rs.getDouble("EXDTOT"));
				exdrec.setMontoNeto(rs.getDouble("EXDMNT"));
				exdrec.setMontoTotalNeto(rs.getDouble("EXDMTN"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		return exdrec;
	}
	
	public int actualizaDetalle(ExdrecDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar=" UPDATE CASEDAT.EXDREC " +
				" SET exdst2 = exdst2 +"+dto.getStockRecepcionado()+
				" WHERE exmemp = "+dto.getCodigoEmpresa()+
				" AND exmnum = "+dto.getNumeroOrden()+
				" AND fchcon = "+dto.getFechaConfirmacionRecepcion()+
				" AND horcon = "+dto.getHoraConfirmacionRecepcion()+
				" AND camco1 = "+dto.getCodigoBodega()+
				" AND exdco3 = "+dto.getCodigoArticulo()+
				" AND exddi3 = '"+dto.getDvArticulo()+"'";
		
		log.info("ACTUALIZA ARTICULO EXDREC : " + sqlObtenerVecmar);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
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
	
	public ExdrecDTO buscaDetalleCobro(int empresa, int nroot, int fecha, int hora, int codbod){
		int res=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ExdrecDTO exdrec = null;
		
		String sqlObtenerVecmar="SELECT * FROM"+
		        "  CASEDAT.EXDREC " + 
		        " WHERE EXMEMP = "+empresa+
		        " AND EXMNUM = "+nroot+
		        " AND FCHCON = "+fecha+
		        " AND HORCON = "+hora+
		        " AND CAMCO1 = "+codbod;
		
		log.info("SQL DETALLE COBRO DEL CHOFER " + sqlObtenerVecmar);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdrec = new ExdrecDTO();
				
				exdrec.setCodigoEmpresa(rs.getInt("EXMEMP"));
				exdrec.setNumeroOrden(rs.getInt("EXMNUM"));
				exdrec.setFechaConfirmacionRecepcion(rs.getInt("FCHCON"));
				exdrec.setHoraConfirmacionRecepcion(rs.getInt("HORCON"));
				exdrec.setLinea(rs.getInt("EXDLIN"));
				exdrec.setCodigoBodega(rs.getInt("CAMCO1"));
				exdrec.setCodigoArticulo(rs.getInt("EXDCO3"));
				exdrec.setDvArticulo(rs.getString("EXDDI3"));
				exdrec.setDescripcionArticulo(rs.getString("EXMDES"));
				exdrec.setStockRecepcionado(rs.getDouble("EXDST2"));
				exdrec.setStockSolicitado(rs.getDouble("STREWM"));
				exdrec.setFechaVencimiento(rs.getInt("EXDFVA"));
				exdrec.setEstadoInventario(rs.getString("CODEIN"));
				exdrec.setPrecioBruto(rs.getDouble("EXDPRE"));
				exdrec.setPrecioNeto(rs.getDouble("EXDPRN"));
				exdrec.setMontoBruto(rs.getDouble("EXDMBT"));
				exdrec.setMontoTotal(rs.getDouble("EXDTOT"));
				exdrec.setMontoNeto(rs.getDouble("EXDMNT"));
				exdrec.setMontoTotalNeto(rs.getDouble("EXDMTN"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		return exdrec;
	}
	
	public List recuperaDetalleCompletoCobro(int empresa, int nroot, int fecha, int hora, int codbod){
		ExdrecDTO exdrecDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar="SELECT * FROM"+
		        "  CASEDAT.EXDREC " + 
		        " WHERE EXMEMP = "+empresa+
		        " AND EXMNUM = "+nroot+
		        " AND FCHCON = "+fecha+
		        " AND HORCON = "+hora+
		        " AND CAMCO1 = "+codbod+
		        " FOR READ ONLY";
		
		log.info("SQL" + sqlObtenerVecmar);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
				
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdrecDTO = new ExdrecDTO();
				
				exdrecDTO.setCodigoEmpresa(rs.getInt("EXMEMP"));
				exdrecDTO.setNumeroOrden(rs.getInt("EXMNUM"));
				exdrecDTO.setFechaConfirmacionRecepcion(rs.getInt("FCHCON"));
				exdrecDTO.setHoraConfirmacionRecepcion(rs.getInt("HORCON"));
				exdrecDTO.setLinea(rs.getInt("EXDLIN"));
				exdrecDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				exdrecDTO.setCodigoArticulo(rs.getInt("EXDCO3"));
				exdrecDTO.setDvArticulo(rs.getString("EXDDI3"));
				exdrecDTO.setDescripcionArticulo(rs.getString("EXMDES"));
				exdrecDTO.setStockRecepcionado(rs.getDouble("EXDST2"));
				exdrecDTO.setStockSolicitado(rs.getDouble("STREWM"));
				exdrecDTO.setFechaVencimiento(rs.getInt("EXDFVA"));
				exdrecDTO.setEstadoInventario(rs.getString("CODEIN"));
				exdrecDTO.setPrecioBruto(rs.getDouble("EXDPRE"));
				exdrecDTO.setPrecioNeto(rs.getDouble("EXDPRN"));
				exdrecDTO.setMontoBruto(rs.getDouble("EXDMBT"));
				exdrecDTO.setMontoTotal(rs.getDouble("EXDTOT"));
				exdrecDTO.setMontoNeto(rs.getDouble("EXDMNT"));
				exdrecDTO.setMontoTotalNeto(rs.getDouble("EXDMTN"));
				lista.add(exdrecDTO);
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
