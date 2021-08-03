package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.CldmcoDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ExtariDTO;
import cl.caserita.dto.PrecioDescDTO;

public class CldmcoDAOImpl implements CldmcoDAO {

	private  static Logger log = Logger.getLogger(CldmcoDAOImpl.class);

	private Connection conn;
	
	public CldmcoDAOImpl(Connection conn){
		this.conn=conn;
	}
	
	public List obtieneArticulos(int empresa, int codigo, int rutCliente, String dv, int fecha, int hora, int bodega, int tipoVendedor, int tipoMov, int numero){
		CldmcoDTO cldmcoDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLDMCO " + 
        " Where CLDEMP="+empresa+" AND CLDCO4="+codigo+" AND CLDRU2="+rutCliente+" AND CLDDV2='"+dv+"' AND CLDFEC="+fecha+" AND CLDHOR="+hora+" order by cldcor FOR READ ONLY" ;
		List cldmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cldmcoDTO = new CldmcoDTO();
				cldmcoDTO.setCodDocumento(rs.getInt("CLDCO4"));
				cldmcoDTO.setRutCliente(rs.getString("CLDRU2"));
				cldmcoDTO.setDvCliente(rs.getString("CLDDV2"));
				cldmcoDTO.setFechaMovimiento(rs.getInt("CLDFEC"));
				cldmcoDTO.setHoraMovimiento(rs.getInt("CLDHOR"));
				cldmcoDTO.setCorrelativo(rs.getInt("CLDCOR"));
				cldmcoDTO.setRutProveedor(rs.getString("CLDRUT"));
				cldmcoDTO.setDvProveedor(rs.getString("CLDDVR"));
				cldmcoDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
				cldmcoDTO.setDigitoverificador(rs.getString("CLDDIG"));
				cldmcoDTO.setCantidadArticulo(rs.getInt("CLDCAN"));
				cldmcoDTO.setCostoArticulo(rs.getInt("CLDCOS"));
				cldmcoDTO.setDescuentoLinea(rs.getInt("CLDDES"));
				cldmcoDTO.setMontoIva(rs.getInt("CLDMO1"));
				cldmcoDTO.setMontoFlete(rs.getInt("CLDMO2"));
				cldmcoDTO.setMontoCompra(rs.getInt("CLDMON"));
				cldmcoDTO.setSectorBodega(rs.getInt("CLDSEC"));
				cldmcoDTO.setEstado(rs.getInt("CLDEST"));
				cldmcoDTO.setGlosa(rs.getString("CLDGLO"));
				cldmcoDTO.setValorNeto(rs.getDouble("CLDMTN"));
				cldmcoDTO.setPrecioNeto(rs.getDouble("CLDPRN"));
				cldmcoDTO.setPrecio(rs.getDouble("CLDPRB"));
				cldmcoDTO.setDescuentoLinea(rs.getDouble("CLDDEN"));
				//cldmcoDTO.setDescuentoentero(rs.getInt(""));
				cldmcoDTO.setMontoExento(rs.getInt("CLDMEX"));
				PrecioDescDTO precio=null;
				//if (codigo!=39){
					//precio= recuperaPrecioVedmar(tipoMov, fecha, numero, cldmcoDTO.getCodigoArticulo());
					//cldmcoDTO.setPrecio(precio.getPrecio());
					//cldmcoDTO.setDescuentoLinea(precio.getDescuento$());
					//int numero44 = (int) precio.getDescuento$();
					
					//cldmcoDTO.setDescuentoentero(numero44);
				//}
//				if (codigo==39){
//					cldmcoDTO.setPrecio(rs.getInt("CLDCOS"));
//				}
				List lista = null;
				cldmcoDTO.setDescArticulo(obtieneDescripcion(cldmcoDTO.getCodigoArticulo(), cldmcoDTO.getDigitoverificador())+"@"+cldmcoDTO.getPrecio()+"@");
				if (verificaArticuloCombo(cldmcoDTO.getCodigoArticulo(), cldmcoDTO.getDigitoverificador())==0){
					//iint empresa, nt codigo, int rutCliente, String dv, int fecha, int hora,int correlativo
					
						
							
							lista = recuperaImpuestoArticulo(cldmcoDTO.getCodigoArticulo(), cldmcoDTO.getDigitoverificador());
						
						
				
						if (lista.size()>0){
							cldmcoDTO.setImpuestos(lista);
						}
					
					cldmco.add(cldmcoDTO);
				}else{
					CldmcoDTO cldmcoDTO2=null;
					List cldcombo=null;
					if (verificaSoloivaCombo(cldmcoDTO.getCodigoArticulo(), cldmcoDTO.getDigitoverificador())==1){
						//Procesa Solo IVA
						/*ExtariDTO exta = new ExtariDTO();
						exta.setCodImpto(2);
						exta.setCodigoArticulo(cldmcoDTO.getCodigoArticulo());
						exta.setDv(cldmcoDTO.getDigitoverificador());
						
						List lista2 = new ArrayList();
						lista2.add(exta);
						if (lista2.size()>0){
							cldmcoDTO2.setImpuestos(lista2);
						}*/
						//cldmco.add(cldmcoDTO2);
						cldmcoDTO2 = cldmcoDTO;
						cldmco.add(cldmcoDTO2);
					}else{
						if (recuperaClddia( empresa,  codigo,  rutCliente,  dv,  fecha,  hora,cldmcoDTO.getCorrelativo())>2){
							cldcombo = recuperaCldcomb(empresa,  codigo,  rutCliente,  dv,  fecha,  hora, cldmcoDTO.getCodigoArticulo(), cldmcoDTO.getDigitoverificador(),cldmcoDTO.getCorrelativo());
							
						}else
						{
							cldmcoDTO2 = cldmcoDTO;
							lista = recuperaImpuestoCombo(cldmcoDTO.getCodigoArticulo(), cldmcoDTO.getDigitoverificador());
							if (lista.size()>0){
								cldmcoDTO2.setImpuestos(lista);
							}
							cldmco.add(cldmcoDTO2);
							//cldmcoDTO2=cldmcoDTO;
						}
					}
					if (cldcombo!=null){
						if (cldcombo.size()>0){
								Iterator iterCld = cldcombo.iterator();
								while (iterCld.hasNext()){
									cldmcoDTO2 = (CldmcoDTO) iterCld.next();
									cldmcoDTO2.setDescArticulo(obtieneDescripcion(cldmcoDTO2.getCodigoArticulo(), cldmcoDTO2.getDigitoverificador())+"@"+cldmcoDTO.getPrecio()+"@");
									cldmco.add(cldmcoDTO2);
							}
								
						}
					}/*else{
						cldmco.add(cldmcoDTO2);
					}*/
					
					
				
				}
				
				
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
		return cldmco;
	}
	public String obtieneDescripcion(int codigo, String dv){
		String des="";
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMART " + 
        " Where EXMCOD="+codigo+" AND EXMDIG='"+dv+"'  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				des = rs.getString("EXMDES");
				des = des.replaceAll("&", "&amp;");
				des = des.replaceAll("\"", "&quot;");
				des = des.replaceAll("\'", "&apos;");
				des = des.replaceAll("�", "N");
				//log.info("descripcion articulo:"+des);
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
		
		
		return des;
	}
	public int recuperaPrecio(int bodega, int articulo, String digito, int tipovendedor){
		int precio=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMPTV " + 
        " Where EXICO9="+bodega+" AND EXIC01="+articulo+" AND EXIDI3='"+digito+"' AND EXIC02="+tipovendedor+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				precio = rs.getInt("EXIPRE");
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
		
		
		return precio;
		
		
	}
	public PrecioDescDTO recuperaPrecioVedmar(int tipoMov, int fecha, int numero, int codArticulo){
		PrecioDescDTO precio=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VEDMAR " + 
        " Where VENC11="+tipoMov+" AND VENFE3="+fecha+" AND VENNU4='"+numero+"' AND  VENC16="+codArticulo+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				precio = new PrecioDescDTO();
				precio.setPrecio(rs.getDouble("VEDPR2"));
				precio.setDescuento$(rs.getDouble("VENMON"));
				precio.setDescuentoPorc(rs.getDouble("VENDES"));
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
		
		
		return precio;
	}
	
