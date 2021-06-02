package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regla {
	private String interfaz;
	private String hris;
	private String nombre;
	private String formato;
	private String xPath;
	private String longitud;
	private String requerido;
	private String regexp;//expresión regular
	
}
