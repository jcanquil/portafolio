package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmrecDAO;
import cl.caserita.dto.ExmrecDTO;

public class ExmrecDAOImpl implements ExmrecDAO {

	private static Logger log = Logger.getLogger(ExmrecDAOImpl.class);

private Connection conn;
	
	public ExmrecDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaEncabezado(ExmrecDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXMREC " + 
        " (EXMEMP,EXMNUM,FCHCON,HORCON,CAMCO1,CLDRUT,CONDI4,CLCNUM,NOMACN,IDCAMI) VALUES("+dto.getCodEmpresa()+","+dto.getNumeroOrden()+","+dto.getFechaConfirmacionRecepcion()+","+dto.getHoraConfirmacionRecepcion()+","+dto.getCodigoBodega()+","+dto.getRutProveedor()+",'"+dto.getDvProveedor()+"',"+dto.getNumeroDocumento()+",'"+dto.getNombreArchivoConfirmacion()+"','"+dto.getIdCamion()+"')";
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
	public ExmrecDTO buscaEncabezado(ExmrecDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ExmrecDTO exmrec = null;
		String sqlObtenerVecmar="SELECT * FROM"+
        "  CASEDAT.EXMREC " + 
        " WHERE EXMEMP="+dto.getCodEmpresa()+" AND EXMNUM="+dto.getNumeroOrden()+" AND CLCNUM="+dto.getNumeroDocumento()+" ";
		log.info("RECUPERA DATOS TRASPASOS" + sqlObtenerVecmar);
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmrec = new ExmrecDTO();
				exmrec.setCodEmpresa(rs.getInt("EXMEMP"));
				exmrec.setNumeroOrden(rs.getInt("EXMNUM"));
				exmrec.setFechaConfirmacionRecepcion(rs.getInt("FCHCON"));
				exmrec.setHoraConfirmacionRecepcion(rs.getInt("HORCON"));
				exmrec.setNumeroDocumento(rs.getInt("CLCNUM"));
				exmrec.setRutProveedor(rs.getInt("CLDRUT"));
				exmrec.setDvProveedor(rs.getString("CONDI4"));
				exmrec.setCodigoBodega(rs.getInt("CAMCO1"));
			
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		return exmrec;
	}
	
	public int actualizaEncabezado(ExmrecDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE " +
			" CASEDAT.EXMREC SET nomacn = '"+dto.getNombreArchivoConfirmacion().trim()+"',"+
			" idcami = '"+dto.getIdCamion()+"'"+
			" WHERE exmemp = "+dto.getCodEmpresa()+
			" AND exmnum = "+dto.getNumeroOrden()+
			" AND fchcon = "+dto.getFechaConfirmacionRecepcion()+
			" AND horcon = "+dto.getHoraConfirmacionRecepcion()+
			" AND camco1 = "+dto.getCodigoBodega()+
			" AND cldrut = "+dto.getRutProveedor()+
			" AND condi4 = '"+dto.getDvProveedor()+"'"+
			" AND clcnum = "+dto.getNumeroDocumento();
			
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
	
	public ExmrecDTO buscaOcRececp(String idCamion){
		int res=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ExmrecDTO exmrec = null;
		
		String sqlObtenerVecmar="SELECT * FROM"+
        " CASEDAT.EXMREC " + 
        " WHERE idcami='"+idCamion+"'";
		
		log.info("BUSCAR ID CAMION" + sqlObtenerVecmar);
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exmrec = new ExmrecDTO();
				exmrec.setCodEmpresa(rs.getInt("EXMEMP"));
				exmrec.setNumeroOrden(rs.getInt("EXMNUM"));
				exmrec.setNumeroDocumento(rs.getInt("CLCNUM"));
				exmrec.setRutProveedor(rs.getInt("CLDRUT"));
				exmrec.setDvProveedor(rs.getString("CONDI4"));
				exmrec.setCodigoBodega(rs.getInt("CAMCO1"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();

			} catch (SQLException e1) { }
		} 
		return exmrec;
	}
	
}
