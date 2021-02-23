package coop.tecso.examen.dto;

import java.io.Serializable;
import java.util.List;

public class TitularDto implements Serializable {

	private static final long serialVersionUID = 6350617030656397588L;
	
	private Long id;
	private String isoCode;
	private String rut;
	private List<CuentaCorrienteDto> cuentasCorrientes;
	
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

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public List<CuentaCorrienteDto> getCuentasCorrientes() {
		return cuentasCorrientes;
	}

	public void setCuentasCorrientes(List<CuentaCorrienteDto> cuentasCorrientes) {
		this.cuentasCorrientes = cuentasCorrientes;
	}

}
