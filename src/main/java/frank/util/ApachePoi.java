package frank.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ApachePoi {

	private static XSSFWorkbook libro;
	private static XSSFWorkbook libro2;

	private static void EscribirExcel() {
		String ruta = "C:\\Team\\proyecto\\excel\\reglas.xlsx";

		String hoja = "Job Profile";

		libro = new XSSFWorkbook();
		XSSFSheet hoja1 = libro.createSheet(hoja);

		// Cabecera de la hoja excel
		String[] header = { "CAMPO", "FORMATO", "LONGITUD","PATH" };
		// Contenido de la hoja excel
		String[][] document = new String[][] { 
			// Nota : se necesita agregar por objetos 
			{ "Job Level", "char", "0","/jp:Job_Profile_Sync/jp:Job_Profile/jp:Job_Level" },
			{ "Management Level", "char", "0","/jp:Job_Profile_Sync/jp:Job_Profile/jp:Management_Level" } };

		// Poner en negrita la cabecera (Opcional)
		CellStyle styleExcel = libro.createCellStyle();
		Font font = libro.createFont();
		font.setBold(true);
		styleExcel.setFont(font);

		// Generar los datos para el documento

		for (int i = 0; i <= document.length; i++) {
			XSSFRow row = hoja1.createRow(i); // Se crea la fila
			for (int j = 0; j < header.length; j++) {
				if (i == 0) {
					XSSFCell cell = row.createCell(j); // Se crean la celdas para la cabecera
					cell.setCellValue(header[j]);
				} else {
					XSSFCell cell = row.createCell(j); // Se crean la celdas para el contenido
					cell.setCellValue(document[i - 1][j]); // Se añade el contenido
				}
			}
		}

		// Crea un archivo

		try (OutputStream fileOut = new FileOutputStream(ruta)) {

			System.out.println("Se creo el excel");
			libro.write(fileOut);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	
	private static void LeerExcel() {
		final String ruta = "C:\\\\Team\\\\proyecto\\\\excel\\\\reglas.xlsx";
		//String hoja = "Job Profile";
		
		try (FileInputStream file = new FileInputStream(new File(ruta))) {
			libro2 = new XSSFWorkbook(file);
			//Obtener la hoja que se va leer 
			XSSFSheet sheet = libro2.getSheetAt(0);
			//Obtener todas las filas de la hoja de Excel
			Iterator<Row> rowIterador = sheet.iterator();
			Row row;
			// Se recorre cada fila hasta el final
			while (rowIterador.hasNext()) {
				row = rowIterador.next();
				//Se obtienen las celdas por fila
				Iterator<Cell> cellIterador = row.cellIterator();
				Cell cell;
				//Se recorre cada celda
				while(cellIterador.hasNext()) {
					//Se obtienen la celda ne especifico y se imprime
					cell = cellIterador.next();
					System.out.print(cell.getStringCellValue().concat("\t"));
				}
				System.out.println();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void main(String[] args) {
		//EscribirExcel();
		LeerExcel();
	}

}
