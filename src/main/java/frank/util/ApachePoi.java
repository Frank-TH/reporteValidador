package frank.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ApachePoi {

	final static String ruta = "C:\\Team\\proyecto\\excel\\PLUbicacion.xlsx";
	// -----------------------------
	static FileInputStream file;
	static XSSFWorkbook wb;
	static XSSFSheet sheet;

	private static boolean PL_ubigeo(int codigo) {

		List<Integer> data = new ArrayList<>();
		try {
			file = new FileInputStream(new File(ruta));
			wb = new XSSFWorkbook(file);
			sheet = wb.getSheetAt(0);
			int numFilas = sheet.getLastRowNum();

			for (int a = 1; a <= numFilas; a++) {
				Row fila = sheet.getRow(a);
				Cell celda = fila.getCell(2);
				switch (celda.getCellType().toString()) {
				case "NUMERIC":
					data.add((int) celda.getNumericCellValue());
					break;
				}
			}
			file.close();
			for (int valor : data) {
				if (codigo == valor) {
					return true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	private static boolean PL_country(String codigo) {

		List<String> data = new ArrayList<>();
		try {
			file = new FileInputStream(new File(ruta));
			wb = new XSSFWorkbook(file);
			sheet = wb.getSheetAt(1);
			int numFilas = sheet.getLastRowNum();
			for (int a = 1; a <= numFilas; a++) {
				Row fila = sheet.getRow(a);
				Cell celda = fila.getCell(2);

				switch (celda.getCellType().toString()) {
				case "STRING":
					data.add(celda.getStringCellValue());
					break;
				default:
					break;
				}

			}
			file.close();

			for (String valor : data) {

				if (codigo.equalsIgnoreCase(valor)) {
					return true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;

	}

	private static boolean PL_provincia(int codigo) {
		List<Integer> data = new ArrayList<>();
		try {
			file = new FileInputStream(new File(ruta));
			wb = new XSSFWorkbook(file);
			sheet = wb.getSheetAt(2);
			int numFilas = sheet.getLastRowNum();
			for (int a = 1; a <= numFilas; a++) {
				Row fila = sheet.getRow(a);
				Cell celda = fila.getCell(2);

				switch (celda.getCellType().toString()) {
				case "NUMERIC":
					data.add((int) celda.getNumericCellValue());
					break;
				default:
					break;
				}

			}
			file.close();

			for (int valor : data) {

				if (codigo == valor) {
					return true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	private static boolean PL_tipovia(String codigo) {
		List<String> data = new ArrayList<>();
		try {
			file = new FileInputStream(new File(ruta));
			wb = new XSSFWorkbook(file);
			sheet = wb.getSheetAt(3);
			int numFilas = sheet.getLastRowNum();
			for (int a = 1; a <= numFilas; a++) {
				Row fila = sheet.getRow(a);
				Cell celda = fila.getCell(2);

				switch (celda.getCellType().toString()) {
				case "STRING":
					data.add(celda.getStringCellValue());
					break;
				default:
					break;
				}

			}
			file.close();

			for (String valor : data) {

				if (codigo.equalsIgnoreCase(valor)) {
					return true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	private static boolean PL_tipozona(String codigo) {
		List<String> data = new ArrayList<>();
		try {
			file = new FileInputStream(new File(ruta));
			wb = new XSSFWorkbook(file);
			sheet = wb.getSheetAt(4);
			int numFilas = sheet.getLastRowNum();
			for (int a = 1; a <= numFilas; a++) {
				Row fila = sheet.getRow(a);
				Cell celda = fila.getCell(2);

				switch (celda.getCellType().toString()) {
				case "STRING":
					data.add(celda.getStringCellValue());
					break;
				default:
					break;
				}

			}
			file.close();

			for (String valor : data) {

				if (codigo.equalsIgnoreCase(valor)) {
					return true;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public static void main(String[] args) {

		System.out.println(PL_ubigeo(10106));

		System.out.println(PL_country("ant"));

		System.out.println(PL_provincia(102));

		System.out.println(PL_tipovia("al"));

		System.out.println(PL_tipozona("coo"));
	}

}
