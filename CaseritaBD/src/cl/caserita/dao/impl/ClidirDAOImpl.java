package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.ClidiraDTO;
import cl.caserita.dto.UsuarioCanastaDTO;

public class ClidirDAOImpl implements ClidirDAO{

	private  static Logger log = Logger.getLogger(ClidirDAOImpl.class);

	private Connection conn;
	
	public ClidirDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public int generaDireccion(ClidirDTO clidir){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO "+
        "  CASEDAT.CLIDIR " + 
        " (CLIRUT,CLIDIG,CLICOR,CLIDI1,CLINUM,CLITEL,CLITE1,CLIFAX,CLICON,CLIFEC,CLIFE1,CLIEMA,CLICOD,CLICO1,CLICO2,CLICO3) VALUES("+clidir.getRutCliente()+",'"+clidir.getDvCliente().trim()+"',"+clidir.getCorrelativo()+",'"+clidir.getDireccionCliente()+"','"+clidir.getVillaPoblacion().substring(0, 5)+"','"+clidir.getTelefono()+"','"+clidir.getCelular()+"','"+clidir.getVillaPoblacion().substring(6, 15)+"','"+clidir.getNombreContacto()+"',0,0,'"+clidir.getMail()+"',0,"+clidir.getRegion()+","+clidir.getCiudad()+","+clidir.getComuna()+")" ;
		System.out.println("SQL CLIDIR" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int generaClidira(ClidiraDTO clidira){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO "+
        "  CASEDAT.CLIDIRA " + 
        " (CLIRUT,CLIDIG,CLICOR,LATDIR,LONDIR,GLCMDI,ESTDIR) VALUES("+clidira.getRutCliente()+",'"+clidira.getDvCliente().trim()+"',"+clidira.getCorrelativo()+",'"+clidira.getLatitud().trim()+"','"+clidira.getLongitud().trim()+"','"+clidira.getObservacion().trim()+"','A')" ;
		System.out.println("SQL CLIDIRA" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int actualizaDireccionClidir(ClidirDTO clidir){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE "+
        "  CASEDAT.CLIDIR " + 
        " SET CLIDI1='"+clidir.getDireccionCliente()+"' , CLINUM='"+clidir.getVillaPoblacion().substring(0, 5)+"' , CLITEL='"+clidir.getTelefono()+"' , CLITE1='"+clidir.getCelular()+"' , CLIFAX='"+clidir.getVillaPoblacion().substring(6, 15)+"' , CLICON='"+clidir.getNombreContacto()+"' , CLIFEC=0 , CLIFE1=0 , CLIEMA='"+clidir.getMail()+"' , CLICOD=0 , CLICO1="+clidir.getRegion()+" , CLICO2="+clidir.getCiudad()+"  , CLICO3="+clidir.getComuna()+" WHERE CLIRUT="+clidir.getRutCliente()+" AND CLIDIG="+clidir.getDvCliente()+" AND CLICOR="+clidir.getCorrelativo()+"" ;
		System.out.println("ACTUALIZA CLIDIR" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int actualizaClidira(ClidiraDTO clidira){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE "+
        "  CASEDAT.CLIDIRA " + 
        " SET LATDIR='"+clidira.getLatitud().trim()+"' , LONDIR='"+clidira.getLongitud().trim()+"' , GLCMDI='"+clidira.getObservacion().trim()+"'  WHERE CLIRUT="+clidira.getRutCliente()+" AND CLIDIG='"+clidira.getDvCliente().trim()+"' AND CLICOR="+clidira.getCorrelativo()+" " ;
		System.out.println("SQL CLIDIRA" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			correlativo=-100;
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	public ClidirDTO obtieneDireccion(int rut,  int correlativo)
    {
        PreparedStatement pstmt;
        ResultSet rs;
        List dir = new ArrayList();
        String sqlObtenerVecmar;
        ClidirDTO clidirDTO = null;
        pstmt = null;
        rs = null;
        
        sqlObtenerVecmar = "Select *  from CASEDAT.CLIDIR WHERE CLIRUT="+rut+" AND CLICOR="+correlativo+"   ";
        System.out.println((new StringBuilder("SQL IDDIRCLI")).append(sqlObtenerVecmar).toString());
        try
        {
            pstmt = conn.prepareStatement(sqlObtenerVecmar);
            rs = pstmt.executeQuery();
            while( rs.next() )
            {
                clidirDTO = new ClidirDTO();
                System.out.println("Direccion:"+rs.getString("CLIDI1").substring(35, 40));
                System.out.println("Direccion:"+rs.getString("CLIDI1"));
                clidirDTO.setNombreContacto(rs.getString("CLICON").trim());
                String depto = rs.getString("CLIDI1").substring(35, 40).trim();
                if (!depto.equals("null") && depto!=null && !depto.equals("ll") && !depto.equals("ull")){
                	// clidirDTO.setDepto(Integer.parseInt(depto.trim()));
                	 clidirDTO.setCelular(depto);
                }else{
                	clidirDTO.setCelular("");
                }
               
            }

           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally{
        try
        {
            if(rs != null)
            {
                rs.close();
                pstmt.close();
            }
        }catch(Exception e){
        	e.printStackTrace();
        }
        }
        return clidirDTO;
    }
	public int actualizaClidiraLatLng(ClidiraDTO clidira){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE "+
        "  CASEDAT.CLIDIRA " + 
        " SET LATDIR='"+clidira.getLatitud().trim()+"' , LONDIR='"+clidira.getLongitud().trim()+"'   WHERE CLIRUT="+clidira.getRutCliente()+" AND CLICOR="+clidira.getCorrelativo()+" " ;
		System.out.println("SQL CLIDIRA" + sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			
			
			
			 pstmt.executeUpdate();
			correlativo=1;
		}catch(Exception e){
			correlativo=-100;
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int obtieneCorrelativoFacturacion(int rut, String dv){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT MAX(CLICOR) AS CLICOR FROM "+
        "  CASEDAT.CLIDIR " + 
        " WHERE CLIRUT="+rut+" AND CLIDIG='"+dv.trim()+"'  AND CLICOR BETWEEN 0 AND 99   " ;
		System.out.println("SQL CLIDIR:"+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				correlativo = rs.getInt("CLICOR");
				System.out.println("CORRELATIVO:"+correlativo);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	
	public int obtieneCorrelativo(int rut, String dv){
		int correlativo=100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT MAX(CLICOR) AS CLICOR FROM "+
        "  CASEDAT.CLIDIR " + 
        " WHERE CLIRUT="+rut+" AND CLIDIG='"+dv+"'  AND CLICOR BETWEEN 100 AND 199   " ;
		System.out.println("SQL CLIDIR:"+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				correlativo = rs.getInt("CLICOR");
				System.out.println("CORRELATIVO 2:"+correlativo);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		if (correlativo==0){
			correlativo=100;
		}
		return correlativo;
	}
	
	public List obtieneDirecciones(int rut, String dv){
		int correlativo=0;
		List clidirList = new ArrayList();
		ClidirDTO clidirDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * FROM "+
        "  CASEDAT.CLIDIR " + 
        " WHERE CLIRUT="+rut+" AND CLIDIG='"+dv+"'     " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				clidirDTO = new ClidirDTO();
				clidirDTO.setRutCliente(rut);
				clidirDTO.setCorrelativo(rs.getInt("CLICOR"));
				clidirDTO.setDireccionCliente(rs.getString("CLIDI1").substring(0, 30).trim());
				String dire = rs.getString("CLIDI1");
				if (rs.getString("CLIDI1").substring(30, 35).trim().length()>0){
					clidirDTO.setNumeroDireccion(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
				}
				
				clidirDTO.setTelefono(rs.getString("CLITEL"));
				clidirDTO.setCelular(rs.getString("CLITE1"));
				clidirDTO.setRegion(rs.getInt("CLICO1"));
				clidirDTO.setDescripcionRegion(recuperaRegion(clidirDTO.getRegion()).trim());
				clidirDTO.setCiudad(rs.getInt("CLICO2"));
				clidirDTO.setDescripcionCiudad(recuperaCiudad(clidirDTO.getRegion(), clidirDTO.getCiudad()).trim());
				clidirDTO.setComuna(rs.getInt("CLICO3"));
				clidirDTO.setDescripcionComuna(recuperaComuna(clidirDTO.getRegion(), clidirDTO.getCiudad(), clidirDTO.getComuna()).trim());
				if (rs.getString("CLIDI1").substring(35, 39).trim().length()>0){
					clidirDTO.setDepto(Integer.parseInt(rs.getString("CLIDI1").substring(35, 40).trim()));
				}
				
				clidirDTO.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX"));
				clidirList.add(clidirDTO);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return clidirList;
	}
	
	public ClidirDTO obtieneDireccionCliente(int rut, int correlativo){
		ClidirDTO clidirDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * FROM "+
        "  CASEDAT.CLIDIR " + 
        " WHERE CLIRUT="+rut+" AND CLICOR="+correlativo+"     " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				clidirDTO = new ClidirDTO();
				clidirDTO.setCorrelativo(rs.getInt("CLICOR"));
				clidirDTO.setDireccionCliente(rs.getString("CLIDI1").substring(0, 30).trim());
				String dire = rs.getString("CLIDI1");
				System.out.println("Direccion :"+clidirDTO.getDireccionCliente());
				System.out.println("Rut :"+rut);

				if (rs.getString("CLIDI1").substring(30, 35).trim().length()>0){
					clidirDTO.setNumeroDireccion(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
				}
				
				clidirDTO.setTelefono(rs.getString("CLITEL"));
				clidirDTO.setCelular(rs.getString("CLITE1"));
				clidirDTO.setRegion(rs.getInt("CLICO1"));
				clidirDTO.setDescripcionRegion(recuperaRegion(clidirDTO.getRegion()).trim());
				clidirDTO.setCiudad(rs.getInt("CLICO2"));
				clidirDTO.setDescripcionCiudad(recuperaCiudad(clidirDTO.getRegion(), clidirDTO.getCiudad()).trim());
				clidirDTO.setComuna(rs.getInt("CLICO3"));
				clidirDTO.setDescripcionComuna(recuperaComuna(clidirDTO.getRegion(), clidirDTO.getCiudad(), clidirDTO.getComuna()).trim());
				if (rs.getString("CLIDI1").substring(35, 39).trim().length()>0){
					clidirDTO.setDepto(Integer.parseInt(rs.getString("CLIDI1").substring(35, 40).trim()));
				}
				
				clidirDTO.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return clidirDTO;
	}
	
	public int obtieneCorrelativo2(int rut, String dv){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT CLICOR FROM "+
        "  CASEDAT.CLIDIR " + 
        " WHERE CLIRUT="+rut+" AND CLIDIG='"+dv+"'  AND CLICOR BETWEEN 300 AND 9999   " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				correlativo = rs.getInt("CLICOR");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return correlativo;
	}
	public String recuperaRegion(int region){
		String desregion="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTREG " + 
        " Where TPTCO1="+region+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				desregion = rs.getString("TPTDE1");
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
		
		
		return desregion;
		
		
	}
	
	
	public String recuperaComuna(int region, int ciudad, int comuna){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCOM " + 
        " Where TPTC19="+region+" AND TPTC20="+ciudad+" AND TPTC22="+comuna+" FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTD20");
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
		
		
		return descomuna;
		
		
	}
	
	public String recuperaCiudad(int region, int ciudad){
		String descomuna="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCTY " + 
        " Where TPTCO6="+region+" AND TPTCO7="+ciudad+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//System.out.println("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				descomuna = rs.getString("TPTDE7");
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
		
		
		return descomuna;
		
		
	}
	
	public ClidirDTO recuperaDireccionesCorrrelativo(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		List lista = new ArrayList();
		String sqlObtenerCldmco ="Select *  "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= "+correlativo+" AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C1.CLICOR=C2.CLICOR   FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClidirDTO();
				clidir.setCorrelativo(rs.getInt("CLICOR"));
				clidir.setDireccionCliente(rs.getString("CLIDI1").substring(0, 30).trim());
				String dire = rs.getString("CLIDI1");
				System.out.println("Direccion :"+dire);
				System.out.println("Rut :"+rut);

				if (rs.getString("CLIDI1").substring(30, 35).trim().length()>0){
					if (isNumeric(rs.getString("CLIDI1").substring(30, 35).trim())){
						
						clidir.setNumeroDireccion(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));

					}

				}
				
				clidir.setTelefono(rs.getString("CLITEL"));
				clidir.setCelular(rs.getString("CLITE1"));
				clidir.setRegion(rs.getInt("CLICO1"));
				clidir.setDescripcionRegion(recuperaRegion(clidir.getRegion()).trim());
				clidir.setCiudad(rs.getInt("CLICO2"));
				clidir.setDescripcionCiudad(recuperaCiudad(clidir.getRegion(), clidir.getCiudad()).trim());
				clidir.setComuna(rs.getInt("CLICO3"));
				clidir.setDescripcionComuna(recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna()).trim());
				if (rs.getString("CLIDI1").substring(35, 39).trim().length()>0){
					if (isNumeric(rs.getString("CLIDI1").substring(35, 40).trim())){
						clidir.setDepto(Integer.parseInt(rs.getString("CLIDI1").substring(35, 40).trim()));

					}
				}
				
				clidir.setVillaPoblacion(rs.getString("CLINUM")+rs.getString("CLIFAX"));
				clidir.setLatitud(rs.getString("LATDIR"));
				clidir.setLongitud(rs.getString("LONDIR"));
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
		return clidir;
	}
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	} 
}
