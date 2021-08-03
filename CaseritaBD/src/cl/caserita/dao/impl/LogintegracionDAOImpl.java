package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ExmodcDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.LogintegracionDTO;
import cl.caserita.dto.VecmarDTO;

public class LogintegracionDAOImpl implements LogintegracionDAO{

	private static Logger log = Logger.getLogger(LogintegracionDAOImpl.class);
	private Connection conn;
	
	public LogintegracionDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaLogArticulo(LogintegracionDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXMLINT " + 
        " (EXMCOD, EXMDIG,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto.getCod()+",'"+dto.getDv()+"','"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogProveedor(LogintegracionDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.PRMPINT " + 
        " (PRMRUT, PRMDIG,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto.getCod()+",'"+dto.getDv()+"','"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogChoferes(LogintegracionDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CHOFINFL " + 
        " (TPCRUT, TPCDVT,CAMRUT,EXMDI3,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto.getCod()+",'"+dto.getDv()+"',"+dto.getCod2()+",'"+dto.getDv2()+"','"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogCliente(LogintegracionDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CLIDLINT " + 
        " (CLIRUT, CLIDIG,CLICOR,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto.getCod()+",'"+dto.getDv()+"',"+dto.getCorrelativoDir()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogCarguio(LogintegracionDTO dto, CarguioDTO dto2){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.CARGLINT " + 
        " (CACEMP,NUMCAR,VECPAT,CAMCO1,NUMVEN,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto2.getCodigoEmpresa()+","+dto2.getNumeroCarguio()+",'"+dto2.getPatente()+"',"+dto2.getCodigoBodega()+","+dto2.getNumeroRuta()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogTraspaso(LogintegracionDTO dto, ExmtraDTO dto2){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXMLGINT " + 
        " (EXIMEM,EXINUM,EXIBO4,EXIBO5,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto2.getCodigoEmpresa()+","+dto2.getNumTraspaso()+","+dto2.getBodegaOrigen()+","+dto2.getBodegaDestino()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogOrdenesCompra(LogintegracionDTO dto, ExmodcDTO dto2){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.EXMOLIN " + 
        " (EXMEMP,EXMNUM,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto2.getCodEmpresa()+","+dto2.getNumeroOrden()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogMerma(LogintegracionDTO dto, VecmarDTO dto2){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.VECLOIN " + 
        " (VENEMP,VENCOD,VENFEC,VENNUM,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto2.getCodigoEmpresa()+","+dto2.getCodTipoMvto()+","+dto2.getFechaMvto()+","+dto2.getNumDocumento()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogTptbdg(LogintegracionDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.TPTBLOIN " + 
        " (TPTC18,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto.getCod()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	public int generaLogTptcom(LogintegracionDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.TPTCLOI " + 
        " (CLICO1,CLICO3,CODALOG,FCHALOG,HORALOG,NOMALOG,TIPACC,IPEQUI,NMEQUI,USRSYS, ESTAENV, TIPENV) VALUES("+dto.getCod()+","+dto.getCod2()+",'"+dto.getTabla()+"',"+dto.getFechaArchivo()+","+dto.getHoraArchivo()+",'"+dto.getNombreArchivo()+"','"+dto.getTipoAccion().trim()+"','"+dto.getIpEquipo()+"','"+dto.getNombreEquipo()+"','"+dto.getUsuario()+"',"+dto.getEstadoEnvio()+",'"+dto.getTipoEnvio()+"') " ;
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
	
	
	
	
}
