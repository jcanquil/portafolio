package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.RespquadDAO;
import cl.caserita.dto.RespquadDTO;

public class RespquadDAOImpl implements RespquadDAO {
	
	private static Logger log = Logger.getLogger(RespquadDAOImpl.class);
	private Connection conn;
	
	public RespquadDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int buscaRespquad(RespquadDTO respa){
		int existe=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT NUMCAR FROM CASEDAT.RESPQUAD "+
		       	" WHERE CACEMP="+respa.getCodigoEmpresa()+" AND CAMRUT="+respa.getRutChofer()+
		       	" AND EXMDI3='"+respa.getDvChofer()+"'"+
				" AND NUMCAR="+respa.getNumCarguio()+ " ";

			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				existe=1;
			}		
				}catch(Exception e){
					e.printStackTrace();
				}finally {

					try {
						 if (rs != null) { rs.close(); 
						 pstmt.close();
						 }

					} catch (SQLException e1) { }

			  } 

		return existe;
		
	}

	
	public int insertaRespquad(RespquadDTO cargu){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="INSERT INTO CASEDAT.RESPQUAD " + 
        " (CACEMP, CAMRUT, EXMDI3, NUMCAR, FCHUSR, HORUSR, CARGEXI, "+
        " CARGERR, ORDEXI, ORDERR, DETEXI, DETERR) " +
		" VALUES("+cargu.getCodigoEmpresa()+","+cargu.getRutChofer()+",'"+cargu.getDvChofer()+"',"
        +cargu.getNumCarguio()+","+cargu.getFechaUser()+","+cargu.getHoraUser()+","
		+cargu.getCarguioExi()+","+cargu.getCarguioErr()+","+cargu.getOvExi()+","
        +cargu.getOvErr()+","+cargu.getDetExi()+","+cargu.getDetErr()+") " ;
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			//System.out.println("INSERTA EN TABLA Respquad  : " + cargu.getNumCarguio());
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
	
	

	
	
	
	public int actualizaRespquad(RespquadDTO cargu){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="UPDATE "+
        "  CASEDAT.RESPQUAD " + 
        " SET CARGEXI="+cargu.getCarguioExi()+", CARGERR="+cargu.getCarguioErr()+","+
        " ORDEXI="+cargu.getOvExi()+", ORDERR="+cargu.getOvErr()+","+
        " DETEXI="+cargu.getDetExi()+", DETERR="+cargu.getDetErr()+","+
        " FCHUSR="+cargu.getFechaUser()+", HORUSR="+cargu.getHoraUser()+" "+
       	" WHERE CACEMP="+cargu.getCodigoEmpresa()+" AND CAMRUT="+cargu.getRutChofer()+
       	" AND EXMDI3='"+cargu.getDvChofer()+"'"+
		" AND NUMCAR="+cargu.getNumCarguio()+ " ";
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			//System.out.println("update ordvdet" + sqlObtener);
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
	
	
	
	
	
	
	
	
	

}
