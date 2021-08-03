package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ExdacpDAO;
import cl.caserita.dao.iface.VedmarDAO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.ExdacpDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.VedmarDTO;



public class ExdacpDAOImpl implements ExdacpDAO {
	
	private Connection conn;
	
	public ExdacpDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	
	public ExdacpDTO calculamontosCombo(int codigoEmpresa, int codCombo, String dvCombo, int codBodega, int codTven, int cantidev, int porcendescto){
		ExdacpDTO exdacpdto = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		DAOFactory dao = DAOFactory.getInstance();
		VedmarDAO vedmardao = dao.getVedmarDAO();
		
		String sqlObtenerExdacp = "SELECT p.exdco2, p.exddi2, p.exdcan, p.exipre, p.exipbn "+
		" FROM CASEDAT.EXDACP p" +
		" INNER JOIN CASEDAT.EXDACB b"+
		" ON p.EXDCOD=b.EXDCOD AND p.EXDDIG=b.EXDDIG AND p.EXDCO2=b.EXDCO2 AND p.exddi2=b.exddi2"+
		" WHERE p.AA12A="+codigoEmpresa+ ""+
		" AND p.camco1="+codBodega+" AND p.clmco6="+codTven+" AND p.exdcod="+codCombo+" AND p.exddig='"+dvCombo+"'"+
		" AND b.exdcob='C'" +
		" FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerExdacp);
			
			int 	canthijo=0;
			double 	netoline=0;
			double 	brutline=0;
			double 	totaneto=0;
			double 	totabrut=0;
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				long parteEntera;
				
				DecimalFormat formateadorVedmar = new DecimalFormat("#######.00");
			
				exdacpdto = new ExdacpDTO();
				
				canthijo=(cantidev*rs.getInt("EXDCAN"));
				netoline=(canthijo*rs.getDouble("EXIPBN"));
				String netolinea=formateadorVedmar.format(netoline);
				netolinea=netolinea.replace(",", ".");
				netoline = Double.parseDouble(netolinea);
				/*
				if (porcendescto>0){
					netoline = netoline - ((porcendescto / 100) * netoline);
				}
				int punton =(netolinea.indexOf("."))+1; 
				if (punton>0) {
					String decimale = netolinea.substring(punton,netolinea.length());
					String partedec = decimale.substring(0, 1);
					if (Integer.parseInt(partedec)>4) {
						parteEntera=(long)(Double.parseDouble(netolinea));
						if (netoline > parteEntera) {
							netoline = netoline + 1;
							netoline = Math.floor(netoline);
						}
					}
				}
				*/
				
				totaneto = totaneto + netoline;
				
				double  impuestos = vedmardao.calculaImpuestosArticulo(rs.getInt("EXDCO2"), rs.getString("EXDDI2"));
				if (impuestos>1.0) {
					brutline =(netoline*impuestos);
					String brutlinea=formateadorVedmar.format(brutline);
					brutlinea=brutlinea.replace(",", ".");
					brutline = Double.parseDouble(brutlinea);
					
					int punto =( brutlinea.indexOf("."))+1; 
					if (punto>0) {
						String decimale = brutlinea.substring(punto,brutlinea.length());
						String partedec = decimale.substring(0, 1);
						if (Integer.parseInt(partedec)>4) {
							parteEntera=(long)(Double.parseDouble(brutlinea));
							if (brutline > parteEntera) {
								brutline = brutline + 1;
								brutline = Math.floor(brutline);
							}
						}
					}
					
					totabrut = totabrut + brutline;
					totabrut = Math.round(totabrut);
					
				}		
						
			}
			
				if (totabrut<=0 && totaneto>0) {
					totabrut = totaneto;
				}
				exdacpdto.setPrecioNeto(totaneto);
				exdacpdto.setPrecioVenta(totabrut);
			
		
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
		
		return exdacpdto;
		
		
	}


}
