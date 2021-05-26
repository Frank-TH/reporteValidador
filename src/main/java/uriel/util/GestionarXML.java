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

public class GestionarXML {

	public static List<DatoXML> leer(String rutaXML, String xpath) {
		List<DatoXML> datos = new ArrayList<DatoXML>();
		String[] arrayXpath = xpath.split("/");
		String path = arrayXpath[arrayXpath.length - 2];
		String etiqueta = arrayXpath[arrayXpath.length - 1];

		System.out.println("Path: " + path);
		System.out.println("Etiqueta: " + etiqueta);
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
				System.out.println(dato);

				DatoXML xml = new DatoXML(path, etiqueta, dato);
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
}
