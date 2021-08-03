package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.DetlibDAO;
import cl.caserita.dto.DetlibDTO;
import cl.caserita.dto.GenlibDTO;

public class DetlibDAOImpl implements DetlibDAO{

	private static Logger log = Logger.getLogger(DetlibDAOImpl.class);
	private Connection conn;
	
	public DetlibDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int genDetlib(DetlibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DETLIB " + 
        " (CODLCNT,TPTC30,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,CODDLI,TPACO1,FOLDOC,IDEDOC,TSIMDET,IDSVPE,FCDODET,CDSUCSI,RUPROL,RAPROL,MNEXDT,MNNEDT,MNIVDT,IVFUPDT,MNTTDT) VALUES('"+gen.getCodLibro()+"',"+gen.getEmpresa()+",'"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+",'"+gen.getCodDetalleLibro()+"',"+gen.getCodDocumento()+","+gen.getFolioDocumento()+",'"+gen.getIdDocumentoAnulado()+"',"+gen.getTasaImpuestoDetalle()+","+gen.getIdServicioPeriodico()+",'"+gen.getFechaDocumento()+"',"+gen.getCodigoSucursalSII()+","+gen.getRutProveedor()+",'"+gen.getRazonSocial()+"',"+gen.getMontoExento()+","+gen.getMontoNetoDetalle()+","+gen.getMontoIvaDetalle()+","+gen.getMontoIvaFueraPlazo()+","+gen.getMontoTotal()+")";
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
	public int genDetlibCompras(DetlibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.DETLIB " + 
        " (CODLCNT,TPTC30,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,CODDLI,TPACO1,FOLDOC,IDEDOC,TSIMDET,IDSVPE,FCDODET,CDSUCSI,RUPROL,RAPROL,MNEXDT,MNNEDT,MNTACT,MNIVACT,MNIVDT,IVFUPDT,MNTTDT) VALUES('"+gen.getCodLibro()+"',"+gen.getEmpresa()+",'"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+",'"+gen.getCodDetalleLibro()+"',"+gen.getCodDocumento()+","+gen.getFolioDocumento()+",'"+gen.getIdDocumentoAnulado()+"',"+gen.getTasaImpuestoDetalle()+","+gen.getIdServicioPeriodico()+",'"+gen.getFechaDocumento()+"',"+gen.getCodigoSucursalSII()+","+gen.getRutProveedor()+",'"+gen.getRazonSocial()+"',"+gen.getMontoExento()+","+gen.getMontoNetoDetalle()+","+gen.getMontoNetoActivoFijo()+","+gen.getMontoIvaActivoFijo()+","+gen.getMontoIvaDetalle()+","+gen.getMontoIvaFueraPlazo()+","+gen.getMontoTotal()+")";
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
