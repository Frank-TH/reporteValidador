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
		regla.setNombre("Effective Date");
		regla.setFormato("DD/MM/YYYY");
		regla.setXPath("empresa/empleado/username");
		regla.setLongitud(3);
		regla.setRequerido(true);
		regla.setRegexp("\\d{2}\\/\\d{2}\\/\\d{4}");
		
		List<DatoXML> datosXML = GestionarXML.leer("/rutas/datos/test.xml", regla.getXPath());
		
		validar(regla,datosXML);
	}
	
	private static void validar(Regla regla, List<DatoXML> dato) {
		Pattern pat = Pattern.compile(regla.getRegexp());
		
		for (DatoXML datoXML : dato) {
			Matcher mat = pat.matcher(datoXML.getDato());
		     if (mat.matches()) {
		         System.out.println("Válido");                                                                            
		     } else {
		         System.out.println("No Válido");                                                                         
		     }
		}
		
	     
	}
}
