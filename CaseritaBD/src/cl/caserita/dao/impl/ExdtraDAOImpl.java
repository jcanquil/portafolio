package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExdtraDAO;
import cl.caserita.dao.iface.ExmtraDAO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ClmcliDTO;
import cl.caserita.dto.ExdrecDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.PrecioDescDTO;
import cl.caserita.dto.TptbdgDTO;
import cl.caserita.dto.VecmarDTO;

public class ExdtraDAOImpl implements ExdtraDAO{

	private static Logger log = Logger.getLogger(ExdtraDAOImpl.class);
	private Connection conn;
	
	public ExdtraDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List recuperaDetalle(int empresa, int numTraspaso){
		ExdtraDTO exdtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXDTRA " + 
        " Where EXIEMP="+empresa+" AND EXINU2="+numTraspaso+" FOR READ ONLY" ;
		List exdtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdtraDTO = new ExdtraDTO();
				exdtraDTO.setNumTraspaso(rs.getInt("EXINU2"));
				exdtraDTO.setLinea(rs.getInt("EXILIN"));
				exdtraDTO.setCodBarra(rs.getString("EXICO6"));
				exdtraDTO.setFormato(rs.getString("EXIFOR"));
				
				exdtraDTO.setPesoArticulo(rs.getDouble("EXIPE1"));
				exdtraDTO.setVolumenArticulo(rs.getDouble("EXIVO1"));
				exdtraDTO.setCantDespachada(rs.getDouble("EXICA2"));
				exdtraDTO.setCantRecibida(rs.getDouble("EXICA1"));
				exdtraDTO.setCantPendiente(rs.getDouble("EXICA3"));
				exdtraDTO.setCantUnitariaRecepcionada(rs.getDouble("EXICA2"));
				exdtraDTO.setFechaRecepcion(rs.getInt("EXIFE1"));
				exdtraDTO.setCodArticulo(rs.getInt("EXICO1"));
				exdtraDTO.setDigitoVerificador(rs.getString("EXIDIG"));
				exdtraDTO.setValorUnitario(calculaPrecio(exdtraDTO.getCodArticulo(),exdtraDTO.getDigitoVerificador(),rs.getDouble("EXIVAL")));
				exdtraDTO.setCodigoJefeLocal(rs.getInt("EXICO2"));
				exdtraDTO.setDescArticulo(obtieneDescripcion(exdtraDTO.getCodArticulo(),exdtraDTO.getDigitoVerificador()));
				exdtra.add(exdtraDTO);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		return exdtra;
	}
	
	public String obtieneDescripcion(int codigo, String dv){
		String des="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMART " + 
        " Where EXMCOD="+codigo+" AND EXMDIG='"+dv+"'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				des = rs.getString("EXMDES");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		
		
		return des;
	}
	public double calculaPrecio(int articulo, String dv, double valorUni){
		ExtariDTO extari=null;
		double impuesto=0;
		double tasa=0;
		PreparedStatement pstmt =null;
		List extariList = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXTARI E, CASEDAT.TPTIMP T " + 
        " WHERE E.EXTCO1="+articulo+" AND E.EXTDI2='"+dv+"' AND E.EXTCO2= T.TPTC32 FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				extari = new ExtariDTO();
					tasa = tasa + rs.getDouble("TPTVA4");	
			}
			tasa = tasa / 100 + 1;
			impuesto = valorUni / tasa ;
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { 
					 rs.close();
					 pstmt.close();
					 }

			} catch (SQLException e1) { }

	  } 
		
		
		return impuesto;
	}
	
	public int actualizaEstado(ExdtraDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="UPDATE CASEDAT.EXDTRA SET exdetr ='"+dto.getEstado()+"'"+
				" WHERE EXIEMP = "+dto.getCodEmpresa()+
				" AND EXINU2 = "+dto.getNumTraspaso();
		
		System.out.println("ACTUALIZA ESTADO TRASPASO" + sqlObtenerVecmar);
		
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
	
	public int actualizaUnidadesOT(ExdtraDTO dto){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		//llevo todo a unidad solo WMS
		String sqlObtenerExdtra ="UPDATE "+
        " CASEDAT.EXDTRA " + 
        " SET EXIFOR = '"+dto.getFormato()+"',"+
        " EXICAN = "+dto.getCantDespachada()+","+
        " EXDCA2 = "+dto.getCantPendiente()+","+
        " EXDCNR = "+dto.getCantRecepCarguio()+
        " WHERE EXIEMP = "+dto.getCodEmpresa()+
        " AND EXINU2 = "+dto.getNumTraspaso()+
        " AND EXICO1 = "+dto.getCodArticulo();
		
		System.out.println("SQL ACTUALIZA EXDTRA "+ sqlObtenerExdtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdtra);
			pstmt.executeUpdate();
			actualiza=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				pstmt.close();
				 
			} catch (SQLException e1) { }
	  } 
		pstmt=null;
		
		return actualiza;
	}
	
	public ExdtraDTO buscaOTCarguio(ExdtraDTO dto){
		ExdtraDTO exdtraDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
			" FROM CASEDAT.EXDTRA " + 
		        " WHERE EXIEMP = "+dto.getCodEmpresa()+
		        " AND EXINU2 = "+dto.getNumTraspaso() +
		        " AND EXICO1 = "+dto.getCodArticulo()+
		        " FOR READ ONLY" ;
		System.out.println("SQL BUSCA OT DEL CARGUIO "+ sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
				exdtraDTO = new ExdtraDTO();
				
				exdtraDTO.setCantDespachada(rs.getDouble("EXICAN"));
				exdtraDTO.setCantUnitariaDespachada(rs.getDouble("EXICA2"));
				exdtraDTO.setCantPendiente(rs.getDouble("EXDCA2"));
				exdtraDTO.setFormato(rs.getString("EXIFOR"));
				exdtraDTO.setLinea(rs.getInt("EXILIN"));
				exdtraDTO.setCodBarra(rs.getString("EXICO6"));
				exdtraDTO.setDigitoVerificador(rs.getString("EXIDIG"));
				exdtraDTO.setValorUnitario(rs.getDouble("EXIVAL"));
				exdtraDTO.setVolumenArticulo(rs.getDouble("EXIVO1"));
				exdtraDTO.setPesoArticulo(rs.getDouble("EXIPE1"));
				
				
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
		return exdtraDTO;
		
	}
	
}
