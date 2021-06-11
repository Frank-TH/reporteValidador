package uriel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.Position;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.SystemOutLogger;
import org.apache.xmlbeans.impl.common.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test2 {
	public static Workbook wb;
	public static Sheet sh;
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static Row row;
	public static Cell cell;

	public static void main(String[] args) throws Exception {
	
		test("C:/Mapeo.xlsx", "UO","C:/Creación_UO.xml");	
		
	}

	public static List<String> test(String urlExcel, String hojaExcel, String urlXml) {
		List<String> Xpath = new ArrayList<String>();//contiene las filas de la columna D del archivo excel
		List<String> campoEC = new ArrayList<String>();
		List<String> Atributo = new ArrayList<String>();
		List<String> Obligatorio = new ArrayList<String>();//contiene las filas de la columna E del archivo excel
		List<List<String>> datos = new ArrayList<List<String>>();//contiene todo los datos del archivo excel
		List<List<String>> XpathTotal = new ArrayList<List<String>>();
		List<String> salida = new ArrayList<String>();

		try {
			FileInputStream fis = new FileInputStream(urlExcel);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(hojaExcel);
			int noOfRows = sh.getLastRowNum();// Retorna el numero de filas - 1 del excel

			for (int i = 0; i <= 11; i++) {// Se genera los List por grupos de columnas
				datos.add(new ArrayList<String>());
			}

			for (int k = 0; k <= 11; k++) { // Se carga los List con informacion del Excel
				for (int j = 0; j <= noOfRows; j++) {
					if( datos.get(k).add(sh.getRow(j).getCell(k).toString()) != true) {
						datos.get(k).add(sh.getRow(j).getCell(k).toString());
					}
				}
			}
			
			for (int i = 1; i <= noOfRows; i++) {//Se carga la columna D y E del excel en el List = Xpath y Obligatorio
				if(datos.get(2).get(i) != "") {
					Xpath.add(datos.get(2).get(i));
					String string = datos.get(2).get(i);
					String[] parts = string.split("/");
					Atributo.add(parts[parts.length -1]); 
					campoEC.add(datos.get(1).get(i));
				}

				if(datos.get(10).get(i) != "") {
					Obligatorio.add(datos.get(10).get(i));
				}
			}
			
			File archivo = new File(urlXml);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document document = documentBuilder.parse(archivo);
			document.getDocumentElement().normalize();
			
			//Listas con los nodos para el fichero xml Position
			NodeList listaPosition = null;
			NodeList listaPosition_Data = null;
			
			//Listas con los nodos para el fichero xml Organization
			NodeList listaUO = null;
			NodeList listaUOAdditional_Information = null;
			
			//*****************************SE IMPLEMENTA LA LECTURA DEL XML POSITION Y SU VALIDACION******************************
			if(hojaExcel.equals("Position")){
				listaPosition = document.getElementsByTagName("ps:Position");
				listaPosition_Data = document.getElementsByTagName("ps:Position_Data");
						
				for (int temp = 0; temp < listaPosition.getLength(); temp++) {
					XpathTotal.add(new ArrayList<String>());
				}
				
				for (int temp = 0; temp < listaPosition.getLength(); temp++) {
					Node nodo1 = listaPosition.item(temp);
					Element element1 = (Element) nodo1;
					
					Node nodo2 = listaPosition_Data.item(temp);
					Element element2 = (Element) nodo2;
					
					for(int indice=0; indice < Xpath.size();indice++) {//Se LEE todos los fullpath de todos los nodos POSITION
						if(element1.getElementsByTagName(Atributo.get(indice)).item(0)!=null) {
							XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element2.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(indice)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(indice)).item(0).getNodeName());
						} else {
							XpathTotal.get(temp).add("");
						}
					}
				}
				
				//VALIDA XPATH DEL EXCEL CONTRA EL FULLPATH DEL XML GENERADO
				int contador = 0;
				
				for (int i = 0; i < Xpath.size();i++) {//son las 15 filas del excel tanto de la columa D y E
					for (int k = 0;k < listaPosition.getLength();k++) {//Recorre tantos nodos POSITION exista en el XML
						contador = 0;
						for (int j= 0;j < XpathTotal.get(0).size();j++) {//15 Xpathfull generadas al leer el XML
							if(Obligatorio.get(i).equals("SI")) {
								if (Xpath.get(i).equals(XpathTotal.get(k).get(j))) {
									break;
								}else{	
									contador++;
								}
							}
						}
						if(contador == XpathTotal.get(0).size()){
							System.out.println("Position "+ (k+1) + ": Para el campo "+campoEC.get(i)+" no se encuentra el FullPath obligatorio: " + Xpath.get(i));
							salida.add("Position "+ (k+1) + ": Para el campo "+campoEC.get(i)+" no se encuentra el FullPath obligatorio: " + Xpath.get(i));
						}			
					}
				}
			}
			
			//*************************SE IMPLEMENTA LA LECTURA DEL XML SUPERVISORY Y SU VALIDACION*****************************
			if(hojaExcel.equals("UO")){
				listaUO = document.getElementsByTagName("orgc:Organization");
				listaUOAdditional_Information = document.getElementsByTagName("orgc:Additional_Information");
										
				for (int temp = 0; temp < listaUO.getLength(); temp++) {
					XpathTotal.add(new ArrayList<String>());
				}
				
				for (int temp = 0; temp < listaUO.getLength(); temp++) {
					Node nodo1 = listaUO.item(temp);
					Element element1 = (Element) nodo1;
					
					Node nodo2 = listaUOAdditional_Information.item(temp);
					Element element2 = (Element) nodo2;
					
					if(element1.getElementsByTagName(Atributo.get(0)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(0)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(0)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(1)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element2.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(1)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(1)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(2)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(2)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(2)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(3)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(3)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(3)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(4)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element2.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(4)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(4)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(5)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(5)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(5)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(6)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element2.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(6)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(6)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
					if(element1.getElementsByTagName(Atributo.get(7)).item(0)!=null) {
						XpathTotal.get(temp).add("/"+element1.getParentNode().getNodeName()+"/"+element2.getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(7)).item(0).getParentNode().getNodeName()+"/"+element1.getElementsByTagName(Atributo.get(7)).item(0).getNodeName());
					} else {
						XpathTotal.get(temp).add("");
					}
				}
				
				//VALIDA XPATH DEL EXCEL CONTRA EL FULLPATH DEL XML GENERADO
				int contador = 0;
				
				for (int i = 0; i < Xpath.size();i++) {//son las 8 filas del excel tanto de la columa D y E
					for (int k = 0;k < listaUO.getLength();k++) {//Recorre tantos nodos ORGANIZATION exista en el XML
						contador = 0;
						for (int j= 0;j < XpathTotal.get(0).size();j++) {//8 Xpathfull generadas al leer el XML
							if(Obligatorio.get(i).equals("SI")) {
								if (Xpath.get(i).equals(XpathTotal.get(k).get(j))) {
									break;
								}else{	
									contador++;
								}
							}
						}
						if(contador == XpathTotal.get(0).size()){
							System.out.println("Organization "+ (k+1) + ": Para el campo "+campoEC.get(i)+" no se encuentra el FullPath obligatorio: " + Xpath.get(i));
							salida.add("Organization "+ (k+1) + ": Para el campo "+campoEC.get(i)+" no se encuentra el FullPath obligatorio: " + Xpath.get(i));
						}			
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return salida;
	}

}
