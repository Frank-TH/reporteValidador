package uriel.util;

import java.io.PrintWriter;
import org.json.JSONObject;


public class Salida {
	
	public static void imprimir(String logg, String rutaSalida) {
		imprimirConsola(logg);
		imprimirTxt(logg,rutaSalida);
		//imprimirCsv(logg,rutaSalida);
	}
	
	public static void imprimirCsv(String logg, String rutaSalida) {
		//rutaSalida=rutaSalida.split("\\.")[0];
		try {
            PrintWriter writer = new PrintWriter(rutaSalida+".csv", "UTF-8");
            writer.println(logg);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void imprimirTxt(String logg, String rutaSalida) {
		//rutaSalida=rutaSalida.split("\\.")[0];
		try {
            PrintWriter writer = new PrintWriter(rutaSalida+".txt", "UTF-8");
            writer.println(logg);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void imprimirConsola(String logg) {
		System.out.println(logg);
	}
	
	public static void imprimirJson(JSONObject myObject, String rutaSalida) {
		//rutaSalida=rutaSalida.split("\\.")[0];
		try {
            PrintWriter writer = new PrintWriter(rutaSalida+".json", "UTF-8");
            writer.println(myObject);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
