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

import cl.caserita.informe.dto.ExmgesDTO;
import cl.caserita.informe.dto.GastosDTO;
import cl.caserita.informe.dto.UnicoDTO;

public class generaExcelInformeGastos {

	public String generaExcelGes(List lista){
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
			
			GastosDTO ges = new GastosDTO();
			ges = (GastosDTO) iter.next();
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
						int tit =12;
						int tit1=0;
						for (int i=0;i<=11;i++){
							celda = fila2.createCell(i);
							if (tit1==0){
								nomcel="ORIGEN";
							}
							else if (tit1==1){
								nomcel="GRUPO CUENTA";
							}else if (tit1==2){
								nomcel="CUENTA";
							}else if (tit1==3){
								nomcel="CENTRO COSTO";
							}else if (tit1==4){
								nomcel="GASTOS";
							}else if (tit1==5){
								nomcel="FECHA";
							}else if (tit1==6){
								nomcel="GLOSA";
							}else if (tit1==7){
								nomcel="LIBRO";
							}else if (tit1==8){
								nomcel="FOLIO";
							}else if (tit1==9){
								nomcel="NUM DOCUMENTO";
							}else if (tit1==10){
								nomcel="FECHA EMISION";
							}else if (tit1==11){
								nomcel="RUT";
							}else if (tit1==12){
								nomcel="NOMBRE";
							}else if (tit1==13){
								nomcel="MONTO";
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
							
			/*FileOutputStream archivo = new FileOutputStream("INFORME GASTOS MENSUAL" + " " + fechaStr +".xls");
			nombreArchivo="INFORME GASTOS MENSUAL" + " " + fechaStr+".xls";*/
			
			FileOutputStream archivo = new FileOutputStream("/home/ServiciosCaserita/informesGasto/INFORME_GASTOS_"+ fechaStr +".xls");
			
			nombreArchivo="INFORME_GASTOS_" + fechaStr+".xls";
			String directorio = "/home/ServiciosCaserita/informesGasto/";
			nombreArchivo=directorio+nombreArchivo;
			System.out.println("Genera Archivo Mail :"+nombreArchivo);
			File objFile = new File(nombreArchivo);
			FileOutputStream archivoSalida = new FileOutputStream(objFile);
			
			
			book.write(archivo);
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
		List listanueva = new ArrayList();
		
		generaExcel excel = new generaExcel();
		excel.generaExcelGes(listanueva);
		
		
	}
	public List llenaListaUnica(GastosDTO dato){
		List lista = new ArrayList();
		int cant=0;
		int cant2=12;
		for (int i=0;i<=cant2;i++){
			UnicoDTO unico = new UnicoDTO();
			if (cant==0){
				unico.setDescripcion(dato.getOrigen());
			}
			else if (cant==1){
				unico.setDescripcion(dato.getCuenta());
			}else if (cant==2){
				unico.setDescripcion(dato.getSubCuenta());
			}else if (cant==3){
				unico.setDescripcion(dato.getCentroCosto());
			}else if (cant==4){
				unico.setDescripcion(dato.getGastosString());
			}else if (cant==5){
				unico.setDescripcion(dato.getFecha());
			}else if (cant==6){
				unico.setDescripcion(dato.getGlosa());
			}else if (cant==7){
				unico.setDescripcion(dato.getLibro());
			}else if (cant==8){
				unico.setDescripcion(String.valueOf(dato.getFolio()));
			}else if (cant==9){
				unico.setDescripcion(dato.getNumDocumento());
			}else if (cant==10){
				unico.setDescripcion(dato.getFechaEmision());
			}else if (cant==11){
				unico.setDescripcion(dato.getRut());
			}else if (cant==12){
				
				unico.setDescripcion(dato.getRazonSocial());
			}
			
			lista.add(unico);
			cant++;
		}
		
		
		return lista;
	}
	
}
