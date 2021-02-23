package coop.tecso.examen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Movimiento;

public interface MovimientoRepository  extends JpaRepository<Movimiento, Long> {

	List<Movimiento> findByCuentaCorriente(CuentaCorriente cuentaCorriente);

	List<Movimiento> findByCuentaCorrienteOrderByFecha(CuentaCorriente cuentaCorriente);

	boolean existsByCuentaCorriente(CuentaCorriente cc);

}