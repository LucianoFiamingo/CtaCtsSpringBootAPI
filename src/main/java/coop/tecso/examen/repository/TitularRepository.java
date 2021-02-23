package coop.tecso.examen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import coop.tecso.examen.model.Titular;

public interface TitularRepository  extends JpaRepository<Titular, Long> {

	Boolean existsByRut(String rut);

    @Query(value = "SELECT distinct t from Titular as t inner join CuentaCorriente as c on t = c.titular")
	List<Titular> findAllWithAccounts();

	List<Titular> findAllByCuentasCorrientesNotNull();
}
