package uriel.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import uriel.Valida;

public class GestionaXML {

	public static Map<String,Object> leerValidar(String rutaXML, List<Regla> reglas, String nodoPadre) {
		Map<String,Object> errorValidacion = new HashMap<String,Object>();
		List<Object> listNodoError = new ArrayList<Object>();//lista de {errores} json

		try {
			File archivo = new File(rutaXML);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document document = documentBuilder.parse(archivo);
			document.getDocumentElement().normalize();
			NodeList listaEmpleados = document.getElementsByTagName(nodoPadre);
			
			for (int x = 0, size = listaEmpleados.getLength(); x < size; x++) {
				Node nodo = listaEmpleados.item(x);
				Element element = (Element) nodo;
				List<DatoXML> datos = new ArrayList<DatoXML>();
				
				Map<String,Object> errorNodo = new HashMap<String,Object>();//lista de {error} json
				List<Object> listError = new ArrayList<Object>();//lista de {errores} json
				

				for (Regla regla : reglas) {
					if(!regla.getXPath().equals("")) {
						String[] arrayXpath = regla.getXPath().split("/");
						String etiqueta = arrayXpath[arrayXpath.length - 1];
						String path = arrayXpath[arrayXpath.length - 2];
						
						if(element.getElementsByTagName(etiqueta).item(0)!=null) {
							String dato = element.getElementsByTagName(etiqueta).item(0).getTextContent();
							DatoXML xml = new DatoXML(path, etiqueta, dato,x);
							datos.add(xml);
							
							String validaRegexp = Valida.validarRegexp(regla, xml);
							String validaLongitud = Valida.validarLongitud(regla, xml);
							
							if(!validaRegexp.equals("") || !validaLongitud.equals("")) {
								String resultado ="nodo:"+xml.getNodo()+","+regla.getHris()+",<" + xml.getEtiqueta() + ">,DatoEncontrado:" + xml.getDato() + ",";
								resultado += validaRegexp;
								resultado += validaLongitud;
								Valida.logg += resultado + "\n";
							}
							
							if(!validaRegexp.equals("")) {
								Map<String,Object> error = new HashMap<String,Object>();
								error.put("campo", regla.getHris());
								error.put("tipoError", "Expresión Regular");
								error.put("descripcion", regla.getNombre());
								error.put("valorEncontrado", xml.getDato());
								error.put("valorEsperado", regla.getRegexp());
								listError.add(error);
							}
							
							if(!validaLongitud.equals("")) {
								Map<String,Object> error = new HashMap<String,Object>();
								error.put("campo", regla.getHris());
								error.put("tipoError", "Max Length");
								error.put("descripcion", regla.getNombre());
								error.put("valorEncontrado", xml.getDato());
								error.put("valorEsperado", regla.getLongitud());
								listError.add(error);
							}
						}
						
					}
				}
				errorNodo.put("Errores",listError);
				errorNodo.put("Nodo",x);
				listNodoError.add(errorNodo);
	
			}
			errorValidacion.put("Error Validacion", listNodoError);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return errorValidacion;
	}

}
