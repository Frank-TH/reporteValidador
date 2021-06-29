package uriel;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
	public static String logg = "";
	private static JSONObject jsonLogg;

	public static void main(String[] args) {
		try {
			Properties propiedades = new Properties();
			propiedades.load(new FileReader("config.properties"));
			String carpetaLogg = propiedades.getProperty("carpetaLogg");
			String urlExcel = propiedades.getProperty("urlExcel");
			String[] configuracion = propiedades.getProperty("configuracion").split(",");

			for (int i = 0; i < configuracion.length; i++) {
				validar(configuracion[i].split("\\|")[0], urlExcel, configuracion[i].split("\\|")[1],
						configuracion[i].split("\\|")[2], carpetaLogg + "/" + configuracion[i].split("\\|")[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void validar(String rutaXml, String urlExcel, String libro, String nodoPadre, String nombreSalida) {
		jsonLogg = new JSONObject();
		jsonLogg.put("Objeto", libro);
		jsonLogg.put("Nombre XML", rutaXml);
		jsonLogg.put("Equipo", "Templarios");
		jsonLogg.put("Fecha", new Date());

		CeldaExcel ce = GestionaExcel.obtenerCeldas(urlExcel, libro);// Identificar las columnas y las filas a extraer
		List<Regla> reglas = GestionaExcel.obtenerDatos(ce);// Leer excel y obtener reglas
		Map<String, Object> datosXml = GestionaXML.leerValidar(rutaXml, reglas, nodoPadre);// leer xml, obtener datos y armar ambos logg (json, txt)

		jsonLogg.put("informe", datosXml);
		Salida.imprimirJson(jsonLogg, nombreSalida);
		Salida.imprimir(logg, nombreSalida);
		logg = "";
	}

	public static String validarEstructura(Regla regla, String pathProcesado) {
		if (regla.getXPath() == null || regla.getXPath().equals("")) {
			return "[[[Estructura XML (Xpath)]]]";
		}

		if (!regla.getXPath().equals(pathProcesado)) {
			return "[Estructura XML]";
		} else {
			return "";
		}
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
		if (regla.getLongitud() == null || regla.getLongitud().equals("")) {
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

	public static String validarObligatorio(Regla regla, DatoXML dato) {
		if (regla.getRequerido() == null || regla.getRequerido().equals("")) {
			return "[[[Obligatorio]]]";
		}

		if ((dato.getDato()=="" || dato.getDato()==null) && regla.getRequerido().equals("SI") ) {
			return "[Obligatorio]";
		} else {
			return "";
		}
	}
	
	/*public static String validarListado(Regla regla, DatoXML dato) {
		if (regla.getFormato() == null || regla.getFormato().equals("")) {
			return "[[[Tipo Dato]]]";
		}
		
		Map<String,String> pl = GestionaExcel.obtenerPickListCeldas(logg, logg, logg)

		if (!pl.contains(dato.getDato())) {
			return "[Tipo Dato]";
		} else {
			return "";
		}
	}*/

}
