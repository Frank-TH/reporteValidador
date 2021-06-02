package model;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CeldaExcel {
	
	private String ruta;
	private String libro;
	private Sheet sheet;
	private int filaTitulo;
	private int colHris;
	private int colNombre;
	private int colFormato;
	private int colXPath;
	private int colRequerido;
	private int colLongitud;
	private int colRegexp;
}
