package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.iface.ConnodDAO;
import cl.caserita.dto.ClcdiaDTO;
import cl.caserita.dto.ClcmcoDTO;
import cl.caserita.dto.CldmcoDTO;
import cl.caserita.dto.ConnodDTO;
import cl.caserita.dto.ConnohDTO;
import cl.caserita.dto.ExtariDTO;

public class ConnodDAOImpl implements ConnodDAO{
	
	private  static Logger log = Logger.getLogger(ConnodDAOImpl.class);

	private Connection conn;
	
	public ConnodDAOImpl(Connection conn){
		this.conn=conn;
	}
	

	public List buscaConnod(int empresa, String tipNota, int numNota, int fechaEmision, int codDoc){
		
		ConnodDTO connod = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CONNOD " + 
        " Where CONDEM="+empresa+" and CONTI2='"+tipNota+"' AND CONNU3="+numNota+" AND CONFE3='"+fechaEmision+"' FOR READ ONLY" ;
		List conno = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				connod = new ConnodDTO();
				connod.setCodigoEmpresa(rs.getInt("CONDEM"));
				connod.setTipoNota(rs.getString("CONTI2"));
				connod.setNumeroNota(rs.getInt("CONNU3"));
				connod.setFechaNota(rs.getInt("CONFE3"));
				connod.setCorrelativo(rs.getInt("CONCOR"));
				connod.setCodArticulo(rs.getInt("CONCO7"));
				connod.setDigArticulo(rs.getString("CONDI3"));
				connod.setDescripcion(rs.getString("CONDE4"));
				connod.setCantidad(rs.getInt("CONCAN"));
				connod.setFormato(rs.getString("CONFOR"));
				connod.setPrecioUnitario(rs.getDouble("CONPNT"));
				connod.setCantUnidades(rs.getInt("CONCA1"));
				connod.setTotalLinea(rs.getInt("CONTO1"));
				connod.setTotalNeto(rs.getInt("CONMTN"));
				connod.setDescLinea(rs.getInt("CONDCN"));
				connod.setMontoExento(rs.getInt("CONMEX"));
				//public ClcmcoDTO obtieneClcmco(int empresa, int codigo, int rutCliente, String dv, int fecha, int numero)
				//public ConnohDTO buscaConnoh(int empresa, String tipNota, int numNota, int fechaEmision)
				ConnohDTO connoh = buscaConnoh(connod.getCodigoEmpresa(),connod.getTipoNota(),connod.getNumeroNota(),connod.getFechaNota());
				ClcmcoDTO clc = obtieneClcmco(connod.getCodigoEmpresa(),codDoc,connoh.getRutCliente(),connoh.getDivCliente(),connod.getFechaNota(),connod.getNumeroNota());
				List lista = null;
				if (verificaArticuloCombo(connod.getCodArticulo(), connod.getDigArticulo())==0){
					lista = recuperaImpuestoArticulo(connod.getCodArticulo(), connod.getDigArticulo());
					if (lista.size()>0){
						connod.setImpuesto(lista);

					}
					conno.add(connod);
				}else{
					ConnodDTO connodTO2=null;
					List cldcombo=null;
					if (verificaSoloivaCombo(connod.getCodArticulo(), connod.getDigArticulo())==1){
						connodTO2 = connod;
						conno.add(connodTO2);
					}
					else{
						if (recuperaClddia(connod.getCodigoEmpresa(),  codDoc, connoh.getRutCliente(),  connoh.getDivCliente(),  connoh.getFechaNota(),  clc.getHoraMovimiento(),connod.getCorrelativo())>2){
							cldcombo = recuperaCldcomb(empresa,  codDoc,  connoh.getRutCliente(),  connoh.getDivCliente(),  connoh.getFechaNota(),  clc.getHoraMovimiento(), connod.getCodArticulo(), connod.getDigArticulo(),connod.getCorrelativo());
							
						}
						else
						{
							connodTO2 = connod;
							lista = recuperaImpuestoCombo(connod.getCodArticulo(), connod.getDigArticulo());
							if (lista.size()>0){
								connodTO2.setImpuesto(lista);
								
							}
							conno.add(connodTO2);
							//cldmcoDTO2=cldmcoDTO;
						}
						}
					
					if (cldcombo!=null){
						if (cldcombo.size()>0){
								Iterator iterCld = cldcombo.iterator();
								while (iterCld.hasNext()){
									CldmcoDTO cldmcoDTO2 = (CldmcoDTO) iterCld.next();
									connodTO2 = new ConnodDTO();
									cldmcoDTO2.setDescArticulo(obtieneDescripcion(cldmcoDTO2.getCodigoArticulo(), cldmcoDTO2.getDigitoverificador()));
									connodTO2.setCodArticulo(cldmcoDTO2.getCodigoArticulo());
									connodTO2.setDescripcion(cldmcoDTO2.getDescArticulo());
									connodTO2.setDigArticulo(cldmcoDTO2.getDigitoverificador());
									connodTO2.setPrecioUnitario(cldmcoDTO2.getPrecioNeto());
									connodTO2.setCantidad(cldmcoDTO2.getCantidadArticulo());
									int dsc = (int) cldmcoDTO2.getDescuentoLinea();
									connodTO2.setDescLinea(dsc);
									int neto = (int) cldmcoDTO2.getValorNeto();
									connodTO2.setTotalNeto(neto);
									connodTO2.setMontoExento(cldmcoDTO2.getMontoExento());
									connodTO2.setImpuesto(cldmcoDTO2.getImpuestos());
									
									//connodTO2.setDescArticulo(obtieneDescripcion(connod.getCodArticulo(), connod.getDigArticulo())+"@"+cldmcoDTO.getPrecio()+"@");
									conno.add(connodTO2);
							}
								
						}
					}
				}
				/*List lista = recuperaImpuestoArticulo(connod.getCodArticulo(), connod.getDigArticulo());
				if (lista.size()>0){
					connod.setImpuesto(lista);

				}
				conno.add(connod);*/
				
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
			
			return conno;
	}
	
