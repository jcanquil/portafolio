package cl.caserita.proceso;

import java.util.Random;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.UsrcanastaDAO;
import cl.caserita.dto.UsuarioCanastaDTO;

public class generaUsuarios {
	
	public static void main (String[]args){
		
		 Random rnd = new Random();
		 DAOFactory dao = DAOFactory.getInstance();
		 UsrcanastaDAO usuarioCan = dao.getUsrcanastaDAO();
		 
         System.out.println("Número aleatorio real entre [0,1[ : "+rnd.nextDouble());
         int cantidad_números_rango=109000;
         int término_inicial_rango=110000;
         int contador=100000;
         int numeroNombre=1;
         while (contador<=110000){
        	 int aleatorio = (int) (rnd.nextDouble() * cantidad_números_rango + término_inicial_rango);
             generaUsuarios gen = new generaUsuarios();
             String digito = gen.digitoVerificador(String.valueOf(aleatorio));
        	 contador=contador+1;
        	 //Crea Clientes
        	 UsuarioCanastaDTO usr = new UsuarioCanastaDTO();
        	 usr.setRutCliente(15448543);
        	 usr.setDvCliente("0");
        	 usr.setNombreCliente("BANCO SANTANDER");
        	 usr.setRutPersonal(aleatorio);
        	 usr.setDvPersonal(digito);
        	 usr.setNombrePersonal(gen.obtieneNombre(numeroNombre));
        	 usr.setPasswordPersonal(String.valueOf(aleatorio).substring(0, 4));
        	 usr.setTipoUsuario("U");
        	 usr.setTipoDespacho(0);
        	 usr.setCorrelativoDirecciones(0);
        	 usr.setContactoRetiro("");
        	 usr.setObservaciones("");
        	 usuarioCan.generaUsuario(usr);
        	 if (numeroNombre==11){
        		 numeroNombre=0;
        	 }
        	 numeroNombre=numeroNombre+1;
         }
         
		
		
	}
	public String digitoVerificador(String numero){
		String digito="";
		int contador=numero.length();
		int mult=2;
		int indice=8;
		int indice2=contador-1;
		int suma=0;
		while (contador>=1){
			
			int num = Integer.parseInt(numero.substring(indice2,contador));
			suma = suma+(num * mult);
			if (mult==7){
				mult=1;
			}
			mult=mult+1;
			contador=contador-1;
			indice2=indice2-1;
		}
		int residuo=0;
		residuo=suma%11;
		residuo=11-residuo;
		digito = String.valueOf(residuo);
		if (digito.equals("11")){
			digito="0";
		}else if (digito.equals("10")){
			digito="K";
		}
		
		
		return digito;
	}
	public String obtieneNombre(int numero){
		String nombre="";
		if (numero==1){
			nombre ="JOSE ALBERTO ARANCIBIA";
		}else if (numero==2){
			nombre ="MARIANO JOAQUIN VAZQUEZ";
		}else if (numero==3){
			nombre ="MARIA DEL PILAR COFRE";
		}else if (numero==4){
			nombre ="FERNANDA MARCELA YANEZ";
		}else if (numero==5){
			nombre ="FRANCISCO JAVIER PALMA";
		}else if (numero==6){
			nombre ="MARCOS ANTONIO SILVA";
		}else if (numero==7){
			nombre ="MATIAS GUILLERMO FERNANDEZ";
		}else if (numero==8){
			nombre ="TOMAS ALEXIS GONZALEZ";
		}else if (numero==9){
			nombre ="PATRICIO HERNAN ORTIZ";
		}else if (numero==10){
			nombre ="FABIOLA VALESCA TOLEDO";
		}else if (numero==11){
			nombre ="JUAN JOSE BARRA";
		}
		
		
		return nombre;
	}

}
