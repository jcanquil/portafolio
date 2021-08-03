package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.TpctraDAO;
import cl.caserita.dto.TpctraDTO;
import cl.caserita.dto.TptbdgDTO;

public class TpctraDAOImpl implements TpctraDAO{

	private static Logger log = Logger.getLogger(TpctraDAOImpl.class);
	private Connection conn;
	
	public TpctraDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List buscaAllTransportista(){
		TpctraDTO tpctra = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List lista = new ArrayList();
		String sqlObtenerVecmar="Select * "+
        " FROM CASEDAT.CHOFTRAN C1, CASEDAT.TPCTRA T1, CASEDAT.TPTREG T2, CASEDAT.TPTCTY T3, CASEDAT.TPTCOM T4 " + 
        " Where C1.TPCRUT= T1.TPCRUT AND C1.TPCDVT= T1.TPCDVT AND T1.TPCCOD=  T2.TPTCO1 AND T1.TPCCOD= T3.TPTCO6 AND T1.TPCCO1= T3.TPTCO7 AND "+
        " T1.TPCCOD= T4.TPTC19 AND T1.TPCCO1= T4.TPTC20 AND T1.TPCCO2=T4.TPTC22";
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tpctra = new TpctraDTO();
				tpctra.setRutChofer(rs.getInt("CAMRUT"));
				tpctra.setDvChofer(rs.getString("EXMDI3"));
				tpctra.setDireccion(rs.getString("TPCDIR"));
				tpctra.setCodRegion(rs.getInt("TPCCOD"));
				tpctra.setDescripcionRegion(rs.getString("TPTDE1"));
				tpctra.setCodCiudad(rs.getInt("TPCCO1"));
				tpctra.setDescripcionCiudad(rs.getString("TPTDE7"));
				tpctra.setCodComuna(rs.getInt("TPCCO2"));
				tpctra.setDescripcionComuna(rs.getString("TPTD20"));
				tpctra.setNombreEmpresa(rs.getString("TPCRAZ"));
				lista.add(tpctra);
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
		
		
		return lista;
	}
	
	public TpctraDTO buscaTransportista(int rut){
		TpctraDTO tpctra = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerVecmar="Select * "+
        " FROM CASEDAT.CHOFTRAN C1, CASEDAT.TPCTRA T1, CASEDAT.TPTREG T2, CASEDAT.TPTCTY T3, CASEDAT.TPTCOM T4 " + 
        " Where C1.CAMRUT= "+rut+"  AND T1.TPCCOD=  T2.TPTCO1 AND T1.TPCCOD= T3.TPTCO6 AND T1.TPCCO1= T3.TPTCO7 AND "+
        " T1.TPCCOD= T4.TPTC19 AND T1.TPCCO1= T4.TPTC20 AND T1.TPCCO2=T4.TPTC22";
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tpctra = new TpctraDTO();
				tpctra.setRutChofer(rs.getInt("CAMRUT"));
				tpctra.setDvChofer(rs.getString("EXMDI3"));
				tpctra.setDireccion(rs.getString("TPCDIR"));
				tpctra.setCodRegion(rs.getInt("TPCCOD"));
				tpctra.setDescripcionRegion(rs.getString("TPTDE1"));
				tpctra.setCodCiudad(rs.getInt("TPCCO1"));
				tpctra.setDescripcionCiudad(rs.getString("TPTDE7"));
				tpctra.setCodComuna(rs.getInt("TPCCO2"));
				tpctra.setDescripcionComuna(rs.getString("TPTD20"));
				tpctra.setNombreEmpresa(rs.getString("CAMNOM"));
				tpctra.setRutTransportista(rs.getInt("TPCRUT"));
				tpctra.setDvTransportista(rs.getString("TPCDVT").trim());
				
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
		
		
		return tpctra;
	}
	
}