	public int actualizaConnod(int empresa, String tipoNota, int numNota, int fechaEmision, int numero){
		int actualiza=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerConnod ="UPDATE "+
        " CASEDAT.CONNOD " + 
        " SET CONNU3="+numero+" Where CONDEM="+empresa+" and CONTI2='"+tipoNota+"' AND CONNU3="+numNota+" AND CONFE3="+fechaEmision+" " ;
		log.info(sqlObtenerConnod);
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnod);
			pstmt.executeUpdate();
			actualiza=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();
				 

			} catch (SQLException e1) { }

	  } 
		
		
		
		
		return actualiza;
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
	public int recuperaConnoi(int empresa,String tipo, int numeroNota, int fecha,int correlativo){
		ClcdiaDTO clcdiaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select * "+
        " from CASEDAT.CONNOD " + 
        " Where CONTEM="+empresa+" AND CONTI4='"+tipo+"' AND CONNU4="+numeroNota+" AND CONFE4="+fecha+" AND CONCO9="+correlativo+"  FOR READ ONLY" ;
		int cant=0;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			//log.info("SQL" + sqlObtenerCamtra);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CONCO8")!=2)
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
	public ClcmcoDTO obtieneClcmco(int empresa, int codigo, int rutCliente, String dv, int fecha, int numero){
		ClcmcoDTO clcmcoDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLCMCO1 " + 
        " Where CLCEMP="+empresa+" AND CLCCO1="+codigo+" AND CLCRUT="+rutCliente+" AND CLCDIG='"+dv+"' AND CLCFEC="+fecha+" AND CLCNUM="+numero+" FOR READ ONLY" ;
		List clcmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clcmcoDTO = new ClcmcoDTO();
				clcmcoDTO.setCodDocumento(rs.getInt("CLCCO1"));
				clcmcoDTO.setRutCliente(rs.getString("CLCRUT"));
				clcmcoDTO.setDvCliente(rs.getString("CLCDIG"));
				clcmcoDTO.setFechaMovimiento(rs.getInt("CLCFEC"));
				clcmcoDTO.setHoraMovimiento(rs.getInt("CLCHOR"));
				clcmcoDTO.setNumeroDocumento(rs.getInt("CLCNUM"));
				clcmcoDTO.setCodigoBodega(rs.getInt("CLCBOD"));
				clcmcoDTO.setCodigoVendedor(rs.getInt("CLCCOD"));
				clcmcoDTO.setCantidadLineaDetalle(rs.getInt("CLCCAN"));
				clcmcoDTO.setTotalCosto(rs.getInt("CLCTO4"));
				clcmcoDTO.setTotalIva(rs.getInt("CLCTO1"));
				clcmcoDTO.setTotalDescuento(rs.getInt("CLCTO3"));
				clcmcoDTO.setTotalFlete(rs.getInt("CLCTO2"));
				clcmcoDTO.setTotalDocumento(rs.getInt("CLCTOT"));
				clcmcoDTO.setFormaPago(rs.getString("CLCFOR"));
				clcmcoDTO.setMontoPie(rs.getInt("CLCMON"));
				clcmcoDTO.setMontoInteres(rs.getInt("CLCMO1"));
				clcmcoDTO.setCantidadCheques(rs.getInt("CLCCA1"));
				clcmcoDTO.setMontoCheques(rs.getInt("CLCMO2"));
				clcmcoDTO.setMesMovimiento(rs.getInt("CLCMES"));
				clcmcoDTO.setAñoMovimiento(rs.getInt("CLCANO"));
				clcmcoDTO.setEstado(rs.getInt("CLCEST"));
				clcmcoDTO.setFechaTranProveedor(rs.getInt("CLCFE2"));
				clcmcoDTO.setValorNeto(rs.getInt("CLCTNT"));
				clcmcoDTO.setTotalExento(rs.getInt("CLCTEN"));
				
				
				
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
		return clcmcoDTO;
	}
	
	public ConnohDTO buscaConnoh(int empresa, String tipNota, int numNota, int fechaEmision){
		ConnohDTO connoh = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CONNOH " + 
        " Where CONEMP="+empresa+" and CONTIP='"+tipNota+"' AND CONNU2="+numNota+" AND CONFE2='"+fechaEmision+"' FOR READ ONLY" ;
		List cldmco = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				connoh = new ConnohDTO();
				connoh.setTipoNota(rs.getString("CONTIP"));
				connoh.setNumeroNota(rs.getInt("CONNU2"));
				connoh.setFechaNota(rs.getInt("CONFE2"));
				connoh.setCodDocumento(rs.getInt("CONTI1"));
				connoh.setCodigoBodega(rs.getInt("CONCO1"));
				connoh.setCodigoMovimiento(rs.getInt("CONCOD"));
				connoh.setNumeroDocumento(rs.getInt("CONNRO"));
				connoh.setRutCliente(rs.getInt("CONRU1"));
				connoh.setDivCliente(rs.getString("CONDI1"));
				connoh.setCodigoVendedor(rs.getInt("CONCO6"));
				connoh.setNombreCliente(rs.getString("CONNO2"));
				connoh.setMontoTotal(rs.getInt("CONMO1"));
				connoh.setMontoNeto(rs.getInt("CONMO2"));
				connoh.setMontoIva(rs.getInt("CONMO4"));
				connoh.setMontoImptoAdicional(rs.getInt("CONMO5"));
				connoh.setEstado(rs.getString("CONEST"));
				connoh.setResponsableNota(rs.getString("CONRES"));
				
				
				
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
		
		return connoh;
		
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
	
	public int insertaConnod(ConnodDTO dto){
		int correlativo =-100;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		
		String cmp ="CONDEM, CONTI2, CONNU3, CONFE3, CONCOR, CONCO7, CONDI3, CONDE4, CONCAN, CONFOR, CONPRE, CONPNT,"+
				"CONCOS, CONCNT, CONCTN, CONDCL, CONDCN, CONTO1, CONTO2, CONMTN, CONMEX, CONCA1";
		
		String sqlObtenerCldmco ="INSERT INTO  "+
		        " CASEDAT.CONNOD ("+cmp+") "+
				" VALUES (2,'"+dto.getTipoNota()+"',"+dto.getNumeroNota()+","+dto.getFechaNota()+","+dto.getCorrelativo()+","+
		        dto.getCodArticulo()+",'"+dto.getDigArticulo().trim()+"','"+dto.getDescripcion().trim()+"',"+dto.getCantidad()+",'"+
				dto.getFormato().trim()+"',"+(dto.getPrecioUnitario())+","+dto.getPrecioNeto()+","+(dto.getCostoArticulo())+","+
				dto.getCostoArticulo()+","+dto.getCostoTotalNeto()+","+dto.getTotalDescuento()+","+dto.getTotalDescuentoNeto()+","+
				dto.getTotalLinea()+","+dto.getTotalNeto()+","+(dto.getMontoNeto())+","+dto.getMontoExento()+","+dto.getCantidad()+")";
		
				try{
					pstmt = conn.prepareStatement(sqlObtenerCldmco);
					pstmt.executeUpdate();
					
					//log.info("SQL Orden" + sqlObtenerCldmco);
					//log.info("Ok !! insert connod:"+dto.getCodArticulo()+"-"+dto.getDescripcion());
					
					correlativo=1;
					
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
	
	public List obtieneConsolidadoNCp(String numerosNCpes, int codEmpresa,int numeroCarguio, int codBodega){
		
		int 	codArticulo=0;
		String	dvArticulo="";
		int		cantidadConsolidado=0;
		int		precioBruto=0;
		int		precioNeto=0;
		
		ConnodDTO connod = null;
		List lista = new ArrayList();
		
		PreparedStatement pstmt =null;
		PreparedStatement pstmt2 =null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String sqlObtenerConnod="Select CONCO7, CONDI3, SUM(CONCAN) as suma "+
        " FROM CASEDAT.CONNOD " + 
        " WHERE CONDEM="+codEmpresa+" AND CONTI2='P'" +
        " AND CONNU3 IN ("+numerosNCpes+") AND CONCO7<>7777777 "+
        " GROUP BY conco7, condi3"+
        " ORDER BY conco7, condi3"+
        " FOR READ ONLY" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnod);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				codArticulo=rs.getInt("CONCO7");
				dvArticulo=rs.getString("CONDI3");
				cantidadConsolidado=rs.getInt("suma");
				
				precioBruto=0;
				precioNeto=0;
				
				String sqlObtenernetobruto=" SELECT conpre, conpnt " +
				" FROM CASEDAT.CONNOD " +
				" WHERE CONDEM="+codEmpresa+
				" AND CONTI2 = 'P' " +
				" AND CONNU3 IN ("+numerosNCpes+")"+
				" AND CONCO7 ="+codArticulo+
				" AND CONDI3='"+dvArticulo+"' " +
				" ORDER BY CONPRE ASC " +
				" FOR READ ONLY" ;
				try{
					pstmt2 = conn.prepareStatement(sqlObtenernetobruto);
					rs2 = pstmt2.executeQuery();
					while (rs2.next()){
						
						precioBruto=rs2.getInt("CONPRE");
						precioNeto=rs2.getInt("CONPNT");
						
						connod = new ConnodDTO();
						connod.setCodArticulo(codArticulo);
						connod.setDigArticulo(dvArticulo);
						connod.setCantidad(cantidadConsolidado);
						connod.setPrecioNeto(precioNeto);
						connod.setPrecioUnitario(precioBruto);
						lista.add(connod);
						break;	
					}
					
					}catch(Exception e){
						e.printStackTrace();
					}finally {

						try {
							
							if (rs2 != null) { rs2.close();
								 pstmt2.close();
								 }

						} catch (SQLException e1) { }
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

		
		return lista;
		
	}
	
	public int buscaExisteConnod(ConnodDTO nota){
		int correlativo=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT CONNU3 FROM CASEDAT.CONNOD "+
		" Where CONDEM="+nota.getCodigoEmpresa()+" and CONTI2='"+nota.getTipoNota()+"' "+
		" AND CONFE3="+nota.getFechaNota()+" "+
		" AND CONCOR="+nota.getCorrelativo()+" AND CONCO7="+nota.getCodArticulo()+ " FOR READ ONLY " ;
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=1;
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
	
	public int eliminaConnod (ConnodDTO nota){
		int elimina=0;
		PreparedStatement pstmt =null;
		
		String sqlObtenerConnod ="DELETE FROM "+
        " CASEDAT.CONNOD " + 
        " Where CONDEM="+nota.getCodigoEmpresa()+" and CONTI2='"+nota.getTipoNota()+"' "+
        " AND CONFE3="+nota.getFechaNota()+" AND CONCOR="+nota.getCorrelativo()+" AND CONCO7="+nota.getCodArticulo()+ " " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerConnod);
			pstmt.executeUpdate();
			elimina=1;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				
				 pstmt.close();

			} catch (SQLException e1) { }

	  } 
		
		return elimina;
	}
	
public List<ConnodDTO> buscaExisteConnodTransp(ConnodDTO nota){
		
		List<ConnodDTO> lista = new ArrayList<ConnodDTO>();
		ConnodDTO connod = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtener ="SELECT d.CONDEM, d.CONTI2, d.CONNU3, d.CONFE3, d.CONCOR, d.CONCO7, d.CONDI3, d.CONCAN "+
		" FROM CASEDAT.CONNOH c"+
		" INNER JOIN CASEDAT.CONNOD d ON c.CONEMP=d.CONDEM AND c.CONTIP=d.CONTI2 AND c.CONNU2=d.CONNU3 AND c.CONFE2=d.CONFE3 "+
		" Where d.CONDEM="+nota.getCodigoEmpresa()+" and d.CONTI2='"+nota.getTipoNota()+"' "+
		" AND d.CONNU3="+nota.getNumeroNota()+
		" GROUP BY d.CONDEM, d.CONTI2, d.CONNU3, d.CONFE3, d.CONCOR, d.CONCO7, d.CONDI3, d.CONCAN "+
		" ORDER BY d.CONDEM, d.CONTI2, d.CONNU3, d.CONFE3, d.CONCOR, d.CONCO7, d.CONDI3, d.CONCAN "+
		" FOR READ ONLY " ;
		log.info("SELECT DATOS CONNOH Y CONNOD :   "+sqlObtener);
			try{
			pstmt = conn.prepareStatement(sqlObtener);
			rs = pstmt.executeQuery();
			log.info("O K E Y     SELECT DATOS CONNOH Y CONNOD : "+sqlObtener);
			while (rs.next()) {
				connod = new ConnodDTO();
				connod.setCodigoEmpresa(rs.getInt("CONDEM"));
				connod.setTipoNota(rs.getString("CONTI2"));
				connod.setNumeroNota(rs.getInt("CONNU3"));
				connod.setFechaNota(rs.getInt("CONFE3"));
				connod.setCorrelativo(rs.getInt("CONCOR"));
				connod.setCodArticulo(rs.getInt("CONCO7"));
				connod.setDigArticulo(rs.getString("CONDI3"));
				connod.setCantidad(rs.getInt("CONCAN"));
				lista.add(connod);
				
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

		return lista;
		
	}

public int eliminaConnodTransp (ConnodDTO nota){
	int elimina=0;
	PreparedStatement pstmt =null;
	
	String sqlObtenerConnod ="DELETE FROM "+
    " CASEDAT.CONNOD " + 
    " Where CONDEM="+nota.getCodigoEmpresa()+" and CONTI2='"+nota.getTipoNota()+"' "+
    " AND CONNU3="+nota.getNumeroNota()+ " " ;
	log.info("ANTES DE DELETE CONNOD  :  "+sqlObtenerConnod);
	try{
		pstmt = conn.prepareStatement(sqlObtenerConnod);
		pstmt.executeUpdate();
		elimina=1;
		log.info("O K E Y  DELETE CONNOD  :  "+sqlObtenerConnod);
	}
	catch(Exception e){
		e.printStackTrace();
	}finally {

		try {
			
			 pstmt.close();

		} catch (SQLException e1) { }

  } 
	
	return elimina;
}
}
