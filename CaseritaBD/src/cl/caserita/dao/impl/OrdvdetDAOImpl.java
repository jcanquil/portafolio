package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.caserita.dao.iface.OrdvdetDAO;
import cl.caserita.dto.OrdvddeDTO;
import cl.caserita.dto.OrdvdetDTO;

public class OrdvdetDAOImpl implements OrdvdetDAO{
	
	private Connection conn;
	
	public OrdvdetDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	
	public int insertaOrdvdet(OrdvdetDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="INSERT INTO CASEDAT.ORDVDET " + 
        " (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18, DETDESP, TIPDEO, "+
        " CORDOV, CLDCOD, VENFOR, CLDCAN, VEDCA4, FCHUSR, HORUSR, USRSYS) " +
		" VALUES("+orden.getEmpresa()+","+orden.getNumeroOV()+","+orden.getRutCliente()+",'"+orden.getDigCliente()+"',"
        +orden.getCodigoBodega()+","+orden.getDetalledespacho()+","+orden.getTipoDespacho()+","
		+orden.getCorrelativo() +","+orden.getCodigoArticulo()+",'"+orden.getFormatoArticulo()+"',"
        +orden.getCantidadArticulo()+","+orden.getCantidadRecepcionada()+","
        +orden.getFechauser()+","+orden.getHorauser()+",'"+orden.getUsuario()+"') " ;
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			System.out.println("INSERTA EN TABLA ordvdet  OV : " + orden.getNumeroOV());
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
	
	
	public int buscaOrdvdet(OrdvdetDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT NUMVEN FROM CASEDAT.ORDVDET "+
			" WHERE ORDEMP="+orden.getEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+ " AND CLMRUT="+orden.getRutCliente()+
			" AND CLMDIG='"+orden.getDigCliente()+"' AND TPTC18="+orden.getCodigoBodega()+
			" AND CORDOV="+orden.getCorrelativo()+
			" AND CLDCOD="+orden.getCodigoArticulo()+" FOR READ ONLY " ;
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=1;
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
	
	
	
	public int actualizaOrdvdet(OrdvdetDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="UPDATE  "+
        "  CASEDAT.ORDVDET " + 
        " SET TIPDEO="+orden.getTipoDespacho()+", DETDESP="+orden.getDetalledespacho()+","+
        " VENFOR='"+orden.getFormatoArticulo()+"', CLDCAN="+orden.getCantidadArticulo()+","+
        " VEDCA4="+orden.getCantidadRecepcionada()+","+
        " FCHUSR="+orden.getFechauser()+", HORUSR="+orden.getHorauser()+" "+
       	" WHERE ORDEMP="+orden.getEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+
       	" AND CLMRUT="+orden.getRutCliente()+ " AND CLMDIG='"+orden.getDigCliente()+"'"+
		" AND TPTC18="+orden.getCodigoBodega()+
		" AND CORDOV="+orden.getCorrelativo()+ " AND CLDCOD="+orden.getCodigoArticulo()+ " ";
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
	
	
	
	
	
	
	public List buscaOrdvdetDatos(int codigoEmpresa, int numeroOV, int rutCli, String digCli, int codigoBodega,  int tipodespacho){
		List ordvdet = new ArrayList();
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT CORDOV, CLDCOD, VENFOR, CLDCAN, VEDCA4"+
			" FROM CASEDAT.ORDVDET "+
			" WHERE ORDEMP="+codigoEmpresa+" AND NUMVEN="+numeroOV+ " AND CLMRUT="+rutCli+ 
			" AND CLMDIG='"+digCli+"'"+
			" AND TPTC18="+codigoBodega+
			" AND TIPDEO="+tipodespacho+ " FOR READ ONLY " ;
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrdvdetDTO ordvdetdat = new OrdvdetDTO();

				ordvdetdat.setCorrelativo(rs.getInt("CORDOV"));
				ordvdetdat.setCodigoArticulo(rs.getInt("CLDCOD"));
				ordvdetdat.setFormatoArticulo(rs.getString("VENFOR"));
				ordvdetdat.setCantidadArticulo(rs.getInt("CLDCAN"));
				ordvdetdat.setCantidadRecepcionada(rs.getInt("VEDCA4"));
				ordvdet.add(ordvdetdat);
				
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

			return ordvdet;
		
	}

	
	

	

}
