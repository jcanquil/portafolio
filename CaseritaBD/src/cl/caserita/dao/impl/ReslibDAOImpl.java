package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ReslibDAO;
import cl.caserita.dto.ResimlibDTO;
import cl.caserita.dto.ReslibDTO;

public class ReslibDAOImpl implements ReslibDAO{

	private static Logger log = Logger.getLogger(ReslibDAOImpl.class);
	private Connection conn;
	
	public ReslibDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaResLib(ReslibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.RESLIB " + 
        " (CODLCNT,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,CODRES,TPACO1,CANTDOC,TOTDANU,TMNEXE,TMNNET,TOTMNIVA,TMNIVP,TMONTO) VALUES('"+gen.getCodLibro()+"','"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+",'"+gen.getCodResumen()+"',"+gen.getCodDocumento()+","+gen.getCantDocumento()+","+gen.getTotalDocumentoAnulado()+","+gen.getTotalMontoExento()+","+gen.getTotalMontoNeto()+","+gen.getTotalMontoIva()+","+gen.getTotalMontoIvaFueraPlazo()+","+gen.getTotalDocumento()+")";
		//System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
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
	public int generaResLibCompras(ReslibDTO gen){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.RESLIB " + 
        " (CODLCNT,TPTC30,ADSCO4,FCHGLIB,HRGNLIB,PEINGL,PEFNGL,CODRES,TPACO1,CANTDOC,TOTDANU,TMNEXE,TMNNET,TOTNACT,TOTIACT,TOTMNIVA,TMNIVP,TMONTO) VALUES('"+gen.getCodLibro()+"',"+gen.getEmpresa()+",'"+gen.getCodigoUsuario()+"',"+gen.getFechaGeneracion()+","+gen.getHoraGeneracion()+","+gen.getPeriodoInicial()+","+gen.getPeriodoFinal()+",'"+gen.getCodResumen()+"',"+gen.getCodDocumento()+","+gen.getCantDocumento()+","+gen.getTotalDocumentoAnulado()+","+gen.getTotalMontoExento()+","+gen.getTotalMontoNeto()+","+gen.getTotalNetoActivoFijo()+","+gen.getTotalIvaActivoFijo()+","+gen.getTotalMontoIva()+","+gen.getTotalMontoIvaFueraPlazo()+","+gen.getTotalDocumento()+")";
		//System.out.println("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		try{
			System.out.println("Genera Resimlib"+sqlObtenerVecmar);
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
