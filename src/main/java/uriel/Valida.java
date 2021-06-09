package uriel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import model.CeldaExcel;
import model.DatoXML;
import model.Regla;
import uriel.util.GestionaExcel;
import uriel.util.GestionaXML;
import uriel.util.Salida;

public class Valida {
	private static String rutaSalida = "/rutas/logg.txt";
	public static String logg = "";
	private static JSONObject jsonLogg;

	public static void main(String[] args) {
		String rutaExcel = "/rutas/datos/Mapeo.xlsx";
		String rutaXML = "/rutas/datos/xml/Full File Cost Center.xml";
		String libro = "Cost Center";
		String nodoPadre = "orgc:Organization";
		
		jsonLogg = new JSONObject();
		jsonLogg.put("Objeto", libro);
		//jsonLogg.put("Nombre XML", rutaXML.split("/")[rutaXML.split("/").length-1]);
		jsonLogg.put("Nombre XML", rutaXML);
		jsonLogg.put("Equipo", "Templarios");
		jsonLogg.put("Fecha", new Date());
		
		CeldaExcel ce = GestionaExcel.obtenerCeldas(rutaExcel, libro);// Identificar las columnas y las filas a extraer
		List<Regla> reglas = GestionaExcel.obtenerDatos(ce);// Leer excel y obtener reglas
		Map<String,Object> datosXML = GestionaXML.leerValidar(rutaXML, reglas, nodoPadre);// leer xml, obtener datos y armar ambos logg (json, txt)

		jsonLogg.put("informe", datosXML);
		
		Salida.imprimirJson(jsonLogg, rutaSalida);
		Salida.imprimir(logg,rutaSalida);
	}

	public static String validarRegexp(Regla regla, DatoXML dato) {
		if (regla.getRegexp() == null || regla.getRegexp().equals("")) {
			return "[[[Expresión Regular]]]";
		}

		Pattern pat = Pattern.compile(regla.getRegexp());
		Matcher mat = pat.matcher(dato.getDato());
		if (!mat.matches()) {
			return "[Expresión Regular]";
		} else {
			return "";
		}
	}

	public static String validarLongitud(Regla regla, DatoXML dato) {
		if (regla.getLongitud() == null || regla.getLongitud().equals("0.0")) {
			return "[[[Max Length]]]";
		}

		double longitudMax = Double.parseDouble(regla.getLongitud());
		int longitud = dato.getDato().length();
		if (longitud > longitudMax) {
			return "[Max Length]";
		} else {
			return "";
		}
	}

	/*
	 * private static String validarRequerido(Regla regla, DatoXML dato) {
	 * if(regla.getRequerido() == null || regla.getRequerido().equals("")) { return
	 * "[[[Requerido]]]"; }
	 * 
	 * if (dato.getDato()) { return "[Requerido]"; } else { return ""; } }
	 */
}