	public List recuperaImpuestoArticulo(int articulo, String dv){
		ExtariDTO extari=null;
		PreparedStatement pstmt =null;
		List extariList = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXTARI " + 
        " Where EXTCO1="+articulo+" AND EXTDI2='"+dv+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				extari = new ExtariDTO();
				if (rs.getInt("EXTCO2")!=2){
					extari.setCodigoArticulo(rs.getInt("EXTCO1"));
					extari.setDv(rs.getString("EXTDI2"));
					extari.setCodImpto(rs.getInt("EXTCO2"));
					extariList.add(extari);
				}
				
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
		
		
		return extariList;
	}
	
	public List recuperaImpuestoCombo(int articulo, String dv){
		ExtariDTO extari=null;
		PreparedStatement pstmt =null;
		List extariList = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select  "+
        " E2.EXTCO2 FROM CASEDAT.EXDACB E1, CASEDAT.EXTARI E2 " + 
        " WHERE E1.EXDCOD ="+articulo+" AND E1.EXDDIG='"+dv+"' AND E1.EXDCO2 = E2.EXTCO1 AND E2.EXTCO2 <>2 GROUP BY E2.EXTCO2 fetch first 2 rows only" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				extari = new ExtariDTO();
				if (rs.getInt("EXTCO2")!=2){
					extari.setCodigoArticulo(0);
					extari.setDv("0");
					extari.setCodImpto(rs.getInt("EXTCO2"));
					extariList.add(extari);
				}
				
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
		
		
		return extariList;
	}
	public int verificaArticuloCombo(int articulo, String dv){
		ExtariDTO extari=null;
		PreparedStatement pstmt =null;
		int combo=0;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        "  FROM CASEDAT.EXMART " + 
        " WHERE EXMCOD ="+articulo+" AND EXMDIG='"+dv+"' FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String comboE=rs.getString("EXMCOM");
				if ("C".equals(comboE)){
					combo=1;
				}
				
				
				
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
		
		
		return combo;
	}
	public int verificaSoloivaCombo(int articulo, String dv){
		ExtariDTO extari=null;
		PreparedStatement pstmt =null;
		int combo=2;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        "  FROM CASEDAT.ORDVTAD " + 
        " WHERE xonm1 ="+articulo+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String comboE=rs.getString("xocl2").trim();
				
				if (comboE!=null){
					if (comboE!="" ){
						combo=Integer.parseInt(comboE.trim());
					}
				}
				
				
				
				
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
		
		
		return combo;
	}
	
