package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.FactudevDAO;
import cl.caserita.dto.FaccarguDTO;
import cl.caserita.dto.FactudevDTO;

public class FactudevDAOImpl implements FactudevDAO{

	private static Logger log = Logger.getLogger(FactudevDAOImpl.class);

	private Connection conn;
		
		public FactudevDAOImpl(Connection conn){
			this.conn=conn;
		}
		
		
		public int generaMovimiento(FactudevDTO dto){
			int res=0;
			PreparedStatement pstmt =null;
			
			String sqlObtenerVecmar="INSERT INTO"+
	        "  CASEDAT.FACTUDEV " + 
	        " (CAMC12, CAMRUT,CAMDVR,CAMNUM,CAMFEC,CCWHA, CCWIA,CCWJA, CCWKA, CCZ2A, CAMMON,CAMCO1,NUMCAR,NUMVEN,CAMNFA,CLCNUM,CONNU2,CONNU3, FCHUSR,HORUSR,IPEQUI,NMEQUI,USRSYS,CONFE2) "
	        + "VALUES("+dto.getCorrelativo()+","+dto.getRutCliente()+",'"+dto.getDvCliente()+"',"+dto.getNumeroDocumento()+",'"+dto.getFechaDocumento()+"', "
	        		+ ""+dto.getNumeroDocumentoNuevo()+","+dto.getFechaDocumentoNuevo()+",'"+dto.getNumeroDocumentoDevolucion()+"',"+dto.getFechaDocumentoDevolucion()+","
	        + "'"+dto.getTipoPago()+"',"+dto.getMonto()+","+dto.getEstado()+","+dto.getCodigoBodega()+","+dto.getNumeroCarguio()+","
	        + ""+dto.getNumeroOrdenVenta()+","+dto.getNumeroFacturaBoleta()+","+dto.getNumeroDocumento()+","+dto.getNumeroNC1()+","
	        + ""+dto.getNumeroNC2()+","+dto.getFechaUsuario()+","+dto.getHoraUsuario()+","+dto.getIpEquipo()+","+dto.getNombreEquipo()+","+dto.getUsuario()+","+dto.getFechaEmision()+") " ;
			log.info("INSERTA FACTUDEV" + sqlObtenerVecmar);
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
