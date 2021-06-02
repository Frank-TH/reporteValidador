package uriel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CeldaExcel;
import model.DatoXML;
import model.Regla;
import uriel.util.GestionaExcel;
import uriel.util.GestionaXML;
import uriel.util.Salida;

public class Valida {
	public static void main(String[] args) {
		/*Regla regla = new Regla();
		regla.setNombre("Organization_ID");
		regla.setFormato("DD/MM/YYYY");
		regla.setXPath("orgc:Organizations" 
				+ "/orgc:Organization" 
				+ "/orgc:Additional_Information"
				+ "/orgc:Location_Effective_Date");
		regla.setLongitud(10);
		regla.setRequerido(true);
		regla.setRegexp("\\d{4}\\-\\d{2}\\-\\d{2}");*/
		
		//System.out.println("-----Datos Mapeado");
		String rutaExcel="/rutas/Mapeo.xlsx";
		String rutaXML="/rutas/datos/Cost Center/Full File Cost Center.xml";
		String libro = "Cost Center";
		
		CeldaExcel ce = GestionaExcel.obtenerCeldas(rutaExcel, libro);
		
		List<Regla> reglas = GestionaExcel.obtenerDatos(ce);

		for (Regla regla : reglas) {
			List<DatoXML> datosXML = GestionaXML.leer(rutaXML, regla);
			validar(regla, datosXML);
		}
	}

	private static void validar(Regla regla, List<DatoXML> datos) {
		Pattern pat = Pattern.compile(regla.getRegexp());
		System.out.println("-----Datos Validado con "+regla.getRegexp());
		for (DatoXML dato : datos) {
			Matcher mat = pat.matcher(dato.getDato());
			if (!mat.matches())
				Salida.imprimirConsola(dato);
		}
	}
}