	public int recuperaClddia(int empresa,int codigo, int rutCliente, String dv, int fecha, int hora,int correlativo){
		ClcdiaDTO clcdiaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CLDDIA " + 
        " Where CLDCEM="+empresa+" AND CLDCO6="+codigo+" AND CLDRU3="+rutCliente+" AND CLDDV3='"+dv+"' AND CLDFE4="+fecha+" AND CLDHO1="+hora+" AND CLDCO7="+correlativo+" FOR READ ONLY" ;
		int cant=0;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLDCO8")!=2)
				{
					cant=cant+1;
				}
				
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
		return cant;
	}
	public List recuperaCldcomb(int empresa,int codigo, int rutCliente, String dv, int fecha, int hora,int articulo, String digito, int correlativo){
		ClcdiaDTO clcdiaDTO = null;
		List cld=new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		CldmcoDTO cldmcoDTO = null;
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CLDCOMB " + 
        " Where CLDEMP="+empresa+" AND CLDCO4="+codigo+" AND CLDRU2="+rutCliente+" AND CLDDV2='"+dv+"' AND CLDFEC="+fecha+" AND CLDHOR="+hora+" AND CLDCOR="+correlativo+" FOR READ ONLY" ;
		int cant=0;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cldmcoDTO = new CldmcoDTO();
				cldmcoDTO.setCodDocumento(rs.getInt("CLDCO4"));
				cldmcoDTO.setRutCliente(rs.getString("CLDRU2"));
				cldmcoDTO.setDvCliente(rs.getString("CLDDV2"));
				cldmcoDTO.setFechaMovimiento(rs.getInt("CLDFEC"));
				cldmcoDTO.setHoraMovimiento(rs.getInt("CLDHOR"));
				cldmcoDTO.setCorrelativo(rs.getInt("CLDCOR"));
				int correlativo2 = rs.getInt("CLDCO7");
				cldmcoDTO.setRutProveedor(rs.getString("CLDRU2"));
				cldmcoDTO.setDvProveedor(rs.getString("CLDDV2"));
				cldmcoDTO.setCodigoArticulo(articulo);
				cldmcoDTO.setDigitoverificador(digito);
				cldmcoDTO.setCantidadArticulo(rs.getInt("CLDCAN"));
				//cldmcoDTO.setCostoArticulo(rs.getInt("CLDCOS"));
				//cldmcoDTO.setDescuentoLinea(rs.getInt("CLDDES"));
				//cldmcoDTO.setMontoIva(rs.getInt("CLDMO1"));
				//cldmcoDTO.setMontoFlete(rs.getInt("CLDMO2"));
				cldmcoDTO.setMontoCompra(rs.getInt("CLDMON"));
				//cldmcoDTO.setSectorBodega(rs.getInt("CLDSEC"));
				//cldmcoDTO.setEstado(rs.getInt("CLDEST"));
				//cldmcoDTO.setGlosa(rs.getString("CLDGLO"));
				cldmcoDTO.setValorNeto(rs.getDouble("CLDMTN"));
				cldmcoDTO.setPrecioNeto(rs.getDouble("CLDPRN"));
				cldmcoDTO.setPrecio(rs.getDouble("CLDPRB"));
				cldmcoDTO.setDescuentoLinea(rs.getDouble("CLDDEN"));
				//cldmcoDTO.setDescuentoentero(rs.getInt(""));
				//cldmcoDTO.setMontoExento(rs.getInt("CLDMEX"));
				cldmcoDTO.setImpuestos(recuperaImpuestoCldcomb( empresa, codigo,  rutCliente,  dv,  fecha,  hora,cldmcoDTO.getCorrelativo(),correlativo2));
				cld.add(cldmcoDTO);
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
		return cld;
	}
	
