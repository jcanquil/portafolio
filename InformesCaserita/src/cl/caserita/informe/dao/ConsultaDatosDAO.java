package cl.caserita.informe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.caserita.informe.dto.ExmgesDTO;
import cl.caserita.informe.dto.GastosDTO;

public class ConsultaDatosDAO {

	private Connection conection;
	
	public void SetConnection(Connection conn){
		this.conection = conn;
	}
	
	private static final String sqlConsulta = "SELECT * FROM CASEDAT.PASOJ1";
	
	public List datosExmges() throws Exception {
		
		List lista = new ArrayList();
		
			
		
		ResultSet rs = null;
		PreparedStatement ps = conection.prepareStatement(sqlConsulta);
		try{
			rs = ps.executeQuery();
			
			while (rs.next())
			{
			
				
				ExmgesDTO dto = new ExmgesDTO();
				dto.setCodBodega(rs.getString("EXMC01"));
				dto.setCodArticulo(rs.getString("EXMC02"));
				dto.setDescArticulo(rs.getString("EXMDES"));
				dto.setStockComputacional(rs.getInt("EXMST6"));
				dto.setCosto(rs.getInt("EXMCO2"));
				
				//dto.setTotal(rs.getDouble("TOTAL"));
				dto.setDescripcion1(rs.getString("TPTD09"));
				dto.setDescFamilia(rs.getString("TPTD10"));
				dto.setDescripcion2(rs.getString("TPTD22"));
				dto.setRut(rs.getInt("EXTRUT"));
				dto.setRazonSocial(rs.getString("PRMNOM"));
				
				
				lista.add(dto);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return lista;
		
		
	}
	
	public List obtieneGastos(String fecha, String hora){
		List listaGasto = new ArrayList();
		
		
		ResultSet rs = null;
		
		try{
			
			String consultaGastos = "SELECT * FROM CASEDAT.INfgasto WHERE AAGMA="+Integer.parseInt(fecha)+" AND AAGNA="+Integer.parseInt(hora)+" ";
		
			PreparedStatement ps = conection.prepareStatement(consultaGastos);
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				GastosDTO gastos = new GastosDTO();
				gastos.setOrigen(rs.getString("AAGLA"));
				gastos.setCuenta(rs.getString("AAGSA"));
				gastos.setSubCuenta(rs.getString("AAGTA"));
				gastos.setCentroCosto(rs.getString("TPTD18"));
				gastos.setGastosString(String.valueOf((rs.getDouble("AAG0A"))));
				gastos.setFecha(String.valueOf(rs.getInt("CAMFEC")));
				gastos.setGlosa(rs.getString("AAH7A"));
				gastos.setLibro(rs.getString("AAGOA"));
				gastos.setFolio(rs.getInt("CONFOL"));
				gastos.setNumDocumento(rs.getString("CLCNUM"));
				gastos.setFechaEmision(String.valueOf(rs.getInt("CAMFEC")));
				gastos.setRazonSocial(rs.getString("TPTRAZ"));
				
				if (!"0".equals(rs.getString("CAMRUT")) && rs.getString("CAMRUT")!="0"){
					gastos.setRut(String.valueOf(rs.getString(("CAMRUT")) + "-" + rs.getString("EXMDI2")));
				}
				else{
					gastos.setRut("");
				}
					
				
				listaGasto.add(gastos);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			}
		return listaGasto;
	}
	
	public String obtieneCorreo(){
			
		String mail="";
		ResultSet rs = null;
		
		try{
		
			String consultaGastos = "SELECT * FROM CASEDAT.MAILINF";
		
			PreparedStatement ps = conection.prepareStatement(consultaGastos);
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				mail = rs.getString("CLIEMA");
				
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			}
		return mail;
	}
	
	public List articulos(){
		List articulos=new ArrayList();
		
		
		ResultSet rs = null;
		
		try{
			
			String consultaGastos = "SELECT * FROM CASEDAT.PRECANA5   ";
		
			PreparedStatement ps = conection.prepareStatement(consultaGastos);
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				ExmgesDTO dto = new ExmgesDTO();
				dto.setCodArticulo(String.valueOf(rs.getInt("F1")));
				articulos.add(dto);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			}
		return articulos;
	}
	
	public double precotiW(int articulo){
	
		
		double costo1=0;
		double costo2=0;
		ResultSet rs = null;
		
		try{
			
			String consultaGastos = "SELECT * FROM CASEDAT.PRECANA5 WHERE F1="+articulo+"  ";
		
			PreparedStatement ps = conection.prepareStatement(consultaGastos);
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				costo1 = Double.parseDouble(rs.getString("F8"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
			}
		return costo1;
	}
	
	public double precotiW2(int articulo){
	
		
		
		double costo2=0;
		ResultSet rs = null;
		
		try{
			
			String consultaGastos = "SELECT * FROM CASEDAT.PRECANA5 WHERE F1="+articulo+"  ";
		
			PreparedStatement ps = conection.prepareStatement(consultaGastos);
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				costo2 = Double.parseDouble(rs.getString("F11"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
			}
		return costo2;
	}

public double actualiza(int articulo, double costo, double costo2){
	
	
	
	
	ResultSet rs = null;
	
	try{
		
		String consultaGastos = "UPDATE CASEDAT.PRECOTI SET AAOHA="+costo+" , AAOOA="+costo2+" WHERE EXMCOD="+articulo+"  ";
	
		PreparedStatement ps = conection.prepareStatement(consultaGastos);
		 ps.executeUpdate();
		
		
	}
	catch(Exception e){
		e.printStackTrace();
		}
	return costo2;
}
}
