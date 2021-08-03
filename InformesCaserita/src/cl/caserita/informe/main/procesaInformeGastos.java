package cl.caserita.informe.main;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import cl.caserita.comunes.db.ConectorDB2;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.InfogastoDAO;
import cl.caserita.dao.iface.RemcrdDAO;
import cl.caserita.dao.iface.RemcrhDAO;
import cl.caserita.dto.InfogastoDTO;
import cl.caserita.dto.RemcrdDTO;
import cl.caserita.dto.RemcrhDTO;
import cl.caserita.enviomail.main.EnvioMailCaseritaAdjuntoGasto;
import cl.caserita.enviomail.main.envioMailCaseritaAdjunto;
import cl.caserita.informe.dao.ConsultaDatosDAO;
import cl.caserita.informe.dto.ExmgesDTO;
import cl.caserita.informe.process.generaExcel;
import cl.caserita.informe.process.generaExcelInformeGastos;

public class procesaInformeGastos {

	private static String usr="";
	private static String pass="";
	private static String servidor="";
	private static String bdd ="";
	
	public void procesaInforme(String fecha , String hora, String mail, int periodoIni, int periodoFin){
		Connection conn=null;
		
		cargaProperties();
		ConectorDB2 conector=new ConectorDB2();
		conector.setUserDB2(usr);
		conector.setPassDB2(pass);
		conector.setIpServer(servidor);
		DAOFactory daoFactory = DAOFactory.getInstance();
		
		InfogastoDAO info = daoFactory.getInfogastoDAO();
		RemcrhDAO remcrh = daoFactory.getRemcrhDAO();
		RemcrdDAO remcrd = daoFactory.getRemcrdDAO();
		
		
		try{
			conn = conector.ConexioniSeries();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ConsultaDatosDAO dao = new ConsultaDatosDAO();
		dao.SetConnection(conn);
		try{
			System.out.println("Periodo RRHH:"+periodoIni);
			List remcrhList = remcrh.obtieneHaberes(periodoIni);
			List remcrdList = remcrd.obtieneDescuentos(periodoIni);
			Iterator iter = remcrhList.iterator();
			RemcrhDTO remcrhDTO =null;
			int transaccion=info.obtieneCorrelativo()+1;
			System.out.println("Correlativo Insert InfGasto:"+transaccion);
			while (iter.hasNext()){
				remcrhDTO = (RemcrhDTO) iter.next();
				InfogastoDTO infoDTO = new InfogastoDTO();
				infoDTO.setTransaccion(transaccion);
				infoDTO.setCodigoInforme("REMUN");
				infoDTO.setFechaGeneracion(Integer.parseInt(fecha));
				infoDTO.setHoraGeneracion(Integer.parseInt(hora));
				infoDTO.setRut(remcrhDTO.getRutTrabajador());
				infoDTO.setDv(remcrhDTO.getDvTrabajador());
				infoDTO.setDescripcion1(remcrhDTO.getDescHaber());
				infoDTO.setFechaDocumento(remcrhDTO.getPeriodo());
				infoDTO.setDescripcion2(remcrhDTO.getDescHaber());
				infoDTO.setDescripcion3(remcrhDTO.getDescHaber());
				infoDTO.setDescripcion4(remcrhDTO.getDescHaber());
				//infoDTO.setDescripcion4("");
				infoDTO.setCodigo1("");
				infoDTO.setCodigo2("");
				infoDTO.setCodigo3("");
				infoDTO.setCodigo4("");
				infoDTO.setDescripcion(remcrhDTO.getDescHaber());
				infoDTO.setDescripcionBodega(remcrhDTO.getDescripcionCentroCosto());
				infoDTO.setTipoDocumento("");
				infoDTO.setRazonSocial(remcrhDTO.getNombreTrabajador().trim());
				infoDTO.setValorTotal((int) remcrhDTO.getMontoHaber());
				info.insertaGastoRRHH(infoDTO);
				transaccion=transaccion+1;
			}
			RemcrdDTO remcrdDTO = null;
			/*Iterator iter2 = remcrdList.iterator();
			while (iter2.hasNext()){
				remcrdDTO = (RemcrdDTO) iter2.next();
				InfogastoDTO infoDTO = new InfogastoDTO();
				infoDTO.setTransaccion(transaccion);
				infoDTO.setCodigoInforme("REMUN");
				infoDTO.setFechaGeneracion(Integer.parseInt(fecha));
				infoDTO.setHoraGeneracion(Integer.parseInt(hora));
				infoDTO.setRut(remcrdDTO.getRutTrabajador());
				infoDTO.setDv(remcrdDTO.getDigTrabajador());
				infoDTO.setDescripcion1(remcrdDTO.getDescripcionDescuento());
				infoDTO.setFechaDocumento(remcrdDTO.getPeriodo());
				infoDTO.setDescripcion2("");
				infoDTO.setDescripcion3("");
				infoDTO.setDescripcion4("");
				infoDTO.setDescripcion4("");
				infoDTO.setCodigo1("");
				infoDTO.setCodigo2("");
				infoDTO.setCodigo3("");
				infoDTO.setCodigo4("");
				infoDTO.setDescripcion(remcrdDTO.getDescripcionDescuento());
				infoDTO.setDescripcionBodega(remcrdDTO.getDescripcionCCosto());
				infoDTO.setTipoDocumento("");
				infoDTO.setRazonSocial(remcrdDTO.getNombreTrabajador().trim());
				infoDTO.setValorTotal((int)remcrdDTO.getMontoDescuento());
				info.insertaGastoRRHH(infoDTO);
				transaccion=transaccion+1;
			}*/
			
			List lista = dao.obtieneGastos(fecha, hora);
			generaExcelInformeGastos excel = new generaExcelInformeGastos();
			String nombre = excel.generaExcelGes(lista);
			EnvioMailCaseritaAdjuntoGasto envio = new EnvioMailCaseritaAdjuntoGasto();
			System.out.println("Correo :" + dao.obtieneCorreo());
			boolean exito = envio.envioMail(nombre, dao.obtieneCorreo(),"","Informe de Gastos");
			System.out.println("Exitoso o no :" + exito);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	public static void cargaProperties(){
		usr="jhcanquil";
		pass="guitarrero";
		servidor="192.168.1.10";
		bdd="CASEDAT";
		
	}
	public static void main (String []args){
		//procesaInformeGastos pro = new procesaInformeGastos();
		//pro.procesaInforme("20140402", "154900", "jcanquil@gmail.com", 201401, 201401);
		cargaProperties();
		ConectorDB2 conector=new ConectorDB2();
		conector.setUserDB2(usr);
		conector.setPassDB2(pass);
		conector.setIpServer("192.168.1.10");
		DAOFactory daoFactory = DAOFactory.getInstance();
		Connection conn=null;
		try{
			conn = conector.ConexioniSeries();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ConsultaDatosDAO con = new ConsultaDatosDAO();
		con.SetConnection(conn);
		List art = con.articulos();
		Iterator list = art.iterator();
		while (list.hasNext()){
			ExmgesDTO dto = (ExmgesDTO) list.next();
			double im = con.precotiW(Integer.parseInt(dto.getCodArticulo()));
			double im1 = con.precotiW2(Integer.parseInt(dto.getCodArticulo()));
			con.actualiza(Integer.parseInt(dto.getCodArticulo()), im, im1);
		}
		
		
	}
	
}
