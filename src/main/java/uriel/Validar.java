package uriel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.DatoXML;
import model.Regla;
import uriel.util.GestionarXML;

public class Validar {
	public static void main(String[] args) {
		Regla regla = new Regla();
		regla.setNombre("Organization_ID");
		regla.setFormato("DD/MM/YYYY");
		regla.setXPath(
				"orgc:Organizations"
				+ "/orgc:Organization"
				+ "/orgc:Additional_Information"
				+ "/orgc:Location_Effective_Date");
		regla.setLongitud(3);
		regla.setRequerido(true);
		regla.setRegexp("\\d{4}\\/\\d{2}\\/\\d{2}");
		
		// regla.setRegexp("\\d{2}\\/\\d{2}\\/\\d{4}");

		Regla regla2 = new Regla();
		regla2.setNombre("Effective Date 2");
		regla2.setFormato("DD/MM/YYYY");
		regla2.setXPath("orgc:Organizations/orgc:Organization/orgc:Additional_Information/orgc:ID_Matrícula_del_Manager");
		regla2.setLongitud(3);
		regla2.setRequerido(true);
		regla2.setRegexp("\\d{4}\\/\\d{2}\\/\\d{2}");

		List<DatoXML> datosXML = GestionarXML.leer("/rutas/datos/Creación Organización.xml", regla);

		validar(regla, datosXML);
	}

	private static void validar(Regla regla, List<DatoXML> dato) {
		Pattern pat = Pattern.compile(regla.getRegexp());

		for (DatoXML datoXML : dato) {
			Matcher mat = pat.matcher(datoXML.getDato());
			if (!mat.matches())	System.out.println(regla.getXPath() + " " + datoXML.getDato() + " NoVálido");

		}
	}
}
