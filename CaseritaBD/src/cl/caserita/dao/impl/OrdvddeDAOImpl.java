package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.caserita.dao.iface.OrdvddeDAO;
import cl.caserita.dto.DetordTranspDTO;
import cl.caserita.dto.OrdvddeDTO;

public class OrdvddeDAOImpl implements OrdvddeDAO{
	
	private Connection conn;
	
	public OrdvddeDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public int buscaOrdvdde(OrdvddeDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT NUMVEN FROM CASEDAT.ORDVDDE "+
			" WHERE ORDEMP="+orden.getEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+ " AND CLMRUT="+orden.getRutCliente()+ " AND CLMDIG='"+orden.getDigCliente()+"'"+
			" AND TPTC18="+orden.getCodigoBodega()+ " FOR READ ONLY " ;
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
	
	
	
	public int insertaOrdvdde(OrdvddeDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="INSERT INTO CASEDAT.ORDVDDE " + 
        " (ORDEMP, NUMVEN, CLMRUT, CLMDIG, TPTC18, "+
		" DETDESP, TIPDEO, FCHCOF, LATDIR, LONDIR, DISDES, "+
        " CODEDE, DESEDE, CODMDE, DESMDE, "+
		" URLFOT, COMDES, URLFO2, COMDE2, URLFO3, COMDE3, URLFO4, COMDE4, "+
        " URLFO5, COMDE5, URLFO6, COMDE6, FCHUSR, HORUSR) " +
		" VALUES("+orden.getEmpresa()+","+orden.getNumeroOV()+","+orden.getRutCliente()+",'"+orden.getDigCliente()+"',"+orden.getCodigoBodega()+","
        +orden.getDetalledespacho()+","+orden.getTipoDespacho()+","+orden.getFechaConfirmacion()+",'"+orden.getLatitud()+"','"+orden.getLongitud()+"','"+orden.getDistancia()+"','"
		+orden.getCodEstado()+"','"+orden.getDesEstado()+"','"+orden.getCodMotivo()+"','"+orden.getDesMotivo()+"','"
        +orden.getFoto1()+"','"+orden.getComentario1()+"','"+orden.getFoto2()+"','"+orden.getComentario2()+"','"+orden.getFoto3()+"','"+orden.getComentario3()+"','"
        +orden.getFoto4()+"','"+orden.getComentario4()+"','"+orden.getFoto5()+"','"+orden.getComentario5()+"','"+orden.getFoto6()+"','"+orden.getComentario6()+"',"
        +orden.getFechauser()+","+orden.getHorauser()+") " ;
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			System.out.println("INSERTA EN TABLA ordvDDE  OV : " + orden.getNumeroOV());
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
	
	
	
	
	public int actualizaOrdvdde(OrdvddeDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="UPDATE  "+
        "  CASEDAT.ORDVDDE " + 
        " SET TIPDEO="+orden.getTipoDespacho()+", DETDESP="+orden.getDetalledespacho()+", "+
        " FCHCOF="+orden.getFechaConfirmacion()+", LATDIR='"+orden.getLatitud()+"', LONDIR='"+orden.getLongitud()+"',"+
        " DISDES='"+orden.getDistancia()+"', CODEDE='"+orden.getCodEstado()+"', DESEDE='"+orden.getDesEstado()+"', CODMDE='"+orden.getCodMotivo()+"', DESMDE='"+orden.getDesMotivo()+"',"+
        " URLFOT='"+orden.getFoto1()+"', COMDES='"+orden.getComentario1()+"', URLFO2='"+orden.getFoto2()+"', COMDE2='"+orden.getComentario2()+"',"+
        " URLFO3='"+orden.getFoto3()+"', COMDE3='"+orden.getComentario3()+"', URLFO4='"+orden.getFoto4()+"', COMDE4='"+orden.getComentario4()+"',"+
        " URLFO5='"+orden.getFoto5()+"', COMDE5='"+orden.getComentario5()+"', URLFO6='"+orden.getFoto6()+"', COMDE6='"+orden.getComentario6()+"',"+
        " FCHUSR="+orden.getFechauser()+", HORUSR="+orden.getHorauser()+" "+
       	" WHERE ORDEMP="+orden.getEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+ 
       	" AND CLMRUT="+orden.getRutCliente()+ " AND CLMDIG='"+orden.getDigCliente()+"'"+
		" AND TPTC18="+orden.getCodigoBodega()+ " " ;
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			//System.out.println("update ordvdde" + sqlObtener);
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

	
	public int actualizaOrdvddeEstado(OrdvddeDTO orden){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtener ="UPDATE  "+
        "  CASEDAT.ORDVDDE " + 
        " SET CODEDE='"+orden.getCodEstado()+"', DESEDE='"+orden.getDesEstado()+"',"+
        " CODMDE='"+orden.getCodMotivo()+"', DESMDE='"+orden.getDesMotivo()+"',"+
        " FCHUSR="+orden.getFechauser()+", HORUSR="+orden.getHorauser()+", "+
        " TIPDEO="+orden.getTipoDespacho()+" "+
       	" WHERE ORDEMP="+orden.getEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+ 
       	" AND CLMRUT="+orden.getRutCliente()+ " AND CLMDIG='"+orden.getDigCliente()+"'"+
		" AND TPTC18="+orden.getCodigoBodega()+ " " ;
		try{
			pstmt = conn.prepareStatement(sqlObtener);
			pstmt.executeUpdate();
			correlativo=1;
			//System.out.println("update ordvdde" + sqlObtener);
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


	
	
	
	public List buscaOrdvddeDatos(int codigoEmpresa, int numeroOV, int rutCli, String digCli, int codigoBodega, int tipodespacho){
		List ordvdde = new ArrayList();
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT LATDIR, LONDIR, DISDES, CODEDE, DESEDE, CODMDE, DESMDE, "+
			" URLFOT, COMDES, URLFO2, COMDE2, URLFO3, COMDE3, URLFO4, COMDE4, URLFO5, COMDE5, URLFO6, COMDE6 "+
			" FROM CASEDAT.ORDVDDE "+
			" WHERE ORDEMP="+codigoEmpresa+" AND NUMVEN="+numeroOV+ " AND CLMRUT="+rutCli+ 
			" AND CLMDIG='"+digCli+"'"+
			" AND TPTC18="+codigoBodega+
			" AND TIPDEO="+tipodespacho+ " FOR READ ONLY " ;
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrdvddeDTO ordvddedat = new OrdvddeDTO();

				ordvddedat.setLatitud(rs.getString("LATDIR"));
				ordvddedat.setLongitud(rs.getString("LONDIR"));
				ordvddedat.setDistancia(rs.getString("DISDES"));
				ordvddedat.setCodEstado(rs.getString("CODEDE"));
				ordvddedat.setDesEstado(rs.getString("DESEDE"));
				ordvddedat.setCodMotivo(rs.getString("CODMDE"));
				ordvddedat.setDesMotivo(rs.getString("DESMDE"));
				ordvddedat.setFoto1(rs.getString("URLFOT"));
				ordvddedat.setComentario1(rs.getString("COMDES"));
				ordvddedat.setFoto2(rs.getString("URLFO2"));
				ordvddedat.setComentario2(rs.getString("COMDE2"));
				ordvddedat.setFoto3(rs.getString("URLFO3"));
				ordvddedat.setComentario3(rs.getString("COMDE3"));
				ordvddedat.setFoto4(rs.getString("URLFO4"));
				ordvddedat.setComentario4(rs.getString("COMDE4"));
				ordvddedat.setFoto5(rs.getString("URLFO5"));
				ordvddedat.setComentario5(rs.getString("COMDE5"));
				ordvddedat.setFoto6(rs.getString("URLFO6"));
				ordvddedat.setComentario6(rs.getString("COMDE6"));
				ordvdde.add(ordvddedat);

				
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

			return ordvdde;
		
	}

	

}
