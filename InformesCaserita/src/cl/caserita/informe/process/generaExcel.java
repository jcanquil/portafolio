package cl.caserita.informe.process;

import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import cl.caserita.informe.dto.ExmgesDTO;
import cl.caserita.informe.dto.UnicoDTO;
import cl.caserita.comunes.fecha.Fecha;

import java.io.FileOutputStream;




import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class generaExcel {

	public String generaExcelGes(List lista){
		cl.caserita.comunes.fecha.Fecha fecha = new cl.caserita.comunes.fecha.Fecha();
		String fechaStr = fecha.getYYYYMMDDHHMMSS().substring(0, 8) + "_" + fecha.getYYYYMMDDHHMMSS().substring(8, 12);
		String fechaHoy = fecha.getFechaconFormato();
		fechaHoy.replaceAll(":", " ");
		fechaHoy.replaceAll("/", " ");
				
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet hoja = book.createSheet();
		HSSFRow fila = hoja.createRow(0);
		
		String nombreArchivo ="";
		
		//Titulos
		String nomcel="";
		int tit =11;
		int tit1=1;
		for (int i=0;i<=11;i++){
			HSSFCell cell = fila.createCell(i);
			if (tit1==1){
				nomcel="BOD";
			}else if (tit1==2){
				nomcel="COD";
			}else if (tit1==3){
				nomcel="DESC ARTICULO";
			}else if (tit1==4){
				nomcel="STOCK";
			}else if (tit1==5){
				nomcel="COSTO";
			}else if (tit1==6){
				nomcel="TOTAL";
			}else if (tit1==7){
				nomcel="DESCRIPCION";
			}else if (tit1==8){
				nomcel="DESCRIPCION FAMILIA";
			}else if (tit1==9){
				nomcel="DESCRIPCION";
			}else if (tit1==10){
				nomcel="RUT";
			}else if (tit1==11){
				nomcel="RAZON SOCIAL";
			}
			HSSFRichTextString texto = new HSSFRichTextString(nomcel);
			cell.setCellValue(nomcel);
			tit1++;
		}
		
		
		//Recorre lista para formar excel
		Iterator iter = lista.iterator();
		//Crea Filas
		int numFilas =1;
		while (iter.hasNext()){
			
			ExmgesDTO ges = new ExmgesDTO();
			ges = (ExmgesDTO) iter.next();
			List unica = llenaListaUnica(ges);
			
			
			HSSFRow fila2 = hoja.createRow(numFilas);
				//Crea celdas
				Iterator iter2 = unica.iterator();
				int celn=1;
				while (iter2.hasNext()){
					UnicoDTO dato = (UnicoDTO) iter2.next();
					HSSFCell celda = fila2.createCell(celn);
					HSSFRichTextString textoinf = new HSSFRichTextString(dato.getDescripcion());
					celda.setCellValue(dato.getDescripcion());
					celn++;
				}			
				
				
			numFilas++;
			
		}
		try{
							
			FileOutputStream archivo = new FileOutputStream("AVANCE VENTAS MENSUAL" + " " + fechaStr +".xls");
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
	public List llenaListaUnica(ExmgesDTO dato){
		List lista = new ArrayList();
		int cant=1;
		int cant2=11;
		for (int i=0;i<=cant2;i++){
			UnicoDTO unico = new UnicoDTO();
			if (cant==1){
				unico.setDescripcion(dato.getCodBodega());
			}else if (cant==2){
				unico.setDescripcion(dato.getCodArticulo());
			}else if (cant==3){
				unico.setDescripcion(dato.getDescArticulo());
			}else if (cant==4){
				unico.setDescripcion(String.valueOf(dato.getStockComputacional()));
			}else if (cant==5){
				unico.setDescripcion(String.valueOf(dato.getCosto()));
			}else if (cant==6){
				unico.setDescripcion(String.valueOf(dato.getTotal()));
			}else if (cant==7){
				unico.setDescripcion(dato.getDescripcion1());
			}else if (cant==8){
				unico.setDescripcion(dato.getDescFamilia());
			}else if (cant==9){
				unico.setDescripcion(dato.getDescripcion2());
			}else if (cant==10){
				unico.setDescripcion(String.valueOf(dato.getRut()));
			}else if (cant==11){
				unico.setDescripcion(dato.getRazonSocial());
			}
			
			lista.add(unico);
			cant++;
		}
		
		
		return lista;
	}
}
