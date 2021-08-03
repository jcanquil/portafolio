package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import cl.caserita.dao.iface.RemcrhDAO;
import cl.caserita.dto.RemcrhDTO;
import cl.caserita.dto.RrhmdrDTO;


public class RemcrhDAOImpl implements RemcrhDAO{

	private Connection conn;
	
	public RemcrhDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtieneHaberes(int periodo){
		List haberes = new ArrayList();
		RemcrhDTO remcrh=null;
		PreparedStatement pr = null;
		ResultSet rs = null;
		String sqlRemcrh ="SELECT * FROM OMCADAT.REMCRH WHERE REMP16="+periodo+"";
		try{
			pr = conn.prepareStatement(sqlRemcrh);
			rs = pr.executeQuery();
			while (rs.next()){
				remcrh = new RemcrhDTO();
				remcrh.setPeriodo(rs.getInt("REMP16"));
				remcrh.setRutTrabajador(rs.getInt("RE0101"));
				remcrh.setDvTrabajador(rs.getString("RE0102"));
				remcrh.setCodHaber(rs.getInt("RE0103"));
				remcrh.setDescHaber(rs.getString("RE0104"));
				remcrh.setMontoHaber(rs.getLong("RE0105"));
				remcrh.setCantidad(rs.getInt("RE0106"));
				remcrh.setDescripcionCentroCosto(obtieneCentroCosto(remcrh.getRutTrabajador(), remcrh.getDvTrabajador()));
				remcrh.setNombreTrabajador(obtieneNombreTrabajador(remcrh.getRutTrabajador(), remcrh.getDvTrabajador()));
				haberes.add(remcrh);
				
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
		
		
		
		return haberes;
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
	
	public String obtieneNombreTrabajador(int rut, String dv){
		String nombreTrabajador="";
		PreparedStatement pr = null;
		ResultSet rs =null;
		String sql = "SELECT * FROM OMCADAT.RRHMIT WHERE RRHRUT="+rut+" AND RRHDVD='"+dv+"'";
		
		try{
			pr = conn.prepareStatement(sql);
			rs = pr.executeQuery();
			while (rs.next()){
				nombreTrabajador = rs.getString("RRHNOM") +" " + rs.getString("RRHAPE")+" "+rs.getString("RRHAP1");
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
		
		
		return nombreTrabajador;
	}
	
	
}
