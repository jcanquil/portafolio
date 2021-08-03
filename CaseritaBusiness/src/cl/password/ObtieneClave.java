package cl.caserita.password;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import cl.caserita.canastas.helper.EncriptaHelper;
import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.AdsusrDAO;

public class ObtieneClave {
	private  static Logger log = Logger.getLogger(ObtieneClave.class);

	public String convierteClave(String password){
		String clave="";
		
		int posicion=0;
		int largo=10;
		String letras="";
		int numero=password.length()/10;
		password = password.trim();
		password = password.replaceAll("\n","");
		log.info("Password a Convertir:"+password);
		log.info("Largo:"+password.length());
		DAOFactory dao = DAOFactory.getInstance();
		AdsusrDAO adsUsr = dao.getAdsusrDAO();
		try{
			if (numero>=1){
				while (password.length()>posicion){
					log.info("posicion:"+posicion);
					log.info("largo:"+largo);
					String sub = password.substring(posicion, largo);
					if (letras!="" && letras!=null){
						 letras = letras.trim() + adsUsr.obtieneLetra(sub.trim());
					}else{
						letras = adsUsr.obtieneLetra(sub.trim());
					}
					
					posicion=posicion+10;
					largo=largo+10;
				}
				clave=letras;
			}
		}catch(Exception e){
			e.printStackTrace();
			clave="";
			return clave;
		}
		
		
		
		return clave;
				
	}
	public String encriptaClave(String password){
		String clave="";
		DAOFactory dao = DAOFactory.getInstance();
		AdsusrDAO adsUsr = dao.getAdsusrDAO();
		int posicion=0;
		int largo=1;
		String letras="";
		int numero=password.length()/1;
		//log.info("Password : "+password);
		//log.info("numero : "+numero);

		try{
			if (numero>=1){
				while (password.length()>posicion){
					//log.info("Ingresar a procesar password");
					String sub = password.substring(posicion, largo);
					if (letras!="" && letras!=null){
						 letras = letras.trim() + adsUsr.obtieneLetraEncriptada(sub.trim());
					}else{
						letras = adsUsr.obtieneLetraEncriptada(sub.trim());
					}
					//log.info("letras : "+letras);
					
					posicion=posicion+1;
					largo=largo+1;
				}
				clave=letras;
			}
		}catch(Exception e){
			e.printStackTrace();
			clave="";
			return clave;
		}
		
		
		System.out.println("Encripta clave2:"+clave);
		return clave;
				
	}
	public String getCadenaAlfanumAleatoria (int longitud){
		String cadenaAleatoria = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while ( i < longitud){
		char c = (char)r.nextInt(255);
		if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
		cadenaAleatoria += c;
		i ++;
		}
		}
		return cadenaAleatoria;
		}
	public void actualizaClave(){
		DAOFactory dao = DAOFactory.getInstance();
		//Modifica clave
		AdsusrDAO ads = dao.getAdsusrDAO();
		List letras = ads.letras();
		ObtieneClave obt = new ObtieneClave();
		Iterator iter = letras.iterator();
		while (iter.hasNext()){
			String letr = (String) iter.next();
			String cadena = obt.getCadenaAlfanumAleatoria (10).trim();
			ads.actualizaLetra(letr.trim(), cadena.trim());
			System.out.println("Letra:"+letr);
			System.out.println("string:"+cadena);
		}
	}
	
	public static void main (String[] args){
		
		String pass = "FLORENCIA";
		ObtieneClave obt = new ObtieneClave();
		obt.encriptaClave(pass);
		//obt.convierteClave(pass);
		
		obt.encriptaClave("JAIME");
		
	}
}
