package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.GeovtaveDAO;
import cl.caserita.dto.GenlibDTO;
import cl.caserita.dto.GeovtaveDTO;

public class GeovtaveDAOImpl implements GeovtaveDAO {

	private static Logger log = Logger.getLogger(GeovtaveDAOImpl.class);
	private Connection conn;
	
	public GeovtaveDAOImpl(Connection conn){
		this.conn=conn;
	}
	public int generaGeovtave(GeovtaveDTO dto){
		int res=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerVecmar="INSERT INTO"+
        "  CASEDAT.GEOVTAVE " + 
        " (CODEGE, CODBGE, TIPMGE, FCHRGE, HORRGE, CODSUP, CAMCOD, CLCCO2, CAMNFA, CLCRU1, CLDDI4, CAMNOM, NOMSUP, NOMVND, LATDIR,LONDIR, CLICOR, CLIDI1, "
        + "CLINUM, CLITEL, CLITE1, TPTCO1, TPTDE1,  TPTCO7, TPTDE7, TPTC22, TPTD20, EXMC10, TPTD03, CODANT, DSCANT,  CNTART, CLCTNT, CLCTOT, NUMVEN, CCOEA, "
        + "CCOFA, CODRNV, DSCRNV) "
        + "VALUES("+dto.getCodigoEmpresa()+","+dto.getCodigoBodega()+",'"+dto.getTipoMovimiento()+"',"+dto.getFechaRegistro()+","+dto.getHoraRegistro()+","
        + ""+dto.getCodigoSupervisor()+","+dto.getCodigoVendedor()+","+dto.getCodigoDocumento()+","+dto.getNumeroDocumento()+","+dto.getRutCliente()+","
        + "'"+dto.getDigitoCliente()+"','"+dto.getNombreCliente()+"','"+dto.getNombreSupervisor()+"','"+dto.getNombreVendedor()+"','"+dto.getLatitud()+"',"
        + "'"+dto.getLongitud()+"',"+dto.getCorrelativoDireccion()+",'"+dto.getDireccionCliente()+"',"+dto.getNumeroDireccion()+",'"+dto.getTelefono()+"',"
        + "'"+dto.getCelular()+"',"+dto.getCodigoRegion()+",'"+dto.getDescripcionRegion()+"',"+dto.getCodigoCiudad()+",'"+dto.getDescripcionCiudad()+"',"
        + ""+dto.getCodigoComuna()+",'"+dto.getDescripcionComuna()+"',"+dto.getCodigoTipoVendedor()+",'"+dto.getDescripcionTipoVendedor()+"','"+dto.getCodigoAntena()+"',"
        + "'"+dto.getDescripcionAntena()+"',"+dto.getCantidadArticulos()+","+dto.getTotalNeto()+","+dto.getTotalDocumento()+","+dto.getNumeroOrdenVenta()+","
        + ""+dto.getCodigoTipoNegocio()+",'"+dto.getDescripcionTipoNegocio()+"','"+dto.getCodigoRazonNoVenta()+"','"+dto.getDescripcionRazonNoVenta()+"')";
		//log.info("INSERTA CORRELATIVO" + sqlObtenerVecmar);
		System.out.println("SQL : "+sqlObtenerVecmar);
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
