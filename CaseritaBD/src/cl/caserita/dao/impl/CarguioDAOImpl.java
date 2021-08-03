package cl.caserita.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmrecDAO;
import cl.caserita.dto.CargonwDTO;
import cl.caserita.dto.CarguioDTO;
import cl.caserita.dto.CarguioDetalleDTO;
import cl.caserita.dto.CarguioTranspDTO;
import cl.caserita.dto.CarguiodDTO;
import cl.caserita.dto.CasesmtpDTO;
import cl.caserita.dto.ClidirDTO;
import cl.caserita.dto.DetordDTO;
import cl.caserita.dto.DetordTranspDTO;
import cl.caserita.dto.DimensionesDTO;
import cl.caserita.dto.ExdartDTO;
import cl.caserita.dto.OrdvtaDTO;
import cl.caserita.dto.RedespachocDTO;
import cl.caserita.dto.RedespachodDTO;
import cl.caserita.dto.VecmarDTO;
import cl.caserita.dto.ExmtraDTO;
import cl.caserita.dto.OrdTranspDTO;
import cl.caserita.dto.ExdtraDTO;
import cl.caserita.dto.ExmartDTO;
import cl.caserita.dto.ExmrecDTO;

public class CarguioDAOImpl implements CarguioDAO{

	private  static Logger log = Logger.getLogger(CarguioDAOImpl.class);

	private Connection conn;
	
	public CarguioDAOImpl(Connection conn){
		this.conn=conn;
	}
	public List listaCarguios(int codigoEmpresa, String estado){
		CarguioDTO carguioDTO = null;
		OrdvtaDTO ordvtaDTO = null;
		PreparedStatement pstmt =null;
		List carguios = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select  "+
        " C1.CACEMP, C1.NUMCAR, C2.NUMVEN, C1.NUMRUT , C1.CAMCO1 , C2.CLMRUT,C2.CLMDIG FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2 " + 
        " WHERE C1.CACEMP="+codigoEmpresa+" AND C1.CAMCO1 =26 AND C1.CAMES1 ='"+estado+"' AND C1.CACEMP= C2.CACEMP AND C1.NUMCAR= C2.NUMCAR AND C1.VECPAT= C2.VECPAT AND C1.CAMCO1=C2.CAMCO1 GROUP BY C1.CACEMP, C1.NUMCAR, C2.NUMVEN , C1.NUMRUT, C1.CAMCO1,C2.CLMRUT, C2.CLMDIG  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			log.info("SQL CARGUIO" + sqlObtenerCamtra);
			
			List ordenes=null;
			int numeroCarguio=0;
			int contador=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
					
					ordvtaDTO = new OrdvtaDTO();
					
					if (numeroCarguio!=rs.getInt("NUMCAR")){
						if (contador>0){
							carguios.add(carguioDTO);
							carguioDTO = null;
						}
						
						numeroCarguio=rs.getInt("NUMCAR");
						carguioDTO = new CarguioDTO();
						carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
						carguioDTO.setNumeroRuta(rs.getInt("NUMRUT"));
						carguioDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
						carguioDTO.setCodigoBodega(rs.getInt("CAMCO1"));
						ordenes = new ArrayList();
						
					}
					
					ordvtaDTO = obtieneOrdenes(rs.getInt("CACEMP"),rs.getInt("NUMVEN"),rs.getInt("CAMCO1"));
					DimensionesDTO dimen = obtieneVolPesoCaja(rs.getInt("CACEMP"), rs.getInt("CAMCO1"), rs.getInt("NUMVEN"));
					if (dimen!=null){
						ordvtaDTO.setVolumen(dimen.getVolumen());
						ordvtaDTO.setCantidadCaja(dimen.getCantidadCajas());
						ordvtaDTO.setPeso(dimen.getPeso());
					}else{
						ordvtaDTO.setVolumen(0);
						ordvtaDTO.setCantidadCaja(0);
						ordvtaDTO.setPeso(0);
					}
					
					
					carguioDTO.getOrdenes().add(ordvtaDTO);
					contador=contador+1;
					
					
				
			}
			if (carguioDTO!=null){
				carguios.add(carguioDTO);
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
		return carguios;
	}
	
	public List listarCarguiosTranfeWms(int codigoEmpresa, String estado, int CarguioTranf, int codigoBodega){
		
		CarguioDTO carguioDTO = null;
		OrdvtaDTO ordvtaDTO = null;
		CarguiodDTO carguiodDTO = null;
		List carguioDD=new ArrayList();
		PreparedStatement pstmt =null;
		List carguios = new ArrayList();
		ResultSet rs = null;
		
		//BUSCA TODOS LOS CARGUIOS ASOCIADOS AL CARGUIO DE TRANSFERENCIA
		String sqlObtenerCamtra = " SELECT C1.cacemp, C1.numcar, C1.vecpat, C1.camco1, C1.camrut, C1.exmdi3, C1.exmfec, "+
			" C1.profe7, C1.cames1, C1.totcaj, C1.horlle, C1.horsal, C1.estaut, C1.tipcar, C1.codbde, "+
			" C1.sellcar, C1.numcvi, C1.totmnc, C1.totmca, C1.totcca, C1.numrut, C1.cantov, C1.cancde, C1.candde, C3.codein, C2.numven, C2.cldcod, C2.cordca, C2.cldcan, C2.venfor, C4.exmdig, C2.CLICOR, C2.CLICO1, C2.CLICO2, C2.CLICO3, C2.VENCA2 "+
			" FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3, CASEDAT.EXMART C4 " + 
			" WHERE C1.CACEMP = "+codigoEmpresa +
			" AND C1.CAMCO1 = "+codigoBodega + 
			" AND C1.NUMCVI = "+CarguioTranf +
			" AND C1.NUMCVI <> C1.NUMCAR " +
			" AND C1.TIPCAR = 'N' " +
			" AND C1.CACEMP = C2.CACEMP " +
			" AND C1.NUMCAR = C2.NUMCAR " + 
			" AND C1.VECPAT = C2.VECPAT " +
			" AND C1.CAMCO1 = C2.CAMCO1 " +
			" AND C1.CACEMP = C3.CACEMP " +
			" AND C1.NUMCAR = C3.NUMCAR " +
			" AND C1.VECPAT = C3.VECPAT " +
			" AND C1.CAMCO1 = C3.CAMCO1 " +
			" AND C2.CLDCOD = C4.EXMCOD " +
			" GROUP BY C1.cacemp, C1.numcar, C1.vecpat, C1.camco1, C1.camrut, C1.exmdi3, C1.exmfec, "+
			" C1.profe7, C1.cames1, C1.totcaj, C1.horlle, C1.horsal, C1.estaut, C1.tipcar, C1.codbde, "+
			" C1.sellcar, C1.numcvi, C1.totmnc, C1.totmca, C1.totcca, C1.numrut, C1.cantov, C1.cancde, C1.candde, C3.codein, C2.numven, C2.cldcod, C2.cordca, C2.cldcan, C2.venfor, C4.exmdig, C2.CLICOR, C2.CLICO1, C2.CLICO2, C2.CLICO3, C2.VENCA2  "+
			" HAVING((SELECT COUNT(*) FROM CASEDAT.CARGUIOC C4 WHERE C4.cacemp = C1.cacemp AND C4.camco1 = C1.camco1 AND C4.numcar = C1.numcvi AND C4.cames1 = '"+estado+"') > 0) "+
			" ORDER BY C1.NUMCAR , C2.NUMVEN " +
			" FOR READ ONLY" ;
			
			log.info("Consulta CARGUIO TRANSFERENCIA " + sqlObtenerCamtra);
			
		try{			
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			List ordenes=new ArrayList();
			int numeroCarguio=0;
			int contador=0;
			int numeroOV=0;
			int CantidUnid=0;
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
							
				if (numeroCarguio!=rs.getInt("NUMCAR")){
					
					if (contador>0){
						if (numeroOV!=rs.getInt("NUMVEN")){
							numeroOV = rs.getInt("NUMVEN");
							if (ordvtaDTO!=null){
								ordvtaDTO.setDetord(ordenes);
								carguioDTO.getOrdenes().add(ordvtaDTO);
								ordenes=new ArrayList();

							}
						}
						carguios.add(carguioDTO);
						carguioDTO = null;
					}
					
					carguiodDTO = new CarguiodDTO();
					numeroCarguio=rs.getInt("NUMCAR");
					carguioDTO = new CarguioDTO();
					carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
					carguioDTO.setNumeroRuta(rs.getInt("NUMRUT"));
					carguioDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
					carguioDTO.setCodigoBodega(rs.getInt("CAMCO1"));
					carguioDTO.setRutChofer(rs.getInt("CAMRUT"));
					carguioDTO.setPatente(rs.getString("VECPAT"));

					carguioDTO.setDvChofer(rs.getString("EXMDI3"));
					carguioDTO.setEstadoInvWMS(rs.getString("CODEIN"));
					carguioDTO.setnumeroCarguioTransf(rs.getInt("NUMCVI"));		
				}
				if (numeroOV!=rs.getInt("NUMVEN")){
					numeroOV = rs.getInt("NUMVEN");
					if (ordvtaDTO!=null){
						ordvtaDTO.setDetord(ordenes);
						carguioDTO.getOrdenes().add(ordvtaDTO);
						ordenes=new ArrayList();

					}
				}
				
				//busco datos de Pallet, Display, Caja del articulo
				ExmartDTO exmartDTO3 = recuperaArticulo(rs.getInt("CLDCOD"), rs.getString("EXMDIG"));
				
				if ("C".equals(rs.getString("VENFOR").trim())){
					if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0){
						CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getCaja() * exmartDTO3.getDisplay());
					};
					
					if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0){
						CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getCaja();
					};
					
