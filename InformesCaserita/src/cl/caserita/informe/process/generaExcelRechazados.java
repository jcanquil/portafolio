package cl.caserita.informe.process;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.caserita.dao.base.DAOFactory;
import cl.caserita.dao.iface.CamtraDAO;
import cl.caserita.dto.CamtraDTO;
import cl.caserita.informe.dto.GastosDTO;
import cl.caserita.informe.dto.UnicoDTO;

public class generaExcelRechazados  {

	public String generaExcelGes(List lista, int codDoc){
		cl.caserita.comunes.fecha.Fecha fecha = new cl.caserita.comunes.fecha.Fecha();
		String fechaStr = fecha.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fecha.getYYYYMMDDHHMMSS().substring(8, 12);
		String fechaHoy = fecha.getFechaconFormato();
		fechaHoy.replaceAll(":", " ");
		fechaHoy.replaceAll("/", " ");
				
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet hoja = book.createSheet();
		
	
		String nombreArchivo ="";	
		
		//Recorre lista para formar excel
		Iterator iter = lista.iterator();
		//Crea Filas
		int numFilas =0;
		HSSFCell celda;
		int repita=1;
		while (iter.hasNext()){
			
			CamtraDTO ges = new CamtraDTO();
			ges = (CamtraDTO) iter.next();
			List unica = llenaListaUnica(ges);
			
			
			HSSFRow fila2 = hoja.createRow(numFilas);
			//Titulos
			
			
				//Crea celdas
				Iterator iter2 = unica.iterator();
				int celn=0;
				while (iter2.hasNext()){
					UnicoDTO dato = (UnicoDTO) iter2.next();
					if (repita==1){						
						String nomcel="";
						int tit =11;
						int tit1=1;
						for (int i=0;i<=10;i++){
							celda = fila2.createCell(i);
							if (tit1==1){
								nomcel="EMPRESA";
							}else if (tit1==2){
								nomcel="NUMERO-INTERNO";
							}else if (tit1==3){
								nomcel="FECHA";
							}else if (tit1==4){
								nomcel="BODEGA";
							}else if (tit1==5){
								nomcel="RUT";
							}else if (tit1==6){
								nomcel="DVR";
							}else if (tit1==7){
								nomcel="NOMBRE";
							}else if (tit1==8){
								nomcel="CODIGO DOC";
							}else if (tit1==9){
								nomcel="NUM DOCUMENTO";
							}else if (tit1==10){
								nomcel="USUARIO";
							}else if (tit1==11){
								nomcel="VALOR";
							}
							HSSFRichTextString texto = new HSSFRichTextString(nomcel);
							celda.setCellValue(nomcel);
							tit1++;
						}
						numFilas++;
						fila2 = hoja.createRow(numFilas);
					}
					repita++;
					celda = fila2.createCell(celn);
					HSSFRichTextString textoinf = new HSSFRichTextString(dato.getDescripcion());
					celda.setCellValue(dato.getDescripcion());
				
					celn++;
				}			
				
				
			numFilas++;
			
		}
		try{
			String documento="";
			if (codDoc==33){
				documento="FACTURA_ELECTRONICA";
			}else if (codDoc==35){
				documento="NOTA_CREDITO_ELECTRONICA";
			}else if (codDoc==36){
				documento="FACTURA_EXENTA_ELECTRONICA";
			}else if (codDoc==42){
				documento="GUIAS_ELECTRONICA";
			}
			System.out.println("Genera Excel :");				
			FileOutputStream archivo = new FileOutputStream("/home/ServiciosCaserita/informes/INFORME_DOCUMENTOS_RECHAZADOS_" +documento+ "_" + fechaStr +".xls");
			
			nombreArchivo="INFORME_DOCUMENTOS_RECHAZADOS_" +documento+ "_" + fechaStr+".xls";
			String directorio = "/home/ServiciosCaserita/informes/";
			nombreArchivo=directorio+nombreArchivo;
			System.out.println("Genera Archivo Mail :"+nombreArchivo);
			File objFile = new File(nombreArchivo);
			FileOutputStream archivoSalida = new FileOutputStream(objFile);
			
			book.write(archivoSalida);
			
			archivo.close();
			
			System.out.println("Exito");
		}
		catch (Exception e){
			e.printStackTrace();
			System.out.println("Problemas");
		}
		
		
		return nombreArchivo;
	}

	public static void main (String[]args){
		List listanueva = null;
		DAOFactory factory = DAOFactory.getInstance();
		CamtraDAO camtra = (CamtraDAO) factory.getCamtraDAO();
		listanueva = camtra.obtenerDatosEstadoSII(2, 20140324, 33);
		generaExcelRechazados excel = new generaExcelRechazados();
		
		excel.generaExcelGes(listanueva, 33);
		
		
	}
	public List llenaListaUnica(CamtraDTO dato){
		List lista = new ArrayList();
		int cant=1;
		int cant2=11;
		for (int i=0;i<=cant2;i++){
			UnicoDTO unico = new UnicoDTO();
			if (cant==1){
				unico.setDescripcion(String.valueOf(dato.getCodigoEmpresa()));
			}else if (cant==2){
				unico.setDescripcion(String.valueOf(dato.getNumeroDocumento()));
			}else if (cant==3){
				unico.setDescripcion(String.valueOf(dato.getFechaDocumento()));
			}else if (cant==4){
				unico.setDescripcion(String.valueOf(dato.getCodigoBodega()));
			}else if (cant==5){
				unico.setDescripcion(dato.getRutCliente());
			}else if (cant==6){
				unico.setDescripcion(dato.getDvCliente());
			}else if (cant==7){
				unico.setDescripcion(dato.getNombreCliente());
			}else if (cant==8){
				unico.setDescripcion(String.valueOf(dato.getCodigoDocumento()));
			}else if (cant==9){
				unico.setDescripcion(String.valueOf(dato.getNumeroBolfactura()));
			}else if (cant==10){
				unico.setDescripcion(dato.getCodigoUsuario());
			}else if (cant==11){
				unico.setDescripcion(String.valueOf(dato.getValorDocumento()));
			}
			
			lista.add(unico);
			cant++;
		}
		
		
		return lista;
	}

	
}
