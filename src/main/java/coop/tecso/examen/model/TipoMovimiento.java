package coop.tecso.examen.model;

import javax.persistence.Entity;

@Entity
public class TipoMovimiento extends AbstractPersistentObject {

	private static final long serialVersionUID = 4935401535631500043L;
	
	private String isoCode;
	private String nombre;
	
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
}
