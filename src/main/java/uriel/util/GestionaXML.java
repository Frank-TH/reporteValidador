package uriel.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.DatoXML;
import model.Regla;

public class GestionaXML {

	public static List<DatoXML> leer(String rutaXML, Regla regla) {
		List<DatoXML> datos = new ArrayList<DatoXML>();
		String[] arrayXpath = invetirArray(regla.getXPath().split("/"));
		
		String etiqueta = arrayXpath[0];
		String path = arrayXpath[1];

		//System.out.println("Path: "+path);
		//System.out.println("Etiqueta: "+etiqueta);
		try {
			File archivo = new File(rutaXML);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document document = documentBuilder.parse(archivo);
			document.getDocumentElement().normalize();
			NodeList listaEmpleados = document.getElementsByTagName(path);
			
			for (int x = 0, size = listaEmpleados.getLength(); x < size; x++) {
				Node nodo = listaEmpleados.item(x);
				Element element = (Element) nodo;
				String dato = element.getElementsByTagName(etiqueta).item(0).getTextContent();
				//System.out.println(dato);
				DatoXML xml = new DatoXML(path,etiqueta,dato);
				
				datos.add(xml);
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return datos;
	}
	
	private static String[] invetirArray(String[] numeros) {
        String aux;
        for (int i=0; i<numeros.length/2; i++) {
            aux = numeros[i];
            numeros[i] = numeros[numeros.length-1-i];
            numeros[numeros.length-1-i] = aux;
        }
        return numeros;
	}
	
}
