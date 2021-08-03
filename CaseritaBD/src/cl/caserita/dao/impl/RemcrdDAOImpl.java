package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cl.caserita.dao.iface.RemcrdDAO;
import cl.caserita.dto.RemcrdDTO;
import cl.caserita.dto.RrhmdrDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
	public class RemcrdDAOImpl implements RemcrdDAO{

	private Connection conn;
	private  static Logger log = Logger.getLogger(RemcrdDAOImpl.class);

	public RemcrdDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtieneDescuentos(int periodo){
		List descuentos = new ArrayList();
		RemcrdDTO remcrd=null;
		PreparedStatement pre =null;
		ResultSet rs =null;
		String sqlDescuentos ="SELECT * FROM OMCADAT.REMCRD WHERE REMP17="+periodo+"";
		try{
			pre = conn.prepareStatement(sqlDescuentos);
			rs = pre.executeQuery();
			while (rs.next()){
				remcrd = new RemcrdDTO();
				remcrd.setPeriodo(rs.getInt("REMP17"));
				remcrd.setRutTrabajador(rs.getInt("RE0111"));
				remcrd.setDigTrabajador(rs.getString("RE0112"));
				remcrd.setCodDescuento(rs.getInt("RE0107"));
				remcrd.setDescripcionDescuento(rs.getString("RE0108"));
				remcrd.setMontoDescuento(rs.getLong("RE0109"));
				remcrd.setCantidad(rs.getInt("RE0110"));
				remcrd.setDescripcionCCosto(obtieneCentroCosto(remcrd.getRutTrabajador(), remcrd.getDigTrabajador()));
				remcrd.setNombreTrabajador(obtieneNombre(remcrd.getRutTrabajador(), remcrd.getDigTrabajador()));
				descuentos.add(remcrd);
	
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs!=null){
					rs.close();
					pre.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return descuentos;
	}
	public String obtieneCentroCosto(int rut, String dv){
		RrhmdrDTO rrh =null;
		String descripcion="";
		PreparedStatement pre = null;
		ResultSet rs =null;
		String sqlRRH = "SELECT * FROM OMCADAT.RRHMDR R , OMCADAT.TPUTCC T WHERE R.RRHRU4 ="+rut+" AND R.RRHDV3='"+dv+"'" +
				" AND R.RRHC04=T.TPUC08";
		try{
			pre = conn.prepareStatement(sqlRRH);
			rs = pre.executeQuery();
			while (rs.next()){
				rrh = new RrhmdrDTO();
				rrh.setCodigoCCosto(rs.getInt("TPUC08"));
				rrh.setDescripcionCentroCosto(rs.getString("TPUNO4"));
				descripcion=rs.getString("TPUNO4");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs!=null){
					rs.close();
					pre.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return descripcion;
	}
	
	public String obtieneNombre(int rut, String dv){
		String nombreCompleto="";
		PreparedStatement pr =null;
		ResultSet rs = null;
		String sql ="SELECT * FROM OMCADAT.RRHMIT WHERE RRHRUT="+rut+" AND RRHDVD='"+dv+"'";
		
		try{
			pr = conn.prepareStatement(sql);
			rs = pr.executeQuery();
			while (rs.next()){
				nombreCompleto = rs.getString("RRHNOM")+" " + rs.getString("RRHAPE") + " " + rs.getString("RRHAP1");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs!=null){
					rs.close();
					pr.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return nombreCompleto;
	}
}
