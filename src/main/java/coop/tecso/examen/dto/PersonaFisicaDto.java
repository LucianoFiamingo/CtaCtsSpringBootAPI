package coop.tecso.examen.dto;

import java.io.Serializable;

public class PersonaFisicaDto extends TitularDto implements Serializable{

	private static final long serialVersionUID = 5740123240740905328L;
	
	private Long id;
	private String isoCode;
	private String nombre;
	private String apellido;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
