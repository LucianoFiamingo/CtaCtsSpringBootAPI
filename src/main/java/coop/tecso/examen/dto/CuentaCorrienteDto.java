package coop.tecso.examen.dto;

import java.io.Serializable;

import coop.tecso.examen.model.Titular;

public class CuentaCorrienteDto implements Serializable {

	private static final long serialVersionUID = -3089605207154227750L;
	
	private Long id;
	private String isoCode;
	private Integer numero;
	private String moneda;
	private Float saldo;
	private TitularDto titular;
	
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
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public Float getSaldo() {
		return saldo;
	}
	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}
	public TitularDto getTitular() {
		return titular;
	}
	public void setTitular(TitularDto titular) {
		this.titular = titular;
	}

}
