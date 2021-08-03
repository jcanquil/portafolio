package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ClienteCanastaDAO;
import cl.caserita.dto.ActecoDTO;
import cl.caserita.dto.ClienteCanastaDTO;

public class ClienteCanastaDAOImpl  implements ClienteCanastaDAO{

	private static Logger log = Logger.getLogger(ClienteCanastaDAOImpl.class);
	private Connection conn;
	
	public ClienteCanastaDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtieneClienteCanasta(){
		ClienteCanastaDTO clienteCanasta = null;
		int usuarioRetorno=0;
		PreparedStatement pstmt =null;
		List clientes = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.PROYCLI " ;
		List actecoList = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			//log.info("SQL" + sqlObtenerCamtra);
						
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clienteCanasta = new ClienteCanastaDTO();
				clienteCanasta.setRutCliente(rs.getInt("CLCRU1"));
				clienteCanasta.setDvCliente(rs.getString("CLCDVR"));
				clienteCanasta.setNombreCliente(rs.getString("CAMNOM"));
				clientes.add(clienteCanasta);
				
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
		
		return clientes;
		
	}
}
