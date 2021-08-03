package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ImpauditDAO;
import cl.caserita.dto.ImpauditDTO;

public class ImpauditDAOImpl implements ImpauditDAO {
	private static Logger log = Logger.getLogger(ImpauditDAOImpl.class);

	private Connection conn;
	
	public ImpauditDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public ImpauditDTO buscaColaImpresionAudit(int codemp, int codbod){
		ImpauditDTO impauditDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerImpaudit=" SELECT * "+
				" FROM CASEDAT.IMPAUDIT " + 
				" WHERE aa12a = "+codemp+
				" AND camco1 ="+codbod + " FOR READ ONLY ";
		
		List camtra = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerImpaudit);
			
			log.info("SQL" + sqlObtenerImpaudit);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				impauditDTO = new ImpauditDTO();
				impauditDTO.setCodemp(rs.getInt("AA12A"));
				impauditDTO.setCodbod(rs.getInt("CAMCO1"));
				impauditDTO.setColaImp(rs.getString("TPTC16"));
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
		return impauditDTO;
	}
	
}
