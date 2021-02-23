package coop.tecso.examen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Titular;

public interface CuentaCorrienteRepository  extends JpaRepository<CuentaCorriente, Long> {

	List<CuentaCorriente> findByTitular(Titular t);

	boolean existsByNumero(Integer numero);

	CuentaCorriente findByNumero(Integer integer);
}
