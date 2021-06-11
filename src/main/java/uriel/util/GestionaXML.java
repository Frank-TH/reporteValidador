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
	private static List<String> pathObtenido = new ArrayList<String>();

	public static Map<String,Object> leerValidar(String rutaXML, List<Regla> reglas, String nodoPadre) {
		Map<String,Object> errorValidacion = new HashMap<String,Object>();
		List<Object> listNodoError = new ArrayList<Object>();//lista de {errores} json

		try {
			File archivo = new File(rutaXML);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document document = documentBuilder.parse(archivo);
			document.getDocumentElement().normalize();
			String nodoAbuelo = document.getDocumentElement().getTagName();
			NodeList listaEmpleados = document.getElementsByTagName(nodoPadre);
			
			for (int x = 0, size = listaEmpleados.getLength(); x < size; x++) {
				Valida.logg+="\n\n";
				Node nodo = listaEmpleados.item(x);
				Element element = (Element) nodo;
				List<DatoXML> datos = new ArrayList<DatoXML>();
				
				Map<String,Object> errorNodo = new HashMap<String,Object>();//lista de {error} json
				List<Object> listError = new ArrayList<Object>();//lista de {errores} json
				

				for (Regla regla : reglas) {
					if(!regla.getXPath().equals("")) {
						String[] arrayXpath = regla.getXPath().split("/");
						String etiqueta = arrayXpath[arrayXpath.length - 1];
						///orgc:Organizations/orgc:Organization/adiciona/orgc:Inactive
						//orgc:Inactive
						
						String path = arrayXpath[arrayXpath.length - 2];
						
						if(element.getElementsByTagName(etiqueta).item(0)!=null) {//posibiidad de exisitir mas de una etiqueta con el mismo nombre
							buscarPadre(element,nodoPadre,etiqueta);
							pathObtenido.add(nodoAbuelo);;
							
							String pathProcesado="";
							for (int i = pathObtenido.size()-1; i >= 0; i--) {
								pathProcesado+="/"+pathObtenido.get(i);
							}
							pathObtenido.clear();
							
							String dato = element.getElementsByTagName(etiqueta).item(0).getTextContent();
							DatoXML xml = new DatoXML(path, etiqueta, dato,x);
							datos.add(xml);
							
							String validaRegexp = Valida.validarRegexp(regla, xml);
							String validaLongitud = Valida.validarLongitud(regla, xml);
							String validaEstructura = Valida.validarEstructura(regla, pathProcesado);
							
							if(!validaRegexp.equals("") || !validaLongitud.equals("") || !validaEstructura.equals("")) {
								String resultado ="nodo:"+xml.getNodo()+","+regla.getHris()+",<" + xml.getEtiqueta() + ">,DatoEncontrado:" + xml.getDato() + ",";
								resultado += validaRegexp;
								resultado += validaLongitud;
								resultado += validaEstructura;
								Valida.logg += resultado + "\n";
							}
							//System.out.println(regla.getXPath() + " --- " + pathProcesado);
							if(!validaEstructura.equals("")) {
								Map<String,Object> error = new HashMap<String,Object>();
								error.put("campo", regla.getHris());
								error.put("tipoError", "Estructura Xml");
								error.put("descripcion", regla.getNombre());
								error.put("valorEncontrado", pathProcesado);
								error.put("valorEsperado", regla.getXPath());
								listError.add(error);
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
						} else {
							if(regla.getRequerido().equals("SI")) {
								String resultado ="nodo:"+x+","+regla.getHris()+",<" + etiqueta + ">,Etiqueta no existe y es obligatorio\n";
								//Valida.logg += "Etiqueta no existe -" + etiqueta+"\n";
								Valida.logg += resultado;
								
								Map<String,Object> error = new HashMap<String,Object>();
								error.put("campo", regla.getHris());
								error.put("tipoError", "Estructura Xml");
								error.put("descripcion", regla.getNombre());
								error.put("valorEncontrado", "No encontrado y es requerido");
								error.put("valorEsperado", regla.getXPath());
								listError.add(error);
							}
							
						}
						
					}
				}
				//System.out.println(datos);
				
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
	
	public static void buscarPadre(Element element,String nodoPadre,String etiqueta){
		String padre = element.getElementsByTagName(etiqueta).item(0).getParentNode().getNodeName();
		pathObtenido.add(etiqueta);
		if(!padre.endsWith(nodoPadre)) {
			buscarPadre(element,nodoPadre,padre);
		}else {
			pathObtenido.add(padre);
		}
	}

}
