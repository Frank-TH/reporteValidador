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
			String nodoAbuelo = document.getDocumentElement().getTagName();
			NodeList listaEmpleadosTotal = document.getElementsByTagName(nodoPadre);
			//NodeList listaEmpleadosTotal = document.getChildNodes();
			List<Node> listaEmpleados = new ArrayList<Node>();
			//List<Object> listaEmpleados2 = new ArrayList<Object>();

			for (int x = 0, size = listaEmpleadosTotal.getLength(); x < size; x++) {
				if(listaEmpleadosTotal.item(x).getParentNode().getNodeName().equals(nodoAbuelo)) {
					Node node = listaEmpleadosTotal.item(x);
					listaEmpleados.add(node);
				}
			}
			//listaEmpleados = list.getChildNodes();
			
			for (int x = 0, size = listaEmpleados.size(); x < size; x++) {
				Valida.logg+="\n";
				Node nodo = listaEmpleados.get(x);
				Element element = (Element) nodo;
				List<DatoXML> datos = new ArrayList<DatoXML>();
				
				Map<String,Object> errorNodo = new HashMap<String,Object>();//lista de {error} json
				List<Object> listError = new ArrayList<Object>();//lista de {errores} json
				

				for (Regla regla : reglas) {
					if(!regla.getXPath().equals("")) {
						String[] arrayXpath = regla.getXPath().split("/");
						String etiqueta = arrayXpath[arrayXpath.length - 1];
						
						String path = arrayXpath[arrayXpath.length - 2];
						
						if(element.getElementsByTagName(etiqueta).item(0)!=null) {//posibiidad de exisitir mas de una etiqueta con el mismo nombre
							String pathProcesado = obtenerArbolNodos(element, nodoAbuelo, nodoPadre, etiqueta);
							
							String dato = element.getElementsByTagName(etiqueta).item(0).getTextContent();
							DatoXML xml = new DatoXML(path, etiqueta, dato,x);
							datos.add(xml);
							
							String validaRegexp = Valida.validarRegexp(regla, xml);
							String validaLongitud = Valida.validarLongitud(regla, xml);
							String validaEstructura = Valida.validarEstructura(regla, pathProcesado);
							String validaObligatorio = Valida.validarObligatorio(regla, xml);
							
							//System.out.println(etiqueta);
							
							if(!validaRegexp.equals("") || !validaLongitud.equals("") || !validaEstructura.equals("")) {
								String resultado ="nodo:"+xml.getNodo()+","+regla.getNombre().replace("\n", "")+",<" + xml.getEtiqueta().replace("\n", "") + ">,DatoEncontrado:" + xml.getDato() + ",";
								resultado += validaRegexp;
								resultado += validaLongitud;
								resultado += validaEstructura;
								resultado += validaObligatorio;
								Valida.logg += resultado + "\n";
							}
							
							if(!validaEstructura.equals("")) {
								listError.add(agregarErrorJson(regla, "Estructura XML", pathProcesado, regla.getXPath()));
							}
							
							if(!validaRegexp.equals("")) {
								listError.add(agregarErrorJson(regla, "Expresión Regular", xml.getDato(), regla.getRegexp()));
							}
							
							if(!validaLongitud.equals("")) {
								listError.add(agregarErrorJson(regla, "Longitud", xml.getDato(), regla.getLongitud()));
							}
							
							if(!validaObligatorio.equals("")) {
								listError.add(agregarErrorJson(regla, "Obligatorio", xml.getDato(), regla.getRequerido()));
							}
						} else {
							if(regla.getRequerido().equals("SI")) {
								String resultado ="nodo:"+x+","+regla.getNombre()+",<" + etiqueta + ">,Etiqueta no existe y es obligatorio\n";
								Valida.logg += resultado;
								
								listError.add(agregarErrorJson(regla, "Estructura Xml", "Etiqueta no existe y es obligatorio", regla.getXPath()));
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
	
	public static String obtenerArbolNodos(Element element,String nodoAbuelo, String nodoPadre,String etiqueta){
		List<String> arbolNodo = new ArrayList<String>();
		String pathProcesado="";
		
		while (!etiqueta.endsWith(nodoPadre)) {
			arbolNodo.add(etiqueta);
			etiqueta = element.getElementsByTagName(etiqueta).item(0).getParentNode().getNodeName();
		}
		arbolNodo.add(nodoPadre);
		arbolNodo.add(nodoAbuelo);
		
		for (int i = arbolNodo.size()-1; i >= 0; i--) {
			pathProcesado+="/"+arbolNodo.get(i);
		}
		
		return pathProcesado;
	}
	
	public static Map<String,Object> agregarErrorJson(Regla regla, String tipoError, String valorEncontrado, String valorEsperado){
		Map<String,Object> error = new HashMap<String,Object>();
		error.put("campo", regla.getNombre().replace("\n", ""));
		error.put("tipoError", tipoError);
		error.put("descripcion", regla.getHris().replace("\n", ""));
		error.put("valorEncontrado", valorEncontrado);
		error.put("valorEsperado", valorEsperado);
		
		return error;
	}

}
