package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.InfogastoDAO;
import cl.caserita.dto.InfogastoDTO;

	public class InfogastoDAOImpl implements InfogastoDAO{
		private static Logger log = Logger.getLogger(InfogastoDAOImpl.class);

	private Connection conn;
	public InfogastoDAOImpl(Connection conn){
		this.conn=conn;
	}
	

	public int insertaGastoRRHH(InfogastoDTO info){
		int numero=0;
		PreparedStatement pr = null;
		String sqlInserta ="INSERT INTO CASEDAT.INFGASTO (OBF00B,AAGLA,AAGMA,AAGNA,CAMRUT,EXMDI2,TPTRAZ,CAMCO1,TPTD18,CLCNUM,CONFOL,AAGOA ,CAMFEC,AAGPA,AAGQA,CONVAL,TPTVA4,AAG0A,CONDE4,AAGRA,AAGSA,AAGTA,AAGVA,AAGWA,AAH5A,AAH6A,AAH7A )" +
				" VALUES("+info.getTransaccion()+",'"+info.getCodigoInforme()+"',"+info.getFechaGeneracion()+","+info.getHoraGeneracion()+","+info.getRut()+",'"+info.getDv()+"','"+info.getRazonSocial().trim()+"',"+info.getCodigoBodega()+",'"+info.getDescripcionBodega().trim()+"',"+info.getNumeroDocumento()+","+info.getFolio()+",'"+info.getTipoDocumento()+"',"+info.getFechaDocumento()+","+info.getAno()+","+info.getMes()+","+info.getValorNeto()+","+info.getValorImpto()+","+info.getValorTotal()+",'"+info.getDescripcion1().trim()+"','"+info.getDescripcion2().trim()+"','"+info.getDescripcion3().trim()+"','"+info.getDescripcion4().trim()+"','"+info.getCodigo1().trim()+"','"+info.getCodigo2().trim()+"','"+info.getCodigo3().trim()+"','"+info.getCodigo4().trim()+"','"+info.getDescripcion().trim()+"')";
		log.info("INSERTA :"+sqlInserta);
																																																																																																																																				/*private int transaccion;
																																																																																																																																				private String codigoInforme;
																																																																																																																																				private int fechaGeneracion;
																																																																																																																																				private int horaGeneracion;
																																																																																																																																				private int rut;
																																																																																																																																				private String dv;
																																																																																																																																				private String razonSocial;
																																																																																																																																				private int codigoBodega;
																																																																																																																																				private String descripcionBodega;
																																																																																																																																				private int numeroDocumento;
																																																																																																																																				private int folio;
																																																																																																																																				private String tipoDocumento;
																																																																																																																																				private int fechaDocumento;
																																																																																																																																				private int ano;
																																																																																																																																				private int mes;
																																																																																																																																				private int valorNeto;
																																																																																																																																				private int valorImpto;
																																																																																																																																				private int valorTotal;
																																																																																																																																				private String descripcion1;
																																																																																																																																				private String descripcion2;
																																																																																																																																				private String descripcion3;
																																																																																																																																				private String descripcion4;
																																																																																																																																				private String codigo1;
																																																																																																																																				private String codigo2;
																																																																																																																																				private String codigo3;
																																																																																																																																				private String codigo4;
																																																																																																																																				private String descripcion;*/
		try{
			pr = conn.prepareStatement(sqlInserta);
			pr.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return numero;
	}
	
	public List obtieneGasto(int periodo){
		List lista = new ArrayList();
		
		PreparedStatement pr =null;
		ResultSet rs = null;
		String sqlLista ="SELECT * FROM CASEDAT.INFOGAST";
		try{
			pr = conn.prepareStatement(sqlLista);
			rs = pr.executeQuery();
			while (rs.next()){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return lista;
	}
	public int obtieneCorrelativo(){
		int correlativo=0;
		
		PreparedStatement pr = null;
		ResultSet rs = null;
		String sql ="SELECT MAX(OBF00B) AS MAXIMO FROM CASEDAT.INFGASTO";
		try{
			pr = conn.prepareStatement(sql);
			rs = pr.executeQuery();
			while (rs.next()){
				correlativo = rs.getInt("MAXIMO");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				pr.close();
				rs.close();
			}catch(SQLException s){
				s.printStackTrace();
			}
			
		}
		return correlativo;
	}
}
