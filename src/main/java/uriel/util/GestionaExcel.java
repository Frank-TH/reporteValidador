package uriel.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.CeldaExcel;
import model.Regla;

public class GestionaExcel {

	public static List<Regla> obtenerDatos(CeldaExcel ce) {
		List<Regla> reglas = new ArrayList<Regla>();
		String rHris=null, rNombre=null, rFormato=null, rXPath=null, rLongitud=null, rRequerido=null, rRegexp=null;
		
		for (int i = ce.getFilaTitulo() + 1; i < ce.getFilaFin(); i++) {
			
			Row row = ce.getSheet().getRow(i);
			
			if(row.getCell(ce.getColHris()) != null) {
				rHris = row.getCell(ce.getColHris()).getStringCellValue();
			}
			if(row.getCell(ce.getColNombre()) != null) {
				rNombre = row.getCell(ce.getColNombre()).getStringCellValue();
			}
			if(row.getCell(ce.getColFormato()) != null) {
				rFormato = row.getCell(ce.getColFormato()).getStringCellValue();
			}
			if(row.getCell(ce.getColXPath()) != null) {
				rXPath = row.getCell(ce.getColXPath()).getStringCellValue();
			}
			if(row.getCell(ce.getColLongitud()) != null) {
				rLongitud = row.getCell(ce.getColLongitud()).getNumericCellValue()+"";
			}
			if(row.getCell(ce.getColRequerido()) != null) {
				rRequerido = row.getCell(ce.getColRequerido()).getStringCellValue();
			}
			if(row.getCell(ce.getColRegexp()) != null) {
				rRegexp = row.getCell(ce.getColRegexp()).getStringCellValue();
			}

			Regla regla = new Regla(ce.getLibro(),rHris,rNombre,rFormato,rXPath,rLongitud,rRequerido,rRegexp);
			reglas.add(regla);
			
		}
		
		return reglas;
	}

	public static CeldaExcel obtenerCeldas(String rutaExcel, String libro) {
		CeldaExcel ce = new CeldaExcel();
		ce.setRuta(rutaExcel);
		ce.setLibro(libro);
		try {
			FileInputStream fis = new FileInputStream(ce.getRuta());
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(ce.getLibro());
			ce.setSheet(sh);

			for (int i = 0; i <= sh.getLastRowNum(); i++) {
				if (sh.getRow(i) != null) {
					if (sh.getRow(i).getCell(0) != null) {
						String datoFila = sh.getRow(i).getCell(0).getStringCellValue();
						if (datoFila.equals("Section")) {
							ce.setFilaTitulo(i);
						}
						if(datoFila.equals("")){
							ce.setFilaFin(i);
							break;
						}
					}else {
						ce.setFilaFin(i);
						break;
					}
				}
			}

			Row rw = sh.getRow(ce.getFilaTitulo());
			//System.out.println("---Fila Cabecera : " + ce.getFilaTitulo());
			//System.out.println("---Fila Fin : " + ce.getFilaFin());

			for (int i = 0; i <= rw.getPhysicalNumberOfCells(); i++) {
				if (rw.getCell(i) != null) {//Regla de asignacion de valor 
					String datoColumna = rw.getCell(i).getStringCellValue();
					if ("Section".equals(datoColumna)) {
						ce.setColHris(i);
					} else if ("FieldName".equals(datoColumna)) {
						ce.setColNombre(i);
					} else if ("Tipo de dato".equals(datoColumna)) {
						ce.setColFormato(i);
					} else if ("xpath".equals(datoColumna)) {
						ce.setColXPath(i);
					} else if ("Longitud".equals(datoColumna)) {
						ce.setColLongitud(i);
					} else if ("Expresion Regular".equals(datoColumna)) {
						ce.setColRegexp(i);
					} else if ("Obligatorio".equals(datoColumna)) {
						ce.setColRequerido(i);
					}
				}
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ce;
	}
}