					if (exmartDTO3.getCaja()==0 && exmartDTO3.getDisplay()>0){
						CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
					};
				}
				
				if ("D".equals(rs.getString("VENFOR").trim())){
					if (exmartDTO3.getDisplay()>0){
						CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
					}
				}
				
				if ("P".equals(rs.getString("VENFOR").trim())){
					
					if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()>0){
						CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getPallet() * exmartDTO3.getCaja() * exmartDTO3.getDisplay());
					}
					
					if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()==0){
						CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getCaja() * exmartDTO3.getDisplay());
					};
					
					if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0 && exmartDTO3.getPallet()==0){
						CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getCaja();
					};
					
					if (exmartDTO3.getCaja()==0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()==0){
						CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
					};
					
					if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0 && exmartDTO3.getPallet()>0){
						CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getPallet() * exmartDTO3.getCaja());
					};
					
				}
				
				if ("U".equals(rs.getString("VENFOR").trim())){
					CantidUnid=rs.getInt("VENCA2");
				}
				
				ordvtaDTO = obtieneOrdenes(rs.getInt("CACEMP"),rs.getInt("NUMVEN"),rs.getInt("CAMCO1"));
				if (numeroOV==rs.getInt("NUMVEN")){
					if (rs.getInt("CLDCOD")!=7777777 && rs.getInt("CLDCOD")!=22){
						DetordDTO detord = new DetordDTO();
						detord.setCorrelativoDetalleOV(rs.getInt("CORDCA"));
						detord.setCodigoArticulo(rs.getInt("CLDCOD"));
						detord.setCantidadArticulo(CantidUnid);
						
						//nuevo
						detord.setCorrelativoDespacho(rs.getInt("CLICOR"));
						detord.setCodRegion(rs.getInt("CLICO1"));
						detord.setCodCiudad(rs.getInt("CLICO2"));
						detord.setCodComuna(rs.getInt("CLICO3"));
						
						ordenes.add(detord);
					}
				}
				
				carguiodDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
				carguiodDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
				carguiodDTO.setNumeroOrden(rs.getInt("NUMVEN"));
				carguiodDTO.setCodigoBodega(rs.getInt("CAMCO1"));
				carguioDTO.setPatente(rs.getString("VECPAT"));
				
				carguiodDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
				carguiodDTO.setCantidad(CantidUnid);
				carguiodDTO.setCorrelativo(rs.getInt("CORDCA"));
				carguioDD.add(carguiodDTO);
				contador=contador+1;
			}
			carguios.add(carguioDTO);

			ordvtaDTO.setDetord(ordenes);
			carguioDTO.getOrdenes().add(ordvtaDTO);
			carguioDTO.setCarguioD(carguioDD);

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
		return carguios;
	}
	
	/*
	public CarguioDTO listaCarguiosTranfeWms(int codigoEmpresa, String estado, int CarguioTranf, int codigoBodega){
		CarguioDTO carguioDTO = null;
		OrdvtaDTO ordvtaDTO = null;
		CarguiodDTO carguiodDTO = null;
		List carguioDD=new ArrayList();
		PreparedStatement pstmt =null;
		List carguios = new ArrayList();
		ResultSet rs = null; 
		
		//BUSCA TODOS LOS CARGUIOS ASOCIADOS AL CARGUIO DE TRANSFERENCIA
		String sqlObtenerCamtra = " SELECT * "+
		        " FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3 " + 
		        " WHERE C1.CACEMP = "+codigoEmpresa +
		        " AND C1.CAMCO1 = "+codigoBodega + 
		        " AND C1.CAMES1 = '"+estado+"'" +
		        " AND C1.NUMCVI = "+CarguioTranf +
		        " AND C1.NUMCVI <> C1.NUMCAR " +
		        " AND C1.TIPCAR = 'N' " +
		        " AND C1.CACEMP = C2.CACEMP " +
		        " AND C1.NUMCAR = C2.NUMCAR " + 
		        " AND C1.VECPAT = C2.VECPAT " +
		        " AND C1.CAMCO1 = C2.CAMCO1 " +
		        " AND C1.CACEMP = C3.CACEMP " +
		        " AND C1.NUMCAR = C3.NUMCAR " +
		        " AND C1.VECPAT = C3.VECPAT " +
		        " AND C1.CAMCO1 = C3.CAMCO1 " +
		        " ORDER BY C1.NUMCAR " +
		        " FOR READ ONLY" ;
				
		try{
			
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			List ordenes=new ArrayList();
			int numeroCarguio=0;
			int contador=0;
			int numeroOV=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
					
					if (numeroCarguio!=rs.getInt("NUMCAR")){
						if (contador>0){
							carguios.add(carguioDTO);
							carguioDTO = null;
						}
						carguiodDTO = new CarguiodDTO();
						numeroCarguio=rs.getInt("NUMCAR");
						carguioDTO = new CarguioDTO();
						carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
						carguioDTO.setNumeroRuta(rs.getInt("NUMRUT"));
						carguioDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
						carguioDTO.setCodigoBodega(rs.getInt("CAMCO1"));
						carguioDTO.setRutChofer(rs.getInt("CAMRUT"));
						carguioDTO.setDvChofer(rs.getString("EXMDI3"));
						carguioDTO.setEstadoInvWMS(rs.getString("CODEIN"));
						carguioDTO.setnumeroCarguioTransf(rs.getInt("NUMCVI"));
						
						log.info("CARGUIO:"+numeroCarguio);
						
					}
					
					if (numeroOV!=rs.getInt("NUMVEN")){
						numeroOV = rs.getInt("NUMVEN");
						if (ordvtaDTO!=null){
							ordvtaDTO.setDetord(ordenes);
							carguioDTO.getOrdenes().add(ordvtaDTO);

						}
						log.info("OV:"+numeroOV);
					}
					
					ordvtaDTO = obtieneOrdenes(rs.getInt("CACEMP"),rs.getInt("NUMVEN"),rs.getInt("CAMCO1"));
					if (numeroOV==rs.getInt("NUMVEN")){
						DetordDTO detord = new DetordDTO();
						detord.setCorrelativoDetalleOV(rs.getInt("CORDCA"));
						detord.setCodigoArticulo(rs.getInt("CLDCOD"));
						detord.setCantidadArticulo(rs.getInt("CLDCAN"));
						ordenes.add(detord);
						
						log.info("ARTICULO:"+rs.getInt("CLDCOD"));
					}
					
					carguiodDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
					carguiodDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
					carguiodDTO.setNumeroOrden(rs.getInt("NUMVEN"));
					carguiodDTO.setCodigoBodega(rs.getInt("CAMCO1"));
					carguiodDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
					carguiodDTO.setCantidad(rs.getInt("CLDCAN"));
					carguiodDTO.setCorrelativo(rs.getInt("CORDCA"));
					carguioDD.add(carguiodDTO);
					contador=contador+1;
			}
			ordvtaDTO.setDetord(ordenes);
			carguioDTO.getOrdenes().add(ordvtaDTO);
			carguioDTO.setCarguioD(carguioDD);
			
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
		return carguioDTO;
	}
	*/
	
	public CarguioDTO listaCarguiosOTWms(int codigoEmpresa, String estado, int Carguio, int codigoBodega){
		
		
		CarguioDTO carguioDTO = null;
		ExmtraDTO exmtraDTO = null;
		CarguiodDTO carguiodDTO = null;
		List carguioDD=new ArrayList();
		PreparedStatement pstmt =null;
		List carguios = new ArrayList();
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
        " FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3, CASEDAT.EXMART C4 " + 
        " WHERE C1.CACEMP = "+codigoEmpresa +
        " AND C1.NUMCAR = "+Carguio +
        " AND C1.CAMCO1 = "+codigoBodega + 
        " AND C1.CAMES1 = '"+estado+"'" +
        " AND C1.CACEMP = C2.CACEMP " +
        " AND C1.NUMCAR = C2.NUMCAR " + 
        " AND C1.VECPAT = C2.VECPAT " +
        " AND C1.CAMCO1 = C2.CAMCO1 " +
        " AND C2.CACEMP = C3.CACEMP " +
        " AND C2.NUMCAR = C3.NUMCAR " + 
        " AND C2.VECPAT = C3.VECPAT " +
        " AND C2.CAMCO1 = C3.CAMCO1 " +
        " AND C2.EXINUM > 0 " +
        " AND C2.CLDCOD = C4.EXMCOD " +
        " FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			List ordenesOT=new ArrayList();
			int numeroCarguio=0;
			int contador=0;
			int numeroOT=0;
			int CantidUnid=0;
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					//ordvtaDTO = new OrdvtaDTO();
					
					if (numeroCarguio!=rs.getInt("NUMCAR")){
						if (contador>0){
							carguios.add(carguioDTO);
							carguioDTO = null;
						}
						carguiodDTO = new CarguiodDTO();
						numeroCarguio=rs.getInt("NUMCAR");
						carguioDTO = new CarguioDTO();
						carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
						carguioDTO.setNumeroRuta(rs.getInt("NUMRUT"));
						carguioDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
						carguioDTO.setCodigoBodega(rs.getInt("CAMCO1"));
						carguioDTO.setRutChofer(rs.getInt("CAMRUT"));
						carguioDTO.setDvChofer(rs.getString("EXMDI3"));
						carguioDTO.setEstadoInvWMS(rs.getString("CODEIN"));
						carguioDTO.setPatente(rs.getString("VECPAT"));
						carguioDTO.setnumeroOT(rs.getInt("EXINUM"));
						carguioDTO.setcodigoBodOrigen(rs.getInt("EXIBO4"));
						carguioDTO.setcodigoBodOrigen(rs.getInt("EXIBO5"));
					}
					if (numeroOT!=rs.getInt("EXINUM")){
						numeroOT = rs.getInt("EXINUM");
						
						if (exmtraDTO!=null){
							exmtraDTO.setExdtra(ordenesOT);
							carguioDTO.getOrdenesOT().add(exmtraDTO);
							
							
							ordenesOT=new ArrayList();
						}
					}
					
					exmtraDTO = obtieneOTs(rs.getInt("CACEMP"),rs.getInt("EXINUM"),rs.getInt("EXIBO4"),rs.getInt("EXIBO5"));
					
					//busco datos de Pallet, Display, Caja del articulo
					ExmartDTO exmartDTO3 = recuperaArticulo(rs.getInt("CLDCOD"), rs.getString("EXMDIG"));
					
					if ("C".equals(rs.getString("VENFOR").trim())){
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getCaja() * exmartDTO3.getDisplay());
						};
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getCaja();
						};
						
						if (exmartDTO3.getCaja()==0 && exmartDTO3.getDisplay()>0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
						};
					}
					
					if ("D".equals(rs.getString("VENFOR").trim())){
						if (exmartDTO3.getDisplay()>0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
						}
					}
					
					if ("P".equals(rs.getString("VENFOR").trim())){
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()>0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getPallet() * exmartDTO3.getCaja() * exmartDTO3.getDisplay());
						}
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()==0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getCaja() * exmartDTO3.getDisplay());
						};
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0 && exmartDTO3.getPallet()==0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getCaja();
						};
						
						if (exmartDTO3.getCaja()==0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()==0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
						};
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0 && exmartDTO3.getPallet()>0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getPallet() * exmartDTO3.getCaja());
						};
					}
					
					if ("U".equals(rs.getString("VENFOR").trim())){
						CantidUnid=rs.getInt("VENCA2");
					}
					
					if (numeroOT==rs.getInt("EXINUM")){
						ExdtraDTO exdtra = new ExdtraDTO();
						exdtra.setLinea(rs.getInt("CORDCA"));
						exdtra.setCodArticulo(rs.getInt("CLDCOD"));
						exdtra.setCantDespachada(CantidUnid);
						ordenesOT.add(exdtra);
					}
					
					carguiodDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
					carguiodDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
					carguioDTO.setPatente(rs.getString("VECPAT"));
					
					carguiodDTO.setNumeroOrden(rs.getInt("NUMVEN"));
					carguiodDTO.setNumeroTraspaso(rs.getInt("EXINUM"));
					carguiodDTO.setCodigoBodega(rs.getInt("CAMCO1"));
					carguiodDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
					carguiodDTO.setCantidad(CantidUnid);
					carguiodDTO.setCorrelativo(rs.getInt("CORDCA"));
					carguioDD.add(carguiodDTO);
					contador=contador+1;
			}
			
			exmtraDTO.setExdtra(ordenesOT);
			carguioDTO.getOrdenesOT().add(exmtraDTO);
			carguioDTO.setCarguioD(carguioDD);
			
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
		return carguioDTO;
	}

	public CarguioDTO obtieneCarguioDTO(int codigoEmpresa, int Carguio, int codigoBodega){
		CarguioDTO carguioDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		//Busca solo las OTs del Carguio
		String sqlObtenerCamtra=" Select * "+
        " FROM CASEDAT.CARGUIOC C1 " + 
        " WHERE C1.CACEMP = "+codigoEmpresa +
        " AND C1.NUMCAR = "+Carguio +
        " AND C1.CAMCO1 = "+codigoBodega + 
        " FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {					
						carguioDTO = new CarguioDTO();
						carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
						carguioDTO.setNumeroRuta(rs.getInt("NUMRUT"));
						carguioDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
						carguioDTO.setCodigoBodega(rs.getInt("CAMCO1"));
						carguioDTO.setRutChofer(rs.getInt("CAMRUT"));
						carguioDTO.setDvChofer(rs.getString("EXMDI3"));
						carguioDTO.setPatente(rs.getString("VECPAT"));
						
						
					
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
		return carguioDTO;
	}

	
	
	//***********
	
	public CarguioDTO listaCarguiosWms(int codigoEmpresa, String estado, int Carguio, int codigoBodega){
		//DAOFactory dao = DAOFactory.getInstance();
		//ExmartDAO exmart = dao.getExmartDAO();
		
		CarguioDTO carguioDTO = null;
		OrdvtaDTO ordvtaDTO = null;
		CarguiodDTO carguiodDTO = null;
		List carguioDD=new ArrayList();
		PreparedStatement pstmt =null;
		List carguios = new ArrayList();
		ResultSet rs = null;
		
		ExmartDTO exmartDTO = null;
		
		String sqlObtenerCamtra="Select * "+
        " FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3, CASEDAT.EXMART C4 " + 
        " WHERE C1.CACEMP = "+codigoEmpresa+" AND C1.CAMCO1 = "+codigoBodega+" AND C1.CAMES1 = '"+estado+"'" +
        " AND C1.NUMCAR = "+Carguio+" AND C1.CACEMP = C2.CACEMP AND C1.NUMCAR = C2.NUMCAR " + 
        " AND C1.VECPAT = C2.VECPAT " +
        " AND C1.CAMCO1 = C2.CAMCO1 " +
        " AND C1.CACEMP = C3.CACEMP " +
        " AND C1.NUMCAR = C3.NUMCAR " +
        " AND C1.VECPAT = C3.VECPAT " +
        " AND C1.CAMCO1 = C3.CAMCO1 " +
        " AND C2.CLDCOD = C4.EXMCOD " +
        " ORDER BY C2.NUMVEN, C2.CLICOR "+
        " FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			log.info("CONSULTA CARGUIO "+sqlObtenerCamtra);
			
			List ordenes=new ArrayList();
			int numeroCarguio=0;
			int contador=0;
			int numeroOV=0;
			int CantidUnid=0;
			int corrDirec=0;
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
					//ordvtaDTO = new OrdvtaDTO();
					
					if (numeroCarguio!=rs.getInt("NUMCAR")){
						if (contador>0){
							carguios.add(carguioDTO);
							carguioDTO = null;
						}
						carguiodDTO = new CarguiodDTO();
						numeroCarguio=rs.getInt("NUMCAR");
						carguioDTO = new CarguioDTO();
						carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
						carguioDTO.setNumeroRuta(rs.getInt("NUMRUT"));
						carguioDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
						carguioDTO.setCodigoBodega(rs.getInt("CAMCO1"));
						carguioDTO.setRutChofer(rs.getInt("CAMRUT"));
						carguioDTO.setDvChofer(rs.getString("EXMDI3"));
						carguioDTO.setPatente(rs.getString("VECPAT"));
						carguioDTO.setEstadoInvWMS(rs.getString("CODEIN"));
						
					}
					
					if (numeroOV!=rs.getInt("NUMVEN")){
						if(corrDirec!=rs.getInt("CLICOR")){
							if (ordvtaDTO!=null){
								ordvtaDTO.setDetord(ordenes);
								carguioDTO.getOrdenes().add(ordvtaDTO);
								ordenes = new ArrayList();
							}
						}
						else{
							if (ordvtaDTO!=null){
								ordvtaDTO.setDetord(ordenes);
								carguioDTO.getOrdenes().add(ordvtaDTO);
								ordenes = new ArrayList();
							}
						}
						numeroOV = rs.getInt("NUMVEN");
						corrDirec=rs.getInt("CLICOR");
					}
					//nuevo
					else if (numeroOV==rs.getInt("NUMVEN")){
						if(corrDirec!=rs.getInt("CLICOR")){
							corrDirec=rs.getInt("CLICOR");
							if (ordvtaDTO!=null){
								ordvtaDTO.setDetord(ordenes);
								carguioDTO.getOrdenes().add(ordvtaDTO);
								ordenes = new ArrayList();
							}
						}
					}
					
					
					//busco datos de Pallet, Display, Caja del articulo
					ExmartDTO exmartDTO3 = recuperaArticulo(rs.getInt("CLDCOD"), rs.getString("EXMDIG"));
					
					if ("C".equals(rs.getString("VENFOR").trim())){
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getCaja() * exmartDTO3.getDisplay());
						};
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getCaja();
						};
						
						if (exmartDTO3.getCaja()==0 && exmartDTO3.getDisplay()>0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
						};
					}
					
					if ("D".equals(rs.getString("VENFOR").trim())){
						if (exmartDTO3.getDisplay()>0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
						}
					}
					
					if ("P".equals(rs.getString("VENFOR").trim())){
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()>0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getPallet() * exmartDTO3.getCaja() * exmartDTO3.getDisplay());
						}
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()==0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getCaja() * exmartDTO3.getDisplay());
						};
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0 && exmartDTO3.getPallet()==0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getCaja();
						};
						
						if (exmartDTO3.getCaja()==0 && exmartDTO3.getDisplay()>0 && exmartDTO3.getPallet()==0){
							CantidUnid=rs.getInt("VENCA2") * exmartDTO3.getDisplay();
						};
						
						if (exmartDTO3.getCaja()>0 && exmartDTO3.getDisplay()==0 && exmartDTO3.getPallet()>0){
							CantidUnid=rs.getInt("VENCA2") * (exmartDTO3.getPallet() * exmartDTO3.getCaja());
						};
					}
					
					if ("U".equals(rs.getString("VENFOR").trim())){
						CantidUnid=rs.getInt("VENCA2");
					}
					
					ordvtaDTO = obtieneOrdenes(rs.getInt("CACEMP"),rs.getInt("NUMVEN"),rs.getInt("CAMCO1"));
					if (numeroOV==rs.getInt("NUMVEN")){
						if (rs.getInt("CLDCOD")!=7777777 && rs.getInt("CLDCOD")!=22){
							DetordDTO detord = new DetordDTO();
							
							detord.setCorrelativoDetalleOV(rs.getInt("CORDCA"));
							detord.setCodigoArticulo(rs.getInt("CLDCOD"));
							detord.setCantidadArticulo(CantidUnid);
							
							//nuevo
							detord.setCorrelativoDespacho(rs.getInt("CLICOR"));
							detord.setCodRegion(rs.getInt("CLICO1"));
							detord.setCodCiudad(rs.getInt("CLICO2"));
							detord.setCodComuna(rs.getInt("CLICO3"));
							
							ordenes.add(detord);
						}
					}
					
					carguiodDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
					carguiodDTO.setCodigoEmpresa(rs.getInt("CACEMP"));
					carguiodDTO.setNumeroOrden(rs.getInt("NUMVEN"));
					carguiodDTO.setCodigoBodega(rs.getInt("CAMCO1"));
					carguiodDTO.setCodigoArticulo(rs.getInt("CLDCOD"));
					carguioDTO.setPatente(rs.getString("VECPAT"));
					
					carguiodDTO.setCantidad(CantidUnid);
					carguiodDTO.setCorrelativo(rs.getInt("CORDCA"));
					carguioDD.add(carguiodDTO);
					contador=contador+1;
			}
			ordvtaDTO.setDetord(ordenes);
			carguioDTO.getOrdenes().add(ordvtaDTO);
			carguioDTO.setCarguioD(carguioDD);

			
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
		return carguioDTO;
	}
	
	
	
	public HashMap listaCarguiosValidaConfirmacion(int codigoEmpresa, String estado, int Carguio, int codigoBodega){
		CarguioDTO carguioDTO = null;
		
		HashMap <Integer, OrdvtaDTO> ordenesHash = new HashMap();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select C1.CACEMP ,C1.NUMCAR, C1.CAMCO1 , ORDEMP, NUMVEN "+
        " FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3 " + 
        " WHERE C1.CACEMP = "+codigoEmpresa+" AND C1.CAMCO1 = "+codigoBodega+" AND C1.CAMES1 = '"+estado+"'" +
        " AND C1.NUMCAR = "+Carguio+" AND C1.CACEMP = C2.CACEMP AND C1.NUMCAR = C2.NUMCAR " + 
        " AND C1.VECPAT = C2.VECPAT " +
        " AND C1.CAMCO1 = C2.CAMCO1 " +
        " AND C1.CACEMP = C3.CACEMP " +
        " AND C1.NUMCAR = C3.NUMCAR " +
        " AND C1.VECPAT = C3.VECPAT " +
        " AND C1.CAMCO1 = C3.CAMCO1 " +
        " GROUP BY C1.CACEMP ,C1.NUMCAR, C1.CAMCO1 , C2.ORDEMP, C2.NUMVEN" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			int numeroCarguio=0;
			int contador=0;
			int numeroOV=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
					//ordvtaDTO = new OrdvtaDTO();
					
					int empresa = rs.getInt("CACEMP");
					int bodega = rs.getInt("CAMCO1");
					int numov = rs.getInt("NUMVEN");
					//log.info(numov);
					ordenesHash.put(Integer.valueOf(rs.getInt("NUMVEN")),obtieneOrdenesCarguio(empresa,numov,bodega));
					
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
		return ordenesHash;
	}
	
	
	
	
	public CarguioDTO obtieneCarguio(int codigoEmpresa, String estado, int bodega, int numeroCarguio){
		CarguioDTO carguioDTO = null;
		OrdvtaDTO ordvtaDTO = null;
		PreparedStatement pstmt =null;
		List carguios = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select  "+
        " * FROM CASEDAT.CARGUIOC " + 
        " WHERE CACEMP="+codigoEmpresa+" AND CAMCO1 ="+bodega+" AND NUMCAR= "+numeroCarguio+"   FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				carguioDTO = new CarguioDTO();
						carguioDTO.setNumeroCarguio(rs.getInt("NUMCAR"));
						carguioDTO.setFechaCarguio(rs.getInt("EXMFEC"));
						carguioDTO.setPatente(rs.getString("VECPAT"));
						
						
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
		return carguioDTO;
	}
	
	public List obtienedevolucionCarguio(int codigoEmpresa, String estado, int bodega, int numeroCarguio, String tipcar){
		CargonwDTO cargonw = null;
		OrdvtaDTO ordvtaDTO = null;
		PreparedStatement pstmt =null;
		List consolid = new ArrayList();
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select  "+
        " * FROM CASEDAT.CARGCONW " + 
        " WHERE CACEMP="+codigoEmpresa+" AND CAMCO1 ="+bodega+" AND NUMCAR= "+numeroCarguio+" AND TIPCAR = '"+tipcar+"'" + " FOR READ ONLY" ;
		log.info("SQL:"+sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cargonw = new CargonwDTO();
						cargonw.setCodEmpresa(rs.getInt("CACEMP"));
						cargonw.setNumeroCarguio(rs.getInt("NUMCAR"));
						cargonw.setPatente(rs.getString("VECPAT"));
						cargonw.setCodBodega(rs.getInt("CAMCO1"));
						cargonw.setCodArticulo(rs.getInt("CLDCOD"));
						cargonw.setDvArticulo(rs.getString("CONDI3"));
						cargonw.setCantidad(rs.getInt("CLDCAN"));
						cargonw.setFechaExpiracion(rs.getInt("ADSFEC"));
						consolid.add(cargonw);
						
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
		return consolid;
	}
	
	public int verificaOVCarguio(int codigoEmpresa, int bodega, int numeroCarguio, int numeroOV){
		
		int existe=0;
		PreparedStatement pstmt =null;
		
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select  "+
        " * FROM CASEDAT.CARGUIOD " + 
        " WHERE CACEMP="+codigoEmpresa+" AND CAMCO1 ="+bodega+" AND NUMCAR= "+numeroCarguio+" AND NUMVEN="+numeroOV+"  fetch first 1 rows only FOR READ ONLY" ;
		log.info("Query Carguio:"+sqlObtenerCamtra);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				existe=1;
						
						
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
		return existe;
	}
	
	public ExmtraDTO obtieneOTs(int empresa, int numeroOT, int codigoBodOrigen, int codigoBodDestino){
		ExmtraDTO exmtraDTO = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMTRA " + 
        " WHERE EXIMEM = "+empresa+
        " AND EXINUM = "+numeroOT+
        " AND EXIBO4 = "+codigoBodOrigen+
        " AND EXIBO5 = "+codigoBodDestino+
        " FOR READ ONLY";
		
		List ordvta = new ArrayList();
		List lisCli = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);	
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				exmtraDTO = new ExmtraDTO();
				exmtraDTO.setCodigoEmpresa(rs.getInt("EXIMEM"));
				exmtraDTO.setNumTraspaso(rs.getInt("EXINUM"));
				exmtraDTO.setBodegaOrigen(rs.getInt("EXIBO4"));
				exmtraDTO.setBodegaDestino(rs.getInt("EXIBO5"));
				exmtraDTO.setFechaTraspaso(rs.getInt("EXIFEC"));
				
			}
			//ordvtaDTO.setClidir(lisCli);
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {
				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }
			} catch (SQLException e1) { }
	  } 
		return exmtraDTO;

	}
	
	
	public OrdvtaDTO obtieneOrdenes(int empresa, int numeroOV, int codigoBodega){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND TPTC18="+codigoBodega+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		List lisCli = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
//			log.info("SQL ORDVTA" + sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ordvtaDTO = new OrdvtaDTO();
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setFechaOrden(rs.getInt("EXMFEC"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				int correlativo = obtieneDireccionDespacho(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente());
				
				ClidirDTO clidir = recuperaDatosCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente(), correlativo);
				ordvtaDTO.setCorreDireccionOV(correlativo);
				
				if (clidir!=null){
					ordvtaDTO.setLatitud(clidir.getLatitud().trim());
					ordvtaDTO.setLongitud(clidir.getLongitud().trim());
				}
				else{
					ordvtaDTO.setLatitud("");
					ordvtaDTO.setLongitud("");
					//log.info("cliente sin direccion CLIDIR y CLIDIRA : "+correlativo);
				}
				lisCli.add(clidir);
				
			}
			ordvtaDTO.setClidir(lisCli);
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return ordvtaDTO;
	}
	
	public OrdvtaDTO obtieneOrdenesCarguio(int empresa, int numeroOV, int codigoBodega){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		//HashMap <Integer, OrdvtaDTO> orden = new HashMap();
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA O1, CASEDAT.DETORD D1" + 
        " Where O1.ORDEMP="+empresa+" AND O1.NUMVEN="+numeroOV+" AND O1.TPTC18="+codigoBodega+" AND O1.ORDEMP=D1.ORDEMP AND O1.NUMVEN=D1.NUMVEN AND O1.TPTC18=D1.TPTC18 FOR READ ONLY" ;
		List ordvta = new ArrayList();
		List lisCli = new ArrayList();
		HashMap <Integer, DetordDTO> detalle = new HashMap();
		int fecha=0;
		int numeroDoc=0;
		int bus=0;
		int correlativo=0;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (bus==0){
					ordvtaDTO = new OrdvtaDTO();
				}
				
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				if (correlativo==0){
					 correlativo = obtieneDireccionDespacho(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente());
					
					 ClidirDTO clidir = recuperaDatosCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente(), correlativo);
					 ordvtaDTO.setCorreDireccionOV(correlativo);
					ordvtaDTO.setLatitud(clidir.getLatitud().trim());
					ordvtaDTO.setLongitud(clidir.getLongitud().trim());
					lisCli.add(clidir);
				}
				DetordDTO detord = new DetordDTO();
				detord.setCodEmpresa(rs.getInt("ORDEMP"));
				detord.setCodigoArticulo(rs.getInt("CLDCOD"));
				detord.setCantidadArticulo(rs.getInt("VENCA2"));
				detord.setCorrelativoDetalleOV(rs.getInt("CORDOV"));
				fecha = rs.getInt("EXMFEC");
				numeroDoc = rs.getInt("CAMNUM");
				//detalle.put(Integer.valueOf(detord.getCodigoArticulo()), detord);
				detalle.put(Integer.valueOf(detord.getCorrelativoDetalleOV()), detord);
				bus++;
			}
			ordvtaDTO.setVecmar(obtieneVecmar(2, 21, fecha, numeroDoc));
			ordvtaDTO.setClidir(lisCli);
			ordvtaDTO.setDetalle(detalle);
			
			/*if (ordvtaDTO.getVecmar().getNumeroTipoDocumento()==0){
				ordvtaDTO.setClidir(lisCli);
				ordvtaDTO.setDetalle(detalle);
			}else{
				

				 ordvtaDTO = null;
			}*/
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return ordvtaDTO;
	}
	
	public VecmarDTO obtieneVecmar(int empresa, int tipoMov, int fecha, int numero){
		VecmarDTO vecmarDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		//HashMap <Integer, OrdvtaDTO> orden = new HashMap();
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.VECMAR V1, CASEDAT.CLMCLI C1" + 
        " Where V1.VENEMP="+empresa+" AND V1.VENCOD="+tipoMov+" AND V1.VENFEC="+fecha+" AND V1.VENNUM="+numero+" AND V1.VENRUT=C1.CLMRUT AND V1.VENDIG=C1.CLMDIG FOR READ ONLY" ;
		
	
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vecmarDTO = new VecmarDTO();
				vecmarDTO.setCodigoEmpresa(rs.getInt("VENEMP"));
				vecmarDTO.setCodTipoMvto(rs.getInt("VENCOD"));
				vecmarDTO.setFechaMvto(rs.getInt("VENFEC"));
				vecmarDTO.setNumDocumento(rs.getInt("VENNUM"));
				vecmarDTO.setCodigoDocumento(rs.getInt("VENCO5"));
				vecmarDTO.setFormaPago(rs.getString("VECFOR"));
				vecmarDTO.setCantidadLineaDetalle(rs.getInt("VENCAN"));
				vecmarDTO.setBodegaOrigen(rs.getInt("VENBO2"));
				vecmarDTO.setBodegaDestino(rs.getInt("VENBOD"));
				vecmarDTO.setSwitchPagoCaja(rs.getString("VECSWI"));
				vecmarDTO.setCodigoVendedor(rs.getInt("VENCO4"));
				vecmarDTO.setCodigoTipoVendedor(rs.getInt("VECCO7"));
				vecmarDTO.setRutProveedor(rs.getString("VENRUT"));
				vecmarDTO.setDvProveedor(rs.getString("VENDIG"));
				vecmarDTO.setIndicadorDespacho(rs.getString("VECIND"));
				vecmarDTO.setSwichProceso(rs.getInt("VENSWP"));
				vecmarDTO.setNumeroTipoDocumento(rs.getInt("VENNU2"));
				vecmarDTO.setRazonSocialCliente(rs.getString("CLMNOM").trim());

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
		return vecmarDTO;
	}
	
	
	public DimensionesDTO obtieneVolPesoCaja(int empresa, int bodega, int numeroOV){
		String nombre="";
		DimensionesDTO dimen = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		double cntcaja=0, volumen=0, peso=0;
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD " + 
        " Where ORDEMP="+empresa+" AND TPTC18="+bodega+" AND NUMVEN="+numeroOV+" FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("CLDCOD")!=7777777){
					DimensionesDTO dimension = obtieneVolPesoEXDARTCaja(rs.getInt("CLDCOD"), rs.getString("VENFOR").trim(), rs.getInt("CLDCAN"));
					if (dimension!=null){
						cntcaja = cntcaja + (rs.getInt("CLDCAN") * dimension.getCantidadCajas());
						volumen = volumen + (rs.getInt("CLDCAN") * dimension.getVolumen());
						peso = peso + (rs.getInt("CLDCAN") * dimension.getPeso());
					}
					
				}
				
				
				
			}
			if (cntcaja>0 || volumen>0 || peso>0){
				dimen = new DimensionesDTO();
				dimen.setCantidadCajas(cntcaja);
				dimen.setVolumen(volumen);
				dimen.setPeso(peso);
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
		return dimen;
	}
	
	public DimensionesDTO obtieneVolPesoEXDARTCaja( int codArticulo, String contenedor, int cantidad){
		String nombre="";
		DimensionesDTO dimensiones=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMART E1, CASEDAT.EXDART E2 " + 
        " Where EXMCOD="+codArticulo+" AND E1.EXMCOD=E2.EXDCO1 AND E2.EXDTIP='"+contenedor.trim()+"' ORDER BY EXDFEC DESC FETCH FIRST 1 ROW ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dimensiones = new DimensionesDTO();
				dimensiones.setVolumen(rs.getDouble("EXDVOL"));
				dimensiones.setPeso(rs.getDouble("EXDPES"));
				if (rs.getInt("EXMUNI")>0){
					dimensiones.setCantidadCajas(cantidad/rs.getInt("EXMUNI"));
				}else{
					dimensiones.setCantidadCajas(cantidad/1);
				}
				
				
				
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
		return dimensiones;
	}
	
	public int verificaOrdenExisteCarguio(int empresa, int numeroOV, int codigoBodega, int rutCliente){
		OrdvtaDTO ordvtaDTO = null;
		int result=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CARGUIOD " + 
        " Where CACEMP="+empresa+"  AND CAMCO1="+codigoBodega+" AND ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND CLMRUT="+rutCliente+" AND TPTC18="+codigoBodega+" AND CAMES1<>'X' FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL Carguiod" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result=1;
				
				
				
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
		return result;
	}
	
	public int obtieneDireccionDespacho(int empresa, int numeroOV, int codBodega, int rutCliente, String dv){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.DETORD " + 
        " Where ORDEMP="+empresa+" AND NUMVEN="+numeroOV+" AND CLMRUT="+rutCliente+" AND CLMDIG='"+dv+"' AND TPTC18="+codBodega+" fetch first 1 rows only FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("CLICOR");
				
				
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
	public int actualizarestadoOV(OrdvtaDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTA " + 
        " SET CAMES1='T' Where ORDEMP="+orden.getCodigoEmpresa()+" AND NUMVEN="+orden.getNumeroOV()+" AND CLMRUT="+orden.getRutCliente()+" AND CLMDIG='"+orden.getDvCliente()+"' AND TPTC18="+orden.getCodigoBodega()+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	public int eliminaOVCarguio(CarguioDetalleDTO orden){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="DELETE FROM  "+
        "  CASEDAT.CARGUIOD " + 
        "  Where CACEMP="+orden.getCodigoEmpresa()+" AND NUMCAR="+orden.getNumeroCarguio()+" AND NUMVEN="+orden.getNumeroOrdenVenta()+" AND CLMRUT="+orden.getRutCliente()+" AND CLMDIG='"+orden.getDvCliente()+"' AND TPTC18="+orden.getCodigoBodega()+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	
	public int generaCarguio(CarguioDTO carguio){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO "+
        "  CASEDAT.CARGUIOC " + 
        " (CACEMP,NUMCAR,CAMCO1,EXMFEC,PROFE7,CAMES1,HORLLE,HORSAL,ESTAUT,NUMRUT) VALUES("+carguio.getCodigoEmpresa()+","+carguio.getNumeroCarguio()+","+carguio.getCodigoBodega()+","+carguio.getFechaCarguio()+","+carguio.getFechaCarguio()+",'I',"+carguio.getHoraLlegada()+",0,'I',"+carguio.getNumeroRuta()+")" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	public int generaCarguioDetalle(CarguioDetalleDTO carguioD){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT INTO "+
        "  CASEDAT.CARGUIOD " + 
        " (CACEMP,NUMCAR,VECPAT,CAMCO1,CORDCA,ORDEMP,NUMVEN,CLMRUT,CLMDIG,TPTC18,CLDCOD,VENFOR,CLDCAN,VENCA2,CLICOR,VECFE5,CLICO1,CLICO2,CLICO3,CAMES1,VEDNU2,CAMNFA,CORDC2,ESTAUT) VALUES("+carguioD.getCodigoEmpresa()+","+carguioD.getNumeroCarguio()+",'"+carguioD.getPatente().trim()+"',"+carguioD.getCodigoBodega()+","+carguioD.getCorrelativoDetalle()+","+carguioD.getCodigoEmpresa()+","+carguioD.getNumeroOrdenVenta()+","+carguioD.getRutCliente()+",'"+carguioD.getDvCliente()+"',"+carguioD.getCodigoBodegaOrden()+","+carguioD.getCodigoArticulo()+",'"+carguioD.getFormatoMovimiento().trim()+"',"+carguioD.getCantidadArticulos()+","+carguioD.getCantidadFormato()+","+carguioD.getCorrelativoDirecciones()+","+carguioD.getFechaDespacho()+","+carguioD.getCodigoRegion()+","+carguioD.getCodigoCiudad()+","+carguioD.getCodigoComuna()+",'"+carguioD.getEstadoAutorizacion()+"',"+carguioD.getNumeroGuia()+","+carguioD.getNumeroFactura()+","+carguioD.getCorrelativoDetalleCarguio()+",'"+carguioD.getEstadoAutorizacion2().trim()+"')" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	public int generaCarguioCabecera(CarguioDTO carguioC){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="INSERT  INTO "+
        "  CASEDAT.CARGUIOC " + 
        " (CACEMP,NUMCAR,VECPAT,CAMCO1,CAMRUT,EXMDI3,EXMFEC,PROFE7,CAMES1,TOTCAJ,HORLLE,HORSAL,ESTAUT,NUMRUT, TIPCAR) VALUES("+carguioC.getCodigoEmpresa()+","+carguioC.getNumeroCarguio()+",'',"+carguioC.getCodigoBodega()+",'0','',"+carguioC.getFechaCarguio()+","+carguioC.getFechaCreacion()+",'"+carguioC.getEstado().trim()+"','0',"+carguioC.getHoraLlegada()+",'"+carguioC.getHoraLlegada()+"','"+carguioC.getEstado2().trim()+"',"+carguioC.getNumeroRuta()+",'N')" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	
	
	public String obtieneNombreVendedor(int codigoVendedor){
		String nombre="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.EXMVND " + 
        " Where EXMC09="+codigoVendedor+"  FOR READ ONLY" ;
		List ordvta = new ArrayList();
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nombre=rs.getString("EXMNO1");
				
				
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
		return nombre;
	}
	public int obtieneNumeroCarguio(int codigoBodega, int tipo){
		int correlativo=0;
		int correlativo2=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTANM " + 
        " Where TPTC18="+codigoBodega+" AND OBF007="+tipo+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("OBF00F");
				correlativo2=correlativo+1;
				actualizaCorrelativo(codigoBodega, tipo, correlativo2);
				
				
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
		return correlativo2;
	}
	
	public int obtieneCorrelativoCarguio(int codigoEmpresa, int numeroCarguio, int codigoBodega){
		int correlativo=0;
		int correlativo2=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select max(cordca) aS corre "+
        " from CASEDAT.CARGUIOD " + 
        " Where CACEMP="+codigoEmpresa+" AND NUMCAR="+numeroCarguio+" AND CAMCO1="+codigoBodega+"  FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				correlativo=rs.getInt("CORRE");
				correlativo=correlativo+1;
				
				
				
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
	
	
	public int actualizaCorrelativo(int codigoBodega, int tipo, int correlativo){
		int actualiza=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.ORDVTANM " + 
        " SET OBF00F="+correlativo+" Where TPTC18="+codigoBodega+" AND OBF007="+tipo+"  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return actualiza;
	}
	
	
	
	
	
	public String obtieneNombreCliente(int rut, String dv){
		String nombre="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.CLMCLI " + 
        " Where CLMRUT="+rut+"  AND CLMDIG='"+dv+"' FOR READ ONLY" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nombre=rs.getString("CLMNOM");
				
				
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
		return nombre;
	}
	
	
	public ClidirDTO recuperaDatosCliente(int rut, String dv, int correlativo){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		String sqlObtenerCldmco ="Select C1.CLICO1,C1.CLICO2, C1.CLICO3, C1.CLIDI1, C2.LATDIR, C2.LONDIR "+
        " from CASEDAT.CLIDIR C1, CASEDAT.CLIDIRA C2  " + 
        //" Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= "+correlativo+" AND C1.CLICOR=C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG AND C2.ESTDIR ='A' FOR READ ONLY" ;
        " Where C1.CLIRUT="+rut+" AND C1.CLIDIG='"+dv+"' AND C1.CLICOR= "+correlativo+" AND C1.CLICOR=C2.CLICOR AND C1.CLIRUT= C2.CLIRUT AND C1.CLIDIG= C2.CLIDIG  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				clidir = new ClidirDTO();
				clidir.setRegion(rs.getInt("CLICO1"));
				clidir.setCiudad(rs.getInt("CLICO2"));
				clidir.setComuna(rs.getInt("CLICO3"));
				clidir.setDescripcionComuna(recuperaComuna(clidir.getRegion(), clidir.getCiudad(), clidir.getComuna()).trim());
				clidir.setDireccionCliente(rs.getString("CLIDI1"));
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
	
	public String recuperaComuna(int region, int ciudad, int comuna){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		ClidirDTO clidir = null;
		String Descomuna="";
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.TPTCOM  " + 
        " Where TPTC19="+region+" AND TPTC20="+ciudad+" AND TPTC22= "+comuna+"  FOR READ ONLY" ;
		try
		{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Descomuna=rs.getString("TPTD20").trim();
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
		return Descomuna;
	}
	
	
	public int actualizarestadoDetalleCarguio(int empresa, int bodega, int numeroCarguio, String estado){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.CARGUIOD " + 
        " SET CAMES1='"+estado+"' Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+" AND CAMCO1="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	public int actualizarestadoCarguio(int empresa, int bodega, int numeroCarguio, String estado){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.CARGUIOC " + 
        " SET CAMES1='"+estado+"' Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+" AND CAMCO1="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	public int actualizaChofer(int empresa, int bodega, int numeroCarguio, int rut, String dv, String patente){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.CARGUIOC " + 
        " SET CAMRUT="+rut+", EXMDI3='"+dv+"', VECPAT='"+patente+"' Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+" AND CAMCO1="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	public int actualizaChoferDetalle(int empresa, int bodega, int numeroCarguio, int rut, String dv, String patente){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.CARGUIOD " + 
        " SET CAMRUT="+rut+", EXMDI3='"+dv+"', VECPAT='"+patente+"' Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+" AND CAMCO1="+bodega+" " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
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
	
	
	//carguio transporte segun rut de chofer
	//carguio transporte segun rut de chofer ; solicitud 1
	public List listarCarguiosTransp(int rutChofer, String digChofer, int numeritodecarguio){
		
		String connumero="";
		
		List cargu = new ArrayList();
		CarguioTranspDTO dto = new CarguioTranspDTO();
		
		List ordenes=null;
		OrdTranspDTO ordendto = new OrdTranspDTO();
		
		long conti=0;
		long numcarguiootro=0;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		if (numeritodecarguio>0){
			connumero=" AND c.numcar="+numeritodecarguio;
		}
		
		String sqlObtenerCarguio = " SELECT "+
		" c.numcar, c.numrut, c.camrut, c.exmdi3, ch.camnom, c.vecpat, c.exmfec,"+
		" d.numven, ve.venco5, td.tpade1, ca.camnfa, o.clmrut, de.clicor, o.clmdig, cl.clmnom, di.clidi1, di.clinum, di.clifax, di.clico1, re.tptde1," +
		" di.clico3, co.tptd20, di.clitel, di.clite1, v.exmno1, v.exmtel, v2.exmema, o.clctot, da.latdir, da.londir, d.vecfe5, o.vecfor, "+
		" c.cacemp, c.camco1 " +
		" FROM CASEDAT.CARGUIOC c " +
		" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
		" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
		" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
		" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
		" INNER JOIN CASEDAT.CLIDIR di ON de.clmrut=di.clirut AND de.clmdig=di.clidig AND de.clicor=di.clicor" +
		" INNER JOIN CASEDAT.CLIDIRA da ON de.clmrut=da.clirut AND de.clmdig=da.clidig AND de.clicor=da.clicor" +
		" INNER JOIN CASEDAT.TPTREG re ON re.tptco1=di.clico1" +
		" INNER JOIN CASEDAT.TPTCOM co ON di.clico1=co.tptc19 AND di.clico2=co.tptc20 AND di.clico3=co.tptc22" +
		" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
		" INNER JOIN CASEDAT.TPTDOC td ON ve.venco5=td.tpaco1" +
		" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
		" LEFT JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42" +
		" WHERE c.cacemp=2 AND c.camrut="+rutChofer+" AND c.exmdi3='"+digChofer+"' " +connumero + " "+
		" AND c.cames1 <> 'X' " +
		" AND d.xocl1='' "+
		" GROUP BY " +
		" c.numcar, c.numrut, c.camrut, c.exmdi3, ch.camnom, c.vecpat, c.exmfec,"+
		" d.numven, ve.venco5, td.tpade1, ca.camnfa, o.clmrut, de.clicor, o.clmdig, cl.clmnom, di.clidi1, di.clinum, di.clifax, di.clico1, re.tptde1," +
		" di.clico3, co.tptd20, di.clitel, di.clite1, v.exmno1, v.exmtel, v2.exmema, o.clctot, da.latdir, da.londir, d.vecfe5, o.vecfor, c.cacemp, c.camco1 "+
		" ORDER BY c.numcar ASC "+
		" FOR READ ONLY";			
		//" AND c.cames1 IN ('E','G','U') " +
		
		try{			
			pstmt = conn.prepareStatement(sqlObtenerCarguio);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				if (rs.getInt("NUMCAR") != numcarguiootro && (conti>0)){
					conti=0;
					dto.setOrdenes(ordenes);
					cargu.add(dto);
					dto = new CarguioTranspDTO();
					ordenes = new ArrayList();
				}
				
				
				if (conti==0){
					ordenes = new ArrayList();
					dto.setNumeroCarguio(rs.getInt("NUMCAR"));
					dto.setVersion(rs.getInt("NUMRUT"));     //VERSION
					dto.setRutChofer(rs.getInt("CAMRUT"));
					dto.setDvChofer(rs.getString("EXMDI3"));
					dto.setNombreChofer(rs.getString("CAMNOM").trim());
					dto.setPatente(rs.getString("VECPAT").trim());
					dto.setFechaCarguio(rs.getInt("EXMFEC"));
					numcarguiootro=(rs.getInt("NUMCAR"));
					conti++;
				}
				
				
				ordendto.setNumeroOV(rs.getInt("NUMVEN"));
				ordendto.setTipoDocumento(rs.getInt("VENCO5"));
				ordendto.setDescripcionDocumento(rs.getString("TPADE1").trim());
				ordendto.setNumeroDocumento(rs.getInt("CAMNFA"));
				ordendto.setRutCliente(rs.getInt("CLMRUT"));
				ordendto.setCorrelativoDireccion(rs.getInt("CLICOR"));
				ordendto.setNombreCliente(rs.getString("CLMNOM").trim());
				ordendto.setDireccion(rs.getString("CLIDI1").substring(0,30).trim());
				String numerodire = rs.getString("CLIDI1").substring(30, 35).trim();
				if (numerodire!=null && !numerodire.equals("")){
					if (numerodire.matches(("[0-9]*"))){
					ordendto.setNumeroDir(Integer.parseInt(rs.getString("CLIDI1").substring(30, 35).trim()));
					}else{
					ordendto.setNumeroDir(0);
					}
				}else{
					ordendto.setNumeroDir(0);
				}
				String depto = rs.getString("CLIDI1").substring(35, 40).trim();
				if (!depto.equals("null") && depto!=null && !depto.equals("ll") && !depto.equals("ull") && !depto.equals("")){
					String numerodepa = rs.getString("CLIDI1").substring(35, 40).trim();
					if (numerodepa.matches(("[0-9]*"))){
						ordendto.setDepto(Integer.parseInt(rs.getString("CLIDI1").substring(35, 40).trim()));
					}else{
						ordendto.setDepto(0);
					}
				}else{
					ordendto.setDepto(0);
                }
	            ordendto.setVilla(rs.getString("CLINUM").trim()+rs.getString("CLIFAX").trim());
				ordendto.setCodRegion(rs.getInt("CLICO1"));
				ordendto.setDescRegion(rs.getString("TPTDE1").trim());
				ordendto.setCodComuna(rs.getInt("CLICO3"));
				ordendto.setDescComuna(rs.getString("TPTD20").trim());
				ordendto.setTelefono(rs.getString("CLITEL").trim());
				ordendto.setCelular(rs.getString("CLITE1").trim());
				ordendto.setNombreVendedor(rs.getString("EXMNO1").trim());
				ordendto.setTelefonoVendedor(rs.getString("EXMTEL").trim());
				String vendedorcorreo =rs.getString("EXMEMA");
				if (vendedorcorreo==null || vendedorcorreo.trim()==""){
					ordendto.setCorreoVendedor("");
				}else{
					vendedorcorreo=rs.getString("EXMEMA").trim();
					if (vendedorcorreo!=null && !vendedorcorreo.equals("")){
						ordendto.setCorreoVendedor(rs.getString("EXMEMA").trim());
					}else{
						ordendto.setCorreoVendedor("");
					}
				}
				ordendto.setMonto(rs.getInt("CLCTOT"));
				DimensionesDTO dimen = obtieneVolPesoCaja(2, rs.getInt("CAMCO1"), rs.getInt("NUMVEN")) ;
				if (dimen!=null){
					ordendto.setVolumen(dimen.getVolumen());
				}else{
					ordendto.setVolumen(0);
				}
				ordendto.setLatitud(rs.getString("LATDIR").trim());
				ordendto.setLongitud(rs.getString("LONDIR").trim());
				ordendto.setFechaDespacho(rs.getInt("VECFE5"));
				String formapago = "";
				formapago = String.valueOf(rs.getInt("VECFOR"));
				if ("1".equals(formapago) || "2".equals(formapago)){
					ordendto.setFormaPago(rs.getInt("VECFOR"));
				}else{
					formapago="1";
					ordendto.setFormaPago(Integer.parseInt(formapago));
				}
				
				//detalle ov
				List detalle = listarCarguiosTranspDetOrd(2,rs.getInt("CAMCO1"), rs.getInt("NUMVEN"),rs.getInt("CLMRUT"),rs.getString("CLMDIG"));
				
				ordendto.setDetord(detalle);
				ordenes.add(ordendto);
				ordendto = new OrdTranspDTO();
				
				
			}
			
			
			dto.setOrdenes(ordenes);
			cargu.add(dto);

			
			}catch(Exception e){
				e.printStackTrace();
			}finally {

				try {

					 if (rs != null) { rs.close(); 
					 pstmt.close();
					 }

				} catch (SQLException e1) { }

		  } 
			return cargu;
		}
		
		
		public List listarCarguiosTranspDetOrd(int codigoEmpresa, int bodega, int numeroOV, int rut, String digito){
			List detalleov = new ArrayList();
			
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			String sqlObtenerDetord="SELECT "+
	        " d.cordov, d.cldcod, a.exmdes, d.venfor, d.cldcan "+
	        " FROM CASEDAT.DETORD d "+
	        " INNER JOIN CASEDAT.EXMART a ON d.cldcod=a.exmcod"+
	        " WHERE d.ordemp="+codigoEmpresa+" AND d.tptc18="+bodega+" AND d.numven="+numeroOV+""+
	        " AND d.clmrut="+rut+" AND d.clmdig='"+digito+"' AND d.cames1='E' FOR READ ONLY";
	        
	        try{
				pstmt = conn.prepareStatement(sqlObtenerDetord);
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
						DetordTranspDTO detord = new DetordTranspDTO();

							detord.setCorrelativo(String.valueOf(rs.getInt("CORDOV")).trim());
							detord.setCodigoArticulo(String.valueOf(rs.getInt("CLDCOD")).trim());
							detord.setDescripcionArticulo(rs.getString("EXMDES").trim());
							detord.setFormato(rs.getString("VENFOR"));
							detord.setCantidad(rs.getInt("CLDCAN"));
							detalleov.add(detord);
							
							//log.info(rs.getInt("CORDOV")+ " "+ rs.getInt("CLDCOD")+ " "+rs.getString("EXMDES")+" "+rs.getString("VENFOR")+" "+rs.getInt("CLDCAN"));
							
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
			return detalleov;
		}
		
	
	
	public List buscaCarguiosHijos(int empresa, int bodega, int numeroCarguio, String estado){
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		CarguioDTO carguioc = null;
		List carguios = new ArrayList();
		
		String sqlObtenerCarguioc = " SELECT C1.cacemp, C1.numcar, C1.vecpat, C1.camco1, C1.camrut, C1.exmdi3, C1.exmfec, " +
				" C1.profe7, C1.cames1, C1.totcaj, C1.horlle, C1.horsal, C1.estaut, C1.tipcar, C1.codbde, " +
				" C1.sellcar, C1.numcvi, C1.totmnc, C1.totmca, C1.totcca, C1.numrut, C1.cantov, C1.cancde, C1.candde " +
				" FROM CASEDAT.CARGUIOC C1 " +
				" WHERE C1.CACEMP = "+empresa +
				" AND C1.CAMCO1 = "+bodega +
				" AND C1.NUMCVI = "+numeroCarguio +
				" AND C1.NUMCVI <> C1.NUMCAR " +
				" AND C1.TIPCAR = 'N' " +
				" AND C1.cames1 = '"+estado+"' " +
				" GROUP BY C1.cacemp, C1.numcar, C1.vecpat, C1.camco1, C1.camrut, C1.exmdi3, C1.exmfec, " +
				" C1.profe7, C1.cames1, C1.totcaj, C1.horlle, C1.horsal, C1.estaut, C1.tipcar, C1.codbde, " +
				" C1.sellcar, C1.numcvi, C1.totmnc, C1.totmca, C1.totcca, C1.numrut, C1.cantov, C1.cancde, C1.candde " +
				" HAVING((SELECT COUNT(*) FROM CASEDAT.CARGUIOC C4 WHERE C4.cacemp = C1.cacemp AND C4.camco1 = C1.camco1 AND C4.numcar = C1.numcvi AND C4.cames1 = '"+estado+"') > 0) " +
				" ORDER BY C1.NUMCAR";
		
		log.info("Consulta CARGUIO HIJOS TRANSFERENCIA " + sqlObtenerCarguioc);
		
		try{			
			pstmt = conn.prepareStatement(sqlObtenerCarguioc);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				carguioc = new CarguioDTO();
				carguioc.setCodigoEmpresa(rs.getInt("CACEMP"));
				carguioc.setCodigoBodega(rs.getInt("CAMCO1"));
				carguioc.setNumeroCarguio(rs.getInt("NUMCAR"));
				
				carguios.add(carguioc);
				
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
		return carguios;
	}
	
	public HashMap listaCarguiosFacturacion(int codigoEmpresa, int Carguio, int codigoBodega){
			CarguioDTO carguioDTO = null;
			
			HashMap <Integer, OrdvtaDTO> ordenesHash = new HashMap();
			PreparedStatement pstmt =null;
			ResultSet rs = null; 
			String sqlObtenerCamtra="Select C1.CACEMP ,C1.NUMCAR, C1.CAMCO1 , ORDEMP, NUMVEN "+
	        " FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3 " + 
	        " WHERE C1.CACEMP = "+codigoEmpresa+" AND C1.CAMCO1 = "+codigoBodega+" " +
	        " AND C1.NUMCAR = "+Carguio+" AND C1.CACEMP = C2.CACEMP AND C1.NUMCAR = C2.NUMCAR " + 
	        " AND C1.VECPAT = C2.VECPAT " +
	        " AND C1.CAMCO1 = C2.CAMCO1 " +
	        " AND C1.CACEMP = C3.CACEMP " +
	        " AND C1.NUMCAR = C3.NUMCAR " +
	        " AND C1.VECPAT = C3.VECPAT " +
	        " AND C1.CAMCO1 = C3.CAMCO1 " +
	        " GROUP BY C1.CACEMP ,C1.NUMCAR, C1.CAMCO1 , C2.ORDEMP, C2.NUMVEN" ;
			
			try{
				pstmt = conn.prepareStatement(sqlObtenerCamtra);
				
				int numeroCarguio=0;
				int contador=0;
				int numeroOV=0;
				rs = pstmt.executeQuery();
				while (rs.next()) {
						//ordvtaDTO = new OrdvtaDTO();
						
						int empresa = rs.getInt("CACEMP");
						int bodega = rs.getInt("CAMCO1");
						int numov = rs.getInt("NUMVEN");
						//log.info(numov);
						ordenesHash.put(Integer.valueOf(rs.getInt("NUMVEN")),obtieneOrdenesCarguio(empresa,numov,bodega));
						
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
			return ordenesHash;
		}
	
	
	public int liberaCarguioMapa(int empresa, int codigoBodega, String estado, String estadoNuevo){
		int actualiza=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="UPDATE  "+
        "  CASEDAT.CARGUIOC " + 
        " SET CAMES1='"+estadoNuevo.trim()+"' Where CACEMP="+empresa+" AND CAMCO1="+codigoBodega+" AND CAMES1='"+estado.trim()+"'  " ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			log.info("LIBERA CARGUIOS" + sqlObtenerCldmco);
			
			
			 pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return actualiza;
	}
	
	public HashMap listaCarguiosFacturacionEspeciales(int codigoEmpresa, int Carguio, int codigoBodega){
		CarguioDTO carguioDTO = null;
		
		HashMap <Integer, OrdvtaDTO> ordenesHash = new HashMap();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCamtra="Select C1.CACEMP ,C1.NUMCAR, C1.CAMCO1 , ORDEMP, NUMVEN "+
        " FROM CASEDAT.CARGUIOC C1, CASEDAT.CARGUIOD C2, CASEDAT.CARGUIAD C3 " + 
        " WHERE C1.CACEMP = "+codigoEmpresa+" AND C1.CAMCO1 = "+codigoBodega+" " +
        " AND C1.NUMCAR = "+Carguio+" AND C1.CACEMP = C2.CACEMP AND C1.NUMCAR = C2.NUMCAR " + 
        " AND C1.VECPAT = C2.VECPAT " +
        " AND C1.CAMCO1 = C2.CAMCO1 " +
        " AND C1.CACEMP = C3.CACEMP " +
        " AND C1.NUMCAR = C3.NUMCAR " +
        " AND C1.VECPAT = C3.VECPAT " +
        " AND C1.CAMCO1 = C3.CAMCO1 " +
        " GROUP BY C1.CACEMP ,C1.NUMCAR, C1.CAMCO1 , C2.ORDEMP, C2.NUMVEN" ;
		
		try{
			pstmt = conn.prepareStatement(sqlObtenerCamtra);
			
			int numeroCarguio=0;
			int contador=0;
			int numeroOV=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
					//ordvtaDTO = new OrdvtaDTO();
					
					int empresa = rs.getInt("CACEMP");
					int bodega = rs.getInt("CAMCO1");
					int numov = rs.getInt("NUMVEN");
					//log.info(numov);
					ordenesHash.put(Integer.valueOf(rs.getInt("NUMVEN")),obtieneOrdenesCarguioEspeciales(empresa,numov,bodega,Carguio));
					
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
		return ordenesHash;
	}
	
	public OrdvtaDTO obtieneOrdenesCarguioEspeciales(int empresa, int numeroOV, int codigoBodega, int numeroCarguio){
		OrdvtaDTO ordvtaDTO = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		//HashMap <Integer, OrdvtaDTO> orden = new HashMap();
		String sqlObtenerCldmco ="Select * "+
        " from CASEDAT.ORDVTA O1, CASEDAT.DETORD D1 , CASEDAT.CARGUIOD C1" + 
        " Where O1.ORDEMP="+empresa+" AND O1.NUMVEN="+numeroOV+" AND O1.TPTC18="+codigoBodega+" AND O1.ORDEMP=D1.ORDEMP AND O1.NUMVEN=D1.NUMVEN AND O1.TPTC18=D1.TPTC18 AND " +
        " C1.CACEMP=O1.ORDEMP AND C1.NUMCAR="+numeroCarguio+" AND C1.CAMCO1=O1.TPTC18 AND C1.ORDEMP=O1.ORDEMP AND C1.NUMVEN=O1.NUMVEN AND "+
        "C1.CACEMP=D1.ORDEMP AND C1.NUMVEN=D1.NUMVEN AND C1.CAMCO1=D1.TPTC18 AND C1.CLDCOD=D1.CLDCOD  ";
		log.info("SQL:"+sqlObtenerCldmco);
		List ordvta = new ArrayList();
		List lisCli = new ArrayList();
		HashMap <Integer, DetordDTO> detalle = new HashMap();
		int fecha=0;
		int numeroDoc=0;
		int bus=0;
		int correlativo=0;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL CLC" + sqlObtenerCldmco);
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (bus==0){
					ordvtaDTO = new OrdvtaDTO();
				}
				
				ordvtaDTO.setNumeroOV(rs.getInt("NUMVEN"));
				ordvtaDTO.setCodigoVendedor(rs.getInt("CAMCOD"));
				ordvtaDTO.setRutCliente(rs.getInt("CLMRUT"));
				ordvtaDTO.setTotalDocumento(rs.getDouble("CLCTOT"));
				ordvtaDTO.setDvCliente(rs.getString("CLMDIG").trim());
				ordvtaDTO.setCodigoEmpresa(rs.getInt("ORDEMP"));
				ordvtaDTO.setCodigoBodega(rs.getInt("TPTC18"));
				ordvtaDTO.setNumeroDocumento(rs.getInt("CAMNUM"));
				ordvtaDTO.setNombreVendedor(obtieneNombreVendedor(ordvtaDTO.getCodigoVendedor()));
				ordvtaDTO.setNombreCliente(obtieneNombreCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente()).trim());
				if (correlativo==0){
					 correlativo = obtieneDireccionDespacho(ordvtaDTO.getCodigoEmpresa(), ordvtaDTO.getNumeroOV(), ordvtaDTO.getCodigoBodega(), ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente());
					
					 ClidirDTO clidir = recuperaDatosCliente(ordvtaDTO.getRutCliente(), ordvtaDTO.getDvCliente(), correlativo);
					 ordvtaDTO.setCorreDireccionOV(correlativo);
					ordvtaDTO.setLatitud(clidir.getLatitud().trim());
					ordvtaDTO.setLongitud(clidir.getLongitud().trim());
					lisCli.add(clidir);
				}
				DetordDTO detord = new DetordDTO();
				detord.setCodEmpresa(rs.getInt("ORDEMP"));
				detord.setCodigoArticulo(rs.getInt("CLDCOD"));
				detord.setCantidadArticulo(rs.getInt("VENCA2"));
				fecha = rs.getInt("EXMFEC");
				numeroDoc = rs.getInt("CAMNUM");
				detalle.put(Integer.valueOf(detord.getCodigoArticulo()), detord);
				
				bus++;
			}
			ordvtaDTO.setVecmar(obtieneVecmar(2, 21, fecha, numeroDoc));
			ordvtaDTO.setClidir(lisCli);
			ordvtaDTO.setDetalle(detalle);
			
			/*if (ordvtaDTO.getVecmar().getNumeroTipoDocumento()==0){
				ordvtaDTO.setClidir(lisCli);
				ordvtaDTO.setDetalle(detalle);
			}else{
				

				 ordvtaDTO = null;
			}*/
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {

			try {

				 if (rs != null) { rs.close(); 
				 pstmt.close();
				 }

			} catch (SQLException e1) { }

	  } 
		return ordvtaDTO;
	}
	
	public ExmartDTO recuperaArticulo(int codArticulo, String digito){
		ExmartDTO exmartDTO = null;
		List lista = new ArrayList();
		
		String CodigoGrupo="";
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXMART E1, CASEDAT.EXMAGA1 E2 WHERE E1.EXMCOD="+codArticulo+" AND E1.EXMDIG='"+digito+"' AND E1.EXMCOD=E2.EXMCOD AND E1.EXMDIG=E2.EXMDIG" ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				 exmartDTO = new ExmartDTO();
				 
				exmartDTO.setCodigoArticulo(rs.getInt("EXMCOD"));
				exmartDTO.setDvArticulo(rs.getString("EXMDIG"));
				exmartDTO.setDescripcionArticulo(rs.getString("EXMDES"));
				exmartDTO.setCodigoCategoria(rs.getInt("EXMCO5"));
				exmartDTO.setCodigoFamilia(rs.getInt("EXMCO3"));
				exmartDTO.setDescripcionCortaArticulo(rs.getString("EXMNOM"));
				
				//exmartDTO.setCodigoGrupo(rs.getInt("CODGAL"));
				CodigoGrupo=String.valueOf((rs.getString("CODGAL").trim()));
				
				if ("".equals(CodigoGrupo)){
					exmartDTO.setCodigoGrupo(0);
				}
				else{
					exmartDTO.setCodigoGrupo(Integer.parseInt(CodigoGrupo));
				}
				
				exmartDTO.setDescripcionGrupo(rs.getString("DSGRAL"));
				exmartDTO.setVidaUtil(rs.getInt("XONM4"));
				exmartDTO.setCaja(rs.getInt("EXMCAJ"));
				exmartDTO.setDisplay(rs.getInt("EXMDIS"));
				exmartDTO.setUnidades(rs.getInt("EXMUNI"));
				exmartDTO.setPallet(rs.getInt("EXMPAL"));
				
				//exmartDTO.setCodigosBarras((buscaExdart(exmartDTO.getCodigoArticulo(),exmartDTO.getDvArticulo())));
				//exmartDTO.setCodigos(buscaExdartOrdenada(exmartDTO.getCodigoArticulo(),exmartDTO.getDvArticulo()));
				
				
				
			}
			//exmartDTO.setCodigosBarras(lista);
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
		return exmartDTO;
	}
	
	public List buscaExdart(int codigo, String dv){
		ExdartDTO exdartDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXDART WHERE EXDCO1="+codigo+" AND EXDDI1='"+dv+"' " ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdartDTO = new ExdartDTO();
				exdartDTO.setCodigoArticulo(rs.getInt("EXDCO1"));
				exdartDTO.setDvArticulo(rs.getString("EXDDI1"));
				exdartDTO.setCodigoBarra(rs.getString("EXDC01"));
				exdartDTO.setAlto(rs.getDouble("EXDALT"));
				exdartDTO.setLargo(rs.getDouble("EXDLAR"));
				exdartDTO.setAncho(rs.getDouble("EXDANC"));
				exdartDTO.setCantAltoPallets(rs.getDouble("EXDCAP"));
				exdartDTO.setCantBasePallets(rs.getDouble("EXDCBP"));
				exdartDTO.setCantTotalPallets(rs.getDouble("EXDTCP"));
				exdartDTO.setPeso(rs.getDouble("EXDPES"));
				exdartDTO.setVolumen(rs.getDouble("EXDVOL"));
				exdartDTO.setTipoContenedor(rs.getString("EXDTIP"));
				lista.add(exdartDTO);
				
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
		return lista;
	}
	
	public HashMap buscaExdartOrdenada(int codigo, String dv){
		ExdartDTO exdartDTO = null;
		List lista = new ArrayList();
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		HashMap<String,ExdartDTO> codigos = new HashMap<String,ExdartDTO>();
		String sqlObtenerCldmco ="SELECT * "+
        " from CASEDAT.EXDART WHERE EXDCO1="+codigo+" AND EXDDI1='"+dv+"' " ;
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
			//log.info("SQL" + sqlObtenerCldmco);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				exdartDTO = new ExdartDTO();
				exdartDTO.setCodigoArticulo(rs.getInt("EXDCO1"));
				exdartDTO.setDvArticulo(rs.getString("EXDDI1"));
				exdartDTO.setCodigoBarra(rs.getString("EXDC01").trim());
				exdartDTO.setAlto(rs.getDouble("EXDALT"));
				exdartDTO.setLargo(rs.getDouble("EXDLAR"));
				exdartDTO.setAncho(rs.getDouble("EXDANC"));
				exdartDTO.setCantAltoPallets(rs.getDouble("EXDCAP"));
				exdartDTO.setCantBasePallets(rs.getDouble("EXDCBP"));
				exdartDTO.setCantTotalPallets(rs.getDouble("EXDTCP"));
				exdartDTO.setPeso(rs.getDouble("EXDPES"));
				exdartDTO.setVolumen(rs.getDouble("EXDVOL"));
				exdartDTO.setTipoContenedor(rs.getString("EXDTIP"));
					codigos.put(exdartDTO.getTipoContenedor(), exdartDTO);

				
				//lista.add(exdartDTO);
				
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
		return codigos;
	}
	
	public int buscarCarguioTransp(int rutChofer, String digChofer, int numerocarguio ){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco = " SELECT "+
				" c.numcar " +
				" FROM CASEDAT.CARGUIOC c " +
				" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
				" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
				" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
				" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
				" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
				" INNER JOIN CASEDAT.CLIDIR di ON o.clmrut=di.clirut AND o.clmdig=di.clidig AND o.clicor=di.clicor" +
				" INNER JOIN CASEDAT.CLIDIRA da ON o.clmrut=da.clirut AND o.clmdig=da.clidig AND o.clicor=da.clicor" +
				" INNER JOIN CASEDAT.TPTREG re ON re.tptco1=di.clico1" +
				" INNER JOIN CASEDAT.TPTCOM co ON di.clico1=co.tptc19 AND di.clico2=co.tptc20 AND di.clico3=co.tptc22" +
				" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
				" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
				" INNER JOIN CASEDAT.TPTDOC td ON ve.venco5=td.tpaco1" +
				" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
				" LEFT JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42" +
				" WHERE c.cacemp=2 AND c.camrut="+rutChofer+" AND c.exmdi3='"+digChofer+"' " +
				" AND c.numcar="+numerocarguio+
				" AND c.cames1 <> 'X' " +
				" AND d.exinum=0"+
				" GROUP BY " +
				" c.numcar "+
				" FOR READ ONLY";			
			//" AND c.cames1 IN ('E','G','U') " +
				
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			
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

	
	public int obtieneCarguioyOrdenes(int empresa, int numeroCarguio, int codBodega, int numeroOV, String patente, int rutClie){
		int correlativo=-100;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null; 
		String sqlObtenerCldmco ="Select d.NUMVEN "+
        " from CASEDAT.CARGUIOC c" + 
		" INNER JOIN CASEDAT.CARGUIOD d ON c.CACEMP=d.CACEMP AND c.NUMCAR=d.NUMCAR AND c.VECPAT=d.VECPAT AND c.CAMCO1=d.CAMCO1 "+
        " Where d.CACEMP="+empresa+" AND d.NUMCAR="+numeroCarguio+" AND d.NUMVEN="+numeroOV+
        " AND d.CAMCO1="+codBodega+
        " AND d.VECPAT='"+patente+"' " +
        " AND d.CLMRUT="+rutClie+
        " AND d.venca2>0 "+
        " fetch first 1 rows only FOR READ ONLY" ;
		//logi.info("SELECT EXISTE NUMVEN CARGUIOD : "+sqlObtenerCldmco);
		try{
			pstmt = conn.prepareStatement(sqlObtenerCldmco);
			rs = pstmt.executeQuery();
			//logi.info("O K E Y   SELECT EXISTE NUMVEN CARGUIOD");
			while (rs.next()) {
				correlativo=1;
				break;
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
	
public int contarCarguiosTransp(int rutChofer, String digChofer, int numerocarguio){
		
		int cantidadoves=0;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String sqlObtenerCarguio = " SELECT "+
		" c.numcar, d.numven " +
		" FROM CASEDAT.CARGUIOC c " +
		" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
		" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
		" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
		" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
		" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
		" INNER JOIN CASEDAT.CLIDIR di ON o.clmrut=di.clirut AND o.clmdig=di.clidig AND o.clicor=di.clicor" +
		" INNER JOIN CASEDAT.CLIDIRA da ON o.clmrut=da.clirut AND o.clmdig=da.clidig AND o.clicor=da.clicor" +
		" INNER JOIN CASEDAT.TPTREG re ON re.tptco1=di.clico1" +
		" INNER JOIN CASEDAT.TPTCOM co ON di.clico1=co.tptc19 AND di.clico2=co.tptc20 AND di.clico3=co.tptc22" +
		" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
		" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
		" INNER JOIN CASEDAT.TPTDOC td ON ve.venco5=td.tpaco1" +
		" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
		" LEFT JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42" +
		" WHERE c.cacemp=2 AND c.camrut="+rutChofer+" AND c.exmdi3='"+digChofer+"' " +
		" AND c.numcar="+numerocarguio+
		" AND c.cames1 <> 'X' " +
		" AND d.exinum=0"+
		" GROUP BY c.numcar, d.numven "+
		" ORDER BY c.numcar ASC "+
		" FOR READ ONLY";			
		//" AND c.cames1 IN ('E','G','U') " +
		
		try{			
			pstmt = conn.prepareStatement(sqlObtenerCarguio);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				cantidadoves=cantidadoves+1;
				
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
			return cantidadoves;
		}

public List listarCarguiosTranspRezagados(int rutChofer, String digChofer){
	
	int numcaraux=0;
	int conti=0;
	
	List cargu = new ArrayList();
	RedespachocDTO dto = new RedespachocDTO();
	
	List ordenes = new ArrayList();
	
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	
	String sqlObtenerCarguio = " SELECT "+
	" c.numcar, c.camrut, c.exmdi3, ca.camnfa, ca.camco3 "+
	" FROM CASEDAT.CARGUIOC c " +
	" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
	" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
	" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
	" INNER JOIN CASEDAT.CLIDIR di ON o.clmrut=di.clirut AND o.clmdig=di.clidig AND o.clicor=di.clicor" +
	" INNER JOIN CASEDAT.CLIDIRA da ON o.clmrut=da.clirut AND o.clmdig=da.clidig AND o.clicor=da.clicor" +
	" INNER JOIN CASEDAT.TPTREG re ON re.tptco1=di.clico1" +
	" INNER JOIN CASEDAT.TPTCOM co ON di.clico1=co.tptc19 AND di.clico2=co.tptc20 AND di.clico3=co.tptc22" +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
	" INNER JOIN CASEDAT.TPTDOC td ON ve.venco5=td.tpaco1" +
	" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
	" LEFT JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42" +
	" WHERE c.cacemp=2 AND c.camrut="+rutChofer+" AND c.exmdi3='"+digChofer+"' " +
	" AND c.cames1 <> 'X' " +
	" AND d.xocl1='' "+
	" GROUP BY " +
	" c.numcar, c.camrut, c.exmdi3, ca.camnfa, ca.camco3 "+
	" ORDER BY c.numcar ASC "+
	" FOR READ ONLY";			

	try{
		pstmt = conn.prepareStatement(sqlObtenerCarguio);
		
		rs = pstmt.executeQuery();
		while (rs.next()) {
				
			if (rs.getInt("NUMCAR") != numcaraux && (conti>0)){
				conti=0;
				dto.setOrdenes(ordenes);
				cargu.add(dto);
				dto = new RedespachocDTO();
				ordenes = new ArrayList();
			}
			
			if (conti==0){
				ordenes = new ArrayList();
				dto.setNumeroCarguio(rs.getInt("NUMCAR"));
				dto.setRutChofer(rs.getInt("CAMRUT"));
				dto.setDvChofer(rs.getString("EXMDI3"));
				numcaraux=rs.getInt("NUMCAR");
				conti++;
			}
			
			RedespachodDTO ordendto = new RedespachodDTO();
			ordendto.setNumeroDocumento(rs.getInt("CAMNFA"));
			ordendto.setTipoDocumento(rs.getInt("CAMCO3"));
			ordenes.add(ordendto);
			ordendto = new RedespachodDTO();
			}
			
			if (numcaraux>0) {
				dto.setOrdenes(ordenes);
				cargu.add(dto);
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

		return cargu;	
}
	
public int obtieneCarguioTransp(int rutChofer, String digChofer, int codigoBodega,int numerocarguio){
	
	//int numerocarguio=0;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	String sqlObtenerCarguio = " SELECT "+
	" c.numcar " +
	" FROM CASEDAT.CARGUIOC c " +
	" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
	" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
	" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
	" INNER JOIN CASEDAT.CLIDIR di ON o.clmrut=di.clirut AND o.clmdig=di.clidig AND o.clicor=di.clicor" +
	" INNER JOIN CASEDAT.CLIDIRA da ON o.clmrut=da.clirut AND o.clmdig=da.clidig AND o.clicor=da.clicor" +
	" INNER JOIN CASEDAT.TPTREG re ON re.tptco1=di.clico1" +
	" INNER JOIN CASEDAT.TPTCOM co ON di.clico1=co.tptc19 AND di.clico2=co.tptc20 AND di.clico3=co.tptc22" +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
	" INNER JOIN CASEDAT.TPTDOC td ON ve.venco5=td.tpaco1" +
	" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
	" LEFT JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42" +
	" WHERE c.cacemp=2 AND c.camrut="+rutChofer+" AND c.exmdi3='"+digChofer+"' AND c.camco1="+codigoBodega+" "+
	" AND c.numcar="+numerocarguio+
	" AND c.cames1 <> 'X' " +
	" AND d.exinum=0"+
	" GROUP BY c.numcar "+
	" ORDER BY c.numcar DESC "+
	" FOR READ ONLY";			
	//" AND c.cames1 IN ('E','G','U') " +
	
	try{			
		pstmt = conn.prepareStatement(sqlObtenerCarguio);
		
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			numerocarguio=rs.getInt("NUMCAR");
			break;
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
		return numerocarguio;
	}

public List listarCarguiosTranspRezagados(int rutChofer, String digChofer, int numerocarguio){
	
	int numcaraux=0;
	int conti=0;
	
	List cargu = new ArrayList();
	RedespachocDTO dto = new RedespachocDTO();
	
	List ordenes = new ArrayList();
	
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	
	String sqlObtenerCarguio = " SELECT "+
	" c.numcar, c.camrut, c.exmdi3, ca.camnfa, ca.camco3 "+
	" FROM CASEDAT.CARGUIOC c " +
	" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3" +
	" INNER JOIN CASEDAT.CARGUIOD d ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 " +
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
	" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig" +
	" INNER JOIN CASEDAT.CLIDIR di ON o.clmrut=di.clirut AND o.clmdig=di.clidig AND o.clicor=di.clicor" +
	" INNER JOIN CASEDAT.CLIDIRA da ON o.clmrut=da.clirut AND o.clmdig=da.clidig AND o.clicor=da.clicor" +
	" INNER JOIN CASEDAT.TPTREG re ON re.tptco1=di.clico1" +
	" INNER JOIN CASEDAT.TPTCOM co ON di.clico1=co.tptc19 AND di.clico2=co.tptc20 AND di.clico3=co.tptc22" +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum" +
	" INNER JOIN CASEDAT.TPTDOC td ON ve.venco5=td.tpaco1" +
	" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa" +
	" LEFT JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42" +
	" WHERE c.cacemp=2 AND c.camrut="+rutChofer+" AND c.exmdi3='"+digChofer+"' " +
	" AND c.numcar="+numerocarguio+
	" AND c.cames1 <> 'X' " +
	" AND d.xocl1='' "+
	" AND d.exinum=0"+
	" GROUP BY " +
	" c.numcar, c.camrut, c.exmdi3, ca.camnfa, ca.camco3 "+
	" ORDER BY c.numcar ASC "+
	" FOR READ ONLY";			

	try{
		pstmt = conn.prepareStatement(sqlObtenerCarguio);
		
		rs = pstmt.executeQuery();
		while (rs.next()) {
				
			if (rs.getInt("NUMCAR") != numcaraux && (conti>0)){
				conti=0;
				dto.setOrdenes(ordenes);
				cargu.add(dto);
				dto = new RedespachocDTO();
				ordenes = new ArrayList();
			}
			
			if (conti==0){
				ordenes = new ArrayList();
				dto.setNumeroCarguio(rs.getInt("NUMCAR"));
				dto.setRutChofer(rs.getInt("CAMRUT"));
				dto.setDvChofer(rs.getString("EXMDI3"));
				numcaraux=rs.getInt("NUMCAR");
				conti++;
			}
			
			RedespachodDTO ordendto = new RedespachodDTO();
			ordendto.setNumeroDocumento(rs.getInt("CAMNFA"));
			ordendto.setTipoDocumento(rs.getInt("CAMCO3"));
			ordenes.add(ordendto);
			ordendto = new RedespachodDTO();
			}
			
			if (numcaraux>0) {
				dto.setOrdenes(ordenes);
				cargu.add(dto);
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

		return cargu;	
}



public int buscarCarguioTranspBodega(int rutChofer, String digChofer, int numerocarguio ){
	int codbodega=-100;
	
	String digitoruti="";
	if (digChofer==null || ("".equals(digChofer.trim()))){
		
	} else {
		//digitoruti=" AND c.exmdi3='"+digChofer+"'";
	}
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	String sqlObtenerCldmco = " SELECT "+
	" c.camco1 " +
	" FROM CASEDAT.CARGUIOC c " +
	" WHERE c.cacemp=2 "+
	" AND c.numcar="+numerocarguio+" AND c.camrut="+rutChofer+digitoruti +
	" AND c.cames1 <> 'X' " +
	" GROUP BY " +
	" c.camco1 "+
	" FOR READ ONLY";			
	log.info("SELECT CAMCO1 CARGUIOC : "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		rs = pstmt.executeQuery();
		log.info("OK  SELECT CAMCO1 CARGUIOC");
		while (rs.next()) {
			codbodega=rs.getInt("CAMCO1");
			break;
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
	return codbodega;

}

public int existenumeroVentaBB(int numdoc, int codbod, int rutcli, String digcli){
	int esventabb=-1;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	String sqlObtenerBB ="SELECT bbnumdoc  "+
    " FROM CASEDAT.BBINTC00 " + 
    " WHERE bbcoddoc IN (3,4) "+
    " AND bbnumdoc="+numdoc+
    " AND bbcodbod="+codbod+
    " AND bbrutcli="+rutcli+
    " AND bbdigcli='"+digcli+"'"+
    " GROUP BY bbnumdoc FOR READ ONLY " ;
	
	try{
		pstmt = conn.prepareStatement(sqlObtenerBB);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			esventabb=rs.getInt("BBNUMDOC");
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
	
	return esventabb;
	
}

public String obtieneEstadoCarguioCab(int empresa, int codBodega, int rutChofer, String digChofer, int numCarguio, String patente){
	
	String estadoc="";
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	
	String digitoruti="";
	if (digChofer==null || ("".equals(digChofer.trim()))){
		
	} else {
		//digitoruti=" AND c.exmdi3='"+digChofer+"'";
	}
	
	String sqlObtenerCldmco ="SELECT c.CAMES1 "+
	" FROM CASEDAT.CARGUIOC c" +
	" WHERE c.CACEMP="+empresa+
	" AND c.NUMCAR="+numCarguio+
	" AND c.VECPAT='"+patente+"'"+
	" AND c.CAMCO1="+codBodega+
	" AND c.CAMRUT="+rutChofer+digitoruti+
	" GROUP BY c.CAMES1 "+
	" FOR READ ONLY" ;
	//logi.info("SELECT ESTADO CARGUIOC : "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		rs = pstmt.executeQuery();
		//logi.info("O K E Y    SELECT ESTADO CARGUIOC");
		while (rs.next()) {
			estadoc=rs.getString("CAMES1").trim();
			break;
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
	
	
	return estadoc;
}
















public int actualizarestadoDetalleCarguio99(int empresa, int bodega, int numeroCarguio, int numeroOV, int rutClie){
	int correlativo=-100;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	String sqlObtenerCldmco ="UPDATE "+
    " CASEDAT.CARGUIOD " + 
    " SET XONM2=99 "+
    " Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+
    " AND CAMCO1="+bodega+" AND NUMVEN="+numeroOV+ " AND CLMRUT="+rutClie+" " ;
	log.info("ANTES DE UPDATE CARGUIOD XONM2 =99   : "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		pstmt.executeUpdate();
		log.info("O K E Y  UPDATE CARGUIOD XONM2 =99  : "+sqlObtenerCldmco);
		
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








public List listarCarguiosTranspCorreos(int rutChofer, String digChofer, int numeritodecarguio){
	
	
	int codvendedoraux=	0;
	int codvendedor=0;
	int numoveaux=0;
	int numove=0;
	long numcarguiootro=0;
	long numdocus=0;
	int	conti=0;
	
	List cargu = new ArrayList();
	CarguioTranspDTO dto = new CarguioTranspDTO();
	
	List ordenes=null;
	OrdTranspDTO ordendto = new OrdTranspDTO();
	
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	String connumero="";
	if (numeritodecarguio>0){
		connumero=" AND c.numcar="+numeritodecarguio;
	}
	
	
	String digitoruti="";
	if (digChofer==null || ("".equals(digChofer.trim()))){
		
	} else {
		//digitoruti= " AND c.exmdi3='"+digChofer+"' ";
	}
	
	String cmp ="c.numcar, c.camrut, c.exmdi3, ch.camnom, c.vecpat, c.camco1, o.camcod, o.vecfor, v.exmno1, v2.exmema,"+
	"d.numven, o.exmfec, ca.camnfa, d.vednu2, o.clmrut, o.clmdig, cl.clmnom, ve.vento2, ve.venfe1, ve.venco5, tdo.tpade1,"+
	"bb.bbnumdoc, de.camnum";
	
	String sqlObtenerCarguio=" SELECT "+cmp+ " "+ //VENTAS BB
	" FROM CASEDAT.CARGUIOC c"+
	" INNER JOIN CASEDAT.CARGUIOD d  ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 "+
	" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3"+
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
	" INNER JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42"+
	" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig"+
	" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig AND de.camnum=ve.vennum and ve.vencod<>39"+
	" INNER JOIN CASEDAT.TPTDOC tdo ON ve.venco5=tdo.tpaco1"+
	" INNER JOIN CASEDAT.BBINTC00 bb ON bb.bbcoddoc IN (3,4) AND ve.vennum=bb.bbnumdoc AND ve.venbo2=bb.bbcodbod AND ve.venrut=bb.bbrutcli AND ve.vendig=bb.bbdigcli"+
	" INNER JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa"+
	" WHERE c.cacemp=2 AND c.camrut="+rutChofer+digitoruti+connumero + " "+
	" AND c.cames1 <> 'X' " +
	" AND d.xocl1='' "+
	" AND d.exinum=0"+
	" AND d.venca2>0"+
	" GROUP BY "+cmp+" "+ 
	
	" UNION "+
	
	" SELECT "+cmp+ " "+  //VENTAS LUCY
	" FROM CASEDAT.CARGUIOC c"+
	" INNER JOIN CASEDAT.CARGUIOD d  ON c.cacemp=d.cacemp AND c.numcar=d.numcar AND c.vecpat=d.vecpat AND c.camco1=d.camco1 "+
	" INNER JOIN CASEDAT.CHOFTRAN ch ON c.camrut=ch.camrut AND c.exmdi3=ch.exmdi3"+
	" INNER JOIN CASEDAT.ORDVTA o ON d.cacemp=o.ordemp AND d.camco1=o.tptc18 AND d.numven=o.numven AND d.clmrut=o.clmrut AND d.clmdig=o.clmdig " +
	" INNER JOIN CASEDAT.DETORD de ON o.ordemp=de.ordemp AND o.numven=de.numven AND o.clmrut=de.clmrut AND o.clmdig=de.clmdig AND o.tptc18=de.tptc18 AND o.cames1=de.cames1" +
	" INNER JOIN CASEDAT.EXMVND v ON o.camcod=v.exmc09" +
	" INNER JOIN CASEDAT.EXMOTV v2 ON v.exmc09=v2.exmc42"+
	" INNER JOIN CASEDAT.CLMCLI cl ON o.clmrut=cl.clmrut AND o.clmdig=cl.clmdig"+
	" INNER JOIN CASEDAT.VECMAR ve ON de.ordemp=ve.venemp AND de.tptc18=ve.venbo2 AND ve.vencod=39"+
	" INNER JOIN CASEDAT.TPTDOC tdo ON ve.venco5=tdo.tpaco1"+
	" AND d.vednu2=ve.vennu2 AND de.clmrut=ve.venrut AND de.clmdig=ve.vendig "+
	" LEFT JOIN CASEDAT.CAMTRA ca ON de.ordemp=ca.camemp AND ca.camco2 IN (21,41) AND de.camnum=ca.camnum AND de.tptc18=ca.camco1 AND de.clmrut=ca.camrut AND de.clmdig=ca.camdvr AND ca.camnfa>0 AND ca.camnum<>ca.camnfa"+
	" LEFT JOIN CASEDAT.BBINTC00 bb ON bb.bbcoddoc IN (3,4) AND ve.vennum=bb.bbnumdoc AND ve.venbo2=bb.bbcodbod AND ve.venrut=bb.bbrutcli AND ve.vendig=bb.bbdigcli"+
	" WHERE c.cacemp=2 AND c.camrut="+rutChofer+digitoruti+connumero + " "+
	" AND c.cames1 <> 'X' " +
	" AND d.xocl1='' "+
	" AND d.exinum=0"+
	" AND d.venca2>0 "+
	" GROUP BY "+cmp+ " "+ 
	 
	" ORDER BY numcar asc, camcod asc, numven asc";
	
	
	log.info("SELECT VENTAS BB Y VENTAS LUCY CORREO , SOLICITUD 1  :  "+sqlObtenerCarguio);			
	try{			
		pstmt = conn.prepareStatement(sqlObtenerCarguio);
		rs = pstmt.executeQuery();
		log.info("OK  : SELECT VENTAS BB Y VENTAS LUCY  CORREO , SOLICITUD 1");
	
		while (rs.next()) {
			
			if (rs.getInt("CAMNFA")>0){						
				numdocus=(rs.getInt("CAMNFA"));
			}
			if (rs.getInt("BBNUMDOC")<=0){
				if (rs.getInt("VEDNU2")>0){
					numdocus=(rs.getInt("VEDNU2"));
				}
			}
			codvendedor=rs.getInt("CAMCOD");
			numove=rs.getInt("NUMVEN");
			
			if (numove!=numoveaux){
				
				if (rs.getInt("NUMCAR") != numcarguiootro && (conti>0)){
					conti=0;
					dto.setOrdenes(ordenes);
					cargu.add(dto);
					dto = new CarguioTranspDTO();
					ordenes = new ArrayList();
				}
			
			
				if (conti==0){
					ordenes = new ArrayList();
					dto.setNumeroCarguio(rs.getInt("NUMCAR"));
					dto.setRutChofer(rs.getInt("CAMRUT"));
					dto.setDvChofer(rs.getString("EXMDI3"));
					dto.setNombreChofer(rs.getString("CAMNOM").trim());
					dto.setPatente(rs.getString("VECPAT").trim());
					numcarguiootro=(rs.getInt("NUMCAR"));
					conti++;
				}
			
				ordendto.setNumeroOV(rs.getInt("NUMVEN"));
				ordendto.setFechaDespacho(rs.getInt("EXMFEC"));
				ordendto.setFormaPago(rs.getInt("VECFOR"));
				int fpago=rs.getInt("VECFOR");
				if (fpago==1){
					ordendto.setNombreFormaPago("EFECTIVO");
				} else if (fpago==2)  {
					ordendto.setNombreFormaPago("CREDITO");
				} else {
					ordendto.setNombreFormaPago("CREDITO");
				}
				ordendto.setFechaDocumento(rs.getInt("VENFE1"));
				ordendto.setTipoDocumento(rs.getInt("VENCO5"));
				ordendto.setDescripcionDocumento(rs.getString("TPADE1"));
				
				if (rs.getInt("CAMNFA")>0){						
					ordendto.setNumeroDocumento(rs.getInt("CAMNFA"));
				}
				if (rs.getInt("BBNUMDOC")<=0){
					if (rs.getInt("VEDNU2")>0){
						ordendto.setNumeroDocumento(rs.getInt("VEDNU2"));
					}
				}
				ordendto.setRutCliente(rs.getInt("CLMRUT"));
				ordendto.setDvrutCliente(rs.getString("CLMDIG").trim());
				ordendto.setNombreCliente(rs.getString("CLMNOM").trim());
				ordendto.setNombreVendedor(rs.getString("EXMNO1").trim());
				ordendto.setCodigoVendedor(rs.getInt("CAMCOD"));
				String vendedorcorreo =rs.getString("EXMEMA");
				if (vendedorcorreo==null || vendedorcorreo.trim()==""){
					ordendto.setCorreoVendedor("");
				}else{
					vendedorcorreo=rs.getString("EXMEMA").trim();
					if (vendedorcorreo!=null && !vendedorcorreo.equals("")){
						ordendto.setCorreoVendedor(rs.getString("EXMEMA").trim());
					}else{
						ordendto.setCorreoVendedor("");
					}
				}
				ordendto.setMonto(rs.getInt("VENTO2"));
				
				//revisar si es BB o LUCY
				int numintbb=0;
				if (rs.getInt("CAMNUM")>0){
					numintbb = existenumeroVentaBB(rs.getInt("CAMNUM"), rs.getInt("CAMCO1"), rs.getInt("CLMRUT"), rs.getString("CLMDIG"));
				}
				if (numintbb<=0 && rs.getInt("VEDNU2")>0 && rs.getInt("CAMNFA")<=0){
					ordendto.setNumeroDocumento(rs.getInt("VEDNU2"));
				}
			
				ordenes.add(ordendto);
				ordendto = new OrdTranspDTO();
				
				numoveaux=numove;
				
			
			}
		}
		
		
				dto.setOrdenes(ordenes);
				cargu.add(dto);
			
		
			}catch(Exception e){
				e.printStackTrace();
			}finally {

				try {

					 if (rs != null) { rs.close(); 
					 pstmt.close();
					 }

				} catch (SQLException e1) { }

		  } 
		
	return cargu;
		
	}

public int actualizarestadoDetalleCarguioTerminado(int empresa, int bodega, int numeroCarguio, int numeroOV, int rutClie, int swer){
	int correlativo=-100;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	String sqlObtenerCldmco ="UPDATE "+
    " CASEDAT.CARGUIOD " + 
    " SET XONM3="+swer+
    " Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+
    " AND CAMCO1="+bodega+" AND NUMVEN="+numeroOV+ " AND CLMRUT="+rutClie+" " ;
	//logi.info("ANTES DE UPDATE CARGUIOD XONM3   : "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		pstmt.executeUpdate();
		//logi.info("O K E Y  UPDATE CARGUIOD XONM3  : "+sqlObtenerCldmco);
		
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

public int actualizarestadoDetalleCarguio(int empresa, int bodega, int numeroCarguio, String estado, int numeroOV){
	int correlativo=-100;
	
	PreparedStatement pstmt =null;
	ResultSet rs = null; 
	String sqlObtenerCldmco ="UPDATE  "+
    "  CASEDAT.CARGUIOD " + 
    " SET CAMES1='"+estado+"' Where CACEMP="+empresa+" AND NUMCAR="+numeroCarguio+
    " AND CAMCO1="+bodega+" AND NUMVEN="+numeroOV+ " " ;
	//logi.info("ANTES DE UPDATE CARGUIOD CAMES1  :  "+sqlObtenerCldmco);
	try{
		pstmt = conn.prepareStatement(sqlObtenerCldmco);
		pstmt.executeUpdate();
		//logi.info("O K E Y    UPDATE CARGUIOD CAMES1  :  "+sqlObtenerCldmco);
		
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

public String verificaEstadoCarguioTransp(int codEmpresa, int numCarguio, String patente, int codBodega, int numOV, int rutCli){
	
	String estado="";
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	String sqlObtener = " SELECT c.CAMES1 "+
	" FROM CASEDAT.CARGUIOC c"+
	" INNER JOIN CASEDAT.CARGUIOD d"+
	" ON c.CACEMP=d.CACEMP AND c.NUMCAR=d.NUMCAR AND c.VECPAT=d.VECPAT AND c.CAMCO1=d.CAMCO1"+
	" WHERE c.CACEMP="+codEmpresa+" AND c.NUMCAR="+numCarguio+ " AND c.VECPAT='"+patente+"' AND c.CAMCO1="+codBodega+
	" AND d.NUMVEN="+numOV+ " AND d.CLMRUT="+rutCli+
	" AND d.venca2>0 "+
	" GROUP BY c.CAMES1 "+
	" FOR READ ONLY";
	//logi.info("ANTES DE SELECT CAMES1 DE CARGUIOC : "+sqlObtener);
	try{			
		pstmt = conn.prepareStatement(sqlObtener);
		rs = pstmt.executeQuery();
		//logi.info("O K E Y    DE SELECT CAMES1 DE CARGUIOC : "+sqlObtener);
		while (rs.next()) {
			estado=rs.getString("CAMES1").trim();
			break;
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

	
	
	
	return estado;
			
	
}

public String verificaEstadoXOCL1CarguioTransp(int codEmpresa, int numCarguio, String patente, int codBodega, int numOV, int rutCli){
	
	String estadoent="";
	PreparedStatement pstmt =null;
	ResultSet rs = null;
	
	String sqlObtener = " SELECT d.XOCL1 "+
	" FROM CASEDAT.CARGUIOC c"+
	" INNER JOIN CASEDAT.CARGUIOD d"+
	" ON c.CACEMP=d.CACEMP AND c.NUMCAR=d.NUMCAR AND c.VECPAT=d.VECPAT AND c.CAMCO1=d.CAMCO1"+
	" WHERE c.CACEMP="+codEmpresa+" AND c.NUMCAR="+numCarguio+ " AND c.VECPAT='"+patente+"' AND c.CAMCO1="+codBodega+
	" AND d.NUMVEN="+numOV+ " AND d.CLMRUT="+rutCli+
	" AND d.XOCL1<>'' " +
	" AND d.venca2>0 "+
	" GROUP BY d.XOCL1 "+
	" FOR READ ONLY";
	//logi.info("SELECT XOCL1 CARGUIOD : "+sqlObtener);
	try{			
		pstmt = conn.prepareStatement(sqlObtener);
		rs = pstmt.executeQuery();
		//logi.info("O K E Y   SELECT XOCL1 CARGUIOD : "+sqlObtener);
		while (rs.next()) {
			estadoent=rs.getString("XOCL1").trim();
			break;
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

	return estadoent;
			
	
}

}
