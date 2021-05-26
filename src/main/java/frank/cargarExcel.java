package frank;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class cargarExcel {

	private XSSFWorkbook libro;
	public cargarExcel(File filename) {

		List<Object> cellData = new ArrayList<>();

		try {

			FileInputStream entrada = new FileInputStream(filename);

			libro = new XSSFWorkbook(entrada);

			// Inicializamos la variable i para saber en que fila vamos a comenzar (0 - más)
			XSSFSheet hoja = libro.getSheetAt(0);
			

			Iterator<Row> filaIterator = hoja.rowIterator();

			while (filaIterator.hasNext()) {
				XSSFRow fila = (XSSFRow) filaIterator.next();
				Iterator<Cell> celdaIterator = fila.cellIterator();
				List<Object> celdaTemp = new ArrayList<>();
				while (celdaIterator.hasNext()) {
					XSSFCell celda = (XSSFCell) celdaIterator.next();
					celdaTemp.add(celda);
				}
				cellData.add(celdaTemp);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		obtener(cellData);
	}

	public void obtener(List<Object> celdaDataList) {

		// Inicializamos la variable i para saber en que fila vamos a comenzar (0 - más)
		for (int i = 1; i < celdaDataList.size(); i++) {
			@SuppressWarnings("unchecked")
			List<XSSFCell> celdaTempList = (List<XSSFCell>) celdaDataList.get(i);
			for (int j = 0; j < celdaTempList.size(); j++) {
				XSSFCell celda = (XSSFCell) celdaTempList.get(j);
				String stringCellValue = celda.toString();
				System.out.print(stringCellValue.concat("\t"));
			}
			System.out.println();
		}

	}

	public static void main(String[] args) {

		File f = new File("C:\\Team\\proyecto\\excel\\PaisesMonedasIdiomas.xlsx");
		if (f.exists()) {
			new cargarExcel(f);
		} else {
			System.out.println("No existe");
		}

	}
}