	public List recuperaImpuestoCldcomb(int empresa,int codigo, int rutCliente, String dv, int fecha, int hora,int correlativo, int correlativo2){
		ExtariDTO extariDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		List listaImp= new ArrayList();
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CLDCOIM " + 
        " Where CLDEMP="+empresa+" AND CLDCO4="+codigo+" AND CLDRU2="+rutCliente+" AND CLDDV2='"+dv+"' AND CLDFEC="+fecha+" AND CLDHOR="+hora+" AND CLDCOR="+correlativo+" AND CLDCO7="+correlativo2+" FOR READ ONLY" ;
		int cant=0;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				extariDTO= new ExtariDTO();
				extariDTO.setCodImpto(rs.getInt("CLCCO3"));
				listaImp.add(extariDTO);
				
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
		return listaImp;
	}
	
	public List obtieneFleteCldmco(int empresa, int coddoc, int rutclie, String digclie, int fecha, int codbod, int numdoc, int codarti){
		CldmcoDTO cldmcoDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select  "+
		" d.cldfec, d.cldhor, d.cldcor, d.cldcod, d.clddig, d.cldcan, d.cldcos, d.clddes, d.cldmo1, d.cldmo2, d.cldmon, d.cldmtn,"+
		" d.cldprn, d.cldprb, d.cldden, d.cldmex "+
		" from CASEDAT.CLCMCO c" +
		" INNER JOIN CASEDAT.CLDMCO d ON c.clcemp=d.cldemp AND c.clcco1=d.cldco4 AND c.clcrut=d.cldru2 AND c.clcdig=d.clddv2 "+
        " AND c.clcfec=d.cldfec AND c.clchor=d.cldhor"+
        " Where c.CLCEMP="+empresa+" AND c.CLCCO1="+coddoc+" AND c.CLCRUT="+rutclie+" AND c.CLCDIG='"+digclie+"' "+
        " AND c.CLCFEC="+fecha+" AND c.CLCBOD="+codbod+ " AND c.CLCNUM="+numdoc+
        " AND d.CLDCOD="+codarti+
        " FOR READ ONLY" ;
		
		List cldmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cldmcoDTO = new CldmcoDTO();
				cldmcoDTO.setFechaMovimiento(rs.getInt("CLDFEC"));
				cldmcoDTO.setHoraMovimiento(rs.getInt("CLDHOR"));
				cldmcoDTO.setCorrelativo(rs.getInt("CLDCOR"));
				cldmcoDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
				cldmcoDTO.setDigitoverificador(rs.getString("CLDDIG"));
				cldmcoDTO.setCantidadArticulo(rs.getInt("CLDCAN"));
				cldmcoDTO.setCostoArticulo(rs.getInt("CLDCOS"));
				cldmcoDTO.setDescuentoLinea(rs.getInt("CLDDES"));
				cldmcoDTO.setMontoIva(rs.getInt("CLDMO1"));
				cldmcoDTO.setMontoFlete(rs.getInt("CLDMO2"));
				cldmcoDTO.setMontoCompra(rs.getInt("CLDMON"));
				cldmcoDTO.setValorNeto(rs.getDouble("CLDMTN"));
				cldmcoDTO.setPrecioNeto(rs.getDouble("CLDPRN"));
				cldmcoDTO.setPrecio(rs.getDouble("CLDPRB"));
				cldmcoDTO.setDescuentoLinea(rs.getDouble("CLDDEN"));
				cldmcoDTO.setMontoExento(rs.getInt("CLDMEX"));
				cldmco.add(cldmcoDTO);
				
				
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
		return cldmco;
	}
}
