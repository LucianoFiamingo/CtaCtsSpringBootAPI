package coop.tecso.examen.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class PersonaFisica extends Titular {

	private static final long serialVersionUID = 4077372657574922096L;
	
	private String isoCode;
	
	@NotNull(message = "Debe ingresar su nombre")
	@Size(min = 3, max = 80, message 
		      = "El nombre debe ser entre 3 y 80 caracteres")
	private String nombre;
	
	@NotNull(message = "Debe ingresar su apellido")
	@Size(min = 3, max = 250, message 
	 = "El apellido debe ser entre 3 y 80 caracteres")
	private String apellido;
	
	public String getIsoCode() {
		return isoCode;
	}
	
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

}

