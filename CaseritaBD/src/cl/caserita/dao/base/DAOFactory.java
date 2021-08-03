package cl.caserita.dao.base;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cl.caserita.comunes.db.ConectorDB2;
import cl.caserita.comunes.properties.Constants;
import cl.caserita.dao.iface.AdsusrDAO;
import cl.caserita.dao.iface.ArchordDAO;
import cl.caserita.dao.iface.BBintc00DAO;
import cl.caserita.dao.iface.CajasDAO;
import cl.caserita.dao.iface.CardopdfDAO;
import cl.caserita.dao.iface.CargcestDAO;
import cl.caserita.dao.iface.CargconwDAO;
import cl.caserita.dao.iface.CargfwmsDAO;
import cl.caserita.dao.iface.CarguiadDAO;
import cl.caserita.dao.iface.CarguioDAO;
import cl.caserita.dao.iface.CarguiodDAO;
import cl.caserita.dao.iface.CarmailDAO;
import cl.caserita.dao.iface.CarswmsDAO;
import cl.caserita.dao.iface.CartolasDAO;
import cl.caserita.dao.iface.CentroDistribucionDAO;
import cl.caserita.dao.iface.ChoftranDAO;
import cl.caserita.dao.iface.ClidirDAO;
import cl.caserita.dao.iface.ClienteCanastaDAO;
import cl.caserita.dao.iface.ConsolidaasnDAO;
import cl.caserita.dao.iface.ConvafDAO;
import cl.caserita.dao.iface.DetordDAO;
import cl.caserita.dao.iface.DetswmsDAO;
import cl.caserita.dao.iface.DocGenelDAO;
import cl.caserita.dao.iface.DocRecibidosDiaDAO;
import cl.caserita.dao.iface.DocconfcDAO;
import cl.caserita.dao.iface.DocerrorDAO;
import cl.caserita.dao.iface.DocncpDAO;
import cl.caserita.dao.iface.EmaapmDAO;
import cl.caserita.dao.iface.EncprinDAO;
import cl.caserita.dao.iface.EncswmsDAO;
import cl.caserita.dao.iface.EndPointWSDAO;
import cl.caserita.dao.iface.EnvdmailDAO;
import cl.caserita.dao.iface.ErrorTransportistaDAO;
import cl.caserita.dao.iface.ExdacbDAO;
import cl.caserita.dao.iface.ExdacpDAO;
import cl.caserita.dao.iface.ExdartDAO;
import cl.caserita.dao.iface.ExdfcprDAO;
import cl.caserita.dao.iface.ExdodcDAO;
import cl.caserita.dao.iface.ExdrecDAO;
import cl.caserita.dao.iface.ExmarbDAO;
import cl.caserita.dao.iface.ExmartDAO;
import cl.caserita.dao.iface.ExmfwmsDAO;
import cl.caserita.dao.iface.ExmrecDAO;
import cl.caserita.dao.iface.ExmvndDAO;
import cl.caserita.dao.iface.ExtariDAO;
import cl.caserita.dao.iface.ExtfwmsDAO;
import cl.caserita.dao.iface.FaccarguDAO;
import cl.caserita.dao.iface.FtpprovDAO;
import cl.caserita.dao.iface.GeovtaveDAO;
import cl.caserita.dao.iface.HorarioDAO;
import cl.caserita.dao.iface.IdDireccionDAO;
import cl.caserita.dao.iface.InfogastoDAO;
import cl.caserita.dao.iface.LogcanpeDAO;
import cl.caserita.dao.iface.LogintegracionDAO;
import cl.caserita.dao.iface.NcplogDAO;
import cl.caserita.dao.iface.ObordenDAO;
import cl.caserita.dao.iface.OrdswmsDAO;
import cl.caserita.dao.iface.OrdtrbDAO;
import cl.caserita.dao.iface.OrdttxtDAO;
import cl.caserita.dao.iface.OrdvddeDAO;
import cl.caserita.dao.iface.OrdvdetDAO;
import cl.caserita.dao.iface.OrdvtaDAO;
import cl.caserita.dao.iface.OrdvtadDAO;
import cl.caserita.dao.iface.PaisDAO;
import cl.caserita.dao.iface.ProcedimientoDAO;
import cl.caserita.dao.iface.PryenccDAO;
import cl.caserita.dao.iface.RemcrdDAO;
import cl.caserita.dao.iface.RemcrhDAO;
import cl.caserita.dao.iface.RespquadDAO;
import cl.caserita.dao.iface.RutservDAO;
import cl.caserita.dao.iface.StockdifDAO;
import cl.caserita.dao.iface.StockinventarioDAO;
import cl.caserita.dao.iface.TipodespachoDAO;
import cl.caserita.dao.iface.TiponegocioDAO;
import cl.caserita.dao.iface.TpacorDAO;
import cl.caserita.dao.iface.TpctraDAO;
import cl.caserita.dao.iface.TptDocDAO;
import cl.caserita.dao.iface.TptcomDAO;
import cl.caserita.dao.iface.UsrWebDAO;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dao.iface.VarcostDAO;
import cl.caserita.dao.iface.VecfwmsDAO;
import cl.caserita.dao.iface.VecmonDAO;
import cl.caserita.dao.iface.VedfaltDAO;
import cl.caserita.dao.iface.VencobDAO;
import cl.caserita.dao.iface.VentaprobDAO;
import cl.caserita.dao.impl.*;
import cl.caserita.dao.iface.TptimpDAO;
import cl.caserita.dao.iface.TpttvdDAO;
import cl.caserita.dao.iface.TiptoleDAO;
import cl.caserita.dao.iface.ExmcreDAO;
import cl.caserita.dao.iface.ExmrecvaDAO;
import cl.caserita.dao.iface.ExmvenviDAO;
import cl.caserita.dao.iface.ImpauditDAO;

