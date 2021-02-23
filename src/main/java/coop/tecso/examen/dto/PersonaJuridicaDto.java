package coop.tecso.examen.dto;

import java.io.Serializable;

public class PersonaJuridicaDto extends TitularDto implements Serializable{

	private static final long serialVersionUID = 5740123240740905328L;
	
	private Long id;
	private String isoCode;
	private String razonSocial;
	private int anioFundacion;

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
