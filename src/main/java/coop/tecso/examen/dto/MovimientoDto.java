package coop.tecso.examen.dto;

import java.io.Serializable;
import java.util.Date;

import coop.tecso.examen.model.TipoMovimiento;

public class MovimientoDto implements Serializable {

	private static final long serialVersionUID = 2949046992490189350L;
	
	private Long id;
	private String isoCode;
	private Date fecha;
	private String descripcion;
	private Float importe;
	private TipoMovimiento tipoMovimiento;
	
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Float getImporte() {
		return importe;
	}

	public void setImporte(Float importe) {
		this.importe = importe;
	}

	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

}
