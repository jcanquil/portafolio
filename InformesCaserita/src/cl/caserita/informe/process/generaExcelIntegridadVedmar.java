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

import cl.caserita.dto.VedmarDTO;
import cl.caserita.informe.dto.UnicoDTO;

public class generaExcelIntegridadVedmar {

	public String generaExcelPrecio(List lista){
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
			
			VedmarDTO ges = new VedmarDTO();
			ges = (VedmarDTO) iter.next();
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
						int tit =3;
						int tit1=0;
						for (int i=0;i<=3;i++){
							celda = fila2.createCell(i);
							if (tit1==0){
								nomcel="TIPO MOV";
							}
							else if (tit1==1){
								nomcel="FECHA MOV";
							}else if (tit1==2){
								nomcel="NUMERO DOCUMENTO";
							}else if (tit1==3){
								nomcel="BODEGA";
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
			
			FileOutputStream archivo = new FileOutputStream("/home/ServiciosCaserita/informeIntegridad/INFORME_INTEGRIDAD_"+ fechaStr +".xls");
			
			nombreArchivo="INFORME_INTEGRIDAD_" + fechaStr+".xls";
			String directorio = "/home/ServiciosCaserita/informeIntegridad/";
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
	public List llenaListaUnica(VedmarDTO dato){
		List lista = new ArrayList();
		int cant=0;
		int cant2=3;
		for (int i=0;i<=cant2;i++){
			UnicoDTO unico = new UnicoDTO();
			if (cant==0){
				unico.setDescripcion(String.valueOf(dato.getCodTipoMvto()));
			}
			else if (cant==1){
				unico.setDescripcion(String.valueOf(dato.getFechaMvto()));
			}else if (cant==2){
				unico.setDescripcion(String.valueOf(dato.getNumDocumento()));
			}else if (cant==3){
				unico.setDescripcion(String.valueOf(dato.getCodigoBodega()));
			}
			
			lista.add(unico);
			cant++;
		}
		
		
		return lista;
	}
}
