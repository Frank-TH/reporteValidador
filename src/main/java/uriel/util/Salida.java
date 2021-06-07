package uriel.util;

import java.io.PrintWriter;

import org.json.JSONObject;

import model.DatoXML;

public class Salida {
	
	public static void imprimir(String logg, String rutaSalida) {
		imprimirConsola(logg);
		imprimirTxt(logg,rutaSalida);
		imprimirCsv(logg,rutaSalida);
		imprimirJson(logg,rutaSalida);
	}
	
	public static void imprimirCsv(String logg, String rutaSalida) {
		rutaSalida=rutaSalida.split("\\.")[0];
		try {
            PrintWriter writer = new PrintWriter(rutaSalida+".csv", "UTF-8");
            writer.println(logg);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void imprimirTxt(String logg, String rutaSalida) {
		rutaSalida=rutaSalida.split("\\.")[0];
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
	
	public static void imprimirJson(String logg, String rutaSalida) {
		String[] arrayLogg = logg.split(",");
		
		JSONObject myObject = new JSONObject();
		
		for (int i = 0; i < arrayLogg.length; i++) {
			myObject.put(i+"", arrayLogg[i]);
		}

		rutaSalida=rutaSalida.split("\\.")[0];
		try {
            PrintWriter writer = new PrintWriter(rutaSalida+".json", "UTF-8");
            writer.println(myObject);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		// Cadenas de texto básicas
		/*myObject.put("name", "Carlos");
		myObject.put("last_name", "Carlos");*/

		// Valores primitivos
		/*myObject.put("age", new Integer(21));
		myObject.put("bank_account_balance", new Double(20.2));
		myObject.put("is_developer", new Boolean(true));*/

		// Matrices
		/*double[] myList = { 1.9, 2.9, 3.4, 3.5 };
		myObject.put("number_list", myList);*/

		// Objeto dentro de objeto
		/*JSONObject subdata = new JSONObject();
		myObject.put("extra_data", subdata);*/

		// Generar cadena de texto JSON
	}
}
