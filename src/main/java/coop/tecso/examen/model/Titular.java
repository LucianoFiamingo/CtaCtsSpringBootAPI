package coop.tecso.examen.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Titular  extends AbstractPersistentObject {

	private static final long serialVersionUID = -5398377449173497008L;
	
	private String isoCode;
	private String rut;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "titular")
	private List<CuentaCorriente> cuentasCorrientes;
	
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

	public List<CuentaCorriente> getCuentasCorrientes() {
		return cuentasCorrientes;
	}

	public void setCuentasCorrientes(List<CuentaCorriente> cuentasCorrientes) {
		this.cuentasCorrientes = cuentasCorrientes;
	}

}

