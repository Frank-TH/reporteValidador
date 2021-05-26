package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Regla {
	private String nombre;
	private String formato;
	private String xPath;
	private Integer longitud;
	private Boolean requerido;
	private String regexp;//expresión regular
	
}
