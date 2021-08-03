package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.NotCorreDAO;
import cl.caserita.dao.iface.PrdatcaDAO;
import cl.caserita.dto.PrdatcaDTO;
import cl.caserita.dto.TptempDTO;

public class PrdatcaDAOImpl implements PrdatcaDAO{

	private static Logger log = Logger.getLogger(PrdatcaDAOImpl.class);
	private Connection conn;
	
	public PrdatcaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public PrdatcaDTO obtieneDatosDocumento(int empresa, int cod, int fecha, int numero){
		PrdatcaDTO prdat = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerVecmar="Select * "+
        " from CASEDAT.DATVTCARG " + 
        " Where VENEMP="+empresa+" AND VENCOD="+cod+" AND VENFEC="+fecha+" AND VENNUM="+numero+" FOR READ ONLY" ;
		    
		try{
			pstmt = conn.prepareStatement(sqlObtenerVecmar);
			//pstmt.setString(1, origen);
		
			
			//log.info("SQL CLIENTE" + sqlObtenerVecmar);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				prdat = new PrdatcaDTO();
				prdat.setFormaPago(rs.getString("FORPAG").trim());
				prdat.setCondPago(rs.getString("CONDPAGO").trim());
				prdat.setNumCheque(rs.getString("NUMCHE").trim());
				prdat.setNumCtaCte(rs.getString("NUMCTTE").trim());
				prdat.setBanco(rs.getString("DESCBAN").trim());
				prdat.setNumCarguio(rs.getInt("NUMCAR"));
				prdat.setNumOV(rs.getInt("NUMVEN"));
				prdat.setPlazo(rs.getInt("CLMDIA"));
				prdat.setPatente(rs.getString("VECPAT").trim());
				prdat.setChofer(rs.getString("NOMCLIE").trim());
				prdat.setContacto(rs.getString("CLICON").trim());
				prdat.setTelefono(rs.getString("CLITEL").trim());
				prdat.setDireccion(rs.getString("CLIDI1").trim());
				prdat.setDeptoOficina(rs.getString("DEPOFICI").trim());
				prdat.setPoblacionVilla(rs.getString("POBLVILL").trim());
				prdat.setComuna(rs.getString("DESCOMU").trim());
				prdat.setCiudad(rs.getString("DESCIUD").trim());
				prdat.setNumeroDomicilio(rs.getString("CLINUM").trim());
				prdat.setFechaVencimiento(rs.getInt("CAMFE2"));
				prdat.setDescripcionBodega(rs.getString("TPTD18"));
				prdat.setNumeroOficinaPri(rs.getString("NUOFPR"));
				prdat.setDeptoOficinaPri(rs.getString("DEOFPR"));
				prdat.setHora(rs.getString("HORCHA"));
				prdat.setNombreVendedor(rs.getString("EXMNO1"));
				/*prdat.setNumeroOrden(rs.getString("NUMOCV"));
				prdat.setFechaOrden(rs.getString("FCHOCV"));*/
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
		
		
		return prdat;
	}
}
