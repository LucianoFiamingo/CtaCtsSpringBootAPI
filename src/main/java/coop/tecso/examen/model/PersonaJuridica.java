package coop.tecso.examen.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class PersonaJuridica  extends Titular {
	
	private static final long serialVersionUID = 799905220136654746L;

	private String isoCode;
	
	@NotNull(message = "Debe ingresar la razón social")
	@Size(min = 3, max = 100, message 
		      = "Debe ingresar entre 3 y 100 caracteres")
	private String razonSocial;
	 
	private int anioFundacion;
	
	public String getIsoCode() {
		return isoCode;
	}
	
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public int getAnioFundacion() {
		return anioFundacion;
	}

	public void setAnioFundacion(int anioFundacion) {
		this.anioFundacion = anioFundacion;
	}
	
}

