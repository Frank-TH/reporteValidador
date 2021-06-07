package uriel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
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
	private static String logg = "";
	private static JSONObject jsonLogg;

	public static void main(String[] args) {
		
		
		String rutaExcel = "/rutas/Mapeo.xlsx";
		String rutaXML = "/rutas/datos/Cost Center/Full File Cost Center.xml";
		String libro = "Cost Center";

		CeldaExcel ce = GestionaExcel.obtenerCeldas(rutaExcel, libro);// Identificar las columnas y las filas a extraer
		List<Regla> reglas = GestionaExcel.obtenerDatos(ce);// Leer excel y obtener reglas
		jsonLogg = new JSONObject();
		
		for (Regla regla : reglas) {
			if (regla.getXPath() == "") {
				logg = logg + regla.getHris() + ",Sin Xpath" + "\n";
				jsonLogg.put(regla.getHris(),"Sin Xpath");
			} else {
				List<DatoXML> datosXML = GestionaXML.leer(rutaXML, regla);// leer xml y obtener datos
				validar(regla, datosXML);// validar los datos xml con la reglas
			}
		}
		//espcificar posición del dato en el xml, codigos de errores
		Salida.imprimir(logg,rutaSalida);
	}

	private static void validar(Regla regla, List<DatoXML> datos) {
		for (DatoXML dato : datos) {
			String resultado = regla.getHris() + ",<" + dato.getEtiqueta() + ">," + dato.getDato() + ",";
			resultado = resultado.concat(validarRegexp(regla, dato));
			resultado = resultado.concat(validarLongitud(regla, dato));

			logg = logg + resultado + "\n";
		}
	}

	private static void generarJson(String logg) {
		JSONObject myObject = new JSONObject();

		// Cadenas de texto básicas
		myObject.put("name", "Carlos");
		myObject.put("last_name", "Carlos");

		// Valores primitivos
		myObject.put("age", new Integer(21));
		myObject.put("bank_account_balance", new Double(20.2));
		myObject.put("is_developer", new Boolean(true));

		// Matrices
		double[] myList = { 1.9, 2.9, 3.4, 3.5 };
		myObject.put("number_list", myList);

		// Objeto dentro de objeto
		JSONObject subdata = new JSONObject();
		myObject.put("extra_data", subdata);

		// Generar cadena de texto JSON
		System.out.print(myObject);
	}

	private static String validarRegexp(Regla regla, DatoXML dato) {
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

	private static String validarLongitud(Regla regla, DatoXML dato) {
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
