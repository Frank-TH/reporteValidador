package uriel.util;

import model.DatoXML;

public class Salida {
	public static void imprimirConsola(DatoXML datoXML) {
		System.out.println(datoXML.getPath() +"/"+ datoXML.getEtiqueta() +" = "+ datoXML.getDato()+ " NoVálido");
	}
}
