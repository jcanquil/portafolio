package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.OrdtrbDAO;
import cl.caserita.dto.OrdswmsDTO;
import cl.caserita.dto.OrdtrbDTO;
import cl.caserita.dto.OrdvddeDTO;

public class OrdtrbDAOImpl implements OrdtrbDAO {

	private static Logger log = Logger.getLogger(OrdtrbDAOImpl.class);
	private Connection conn;
	
	public OrdtrbDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaOrden(OrdtrbDTO ord){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.ORDTRB " + 
        " (EMPATE, NUMOT, FCHOT, EXMF08, NOMSOL, DIASOL, FCHCIE, HORCIE, PRIORD, CODDEP,CODTEC, CODEOR,FCHUSR, HORUSR, HORORD) VALUES("+ord.getCodigoEmpresa()+","+ord.getNumeroOrdenTrabajo()+","+ord.getFechaOrdenTrabajo()+","+ord.getFechaSolicitud()+",'"+ord.getNombreSolicitante()+"',"+ord.getDiaSinSolucion()+","+ord.getFechaCierreOrden()+","+ord.getHoraCierreOrden()+","+ord.getPrioridadOrden()+",'"+ord.getCodigoDepartamento().trim()+"',"+ord.getCodigoTecnico()+",'"+ord.getCodigoEstado()+"',"+ord.getFechaCreacion()+","+ord.getHoraCreacion()+","+ord.getHoraOrden()+")";
		log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	
	public int buscaNumeroOrden(){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT MAX(NUMOT) AS NUMOT FROM CASEDAT.ORDTRB "+
			" WHERE EMPATE=2 ";
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("NUMOT");
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

		return correlativo;
		
	}
	
	
	
}
