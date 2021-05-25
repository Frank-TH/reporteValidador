package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Regla {
	private String nombre;
	private String formato;
	private String xPath;
	private Integer longitud;
	private Boolean requerido;
	private String regexp;//expresión regular
}