public class DAOFactory {
	
	private static Connection conn;
	private  static Logger log = Logger.getLogger(DAOFactory.class);
	private static String DATASOURCE = "";
	private static Properties prop=null;
	private DAOFactory() {
		
		//obtiene datasource
		
		getConnection();
	}

	private DAOFactory(String url,String user, String pass){
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			log.error("Error al conectar a base de datos: " + e.getMessage());
		}
	}

//	public static DAOFactory getInstance() {
//		return new DAOFactory();
//	}
	
			
	public static DAOFactory getInstance() {
		//log.debug("Setea BDD");
		
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String ipServer=prop.getProperty("ipServer");
		String userDB2=prop.getProperty("USER");
		String passDB2=prop.getProperty("CLAVE");
		
		/*String ipServer="192.168.1.10";
		String userDB2="SANTANDER";
		String passDB2="BANCOSANT";*/
		
		log.info("IPSERVERNuevo" + ipServer);
		log.info("UserNuevo" + userDB2);
		log.info("PassNuevo" + passDB2);
		
		ConectorDB2 conector = new ConectorDB2();
		conector.setIpServer(ipServer);
		conector.setUserDB2(userDB2);
		conector.setPassDB2(passDB2);
		try{
			conn = conector.ConexioniSeries();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return new DAOFactory("jdbc:as400://"+ipServer,userDB2,passDB2);
	}
	
	public static DAOFactory getInstance2() {
		//log.debug("Setea BDD");
		
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config2.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String ipServer="192.168.1.12";
		String userDB2="AS400";
		String passDB2="AS400";
		
	/*	String ipServer="192.168.1.10";
		String userDB2="SANTANDER";
		String passDB2="BANCOSANT";*/
		
		log.info("IPSERVERNuevo" + ipServer);
		log.info("UserNuevo" + userDB2);
		log.info("PassNuevo" + passDB2);
		
		ConectorDB2 conector = new ConectorDB2();
		conector.setIpServer(ipServer);
		conector.setUserDB2(userDB2);
		conector.setPassDB2(passDB2);
		try{
			conn = conector.ConexioniSeries();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return new DAOFactory("jdbc:as400://"+ipServer,userDB2,passDB2);
	}

	public static DAOFactory getInstance3() {
		//log.debug("Setea BDD");
		
		prop = new Properties();
		try{
			//System.out.println("Ruta Properties:"+Constants.FILE_PROPERTIES);
			prop.load(new FileInputStream(Constants.FILE_PROPERTIES+"config2.properties"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String ipServer="192.168.1.10";
		String userDB2="AS400";
		String passDB2="AS400";
		
	/*	String ipServer="192.168.1.10";
		String userDB2="SANTANDER";
		String passDB2="BANCOSANT";*/
		
		log.info("IPSERVERNuevo" + ipServer);
		log.info("UserNuevo" + userDB2);
		log.info("PassNuevo" + passDB2);
		
		ConectorDB2 conector = new ConectorDB2();
		conector.setIpServer(ipServer);
		conector.setUserDB2(userDB2);
		conector.setPassDB2(passDB2);
		try{
			conn = conector.ConexioniSeries();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return new DAOFactory("jdbc:as400://"+ipServer,userDB2,passDB2);
	}

	private void getConnection() {			
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource)ic.lookup(DAOFactory.DATASOURCE);
					getInstance().
			conn = ds.getConnection();
		} catch (Exception e) {
			log.error("Error al conectar a base de datos: " + e.getMessage());
		}
	}
	
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) { }
	}
	
	public void iniciaTransaccion(){
		try{
			conn.setAutoCommit(false);
		}catch(Exception e){
			log.error("Error al iniciar transaccion");
		}
	}
	
	public void commitTransaccion(){
		try{
			conn.commit();
		}catch(Exception e){
			log.error("Error al realizar commit");
		}
	}
	
	public void rollbackTransaccion(){
		try{
			conn.rollback();
		}catch(Exception e){
			log.error("Error al realizar rollback");
		}
	}
	
	public VecmarDAOImpl getVecmarDAO(){
		return new VecmarDAOImpl(conn);
	}
	
	public ClcmcoDAOImpl getClcmcoDAO(){
		return new ClcmcoDAOImpl(conn);
	}
	
	public CldmcoDAOImpl getCldmcoDAO(){
		return new CldmcoDAOImpl(conn);
	}
	
	public ClcdiaDAOImpl getClcdiaDAO(){
		return new ClcdiaDAOImpl(conn);
	}
	
	public ClmcliDAOImpl getClmcliDAO(){
		return new ClmcliDAOImpl(conn);
	}
	
	public CamtraDAOImpl getCamtraDAO(){
		return new CamtraDAOImpl(conn);
	}
	
	public TptdeleDAOImpl getTptdeleDAO(){
		return new TptdeleDAOImpl(conn);
	}
	public ConnohDAOImpl getConnohDAO(){
		return new ConnohDAOImpl(conn);
	}
	public ConnodDAOImpl getConnodDAO(){
		return new ConnodDAOImpl(conn);
	}
	public ConnoidcDAOImpl getConnoidcDAO(){
		return new ConnoidcDAOImpl(conn);
	}
	public ExmtraDAOImpl getExmtraDAO(){
		return new ExmtraDAOImpl(conn);
	}
	public ExdtraDAOImpl getExdtraDAO(){
		return new ExdtraDAOImpl(conn);
	}
	public VedmarDAOImpl getVedmarDAO(){
		return new VedmarDAOImpl(conn);
	}
	public ConnoiDAOImpl getConnoiDAO(){
		return new ConnoiDAOImpl(conn);
	}
	
	public BdgCorrDAOImpl getBdgCorrDAO(){
		return new BdgCorrDAOImpl(conn);
	}
	
	public NotCorreDAOImpl getNotCorreDAO(){
		return new NotCorreDAOImpl(conn);
	}
	
	public ClddiaDAOImpl getClddiaDAO(){
		return new ClddiaDAOImpl(conn);
	}
	
	public TptbdgDAOImpl getTptbdgDAO(){
		return new TptbdgDAOImpl(conn);
	}
	public LibtpdDAOImpl getLibtpdDAO(){
		return new LibtpdDAOImpl(conn);
	}
	
	public ConarcDAOImpl getConarcDAO(){
		return new ConarcDAOImpl(conn);
	}
	public ExmodcDAOImpl getExmodcDAO(){
		return new ExmodcDAOImpl(conn);
	}
	public CotplcDAOImpl getCotplcDAO(){
		return new CotplcDAOImpl(conn);
	}
	public Conar1DAOImpl getConar1DAO(){
		return new Conar1DAOImpl(conn);
	}
	
	public ActecoDAOImpl getActecoDAO(){
		return new ActecoDAOImpl(conn);
	}
	public DetlibDAOImpl getDetlibDAO(){
		return new DetlibDAOImpl(conn);
	}
	public DetimlibDAOImpl getDetimlibDAO(){
		return new DetimlibDAOImpl(conn);
	}
	public ReslibDAOImpl getReslibDAO(){
		return new ReslibDAOImpl(conn);
	}
	public ResimlibDAOImpl getResimlibDAO(){
		return new ResimlibDAOImpl(conn);
	}
	
	public GenlibDAOImpl getGenlibDAO(){
		return new GenlibDAOImpl(conn);
	}
	
	public TptempDAOImpl getTptempDAO(){
		return new TptempDAOImpl(conn);
		
	}
	public PrmprvDAOImpl getPrmprvDAO(){
		return new PrmprvDAOImpl(conn);
		
	}
	public PrdatcaDAOImpl getPrdatcaDAO(){
		return new PrdatcaDAOImpl(conn);
		
	}
	public ImpcasiiDAOImpl getImpcasiiDAO(){
		return new ImpcasiiDAOImpl(conn);
		
	}
	
	public CasesmtpDAOImpl getCasesmtpDAO(){
		return new CasesmtpDAOImpl(conn);
		
	}
	
	public RemcrhDAO getRemcrhDAO(){
		return new RemcrhDAOImpl(conn);
		
	}
	public RemcrdDAO getRemcrdDAO(){
		return new RemcrdDAOImpl(conn);
		
	}
	public InfogastoDAO getInfogastoDAO(){
		return new InfogastoDAOImpl(conn);
		
	}
	public EndPointWSDAO getEndPointWSDAO(){
		return new EndPointWSDAOImpl(conn);
		
	}
	public DocRecibidosDiaDAO getDocRecibidosDiaDAO(){
		return new DocRecibidosDiaDAOImpl(conn);
		
	}
	public UsrWebDAO getUsrWebDAO(){
		return new UsrWebDAOImpl(conn);
	}
	public AdsusrDAO getAdsusrDAO(){
		return new AdsusrDAOImpl(conn);
	}
	public OrdvtaDAO getOrdvtaDAO(){
		return new OrdvtaDAOImpl(conn);
	}
	
	public CarguioDAO getCarguioDAO(){
		return new CarguioDAOImpl(conn);
	}
	
	public DetordDAO getDetordDAO(){
		return new DetordDAOImpl(conn);
	}
	
	public TiponegocioDAO getTiponegocioDAO(){
		return new TiponegocioDAOImpl(conn);
	}
	public PaisDAO getPaisDAO(){
		return new PaisDAOImpl(conn);
	}
	
	public UsrcanastaDAO getUsrcanastaDAO(){
		return new UsrcanastaDAOImpl(conn);
	}
	
	
	public ClidirDAO getClidirDAO(){
		return new ClidirDAOImpl(conn);
	}
	
	public ClienteCanastaDAO getClienteCanastaDAO(){
		return new ClienteCanastaDAOImpl(conn);
	}
	public TipodespachoDAO getTipodespachoDAO(){
		return new TipodespachoDAOImpl(conn);
	}
	public HorarioDAO getHorarioDAO(){
		return new HorarioDAOImpl(conn);
	}
	public CentroDistribucionDAO getCentroDistribucionDAO(){
		return new CentroDistribucionDAOImpl(conn);
	}
	public IdDireccionDAO getIdDireccionDAO()
    {
        return new IdDireccionDAOImpl(conn);
    }
	
	public CartolasDAO getCartolasDAO()
    {
        return new CartolasDAOImpl(conn);
    }
	public CajasDAO getCajasDAO()
    {
        return new CajasDAOImpl(conn);
    }
	public DocerrorDAO getDocerrorDAO()
    {
        return new DocerrorDAOImpl(conn);
    }
	public ExmartDAO getExmartDAO()
    {
        return new ExmartDAOImpl(conn);
    }
	public TpctraDAO getTpctraDAO()
    {
        return new TpctraDAOImpl(conn);
    }
	
	public ExdodcDAO getExdodcDAO()
    {
        return new ExdodcDAOImpl(conn);
    }
	
	public ExmarbDAO getExmarbDAO()
    {
        return new ExmarbDAOImpl(conn);
    }
	
	public TpacorDAO getTpacorDAO()
    {
        return new TpacorDAOImpl(conn);
    }
	
	public TptcomDAO getTptcomDAO()
    {
        return new TptcomDAOImpl(conn);
    }
	public ExdacbDAO getExdacbDAO()
    {
        return new ExdacbDAOImpl(conn);
    }
	
	public LogintegracionDAO getLogintegracionDAO()
    {
        return new LogintegracionDAOImpl(conn);
    }
	
	public DocGenelDAO getDocGenelDAO()
    {
        return new DocGenelDAOImpl(conn);
    }
	
	public ExmrecDAO getExmrecDAO()
    {
        return new ExmrecDAOImpl(conn);
    }
	
	public ExdrecDAO getExdrecDAO()
    {
        return new ExdrecDAOImpl(conn);
    }
	
	public ProcedimientoDAO getProcedimientoDAO()
    {
        return new ProcedimientoDAOImpl(conn);
    }
	
	public StockinventarioDAO getStockinventarioDAO()
    {
        return new StockinventarioDAOImpl(conn);
    }
	
	public ConsolidaasnDAO getConsolidaasnDAO()
    {
        return new ConsolidaasnDAOImpl(conn);
    }
	public ExdartDAO getExdartDAO()
    {
        return new ExdartDAOImpl(conn);
    }
	
	public VecfwmsDAO getVecfwmsDAO()
    {
        return new VecfwmsDAOImpl(conn);
    }
	public ExmfwmsDAO getExmfwmsDAO()
    {
        return new ExmfwmsDAOImpl(conn);
    }
	public ExtfwmsDAO getExtfwmsDAO()
    {
        return new ExtfwmsDAOImpl(conn);
    }
	public CargfwmsDAO getCargfwmsDAO()
    {
        return new CargfwmsDAOImpl(conn);
    }
	public ConvafDAO getConvafDAO()
	{
		return new ConvafDAOImpl(conn);
	}
	
	public ExdfcprDAO getExdfcprDAO()
	{
		return new ExdfcprDAOImpl(conn);
	}
	public CarguiodDAO getCarguiodDAO()
	{
		return new CarguiodDAOImpl(conn);
	}
	public VedfaltDAO getVedfaltDAO()
	{
		return new VedfaltDAOImpl(conn);
	}
	public FaccarguDAO getFaccarguDAO()
	{
		return new FaccarguDAOImpl(conn);
	}
	
	public ChoftranDAO getChoftranDAO()
	{
		return new ChoftranDAOImpl(conn);
	}
	public FtpprovDAO getFtpprovDAO()
	{
		return new FtpprovDAOImpl(conn);
	}
	public DocconfcDAO getDocconfcDAO()
	{
		return new DocconfcDAOImpl(conn);
	}
	public StockdifDAO getStockdifDAO()
	{
		return new StockdifDAOImpl(conn);
	}
	public DocncpDAO getDocncpDAO(){
		return new DocncpDAOImpl(conn);
	}
	public CargcestDAO getCargcestDAO(){
		return new CargcestDAOImpl(conn);
	}
	public TptimpDAO getTptimpDAO(){
		return new TptimpDAOImpl(conn);
	}
	public TiptoleDAO getTiptoleDAO(){
		return new TiptoleDAOImpl(conn);
	}
	public ExmcreDAO getExmcreDAO(){
		return new ExmcreDAOImpl(conn);
	}
	public RutservDAO getRutServDAO(){
		return new RutservDAOImpl(conn);
	}
	public ExmvndDAO getExmvndDAO(){
		return new ExmvndDAOImpl(conn);
	}
	public ExmrecvaDAO getExmrecvaDAO(){
		return new ExmrecvaDAOImpl(conn);
	}
	public ImpauditDAO getImpauditDAO(){
		return new ImpauditDAOImpl(conn);
	}
	
	public VarcostDAO getVarcostDAO(){
		return new VarcostDAOImpl(conn);
	}
	public ExtariDAO getExtariDAO(){
		return new ExtariDAOImpl(conn);
	}
	public EnvdmailDAO getEnvdmailDAO(){
		return new EnvdmailDAOImpl(conn);
	}
	public EmaapmDAO getEmaapmDAO(){
		return new EmaapmDAOImpl(conn);
	}
	public EncswmsDAO getEncswmsDAO(){
		return new EncswmsDAOImpl(conn);
	}
	public DetswmsDAO getDetswmsDAO(){
		return new DetswmsDAOImpl(conn);
	}
	public CarswmsDAO getCarswmsDAO(){
		return new CarswmsDAOImpl(conn);
	}
	public OrdswmsDAO getOrdswmsDAO(){
		return new OrdswmsDAOImpl(conn);
	}
	public LogcanpeDAO getLogcanpeDAO(){
		return new LogcanpeDAOImpl(conn);
	}
	public PryenccDAO getPryenccDAO(){
		return new PryenccDAOImpl(conn);
	}
	public EncprinDAO getEncprinDAO(){
		return new EncprinDAOImpl(conn);
	}
	
	
	public ObordenDAO getObordenDAO(){
		return new ObordenDAOImpl(conn);
	}
	
	
	public ErrorTransportistaDAO getErrorTransportistaDAO(){
		return new ErrorTransportistaDAOImpl(conn);
	}
	
	public OrdvddeDAO getOrdvddeDAO(){
		return new OrdvddeDAOImpl(conn);
	}
	
	public ExdacpDAO getExdacpDAO(){
		return new ExdacpDAOImpl(conn);
	}
	public OrdvdetDAO getOrdvdetDAO(){
		return new OrdvdetDAOImpl(conn);
	}
	public CarguiadDAO getCarguiadDAO(){
		return new CarguiadDAOImpl(conn);
	}
	public RespquadDAO getRespquadDAO(){
		return new RespquadDAOImpl(conn);
	}
	public CargconwDAO getCargconwDAO(){
		return new CargconwDAOImpl(conn);
	}
	
	public OrdvtadDAO getOrdvtadDAO(){
		return new OrdvtadDAOImpl(conn);
	}
	
	public OrdtrbDAO getOrdtrbDAO(){
		return new OrdtrbDAOImpl(conn);
	}
	
	public OrdttxtDAO getOrdttxtDAO(){
		return new OrdttxtDAOImpl(conn);
	}
	
	public VecmonDAO getVecmonDAO(){
		return new VecmonDAOImpl(conn);
	}
	public GeovtaveDAO getGeovtaveDAO(){
		return new GeovtaveDAOImpl(conn);
	}
	
	public BBintc00DAO getBBintc00DAO(){
		return new BBintc00DAOImpl(conn);
	}
	
	public VencobDAO getVencobDAO(){
		return new VencobDAOImpl(conn);
	}
	public ExmvenviDAO getExmvenviDAO(){
		return new ExmvenviDAOImpl(conn);
	}
	
	public ArchordDAO getArchordDAO(){
		return new ArchordDAOImpl(conn);
	}
	public TpttvdDAO getTpttvdDAO(){
		return new TpttvdDAOImpl(conn);
	}
	
	public CardopdfDAO getCardopdfDAO(){
		return new CardopdfDAOImpl(conn);
	}
	
	public CarmailDAO getCarmailDAO(){
		return new CarmailDAOImpl(conn);
	}
	
	public VentaprobDAO getVentaprobDAO(){
		return new VentaprobDAOImpl(conn);
	}
	
	public TptDocDAO getTptdocDAO(){
		return new TptdocDAOImpl(conn);
	}
	public NcplogDAO getNcplogDAO(){
		return new NcplogDAOImpl(conn);
	}
	
}