package uriel.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.CeldaExcel;
import model.Regla;

public class GestionaExcel {

	public static List<Regla> obtenerDatos(CeldaExcel ce) {
		List<Regla> reglas = new ArrayList<Regla>();
		String rHris = null, rNombre = null, rFormato = null, rXPath = null, rLongitud = null, rRequerido = null,
				rRegexp = null;

		for (int i = ce.getFilaTitulo() + 1; i < ce.getFilaFin(); i++) {

			Row row = ce.getSheet().getRow(i);

			if (row.getCell(ce.getColHris()) != null) {
				row.getCell(ce.getColHris()).setCellType(CellType.STRING);
				rHris = row.getCell(ce.getColHris()).getStringCellValue();
			}
			if (row.getCell(ce.getColNombre()) != null) {
				row.getCell(ce.getColNombre()).setCellType(CellType.STRING);
				rNombre = row.getCell(ce.getColNombre()).getStringCellValue();
			}
			if (row.getCell(ce.getColFormato()) != null) {
				row.getCell(ce.getColFormato()).setCellType(CellType.STRING);
				rFormato = row.getCell(ce.getColFormato()).getStringCellValue();
			}
			if (row.getCell(ce.getColXPath()) != null) {
				row.getCell(ce.getColXPath()).setCellType(CellType.STRING);
				rXPath = row.getCell(ce.getColXPath()).getStringCellValue();
			}
			if (row.getCell(ce.getColLongitud()) != null) {
				row.getCell(ce.getColLongitud()).setCellType(CellType.STRING);
				rLongitud = row.getCell(ce.getColLongitud()).getStringCellValue();
			}
			if (row.getCell(ce.getColRequerido()) != null) {
				row.getCell(ce.getColRequerido()).setCellType(CellType.STRING);
				rRequerido = row.getCell(ce.getColRequerido()).getStringCellValue();
			}
			if (row.getCell(ce.getColRegexp()) != null) {
				row.getCell(ce.getColRegexp()).setCellType(CellType.STRING);
				rRegexp = row.getCell(ce.getColRegexp()).getStringCellValue();
			}

			Regla regla = new Regla(ce.getLibro(), rHris, rNombre, rFormato, rXPath, rLongitud, rRequerido, rRegexp);
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
						if (datoFila.equals("")) {
							ce.setFilaFin(i);
							break;
						}
					} else {
						ce.setFilaFin(i);
						break;
					}
				}
			}

			Row rw = sh.getRow(ce.getFilaTitulo());

			for (int i = 0; i <= rw.getPhysicalNumberOfCells(); i++) {
				if (rw.getCell(i) != null) {// Regla de asignacion de valor
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

	public static Map<String,String> obtenerPickListCeldas(String rutaExcel, String libro, String pickList){
		int nFila=0;
		Map<String,String> pl = new HashMap<String, String>();
		try {
			FileInputStream fis = new FileInputStream(rutaExcel);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(libro);
			
			if (sh.getRow(nFila) != null) {
				for (int i = 0; i < sh.getRow(0).getPhysicalNumberOfCells(); i++) {
					if (sh.getRow(nFila).getCell(i) != null) {
						String tituloFila = sh.getRow(nFila).getCell(i).getStringCellValue();
						String primeraFila = sh.getRow(nFila+1).getCell(i).getStringCellValue();
						nFila+=1;
						if (tituloFila.equals("PICKLIST") && primeraFila.equals(pickList)) {
							while(sh.getRow(nFila).getCell(i)!=null) {
								pl.put(sh.getRow(nFila).getCell(i+2).getStringCellValue(), sh.getRow(nFila).getCell(i+3).getStringCellValue());
								nFila+=1;
							}
							break;
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pl;
	}

}
