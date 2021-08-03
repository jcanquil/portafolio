// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IntegracionDB2MySQL.java

package cl.caserita.integracion;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.ClmcliDAO;
import cl.caserita.dao.iface.IdDireccionDAO;
import cl.caserita.dto.CoordenadasDTO;
import cl.caserita.dto.IdDireccionDTO;
import cl.caserita.procesos.helper.ProcesaLatitudLongitudHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

public class IntegracionDB2MySQL
{

    public IntegracionDB2MySQL()
    {
    }
    
    public void procesaSoloDireccion(){
    	 {
    	        String texto = new String();
    	        DAOFactory daoDB2 = DAOFactory.getInstance();
    	        IdDireccionDAO idDirDAO = daoDB2.getIdDireccionDAO();
    	        int actualiza = 0;
    	        
    	        try
    	        {
    	            
    	                String urlString = "https://maps.googleapis.com/maps/api/geocode/json?";
    	                String direccion2 = "el boldo 4 calle 5 20, curico, chile";
    	               
    	                
    	                    try
    	                    {
    	                        URL url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
    	                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
    	                        String str = "";
    	                        StringBuffer tmp = new StringBuffer();
    	                        while((str = in.readLine()) != null) 
    	                            tmp.append(str);
    	                        in.close();
    	                        System.out.println((new StringBuilder("String:")).append(tmp.toString()).toString());
    	                        texto = tmp.toString();
    	                        ProcesaLatitudLongitudHelper lon = new ProcesaLatitudLongitudHelper();
    	                        CoordenadasDTO coordenada = lon.convierte(texto);
    	                        if(coordenada != null)
    	                        {
    	                        	System.out.println("lat:"+coordenada.getLatitud());
    	                        	System.out.println("lon:"+coordenada.getLongitud());
    	                        }
    	                    }
    	                    catch(MalformedURLException e)
    	                    {
    	                        texto = "<h2>No esta correcta la URL</h2>".toString();
    	                    }
    	                    catch(IOException e)
    	                    {
    	                        texto = "<h2>Error: No se encontro el l pagina solicitada".toString();
    	                    }
    	            }

    	        
    	        catch(Exception e)
    	        {
    	            e.printStackTrace();
    	        }
    	    }
    }
    public void procesaIntegracion()
    {
        String texto = new String();
        DAOFactory daoDB2 = DAOFactory.getInstance();
        IdDireccionDAO idDirDAO = daoDB2.getIdDireccionDAO();
        int actualiza = 0;
        
        try
        {
            List direcciones = idDirDAO.direcciones();
            cl.caserita.integracion.dao.base.DAOFactory daoMySql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
            cl.caserita.integracion.dao.iface.IdDireccionDAO idDAO = daoMySql.getIdDireccionDAO();
            for(Iterator iter = direcciones.iterator(); iter.hasNext();)
            {
                IdDireccionDTO idDire = (IdDireccionDTO)iter.next();
                cl.caserita.integracion.dto.IdDireccionDTO dto = new cl.caserita.integracion.dto.IdDireccionDTO();
                dto.setIdDireccion(idDire.getIdDireccion());
                dto.setCorrelativoDirecciones(idDire.getCorrelativoDirecciones());
                dto.setCodRegion(idDire.getCodRegion());
                dto.setDescripcionRegion(idDire.getDescripcionRegion());
                dto.setCodCiudad(idDire.getCodCiudad());
                dto.setDescripcionCiudad(idDire.getDescripcionCiudad());
                dto.setCodComuna(idDire.getCodComuna());
                dto.setDescripcionComuna(idDire.getDescripcionComuna());
                dto.setDireccion(idDire.getDireccion());
                dto.setNumeroDireccion(idDire.getNumeroDireccion());
                dto.setCodVendedor(idDire.getCodVendedor());
                dto.setNombreVendedor(idDire.getNombreVendedor());
                dto.setRutCliente(idDire.getRutCliente());
                dto.setNombreCliente(idDire.getNombreCliente());
                dto.setNombreContacto(idDire.getNombreContacto());
                dto.setRevision(0);
                cl.caserita.integracion.dto.IdDireccionDTO dto2 = idDAO.obtieneDatos(idDire.getIdDireccion(), idDire.getCorrelativoDirecciones());
                ClmcliDAO clmcliDAO = daoDB2.getClmcliDAO();
                String direccion = idDire.getDireccion();
                String numero = String.valueOf(idDire.getNumeroDireccion());
                String comuna = clmcliDAO.recuperaComuna(idDire.getCodRegion(), idDire.getCodCiudad(), idDire.getCodComuna());
                String ciudad = clmcliDAO.recuperaCiudad(idDire.getCodRegion(), idDire.getCodCiudad());
                String pais ="Chile";
                System.out.println((new StringBuilder("Direccion:")).append(direccion).toString());
                System.out.println((new StringBuilder("Numero:")).append(numero).toString());
                System.out.println((new StringBuilder("Comuna:")).append(comuna).toString());
                String urlString = "https://maps.googleapis.com/maps/api/geocode/json?";
                String direccion2 = (new StringBuilder(String.valueOf(direccion.trim()))).append(" ").append(numero).append(",").append(comuna.trim()).append(",").append(ciudad).toString()+","+pais;
               
                if(dto2 != null && dto2.getLatitud() == 0.0D && dto2.getLongitud() == 0.0D && dto2.getCodVendedor() != 0 && idDire.getCodRegion()==13)
                    try
                    {
                        URL url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                        String str = "";
                        StringBuffer tmp = new StringBuffer();
                        while((str = in.readLine()) != null) 
                            tmp.append(str);
                        in.close();
                        System.out.println((new StringBuilder("String:")).append(tmp.toString()).toString());
                        texto = tmp.toString();
                        ProcesaLatitudLongitudHelper lon = new ProcesaLatitudLongitudHelper();
                        CoordenadasDTO coordenada = lon.convierte(texto);
                        if(coordenada != null)
                        {
                            dto.setLatitud(Double.parseDouble(coordenada.getLatitud()));
                            dto.setLongitud(Double.parseDouble(coordenada.getLongitud()));
                            idDAO.actualizaLatLon(dto);
                            if(++actualiza >= 2000)
                                return;
                        }
                    }
                    catch(MalformedURLException e)
                    {
                        texto = "<h2>No esta correcta la URL</h2>".toString();
                    }
                    catch(IOException e)
                    {
                        texto = "<h2>Error: No se encontro el l pagina solicitada".toString();
                    }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void procesaIntegracionVendedores()
    {
        String texto = new String();
        DAOFactory daoDB2 = DAOFactory.getInstance();
        IdDireccionDAO idDirDAO = daoDB2.getIdDireccionDAO();
        int actualiza = 0;
        
        try
        {
            List direcciones = idDirDAO.direccionesPorRut(246572);
        	//List direcciones = idDirDAO.direcciones();
            cl.caserita.integracion.dao.base.DAOFactory daoMySql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
            cl.caserita.integracion.dao.iface.IdDireccionDAO idDAO = daoMySql.getIdDireccionDAO();
            for(Iterator iter = direcciones.iterator(); iter.hasNext();)
            {
                IdDireccionDTO idDire = (IdDireccionDTO)iter.next();
                cl.caserita.integracion.dto.IdDireccionDTO dto = new cl.caserita.integracion.dto.IdDireccionDTO();
                dto.setIdDireccion(idDire.getIdDireccion());
                dto.setCorrelativoDirecciones(idDire.getCorrelativoDirecciones());
                dto.setCodRegion(idDire.getCodRegion());
                dto.setDescripcionRegion(idDire.getDescripcionRegion());
                dto.setCodCiudad(idDire.getCodCiudad());
                dto.setDescripcionCiudad(idDire.getDescripcionCiudad());
                dto.setCodComuna(idDire.getCodComuna());
                dto.setDescripcionComuna(idDire.getDescripcionComuna());
                dto.setDireccion(idDire.getDireccion());
                dto.setNumeroDireccion(idDire.getNumeroDireccion());
                dto.setCodVendedor(idDire.getCodVendedor());
                dto.setNombreVendedor(idDire.getNombreVendedor());
                dto.setRutCliente(idDire.getRutCliente());
                dto.setNombreCliente(idDire.getNombreCliente().replaceAll("'", ""));
                dto.setNombreContacto(idDire.getNombreContacto().replaceAll("'", ""));
                dto.setRevision(0);
                cl.caserita.integracion.dto.IdDireccionDTO dto2 = idDAO.obtieneDatosPorRut(idDire.getRutCliente(), idDire.getCorrelativoDirecciones());
               if (idDire.getRutCliente()==1783705){
            	   System.out.println("Detener");
               }
            	   if (idDAO.validaExistenciaPorRut(idDire.getRutCliente(), idDire.getCorrelativoDirecciones())==0){
                   	System.out.println("Pruebas");
                   	idDAO.insertaDirecciones(dto);
                       	//idDAO.actualizaNumero(idDire);
                   	
                   	
                   }else{
                	   if (dto2!=null){
                		   if (dto.getCodVendedor()!=dto2.getCodVendedor()){
                              	System.out.println("Actualiza Vendedor");
                              	dto.setIdDireccion(dto2.getIdDireccion());
                              	idDAO.actualizaDirecciones(dto);

                          	}
                	   }
                   	
                   }
               
                
              
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void procesaPorVendedor(int codigoVendedor){
    	String texto = new String();
        DAOFactory daoDB2 = DAOFactory.getInstance();
        int actualiza = 0;
        try
        {
           
            cl.caserita.integracion.dao.base.DAOFactory daoMySql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
            cl.caserita.integracion.dao.iface.IdDireccionDAO idDAO = daoMySql.getIdDireccionDAO();
            List dirxvendedor = idDAO.direccionesVendedor(codigoVendedor);
            Iterator direcciones = dirxvendedor.iterator();
            while (direcciones.hasNext())
            {
            	cl.caserita.integracion.dto.IdDireccionDTO idDire = (cl.caserita.integracion.dto.IdDireccionDTO)direcciones.next();
                
                
                ClmcliDAO clmcliDAO = daoDB2.getClmcliDAO();
                String direccion = idDire.getDireccion();
                String numero = String.valueOf(idDire.getNumeroDireccion());
                String comuna = clmcliDAO.recuperaComuna(idDire.getCodRegion(), idDire.getCodCiudad(), idDire.getCodComuna());
                String ciudad = clmcliDAO.recuperaCiudad(idDire.getCodRegion(), idDire.getCodCiudad());
                String pais ="Chile";
                System.out.println((new StringBuilder("Direccion:")).append(direccion).toString());
                System.out.println((new StringBuilder("Numero:")).append(numero).toString());
                System.out.println((new StringBuilder("Comuna:")).append(comuna).toString());
                String urlString = "https://maps.googleapis.com/maps/api/geocode/json?";
                String direccion2 = (new StringBuilder(String.valueOf(direccion.trim()))).append(" ").append(numero).append(",").append(comuna.trim()).append(",").append(ciudad).toString()+","+pais;
                if(idDire != null && idDire.getLatitud() == 0.0D && idDire.getLongitud() == 0.0D && idDire.getNumeroDireccion() > 0 )
                    try
                    {
                        URL url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                        String str = "";
                        StringBuffer tmp = new StringBuffer();
                        while((str = in.readLine()) != null) 
                            tmp.append(str);
                        in.close();
                        System.out.println((new StringBuilder("String:")).append(tmp.toString()).toString());
                        texto = tmp.toString();
                        ProcesaLatitudLongitudHelper lon = new ProcesaLatitudLongitudHelper();
                        CoordenadasDTO coordenada = lon.convierte(texto);
                        if(coordenada != null)
                        {
                        	idDire.setLatitud(Double.parseDouble(coordenada.getLatitud()));
                        	idDire.setLongitud(Double.parseDouble(coordenada.getLongitud()));
                        	idDire.setRevision(1);
                            idDAO.actualizaLatLon(idDire);
                            if(++actualiza >= 2000)
                                return;
                        }
                    }
                    catch(MalformedURLException e)
                    {
                        texto = "<h2>No esta correcta la URL</h2>".toString();
                    }
                    catch(IOException e)
                    {
                        texto = "<h2>Error: No se encontro el l pagina solicitada".toString();
                    }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void procesaPorVendedorNuevos(int codigoVendedor){
    	String texto = new String();
        DAOFactory daoDB2 = DAOFactory.getInstance();
        int actualiza = 0;
        try
        {
           
            cl.caserita.integracion.dao.base.DAOFactory daoMySql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
            cl.caserita.integracion.dao.iface.IdDireccionDAO idDAO = daoMySql.getIdDireccionDAO();
            List dirxvendedor = idDAO.direccionesVendedor(codigoVendedor);
            
            Iterator direcciones = dirxvendedor.iterator();
            while (direcciones.hasNext())
            {
            	cl.caserita.integracion.dto.IdDireccionDTO idDire = (cl.caserita.integracion.dto.IdDireccionDTO)direcciones.next();
                if (idDire.getIdDireccion()>=217001){
                	ClmcliDAO clmcliDAO = daoDB2.getClmcliDAO();
                    String direccion = idDire.getDireccion();
                    String numero = String.valueOf(idDire.getNumeroDireccion());
                    String comuna = clmcliDAO.recuperaComuna(idDire.getCodRegion(), idDire.getCodCiudad(), idDire.getCodComuna());
                    String ciudad = clmcliDAO.recuperaCiudad(idDire.getCodRegion(), idDire.getCodCiudad());
                    String pais ="Chile";
                    System.out.println((new StringBuilder("Direccion:")).append(direccion).toString());
                    System.out.println((new StringBuilder("Numero:")).append(numero).toString());
                    System.out.println((new StringBuilder("Comuna:")).append(comuna).toString());
                    String urlString = "https://maps.googleapis.com/maps/api/geocode/json?";
                    String direccion2 = (new StringBuilder(String.valueOf(direccion.trim()))).append(" ").append(numero).append(",").append(comuna.trim()).append(",").append(ciudad).toString()+","+pais;
                    if(idDire != null && idDire.getLatitud() == 0.0D && idDire.getLongitud() == 0.0D && idDire.getNumeroDireccion() > 0 )
                        try
                        {
                            URL url = new URL((new StringBuilder(String.valueOf(urlString))).append("address=").append(URLEncoder.encode(direccion2, "UTF-8")).append("&key=AIzaSyD85OE2k-qGwaNIfAp_sJ4swbKtK_qO-u4&sensor=false").toString());
                            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                            String str = "";
                            StringBuffer tmp = new StringBuffer();
                            while((str = in.readLine()) != null) 
                                tmp.append(str);
                            in.close();
                            System.out.println((new StringBuilder("String:")).append(tmp.toString()).toString());
                            texto = tmp.toString();
                            ProcesaLatitudLongitudHelper lon = new ProcesaLatitudLongitudHelper();
                            CoordenadasDTO coordenada = lon.convierte(texto);
                            if(coordenada != null)
                            {
                            	idDire.setLatitud(Double.parseDouble(coordenada.getLatitud()));
                            	idDire.setLongitud(Double.parseDouble(coordenada.getLongitud()));
                            	idDire.setRevision(1);
                                idDAO.actualizaLatLon(idDire);
                                if(++actualiza >= 2000)
                                    return;
                            }
                        }
                        catch(MalformedURLException e)
                        {
                            texto = "<h2>No esta correcta la URL</h2>".toString();
                        }
                        catch(IOException e)
                        {
                            texto = "<h2>Error: No se encontro el l pagina solicitada".toString();
                        }
                }
                
                
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void procesaNumero(int vendedor){
    	String texto = new String();
        DAOFactory daoDB2 = DAOFactory.getInstance();
        IdDireccionDAO idDireccionDAO = daoDB2.getIdDireccionDAO();
        int actualiza = 0;
        try
        {
           
            cl.caserita.integracion.dao.base.DAOFactory daoMySql = cl.caserita.integracion.dao.base.DAOFactory.getInstance();
            cl.caserita.integracion.dao.iface.IdDireccionDAO idDAO = daoMySql.getIdDireccionDAO();
            List dirxvendedor = idDAO.direccionesVendedor(vendedor);
            Iterator direcciones = dirxvendedor.iterator();
            while (direcciones.hasNext())
            {
            	cl.caserita.integracion.dto.IdDireccionDTO idDire = (cl.caserita.integracion.dto.IdDireccionDTO)direcciones.next();
            	
                IdDireccionDTO dto2 = idDireccionDAO.buscaDireccion(idDire.getIdDireccion(), idDire.getCorrelativoDirecciones());
                
                if (idDire.getRutCliente()==dto2.getRutCliente()){
                	if (dto2.getNumeroDireccion()>0 && idDire.getNumeroDireccion()==0 ){
                		idDire.setNumeroDireccion(dto2.getNumeroDireccion());
                		//idDAO.insertaDirecciones(idDire);
                    	idDAO.actualizaNumero(idDire);
                	}
                	
                }
                   
            }

        }
        catch(Exception e)
        {
           
        }
    }
    
    public static void main(String args[])
    {
        IntegracionDB2MySQL in = new IntegracionDB2MySQL();
       DAOFactory daoDB2 = DAOFactory.getInstance();
        IdDireccionDAO idDireccionDAO = daoDB2.getIdDireccionDAO();
        List vendedores = idDireccionDAO.vendedoresNuevos();
        Iterator iter = vendedores.iterator();
        while (iter.hasNext()){
        	IdDireccionDTO id = (IdDireccionDTO) iter.next();
        	if (id.getCodVendedor()>0){
        		in.procesaPorVendedorNuevos(id.getCodVendedor());
        	}
        }
       //in.procesaNumero(749);
       // in.procesaPorVendedor(749);
        //in.procesaSoloDireccion();
       //in.procesaIntegracionVendedores();
        System.out.println("Pruebas");
    }
}
