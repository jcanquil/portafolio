package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ExmrecvaDAO;
import cl.caserita.dto.ExdodcDTO;
import cl.caserita.dto.ExmrecvaDTO;

public class ExmrecvaDAOImpl implements ExmrecvaDAO {

	private static Logger log = Logger.getLogger(ExmrecvaDAOImpl.class);
	private Connection conn;
	
	public ExmrecvaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List listaExmrecva(int empresa, int numoc, int tipDocto, int numeroDocto, int fechaDocto, int codigoBodega, String tipo){
		ExmrecvaDTO exmrecva = null;
		PreparedStatement pstmt =null;
		
		ResultSet rs = null; 
		
		String sqlObtenerExmrecva=" SELECT * "+
				" FROM CASEDAT.EXMRECVA " + 
				" WHERE EXMEMP = "+empresa+
				" AND EXMOC = "+numoc+
				" AND EXMTIP = "+tipDocto+
				" AND EXMNUM = "+numeroDocto+
				" AND EXMFEDO = "+fechaDocto+
				" AND EXMBOD = "+codigoBodega+
				" AND EXMTIPO = '"+tipo+"'"+
				" FOR READ ONLY" ;
		
		log.info("SQL" + sqlObtenerExmrecva);
		
		List listadet = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerExmrecva);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				exmrecva = new ExmrecvaDTO();
				exmrecva.setCodigoEmpresa(rs.getInt("EXMEMP"));
				exmrecva.setNumeroOrden(rs.getInt("EXMOC"));
				exmrecva.setTipoDocto(rs.getInt("EXMTIP"));
				exmrecva.setNumeroDocto(rs.getInt("EXMNUM"));
				exmrecva.setFechaDocto(rs.getInt("EXMFEDO"));
				exmrecva.setCodigoBodega(rs.getInt("EXMBOD"));
				exmrecva.setRutProveedor(rs.getInt("EXMRUT"));
				exmrecva.setDigitoProveedor(rs.getString("EXMDVR"));
				exmrecva.setFechaValorizacion(rs.getInt("EXMFEV"));
				exmrecva.setHoraValorizacion(rs.getInt("EXMHOV"));
				exmrecva.setLinea(rs.getInt("EXMLIN"));
				exmrecva.setCodigoArticulo(rs.getInt("EXMCOD"));
				exmrecva.setDigitoArticulo(rs.getString("EXMDIG"));
				exmrecva.setEstadoInventario(rs.getString("EXMSIN"));
				exmrecva.setFormato(rs.getString("EXMFMT"));
				exmrecva.setCantidadSolicitada(rs.getDouble("EXMCANS"));
				exmrecva.setCantidadRecepcionada(rs.getDouble("EXMCANR"));
				exmrecva.setTotalFactura(rs.getInt("EXMTOTF"));
				exmrecva.setTotalValorizacion(rs.getInt("EXMTOTV"));
				exmrecva.setUsuario(rs.getString("EXMUSU"));
				exmrecva.setTipo(rs.getString("EXMTIPO"));
				
				listadet.add(exmrecva);
				
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
		return listadet;
	}
	
}
