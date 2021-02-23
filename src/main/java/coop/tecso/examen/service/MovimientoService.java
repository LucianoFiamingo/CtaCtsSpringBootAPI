package coop.tecso.examen.service;

import java.util.List;

import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Movimiento;

public interface MovimientoService {
	
	List<Movimiento> findByCuentaCorriente(CuentaCorriente cuentaCorriente);

	List<Movimiento> findByCuentaCorrienteOrderByFecha(CuentaCorriente cuentaCorriente);
	
	List<Movimiento> OrdenarListaMovimientosPorFechaDesc(List<Movimiento> listMov);
	
	Integer getDescubiertoMaximoSegunMoneda(String moneda);

	Boolean validarDescubierto(Float saldo, Float monto, Integer descubiertoMaximo);
}
