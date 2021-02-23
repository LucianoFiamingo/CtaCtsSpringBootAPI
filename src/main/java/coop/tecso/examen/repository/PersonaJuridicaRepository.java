package coop.tecso.examen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import coop.tecso.examen.model.PersonaJuridica;

public interface PersonaJuridicaRepository  extends JpaRepository<PersonaJuridica, Long> {

}
